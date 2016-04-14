package com.consolefire.orm.helper;


import com.consolefire.orm.GenericDao;
import com.consolefire.orm.entity.AuditProperties;
import com.consolefire.orm.entity.Auditable;
import com.consolefire.orm.util.ObjectUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;


public abstract class GenericJpaDao<E, I extends Serializable> implements GenericDao<E, I> {

    private static Logger logger = LoggerFactory.getLogger(GenericJpaDao.class);

    private String identityFieldName;
    private Class<E> entityType;

    @Getter
    @PersistenceContext
    protected EntityManager entityManager;

    public GenericJpaDao() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        entityType = (Class<E>) parameterizedType.getActualTypeArguments()[0];
        List<Field> fields = ObjectUtil.getFields(entityType, true);
        if (null != fields && fields.size() > 0) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)
                        || field.isAnnotationPresent(EmbeddedId.class)) {
                    this.identityFieldName = field.getName();
                    break;
                }
            }
        }
    }

    @Override
    public Class<E> getEntityType() {
        return this.entityType;
    }

    @Override
    public String getIdentityFieldName() {
        return this.identityFieldName;
    }

    @SuppressWarnings("unchecked")
    protected I getIdValue(E entity) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (null != entity) {
            Method idMethod =
                    entityType.getMethod("get" + StringUtils.capitalize(identityFieldName), null);
            return (I) idMethod.invoke(entity, null);
        }
        return null;
    }

    protected void setIdValue(E entity, I id) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (null != entity) {
            Method idMethod =
                    entityType.getMethod("set" + StringUtils.capitalize(identityFieldName),
                            id.getClass());
            idMethod.invoke(entity, id);
        }
    }

    @Override
    public E find(I id) {
        return (E) getEntityManager().find(getEntityType(), id);
    }

    @Override
    public Collection<E> find(Map<String, Object> parameters) {
        if (null == parameters || parameters.size() <= 0)
            return null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        Root<E> entityRoot = criteriaQuery.from(getEntityType());
        List<String> paramList = new ArrayList<>(parameters.keySet());
        Predicate[] predicates = new Predicate[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            String param = paramList.get(i);
            Object value = parameters.get(param);
            if (null != value) {
                if (value instanceof Collection) {
                    Predicate predicate = entityRoot.get(param).in(value);
                    predicates[i] = predicate;
                } else {
                    Predicate predicate =
                            criteriaBuilder.equal(entityRoot.get(param), parameters.get(param));
                    predicates[i] = predicate;
                }
            }

        }
        criteriaQuery = criteriaQuery.where(predicates);
        TypedQuery<E> typedQuery = getEntityManager().createQuery(criteriaQuery);
        List<E> entityList = typedQuery.getResultList();
        return entityList;
    }

    @Override
    public Collection<E> find(Properties properties) {
        if (null != properties) {
            Map<String, Object> parameters = new HashMap<>();
            for (Object key : properties.keySet()) {
                parameters.put(key.toString(), properties.get(key));
            }
            return find(parameters);
        }
        return null;
    }

    @Override
    public Collection<E> findAll() {
        return getEntityManager().createQuery("SELECT e FROM " + getEntityType().getName() + " e")
                .getResultList();
    }

    @Override
    public Collection<E> findAll(List<I> list) {
        if (null == list || list.size() <= 0)
            return null;
        Query query =
                getEntityManager().createQuery(
                        "SELECT e FROM  " + getEntityType().getName() + " e WHERE e."
                                + getIdentityFieldName() + " IN (:idList)");
        query.setParameter("idList", list);
        return query.getResultList();
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long count(Map<String, Object> properties) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public E save(E entity) {
        logger.info("Saving entity");
        prepareAuditProperties(entity, false);
        getEntityManager().persist(entity);
        getEntityManager().flush();
        return entity;
    }

    @Override
    public E update(E entity) {
        prepareAuditProperties(entity, true);
        return getEntityManager().merge(entity);
    }



    @Override
    public E saveOrUpdate(E entity) {
        if (!StringUtils.hasText(getIdentityFieldName())) {
            return getEntityManager().merge(entity);
        }
        try {
            if (null == getIdValue(entity)) {
                return save(entity);
            } else {
                return update(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int saveOrUpdate(List<E> entities) {
        return 0;
    }

    @Override
    public void delete(I id) {

    }

    @Override
    public void delete(E entity) {

    }


    protected void prepareAuditProperties(final E entity, boolean update) {
        if (null == entity) {
            return;
        }
        Annotation annotation = ObjectUtil.getAnnotation(getEntityType(), Auditable.class, true);
        if (null != annotation) {
            AuditProperties auditProperties = null;
            Auditable auditable = (Auditable) annotation;
            if (null != auditable) {
                Method getterMethod =
                        ObjectUtil.getGetterMethod(getEntityType(), auditable.value());
                try {
                    auditProperties = (AuditProperties) getterMethod.invoke(entity, null);
                } catch (Exception e) {
                    auditProperties = new AuditProperties();
                }
            }
            if (null == auditProperties) {
                auditProperties = new AuditProperties();
            }
            if (!update) {
                auditProperties.setCreatedAt(new Date());
                auditProperties.setCreatedBy("APP_USER");
            } else {
                auditProperties.setUpdatedBy("APP_USER");
                auditProperties.setUpdatedAt(new Date());
            }

            try {
                Method setterMethod =
                        getEntityType().getMethod(ObjectUtil.createSetterMethod(auditable.value()),
                                new Class[] {AuditProperties.class});
                if (null == setterMethod) {
                    logger.error("Could not set Audit Properties as not set method");
                }
                setterMethod.invoke(entity, new Object[] {auditProperties});
            } catch (Exception e) {
                logger.error("Could not set Audit Properties");
                throw new RuntimeException("Could not set Audit Properties");
            }
        }
    }
}

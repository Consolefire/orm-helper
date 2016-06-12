package com.consolefire.orm.helper;


import com.consolefire.orm.GenericDao;
import com.consolefire.orm.timed.TimeTrack;
import com.consolefire.orm.util.ObjectUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.Getter;


public abstract class GenericJpaDao<E, I extends Serializable> implements GenericDao<E, I> {

    private static Logger logger = LoggerFactory.getLogger(GenericJpaDao.class);

    protected String identityFieldName;
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
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
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
    protected I getIdValue(E entity) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (null != entity) {
            Method idMethod = entityType.getMethod("get" + StringUtils.capitalize(identityFieldName), null);
            return (I) idMethod.invoke(entity, null);
        }
        return null;
    }

    protected void setIdValue(E entity, I id) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (null != entity) {
            Method idMethod = entityType.getMethod("set" + StringUtils.capitalize(identityFieldName), id.getClass());
            idMethod.invoke(entity, id);
        }
    }

    @Override
    @TimeTrack
    public E find(I id) {
        return (E) getEntityManager().find(getEntityType(), id);
    }

    @Override
    @TimeTrack
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
                    Predicate predicate = criteriaBuilder.equal(entityRoot.get(param), parameters.get(param));
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
    @TimeTrack
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
    @TimeTrack
    public Collection<E> findAll() {
        return getEntityManager().createQuery("SELECT e FROM " + getEntityType().getName() + " e").getResultList();
    }

    @Override
    @TimeTrack
    public Collection<E> findAll(List<I> list) {
        if (null == list || list.size() <= 0)
            return null;
        Query query = getEntityManager().createQuery("SELECT e FROM  " + getEntityType().getName() + " e WHERE e."
                + getIdentityFieldName() + " IN (:idList)");
        query.setParameter("idList", list);
        return query.getResultList();
    }

    @Override
    @TimeTrack
    public Collection<E> findByNamedQuery(String namedQuery, Map<String, Object> parameters) {
        TypedQuery<E> typedQuery = getEntityManager().createNamedQuery(namedQuery, getEntityType());
        if (null != parameters && parameters.size() > 0) {
            for (String key : parameters.keySet()) {
                typedQuery.setParameter(key, parameters.get(key));
            }
        }
        return typedQuery.getResultList();
    }

    @Override
    @TimeTrack
    public E findUniqueByNamedQuery(String namedQuery, Map<String, Object> parameters) {
        TypedQuery<E> typedQuery = getEntityManager().createNamedQuery(namedQuery, getEntityType());
        if (null != parameters && parameters.size() > 0) {
            for (String key : parameters.keySet()) {
                typedQuery.setParameter(key, parameters.get(key));
            }
        }
        return typedQuery.getSingleResult();
    }

    @Override
    @TimeTrack
    public long count() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(getEntityType())));
        return getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    @TimeTrack
    public long count(Map<String, Object> parameters) {
        if (null == parameters || parameters.size() <= 0)
            return 0;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<E> entityRoot = criteriaQuery.from(getEntityType());
        criteriaQuery.select(criteriaBuilder.count(entityRoot));
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
                    Predicate predicate = criteriaBuilder.equal(entityRoot.get(param), parameters.get(param));
                    predicates[i] = predicate;
                }
            }
        }
        criteriaQuery = criteriaQuery.where(predicates);
        return getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    @TimeTrack
    public E save(E entity) {
        logger.info("Saving entity");
        getEntityManager().persist(entity);
        getEntityManager().flush();
        return entity;
    }

    @Override
    @TimeTrack
    public E update(E entity) {
        return getEntityManager().merge(entity);
    }



    @Override
    @TimeTrack
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
    @TimeTrack
    public int saveOrUpdate(List<E> entities) {
        int count = 0;
        if (null != entities && entities.size() > 0) {
            for (E e : entities) {
                saveOrUpdate(e);
                count++;
            }
        }
        return count;
    }

    @Override
    @TimeTrack
    public void delete(I id) {
        E entity = getEntityManager().find(getEntityType(), id);
        if (null != entity) {
            delete(entity);
        }
    }

    @Override
    @TimeTrack
    public void delete(E entity) {
        if (null != entity) {
            entityManager.remove(entity);
        }
    }

}

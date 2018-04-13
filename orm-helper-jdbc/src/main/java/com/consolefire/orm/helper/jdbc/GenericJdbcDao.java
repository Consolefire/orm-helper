package com.consolefire.orm.helper.jdbc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.consolefire.orm.common.GenericDao;
import com.consolefire.orm.helper.jdbc.context.ColumnMetaDataContext;
import com.consolefire.orm.helper.jdbc.context.DatabaseMetaDataContext;
import com.consolefire.orm.helper.jdbc.context.TableMetaDataContext;
import com.consolefire.orm.spring.support.ObjectUtil;

import org.springframework.util.StringUtils;

public abstract class GenericJdbcDao<E, I extends Serializable> implements GenericDao<E, I> {

    protected String identityFieldName;
    private Class<E> entityType;

    private final DatabaseMetaDataContext databaseMetaDataContext;

    public GenericJdbcDao(final DatabaseMetaDataContext databaseMetaDataContext) {
        if(null == databaseMetaDataContext) {
            throw new IllegalArgumentException("Cannot find Database Metadata mapping...");
        }
        this.databaseMetaDataContext = databaseMetaDataContext;
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        entityType = (Class<E>) parameterizedType.getActualTypeArguments()[0];
        processIdentityField();
    }

    protected void processIdentityField() {
        final TableMetaDataContext tableMetaDataContext = this.databaseMetaDataContext.get(entityType);
        if (null == tableMetaDataContext) {
            throw new IllegalArgumentException("Cannot find entity mapping...");
        }
        List<Field> fields = ObjectUtil.getFields(entityType, true);
        if (null != fields && fields.size() > 0) {
            for (Field field : fields) {
                ColumnMetaDataContext columnMetaDataContext = tableMetaDataContext.get(field.getName());
                if (columnMetaDataContext.isPrimary()) {
                    identityFieldName = field.getName();
                    break;
                }
            }
        }
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
    public Class<E> getEntityType() {
        return this.entityType;
    }

    @Override
    public String getIdentityFieldName() {
        return this.identityFieldName;
    }

    @Override
    public E find(I id) {
        
        return null;
    }

    @Override
    public Collection<E> find(Map<String, Object> parameters) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<E> find(Properties properties) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<E> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<E> findAll(List<I> list) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<E> findByNamedQuery(String namedQuery, Map<String, Object> parameters) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E findUniqueByNamedQuery(String namedQuery, Map<String, Object> parameters) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E update(E entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E saveOrUpdate(E entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int saveOrUpdate(List<E> entities) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(I id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(E entity) {
        // TODO Auto-generated method stub

    }


}

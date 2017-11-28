package com.consolefire.orm.mongo.helper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.consolefire.orm.mongo.WrappedDocumentRepository;

public class GenericDocumentWrapperDao<E, I extends Serializable>
        implements WrappedDocumentRepository<E, DocumentWrapper<E>, I> {

    private final Class<E> wrappedType; 
    protected String identityFieldName;
    
    
    public GenericDocumentWrapperDao() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        wrappedType = (Class<E>) parameterizedType.getActualTypeArguments()[0];
    }
    
    @Override
    public Class<E> getEntityType() {
        return wrappedType;
    }

    @Override
    public String getIdentityFieldName() {
        return null;
    }

    @Override
    public E find(I id) {
        // TODO Auto-generated method stub
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
        if(null != entity) {
            
        }
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

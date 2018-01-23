package com.consolefire.orm.mongo.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.bson.Document;

import com.consolefire.orm.mongo.WrappableDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class DocumentWrapper<T> implements WrappableDocument<T> {

    private static volatile boolean wrapped;
    private Document wrappedDocument;
    
    private final T entity;
    private final Class<T> targetEntity;
    

    @SuppressWarnings("unchecked")
    public DocumentWrapper(T entity) {
        if (null == entity) {
            throw new IllegalArgumentException("Entity must not be NULL");
        }
        this.entity = entity;
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        targetEntity = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        if (null == targetEntity) {
            throw new UnsupportedOperationException("Entity type could not be determined...");
        }
    }

    public T unwrap() {
        return entity;
    }

    @Override
    public Document wrap() {
        if (!wrapped) {
            synchronized (this) {
                if (!wrapped) {
                    try {
                        wrappedDocument = doWrap();
                    } catch (JsonProcessingException jsonParseException) {
                        throw new UnsupportedOperationException(jsonParseException);
                    }
                    wrapped = true;
                }
            }
        }
        return wrappedDocument;
    }

    private Document doWrap() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return Document.parse(mapper.writeValueAsString(entity));
    }



}

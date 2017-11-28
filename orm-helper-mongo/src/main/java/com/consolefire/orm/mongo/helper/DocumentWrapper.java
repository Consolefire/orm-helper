package com.consolefire.orm.mongo.helper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.bson.Document;

import com.consolefire.orm.mongo.WrappableDocument;

public class DocumentWrapper<T> implements WrappableDocument<T> {

    private static final long serialVersionUID = 8407284276845560386L;
    private static volatile boolean wrapped;
    private final T entity;
    private final Class<T> targetEntity;
    private Document wrappedDocument;

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
        return null;
    }

    @Override
    public Document wrap() {
        if (!wrapped) {
            synchronized (this) {
                if (!wrapped) {
                    wrappedDocument = doWrap();
                    wrapped = true;
                }
            }
        }
        return wrappedDocument;
    }

    private Document doWrap() {
        Document document = new Document();
        
        return document;
    }



}

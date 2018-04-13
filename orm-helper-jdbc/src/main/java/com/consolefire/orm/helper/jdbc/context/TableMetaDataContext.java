package com.consolefire.orm.helper.jdbc.context;

import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public class TableMetaDataContext extends ConcurrentHashMap<String, ColumnMetaDataContext> {

    private static final long serialVersionUID = -4378282908982535303L;
    private final String tableName;
    private final Class<?> entityClass;


    public TableMetaDataContext(String tableName, Class<?> entityClass) {
        this.tableName = tableName;
        this.entityClass = entityClass;
    }



}

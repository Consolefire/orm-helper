package com.consolefire.orm.helper.jdbc.context;

import java.sql.JDBCType;

public class TypeContext {

    private final Class<?> propertyType;
    private final JDBCType jdbcType;

    public TypeContext(Class<?> propertyType, JDBCType jdbcType) {
        this.propertyType = propertyType;
        this.jdbcType = jdbcType;
    }



}

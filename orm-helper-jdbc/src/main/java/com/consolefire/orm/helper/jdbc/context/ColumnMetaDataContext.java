package com.consolefire.orm.helper.jdbc.context;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnMetaDataContext {

    private final String columnName;
    private final String propertyName;
    private final TypeContext typeContext;
    private boolean primary;
    private int maxLength;
    private boolean nullable;
    private Object defaultValue;

    public ColumnMetaDataContext(String columnName, String propertyName, TypeContext typeContext) {
        this.columnName = columnName;
        this.propertyName = propertyName;
        this.typeContext = typeContext;
    }

}

package com.consolefire.orm.helper.jdbc.context;

import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public class DatabaseMetaDataContext extends ConcurrentHashMap<Class<?>, TableMetaDataContext>{

    private static final long serialVersionUID = 5774950822291137762L;
    private final String namespace;

    public DatabaseMetaDataContext(String namespace) {
        this.namespace = namespace;
    }


}

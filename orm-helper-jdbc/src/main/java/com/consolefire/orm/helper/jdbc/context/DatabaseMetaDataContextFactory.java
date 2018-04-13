package com.consolefire.orm.helper.jdbc.context;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;

public class DatabaseMetaDataContextFactory {

    private final Resource resource;
    private final Map<String, DatabaseMetaDataContext> databaseMetaDataContextMapping;

    public DatabaseMetaDataContextFactory(Resource resource) {
        if (null == resource || !resource.exists()) {
            throw new IllegalArgumentException("Resource does not exist ...");
        }
        if (!resource.isReadable()) {
            throw new IllegalArgumentException("Resource is not readable ...");
        }
        this.resource = resource;
        this.databaseMetaDataContextMapping = new HashMap<>();
        initContext();
    }

    private void initContext() {

    }



}

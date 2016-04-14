package com.consolefire.orm.helper;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author sabuj.das
 *
 */
public class MasterSlaveRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceTypeContext.getDataSourceType();
    }

}

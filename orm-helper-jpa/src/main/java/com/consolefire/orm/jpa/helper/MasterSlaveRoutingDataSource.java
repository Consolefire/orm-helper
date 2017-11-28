package com.consolefire.orm.jpa.helper;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.consolefire.orm.common.DataSourceTypeContext;

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

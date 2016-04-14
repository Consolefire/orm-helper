package com.consolefire.orm.helper;

public class DataSourceTypeContext {
    public enum DataSourceType {
        MASTER, SLAVE
    }

    private static final ThreadLocal<DataSourceType> THREAD_LOCAL_CONTEXT =
            new ThreadLocal<DataSourceType>();

    public static void setDataSourceType(DataSourceType databaseType) {
        THREAD_LOCAL_CONTEXT.set(databaseType);
    }

    public static DataSourceType getDataSourceType() {
        return THREAD_LOCAL_CONTEXT.get();
    }

    public static void clearDataSourceType() {
        THREAD_LOCAL_CONTEXT.remove();
    }
}

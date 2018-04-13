package com.consolefire.orm.helper.jdbc.context;

import java.sql.Types;

import lombok.Getter;

@Getter
public enum JdbcType {

    BIT("BIT", Types.BIT),
    TINYINT("TINYINT", Types.TINYINT),
    SMALLINT("SMALLINT", Types.SMALLINT),
    INTEGER("INTEGER", Types.INTEGER),
    BIGINT("BIGINT", Types.BIGINT),
    FLOAT("FLOAT", Types.FLOAT),
    REAL("REAL", Types.REAL),
    DOUBLE("DOUBLE", Types.DOUBLE),
    NUMERIC("NUMERIC", Types.NUMERIC),
    DECIMAL("DECIMAL", Types.DECIMAL),
    CHAR("CHAR", Types.CHAR),
    VARCHAR("VARCHAR", Types.VARCHAR),
    LONGVARCHAR("LONGVARCHAR", Types.LONGVARCHAR),
    DATE("DATE", Types.DATE),
    TIME("TIME", Types.TIME),
    TIMESTAMP("TIMESTAMP", Types.TIMESTAMP),
    BINARY("BINARY", Types.BINARY),
    VARBINARY("VARBINARY", Types.VARBINARY),
    LONGVARBINARY("LONGVARBINARY", Types.LONGVARBINARY),
    NULL("NULL", Types.NULL),
    OTHER("OTHER", Types.OTHER),
    JAVA_OBJECT("JAVA_OBJECT", Types.JAVA_OBJECT),
    DISTINCT("DISTINCT", Types.DISTINCT),
    STRUCT("STRUCT", Types.STRUCT),
    ARRAY("ARRAY", Types.ARRAY),
    BLOB("BLOB", Types.BLOB),
    CLOB("CLOB", Types.CLOB),
    REF("REF", Types.REF),
    DATALINK("DATALINK", Types.DATALINK),
    BOOLEAN("BOOLEAN", Types.BOOLEAN),
    ROWID("ROWID", Types.ROWID),
    NCHAR("NCHAR", Types.NCHAR),
    NVARCHAR("NVARCHAR", Types.NVARCHAR),
    LONGNVARCHAR("LONGNVARCHAR", Types.LONGNVARCHAR),
    NCLOB("NCLOB", Types.NCLOB),
    SQLXML("SQLXML", Types.SQLXML),
    REF_CURSOR("REF_CURSOR", Types.REF_CURSOR),
    TIME_WITH_TIMEZONE("TIME_WITH_TIMEZONE", Types.TIME_WITH_TIMEZONE),
    TIMESTAMP_WITH_TIMEZONE("TIMESTAMP_WITH_TIMEZONE", Types.TIMESTAMP_WITH_TIMEZONE);
    
    private final String name;
    private final int sqlType;

    private JdbcType(String name, int sqlType) {
        this.name = name;
        this.sqlType = sqlType;
    }

}

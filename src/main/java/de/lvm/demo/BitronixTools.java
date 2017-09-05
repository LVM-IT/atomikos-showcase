package de.lvm.demo;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import java.util.Properties;

public class BitronixTools {

    public static PoolingDataSource buildBitronixPGDataSourceBeanWithoutTestQuery() {
        PoolingDataSource poolingDataSource = new PoolingDataSource();
        poolingDataSource.setClassName("org.postgresql.xa.PGXADataSource");
        poolingDataSource.setUniqueName("postgres");
        poolingDataSource.setMinPoolSize(0);
        poolingDataSource.setMaxPoolSize(5);
        poolingDataSource.setAllowLocalTransactions(true);
        Properties properties = new Properties();
        properties.setProperty("user", "test");
        properties.setProperty("password", "test");
        properties.setProperty("databaseName", "test");
        properties.setProperty("serverName", "localhost");
        properties.setProperty("portNumber", "5499");
        properties.setProperty("driverType", "4");
        poolingDataSource.setDriverProperties(properties);
        return poolingDataSource;
    }

    public static PoolingDataSource buildBitronixPGDataSourceBeanWithTestQuery() {

        PoolingDataSource poolingDataSource = buildBitronixPGDataSourceBeanWithoutTestQuery();
        poolingDataSource.setTestQuery("select 1");

        return poolingDataSource;

    }

    public static PoolingDataSource buildBitronixDB2DataSourceBeanWithoutTestQuery() {
        PoolingDataSource poolingDataSource = new PoolingDataSource();
        poolingDataSource.setClassName("com.ibm.db2.jcc.DB2XADataSource");
        poolingDataSource.setUniqueName("db2");
        poolingDataSource.setMinPoolSize(0);
        poolingDataSource.setMaxPoolSize(5);
        poolingDataSource.setAllowLocalTransactions(true);
        Properties properties = new Properties();
        properties.setProperty("user", "db2inst1");
        properties.setProperty("password", "db2inst1-pwd");
        properties.setProperty("databaseName", "db2inst1");
        properties.setProperty("serverName", "localhost");
        properties.setProperty("portNumber", "50000");
        properties.setProperty("driverType", "4");
        poolingDataSource.setDriverProperties(properties);
        return poolingDataSource;
    }

    public static PoolingDataSource buildBitronixDB2DataSourceBeanWithTestQuery() {

        PoolingDataSource poolingDataSource = buildBitronixDB2DataSourceBeanWithoutTestQuery();
        poolingDataSource.setTestQuery("select 1 from sysibm.sysdummy1");

        return poolingDataSource;

    }


    /**
     * Note: Works only in integration test environment
     */
    public static PoolingDataSource buildBitronixDB2HostDataSourceBeanWithoutTestQuery() {
        PoolingDataSource poolingDataSource = new PoolingDataSource();
        poolingDataSource.setClassName("com.ibm.db2.jcc.DB2XADataSource");
        poolingDataSource.setUniqueName("db2");
        poolingDataSource.setMinPoolSize(0);
        poolingDataSource.setMaxPoolSize(5);
        poolingDataSource.setAllowLocalTransactions(true);
        Properties properties = new Properties();
        properties.setProperty("user", "tdvorg");
        properties.setProperty("password", "tdvorg");
        properties.setProperty("databaseName", "DB2T");
        properties.setProperty("serverName", "db2t.lvm.de");
        properties.setProperty("portNumber", "21020");
        properties.setProperty("driverType", "4");
        poolingDataSource.setDriverProperties(properties);
        return poolingDataSource;
    }

    public static PoolingDataSource buildBitronixDB2HostDataSourceBeanWithTestQuery() {

        PoolingDataSource poolingDataSource = buildBitronixDB2HostDataSourceBeanWithoutTestQuery();
        poolingDataSource.setTestQuery("select 1 from sysibm.sysdummy1");

        return poolingDataSource;

    }

}

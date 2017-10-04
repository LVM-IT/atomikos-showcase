package de.lvm.demo;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.ibm.db2.jcc.DB2XADataSource;
import org.postgresql.xa.PGXADataSource;

import java.util.UUID;

public class AtomikosTools
{

    
    public static AtomikosDataSourceBean buildAtomikosPGDataSourceBeanWithoutTestQuery() {
        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setServerName("localhost");
        pgxaDataSource.setDatabaseName("test");
        pgxaDataSource.setPortNumber(5499);
        pgxaDataSource.setUser("test");
        pgxaDataSource.setPassword("test");
        dsBean.setXaDataSource(pgxaDataSource);

        dsBean.setMinPoolSize(0);
        dsBean.setMaxPoolSize(5);
        dsBean.setUniqueResourceName(UUID.randomUUID().toString());
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);
        dsBean.setSupportsTmJoin(false);
        return dsBean;
        
    }

    public static AtomikosDataSourceBean buildAtomikosPGDataSourceBeanWithTestQuery() {

        AtomikosDataSourceBean dsBean = buildAtomikosPGDataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1");

        return dsBean;

    }

    public static AtomikosDataSourceBean buildAtomikosDB2DataSourceBeanWithoutTestQuery() {
        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
        DB2XADataSource db2XADataSource = new DB2XADataSource();
        db2XADataSource.setServerName("localhost");
        db2XADataSource.setDatabaseName("db2inst1");
        db2XADataSource.setPortNumber(50000);
        db2XADataSource.setUser("db2inst1");
        db2XADataSource.setPassword("db2inst1-pwd");
        db2XADataSource.setDriverType(4);
        db2XADataSource.setDeferPrepares(false);
        dsBean.setXaDataSource(db2XADataSource);

        dsBean.setMinPoolSize(0);
        dsBean.setMaxPoolSize(5);
        dsBean.setUniqueResourceName(UUID.randomUUID().toString());
        dsBean.setBorrowConnectionTimeout(20);
        dsBean.setReapTimeout(0);
        dsBean.setMaintenanceInterval(60);
        dsBean.setMaxLifetime(60);
        dsBean.setSupportsTmJoin(false);
        return dsBean;

    }

    public static AtomikosDataSourceBean buildAtomikosDB2DataSourceBeanWithTestQuery() {

        AtomikosDataSourceBean dsBean = buildAtomikosDB2DataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1 from sysibm.sysdummy1");

        return dsBean;

    }

    public static AtomikosDataSourceBean buildAtomikosDB2HostDataSourceBeanWithoutTestQuery() {
        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
        DB2XADataSource db2XADataSource = new DB2XADataSource();
        db2XADataSource.setServerName("db2t.lvm.de");
        db2XADataSource.setDatabaseName("DB2T");
        db2XADataSource.setPortNumber(21020);
        db2XADataSource.setUser("tdvorg");
        db2XADataSource.setPassword("tdvorg");
        db2XADataSource.setDriverType(4);
        dsBean.setXaDataSource(db2XADataSource);

        dsBean.setMinPoolSize(0);
        dsBean.setMaxPoolSize(5);
        dsBean.setUniqueResourceName(UUID.randomUUID().toString());
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);
        dsBean.setSupportsTmJoin(false);

        return dsBean;

    }

    public static AtomikosDataSourceBean buildAtomikosDB2HostDataSourceBeanWithTestQuery() {

        AtomikosDataSourceBean dsBean = buildAtomikosDB2DataSourceBeanWithTestQuery();
        dsBean.setTestQuery("select 1 from sysibm.sysdummy1");
        return dsBean;

    }

}

package de.lvm.demo;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import javax.sql.XADataSource;

import com.ibm.db2.jcc.DB2XADataSource;
import org.postgresql.xa.PGXADataSource;

public class AtomikosTools
{

    protected static XADataSource buildAtomikosPGDataSource()
    {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setServerName("localhost");
        pgxaDataSource.setDatabaseName("test");
        pgxaDataSource.setPortNumber(5499);
        pgxaDataSource.setUser("test");
        pgxaDataSource.setPassword("test");

        return pgxaDataSource;
        
    }

    protected static XADataSource buildAtomikosDB2DataSource()
    {
        DB2XADataSource db2XADataSource = new DB2XADataSource();
        db2XADataSource.setServerName("localhost");
        db2XADataSource.setDatabaseName("db2inst1");
        db2XADataSource.setPortNumber(50000);
        db2XADataSource.setUser("db2inst1");
        db2XADataSource.setPassword("db2inst1-pwd");
        db2XADataSource.setDriverType(4);
        return db2XADataSource;

    }
    
    public static AtomikosDataSourceBean buildAtomikosPGDataSourceBeanWithoutTestQuery() {
        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
        dsBean.setXaDataSource(buildAtomikosPGDataSource());

        dsBean.setMinPoolSize(0);
        dsBean.setMaxPoolSize(5);
        dsBean.setUniqueResourceName("postgres");
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);

        return dsBean;
        
    }

    public static AtomikosDataSourceBean buildAtomikosDB2DataSourceBeanWithoutTestQuery() {
        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
        dsBean.setXaDataSource(buildAtomikosDB2DataSource());

        dsBean.setMinPoolSize(0);
        dsBean.setMaxPoolSize(5);
        dsBean.setUniqueResourceName("db2");
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);

        return dsBean;

    }


    public static AtomikosDataSourceBean buildAtomikosPGDataSourceBeanWithTestQuery() {
        
        AtomikosDataSourceBean dsBean = buildAtomikosPGDataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1");
        
        return dsBean;
        
    }


    public static AtomikosDataSourceBean buildAtomikosDB2DataSourceBeanWithTestQuery() {

        AtomikosDataSourceBean dsBean = buildAtomikosDB2DataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1 from sysibm.sysdummy1");

        return dsBean;

    }

}

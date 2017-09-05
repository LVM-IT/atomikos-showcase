package de.lvm.demo;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.ibm.db2.jcc.DB2XADataSource;
import org.postgresql.xa.PGXADataSource;

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
        dsBean.setUniqueResourceName("postgres");
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);

        return dsBean;
        
    }

    public static AtomikosDataSourceBean buildAtomikosPGDataSourceBeanWithTestQuery() {

        AtomikosDataSourceBean dsBean = buildAtomikosPGDataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1");

        return dsBean;

    }

    /**
     * Works with Docker / locally
     */
    public static AtomikosDataSourceBean buildAtomikosDB2DataSourceBeanWithoutTestQuery() {
        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
        DB2XADataSource db2XADataSource = new DB2XADataSource();
        db2XADataSource.setServerName("localhost");
        db2XADataSource.setDatabaseName("db2inst1");
        db2XADataSource.setPortNumber(50000);
        db2XADataSource.setUser("db2inst1");
        db2XADataSource.setPassword("db2inst1-pwd");
        db2XADataSource.setDriverType(4);
        dsBean.setXaDataSource(db2XADataSource);

        dsBean.setMinPoolSize(0);
        dsBean.setMaxPoolSize(5);
        dsBean.setUniqueResourceName("db2");
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);

        return dsBean;

    }

    public static AtomikosDataSourceBean buildAtomikosDB2DataSourceBeanWithTestQuery() {

        AtomikosDataSourceBean dsBean = buildAtomikosDB2DataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1 from sysibm.sysdummy1");

        return dsBean;

    }

    /**
     * Note: Works only in integration test environment!
     */
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
        dsBean.setUniqueResourceName("db2");
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);

        return dsBean;

    }

    /**
     * Note: Works only in integration test environment!
     */
    public static AtomikosDataSourceBean buildAtomikosDB2HostDataSourceBeanWithTestQuery() {

        AtomikosDataSourceBean dsBean = buildAtomikosDB2DataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1 from sysibm.sysdummy1");

        return dsBean;

    }

}

package de.lvm.demo;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import javax.sql.XADataSource;
import org.postgresql.xa.PGXADataSource;

public class AtomikosTools
{

    protected static XADataSource buildAtomikosDataSource()
    {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setServerName("localhost");
        pgxaDataSource.setDatabaseName("test");
        pgxaDataSource.setPortNumber(5432);
        pgxaDataSource.setUser("test");
        pgxaDataSource.setPassword("test");

        return pgxaDataSource;
        
    }
    
    public static AtomikosDataSourceBean buildAtomikosDataSourceBeanWithoutTestQuery() {
        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
        dsBean.setXaDataSource(buildAtomikosDataSource());

        dsBean.setMinPoolSize(0);
        dsBean.setMaxPoolSize(5);
        dsBean.setUniqueResourceName("postgres");
        dsBean.setBorrowConnectionTimeout(60);
        dsBean.setReapTimeout(0);

        return dsBean;
        
    }
    public static AtomikosDataSourceBean buildAtomikosDataSourceBeanWithTestQuery() {
        
        AtomikosDataSourceBean dsBean = buildAtomikosDataSourceBeanWithoutTestQuery();
        dsBean.setTestQuery("select 1");
        
        return dsBean;
        
    }

}

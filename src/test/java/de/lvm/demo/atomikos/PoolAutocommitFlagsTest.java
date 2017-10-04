package de.lvm.demo.atomikos;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

import de.lvm.demo.AtomikosTools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Connections taken from the pool keep auto-commit setting as returned to pool
 */
public class PoolAutocommitFlagsTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource ds;

    @Test
    public void show() throws Exception {

        UserTransactionManager utm = new UserTransactionManager();
        utm.init();
        utm.begin();
        
        //check code
        logger.info("open Connection ...");
        Connection conn = open(true);
        conn.setAutoCommit(false);

        logger.info("conn {}", conn);

        logger.info("Auto-commit is xxx '{}'", conn.getAutoCommit());
        logger.info("commit on connection ...");
        conn.commit();
        logger.info("close Connection ...");
        conn.close();

        //business code

        logger.info("open Connection ...");
        conn = open(false);
        logger.info("Auto-commit is '{}'", conn.getAutoCommit());

        //conn.setAutoCommit(true);

        logger.info("conn {}", conn);
        try (PreparedStatement ps = conn.prepareStatement("select 1 from sysibm.sysdummy1")) {
            ps.execute();
        }

        logger.info("Auto-commit is '{}'", conn.getAutoCommit());

        conn.close();
        utm.commit();
        utm.close();

    }

    protected Connection open(boolean tq) throws SQLException {
        if (ds == null) {
            //transacted without test query
            if (tq)
                ds = AtomikosTools.buildAtomikosDB2DataSourceBeanWithTestQuery();
            else
                ds = AtomikosTools.buildAtomikosDB2DataSourceBeanWithoutTestQuery();

            ((AtomikosDataSourceBean) ds).setMaxPoolSize(1);
        }

        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException {
        conn.close();
    }
}

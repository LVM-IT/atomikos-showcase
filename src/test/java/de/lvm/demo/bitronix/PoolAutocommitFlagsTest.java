package de.lvm.demo.bitronix;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import de.lvm.demo.AtomikosTools;
import de.lvm.demo.BitronixTools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Connections taken from the pool keep auto-commit setting as returned to pool
 */
public class PoolAutocommitFlagsTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource ds;

    @Test
    public void show() throws Exception {

        BitronixTransactionManager btm = new BitronixTransactionManager();

        btm.begin();
        
        //check code
        logger.info("open Connection ...");
        Connection conn = open(false);
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
        btm.commit();
        btm.shutdown();

    }

    protected Connection open(boolean tq) throws SQLException {
        if (ds == null) {
            //transacted without test query
            if (tq)
                ds = BitronixTools.buildBitronixDB2DataSourceBeanWithTestQuery();
            else
                ds = BitronixTools.buildBitronixDB2DataSourceBeanWithoutTestQuery();

            ((PoolingDataSource) ds).setMaxPoolSize(1);
        }

        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException {
        conn.close();
    }
}

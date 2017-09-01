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
import javax.transaction.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Atomikos sets isolation level on each connection.open(), even when identical
 * This leads to problems with Postgresql.
 */
public class IsolationLevelTest
{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource ds;

    @Test
    public void show() throws Exception
    {


        BitronixTransactionManager btm = new BitronixTransactionManager();

        //works
        doIt(btm);

        //breaks when setting isolation level (again) 
        assertThat(doIt(btm), is(true));

    }

    protected boolean doIt(BitronixTransactionManager btm) throws RollbackException, HeuristicMixedException, SQLException, SecurityException, NotSupportedException, HeuristicRollbackException, SystemException, IllegalStateException
    {

        String uuid = UUID.randomUUID().toString();

        btm.begin();

        logger.info("open Connection ...");
        Connection conn = open();

        logger.info("conn {}", conn);
        try (PreparedStatement ps = conn.prepareStatement("insert into TEST.daten (id, name, count) VALUES (?,?,?)"))
        {
            ps.setString(1, uuid);
            ps.setString(2, "example");
            ps.setInt(3, 1234);

            logger.info("writing with id: {}", uuid);
            ps.execute();
        }
        logger.info("close Connection ...");
        conn.close();

        btm.commit();
        //btm.shutdown();

        return true;
    }

    protected Connection open() throws SQLException
    {

        if (ds == null)
        {
            //transacted without test query
//            ds = AtomikosTools.buildAtomikosPGDataSourceBeanWithoutTestQuery();
            //transacted with test query
            ds = BitronixTools.buildBitronixPGDataSourceBeanWithTestQuery();

            ((PoolingDataSource) ds).setIsolationLevel("SERIALIZABLE");
        }

        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException
    {
            conn.close();
    }
}

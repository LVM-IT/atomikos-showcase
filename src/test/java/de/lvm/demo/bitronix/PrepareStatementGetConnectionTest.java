package de.lvm.demo.bitronix;

import bitronix.tm.BitronixTransactionManager;
import com.atomikos.icatch.jta.UserTransactionManager;
import de.lvm.demo.AtomikosTools;
import de.lvm.demo.BitronixTools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Closing statement after connection
 * This Test *Only* fails with DB/2!!
 */
public class PrepareStatementGetConnectionTest
{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource ds;

    @Test
    public void show() throws Exception
    {

        BitronixTransactionManager btm = new BitronixTransactionManager();


        String uuid = UUID.randomUUID().toString();

        btm.begin();
        logger.info("open Connection ...");
        Connection conn = open();

        try (PreparedStatement ps = conn.prepareStatement("select count from TEST.daten where name like ?"))
        {
            ps.setString(1, "example%");
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    int count = resultSet.getInt("count");
                    logger.info("Count: " + count);
                }
                logger.info("close connection ...");
                ps.getConnection().close();
            }
        }

        btm.commit();
        //  conn.close();
        btm.shutdown();

    }

    protected Connection open() throws SQLException
    {

        if (ds == null)
        {
            //transacted without test query
            ds = BitronixTools.buildBitronixDB2DataSourceBeanWithoutTestQuery();
            //transacted with test query
//            ds = AtomikosTools.buildAtomikosDB2DataSourceBeanWithTestQuery();
        }

        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException
    {
        conn.close();
    }
}

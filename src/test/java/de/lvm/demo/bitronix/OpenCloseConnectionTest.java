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
 * Exception when using a connection for open-close
 */
public class OpenCloseConnectionTest
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

        //XX
        logger.info("open Connection ...");
        conn = open();
        logger.info("conn {}", conn);
        logger.info("close Connection ...");
        close(conn);
        logger.info("Connection closed ...");

        conn = open();

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
            }
        }

        btm.commit();
        conn.close();
        btm.shutdown();

    }

    protected Connection open() throws SQLException
    {

        if (ds == null)
        {
            //untransacted
//        ds = PgDatabaseTools.buildDataSource();
            //transacted without test query
            ds = BitronixTools.buildBitronixPGDataSourceBeanWithoutTestQuery();
            //transacted with test query
//            ds = AtomikosTools.buildAtomikosPGDataSourceBeanWithTestQuery();
        }

        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException
    {
        conn.close();
    }
}

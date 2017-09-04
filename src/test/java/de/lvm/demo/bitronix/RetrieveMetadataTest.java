package de.lvm.demo.bitronix;

import bitronix.tm.BitronixTransactionManager;
import com.atomikos.icatch.jta.UserTransactionManager;
import de.lvm.demo.AtomikosTools;
import de.lvm.demo.BitronixTools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

/**
 * Example when retrieving metadata from a connection
 * This Test *Only* fails with DB/2 on System Z!!!
 */
public class RetrieveMetadataTest
{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource ds;

    @Test
    public void show() throws Exception
    {

        String uuid = UUID.randomUUID().toString();

        BitronixTransactionManager btm = new BitronixTransactionManager();

        btm.begin();
        logger.info("open Connection ...");
        Connection conn = open();

        //XX
        logger.info("get metadata ...");
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet catalogs = metaData.getCatalogs();
        catalogs.close();

        logger.info("conn {}", conn);
        try (PreparedStatement ps = conn.prepareStatement("select 1 from sysibm.sysdummy1"))
        {
            ps.execute();
        }
        logger.info("close Connection ...");
        conn.close();

        logger.info("open Connection ...");
        conn = open();
        try (PreparedStatement ps = conn.prepareStatement("select 1 from sysibm.sysdummy1"))
        {
            ResultSet resultSet = ps.executeQuery();
        }
        btm.commit();
        conn.close();
        btm.shutdown();

    }

    protected Connection open() throws SQLException
    {

        if (ds == null)
        {
            //transacted without test query
            ds = BitronixTools.buildBitronixDB2HostDataSourceBeanWithoutTestQuery();
            //transacted with test query
//            ds = AtomikosTools.buildAtomikosDataSourceBeanWithTestQuery();
        }

        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException
    {
        conn.close();
    }
}

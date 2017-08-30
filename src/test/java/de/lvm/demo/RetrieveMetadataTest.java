package de.lvm.demo;

import com.atomikos.icatch.jta.UserTransactionManager;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        UserTransactionManager utm = new UserTransactionManager();
        utm.init();
        utm.begin();
        logger.info("open Connection ...");
        Connection conn = open();

        //XX
        logger.info("get metadata ...");
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet catalogs = metaData.getCatalogs();
        catalogs.close();

        //XX
        DatabaseMetaData metaData_ = conn.getMetaData();
        ResultSet catalogs_ = metaData_.getCatalogs();
        catalogs_.close();

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

        logger.info("open Connection ...");
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

        utm.commit();
        conn.close();
        utm.close();

    }

    protected Connection open() throws SQLException
    {

        if (ds == null)
        {
            //transacted without test query
            ds = AtomikosTools.buildAtomikosDB2DataSourceBeanWithoutTestQuery();
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

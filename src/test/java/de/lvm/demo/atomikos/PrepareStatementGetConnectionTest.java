package de.lvm.demo.atomikos;

import com.atomikos.icatch.jta.UserTransactionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import de.lvm.demo.AtomikosTools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        UserTransactionManager utm = new UserTransactionManager();
        utm.init();

        utm.begin();
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

        utm.commit();
        //  conn.close();
        utm.close();

    }

    protected Connection open() throws SQLException
    {

        if (ds == null)
        {
            //transacted without test query
            ds = AtomikosTools.buildAtomikosDB2DataSourceBeanWithoutTestQuery();
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

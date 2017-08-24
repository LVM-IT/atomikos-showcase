package de.lvm.demo;

import com.atomikos.icatch.jta.UserTransactionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception when using a connection for open-close
 */
public class OpenCloseConnection
{

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private DataSource ds;

    @Test
    public void show() throws SystemException, NotSupportedException
    {

        UserTransactionManager utm = new UserTransactionManager();
        utm.init();

        try
        {
            String uuid = UUID.randomUUID().toString();

            utm.begin();
            logger.info("open Connection ...");
            Connection conn = open();

            try
            {
                logger.info("conn {}", conn);
                try (PreparedStatement ps = conn.prepareStatement("insert into TEST.daten (id, name, count) VALUES (?,?,?)"))
                {
                    ps.setString(1, uuid);
                    ps.setString(2, "example");
                    ps.setInt(3, 1234);

                    logger.info("writing with id: {}", uuid);
                    ps.execute();
                }
            } finally
            {
                logger.info("close Connection ...");
                conn.close();
            }

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

            utm.commit();
            conn.close();
            utm.close();

        } catch (SQLException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex)
        {
            logger.error("query ", ex);
        }
    }

    protected Connection open()
    {
        
        if(ds == null){
            //untransacted
//        ds = PgDatabaseTools.buildDataSource();
            //transacted without test query
//            ds = AtomikosTools.buildAtomikosDataSourceBeanWithoutTestQuery();
            //transacted with test query
            ds = AtomikosTools.buildAtomikosDataSourceBeanWithTestQuery();
        }
        
        Connection conn = null;
        try
        {
            conn = ds.getConnection();
        } catch (SQLException ex)
        {
            logger.error("open connection ", ex);
        }
        return conn;
    }

    protected void close(Connection conn)
    {
        try
        {
            conn.close();
        } catch (SQLException ex)
        {
            logger.error("close connection ", ex);
        }
    }
}

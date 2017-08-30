package de.lvm.demo;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Connections taken from the pool keep auto-commit setting as returned to pool
 */
public class PoolAutocommitFlagsTest
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

        boolean initialAutocommitState = conn.getAutoCommit();
        logger.info("Auto-commit is initially '{}'", initialAutocommitState);
        conn.setAutoCommit(!initialAutocommitState);

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

        logger.info("Auto-commit is '{}'", conn.getAutoCommit());
        assertThat(conn.getAutoCommit(), is(initialAutocommitState));

        conn.close();
        utm.commit();
        utm.close();

    }

    protected Connection open() throws SQLException
    {
        if (ds == null)
        {
            //transacted without test query
            ds = AtomikosTools.buildAtomikosPGDataSourceBeanWithoutTestQuery();

            ((AtomikosDataSourceBean) ds).setMaxPoolSize(1);
        }

        Connection conn = null;
        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException
    {
        conn.close();
    }
}

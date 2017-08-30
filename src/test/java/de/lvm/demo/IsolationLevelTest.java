package de.lvm.demo;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

        String uuid = UUID.randomUUID().toString();

        UserTransactionManager utm = new UserTransactionManager();

        //works
        doIt(utm, uuid);

        //breaks when setting isolation level (again) on postgresql
        assertThat(doIt(utm, uuid), is(true));

    }

    protected boolean doIt(UserTransactionManager utm, String uuid) throws RollbackException, HeuristicMixedException, SQLException, SecurityException, NotSupportedException, HeuristicRollbackException, SystemException, IllegalStateException
    {
        utm.init();
        utm.begin();

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

        utm.commit();
        utm.close();

        return true;
    }

    protected Connection open() throws SQLException
    {

        if (ds == null)
        {
            //transacted without test query
//            ds = AtomikosTools.buildAtomikosDataSourceBeanWithoutTestQuery();
            //transacted with test query
            ds = AtomikosTools.buildAtomikosDB2DataSourceBeanWithTestQuery();

            ((AtomikosDataSourceBean) ds).setDefaultIsolationLevel(8);
        }

        return ds.getConnection();
    }

    protected void close(Connection conn) throws SQLException
    {
            conn.close();
    }
}

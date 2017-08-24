package de.lvm.demo;


import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;


public class PgDatabaseTools
{

    public static DataSource buildDataSource()
    {
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setServerName("localhost");
        source.setDatabaseName("test");
        source.setUser("test");
        source.setPassword("test");
        
        return source;
    }
}

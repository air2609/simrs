package com.vone.medisafe.common.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.postgresql.ds.PGPoolingDataSource;

import com.vone.medisafe.common.services.ConfigurationManager;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: May 26, 2005
 * Time: 8:40:45 PM
 */

public class PostgresPoolManager
{
    private static Logger logger = Logger.getLogger(PostgresPoolManager.class);

    Connection con = null;
    static PostgresPoolManager poolmgr = null;
    PGPoolingDataSource poolDS = new PGPoolingDataSource();
    private String server, dbname, url, userid, password = null;
    private int port, maxcon, mincon;

    private PostgresPoolManager()
    {
        initDataSource();
        poolDS.setServerName(server);
        poolDS.setPortNumber(port);
        poolDS.setDatabaseName(dbname);
        poolDS.setUser(userid);
        poolDS.setPassword(password);
        poolDS.setMaxConnections(maxcon);
    }

    public static PostgresPoolManager getInstance()
    {
        if (poolmgr == null)
        {
            synchronized (PostgresPoolManager.class)
            {
                poolmgr = new PostgresPoolManager();
            }
        }
        return poolmgr;
    }

    public Connection getConnection() throws SQLException
    {
        System.out.println("poolDS.getServerName() = " + poolDS.getServerName());
        con = poolDS.getConnection();

        if (con == null)
            System.out.println("connection failed to established");
        else
            System.out.println("connection is established succesfully");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~~~~~~~~POSTGRESQL CONNECTION POOLING~~~~~~~~~~~~");
        System.out.println(" MAX CONNECTION      : " + poolDS.getMaxConnections());
        System.out.println(" SERVER              : " + poolDS.getServerName());
        System.out.println(" DATABASE NAME       : " + poolDS.getDatabaseName());
        System.out.println(" PORT                : " + poolDS.getPortNumber());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return con;
    }

    private void initDataSource()
    {
        String stUrl = null;

        url = ConfigurationManager.getKey("url");
        server = ConfigurationManager.getKey("server");
        dbname = ConfigurationManager.getKey("dbname");
        userid = ConfigurationManager.getKey("userid");
        password = ConfigurationManager.getKey("password");
        try
        {
            port = Integer.parseInt(ConfigurationManager.getKey("port"));
        }
        catch (NumberFormatException e)
        {
            logger.error("Unable to Get Port Value");
        }
        try
        {
            maxcon = Integer.parseInt(ConfigurationManager.getKey("maxcon"));
            mincon = Integer.parseInt(ConfigurationManager.getKey("mincon"));
        }
        catch (NumberFormatException e)
        {
            logger.error("Unable to Get Max/Min Conneciton Value");
        }

        System.out.println("url = " + url);
        System.out.println("server = " + server);
        System.out.println("dbname = " + dbname);
        System.out.println("userid = " + userid);
        System.out.println("password = " + password);
        System.out.println("port = " + port);
        System.out.println("maxcon = " + maxcon);
        System.out.println("mincon = " + mincon);
    }
}
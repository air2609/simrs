package com.vone.medisafe.common.services;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Isyak
 * Date: Jun 24, 2005
 * Time: 11:39:41 PM
 */

public class ConfigurationManager
{
    private static Logger logger = Logger.getLogger(ConfigurationManager.class);

    private static Properties properties = new Properties();

    static
    {
        reload();
    }

    public static String getKey(String key)
    {
    	return properties.getProperty(key);
    }

    public static int getKeyAsInt(String key)
    {
        String value = properties.getProperty(key);

        try
        {
            return Integer.parseInt(value);
        }
        catch (Exception e)
        {
            logger.error("Key is not integer value");
            return 0;
        }

    }

    public static void printProperties()
    {
        Enumeration enumerate = properties.propertyNames();
        logger.debug("[ Listing Properties Files ]");
        while (enumerate.hasMoreElements())
        {
            String key = (String) enumerate.nextElement();
            logger.debug("[ " + key + " ] = " + getKey(key));
        }
    }

    public static void reload()
    {
        try
        {
//            String propertyFile = System.getProperty("medisafe.configuration");
        	properties.load(ConfigurationManager.class.getResourceAsStream("medisafe.properties"));
        	//String propertyFile = "/tomcat5/webapps/medisafe/config/zk-image.properties";
        	String propertyFile = "diganti load dari system";
            
//            Messagebox.show("ini path configuration : "+propertyFile);
            	            
            if (StringUtils.hasValue(propertyFile))
            {
                logger.debug("Loading properties files from = " + propertyFile);
               // properties.load(new FileInputStream(propertyFile));
                properties.load(ConfigurationManager.class.getResourceAsStream("medisafe.properties"));

                printProperties();
            }
            else
            {
                logger.error("Unable to find system.properties");
            }
        }
        catch (FileNotFoundException e)
        {
            logger.error("Failed to locate properties files !");
        }
        catch (Exception e)
        {
            logger.error("Fatal error during loading properties files", e);
        }
    }
}

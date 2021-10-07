package com.vone.medisafe.common.services;

import org.apache.log4j.Logger;

import com.vone.medisafe.common.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Properties;

public class ReportService {
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
            String propertyFile = ConfigurationManager.getKey("jasper.path");
//            String propertyFile = "/tomcat5/webapps/medisafe/config/jasper.lst";

            if (StringUtils.hasValue(propertyFile))
            {
                logger.debug("Loading properties files from = " + propertyFile);
                properties.load(new FileInputStream(propertyFile));

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

package com.vone.medisafe.ui.component.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.ScreenImageURLException;
import com.vone.medisafe.common.services.ConfigurationManager;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.StringUtils;

public class ScreenImageURL {

	private static ScreenImageURL instance = null;
	
	private static Logger logger = Logger.getLogger(ScreenImageURL.class);
	
	private Properties properties = new Properties();
	
	private String screenID;
	private String screenURL;
	
	protected ScreenImageURL() throws ScreenImageURLException{
		
        try
        {
        	
            String screenUrlPath = ConfigurationManager.getKey("file.screenUrl");
//        	String screenUrlPath = "/tomcat5/webapps/medisafe/config/zk-image.properties";
            
            System.out.println("Ini properties imagenya : "+screenUrlPath);
            
            if (StringUtils.hasValue(screenUrlPath))
            {
                logger.debug("Loading screen URL file from : " + screenUrlPath);
                properties.load(new FileInputStream(screenUrlPath));
            }
            else
            {
                throw new ScreenImageURLException(MessagesService.getKey("screenurl.missing_license_file"));
            }

        }
        catch (FileNotFoundException e)
        {
        	e.printStackTrace();
            throw new ScreenImageURLException(MessagesService.getKey("screenurl.missing_license_file"));
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            throw new ScreenImageURLException(MessagesService.getKey("screenurl.fatal_error"));
        }		
        
	}
	
	public synchronized static ScreenImageURL getInstance() throws ScreenImageURLException{
		
		if (instance == null)
			instance = new ScreenImageURL();
		
		return instance;
	}
	
	public synchronized String getURL(String id){
		
		String result = properties.getProperty(id);

		if (StringUtils.hasValue(result))
			return result;
		else{
			return properties.getProperty("default");
		}
	}	
}

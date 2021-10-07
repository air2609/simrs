package com.vone.medisafe.common.services;


import org.apache.log4j.Logger;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: Isyak
 * Date: Aug 23, 2005
 * Time: 12:20:59 AM
 */

public class LogoutListener implements HttpSessionBindingListener, Serializable
{
    private static final Logger logger = Logger.getLogger(LogoutListener.class);

    private String ipAddress;

    public LogoutListener(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public void valueBound(HttpSessionBindingEvent event)
    {
    	logger.debug("Value bound - at IP : "+ipAddress);
    }

    public void valueUnbound(HttpSessionBindingEvent event)
    {   
        logger.debug("Value Unbound - Reseting Session");

        SessionManager sm = SessionManager.getInstance();
        sm.removeSession(ipAddress);
    }
}

package com.vone.medisafe.common.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;

/**
 * Created by IntelliJ IDEA.
 * User: Isyak
 * Date: Aug 22, 2005
 * Time: 10:20:53 PM
 */

public class SessionManager
{
    private static final Logger logger = Logger.getLogger(SessionManager.class);

    private static SessionManager instance = new SessionManager();

    private HashMap sessionMap = new HashMap();
    private List sessionList = new ArrayList();
    
    /**
     * Default access for singleton class
     * @return SessionManage instance
     */
    public static SessionManager getInstance()
    {
        return instance;
    }

    /**
     * Set active user
     * session of ZUL
     * @param ipAddress ipAddress of login user
     */
    public void setSession(String ipAddress, Session session)
//        throws LicenseException
    {
        // Checking Total User Connected
//        int maxConnection = LicenseManager.getInstance().getMaxConnection();
//        if (maxConnection == sessionMap.size())
//        {
//            throw new LicenseException("Maximum Connection Have Been Reach");
//        }

        logger.debug("Adding Session From IP = " + ipAddress);
        sessionMap.put(ipAddress, session);
        sessionList.add(ipAddress);
    }
    
    /**
     * Set active user without session
     * @param ipAddress ipAddress of login user
     */
    public void setSession(String ipAddress)
//        throws LicenseException
    {
        // Checking Total User Connected
//        int maxConnection = LicenseManager.getInstance().getMaxConnection();
//        if (maxConnection == sessionMap.size())
//        {
//            throw new LicenseException("Maximum Connection Have Been Reach");
//        }

        logger.debug("Adding Session From IP = " + ipAddress);
        sessionMap.put(ipAddress, ipAddress);        
        sessionList.add(ipAddress);
    }
    
  
    
    /**
     * return IP HashMap
     * by James
     * @return
     */
    public HashMap getSessionMap(){
    	
    	
    	return sessionMap;
    }
    
    /**
     * return IP List
     * by James
     * @return
     */
    public List getSessionList(){
    	
    	return sessionList;
    }

    /**
     * Remove active user
     * @param ipAddress ipAddress of login user
     */
    public void removeSession(String ipAddress)
    {
        logger.debug("Removing Session From IP = " + ipAddress);
        
        Session session = (Session)sessionMap.get(ipAddress);        
        
        if (session != null)
        	session.invalidate();
        
        sessionMap.remove(ipAddress);
        sessionList.remove(ipAddress);        
    }

    /**
     * Check if the user is currently login
     * @param ipAddress ipAddress that login
     * @return true if the ipAddress is login, false if the ipAddress is not login
     */
    public boolean isUserActive(String ipAddress)
    {
        return sessionMap.containsKey(ipAddress);
    }

    /**
     * Return total ip active on system
     * @return Total ip active on System
     */
    public int getTotalActiveUser()
    {
        return sessionMap.size();
    }

    protected SessionManager()
    {
    }

}

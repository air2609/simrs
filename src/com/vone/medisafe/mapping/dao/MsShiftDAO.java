package com.vone.medisafe.mapping.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.MsShift;

public class MsShiftDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(MsShiftDAO.class);
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
    public List findAll() throws VONEAppException {
    	
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		List list = session.createQuery(" from MsShift order by VShiftCode ")
    			.list();
    		
    		return list;
    		    	
    	}catch(Exception e){
        	e.printStackTrace();
        	logger.error("findAll failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    }
    
    public void save (MsShift pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		e.printStackTrace();
    		
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void update (MsShift pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsShift pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
}

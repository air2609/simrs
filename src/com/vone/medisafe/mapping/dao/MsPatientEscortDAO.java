package com.vone.medisafe.mapping.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.MsPatientEscort;

public class MsPatientEscortDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(MsPatientEscortDAO.class);
	
    public List findAll() throws VONEAppException {
    	
    	Session session = null;
    	
    	try{
    		session = super.getSessionFactory().openSession();
    		
    		List list = session.createQuery(" from MsPatientEscort order by VEscortCode ")
    			.list();
    		
    		return list;
    		    	
    	}catch(Exception e){
        	e.printStackTrace();
        	logger.error("findAll failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}finally{
        	if (session != null)
        		session.close();    		
    	}
    }
    
    public void save (MsPatientEscort pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		e.printStackTrace();
    		
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void update (MsPatientEscort pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsPatientEscort pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
}

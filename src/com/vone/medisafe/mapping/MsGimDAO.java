package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;

public class MsGimDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(MsGimDAO.class);
	
    public List findByExample(MsGim instance) throws VONEAppException{
        logger.debug("finding MsGim instance by example");
        
        Session session = null;
                
        try {
        	session = getCurrentSession();
        	
            List results = session
                    .createCriteria(MsGim.class)
                    .add(Example.create(instance))
            .list();
            logger.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }        
    
    public MsGim getGimByCode(String code) throws VONEAppException {
        logger.debug("finding MsGim instance by getGimByCode");
        
        Session session = null;
        MsGim pojo = null; 
                
        try {
        	session = getCurrentSession();        	       
        	
        	pojo = (MsGim)session.createQuery("from MsGim where v_key = :code").
        			setString("code", code)
        			.uniqueResult();        	
        	  
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        
        return pojo;
    }
    
    public void save (MsGim pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void saveOrUpdate(MsGim pojo) throws VONEAppException {
	    
		try {
			getHibernateTemplate().saveOrUpdate(pojo);
		}catch (Exception e){
			logger.error("save or update error", e);
			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}
    }
        
    public void update (MsGim pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsGim pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    

}

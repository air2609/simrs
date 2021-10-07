package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;

public class MsBankDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(MsBankDAO.class);
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
	
    public List findByExample(MsBank instance) throws VONEAppException{
        logger.debug("finding MsBank instance by example");
        
        Session session = null;
                
        try {
        	session = getCurrentSession();
        	
            List results = session
                    .createCriteria(MsBank.class)
                    .add(Example.create(instance))
            .list();
            logger.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }        
    
    public void save (MsBank pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void update (MsBank pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsBank pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
    
    
    public MsBank findById( java.lang.Integer id) throws VONEAppException{
        logger.debug("getting MsStaff instance with id: " + id);
        try {
            MsBank instance = (MsBank) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsBank", id);
            return instance;
        } catch (RuntimeException re) {
            logger.error("get failed", re);
            throw re;
        }
    }
}

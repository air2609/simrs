package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;

public class MsInsuranceDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(MsInsuranceDAO.class);
	
    public List findByExample(MsInsurance instance) throws VONEAppException{
        logger.debug("finding MsInsurance instance by example");
        
        Session session = null;
                
        try {
        	session = getCurrentSession();
        	
            List results = session
                    .createCriteria(MsInsurance.class)
                    .add(Example.create(instance))
            .list();
            logger.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }        
    
    public List findAll() throws VONEAppException {
    	
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		List list = session.createQuery(" from MsInsurance order by NActiveStatus desc")
    			.list();
    		
    		return list;
    		    	
    	}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());  		
    	}
    }
    
    public void save (MsInsurance pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void update (MsInsurance pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsInsurance pojo) throws VONEAppException {
    	
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

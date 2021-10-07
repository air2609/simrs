package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;

public class MsScreenInUnitDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(MsScreenInUnitDAO.class);
	
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
    		
    		List list = session.createQuery(" from MsScreenInUnit ")
    			.list();
    		
    		return list;
    		    	
    	}catch(Exception e){
        	logger.error("findAll failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    }         
    
    public void save (MsScreenInUnit pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void update (MsScreenInUnit pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsScreenInUnit pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
    
    public List getUnitByScreenId (Integer scrId) throws VONEAppException {
    	
    	Session session = null;
    	StringBuffer stQuery = new StringBuffer();
    	List list = null;
    	
    	stQuery.append(" select {su.*} from ms_screen_in_unit su where n_screen_id = :scrId");
    	
    	try{
    		session = getCurrentSession();
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("su", MsScreenInUnit.class)
    			.setInteger("scrId", scrId.intValue())
    			.list();
    		    		
    	}catch(Exception e){        
        	logger.error("getUnitByScreenId failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    	
    	return list;
    }
}

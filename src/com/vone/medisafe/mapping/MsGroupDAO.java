package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;



/**
 * Data access object (DAO) for domain model class MsGroup.
 * @see com.vone.medisafe.mapping.MsGroup
 * @author MyEclipse - Hibernate Tools
 */
public class MsGroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsGroupDAO.class);

	protected void initDao() {
		//do nothing
	}
    
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
    public void save(MsGroup transientInstance) throws VONEAppException{
        log.debug("saving MsGroup instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (Exception re) {
            log.error("save failed", re);
            throw new VONEAppException(MessagesService.getKey("error.saveadd"));
        }
    }
    
    public void update(MsGroup transientInstance) throws VONEAppException{
        log.debug("updating MsGroup instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (Exception re) {
            log.error("update failed", re);
            throw new VONEAppException(MessagesService.getKey("error.savemodify"));
        }
    }    
    
	public void delete(MsGroup persistentInstance) throws VONEAppException{
        log.debug("deleting MsGroup instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (Exception re) {
            log.error("delete failed", re);
            throw new VONEAppException(MessagesService.getKey("error.delete"));
        }
    }
    
    public MsGroup findById( java.lang.Integer id) throws VONEAppException{
        log.debug("getting MsGroup instance with id: " + id);
        try {
            MsGroup instance = (MsGroup) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsGroup", id);
            return instance;
        } catch (Exception re) {
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        }
    }
        
    public List findByExample(MsGroup instance) throws VONEAppException{
        log.debug("finding MsGroup instance by example");
        
        Session session = null;
        try {        	
        	session = getCurrentSession();
        	
            List results = session
                    .createCriteria("com.vone.medisafe.mapping.MsGroup")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception re) {
            log.error("find by example failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        } 
    }    
    
    public MsGroup findByGroupCode (String groupCode) throws VONEAppException {
    	
    	Session session = null;
    	
    	try {
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(" from MsGroup where VGroupName = :groupCode");
    		
    		MsGroup groupPojo = (MsGroup)session.createQuery(stQuery.toString())
    			.setString("groupCode", groupCode)
    			.uniqueResult();
    		
    		return groupPojo;
    		
    	}catch (Exception e){
    		log.error("findByGroupCode", e);
    		throw new VONEAppException(MessagesService.getKey("error.nodata"));
    	}
    }

}
package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;



/**
 * Data access object (DAO) for domain model class TbGroupPrivilege.
 * @see com.vone.medisafe.mapping.TbGroupPrivilege
 * @author MyEclipse - Hibernate Tools
 */
public class TbGroupPrivilegeDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbGroupPrivilegeDAO.class);

    StringBuffer stQuery = new StringBuffer();
    
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
    
    public void save(TbGroupPrivilege transientInstance) throws VONEAppException{
        log.debug("saving TbGroupPrivilege instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (Exception re) {
            log.error("save failed", re);
            throw new VONEAppException(MessagesService.getKey("error.saveadd"));
        }
    }
    
    public void update(TbGroupPrivilege transientInstance) throws VONEAppException{
        log.debug("updating TbGroupPrivilege instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (Exception re) {
            log.error("update failed", re);
            throw new VONEAppException(MessagesService.getKey("error.savemodify"));
        }
    }    
    
	public void delete(TbGroupPrivilege persistentInstance) throws VONEAppException{
        log.debug("deleting TbGroupPrivilege instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw new VONEAppException(MessagesService.getKey("error.delete"));
        }
    }
    
    public TbGroupPrivilege findById( com.vone.medisafe.mapping.TbGroupPrivilegeId id) throws VONEAppException{
        log.debug("getting TbGroupPrivilege instance with id: " + id);
        try {
            TbGroupPrivilege instance = (TbGroupPrivilege) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.TbGroupPrivilege", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        }
    }
    
    
    public List findByExample(TbGroupPrivilege instance)throws VONEAppException{
        log.debug("finding TbGroupPrivilege instance by example");
        Session session = null;
        try {
        	
        	session = getCurrentSession();
        	
            List results = session
                    .createCriteria("com.vone.medisafe.mapping.TbGroupPrivilege")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception re) {
            log.error("find by example failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        } 
    }    
    
    public List findByGroupCode (String groupCode) throws VONEAppException {    	    	
    	
    	Session session = null;    	
    	List list = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		stQuery.setLength(0);
    	
    		stQuery.append(" from TbGroupPrivilege p, MsGroup g ")
    			.append(" where p.id.msGroup.NGroupId = g.NGroupId ")
    			.append(" and g.VGroupName = :groupCode");
  
    		list =  session.createQuery(stQuery.toString())    			
    			.setString("groupCode", groupCode)
    			.list();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("findByGroupCode",e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        } 
    	
        return list;
    }
}
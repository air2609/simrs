package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;



/**
 * Data access object (DAO) for domain model class TbUserPrivilege.
 * @see com.vone.medisafe.mapping.TbUserPrivilege
 * @author MyEclipse - Hibernate Tools
 */
public class TbUserPrivilegeDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbUserPrivilegeDAO.class);
    
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

    
    public void save(TbUserPrivilege transientInstance) throws VONEAppException{
        log.debug("saving TbUserPrivilege instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (Exception re) {
            log.error("save failed", re);
            throw new VONEAppException(MessagesService.getKey("error.saveadd"));
        }
    }
    
	public boolean delete(int userId, int screenId) throws VONEAppException{
        log.debug("deleting TbUserPrivilege instance");               

        Session session = null;
        try {
        	
        	session = getCurrentSession();
        	stQuery.setLength(0);
        	stQuery.append("delete from TbUserPrivilege")
        		.append(" where n_user_id = :userId and n_screen_id = :screenId");
        	
        	int res = session.createQuery(stQuery.toString())
        		.setInteger("userId", userId)
        		.setInteger("screenId", screenId)
        		.executeUpdate();
        	
        	if (res > 0 )
        		return true;

        	return false;
        	
        } catch (Exception re) {
            log.error("delete failed", re);
            throw new VONEAppException(MessagesService.getKey("error.delete"));
        }
    }
    
    public TbUserPrivilege findById( com.vone.medisafe.mapping.TbUserPrivilegeId id) throws VONEAppException{
        log.debug("getting TbUserPrivilege instance with id: " + id);
        try {
            TbUserPrivilege instance = (TbUserPrivilege) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.TbUserPrivilege", id);
            return instance;
        } catch (Exception re) {
            log.error("get failed", re);            
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        }
    }
    
    public List findByUser (String stUserName) throws VONEAppException {
    	
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		stQuery.setLength(0);
    		stQuery.append(" from TbUserPrivilege priv, MsUser usr ")    		
    			.append(" where priv.id.msUser.NUserId = usr.NUserId ")
    			.append(" and usr.VUserName = :stUserName");
    		
    		List list = session.createQuery(stQuery.toString())
    			.setString("stUserName", stUserName)
    			.list();
    		
    		return list;
    		
    	}catch (Exception e){
    		log.error("find by User failed", e);
    		throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());
    	}
    }
    
    public List findByExample(TbUserPrivilege instance) throws VONEAppException{
        log.debug("finding TbUserPrivilege instance by example");
        
        Session session = null;
                
        try {
        	session = getCurrentSession();
        	
            List results = session
                    .createCriteria(TbUserPrivilege.class)
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception re) {
            log.error("find by example failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), re.getMessage());
        } 
    }        

    public void update(TbUserPrivilege transientInstance) throws VONEAppException {
        log.debug("updating TbUserPrivilege instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (Exception re) {
            log.error("update failed", re);
            
            throw new VONEAppException(MessagesService.getKey("error.savemodify"));
        }
    }  
    
}
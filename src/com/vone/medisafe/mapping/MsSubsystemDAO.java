package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsSubsystem.
 * @see com.vone.medisafe.mapping.MsSubsystem
 * @author MyEclipse - Hibernate Tools
 */
public class MsSubsystemDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsSubsystemDAO.class);

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
    
    public void save(MsSubsystem transientInstance) {
        log.debug("saving MsSubsystem instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    public void saveOnly(MsSubsystem transientInstance) {
        log.debug("saving MsSubsystem instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsSubsystem persistentInstance) {
        log.debug("deleting MsSubsystem instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsSubsystem findById( java.lang.Integer id) {
        log.debug("getting MsSubsystem instance with id: " + id);
        try {
            MsSubsystem instance = (MsSubsystem) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsSubsystem", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
        
    public List findByExample(MsSubsystem instance) throws VONEAppException{
        log.debug("finding MsSubsystem instance by example");
        Session session = getCurrentSession();
        try {
            List results = session
                    .createCriteria(MsSubsystem.class)
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        } 
    }    
    
    
    public String[] getModuleNameDesc (int iModuleId) throws VONEAppException{

    	StringBuffer stQuery = new StringBuffer();
    	
    	Session session = null;
    	
    	List list = null;
		try {
			session = getCurrentSession();
			
			stQuery.append("from MsSubsystem sbs where sbs.NSubsystemId = :iModuleId ");    			
			
			list = session.createQuery(stQuery.toString())    	
			.setInteger("iModuleId", iModuleId)
			.list();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new VONEAppException(e.getMessage());
		} 
    	
    	for (int i=0; i<list.size(); i++){
    		if (list.get(i) instanceof MsSubsystem) {
    			MsSubsystem sbsPojo = (MsSubsystem)list.get(i);
    			
    			return new String[]{sbsPojo.getVSubsystemCode(),sbsPojo.getVDesc()};
    		}
    	}
    	
    	return new String[]{"",""};
    }
    

}
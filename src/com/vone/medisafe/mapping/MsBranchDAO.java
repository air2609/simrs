package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsBranch.
 * @see com.vone.medisafe.mapping.MsBranch
 * @author MyEclipse - Hibernate Tools
 */
public class MsBranchDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsBranchDAO.class);
    
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
    
    public void save(MsBranch transientInstance) {
        log.debug("saving MsBranch instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    public void update(int iBranchId, String stBranchCode) {
        log.debug("update MsBranch instance");

        Session session = null;        
        
        try {
        	session = getCurrentSession();
        	
        	stQuery.setLength(0);
        	stQuery.append(" update MsBranch ")
        		.append(" set v_branch_name = :stBranchCode ")
        		.append(" where n_branch_id = :iBranchId");
        	
        	        	
        	int result = session.createQuery(stQuery.toString())
        		.setInteger("iBranchId", iBranchId)
        		.setString("stBranchCode", stBranchCode)
        		.executeUpdate();
  
        	if (result > 0)
        		log.debug("update successful");
        	else
        		log.debug("update un-successful");
        } catch (Exception re) {
            log.error("update failed", re);
        } 
    }    
    
	public void delete(MsBranch persistentInstance) {
        log.debug("deleting MsBranch instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsBranch findById( java.lang.Integer id) {
        log.debug("getting MsBranch instance with id: " + id);
        try {
            MsBranch instance = (MsBranch) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsBranch", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsBranch instance) throws VONEAppException{
        log.debug("finding MsBranch instance by example");
        
        Session session = getCurrentSession();
        try {
            List results = session
                    .createCriteria("com.vone.medisafe.mapping.MsBranch")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        } 
    }    
    
    public MsBranch merge(MsBranch detachedInstance) {
        log.debug("merging MsBranch instance");
        try {
            MsBranch result = (MsBranch) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsBranch instance) {
        log.debug("attaching dirty MsBranch instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsBranch instance) {
        log.debug("attaching clean MsBranch instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}
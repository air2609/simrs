package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * Data access object (DAO) for domain model class MsTypeOfViolent.
 * @see com.vone.medisafe.mapping.MsTypeOfViolent
 * @author MyEclipse - Hibernate Tools
 */
public class MsTypeOfViolentDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsTypeOfViolentDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public void save(MsTypeOfViolent transientInstance) {
        log.debug("saving MsTypeOfViolent instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsTypeOfViolent persistentInstance) {
        log.debug("deleting MsTypeOfViolent instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsTypeOfViolent findById( java.lang.String id) {
        log.debug("getting MsTypeOfViolent instance with id: " + id);
        try {
            MsTypeOfViolent instance = (MsTypeOfViolent) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsTypeOfViolent", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsTypeOfViolent instance) {
        log.debug("finding MsTypeOfViolent instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsTypeOfViolent")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsTypeOfViolent merge(MsTypeOfViolent detachedInstance) {
        log.debug("merging MsTypeOfViolent instance");
        try {
            MsTypeOfViolent result = (MsTypeOfViolent) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsTypeOfViolent instance) {
        log.debug("attaching dirty MsTypeOfViolent instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsTypeOfViolent instance) {
        log.debug("attaching clean MsTypeOfViolent instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}
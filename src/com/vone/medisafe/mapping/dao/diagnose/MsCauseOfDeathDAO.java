package com.vone.medisafe.mapping.dao.diagnose;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.mapping.pojo.diagnose.MsCauseOfDeath;



/**
 * Data access object (DAO) for domain model class MsCauseOfDeath.
 * @see com.vone.medisafe.mapping.MsCauseOfDeath
 * @author MyEclipse - Hibernate Tools
 */
public class MsCauseOfDeathDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsCauseOfDeathDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public List getCods(){
		return super.getHibernateTemplate().find(" from "+MsCauseOfDeath.class.getName());
	}
    
    public boolean save(MsCauseOfDeath transientInstance) {
        boolean success = false;
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            success = true;
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
        return success;
    }
    
	public boolean delete(MsCauseOfDeath persistentInstance) {
        boolean success = false;
        try {
            getHibernateTemplate().delete(persistentInstance);
            success = true;
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
        return success;
    }
    
    public MsCauseOfDeath findById( java.lang.String id) {
        log.debug("getting MsCauseOfDeath instance with id: " + id);
        try {
            MsCauseOfDeath instance = (MsCauseOfDeath) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsCauseOfDeath", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsCauseOfDeath instance) {
        log.debug("finding MsCauseOfDeath instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsCauseOfDeath")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsCauseOfDeath merge(MsCauseOfDeath detachedInstance) {
        log.debug("merging MsCauseOfDeath instance");
        try {
            MsCauseOfDeath result = (MsCauseOfDeath) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsCauseOfDeath instance) {
        log.debug("attaching dirty MsCauseOfDeath instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsCauseOfDeath instance) {
        log.debug("attaching clean MsCauseOfDeath instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}
package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * Data access object (DAO) for domain model class TbPregnancy.
 * @see com.vone.medisafe.mapping.TbPregnancy
 * @author MyEclipse - Hibernate Tools
 */
public class TbPregnancyDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbPregnancyDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public void save(TbPregnancy transientInstance) {
        log.debug("saving TbPregnancy instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TbPregnancy persistentInstance) {
        log.debug("deleting TbPregnancy instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TbPregnancy findById( java.lang.String id) {
        log.debug("getting TbPregnancy instance with id: " + id);
        try {
            TbPregnancy instance = (TbPregnancy) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.TbPregnancy", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TbPregnancy instance) {
        log.debug("finding TbPregnancy instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.TbPregnancy")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public TbPregnancy merge(TbPregnancy detachedInstance) {
        log.debug("merging TbPregnancy instance");
        try {
            TbPregnancy result = (TbPregnancy) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbPregnancy instance) {
        log.debug("attaching dirty TbPregnancy instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TbPregnancy instance) {
        log.debug("attaching clean TbPregnancy instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}
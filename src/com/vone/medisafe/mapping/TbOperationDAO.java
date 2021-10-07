package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * Data access object (DAO) for domain model class TbOperation.
 * @see com.vone.medisafe.mapping.TbOperation
 * @author MyEclipse - Hibernate Tools
 */
public class TbOperationDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbOperationDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public void save(TbOperation transientInstance) {
        log.debug("saving TbOperation instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TbOperation persistentInstance) {
        log.debug("deleting TbOperation instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TbOperation findById( java.lang.String id) {
        log.debug("getting TbOperation instance with id: " + id);
        try {
            TbOperation instance = (TbOperation) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.TbOperation", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TbOperation instance) {
        log.debug("finding TbOperation instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.TbOperation")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public TbOperation merge(TbOperation detachedInstance) {
        log.debug("merging TbOperation instance");
        try {
            TbOperation result = (TbOperation) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbOperation instance) {
        log.debug("attaching dirty TbOperation instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TbOperation instance) {
        log.debug("attaching clean TbOperation instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}
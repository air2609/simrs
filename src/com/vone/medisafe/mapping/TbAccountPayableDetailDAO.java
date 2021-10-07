package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TbAccountPayableDetail.
 * @see com.vone.medisafe.mapping.TbAccountPayableDetail
 * @author MyEclipse - Hibernate Tools
 */
public class TbAccountPayableDetailDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbAccountPayableDetailDAO.class);

	//property constants
	public static final String _VJOURNAL_BATCH_ID = "VJournalBatchId";
	public static final String _VWHO_CREATE = "VWhoCreate";
	public static final String _VWHO_CHANGE = "VWhoChange";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TbAccountPayableDetail transientInstance) {
        log.debug("saving TbAccountPayableDetail instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TbAccountPayableDetail persistentInstance) {
        log.debug("deleting TbAccountPayableDetail instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TbAccountPayableDetail findById( java.lang.Integer id) {
        log.debug("getting TbAccountPayableDetail instance with id: " + id);
        try {
            TbAccountPayableDetail instance = (TbAccountPayableDetail) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.TbAccountPayableDetail", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TbAccountPayableDetail instance) {
        log.debug("finding TbAccountPayableDetail instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding TbAccountPayableDetail instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TbAccountPayableDetail as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByVJournalBatchId(Object VJournalBatchId) {
		return findByProperty(_VJOURNAL_BATCH_ID, VJournalBatchId);
	}
	
	public List findByVWhoCreate(Object VWhoCreate) {
		return findByProperty(_VWHO_CREATE, VWhoCreate);
	}
	
	public List findByVWhoChange(Object VWhoChange) {
		return findByProperty(_VWHO_CHANGE, VWhoChange);
	}
	
    public TbAccountPayableDetail merge(TbAccountPayableDetail detachedInstance) {
        log.debug("merging TbAccountPayableDetail instance");
        try {
            TbAccountPayableDetail result = (TbAccountPayableDetail) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbAccountPayableDetail instance) {
        log.debug("attaching dirty TbAccountPayableDetail instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TbAccountPayableDetail instance) {
        log.debug("attaching clean TbAccountPayableDetail instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TbAccountPayableDetailDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TbAccountPayableDetailDAO) ctx.getBean("TbAccountPayableDetailDAO");
	}
}
package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * Data access object (DAO) for domain model class MsTreatmentFee.
 * @see com.vone.medisafe.mapping.MsTreatmentFee
 * @author MyEclipse - Hibernate Tools
 */
public class MsTreatmentFeeDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsTreatmentFeeDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public void save(MsTreatmentFee transientInstance) {
        log.debug("saving MsTreatmentFee instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsTreatmentFee persistentInstance) {
        log.debug("deleting MsTreatmentFee instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
      
    public List findByExample(MsTreatmentFee instance) {
        log.debug("finding MsTreatmentFee instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsTreatmentFee")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsTreatmentFee merge(MsTreatmentFee detachedInstance) {
        log.debug("merging MsTreatmentFee instance");
        try {
            MsTreatmentFee result = (MsTreatmentFee) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsTreatmentFee instance) {
        log.debug("attaching dirty MsTreatmentFee instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsTreatmentFee instance) {
        log.debug("attaching clean MsTreatmentFee instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}
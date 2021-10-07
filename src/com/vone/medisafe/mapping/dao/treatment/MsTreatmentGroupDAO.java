package com.vone.medisafe.mapping.dao.treatment;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentGroup;



/**
 * Data access object (DAO) for domain model class MsTreatmentGroup.
 * @see com.vone.medisafe.mapping.MsTreatmentGroup
 * @author MyEclipse - Hibernate Tools
 */
public class MsTreatmentGroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsTreatmentGroupDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public boolean save(MsTreatmentGroup transientInstance) {
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
    
    public List getAllTreatmentGroup(){
    	return super.getHibernateTemplate().find(" from "+MsTreatmentGroup.class.getName() + " order by VTgroupCode");
    }
    
	public boolean delete(MsTreatmentGroup persistentInstance) {
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
    
    public MsTreatmentGroup findById( java.lang.Integer id) {
        log.debug("getting MsTreatmentGroup instance with id: " + id);
        try {
            MsTreatmentGroup instance = (MsTreatmentGroup) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsTreatmentGroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsTreatmentGroup instance) {
        log.debug("finding MsTreatmentGroup instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsTreatmentGroup")
                    .add(Example.create(instance))
                    
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsTreatmentGroup merge(MsTreatmentGroup detachedInstance) {
        log.debug("merging MsTreatmentGroup instance");
        try {
            MsTreatmentGroup result = (MsTreatmentGroup) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsTreatmentGroup instance) {
        log.debug("attaching dirty MsTreatmentGroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsTreatmentGroup instance) {
        log.debug("attaching clean MsTreatmentGroup instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }

	public MsTreatment reloadMsTreatment(MsTreatment msTreatment) throws VONEAppException{
		return (MsTreatment) getCurrentSession().get(MsTreatment.class, msTreatment.getNTreatmentId());
	}
    
}
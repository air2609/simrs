package com.vone.medisafe.mapping.dao.diagnose;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd;



/**
 * Data access object (DAO) for domain model class MsIcd.
 * @see com.vone.medisafe.mapping.MsIcd
 * @author MyEclipse - Hibernate Tools
 */
public class MsIcdDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsIcdDAO.class);

	protected void initDao() {
		//do nothing
	}
    
	public List getIcds(){
		return super.getHibernateTemplate().find(" from "+ MsIcd.class.getName());
	}
    public boolean save(MsIcd transientInstance) {
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
    
	public boolean delete(MsIcd persistentInstance) {
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
    
    public MsIcd findById( java.lang.String id) {
        log.debug("getting MsIcd instance with id: " + id);
        try {
            MsIcd instance = (MsIcd) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsIcd", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsIcd instance) throws VONEAppException{
        Session session = null;
        try {
        	session = getCurrentSession();
            List results = session
            .createCriteria(MsIcd.class)
            .add(Restrictions.like("VIcdCode", instance.getVIcdCode()))
            .add(Restrictions.like("VIcdName", instance.getVIcdName()))
            .list();
            		
//                    .add(Example.create(instance))
//            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }    
    
    public MsIcd merge(MsIcd detachedInstance) {
        log.debug("merging MsIcd instance");
        try {
            MsIcd result = (MsIcd) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsIcd instance) {
        log.debug("attaching dirty MsIcd instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsIcd instance) {
        log.debug("attaching clean MsIcd instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public List<MsIcd> searchIcd(String icdCode, String icdName) throws VONEAppException{
		
		List<MsIcd> result = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsIcd.class);
			crit.add(Restrictions.like("VIcdCode", icdCode));
			crit.add(Restrictions.like("VIcdName", icdName));
			
			result = crit.list();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    


}
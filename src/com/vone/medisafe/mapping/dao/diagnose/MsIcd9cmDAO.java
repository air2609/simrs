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
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd9cm;



/**
 * Data access object (DAO) for domain model class MsIcd9cm.
 * @see com.vone.medisafe.mapping.MsIcd9cm
 * @author MyEclipse - Hibernate Tools
 */
public class MsIcd9cmDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsIcd9cmDAO.class);

	protected void initDao() {
		//do nothing
	}
    
	public List getIcd9s(){
		return super.getHibernateTemplate().find(" from " + MsIcd9cm.class.getName());
	}
    public boolean save(MsIcd9cm transientInstance) {
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
    
	public boolean delete(MsIcd9cm persistentInstance) {
        boolean success = false;
        try {
            getHibernateTemplate().delete(persistentInstance);
            success = true;
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
        return success;
    }
    
    public MsIcd9cm findById( java.lang.String id) {
        log.debug("getting MsIcd9cm instance with id: " + id);
        try {
            MsIcd9cm instance = (MsIcd9cm) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsIcd9cm", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsIcd9cm instance) throws VONEAppException {
        Session session = null;
        try {
        	session = getCurrentSession();
            List results = session
                    .createCriteria(MsIcd9cm.class)
                    .add(Restrictions.like("VIcd9cmCode", instance.getVIcd9cmCode()))
                    .add(Restrictions.like("VIcd9cmName", instance.getVIcd9cmName()))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }    
    
    public MsIcd9cm merge(MsIcd9cm detachedInstance) {
        log.debug("merging MsIcd9cm instance");
        try {
            MsIcd9cm result = (MsIcd9cm) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsIcd9cm instance) {
        log.debug("attaching dirty MsIcd9cm instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsIcd9cm instance) {
        log.debug("attaching clean MsIcd9cm instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    

	public List<MsIcd9cm> searchIcd9(String icdCode, String icdName) throws VONEAppException{
		List<MsIcd9cm> result = null;
		Session session = null;
		
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsIcd9cm.class);
			crit.add(Restrictions.like("VIcd9cmCode", icdCode));
			crit.add(Restrictions.like("VIcd9cmName", icdName));
			
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
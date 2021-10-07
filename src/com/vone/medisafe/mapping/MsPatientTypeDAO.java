package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsPatientType.
 * @see com.vone.medisafe.mapping.MsPatientType
 * @author MyEclipse - Hibernate Tools
 */
public class MsPatientTypeDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsPatientTypeDAO.class);

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
	 
	
    public List<MsPatientType> getAllPatientType() throws VONEAppException{
    	
    	List<MsPatientType> list = null;
    	
    	Query q = getCurrentSession().createQuery("from MsPatientType order by VTpatient ASC");
    	
    	list = q.list();
    	
    	return list;
    }
    public void save(MsPatientType transientInstance) {
        log.debug("saving MsPatientType instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsPatientType persistentInstance) {
        log.debug("deleting MsPatientType instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsPatientType findById( java.lang.Integer id) {
        log.debug("getting MsPatientType instance with id: " + id);
        try {
            MsPatientType instance = (MsPatientType) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsPatientType", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsPatientType instance) {
        log.debug("finding MsPatientType instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsPatientType")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsPatientType merge(MsPatientType detachedInstance) {
        log.debug("merging MsPatientType instance");
        try {
            MsPatientType result = (MsPatientType) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsPatientType instance) {
        log.debug("attaching dirty MsPatientType instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsPatientType instance) {
        log.debug("attaching clean MsPatientType instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public MsPatientType getPatientTypeByCode(String code){
    	MsPatientType ptype = null;
    	Session session = null;
    	
    	try {
			session = super.getSessionFactory().openSession();
			Criteria crit = session.createCriteria(MsPatientType.class);
			crit.add(Restrictions.eq("VTpatient", code));
			ptype = (MsPatientType)crit.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	
    	return ptype;
    }
    
    public boolean deleteByid(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsPatientType where NPatientTypeId=:ptypeId";
    	
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("ptypeId", id.intValue());
			int hasil = query.executeUpdate();
			if(hasil > 0)sukses=true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	
    	return sukses;
    }

}
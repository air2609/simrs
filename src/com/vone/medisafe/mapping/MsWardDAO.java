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



/**
 * Data access object (DAO) for domain model class MsWard.
 * @see com.vone.medisafe.mapping.MsWard
 * @author MyEclipse - Hibernate Tools
 */
public class MsWardDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsWardDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public void save(MsWard transientInstance) {
        log.debug("saving MsWard instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    public List getAllWard(Class clazz){
    	return super.getHibernateTemplate().find(" from "+clazz.getName());
    }
    
	public void delete(MsWard persistentInstance) {
        log.debug("deleting MsWard instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsWard findById( java.lang.Integer id) {
        log.debug("getting MsWard instance with id: " + id);
        try {
            MsWard instance = (MsWard) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsWard", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsWard instance) {
        log.debug("finding MsWard instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsWard")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsWard merge(MsWard detachedInstance) {
        log.debug("merging MsWard instance");
        try {
            MsWard result = (MsWard) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsWard instance) {
        log.debug("attaching dirty MsWard instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsWard instance) {
        log.debug("attaching clean MsWard instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    public MsWard getWardByCode(String code){
    	MsWard ward = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(MsWard.class);
			criteria.add(Restrictions.eq("VWardCode", code));
			ward = (MsWard)criteria.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		
    	return ward;
    }
    
    public boolean deleteById(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsWard where NWardId=:unitId";
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("unitId", id.intValue());
			int hasil = query.executeUpdate();
			if(hasil > 0)sukses = true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	return sukses;

    }

}
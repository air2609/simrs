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
 * Data access object (DAO) for domain model class MsMedicStaffGroup.
 * @see com.vone.medisafe.mapping.MsMedicStaffGroup
 * @author MyEclipse - Hibernate Tools
 */
public class MsMedicStaffGroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsMedicStaffGroupDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public List getAllMedicStaffGroup(Class clazz){
		return super.getHibernateTemplate().find(" from "+clazz.getName());
	}
    
    public void save(MsMedicStaffGroup transientInstance) {
        log.debug("saving MsMedicStaffGroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsMedicStaffGroup persistentInstance) {
        log.debug("deleting MsMedicStaffGroup instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsMedicStaffGroup findById( java.lang.Integer id) {
        log.debug("getting MsMedicStaffGroup instance with id: " + id);
        try {
            MsMedicStaffGroup instance = (MsMedicStaffGroup) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsMedicStaffGroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsMedicStaffGroup instance) {
        log.debug("finding MsMedicStaffGroup instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsMedicStaffGroup")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsMedicStaffGroup merge(MsMedicStaffGroup detachedInstance) {
        log.debug("merging MsMedicStaffGroup instance");
        try {
            MsMedicStaffGroup result = (MsMedicStaffGroup) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsMedicStaffGroup instance) {
        log.debug("attaching dirty MsMedicStaffGroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsMedicStaffGroup instance) {
        log.debug("attaching clean MsMedicStaffGroup instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public MsMedicStaffGroup getByCode(String code){
    	MsMedicStaffGroup msg = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().openSession();
			Criteria crit = session.createCriteria(MsMedicStaffGroup.class);
			crit.add(Restrictions.eq("VMsgroupCode", code));
			msg = (MsMedicStaffGroup)crit.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	
    	return msg;
    }
    
    public boolean deleteById(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsMedicStaffGroup where NMsgroupId=:msgId";
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("msgId", id.intValue());
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
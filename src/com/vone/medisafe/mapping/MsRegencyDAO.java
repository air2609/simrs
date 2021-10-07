package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsRegency.
 * @see com.vone.medisafe.mapping.MsRegency
 * @author MyEclipse - Hibernate Tools
 */
public class MsRegencyDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsRegencyDAO.class);

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
	
	public List<MsRegency> getAllRegencyList() throws VONEAppException{
		
		List<MsRegency> list = null;
		
		Query q = getCurrentSession().createQuery("from MsRegency order by VRegencyId");
		list = q.list();
		
		return list;
		
	}
	
	public List<MsRegency> getAllRegencyByProvince(MsProvince province) throws VONEAppException{
		
		List<MsRegency> list = null;
		
		Query q = getCurrentSession().createQuery("from MsRegency where province=:myprov");
		q.setParameter("myprov", province);
		list = q.list();
		
		return list;
		
	}
    
    public void save(MsRegency transientInstance) {
        log.debug("saving MsRegency instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsRegency persistentInstance) {
        log.debug("deleting MsRegency instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsRegency findById( java.lang.Integer id) {
        log.debug("getting MsRegency instance with id: " + id);
        try {
            MsRegency instance = (MsRegency) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsRegency", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsRegency instance) {
        log.debug("finding MsRegency instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsRegency")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsRegency merge(MsRegency detachedInstance) {
        log.debug("merging MsRegency instance");
        try {
            MsRegency result = (MsRegency) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsRegency instance) {
        log.debug("attaching dirty MsRegency instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsRegency instance) {
        log.debug("attaching clean MsRegency instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public MsRegency getByCode(String code){
    	MsRegency regency = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().openSession();
			Criteria crit = session.createCriteria(MsRegency.class);
			crit.add(Restrictions.eq("VRegencyId",code));
			regency = (MsRegency)crit.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		return regency;
    }
    
    public boolean deleteById(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsRegency where NRegencyId=:regId";
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("regId", id.intValue());
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
    
    public List searchRegency(String code, String name){
    	List result = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().openSession();
			Criteria crit = session.createCriteria(MsRegency.class);
			Criterion crCode = Restrictions.like("VRegencyId", code+"%");
			Criterion crName = Restrictions.like("VRegencyName", name+"%");
			
			Conjunction con = Restrictions.conjunction();
			con.add(crCode);
			con.add(crName);
			
			crit.add(con);
			result = crit.list();
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	return result;
    }
}
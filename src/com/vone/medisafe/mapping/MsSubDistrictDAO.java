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
 * Data access object (DAO) for domain model class MsSubDistrict.
 * @see com.vone.medisafe.mapping.MsSubDistrict
 * @author MyEclipse - Hibernate Tools
 */
public class MsSubDistrictDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsSubDistrictDAO.class);

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
	
	
	public List<MsSubDistrict> getAllSubdistrict() throws VONEAppException
	{
		List<MsSubDistrict> list = null;
		
		Query q = getCurrentSession().createQuery("from MsSubDistrict");
		
		list = q.list();
		
		return list;
	}
	
	public List<MsSubDistrict> getAllSubdistrictByRegency(MsRegency regency) throws VONEAppException
	{
		List<MsSubDistrict> list = null;
		
		Query q = getCurrentSession().createQuery("from MsSubDistrict where regency=:myRegency");
		q.setParameter("myRegency", regency);
		
		list = q.list();
		
		return list;
	}
	
    
    public void save(MsSubDistrict transientInstance) {
        log.debug("saving MsSubDistrict instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsSubDistrict persistentInstance) {
        log.debug("deleting MsSubDistrict instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsSubDistrict findById( java.lang.Integer id) {
        log.debug("getting MsSubDistrict instance with id: " + id);
        try {
            MsSubDistrict instance = (MsSubDistrict) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsSubDistrict", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsSubDistrict instance) {
        log.debug("finding MsSubDistrict instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsSubDistrict")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsSubDistrict merge(MsSubDistrict detachedInstance) {
        log.debug("merging MsSubDistrict instance");
        try {
            MsSubDistrict result = (MsSubDistrict) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsSubDistrict instance) {
        log.debug("attaching dirty MsSubDistrict instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsSubDistrict instance) {
        log.debug("attaching clean MsSubDistrict instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public MsSubDistrict getByCode(String code){
    	MsSubDistrict subdistrict = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().openSession();
			Criteria crit = session.createCriteria(MsSubDistrict.class);
			crit.add(Restrictions.eq("VSubDistrictId", code));
			subdistrict = (MsSubDistrict)crit.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	return subdistrict;
    }
    
    public boolean deleteById(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsSubDistrict where NSubdistrictId=:subdistrictId";
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("subdistrictId", id.intValue());
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
    
    public List searchSubdistrict(String code, String name){
    	Session session = null;
    	List result = null;
    	try {
			session = super.getSessionFactory().openSession();
			Criteria crit = session.createCriteria(MsSubDistrict.class);
			Criterion sbCode = Restrictions.like("VSubDistrictId", code+"%");
			Criterion sbName = Restrictions.like("VSubDistrictName", name+"%");
			Conjunction con = Restrictions.conjunction();
			con.add(sbCode);
			con.add(sbName);
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
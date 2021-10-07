package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsProvince.
 * @see com.vone.medisafe.mapping.MsProvince
 * @author MyEclipse - Hibernate Tools
 */
public class MsProvinceDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsProvinceDAO.class);

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
	
	public List<MsProvince> getAllProvince() throws VONEAppException{
		
		List<MsProvince> list = null;
		
		Query q = getCurrentSession().createQuery("from MsProvince order by VProvinceId");
		
		list = q.list(); 
		
		return list;
		
	}
    
    public void save(MsProvince transientInstance) {
        log.debug("saving MsProvince instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsProvince persistentInstance) {
        log.debug("deleting MsProvince instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsProvince findById( java.lang.Integer id) {
        log.debug("getting MsProvince instance with id: " + id);
        try {
            MsProvince instance = (MsProvince) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsProvince", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsProvince instance) {
        log.debug("finding MsProvince instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsProvince")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsProvince merge(MsProvince detachedInstance) {
        log.debug("merging MsProvince instance");
        try {
            MsProvince result = (MsProvince) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsProvince instance) {
        log.debug("attaching dirty MsProvince instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsProvince instance) {
        log.debug("attaching clean MsProvince instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    public Integer getRegistrationId(){
    	
    	//StringBuffer stQuery = new StringBuffer();
    	String stQuery = "select nextval('tb_registration_n_reg_id_seq') as nextv";
    	//stQuery.append(" select nextval('tb_registration_n_reg_id_seq') as nextv");    
    	
    	Session session = super.getSessionFactory().openSession();
    	
    	Integer list = null;
    	
    	try {
			list = (Integer)session.createSQLQuery(stQuery)
			.addScalar("nextv", Hibernate.INTEGER)
			.uniqueResult();
//			.list();
			
			//if (list == null || list.size()== 0 || !(list.get(0) instanceof Integer))
			//	return null;
			
//			return (Integer)list.get(0);
			return list;
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (session != null)
				session.close();
		}
    	
    	return null;

    }
    
    public MsProvince getProvinceByCode(String code){
    	MsProvince province = null;
    	Session session = null;
    	
    	try {
			session = super.getSessionFactory().openSession();
			Criteria crit = session.createCriteria(MsProvince.class);
			crit.add(Restrictions.eq("VProvinceId", code));
			province = (MsProvince)crit.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	
    	return province;
    }
    
    public boolean deleteById(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsProvince where NProvinceId=:provId";
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("provId", id.intValue());
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
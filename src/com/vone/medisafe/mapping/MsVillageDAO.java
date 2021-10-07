package com.vone.medisafe.mapping;


import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsVillage.
 * @see com.vone.medisafe.mapping.MsVillage
 * @author MyEclipse - Hibernate Tools
 */
public class MsVillageDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsVillageDAO.class);

    
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public List searchVillage(String villageCode, String villageName){
		Session session = null;
		List result = null;
		String query = "select {vil.*} from ms_village vil where vil.v_village_code like :vCode and vil.v_village_name like :vName";
		try {
			session = super.getSessionFactory().openSession();
			result = session.createSQLQuery(query)
					 .addEntity("vil", MsVillage.class)
					 .setString("vCode", villageCode)
					 .setString("vName", villageName)
					 .list();
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		return result;
	}
	
	
	public List<MsVillage> getAllVillage() throws VONEAppException{
		
		List<MsVillage> list = null;
		
		Query q = getCurrentSession().createQuery("from MsVillage");
		
		list = q.list();
		
		return list;
	}
	
public List<MsVillage> getAllVillageBySubdistrict(MsSubDistrict subdistrict) throws VONEAppException{
		
		List<MsVillage> list = null;
		
		Query q = getCurrentSession().createQuery("from MsVillage where subdistrict=:mysub");
		q.setParameter("mysub", subdistrict);
		
		list = q.list();
		
		return list;
	}

	public void update(MsVillage village){
		getHibernateTemplate().update(village);
	}
    
    public void save(MsVillage transientInstance) {
    	
        log.debug("saving MsVillage instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
//    public void save1(MsVill)
    
	public void delete(MsVillage persistentInstance) {
        log.debug("deleting MsVillage instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsVillage findById( java.lang.Integer id) {
        log.debug("getting MsVillage instance with id: " + id);
        try {
            MsVillage instance = (MsVillage) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsVillage", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsVillage instance) {
        log.debug("finding MsVillage instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsVillage")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsVillage merge(MsVillage detachedInstance) {
        log.debug("merging MsVillage instance");
        try {
            MsVillage result = (MsVillage) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsVillage instance) {
        log.debug("attaching dirty MsVillage instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsVillage instance) {
        log.debug("attaching clean MsVillage instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public Integer getUserOld(){
//    	SQLQuery sql = super.getSession().createSQLQuery(query);
    	Integer integer = (Integer)super.getSession().createSQLQuery("select nextval('test') as id")
    						.addScalar("id", Hibernate.INTEGER)
    						.uniqueResult();
    	
    	
    	return integer;
    
    }
    public MsVillage getVillageByCode(String code){
    	MsVillage vil = null;
    	Session session = null;
    	String query = "select {vil.*} from ms_village vil where vil.v_village_code=:vCode";
    	try {
			session = super.getSessionFactory().openSession();
			vil = (MsVillage)session.createSQLQuery(query)
			.addEntity("vil",MsVillage.class)
			.setString("vCode",code)
			.uniqueResult();
			
		} catch (HibernateException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    	
    	return vil;
    }
    
    public boolean deleteById(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsVillage where NVillageId=:unitId";
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
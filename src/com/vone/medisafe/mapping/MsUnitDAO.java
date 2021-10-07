package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsUnit.
 * @see com.vone.medisafe.mapping.MsUnit
 * @author MyEclipse - Hibernate Tools
 */
public class MsUnitDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsUnitDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public boolean save(MsUnit transientInstance) throws VONEAppException{
        Session session = null;

        boolean success = false;
        
        try {
			session = getCurrentSession();
			
			session.saveOrUpdate(transientInstance);
			
			success = true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		return success;
        
       
    }
    
    public List<MsUnit> getAllUnit() throws VONEAppException{
    	
    	List<MsUnit> unitList = null;
    	
    	Query q = getCurrentSession().createQuery("from MsUnit");
    	
    	unitList = q.list();
		
    	return unitList;
    }  
    
    public List<MsUnit> getRegistrationUnit() throws VONEAppException{
    	List<MsUnit> unitList = null;
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("select {unit.*} from ms_unit unit, ms_division div");
    	sb.append(" where unit.n_division_id=div.n_division_id ");
    	sb.append(" and div.v_registration_unit=:regUnit");
    	
    	SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
    	query.addEntity("unit", MsUnit.class);
    	query.setString("regUnit", "YES");
    	
    	unitList = query.list();
    	
    	return unitList;
    	
    }
    
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
	public void delete(MsUnit persistentInstance) {
        log.debug("deleting MsUnit instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsUnit findById( java.lang.Integer id) {
        log.debug("getting MsUnit instance with id: " + id);
        try {
            MsUnit instance = (MsUnit) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsUnit", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsUnit instance) throws VONEAppException{
        log.debug("finding MsUnit instance by example");
        Session session = getCurrentSession();
        try {
            List results = session
                    .createCriteria("com.vone.medisafe.mapping.MsUnit")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        } 
    }    
    
    public MsUnit merge(MsUnit detachedInstance) {
        log.debug("merging MsUnit instance");
        try {
            MsUnit result = (MsUnit) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsUnit instance) {
        log.debug("attaching dirty MsUnit instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsUnit instance) {
        log.debug("attaching clean MsUnit instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public MsUnit getUnitByCode(String code) throws VONEAppException{
    	MsUnit unit = null;
    	Session session = null;
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsUnit.class);
			crit.add(Restrictions.eq("VUnitCode", code));
			unit = (MsUnit)crit.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return unit;
    }
    
    public boolean deleteUnitById(Integer id) throws VONEAppException{
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsUnit where NUnitId=:unitId";
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setInteger("unitId", id.intValue());
			int hasil = query.executeUpdate();
			if(hasil > 0)sukses = true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return sukses;
    }

	public List<MsUnit> getAllUnitList() throws HibernateException, VONEAppException {
		List<MsUnit> unitList = null;
    	
    	Query q = getCurrentSession().createQuery("from MsUnit");
    	
    	unitList = q.list();
		
    	return unitList;
	}

}
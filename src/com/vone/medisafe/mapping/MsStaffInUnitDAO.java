package com.vone.medisafe.mapping;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsStaffInUnit.
 * @see com.vone.medisafe.mapping.MsStaffInUnit
 * @author MyEclipse - Hibernate Tools
 */
public class MsStaffInUnitDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsStaffInUnitDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public void save(MsStaffInUnit transientInstance) {
        log.debug("saving MsStaffInUnit instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    public List getAllStaffInUnit(Class clasz){
    	return super.getHibernateTemplate().find(" from "+clasz.getName());
    }
    
	public void delete(MsStaffInUnit persistentInstance) {
        log.debug("deleting MsStaffInUnit instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsStaffInUnit findById( com.vone.medisafe.mapping.MsStaffInUnitId id) {
        log.debug("getting MsStaffInUnit instance with id: " + id);
        try {
            MsStaffInUnit instance = (MsStaffInUnit) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsStaffInUnit", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsStaffInUnit instance) {
        log.debug("finding MsStaffInUnit instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsStaffInUnit")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsStaffInUnit merge(MsStaffInUnit detachedInstance) {
        log.debug("merging MsStaffInUnit instance");
        try {
            MsStaffInUnit result = (MsStaffInUnit) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsStaffInUnit instance) {
        log.debug("attaching dirty MsStaffInUnit instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsStaffInUnit instance) {
        log.debug("attaching clean MsStaffInUnit instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public List getStaffByUnitId(Integer iUnit)throws VONEAppException{
    	
    	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();//super.getSessionFactory().openSession();
    	List result = null;
    	
    	StringBuffer stQuery = new StringBuffer();
    	stQuery.append("select {staffunit.*} from ms_staff_in_unit staffunit where n_unit_id = :iUnit");
    	
    	//System.out.println(stQuery.toString());
    	try{
    		
    		result = session.createSQLQuery(stQuery.toString())
        	.addEntity("staffunit", MsStaffInUnit.class)    	
        	.setInteger("iUnit",iUnit.intValue())    	
        	.list();
    		
    	}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
    	}
    	
    	
    	return result;
    }
    
    
    public MsStaffInUnit getMsStaffInUnitByStaffId(Integer staffId){
    	Session session = super.getSessionFactory().openSession();
    	MsStaffInUnit staffInUnit = null;
    	String query = "select {staffunit.*} from ms_staff_in_unit staffunit where n_staff_id = :staffId";
    	try{
    		
    		staffInUnit = (MsStaffInUnit)session.createSQLQuery(query)
			.addEntity("staffunit", MsStaffInUnit.class)
			.setInteger("staffId",staffId.intValue())
			.uniqueResult();
    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally{
    		if(session != null) session.close();
    	}
    	 
    	return staffInUnit;
    }
    
    public List getMsStaffInUnitListByStaffId(Integer staffId){
    	Session session = super.getSessionFactory().openSession();
    	List list = null;
    	
    	String query = "select {staffunit.*} from ms_staff_in_unit staffunit where n_staff_id = :staffId";
    	try{
    		
    		list = session.createSQLQuery(query)
			.addEntity("staffunit", MsStaffInUnit.class)
			.setInteger("staffId",staffId.intValue())
			.list();
    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally{
    		if(session != null) session.close();
    	}
    	 
    	return list;
    }    
    
    public void delteStaffInUnitByStaffId(Integer id){
    	Session session = null;
    	String hql = "delete from MsStaffInUnit where n_staff_id=:staffId";
    	
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("staffId",id.intValue());
			query.executeUpdate();
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
    }
    
      
}
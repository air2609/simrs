package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsRoom.
 * @see com.vone.medisafe.mapping.MsRoom
 * @author MyEclipse - Hibernate Tools
 */
public class MsRoomDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsRoomDAO.class);

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
	  
	  
	public List getAllRoom(Class clazz){
		return super.getHibernateTemplate().find(" from "+clazz.getName());
	}
    
    public void save(MsRoom transientInstance) {
        log.debug("saving MsRoom instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsRoom persistentInstance) {
        log.debug("deleting MsRoom instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsRoom findById( java.lang.Integer id) {
        log.debug("getting MsRoom instance with id: " + id);
        try {
            MsRoom instance = (MsRoom) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsRoom", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsRoom instance) {
        log.debug("finding MsRoom instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsRoom")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsRoom merge(MsRoom detachedInstance) {
        log.debug("merging MsRoom instance");
        try {
            MsRoom result = (MsRoom) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsRoom instance) {
        log.debug("attaching dirty MsRoom instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsRoom instance) {
        log.debug("attaching clean MsRoom instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public List<MsRoom> getRoomByHall(MsHall hall) throws VONEAppException{
    	List<MsRoom> result = null;
    	
    		
		Criteria criteria = getCurrentSession().createCriteria(MsRoom.class);
		criteria.add(Restrictions.eq("msHall", hall));
		
		result = criteria.list();
		
		
    	return result;
    }
    
    public boolean deleteById(Integer id){
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsRoom where NRoomId=:tclassId";
    	try {
			session = super.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger("tclassId", id.intValue());
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
    
    public List<MsRoom> searchRoomByName(String name){
    	List<MsRoom> result = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(MsRoom.class);
			criteria.add(Restrictions.like("VRoomName","%"+name+"%"));
			result = criteria.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return result;
    }
    
    public MsRoom getRoomByName(String name){
    	MsRoom result = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(MsRoom.class);
			criteria.add(Restrictions.eq("VRoomName",name));
			result = (MsRoom)criteria.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    
    public List getListRoomByName(String name){
    	List result = null;
    	Session session = null;
    	try {
			session = super.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(MsRoom.class);
			criteria.add(Restrictions.eq("VRoomName",name));
			result = criteria.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }

	public List getDuplicateRoom() {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		try{
			session = super.getSessionFactory().getCurrentSession();
			sb.append("select count(1) as jumlah, v_room_name as room ");
			sb.append(" from ms_room group by room having count(1) > 1 ");
			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.addScalar("jumlah", Hibernate.INTEGER);
			query.addScalar("room", Hibernate.STRING);
			
			return query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsHall.
 * @see com.vone.medisafe.mapping.MsHall
 * @author MyEclipse - Hibernate Tools
 */
public class MsHallDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsHallDAO.class);

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
	
	public List getAll(Class clasz) throws VONEAppException{
		return super.getHibernateTemplate().find(" from " + clasz.getName());
	}
    
    public void save(MsHall transientInstance) throws VONEAppException{
        log.debug("saving MsHall instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
	public void delete(MsHall persistentInstance) throws VONEAppException{
        log.debug("deleting MsHall instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public MsHall findById( java.lang.Integer id) throws VONEAppException{
    	MsHall hall = null;
        
        try {
        	Session session = getCurrentSession();
			hall = (MsHall)session.createSQLQuery("select {hall.*} from ms_hall hall where hall.n_hall_id=:id")
							.addEntity("hall",MsHall.class)
							.setInteger("id",id.intValue())
							.uniqueResult();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
        
        
        return hall;
        				
   
    }
    
    
    public List findByExample(MsHall instance) throws VONEAppException{
        log.debug("finding MsHall instance by example");
        try {
            List results = getCurrentSession()
                    .createCriteria("com.vone.medisafe.mapping.MsHall")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }    
    
    public MsHall merge(MsHall detachedInstance) throws VONEAppException{
        log.debug("merging MsHall instance");
        try {
        	Session session = getCurrentSession();
            MsHall result = (MsHall) session
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }

    public void attachDirty(MsHall instance) throws VONEAppException{
        log.debug("attaching dirty MsHall instance");
        try {
        	Session session = getCurrentSession();
        	session.saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public void attachClean(MsHall instance) throws VONEAppException{
        log.debug("attaching clean MsHall instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    
    public List<MsHall> getHallByTClassId(Integer tclassId) throws VONEAppException{
    
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append(" select distinct ");
    	sql.append(" {hall.*} ");
    	
    	sql.append(" from ");
    	sql.append(" ms_hall hall, ms_room room, ms_bed bed ");
    	
    	sql.append(" where ");
    	sql.append(" hall.n_tclass_id=:id ");
    	sql.append(" and hall.n_hall_id=room.n_hall_id ");
    	sql.append(" and room.n_room_id=bed.n_room_id ");
    	sql.append(" and bed.v_bed_active_status='A'");
    		
		SQLQuery q = getCurrentSession().createSQLQuery(sql.toString());
				 q.addEntity("hall", MsHall.class);
				 q.setInteger("id", tclassId);
		
		List<MsHall> result = q.list();
			
			
    	return result;
    }
    
    public MsHall getByCode(String code)throws VONEAppException{
    	MsHall hall = null;
    	Session session = null;
    	
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsHall.class);
			crit.add(Restrictions.eq("VHallName", code));
			hall = (MsHall)crit.uniqueResult();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	
    	return hall;
    }
    
    public boolean deleteById(Integer id) throws VONEAppException{
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsHall where NHallId=:tclassId";
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setInteger("tclassId", id.intValue());
			int hasil = query.executeUpdate();
			if(hasil > 0)sukses = true;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return sukses;
    }
    
    public List searchHall(String name) throws VONEAppException{
    	List result = null;
    	Session session = null;
    	
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsHall.class);
			crit.add(Restrictions.like("VHallName", name+"%"));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return result;
    }
    
    
    
    public List<MsHall> searchHall(String hallCode, String hallName) throws VONEAppException
    {
    	List<MsHall> list = null;
    	
    	try {
			Criteria crit = getCurrentSession().createCriteria(MsHall.class);
			crit.add(Restrictions.like("VHallCode", hallCode));
			crit.add(Restrictions.like("VHallName", hallName));
			
			list = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
    	
    	
    	return list;
    	
    }


	public MsHall getHallByRoom(MsRoom room) throws VONEAppException{
		
		MsHall hall = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select ");
		sb.append(" {hall.*} ");
		
		sb.append(" from ");
		sb.append(" ms_hall hall, ");
		sb.append(" ms_room kamar ");
		
		sb.append(" where ");
		sb.append(" kamar.n_hall_id=hall.n_hall_id ");
		sb.append(" and kamar.n_room_id=:roomId ");
		
		
		try {
			hall = (MsHall)getCurrentSession().createSQLQuery(sb.toString())
				   .addEntity("hall", MsHall.class)
				   .setInteger("roomId", room.getNRoomId().intValue())
				   .uniqueResult();
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		
		return hall;
		
		
	}
    
    

}
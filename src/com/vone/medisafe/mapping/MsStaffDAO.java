package com.vone.medisafe.mapping;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;



/**
 * Data access object (DAO) for domain model class MsStaff.
 * @see com.vone.medisafe.mapping.MsStaff
 * @author MyEclipse - Hibernate Tools
 */
public class MsStaffDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsStaffDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public List getAllStaff(Class clazz) throws VONEAppException{
		return super.getHibernateTemplate().find(" from "+clazz.getName());
	}
    
    public boolean save(MsStaff staff, Set unitList) throws VONEAppException{
    	boolean sukses = false;
    	Session session = null;
    	Transaction trans = null;
    	
    	try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			
			session.saveOrUpdate(staff);
			
			Iterator it = unitList.iterator();
			while(it.hasNext()){
				Listitem item = (Listitem)it.next();
				MsUnit unit = new MsUnit();
				unit.setNUnitId((Integer)item.getValue());
				MsStaffInUnitId siui = new MsStaffInUnitId(unit,staff);
				MsStaffInUnit siu = new MsStaffInUnit(siui);
				session.save(siu);
			}
			trans.commit();
			sukses = true;
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return sukses;
    }
    
	public boolean delete(MsStaff persistentInstance) throws VONEAppException{
        log.debug("deleting MsStaff instance");
        boolean sukses = false;
        try {
            getHibernateTemplate().delete(persistentInstance);
            sukses = true;
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            sukses = false;
            throw re;
        }
        return sukses;
    }
    
    public MsStaff findById( java.lang.Integer id) throws VONEAppException{
        log.debug("getting MsStaff instance with id: " + id);
        try {
            MsStaff instance = (MsStaff) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsStaff", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsStaff instance) throws VONEAppException{
        log.debug("finding MsStaff instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsStaff")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsStaff merge(MsStaff detachedInstance) throws VONEAppException{
        log.debug("merging MsStaff instance");
        try {
            MsStaff result = (MsStaff) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsStaff instance) throws VONEAppException{
        log.debug("attaching dirty MsStaff instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsStaff instance) throws VONEAppException{
        log.debug("attaching clean MsStaff instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public List getStaffByRole(short iRole) throws VONEAppException{
    	Session session = getCurrentSession();
    	List result = null;
    	StringBuffer stQuery = new StringBuffer();
    	stQuery.append("select {staff.*} from ms_staff staff where n_staff_role = :iRole");
    	
    	try {
				result = session.createSQLQuery(stQuery.toString())
				.addEntity("staff", MsStaff.class)    	
				.setShort("iRole",iRole)
				.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
    	}
		
    	return result;
    }
    
    public MsStaff getStaffByCode(String code) throws VONEAppException{
    	MsStaff staff = null;
    	Session session = null;
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsStaff.class);
			crit.add(Restrictions.eq("VStaffCode", code));
			staff = (MsStaff)crit.uniqueResult();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return staff;
    }
    
    public List serarchStaff(String code, String name) throws VONEAppException{
    	
    	List result = null;
    	Session session = null;
    	String hql = "from MsStaff where VStaffCode like :staffCode and VStaffName like :staffName";
//    	String hql = "from MsStaff where VStaffCode like :staffCode and VStaffName like :staffName and NStaffRole=:staffRole";
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setString("staffCode", "%"+code+"%");
			query.setString("staffName", "%"+name+"%");
//			query.setShort("staffRole", MedisafeConstants.STAFF); modified by arif on feb 18, 2012
			
			result = query.list();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return result;
    }
    
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }

	


}
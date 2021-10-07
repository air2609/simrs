package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;



/**
 * Data access object (DAO) for domain model class MsDivision.
 * @see com.vone.medisafe.mapping.MsDivision
 * @author MyEclipse - Hibernate Tools
 */
public class MsDivisionDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsDivisionDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public List getAllDivision(Class clasz){
		return super.getHibernateTemplate().find(" from "+clasz.getName());
	}
    
    public void save(MsDivision transientInstance) {
        log.debug("saving MsDivision instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsDivision persistentInstance) {
        log.debug("deleting MsDivision instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsDivision findById( java.lang.Integer id) {
        log.debug("getting MsDivision instance with id: " + id);
        try {
            MsDivision instance = (MsDivision) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsDivision", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsDivision instance) {
        log.debug("finding MsDivision instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsDivision")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsDivision merge(MsDivision detachedInstance) {
        log.debug("merging MsDivision instance");
        try {
            MsDivision result = (MsDivision) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsDivision instance) throws VONEAppException{
        log.debug("attaching dirty MsDivision instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public void attachClean(MsDivision instance) throws VONEAppException{
        log.debug("attaching clean MsDivision instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
    public MsDivision getDivisionByCode(String code) throws VONEAppException{
    	MsDivision division = null;
    	Session session = null;
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsDivision.class);
			crit.add(Restrictions.eq("VDivisionCode",code));
			division = (MsDivision)crit.uniqueResult();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return division;
    }
    
    public boolean deleteById(Integer id)throws VONEAppException{
    	Session session = null;
    	boolean sukses = false;
    	String hql = "delete from MsDivision where NDivisionId=:divId";
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setInteger("divId", id.intValue());
			int hasil = query.executeUpdate();
			if(hasil > 0)sukses = true;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return sukses;
    }

}
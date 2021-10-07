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
 * Data access object (DAO) for domain model class MsTreatmentClass.
 * @see com.vone.medisafe.mapping.MsTreatmentClass
 * @author MyEclipse - Hibernate Tools
 */
public class MsTreatmentClassDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsTreatmentClassDAO.class);

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
	
	
	public List<MsTreatmentClass> getAllTreatmentClass() throws VONEAppException{

		Query q = getCurrentSession().createQuery("from MsTreatmentClass order by VTclassCode");
		
		List<MsTreatmentClass> list = q.list();
		
		return list;
	}
    
    public void save(MsTreatmentClass transientInstance) throws VONEAppException{
        log.debug("saving MsTreatmentClass instance");
        try {
        	Session session = getCurrentSession();
        	session.saveOrUpdate(transientInstance);
            log.debug("save successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
	public void delete(MsTreatmentClass persistentInstance) throws VONEAppException{
        log.debug("deleting MsTreatmentClass instance");
        try {
        	Session session = getCurrentSession();
            session.delete(persistentInstance);
            log.debug("delete successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public MsTreatmentClass findById( java.lang.Integer id) throws VONEAppException{
    	MsTreatmentClass tclass = null;
    	Session session = null;
    		try {
				session = getCurrentSession();
				Criteria criteria = session.createCriteria(MsTreatmentClass.class);
				criteria.add(Restrictions.eq("NTclassId",id));
				tclass =(MsTreatmentClass)criteria.uniqueResult();
			}catch (Exception e){
				logger.error(e.getMessage());
				throw new VONEAppException(e.getMessage());
			}
	
		
    	return tclass;
       
    }
    
    
    public List findByExample(MsTreatmentClass instance) throws VONEAppException{
        log.debug("finding MsTreatmentClass instance by example");
        try {
        	Session session = getCurrentSession();
            List results = session
                    .createCriteria("com.vone.medisafe.mapping.MsTreatmentClass")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }    
    
    public MsTreatmentClass merge(MsTreatmentClass detachedInstance) throws VONEAppException{
        log.debug("merging MsTreatmentClass instance");
        try {
        	Session session = getCurrentSession();
            MsTreatmentClass result = (MsTreatmentClass) session
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }

    public void attachDirty(MsTreatmentClass instance) throws VONEAppException{
        log.debug("attaching dirty MsTreatmentClass instance");
        try {
        	Session session = getCurrentSession();
        	session.saveOrUpdate(instance);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public void attachClean(MsTreatmentClass instance) throws VONEAppException{
        log.debug("attaching clean MsTreatmentClass instance");
        try {
        	Session session = getCurrentSession();
        	session.lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public MsTreatmentClass getByCode(String code) throws VONEAppException{
    	MsTreatmentClass tclass = null;
    	Session session = null;
    	
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsTreatmentClass.class);
			crit.add(Restrictions.eq("VTclassCode", code));
			tclass = (MsTreatmentClass)crit.uniqueResult();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	
    	return tclass;
    }
    
    public boolean deleteById(Integer id) throws VONEAppException{
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsTreatmentClass where NTclassId=:tclassId";
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

}
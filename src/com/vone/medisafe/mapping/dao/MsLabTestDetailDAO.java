package com.vone.medisafe.mapping.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;



/**
 * Data access object (DAO) for domain model class MsLabTestDetail.
 * @see com.vone.medisafe.mapping.MsLabTestDetail
 * @author MyEclipse - Hibernate Tools
 */
public class MsLabTestDetailDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsLabTestDetailDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public List getLabTestDetails() throws VONEAppException{
		Session session = getCurrentSession();
		return session.createQuery("from MsLabTestDetail order by NLabTestDetId").list();
		//return session.find(" from "+MsLabTestDetail.class.getName());
	}
    
    public boolean save(MsLabTestDetail transientInstance) throws VONEAppException{
        boolean success = false;
        try {
        	Session session = getCurrentSession();
        	session.saveOrUpdate(transientInstance);
            success = true;
            log.debug("save successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        return success;
    }
    
	public boolean delete(MsLabTestDetail persistentInstance) throws VONEAppException{
        boolean success = false;
        try {
        	Session session = getCurrentSession();
        	session.delete(persistentInstance);
            success = true;
            log.debug("delete successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        return success;
    }
    
    public MsLabTestDetail findById(Integer id) throws VONEAppException{
        log.debug("getting MsLabTestDetail instance with id: " + id);
        try {
        	Session session = getCurrentSession();
            MsLabTestDetail instance = (MsLabTestDetail) session
                    .get("com.vone.medisafe.mapping.MsLabTestDetail", id);
            return instance;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    
    public List findByExample(MsLabTestDetail instance) throws VONEAppException{
        log.debug("finding MsLabTestDetail instance by example");
        try {
        	Session session = getCurrentSession();
            List results = session
                    .createCriteria("com.vone.medisafe.mapping.MsLabTestDetail")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }    
    
    public MsLabTestDetail merge(MsLabTestDetail detachedInstance) throws VONEAppException{
        log.debug("merging MsLabTestDetail instance");
        try {
        	Session session = getCurrentSession();
            MsLabTestDetail result = (MsLabTestDetail) session
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }

    public void attachDirty(MsLabTestDetail instance) throws VONEAppException{
        log.debug("attaching dirty MsLabTestDetail instance");
        try {
        	Session session = getCurrentSession();
        	session.saveOrUpdate(instance);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public void attachClean(MsLabTestDetail instance) throws VONEAppException{
        log.debug("attaching clean MsLabTestDetail instance");
        try {
        	Session session = getCurrentSession();
        	session.lock(instance, LockMode.NONE);
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

	public boolean save(MsLabTreatmentDetil msLabTreatmentDetil) throws VONEAppException{
        boolean success = false;
        try {
        	Session session = getCurrentSession();
        	session.saveOrUpdate(msLabTreatmentDetil);
            success = true;
            log.debug("save successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        return success;
	}

	public boolean delete(MsLabTreatmentDetil msLabTreatmentDetil) throws VONEAppException{
		boolean success = false;
        try {
        	Session session = getCurrentSession();
        	session.delete(msLabTreatmentDetil);
            success = true;
            log.debug("delete successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        return success;
	}
    


}
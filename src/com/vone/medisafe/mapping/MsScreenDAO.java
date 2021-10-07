package com.vone.medisafe.mapping;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;


/**
 * Data access object (DAO) for domain model class MsScreen.
 * @see com.vone.medisafe.mapping.MsScreen
 * @author MyEclipse - Hibernate Tools
 */
public class MsScreenDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsScreenDAO.class);
    
    StringBuffer stQuery = new StringBuffer();

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
    
    public Integer getScreenIdByCode (String screenCode) throws VONEAppException { 
    	try{
    		Session session = getCurrentSession();
    		
    		stQuery.setLength(0);
    		
    		stQuery.append(" from MsScreen where VScreenCode = :code ");
    		
    		MsScreen msScreen = (MsScreen)session.createQuery(stQuery.toString())
    				.setString("code", screenCode)
    				.uniqueResult();
    		
    		return msScreen.getNScreenId();
    		
        } catch (Exception re) {
            log.error("get failed", re);
            
            throw new VONEAppException(MessagesService.getKey("error.nodata"), re.getMessage());
        }
    }

    public void update(int iScreenId, String stScreenCode, String stScreenName, int iSubsystemId) throws VONEAppException{
        log.debug("update MsScreen instance");
        Session session = null;
        
        try {
        	session = getCurrentSession();
        	
        	stQuery.setLength(0);
        	stQuery.append(" update MsScreen ")
        		.append(" set VScreenCode = :stScreenCode ")
        		.append(" , VDesc = :stScreenName ")
        		.append(" , n_subsystem_id =:iSubsystemId")
        		.append(" where NScreenId = :iScreenId");
        	        	
        	int result = session.createQuery(stQuery.toString())
        		.setString("stScreenCode", stScreenCode)
        		.setString("stScreenName", stScreenName)
        		.setInteger("iScreenId", iScreenId)
        		.setInteger("iSubsystemId", iSubsystemId)
        		.executeUpdate();
            
        	if (result > 0)
        		log.debug("update successful");
        	else {
        		log.debug("update un-successful");
        		throw new VONEAppException(MessagesService.getKey("error.savemodify.nodata"));
        	}
        } catch (Exception re) {
            log.error("update failed", re);
            throw new VONEAppException(MessagesService.getKey("error.savemodify"), re.getMessage());
        } 
    }      
    
    public void saveOnly (MsScreen transientInstance) throws VONEAppException{
        log.debug("saving MsScreen instance");
        
        try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("save failed", e);			
			
			throw new VONEAppException(MessagesService.getKey("error.saveadd"), e.getMessage());
			
		}

    }
    
    public boolean save(MsScreen screen) throws VONEAppException {
    	boolean sukses = false;
    	Session session = null;
    	Transaction trans = null;
    	
    	try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			
			session.save(screen);			
						
			Iterator it = screen.getMsScreenInUnits().iterator();
			
			while (it.hasNext()){
				MsScreenInUnit siuPojo = (MsScreenInUnit)it.next();
								
				session.save(siuPojo);
			}
			
			trans.commit();
			sukses = true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			trans.rollback();
			sukses = false;
			logger.error("SAVE",e);
			throw new VONEAppException(MessagesService.getKey("error.saveadd"), e.getMessage());
		}
    	return sukses;
    }
    
    public boolean update(MsScreen screen) throws VONEAppException {
    	boolean sukses = false;
    	Session session = null;
    	Transaction trans = null;
    	
    	try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			
			session.update(screen);
			
			stQuery.setLength(0);
			stQuery.append(" delete from MsScreenInUnit where n_screen_id = :scrId");
			session.createQuery(stQuery.toString())
				.setInteger("scrId", screen.getNScreenId().intValue())
				.executeUpdate();
						
			Iterator it = screen.getMsScreenInUnits().iterator();
			
			while (it.hasNext()){
				MsScreenInUnit siuPojo = (MsScreenInUnit)it.next();
								
				session.save(siuPojo);
			}
			
			trans.commit();
			sukses = true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			trans.rollback();
			sukses = false;
			logger.error("update",e);
			throw new VONEAppException(MessagesService.getKey("error.savemodify"), e.getMessage());
		}
    	return sukses;
    }    
    
	public void delete(MsScreen persistentInstance) throws VONEAppException {
        log.debug("deleting MsScreen instance");
        try {        	
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (Exception re) {
            log.error("delete failed", re);
            
            throw new VONEAppException(MessagesService.getKey("error.delete"), re.getMessage());
        }
    }	
	
    public MsScreen findById( java.lang.Integer id)  throws VONEAppException{
        log.debug("getting MsScreen instance with id: " + id);
        try {
            MsScreen instance = (MsScreen) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsScreen", id);
            return instance;
        } catch (Exception re) {
            log.error("get failed", re);
            
            throw new VONEAppException(MessagesService.getKey("error.nodata"), re.getMessage());
        }
    }
    
    
    public List findByExample(MsScreen instance)  throws VONEAppException{
        log.debug("finding MsScreen instance by example");
        Session session = getCurrentSession();
        try {
            List results = session
                    .createCriteria("com.vone.medisafe.mapping.MsScreen")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception re) {
            log.error("find by example failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), re.getMessage());
        } 
    }    
    
    public List searchByCriteria(String screenCode, String screenName)  throws VONEAppException{
    	
    	log.debug("search MsScreen by Criteria");
    	
    	if (screenCode == null) screenCode = "";
    	if (screenName == null) screenName = "";    	    

    	Session session = getCurrentSession();    	
    	    	    	
    	List list = null;
		try {
	    	Criteria crit = session.createCriteria(MsScreen.class);
	    	crit.add(Restrictions.ilike("VScreenCode", "%"+screenCode+"%"));
	    	crit.add(Restrictions.ilike("VDesc", "%"+screenName+"%"));
	    	list = crit.list();
	    	
		} catch (Exception e) {
			log.error("search by Criteria Failed", e);
			// TODO Auto-generated catch block
			throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());
		} 
    	return list;
    }

    public List searchByCriteriaOr(String screenCode, String screenName)  throws VONEAppException{
    	
    	log.debug("search MsScreen by Criteria");
    	
    	if (screenCode == null) screenCode = "";
    	if (screenName == null) screenName = "";    	    

    	Session session = getCurrentSession();    	
    	    	    	
    	List list = null;
		try {
			list = session.createQuery("from MsScreen where VScreenCode like :screenCode or " +
					"VDesc like :screenName")
					.setString("screenCode", "%"+screenCode+"%")
					.setString("screenName", "%"+screenName+"%").list();
			
		} catch (Exception e) {
			log.error("search by Criteria Failed", e);
			// TODO Auto-generated catch block
			throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());
		} 
    	return list;
    }
    
    public String[] getScreenCodeName (int iScreenId) throws VONEAppException{

    	StringBuffer stQuery = new StringBuffer();
    	
    	List list = null;
    	
    	Session session = null;
		try {
			session = getCurrentSession();			
			
			stQuery.append("from MsScreen scr where scr.NScreenId = :iScreenId ");    			
			
			list = session.createQuery(stQuery.toString())    	
			.setInteger("iScreenId", iScreenId)
			.list();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new VONEAppException(e.getMessage());
		} 
    	
		if (list != null)
    	for (int i=0; i<list.size(); i++){
    		if (list.get(i) instanceof MsScreen) {
    			MsScreen screenPojo = (MsScreen)list.get(i);
    			
    			return new String[]{screenPojo.getVScreenCode(),screenPojo.getVDesc()};
    		}
    	}
    	
    	return new String[]{"",""};
    }
   
    public MsScreen getScreenByCode (String code) throws VONEAppException{
    	
    	Session session = null;
    	MsScreen screen = null;
    	
    	try{
    		session = getCurrentSession();
    		
			Criteria crit = session.createCriteria(MsScreen.class);
			crit.add(Restrictions.eq("VScreenCode", code));
			screen = (MsScreen)crit.uniqueResult();
    		
    	}catch(Exception e){        
        	logger.error("getScreenByCode failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    	
    	return screen;
    }
}
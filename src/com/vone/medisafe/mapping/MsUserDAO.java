package com.vone.medisafe.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;


/**
 * Data access object (DAO) for domain model class MsUser.
 * @see com.vone.medisafe.mapping.MsUser
 * @author MyEclipse - Hibernate Tools
 */
public class MsUserDAO extends HibernateDaoSupport {

	private StringBuffer stQuery = new StringBuffer();
	
    private static final Log log = LogFactory.getLog(MsUserDAO.class);

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
    
    public void save(MsUser transientInstance) throws VONEAppException{
        log.debug("saving MsUser instance");
        
        try {      	   
        	
            getHibernateTemplate().save(transientInstance);
            
            log.debug("save successful");
        } catch (Exception re) {
        	
            log.error("save failed", re);
            
            throw new VONEAppException(MessagesService.getKey("error.saveadd"),re);
         
        }
    }

    public void update(MsUser transientInstance) throws VONEAppException {
        log.debug("updating MsUser instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (Exception re) {
            log.error("update failed", re);
            
            throw new VONEAppException(MessagesService.getKey("error.savemodify"));
        }
    }    
    
	public void delete(MsUser persistentInstance) throws VONEAppException{
        log.debug("deleting MsUser instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (Exception re) {
            log.error("delete failed", re);
            throw new VONEAppException(MessagesService.getKey("error.delete"));
        }
    }
	
    public MsUser findById( java.lang.Integer id) throws VONEAppException {
        log.debug("getting MsUser instance with id: " + id);
        
        Session session = null;
        	
        try {
        	session = getCurrentSession();
        	
        	Criteria crit = session.createCriteria(MsUser.class);        	
        	crit.add(Restrictions.eq("NUserId",id));        	   
        	
        	if (crit.uniqueResult() != null)
        		return (MsUser)crit.uniqueResult();
        	        	
            return null; 
        	
        } catch (Exception re) {
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        } 
    }
    
    
    public List findByExample(MsUser instance) throws VONEAppException {
        log.debug("finding MsUser instance by example");
        
        Session session = getCurrentSession();
        try {
            List results = session
                    .createCriteria(MsUser.class)
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception re) {
            log.error("find by example failed", re);            
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
        } 
    }    
    

    
    /**
     * will return List of 3 Integer Object
     * Object[0] : Module Id
     * Object[1] : Screen Id
     * Object[2] : Access Type Id
     * note : redundant data is possible as User Privilege is superior to Group Privilege
     * Privileges will be listed from User Privileges to Group Privileges, read array method
     * should be from zero up.
     * @param stUserName
     * @return
     */
    public List getPrivileges (String stUserName)throws VONEAppException{
    	
    	List listPrivilege = new ArrayList();
    	
    	Session session = null;
    	
    	stQuery.setLength(0);
    	    	
    	stQuery.append("from MsUser usr where usr.VUserName = :stUserName ");    			
    	
    	try {
    		session = getCurrentSession();
    		
			MsUser userPojo = (MsUser)session.createQuery(stQuery.toString())    	
			.setString("stUserName", stUserName)
			.uniqueResult();					
			
			//User Privileges
			Set setUserPriv = userPojo.getTbUserPrivileges();					
			
			Iterator it = setUserPriv.iterator();
			
			while (it.hasNext()){
				TbUserPrivilege userPrivPojo = (TbUserPrivilege)it.next();
				Object[] aPriv = new Object[3];
				
				//set Module Id
				aPriv[0] = userPrivPojo.getId().getMsScreen().getMsSubsystem().getNSubsystemId();

				//set Screen Id
				aPriv[1] = userPrivPojo.getId().getMsScreen().getNScreenId();
				
				//set Access Type Id
				aPriv[2] = userPrivPojo.getVAccessType();
				
				listPrivilege.add(aPriv);
				
			}
			
			//Group Privileges
			Set setGroupPriv = userPojo.getMsGroup().getTbGroupPrivileges();
			
			it = setGroupPriv.iterator();
			
			while (it.hasNext()){
				TbGroupPrivilege grpPrvPojo = (TbGroupPrivilege)it.next();
				Object[] aPriv = new Object[3];
				
				//set Module Id
				aPriv[0] = grpPrvPojo.getId().getMsScreen().getMsSubsystem().getNSubsystemId();
				
				//set Screen Id
				aPriv[1] = grpPrvPojo.getId().getMsScreen().getNScreenId();
				
				//set Access Type Id
				aPriv[2] = grpPrvPojo.getVAccessType();
				
				listPrivilege.add(aPriv);
						
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getPrivilege() error", e);
			throw new VONEAppException(MessagesService.getKey("error.internal"));
		} 
    	return listPrivilege;
    }            
    
    /**
     * will return Object[2] consist of n_screen_id, n_unit_id and v_screen_code
     * @return
     * @throws VONEAppException
     */
    public List getScreenToUnit (String stUserName) throws VONEAppException{

    	    	
    	List list = null;
    	
    	
    	stQuery.setLength(0);
    	    	
    	stQuery.append(" SELECT SCR.N_SCREEN_ID AS N_SCREEN_ID, UNT.N_UNIT_ID AS N_UNIT_ID, ")
    		.append(" SCR.V_SCREEN_CODE AS V_SCREEN_CODE, UNT.V_UNIT_CODE AS V_UNIT_CODE, UNT.V_UNIT_NAME AS V_UNIT_NAME")
    		.append(" FROM (")
    		.append(" SELECT DISTINCT SCRUNIT.N_SCREEN_ID AS N_SCREEN_ID, STFUNIT.N_UNIT_ID AS N_UNIT_ID")
    		.append(" FROM MS_SCREEN_IN_UNIT AS SCRUNIT,")
    		.append(" (")
    		.append(" SELECT STFUNIT.* ")
    		.append(" FROM MS_STAFF_IN_UNIT STFUNIT, MS_USER USR")
    		.append(" WHERE STFUNIT.N_STAFF_ID = USR.N_STAFF_ID")
    		.append(" AND USR.V_USER_NAME = :stUserName) STFUNIT")
    		.append(" WHERE SCRUNIT.N_UNIT_ID = STFUNIT.N_UNIT_ID) AS RES,")
    		.append(" MS_SCREEN AS SCR, MS_UNIT AS UNT")
    		.append(" where RES.N_SCREEN_ID = SCR.N_SCREEN_ID")
    		.append(" AND RES.N_UNIT_ID = UNT.N_UNIT_ID");
    	
    	try{
    		

    		list =  getCurrentSession().createSQLQuery(stQuery.toString())
    			.addScalar("N_SCREEN_ID", Hibernate.INTEGER)
    			.addScalar("N_UNIT_ID", Hibernate.INTEGER)
    			.addScalar("V_SCREEN_CODE", Hibernate.STRING)
    			.addScalar("V_UNIT_CODE", Hibernate.STRING)
    			.addScalar("V_UNIT_NAME", Hibernate.STRING)
    			.setString("stUserName", stUserName)
    			.list();
    		    		
    	}catch (Exception e){
    		logger.error("getScreenInUnit",e);
    		throw new VONEAppException(MessagesService.getKey("error.internal"));
    	}
    	
    	return list;
    }    
    
    public MsUser getMsUser (String stUserName) throws VONEAppException{
    	
    	stQuery.setLength(0);
    	
    	stQuery.append("from MsUser usr where usr.VUserName = :stUserName and usr.msStaff.DStaffFiredDate is null");
    	
    	Session session = getCurrentSession();
    	
    	MsUser userPojo = null;
    	
		try {
			userPojo = (MsUser)session.createQuery(stQuery.toString())
			.setString("stUserName", stUserName)
			.uniqueResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getMsUser", e);
			throw new VONEAppException(MessagesService.getKey("error.nodata"));
		} 
		
    	return userPojo;
    }

	public List<MsUser> searchUser(String input) throws VONEAppException{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" {usr.*} ");
		
		sql.append(" from ");
		sql.append(" ms_user usr, ");
		sql.append(" ms_group grup, ");
		sql.append(" ms_staff staff ");
		
		sql.append(" where ");
		sql.append(" usr.n_group_id=grup.n_group_id ");
		sql.append(" and usr.n_staff_id=staff.n_staff_id ");
		sql.append(" and (usr.v_user_name like :inputName ");
		sql.append(" or grup.v_group_name like :inputGrup ");
		sql.append(" or staff.v_staff_code like :inputCode) ");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity("usr", MsUser.class);
		
		query.setString("inputName", input);
		query.setString("inputGrup", input);
		query.setString("inputCode", input);
		
		List<MsUser> userList = query.list();
		
		return userList;
	}
    
}
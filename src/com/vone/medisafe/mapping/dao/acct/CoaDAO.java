package com.vone.medisafe.mapping.dao.acct;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsStaff;

public class CoaDAO extends HibernateDaoSupport{

	StringBuffer stQuery = new StringBuffer();
	Logger log = Logger.getLogger(CoaDAO.class);
	
	public void initDao(){
		
	}
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public List getCoaHeader() throws VONEAppException{
		Session session = null;
		List list = null;
		
		try{
			stQuery.setLength(0);
			stQuery.append("from MsCoa ")
			.append(" where n_sup_coa_id is NULL")
			.append(" order by v_acct_no");
			
			session = getCurrentSession();
			
			list = session.createQuery(stQuery.toString()).list();				
			
        } catch (Exception re) {        	
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
        
		return list;
	}
	
	public List getCoaType() throws VONEAppException{
		Session session = null;
		List list = null;
		
		try{
			stQuery.setLength(0);
			stQuery.append("from MsCoaType ")
			.append(" order by n_ct_id");
			
			session = getCurrentSession();
			
			list = session.createQuery(stQuery.toString()).list();				
			
        } catch (Exception re) {        	
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
        
		return list;
	}	
	
	public List getCoaHeader(int status) throws VONEAppException{
		Session session = null;
		List list = null;
		
		try{					
			stQuery.setLength(0);
			stQuery.append("from MsCoa ")
			.append(" where n_sup_coa_id is NULL")
			.append(" and n_status =:status")
			.append(" order by v_acct_no");
			
			session = getCurrentSession();
			
			list = session.createQuery(stQuery.toString())
				.setInteger("status", status)
				.list();
			
        } catch (Exception re) {        	
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
        
		return list;
	}
	
	public List getCoaChild(MsCoa headPojo) throws VONEAppException {
		Session session = null;
		List list = null;
		
		try{
					
			stQuery.setLength(0);
			stQuery.append("from MsCoa ")
			.append(" where msCoa = :headPojo")
			.append(" order by v_acct_no");
			
			session = getCurrentSession();
			
			list = session.createQuery(stQuery.toString())
					.setEntity("headPojo", headPojo)
					.list();
			
        } catch (Exception re) {
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
        
		return list;		
	}
	
	public List getCoaChild(MsCoa headPojo, int status) throws VONEAppException {
		Session session = null;
		List list = null;
		
		try{
			stQuery.setLength(0);
			stQuery.append("from MsCoa ")
			.append(" where msCoa = :headPojo")
			.append(" and n_status =:status")
			.append(" order by v_acct_no");
			
			session = getCurrentSession();
			
			list = session.createQuery(stQuery.toString())
					.setEntity("headPojo", headPojo)
					.setInteger("status", status)
					.list();
			
        } catch (Exception re) {
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		return list;		
	}
	
	public MsCoa getCoaByCode(String code) throws VONEAppException {
		Session session = null;
		MsCoa coa = null;
		
		try{
			stQuery.setLength(0);
			stQuery.append("from MsCoa ")
			.append(" where v_acct_no = :code");
			
			session = getCurrentSession();
			
			coa = (MsCoa)session.createQuery(stQuery.toString())					
					.setString("code", code)
					.uniqueResult();
			
        } catch (Exception re) {
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		return coa;		
	}
	
	public List getCoaBaseOnType(int type) throws VONEAppException{
		List list = null;
		
		Session session = null;
		
		String hql = "";
		
		if (type == MedisafeConstants.COA_ALL)
			hql = "select {coa.*} from ms_coa coa order by v_acct_no ";
		else
			hql = "select {coa.*} from ms_coa coa where coa.n_type=:coaType order by v_acct_no ";
		
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(hql);
			query.addEntity("coa",MsCoa.class);
			if (type != MedisafeConstants.COA_ALL)
				query.setShort("coaType", (short)type);
			list = query.list();			  
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List getCoaBaseOnTypeNoChild(int type) throws VONEAppException{
		List list = null;
		
		Session session = null;
		
		String hql = "";
		
		if (type == MedisafeConstants.COA_ALL)
			hql = "select {coa.*} from ms_coa coa where n_sup_coa_id is NULL";
		else
			hql = "select {coa.*} from ms_coa coa where coa.n_type=:coaType and n_sup_coa_id is NULL";
		
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(hql);
			query.addEntity("coa",MsCoa.class);
			if (type != MedisafeConstants.COA_ALL)
				query.setShort("coaType", (short)type);
			list = query.list();			  
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public void save(MsCoa pojo) throws VONEAppException{
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
	}
	
	public void update(MsCoa pojo) throws VONEAppException{
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
	}
	
	public void delete(MsCoa persistentInstance) throws VONEAppException {
        log.debug("deleting MsCoa instance");
        try {        	
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (Exception re) {
            log.error("delete failed", re);
            
            throw new VONEAppException(MessagesService.getKey("error.delete"), re.getMessage());
        }
    }	
	
    public MsCoa findByExample(MsCoa instance) throws VONEAppException{
        logger.debug("finding MsCoa instance by example");
        
        Session session = null;
                
        try {
        	session = getCurrentSession();
        	
            MsCoa results = (MsCoa)session
                    .createCriteria(MsCoa.class)
                    .add(Example.create(instance))
                    .uniqueResult();
            
            return results;
        } catch (Exception re) {
        	re.printStackTrace();
        	logger.error("find by example failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), re.getMessage());
        } 
    }

	public MsStaff getMsStaff(MsStaff staff) throws VONEAppException{
		Session session = getCurrentSession();
		MsStaff stf = (MsStaff) session.get(MsStaff.class, staff.getNStaffId());
		getHibernateTemplate().initialize(stf.getMsCoa());
		return stf;
	}

	public List<MsCoa> getCoaByCodeAndName(String coaCode, String coaName) throws VONEAppException {
		
		List<MsCoa> coalist = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {coa.*} ");
		
		sql.append(" from ");
		sql.append(" ms_coa coa ");
		
		sql.append(" where ");
		if(coaCode.equals("%%"))
			sql.append(" coa.v_acct_name like :coaName ");
		else sql.append(" coa.v_acct_no like :code ");
		
		sql.append(" limit 100 ");
//		sql.append(" or coa.v_acct_name like :coaName ");
		
		System.out.println(sql.toString());
		SQLQuery crit = getCurrentSession().createSQLQuery(sql.toString());
		crit.addEntity("coa", MsCoa.class);
		
		if(coaCode.equals("%%"))
			crit.setString("coaName", coaName);
		else{
			System.out.println("ini dieksekusi");
			crit.setString("code", coaCode);
		}
			
		
//		crit.setFirstResult(1);
//		crit.setMaxResults(100);
		
//		Criteria crit = getCurrentSession().createCriteria(MsCoa.class);
//		
//		crit.setFirstResult(1);
//		crit.setMaxResults(100);
//		
//		crit.add(Restrictions.like("VAcctNo", coaCode));
//		crit.add(Restrictions.like("VAcctName", coaName));
//		
		coalist = crit.list();
		
		
		
		return coalist;
	}        
}

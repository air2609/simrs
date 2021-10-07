package com.vone.medisafe.mapping.dao.item;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.pojo.item.MsVendor;


public class VendorDAO extends HibernateDaoSupport{
	
	private static final Logger log = Logger.getLogger(VendorDAO.class);
	
	protected void initDao(){
		
	}
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public List getAllVendor() throws VONEAppException{
		List itemGroups = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsVendor.class);
			itemGroups = crit.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			log.error("terjadi exception "+ e);
			
			throw new VONEAppException(e.getMessage());
		}
		return itemGroups;
	}
	
	public List searchVendor(String code, String name) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			session = getCurrentSession();
			
			StringBuffer stQuery = new StringBuffer();
			stQuery.append("from MsVendor where VVendorCode like :code ")
				.append(" AND VVendorName like :name");
			
			list = session.createQuery(stQuery.toString())			
									.setString("code", code)
									.setString("name", name)
									.list();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return list;
	}		
	
	public List searchVendorByCriteria(String crit) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			session = getCurrentSession();
			
			StringBuffer stQuery = new StringBuffer();
			stQuery.append("from MsVendor where VVendorCode like :crit ")
				.append(" OR VVendorName like :crit");
			
			list = session.createQuery(stQuery.toString())			
									.setString("crit", "%"+crit+"%")
									.setString("crit", "%"+crit+"%")
									.list();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return list;
	}	
	
	public MsVendor getVendorByCode (String code) throws VONEAppException{
		Session session = null;
		MsVendor vendor;
		
		try{
			session = getCurrentSession();
			
			StringBuffer stQuery = new StringBuffer();
			stQuery.append("from MsVendor where VVendorCode = :code ");
			
			vendor = (MsVendor)session.createQuery(stQuery.toString())			
									.setString("code", code)
									.uniqueResult();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		return vendor;		
	}
	
	public MsVendor getVendorById (Integer code) throws VONEAppException{
		Session session = null;
		MsVendor vendor;
		
		try{
			session = getCurrentSession();
			
			StringBuffer stQuery = new StringBuffer();
			stQuery.append("from MsVendor where NVendorId = :code ");
			
			vendor = (MsVendor)session.createQuery(stQuery.toString())			
									.setInteger("code", code)
									.uniqueResult();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		return vendor;		
	}
	
	public boolean save(MsVendor transientInstance) throws VONEAppException{
        
        boolean success = false;
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            success = true;
            log.debug("save successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        return success;
    }
	
	public boolean delete(MsVendor itemGroup) throws VONEAppException{
		boolean success = false;
		try {
            getHibernateTemplate().delete(itemGroup);
            success = true;
            log.debug("delete successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
		return success;
	}
	
	
}

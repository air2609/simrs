package com.vone.medisafe.mapping;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;


public class TbPurchaseRequestDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbPurchaseRequestDAO.class);
    
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
    
	public void save(TbPurchaseRequest pojo) throws VONEAppException{

		Session session = null;
		Transaction trx = null;
		
		try {
			session = getCurrentSession();		
			
			session.save(pojo);
						
			Iterator it = pojo.getTbPurchaseRequestDetails().iterator();			
			
			while (it.hasNext()){
				
				TbPurchaseRequestDetail det = (TbPurchaseRequestDetail)it.next();
				
				det.setTbPurchaseRequest(pojo);
				
				session.save(det);
			}
			
		
		}catch (Exception e){
			logger.error("Save Error", e);

			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}
	}    
    
	public void update (TbPurchaseRequest pojo) throws VONEAppException {
		Session session = null;
		Transaction trx = null;
		
		try {			
			
			session = getCurrentSession();
			trx = session.beginTransaction();		
			
    		//delete previous detail
    		stQuery.setLength(0);
    		stQuery.append(" delete from TbPurchaseRequestDetail ")
    			.append(" where n_pr_id = :prId");
    		
    		int res = session.createQuery(stQuery.toString())
    				.setInteger("prId", pojo.getNPrId().intValue())
    				.executeUpdate();
    		
    		//update TbPurchaseOrder
    		session.update(pojo);
    		
//    		set children
    		
			Iterator it = pojo.getTbPurchaseRequestDetails().iterator();
			
			while (it.hasNext()){
				
				TbPurchaseRequestDetail det = (TbPurchaseRequestDetail)it.next();
				
				session.save(det);
			}
			
			trx.commit();
		
		}catch (Exception e){
			logger.error("Update Error", e);
			trx.rollback();
			throw new VONEAppException(MessagesService.getKey("error.savemodify"));
		}	
	}
	
	public void updatePojoOnly (TbPurchaseRequest pojo) throws VONEAppException {
				
		try {
			
			getHibernateTemplate().update(pojo);
						
		}catch (Exception e){
			logger.error("Update Error", e);
			throw new VONEAppException(MessagesService.getKey("error.savemodify"));
		}		
	}	
	
	public TbPurchaseRequest getTbPurchaseRequestByCode (String code) throws VONEAppException {

		Session session = null;
		TbPurchaseRequest obj;
		
		try{
			session = getCurrentSession();
			
			obj = (TbPurchaseRequest)session.createQuery("from TbPurchaseRequest where VPrCode = :code")
									.setString("code", code)
									.uniqueResult();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}	
		
		return obj;
	}
	
	public List searchTbPurchaseRequestByCode (String code) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			session = getCurrentSession();
			
			list = session.createQuery("from TbPurchaseRequest where VPrCode like :code")
									.setString("code", code)
									.list();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		return list;
	}	
	
	public List searchTbPurchaseRequestByCode(String code, String status) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			session = getCurrentSession();
			
			StringBuffer stQuery = new StringBuffer();
			stQuery.append("from TbPurchaseRequest where VPrCode like :code ")
				.append(" and VPrStatus = :status");
			
			list = session.createQuery(stQuery.toString())			
									.setString("code", code)
									.setString("status", status)
									.list();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}	
		
		return list;
	}		
	
	public List searchTbPurchaseRequestByCodeForApproval (String code) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			session = getCurrentSession();
			
			StringBuffer stQuery = new StringBuffer();
			stQuery.append("from TbPurchaseRequest where VPrCode like :code ")
				.append(" and VPrStatus = :status");
			
			list = session.createQuery(stQuery.toString())			
									.setString("code", code)
									.setString("status", MedisafeConstants.PURCHASING_OPEN)
									.list();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}	
		
		return list;
	}	
}
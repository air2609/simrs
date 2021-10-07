package com.vone.medisafe.ui.purchasing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsBranch;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.TbPurchaseRequestDetail;
import com.vone.medisafe.mapping.pojo.item.MsVendor;

/**
 * Data access object (DAO) for domain model class MsBranch.
 * @see com.vone.medisafe.mapping.MsBranch
 * @author MyEclipse - Hibernate Tools
 */
public class TbPurchaseOrderDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbPurchaseOrderDAO.class);
    
    StringBuffer stQuery = new StringBuffer();
    
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }

    public Integer getQtyOrderArrived(Integer prId, Integer itemId) throws VONEAppException{
    	Session session = null;
    	Integer result = null;
    	
		try{
			session = getCurrentSession();
			
			stQuery.setLength(0);
			stQuery.append(" select sum(pod.n_po_det_qty_ordered) as qty_ordered from tb_purchase_order_detail pod, tb_purchase_order po")
				.append(" where ")
				.append(" pod.n_po_id = po.n_po_id")
				.append(" and")
				.append(" po.n_pr_id= :prId ")
				.append(" and ")
				.append(" pod.n_item_id= :itemId ")
				.append(" and ")
				.append(" (po.v_po_status =:stat1 or po.v_po_status = :stat2) ");
			
			Object obj = session.createSQLQuery(stQuery.toString())
									.addScalar("qty_ordered", Hibernate.INTEGER)
									.setInteger("prId", prId.intValue())
									.setInteger("itemId", itemId.intValue())
									.setString("stat1", MedisafeConstants.PURCHASING_OPEN)
									.setString("stat2", MedisafeConstants.PURCHASING_APPROVED)
									.uniqueResult();
			
			if (obj == null) result = new Integer(0);
			else
				result = (Integer)obj;
			
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return result;
    }
    
    public TbPurchaseOrder getPOByCode(String code) throws VONEAppException {
		Session session = null;
		TbPurchaseOrder obj;
		
		try{
			session = getCurrentSession();
			
			obj = (TbPurchaseOrder)session.createQuery("from TbPurchaseOrder where VPoCode = :code")
									.setString("code", code)
									.uniqueResult();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return obj;  	
    }
    
    public void updateHeaderOnly (TbPurchaseOrder pojo) throws VONEAppException {
		Session session = null;	
		
		try {
			session = getCurrentSession();		
			
			session.update(pojo);
						
		}catch (Exception e){
			logger.error("Update Error", e);

			throw new VONEAppException(MessagesService.getKey("error.savemodify"));
		}
    }    
    
    public void save (TbPurchaseOrder pojo) throws VONEAppException {
    	Session session = null;
    	try{
    		session = getCurrentSession();
    		
    		session.save(pojo);    		
    		
    		Iterator it = pojo.getTbPurchaseOrderDetails().iterator();
    		
    		while (it.hasNext()){
    			TbPurchaseOrderDetail tbPOD = (TbPurchaseOrderDetail)it.next();
    			
    			session.save(tbPOD);
    		}
    		
		}catch (Exception e){
			
			logger.error("Save Error", e);
			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}
    }
    
    public void update (TbPurchaseOrder pojo) throws VONEAppException {
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		    		
    		//delete previous detail
    		stQuery.setLength(0);
    		stQuery.append(" delete from TbPurchaseOrderDetail ")
    			.append(" where n_po_id = :poId");
    		
    		int res = session.createQuery(stQuery.toString())
    				.setInteger("poId", pojo.getNPoId().intValue())
    				.executeUpdate();
    		
    		//update TbPurchaseOrder
    		session.update(pojo);
    		
    		//set children
    		
    		Iterator it = pojo.getTbPurchaseOrderDetails().iterator();
    		
    		while (it.hasNext()){
    			TbPurchaseOrderDetail tbPOD = (TbPurchaseOrderDetail)it.next();
    			
    			session.save(tbPOD);
    		}
    		
		}catch (Exception e){
			logger.error("Save Error", e);
			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}	
    }
    
	public List searchPOByCodeSup(String poCode, String supCode) throws VONEAppException {

		Session session = null;
		List list;
		
		if (poCode == null)
			poCode = "%%";
		
		if (supCode == null)
			supCode = "%%";
		
		try{
			session = getCurrentSession();	
			
			stQuery.setLength(0);
			stQuery.append("select {po.*} from tb_purchase_order po, ms_vendor ven ")
				.append(" where po.n_vendor_id = ven.n_vendor_id")
				.append(" and ven.v_vendor_name like :supCode")
				.append(" and po.v_po_code like :poCode")
				.append(" and po.v_po_status != :status")
				.append(" order by po.d_whn_create desc");
			
			list = session.createSQLQuery(stQuery.toString())
									.addEntity("po", TbPurchaseOrder.class)
									.setString("supCode", supCode)
									.setString("poCode", poCode)
									.setString("status", MedisafeConstants.PURCHASING_CLOSED)
									.list();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return list;
	}
    
	public List searchPOByCodeStatus(String code, String status) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			
			session = getCurrentSession();
			
			stQuery.setLength(0);
			stQuery.append("from TbPurchaseOrder where VPoCode like :code ")
				.append(" and VPoStatus = :status")
				.append(" order by d_whn_create desc");
			
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
	
	public List searchPOByCodeStatus(String code, String status1, String status2) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			
			session = getCurrentSession();
			
			stQuery.setLength(0);
			stQuery.append("from TbPurchaseOrder where VPoCode like :code ")
				.append(" and (VPoStatus = :status1 or VPoStatus = :status2)")
				.append(" order by d_whn_create desc");
			
			list = session.createQuery(stQuery.toString())			
									.setString("code", code)
									.setString("status1", status1)
									.setString("status2", status2)
									.list();
						
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return list;
	}			
	
	public List searchPOByCodeStatus(String code, String status1, String status2, String status3) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			
			session = getCurrentSession();
			
			stQuery.setLength(0);
			stQuery.append("from TbPurchaseOrder where VPoCode like :code ")
				.append(" and (VPoStatus = :status1 or VPoStatus = :status2 or VPoStatus = :status3)")
				.append(" order by d_whn_create desc");
			
			list = session.createQuery(stQuery.toString())			
									.setString("code", code)
									.setString("status1", status1)
									.setString("status2", status2)
									.setString("status3", status3)
									.list();
						
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return list;
	}			
	
	public List searchPOByCodeSupStatus(String poCode, String supCode, String status) throws VONEAppException {

		Session session = null;
		List list;
		
		if (poCode == null)
			poCode = "%%";
		
		if (supCode == null)
			supCode = "%%";
		
		try{
			session = getCurrentSession();	
			
			stQuery.setLength(0);
			stQuery.append("select {po.*} from tb_purchase_order po, ms_vendor ven ")
				.append(" where po.n_vendor_id = ven.n_vendor_id")
				.append(" and ven.v_vendor_name like :supCode")
				.append(" and po.v_po_code like :poCode")
				.append(" and po.v_po_status = :status")
				.append(" order by po.d_whn_create desc");
			
			list = session.createSQLQuery(stQuery.toString())
									.addEntity("po", TbPurchaseOrder.class)
									.setString("supCode", supCode)
									.setString("poCode", poCode)
									.setString("status", status)
									.list();
		}catch (Exception e){
			e.printStackTrace();
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return list;
	}
	
	public List searchPOActiveByCodeSup(String poCode, String supCode) throws VONEAppException {

		Session session = null;
		List list;
		
		try{
			session = getCurrentSession();			
			
			stQuery.setLength(0);
			stQuery.append("select {po.*} from tb_purchase_order po, ms_vendor ven ")
				.append(" where po.n_vendor_id = ven.n_vendor_id")
				.append(" and ven.v_vendor_name like :supCode")
				.append(" and po.v_po_code like :poCode")
				.append(" and (po.v_po_status = :status1")
				.append(" or po.v_po_status = :status2)");
			
			list = session.createSQLQuery(stQuery.toString())
									.addEntity("po", TbPurchaseOrder.class)
									.setString("supCode", supCode)
									.setString("poCode", poCode)
									.setString("status1", MedisafeConstants.PURCHASING_APPROVED)
									.setString("status2", MedisafeConstants.PURCHASING_BACKORDER)
									.list();
		}catch (Exception e){
			e.printStackTrace();
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return list;
	}
	
	public BigDecimal getLastPrice (Integer itemId, MsVendor vendorPojo) throws VONEAppException{

		Session session = null;
		BigDecimal bDec = new BigDecimal(0);		
		
		try{
			session = getCurrentSession();
			
			stQuery.setLength(0);
			
			if (vendorPojo != null)
			stQuery.append(" select n_po_det_cost as cost from tb_purchase_order_detail det, tb_purchase_order po ")
				.append(" where det.n_item_id =:itemId")
				.append(" and det.n_po_id = po.n_po_id ")
				.append(" and po.n_vendor_id = :vendorId")
				.append(" order by det.d_whn_create desc");
			else
			stQuery.append(" select n_po_det_cost as cost from tb_purchase_order_detail ")
				.append(" where n_item_id =:itemId")
				.append(" order by d_whn_create desc");
			
			Object result = null;
			
			if (vendorPojo != null)
			result = session.createSQLQuery(stQuery.toString())
				.addScalar("cost", Hibernate.DOUBLE)
				.setInteger("itemId", itemId.intValue())
				.setInteger("vendorId", vendorPojo.getNVendorId())
				.setFirstResult(0)
				.setMaxResults(1)
				.uniqueResult();
			else
			result = session.createSQLQuery(stQuery.toString())
				.addScalar("cost", Hibernate.DOUBLE)
				.setInteger("itemId", itemId.intValue())
				.setFirstResult(0)
				.setMaxResults(1)
				.uniqueResult();
			
			if (result != null){
				Double d = (Double)result;
				bDec = bDec.add(new BigDecimal(d.doubleValue()));
			}
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}	
		
		return bDec;
	}
	
	public void updateChild(TbPurchaseOrderDetail pojo) throws VONEAppException{
		Session session = null;
		
		try {
			session = getCurrentSession();		
			
			session.update(pojo);
						
		}catch (Exception e){
			logger.error("Update Error", e);
			throw new VONEAppException(MessagesService.getKey("error.savemodify"));
		}
	}
}
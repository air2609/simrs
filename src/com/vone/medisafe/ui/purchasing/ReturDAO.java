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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsBranch;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.TbAccountPayable;
import com.vone.medisafe.mapping.TbDeliveryOrder;
import com.vone.medisafe.mapping.TbDeliveryOrderDetail;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.TbReturItem;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;
import com.vone.medisafe.service.PurchaseServiceLocator;
import com.vone.medisafe.service.Service;

/**

 * Data access object (DAO) for domain model class MsBranch.
 * @see com.vone.medisafe.mapping.MsBranch
 * @author MyEclipse - Hibernate Tools
 */
public class ReturDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ReturDAO.class);
    
    StringBuffer stQuery = new StringBuffer();   
    
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
    public List searchDOandBatch(String doNo, String batchNo) throws VONEAppException {
    	
    	if (doNo == null)
    		doNo = "%%";
    	else 
    		doNo = "%"+doNo+"%";
    	
    	if (batchNo == null)
    		batchNo = "%%";
    	else
    		batchNo = "%"+batchNo+"%";
    	
    	
    	try {
	    	Session session = getCurrentSession();
	    	
	    	StringBuffer stQuery = new StringBuffer();
	    	
	    	stQuery.append(" select distinct {tbdo.*} from tb_delivery_order tbdo, tb_delivery_order_detail tbdodet, tb_batch_item tbbi ")
	    		.append(" where ")
	    		.append(" tbdo.n_do_id = tbdodet.n_do_id and")
	    		.append(" tbdodet.n_do_det_id = tbbi.n_do_detail_id and")
	    		.append(" tbdo.v_do_code like :doNo and")
	    		.append(" tbbi.v_batch_no like :batchNo and")
	    		.append(" tbdo.v_do_status = :status ")
	    		.append(" limit 100");
	    	
	    	return session.createSQLQuery(stQuery.toString())
	    		.addEntity("tbdo", TbDeliveryOrder.class)
	    		.setString("doNo", doNo)
	    		.setString("batchNo", batchNo)
	    		.setString("status", MedisafeConstants.PURCHASING_APPROVED)
	    		.list();
	    	
    	}catch(Exception e){
            log.error("get failed", e);            
            
            throw new VONEAppException(MessagesService.getKey("error.nodata"));    		
    	}
    	    	
    }
    
    
    /**
     * Journal : 
     * D			K
     * AP Vendor			
     * 				Inventory/Gudang Credit
     * @param ctr
     * @param detCtr
     * @param tbDO
     * @throws VONEAppException
     */
    public void save (ReturController ctr, ReturDetailController detCtr,TbDeliveryOrder tbDO) throws VONEAppException {
    	
		PlatformTransactionManager txManager = PurchaseServiceLocator.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = txManager.getTransaction(def);
		
		try {
			
			String reason = detCtr.getCancelReason().getValue();	
			Double grandTotal = detCtr.getTotal().getValue().doubleValue();		
			
			Iterator<Listitem> itLI = detCtr.getList().getItems().iterator();
			
			JournalBeanHandler jbHandler = new JournalBeanHandler(MedisafeConstants.ACCT_AP_STR);
								
			//SET UP JOURNAL HEADER
			jbHandler.addJournal(tbDO.getVDoCode(), "", grandTotal, MedisafeConstants.ACCT_DEBIT, new Date(), 
					ctr.getUserInfoBean().getStUserId(), tbDO.getTbPurchaseOrder().getMsVendor().getMsCoa());
			//END OF SETTING UP JOURNAL HEADER
			
			while (itLI.hasNext()){
				Listitem item = itLI.next();
				
				TbBatchItem tbBI = (TbBatchItem)item.getValue();
				TbItemInventory tbII = (TbItemInventory)item.getAttribute(ReturController.tbInventory);				
								
				Intbox box = (Intbox)item.getAttribute(ReturDetailController.qtyReturChild);
				Integer qtyRetur = box.getValue();
				Double totalPrice = (Double)item.getAttribute(ReturDetailController.totalPriceChild);
							
				//PROCESS ENTRY TO RETUR ITEM TABLE
				TbReturItem tbRI = new TbReturItem();
				tbRI.setTbBatchItem(tbBI);
				tbRI.setNQty(qtyRetur);
				tbRI.setNPrice(totalPrice);
				tbRI.setVReason(reason);
				tbRI.setVWhoCreate(ctr.getUserInfoBean().getStUserId());
				tbRI.setDWhnCreate(new Date());
				
				getHibernateTemplate().save(tbRI);
				//END OF PROCESSING ENTRY
				
				
				//PROCESSING INVENTORY UPDATES
				tbII.setNItemInvQty(tbII.getNItemInvQty() - qtyRetur);
				
				getHibernateTemplate().update(tbII);
				//END OF PROCESSING INVENTORY UPDATES
								
				
				//PROCESS JOURNAL ENTRY BODY
				
				jbHandler.addJournal(tbDO.getVDoCode(), "", totalPrice, MedisafeConstants.ACCT_CREDIT, new Date(), 
						ctr.getUserInfoBean().getStUserId(), tbII.getMsWarehouse().getMsCoa());
				
				//END OF PROCESSING JOURNAL ENTRY BODY

								
			}	

			
			//SAVING JOURNAL
			if (!jbHandler.isBalance()){
				throw new Exception();
			}
			
			Iterator itJournal = jbHandler.getListJournal().iterator();
			while (itJournal.hasNext()){
				getHibernateTemplate().save(itJournal.next());
			}
			
			//END OF SAVING JOURNAL

			
		}catch (Exception e){
			
			logger.error("Save Error", e);
			txManager.rollback(status);
			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}	
		
		txManager.commit(status);
    }
    
    public List getBatchItemByDOD (TbDeliveryOrderDetail tbDOD) throws VONEAppException {
    	
    	try {
	    	Session session = getCurrentSession();
	    	
	    	StringBuffer stQuery = new StringBuffer();
	    	
	    	stQuery.append(" from TbBatchItem where tbDeliveryOrderDetail = :tbDOD");
	    	
	    	return session.createQuery(stQuery.toString())
	    		.setEntity("tbDOD", tbDOD)
	    		.list();
	    	
    	}catch (Exception e){
            log.error("get failed", e);            
            
            throw new VONEAppException(MessagesService.getKey("error.nodata"));    		    		
    	}
    }
}
package com.vone.medisafe.ui.purchasing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.TbAccountPayable;
import com.vone.medisafe.mapping.TbDeliveryOrder;
import com.vone.medisafe.mapping.TbDeliveryOrderDetail;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;
import com.vone.medisafe.service.PurchaseServiceLocator;

public class TbDeliveryOrderDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbDeliveryOrderDAO.class);
    
    StringBuffer stQuery = new StringBuffer();   
    
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
    public void updateHeaderOnly (TbDeliveryOrder pojo) throws VONEAppException {
		
		try {
			
			getHibernateTemplate().update(pojo);
			
		}catch (Exception e){
			logger.error("Update Error", e);

			throw new VONEAppException(MessagesService.getKey("error.savemodify"));
		}
    }
    
    public void save (TbDeliveryOrder pojo) throws VONEAppException {
    	
    	try{
    		
    		getHibernateTemplate().save(pojo);
    		    		
    		Iterator it = pojo.getTbDeliveryOrderDetails().iterator();
    		
    		while (it.hasNext()){
    			TbDeliveryOrderDetail tbDOD = (TbDeliveryOrderDetail)it.next();
    			
    			getHibernateTemplate().save(tbDOD);
    		}
    		
		}catch (Exception e){			
			logger.error("Save Error", e);

			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}	
    }
    
    public TbDeliveryOrder getDObyCode(String code) throws VONEAppException{
    	Session session = null;
    	TbDeliveryOrder tbDO = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		stQuery.setLength(0);
    		stQuery.append(" from TbDeliveryOrder where VDoCode = :code");
    		
    		tbDO = (TbDeliveryOrder)session.createQuery(stQuery.toString())
    			.setString("code", code)
    			.uniqueResult();
    		
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return tbDO;
    }
    
    public void delete (TbDeliveryOrder pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
    
    public void update (TbDeliveryOrder pojo) throws VONEAppException {
    	Session session = null;
   
    	try{
    		session = getCurrentSession();
    		    		
    		//delete previous detail
    		stQuery.setLength(0);
    		stQuery.append(" delete from TbDeliveryOrderDetail ")
    			.append(" where n_do_id = :doId");
    		
    		int res = session.createQuery(stQuery.toString())
    				.setInteger("doId", pojo.getNDoId().intValue())
    				.executeUpdate();
    		
    		//update TbPurchaseOrder
    		session.update(pojo);
    		
    		//set children
    		
    		Iterator it = pojo.getTbDeliveryOrderDetails().iterator();
    		
    		while (it.hasNext()){
    			TbDeliveryOrderDetail tbDOD = (TbDeliveryOrderDetail)it.next();
    			
    			session.save(tbDOD);
    		}
    		
		}catch (Exception e){
			logger.error("Save Error", e);

			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}
    }
    
    
    /**
     * If there's bonus, then COGS price will be affected.
     * ex. buy 10 items, on the price 5000/item, then we got 1 item in bonus
     * so the COGS price is not 5000 any more, but 50000/11.
     * 
     * Journal : 
     * D			K
     * Warehouse
     * 				AP Vendor
     * 
     * Tables involved :
     * TbPurchaseOrder
     * TbPurchaseOrderDetail 
     * TbDeliveryOrder
     * TbDeliveyrOrderDetail
     * TbAccountPayable
     * TbBatchItem
     * 
     * @param tbDO
     * @param listBatch
     * @throws VONEAppException
     */
    public void executeApproval (TbDeliveryOrder tbDO, Listbox listBatch) throws VONEAppException{
    	
		PlatformTransactionManager txManager = PurchaseServiceLocator.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = txManager.getTransaction(def);
						
		try {
			
			org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
			UserInfoBean uib = (UserInfoBean)zkSession.getAttribute(Constant.USER_INFO);
			String userId = ((UserInfoBean)zkSession.getAttribute(Constant.USER_INFO)).getStUserId();

			
			TbPurchaseOrder tbPO = tbDO.getTbPurchaseOrder();
			
			//alter DO's status
			tbDO.setVDoStatus(MedisafeConstants.PURCHASING_APPROVED);
			
			getHibernateTemplate().update(tbDO);
							
			
			//insert Journal AP
			String memo = "VENDOR:"+tbDO.getTbPurchaseOrder().getMsVendor().getVVendorCode()+
							"TOTAL INVOICE:"+tbDO.getNTotalAfterPpn();												
			
			JournalBeanHandler jbHandler = new JournalBeanHandler(MedisafeConstants.ACCT_AP_STR);

			//COA VENDOR
			MsCoa apAcct = tbDO.getTbPurchaseOrder().getMsVendor().getMsCoa();
			
			//GET DEFAULT ACCT IF VENDOR COA IS NULL
			if (apAcct == null)
				apAcct = jbHandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_AP); 			
	
			jbHandler.addJournal(tbDO.getVDoCode(), memo, tbDO.getNTotalAfterPpn(), 
					MedisafeConstants.ACCT_CREDIT, tbDO.getDRecDate(), uib.getStUserId(), apAcct);

			//setAP
			TbAccountPayable tbAP = new TbAccountPayable();
			tbAP.setTbJournalTrx(jbHandler.getListJournal().get(0));
			tbAP.setMsVendor(tbPO.getMsVendor());
			tbAP.setNTotalRemaining(tbDO.getNTotalAfterPpn());
			tbAP.setDDueDate(tbPO.getDPaymentDue());
			tbAP.setVWhoCreate(userId);
			tbAP.setDWhnCreate(new Date());
			//save AP
			getHibernateTemplate().save(tbAP);
			
			
			Iterator it = tbDO.getTbDeliveryOrderDetails().iterator();
			
			//GET TOTAL ITEMS ON DO 
			//IF DISCOUNT TYPE IS RP
			int totalItem = 0;
			
			if (MedisafeConstants.RP.equals(tbPO.getVDiscountType())){
				while (it.hasNext()){
					TbDeliveryOrderDetail tbDOD = (TbDeliveryOrderDetail)it.next();
					totalItem += tbDOD.getNDoDetQty() + tbDOD.getNDoBonusQty();
				}
			}

			
			
			
			//*begin* allocating total price per batch item, minus discount
			Iterator itDOD = tbDO.getTbDeliveryOrderDetails().iterator();
			
			List<MsItem> items = new ArrayList<MsItem>();
			List<Double> price = new ArrayList<Double>();
			
			while (itDOD.hasNext()){
				TbDeliveryOrderDetail tbDOD = (TbDeliveryOrderDetail)itDOD.next();
				int bonusQty = tbDOD.getNDoBonusQty();
												
				Iterator itBatch = listBatch.getItems().iterator();
				
				while (itBatch.hasNext()){
					Listitem item = (Listitem)itBatch.next();
					MsItem msItem = (MsItem)item.getValue();					
					int qtyBatch = (Integer)item.getAttribute(DOBatchController.QTY);
					String endM = (String)item.getAttribute(DOBatchController.FINAL_M);
					int multiplier = (Short)item.getAttribute(DOBatchController.MULTIPLIER);
					
					
					
					if (tbDOD.getMsItem().getNItemId().intValue() != msItem.getNItemId().intValue())
						continue;
					
					System.out.println("==============");
					System.out.println("end multiplier : "+endM);
					System.out.println("multiplier value : "+multiplier);
					

					//UPDATING DOD FOR MEASUREMENT-END
					tbDOD.setVEndM(endM);
					tbDOD.setNMultipleM(multiplier);
					getHibernateTemplate().update(tbDOD);
					//END OF UPDATING
					
					items.add(msItem);
					
					System.out.println("=========BATCH PRICE==========");
					double priceEach = PurchaseController.calculatePrice(tbDOD.getNSubtotal(), tbPO.getNDiscount(), 
							tbPO.getVDiscountType(), tbDO.getNDoTax(), tbDO.getVTaxType(), (tbDOD.getNDoDetQty() + tbDOD.getNDoBonusQty()), totalItem) 
							/ ((tbDOD.getNDoDetQty() + tbDOD.getNDoBonusQty()) * multiplier);
					System.out.println("=========END OF BATCH PRICE==========");
					
					System.out.println("price each : "+priceEach);
					System.out.println("==============");
					
					TbBatchItem biPojo = new TbBatchItem();
					biPojo.setTbDeliveryOrderDetail(tbDOD);
					biPojo.setVBatchNo(item.getAttribute(DOBatchController.BATCH_NO).toString());				
					biPojo.setDBatchExpDate((Date)item.getAttribute(DOBatchController.EXP_DATE));
					biPojo.setNBatchItemQty(((Integer)item.getAttribute(DOBatchController.QTY)).shortValue());
					biPojo.setMsItem(tbDOD.getMsItem());	
					biPojo.setNCogsPrice(priceEach);
					
					price.add(new Double(priceEach));
					
					biPojo.setVWhoCreate(userId);
					biPojo.setDWhnCreate(new Date());
					
					getHibernateTemplate().save(biPojo);
															
					
					//inventory updates
					TbItemInventory tbII = new TbItemInventory();
					tbII.setMsItem(biPojo.getMsItem());
					tbII.setTbBatchItem(biPojo);
					tbII.setNItemInvQty(qtyBatch);
					tbII.setMsWarehouse(tbDO.getMsWarehouse());
					
					tbII.setVWhoCreate(uib.getStUserId());
					tbII.setDWhnCreate(new Date());
				
					getHibernateTemplate().save(tbII);
					
					
				}
			}
			//*end* allocating total price per batch item, minus discount										
								
			it = tbDO.getTbDeliveryOrderDetails().iterator();
			
			double subtotal = 0;
			while (it.hasNext()){
				TbDeliveryOrderDetail tbDOD = (TbDeliveryOrderDetail)it.next();						
				
				TbPurchaseOrderDetail tbPOD = tbDOD.getTbPurchaseOrderDetail();
				
				//*begin* saving total Qty & Bonus Arrived
				short qtyRec = tbPOD.getNPoDetQtyReceived();
				
				qtyRec += tbDOD.getNDoDetQty();
				
				short qtyBonusRec = tbPOD.getNBonusRecieved();
				
				qtyBonusRec += tbDOD.getNDoBonusQty();
				
				tbPOD.setNPoDetQtyReceived(qtyRec);
				tbPOD.setNBonusRecieved(qtyBonusRec);
				
				//save total Qty & Bonus Arrived
				getHibernateTemplate().update(tbPOD);
				//*end* saving total Qty & Bonus
				
				//insert Journal Detail
				memo = "VENDOR:"+tbDO.getTbPurchaseOrder().getMsVendor().getVVendorCode()+";INV:"+tbDOD.getMsItem().getVItemCode()+
								";QTY:"+tbDOD.getNDoDetQty();

				
				
				//TOTAL subtotal 				
				System.out.println("========SUBTOTAL PRICE=========");
				subtotal = PurchaseController.calculatePrice(tbDOD.getNSubtotal(), tbPO.getNDiscount(), 
						tbPO.getVDiscountType(), tbDO.getNDoTax(), tbDO.getVTaxType(), tbDOD.getNDoDetQty() + tbDOD.getNDoBonusQty(), totalItem);
				System.out.println("========SUBTOTAL PRICE=========");
				
				jbHandler.addJournal(tbDO.getVDoCode(), memo, subtotal, 
							MedisafeConstants.ACCT_DEBIT, tbDO.getDRecDate(), uib.getStUserId(), tbDOD.getTbDeliveryOrder().getMsWarehouse().getMsCoa());	
				
												
			}
			
			
			
			//*begin* saving Journal Account
			//cancel all if discrepancy happens
			if (!jbHandler.isBalance()){
				throw new VONEAppException(MessagesService.getKey("master.coa.journal.discrepancy.internal"));
			}		
			
			Iterator<TbJournalTrx> itJournal = jbHandler.getListJournal().iterator();
			
			while (itJournal.hasNext()){
				getHibernateTemplate().save(itJournal.next());
			}						
			
			//*end* saving Journal Account
				
			//*begin* alter PO status to Back-Order or Close (if all items had arrived)
			
			it = tbPO.getTbPurchaseOrderDetails().iterator();
			boolean allZero = true;
			
			while (it.hasNext()){
				TbPurchaseOrderDetail tbPODetail = (TbPurchaseOrderDetail)it.next();				
				
				if (tbPODetail.getNPoDetQtyOrdered() - tbPODetail.getNPoDetQtyReceived() != 0){
					allZero = false;
					break;
				}
				if (tbPODetail.getNBonus() - tbPODetail.getNBonusRecieved() != 0){
					allZero = false;
					break;
				}
					
			}
			
			if (allZero)
				tbPO.setVPoStatus(MedisafeConstants.PURCHASING_CLOSED);
			else
				tbPO.setVPoStatus(MedisafeConstants.PURCHASING_BACKORDER);
			
			//saving PO
			getHibernateTemplate().update(tbPO);
			
			//*end* altering PO
			
			String query ="from MsItemSellingPrice p where p.msItem=?";
			
			
			
			MsItemSellingPrice itemsel = null;
			
			
			// arif added code to accomodate automatic selling price, no need manual input for item selling price
			
			int indPrice = 0;
			//update item selling price
			for(MsItem item : items){
				

				//Messagebox.show(item.getVItemName());
				
				double priceEach = price.get(indPrice).doubleValue();
				indPrice++; 
				itemsel = new MsItemSellingPrice();
				itemsel.setMsItem(item);
				List<MsItemSellingPrice> sp = getHibernateTemplate().find(query, item);
				
				//Messagebox.show("banyak item " + sp.size());
				
				
				//getSellingPriceBaseOnItem(item);
				List<MsTreatmentClass> tc = getHibernateTemplate().loadAll(MsTreatmentClass.class);
				getHibernateTemplate().deleteAll(sp);
				
				String persenUntung = ""; //MessagesService.getKey("persen.untung.obat1");
				
				if(item.getNItemType() != null && item.getNItemType().intValue() == 5){
					persenUntung = MessagesService.getKey("persen.untung.obat.bpjs");
				}
				else {
					persenUntung = MessagesService.getKey("persen.untung.obat1");
				}
				
				Double hargaJual = null;
				
//				String persenUntung2 = MessagesService.getKey("persen.untung.obat2");
				
				MsItemSellingPrice selPrice = null;
//				MsItemSellingPrice selprice2 = null;
				//List<MsItemSellingPrice> sell = new ArrayList<MsItemSellingPrice>(0);
				
				for(MsTreatmentClass t : tc){
					selPrice = new MsItemSellingPrice();
//					selprice2 = new MsItemSellingPrice();
					selPrice.setMsItem(item);
//					selprice2.setMsItem(item);
					
					selPrice.setMsTreatmentClass(t);
//					selprice2.setMsTreatmentClass(t);
					
					hargaJual = priceEach + (new Double(persenUntung).doubleValue()/100) * priceEach;
					selPrice.setNSellingPrice(hargaJual.doubleValue());
					
//					hargaJual = priceEach + (new Double(persenUntung2).doubleValue()/100) * priceEach;
//					selprice2.setNSellingPrice(hargaJual.doubleValue());
					
					
					getHibernateTemplate().save(selPrice);
//					getHibernateTemplate().save(selprice2);
					
				}
				
				String batchQuery = "from TbBatchItem b where b.msItem=?";
				List<TbBatchItem> batchItems = getHibernateTemplate().find(batchQuery, item);
				for(TbBatchItem batch:batchItems){
						batch.setNCogsPrice(priceEach);
						getHibernateTemplate().saveOrUpdate(batch);
				}
			}
			
//			String batchQuery = "from TbBatchItem b where b.msItem=?";
////			TbBatchItem batch = null;
//			double jumlahItem=0;
//			double totalHargaPerItems=0;
//			double hargaCogsBaru= 0;
//			
//			for(MsItem item : items){
//				jumlahItem = 0;
//				totalHargaPerItems = 0;
//				List<TbBatchItem> batchItems = getHibernateTemplate().find(batchQuery, item);
//				
//				for(TbBatchItem batch:batchItems){
//					jumlahItem = jumlahItem + batch.getNBatchItemQty();
//					totalHargaPerItems = totalHargaPerItems + (batch.getNBatchItemQty() * batch.getNCogsPrice());
//				}
//				hargaCogsBaru = totalHargaPerItems/jumlahItem;
//				
//				for(TbBatchItem batch:batchItems){
//					batch.setNCogsPrice(hargaCogsBaru);
//					getHibernateTemplate().saveOrUpdate(batch);
//				}
//				
//			}
									
					
		}catch (Exception e){
			logger.error("Save Error", e);
			txManager.rollback(status);
			throw new VONEAppException(MessagesService.getKey("error.saveadd"));
		}
		txManager.commit(status);
    }
    
    public List searchDObyCodeWhouse(String code, String whouseCode) throws VONEAppException{
    	
    	Session session = null;
    	List list = null;
    	
    	try{
    		if (code == null)
    			code = "%%";
    		if (whouseCode == null)
    			whouseCode = "%%";
    		
    		session = getCurrentSession();
    		
    		stQuery.setLength(0);
    		
    		stQuery.append("select {tbdo.*} from  ")
    		.append(" tb_delivery_order tbdo, ms_warehouse whouse ")
    		.append(" where ")
    		.append(" tbdo.n_whouse_id = whouse.n_whouse_id ")
    		.append(" and tbdo.v_do_code like :code")
    		.append(" and whouse.v_whouse_code like :whouseCode")
    		.append(" and tbdo.v_do_status =:status");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("tbdo", TbDeliveryOrder.class)
    			.setString("code", code)
    			.setString("whouseCode", whouseCode)
    			.setString("status", MedisafeConstants.PURCHASING_OPEN)
    			.list();
		}catch (Exception e){
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}	
		
		return list;
    }
    
    public TbBatchItem getBatchItemByBatchCode (String code) throws VONEAppException{
    	Session session = null;
    	TbBatchItem pojo = null;
    	
    	try{
    		
    		session = getCurrentSession();
    		
    		stQuery.setLength(0);
    		
    		stQuery.append("select {item.*} from  ")
    		.append(" tb_batch_item item where v_batch_no = :code ");    		
    		
    		pojo = (TbBatchItem)session.createSQLQuery(stQuery.toString())
    			.addEntity("item", TbBatchItem.class)
    			.setString("code", code)
    			.uniqueResult();
    		
		}catch (Exception e){		
            log.error("get failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		
		return pojo;
    }
    
    public List<MsItemSellingPrice> getSellingPriceBaseOnItem(MsItem item){
    	
    	List<MsItemSellingPrice> hasil = null;
    	StringBuffer sb = new StringBuffer();
    	try {
			Session session = getCurrentSession();
			sb.append("select {tclass.*} from ms_selling_price tclass");
			sb.append(" where tclass.n_item_id=:itemId");
			
			hasil = session.createSQLQuery(sb.toString()).addEntity("tclass", MsItemSellingPrice.class)
			.setInteger("itemId", item.getNItemId()).list();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return hasil;
    	
    	
    }
    
    
    public List<MsTreatmentClass> getAllTreatmentClass(){
    	List<MsTreatmentClass> hasil = null;
    	StringBuffer sb = new StringBuffer();
    	try {
			Session session = getCurrentSession();
			sb.append("select {tclass.*} from ms_treatment_class tclass");
			
			hasil = session.createSQLQuery(sb.toString()).addEntity("tclass", MsTreatmentClass.class).list();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return hasil;
    	
    }
    
    
}
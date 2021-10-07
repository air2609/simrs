package com.vone.medisafe.mapping.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemMutation;
import com.vone.medisafe.mapping.TbPurchaseRequestDetail;
import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.Service;

public class MsWarehouseDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(MsWarehouseDAO.class);
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
    public List findAll() throws VONEAppException {
    	
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		List list = session.createQuery(" from MsWarehouse order by VWhouseCode")
    			.list();
    		
    		return list;
    		    	
    	}catch(Exception e){
        	logger.error("findAll failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    }
    
    public void save (MsWarehouse pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void update (MsWarehouse pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsWarehouse pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
    
    /**
     * Get all Item under Buffer limit, and current qty, will retrieve all if whouseId is set to null
     * @param whouseId
     * @throws VONEAppException
     * return : List -> Object[] {MsItem,Integer qty}  
     */
    public List getItemUnderBuffer(Integer whouseId) throws VONEAppException{ 
    	
    	Session session = null;
    	List list = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(" SELECT {IT.*}, Q1.QTY ")
    			.append(" FROM MS_ITEM IT,")
    			.append(" (SELECT INV.N_ITEM_ID, SUM(N_ITEM_INV_QTY) AS QTY FROM TB_ITEM_INVENTORY INV");
    		
    		if (whouseId != null)
    			stQuery.append(" WHERE INV.N_WHOUSE_ID = :whouseId AND INV.N_ITEM_INV_QTY > 0 ");
    			
    		stQuery.append(" GROUP BY N_ITEM_ID)Q1")
    			.append(" WHERE")
    			.append(" IT.N_ITEM_ID = Q1.N_ITEM_ID")
    			.append(" AND")
    			.append(" Q1.QTY <= IT.N_ITEM_BUFFER_LIMIT")
    			.append(" ORDER BY Q1.N_ITEM_ID ");

    		SQLQuery query = session.createSQLQuery(stQuery.toString())
    			.addEntity("IT", MsItem.class)
    			.addScalar("QTY", Hibernate.INTEGER);
    		
    		if (whouseId != null)
    			query.setInteger("whouseId", whouseId.intValue());
    		
    		list = query.list();
    		    		
    	}catch(Exception e){
        	logger.error("getItemUnderBuffer failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    	
    	return list;
    }
    
    /**
     * getQty Availabel on Warehouse on specific ItemID
     * @param whouseId
     * @param itemId
     * @return
     * @throws VONEAppException
     */
    public Integer getQtyAvail(Integer whouseId, Integer itemId) throws VONEAppException{ 
    	
    	if (whouseId == null || itemId == null) return new Integer(0);
    	
    	Session session = null;
    	Integer qty = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(" SELECT INV.N_ITEM_ID, SUM(N_ITEM_INV_QTY) AS QTY FROM TB_ITEM_INVENTORY INV")    	
    			.append(" WHERE INV.N_WHOUSE_ID = :whouseId")
    			.append(" AND INV.N_ITEM_ID = :itemId")
    			.append(" AND INV.N_ITEM_INV_QTY > 0 ")
    			.append(" GROUP BY N_ITEM_ID");
 
    		SQLQuery query = session.createSQLQuery(stQuery.toString())
    			.addScalar("N_ITEM_ID", Hibernate.INTEGER)
    			.addScalar("QTY", Hibernate.INTEGER);
    		query.setInteger("whouseId", whouseId.intValue())
    			.setInteger("itemId", itemId.intValue());
    		
    		Object obj = query.uniqueResult();
    		if (obj == null || !(obj instanceof Object[])) return new Integer(0);
    		
    		Object[] objArr = (Object[])obj;    		
    		
    		if (objArr[1] !=  null)
    			qty = (Integer)objArr[1];
    		else 
    			qty = new Integer(0);
    		    		
    	}catch(Exception e){
        	logger.error("getQtyAvail failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    	
    	return qty;
    }    
    
    public List getWhouseByStaffId(Integer staffId) throws VONEAppException{ 
    	
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(" select distinct {whouse.*} from ms_staff_in_unit siu, ms_unit unit, ms_warehouse whouse")
    			.append(" where siu.n_staff_id =:staffId ")
    			.append(" and siu.n_unit_id = unit.n_unit_id")
    			.append(" and unit.n_whouse_id = whouse.n_whouse_id");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("whouse", MsWarehouse.class)
    			.setInteger("staffId", staffId)
    			.list();
    		    		
    	}catch(Exception e){
        	logger.error("getItemUnderBuffer failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());    		
    	}
    	
    	return list;
    }
    
    public List getItemRequest(int whouseid) throws VONEAppException{
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(
    				" select " +
    				"	{itemreq.*} " +
    				" from " +
    				" 	tb_item_request itemreq " +
    				" where " +
    				" 	itemreq.n_qty_sent < itemreq.n_qty_req " +
    				"	and itemreq.n_target_whouse_id = :whouseid " +
    				" order by itemreq.v_request_code, itemreq.n_ir_id");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("itemreq", TbItemRequest.class)
    			.setInteger("whouseid", whouseid)
    			.list();
    		    		
    	}catch(Exception e){
        	logger.error(e.getMessage());
        	throw new VONEAppException(e.getMessage());
    	}
    	
    	return list;
    }
    
    public List getItemRequestApprove(int whouseid) throws VONEAppException{
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(
    				" select distinct " +
    				"	{itemmut.*}" +
    				" from " +
    				" 	tb_item_request itemreq, " +
    				"	tb_item_mutation itemmut " +
    				" where " +
    				" 	itemreq.n_qty_sent <= itemreq.n_qty_req " +
    				"	and itemreq.n_source_whouse_id = :whouseid " +
    				"	and itemmut.v_status = 1 ");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("itemmut", TbItemMutation.class)
    			.setInteger("whouseid", whouseid)
    			.list();
    		    		
    	}catch(Exception e){
        	logger.error(e.getMessage());
        	throw new VONEAppException(e.getMessage());
    	}
    	
    	return list;
    }
    public List getItemRequestByCode(String requestCode) throws VONEAppException{
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(
    				" select " +
    				"	{itemreq.*} " +
    				" from " +
    				" 	tb_item_request itemreq " +
    				" where " +
    				" 	itemreq.n_qty_sent < itemreq.n_qty_req " +
    				"	and itemreq.v_request_code = :requestCode " +
    				" order by itemreq.v_request_code, itemreq.n_ir_id");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("itemreq", TbItemRequest.class)
    			.setString("requestCode", requestCode)
    			.list();
    		    		
    	}catch(Exception e){
        	logger.error(e.getMessage());
        	throw new VONEAppException(e.getMessage());
    	}
    	
    	return list;
    }
    
    public List getTbItemInventory(Integer whouseId, Integer itemId) throws VONEAppException{
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(
    				" select " +
    				"	{inv.*} " +
    				" from " +
    				" 	tb_item_inventory inv" +
    				" where " +
    				"   inv.n_item_inv_qty > 0 " +
    				" 	and inv.n_whouse_id = :whouseId " +
    				"	and inv.n_item_id = :itemId ");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("inv", TbItemInventory.class)
    			.setInteger("whouseId", whouseId)
    			.setInteger("itemId", itemId)
    			.list();
    		    		
    	}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());    		
    	}
    	
    	return list;
    }
    public boolean saveItemMutation(Set itemMutationSet, Set itemRequestSet) throws VONEAppException{
		logger.debug("Save item mutation - ENTER");
		boolean success = false;
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);

		try {

			Iterator iter = itemRequestSet.iterator();
			TbItemRequest itemRequest;
			while (iter.hasNext()) {
				itemRequest = (TbItemRequest) iter.next();
				getHibernateTemplate().update(itemRequest);
			}

			Iterator it = itemMutationSet.iterator();
			TbItemMutation itemMutation;
			while (it.hasNext()) {
				itemMutation = (TbItemMutation) it.next();
				getHibernateTemplate().save(itemMutation);
			}
			
			txManager.commit(status);
			success = true;
		} catch (HibernateException e) {
			txManager.rollback(status);
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		} 

		logger.debug("Save item mutation EXIT");
		return success;
    }
    public boolean saveItemApprove(Set itemMutationSet) throws VONEAppException{
		logger.debug("Save item approve - ENTER");
		boolean success = false;
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		

		try {

			Iterator iter = itemMutationSet.iterator();
			TbItemMutation tbItemMutation;
			TbItemInventory tbItemInventoryTarget;
			TbItemInventory tbItemInventorySource;
			Date tglMutation = new Date();
			while (iter.hasNext()) {
				tbItemMutation = (TbItemMutation) iter.next();
				//rule: item di wh target pindah ke wh source
				
				tbItemInventoryTarget = getTbItemInventory(
						tbItemMutation.getTbItemRequest().getMsItem().getNItemId(),
						tbItemMutation.getTbItemRequest().getWarehouseTarget().getNWhouseId(),
						tbItemMutation.getTbBatchItem().getNBatchId()
						);
				tbItemInventoryTarget.setNItemInvQty(tbItemInventoryTarget.getNItemInvQty() - tbItemMutation.getNMitemQty());
				tbItemInventoryTarget.setDWhnChange(tglMutation);
				
				//try ambil record di wh source
				tbItemInventorySource = getTbItemInventory(
						tbItemMutation.getTbItemRequest().getMsItem().getNItemId(),
						tbItemMutation.getTbItemRequest().getWarehouseSource().getNWhouseId(),
						tbItemMutation.getTbBatchItem().getNBatchId()
						);
				
				if(tbItemInventorySource == null){
					//jika di wh source blm ada, buat new record
					tbItemInventorySource = new TbItemInventory();
					tbItemInventorySource.setNItemInvQty(tbItemMutation.getNMitemQty());
					tbItemInventorySource.setMsItem(tbItemInventoryTarget.getMsItem());
					tbItemInventorySource.setMsWarehouse(tbItemMutation.getTbItemRequest().getWarehouseSource());
					tbItemInventorySource.setTbBatchItem(tbItemMutation.getTbBatchItem());
					tbItemInventorySource.setDWhnCreate(tglMutation);
				}else{
					//jika di wh source sdh ada, tambahkan item ke record yg sdh ada
					tbItemInventorySource.setNItemInvQty(tbItemMutation.getNMitemQty() + tbItemInventorySource.getNItemInvQty());
					tbItemInventorySource.setMsItem(tbItemInventoryTarget.getMsItem());
					tbItemInventorySource.setMsWarehouse(tbItemMutation.getTbItemRequest().getWarehouseSource());
					tbItemInventorySource.setTbBatchItem(tbItemMutation.getTbBatchItem());
					tbItemInventorySource.setDWhnChange(tglMutation);
					
				}
					
				getHibernateTemplate().saveOrUpdate(tbItemInventoryTarget);//target
				getHibernateTemplate().saveOrUpdate(tbItemInventorySource);//source
				
				tbItemMutation.setNStatus(MedisafeConstants.APPROVED_STATUS);//APPROVED
				getHibernateTemplate().update(tbItemMutation);
				if(tbItemMutation.getTbItemRequest().getNQtyReq().intValue() == tbItemMutation.getTbItemRequest().getNQtySent().intValue()){
					tbItemMutation.getTbItemRequest().setNStatus(MedisafeConstants.ALLSENT_STATUS);//ALL SENT
					getHibernateTemplate().update(tbItemMutation.getTbItemRequest());
				}
			}

			txManager.commit(status);
			success = true;
		}catch (Exception e){
			txManager.rollback(status);
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(),e);
		}

		logger.debug("Save item approve EXIT");
		return success;
    }

	private TbItemInventory getTbItemInventory(Integer itemId, Integer whouseId, Integer batchId) throws VONEAppException{
		List list = getCurrentSession()
			.createSQLQuery("" +
					"select {item.*} " +
					"from tb_item_inventory item " +
					"where item.n_item_id = :n_item_id " +
					"and item.n_whouse_id = :n_whouse_id " +
					"and item.n_batch_id = :n_batch_id")
			.addEntity("item", TbItemInventory.class)
			.setInteger("n_item_id", itemId)
			.setInteger("n_whouse_id", whouseId)
			.setInteger("n_batch_id", batchId)
			.list();
		Iterator iter = list.iterator();
		if(iter.hasNext())
			return(TbItemInventory)iter.next();
		else
			return null;
	}

//	private TbItemInventory getTbItemInventory(TbItemMutation itemMutation) throws VONEAppException{
//		
//		List list = getCurrentSession()
//						.createSQLQuery("" +
//								"select {item.*} " +
//								"from tb_item_inventory item " +
//								"where item.n_item_id = :n_item_id " +
//								"and item.n_whouse_id = :n_whouse_id " +
//								"and item.n_batch_id = :n_batch_id")
//						.addEntity("item", TbItemInventory.class)
//						.setInteger("n_item_id", itemMutation.getTbItemRequest().getMsItem().getNItemId())
//						.setInteger("n_whouse_id", itemMutation.getTbItemRequest().getWarehouseTarget().getNWhouseId())
//						.setInteger("n_batch_id", itemMutation.getTbBatchItem().getNBatchId())
//						.list();
//		Iterator iter = list.iterator();
//		if(iter.hasNext())
//			return(TbItemInventory)iter.next();
//		else
//			return null;
//	}

	public List getItemRequestBySource(int whouseid) throws VONEAppException{
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(
    				" select " +
    				"	{itemreq.*} " +
    				" from " +
    				" 	tb_item_request itemreq " +
    				" where " +
    				" 	itemreq.n_qty_sent < itemreq.n_qty_req " +
    				"	and itemreq.n_source_whouse_id = :whouseid " +
    				" order by itemreq.v_request_code, itemreq.n_ir_id");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("itemreq", TbItemRequest.class)
    			.setInteger("whouseid", whouseid)
    			.list();
    		    		
    	}catch(Exception e){
    		logger.error(e.getMessage());
    		throw new VONEAppException(e.getMessage());
    		
    	}
    	
    	return list;
    }
	
	
	public MsWarehouse findById( java.lang.Integer id) {
        logger.debug("getting MsUnit instance with id: " + id);
        try {
            MsWarehouse instance = (MsWarehouse) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsWarehouse", id);
            return instance;
        } catch (RuntimeException re) {
            logger.error("get failed", re);
            throw re;
        }
    }

	public void cancelItemRequest(Set<TbItemRequest> itemRequestSet) throws VONEAppException{
		// todo Auto-generated method stub
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		

		try {

			Iterator iter = itemRequestSet.iterator();
			TbItemRequest tbItemRequest;
			while (iter.hasNext()) {
				tbItemRequest = (TbItemRequest) iter.next();
				getHibernateTemplate().delete(tbItemRequest);
				System.out.println("CEK");
			}

			txManager.commit(status);
		}catch (Exception e){
			txManager.rollback(status);
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(),e);
		}

	}

	public void cancelItemApprove(Set<TbItemMutation> itemMutationSet) throws VONEAppException{
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		

		try {

			Iterator iter = itemMutationSet.iterator();
			TbItemMutation tbItemMutation;
			while (iter.hasNext()) {
				tbItemMutation = (TbItemMutation) iter.next();
				getHibernateTemplate().delete(tbItemMutation);
			}

			txManager.commit(status);
		}catch (Exception e){
			txManager.rollback(status);
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(),e);
		}

	}

	public List getItemRequestAll(int whouseid, Date startDate, Date endDate) throws VONEAppException{
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(
    				" select " +
    				"	{itemreq.*} " +
    				" from " +
    				" 	tb_item_request itemreq " +
    				" where " +
    				"	itemreq.n_source_whouse_id = :whouseid " +
    				"	and (d_whn_create between :startDate and :endDate) " +
    				" order by itemreq.v_request_code, itemreq.n_ir_id");
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("itemreq", TbItemRequest.class)
    			.setInteger("whouseid", whouseid)
    			.setDate("startDate", startDate)
    			.setDate("endDate", endDate)
    			.list();
    		System.out.println(list.size());
    		    		
    	}catch(Exception e){
        	logger.error(e.getMessage());
        	throw new VONEAppException(e.getMessage());
    	}
    	
    	return list;	}

	public List getItemMutasiAll(Integer whouseid, Date startDate, Date endDate) throws VONEAppException{
    	List list = null;
    	Session session = null;
    	
    	try{
    		session = getCurrentSession();
    		
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(
    				" select distinct " +
    				"	{itemmut.*}" +
    				" from " +
    				" 	tb_item_request itemreq, " +
    				"	tb_item_mutation itemmut " +
    				" where " +
    				"	itemreq.n_target_whouse_id = :whouseid " +
    				"	and (itemmut.d_whn_create between :startDate and :endDate) ");
    		
    		list = session.createSQLQuery(stQuery.toString())
    			.addEntity("itemmut", TbItemMutation.class)
    			.setInteger("whouseid", whouseid)
    			.setDate("startDate", startDate)
    			.setDate("endDate", endDate)
    			.list();
    		    		
    	}catch(Exception e){
        	logger.error(e.getMessage());
        	//e.printStackTrace();
        	throw new VONEAppException(e.getMessage());
    	}
    	return list;
    }
	
	public List<TbPurchaseRequestDetail> getOpenOpp(MsItem item) throws VONEAppException{
		List<TbPurchaseRequestDetail> openOpp = null;
		Session session = null;
		try{
			session = getCurrentSession();
			String query = "select {d.*} from tb_purchase_request_detail d, tb_purchase_request pr "+
			                 "where d.n_item_id=:itemId and pr.n_pr_id=d.n_pr_id and pr.v_pr_status='APPROVED'  "+
			                 "and pr.n_supplier_id is not null and date_part('day',now()-pr.d_whn_create) between 0 and 360 ";
			SQLQuery sql = session.createSQLQuery(query);
			sql.setInteger("itemId", item.getNItemId());
			sql.addEntity("d", TbPurchaseRequestDetail.class);
			
			openOpp = sql.list();
		}catch(Exception e){
			logger.error(e.getMessage());
        	
        	throw new VONEAppException(e.getMessage());
		}
		return openOpp;
	}
    
}

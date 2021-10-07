package com.vone.medisafe.service.iface.purchasing;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbDeliveryOrder;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;
import com.vone.medisafe.ui.purchasing.DOBatchController;
import com.vone.medisafe.ui.purchasing.DOController;

public interface DOManager {
	
	public void save (TbDeliveryOrder pojo) throws VONEAppException;
	
	public void update (TbDeliveryOrder pojo) throws VONEAppException;
	
	public void updateHeaderOnly (TbDeliveryOrder pojo) throws VONEAppException;
	
	public TbDeliveryOrder getDObyCode (String code) throws VONEAppException;
	
	public void executeApproval (TbDeliveryOrder tbDO, Listbox listBatch) throws VONEAppException;

	public List searchDObyCodeWhouse(String code, String whouseCode) throws VONEAppException;
	
	public void delete (TbDeliveryOrder pojo) throws VONEAppException;
	
	public TbBatchItem getBatchItemByBatchCode (String code) throws VONEAppException;
	
	public void doSearch(DOController ctr) throws VONEAppException;
	
	public void doSearchDO(DOController ctr) throws VONEAppException;
	
	public void redraw(DOController ctr, TbPurchaseOrder poPojo) throws VONEAppException;
	
	public void redrawExistingDO(DOController ctr) throws VONEAppException;
	
	public boolean isValidInput(DOController ctr) throws InterruptedException, VONEAppException;
	
	public void redraw(DOBatchController ctr) throws VONEAppException;
	
	public boolean doSave(DOBatchController ctr) throws VONEAppException, InterruptedException;
	
	public boolean isAllItemRegistered(DOBatchController ctr) throws InterruptedException, VONEAppException;
	
	public void doSaveAdd(DOController ctr, TbPurchaseOrder tbPO, TbDeliveryOrder tbDO) throws VONEAppException, InterruptedException;
}

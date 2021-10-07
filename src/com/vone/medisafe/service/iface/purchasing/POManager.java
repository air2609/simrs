package com.vone.medisafe.service.iface.purchasing;

import java.math.BigDecimal;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.ui.purchasing.POApproval;
import com.vone.medisafe.ui.purchasing.POController;

public interface POManager {
	
	public Integer getQtyOrderArrived(Integer prId, Integer itemId) throws VONEAppException;
	
	public void save (TbPurchaseOrder pojo) throws VONEAppException;
	
	public void update (TbPurchaseOrder pojo) throws VONEAppException;
	
	public TbPurchaseOrder getPOByCode(String code) throws VONEAppException;
	
	public void updateHeaderOnly (TbPurchaseOrder pojo) throws VONEAppException;
	
	public List searchPOByCodeStatus(String code, String status) throws VONEAppException;
	
	public BigDecimal getLastPrice (Integer itemId, MsVendor vendorId) throws VONEAppException;
	
	public List searchPOByCodeSupStatus(String poCode, String supCode, String status) throws VONEAppException;
	
	public List searchPOActiveByCodeSup(String poCode, String supCode) throws VONEAppException;
	
	public void updateChild(TbPurchaseOrderDetail pojo) throws VONEAppException;
	
	public void doSearchPOR(Component cmp) throws VONEAppException;
	
	public void doSearchSupPOController(Component cmp) throws VONEAppException;
	
	public void doSearchPO(Component cmp) throws VONEAppException;
	
	public void redraw(POController poC, TbPurchaseRequest porPojo, Component cmp) throws VONEAppException, InterruptedException;
	
	public void redrawPO(POController poC, TbPurchaseOrder tbPO, Component cmp) throws VONEAppException, InterruptedException;
	
	public void doFilterSup(POController poC, MsVendor supPojo, Listbox listTemp, Listbox list) throws VONEAppException, InterruptedException;
	
	public void doSearchApproval(Component cmp) throws VONEAppException;
	
	public void doApprove(POApproval ctr, TbPurchaseOrder pojo) throws InterruptedException, VONEAppException;
	
	public void redraw(POApproval ctr, TbPurchaseOrder pojo) throws InterruptedException, VONEAppException;
	
	public void addItem(POController ctr, MsItem itemPojo) throws VONEAppException;
}

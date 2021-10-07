package com.vone.medisafe.service.iface.purchasing;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.ui.purchasing.BufferController;
import com.vone.medisafe.ui.purchasing.PORController;

public interface PORManager {
	
	public void save(TbPurchaseRequest pojo) throws VONEAppException;
	
	public TbPurchaseRequest getTbPurchaseRequestByCode (String code) throws VONEAppException;
	
	public void update (TbPurchaseRequest pojo) throws VONEAppException;
	
	public void updatePojoOnly (TbPurchaseRequest pojo) throws VONEAppException;
	
	public List searchTbPurchaseRequestByCode (String code) throws VONEAppException;
	
	public List searchTbPurchaseRequestByCode(String code, String status) throws VONEAppException;
	
	public List searchTbPurchaseRequestByCodeForApproval (String code) throws VONEAppException;

	public void doSearchPORController(String code, Listbox listOpp) throws VONEAppException;
	
	public void doSearchPORApproval(Component cmp) throws VONEAppException;

	public boolean doSaveModifyPORController(Bandbox oppNo, Label status, Listbox list, UserInfoBean uib)throws InterruptedException, VONEAppException;

	public void doSaveAddPORController(Listbox list, Bandbox oppNo, Label status, Listbox location, String sq, 
			UserInfoBean uib) throws InterruptedException, VONEAppException;

	public void redrawSearchPORController(TbPurchaseRequest pojo, PORController ctr) 
		throws InterruptedException, VONEAppException;

	public void redrawPORController(PORController ctr) throws VONEAppException;
	
	public void redrawPORController(BufferController ctr) throws VONEAppException;
	
	public void addItem(MsItem itemPojo, Listbox list, PORController ctr) throws VONEAppException;
	
	public void addItemRequest(Object[] itemPojo, Listbox list, PORController ctr) throws VONEAppException;
	
	public void redrawPORApproval(TbPurchaseRequest pojo, Component cmp) throws VONEAppException;
	
	public void doApprove(TbPurchaseRequest pojo, UserInfoBean uib, Component cmp) throws InterruptedException, VONEAppException;
	
}

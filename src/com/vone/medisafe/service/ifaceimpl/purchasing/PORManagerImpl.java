package com.vone.medisafe.service.ifaceimpl.purchasing;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zhtml.A;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUserDAO;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.TbPurchaseRequestDAO;
import com.vone.medisafe.mapping.TbPurchaseRequestDetail;
import com.vone.medisafe.mapping.dao.MsWarehouseDAO;
import com.vone.medisafe.mapping.dao.item.ItemDAO;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.service.iface.purchasing.PORManager;
import com.vone.medisafe.ui.purchasing.BufferController;
import com.vone.medisafe.ui.purchasing.PORController;

public class PORManagerImpl implements PORManager{

	Logger logger = Logger.getLogger(PORManagerImpl.class);
	
	TbPurchaseRequestDAO dao;		
	MsWarehouseDAO whousedao;
	ItemDAO itemdao;
	MsUserDAO userdao;
	
	public MsUserDAO getUserdao() {
		return userdao;
	}

	public void setUserdao(MsUserDAO userdao) {
		this.userdao = userdao;
	}

	public MsWarehouseDAO getWhousedao() {
		return whousedao;
	}

	public void setWhousedao(MsWarehouseDAO whousedao) {
		this.whousedao = whousedao;
	}

	public TbPurchaseRequestDAO getDao() {
		return dao;
	}

	public void setDao(TbPurchaseRequestDAO dao) {
		this.dao = dao;
	}

	public void save(TbPurchaseRequest pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public TbPurchaseRequest getTbPurchaseRequestByCode (String code) throws VONEAppException{
		return this.dao.getTbPurchaseRequestByCode(code);
	}
	
	public void update (TbPurchaseRequest pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void updatePojoOnly (TbPurchaseRequest pojo) throws VONEAppException{
		this.dao.updatePojoOnly(pojo);
	}
	
	public List searchTbPurchaseRequestByCode (String code) throws VONEAppException{
		return this.dao.searchTbPurchaseRequestByCode(code);
	}
	
	public List searchTbPurchaseRequestByCode(String code, String status) throws VONEAppException {
		return this.dao.searchTbPurchaseRequestByCode(code, status);
	}
	
	public List searchTbPurchaseRequestByCodeForApproval (String code) throws VONEAppException {
		return this.dao.searchTbPurchaseRequestByCodeForApproval(code);
	}
	
	public void doSearchPORController(String code, Listbox listOpp) throws VONEAppException{
		
		listOpp.getItems().clear();		
		
		List list = this.dao.searchTbPurchaseRequestByCodeForApproval(code);
		
		if (list == null || list.size() < 1) return;
		
		Iterator it = list.iterator();				
		
		while (it.hasNext()){
			TbPurchaseRequest pojo = (TbPurchaseRequest)it.next();
			
			Listitem item = new Listitem();
			item.setParent(listOpp);
			
			item.appendChild(new Listcell(pojo.getVPrCode()));
			if (pojo.getMsUnitId() != null)
				item.appendChild(new Listcell(pojo.getMsUnitId().getVUnitName()));
			
			item.setValue(pojo);
		}
	}
	
	public void doSearchPORApproval(Component cmp) throws VONEAppException {
		Listbox listOpp = (Listbox)cmp.getFellow("listOpp");
		Textbox oppNoSearch = (Textbox)cmp.getFellow("oppNoSearch");
		
		List list = this.dao.searchTbPurchaseRequestByCodeForApproval("%"+oppNoSearch.getText()+"%");
		
		if (list == null || list.size() < 1) return;
		
		Iterator it = list.iterator();
		
		listOpp.getItems().clear();		
		
		while (it.hasNext()){
			TbPurchaseRequest pojo = (TbPurchaseRequest)it.next();
			
			Listitem item = new Listitem();
			item.setParent(listOpp);
			
			item.appendChild(new Listcell(pojo.getVPrCode()));
			if (pojo.getMsUnitId() != null)
				item.appendChild(new Listcell(pojo.getMsUnitId().getVUnitName()));
			
			item.setValue(pojo);
		}
	}

	public void redrawPORApproval(TbPurchaseRequest pojo, Component cmp) throws VONEAppException {

		Listbox list = (Listbox)cmp.getFellow("list");
		Textbox issuedBy = (Textbox)cmp.getFellow("issuedBy");
		Textbox approvedBy = (Textbox)cmp.getFellow("approvedBy");
		Label status = (Label)cmp.getFellow("status");
		
		pojo = this.dao.getTbPurchaseRequestByCode(pojo.getVPrCode());	
		
		approvedBy.setText("");
		
		issuedBy.setText(pojo.getMsStaffByNIssuerId().getVStaffCode()+"-"+pojo.getMsStaffByNIssuerId().getVStaffName());
		
		status.setValue("STATUS : "+ pojo.getVPrStatus());
		
		list.getItems().clear();
				
		Iterator it = pojo.getTbPurchaseRequestDetails().iterator();
		
		while (it.hasNext()){
			TbPurchaseRequestDetail pojoDet = (TbPurchaseRequestDetail)it.next();
			
			Integer qtyAvail;
			
			try{
			qtyAvail = this.whousedao.getQtyAvail(pojo.getMsUnitId().getMsWarehouse().getNWhouseId(), pojoDet.getMsItem().getNItemId());
			}catch(Exception e){
				qtyAvail = new Integer(0);
				logger.error(e);
			}
			
			Listitem item = new Listitem();
			item.setValue(pojoDet);
			item.setParent(list);
			
			item.appendChild(new Listcell(pojoDet.getMsItem().getVItemCode()));
			item.appendChild(new Listcell(pojoDet.getMsItem().getVItemName()));
			item.appendChild(new Listcell(pojoDet.getMsItem().getMsItemGroup().getVItemGroupCode()));
			item.appendChild(new Listcell(qtyAvail.toString()));
			if (pojoDet.getMsItem().getNItemBufferLimit() != null)
				item.appendChild(new Listcell(pojoDet.getMsItem().getNItemBufferLimit().toString()));
			else
				item.appendChild(new Listcell("0"));
			
			if (pojoDet.getMsItem().getnMaxOrder() != null)
				item.appendChild(new Listcell(pojoDet.getMsItem().getnMaxOrder().toString()));
			else
				item.appendChild(new Listcell("0"));
			
			if (pojoDet.getMsItemMeasurement() != null)
				item.appendChild(new Listcell(pojoDet.getMsItemMeasurement().getVMitemEarlyQuantify()));
			else
				item.appendChild(new Listcell("-"));
			
			item.appendChild(new Listcell(""+pojoDet.getNPrDetQtyRequested()));
		}			
	}
	
	public void doApprove(TbPurchaseRequest pojo, UserInfoBean uib, Component cmp) throws InterruptedException, VONEAppException {

		Listbox listOpp = (Listbox)cmp.getFellow("listOpp");
		Textbox approvedBy = (Textbox)cmp.getFellow("approvedBy");
		
		pojo = this.dao.getTbPurchaseRequestByCode(pojo.getVPrCode());	
		
		pojo.setVPrStatus(MedisafeConstants.PURCHASING_APPROVED);
		pojo.setMsStaffByNApproverId(uib.getMsUser().getMsStaff());	
		
		this.dao.updatePojoOnly(pojo);									
		
		Messagebox.show(MessagesService.getKey("pch.order.request.approve.success"));
		
		MsStaff msStaff = this.userdao.findById(uib.getMsUser().getNUserId()).getMsStaff();

		approvedBy.setText(msStaff.getVStaffCode()+"-"+msStaff.getVStaffName());
				
		if (listOpp.getSelectedItem() != null)
			listOpp.removeChild(listOpp.getSelectedItem());	
	}
	
	public boolean doSaveModifyPORController(Bandbox oppNo, Label status, Listbox list, UserInfoBean uib) throws InterruptedException, VONEAppException{
		// TODO Auto-generated method stub
				
		TbPurchaseRequest pojo = this.dao.getTbPurchaseRequestByCode(oppNo.getText());
		
		if (pojo == null){
			Messagebox.show(MessagesService.getKey("pch.order.request.no.opp"));
			return false;
		}
		
		if (!pojo.getVPrStatus().equals(MedisafeConstants.PURCHASING_OPEN)){
			Messagebox.show(MessagesService.getKey("pch.order.request.modify.status.invalid"));
			status.setValue("STATUS : "+pojo.getVPrStatus());
			return false;
		}
		
		Set<TbPurchaseRequestDetail> set = new HashSet<TbPurchaseRequestDetail>();
		pojo.setTbPurchaseRequestDetails(set);
		pojo.setVWhoChange(uib.getStUserId());
		pojo.setDWhnChange(new Date());	
		
		for (int i=0; i<list.getItemCount(); i++){			
			
			TbPurchaseRequestDetail reqDet = new TbPurchaseRequestDetail();
			set.add(reqDet);
			reqDet.setTbPurchaseRequest(pojo);
			
			Listitem item = (Listitem)list.getItemAtIndex(i);
			
			Listcell cell = (Listcell)item.getChildren().get(7);
			Intbox qty = (Intbox)cell.getChildren().get(0);
			
			reqDet.setMsItem((MsItem)item.getValue());	
			reqDet.setNPrDetQtyRequested(qty.getValue().shortValue());
			
			//ITEM MEASUREMENT
			cell = (Listcell)item.getChildren().get(6);
			Listbox box = (Listbox)cell.getChildren().get(0);
			Object obj = box.getSelectedItem().getValue();
			
			if (obj != null)
				reqDet.setMsItemMeasurement((MsItemMeasurement)obj);
			
			reqDet.setVWhoChange(uib.getStUserId());
			reqDet.setDWhnChange(new Date());	
			
		}		
		
		this.dao.update(pojo);	
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		return true;
	}

	public void doSaveAddPORController(Listbox list, Bandbox oppNo, 
			Label status, Listbox location, String sq, 
			UserInfoBean uib) throws InterruptedException, VONEAppException{
		// TODO Auto-generated method stub
		
		Set<TbPurchaseRequestDetail> set = new HashSet<TbPurchaseRequestDetail>();	
				
		TbPurchaseRequest req = new TbPurchaseRequest();
		req.setMsStaffByNIssuerId(uib.getMsUser().getMsStaff());
		
		MsVendor vendor = (MsVendor)list.getAttribute("supplier");
		
		req.setTbPurchaseRequestDetails(set);
		req.setVPrCode(sq);
		req.setVPrStatus(MedisafeConstants.PURCHASING_OPEN);
		req.setMsUnitId((MsUnit)location.getSelectedItem().getValue());
		req.setVWhoCreate(uib.getStUserId());
		req.setDWhnCreate(new Date());
		req.setSupplierId(vendor.getNVendorId());
					
		Iterator it = list.getItems().iterator();		
		
		while (it.hasNext()){
			TbPurchaseRequestDetail reqDet = new TbPurchaseRequestDetail();
			set.add(reqDet);
			
			Listitem item = (Listitem)it.next();
			
			Listcell cell = (Listcell)item.getChildren().get(7);
			Intbox qty = (Intbox)cell.getChildren().get(0);
			
			//ITEM MEASUREMENT
			cell = (Listcell)item.getChildren().get(6);
			Listbox box = (Listbox)cell.getChildren().get(0);
			Object obj = box.getSelectedItem().getValue();
			
			if (obj != null)
				reqDet.setMsItemMeasurement((MsItemMeasurement)obj);
			
			reqDet.setMsItem((MsItem)item.getValue());		
			reqDet.setNPrDetQtyRequested(qty.getValue().shortValue());			
			reqDet.setVWhoCreate(uib.getStUserId());
			reqDet.setDWhnCreate(new Date());						
		}
		
		this.dao.save(req);
		
		oppNo.setText(sq);
				
		status.setValue("STATUS : "+MedisafeConstants.PURCHASING_OPEN);
		
		Messagebox.show(MessagesService.getKey("pch.order.request.add.success")+sq);
	}

	public void redrawSearchPORController(TbPurchaseRequest pojo, PORController ctr) 
		throws InterruptedException, VONEAppException {
		
		pojo = this.dao.getTbPurchaseRequestByCode(pojo.getVPrCode());
		
		ctr.getIssuedBy().setText(pojo.getMsStaffByNIssuerId().getVStaffCode()+"-"+pojo.getMsStaffByNIssuerId().getVStaffName());
		
		ctr.getStatus().setValue("STATUS : "+ pojo.getVPrStatus());
		
		ctr.getList().getItems().clear();
				
		Iterator it = pojo.getTbPurchaseRequestDetails().iterator();
		
		while (it.hasNext()){
			TbPurchaseRequestDetail pojoDet = (TbPurchaseRequestDetail)it.next();
			
			Integer qtyAvail;
			
			try{
				qtyAvail = this.whousedao.getQtyAvail(pojo.getMsUnitId().getMsWarehouse().getNWhouseId(), pojoDet.getMsItem().getNItemId());
			}catch(Exception e){
				qtyAvail = new Integer(0);
				logger.error(e);
			}
			
			Listitem item = new Listitem();
			item.setValue(pojoDet.getMsItem());
			item.setParent(ctr.getList());
			
			item.appendChild(new Listcell(pojoDet.getMsItem().getVItemCode()));
			item.appendChild(new Listcell(pojoDet.getMsItem().getVItemName()));
			item.appendChild(new Listcell(pojoDet.getMsItem().getMsItemGroup().getVItemGroupCode()));
			item.appendChild(new Listcell(qtyAvail.toString()));
			if (pojoDet.getMsItem().getNItemBufferLimit() != null)
				item.appendChild(new Listcell(pojoDet.getMsItem().getNItemBufferLimit().toString()));
			else
				item.appendChild(new Listcell("0"));
			
			if(pojoDet.getMsItem().getnMaxOrder() != null)
				item.appendChild(new Listcell(pojoDet.getMsItem().getnMaxOrder().toString()));
			else item.appendChild(new Listcell("0"));
			
			//ITEM MEASUREMENT
			Listcell cellM = new Listcell();
			ctr.setListMeasurement();
			Listbox boxM = new Listbox();
			boxM.setMold("select");
			boxM.setStyle("width:100%;font-size:8pt");
			ctr.setListboxMeasurement(boxM);
			
						
			if (pojoDet.getMsItem().getMsItemMeasurement() != null){
				ctr.selectListboxMeasurement(boxM, pojoDet.getMsItemMeasurement().getVMitemEarlyQuantify());
				
			}
		
			boxM.setParent(cellM);
			
			item.appendChild(cellM);
			//END OF SETTING ITEM MEASUREMENT
				
			Listcell cell = new Listcell();
			cell.appendChild(new Intbox(pojoDet.getNPrDetQtyRequested()));
			item.appendChild(cell);		
			
		}
		
	}

	public void redrawPORController(PORController ctr) throws VONEAppException {
		// TODO Auto-generated method stub

		MsUnit unitPojo = (MsUnit)ctr.getLocation().getSelectedItem().getValue();
		Integer whouseId = unitPojo.getMsWarehouse().getNWhouseId();
		
		List listItem = this.whousedao.getItemUnderBuffer(whouseId);
		
		Iterator it = listItem.iterator();
		
		while (it.hasNext()){
			Object[] obj = (Object[])it.next();
			
			Integer qty = (Integer)obj[0];
			MsItem itemPojo = (MsItem)obj[1];
			
			addItem(itemPojo, qty, ctr.getList(), ctr);
		}
	}
	
	
	public void redrawPORController(BufferController ctr) throws VONEAppException {
		// TODO Auto-generated method stub

		MsUnit unitPojo = (MsUnit)ctr.getLocation().getSelectedItem().getValue();
		Integer whouseId = unitPojo.getMsWarehouse().getNWhouseId();
		
		List listItem = this.whousedao.getItemUnderBuffer(whouseId);
		
		Iterator it = listItem.iterator();
		
		while (it.hasNext()){
			Object[] obj = (Object[])it.next();
			
			Integer qty = (Integer)obj[0];
			MsItem itemPojo = (MsItem)obj[1];
			
			addItem(itemPojo, qty, ctr.getList(), ctr);
		}
	}
	
	private void addItem(MsItem itemPojo, Integer qty, Listbox list, BufferController ctr) throws VONEAppException{
		Listitem itemList = new Listitem();
		itemList.setParent(list);
		
		itemList.setValue(itemPojo);
		
		itemList.appendChild(new Listcell(itemPojo.getVItemCode()));
		itemList.appendChild(new Listcell(itemPojo.getVItemName()));
		itemList.appendChild(new Listcell(itemPojo.getMsItemGroup().getVItemGroupCode()));
		itemList.appendChild(new Listcell(qty.toString()));
		if (itemPojo.getNItemBufferLimit() != null)
			itemList.appendChild(new Listcell(itemPojo.getNItemBufferLimit().toString()));
		else
			itemList.appendChild(new Listcell("0"));
		
		
		//ITEM MEASUREMENT
		Listcell cellM = new Listcell();
		ctr.setListMeasurement();
		Listbox boxM = new Listbox();
		boxM.setMold("select");
		boxM.setStyle("width:100%;font-size:8pt");
		ctr.setListboxMeasurement(boxM);	
		if (itemPojo.getMsItemMeasurement() != null)
			ctr.selectListboxMeasurement(boxM, itemPojo.getMsItemMeasurement().getVMitemEarlyQuantify());
	
		boxM.setParent(cellM);
		
		itemList.appendChild(cellM);
		//END OF SETTING ITEM MEASUREMENT
		
		Listcell cell = new Listcell();
		cell.appendChild(new Intbox());
		itemList.appendChild(cell);	
		
		List<TbPurchaseRequestDetail> reqdet = this.whousedao.getOpenOpp(itemPojo);
		cell = new Listcell(reqdet.size()+"");
		if(reqdet.size() > 0){
			String purNo = "";
			for(TbPurchaseRequestDetail det : reqdet){
				purNo = purNo + det.getTbPurchaseRequest().getVPrCode()+",";
			}
			if(purNo.length()>0)purNo = purNo.substring(0, purNo.length()-1);
			cell.setTooltiptext(purNo);
		}
		itemList.appendChild(cell);

				
	}
		
	private void addItem(MsItem itemPojo, Integer qty, Listbox list, PORController ctr) throws VONEAppException{
		Listitem itemList = new Listitem();
		itemList.setParent(list);
		
		itemList.setValue(itemPojo);
		
		itemList.appendChild(new Listcell(itemPojo.getVItemCode()));
		itemList.appendChild(new Listcell(itemPojo.getVItemName()));
		itemList.appendChild(new Listcell(itemPojo.getMsItemGroup().getVItemGroupCode()));
		itemList.appendChild(new Listcell(qty.toString()));
		if (itemPojo.getNItemBufferLimit() != null)
			itemList.appendChild(new Listcell(itemPojo.getNItemBufferLimit().toString()));
		else
			itemList.appendChild(new Listcell("0"));
			
		if(itemPojo.getnMaxOrder() != null)
			itemList.appendChild(new Listcell(itemPojo.getnMaxOrder().toString()));
		else itemList.appendChild(new Listcell("0"));
		
		//ITEM MEASUREMENT
		Listcell cellM = new Listcell();
		ctr.setListMeasurement();
		Listbox boxM = new Listbox();
		boxM.setMold("select");
		boxM.setStyle("width:100%;font-size:8pt");
		ctr.setListboxMeasurement(boxM);	
		
		
		
		if (itemPojo.getMsItemMeasurement() != null)
			ctr.selectListboxMeasurement(boxM, itemPojo.getMsItemMeasurement().getVMitemEarlyQuantify());
	
		boxM.setParent(cellM);
		
		itemList.appendChild(cellM);
		//END OF SETTING ITEM MEASUREMENT

		
		Listcell cell = new Listcell();
		cell.appendChild(new Intbox());
		itemList.appendChild(cell);		
	}
	
	
	
	public void addItem(MsItem itemPojo, Listbox list, PORController ctr) throws VONEAppException{
		
		Listbox location = (Listbox)list.getFellow("location");
		
		itemPojo = this.itemdao.getById(itemPojo.getNItemId());
		
		//get WarehouseId
		MsUnit unitPojo = (MsUnit)location.getSelectedItem().getValue();
		Integer whouseId = unitPojo.getMsWarehouse().getNWhouseId();
		
		//getQty
		Integer qty = this.whousedao.getQtyAvail(whouseId, itemPojo.getNItemId());
		
		addItem(itemPojo, qty, list, ctr);
	}

	public ItemDAO getItemdao() {
		return itemdao;
	}

	public void setItemdao(ItemDAO itemdao) {
		this.itemdao = itemdao;
	}

	@Override
	public void addItemRequest(Object[] pojo, Listbox list,
			PORController ctr) throws VONEAppException {
		Listbox location = (Listbox)list.getFellow("location");
		Integer itemId = new Integer(pojo[1].toString());
		
		MsItem itemPojo = this.itemdao.getById(itemId);
		
		//get WarehouseId
		MsUnit unitPojo = (MsUnit)location.getSelectedItem().getValue();
		Integer whouseId = unitPojo.getMsWarehouse().getNWhouseId();
		
		//getQty
		Integer qty = this.whousedao.getQtyAvail(whouseId, itemPojo.getNItemId());
		
		addItem(itemPojo, qty, list, ctr);
		
	}

}

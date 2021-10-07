package com.vone.medisafe.ui.master.item;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class UnitInventoryController extends BaseController{

	public Listbox location = null;
	public Textbox itemCode = null;
	public Textbox batchNo = null;
	public Textbox cariTextbox = null;
	
	public Decimalbox qty = null;
	public Decimalbox cogsPrice = null;	
	
	ZulConstraint cst = new ZulConstraint();
	
	StringBuffer stMsg = new StringBuffer();
	
	public Listbox list = null;	
	
	private Session session = null;
	
	public static final String ITEM_INVENTORY_ATTRIBUTE = "ITEM_INVENTORY_ATTRIBUTE";
	public static final String BATCH_ATTRIBUTE = "BATCH_ATTRIBUTE";
	public static final String ITEM_ATTRIBUTE = "ITEM_ATTRIBUTE";
	
	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		location = (Listbox)cmp.getFellow("location");
		itemCode = (Textbox)cmp.getFellow("itemCode");		
		batchNo = (Textbox)cmp.getFellow("batchNo");
		list = (Listbox)cmp.getFellow("list");
		
		qty = (Decimalbox)cmp.getFellow("qty");
		cogsPrice = (Decimalbox)cmp.getFellow("cogsPrice");
		
		//set Search textbox
		cariTextbox = (Textbox)cmp.getFellow("cariTextbox");
		
		//SET CONSTRAINT
		cst.registerComponent(this.cariTextbox, ZulConstraint.UPPER_CASE);
		
		cst.registerComponent(this.itemCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(this.itemCode, ZulConstraint.NO_EMPTY);
		
		cst.registerComponent(this.batchNo, ZulConstraint.UPPER_CASE);
		cst.registerComponent(this.batchNo, ZulConstraint.NO_EMPTY);
		
		cst.registerComponent(this.qty, ZulConstraint.NO_EMPTY);
		
		cst.registerComponent(this.cogsPrice, ZulConstraint.NO_EMPTY);
		
		cst.validateComponent(false);
		
		
		//set Session
		session = Sessions.getCurrent();
		session.setAttribute(MedisafeConstants.ITEM_INVENTORY_SESSION, this);	
		
		//fill location						
		location.getItems().clear();
		List listWhouse = MasterServiceLocator.getWarehouseManager().getWhouseByStaffId(super.getUserInfoBean().getMsUser().getMsStaff().getNStaffId());
		listWhouse = MasterServiceLocator.getWarehouseManager().findAll();
		
		if (listWhouse == null){
			setErrorButton();
			
			return;
		}		
		
		Iterator it = listWhouse.iterator();
		
		while (it.hasNext()){			
			
			MsWarehouse whousePojo = (MsWarehouse)it.next();
			
			if (whousePojo == null)
				continue;			
			
			Listitem item = new Listitem();
			item.setParent(location);
			
			item.appendChild(new Listcell(whousePojo.getVWhouseCode()+" - "+whousePojo.getVWhouseName()));
			item.setValue(whousePojo);
		}
		
		if (location.getItemCount() > 0)
			location.setSelectedIndex(0);
		//end filling location
		
		redraw();
		
		location.addEventListener("onSelect", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redraw();
				} catch (VONEAppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			
		});
	}
	
	public void redrawByCriteria() throws VONEAppException {
		
		Service.getItemInventoryManager().redrawByCriteria(this);
		
		this.cariTextbox.select();
	}
	
	public void addItem(MsItem itemPojo, int qty, String batchNo) throws InterruptedException, VONEAppException {
		
		TbItemInventory iiPojo = new TbItemInventory();
		iiPojo.setMsItem(itemPojo);
		iiPojo.setMsWarehouse((MsWarehouse)this.location.getSelectedItem().getValue());
		iiPojo.setNItemInvQty(new Integer(qty).shortValue());
		iiPojo.setVWhoCreate(super.getUserInfoBean().getStUserId());
		iiPojo.setDWhnCreate(new Date());
		
		Service.getItemInventoryManager().save(iiPojo);
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		
		Listitem item = new Listitem();
		item.setParent(list);
		
		//kode item
		item.appendChild(new Listcell(itemPojo.getVItemCode()));
		
		//no batch
		item.appendChild(new Listcell());
		
		//nama			
		item.appendChild(new Listcell(itemPojo.getVItemName()));
		
		//qty			
		item.appendChild(new Listcell(""+qty));

	}
	
	public void doClear() throws InterruptedException,VONEAppException {
		
		this.itemCode.setText("");
		this.batchNo.setText("");
		this.qty.setValue(new BigDecimal(0));
		this.cogsPrice.setValue(new BigDecimal(0));
		
		redraw();
	}
	
	public void doSearch() throws InterruptedException,VONEAppException {
		
		redraw();
	}
	
	private void redraw() throws InterruptedException,VONEAppException {
		
		this.cariTextbox.setText("");
		this.doCancel(this.getList());
		
		Service.getItemInventoryManager().redrawByCriteria(this);
				
	}
	

	@Override
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	@Override
	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int respond = 0;
		
		stMsg.setLength(0);
		stMsg.append(MessagesService.getKey("master.iteminventory.manipulate.warning"))
		.append(MessagesService.getKey("common.delete.confirm"));
		
		respond = Messagebox.show(stMsg.toString(), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;			
		
		super.doDelete(cmp);
		
		Service.getItemInventoryManager().delete((TbItemInventory)list.getSelectedItem().getValue());
		
		list.removeChild(list.getSelectedItem());
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));	
	}

	public void setErrorButton(){
		
		this.btnDelete.setDisabled(true);		
	}

	@Override
	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		
		if (this.list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));			
			return;
		}
		
		this.itemCode.setDisabled(true);
		this.batchNo.setDisabled(true);
		this.cogsPrice.setDisabled(true);
		
		Listitem item = this.list.getSelectedItem();
		
		TbItemInventory tbII = (TbItemInventory)item.getValue();
		TbBatchItem tbBI = (TbBatchItem)item.getAttribute(this.BATCH_ATTRIBUTE);
		MsItem msI = (MsItem)item.getAttribute(this.ITEM_ATTRIBUTE);
		
		this.itemCode.setValue(msI.getVItemCode());
		this.qty.setValue(new BigDecimal(tbII.getNItemInvQty()));
		this.batchNo.setValue(tbBI.getVBatchNo());
		this.cogsPrice.setValue(new BigDecimal(tbBI.getNCogsPrice()));
		
		//SET ATTRIBUTE TO ITEM CODE
		this.itemCode.setAttribute(this.ITEM_INVENTORY_ATTRIBUTE,tbII);
		
		this.qty.select();
		
	}
	
	private void initComponent(){
		
		this.itemCode.setDisabled(false);
		this.batchNo.setDisabled(false);
		this.qty.setDisabled(false);
		this.cogsPrice.setDisabled(false);
		
	}

	@Override
	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (!cst.validateComponent(true))
			return;
		
		super.doSave(cmp);
	}

	@Override
	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		
		Messagebox.show("PENAMBAHAN ITEM MANUAL TIDAK DI-IMPLEMENTASIKAN, UNTUK MENGHINDARI BATCH NO HANTU/TIDAK PUNYA HISTORY!");
		
		doCancel(cmp);
	}

	@Override
	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
					
		TbItemInventory tbII = (TbItemInventory)this.itemCode.getAttribute(this.ITEM_INVENTORY_ATTRIBUTE);
		tbII.setNItemInvQty(this.qty.getValue().floatValue());
		
		Service.getItemInventoryManager().update(tbII);
		
		redrawByCriteria();
		
		doCancel(cmp);
	}

	public Textbox getCariTextbox() {
		return cariTextbox;
	}

	public void setCariTextbox(Textbox cariTextbox) {
		this.cariTextbox = cariTextbox;
	}

	public Textbox getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Textbox batchNo) {
		this.batchNo = batchNo;
	}

	public Decimalbox getCogsPrice() {
		return cogsPrice;
	}

	public void setCogsPrice(Decimalbox cogsPrice) {
		this.cogsPrice = cogsPrice;
	}

	public Textbox getItemCode() {
		return itemCode;
	}

	public void setItemCode(Textbox itemCode) {
		this.itemCode = itemCode;
	}

	public Listbox getLocation() {
		return location;
	}

	public void setLocation(Listbox location) {
		this.location = location;
	}

	public Decimalbox getQty() {
		return qty;
	}

	public void setQty(Decimalbox qty) {
		this.qty = qty;
	}

	@Override
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		initComponent(); 
		
		this.itemCode.setText("");
		this.batchNo.setText("");
		this.qty.setValue(new BigDecimal(0));
		this.cogsPrice.setValue(new BigDecimal(0));
		
		this.itemCode.focus();
	}
}

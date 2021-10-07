package com.vone.medisafe.ui.master.item;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.ui.base.BaseController;

public class UpdateInventoryController extends BaseController{

	public Textbox itemCode = null;
	public Textbox batchNo = null;
	public Textbox cariTextbox = null;
	ItemManager itemManager = MasterServiceLocator.getItemManager(); 
	
	public Decimalbox qty = null;
	
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
		super.init(cmp);
		
		itemCode = (Textbox)cmp.getFellow("itemCode");		
		batchNo = (Textbox)cmp.getFellow("batchNo");
		list = (Listbox)cmp.getFellow("list");
		
		qty = (Decimalbox)cmp.getFellow("qty");

		
		cariTextbox = (Textbox)cmp.getFellow("cariTextbox");
		
		cst.registerComponent(this.cariTextbox, ZulConstraint.UPPER_CASE);
		
		cst.registerComponent(this.itemCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(this.itemCode, ZulConstraint.NO_EMPTY);
		
		cst.registerComponent(this.batchNo, ZulConstraint.UPPER_CASE);
		cst.registerComponent(this.batchNo, ZulConstraint.NO_EMPTY);
		
		cst.registerComponent(this.qty, ZulConstraint.NO_EMPTY);
		
		cst.validateComponent(false);
		
		redrawByCriteria();
	}
	
	public void redrawByCriteria() throws VONEAppException {
		this.list.getItems().clear();
		
		List listItem = itemManager.searchItemUnderBuffer("", cariTextbox.getText());	
		Iterator it = listItem.iterator();
		while (it.hasNext()){
			Object[] pojo = (Object[])it.next();
			
			if(pojo[0].toString().equals("0")){
			
			Listitem item = new Listitem();
			item.setParent(this.list);
			item.setValue(pojo);
			
			item.appendChild(new Listcell(pojo[3].toString()));
			item.appendChild(new Listcell(pojo[4].toString()));
			item.appendChild(new Listcell(pojo[0].toString()));
			}
			
		}
	}
	
	public void addItem(MsItem itemPojo, int qty, String batchNo) throws InterruptedException, VONEAppException {
		
		TbItemInventory iiPojo = new TbItemInventory();
		iiPojo.setMsItem(itemPojo);
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
		
		redraw();
	}
	
	public void doSearch() throws InterruptedException,VONEAppException {
		
		redrawByCriteria();
	}
	
	private void redraw() throws InterruptedException,VONEAppException {
		
		this.cariTextbox.setText("");
		this.doCancel(this.getList());
		
//		Service.getItemInventoryManager().redrawByCriteria(this);
				
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

		
		Listitem item = this.list.getSelectedItem();
		Object[] o = (Object[])item.getValue();
				
		this.itemCode.setValue(o[3].toString());
		this.qty.setValue(new BigDecimal(o[0].toString()));
		this.batchNo.setValue(o[4].toString());
		
		this.qty.select();
		
	}
	
	private void initComponent(){
		
		this.itemCode.setDisabled(false);
		this.batchNo.setDisabled(false);
		this.qty.setDisabled(false);
		
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
		
		MsItem msItem = MasterServiceLocator.getItemManager().getItemByCode(itemCode.getValue());
		
		TbItemInventory tbII = Service.getItemInventoryManager().getLastInventory(msItem);
		tbII.setNItemInvQty(this.qty.getValue().floatValue());
		
		Service.getItemInventoryManager().update(tbII);
		
		Messagebox.show("DATA SUKSES DIUPDATED!");
		
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


	public Textbox getItemCode() {
		return itemCode;
	}

	public void setItemCode(Textbox itemCode) {
		this.itemCode = itemCode;
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

		
		this.itemCode.focus();
	}
}

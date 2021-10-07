package com.vone.medisafe.ui.master.item;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class VendorController extends BaseController{
	private static final Logger log = Logger.getLogger(VendorController.class);
	ZulConstraint constraints = new ZulConstraint();
	
	
	public Textbox vendorCode;
	public Textbox vendorName;
	public Textbox vendorAddress;
	public Textbox telpNo;
	public Textbox alvTelpNo;
	public Textbox faxNo;
	public Textbox contactPerson;
	public Listbox supplierList;
	public Bandbox coa;
	Textbox cariTextbox;

	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("VendorController -- doCancel() -- MASUK");
		
		super.doCancel(win);
		
		vendorAddress.setValue(null);
		vendorCode.setValue(null);
		vendorName.setValue(null);
		telpNo.setValue(null);
		alvTelpNo.setValue(null);
		faxNo.setValue(null);
		contactPerson.setValue(null);
		coa.setText("");
		coa.removeAttribute("coa");
		vendorCode.focus();
		log.debug("VendorController -- doCancel() -- KELUAR");
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		super.doDelete(win);
		
		if(supplierList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.vendor.vendorlist.notselected"));
			return;
		}
		
		MsVendor vendor = (MsVendor)supplierList.getSelectedItem().getValue();
		int selectedItem = supplierList.getSelectedIndex();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		if(MasterServiceLocator.getVendorManager().delete(vendor)){
			log.debug("VendorController -- doDelete() -- MENGHAPUS DATA VENDOR SUKSES");
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			supplierList.removeItemAt(selectedItem);
		}
		else{
			log.debug("VendorController -- doDelete() -- MENGHAPUS DATA VENDOR GAGAL");
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
		log.debug("VendorController -- doDelete() -- KELUAR");
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		
		log.debug("VendorController -- doModify() -- MASUK");
		
		super.doModify(win);
		
		if(supplierList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.vendor.vendorlist.notselected"));
			return;
		}
		
		MasterServiceLocator.getVendorManager().prepreModify(this);
		
		
		
		
		log.debug("VendorController -- doModify() -- KELUAR");
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("VendorController -- doSaveAdd() -- MASUK");
				super.doSaveAdd(win);
		
		Listcell cell = null;
		
		MsVendor vendor = new MsVendor();
		vendor.setVVendorAddress(vendorAddress.getValue());
		vendor.setVVendorAltContactNo(alvTelpNo.getValue());
		vendor.setVVendorCode(vendorCode.getText());
		vendor.setVVendorContactNo(telpNo.getText());
		vendor.setVVendorContactPerson(contactPerson.getText());
		vendor.setVVendorName(vendorName.getText());
		vendor.setVVendorFaxNo(faxNo.getText());
		vendor.setMsCoa((MsCoa)coa.getAttribute("coa"));
		
		if(MasterServiceLocator.getVendorManager().save(vendor)){
			log.debug("VendorController -- doSaveAdd() -- MENAMBAH DATA VENDOR SUKSES");
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			Listitem item = new Listitem();
			item.setValue(vendor);
			
			cell = new Listcell(vendor.getVVendorCode());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorName());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorAddress());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactPerson());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactNo());
			cell.setParent(item);
			
			supplierList.appendChild(item);
			
			doCancel(win);
		}
		else{
			log.debug("VendorController -- doSaveAdd() -- MENAMBAH DATA VENDOR GAGAL");
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		log.debug("VendorController -- doSaveAdd() -- KELUAR");
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("VendorController -- doSaveModify() -- MASUK");
				
		super.doSaveModify(win);
		
		Listitem item = supplierList.getSelectedItem();
		MsVendor vendor = (MsVendor)item.getValue();
		item.getChildren().clear();
		vendor.setVVendorAddress(vendorAddress.getValue());
		vendor.setVVendorAltContactNo(alvTelpNo.getValue());
		vendor.setVVendorCode(vendorCode.getText());
		vendor.setVVendorContactNo(telpNo.getText());
		vendor.setVVendorContactPerson(contactPerson.getText());
		vendor.setVVendorName(vendorName.getText());
		vendor.setVVendorFaxNo(faxNo.getText());
		vendor.setMsCoa((MsCoa)coa.getAttribute("coa"));
		
		Listcell cell = null;
		
		if(MasterServiceLocator.getVendorManager().save(vendor)){
			log.debug("VendorController -- doSaveModify() -- MENGUBAH DATA SUKSES");
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.setValue(vendor);
			cell = new Listcell(vendor.getVVendorCode());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorName());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorAddress());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactPerson());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactNo());
			cell.setParent(item);
			supplierList.removeItemFromSelection(item);
			doCancel(win);
		}else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		log.debug("VendorController -- doSaveModify() -- KELUAR");
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		
		vendorCode = (Textbox)win.getFellow("vendorCode");
		vendorName = (Textbox)win.getFellow("vendorName");
		vendorAddress = (Textbox)win.getFellow("vendorAddress");
		telpNo = (Textbox)win.getFellow("telpNo");
		alvTelpNo = (Textbox)win.getFellow("alvTelpNo");
		faxNo = (Textbox)win.getFellow("faxNo");
		contactPerson = (Textbox)win.getFellow("contactPerson");
		supplierList = (Listbox)win.getFellow("supplierList");
		coa = (Bandbox)win.getFellow("coa");
		//search component
		cariTextbox = (Textbox)win.getFellow("cariTextbox");
	
		super.init(win);
		
		constraints.registerComponent(cariTextbox,ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(vendorCode,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(vendorCode,ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(vendorName,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(vendorName,ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(contactPerson,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(contactPerson,ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(vendorAddress,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(vendorAddress,ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(telpNo,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(alvTelpNo,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(faxNo,ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		getVendorDataList(supplierList);
		
		log.debug("VendorController -- INIT -- KELUAR");
	}
	
	public void redrawListVendorByCriteria(VendorController ctr) throws VONEAppException {
		MasterServiceLocator.getVendorManager().searchVendoryByCriteria(ctr);
	}
	
	public void getVendorDataList(Listbox vendorList) throws VONEAppException{
		log.debug("VendorController -- getVendorDataList() -- MASUK");
		vendorList.getItems().clear();
		Listitem item = null;
		Listcell cell = null;
		MsVendor vendor = null;
		
		List vendors = MasterServiceLocator.getVendorManager().getAllVendor();
		Iterator it = vendors.iterator();
		while(it.hasNext()){
			vendor = (MsVendor)it.next();
			item = new Listitem();
			item.setValue(vendor);
			item.setParent(vendorList);
			
			cell = new Listcell(vendor.getVVendorCode());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorName());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorAddress());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactPerson());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactNo());
			cell.setParent(item);
		}
		MiscTrxController.setFont(vendorList);
		log.debug("VendorController -- getVendorDataList() -- KELUAR");
	}
	
	/**
	 * added at 29-09-2006
	 * @param vendorList
	 * @throws VONEAppException 
	 */
	public static void getVendorList(Listbox vendorList) throws VONEAppException{
		
		log.debug("VendorController -- getVendorList() -- MASUK");
		vendorList.getItems().clear();
		Listitem item = null;
		MsVendor vendor = null;
		
		List vendors = MasterServiceLocator.getVendorManager().getAllVendor();
		Iterator it = vendors.iterator();
		while(it.hasNext()){
			vendor = (MsVendor)it.next();
			item = new Listitem();
			item.setValue(vendor);
			item.setLabel(vendor.getVVendorName());
			item.setParent(vendorList);
		}
		MiscTrxController.setFont(vendorList);
//		if(vendorList.getItems().size()>0)vendorList.setSelectedIndex(0);
		log.debug("VendorController -- getVendorList() -- KELUAR");
		
	}

	public Textbox getCariTextbox() {
		return cariTextbox;
	}

	public void setCariTextbox(Textbox cariTextbox) {
		this.cariTextbox = cariTextbox;
	}

	public Textbox getAlvTelpNo() {
		return alvTelpNo;
	}

	public void setAlvTelpNo(Textbox alvTelpNo) {
		this.alvTelpNo = alvTelpNo;
	}

	public Bandbox getCoa() {
		return coa;
	}

	public void setCoa(Bandbox coa) {
		this.coa = coa;
	}

	public ZulConstraint getConstraints() {
		return constraints;
	}

	public void setConstraints(ZulConstraint constraints) {
		this.constraints = constraints;
	}

	public Textbox getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(Textbox contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Textbox getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(Textbox faxNo) {
		this.faxNo = faxNo;
	}

	public Listbox getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(Listbox supplierList) {
		this.supplierList = supplierList;
	}

	public Textbox getTelpNo() {
		return telpNo;
	}

	public void setTelpNo(Textbox telpNo) {
		this.telpNo = telpNo;
	}

	public Textbox getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(Textbox vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public Textbox getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(Textbox vendorCode) {
		this.vendorCode = vendorCode;
	}

	public Textbox getVendorName() {
		return vendorName;
	}

	public void setVendorName(Textbox vendorName) {
		this.vendorName = vendorName;
	}

}

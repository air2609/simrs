package com.vone.medisafe.ui.master.item;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;



public class ItemMeasurementController extends BaseController{
	
	private static final Logger log = Logger.getLogger(ItemMeasurementController.class);
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("ItemMeasurementController -- doCancel() -- MASUK ");
		Textbox earlyQuantify = (Textbox)win.getFellow("earlyQuantify");
		Textbox endQuantify = (Textbox)win.getFellow("endQuantify");
		Intbox endQuantity = (Intbox)win.getFellow("endQuantity");
		super.doCancel(win);
		earlyQuantify.setValue(null);
		endQuantify.setValue(null);
		endQuantity.setValue(null);
		earlyQuantify.focus();
		log.debug("ItemMeasurementController -- doCancel() -- KELUAR ");
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("ItemMeasurementController -- doDelete() -- MASUK ");
		Listbox itemMeasurementList = (Listbox)win.getFellow("itemMeasurementList");
		
		super.doDelete(win);
		
		MsItemMeasurement vendor = (MsItemMeasurement)itemMeasurementList.getSelectedItem().getValue();
		int selectedItem = itemMeasurementList.getSelectedIndex();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		if(MasterServiceLocator.getItemMeasurementManager().delete(vendor)){
			log.debug("ItemMeasurementController -- doDelete() -- MENGHAPUS ITEM MEASUREMENT SUKSES ");
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			itemMeasurementList.removeItemAt(selectedItem);
		}else{
			log.debug("ItemMeasurementController -- doDelete() -- MENGHAPUS ITEM MEASUREMENT GAGAL ");
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
		log.debug("ItemMeasurementController -- doDelete() -- KELUAR ");
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("ItemMeasurementController -- doModify() -- MASUK ");
		Textbox earlyQuantify = (Textbox)win.getFellow("earlyQuantify");
		Textbox endQuantify = (Textbox)win.getFellow("endQuantify");
		Intbox endQuantity = (Intbox)win.getFellow("endQuantity");
		Listbox itemMeasurementList = (Listbox)win.getFellow("itemMeasurementList");
		
		super.doModify(win);
		if(itemMeasurementList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.itemmeasurement.list.notselected"));
			return;
		}
		MsItemMeasurement itemMeasurement = (MsItemMeasurement)itemMeasurementList.getSelectedItem().getValue();
		earlyQuantify.setText(itemMeasurement.getVMitemEarlyQuantify());
		endQuantify.setText(itemMeasurement.getVMitemEndQuantify());
		endQuantity.setValue(new Integer(itemMeasurement.getNMitemEndQty()));
		log.debug("ItemMeasurementController -- doModify() -- KELUAR ");
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("ItemMeasurementController -- doSaveAdd() -- MASUK ");
		Textbox earlyQuantify = (Textbox)win.getFellow("earlyQuantify");
		Textbox endQuantify = (Textbox)win.getFellow("endQuantify");
		Intbox endQuantity = (Intbox)win.getFellow("endQuantity");
		Listbox itemMeasurementList = (Listbox)win.getFellow("itemMeasurementList");
		
		super.doSaveAdd(win);
		
		Listitem item = null;
		Listcell cell = null;
		
		MsItemMeasurement itemMeasurement = new MsItemMeasurement();
		itemMeasurement.setVMitemEarlyQuantify(earlyQuantify.getText());
		itemMeasurement.setVMitemEndQuantify(endQuantify.getText());
		itemMeasurement.setNMitemEndQty(endQuantity.getValue().shortValue());
		if(MasterServiceLocator.getItemMeasurementManager().save(itemMeasurement)){
			log.debug("ItemMeasurementController -- doSaveAdd() -- MENAMBAH DATA ITEM MEASUREMENT SUKSES ");
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setValue(itemMeasurement);
			
			cell = new Listcell(itemMeasurement.getVMitemEarlyQuantify());
			cell.setParent(item);
			
			cell = new Listcell(itemMeasurement.getVMitemEndQuantify());
			cell.setParent(item);
			
			cell = new Listcell(""+itemMeasurement.getNMitemEndQty());
			cell.setParent(item);
			
			itemMeasurementList.appendChild(item);
			
			doCancel(win);
		}else{
			log.debug("ItemMeasurementController -- doSaveAdd() -- MENAMBAH ITEM MEASUREMENT GAGAL");
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		log.debug("ItemMeasurementController -- doSaveAdd() -- KELUAR ");
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("ItemMeasurementController -- doSaveModify() -- MASUK ");
		Textbox earlyQuantify = (Textbox)win.getFellow("earlyQuantify");
		Textbox endQuantify = (Textbox)win.getFellow("endQuantify");
		Intbox endQuantity = (Intbox)win.getFellow("endQuantity");
		Listbox itemMeasurementList = (Listbox)win.getFellow("itemMeasurementList");
		
		super.doSaveModify(win);
		
		Listitem item = itemMeasurementList.getSelectedItem();
		Listcell cell = null;
		MsItemMeasurement itemMeasurement = (MsItemMeasurement)item.getValue();
		itemMeasurement.setVMitemEarlyQuantify(earlyQuantify.getText());
		itemMeasurement.setVMitemEndQuantify(endQuantify.getText());
		itemMeasurement.setNMitemEndQty(endQuantity.getValue().shortValue());
		if(MasterServiceLocator.getItemMeasurementManager().save(itemMeasurement)){
			log.debug("ItemMeasurementController -- doSaveModify() -- MENGUBAH ITEM MEASUREMENT SUKSES ");
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(itemMeasurement);
			
			cell = new Listcell(itemMeasurement.getVMitemEarlyQuantify());
			cell.setParent(item);
			
			cell = new Listcell(itemMeasurement.getVMitemEndQuantify());
			cell.setParent(item);
			
			cell = new Listcell(""+itemMeasurement.getNMitemEndQty());
			cell.setParent(item);
			
			itemMeasurementList.removeItemFromSelection(item);
			
			doCancel(win);
		}else{
			log.debug("ItemMeasurementController -- doSaveModify() -- MEGHUBAH ITEM MEASUREMENT GAGAL ");
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		log.debug("ItemMeasurementController -- doSaveModify() -- KELUAR ");
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("ItemMeasurementController -- init() -- MASUK ");
		Textbox earlyQuantify = (Textbox)win.getFellow("earlyQuantify");
		Textbox endQuantify = (Textbox)win.getFellow("endQuantify");
		Intbox endQuantity = (Intbox)win.getFellow("endQuantity");
		Listbox itemMeasurementList = (Listbox)win.getFellow("itemMeasurementList");
		
		super.init(win);
		
		constraints.registerComponent(earlyQuantify, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(earlyQuantify, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(endQuantify, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(endQuantify, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(endQuantity, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
		getAllDataItemMeasurement(itemMeasurementList);
		log.debug("ItemMeasurementController -- init() -- KELUAR ");
		
	}
	
	public void getAllDataItemMeasurement(Listbox itemMeasurementList) throws VONEAppException{
		log.debug("ItemMeasurementController -- getAllDataItemMeasurement() -- MASUK ");
		
		itemMeasurementList.getItems().clear();
		Listitem item = null;
		Listcell cell = null;
		MsItemMeasurement itemMeasurement = null;
		
		List measurements = MasterServiceLocator.getItemMeasurementManager().getAllItemMeasurement();
		Iterator it = measurements.iterator();
		while(it.hasNext()){
			itemMeasurement = (MsItemMeasurement)it.next();
			item = new Listitem();
			item.setValue(itemMeasurement);
			item.setParent(itemMeasurementList);
			
			cell = new Listcell(itemMeasurement.getVMitemEarlyQuantify());
			cell.setParent(item);
			
			cell = new Listcell(itemMeasurement.getVMitemEndQuantify());
			cell.setParent(item);
			
			cell = new Listcell(""+itemMeasurement.getNMitemEndQty());
			cell.setParent(item);
						
		}
		MiscTrxController.setFont(itemMeasurementList);
		
		log.debug("ItemMeasurementController -- getAllDataItemMeasurement() -- KELUAR ");
		
	}

	/**
	 * added at 29-09-2006
	 * @param itemMeasurementList
	 * @throws VONEAppException 
	 */
	public static void getItemMeasurementLastQuantityForSelect(Listbox itemMeasurementList) throws VONEAppException{
		log.debug("ItemMeasurementController -- getItemMeasurementLastQuantityForSelect() -- MASUK ");
		
		itemMeasurementList.getItems().clear();
		Listitem item = null;
		MsItemMeasurement itemMeasurement = null;
		
		List measurements = MasterServiceLocator.getItemMeasurementManager().getAllItemMeasurement();
		Iterator it = measurements.iterator();
		while(it.hasNext()){
			itemMeasurement = (MsItemMeasurement)it.next();
			item = new Listitem();
			item.setValue(itemMeasurement);
			item.setParent(itemMeasurementList);
			item.setLabel(itemMeasurement.getVMitemEarlyQuantify());
		}
		if(itemMeasurementList.getItems().size()>0)itemMeasurementList.setSelectedIndex(0);
		
		log.debug("ItemMeasurementController -- getItemMeasurementLastQuantityForSelect() -- KELUAR ");
		
	}
}

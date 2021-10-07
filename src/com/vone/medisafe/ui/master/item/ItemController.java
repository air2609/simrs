package com.vone.medisafe.ui.master.item;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemGroup;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.ui.base.BaseController;

public class ItemController extends BaseController{
	private static final Logger log = Logger.getLogger(ItemController.class);
	private ZulConstraint constrains = new ZulConstraint();
	private MsItem msItem;
	
	public Listbox itemGroupList;
	public Textbox itemCode;
	public Textbox itemName;
	public Listbox drugTypeList;
	public Textbox barcode;
	public Listbox supplier;
	public Listbox medicineDesc;
	public Intbox plafon;
	public Intbox bufferLimit;
	public Listbox measureItemList;
	public Intbox maxOrder;
	public Intbox jasaR;
	//public Bandbox coa;
//	Listbox invAccNo;
	public Listbox itemList;
	
	Listitem item;
	Listcell cell;
	
	static ItemManager itemServ = MasterServiceLocator.getItemManager();

	public void doCancel(Component win) throws InterruptedException, VONEAppException {
						
				
		super.doCancel(win);
		
		if(itemGroupList.getItems().size() > 0)itemGroupList.setSelectedIndex(0);
		itemName.setValue(null);
		itemCode.setValue(null);
		barcode.setValue(null);
		jasaR.setValue(null);
		supplier.clearSelection();
		plafon.setValue(null);
		bufferLimit.setValue(null);
		maxOrder.setValue(null);
		if(measureItemList.getItems().size() > 0)measureItemList.setSelectedIndex(0);
		

		medicineDesc.clearSelection();
		itemGroupList.focus();
		itemList.clearSelection();
		drugTypeList.clearSelection();
		
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		log.debug("ItemController -- doDelete() -- MASUK");
			
		super.doDelete(win);
		if(itemList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.item.list.notselected"));
			return;
		}
		msItem = (MsItem)itemList.getSelectedItem().getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = itemList.getSelectedIndex();
		if(itemServ.delete(msItem)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			itemList.removeItemAt(indexSelected);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
		log.debug("ItemController -- doDelete() -- KELUAR");
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
				
		super.doModify(win); 
		if(itemList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.item.list.notselected"));
			btnModify.setDisabled(false);
			btnCancel.setDisabled(true);
			btnDelete.setDisabled(false);
			return;
		}
		ItemManager service = MasterServiceLocator.getItemManager();
		
		
		service.doModify(this);
				
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constrains.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		log.debug("ItemController -- doSaveAdd() -- MASUK");
		
						
		super.doSaveAdd(win);
		
		 msItem = new MsItem();
		
		MsItemGroup itemGroup = (MsItemGroup)itemGroupList.getSelectedItem().getValue();
		MsItemMeasurement itemMeasurement = (MsItemMeasurement)measureItemList.getSelectedItem().getValue();
		
		MsCoa coaCogs = new MsCoa();
		coaCogs.setNCoaId(2);
		msItem.setItemCogsNo(coaCogs);
		
		MsCoa coaSelling = new MsCoa();
		coaSelling.setNCoaId(1);
		msItem.setItemSellAcctNo(coaSelling);
		
		
		msItem.setMsItemGroup(itemGroup);
		msItem.setVItemName(itemName.getText());
		msItem.setNR(jasaR.getValue().shortValue());
		msItem.setVItemCode(itemCode.getText());
		msItem.setnMaxOrder(maxOrder.getValue());
		if(barcode.getValue().length()>0)msItem.setVBarcodeNo(barcode.getValue());
		msItem.setMsItemMeasurement(itemMeasurement);
		if(bufferLimit.getValue() != null)msItem.setNItemBufferLimit(new Short(bufferLimit.getValue().shortValue()));
		if(plafon.getValue() != null)msItem.setNItemPlafon(new Short(plafon.getValue().shortValue()));
				
		msItem.setVItemReturnable(medicineDesc.getSelectedItem().getValue().toString());
		msItem.setDWhnCreate(new Date());
		
		if(itemGroupList.getSelectedItem().getLabel().equals("OBAT")){
			if(drugTypeList.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
				Messagebox.show(MessagesService.getKey("drugs.type.not.selected"));
				drugTypeList.focus();
				return;
			}
			msItem.setNItemType(new Short(drugTypeList.getSelectedItem().getValue().toString()));
		}
		
				
		if(itemServ.save(msItem, supplier.getSelectedItems())){
			log.debug("MENYIMPAN DATA ITEM DAN DATA ITEM SUPPLIED SUKSES");
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setValue(msItem);
			cell = new Listcell(msItem.getVItemCode());
			cell.setParent(item);
			
			cell = new Listcell(msItem.getVItemName());
			cell.setParent(item);
			
			cell = new Listcell(msItem.getMsItemGroup().getVItemGroupName());
			cell.setParent(item);
			
			Set suppliers = supplier.getSelectedItems();
			log.debug("BANYAKNYA SUPPLIER UNTUK ITEM INI : "+suppliers.size());
			
			String namaSupplier ="";
			Iterator itr = suppliers.iterator();
			while(itr.hasNext()){
				Listitem listSupllier = (Listitem)itr.next();
				namaSupplier = namaSupplier + listSupllier.getLabel()+";";
			}
			if(namaSupplier.length()> 1)namaSupplier = namaSupplier.substring(0,(namaSupplier.length()-1));
			cell = new Listcell(namaSupplier);
			cell.setParent(item);
			
			cell = new Listcell(msItem.getMsItemMeasurement().getVMitemEndQuantify());
			cell.setParent(item);
			
			
			itemList.appendChild(item);
			doCancel(win);
			
		}
		else Messagebox.show(MessagesService.getKey("common.add.fail"));
		log.debug("ItemController -- doSaveAdd() -- KELUAR");
		MiscTrxController.setFont(this.itemList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		log.debug("ItemController -- doSaveModify() -- MASUK");
		
		
		super.doSaveModify(win);
		
		item = itemList.getSelectedItem();
		msItem = (MsItem)item.getValue();
		
		MsItemGroup itemGroup = (MsItemGroup)itemGroupList.getSelectedItem().getValue();
		
		MsItemMeasurement itemMeasurement = (MsItemMeasurement)measureItemList.getSelectedItem().getValue();
		
		
		
		msItem.setMsItemGroup(itemGroup);
		msItem.setNR(jasaR.getValue().shortValue());
		msItem.setVItemName(itemName.getText());
		msItem.setVItemCode(itemCode.getText());
		msItem.setnMaxOrder(maxOrder.getValue());
		if(barcode.getValue().length()>0)msItem.setVBarcodeNo(barcode.getValue());
		msItem.setMsItemMeasurement(itemMeasurement);
		if(bufferLimit.getValue() != null)msItem.setNItemBufferLimit(new Short(bufferLimit.getValue().shortValue()));
		if(plafon.getValue() != null)msItem.setNItemPlafon(new Short(plafon.getValue().shortValue()));
		
		msItem.setVItemReturnable(medicineDesc.getSelectedItem().getValue().toString());
		
		if(itemGroupList.getSelectedItem().getLabel().equals("OBAT")){
			if(drugTypeList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG)){
				Messagebox.show(MessagesService.getKey("drugs.type.not.selected"));
				drugTypeList.focus();
				return;
			}
			msItem.setNItemType(new Short(drugTypeList.getSelectedItem().getValue().toString()));
		}
		
				
		if(itemServ.update(msItem, supplier.getSelectedItems())){
			log.debug("MENGUBAH DATA ITEM DAN DATA ITEM SUPPLIED SUKSES");
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			
			item.setValue(msItem);
			cell = new Listcell(msItem.getVItemCode());
			cell.setParent(item);
			
			cell = new Listcell(msItem.getVItemName());
			cell.setParent(item);
			
			cell = new Listcell(msItem.getMsItemGroup().getVItemGroupName());
			cell.setParent(item);
			
			Set suppliers = supplier.getSelectedItems();
			log.debug("BANYAKNYA SUPPLIER UNTUK ITEM INI : "+suppliers.size());
			
			String namaSupplier ="";
			Iterator itr = suppliers.iterator();
			while(itr.hasNext()){
				Listitem listSupllier = (Listitem)itr.next();
				namaSupplier = namaSupplier + listSupllier.getLabel()+";";
			}
			if(namaSupplier.length()> 1)namaSupplier = namaSupplier.substring(0,(namaSupplier.length()-1));
			cell = new Listcell(namaSupplier);
			cell.setParent(item);
			
			cell = new Listcell(msItem.getMsItemMeasurement().getVMitemEndQuantify());
			cell.setParent(item);
			
									
			doCancel(win);
			
		}
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		log.debug("ItemController -- doSaveModify() -- KELUAR");
		MiscTrxController.setFont(this.itemList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		log.debug("ItemController -- init() -- MASUK");
		
		itemGroupList = (Listbox)win.getFellow("itemGroupList");
		itemName = (Textbox)win.getFellow("itemName");
		itemCode = (Textbox)win.getFellow("itemCode");
		barcode = (Textbox)win.getFellow("barcode");
		supplier = (Listbox)win.getFellow("supplier");
		maxOrder = (Intbox)win.getFellow("maxOrder");
		
		measureItemList = (Listbox)win.getFellow("measureItemList");
		
		jasaR = (Intbox)win.getFellow("jasaR");

		itemList = (Listbox)win.getFellow("itemList");
		
		
		drugTypeList = (Listbox)win.getFellow("drugTypeList");
		plafon = (Intbox)win.getFellow("plafon");
		bufferLimit = (Intbox)win.getFellow("bufferLimit");
		medicineDesc = (Listbox)win.getFellow("medicineDesc");
		
		super.init(win);
		
		constrains.registerComponent(itemCode, ZulConstraint.NO_EMPTY);
		constrains.registerComponent(itemCode, ZulConstraint.UPPER_CASE);
		
		constrains.registerComponent(itemName, ZulConstraint.NO_EMPTY);
		constrains.registerComponent(medicineDesc, ZulConstraint.NO_EMPTY);
		constrains.registerComponent(itemName, ZulConstraint.UPPER_CASE);
		
		constrains.registerComponent(barcode, ZulConstraint.UPPER_CASE);
	
		constrains.validateComponent(false);
		
		VendorController.getVendorList(supplier);
		ItemGroupController.getItemGroupForSelect(itemGroupList);
		ItemMeasurementController.getItemMeasurementLastQuantityForSelect(measureItemList);

		itemGroupList.focus();
		log.debug("ItemController -- init() -- KELUAR");
	}
	
	public void getItemDataList() throws VONEAppException{
		
		MasterServiceLocator.getItemManager().getAllItem(itemList);
		MiscTrxController.setFont(itemList);
		

		
	}
	
	public void checkItemIfDrugs() throws InterruptedException{
		if(itemGroupList.getSelectedItem().getLabel().equals("OBAT")){
			drugTypeList.setVisible(true);
			
		}
		else{
			drugTypeList.setVisible(false);
			
		}
		
	}
	
	public static void searchItem(Listbox result, Textbox itemCode, Textbox itemName) throws WrongValueException, VONEAppException{
		result.getItems().clear();
		
		Listitem listitem = null;
		Listcell cell = null;
		MsItem item = null;
		
		
		
		List items = MasterServiceLocator.getItemManager().searchItem(itemCode.getValue(), itemName.getValue());
		Iterator itr = items.iterator();
		while(itr.hasNext()){
			item = (MsItem)itr.next();
			listitem = new Listitem();
			listitem.setValue(item);
			listitem.setParent(result);
			
			cell = new Listcell(item.getVItemCode());
			cell.setParent(listitem);
			
			cell = new Listcell(item.getVItemName());
			cell.setParent(listitem);
		}
	}

	public void cariClick(Listbox itemList, Textbox input) throws VONEAppException{
		
		input.setValue(input.getText().toUpperCase());
		MasterServiceLocator.getItemManager().search("%"+input.getText()+"%",itemList);
		
		
		MiscTrxController.setFont(itemList);
	}
}

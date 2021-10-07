package com.vone.medisafe.service.ifaceimpl.master.item;

/**
 * ItemManangerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Sept, 30 2006
 * @category service (logic model)
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.dao.item.ItemDAO;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.mapping.pojo.item.MsItemSupplied;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.ui.master.item.ItemController;
import com.vone.medisafe.ui.master.item.ItemInventoryPicker;

public class ItemManangerImpl implements ItemManager{
	
	private ItemDAO dao;

	public ItemDAO getDao() {
		return dao;
	}

	public void setDao(ItemDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsItem item) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.delete(item);
	}

	public List getAllItem() throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getAllItem();
	}

	public boolean save(MsItem item, Set suppler) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(item, suppler);
	}

	public boolean update(MsItem item, Set supplier)throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.update(item, supplier);
	}

	public List searchItem(String code, String name) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.searchItem(code, name);
	}

	public MsItem getItemByCode(String itemCode)throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getByCode(itemCode);
	}

	public MsItem getBloodItem(String name) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getBloodItem(name);
	}

	public List getItemByUnitAndTclass(Integer warehouseUnit, String tclass)throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getItemByWarehouseUnitAndTclass(warehouseUnit, tclass);
	}

	public float getItemQuantity(MsItem item, String kelasTarif, Integer wId)throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getItemQuantity(item, kelasTarif,wId);
	}

	public List searchItemByWarehoueseAndTclass(Integer wId, String tclass, 
			String itemCode, String itemName) throws VONEAppException{
		// TODO Auto-generated method stub
		if(wId != null)
			return dao.searchItemByUnitAndTclass(wId, tclass, itemCode, itemName);
		return null;
	}

	public List searchItemByWarehouese(Integer wId, String itemCode, String itemName)throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.searchItemByWarehouese(wId, itemCode, itemName);
	}

	public List getStok(Integer id) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getStok(id);
	}

	public boolean saveItemRequest(Set tir)throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.saveItemRequest(tir);
	}

	public MsWarehouse getWarehouseByCode(String itemCode) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getWarehouseByCode(itemCode);
	}

	public void searchItem(ItemInventoryPicker picker) throws VONEAppException {
		if (!StringUtils.hasValue(picker.code.getText()) && !StringUtils.hasValue(picker.batch.getText())){
			try {
				Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			picker.code.select();
			return;
		}

		picker.list.getItems().clear();		
		
		List listItem = searchItem(picker.code.getText(), picker.batch.getText());
		
		Iterator it = listItem.iterator();
		
		int qtyGlobal = 1;
		
		if (picker.qty.getValue() != null && picker.qty.getValue() > 1)
			qtyGlobal = picker.qty.intValue();
		
		while (it.hasNext()){
			MsItem pojo = (MsItem)it.next();
			
			Listitem item = new Listitem();
			item.setParent(picker.list);
			item.setValue(pojo);
			
			item.appendChild(new Listcell(pojo.getVItemCode()));
			item.appendChild(new Listcell(pojo.getVItemName()));
			item.appendChild(new Listcell(pojo.getMsItemMeasurement().getVMitemEndQuantify()));
			item.appendChild(new Listcell(""+qtyGlobal));
		}
	}

	public void getAllItem(Listbox itemList) throws VONEAppException {
		itemList.getItems().clear();
		MsItemSupplied supplier =  null;
		
		List items = getAllItem();
		
		Iterator it = items.iterator();
		while(it.hasNext()){
			MsItem msItem = (MsItem)it.next();
			Listitem item = new Listitem();
			item.setValue(msItem);
			item.setParent(itemList);
			
			Listcell cell = new Listcell(msItem.getVItemCode());
			cell.setParent(item);
			
			cell = new Listcell(msItem.getVItemName());
			cell.setParent(item);
			
			cell = new Listcell(msItem.getMsItemGroup().getVItemGroupName());
			cell.setParent(item);
			
			Set suppliers = msItem.getMsItemSupplied();
			
			String namaSupplier ="";
			Iterator itr = suppliers.iterator();
			while(itr.hasNext()){
				supplier = (MsItemSupplied)itr.next();
				namaSupplier = namaSupplier + supplier.getMsVendor().getVVendorName()+";";
			}
			if(namaSupplier.length()> 1)namaSupplier = namaSupplier.substring(0,(namaSupplier.length()-1));
			cell = new Listcell(namaSupplier);
			cell.setParent(item);
			
			cell = new Listcell(msItem.getMsItemMeasurement().getVMitemEndQuantify());
			cell.setParent(item);
			
			
		} 
	}
	
	public void doModify(ItemController controller) throws VONEAppException {
		if(controller.itemList.getSelectedItem() == null){
			try {
				Messagebox.show(MessagesService.getKey("master.item.list.notselected"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		Listitem item = controller.itemList.getSelectedItem();
		MsItem msItem = (MsItem)item.getValue();
		msItem = dao.getMsItem(msItem.getNItemId());
		for(int i=0; i < controller.itemGroupList.getItems().size(); i++){
			if(msItem.getMsItemGroup().getVItemGroupName().equals(controller.itemGroupList.getItemAtIndex(i).getLabel())){
				controller.itemGroupList.setSelectedIndex(i);
				break;
			}
		}
		controller.itemName.setValue(msItem.getVItemName());
		controller.itemCode.setValue(msItem.getVItemCode());
		controller.barcode.setValue(msItem.getVBarcodeNo());
		controller.jasaR.setValue(msItem.getNR().intValue());
		controller.maxOrder.setValue(msItem.getnMaxOrder());
		
		Listcell cell = (Listcell)item.getChildren().get(3);
		String vendors = cell.getLabel();
		String[] suppliers = vendors.split(";");
		
		Listbox listbox = new Listbox();
		listbox.setMultiple(true);
		Listitem itm;
		
		int counter = 0;
		
		for(int i=0; i < suppliers.length; i++){
			for(int k=0; k < controller.supplier.getItems().size(); k++){
				if(suppliers[i].equals(controller.supplier.getItemAtIndex(k).getLabel())){
					
					itm = controller.supplier.getItemAtIndex(k);
					itm.setParent(listbox);
//					listbox.addItemToSelection(itm);
//					controller.supplier.addItemToSelection(controller.supplier.getItemAtIndex(k));
				}
			}
		}
		
		
		List<Listitem> items = controller.supplier.getItems();
		
		
		for(Listitem listitem : items){
			
			itm = (Listitem)listitem.clone();
			itm.setParent(listbox);
					
		}
		
			
		controller.supplier.getItems().clear();
		
		List<Listitem> listboxItems = listbox.getItems();
		for(Listitem listitem : listboxItems){
			itm = (Listitem)listitem.clone();
			itm.setParent(controller.supplier);
			if(counter < suppliers.length){
				controller.supplier.addItemToSelection(itm);
			}
			counter++;
		}
		
		if(msItem.getNItemPlafon() != null)controller.plafon.setValue(new Integer(msItem.getNItemPlafon().shortValue()));
		if(msItem.getNItemBufferLimit() != null)controller.bufferLimit.setValue(new Integer(msItem.getNItemBufferLimit().shortValue()));
		
		for(int i=0; i < controller.measureItemList.getItems().size(); i++){
			if(msItem.getMsItemMeasurement().getVMitemEndQuantify().equals(controller.measureItemList.getItemAtIndex(i).getLabel())){
				controller.measureItemList.setSelectedIndex(i);
			}
		}
		
		/**
		if(msItem.getItemCogsNo() != null){
			for(int i=1; i < controller.cogsNo.getItems().size(); i++){
				if(msItem.getItemCogsNo().getVAcctNo().equals(controller.cogsNo.getItemAtIndex(i).getLabel())){
					controller.cogsNo.setSelectedIndex(i);
				}
			}
		}*/
		
		//if(msItem.getItemSellAcctNo() != null){
			
			//controller.coa.setText(msItem.getItemSellAcctNo().getVAcctNo()+"-"+msItem.getItemSellAcctNo().getVAcctName());
			//controller.coa.setAttribute("coa", msItem.getItemSellAcctNo());
			/*
			for(int i=1; i < controller.sellAccNo.getItems().size(); i++){
				if(msItem.getItemSellAcctNo().getVAcctNo().equals(controller.sellAccNo.getItemAtIndex(i).getLabel())){
					controller.sellAccNo.setSelectedIndex(i);
				}
			}*/
		//}
		
	/**
		for(int i=0; i < invAccNo.getItems().size(); i++){
			if(msItem.getItemInvAccNo().getVAcctNo().equals(invAccNo.getItemAtIndex(i).getLabel())){
				
			}
		}*/
		
		for(int i=1; i < controller.medicineDesc.getItems().size(); i++){
			if(controller.medicineDesc.getItemAtIndex(i).getValue().toString().equals(msItem.getVItemReturnable())){
				controller.medicineDesc.setSelectedIndex(i);
			}
		}
		
		if(msItem.getMsItemGroup().getVItemGroupName().equals("OBAT")){
			for(int i=1; i < controller.drugTypeList.getItems().size(); i++){
				if(msItem.getNItemType() != null){
					if(controller.drugTypeList.getItemAtIndex(i).getValue().toString().equals(msItem.getNItemType().toString())){
						controller.drugTypeList.setSelectedIndex(i);
					}
				}
			}
		}
		else controller.drugTypeList.setVisible(false);
	}

	

	public void search(String input, Listbox hasilCari) throws VONEAppException {
		hasilCari.getItems().clear();
		Decimalbox db = new Decimalbox();
		db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		Listitem item;
		Listcell cell;
		MsItemSupplied supplier =  null;
		
		List<MsItem> list = dao.cariItem(input);
		
		for(MsItem items : list){
			
			item = new Listitem();
			item.setValue(items);
			item.setParent(hasilCari);
			
			cell = new Listcell(items.getVItemCode());
			cell.setParent(item);
			
			cell = new Listcell(items.getVItemName());
			cell.setParent(item);
			
			cell = new Listcell();
			if(items.getMsItemGroup() != null)
				cell.setLabel(items.getMsItemGroup().getVItemGroupName());
			cell.setParent(item);
			
			Set suppliers = items.getMsItemSupplied();
			
			String namaSupplier ="";
			Iterator itr = suppliers.iterator();
			while(itr.hasNext()){
				supplier = (MsItemSupplied)itr.next();
				namaSupplier = namaSupplier + supplier.getMsVendor().getVVendorName()+";";
			}
			if(namaSupplier.length()> 1)namaSupplier = namaSupplier.substring(0,(namaSupplier.length()-1));
			cell = new Listcell(namaSupplier);
			cell.setParent(item);
			
			cell = new Listcell(items.getMsItemMeasurement().getVMitemEndQuantify());
			cell.setParent(item);
			
		}
	}

	@Override
	public void getItemObat(Listbox itemList) throws VONEAppException {
		itemList.getItems().clear();
		Listitem listitem;
		Listcell listcell;
//		List<MsItem> items = this.dao.getObat();
		List<Object[]> items = this.dao.getObatDetail();
		for(Object[] o : items){
			listitem = new Listitem();
			listitem.setValue(o);
			listitem.setParent(itemList);
			
			listcell = new Listcell(o[1].toString());
			listcell.setParent(listitem);
			
			listcell = new Listcell(o[2].toString());
			listcell.setParent(listitem);
			
			listcell = new Listcell();
			if(o[3] != null)
				listcell.setLabel(o[3].toString());
			else listcell.setLabel("0");
			listcell.setParent(listitem);
			
			listcell = new Listcell();
			if(o[4] != null)
				listcell.setLabel(o[4].toString());
			else listcell.setLabel("0");
			listcell.setParent(listitem);
			
			listcell = new Listcell(o[5] != null ? o[5].toString(): "0");
			listcell.setParent(listitem);
			
			listcell = new Listcell(o[6] != null ? o[6].toString() : "0");
			listcell.setParent(listitem);
			
		}
		
		//items.clear();
		//items = null;
		
	}

	@Override
	public void updateItemBatch(Listbox itemList) throws VONEAppException {
		List<MsItem> msItems = new ArrayList<MsItem>();
		List<MsItemSellingPrice> sellingPrice = new ArrayList<MsItemSellingPrice>();
		MsItem msItem = null;
		List<Listitem> items = itemList.getItems();
		for(Listitem item : items){
			msItem = this.dao.getByCode(((Listcell)item.getChildren().get(0)).getLabel());
			msItem.setNItemBufferLimit(new Short(((Listcell)item.getChildren().get(2)).getLabel()));
			msItem.setnMaxOrder(new Integer(((Listcell)item.getChildren().get(3)).getLabel()));
			msItems.add(msItem);
			
			List<MsItemSellingPrice> msItemSellPrice = this.dao.getItemSellingPrice(msItem);
			for(MsItemSellingPrice sell : msItemSellPrice){
				sell.setNSellingPrice(new Double(((Listcell)item.getChildren().get(5)).getLabel()));
				sellingPrice.add(sell);
			}
		}
		
		this.dao.updateBatchItem(msItems, sellingPrice);
		
		try {
			Messagebox.show("Data Successfully Updated...", "Information", Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List getRajalReport(String mulai, String sampai, String kodeRajal,
			String shift) {
		List hasil = null;
		try {
			hasil = dao.getRajalReport(mulai, sampai, kodeRajal, shift);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasil;
	}

	@Override
	public List getRanapReport(String mulai, String sampai, String kodeRajal,
			String shift) {
		List hasil = null;
		try {
			hasil = dao.getRanapReport(mulai, sampai, kodeRajal, shift);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasil;
	}

	@Override
	public List searchItemUnderBuffer(String code, String name)
			throws VONEAppException {
		
		return this.dao.serachItemUnderBuffer(code, name);
	}

	@Override
	public List getReturReport(String mulai, String sampai, String kode) {
		List hasil = this.dao.getReturReport(mulai, sampai, kode);
		return hasil;
	}
	
	
	
	
}

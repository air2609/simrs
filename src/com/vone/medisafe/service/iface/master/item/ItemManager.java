package com.vone.medisafe.service.iface.master.item;

/**
 * ItemManager.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Sept, 30 2006
 * @category  business model
 */

import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.ui.master.item.ItemController;
import com.vone.medisafe.ui.master.item.ItemInventoryPicker;



public interface ItemManager {
	public boolean save(MsItem item, Set supplier)throws VONEAppException;
	public boolean update(MsItem item, Set supplier)throws VONEAppException;
	public boolean delete(MsItem item)throws VONEAppException;
	public List getAllItem()throws VONEAppException;
	public List searchItem(String code, String name)throws VONEAppException;
	public List searchItemUnderBuffer(String code, String name) throws VONEAppException;
	public MsItem getItemByCode(String itemCode)throws VONEAppException;
	public MsItem getBloodItem(String name)throws VONEAppException;
	public List getItemByUnitAndTclass(Integer warehouseUnit, String tclass)throws VONEAppException;
	public float getItemQuantity(MsItem item, String kelasTarif,Integer wId)throws VONEAppException;
	public List searchItemByWarehoueseAndTclass(Integer wId, String tclass, String itemCode, String itemName)throws VONEAppException;
	//kun
	public List searchItemByWarehouese(Integer wId, String itemCode, String itemName)throws VONEAppException;
	public List getStok(Integer id)throws VONEAppException;
	
	public boolean saveItemRequest(Set tir)throws VONEAppException;
	
	public MsWarehouse getWarehouseByCode(String itemCode)throws VONEAppException;
	
	public void searchItem(ItemInventoryPicker picker) throws VONEAppException;
	
	public void getAllItem(Listbox itemList) throws VONEAppException;
	
	public void doModify(ItemController controller) throws VONEAppException;
	
	public void search(String input, Listbox hasilCari) throws VONEAppException;
	public void getItemObat(Listbox itemList) throws VONEAppException;
	public void updateItemBatch(Listbox itemList) throws VONEAppException;
	public List getRajalReport(String mulai, String sampai, String kodeRajal,
			String shift);
	public List getRanapReport(String mulai, String sampai, String kodeRajal,
			String shift);
	
	public List getReturReport(String mulai, String sampai, String kode);
	
	
}

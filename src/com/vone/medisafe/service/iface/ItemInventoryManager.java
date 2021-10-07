package com.vone.medisafe.service.iface;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.ui.master.item.UnitInventoryController;

public interface ItemInventoryManager {
	
	public List getItemBaseOnUnit(MsItem item, MsUnit unit) throws VONEAppException;
	
	public TbItemInventory getItemInventory(MsItem item, MsUnit unit) throws VONEAppException;
	
	public List getInventoryOnWhouse (Integer whouseId) throws VONEAppException;

	public void save (TbItemInventory pojo) throws VONEAppException;
	
	public Double getQtyByBatchNo (String batchNo) throws VONEAppException;
	
	public void update (TbItemInventory pojo) throws VONEAppException;
	
	public void delete (TbItemInventory pojo) throws VONEAppException;
	
	public List getInventoryOnWhouseItem (Integer whouseId, String itemCode, String itemName, String batchNo) throws VONEAppException;
	
	public List getInventoryAll () throws VONEAppException;

	public void getInventoryOnWhouseItem(UnitInventoryController controller) throws VONEAppException;

	public void redrawByCriteria(UnitInventoryController controller) throws VONEAppException;
	
	public List getLaporanPersediaanObat(Integer warehouseid) throws VONEAppException;
	
	public void getExpiredReport(Listbox itemList) throws VONEAppException;
	
	public TbItemInventory getLastInventory(MsItem item) throws VONEAppException;
}

package com.vone.medisafe.service.iface.master;

import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbItemMutation;
import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.warehouse.HistoryMutasiController;
import com.vone.medisafe.warehouse.HistoryRequestController;
import com.vone.medisafe.warehouse.ItemRequestApproveController;
import com.vone.medisafe.warehouse.ItemRequestController;
import com.vone.medisafe.warehouse.WarehouseController;

public interface WarehouseManager {

	public List findAll () throws VONEAppException;
	
	public void save (MsWarehouse pojo) throws VONEAppException;
	
	public void update (MsWarehouse pojo) throws VONEAppException;
	
	public void delete (MsWarehouse pojo) throws VONEAppException;
	
	public List getItemUnderBuffer (Integer whouseId) throws VONEAppException;
	
	public Integer getQtyAvail(Integer whouseId, Integer itemId) throws VONEAppException;
	
	public List getWhouseByStaffId(Integer staffId) throws VONEAppException;
	
	public List getItemRequest(int whouseid) throws VONEAppException;
	
	public List getItemRequestApprove(int whouseid) throws VONEAppException;
	
	public List getItemRequestByCode(String requestCode) throws VONEAppException;

	public List getTbItemInventory(Integer whouseId, Integer itemId)throws VONEAppException;
	
	public boolean saveItemMutation(Set itemMutationSet, Set itemRequestSet)throws VONEAppException;
	
	public boolean saveItemApprove(Set itemMutationSet)throws VONEAppException;

	public List getItemRequestBySource(int whouseid)throws VONEAppException;

	public void initItemRequest(ItemRequestController controller)throws VONEAppException;

	public void loadItemRequest(WarehouseController controller, int whouseid) throws VONEAppException;

	public void getSentItem(ItemRequestApproveController controller) throws VONEAppException;

	public void getRequestItem(ItemRequestApproveController controller)throws VONEAppException;

	public void redrawList(Listbox list) throws VONEAppException;

	public void prepareModify(com.vone.medisafe.ui.master.WarehouseController controller) throws VONEAppException;

	public void cancelItemRequest(Set<TbItemRequest> itemRequestSet) throws VONEAppException;

	public void cancelItemApprove(Set<TbItemMutation> itemMutationSet) throws VONEAppException;

	public void getSentItem(HistoryRequestController controller) throws VONEAppException;

	public void getMutasiItem(HistoryMutasiController controller) throws VONEAppException;

	
	
}

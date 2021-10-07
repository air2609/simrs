package com.vone.medisafe.service.ifaceimpl;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.dao.ItemInventoryDAO;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.iface.ItemInventoryManager;
import com.vone.medisafe.ui.master.item.UnitInventoryController;

public class ItemInventoryManagerImpl implements ItemInventoryManager{

	private ItemInventoryDAO dao;
	
	public ItemInventoryDAO getDao() {
		return dao;
	}

	public void setDao(ItemInventoryDAO dao) {
		this.dao = dao;
	}

	public List getItemBaseOnUnit(MsItem item, MsUnit unit) throws VONEAppException{
		List list = null;
		try {
			list = dao.getItemInventoryBaseOnUnit(item, unit);
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public Double getQtyByBatchNo (String batchNo) throws VONEAppException {
		return this.dao.getQtyByBatchNo(batchNo);
	}

	public TbItemInventory getItemInventory(MsItem item, MsUnit unit) throws VONEAppException{
		TbItemInventory inventory = null;
		
		try {
			inventory =  dao.getInventory(item, unit);
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inventory;
	}

	public List getInventoryOnWhouse (Integer whouseId) throws VONEAppException {
		return this.dao.getInventoryOnWhouse(whouseId);
	}
	
	public void save (TbItemInventory pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (TbItemInventory pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (TbItemInventory pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}
	
	public List getInventoryOnWhouseItem (Integer whouseId, String itemCode, String itemName, String batchNo) throws VONEAppException{
		return this.dao.getInventoryOnWhouseItem(whouseId, itemCode, itemName, batchNo);
	}
	
	public List getInventoryAll () throws VONEAppException{
		return this.dao.getInventoryAll();
	}
	
	public void redrawByCriteria(UnitInventoryController controller) throws VONEAppException {
		controller.list.getItems().clear();
		
		if (controller.location.getItemCount() < 1 || controller.location.getSelectedItem() == null){
			return;
		}
		
		List listItem = 
			this.dao.getInventoryOnWhouseByCriteria(((MsWarehouse)controller.location.getSelectedItem().getValue()).getNWhouseId(),
					"%"+controller.getCariTextbox().getText()+"%");
			
		if (listItem == null)
			return;
				
		
		Iterator it = listItem.iterator();
		
		while (it.hasNext()){
			TbItemInventory pojo = (TbItemInventory)it.next();
			
			Listitem item = new Listitem();
			item.setValue(pojo);
			item.setAttribute(UnitInventoryController.BATCH_ATTRIBUTE, pojo.getTbBatchItem());
			item.setAttribute(UnitInventoryController.ITEM_ATTRIBUTE, pojo.getMsItem());
			item.setParent(controller.list);
			
			//kode item
			item.appendChild(new Listcell(pojo.getMsItem().getVItemCode()));
			
			//no batch
			if (pojo.getTbBatchItem() != null)
				item.appendChild(new Listcell(pojo.getTbBatchItem().getVBatchNo()));
			else	
				item.appendChild(new Listcell());
			
			//nama			
			item.appendChild(new Listcell(pojo.getMsItem().getVItemName()));
			
			//qty			
			item.appendChild(new Listcell(""+pojo.getNItemInvQty()));
		
			//COGS			
			item.appendChild(new Listcell(""+pojo.getTbBatchItem().getNCogsPrice()));
		}
		
	}

	public void getInventoryOnWhouseItem(UnitInventoryController controller) throws VONEAppException {
		controller.list.getItems().clear();
		
		if (controller.location.getItemCount() < 1 || controller.location.getSelectedItem() == null){
			return;
		}
		
		List listItem = 
			getInventoryOnWhouseItem(((MsWarehouse)controller.location.getSelectedItem().getValue()).getNWhouseId(),
				"%%", "%%", "%%");

		if (listItem == null)
			return;
				
		
		Iterator it = listItem.iterator();
		
		while (it.hasNext()){
			TbItemInventory pojo = (TbItemInventory)it.next();
			
			Listitem item = new Listitem();
			item.setValue(pojo);
			item.setParent(controller.list);
			
			//kode item
			item.appendChild(new Listcell(pojo.getMsItem().getVItemCode()));
			
			//no batch
			if (pojo.getTbBatchItem() != null)
				item.appendChild(new Listcell(pojo.getTbBatchItem().getVBatchNo()));
			else	
				item.appendChild(new Listcell());
			
			//nama			
			item.appendChild(new Listcell(pojo.getMsItem().getVItemName()));
			
			//qty			
			item.appendChild(new Listcell(""+pojo.getNItemInvQty()));
		
			//COGS			
			item.appendChild(new Listcell(""+pojo.getTbBatchItem().getNCogsPrice()));
		}
	}
	
	public List getLaporanPersediaanObat(Integer warehouseid) throws VONEAppException{
		return dao.getLaporanInventory(warehouseid);
	}

	@Override
	public void getExpiredReport(Listbox itemList) throws VONEAppException {
		itemList.getItems().clear();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		List<TbItemInventory> invs = this.dao.getExpiredItem();
		for(TbItemInventory inv : invs){
			Listitem item = new Listitem();
			item.setParent(itemList);
			
			Listcell cell = new Listcell(inv.getMsItem().getVItemCode());
			cell.setParent(item);
			
			cell = new Listcell(inv.getMsItem().getVItemName());
			cell.setParent(item);
			
			cell = new Listcell(inv.getTbBatchItem().getVBatchNo());
			cell.setParent(item);
			
			cell = new Listcell(sdf.format(inv.getTbBatchItem().getDBatchExpDate()));
			cell.setParent(item);
			
			cell = new Listcell(inv.getMsWarehouse().getVWhouseName());
			cell.setParent(item);
			
			cell = new Listcell(inv.getNItemInvQty()+"");
			cell.setParent(item);
		}
	
	}

	@Override
	public TbItemInventory getLastInventory(MsItem item) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getLastInventory(item);
	}


}

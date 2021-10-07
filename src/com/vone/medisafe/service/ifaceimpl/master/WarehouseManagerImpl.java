package com.vone.medisafe.service.ifaceimpl.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUserDAO;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbItemMutation;
import com.vone.medisafe.mapping.dao.MsWarehouseDAO;
import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.warehouse.HistoryMutasiController;
import com.vone.medisafe.warehouse.HistoryRequestController;
import com.vone.medisafe.warehouse.ItemRequestApproveController;
import com.vone.medisafe.warehouse.ItemRequestController;
import com.vone.medisafe.warehouse.WarehouseController;

public class WarehouseManagerImpl implements WarehouseManager{

	private MsWarehouseDAO dao;
	private MsUserDAO userDao;
	
	public MsWarehouseDAO getDao() {
		return dao;
	}

	public void setDao(MsWarehouseDAO dao) {
		this.dao = dao;
	}

	public List findAll () throws VONEAppException {
		return this.dao.findAll();
	}
	
	public void save (MsWarehouse pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (MsWarehouse pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (MsWarehouse pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}
	
	public List getItemUnderBuffer (Integer whouseId) throws VONEAppException {
		return this.dao.getItemUnderBuffer(whouseId);
	}
	
	public Integer getQtyAvail(Integer whouseId, Integer itemId) throws VONEAppException {
		return this.dao.getQtyAvail(whouseId, itemId);
	}
	
	public List getWhouseByStaffId(Integer staffId) throws VONEAppException{
		return this.dao.getWhouseByStaffId(staffId);
	}

	public List getItemRequest(int whouseid) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getItemRequest(whouseid);
	}

	public List getItemRequestByCode(String requestCode) throws VONEAppException{
		return dao.getItemRequestByCode(requestCode);
	}

	public List getTbItemInventory(Integer whouseId, Integer itemId) throws VONEAppException{
		return dao.getTbItemInventory(whouseId, itemId);
	}

	public boolean saveItemMutation(Set itemMutationSet, Set itemRequestSet) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.saveItemMutation(itemMutationSet, itemRequestSet);
	}

	public List getItemRequestApprove(int whouseid) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getItemRequestApprove(whouseid);
	}

	public boolean saveItemApprove(Set itemMutationSet) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.saveItemApprove(itemMutationSet);
	}

	public List getItemRequestBySource(int whouseid) throws VONEAppException{
		return dao.getItemRequestBySource(whouseid);
	}

	public MsUserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(MsUserDAO userDao) {
		this.userDao = userDao;
	}

	public void initItemRequest(ItemRequestController controller) throws VONEAppException {
		controller.user = userDao.getMsUser(controller.user.getVUserName());
		Object[] obj = controller.user.getMsStaff().getMsStaffInUnits().toArray(); 
		if(obj.length == 1){
			controller.siu = (MsStaffInUnit)obj[0];
			if(controller.siu.getId().getMsUnit().getMsWarehouse() != null)
				controller.whouseId = controller.siu.getId().getMsUnit().getMsWarehouse().getNWhouseId();
		}
		
//		asal
		MsWarehouse msWarehouse;
		controller.locationList.getItems().clear();
		List list = getWhouseByStaffId(controller.user.getMsStaff().getNStaffId());
		Iterator iter = list.iterator();
		Listitem item;
		while (iter.hasNext()) {
			msWarehouse = (MsWarehouse) iter.next();
			item = new Listitem();
			item.setValue(msWarehouse);
			item.setLabel(msWarehouse.getVWhouseName());
			item.setParent(controller.locationList);
			
		}
		if (controller.locationList.getItems().size() > 0){
			controller.locationList.setSelectedIndex(0);
			msWarehouse = (MsWarehouse)controller.locationList.getSelectedItem().getValue();
			controller.session.setAttribute("whouseid",msWarehouse.getNWhouseId());
		}
		controller.session.setAttribute("asalList",controller.locationList);
		
		//tujuan
		controller.warehouseList.getItems().clear();
		List whList = findAll();
		
		iter = whList.iterator();
		while (iter.hasNext()) {
			msWarehouse = (MsWarehouse) iter.next();
			item = new Listitem();
			item.setValue(msWarehouse);
			item.setLabel(msWarehouse.getVWhouseName());
			item.setParent(controller.warehouseList);
			
		}
		controller.warehouseList.setSelectedIndex(0);
	}

	public void loadItemRequest(WarehouseController controller, int whouseid) throws VONEAppException {
		controller.permintaanTreechildren.getChildren().clear();
		controller.itemMutationList.getItems().clear();
		
		List list = getItemRequest(whouseid);
		String kepala1 = "", kepala2="";
		Iterator iterator = list.iterator();
		String satuan;
		while (iterator.hasNext()) {
			TbItemRequest element = (TbItemRequest) iterator.next();
			kepala2 = element.getVRequestCode();
			if(!kepala1.equals(kepala2)){
				controller.createGroupTree(element, kepala2, 
						element.getWarehouseSource().getVWhouseName());
			}
			satuan = element.getMsItem().getMsItemMeasurement().getVMitemEndQuantify();
			controller.createItemTree(element, element.getMsItem().getVItemName(), satuan, element.getNQtyReq(), 
					element.getNQtySent(), 
					element.getNQtyReq().intValue() - element.getNQtySent().intValue());
			kepala1 = element.getVRequestCode();
		}
	}

	public void getSentItem(ItemRequestApproveController controller) throws VONEAppException {

		controller.setujuTreechildren.getChildren().clear();
		
		controller.whouseid = (Integer) controller.session.getAttribute("whouseid");
		Listbox asalList = (Listbox) controller.session.getAttribute("asalList");
		MsWarehouse wh = (MsWarehouse) asalList.getSelectedItem().getValue();
		controller.whouseid = wh.getNWhouseId();
		
		List list = getItemRequestApprove(controller.whouseid);
		
		System.out.println("banyaknya data dari item mutation : "+list.size());

		String kepala1 = "", kepala2="";
		Iterator iterator = list.iterator();
		String satuan;
		while (iterator.hasNext()) {
			TbItemMutation itemMutation = (TbItemMutation) iterator.next();
			TbItemRequest itemRequest = itemMutation.getTbItemRequest();//not work???
			kepala2 = itemRequest.getVRequestCode();
			if(!kepala1.equals(kepala2)){
				controller.createGroupTree(itemRequest, kepala2, controller.setujuTreechildren);
			}
			satuan = itemRequest.getMsItem().getMsItemMeasurement().getVMitemEndQuantify();
			controller.createItemTree(itemMutation, itemRequest.getMsItem().getVItemName(), satuan, itemRequest.getNQtyReq(), 
					 itemMutation.getNMitemQty(), //itemRequest.getNQtySent(), 
					itemRequest.getNQtyReq().intValue() - itemRequest.getNQtySent().intValue());
			kepala1 = itemRequest.getVRequestCode();
		}

	}

	public void getRequestItem(ItemRequestApproveController controller) throws VONEAppException {
		controller.requestTreechildren.getChildren().clear();
		List list = controller.warehouseManager.getItemRequestBySource(controller.whouseid);
		String kepala1 = "", kepala2="";
		Iterator iterator = list.iterator();
		String satuan;
		while (iterator.hasNext()) {
			TbItemRequest element = (TbItemRequest) iterator.next();
			String whouseTarget = element.getWarehouseTarget().getVWhouseName();
			kepala2 = element.getVRequestCode();
			if(!kepala1.equals(kepala2)){
				controller.createGroupTree(element, kepala2 + " [" + whouseTarget + "]", controller.requestTreechildren);
			}
			satuan = element.getMsItem().getMsItemMeasurement().getVMitemEndQuantify();
			controller.createItemTree(element, element.getMsItem().getVItemName(), satuan, element.getNQtyReq(), 
					element.getNQtySent(), 
					element.getNQtyReq().intValue() - element.getNQtySent().intValue());
			kepala1 = element.getVRequestCode();
		}

	}

	public void redrawList(Listbox list) throws VONEAppException {
		list.getItems().clear();
		
		List listWarehouse = findAll();
		
		Iterator it = listWarehouse.iterator();
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsWarehouse warehousePojo = (MsWarehouse)it.next();
			
			item.appendChild(new Listcell(warehousePojo.getVWhouseCode()));					
			item.appendChild(new Listcell(warehousePojo.getVWhouseName()));
			
			if(warehousePojo.getVWhouseLoc() != null)
				item.appendChild(new Listcell(warehousePojo.getVWhouseLoc()));
			else
				item.appendChild(new Listcell(""));
			
			if (warehousePojo.getMsWarehouse() != null)
				item.appendChild(new Listcell(warehousePojo.getMsWarehouse().getVWhouseName()));
			else
				item.appendChild(new Listcell(""));
			
			if (warehousePojo.getMsCoa() != null)
				item.appendChild(new Listcell(warehousePojo.getMsCoa().getVAcctNo()+"-"+warehousePojo.getMsCoa().getVAcctName()));
			else
				item.appendChild(new Listcell(""));
			
			item.setValue(warehousePojo);
			
			item.setParent(list);
		}
		
	}

	
	public void prepareModify(com.vone.medisafe.ui.master.WarehouseController controller) throws VONEAppException {
		
		MsWarehouse pojo = (MsWarehouse)controller.list.getSelectedItem().getValue();
		
		pojo = this.dao.findById(pojo.getNWhouseId());
		
		controller.code.setText(pojo.getVWhouseCode());
		controller.name.setText(pojo.getVWhouseName());
		controller.location.setText(pojo.getVWhouseLoc());
		if (pojo.getMsWarehouse() != null){
			Iterator it = controller.superior.getItems().iterator();
			
			while (it.hasNext()){
				Listitem item = (Listitem) it.next();
				
				if (! (item.getValue() instanceof MsWarehouse)) continue;
				
				Integer supId1 = ((MsWarehouse)item.getValue()).getNWhouseId();
				Integer supId2 = pojo.getNWhouseId();
				
				if (supId1.intValue() == supId2.intValue()){
					controller.superior.setSelectedItem(item);
				}
				
			}
			
		}
		
		MsCoa msCoa = pojo.getMsCoa();
		if(msCoa != null){
			controller.coa.setValue(msCoa.getVAcctNo()+" - "+msCoa.getVAcctName());
			controller.coa.setAttribute("coa", msCoa);

		}
	}

	public void cancelItemRequest(Set<TbItemRequest> itemRequestSet) throws VONEAppException {
		// batalkan item request
		dao.cancelItemRequest(itemRequestSet);
		System.out.println("IMPL");
		
	}

	public void cancelItemApprove(Set<TbItemMutation> itemMutationSet) throws VONEAppException {
		dao.cancelItemApprove(itemMutationSet);
	}

	@SuppressWarnings("deprecation")
	public void getSentItem(HistoryRequestController controller) throws VONEAppException {
		controller.permintaanTreechildren.getChildren().clear();
		
		controller.whouseid = (Integer) controller.session.getAttribute("whouseid");
		Listbox asalList = (Listbox) controller.session.getAttribute("asalList");
		MsWarehouse wh = (MsWarehouse) asalList.getSelectedItem().getValue();
		controller.whouseid = wh.getNWhouseId();
		
		Date a1 = controller.startDate.getValue();
		Date a2 = controller.endDate.getValue();

		a2.setDate(a2.getDate()+1);

		List list = null;
		list = dao.getItemRequestAll(controller.whouseid, 
				a1, 
				a2
				);

		String kepala1 = "", kepala2="";
		Iterator iterator = list.iterator();
		String satuan;
		while (iterator.hasNext()) {
			TbItemRequest itemRequest = (TbItemRequest) iterator.next();
//			TbItemRequest itemRequest = itemMutation.getTbItemRequest();//not work???
			kepala2 = itemRequest.getVRequestCode();
			if(!kepala1.equals(kepala2)){
				controller.createGroupTree(
						itemRequest, 
						kepala2 + " / "+ PrintClient.getDate(itemRequest.getDWhnCreate(),
								MedisafeConstants.DATE_FORMAT),
						itemRequest.getWarehouseTarget().getVWhouseName(),
						controller.permintaanTreechildren);
			}
			satuan = itemRequest.getMsItem().getMsItemMeasurement().getVMitemEndQuantify();
			controller.createItemTree(null, itemRequest.getMsItem().getVItemName(), satuan, itemRequest.getNQtyReq(), 
					itemRequest.getNQtySent(), 
					itemRequest.getNQtyReq().intValue() - itemRequest.getNQtySent().intValue());
			kepala1 = itemRequest.getVRequestCode();
		}
	}

	@SuppressWarnings("deprecation")
	public void getMutasiItem(HistoryMutasiController controller) throws VONEAppException {
		controller.permintaanTreechildren.getChildren().clear();
		
		MsWarehouse wh = (MsWarehouse) controller.locationList.getSelectedItem().getValue();
		controller.whouseid = wh.getNWhouseId();
		
		Date a1 = controller.startDate.getValue();
		Date a2 = controller.endDate.getValue();

		a2.setDate(a2.getDate()+1);
		
		List list = null;
		list = dao.getItemMutasiAll(controller.whouseid, 
				a1, 
				a2
				);

		String kepala1 = "", kepala2="";
		Iterator iterator = list.iterator();
		String satuan;
		while (iterator.hasNext()) {
			TbItemMutation tbItemMutation = (TbItemMutation) iterator.next();
			
			kepala2 = tbItemMutation.getTbItemRequest().getVRequestCode();
			if(!kepala1.equals(kepala2)){
				controller.createGroupTree(
						tbItemMutation, 
						kepala2 + " / "+ PrintClient.getDate(tbItemMutation.getDWhnCreate(),
								MedisafeConstants.DATE_FORMAT), 
						tbItemMutation.getTbItemRequest().getWarehouseSource().getVWhouseName(),
						controller.permintaanTreechildren);
			}
			satuan = tbItemMutation.getTbItemRequest().getMsItem().getMsItemMeasurement().getVMitemEndQuantify();
			controller.createItemTree(null, tbItemMutation.getTbItemRequest().getMsItem().getVItemName(), satuan, tbItemMutation.getTbItemRequest().getNQtyReq(), 
					tbItemMutation.getTbItemRequest().getNQtySent(), 
					tbItemMutation.getTbItemRequest().getNQtyReq().intValue() - tbItemMutation.getTbItemRequest().getNQtySent().intValue());
			kepala1 = tbItemMutation.getTbItemRequest().getVRequestCode();
		}
		
	}

}

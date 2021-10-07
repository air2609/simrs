package com.vone.medisafe.warehouse;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.ui.base.BaseController;

public class ItemRequestController extends BaseController {
	public Listbox locationList;
	public Listbox warehouseList;
	Textbox itemCode;
	Textbox itemName;
	Listbox itemList;
	Textbox requestCode;
	Component win;
	Intbox qty;
	
	public MsStaffInUnit siu;
	public MsUser user;
	
	public Session session;
	ZulConstraint constraints = new ZulConstraint();
	public Integer whouseId;
	Listitem item;
	Listcell cell;
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		super.doClose(cmp);
	}

	
	public UserInfoBean getUserInfoBean() {
		return super.getUserInfoBean();
	}
	
	private WarehouseManager warehouseManager = MasterServiceLocator.getWarehouseManager();
	private ItemManager itemManager = MasterServiceLocator.getItemManager();
	
	public void init(Component win) throws InterruptedException,
	VONEAppException {
		//zkpages/common/permintaanItem.zul

		locationList = (Listbox)win.getFellow("locationList");
		warehouseList = (Listbox)win.getFellow("warehouseList");
		itemCode = (Textbox)win.getFellow("itemCode");
		itemName = (Textbox)win.getFellow("itemName");
		itemList = (Listbox)win.getFellow("itemList");
		requestCode = (Textbox)win.getFellow("requestCode");
		qty = (Intbox)win.getFellow("qty");
		
		this.win=win;
		
		super.init(win);
		
		constraints.registerComponent(itemCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(itemName,ZulConstraint.UPPER_CASE);
		
		session = win.getDesktop().getSession();
		user = getUserInfoBean().getMsUser();
		
		itemList.getItems().clear();
		
		warehouseManager.initItemRequest(this);
		
		itemCode.focus();
	}
	
	public void tambahClick() throws InterruptedException, VONEAppException{
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.#");
		
		boolean selected = false;
		Set treatments = itemList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		itemList.getItems().clear();
		
		
		for(int i=0; i < listitem.length; i++){
			item = listitem[i];
			item.setParent(itemList);
		}
		
		
		//add search result to listbox, if result size is 1, add result to selection
		if(itemCode.getText().trim().equals("") && itemName.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			itemCode.focus();
			return;
		}
		
		MsWarehouse wh = (MsWarehouse)warehouseList.getSelectedItem().getValue();
		
		whouseId =wh.getNWhouseId();
		
		List list = itemManager.searchItemByWarehouese(
					whouseId, "%"+itemCode.getText()+"%","%"+itemName.getText()+"%");
		
		if(list.size() == 1){
			//"select inv.n_item_id as id, item.v_item_code as code, item.v_item_name as name, sat.v_mitem_end_quantify as satuan " +

			Object[] obj = (Object[])list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				Object[] object = (Object[])item.getValue();
				if(((Integer)object[0]).equals((Integer)obj[0]))
					return;
			}
			item = new Listitem();
			item.setValue(obj);
			item.setParent(itemList);
			
			cell = new Listcell(obj[1].toString());
			cell.setParent(item);
			
			cell = new Listcell(obj[2].toString());
			cell.setParent(item);
			
			Integer jml = warehouseManager.getQtyAvail(whouseId, (Integer)obj[0]);
			
			cell = new Listcell(jml.toString());
			cell.setParent(item);
			
			cell = new Listcell(obj[3].toString());
			cell.setParent(item);
			
			Listcell listcell = new Listcell();
			listcell.setParent(item);
			Intbox ibox = new Intbox();
			ibox.setWidth("90%");
			ibox.setParent(listcell);
			
			if(qty.getValue() != null){
				ibox.setValue(qty.getValue());
			}
			item.setAttribute("jumlah", ibox);
			itemList.addItemToSelection(item);	
			item.setAttribute("stok", jml);

//			item.setAttribute("jumlah", ibox);
			return;
			
		}
		
		it = list.iterator();
		
		while(it.hasNext()){
			
			Object[] obj = (Object[])it.next();
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				Object[] object = (Object[])item.getValue();
				if(((Integer)object[0]).equals((Integer)obj[0]))
				{
					selected = true;
					break;
				}
				else{
					selected = false;
				}
			}
			
			if(selected){
				//do nothing
			}else{
				item = new Listitem();
				item.setValue(obj);
				item.setParent(itemList);
				
				cell = new Listcell(obj[1].toString());
				cell.setParent(item);
				
				cell = new Listcell(obj[2].toString());
				cell.setParent(item);
				
				
				Integer jml = warehouseManager.getQtyAvail(whouseId, (Integer)obj[0]);
				
				cell = new Listcell(jml.toString());
				cell.setParent(item);
				
				cell = new Listcell(obj[3].toString());
				cell.setParent(item);
				
				Listcell listcell = new Listcell();
				listcell.setParent(item);
				Intbox ibox = new Intbox();
				ibox.setWidth("90%");
				ibox.setParent(listcell);
				
				if(qty.getValue() != null){
					ibox.setValue(qty.getValue());
				}
				item.setAttribute("stok", jml);
				item.setAttribute("jumlah", ibox);
				
				selected = false;
				
			}
				
		}
		MiscTrxController.setFont(itemList);
		itemCode.setText(null);
		itemName.setText(null);
		qty.setValue(null);
	}
	
	public void kirimClick() throws InterruptedException, VONEAppException{
		Set list = itemList.getSelectedItems();
		Iterator iterator = list.iterator();
		if(list.size()==0){
			Messagebox.show("ISILAH ITEM PERMINTAAN DULU!!");
			return;
		}
		
		
		Set<TbItemRequest> itemRequestSet = new HashSet<TbItemRequest>();
		MsWarehouse asal = (MsWarehouse)locationList.getSelectedItem().getValue();
		MsWarehouse tujuan = (MsWarehouse)warehouseList.getSelectedItem().getValue();
		MsItem msItem = null;
		TbItemRequest itemRequest;
		Intbox ibox=null;
		
		Integer noNota = IdsServiceLocator.getIdsManager().getSequence(
				MedisafeConstants.NOTA_REQUEST);
		
		String nomor = MedisafeUtil.generateNoRequest(noNota, new Date());
//		requestCode.setText(nomor);
		
		while (iterator.hasNext()) {
			//obj[0] = item id			
			//obj[1] = item code
			//ojb[2] = item name
			//ojb[3] = item quantify
			Listitem element = (Listitem) iterator.next();
			
			Object[] obj = (Object[])element.getValue();
			
			msItem = itemManager.getItemByCode(obj[1].toString());
			ibox = (Intbox)element.getAttribute("jumlah");
			Integer stok = (Integer)element.getAttribute("stok");
			if(ibox.getValue() == null){
				Messagebox.show("ISILAH JUMLAH STOK YG DIMINTA");
				return;
			}
			System.out.println(ibox.getValue());
			if(stok.intValue()<ibox.getValue().intValue()){
				Messagebox.show("PERMINTAAN TDK BOLEH MELEBIHI STOK");
				return;
			}
			if(ibox.getValue().intValue()<=0){
				Messagebox.show("PERMINTAAN TDK BOLEH NEGATIF/NOL");
				return;				
			}
			
			itemRequest = new TbItemRequest();
			
			
			itemRequest.setMsItem(msItem);
			itemRequest.setWarehouseSource(asal);
			itemRequest.setWarehouseTarget(tujuan);
			
			itemRequest.setVWhoCreate(user.getVUserName());
			
			itemRequest.setDWhnCreate(new Date());
			itemRequest.setNQtyReq(ibox.getValue());
			itemRequest.setNQtySent(0);
			itemRequest.setNStatus(0);
			
			itemRequest.setVRequestCode(nomor);
			
			itemRequestSet.add(itemRequest);
		}
		
		boolean b = itemManager.saveItemRequest(itemRequestSet);
		
		if(!b)
			Messagebox.show("GAGAL MENYIMPAN TRANSAKSI");
		else{
			requestCode.setText(nomor);
			Messagebox.show("PERMINTAAN TELAH DIKIRIM");

			Button btnKirim = (Button)win.getFellow("btnKirim");
			btnKirim.setDisabled(true);
			Button btnTambah = (Button)win.getFellow("btnTambah");
			btnTambah.setDisabled(true);

		}
	}
	
	public void clear(){
		itemList.getItems().clear();
		itemCode.setValue(null);
		itemName.setValue(null);
		qty.setValue(null);
		requestCode.setValue(null);
		Button btnKirim = (Button)win.getFellow("btnKirim");
		btnKirim.setDisabled(false);
		Button btnTambah = (Button)win.getFellow("btnTambah");
		btnTambah.setDisabled(false);
		itemCode.focus();

	}
	
}

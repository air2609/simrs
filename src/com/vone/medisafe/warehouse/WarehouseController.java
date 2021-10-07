package com.vone.medisafe.warehouse;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemMutation;
import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.ui.base.BaseController;

public class WarehouseController extends BaseController {
	Listbox locationList;
	Tree permintaanTree;
	public Treechildren permintaanTreechildren;
	Button btnRefresh;
	Button btnDetail;

	public Listbox itemMutationList;
	Button btnKirim;
	Button btnDelete;
	
	MsWarehouse msWarehouse; 
	Session session;
	
	MsUser user;
	MsStaffInUnit siu;
	
	Listitem item;
	Listcell cell;
	Intbox intbox;
	
	Treechildren child;
	Treeitem treeItem;
	Treerow treeRow;
	Treechildren children;
	Treecell treeCell;

	Logger log = Logger.getLogger(WarehouseController.class);
	WarehouseManager warehouseManager = Service.getWarehouseManager();

	public void init(Component win) throws InterruptedException,
			VONEAppException {
		
		super.init(win);
		
		locationList = (Listbox)win.getFellow("locationList");
		permintaanTree = (Tree)win.getFellow("permintaanTree");
		permintaanTreechildren = (Treechildren)win.getFellow("permintaanTreechildren");
		btnRefresh = (Button)win.getFellow("btnRefresh");
		btnDetail = (Button)win.getFellow("btnDetail");

		itemMutationList = (Listbox)win.getFellow("itemMutationList");
		btnKirim = (Button)win.getFellow("btnKirim");
		btnDelete = (Button)win.getFellow("btnDelete");
		
		session = win.getDesktop().getSession();
		user = getUserInfoBean().getMsUser();
		getUnitStaffForSelect();
		refreshClick();

	}
	
	private void getUnitStaffForSelect() throws VONEAppException {
		locationList.getItems().clear();
		List list = warehouseManager.getWhouseByStaffId(user.getMsStaff().getNStaffId());
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			msWarehouse = (MsWarehouse) iter.next();
			item = new Listitem();
			item.setValue(msWarehouse);
			item.setLabel(msWarehouse.getVWhouseName());
			item.setParent(locationList);
		}
		if (locationList.getItems().size() > 0)
			locationList.setSelectedIndex(0);
	}
	
	private void loadItemRequest(int whouseid) throws VONEAppException{
		warehouseManager.loadItemRequest(this, whouseid);
		MiscTrxController.setFont(permintaanTree);
	}

	public void refreshClick() throws VONEAppException{
		log.debug("REFRESH CLICK");
		msWarehouse = (MsWarehouse)locationList.getSelectedItem().getValue();
		
		loadItemRequest(msWarehouse.getNWhouseId());
	}
	
	public void hapusClick() throws InterruptedException{
		log.debug("HAPUS CLICK");
		//itemMutationList. itemMutationList.getSelectedItem();
		if (itemMutationList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		int index = itemMutationList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		itemMutationList.removeItemAt(index);
	}
	
	public void kirimClick() throws VONEAppException{
		if(itemMutationList.getItems().size()==0){
			try {
				Messagebox.show("Isi Data Item Terlebih Dahulu..!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		log.debug("KIRIM CLICK");
		Iterator iter = itemMutationList.getItems().iterator();
		
		
		Set<TbItemMutation> itemMutationSet = new HashSet<TbItemMutation>();
		Set<TbItemRequest> itemRequestSet = new HashSet<TbItemRequest>();
		
		TbItemMutation itemMut = null;
		TbItemRequest itemReq;
		TbItemInventory itemInv;
		
		while (iter.hasNext()) {
			item = (Listitem) iter.next();
			itemInv = (TbItemInventory)item.getValue();
			itemReq = (TbItemRequest)item.getAttribute("itemReq");
			
			log.debug("JUMLAH ATRIBUTE: " + item.getAttributes().size());
			log.debug(itemReq.getNIrId());
			
			//set the fields here
			itemMut = new TbItemMutation();
			itemMut.setDWhnCreate(new Date());
			Integer jumlah = (Integer)item.getAttribute("jumlah");
			itemMut.setNMitemQty(jumlah);
			itemMut.setTbBatchItem(itemInv.getTbBatchItem());
			itemMut.setTbItemRequest(itemReq);
			itemMut.setVWhoCreate(user.getVUserName());
			itemMut.setNStatus(1);//NEW
			
			itemReq.setNQtySent(itemReq.getNQtySent() + jumlah);
			itemMutationSet.add(itemMut);
			itemRequestSet.add(itemReq);
		}
		log.debug(itemMutationSet.size() + " RECORDS");
		
		boolean success = warehouseManager.saveItemMutation(itemMutationSet, itemRequestSet);
		if(success){
			try {
				Messagebox.show("Permintaan Telah Di Kirim..!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshClick();
		}
	}
	
	/*
	 * 
	 */
	public void detailClick() throws VONEAppException{
		log.debug("DETAIL CLICK");
		session.setAttribute("permintaanTreechildren", permintaanTreechildren);
		
		final Window win = (Window) Executions.createComponents("/zkpages/warehouse/detailItem.zul",null,null);
		Listbox itemList = (Listbox) win.getFellow("itemList");
		itemList.getItems().clear();
		//diisi dengan request yg dipilih, ambil dari permintaanTreechildren 
		Set listPilihan = permintaanTree.getSelectedItems();
		log.debug("" + listPilihan.size());
		
		Iterator iterPilihan = listPilihan.iterator();
		String nama, satuan;
		while (iterPilihan.hasNext()) {
			Treeitem elemItem = (Treeitem) iterPilihan.next();
			
			log.debug(elemItem.getAttribute("tipe") + " <-- ");
			if (elemItem.getAttribute("tipe").equals("anak")){
				TbItemRequest itemReq = (TbItemRequest)elemItem.getValue();
				
				msWarehouse = (MsWarehouse)locationList.getSelectedItem().getValue();
				Integer whouseId = msWarehouse.getNWhouseId();
				Integer itemId = itemReq.getMsItem().getNItemId();
				
				List listItemInv = warehouseManager.getTbItemInventory(whouseId, itemId);
				log.debug("" + whouseId + " " + itemId + " <---");
				
				Iterator iterInv = listItemInv.iterator();
				boolean lagi = true;
				int jmlReq = 0;
				jmlReq = (Integer)elemItem.getAttribute("jmlSisa");
				while (iterInv.hasNext() && lagi) {
					TbItemInventory itemInv = (TbItemInventory) iterInv.next();
					
					item = new Listitem(itemReq.getMsItem().getVItemCode());
					item.setValue(itemInv);
					item.setParent(itemList);
					item.setSelected(true);
					
					item.setAttribute("itemReq", itemReq);
					log.debug(itemReq.getMsItem().getVItemName());
					int nBatchId =itemInv.getTbBatchItem().getNBatchId();
					
//					batch=itemReq.getMsItem().get
					cell = new Listcell("" + nBatchId);
					cell.setParent(item);
					item.setAttribute("batch", nBatchId);
					
					double nJmlStok = itemInv.getNItemInvQty();
//					double stokBatch = 
						
					cell = new Listcell("" + nJmlStok);
					cell.setParent(item);
					item.setAttribute("jmlStok", new Double(nJmlStok));
					
					nama = (String)elemItem.getAttribute("nama");
					cell = new Listcell(nama);
					cell.setParent(item);
					item.setAttribute("nama", nama);
					
					satuan = (String)elemItem.getAttribute("satuan");
					cell = new Listcell(satuan);
					cell.setParent(item);
					item.setAttribute("satuan", satuan);
					
					cell = new Listcell("");
					cell.setParent(item);
					intbox = new Intbox();
					
 					if(jmlReq>nJmlStok){
 	 					intbox.setValue(new Double(nJmlStok).intValue());
 						lagi = true;
 						jmlReq -= nJmlStok;
 						if(jmlReq==0)
 							lagi = false;
 					}
 					else{
 	 					intbox.setValue(jmlReq);
 						lagi = false;
 					}
					
					intbox.setParent(cell);
					item.setAttribute("intbox", intbox);
					
				}
			}
		}
		
		win.setAttribute("aksi", "0x0");
		try {
			MiscTrxController.setFont(itemList);
			win.doModal();
			String aksi = (String)win.getAttribute("aksi"); 
			log.debug(aksi);
			if(aksi.equals("Save")){
				log.debug("selected: " + itemList.getSelectedItems().size());
				//proses disini
				if(itemList.getSelectedItems().size()==0){
					Messagebox.show("Pilih Item Terlebih Dahulu..!");
					return;
				}
					
				Iterator iter = itemList.getSelectedItems().iterator();
				itemMutationList.getItems().clear();
				Listitem itemMut = null;
				while (iter.hasNext()) {
					item = (Listitem) iter.next();
					intbox = (Intbox)item.getAttribute("intbox");
					Double jmlStok = (Double)item.getAttribute("jmlStok");
					
					if(intbox.getValue() == null){
						return;
					}else{
						if(intbox.getValue().intValue() <= 0){
							Messagebox.show("Jumlah Tidak Boleh Nol atau Negatif..!");
							intbox.focus();
							return;
						}
					}
					if(intbox.getValue().intValue()>jmlStok.intValue()){
						Messagebox.show("Jumlah Tidak Boleh Melebihi Stok..!");
						intbox.focus();
						return;
					}
					if(jmlStok.intValue()<=0){
						Messagebox.show("Jumlah Tidak Boleh Negatif..!");
						intbox.focus();
						return;
					}
					
					TbItemInventory iteminv = (TbItemInventory)item.getValue();
					//masukkan ke itemMutationList
					//log(itemReq.getVRequestCode() + " <-->");
					//item.setParent(itemMutationList);
					
					Integer ibatch = (Integer)item.getAttribute("batch");
					itemMut = new Listitem(ibatch.toString());
					itemMut.setParent(itemMutationList);
					itemMut.setValue(iteminv);
					
					nama = (String)item.getAttribute("nama");
					cell = new Listcell(nama);
					cell.setParent(itemMut);
					
					satuan = (String)item.getAttribute("satuan");
					cell = new Listcell(satuan);
					cell.setParent(itemMut);
					
					
					
					cell = new Listcell("" + intbox.getValue());
					cell.setParent(itemMut);
					itemMut.setAttribute("jumlah", intbox.getValue());
					
					TbItemRequest itemReq = (TbItemRequest)item.getAttribute("itemReq");
					itemMut.setAttribute("itemReq", itemReq);
					//log.debug(itemReq.getNIrId());
					log.debug(itemReq.getMsItem().getVItemName());
					//itemReq.setNQtySent(intbox.getValue());
					
				}
				MiscTrxController.setFont(itemMutationList);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void detailSimpanClick(){
		log.debug("DETAIL SIMPAN CLICK"); //not work
	}
	
	public void createGroupTree(Object value, String nomorPermintaan, String namaGudang) {
		treeItem = new Treeitem();
		treeItem.setValue(value);
		treeItem.setParent(permintaanTreechildren);
		treeItem.setAttribute("tipe", "group");

		treeRow = new Treerow();
		treeRow.setParent(treeItem);
		
		treeCell = new Treecell(nomorPermintaan);
		treeCell.setParent(treeRow);
		treeCell.addEventListener("onClick", new GroupItemListener());
		
		treeCell = new Treecell(namaGudang);
		treeCell.setParent(treeRow);
		treeCell.addEventListener("onClick", new GroupItemListener());
		
		children = new Treechildren();
		children.setParent(treeItem);
		
	}

	public void createItemTree(Object value, String itemCaption, String satuan,
			int jmlOrder, int jmlTerima, int jmlSisa) {
		//todo createItemTree
		Treeitem it;
		it = new Treeitem();
		it.setValue(value);
		it.setParent(children);
		it.setAttribute("tipe", "anak");

		treeRow = new Treerow();
		treeRow.setParent(it);
		treeCell = new Treecell(itemCaption);
		treeCell.setParent(treeRow);
		it.setAttribute("nama", itemCaption);

		treeCell = new Treecell("");
		treeCell.setParent(treeRow);

		treeCell = new Treecell(satuan);
		treeCell.setParent(treeRow);
		it.setAttribute("satuan", satuan);
		
		treeCell = new Treecell("" + jmlOrder);
		treeCell.setParent(treeRow);

		treeCell = new Treecell("" + jmlTerima);
		treeCell.setParent(treeRow);

		treeCell = new Treecell("" + jmlSisa);
		treeCell.setParent(treeRow);
		it.setAttribute("jmlSisa", jmlSisa);
	}
	/*
	 * jika group di cek, maka semua anak ikut ter-cek
	 * 
	 */
	public void permintaanClick(){

	}
}

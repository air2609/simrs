package com.vone.medisafe.ui.master;



import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsVillage;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.VillageManager;
import com.vone.medisafe.ui.base.BaseController;


/**
 * VillageController.java
 * @author Arifullah Ibn Rusyd
 * @version 0.3 -- 03-08-2006 -- utk sementara sudah final
 * @since 29-07-2006
 * used to controll all flow of Village data from .zul file
 */

public class VillageController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	
	VillageManager villageService = MasterServiceLocator.getVillageManager();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("kode_kelurahan");
		Textbox nama = (Textbox)win.getFellow("nama_kelurahan");
		super.doCancel(win);
		kode.setValue(null);
		nama.setValue(null);
		
		kode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox  villageList =(Listbox)win.getFellow("villageList");
		super.doDelete(win);
		if(villageList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.village.villagelist.notselected"));
			return;
		}
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getVillageManager().deleteById((Integer)villageList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getVillageDataList(villageList);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		Textbox kode = (Textbox)win.getFellow("kode_kelurahan");
		Textbox nama = (Textbox)win.getFellow("nama_kelurahan");
		Listbox  villageList =(Listbox)win.getFellow("villageList");
		super.doModify(win);
		if(villageList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.village.villagelist.notselected"));
			return;
		}
		Listitem item = villageList.getSelectedItem();
		Listcell cellCode = (Listcell)item.getChildren().get(0);
		Listcell cellName = (Listcell)item.getChildren().get(1);
		kode.setValue(cellCode.getLabel());
		nama.setValue(cellName.getLabel());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("kode_kelurahan");
		Textbox nama = (Textbox)win.getFellow("nama_kelurahan");
		Listbox  villageList =(Listbox)win.getFellow("villageList");
		super.doSaveAdd(win);
		if(MasterServiceLocator.getVillageManager().getVillageByCode(kode.getText())!= null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		MsVillage vil = new MsVillage();
		vil.setVVillageCode(kode.getText());
		vil.setVVillageName(nama.getText());
		vil.setDWhnCreate(new Date());
		MasterServiceLocator.getVillageManager().save(vil);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getVillageDataList(villageList);
		MiscTrxController.setFont(villageList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("kode_kelurahan");
		Textbox nama = (Textbox)win.getFellow("nama_kelurahan");
		Listbox  villageList =(Listbox)win.getFellow("villageList");
		super.doSaveModify(win);
		
		MsVillage vil = new MsVillage();
		
		vil.setVVillageCode(kode.getText());
		vil.setVVillageName(nama.getText());
		vil.setNVillageId((Integer)villageList.getSelectedItem().getValue());
		MasterServiceLocator.getVillageManager().save(vil);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getVillageDataList(villageList);
		MiscTrxController.setFont(villageList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox  villageList =(Listbox)win.getFellow("villageList");
		Listbox crKelurahanList = (Listbox)win.getFellow("crKelurahanList");
		Textbox crKodeKelurahan = (Textbox)win.getFellow("crKodeKelurahan"); 
		Textbox crNamaKelurahan = (Textbox)win.getFellow("crNamaKelurahan");
		Textbox kode = (Textbox)win.getFellow("kode_kelurahan");
		Textbox nama = (Textbox)win.getFellow("nama_kelurahan");
		super.init(win);
		
		constraints.registerComponent(kode,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(nama,ZulConstraint.NO_EMPTY);
		
		constraints.registerComponent(kode,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(nama,ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(crKodeKelurahan,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNamaKelurahan,ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		getVillageDataList(villageList);
		crKelurahanList.getItems().clear();
	}

	
	public static void getVillage(Window win){
		Tab tabForm = (Tab)win.getFellow("formKelurahan");
		Tab tabPencarian = (Tab)win.getFellow("formPencarian");
		Listbox crKelurahanList = (Listbox)win.getFellow("crKelurahanList");
		Listbox  villageList =(Listbox)win.getFellow("villageList");
		Textbox crKodeKelurahan = (Textbox)win.getFellow("crKodeKelurahan"); 
		Textbox crNamaKelurahan = (Textbox)win.getFellow("crNamaKelurahan");
		if(crKelurahanList.getSelectedItem() != null){
			villageList.getItems().clear();
			Listitem item = crKelurahanList.getSelectedItem();
			item.setParent(villageList);
			villageList.addItemToSelection(item);
			crKelurahanList.getItems().clear();
			crKodeKelurahan.setValue(null);
			crNamaKelurahan.setValue(null);
			tabPencarian.setSelected(false);
			tabForm.setSelected(true);
		}
		else{
			try {
				Messagebox.show("Pilih data kelurahan!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void searchVillage(Window win){
		Textbox crKodeKelurahan = (Textbox)win.getFellow("crKodeKelurahan"); 
		Textbox crNamaKelurahan = (Textbox)win.getFellow("crNamaKelurahan");
		Listbox crKelurahanList = (Listbox)win.getFellow("crKelurahanList");
		crKelurahanList.getItems().clear();
		MsVillage village = null;
		List searchResul = MasterServiceLocator.getVillageManager().searchVillage(crKodeKelurahan.getText(), crNamaKelurahan.getText());
		
		if (searchResul.size() > 0) {
			Iterator it = searchResul.iterator();
			while (it.hasNext()) {
				village = (MsVillage) it.next();
				Listitem item = new Listitem();
				item.setValue(village.getNVillageId());
				item.setParent(crKelurahanList);

				Listcell kode = new Listcell(village.getVVillageCode());
				kode.setParent(item);

				Listcell nama = new Listcell(village.getVVillageName());
				nama.setParent(item);
			}
		}else{
			try {
				Messagebox.show("Tidak ada data yg sesuai dengan kriteria pencarian");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		MiscTrxController.setFont(crKelurahanList);
	}
	
	
	public void getVillageDataList(Listbox villageList) throws VONEAppException{
		
		
		villageService.getVillageData(villageList);
		MiscTrxController.setFont(villageList);
			
		
	}	

	
	public static void getAllVillageList(Listbox listbox)throws VONEAppException{
		
		
	}
			
}

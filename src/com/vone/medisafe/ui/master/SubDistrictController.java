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

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsSubDistrict;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.SubDstrictManager;
import com.vone.medisafe.ui.base.BaseController;

/**
 * SubDistrictController.java
 * @author Arifullah Ibn Rusyd
 * @since 29-07-2006
 * @version 0.1
 * used to controll all data flow from.zul file related to subdistrict/kecamatan data
 */

public class SubDistrictController extends BaseController{
	
	ZulConstraint constraint = new ZulConstraint();
	
	SubDstrictManager service = MasterServiceLocator.getSubDistrictManager();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		super.doCancel(win);
		id.setValue(null);
		name.setValue(null);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox subdistrictList = (Listbox)win.getFellow("subdistrictList");
		if(subdistrictList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.subdistrict.subdistrictlist.notselected"));
			return;
		}
		super.doDelete(win);
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getSubDistrictManager().deleteSubdistrictById((Integer)subdistrictList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getSubdistrictListData(subdistrictList);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox subdistrictList = (Listbox)win.getFellow("subdistrictList");
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		if(subdistrictList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.subdistrict.subdistrictlist.notselected"));
			return;
		}
		super.doModify(win);
		id.setText(((Listcell)subdistrictList.getSelectedItem().getChildren().get(0)).getLabel());
		name.setText(((Listcell)subdistrictList.getSelectedItem().getChildren().get(1)).getLabel());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!constraint.validateComponent(true)) return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox subdistrictList = (Listbox)win.getFellow("subdistrictList");
		if(MasterServiceLocator.getSubDistrictManager().getSubdistrictByCode(id.getText()) != null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		super.doSaveAdd(win);
		MsSubDistrict subdistrict = new MsSubDistrict();
		subdistrict.setDWhnCreate(new Date());
		subdistrict.setVSubDistrictId(id.getText());
		subdistrict.setVSubDistrictName(name.getText());
		MasterServiceLocator.getSubDistrictManager().save(subdistrict);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getSubdistrictListData(subdistrictList);
		MiscTrxController.setFont(subdistrictList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox subdistrictList = (Listbox)win.getFellow("subdistrictList");
		
		super.doSaveModify(win);
		MsSubDistrict subdistrict = new MsSubDistrict();
		subdistrict.setNSubdistrictId((Integer)subdistrictList.getSelectedItem().getValue());
		subdistrict.setVSubDistrictId(id.getText());
		subdistrict.setVSubDistrictName(name.getText());
		MasterServiceLocator.getSubDistrictManager().save(subdistrict);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getSubdistrictListData(subdistrictList);
		MiscTrxController.setFont(subdistrictList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Textbox crKodeKecamatan = (Textbox)win.getFellow("crKodeKecamatan");
		Textbox crNamaKecamatan = (Textbox)win.getFellow("crNamaKecamatan"); 
		Listbox subdistrictList = (Listbox)win.getFellow("subdistrictList");
		Listbox searchSubdistrictList = (Listbox)win.getFellow("searchSubdistrictList");
		
		constraint.registerComponent(id, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(name, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(crKodeKecamatan, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(crNamaKecamatan, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(id, ZulConstraint.NO_EMPTY);
		constraint.registerComponent(name, ZulConstraint.NO_EMPTY);
		constraint.validateComponent(false);
		getSubdistrictListData(subdistrictList);
		searchSubdistrictList.getItems().clear();
		
	}
	
	public void doSearch(Component win){
		Textbox crKodeKecamatan = (Textbox)win.getFellow("crKodeKecamatan");
		Textbox crNamaKecamatan = (Textbox)win.getFellow("crNamaKecamatan");
		Listbox searchSubdistrictList = (Listbox)win.getFellow("searchSubdistrictList");
		MsSubDistrict sub = null;
		
		List result = MasterServiceLocator.getSubDistrictManager().searchSubdistrict(crKodeKecamatan.getText(), crNamaKecamatan.getText());
		Iterator it = result.iterator();
		while(it.hasNext()){
			sub = (MsSubDistrict)it.next();
			Listitem item = new Listitem();
			item.setValue(sub.getNSubdistrictId());
			item.setParent(searchSubdistrictList);
			
			Listcell code = new Listcell(sub.getVSubDistrictId());
			code.setParent(item);
			Listcell name = new Listcell(sub.getVSubDistrictName());
			name.setParent(item);
		}
		MiscTrxController.setFont(searchSubdistrictList);
	}
	
	public void getSubdistrictData(Component win) throws InterruptedException{
		Listbox searchSubdistrictList = (Listbox)win.getFellow("searchSubdistrictList");
		Listbox subdistrictList = (Listbox)win.getFellow("subdistrictList");
		Textbox crKodeKecamatan = (Textbox)win.getFellow("crKodeKecamatan");
		Textbox crNamaKecamatan = (Textbox)win.getFellow("crNamaKecamatan");
		Tab tab1 = (Tab)win.getFellow("tab1");
		Tab tab2 = (Tab)win.getFellow("tab2");
		
		if(searchSubdistrictList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.subdistrict.subdistrictlist.notselected"));
			return;
		}
		subdistrictList.getItems().clear();
		Listitem item = searchSubdistrictList.getSelectedItem();
		item.setParent(subdistrictList);
		searchSubdistrictList.getItems().clear();
		crKodeKecamatan.setValue(null);
		crNamaKecamatan.setValue(null);
		
		tab2.setSelected(false);
		tab1.setSelected(true);
		MiscTrxController.setFont(subdistrictList);
	}

	
	public void getSubdistrictListData(Listbox subdistrictList) throws VONEAppException{
		
		service.getSubdistrictData(subdistrictList);
		MiscTrxController.setFont(subdistrictList);
					
	}
	
	public static void getSubdistrictList(Listbox listbox){
		
	}
	
}

package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsProvince;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

/**
 * ProvinceController.java
 * @author Arifullah Ibn Rusyd
 * @version 0.3 -- last revised at 03-08-2006
 * @since 29-07-2006
 * used to controll all data flow from .zul file which used province data
 */

public class ProvinceController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		super.doCancel(win);
		id.setValue(null);
		name.setValue(null);
		id.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox prov = (Listbox)win.getFellow("provinceList");
		if(prov.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.province.provincelist.notselected"));
			return;
		}
		super.doDelete(win);
		
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getProvinceManager().deleteProvincyByid((Integer)prov.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getProvinceDataList(prov);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox prov = (Listbox)win.getFellow("provinceList");
		if(prov.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.province.provincelist.notselected"));
			return;
		}
		super.doModify(win);
		id.setText(((Listcell)prov.getSelectedItem().getChildren().get(0)).getLabel());
		name.setText(((Listcell)prov.getSelectedItem().getChildren().get(1)).getLabel());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!constraints.validateComponent(true)) return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox prov = (Listbox)win.getFellow("provinceList");
		super.doSaveAdd(win);
		
		if(MasterServiceLocator.getProvinceManager().getProvinceByCode(name.getText()) != null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		
		MsProvince province = new MsProvince();
		province.setDWhnCreate(new Date());
		province.setVProvinceId(id.getText());
		province.setVProvinceName(name.getText());
		MasterServiceLocator.getProvinceManager().save(province);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getProvinceDataList(prov);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox prov = (Listbox)win.getFellow("provinceList");
		super.doSaveModify(win);
		
		MsProvince province = new MsProvince();
		province.setNProvinceId((Integer)prov.getSelectedItem().getValue());
		province.setVProvinceId(id.getText());
		province.setVProvinceName(name.getText());
		MasterServiceLocator.getProvinceManager().save(province);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getProvinceDataList(prov);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox prov = (Listbox)win.getFellow("provinceList");
		super.init(win);
		
		constraints.registerComponent(id, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(name, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(id, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name,ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		getProvinceDataList(prov);
	}

	public static void getProvinceDataList(Listbox prov) throws VONEAppException{
		prov.getItems().clear();
		List list = MasterServiceLocator.getProvinceManager().getAllProvince();
		Iterator it = list.iterator();
		MsProvince province = null;
		Listitem listitem = null;
		while(it.hasNext()){
			
			province = (MsProvince)it.next();
	        
            listitem = new Listitem();
            listitem.setValue(province.getNProvinceId());
            listitem.setParent(prov);

            Listcell nameCell = new Listcell(province.getVProvinceId());
            nameCell.setParent(listitem);
            Listcell surnameCell = new Listcell(province.getVProvinceName());
            surnameCell.setParent(listitem);
		}
		MiscTrxController.setFont(prov);

//		MasterServiceLocator.getProvinceManager().getProvinceForSelect(prov);
//		MsProvince province = null;
//						
//		Listitem listitem = null;
		
		
//		try{
//			List list = MasterServiceLocator.getProvinceManager().getAllProvince();
//			Iterator it = list.iterator();
//			while(it.hasNext()){
//				
//				province = (MsProvince)it.next();
//		        
//	            listitem = new Listitem();
//	            listitem.setValue(province.getNProvinceId());
//	            listitem.setParent(prov);
//
//	            Listcell nameCell = new Listcell(province.getVProvinceId());
//	            nameCell.setParent(listitem);
//	            Listcell surnameCell = new Listcell(province.getVProvinceName());
//	            surnameCell.setParent(listitem);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
	}
	
	public static void getAllProvinceList(Listbox listbox){
		
	}
	
}

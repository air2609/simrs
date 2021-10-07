package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsDivision;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DivisionManager;
import com.vone.medisafe.ui.base.BaseController;

/**
 * DivisionController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since sep, 06 2006
 */

public class DivisionController  extends BaseController{
	
	Textbox kode;
	Textbox nama;
	Listbox divisiList;
	Listbox unitRegistrasi;
	Intbox registrationCharge;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraint = new ZulConstraint();
	
	private DivisionManager serv = MasterServiceLocator.getDivisionManager();
	
	public void getDivisionList() throws VONEAppException{
			divisiList.getItems().clear();
			MsDivision village = null;
			
			Intbox intbox = new Intbox();
			intbox.setFormat("#,###");
			
			List list = serv.getAllDivision();
			Iterator it = list.iterator();
			while(it.hasNext()){
				village = new MsDivision();
				village = (MsDivision)it.next();
				item = new Listitem();
				item.setValue(village.getNDivisionId());
				item.setParent(divisiList);
				
				cell = new Listcell(village.getVDivisionCode());
				cell.setParent(item);
				
				cell = new Listcell(village.getVDivisionName());
				cell.setParent(item);
				
				if(village.getVRegistrationUnit() != null){
					if(village.getVRegistrationUnit().equalsIgnoreCase("YES")){
						cell = new Listcell("UNIT PENDAFTARAN");
						cell.setParent(item);
					}
					else {
						cell = new Listcell("NON UNIT PENDAFTARAN");
						cell.setParent(item);
					}
					
				}else{
					cell = new Listcell("NON UNIT PENDAFTARAN");
					cell.setParent(item);
				}
				
				
				if(village.getNRegistrationCharge() != null){
					intbox.setValue(village.getNRegistrationCharge());
					cell = new Listcell(intbox.getText());
					cell.setParent(item);
				}else{ 
					cell = new Listcell("0");
					cell.setParent(item);
				}
			}
			MiscTrxController.setFont(divisiList);
			
		}	


	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(win);
		kode.setValue(null);
		nama.setValue(null);
		this.registrationCharge.setValue(0);
		this.unitRegistrasi.setSelectedIndex(0);

	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(divisiList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.division.divisionlist.notselected"));
			return;
		}
		super.doDelete(win);
		int index = divisiList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			if(serv.deleteById((Integer)divisiList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				divisiList.removeItemAt(index);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if(divisiList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.division.divisionlist.notselected"));
			return;
		}
		super.doModify(win);
		item = divisiList.getSelectedItem();
		kode.setText(((Listcell)item.getChildren().get(0)).getLabel());
		nama.setText(((Listcell)item.getChildren().get(1)).getLabel());
		this.registrationCharge.setText(((Listcell)item.getChildren().get(3)).getLabel());
		
		List<Listitem> items = this.unitRegistrasi.getItems();
		for(Listitem item : items){
			if(item.getLabel().equalsIgnoreCase(item.getLabel()))
				this.unitRegistrasi.setSelectedItem(item);
		}
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!constraint.validateComponent(true)) return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		super.doSaveAdd(win);
		MsDivision cekDiv = MasterServiceLocator.getDivisionManager().getDivisionByCode(kode.getText());
		
		
		if (cekDiv == null) {
			MsDivision division = new MsDivision();
			division.setVDivisionCode(kode.getText());
			division.setVDivisionName(nama.getText());
			division.setVRegistrationUnit(this.unitRegistrasi.getSelectedItem().getValue().toString());
			division.setNRegistrationCharge(this.registrationCharge.getValue());
			division.setDWhnCreate(new Date());
			try {
				serv.save(division);
				Messagebox.show(MessagesService.getKey("common.add.success"));
				
				item = new Listitem();
				item.setValue(division.getNDivisionId());
				item.setParent(divisiList);
				
				cell = new Listcell(division.getVDivisionCode());
				cell.setParent(item);
				
				cell = new Listcell(division.getVDivisionName());
				cell.setParent(item);
				
				if(division.getVRegistrationUnit().equalsIgnoreCase("YES")){
					cell = new Listcell("UNIT PENDAFTARAN");
					cell.setParent(item);
				}else{
					cell = new Listcell("NON UNIT PENDAFTARAN");
					cell.setParent(item);
				}
				
				cell = new Listcell(this.registrationCharge.getText());
				cell.setParent(item);
				
				doCancel(win);
				
				
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
		}
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		super.doSaveModify(win);
				
		MsDivision division = new MsDivision();
		item = divisiList.getSelectedItem();
		division.setNDivisionId((Integer) item.getValue());
		division.setVDivisionCode(kode.getText());
		division.setVDivisionName(nama.getText());
		division.setNRegistrationCharge(this.registrationCharge.getValue());
		division.setVRegistrationUnit(this.unitRegistrasi.getSelectedItem().getValue().toString());
		serv.save(division);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		item.getChildren().clear();
		
		cell = new Listcell(division.getVDivisionCode());
		cell.setParent(item);
		
		cell = new Listcell(division.getVDivisionName());
		cell.setParent(item);
		
		cell = new Listcell(this.unitRegistrasi.getSelectedItem().getLabel());
		cell.setParent(item);
		
		cell = new Listcell(this.registrationCharge.getText());
		cell.setParent(item);
		
		doCancel(win);
			
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		kode = (Textbox)win.getFellow("id");
		nama = (Textbox)win.getFellow("name");
		divisiList = (Listbox)win.getFellow("divisionList");
		this.registrationCharge = (Intbox)win.getFellow("registrationCharge");
		this.unitRegistrasi = (Listbox)win.getFellow("unitRegistrasi");
		this.registrationCharge.setValue(0);
		
		constraint.registerComponent(kode, ZulConstraint.NO_EMPTY);
		constraint.registerComponent(nama, ZulConstraint.NO_EMPTY);
		
		constraint.registerComponent(kode, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(nama, ZulConstraint.UPPER_CASE);
		
		constraint.validateComponent(false);
		
		getDivisionList();
		
	}
	
	public static void getDropDownDivisionList(Listbox listbox) throws VONEAppException{
		listbox.getItems().clear();
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(listbox);
				
		MsDivision division = null;
		List list = MasterServiceLocator.getDivisionManager().getAllDivision();
		Iterator it = list.iterator();
		while(it.hasNext()){
			division = (MsDivision)it.next();
			item = new Listitem();
			item.setLabel(division.getVDivisionCode()+" - "+division.getVDivisionName());
			item.setValue(division.getNDivisionId());
			item.setParent(listbox);
		}
		if(listbox.getItems().size() > 0) listbox.setSelectedIndex(0);
	}
	
	public static void getDivisionForSelect(Listbox listbox) throws VONEAppException{
		listbox.getItems().clear();
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(listbox);
				
		MsDivision division = null;
		List list = MasterServiceLocator.getDivisionManager().getAllDivision();
		Iterator it = list.iterator();
		while(it.hasNext()){
			division = (MsDivision)it.next();
			item = new Listitem();
			item.setLabel(division.getVDivisionCode()+" - "+division.getVDivisionName());
			item.setValue(division);
			item.setParent(listbox);
		}
		if(listbox.getItems().size() > 0) listbox.setSelectedIndex(0);
	}
}

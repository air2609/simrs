package com.vone.medisafe.ui.master;


import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.StaffManager;
import com.vone.medisafe.ui.base.BaseController;


public class StaffController extends BaseController{
	
	private MsStaff staff;
//	private MsStaffInUnit siu;
	
	public Bandbox kodeStaff;
	public Textbox noTelp;
	public Textbox namaStaff;
	public Decimalbox gaji;
	public Textbox alamat;
	public Listbox unitList;
	public Bandbox coa;
	public Datebox tglMasuk;
	public Datebox tglKeluar;
	public Listbox dataStaffList;
	public Textbox crKodeStaff;
	public Textbox crNama;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
			
		super.doCancel(win);
		
		kodeStaff.setValue(null);
		noTelp.setValue(null);
		namaStaff.setValue(null);
		gaji.setValue(null);
		alamat.setValue(null);
		unitList.clearSelection();
		tglMasuk.setValue(null);
		tglKeluar.setValue(null);
		kodeStaff.focus();
		coa.setValue(null);
		coa.removeAttribute("coa");
		
		
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(win);
		if(dataStaffList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.staff.stafflist.notselected"));
			return;
		}
		staff = (MsStaff)dataStaffList.getSelectedItem().getValue();
		int index = dataStaffList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getStaffManager().delete(staff)){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				dataStaffList.removeItemAt(index);
				doCancel(win);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		Button btnSave = (Button)win.getFellow("btnSave");
		
		if(dataStaffList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.staff.stafflist.notselected"));
			return;
		}
		super.doModify(win);
		btnSave.setDisabled(false);
		
		StaffManager staffService = MasterServiceLocator.getStaffManager();
		
		staffService.prepareModify(this);
		
		
		
		
		
	
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
			
		super.doSaveAdd(win);
		
		if(MasterServiceLocator.getStaffManager().getStaffByCode(kodeStaff.getText())!= null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		if(unitList.getSelectedItems().size() == 0){
			Messagebox.show(MessagesService.getKey("master.unit.unitlist.notselected"));
			return;
		}
		
		staff = new MsStaff();
		
		staff.setVStaffCode(kodeStaff.getValue());
		if(gaji.getValue() != null)staff.setNStaffSalary(gaji.getValue().doubleValue());
		staff.setNStaffRole(MedisafeConstants.STAFF);
		staff.setVStaffAddr(alamat.getValue());
		if(noTelp.getValue().trim().length() > 0)staff.setVStaffPhNo(noTelp.getValue());
		staff.setVStaffName(namaStaff.getValue());
		if(tglMasuk.getValue() != null)staff.setDStaffHiredDate(tglMasuk.getValue());
		if(tglKeluar.getValue() != null)staff.setDStaffFiredDate(tglKeluar.getValue());
		
		
		MsCoa msCoa = (MsCoa)coa.getAttribute("coa");
		
		staff.setMsCoa(msCoa);
		
		
		if(MasterServiceLocator.getStaffManager().save(staff, unitList.getSelectedItems())){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setValue(staff);
			item.setParent(dataStaffList);
			
			cell = new Listcell(kodeStaff.getText());
			cell.setParent(item);
			
			cell = new Listcell(namaStaff.getText());
			cell.setParent(item);
			
			Set units = unitList.getSelectedItems();
			Iterator it = units.iterator();
			String itemselected = "";
			while(it.hasNext()){
				Listitem listitem = (Listitem)it.next();
				itemselected = itemselected + listitem.getLabel()+";";
			}
			itemselected = itemselected.substring(0,(itemselected.length()-1));
			cell = new Listcell(itemselected);
			cell.setParent(item);
//			getStaffDataList(dataStaffList);
			doCancel(win);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(win);
		
		if(unitList.getSelectedItems().size() == 0){
			Messagebox.show(MessagesService.getKey("master.unit.unitlist.notselected"));
			return;
		}
		item = dataStaffList.getSelectedItem();
		staff = (MsStaff)item.getValue();
		MasterServiceLocator.getStaffInUnitManager().deleteByStaffId(staff.getNStaffId());				
		staff.setVStaffCode(kodeStaff.getValue());
		if(gaji.getValue() != null)staff.setNStaffSalary(gaji.getValue().doubleValue());
		staff.setNStaffRole(MedisafeConstants.STAFF);
		staff.setVStaffAddr(alamat.getValue());
		if(noTelp.getValue().trim().length() > 0)staff.setVStaffPhNo(noTelp.getValue());
		staff.setVStaffName(namaStaff.getValue());
		if(tglMasuk.getValue() != null)staff.setDStaffHiredDate(tglMasuk.getValue());
		if(tglKeluar.getValue() != null)staff.setDStaffFiredDate(tglKeluar.getValue());
		
		MsCoa msCoa = (MsCoa)coa.getAttribute("coa");
		staff.setMsCoa(msCoa);
		
		
		if(MasterServiceLocator.getStaffManager().save(staff, unitList.getSelectedItems())){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			item.getChildren().clear();
			item.setValue(staff);
						
			cell = new Listcell(kodeStaff.getText());
			cell.setParent(item);
			
			cell = new Listcell(namaStaff.getText());
			cell.setParent(item);
			
			Set units = unitList.getSelectedItems();
			Iterator it = units.iterator();
			String itemselected = "";
			while(it.hasNext()){
				Listitem listitem = (Listitem)it.next();
				itemselected = itemselected + listitem.getLabel()+";";
			}
			itemselected = itemselected.substring(0,(itemselected.length()-1));
			cell = new Listcell(itemselected);
			cell.setParent(item);
			
			doCancel(win);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		kodeStaff = (Bandbox)win.getFellow("kodeStaff");
		noTelp = (Textbox)win.getFellow("noTelp");
		namaStaff = (Textbox)win.getFellow("namaStaff");
		gaji = (Decimalbox)win.getFellow("gaji");
		alamat = (Textbox)win.getFellow("alamat");
		unitList = (Listbox)win.getFellow("unitList");
		tglMasuk = (Datebox)win.getFellow("tglMasuk");
		tglKeluar = (Datebox)win.getFellow("tglKeluar");
		dataStaffList =(Listbox) win.getFellow("dataStaffList");
		crKodeStaff = (Textbox)win.getFellow("crKodeStaff");
		crNama = (Textbox)win.getFellow("crNama");
		coa = (Bandbox)win.getFellow("coa");
		
		super.init(win);
		
		constraints.registerComponent(namaStaff, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(alamat, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kodeStaff, ZulConstraint.NO_EMPTY);
		
		constraints.registerComponent(namaStaff, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(alamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(kodeStaff, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crKodeStaff, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		
		
		constraints.validateComponent(false);
		
//		UnitController unitServie = new UnitController();
		UnitController.getStaffUnitList(unitList);
//		UnitController.getUnitForSelect(unitList);
//		CoaController.getCoaForSelect(coaList, MedisafeConstants.COA_ALL);
		getStaffDataList(dataStaffList);
		kodeStaff.focus();
		MiscTrxController.setFont(dataStaffList);
	}
	
	
	public void getStaffDataList(Listbox dataStaffList) throws VONEAppException{
		
		MasterServiceLocator.getStaffManager().getStaffByRole(dataStaffList);
		MiscTrxController.setFont(dataStaffList);

		
	}
	
	public void doSearchStaff(Component win) throws InterruptedException, VONEAppException{
		Listbox dataStaffList =(Listbox) win.getFellow("staffSearchList");
		dataStaffList.getItems().clear();
		
		MasterServiceLocator.getDoctorManager().searchDoctor(crKodeStaff.getText(), 
				crNama.getText(), dataStaffList, MedisafeConstants.STAFF);
		
		/**		
		List list = MasterServiceLocator.getStaffManager().searchStaff(crKodeStaff.getText(), crNama.getText());
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			staff = (MsStaff)it.next();
			item = new Listitem();
			item.setValue(staff);
			item.setParent(dataStaffList);
			
			Listcell kode = new Listcell(staff.getVStaffCode());
			kode.setParent(item);
			Listcell nama = new Listcell(staff.getVStaffName());
			nama.setParent(item);
			
			Set siu = staff.getMsStaffInUnits();
			String unit = "";
			Iterator itr = siu.iterator();
			while(itr.hasNext()){
				MsStaffInUnit staffInUnit = (MsStaffInUnit)itr.next();
				unit = unit + staffInUnit.getId().getMsUnit().getVUnitName()+";";
			}
			unit = unit.substring(0,(unit.length()-1));
			Listcell unitcell = new Listcell(unit);
			unitcell.setParent(item);
		}*/
		doCancel(win);
	}
	
	public void getStaffDetail(Component win){
		
		Button btnSave = (Button)win.getFellow("btnSave");
		Listbox staffSearchList =(Listbox) win.getFellow("staffSearchList");
		
		
		staff = (MsStaff)staffSearchList.getSelectedItem().getValue();
		kodeStaff.setValue(staff.getVStaffCode());
		noTelp.setValue(staff.getVStaffPhNo());
		namaStaff.setValue(staff.getVStaffName());
		gaji.setValue(new BigDecimal(staff.getNStaffSalary()));
		alamat.setValue(staff.getVStaffAddr());
		tglMasuk.setValue(staff.getDStaffHiredDate());
		tglKeluar.setValue(staff.getDStaffFiredDate());
		
		for(int i=0; i < dataStaffList.getItems().size(); i++){
			MsStaff dataStaff = (MsStaff)dataStaffList.getItemAtIndex(i).getValue();
			if(dataStaff.getNStaffId().equals(staff.getNStaffId())){
				dataStaffList.setSelectedIndex(i);
				break;
			}
		}
		
		item = staffSearchList.getSelectedItem();
		Listcell subdivisi = (Listcell)item.getChildren().get(2);
		String [] units = subdivisi.getLabel().split(";");
		
		
		
		Listbox listbox = new Listbox();
		listbox.setMultiple(true);
		Listitem item;
		

		int counter = 0;
		for(int i=0; i < units.length; i++){
			for(int j=0; j < unitList.getItems().size(); j++){
				if(unitList.getItemAtIndex(j).getLabel().equals(units[i])){
					
					item = unitList.getItemAtIndex(j);
					item.setParent(listbox);
//					listbox.addItemToSelection(item);

				}
			}
		}
		
		List<Listitem> items = unitList.getItems();
		
				
		for(Listitem listitem : items){
			
			item = (Listitem)listitem.clone();
			item.setParent(listbox);
					
		}
		
			
		unitList.getItems().clear();
		
		List<Listitem> listboxItems = listbox.getItems();
		for(Listitem itm : listboxItems){
			item = (Listitem)itm.clone();
			item.setParent(unitList);
			if(counter < units.length){
				unitList.addItemToSelection(item);
			}
			counter++;
		}
		
		
		btnSave.setDisabled(true);
		
	}


}

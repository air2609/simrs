package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsDivision;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.ui.base.BaseController;
/**
 * UnitController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Sep, 06 2006	
 */

public class UnitController extends BaseController
{
	private MsUnit unit;
	
	public Listbox division;
	public Listbox unitList;
	public Textbox kode;
	public Textbox nama;
	public Listbox warehouseList;
	public Bandbox coa;
	public Listbox unitType;
	
	Listitem item;
	Listcell cell;
	
	UnitManager serv = MasterServiceLocator.getUnitManager();
	
	ZulConstraint constraint = new ZulConstraint();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(win);
		
		nama.setValue(null);
		kode.setValue(null);
		division.setSelectedIndex(0);
		warehouseList.setSelectedIndex(0);
		coa.setValue(null);
		unitList.clearSelection();
		
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		unitList = (Listbox)win.getFellow("unitList");
		if(unitList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.unit.unitlist.notselected"));
			return;
		}
		super.doDelete(win);
		
		int index = unitList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(serv.deleteUnitById(((MsUnit)unitList.getSelectedItem().getValue()).getNUnitId())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				unitList.removeItemAt(index);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
		
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if(unitList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.unit.unitlist.notselected"));
			return;
		}

		super.doModify(win);
		
		UnitManager service = MasterServiceLocator.getUnitManager();
		service.prepareModify(this);
		
//		serv.doModify(this);
		
		
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!constraint.validateComponent(true)) return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if(serv.getUnitByCode(kode.getText()) != null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		super.doSaveAdd(win);
		unit = new MsUnit();
		unit.setVUnitCode(kode.getText());
		unit.setVUnitName(nama.getText());
		unit.setDWhnCreate(new Date());
		unit.setMsDivision((MsDivision)division.getSelectedItem().getValue());
		if(warehouseList.getSelectedIndex() != 0){
			unit.setMsWarehouse((MsWarehouse)warehouseList.getSelectedItem().getValue());
		}
		unit.setUnitType(new Integer(unitType.getSelectedItem().getValue().toString()));
		unit.setCoa((MsCoa)coa.getAttribute("coa"));
//		if(coaList.getSelectedIndex() != 0){
//			unit.setCoa((MsCoa)coaList.getSelectedItem().getValue());
//		}
			
		if(serv.save(unit)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setParent(unitList);
			item.setValue(unit);
			item.setAttribute("warehouse", unit.getMsWarehouse());
					
			
			cell = new Listcell(unit.getVUnitCode());
			cell.setParent(item);
			
			cell = new Listcell(unit.getMsDivision().getVDivisionName());
			cell.setParent(item);
			
			cell = new Listcell(unit.getVUnitName());
			cell.setParent(item);
			
			cell = new Listcell(unitType.getSelectedItem().getLabel());
			cell.setParent(item);
			
			if(unit.getMsWarehouse()!= null){
				cell = new Listcell(unit.getMsWarehouse().getVWhouseName());
				cell.setParent(item);
			}else{
				cell = new Listcell("-");
				cell.setParent(item);
			}
			
			
					
			doCancel(win);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		
		
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		super.doSaveModify(win);
		
		item = unitList.getSelectedItem();
		unit = (MsUnit)item.getValue();
		unit.setVUnitCode(kode.getText());
		unit.setVUnitName(nama.getText());
		unit.setMsDivision((MsDivision)division.getSelectedItem().getValue());
		if(warehouseList.getSelectedIndex() != 0){
			unit.setMsWarehouse((MsWarehouse)warehouseList.getSelectedItem().getValue());
		}
		unit.setUnitType(new Integer(unitType.getSelectedItem().getValue().toString()));
		unit.setCoa((MsCoa)coa.getAttribute("coa"));
//		if(coaList.getSelectedIndex() != 0){
//			unit.setCoa((MsCoa)coaList.getSelectedItem().getValue());
//		}
				
		if(serv.save(unit)){
			
			
			item.getChildren().clear();
			item.setAttribute("warehouse", unit.getMsWarehouse());
					
			
			cell = new Listcell(unit.getVUnitCode());
			cell.setParent(item);
			
			cell = new Listcell(unit.getMsDivision().getVDivisionName());
			cell.setParent(item);
			
			cell = new Listcell(unit.getVUnitName());
			cell.setParent(item);
			
			cell = new Listcell(unitType.getSelectedItem().getLabel());
			cell.setParent(item);
			
			if(unit.getMsWarehouse() != null){
				cell = new Listcell(unit.getMsWarehouse().getVWhouseName());
				cell.setParent(item);
			}else{
				cell = new Listcell("-");
				cell.setParent(item);
			}
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			doCancel(win);
		}else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		
			
		
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		division = (Listbox)win.getFellow("divisionList");
		unitList = (Listbox)win.getFellow("unitList");
		kode = (Textbox)win.getFellow("id");
		nama = (Textbox)win.getFellow("name");
		warehouseList = (Listbox)win.getFellow("warehouseList");
		coa = (Bandbox)win.getFellow("coa");
		unitType = (Listbox)win.getFellow("unitType");
		
		constraint.registerComponent(division, ZulConstraint.NO_EMPTY);
		constraint.registerComponent(kode, ZulConstraint.NO_EMPTY);
		constraint.registerComponent(nama, ZulConstraint.NO_EMPTY);
		constraint.registerComponent(kode, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(nama, ZulConstraint.UPPER_CASE);
		constraint.validateComponent(false);
		
		
		DivisionController.getDivisionForSelect(division);
		WarehouseController.getWarehouseList(warehouseList);
//		CoaController.getCoaForSelect(coaList, MedisafeConstants.COA_ALL);
		getUnitList();
		
		
	}
	
 
	public void getUnitList() throws VONEAppException{
		
		
		serv.getAllUnit(unitList);
		MiscTrxController.setFont(unitList);
		
	}
	
	
	public static void getAllUnit(Listbox listbox) throws VONEAppException
	
	{
		UnitManager unitServie = MasterServiceLocator.getUnitManager();
		unitServie.getAllUnit(listbox);

	}
	
	public static void getStaffUnitList(Listbox listbox) throws VONEAppException{
		listbox.getItems().clear();
		MsUnit msUnit = null;
		
		UnitManager service = MasterServiceLocator.getUnitManager();
		
		Listitem item;
		
		List list = service.getUnitForSelect();
		Iterator it = list.iterator();
		while(it.hasNext()){
			msUnit = (MsUnit)it.next();
			item = new Listitem();
			item.setValue(msUnit.getNUnitId());
			item.setParent(listbox);
			Listcell name = new Listcell(msUnit.getVUnitName());
			name.setParent(item);
		}
		MiscTrxController.setFont(listbox);
		
	}
	
	public static void getUnitForSelect(Listbox listbox) throws VONEAppException
	{
		UnitManager service = MasterServiceLocator.getUnitManager();
		MiscTrxController.setFont(listbox);
		
		service.getMsUnitForSelect(listbox);
//		listbox.getItems().clear();
//		Listitem item = new Listitem();
//		item.setValue(MedisafeConstants.LISTKOSONG);
//		item.setLabel(MedisafeConstants.LABELKOSONG);
//		item.setParent(listbox);
//		MsUnit ptype;
//		
//		List list = serv.getAllUnit();
//		Iterator it = list.iterator();
//		int i = 1;
//		while(it.hasNext()){
//			ptype = (MsUnit)it.next();
//			item = new Listitem();
//			item.setValue(ptype);
//			item.setLabel(i+". "+ptype.getVUnitCode()+"-"+ptype.getVUnitName());
//			item.setParent(listbox);
//			i++;
//		}
//		if(listbox.getItems().size() > 0)listbox.setSelectedIndex(0);
	}
}

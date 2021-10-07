package com.vone.medisafe.service.ifaceimpl.master;


import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsDivision;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUnitDAO;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.ui.master.UnitController;

/**
 * UnitManagerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Sep, 06 2006
 * @category service
 */

public class UnitManagerImpl implements UnitManager{
	
	private MsUnitDAO dao;

	public MsUnitDAO getDao() {
		return dao;
	}

	public void setDao(MsUnitDAO dao) {
		this.dao = dao;
	}

	public boolean save(MsUnit unit) throws VONEAppException {
		return this.dao.save(unit);
	}

	public void delete(MsUnit unit) throws VONEAppException {
		this.dao.delete(unit);
	}

	public MsUnit getById(Integer id) throws VONEAppException {
		return this.dao.findById(id);
	}

	public MsUnit getUnitByCode(String code) throws VONEAppException {
		return dao.getUnitByCode(code);
	}

	public boolean deleteUnitById(Integer id) throws VONEAppException {
		return dao.deleteUnitById(id);
	}
	

	public void getMsUnitForSelect(Listbox listbox) throws VONEAppException {
		
		listbox.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(listbox);
		
				
		List<MsUnit> list = this.dao.getAllUnit(); 
		int i = 1;
		
		
		
		for(MsUnit unit : list){
			
			item = new Listitem();
			item.setValue(unit);
			item.setLabel(i+". "+unit.getVUnitCode()+"-"+unit.getVUnitName());
			item.setParent(listbox);
			i++;
		}
		
		if(listbox.getItems().size() > 0)listbox.setSelectedIndex(0);
		
	}
	
	public void getRegistrationUnit(Listbox listbox) throws VONEAppException{
		
		listbox.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(listbox);
		
				
		List<MsUnit> list = this.dao.getRegistrationUnit();
		int i = 1;
		
		
		
		for(MsUnit unit : list){
			
			item = new Listitem();
			item.setValue(unit);
			item.setLabel(i+". "+unit.getVUnitCode()+"-"+unit.getVUnitName());
			item.setParent(listbox);
			i++;
		}
		
		if(listbox.getItems().size() > 0)listbox.setSelectedIndex(0);
		
	}

	public void getAllUnit(Listbox unitList) throws VONEAppException {
		List<MsUnit> list = dao.getAllUnit();
		unitList.getItems().clear();
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			MsUnit unit = (MsUnit)it.next();
			Listitem item = new Listitem();
			item.setValue(unit);
			item.setAttribute("warehouse", unit.getMsWarehouse());
			item.setParent(unitList);
			
			Listcell cell = new Listcell(unit.getVUnitCode());
			cell.setParent(item);
			
			cell = new Listcell(unit.getMsDivision().getVDivisionName());
			cell.setParent(item);
			
			cell = new Listcell(unit.getVUnitName());
			cell.setParent(item);
			
			cell = new Listcell();
			if(unit.getUnitType() != null){
				if(unit.getUnitType().intValue() == 1)
					cell.setLabel("TRANSAKSIONAL");
				else cell.setLabel("NON TRANSAKSIONAL");
			}
			else cell.setLabel("NON TRANSAKSIONAL");
			cell.setParent(item);
			
			if(unit.getMsWarehouse() != null){
				cell = new Listcell(unit.getMsWarehouse().getVWhouseName());
				cell.setParent(item);
			}
			else{
				cell = new Listcell("-");
				cell.setParent(item);
			}
			
		}

	}

	public List getAllUnit() throws VONEAppException {
		return dao.getAllUnit();
	}
	
	
	public List getUnitForSelect() throws VONEAppException{
		return dao.getAllUnitList();
	}

	
	
	
	public void prepareModify(UnitController controller) throws VONEAppException {
		
		Listitem item = controller.unitList.getSelectedItem();
		
		
		
		MsWarehouse whouse = (MsWarehouse)item.getAttribute("warehouse");
		MsUnit unit =(MsUnit)item.getValue();
		
		unit = this.dao.findById(unit.getNUnitId());
		
		controller.kode.setValue(unit.getVUnitCode());
		controller.nama.setValue(unit.getVUnitName());
		
		controller.coa.setValue(unit.getCoa().getVAcctNo()+" - "+unit.getCoa().getVAcctName());
		controller.coa.setAttribute("coa", unit.getCoa());
		
		
		for(int i=1; i < controller.division.getItems().size(); i++){
			if(((MsDivision)controller.division.getItemAtIndex(i).getValue()).getNDivisionId().equals(unit.getMsDivision().getNDivisionId())){
				controller.division.setSelectedIndex(i);
				break;
			}
		}
		if(whouse != null){
			for(int i=1; i < controller.warehouseList.getItems().size(); i++){
				if(((MsWarehouse)controller.warehouseList.getItemAtIndex(i).getValue()).getNWhouseId().equals(whouse.getNWhouseId())){
					controller.warehouseList.setSelectedIndex(i);
					break;
				}
			}
		}else{
			controller.warehouseList.setSelectedIndex(0);
		}
		
		String unitType = ((Listcell)item.getChildren().get(3)).getLabel();
		
		List<Listitem> items = controller.unitType.getItems();
		for(Listitem itm : items){
			if(itm.getLabel().equalsIgnoreCase(unitType))
				controller.unitType.setSelectedItem(itm);
		}
		
	}
	
	
}

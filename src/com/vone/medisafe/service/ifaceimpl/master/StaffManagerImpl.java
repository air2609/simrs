package com.vone.medisafe.service.ifaceimpl.master;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsStaffDAO;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.service.iface.master.StaffManager;
import com.vone.medisafe.ui.master.StaffController;

public class StaffManagerImpl implements StaffManager{
	
	private MsStaffDAO dao;

	public MsStaffDAO getDao() {
		return dao;
	}

	public void setDao(MsStaffDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsStaff staff) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.delete(staff);
	}

	public MsStaff getByStaffId(Integer id) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public List getAllStaff() throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.getAllStaff(MsStaff.class);
	}

	public List getStaffByCriteria(MsStaff staff) throws VONEAppException{
		// TODO Auto-generated method stub
		return null;
	}

	public List getStaffByRole(short role) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.getStaffByRole(role);
	}

	public MsStaff getStaffByCode(String code) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getStaffByCode(code);
	}

	public boolean save(MsStaff staff, Set units) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(staff,units);
	}

	public List searchStaff(String code, String name) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.serarchStaff(code, name);
	}

	public void getStaffByRole(Listbox dataStaffList) throws VONEAppException {
		dataStaffList.getItems().clear();
		
		List list = getStaffByRole(MedisafeConstants.STAFF);
		Iterator it = list.iterator();
		while(it.hasNext()){
			MsStaff staff = (MsStaff)it.next();
			Listitem item = new Listitem();
			item.setValue(staff);
			item.setParent(dataStaffList);
			
			Listcell cell = new Listcell(staff.getVStaffCode());
			cell.setParent(item);
			cell = new Listcell(staff.getVStaffName());
			cell.setParent(item);
			
			Set siuSet = staff.getMsStaffInUnits();
			String unit = "";
			Iterator itr = siuSet.iterator();
			while(itr.hasNext()){
				MsStaffInUnit siu = (MsStaffInUnit)itr.next();
				unit = unit + siu.getId().getMsUnit().getVUnitName()+";";
			}
			if(unit.length()>0)	unit = unit.substring(0,(unit.length()-1));
			cell = new Listcell(unit);
			cell.setParent(item);
		}
		 
	}
	

	public void prepareModify(StaffController controller) throws VONEAppException {
		
		MsStaff staff = (MsStaff)controller.dataStaffList.getSelectedItem().getValue();
		
		staff = this.dao.findById(staff.getNStaffId());
		
		controller.kodeStaff.setValue(staff.getVStaffCode());
		controller.noTelp.setValue(staff.getVStaffPhNo());
		controller.namaStaff.setValue(staff.getVStaffName());
		controller.gaji.setValue(new BigDecimal(staff.getNStaffSalary()));
		controller.alamat.setValue(staff.getVStaffAddr());
		controller.tglMasuk.setValue(staff.getDStaffHiredDate());
		controller.tglKeluar.setValue(staff.getDStaffFiredDate());
		
		Listitem item = controller.dataStaffList.getSelectedItem();
		Listcell subdivisi = (Listcell)item.getChildren().get(2);
		String [] units = subdivisi.getLabel().split(";");
		
		Listbox listbox = new Listbox();
		listbox.setMultiple(true);
		Listitem itm;
		
		int counter = 0;
		
		for(int i=0; i < units.length; i++){
			for(int j=0; j < controller.unitList.getItems().size(); j++){
				if(controller.unitList.getItemAtIndex(j).getLabel().equals(units[i])){
					
					itm = controller.unitList.getItemAtIndex(j);
					itm.setParent(listbox);
//					listbox.addItemToSelection(itm);
//					controller.unitList.addItemToSelection(controller.unitList.getItemAtIndex(j));
				}
			}
		}
		
		
		List<Listitem> items = controller.unitList.getItems();
		
		
		for(Listitem listitem : items){
			
			itm = (Listitem)listitem.clone();
			itm.setParent(listbox);
					
		}
		
			
		controller.unitList.getItems().clear();
		
		List<Listitem> listboxItems = listbox.getItems();
		for(Listitem listitem : listboxItems){
			itm = (Listitem)listitem.clone();
			itm.setParent(controller.unitList);
			if(counter < units.length){
				controller.unitList.addItemToSelection(itm);
			}
			counter++;
		}
		
		
		if(staff.getMsCoa() != null){
			controller.coa.setValue(staff.getMsCoa().getVAcctNo()+" - "+staff.getVStaffName());
		}
		
		
		
	}

	public void searchStaff(String code, String name, Listbox staffList) throws VONEAppException {
		
		List listStaff = dao.serarchStaff(code, name);
		
		if (listStaff == null) return;
		
		Iterator it = listStaff.iterator();
		
		String divisi;
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			divisi = "";
			MsStaff staffPojo = (MsStaff)it.next();
			
			item.appendChild(new Listcell(staffPojo.getVStaffCode()));
			item.appendChild(new Listcell(staffPojo.getVStaffName()));
			
			Set<MsStaffInUnit> siu = staffPojo.getMsStaffInUnits();
			Iterator<MsStaffInUnit> iter = siu.iterator();
			while(iter.hasNext()){
				MsStaffInUnit unit = iter.next();
				divisi = divisi + unit.getId().getMsUnit().getVUnitName()+";";
			}
			item.appendChild(new Listcell(divisi));
			item.setValue(staffPojo.getNStaffId());
			
			item.setParent(staffList);
		}
		
	}

	


}

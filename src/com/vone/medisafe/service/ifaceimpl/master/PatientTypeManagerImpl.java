package com.vone.medisafe.service.ifaceimpl.master;


import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsPatientTypeDAO;
import com.vone.medisafe.service.iface.master.PatientTypeManager;

public class PatientTypeManagerImpl implements PatientTypeManager{

	private MsPatientTypeDAO dao;
	public MsPatientTypeDAO getDao() {
		return dao;
	}
	public void setDao(MsPatientTypeDAO dao) {
		this.dao = dao;
	}
	public void save(MsPatientType patientType) {
		// TODO Auto-generated method stub
		this.dao.save(patientType);
	}
	
	
	
	public MsPatientType getPatienType(MsPatientType patientType) {
		// TODO Auto-generated method stub
		return null;
	}
	public void delete(MsPatientType pType) {
		// TODO Auto-generated method stub
		this.dao.delete(pType);
		
	}
	public MsPatientType getById(Integer id) {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}
	public MsPatientType getByCode(String code) {
		// TODO Auto-generated method stub
		return dao.getPatientTypeByCode(code);
	}
	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteByid(id);
	}
	
	
	public void getPatientTypeForSelect(Listbox tipePasienList) throws VONEAppException {
		
		tipePasienList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		
		item.setParent(tipePasienList);
		
		
		
		List<MsPatientType> list = this.dao.getAllPatientType();
		
		
		for(MsPatientType ptype : list){
			
			item = new Listitem();
			item.setValue(ptype);
			item.setLabel(ptype.getVTpatient()+" - "+ ptype.getVTpatientDesc());
			item.setParent(tipePasienList);
		}
		
		tipePasienList.setSelectedIndex(0);
		
	}
	
	
	
	public void getAllPatientTypeData(Listbox prov) throws VONEAppException {
		
		prov.getItems().clear();
		
		Listitem item;
		Listcell cell;
		
		
		List<MsPatientType> list = this.dao.getAllPatientType();
			
			
		for(MsPatientType type : list){
				
				
	         item = new Listitem();
	         item.setValue(type.getNPatientTypeId());
	         item.setParent(prov);


	         cell = new Listcell(type.getVTpatient());
	         cell.setParent(item);
	          
	         cell = new Listcell(type.getVTpatientDesc());
	         cell.setParent(item);
	          
		}
		
		
	}
	
	

}

package com.vone.medisafe.service.ifaceimpl.master;


import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentClassDAO;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;

public class TreatmentClassManagerImpl implements TreatmentClassManager{
	
	private MsTreatmentClassDAO dao;

	/**
	 * @return Returns the dao.
	 */
	public MsTreatmentClassDAO getDao() {
		return dao;
	}

	/**
	 * @param dao The dao to set.
	 */
	public void setDao(MsTreatmentClassDAO dao) {
		this.dao = dao;
	}

	public void save(MsTreatmentClass treatmentClass) throws VONEAppException {
		// TODO Auto-generated method stub
		this.dao.save(treatmentClass);
	}

	public void delete(MsTreatmentClass treatmentClass) throws VONEAppException {
		// TODO Auto-generated method stub
		this.dao.delete(treatmentClass);
	}

	public MsTreatmentClass getById(Integer id) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	

	public MsTreatmentClass getTClassByCode(String code) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getByCode(code);
	}

	public boolean deleteById(Integer id) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}

	
	public void getTClassForSelect(Listbox tclassList) throws VONEAppException {
		
		tclassList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(tclassList);
		
		
		int i = 1;
		List<MsTreatmentClass> list = dao.getAllTreatmentClass();
		
		
		for(MsTreatmentClass tclass : list){
			
			item = new Listitem();
			item.setValue(tclass);
			item.setLabel(i+". "+tclass.getVTclassDesc());
			item.setParent(tclassList);
			
			i++;
		}
		
		tclassList.setSelectedIndex(0);
		
	}
	

	
	public void getTreatmentClassData(Listbox treatmentClassList) throws VONEAppException {
		
		treatmentClassList.getItems().clear();
		
		
		Listitem item;
		Listcell cell;
		
		List<MsTreatmentClass> list = this.dao.getAllTreatmentClass();
		
		for(MsTreatmentClass tclass : list){
			
			item = new Listitem();
			item.setValue(tclass);//ganti jadi integer??  .getNTclassId()
			item.setParent(treatmentClassList);
			
			cell = new Listcell(tclass.getVTclassCode());
			cell.setParent(item);
			
			cell = new Listcell(tclass.getVTclassDesc());
			cell.setParent(item);
		}
	}

	
}

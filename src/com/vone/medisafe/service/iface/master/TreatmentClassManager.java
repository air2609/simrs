package com.vone.medisafe.service.iface.master;



import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsTreatmentClass;

public interface TreatmentClassManager {

	public void save(MsTreatmentClass treatmentClass) throws VONEAppException;
	
	public void delete(MsTreatmentClass treatmentClass) throws VONEAppException;
	
	public MsTreatmentClass getById(Integer id) throws VONEAppException;
	
	public MsTreatmentClass getTClassByCode(String code) throws VONEAppException;
	
	public boolean deleteById(Integer id) throws VONEAppException;
	
	public void getTClassForSelect(Listbox tclassList) throws VONEAppException;

	public void getTreatmentClassData(Listbox treatmentClassList) throws VONEAppException;
}

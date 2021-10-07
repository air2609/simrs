package com.vone.medisafe.service.iface.master;



import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatientType;

public interface PatientTypeManager {
	
	public void save(MsPatientType patientType);
	
	public MsPatientType getPatienType(MsPatientType patientType);
	
	public MsPatientType getById(Integer id);
	
	public void delete(MsPatientType pType);
	
	public MsPatientType getByCode(String code);
	
	public boolean deleteById(Integer id);
	
	public void getPatientTypeForSelect(Listbox tipePasienList) throws VONEAppException;

	public void getAllPatientTypeData(Listbox prov) throws VONEAppException;
}

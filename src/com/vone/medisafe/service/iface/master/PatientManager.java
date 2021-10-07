package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;




import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbMedicalRecord;

public interface PatientManager {
	
	public boolean save(MsPatient patient, TbMedicalRecord mr);
	
	public List searchPatient(String code, String nameP, String addressP);
	
		
	public void cariPasienYgPunyaMr(Textbox crNoMR, Textbox crNama, Datebox crTgl, Textbox crAlamat, Listbox patientSearchList)
	 throws VONEAppException,InterruptedException;

	public void serachRegisteredPatient(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList, Listbox unitList)
		throws VONEAppException, InterruptedException;
	
	public void serachRegisteredPatient(Textbox crNoMR, Textbox crNama, Datebox crTgl, Textbox crAlamat, Listbox patientSearchList)
	throws VONEAppException, InterruptedException;

	public void searchRanapPatient(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList)
	 throws VONEAppException,InterruptedException;

	public void getPatientDetil(Window win, int type) throws VONEAppException, InterruptedException;

	public List searchPatient(String noMr, String name, String alamat, String ruangan, String doctor) throws VONEAppException;

	public void getPatientBaseOnWard(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList,
			Listbox bangsalList)
	throws VONEAppException,InterruptedException;
}
	

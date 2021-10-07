package com.vone.medisafe.service.iface.admission;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbIcd9Diagnose;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbIllness;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbOperation;
import com.vone.medisafe.mapping.TbPregnancy;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbStillbirth;
import com.vone.medisafe.mapping.TbViolent;
import com.vone.medisafe.ui.mr.MRViewController;

public interface MedicalRecordManager {
	
	public void save(TbMedicalRecord mr) throws VONEAppException;
	
	public TbMedicalRecord getMrById(Integer id) throws VONEAppException;
	
	public TbMedicalRecord getMedicalRecord(String mrCode) throws VONEAppException;
	
	public TbMedicalRecord getByRegistration(TbRegistration reg) throws VONEAppException;
	
	public TbMedicalRecord getMrByPatientId(Integer id) throws VONEAppException;
	
	public List getMROut() throws VONEAppException;
	
	public TbMedicalRecord getMrRegistered(String mrCode) throws VONEAppException;
	
	public List<TbMedicalRecord> getMrByStatus(String mrStatus) throws VONEAppException;

	public Integer countVisit(TbMedicalRecord mr) throws VONEAppException;

	public boolean saveDiagnose(TbDiagnose diagnose, List<TbIcdDiagnose> icdList, List<TbIcd9Diagnose> icd9List, 
			List<TbIllness> illness, List<TbOperation> operation, List<TbViolent> rudapaksa, 
			List<TbPregnancy> kehamilan, List<TbStillbirth> keguguran, boolean isUpdate) throws VONEAppException;

	public void viewMrStatus(Listbox mrFileStatueList, Listbox mrFileList) throws VONEAppException;

	public void getMrStatus(MRViewController controller, int type) throws VONEAppException, InterruptedException;

	public void getMROut(Listbox medicalRecordFile) throws VONEAppException;

	public void updateStatus(Component win, String status) throws VONEAppException, InterruptedException;
}

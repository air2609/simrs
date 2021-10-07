package com.vone.medisafe.service.iface;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.ui.common.PatientHistoryController;

public interface CommonPatientHistoryManager {
	
	public List getPatientNote(MsPatient patient, MsUnit unit) throws VONEAppException;
	
	public List getTreatmentBaseOnNote(TbExamination note) throws VONEAppException;
	
	public List getItemBaseOnNote(TbExamination note) throws VONEAppException;

	public void getTreatmentData(PatientHistoryController controller) throws VONEAppException, 
		InterruptedException;

	public void getOption(PatientHistoryController controller) throws VONEAppException,
		InterruptedException;
}

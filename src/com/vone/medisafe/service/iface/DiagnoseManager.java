package com.vone.medisafe.service.iface;

import java.util.List;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.ui.mr.MedicalRecordDiagnose;

public interface DiagnoseManager {

	public void getMrDetail(Bandbox mrNo, Listbox patientList, Textbox age, Textbox patientName, Radiogroup gender, 
			Textbox patientType, Textbox dateOfBirth, Intbox visitTime, int type) throws VONEAppException, InterruptedException;

	public void test(Textbox crNoMR, Textbox crNama, Textbox crNoAlamat, Listbox patientList) throws VONEAppException;

	public void testLagi(String string, String string2, String string3);

	public void getRegistrationDetail(MedicalRecordDiagnose diagnose, int type) throws VONEAppException;

	public void saveDiagnoseAndReceipt(MsPatient patient, TbDiagnose diagnose, TbIcdDiagnose icd, TbExamination nota, List<TbItemTrx> itemTrx, 
			List<TbDrugIngredients> racikans, Integer warehouseId) throws VONEAppException;

	public void getDiagnoseHistory(TbMedicalRecord mr, Listbox diagoseList) throws VONEAppException;

}

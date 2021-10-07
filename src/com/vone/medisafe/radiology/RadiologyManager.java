package com.vone.medisafe.radiology;

import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.cafediet.CafeDietController;
import com.vone.medisafe.cafediet.KlinikGiziController;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.fisioterapi.FisioterapiController;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.renal.RenalUnitController;
import com.vone.medisafe.renal.TambahTindakanController;
import com.vone.medisafe.surgery.SurgeryController;

public interface RadiologyManager {
	public List getMedician(String sMedicCategory) throws VONEAppException;
	public boolean save(MsPatient patient, TbExamination nota, Set treatmentTrx, Set itemTrx,
			MsUnit unit, boolean isRanap)throws VONEAppException;
	public MsPatient getMspatient(TbExamination nota) throws VONEAppException;
	public void cariNotaClick(RadiologyController controller) throws VONEAppException;
	public Object[] getMsStaffInUnits(MsUser user) throws VONEAppException;
	public TbRegistration getRegistrationByMrId(Integer mrId) throws VONEAppException;
	
	public void getMsStaffInUnits(MsUser user, Listbox locationList) throws VONEAppException;

	public void getRegistration(RadiologyController controller, int type) throws VONEAppException;
	public void getRegistrationSurgery(SurgeryController controller, int type) throws VONEAppException;
	public void getRegistrationRenal(RenalUnitController controller, int type) throws VONEAppException;

	public void getNoteDetail(RadiologyController controller) throws VONEAppException;
	public void getNoteDetailSurgery(SurgeryController controller) throws VONEAppException;

	public void searchTreatmentSurgery(com.vone.medisafe.surgery.TambahTindakanController controller) throws VONEAppException;
	public void searchTreatment(com.vone.medisafe.radiology.TambahTindakanController controller) throws VONEAppException;
	public void getNoteDetail(RenalUnitController controller) throws VONEAppException;
	public void searchTreatmentRenal(TambahTindakanController controller) throws VONEAppException;
	public void getRegistrationCafe(CafeDietController controller, int type) throws VONEAppException;
	public void getNoteDetailCafe(CafeDietController controller) throws VONEAppException;
	public void getRegistrationFisio(FisioterapiController controller, int type) throws VONEAppException;
	public void getNoteDetailFisio(FisioterapiController controller) throws VONEAppException;
	public void getRegistrationKlinik(KlinikGiziController controller, int type) throws VONEAppException;
	public void getNoteDetailKlinik(KlinikGiziController controller) throws VONEAppException;
	public void ambilDetailNota(TbExamination nota, Listbox theList) throws VONEAppException;
	public void getKelasForSelect(Listbox tclassList) throws VONEAppException;
}

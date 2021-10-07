package com.vone.medisafe.laborat;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbLaboratoryResult;
import com.vone.medisafe.mapping.TbLaboratoryResultDetail;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;

public interface LaboratManager {
	
	public List getTreatmentTrx(TbExamination note) throws VONEAppException;
	public List getTreatment(MsPatient pat)throws VONEAppException ;
	public List getTreatmentResult(MsPatient pat) throws VONEAppException ;
	public MsTreatment getTreatmentByLrd(TbLaboratoryResultDetail lrd) throws VONEAppException ;
	
	public List serachRegisteredPatient(String code, String nameP,String addressP)throws VONEAppException ;
	public MsLabTestDetail getLabTestDetail(int code)throws VONEAppException ;
	public TbExamination getExaminationByMr(TbMedicalRecord mr)throws VONEAppException ;
	public boolean saveHasilLab(MsPatient patient, TbLaboratoryResult nota, TbExamination exam, Set resultDetailSet,  
			MsUnit unit, boolean isRanap)throws VONEAppException ;
	public TbExamination getTbExaminationById(int id)throws VONEAppException ;
	public List searchHasilLab(String code, String name)throws VONEAppException ;
	public List getTbLabResultDetail(int code)throws VONEAppException ;

	public MsPatient getFreeBuyer()throws VONEAppException ;

	public boolean saveModifyHasilLab(MsPatient patient, TbLaboratoryResult labResult, TbExamination exam, Set<TbLaboratoryResultDetail> labResultDetailSet)throws VONEAppException;
	public void getRegistration(LaboratController controller, int type) throws VONEAppException;
	public void getNoteDetail(LaboratController controller) throws VONEAppException;
	public void searchNote(LaboratController controller) throws VONEAppException;
	public void getItemsFromList(PanelLabController controller, String list) throws VONEAppException ;
	public void fillTreatment(KimiaController controller, String code, String kelasTarif) throws VONEAppException;
	public void getSelectedTreatment(TambahPemeriksaanController controller) throws VONEAppException;
	public void searchTreatment(TambahPemeriksaanController controller) throws VONEAppException;
	public Integer getWhoustId(Listbox locationList) throws VONEAppException ;
	public void getLabDetail(HasilLaboratController controller) throws VONEAppException;
	public void getDetailLabTest(HasilLaboratController controller, TbExamination exam) throws VONEAppException;
	public void getRegistration(HasilLaboratController controller, int type) throws VONEAppException;
	public void searchNote(HasilLaboratController controller) throws VONEAppException;
	public void searchRegisteredPatient(HasilLaboratController controller, Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList) throws VONEAppException;
	public void searchHasilLab(HasilLaboratController controller) throws VONEAppException;
	public void getLabResult(HasilLaboratController controller) throws VONEAppException;
	
	public void ambilDetailNota(TbExamination nota, Listbox theList) throws VONEAppException;
	public void CreateReport(TbExamination exam, StringBuffer sb) throws VONEAppException;
	public void CreateReport(TbReturPharmacyTrx tbReturPharmacyTrx, StringBuffer sb) throws VONEAppException;
	public void CreateReport(Collection notas, String noKwitansi, Date tgl, double total, double jumlahDiskon, double persentasiPajak, double nilaiPajak, double totalAkhir, double bayarTunai, double bayarKredit, double bayarDeposit, double retur, StringBuffer sb) throws VONEAppException;
	public void getListboxDetail(Listbox list, MsTreatmentGroup msTreatmentGroup) throws VONEAppException;
	public MsLabTreatmentDetil getMsLabTreatmentDetilById(Integer labDetilId) throws VONEAppException;
	
	public void CreateCopyResep(TbExamination exam, StringBuffer sb) throws VONEAppException;
}

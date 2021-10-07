package com.vone.medisafe.service.iface;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;

public interface NoteManager {
	
	public boolean save(TbExamination exam) throws VONEAppException;
	
	public void searchNote(String noteNumber, String patientName, MsUnit unit,
			Listbox notaList, int statusNota) throws VONEAppException ;
	
	public List searchNoteLab(String noteNumber, String patientName, MsUnit unit)throws VONEAppException;
	
		
	public TbExamination getNoteById(Integer id) throws VONEAppException;
	
	public boolean saveModifyNote(TbExamination note, Set<TbItemTrx> itemTrxs, Set<TbTreatmentTrx> treatTrxs,
			Set<TbBundledTrx> bundleTrxs, Set<TbMiscTrx> miscTrxs, Set<TbDrugIngredients> racikans, 
			Integer warehouseId) throws VONEAppException;
	
	public List<TbExamination> getNotesByRegistration(TbRegistration reg) throws VONEAppException;

	public void saveTrx(Listbox laboratoryList, MsUser user, Set<TbTreatmentTrx> treatmentSet, Set<TbItemTrx> 
		itemSet, Set<TbMiscTrx> miscSet) throws VONEAppException;

//	public boolean saveModifyNote(TbExamination nota, Set<TbItemTrx> itemSet, Set<TbTreatmentTrx> treatmentSet, 
//			Set<TbBundledTrx> bundledSet, Set<TbMiscTrx> miscSet, Object object, Listbox locationList) 
//			throws VONEAppException;

	public void cancelNote(String alasanBatal) throws VONEAppException, InterruptedException;

	public void getNoteDetil(TbExamination nota, Listbox emergencyList) throws VONEAppException;

	public MsUnit getUnitNote(TbExamination exam) throws VONEAppException;
	
	public void searchNote(Textbox nomorNota, Textbox namaPasien, Datebox tglMulai, Datebox tglAkhir, 
			Listbox hasilList, Listbox locationList) throws VONEAppException;
	
	
	public void searchNote(Textbox nomorNota, Textbox namaPasien, Textbox noMr, Textbox nomorResep, 
		Datebox tglMulai, Datebox tglAkhir, Listbox hasilList, Listbox locationList) throws VONEAppException;

	public void getPendapatanDokter(MsStaff staff, String tipeLaporan, Date value, Date value2, Listbox pendapatanDokterList, 
			Decimalbox db, String patientType);
	
	
	public void getNoteByRangeDate(Datebox date, Datebox date2, Listbox notalist, Intbox jumlahR, Intbox nilaiR) throws VONEAppException;

	public void getRawatInapJalan(Date date1, Date date2, String tipeLaporan, Listbox resultList, Decimalbox dBox) throws VONEAppException;
	
	public void getRekapObat(Date dateFrom, Date dateTo, Listbox resultList, Decimalbox dBox) throws VONEAppException;

	public void getDoctorReportAll(Listbox doctorList, Date from, Date to,
			Listbox laporan, Decimalbox totalPendapatan, String patientType) throws VONEAppException;
}

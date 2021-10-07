package com.vone.medisafe.cashier;

import java.util.Date;
import java.util.List;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.mapping.TbPatientDeposit;
import com.vone.medisafe.mapping.TbPatientSettlement;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.TbKwitansi;
import com.vone.medisafe.mapping.MsCoa;

public interface CashierManager{
	
	
	public List<TbExamination> getNotes(MsPatient patient) throws VONEAppException;
	
	
	public List<TbItemTrx> getItemsTrx(List<TbExamination> nota) throws VONEAppException;
	
	public List<TbDrugIngredients> getRacikanTrx(List<TbExamination> nota) throws VONEAppException;
	
	public List<TbBundledTrx> getBundlesTrx(List<TbExamination> nota) throws VONEAppException;
	
	public List<TbTreatmentTrx> getTreatmentTrx(List<TbExamination> nota) throws VONEAppException;
	
	public List<TbBedTrx> getBedTrx(List<TbExamination> nota) throws VONEAppException;
	
	
	
		
	public List<TbReturPharmacyDetailTrx> getRetursDetail(List<TbReturPharmacyTrx> returs) throws VONEAppException;
	
	public boolean saveBillRetur(TbPatientBill pbill, TbReturPharmacyTrx retur) throws VONEAppException;
	
	public boolean saveBillNote(TbPatientBill pbill, List<TbExamination> notas, List<TbPatientSettlement> 
			settlements, Decimalbox depositPaid, MsCoa coaKas) throws VONEAppException;  
	
	public boolean saveDeposit(TbPatientDeposit deposit, TbPatientBill pbill, MsCoa coaKas, 
				List<TbPatientSettlement> detils) throws VONEAppException;
	
	public TbPatientDeposit getPatientBalance(TbRegistration reg) throws VONEAppException;
	
	public List<TbPatientBill> getPatientBills(String nameOnBill, String billCode, short paymentStatus,
			boolean isRetur) throws VONEAppException;
	
		
	public boolean lockRegistration(TbRegistration reg) throws VONEAppException;
	
	public boolean cancelBill(TbPatientBill bill) throws VONEAppException;

	public void cekCoa(CashierTransactionController controller) throws VONEAppException, InterruptedException;

	public void searchNote(CashierTransactionController controller)throws VONEAppException, InterruptedException;

	public void getNoteDetil(CashierTransactionController controller) throws VONEAppException, InterruptedException;

	public void getRegistration(CashierTransactionController controller, int type) throws 
		VONEAppException, InterruptedException;


	public void getBillDetil(CashierTransactionController controller) throws VONEAppException;


	public void getReturRegistration(ReturKwitansiController controller, int type) throws VONEAppException,
		InterruptedException;


	public void testPersen() throws VONEAppException;


	public void getBillNote(ReturKwitansiController controller) throws VONEAppException;

	/****************************************************************************
	 * 
	 * manager utk PrintController
	 * 
	 ****************************************************************************/
	public void btnAllClick(PrintController controller) throws VONEAppException;
	public void btnUnitClick(PrintController controller) throws VONEAppException;
	public void btnItemsClick(PrintController controller) throws VONEAppException;
	public void getRegistration(PrintController controller, int input_type) throws VONEAppException;
	public void getRegistration(RekapObatController controller, int input_type) throws VONEAppException;
	public void cariNotaClick(PrintController controller) throws VONEAppException;


	public List<TbExamination> getNota(TbRegistration reg, int active_note) throws VONEAppException;
	
	public List<TbExamination> getUnbillNote(TbRegistration reg) throws VONEAppException;


	public void getPatientSettlement(Datebox transDate, Listbox shiftList, Listbox kwitansiList, Decimalbox tunai, Decimalbox nonTunai);


	public void getRekapKasir(Listbox pendapatanList, Listbox tipeList, Datebox dateFrom, Datebox dateTo, Listbox rekapList,
			Decimalbox tunai, Decimalbox nontunai,Decimalbox card, String path); 


	public void getRekapObat(RekapObatController rekapObatController) throws VONEAppException;
	
	public String generateKwitansi(String noKwitansi, String path) throws Exception;


	public List<TbKwitansi> getAllKwitansi() throws VONEAppException;


	public TbKwitansi getKwitansi(Date value, String tipe) throws VONEAppException;


	public boolean saveKwitansi(TbKwitansi kwitansi) throws VONEAppException;;
	
}

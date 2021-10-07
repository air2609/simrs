package com.vone.medisafe.apotik;

import java.util.List;
import java.util.Set;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;


import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;


/**
 * ApotikManager.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 15 2006
 * @category business logic (M)
 */

public interface ApotikManager {
	
	public boolean save(MsPatient patient, TbExamination note, Set<TbItemTrx> itemTrx, 
			Set<TbDrugIngredients> racikan, MsUnit unit, boolean isRanap, Set<TbMiscTrx> miscs) throws VONEAppException;
	

	
	public List<TbDrugIngredients> getRacikans(TbExamination nota) throws VONEAppException;
	
	
	public List<TbDrugIngredientsDetail> getDetilRacikan(TbDrugIngredients racikan) throws VONEAppException;
	
	public boolean saveItemRetur(TbReturPharmacyTrx retur, Set<TbReturPharmacyDetailTrx> detilRetur,
			Set<TbPatientInventory> patInvs, Set<TbItemTrx> itemTrx, MsUnit unit) throws VONEAppException;
	
	public List<TbExamination> getNoteBaseOnReceiptNumber(String receiptNumber) throws VONEAppException;
	
	public void searchReturItems(String noteNumber, String patientName, Listbox list, Datebox startDate, Datebox endDate) 
		throws VONEAppException;
	
	
	
	public boolean cancelReturnTrx(TbReturPharmacyTrx retur, List<TbReturPharmacyDetailTrx> detil,
			MsUnit unit) throws VONEAppException;
	
	public boolean validateReturNote(TbReturPharmacyTrx retur, MsUnit unit) throws VONEAppException;
	
	public List getRacikanItems(Integer wid, String kelasTarif) throws VONEAppException;
	
	public List searchRacikanItems(Integer wid, String tclass, String itemCode, String itemName) 
		throws VONEAppException;
	
	public TbReturPharmacyTrx getTbReturPharmacyTrx(Integer id) throws VONEAppException; 
	
	public boolean saveModify(TbExamination nota, Set<TbItemTrx> itemTrxs, Set<TbDrugIngredients> 
		racikans, Integer warehouseId, Set<TbMiscTrx> miscs) throws VONEAppException;
	
	
	public boolean saveReturModify(TbReturPharmacyTrx retur, Set<TbReturPharmacyDetailTrx> detils, 
			Integer warehouseId) throws VONEAppException;

	public void getRegistration(ApotikController controller, int type) 
		throws VONEAppException, InterruptedException;

	public void getReturNoteDetil(ReturObatController controller, int type) throws VONEAppException;


	public void getPatientItems(ReturObatController controller, int type) 
		throws VONEAppException, InterruptedException;



	public void getReturDetil(ReturObatController controller, Integer integer) throws VONEAppException;



	public List<TbReturPharmacyDetailTrx> getReturDetil(TbReturPharmacyTrx retur) throws VONEAppException;



	public void getNoteDetil(ApotikController controller) throws VONEAppException;



	public void saveReturPatientInventory(ReturObatController controller) throws VONEAppException, InterruptedException;



	public void getValidatedNoteForAntrian(Listbox listPasien1, Listbox listPasien2) throws VONEAppException;



	public void takeOutAntrianApotik(Listbox myList, Listitem myItem);



	public void moveToAntiranApotik(Listbox validList, Listbox jadiList,
			Listitem item);



	public void getTextAntrian(Button btnSave, Button btnEdit, Textbox textAntrian) throws VONEAppException;



	public void saveAntrian(Button btnSave, Button btnEdit, Textbox textAntrian) throws VONEAppException;



	public void getApotikAntrian(Listbox listPasien) throws VONEAppException;
	
}

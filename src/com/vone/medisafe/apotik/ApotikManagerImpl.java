package com.vone.medisafe.apotik;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.antrian.MsAntrian;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ward.PatientInventoryDAO;

/**
 * ApotikManagerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 15 2006
 * @category service (logic model - M)
 */

public class ApotikManagerImpl implements ApotikManager{
	
	private ApotikDAO dao;
	private TbMedicalRecordDAO mrDao;
	private TbRegistrationDAO regDao;
	private TbBedOccupancyDAO bocDao;
	private NoteDAO noteDao;
	private PatientInventoryDAO patinvDao;
	

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}

	public PatientInventoryDAO getPatinvDao() {
		return patinvDao;
	}

	public void setPatinvDao(PatientInventoryDAO patinvDao) {
		this.patinvDao = patinvDao;
	}

	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}

	public TbRegistrationDAO getRegDao() {
		return regDao;
	}

	public void setRegDao(TbRegistrationDAO regDao) {
		this.regDao = regDao;
	}

	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	public ApotikDAO getDao() {
		return dao;
	}

	public void setDao(ApotikDAO dao) {
		this.dao = dao;
	}

	public boolean save(MsPatient patient, TbExamination note, Set<TbItemTrx> itemTrx, 
			Set<TbDrugIngredients> racikan,MsUnit unit, boolean isRanap,
			Set<TbMiscTrx> miscs) throws VONEAppException {
		
		return dao.save(patient, note, itemTrx, racikan, unit, isRanap,miscs);
	}

	

	public List<TbDrugIngredients> getRacikans(TbExamination nota) throws VONEAppException {

		List<TbDrugIngredients> racikans = new ArrayList<TbDrugIngredients>();
		TbDrugIngredients drug = null;
		
		List result = dao.getRacikanTrx(nota);
		Iterator it = result.iterator();
		
		while(it.hasNext()){
			drug = (TbDrugIngredients)it.next();
			racikans.add(drug);
		}
		
		return racikans;
	}

	
	public List<TbDrugIngredientsDetail> getDetilRacikan(TbDrugIngredients racikan) throws VONEAppException {
		// TODO Auto-generated method stub
		List<TbDrugIngredientsDetail> hasil = new ArrayList<TbDrugIngredientsDetail>();
		List result = dao.getDetilRacikan(racikan);
		Iterator it = result.iterator();
		while(it.hasNext()){
			hasil.add((TbDrugIngredientsDetail)it.next());
		}
		return hasil;
	}

	public boolean saveItemRetur(TbReturPharmacyTrx retur, Set<TbReturPharmacyDetailTrx> detilRetur, 
			Set<TbPatientInventory> patInvs, Set<TbItemTrx> itemTrx, MsUnit unit) throws VONEAppException {
		
		return dao.saveItemRetur(retur, detilRetur,patInvs, itemTrx, unit);
	}

	

	public List<TbExamination> getNoteBaseOnReceiptNumber(String receiptNumber) throws VONEAppException {
		List<TbExamination> hasil = new ArrayList<TbExamination>();
		TbExamination exam = null;
		List result = dao.getExaminationBaseOnReceipt(receiptNumber);
		Iterator it = result.iterator();
		
		while(it.hasNext()){
			exam = (TbExamination)it.next();
			hasil.add(exam);
		}
		
		return hasil;
	}
	

	public void searchReturItems(String noteNumber, String patientName, Listbox list, Datebox tglMulai, 
			Datebox tglAkhir) 
	throws VONEAppException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		if(tglMulai.getValue() == null){
			
			tglMulai.setValue(new Date());
		}
		
		if(tglAkhir.getValue() == null){
			
			tglAkhir.setValue(tglMulai.getValue());
		}
		
		String tanggalMulai = tglMulai.getText() +" "+"00:00:00";
		String tanggalAkhir = tglAkhir.getText()+" "+"23:59:59";
		
	
//		String unitCode = ((MsUnit)locationList.getSelectedItem().getValue()).getVUnitCode();
		List<TbReturPharmacyTrx> result = null;
		
		try {
			 Date tgl1 = sdf.parse(tanggalMulai);
			 Date tgl2 = sdf.parse(tanggalAkhir);
			 result = dao.searchReturItems("%"+noteNumber+"%","%"+patientName+"%",
					tgl1,tgl2);
			 
			 
		} catch (WrongValueException e) {
			
		} catch (ParseException e) {
			
		}
		
//		List<> result = dao.searchReturItems(noteNumber, patientName, startDate, endDate);
		
		list.getItems().clear();
		Listitem item;
		Listcell cell;
		
		for(TbReturPharmacyTrx retur : result){
			item = new Listitem();
			item.setParent(list);
			item.setValue(retur);
			
			cell = new Listcell(retur.getVReturCode());
			cell.setParent(item);
			
			cell = new Listcell(retur.getMsPatient().getVPatientName());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.getNoteStatus(retur.getNStatus()));
			cell.setParent(item);
		}
		
	}

	

	public boolean cancelReturnTrx(TbReturPharmacyTrx retur, List<TbReturPharmacyDetailTrx> detil, MsUnit unit) throws VONEAppException {
		
		return dao.cancelRetur(retur, detil, unit);
	}

	
	public boolean validateReturNote(TbReturPharmacyTrx retur, MsUnit unit) throws VONEAppException {
		
		return dao.validateRetur(retur,unit);
	}

	
	public List getRacikanItems(Integer wid, String kelasTarif) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getRacikanItems(wid, kelasTarif);
	}

	public List searchRacikanItems(Integer wid, String tclass, String itemCode, String itemName) throws VONEAppException {
		
		return dao.searchRacikanItems(wid, tclass, itemCode, itemName);
	}

	public TbReturPharmacyTrx getTbReturPharmacyTrx(Integer id) throws VONEAppException{
		
		return dao.getTbReturPharmacyTrx(id);
	}

	/**
	
	public boolean saveModify(TbExamination nota, Set<TbItemTrx> itemTrxs, Set<TbDrugIngredients> racikans, 
			Integer warehouseId, Set<TbMiscTrx> miscs) throws VONEAppException {
		
//		note
		return dao.saveModify(nota, itemTrxs, racikans, warehouseId);
	}
	*/
	
	public boolean saveReturModify(TbReturPharmacyTrx retur, Set<TbReturPharmacyDetailTrx> detils, Integer warehouseId) 
		throws VONEAppException {
		
		return dao.saveReturModify(retur, detils, warehouseId);
	}

	
	
	
	public void getRegistration(ApotikController controller, int type) throws VONEAppException,
		InterruptedException{
		
		Listitem item;
		
		TbMedicalRecord mr = null;
		
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			mr = mrDao.getMrRegistered(code);
									
			if(mr == null){
				
				Messagebox.show(MessagesService.getKey("mr.not.found"));
				controller.MRNumber.setDisabled(false);
				controller.MRNumber.focus();
				
				return;
			}
			
			controller.patient = mr.getMsPatient();
		}
		else{
			item = controller.patientList.getSelectedItem();
			mr = (TbMedicalRecord)item.getValue();
			controller.patient = mr.getMsPatient();
		}
		
		controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId()); 
			
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.dateOfBirth.setValue(controller.patient.getDPatientDob());
		controller.address.setValue(mr.getMsPatient().getVPatientMainAddr());
		
		int[] umurSkrg = MedisafeUtil.calculateAge(mr.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(umurSkrg));
		
		if(mr.getMsPatient().getVPatientGender().equals("M")){
			
			controller.gender.setSelectedIndex(0);
		}
		else{
			
			controller.gender.setSelectedIndex(1);
		}
		
		
		if(mr.getMsPatient().getMsPatientType() != null){
			for(int i=1; i < controller.patientType.getItems().size(); i++){
				if(((MsPatientType)controller.patientType.getItemAtIndex(i).getValue()).getNPatientTypeId().
						equals(mr.getMsPatient().getMsPatientType().getNPatientTypeId()))
//					
					controller.patientType.setSelectedIndex(i);
			}
		}
		
		
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
		controller.referencePatient.setDisabled(true);
		if(controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")){
			
			controller.isRanap = true;
			TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.reg.getNRegId()); 
				
			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc();
			
		}
		else{
			controller.isRanap = false;
		}
		
		controller.patient = mr.getMsPatient();
		controller.setFieldDisable(true);
		controller.receiptNumber.focus();
		
		Integer unitId = ((MsUnit)controller.locationList.getSelectedItem().getValue()).getNUnitId();
		
		TbExamination note = noteDao.getReceiptFromDoctor(controller.reg.getNRegId(), unitId);
		if(note != null){
			controller.transactionNumber.setValue(note.getVNoteNo());
			controller.transactionNumber.setAttribute("nota", note);
			
			this.getItemsDetail(controller, note);
		}else{
			controller.statusNota.setVisible(false);
			controller.labelStatus.setVisible(false);
		}
		
		Sessions.getCurrent().setAttribute("mr", mr);
		
	}

	
	public void getReturNoteDetil(ReturObatController controller, int type) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		TbExamination note = null;
		
		if(type == MedisafeConstants.NOTA){
			note = (TbExamination)controller.notaList.getSelectedItem().getValue();
		}
		else note = (TbExamination)controller.recieptList.getSelectedItem().getValue(); 
		
		note = noteDao.getNote(note.getNExamId());
				
		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);
		controller.patient = note.getMsPatient();
		
		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(note.getMsPatient().getNPatientId());
		
		controller.patientName.setValue(controller.patient.getVPatientName());
		controller.address.setValue(controller.patient.getVPatientMainAddr());
		
		if(!note.getVNoteNo().trim().equals(""))
			controller.receiptNumber.setValue(note.getVRecipeNo());
			
				
		if(mr != null) 
		{
			controller.MRNumber.setValue(mr.getVMrCode());
//			MRNumber.setAttribute("patient", mr.getMsPatient());
		}
		if(note.getTbRegistration() != null)
			controller.registrationNumber.setValue(note.getTbRegistration().getVRegSecondaryId());
		
				
		
		//AMBIL DETIL NOTA
		controller.pharmacyList.getItems().clear();
		double hargaSatuan = 0;
		
	
		List<TbItemTrx> obats = dao.getItemTrxReturnable(note); 
						
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		for(TbItemTrx itmTrx : obats){
			
			item = new Listitem();
			item.setValue(itmTrx);
			item.setParent(controller.pharmacyList);
			
			//item code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(itmTrx.getMsItem().getVItemCode());
			
			//item name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(itmTrx.getMsItem().getVItemName());
			
			//item tipe
			cell = new Listcell(MedisafeUtil.convertIntoDrugsType(itmTrx.getMsItem().
					getNItemType().shortValue()));
			cell.setParent(item);
			
			
			//satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(itmTrx.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
			
			//jumlah 
			db.setValue(new BigDecimal(itmTrx.getNQty()));
			Listcell cell2 = new Listcell();
			cell2.setParent(item);
			cell2.setLabel(db.getText());
			
			hargaSatuan = itmTrx.getNAmountAfterDisc()/itmTrx.getNQty();
			db.setValue(new BigDecimal(hargaSatuan));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			

			//item price before discount
			cell = new Listcell();
			Intbox jumlahRetur = new Intbox();
			jumlahRetur.setWidth("90%");
			jumlahRetur.setStyle("font-size:8pt");
			jumlahRetur.setParent(cell);
			cell.setParent(item);
				
			cell = new Listcell();
			jumlahRetur.addEventListener("onChange", new ReturListener(jumlahRetur, 
								controller.total,hargaSatuan,cell, cell2, controller.pharmacyList));
			cell.setParent(item);
					
		}

		
	}
	
	

	public void getPatientItems(ReturObatController controller, int type) throws VONEAppException,
		InterruptedException{
		
		Listitem item;
		Listcell cell;
		
		TbMedicalRecord mr = null;
		TbRegistration reg = null;
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###");
		
	
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getValue());
			mr = mrDao.getMrRegistered(code);
				
			if(mr == null){
				Messagebox.show(MessagesService.getKey("admission.mr.notfound"));
				controller.MRNumber.focus();
				return;
			}
		}
		else{
			mr = (TbMedicalRecord)controller.patientList.getSelectedItem().getValue();
			mr = mrDao.getMrRegistered(mr.getVMrCode());
		}
		
		
		
		reg = regDao.getLastRegistrationByMrId(mr.getNMrId()); 
		
	
		
		if(reg != null)
			controller.patient = reg.getTbMedicalRecord().getMsPatient();
			
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.address.setValue(mr.getMsPatient().getVPatientMainAddr());
		if(reg != null) controller.registrationNumber.setValue(reg.getVRegSecondaryId());
		
		List msItems = this.dao.getReturnableItems(reg);
		
		
		int jumlahTotal=0;
		int terpakai=0;
		int jumlahInv = 0;
		double hargaSatuan = 0;
		
		Iterator it = msItems.iterator();
		
		while(it.hasNext()){
			Object[] obj = (Object[])it.next();
			item = new Listitem();
			item.setValue(obj);
			item.setParent(controller.pharmacyList);
			
			//item code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(obj[1].toString());
			
			//item name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(obj[2].toString());
			
			//item tipe
			if(obj[3] == null){
				cell = new Listcell(" - ");
			}
			else
				cell = new Listcell(MedisafeUtil.convertIntoDrugsType((Short)obj[3]));
			cell.setParent(item);
			
			
			//satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(obj[4].toString());
			

			jumlahTotal = (Integer)obj[5];
			terpakai = (Integer)obj[6];
			db.setValue(new BigDecimal((jumlahTotal-terpakai)));
			Listcell cell2 = new Listcell();
//			cell.setAttribute("terpakai", new Integer(terpakai));
			cell2.setParent(item);
			cell2.setLabel(db.getText());
			
			MsItem msItem = new MsItem();
			msItem.setNItemId((Integer)obj[0]);
			
			List<TbPatientInventory> itemInv = patinvDao.getPatientInventory(msItem, reg); 
				
			for(TbPatientInventory inv : itemInv){
				jumlahInv = jumlahInv + inv.getNQty();
				/*
				if(jumlahInv >= terpakai){
					//ambil item trx utk inventory ini
					if(inv.getTbItemTrx() != null)
						if(inv.getTbItemTrx().getNQty() > 0)
							hargaSatuan = inv.getTbItemTrx().getNAmountAfterDisc()/inv.getTbItemTrx().getNQty();
				} */
			}
			
			hargaSatuan = this.dao.getHargaSatuan(msItem,reg);
			
			db.setValue(new BigDecimal(hargaSatuan));
			cell = new Listcell(db.getText());
			cell.setParent(item);

			//item price before discount
			cell = new Listcell();
			Intbox jumlahRetur = new Intbox();
			jumlahRetur.setWidth("90%");
			jumlahRetur.setParent(cell);
			jumlahRetur.setStyle("font-size:8pt");
			cell.setParent(item);
			
			cell = new Listcell();
			jumlahRetur.addEventListener("onChange", new ReturListener(jumlahRetur, controller.total,
					hargaSatuan,cell, cell2, controller.pharmacyList));
			cell.setParent(item);
			item.setAttribute("hargaSatuan", new Double(hargaSatuan));
			
			jumlahTotal = 0;
			terpakai = 0;
		}

		
	}
	
	

	public void getReturDetil(ReturObatController controller, Integer returId) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		double hargaSatuan = 0;
		
		TbReturPharmacyTrx retur = null;
//			(TbReturPharmacyTrx)controller.returnList.getSelectedItem().getValue();
		retur = this.dao.getTbReturPharmacyTrx(returId);
	
		controller.labelStatus.setVisible(true);
		controller.statusNota.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(retur.getNStatus()));
		
		controller.apotikReturn = retur;
		controller.returnNoteNumber.setValue(retur.getVReturCode());
		controller.returnNoteNumber.setAttribute("retur", retur);
		controller.address.setValue(retur.getMsPatient().getVPatientMainAddr());
		controller.patientName.setValue(retur.getMsPatient().getVPatientName());
		
		if(retur.getTbExamination() != null){
			
			controller.receiptNumber.setValue(retur.getTbExamination().getVRecipeNo());
			controller.transactionNumber.setValue(retur.getTbExamination().getVNoteNo());
		}
		
		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(retur.getMsPatient().getNPatientId());
		
		if(mr != null) controller.MRNumber.setValue(mr.getVMrCode());
		
//		this.dao.save(retur);
		
		
		Set<TbReturPharmacyDetailTrx> detils = retur.getTbReturPharmacyDetailTrxes();
//		List<TbReturPharmacyDetailTrx> detils = dao.getReturDetil(retur.getNReturId()); 
			
		controller.pharmacyList.getItems().clear();
		
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
	
		
		for(TbReturPharmacyDetailTrx detil : detils){
			
			if(detil.getNQty().intValue() == 0)continue;
			
			item = new Listitem();
			item.setParent(controller.pharmacyList);
			item.setValue(detil);
			
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(detil.getMsItem().getVItemCode());
			
			//item name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(detil.getMsItem().getVItemName());
			
			//item tipe
			
			if(detil.getMsItem().getNItemType() == null){
				cell = new Listcell("-");
			}
			else
				cell = new Listcell(MedisafeUtil.convertIntoDrugsType(detil.getMsItem().
													getNItemType().shortValue()));
			cell.setParent(item);
			
			
			//satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(detil.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
			
			
			if(detil.getNTotalQty() == null)
				cell = new Listcell("-");
			else cell = new Listcell(detil.getNTotalQty().toString());
			cell.setParent(item);
			
			if(detil.getNQty().intValue() == 0)
				hargaSatuan = 0;
			else
				hargaSatuan = detil.getNValue()/detil.getNQty();
			db.setValue(new BigDecimal(hargaSatuan));
			
			cell = new Listcell(db.getText());
			cell.setParent(item);
			

			//item price before discount
			cell = new Listcell();
			Intbox jumlahRetur = new Intbox();
			jumlahRetur.setValue(new Integer(detil.getNQty()));
			jumlahRetur.setWidth("90%");
			jumlahRetur.setParent(cell);
			jumlahRetur.setDisabled(true);
			cell.setParent(item);
			
			db.setValue(new BigDecimal(detil.getNValue()));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
						
		}
		
		controller.total.setValue(new BigDecimal(retur.getNTrxValue()));

	}

	
	

	public List<TbReturPharmacyDetailTrx> getReturDetil(TbReturPharmacyTrx retur) throws VONEAppException {
		
		
		return null;
	}
	
	

	public void getNoteDetil(ApotikController controller) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		TbExamination note = (TbExamination)controller.notaList.getSelectedItem().getValue();
		
		note = noteDao.getNote(note.getNExamId());
		
		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);
		
		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(note.getMsPatient().getNPatientId());
		
		controller.patientName.setValue(note.getMsPatient().getVPatientName());
		controller.address.setValue(note.getMsPatient().getVPatientMainAddr());
		controller.dateOfBirth.setValue(note.getMsPatient().getDPatientDob());
		controller.receiptNumber.setValue(note.getVRecipeNo());
		
		
		int[] age = MedisafeUtil.calculateAge(note.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));
		
		if(note.getMsPatient().getMsPatientType() != null){
			for(int i=1; i < controller.patientType.getItems().size(); i++){
				if(((MsPatientType)controller.patientType.getItemAtIndex(i).getValue()).getNPatientTypeId().
						equals(note.getMsPatient().getMsPatientType().getNPatientTypeId()))
					
					controller.patientType.setSelectedItem(controller.patientType.getItemAtIndex(i));

			}
		}
		
				
		if(mr != null) controller.MRNumber.setValue(mr.getVMrCode());
		if(note.getTbRegistration() != null) controller.registrationNumber.setValue(note.getTbRegistration().
				getVRegSecondaryId());
		
		if(note.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		this.getItemsDetail(controller, note);
	}
	
	private Listbox getAturanPakai(String ap) {
		String aturanPakai[] = {"(Setiap 24 Jam) sesudah makan","(Setiap 12 Jam) sesudah makan", "(Setiap 8 Jam) sesudah makan", 
				                "(Setiap 6 Jam) sesudah makan","(Setiap 24 Jam) sebelum makan", "(Setiap 12 Jam) sebelum makan",
				                "(Setiap 8 Jam) sebelum makan","(Setiap 6 Jam) sebelum makan"};
		
		Listbox lb = new Listbox();
		Listitem item = null;
		for(int i=0; i < aturanPakai.length; i++){
			item = new Listitem(aturanPakai[i]);
			item.setParent(lb);
			if(aturanPakai[i].equals(ap)) lb.setSelectedItem(item);
		}
		return lb;
	}
	
	public void getItemsDetail(ApotikController controller, TbExamination note) throws VONEAppException{
		Listitem item = null;
		Listcell cell = null;
		//AMBIL DETIL NOTA
		controller.pharmacyList.getItems().clear();
		
		
//		List<TbItemTrx> obats = apServ.getItemsTrx(note);
		List obats = Service.getPolyclinicManager().getItemTrx(note);
		List<TbDrugIngredients> racikans = dao.getRacikanTrx(note);
		Set<TbMiscTrx> miscs = note.getTbMiscTrx();
		
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		double hargaSatuan = 0;
		int jasaR = 0;
		
		Iterator it = obats.iterator();
		while(it.hasNext()){
			Object[] obj = (Object[])it.next();;
			item = new Listitem();
			
			TbItemTrx itemtrx = this.dao.getExpiredDateItemTrx(note.getNExamId(), (Integer)obj[0]);
			
			item.setAttribute("expired", itemtrx.getTbBatchItem().getDBatchExpDate());
			item.setValue(obj);
			
			item.setParent(controller.pharmacyList);
			
//			item code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(obj[1].toString());
//			cell.setLabel(itemTrx.getMsItem().getVItemCode());
			
			//item name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(obj[2].toString());
//			cell.setLabel(itemTrx.getMsItem().getVItemName());
			
			//item tipe
			if(obj[8] == null){
				cell = new Listcell(" - ");
			}
			else
				cell = new Listcell(MedisafeUtil.convertIntoDrugsType((Short)obj[8]));
			cell.setParent(item);
			
			
			//satuan
			cell = new Listcell(obj[3].toString());
			cell.setParent(item);
			
//		
			
			//jumlah
			cell = new Listcell();
			
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setHeight("14px");
			jumlah.setStyle("font-size:8pt");
			jumlah.setValue(new Integer(obj[4].toString()));
			jumlah.setParent(cell);
			
			cell.setParent(item);
//			cell.setLabel(obj[4].toString());
			
			//aturan pakai
			cell = new Listcell();
			if(obj[12] != null){
				String[] aturanPakai = obj[12].toString().split("-");
				Textbox tbox = new Textbox(aturanPakai[0]);
				tbox.setParent(cell);
				tbox.setWidth("25%");
				tbox.setHeight("13px");
				Listbox lb = getAturanPakai(aturanPakai[1]);
				lb.setParent(cell);
				lb.setWidth("73%");
				lb.setMold("select");
				lb.setStyle("font-size:9pt");
				
			}
				
			cell.setParent(item);
			
			
			hargaSatuan = (Double) obj[5] / (Integer)obj[4];
			db.setValue(new BigDecimal(hargaSatuan));
			//item price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			

			
			//discount
//			double discount = (Double)obj[5] - (Double)obj[7];
//			db.setValue(new BigDecimal(discount));
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setStyle("font-size:8pt");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal((Double)obj[6]));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(obj[9].toString().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
//			cell.setLabel(db.getText());
			
			
			db.setValue(new BigDecimal((Double)obj[7]));
			
			//item price after discount			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			jasaR = jasaR + ((Short)obj[11]).intValue();
			
		}
		
		for(TbDrugIngredients drug : racikans){
			item = new Listitem();
			item.setValue(drug);
			item.setAttribute("aturanPakai", drug.getAturanPakai());
			item.setParent(controller.pharmacyList);
			
			//racikan code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(drug.getVDingrId());
			
			//racikan name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(drug.getVItemComposition());
			
			//racikan tipe
			cell = new Listcell("RACIKAN");
			cell.setParent(item);
			
			
			//satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(drug.getNDingrQuantify());
			
			//jumlah 

			cell = new Listcell();
			
			Intbox jumlah = new Intbox();
			jumlah.setStyle("font-size:8pt");
			jumlah.setHeight("14px");
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(drug.getNDingrQty()));
			
			jumlah.setParent(cell);
			
			cell.setParent(item);
			
			//aturan pakai
			cell = new Listcell();
			if(drug.getAturanPakai() != null){
				String[] aturanPakai = drug.getAturanPakai().split("-");
				Textbox tbox = new Textbox(aturanPakai[0]);
				tbox.setParent(cell);
				tbox.setWidth("25%");
				tbox.setHeight("13px");
				Listbox lb = getAturanPakai(aturanPakai[1]);
				lb.setParent(cell);
				lb.setWidth("73%");
				lb.setMold("select");
				lb.setStyle("font-size:9pt");
			}
			cell.setParent(item);

			
			hargaSatuan = drug.getNAmountTrx()/drug.getNDingrQty();
			db.setValue(new BigDecimal(hargaSatuan));
			//item price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			

			
			//discount
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setStyle("font-size:8pt");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(drug.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(drug.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			
			
			
			db.setValue(new BigDecimal(drug.getNAmountAfterDisc()));
			
			//treatment price after discount			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			Set<TbDrugIngredientsDetail> detil = drug.getTbDrugIngredientsDetails();
			for(TbDrugIngredientsDetail det : detil){
				jasaR= jasaR +det.getMsItem().getNR().intValue();
			}
			
		}
		
		for(TbMiscTrx trx : miscs){
			
			item = new Listitem();
			item.setValue(trx);
			item.setParent(controller.pharmacyList);
			
			//tratment code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(MedisafeConstants.MISC_CODE);
			
			//treatment name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(trx.getVMiscName());
			
			//tipe
			cell = new Listcell("OBAT LUAR");
			cell.setParent(item);
			
			
//			satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel("-");
			
			//treatment quantity
			cell = new Listcell();
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(trx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);
//			cell.setLabel("1");
			
			//aturan pakai
			cell = new Listcell("-");
			cell.setParent(item);
			
			hargaSatuan = trx.getNAmountTrx() / trx.getNQty();
			
			db.setValue(new BigDecimal(hargaSatuan));
			//treatment price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			
			//discount
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(trx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(trx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			
			
			db.setValue(new BigDecimal(trx.getNAmountAfterDisc()));
			
			//treatment price after discount			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());

			
		}
		
		MiscTrxController.setFont(controller.pharmacyList);
		
		if(note.getTotal() != null)controller.grandTotal.setValue(new BigDecimal(note.getTotal().doubleValue()));
		
		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(note.getNExamStatus().intValue()));
		
		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		
		if(note.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			controller.btnModify.setDisabled(true);
			controller.btnValidation.setDisabled(true);
		}

		controller.nota=note;
		controller.discount.setValue(new BigDecimal(jasaR));

		
	}

	
	
	public void saveReturPatientInventory(ReturObatController controller) throws VONEAppException,InterruptedException {
		
		Listitem item;
		Listcell cell;
		
		List listitems = controller.pharmacyList.getItems();
		List<TbPatientInventory> itemInv = null;
		MsItem msItem = null;
		TbPatientInventory patInv = null;
		
		Intbox itemBack;
		Date tanggal = new Date();
		
		TbRegistration reg = this.regDao.getRegistrationByRegistrationCode(controller.registrationNumber.getValue());

		Integer terpakai = null;
		
		int jumlahInv = 0;
		int kembali = 0;
		
		Set<TbReturPharmacyDetailTrx> detil = new HashSet<TbReturPharmacyDetailTrx>();
		Set<TbPatientInventory> patInvs = new HashSet<TbPatientInventory>();
		
		Set<TbItemTrx> itemsTrxBack = new HashSet<TbItemTrx>();
				
		Iterator it = listitems.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			double hargaSatuan =(Double)item.getAttribute("hargaSatuan");
			Object[] obj = (Object[])item.getValue();
			msItem = new MsItem();
			msItem.setNItemId((Integer)obj[0]);

			terpakai = (Integer)obj[6];
			
			cell = (Listcell)item.getChildren().get(6);
			itemBack = (Intbox)cell.getChildren().get(0);
			
					
						
			if(itemBack.getValue() != null){
				
				kembali = itemBack.intValue();
				
				patInv = new TbPatientInventory();
				patInv.setMsItem(msItem);
				patInv.setMsPatient(controller.patient);
				patInv.setNQtyOut(itemBack.getValue().shortValue());
				patInv.setDWhnCreate(new Date());
				patInv.setVWhoCreate(controller.user.getVUserName());
				patInv.setTbRegistration(reg);
				
				patInvs.add(patInv);
				
				itemInv = this.patinvDao.getPatientInventoryTrx(msItem, reg); 
					
				for(TbPatientInventory inv : itemInv){
					jumlahInv = jumlahInv + inv.getNQty();
					if(kembali == 0) break;
					if(jumlahInv >= terpakai.intValue()){
						if((terpakai.intValue() + kembali) > jumlahInv){
							//ambil item trx utk inventory ini
//							hargaSatuan = inv.getTbItemTrx().getNAmountAfterDisc()/inv.getTbItemTrx().getNQty();
							int nqty = jumlahInv - terpakai.intValue();
							kembali = kembali - nqty;
							
							controller.returDetil = new TbReturPharmacyDetailTrx();
							controller.returDetil.setVWhoCreate(controller.user.getVUserName());
							controller.returDetil.setMsItem(msItem);
							controller.returDetil.setNQty((short)nqty);
							controller.returDetil.setDWhnCreate(tanggal);
							if(inv.getTbItemTrx() != null)controller.returDetil.setTbBatchItem(inv.getTbItemTrx().getTbBatchItem());
							controller.returDetil.setNValue(nqty * hargaSatuan);
							
							detil.add(controller.returDetil);
							
//							nilaiTransaksi = nilaiTransaksi + (jumlahInv - terpakai.intValue()) * hargaSatuan;
							
//							TbItemTrx trx = inv.getTbItemTrx();
							TbItemTrx trx = new TbItemTrx();
							trx.setMsItem(msItem);
							trx.setTbBatchItem(inv.getTbItemTrx().getTbBatchItem());
							trx.setNQty(new Float(nqty));
							itemsTrxBack.add(trx);
							
						}else{
//							hargaSatuan = inv.getTbItemTrx().getNAmountAfterDisc()/inv.getTbItemTrx().getNQty();
							
							controller.returDetil = new TbReturPharmacyDetailTrx();
							cell = (Listcell)item.getChildren().get(4);
							
							controller.returDetil.setVWhoCreate(controller.user.getVUserName());
							controller.returDetil.setNTotalQty(new Short(cell.getLabel()));
							controller.returDetil.setMsItem(msItem);
							controller.returDetil.setNQty((short)kembali);
							controller.returDetil.setDWhnCreate(tanggal);
							if(inv.getTbItemTrx() != null)controller.returDetil.setTbBatchItem(inv.getTbItemTrx().getTbBatchItem());
							controller.returDetil.setNValue(kembali * hargaSatuan);
							
							detil.add(controller.returDetil);
							
							//TbItemTrx trx = inv.getTbItemTrx();
							TbItemTrx trx = new TbItemTrx();
							trx.setMsItem(msItem);
							if(inv.getTbItemTrx() != null) trx.setTbBatchItem(inv.getTbItemTrx().getTbBatchItem());
//							trx.setNAmountAfterDisc(hargaSatuan * kembali);
							trx.setNQty(new Float(kembali));
							itemsTrxBack.add(trx);
//							nilaiTransaksi = nilaiTransaksi + (kembali * hargaSatuan);
							kembali = 0;
						}
					}
				}
				
			}
			
			
		}
		
				
		controller.apotikReturn = new TbReturPharmacyTrx();
		controller.apotikReturn.setVWhoCreate(controller.user.getVUserName());
		if(!controller.transactionNumber.getText().trim().equals(""))
			controller.apotikReturn.setTbExamination((TbExamination)controller.transactionNumber.
					getAttribute("nota"));
		controller.apotikReturn.setNTrxValue(controller.total.getValue().doubleValue());
		controller.apotikReturn.setNStatus((short)MedisafeConstants.ACTIVE_NOTE);
		controller.apotikReturn.setMsPatient(controller.patient);
		controller.apotikReturn.setTbRegistration(reg);
		controller.apotikReturn.setDWhnCreate(tanggal);
		
		Integer nomorNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RETUR);
		String codeRetur = MedisafeUtil.generateNotaNumber(nomorNota, tanggal,((MsUnit)controller.locationList.
							getSelectedItem().getValue()).getVUnitCode(), MedisafeConstants.RETUR_ITEM);
		controller.apotikReturn.setVReturCode(codeRetur);
		
		
		if(this.saveItemRetur(controller.apotikReturn, detil,patInvs,
				itemsTrxBack, (MsUnit)controller.locationList.getSelectedItem().getValue())){
			
			Messagebox.show(MessagesService.getKey("common.save.success"));
			controller.returnNoteNumber.setValue(controller.apotikReturn.getVReturCode());
			controller.returnNoteNumber.setAttribute("retur", controller.apotikReturn);
			
			controller.labelStatus.setVisible(true);
			controller.statusNota.setVisible(true);
			controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.apotikReturn.getNStatus()));
			
			controller.setButtonDisable(false);
			controller.btnSave.setDisabled(true);
			
			
			
		}else{
			Messagebox.show(MessagesService.getKey("common.save.fail"));
		}
		
		controller.isRanap = false;

		
	}
	
	

	public boolean saveModify(TbExamination nota, Set<TbItemTrx> itemTrxs, Set<TbDrugIngredients> racikans, 
				Integer warehouseId, Set<TbMiscTrx> miscs) throws VONEAppException {
		
		return dao.saveModify(nota, itemTrxs, racikans, warehouseId, miscs);
	}

	
	@Override
	public void getValidatedNoteForAntrian(Listbox listPasien1, Listbox listPasien2)
			throws VONEAppException {
		listPasien1.getItems().clear();
		listPasien2.getItems().clear();
		Listitem item;
		Listcell cell;
		
		List<TbExamination> siaps = this.dao.getObatJadi();
		for(TbExamination nota : siaps){
			item = new Listitem();
			item.setValue(nota);
			item.setParent(listPasien2);
			
			cell = new Listcell(nota.getMsPatient().getVPatientName());
			cell.setParent(item);
			
			item.addEventListener("onClick", new TakeoutAntrianListener(listPasien2, item));
			
		}
		
		List<TbExamination> notas = this.dao.getValidatedNoteToday();
		for(TbExamination nota : notas){
			item = new Listitem();
			item.setValue(nota);
			item.setParent(listPasien1);
			
			cell = new Listcell(nota.getMsPatient().getVPatientName());
			cell.setParent(item);
			
			item.addEventListener("onClick", new MoveToAntrianApotikListener(listPasien1, listPasien2, item));
		}
		
	}

	@Override
	public void takeOutAntrianApotik(Listbox myList, Listitem myItem) {
		TbExamination nota = (TbExamination)myItem.getValue();
		nota.setAntrianStatus(2);
		
		this.dao.updateAntrianNote(nota);
		
		myList.removeChild(myItem);
		
	}

	@Override
	public void moveToAntiranApotik(Listbox validList, Listbox jadiList,
			Listitem item) {
		TbExamination nota = (TbExamination)item.getValue();
		nota.setAntrianStatus(1);
		nota.setDWhnChange(new Date());
		this.dao.updateAntrianNote(nota);
		try {
			getValidatedNoteForAntrian(validList, jadiList);
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		jadiList.appendChild(item);
//		validList.removeChild(item);
		
	}

	@Override
	public void getTextAntrian(Button btnSave, Button btnEdit, Textbox textAntrian)
			throws VONEAppException {
		MsAntrian antrian = this.dao.getMasterAntrian();
		textAntrian.setAttribute("textapotik", antrian);
		if(antrian.getAntrianApotik()== null){
			btnEdit.setDisabled(true);
			btnSave.setDisabled(false);
		}
		else{
			btnEdit.setDisabled(false);
			btnSave.setDisabled(true);
			textAntrian.setValue(antrian.getAntrianApotik());
			textAntrian.setReadonly(true);
		}
		
	}

	@Override
	public void saveAntrian(Button btnSave, Button btnEdit, Textbox textAntrian)
			throws VONEAppException {
		MsAntrian antrian = (MsAntrian)textAntrian.getAttribute("textapotik");
		antrian.setAntrianApotik(textAntrian.getValue());
		this.dao.updateAntrianNote(antrian);
		try {
			Messagebox.show("Data Sukses Disimpan...!", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnSave.setDisabled(true);
		btnEdit.setDisabled(false);
		textAntrian.setReadonly(true);
		
//		textAntrian.removeAttribute("textapotik");
		
	}

	@Override
	public void getApotikAntrian(Listbox listPasien) throws VONEAppException {
		listPasien.getItems().clear();
		Listitem item;
		Listcell cell;
		int counter = 1;
		String noRm;
		String nomor;
		String typeObat;
		List<TbExamination> notas = this.dao.getObatJadi();
		for(TbExamination nota : notas){
			item = new Listitem();
			item.setValue(nota);
			item.setParent(listPasien);
			
			noRm = "";
			if(nota.getTbRegistration() != null)
				noRm = " (" +nota.getTbRegistration().getTbMedicalRecord().getVMrCode()+")";
			if(counter < 10) nomor = "0"+counter;
			else nomor=counter+"";
			cell = new Listcell(nomor +". " +nota.getMsPatient().getVPatientName() + noRm);
			cell.setStyle("font-weight:bold;font-size:20pt");
			cell.setParent(item);
			
			typeObat="";
			if(nota.getTbDrugIngredients().size() > 0)
				typeObat = "RACIKAN";
			else typeObat = "NON-RACIKAN";
			
			cell = new Listcell(typeObat);
			cell.setStyle("font-weight:bold;font-size:20pt");
			cell.setParent(item);
			
			counter = counter + 1;
		}
		
		notas.clear();
		
	}
	
	


   
	
}

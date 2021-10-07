package com.vone.medisafe.ward;

import java.util.HashSet;
import java.util.Set;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;

/**
 * WardTransactionManagerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 09 2006
 * @category service
 */

public class WardTransactionManagerImpl implements WardTransactionManager{
	
	private WardTransactionDAO dao;
	private TbRegistrationDAO regDao;
	private TbBedOccupancyDAO bocDao;
	private NoteDAO noteDao;
	private TbMedicalRecordDAO mrDoo;

	public TbMedicalRecordDAO getMrDoo() {
		return mrDoo;
	}

	public void setMrDoo(TbMedicalRecordDAO mrDoo) {
		this.mrDoo = mrDoo;
	}

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
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

	public WardTransactionDAO getDao() {
		return dao;
	}

	public void setDao(WardTransactionDAO dao) {
		this.dao = dao;
	}



	
	
	
	public void save(WardTransactionController controller) throws VONEAppException, InterruptedException {
		
		if(controller.isUpdate){
			controller.saveModify();
			controller.isUpdate = false;
			return;
		}
		
		if((controller.patientName.getText().trim() == "") || (controller.wardList.getItems().size() == 0)){
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if(controller.total.getValue() == null){
			Messagebox.show(MessagesService.getKey("common.transaction.total.not.counted"));
			return;
		}
		
		
		//step
		//simpan ke dalam tabel examination, generate note here, assign juga siapa pasiennya
		//masukan ke tabel transaksi, lakukan updating langsung terhadap jumlah item di inventory (paling ruwet)
		
			
		controller.nota = new TbExamination();
		controller.nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		controller.nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		controller.nota.setTbRegistration(controller.reg);
		controller.nota.setMsUnit((MsUnit)controller.locationList.getSelectedItem().getValue());
		controller.nota.setTotal(new Double(controller.total.getValue().doubleValue()));
		controller.nota.setMsPatient(controller.mr.getMsPatient());
				
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		
		MiscTrxController.saveTrx(controller.wardList, controller.user, treatmentSet, itemSet, miscSet);
		
		//save(nota,treatmentSet,itemSet)
		if(Service.getPolyclinicManager().save(controller.mr.getMsPatient(), controller.nota, treatmentSet, 
				itemSet, bundledSet , miscSet, (MsUnit)controller.locationList.getSelectedItem().getValue(),true)){
			
			Messagebox.show(MessagesService.getKey("common.save.success"));
			controller.transactionNumber.setValue(controller.nota.getVNoteNo());
			controller.transactionNumber.setAttribute("nota", controller.nota);
			
			controller.statusNota.setVisible(true);
			controller.labelStatus.setVisible(true);
			controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.nota.getNExamStatus().intValue()));
			
			controller.setFieldDisable(true);
			controller.setButtonActive(false);
			controller.setInnerButtonDisable(true);
		}
		else
		
			Messagebox.show(MessagesService.getKey("common.save.fail"));
		
		
	}
	
	

	public void getRegistrationDetil(WardTransactionController controller, int type) throws VONEAppException,
		InterruptedException
	{
		Listitem item;
		
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			controller.reg = this.dao.getRanapByMrCode(code); 
						
			
			if(controller.reg == null){
				
				Messagebox.show(MessagesService.getKey("mr.not.found"));
				controller.MRNumber.focus();
				
				return;
			}
			
			controller.mr = controller.reg.getTbMedicalRecord();
		}
		else{
			
			item = controller.mRNumberList.getSelectedItem();
			
			controller.mr = (TbMedicalRecord)item.getValue();
			controller.reg = this.dao.getLastRanap(controller.mr);
			controller.mr = controller.reg.getTbMedicalRecord();
			controller.MRNumber.setValue(controller.mr.getVMrCode());
		}
		
		
		
				
		int[] umurSkrg = MedisafeUtil.calculateAge(controller.mr.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(umurSkrg));
		
		if(controller.mr.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		
		if(controller.reg.getMsStaff() != null){
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode()+"-"+
					controller.reg.getMsStaff().getVStaffName());
		}		
		
		controller.patientName.setValue(controller.mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
		TbBedOccupancy boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId()); 
			
		
		controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc();
		
		if(controller.ranapKelas != null)controller.treatmentClass.setValue(controller.ranapKelas);	
		
		controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
		controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());
		
		if(controller.mr.getMsPatient().getMsPatientType() != null)
			controller.patientType.setValue(controller.mr.getMsPatient().getMsPatientType().getVTpatientDesc());
		
		
	}

	
	public void getNoteDetil(WardTransactionController controller) throws VONEAppException, InterruptedException {
		
		
		TbExamination note = (TbExamination)controller.notaList.getSelectedItem().getValue();
		
		note = this.noteDao.getNote(note.getNExamId());
		
		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);
		
		controller.reg = note.getTbRegistration();
		
		controller.mr = mrDoo.getMedicalRecordByPaitentId(note.getMsPatient().getNPatientId());
		
		TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
		
		controller.patientName.setValue(note.getMsPatient().getVPatientName());
						
		if(note.getTbRegistration() != null){
			if(note.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(note.getTbRegistration().getMsStaff().getVStaffName());
		}
		
		int[] age = MedisafeUtil.calculateAge(note.getMsPatient().getDPatientDob());
		
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));
		
		
		
		if(controller.mr != null) controller.MRNumber.setValue(controller.mr.getVMrCode());
		if(note.getTbRegistration() != null) controller.registrationNumber.setValue(note.getTbRegistration().getVRegSecondaryId());
		
		if(note.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		controller.treatmentClass.setValue(boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc());	
		controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
		controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());
		if(controller.mr.getMsPatient().getMsPatientType() != null)
			controller.patientType.setValue(controller.mr.getMsPatient().getMsPatientType().getVTpatientDesc());
		
		
		
//		 AMBIL DETIL NOTA
		controller.wardList.getItems().clear();
		controller.nota = note;

		Service.getNotaManager().getNoteDetil(controller.nota, controller.wardList);

	}

	
	
	public void updateRegistration(TbRegistration reg) throws VONEAppException {
		
		regDao.updateRegistration(reg);
	}

	public void getRegistration(int type, Window win) throws VONEAppException, InterruptedException {
		
		Listitem item;
		TbRegistration reg = null;
		TbMedicalRecord mr = null;
		
		Bandbox noMR = (Bandbox)win.getFellow("noMR");
		Textbox namaPasien = (Textbox)win.getFellow("namaPasien");
		Listbox patientSearchList = (Listbox)win.getFellow("patientSearchList");
		Textbox bed = (Textbox)win.getFellow("bed");
		Listbox locationList = (Listbox)win.getFellow("locationList");
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(noMR.getText());
			noMR.setValue(code);
			
			reg = this.regDao.getRegistrationBaseOnWard(code,unit.getVUnitName());
			
//			reg = this.dao.getRanapByMrCode(code); 
						
			
			if(reg == null){
				
				Messagebox.show(MessagesService.getKey("pasien.tidak.terdaftar.dibangsal.ini"));
				noMR.focus();
				
				return;
			}
			
			mr = reg.getTbMedicalRecord();
		}
		else{
			
			item = patientSearchList.getSelectedItem();
			
			mr = (TbMedicalRecord)item.getValue();
			reg = this.dao.getLastRanap(mr);
			mr = reg.getTbMedicalRecord();
			noMR.setValue(mr.getVMrCode());
		}
		
		TbBedOccupancy boc = bocDao.getBedOccupanyByRegId(reg.getNRegId());
		namaPasien.setText(mr.getMsPatient().getVPatientName());
		bed.setText(boc.getId().getMsBed().getVBedDesc());
		bed.setAttribute("ruangan", boc.getId().getMsBed().getMsRoom().getMsHall().getVHallName());
		bed.setAttribute("kelas", boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc());
		noMR.setAttribute("regId", reg.getNRegId());
		noMR.setAttribute("regNo", reg.getVRegSecondaryId());
		
	}

	

	

}

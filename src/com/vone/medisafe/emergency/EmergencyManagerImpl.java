package com.vone.medisafe.emergency;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsUnit;
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




public class EmergencyManagerImpl implements EmergencyManager{
	
	private EmergencyDAO dao;
	private TbMedicalRecordDAO mrDao;
	private NoteDAO noteDao;
	private TbRegistrationDAO regDao;

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
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

	public EmergencyDAO getDao() {
		return dao;
	}

	public void setDao(EmergencyDAO dao) {
		this.dao = dao;
	}

	
	

	
	public void getPatientDetil(EmergencyController controller, int type) 
		throws VONEAppException, InterruptedException {
		
		Listitem item;
		
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
//			controller.mr = mrDao.getPatientMedicalRecord(code);
			controller.mr = mrDao.getMrRegistered(code);
			if(controller.mr == null){
				
				Messagebox.show(MessagesService.getKey("mr.not.found"));
				controller.MRNumber.focus();
				
				return;
			}
		}
		else{
			item = controller.mRNumberList.getSelectedItem();
			controller.mr = (TbMedicalRecord)item.getValue();
			
		}
		
		controller.reg = regDao.getLastRegistrationByMrId(controller.mr.getNMrId());
				
		controller.MRNumber.setValue(controller.mr.getVMrCode());
		controller.dateOfBirth.setValue(controller.mr.getMsPatient().getDPatientDob());
		controller.address.setValue(controller.mr.getMsPatient().getVPatientMainAddr());
		
		int[] umurSkrg = MedisafeUtil.calculateAge(controller.mr.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(umurSkrg));
		
		if(controller.mr.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		
		if(controller.mr.getMsPatient().getMsPatientType() != null){
			for(int i=1; i < controller.patientTypeList.getItems().size(); i++){
				if(((MsPatientType)controller.patientTypeList.getItemAtIndex(i).getValue()).getNPatientTypeId().
						equals(controller.mr.getMsPatient().getMsPatientType().getNPatientTypeId()))
					
					controller.patientTypeList.setSelectedItem(controller.patientTypeList.getItemAtIndex(i));
			}
		}
		
		controller.patientName.setValue(controller.mr.getMsPatient().getVPatientName());
		if(controller.reg.getMsStaff() != null){
			controller.mainDoctor.setAttribute("doctor", controller.reg.getMsStaff());
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode()+"-"+
					controller.reg.getMsStaff().getVStaffName());
		}
		
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
	}
	
	

	public void getNoteDetil(EmergencyController controller) throws VONEAppException {
	
				
		controller.nota = (TbExamination)controller.notaList.getSelectedItem().getValue();
		
		controller.nota = noteDao.getNote(controller.nota.getNExamId());
		
		controller.transactionNumber.setValue(controller.nota.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", controller.nota);
		
		if(controller.nota.getTbRegistration() != null){
			
			controller.MRNumber.setValue(controller.nota.getTbRegistration().
											getTbMedicalRecord().getVMrCode());
			
			controller.registrationNumber.setValue(controller.nota.getTbRegistration().getVRegSecondaryId());
			
		}
		
		
		controller.patientName.setValue(controller.nota.getMsPatient().getVPatientName());
		controller.address.setValue(controller.nota.getMsPatient().getVPatientMainAddr());
		controller.dateOfBirth.setValue(controller.nota.getMsPatient().getDPatientDob());
		
		if(controller.nota.getTbRegistration() != null){
			if(controller.nota.getTbRegistration().getMsStaff() != null)
				 controller.mainDoctor.setValue(controller.nota.getTbRegistration().getMsStaff().getVStaffName());
				
		}
		
		int[] age = MedisafeUtil.calculateAge(controller.nota.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));
		
		if(controller.nota.getMsPatient().getMsPatientType() != null){
			for(int i=1; i < controller.patientTypeList.getItems().size(); i++){
				if(((MsPatientType)controller.patientTypeList.getItemAtIndex(i).getValue()).getNPatientTypeId().
						equals(controller.nota.getMsPatient().getMsPatientType().getNPatientTypeId()))
					
					controller.patientTypeList.setSelectedItem(controller.patientTypeList.getItemAtIndex(i));

			}
		}
		
		if(controller.nota.getMsPatientEscort() != null){
			for(int i=1; i < controller.patientEscortList.getItems().size(); i++){
				if(((MsPatientEscort)controller.patientEscortList.getItemAtIndex(i).getValue()).
						getNEscortPrimaryId().equals(controller.nota.getMsPatientEscort().getNEscortPrimaryId()))
					
					controller.patientEscortList.setSelectedItem(controller.patientEscortList.getItemAtIndex(i));

			}
		}
		
				
		if(controller.nota.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		
		Service.getNotaManager().getNoteDetil(controller.nota, controller.emergencyList);
			
		
	}
	

	public void save(EmergencyController controller) throws VONEAppException, InterruptedException {
		
		if(controller.isUpdate){
			controller.saveModify();
			controller.isUpdate = false;
			return;
		}
		
//		if(!constraints.validateComponent(true))return;
		if((controller.MRNumber.getText().trim().equals("")) || (controller.emergencyList.getItems().size() == 0)){
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		
		if(controller.total.getValue() == null){
			Messagebox.show(MessagesService.getKey("common.transaction.total.not.counted"));
			return;
		}
		
		
		
		controller.reg = regDao.getLastRegistrationByMrId(controller.mr.getNMrId());
		
		controller.nota = new TbExamination();
		
		if(controller.reg == null){
			
			controller.reg = new TbRegistration();
			controller.reg.setRegStatus(MedisafeConstants.REG_ACTIVE);
			controller.reg.setTbMedicalRecord(controller.mr);
			controller.reg.setVWhoCreate(controller.user.getVUserName());
			controller.reg.setMsUnit((MsUnit)controller.locationList.getSelectedItem().getValue());
			controller.reg.setDWhnCreate(new Date());
		
		}
		
		
		
		if(controller.patientEscortList.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG){
			
			controller.nota.setMsPatientEscort((MsPatientEscort)controller.patientEscortList.getSelectedItem().getValue());
			controller.reg.setNEscortPrimaryId(((MsPatientEscort)controller.patientEscortList.getSelectedItem().
					getValue()).getNEscortPrimaryId());
			
		}
		
		controller.nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));	
		controller.nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		controller.nota.setMsUnit((MsUnit)controller.locationList.getSelectedItem().getValue());
		controller.nota.setTotal(new Double(controller.total.getValue().doubleValue()));
		controller.nota.setVWhoCreate(controller.user.getVUserName());
		controller.nota.setMsPatient(controller.mr.getMsPatient());
		
		
		
		if(!controller.mainDoctor.getText().equalsIgnoreCase("")){
			
			controller.reg.setMsStaff((MsStaff)controller.mainDoctor.getAttribute("doctor"));
			controller.reg.setVMainDoctorStatus(MedisafeConstants.MAIN_DOCTOR);
			
			
		}else{
			
			controller.reg.setVMainDoctorStatus(MedisafeConstants.NO_DOCTOR);
			
		}
		
		
		
		
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
//		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);

		MiscTrxController.saveTrx(controller.emergencyList, controller.user, treatmentSet, itemSet, miscSet);
		
		if(dao.save(controller.reg, controller.nota, treatmentSet, itemSet, (MsUnit)controller.locationList.
				getSelectedItem().getValue(), miscSet)){
			
			Messagebox.show(MessagesService.getKey("common.save.success"));
			
			controller.nota = noteDao.getNote(controller.nota.getNExamId());
			
			controller.transactionNumber.setValue(controller.nota.getVNoteNo());
			controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
			controller.transactionNumber.setAttribute("nota", controller.nota);
			
			controller.statusNota.setVisible(true);
			controller.labelStatus.setVisible(true);
			
			controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.nota.
					getNExamStatus().intValue()));
			controller.setFieldDisable(true);
			controller.setButtonActive(false);
			controller.setInnerButtonDisable(true);
			
			controller.transactionNumber.setDisabled(false);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.save.fail"));
		}
		
	}
	

}

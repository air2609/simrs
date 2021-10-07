package com.vone.medisafe.service.ifaceimpl.polyclinic;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbBundledTreatment;
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
import com.vone.medisafe.mapping.dao.PolyclinicDAO;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.polyclinic.PolyclinicManager;
import com.vone.medisafe.ui.polyclinic.PolyclinicController;

public class PolyclinicManagerImpl implements PolyclinicManager{
	
	private PolyclinicDAO dao;
	private TbMedicalRecordDAO mrDao;
	private TbRegistrationDAO regDao;
	private TbBedOccupancyDAO bocDao;
	private NoteDAO noteDao;

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}

	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	public TbRegistrationDAO getRegDao() {
		return regDao;
	}

	public void setRegDao(TbRegistrationDAO regDao) {
		this.regDao = regDao;
	}

	public PolyclinicDAO getDao() {
		return dao;
	}

	public void setDao(PolyclinicDAO dao) {
		this.dao = dao;
	}

	public boolean save(MsPatient patient, TbExamination nota, Set treatmentTrx, Set itemTrx,
						Set bundleTrx, MsUnit unit, boolean isRanap) throws VONEAppException {
		// TODO Auto-generated method stub
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		return dao.saveTransaction(patient, nota, treatmentTrx, itemTrx, bundleTrx, miscSet, unit, isRanap);
	}

	

	

	

	

	public List getItemTrx(TbExamination note) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getItemTrx(note);
	}

	

	public List getBundelTreatTrxList(TbBundledTreatment bundel) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean save(MsPatient patient, TbExamination nota, Set<TbTreatmentTrx> treatmentSet, 
			Set<TbItemTrx> itemSet, Set<TbBundledTrx> bundledSet, Set<TbMiscTrx> miscSet, MsUnit unit, 
			boolean isRanap) throws VONEAppException {
		
		return dao.saveTransaction(patient, nota, treatmentSet, itemSet, bundledSet, miscSet, unit, isRanap);
	}

	public List getMiscTrx(TbExamination nota)  throws VONEAppException{
		return dao.getMiscTrx(nota);
	}

	
	
	public void getRegistrationDetil(PolyclinicController controller, int type) 
		throws VONEAppException, InterruptedException {
		
		
		
		TbMedicalRecord mr = null;
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			mr = mrDao.getMrRegistered(code);
			if(mr == null){
				
				Messagebox.show(MessagesService.getKey("mr.not.found"));
				controller.MRNumber.focus();
				
				return;
			}
			
			controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		}
		else{
			controller.item = controller.mRNumberList.getSelectedItem();
			mr = (TbMedicalRecord)controller.item.getValue();
			controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
			mr = controller.reg.getTbMedicalRecord();
		}
		
		 
		
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.dateOfBirth.setValue(mr.getMsPatient().getDPatientDob());
		controller.address.setValue(mr.getMsPatient().getVPatientMainAddr());
		
		int[] umurSkrg = MedisafeUtil.calculateAge(mr.getMsPatient().getDPatientDob());
		controller.age.setValue(umurSkrg[0]+" thn "+umurSkrg[1]+" bln "+umurSkrg[2]+" hr");
		
		if(mr.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		
		if(mr.getMsPatient().getMsPatientType() != null){
			for(int i=1; i < controller.patientTypeList.getItems().size(); i++){
				if(((MsPatientType)controller.patientTypeList.getItemAtIndex(i).getValue()).getNPatientTypeId().equals(mr.getMsPatient().getMsPatientType().getNPatientTypeId()))
					controller.patientTypeList.setSelectedItem(controller.patientTypeList.getItemAtIndex(i));

			}
		}
		
		
		
		if(controller.reg.getMsStaff() != null){
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode()+"-"+
					controller.reg.getMsStaff().getVStaffName());
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
		Sessions.getCurrent().setAttribute("mr", mr);
		Sessions.getCurrent().setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_POLY);
		Sessions.getCurrent().setAttribute("location", controller.locationList);
		
		
		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);
		
		controller.setFieldDisable(true);
		

		
	}

	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}

	
	public void getNoteDetil(PolyclinicController controller) throws VONEAppException {
		
		
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
			for(int i=1; i < controller.patientEscort.getItems().size(); i++){
				if(((MsPatientEscort)controller.patientEscort.getItemAtIndex(i).getValue()).getNEscortPrimaryId()
						.equals(controller.nota.getMsPatientEscort().getNEscortPrimaryId()))
					controller.patientEscort.setSelectedItem(controller.patientEscort.getItemAtIndex(i));

			}
		}
		
				
		if(controller.nota.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		
		//AMBIL DETIL NOTA
		controller.polyclinicList.getItems().clear();
		
		
		
		Service.getNotaManager().getNoteDetil(controller.nota, controller.polyclinicList);	
		
		if(controller.nota.getTotal() != null)controller.total.setValue(new BigDecimal(
				controller.nota.getTotal().doubleValue()));
		
		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.nota.getNExamStatus().intValue()));
		
		
		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		
		if(controller.nota.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			controller.btnModify.setDisabled(true);
			controller.btnValidation.setDisabled(true);
		}
		
		
		
	}

	
	public void updateRegistration(TbRegistration reg) throws VONEAppException {
		this.regDao.updateRegistration(reg);
	}

	


}
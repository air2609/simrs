package com.vone.medisafe.vk;

import java.math.BigDecimal;

import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ward.WardTransactionDAO;

public class VkManagerImpl implements VkManager{

	private WardTransactionDAO warDao;
	private TbBedOccupancyDAO bocDao;
	private NoteDAO noteDao;


	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}

	public WardTransactionDAO getWarDao() {
		return warDao;
	}

	public void setWarDao(WardTransactionDAO warDao) {
		this.warDao = warDao;
	}

	
	public void getRegistration(VkController controller, int type) throws VONEAppException, 
		InterruptedException {
		
		Listitem item;
		
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			controller.reg = warDao.getRanapByMrCode(code);
			
			
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
			controller.reg = warDao.getLastRanap(controller.mr);
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
		
		TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
		
		if(boc == null) System.out.println("boc kosong");
		
		else {
			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc();
			controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
			controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());
		}
			
		if(controller.ranapKelas != null)controller.tclass.setValue(controller.ranapKelas);	
		
		

		if(controller.mr.getMsPatient().getMsPatientType() != null)
			controller.patientType.setValue(controller.mr.getMsPatient().getMsPatientType().getVTpatientDesc());
		
	}
	
	

	public void getNoteDetil(VkController controller) throws VONEAppException {
		
		TbExamination note = (TbExamination)controller.transactionNumberList.getSelectedItem().getValue();
		
		note = noteDao.getNote(note.getNExamId());
		
		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);
		
		controller.reg = note.getTbRegistration();
		
		TbMedicalRecord mr = controller.reg.getTbMedicalRecord();
		
		TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
		controller.patientName.setValue(note.getMsPatient().getVPatientName());
						
		if(note.getTbRegistration() != null){
			if(note.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(note.getTbRegistration().getMsStaff().getVStaffName());
		}
		
		int[] age = MedisafeUtil.calculateAge(note.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));
		
		
		
		if(mr != null) controller.MRNumber.setValue(mr.getVMrCode());
		if(note.getTbRegistration() != null) controller.registrationNumber.setValue(note.
				getTbRegistration().getVRegSecondaryId());
		
		if(note.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		controller.tclass.setValue(boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc());	
		controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
		controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());
		if(mr.getMsPatient().getMsPatientType() != null)
			
			controller.patientType.setValue(mr.getMsPatient().getMsPatientType().getVTpatientDesc());
		
		
		
//		 AMBIL DETIL NOTA
		controller.vkList.getItems().clear();
		controller.nota = note;

		Service.getNotaManager().getNoteDetil(controller.nota, controller.vkList);	
		
				
		if(note.getTotal() != null)controller.total.setValue(new BigDecimal(note.getTotal().doubleValue()));

	}

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}
}

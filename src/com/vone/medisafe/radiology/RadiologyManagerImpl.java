package com.vone.medisafe.radiology;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.cafediet.CafeDietController;
import com.vone.medisafe.cafediet.KlinikGiziController;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.fisioterapi.FisioterapiController;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.TbRoomReservation;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.mapping.dao.treatment.MsTreatmentDAO;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.renal.RenalUnitController;
import com.vone.medisafe.renal.TambahTindakanController;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.surgery.SurgeryController;

public class RadiologyManagerImpl implements RadiologyManager {
	
	private static final String SELECT = "select";
	private static final String DASH = "-";
	private static final String SELECT_PERSEN = "2. %";
	private static final String SELECT_RP = "1. RP";
	private static final String PERSEN_40 = "40%";
	private static final String PERSEN_50 = "50%";
	private static final String HEIGHT_14PX = "14px";
	private static final String FONT_SIZE_9PT = "font-size:9pt";
	
	private RadiologyDAO radDao;
	private TbRegistrationDAO regDao;
	private NoteDAO noteDao;
	private MsTreatmentDAO treatDao;
	private TbMedicalRecordDAO mrDao;
	private TbBedOccupancyDAO bocDao;
	
	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}

	public List getMedician(String sMedicCategory) throws VONEAppException {
		return radDao.getMedician(sMedicCategory);
	}

	public RadiologyDAO getRadDao() {
		return radDao;
	}

	public void setRadDao(RadiologyDAO dao) {
		this.radDao = dao;
	}

	public TbRegistrationDAO getRegDao() {
		return regDao;
	}

	public void setRegDao(TbRegistrationDAO regDao) {
		this.regDao = regDao;
	}

	public boolean save(MsPatient patient, TbExamination nota, Set treatmentTrx, Set itemTrx, MsUnit unit, boolean isRanap) throws VONEAppException {
		return radDao.saveTransaction(patient, nota, treatmentTrx, itemTrx, unit, isRanap);
	}

	public MsPatient getMspatient(TbExamination nota) throws VONEAppException {
		return nota.getMsPatient();
	}

	public void getRegistration(RadiologyController controller, int type) throws VONEAppException {
		//regDao = new TbRegistrationDAO();
		
		TbMedicalRecord mr = null;
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			//mr = AdmissionServiceLocator.getMedicalRecordManager().getMrRegistered(code);
			mr = mrDao.getMrRegistered(code);
			if(mr == null){
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
					controller.MRNumber.focus();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
		else{
			controller.item = controller.MRNumberList.getSelectedItem();
			mr = (TbMedicalRecord)controller.item.getValue();

		}
		
		//reg = AdmissionServiceLocator.getRegistrationManager().getRegistrationByMrId(mr.getNMrId());
		controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		
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
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode()+DASH
					+ controller.reg.getMsStaff().getVStaffName());
		}
		
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
		controller.referencePatient.setDisabled(true);
		if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

			controller.isRanap = true;
			
			TbBedOccupancy boc; 
			boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();

			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();

			controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
			controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());

		} else {
			controller.isRanap = false;
			controller.bed.setValue("");
			controller.hall.setValue("");
		}

		controller.patient = mr.getMsPatient();
		controller.session.setAttribute("mr", mr);
		controller.session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_RADIOLOGY);
		
		
		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);
		
		controller.setFieldDisable(true);
	}

	public void getNoteDetail(RadiologyController controller)
			throws VONEAppException {
		
		TbExamination selectedNote = (TbExamination) controller.notaList.getSelectedItem().getValue();
		controller.nota = selectedNote;
		controller.nota = noteDao.getNote(selectedNote.getNExamId());

		controller.transactionNumber.setValue(controller.nota.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", controller.nota);

		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(controller.nota.getMsPatient().getNPatientId());
		
		
		controller.patientName.setValue(controller.nota.getMsPatient().getVPatientName());
		controller.address.setValue(controller.nota.getMsPatient().getVPatientMainAddr());
		controller.dateOfBirth.setValue(controller.nota.getMsPatient().getDPatientDob());

		if (controller.nota.getTbRegistration() != null) {
			if (controller.nota.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(controller.nota.getTbRegistration().getMsStaff()
						.getVStaffName());
		}

		int[] age = MedisafeUtil.calculateAge(controller.nota.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));

		if (controller.nota.getMsPatient().getMsPatientType() != null) {
			for (int i = 1; i < controller.patientTypeList.getItems().size(); i++) {
				if (((MsPatientType) controller.patientTypeList.getItemAtIndex(i)
						.getValue()).getNPatientTypeId().equals(
						controller.nota.getMsPatient().getMsPatientType()
								.getNPatientTypeId()))
					controller.patientTypeList.setSelectedItem(controller.patientTypeList
							.getItemAtIndex(i));
				// this.patientTypeList.setSelectedIndex(i);
			}
		}

		if (controller.nota.getMsPatientEscort() != null) {
			for (int i = 1; i < controller.patientEscort.getItems().size(); i++) {
				if (((MsPatientEscort) controller.patientEscort.getItemAtIndex(i)
						.getValue()).getNEscortPrimaryId().equals(
						controller.nota.getMsPatientEscort().getNEscortPrimaryId()))
					controller.patientEscort.setSelectedItem(controller.patientEscort
							.getItemAtIndex(i));
				// this.patientTypeList.setSelectedIndex(i);
			}
		}

		if (mr != null)
			controller.MRNumber.setValue(mr.getVMrCode());
		if (controller.nota.getTbRegistration() != null)
			controller.registrationNumber.setValue(controller.nota.getTbRegistration()
					.getVRegSecondaryId());

		if (controller.nota.getMsPatient().getVPatientGender().equals("M")) {
			controller.gender.setSelectedIndex(0);
		} else {
			controller.gender.setSelectedIndex(1);
		}
		controller.patient = controller.nota.getMsPatient();
		
		if (mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		
		if (controller.reg != null)
			if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

				controller.isRanap = true;
				
				TbBedOccupancy boc; 
				boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
				
				controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
						.getVTclassDesc();

				controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
				controller.hall.setValue(boc.getId().getMsBed().getMsRoom()
								.getVRoomName());

			} else {
				controller.isRanap = false;
				controller.bed.setValue("");
				controller.hall.setValue("");
			}

		// AMBIL DETIL NOTA
		ambilDetailNota(controller.nota, controller.radiologyList);

		if (controller.nota.getTotal() != null)
			controller.total.setValue(new BigDecimal(controller.nota.getTotal().doubleValue()));

		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.nota
				.getNExamStatus().intValue()));

		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		MiscTrxController.setFont(controller.radiologyList);
		
		if(controller.nota.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			controller.btnValidation.setDisabled(true);
			controller.btnModify.setDisabled(true);
		}

	}

	public void cariNotaClick(RadiologyController controller) throws VONEAppException {
		controller.notaList.getItems().clear();

		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);

		MsUnit unit = (MsUnit) controller.locationList.getSelectedItem().getValue();

		List result = noteDao.searchNote(
				"%" + controller.searchNota.getValue() + "%",
				"%" + controller.searchName.getValue() + "%", unit, MedisafeConstants.ACTIVE_NOTE);

		Listitem item;
		Listcell cell;
		if (result.size()==0) {
			try {
				Messagebox.show("TIDAK ADA DATA");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		Iterator it = result.iterator();
		while (it.hasNext()) {
			controller.nota = (TbExamination) it.next();
			item = new Listitem();
			item.setValue(controller.nota);
			item.setParent(controller.notaList);

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(controller.nota.getVNoteNo());

			cell = new Listcell();
			cell.setParent(item);
			
			MsPatient msPatient = controller.nota.getMsPatient();
			cell.setLabel(msPatient.getVPatientName());

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(MedisafeUtil.getNoteStatus(controller.nota.getNExamStatus()
					.intValue()));
			
			MiscTrxController.setFont(controller.notaList);
		}
		
	}

	public Object[] getMsStaffInUnits(MsUser user) throws VONEAppException {

		user = radDao.getUser(user);//biar dpt session
		Object[] obj = user.getMsStaff().getMsStaffInUnits().toArray();
		
		return obj;
	}

	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	public MsTreatmentDAO getTreatDao() {
		return treatDao;
	}

	public void setTreatDao(MsTreatmentDAO treatDao) {
		this.treatDao = treatDao;
	}

	public TbRegistration getRegistrationByMrId(Integer mrId) throws VONEAppException {
//		reg = AdmissionServiceLocator.getRegistrationManager()
//		.getRegistrationByMrId(mr.getNMrId());
		
		return regDao.getLastRegistrationByMrId(mrId);
	}

	public void getMsStaffInUnits(MsUser user, Listbox locationList) throws VONEAppException {
		user = radDao.getUser(user);//biar dpt session
		Object[] obj = user.getMsStaff().getMsStaffInUnits().toArray();
		
		System.out.println(obj.length);
		
		locationList.getItems().clear();
		for (int i = 0; i < obj.length; i++) {
			MsStaffInUnit siu = (MsStaffInUnit) obj[i];
			Listitem item = new Listitem();
			item.setValue(siu.getId().getMsUnit());
			item.setLabel(siu.getId().getMsUnit().getVUnitCode() + DASH
					+ siu.getId().getMsUnit().getVUnitName());
			item.setParent(locationList);
		}
		locationList.setSelectedIndex(0);

	}

	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}

	public void getRegistrationSurgery(SurgeryController controller, int type) throws VONEAppException {
		TbMedicalRecord mr = null;
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			mr = mrDao.getMrRegistered(code);

			if(mr == null){
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
					controller.MRNumber.focus();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		else{
			controller.item = controller.MRNumberList.getSelectedItem();
			mr = (TbMedicalRecord)controller.item.getValue();

		}
		
		//reg = AdmissionServiceLocator.getRegistrationManager().getRegistrationByMrId(mr.getNMrId());
		controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		controller.MRNumber.setValue(mr.getVMrCode());
		
		int[] umurSkrg = MedisafeUtil.calculateAge(mr.getMsPatient().getDPatientDob());
		controller.age.setValue(umurSkrg[0]+" thn "+umurSkrg[1]+" bln "+umurSkrg[2]+" hr");
		
		if(mr.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		
		
		if(controller.reg.getMsStaff() != null){
			controller.mainDoctor.setValue(
					controller.reg.getMsStaff().getVStaffCode()+DASH+
					controller.reg.getMsStaff().getVStaffName());
		}
		
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
		if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

			controller.isRanap = true;
			TbBedOccupancy boc;
			boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();
			
			controller.session.setAttribute("kelasTarif", controller.ranapKelas);

			controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
			controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());

		} else {
			controller.isRanap = false;
			controller.bed.setValue("");
			controller.hall.setValue("");
		}

		controller.patient = mr.getMsPatient();
		TbRoomReservation tbRoomReservation = radDao.getTbRoomReservation(controller.reg);
		if (tbRoomReservation != null)
			controller.antriKelas.setText(tbRoomReservation.getMsTreatmentClass().getVTclassDesc());
		else
			controller.antriKelas.setText(null);
		controller.session.setAttribute("mr", mr);
		controller.session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_SURGERY);
		
		
		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);
		
		controller.setFieldDisable(true);
		
	}

	public void getNoteDetailSurgery(SurgeryController controller)
			throws VONEAppException {
		controller.nota = (TbExamination) controller.notaList.getSelectedItem().getValue();
		controller.nota = noteDao.getNote(controller.nota.getNExamId()); //tarik lagi
		
		controller.transactionNumber.setValue(controller.nota.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", controller.nota);

		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(controller.nota.getMsPatient().getNPatientId());

		controller.patientName.setValue(controller.nota.getMsPatient().getVPatientName());

		if (controller.nota.getTbRegistration() != null) {
			if (controller.nota.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(controller.nota.getTbRegistration().getMsStaff()
						.getVStaffName());
		}

		int[] age = MedisafeUtil.calculateAge(controller.nota.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));

		if (mr != null)
			controller.MRNumber.setValue(mr.getVMrCode());
		if (controller.nota.getTbRegistration() != null)
			controller.registrationNumber.setValue(controller.nota.getTbRegistration()
					.getVRegSecondaryId());

		if (controller.nota.getMsPatient().getVPatientGender().equals("M")) {
			controller.gender.setSelectedIndex(0);
		} else {
			controller.gender.setSelectedIndex(1);
		}
		controller.patient = controller.nota.getMsPatient();
		
		if (controller.reg != null)
			if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

				controller.isRanap = true;
				TbBedOccupancy boc; 
				boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
				
				controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
						.getVTclassDesc();

				controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
				controller.hall.setValue(boc.getId().getMsBed().getMsRoom()
								.getVRoomName());

			} else {
				controller.isRanap = false;
				controller.bed.setValue("");
				controller.hall.setValue("");
			}


		// AMBIL DETIL NOTA
		ambilDetailNota(controller.nota, controller.surgeryList);
		
		if (controller.nota.getTotal() != null)
			controller.total.setValue(new BigDecimal(controller.nota.getTotal().doubleValue()));

		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.nota
				.getNExamStatus().intValue()));

		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		
		if(controller.nota.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			controller.btnModify.setDisabled(true);
			controller.btnValidation.setDisabled(true);
		}

	}

	public void searchTreatmentSurgery(com.vone.medisafe.surgery.TambahTindakanController controller) throws VONEAppException {
		boolean selected = false;
		Set treatments = controller.treatmentList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		controller.treatmentList.getItems().clear();
		
		
		Listitem item;
		for(int i=0; i < listitem.length; i++){
			item = listitem[i];
			item.setParent(controller.treatmentList);
		}
		
			
		//add search result to listbox, if result size is 1, add result to selection
		if(controller.treatmentCode.getText().trim().equals("") && controller.treatmentName.getText().trim().equals("")){
			try {
				Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			controller.treatmentCode.focus();
			return;
		}
		
		List list = treatDao.getSearchTreatmentByUnit(null, 
				"%"+
				controller.treatmentCode.getText()+"%", "%"+
				controller.treatmentName.getText()+"%", 
				controller.kelasTarif, 
				MedisafeConstants.NON_PAKET);

		
		Listcell cell;
		if(list.size() == 1){
			
			controller.tfee = (MsTreatmentFee)list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				if(((MsTreatmentFee)item.getValue()).getNTreatmentFeeId().equals(controller.tfee.getNTreatmentFeeId()))
					return;
			}
			item = new Listitem();
			item.setValue(controller.tfee);
			item.setParent(controller.treatmentList);
			
			cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			controller.decbox.setValue(new BigDecimal(controller.tfee.getNTrtfeeFee()));
			cell = new Listcell(controller.decbox.getText());
			cell.setParent(item);
			
			controller.treatmentList.addItemToSelection(item);
			return;
			
		}
		
		it = list.iterator();
		while(it.hasNext()){
			
			controller.tfee = (MsTreatmentFee)it.next();
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				if(((MsTreatmentFee)item.getValue()).getNTreatmentFeeId().equals(controller.tfee.getNTreatmentFeeId()))
				{
					selected = true;
					break;
				}else selected = false;
					
			}
			if(selected){
				//do nothing
			}else{
				item = new Listitem();
				item.setValue(controller.tfee);
				item.setParent(controller.treatmentList);
				
				cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentCode());
				cell.setParent(item);
				
				cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentName());
				cell.setParent(item);
				
				controller.decbox.setValue(new BigDecimal(controller.tfee.getNTrtfeeFee()));
				cell = new Listcell(controller.decbox.getText());
				cell.setParent(item);
				
				selected = false;
				
			}
				
		}
	}

	public void searchTreatment(com.vone.medisafe.radiology.TambahTindakanController controller) throws VONEAppException {
		boolean selected = false;
		Set treatments = controller.treatmentList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		controller.decbox.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		controller.treatmentList.getItems().clear();
		
		
		for(int i=0; i < listitem.length; i++){
			controller.item = listitem[i];
			controller.item.setParent(controller.treatmentList);
		}
		
			
		//add search result to listbox, if result size is 1, add result to selection
		if(controller.treatmentCode.getText().trim().equals("") && controller.treatmentName.getText().trim().equals("")){
			try {
				Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			controller.treatmentCode.focus();
			return;
		}
		List list = treatDao.getSearchTreatmentByUnit(controller.unit.getNUnitId(), 
				"%"+
				controller.treatmentCode.getText()+"%", "%"+
				controller.treatmentName.getText()+"%", 
				controller.kelasTarif, 
				MedisafeConstants.NON_PAKET);
		
		if(list.size() == 1){
			
			controller.tfee = (MsTreatmentFee)list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				controller.item = listitem[i];
				if(((MsTreatmentFee)controller.item.getValue()).getNTreatmentFeeId().equals(controller.tfee.getNTreatmentFeeId()))
					return;
			}
			controller.item = new Listitem();
			controller.item.setValue(controller.tfee);
			controller.item.setParent(controller.treatmentList);
			
			controller.cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentCode());
			controller.cell.setParent(controller.item);
			
			controller.cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentName());
			controller.cell.setParent(controller.item);
			
			controller.decbox.setValue(new BigDecimal(controller.tfee.getNTrtfeeFee()));
			controller.cell = new Listcell(controller.decbox.getText());
			controller.cell.setParent(controller.item);
			
			controller.treatmentList.addItemToSelection(controller.item);
			MiscTrxController.setFont(controller.treatmentList);
			return;
			
		}
		
		it = list.iterator();
		while(it.hasNext()){
			
			controller.tfee = (MsTreatmentFee)it.next();
			for(int i=0; i < listitem.length; i++){
				controller.item = listitem[i];
				if(((MsTreatmentFee)controller.item.getValue()).getNTreatmentFeeId().equals(controller.tfee.getNTreatmentFeeId()))
				{
					selected = true;
					break;
				}else selected = false;
					
			}
			if(selected){
				//do nothing
			}else{
				controller.item = new Listitem();
				controller.item.setValue(controller.tfee);
				controller.item.setParent(controller.treatmentList);
				
				controller.cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentCode());
				controller.cell.setParent(controller.item);
				
				controller.cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentName());
				controller.cell.setParent(controller.item);
				
				controller.decbox.setValue(new BigDecimal(controller.tfee.getNTrtfeeFee()));
				controller.cell = new Listcell(controller.decbox.getText());
				controller.cell.setParent(controller.item);
				
				selected = false;
				
			}
				
		}
		MiscTrxController.setFont(controller.treatmentList);
		

	}

	public void getRegistrationRenal(RenalUnitController controller, int type) throws VONEAppException {
		TbMedicalRecord mr = null;
		if (type == MedisafeConstants.INPUT_BY_MANUAL) {
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);

			mr = mrDao.getMrRegistered(code);
			if (mr == null) {
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
					controller.MRNumber.focus();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
		} else {
			controller.item = controller.MRNumberList.getSelectedItem();
			mr = (TbMedicalRecord) controller.item.getValue();
		}

		controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.dateOfBirth.setValue(mr.getMsPatient().getDPatientDob());
		controller.address.setValue(mr.getMsPatient().getVPatientMainAddr());

		int[] umurSkrg = MedisafeUtil.calculateAge(mr.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(umurSkrg[0] + " thn " + umurSkrg[1] + " bln "
				+ umurSkrg[2] + " hr");

		if (mr.getMsPatient().getVPatientGender().equals("M")) {
			controller.gender.setSelectedIndex(0);
		} else {
			controller.gender.setSelectedIndex(1);
		}

		if (mr.getMsPatient().getMsPatientType() != null) {
			for (int i = 1; i < controller.patientTypeList.getItems().size(); i++) {
				if (((MsPatientType) controller.patientTypeList.getItemAtIndex(i)
						.getValue()).getNPatientTypeId().equals(
						mr.getMsPatient().getMsPatientType()
								.getNPatientTypeId()))
					controller.patientTypeList.setSelectedItem(controller.patientTypeList
							.getItemAtIndex(i));
			}
		}
		
		if (controller.reg == null){
			try {
				Messagebox.show("REG ERROR");
				controller.getNewTransaction();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (controller.reg.getMsStaff() != null) {
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode() + DASH
					+ controller.reg.getMsStaff().getVStaffName());
		}

		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());

		controller.referencePatient.setDisabled(true);
		if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

			controller.isRanap = true;

			TbBedOccupancy boc; 
			boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());

			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();

			controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
			controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());

		} else {
			controller.isRanap = false;
			controller.bed.setValue("");
			controller.hall.setValue("");
		}

		controller.patient = mr.getMsPatient();
		controller.session.setAttribute("mr", mr);
		controller.session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_RENAL);
		controller.session.setAttribute("location", controller.locationList);

		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);

		controller.setFieldDisable(true);

	}

	public void getNoteDetail(RenalUnitController controller)
			throws VONEAppException {
		TbExamination note = (TbExamination) controller.notaList.getSelectedItem()
				.getValue();
		
		note = noteDao.getNote(note.getNExamId());
		
		controller.nota = note;

		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);

		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(note.getMsPatient().getNPatientId());

		controller.patientName.setValue(note.getMsPatient().getVPatientName());
		controller.address.setValue(note.getMsPatient().getVPatientMainAddr());
		controller.dateOfBirth.setValue(note.getMsPatient().getDPatientDob());

		if (note.getTbRegistration() != null) {
			if (note.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(note.getTbRegistration().getMsStaff()
						.getVStaffName());
		}

		int[] age = MedisafeUtil.calculateAge(note.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));

		if (note.getMsPatient().getMsPatientType() != null) {
			for (int i = 1; i < controller.patientTypeList.getItems().size(); i++) {
				if (((MsPatientType) controller.patientTypeList.getItemAtIndex(i)
						.getValue()).getNPatientTypeId().equals(
						note.getMsPatient().getMsPatientType()
								.getNPatientTypeId()))
					controller.patientTypeList.setSelectedItem(controller.patientTypeList
							.getItemAtIndex(i));
				// this.patientTypeList.setSelectedIndex(i);
			}
		}

		if (note.getMsPatientEscort() != null) {
			for (int i = 1; i < controller.patientEscort.getItems().size(); i++) {
				if (((MsPatientEscort) controller.patientEscort.getItemAtIndex(i)
						.getValue()).getNEscortPrimaryId().equals(
						note.getMsPatientEscort().getNEscortPrimaryId()))
					controller.patientEscort.setSelectedItem(controller.patientEscort
							.getItemAtIndex(i));
				// this.patientTypeList.setSelectedIndex(i);
			}
		}

		if (mr != null)
			controller.MRNumber.setValue(mr.getVMrCode());
		if (note.getTbRegistration() != null)
			controller.registrationNumber.setValue(note.getTbRegistration()
					.getVRegSecondaryId());

		if (note.getMsPatient().getVPatientGender().equals("M")) {
			controller.gender.setSelectedIndex(0);
		} else {
			controller.gender.setSelectedIndex(1);
		}
		controller.patient = note.getMsPatient();

		if (mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());

		if (controller.reg != null)
			if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

				controller.isRanap = true;
				
				TbBedOccupancy boc; 
				boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());

				controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
						.getVTclassDesc();

				controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
				controller.hall
						.setValue(boc.getId().getMsBed().getMsRoom()
								.getVRoomName());

			} else {
				controller.isRanap = false;
				controller.bed.setValue("");
				controller.hall.setValue("");
			}

		// AMBIL DETIL NOTA
		ambilDetailNota(controller.nota, controller.renalUnitList);


		if (note.getTotal() != null)
			controller.total.setValue(new BigDecimal(note.getTotal().doubleValue()));

		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(note
				.getNExamStatus().intValue()));

		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		
		if(note.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			controller.btnValidation.setDisabled(true);
			controller.btnModify.setDisabled(true);
			
		}

	}

	public void searchTreatmentRenal(TambahTindakanController controller) throws VONEAppException {
		controller.decbox.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		boolean selected = false;
		Set treatments = controller.treatmentList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		controller.treatmentList.getItems().clear();
		
		
		Listitem item;
		for(int i=0; i < listitem.length; i++){
			item = listitem[i];
			item.setParent(controller.treatmentList);
		}
		
			
		//add search result to listbox, if result size is 1, add result to selection
		if(controller.treatmentCode.getText().trim().equals("") && controller.treatmentName.getText().trim().equals("")){
			try {
				Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			controller.treatmentCode.focus();
			return;
		}
		
		List list = treatDao.getSearchTreatmentByUnit(controller.unit.getNUnitId(), 
				"%"+
				controller.treatmentCode.getText()+"%", "%"+
				controller.treatmentName.getText()+"%", 
				controller.kelasTarif, 
				MedisafeConstants.NON_PAKET);
		
		MsTreatmentFee tfee;
		Listcell cell;
		if(list.size() == 1){
			
			tfee = (MsTreatmentFee)list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				if(((MsTreatmentFee)item.getValue()).getNTreatmentFeeId().equals(tfee.getNTreatmentFeeId()))
					return;
			}
			item = new Listitem();
			item.setValue(tfee);
			item.setParent(controller.treatmentList);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			controller.decbox.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			cell = new Listcell(controller.decbox.getText());
			cell.setParent(item);
			
			controller.treatmentList.addItemToSelection(item);
			return;
			
		}
		
		it = list.iterator();
		while(it.hasNext()){
			
			tfee = (MsTreatmentFee)it.next();
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				if(((MsTreatmentFee)item.getValue()).getNTreatmentFeeId().equals(tfee.getNTreatmentFeeId()))
				{
					selected = true;
					break;
				}else selected = false;
					
			}
			if(selected){
				//do nothing
			}else{
				item = new Listitem();
				item.setValue(tfee);
				item.setParent(controller.treatmentList);
				
				cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
				cell.setParent(item);
				
				cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
				cell.setParent(item);
				
				controller.decbox.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
				cell = new Listcell(controller.decbox.getText());
				cell.setParent(item);
				
				selected = false;
				
			}
				
		}
		

	}

	public void getRegistrationCafe(CafeDietController controller, int type) throws VONEAppException {
		TbMedicalRecord mr = null;
		if (type == MedisafeConstants.INPUT_BY_MANUAL) {
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);

			mr = mrDao.getMrRegistered(code);
			
			if (mr == null) {
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
					controller.MRNumber.focus();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
		} else {
			controller.item = controller.MRNumberList.getSelectedItem();
			mr = (TbMedicalRecord) controller.item.getValue();
		}

		controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		// if (reg == null){
		// Messagebox.show("REGISTRASI TIDAK AKTIF");
		// return;
		// }
		controller.MRNumber.setValue(mr.getVMrCode());

		if (controller.reg == null){
			try {
				Messagebox.show("REG ERROR");
				controller.getNewTransaction();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());

		controller.referencePatient.setDisabled(true);
		if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

			controller.isRanap = true;
			
			TbBedOccupancy boc; 
			boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());

			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();


		} else {
			controller.isRanap = false;
		}

		controller.patient = mr.getMsPatient();
		controller.session.setAttribute("mr", mr);
//		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,
//				MedisafeConstants.COMMON_POLY);
		controller.session.setAttribute("location", controller.locationList);

		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);

		controller.setFieldDisable(true);
		// NGESET kelasTarif disini...
		// masukkan ke session

	}

	public void getNoteDetailCafe(CafeDietController controller)
			throws VONEAppException {
		TbExamination note = (TbExamination) controller.notaList.getSelectedItem()
				.getValue();
		
		note = noteDao.getNote(note.getNExamId());
		
		controller.nota = note;

		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);

		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(note.getMsPatient().getNPatientId());

		controller.patientName.setValue(note.getMsPatient().getVPatientName());

		if (mr != null)
			controller.MRNumber.setValue(mr.getVMrCode());
		if (note.getTbRegistration() != null)
			controller.registrationNumber.setValue(note.getTbRegistration()
					.getVRegSecondaryId());

		controller.patient = note.getMsPatient();

		if (mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());

		if (controller.reg != null)
			if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

				controller.isRanap = true;
				
				TbBedOccupancy boc; 
				boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());

				controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
						.getVTclassDesc();

			} else {
				controller.isRanap = false;
			}

		// AMBIL DETIL NOTA
		ambilDetailNota(controller.nota, controller.cafeDietList);

		if (note.getTotal() != null)
			controller.total.setValue(new BigDecimal(note.getTotal().doubleValue()));

		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(note
				.getNExamStatus().intValue()));

		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		
		if(note.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			controller.btnValidation.setDisabled(true);
			controller.btnModify.setDisabled(true);
			
		}

	}

	public void getRegistrationFisio(FisioterapiController controller, int type) throws VONEAppException {
		TbMedicalRecord mr = null;
		if (type == MedisafeConstants.INPUT_BY_MANUAL) {
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);

			mr = mrDao.getMrRegistered(code);
			if (mr == null) {
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
					controller.MRNumber.focus();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
		} else {
			controller.item = controller.MRNumberList.getSelectedItem();
			mr = (TbMedicalRecord) controller.item.getValue();
		}

		controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		// if (reg == null){
		// Messagebox.show("REGISTRASI TIDAK AKTIF");
		// return;
		// }
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.dateOfBirth.setValue(mr.getMsPatient().getDPatientDob());
		controller.address.setValue(mr.getMsPatient().getVPatientMainAddr());

		int[] umurSkrg = MedisafeUtil.calculateAge(mr.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(umurSkrg[0] + " thn " + umurSkrg[1] + " bln "
				+ umurSkrg[2] + " hr");

		if (mr.getMsPatient().getVPatientGender().equals("M")) {
			controller.gender.setSelectedIndex(0);
		} else {
			controller.gender.setSelectedIndex(1);
		}

		if (mr.getMsPatient().getMsPatientType() != null) {
			for (int i = 1; i < controller.patientTypeList.getItems().size(); i++) {
				if (((MsPatientType) controller.patientTypeList.getItemAtIndex(i)
						.getValue()).getNPatientTypeId().equals(
						mr.getMsPatient().getMsPatientType()
								.getNPatientTypeId()))
					controller.patientTypeList.setSelectedItem(controller.patientTypeList
							.getItemAtIndex(i));
			}
		}
		
		if (controller.reg == null){
			try {
				Messagebox.show("REG ERROR");
				controller.getNewTransaction();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (controller.reg.getMsStaff() != null) {
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode() + DASH
					+ controller.reg.getMsStaff().getVStaffName());
		}

		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());

		controller.referencePatient.setDisabled(true);
		if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

			controller.isRanap = true;
			
			TbBedOccupancy boc; 
			boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());

			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();

			controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
			controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());

		} else {
			controller.isRanap = false;
			controller.bed.setValue("");
			controller.hall.setValue("");
		}

		controller.patient = mr.getMsPatient();
		controller.session.setAttribute("mr", mr);
		controller.session.setAttribute(MedisafeConstants.PATIENT_HISTROY,
				MedisafeConstants.COMMON_FISIOTERAPI);
		controller.session.setAttribute("location", controller.locationList);

		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);

		controller.setFieldDisable(true);

	}

	public void getNoteDetailFisio(FisioterapiController controller)
			throws VONEAppException {
		//todo fisio
		TbExamination note = (TbExamination)controller.notaList.getSelectedItem()
				.getValue();
		note = noteDao.getNote(note.getNExamId());
		controller.nota = note;

		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);

		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(controller.nota.getMsPatient().getNPatientId());
		
		controller.patientName.setValue(note.getMsPatient().getVPatientName());
		controller.address.setValue(note.getMsPatient().getVPatientMainAddr());
		controller.dateOfBirth.setValue(note.getMsPatient().getDPatientDob());

		if (note.getTbRegistration() != null) {
			if (note.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(note.getTbRegistration().getMsStaff()
						.getVStaffName());
		}

		int[] age = MedisafeUtil.calculateAge(note.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));

		if (note.getMsPatient().getMsPatientType() != null) {
			for (int i = 1; i < controller.patientTypeList.getItems().size(); i++) {
				if (((MsPatientType) controller.patientTypeList.getItemAtIndex(i)
						.getValue()).getNPatientTypeId().equals(
						note.getMsPatient().getMsPatientType()
								.getNPatientTypeId()))
					controller.patientTypeList.setSelectedItem(controller.patientTypeList
							.getItemAtIndex(i));
				// this.patientTypeList.setSelectedIndex(i);
			}
		}

		if (note.getMsPatientEscort() != null) {
			for (int i = 1; i < controller.patientEscortList.getItems().size(); i++) {
				if (((MsPatientEscort) controller.patientEscortList.getItemAtIndex(i)
						.getValue()).getNEscortPrimaryId().equals(
						note.getMsPatientEscort().getNEscortPrimaryId()))
					controller.patientEscortList
							.setSelectedItem(controller.patientEscortList
									.getItemAtIndex(i));
				// this.patientTypeList.setSelectedIndex(i);
			}
		}

		if (mr != null)
			controller.MRNumber.setValue(mr.getVMrCode());
		if (note.getTbRegistration() != null)
			controller.registrationNumber.setValue(note.getTbRegistration()
					.getVRegSecondaryId());

		if (note.getMsPatient().getVPatientGender().equals("M")) {
			controller.gender.setSelectedIndex(0);
		} else {
			controller.gender.setSelectedIndex(1);
		}
		controller.patient = note.getMsPatient();

		if (mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());

		if (controller.reg != null)
			if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

				controller.isRanap = true;
				
				TbBedOccupancy boc; 
				boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());

				controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
						.getVTclassDesc();

				controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
				controller.hall
						.setValue(boc.getId().getMsBed().getMsRoom()
								.getVRoomName());

			} else {
				controller.isRanap = false;
				controller.bed.setValue("");
				controller.hall.setValue("");
			}

		// AMBIL DETIL NOTA
		ambilDetailNota(controller.nota, controller.physiotherapyList);

		if (note.getTotal() != null)
			controller.total.setValue(new BigDecimal(note.getTotal().doubleValue()));

		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(note
				.getNExamStatus().intValue()));

		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		
		if(note.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			controller.btnValidation.setDisabled(true);
			controller.btnModify.setDisabled(true);
		}

	}

	public void getRegistrationKlinik(KlinikGiziController controller, int type) throws VONEAppException {
		TbMedicalRecord mr = null;
		if (type == MedisafeConstants.INPUT_BY_MANUAL) {
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);

			mr = AdmissionServiceLocator.getMedicalRecordManager()
					.getMrRegistered(code);
			if (mr == null) {
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
					controller.MRNumber.focus();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		} else {
			controller.item = controller.MRNumberList.getSelectedItem();
			mr = (TbMedicalRecord) controller.item.getValue();
		}

		controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());

		controller.MRNumber.setValue(mr.getVMrCode());

		if (controller.reg == null){
			try {
				Messagebox.show("REG ERROR");
				controller.getNewTransaction();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
		if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

			controller.isRanap = true;
			
			TbBedOccupancy boc; 
			boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();

			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
					.getVTclassDesc();

			controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
			controller.hall.setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());

		} else {
			controller.isRanap = false;
			controller.bed.setValue("");
			controller.hall.setValue("");
		}


		controller.patient = mr.getMsPatient();
		controller.session.setAttribute("mr", mr);
//		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,
//				MedisafeConstants.COMMON_POLY);
		controller.session.setAttribute("location", controller.locationList);

		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);

		controller.setFieldDisable(true);
		// NGESET kelasTarif disini...
		// masukkan ke session
	}
	/**
	 *
	 * 
	 */
	
	public void getNoteDetailKlinik(KlinikGiziController controller)
			throws VONEAppException {
		TbExamination note = (TbExamination) controller.notaList.getSelectedItem()
				.getValue();
		
		/**
		 * 
		 * ambil ulang note dari db, biar terhubung dgn session
		 * 
		 */ 
		
		note = noteDao.getNote(note.getNExamId());
		controller.nota = note;

		controller.transactionNumber.setValue(note.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", note);

		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(controller.nota.getMsPatient().getNPatientId());

		controller.patientName.setValue(note.getMsPatient().getVPatientName());

		if (mr != null)
			controller.MRNumber.setValue(mr.getVMrCode());
		if (note.getTbRegistration() != null)
			controller.registrationNumber.setValue(note.getTbRegistration()
					.getVRegSecondaryId());

		controller.patient = note.getMsPatient();

		if (mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(mr.getNMrId());

		if (controller.reg != null)
			if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

				controller.isRanap = true;
				
				TbBedOccupancy boc; 
				boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
				
				controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass()
						.getVTclassDesc();

				controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
				controller.hall.setValue(boc.getId().getMsBed().getMsRoom()
								.getVRoomName());

			} else {
				controller.isRanap = false;
				controller.bed.setValue("");
				controller.hall.setValue("");
			}


		// AMBIL DETIL NOTA
		ambilDetailNota(controller.nota, controller.clinicList);



		if (note.getTotal() != null)
			controller.total.setValue(new BigDecimal(note.getTotal().doubleValue()));

		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(note
				.getNExamStatus().intValue()));

		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);
		
		if(note.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			controller.btnValidation.setDisabled(true);
			controller.btnModify.setDisabled(true);
			
		}
	}
	
	/**
	 * 
	 * ambil detail nota lalu masukkan ke listbox
	 * 
	 */
	public void ambilDetailNota(TbExamination nota, Listbox theList) throws VONEAppException {
		// todo semua taruh disini
		// nota tarik lagi?? tdk perlu, sdh ditarik dari atas
		
		theList.getItems().clear();
		//MsLabTestDetail ltd;
		
		Decimalbox db = new Decimalbox();
		db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		db.setHeight(HEIGHT_14PX);
		db.setStyle(FONT_SIZE_9PT);

		//TREATMENT
		Set treatmentTrxList = nota.getTbTreatmentTrx(); //Service.getLaboratManager().getTreatmentTrx(nota);
		Iterator it = treatmentTrxList.iterator();

		while (it.hasNext()) {
			TbTreatmentTrx treatmentTrx = (TbTreatmentTrx) it.next();

			Listitem item = new Listitem();
			item.setValue(treatmentTrx);
			item.setParent(theList);

			// tratment code
			Listcell cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(treatmentTrx.getMsTreatmentFee().getMsTreatment()
					.getVTreatmentCode());

			// treatment name
			// ambil radiografer-nya
			MsStaff radiografer = treatmentTrx.getAnastesiStaff();
			MsStaff dokter = treatmentTrx.getMsDoctor();
			
			String ket = treatmentTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName();
			if(dokter != null)
				ket = ket + " - " + dokter.getVStaffName();
			if(radiografer != null)
				ket = ket + " - " + radiografer.getVStaffName();
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(ket);	

//			treatment quantity
			cell = new Listcell();
			Intbox jumlah = new Intbox();
			jumlah.setHeight(HEIGHT_14PX);
			jumlah.setStyle(FONT_SIZE_9PT);
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(treatmentTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);

			// satuan
			cell = new Listcell(DASH);
			//ltd = Service.getLaboratManager().getLabTestDetail(treatmentTrx.getNTreatmentId()); 
//			ltd = getLabTestDetail(treatmentTrx.getNTreatmentId());
//			ltd = lab
//			if(ltd != null){
//				cell = new Listcell(ltd.getVLabTestQuantify());
//			}
			cell.setParent(item);

			db.setValue(new BigDecimal(treatmentTrx.getNAmountTrx()/treatmentTrx.getNQty()));
			// treatment price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());

			//discount
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth(PERSEN_50);
			diskon.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			diskon.setHeight(HEIGHT_14PX);
			diskon.setStyle(FONT_SIZE_9PT);
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(treatmentTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth(PERSEN_40);
			diskonList.setMold(SELECT);
			diskonList.setHeight(HEIGHT_14PX);
			diskonList.setStyle(FONT_SIZE_9PT);
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel(SELECT_RP);
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel(SELECT_PERSEN);
			listitem.setParent(diskonList);
			
			if(treatmentTrx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			

			db.setValue(new BigDecimal(treatmentTrx.getNAmountAfterDisc()));

			// treatment price after discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
		//
		Set itemTrxList = nota.getTbItemTrx();//Service.getPolyclinicManager().getItemTrx(nota);
		it = itemTrxList.iterator();

		// ITEM
		while(it.hasNext()){
			//obj[0] = item id
			//obj[1] = item code
			//ojb[2] = item name
			//obj[3] = item quantify
			//obj[4] = item qty
			//obj[5] = trx value
			//obj[6] = discount value
			//obj[7] = subtotal value
			
			//Object[] obj = (Object[])it.next();
			Object[] obj = new Object[2];
			
			
			TbItemTrx itemTrx = (TbItemTrx) it.next();
			Listitem item = new Listitem();
			//item.setValue(itemTrx.getMsItem());
			obj[0] = itemTrx.getMsItem().getNItemId();
			item.setValue(obj);
			item.setParent(theList);
			
			//item code
			Listcell cell = new Listcell(itemTrx.getMsItem().getVItemCode());
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
//			cell.setLabel(itemTrx.getMsItem().getVItemCode());
			
			
			//item name
			cell = new Listcell(itemTrx.getMsItem().getVItemName());
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);

			
			//item quantity
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			Intbox jumlah = new Intbox();
			jumlah.setHeight(HEIGHT_14PX);
			jumlah.setStyle(FONT_SIZE_9PT);

			jumlah.setDisabled(true);
			jumlah.setValue(itemTrx.getNQty().intValue());
			jumlah.setParent(cell);
			cell.setParent(item);

			
//			satuan
			cell = new Listcell(itemTrx.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);

			
			
			double hargaSatuan = (Double) itemTrx.getNAmountTrx() / itemTrx.getNQty();
			db.setValue(new BigDecimal(hargaSatuan));
			//item price before discount
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
			

			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth(PERSEN_50);
			diskon.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			diskon.setHeight(HEIGHT_14PX);
			diskon.setStyle(FONT_SIZE_9PT);

			diskon.setParent(cell);
			diskon.setValue(new BigDecimal((Double)itemTrx.getNDiscAmount()));
			
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth(PERSEN_40);
			diskonList.setMold(SELECT);
			diskonList.setHeight(HEIGHT_14PX);
			diskonList.setStyle(FONT_SIZE_9PT);
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel(SELECT_RP);
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel(SELECT_PERSEN);
			listitem.setParent(diskonList);
			
			if(itemTrx.getVDiscType().toString().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
//			cell.setLabel();
//			cell.setLabel(db.getText());
			
			

			db.setValue(new BigDecimal((Double)itemTrx.getNAmountAfterDisc()));
			
			//item price after discount			
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
		
		//MISC
		Set miscTrxList = nota.getTbMiscTrx();//Service.getPolyclinicManager().getMiscTrx(nota);
		it = miscTrxList.iterator();
		while(it.hasNext()){
			TbMiscTrx tbMiscTrx = (TbMiscTrx)it.next();
			Listitem item = new Listitem();
			item.setValue(tbMiscTrx);
			item.setParent(theList);
			
			//tratment code
			Listcell cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(MedisafeConstants.MISC_CODE);
			
			//treatment name
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(tbMiscTrx.getVMiscName());
			
			//treatment quantity
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			Intbox jumlah = new Intbox();
			jumlah.setHeight(HEIGHT_14PX);
			jumlah.setStyle(FONT_SIZE_9PT);

			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(tbMiscTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);
			
			//satuan
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(DASH);
			
			double hargaSatuan = tbMiscTrx.getNAmountTrx() / tbMiscTrx.getNQty();

			db.setValue(new BigDecimal(hargaSatuan));
			//treatment price before discount
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			
			//discount
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth(PERSEN_50);
			diskon.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			diskon.setStyle(FONT_SIZE_9PT);
			diskon.setHeight(HEIGHT_14PX);
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(tbMiscTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth(PERSEN_40);
			diskonList.setMold(SELECT);
			diskonList.setHeight(HEIGHT_14PX);
			diskonList.setStyle(FONT_SIZE_9PT);
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel(SELECT_RP);
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel(SELECT_PERSEN);
			listitem.setParent(diskonList);
			
			if(tbMiscTrx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			
			db.setValue(new BigDecimal(tbMiscTrx.getNAmountAfterDisc()));
			
			//treatment price after discount			
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
		}

		
	}

	public void getKelasForSelect(Listbox tclassList) throws VONEAppException {
		List list = radDao.getKelasForSelect();
		tclassList.getItems().clear();
		Listitem item;
		Iterator it = list.iterator();
		MsTreatmentClass msTreatmentClass;
		while (it.hasNext()) {
			msTreatmentClass = (MsTreatmentClass) it.next();
			item = new Listitem();
			item.setValue(msTreatmentClass);
			item.setLabel(msTreatmentClass.getVTclassDesc());
			item.setParent(tclassList);
		}

	}
}

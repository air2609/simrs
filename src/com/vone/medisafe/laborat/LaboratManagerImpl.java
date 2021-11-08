package com.vone.medisafe.laborat;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.apotik.ApotikDAO;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.DiscontListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbLaboratoryResult;
import com.vone.medisafe.mapping.TbLaboratoryResultDetail;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.mapping.dao.PolyclinicDAO;
import com.vone.medisafe.mapping.dao.treatment.MsTreatmentDAO;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;

public class LaboratManagerImpl implements LaboratManager{
	
	private static final String KURUNG_TUTUP = " )";
	private static final String KURUNG_BUKA = "( ";
	private static final String HEAD_SUBTTL = "SUBTTL";
	private static final String HEAD_QTY = "QTY";
	private static final String HEAD_KET = "KET";
	private static final String HEAD_KODE = "KODE";
	private static final String HEAD_NO = "NO";
	private static final String TOTAL_NOTA = "TOTAL:";
	
	private static final String JENIS  = " Jenis : ";
	private static final String DOKTER = " Dokter: ";
	private static final String UNIT   = " Unit  : ";
	private static final String NOMOR  = " No.   : ";
	
	/*
	private static final String JENIS  = " Jenis    : ";
	private static final String DOKTER = " Dokter   : ";
	private static final String UNIT   = " Unit     : ";
	private static final String NOMOR  = " Tgl Bayar: ";
	*/
	
	private static final String NO_REGISTER    = "No. Register   : ";
	private static final String TGL            = "Tgl            : ";
	private static final String NAMA_PASIEN    = "Nama Pasien    : ";
	private static final String NO_REKAM_MEDIK = "No. Rekam Medik: ";
	private static final String NO_RETUR       = "No. Retur      : ";
	private static final String NO_RESEP       = "No. Resep      : ";
	
	private static final String PRINT_DATE     = "Print date: ";
	
	private static final String SPACE = " ";
	private static final String DOUBLE_STAR = "** ";
	
	private static final String DASH = "-";
	private static final String SATU = "1";
	private static final String TIDAK_ADA_DATA = "TIDAK ADA DATA";
	private static final String SELECT = "select";
	private static final String PERSEN_50 = "50%";
	private static final String PERSEN_40 = "40%";
	private static final String SELECT_PERSEN = "2. %";
	private static final String SELECT_RP = "1. RP";
	private static final String HEIGHT_14PX = "14px";
	
	private LaboratDAO labDao;
	private TbRegistrationDAO regDao;
	private TbMedicalRecordDAO mrDao;
	private TbBedOccupancyDAO bocDao;
	private NoteDAO noteDao;
	private MsTreatmentDAO treatDao;
	private PolyclinicDAO polyDao;
	private ApotikDAO apotikDao;
	
	private String noNota;
	private String tglNota;
	private int brs;
	
	private final short COLUMN_01 = 3;
	private final short COLUMN_02 = 10;
	private final short COLUMN_03 = 30;
	private final short COLUMN_04 = 5;
	private final short COLUMN_05 = 12;
	private final short PAGE_WIDTH = 60;
	private final short COLUMN_HEAD = 17;
	
	public LaboratDAO getLabDao() {
		return labDao;
	}

	public void setLabDao(LaboratDAO dao) {
		this.labDao = dao;
	}

	public PolyclinicDAO getPolyDao() {
		return polyDao;
	}

	public void setPolyDao(PolyclinicDAO polyDao) {
		this.polyDao = polyDao;
	}

	public List getTreatmentTrx(TbExamination note) throws VONEAppException {
		return labDao.getTreatmentTrx(note);
	}

	public boolean save(MsPatient patient, TbExamination nota, Set treatmentTrx, MsUnit unit, boolean isRanap) throws VONEAppException  {
		return labDao.saveTransaction(patient, nota, treatmentTrx, unit, isRanap);
	}

	public List serachRegisteredPatient(String code, String nameP, String addressP) throws VONEAppException {
		return labDao.searchPatientRegistered(code, nameP, addressP);
	}

	public List getTreatment(MsPatient pat) throws VONEAppException {
		return labDao.getTreatment(pat);
	}

	public MsLabTestDetail getLabTestDetail(int code) throws VONEAppException {
		return labDao.getLabTestDetail(code);
	}

	public boolean saveHasilLab(MsPatient patient, TbLaboratoryResult nota, TbExamination exam, Set resultDetailSet, MsUnit unit, boolean isRanap) throws VONEAppException {
		return labDao.saveHasilLab(patient, nota, exam, resultDetailSet, unit, isRanap);
	}

	public TbExamination getExaminationByMr(TbMedicalRecord mr) throws VONEAppException {
		return labDao.getExaminationByMr(mr);
	}

	public List getTreatmentResult(MsPatient pat) throws VONEAppException {
		return labDao.getTreatmentResult(pat);
	}

	public MsTreatment getTreatmentByLrd(TbLaboratoryResultDetail lrd) throws VONEAppException {
		return labDao.getTreatmentByLrd(lrd);
	}

	public TbExamination getTbExaminationById(int id) throws VONEAppException {
		return labDao.getTbExaminationById(id);
	}

	public List searchHasilLab(String code, String name) throws VONEAppException {
		return labDao.searchHasilLab(code, name);
	}

	public List getTbLabResultDetail(int code) throws VONEAppException {
		return labDao.getTbLabResultDetail(code);
	}

	public MsPatient getFreeBuyer() throws VONEAppException {
		return labDao.getFreeBuyer();
	}

	public boolean saveModifyHasilLab(MsPatient patient, TbLaboratoryResult labResult, TbExamination exam, Set<TbLaboratoryResultDetail> labResultDetailSet) throws VONEAppException {
		return labDao.saveModifyHasilLab(patient, labResult, exam, labResultDetailSet);
	}

	public void getRegistration(LaboratController controller, int type) throws VONEAppException {
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

		if (controller.reg.getMsStaff() != null) {
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode() + DASH
					+ controller.reg.getMsStaff().getVStaffName());
			controller.dokterId.setValue(controller.reg.getMsStaff().getVStaffCode() + DASH
					+ controller.reg.getMsStaff().getVStaffName());
			controller.dokterId.setAttribute("doctor",controller.reg.getMsStaff());
		}

		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());

		controller.referencePatient.setDisabled(true);
		if (controller.reg.getVRegSecondaryId().substring(0, 1).equals("I")) {

			controller.isRanap = true;
			TbBedOccupancy boc; 
			boc = bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
			if(boc.getId().getMsBed().getMsTreatmentClass() != null)
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
				MedisafeConstants.COMMON_POLY);
		controller.session.setAttribute("location", controller.locationList);

		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);

		controller.setFieldDisable(true);
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

	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}

	public void getNoteDetail(LaboratController controller) throws VONEAppException {
		TbExamination selectedNote = (TbExamination) controller.notaList.getSelectedItem()
				.getValue();
		controller.nota = selectedNote;
		controller.nota = noteDao.getNote(selectedNote.getNExamId());
		

		controller.transactionNumber.setValue(controller.nota.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", controller.nota);

		TbMedicalRecord mr;
		
		mr = mrDao.getMedicalRecordByPaitentId(controller.nota.getMsPatient().getNPatientId());

		controller.patientName.setValue(controller.nota.getMsPatient().getVPatientName());
		controller.address.setValue(controller.nota.getMsPatient().getVPatientMainAddr());
		controller.dateOfBirth.setValue(controller.nota.getMsPatient().getDPatientDob());

		if (controller.nota.getTbRegistration() != null) {
			if (controller.nota.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(controller.nota.getTbRegistration()
						.getMsStaff().getVStaffName());
		}

		int[] age = MedisafeUtil.calculateAge(controller.nota.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));

		if (controller.nota.getMsPatient().getMsPatientType() != null) {
			for (int i = 1; i < controller.patientTypeList.getItems().size(); i++) {
				if (((MsPatientType) controller.patientTypeList.getItemAtIndex(
						i).getValue()).getNPatientTypeId().equals(
								controller.nota.getMsPatient().getMsPatientType()
								.getNPatientTypeId()))
					controller.patientTypeList
							.setSelectedItem(controller.patientTypeList
									.getItemAtIndex(i));
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
		ambilDetailNota(controller.nota, controller.laboratoryList);
		
		if (controller.nota.getTotal() != null)
			controller.total.setValue(new BigDecimal(controller.nota.getTotal().doubleValue()));

		controller.statusNota.setVisible(true);
		controller.labelStatus.setVisible(true);
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.nota
				.getNExamStatus().intValue()));

		controller.setFieldDisable(true);
		controller.setButtonActive(false);
		controller.setInnerButtonDisable(true);

	}

	public void ambilDetailNota(TbExamination nota, Listbox theList) throws VONEAppException {
		theList.getItems().clear();
		Set treatmentTrxList = nota.getTbTreatmentTrx();
		Iterator it = treatmentTrxList.iterator();
		MsLabTestDetail ltd;
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
			
			//todo masukkan group treatment juga
			String ket = treatmentTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName()
			+ " - " 
			+ treatmentTrx.getMsTreatmentFee().getMsTreatment().getMsTreatmentGroup().getVTgroupName();
			
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
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(treatmentTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);

			// satuan
			cell = new Listcell(DASH);
			//ltd = Service.getLaboratManager().getLabTestDetail(treatmentTrx.getNTreatmentId()); 
			ltd = getLabTestDetail(treatmentTrx.getNTreatmentId());
			if(ltd != null){
				cell = new Listcell(ltd.getVLabTestQuantify());
			}
			cell.setParent(item);

			Decimalbox db = new Decimalbox();
			db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			db.setValue(new BigDecimal(treatmentTrx.getNAmountTrx()));
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
			diskon.setStyle(MedisafeConstants.FONT_SIZE_8PT);
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(treatmentTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold(SELECT);
			diskonList.setStyle("font-size:9pt");
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
			jumlah.setDisabled(true);
			jumlah.setValue(itemTrx.getNQty().intValue());
			jumlah.setParent(cell);
			cell.setParent(item);

			
//			satuan
			cell = new Listcell(itemTrx.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);

			
			
			double hargaSatuan = (Double) itemTrx.getNAmountTrx() / itemTrx.getNQty();
			Decimalbox db  = new Decimalbox();
			db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
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
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal((Double)itemTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold(SELECT);
			diskonList.setStyle("font-size:9pt");
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

			Decimalbox db = new Decimalbox();
			db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
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
			diskon.setStyle(MedisafeConstants.FONT_SIZE_8PT);
			diskon.setHeight(HEIGHT_14PX);
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(tbMiscTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold(SELECT);
			diskonList.setStyle("font-size:9pt");
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

	public void searchNote(LaboratController controller) throws VONEAppException {
		controller.notaList.getItems().clear();

		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);
		
		Object obj = controller.locationList.getSelectedItem().getValue();
		MsUnit unit = (MsUnit) obj;

//		List result = Service.getNotaManager().searchNote( 
//				"%" + controller.searchNota.getValue() + "%",
//				"%" + controller.searchName.getValue() + "%", unit);
		
		List result = noteDao.searchNote(
				"%" + controller.searchNota.getValue() + "%",
				"%" + controller.searchName.getValue() + "%", unit, MedisafeConstants.ACTIVE_NOTE);

		Listitem item;
		Listcell cell;
		if (result == null) {
			try {
				Messagebox.show(TIDAK_ADA_DATA);
			} catch (InterruptedException e) {
			}
			return;
		}
		Iterator it = result.iterator();
		while (it.hasNext()) {
			TbExamination note = (TbExamination) it.next();
			item = new Listitem();
			item.setValue(note);
			item.setParent(controller.notaList);

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(note.getVNoteNo());

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(note.getMsPatient().getVPatientName());

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(MedisafeUtil.getNoteStatus(note.getNExamStatus()
					.intValue()));

		}
	}

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}

	public void getItemsFromList(PanelLabController controller, String list) throws VONEAppException {
		Listbox theList = (Listbox)controller.session.getAttribute(list);
		
		Listitem listitem;
		Listcell listcell;
		Label harga;
		Iterator it = theList.getSelectedItems().iterator();
		MsLabTestDetail ltd;
		int i = 0;
		while(it.hasNext()){
			i++;
			Listitem item = (Listitem)it.next();
			listitem = new Listitem();
			MsTreatmentFee tfee = (MsTreatmentFee)item.getValue();
			tfee = labDao.getMsTreatmentFee(tfee); //RELOAD FROM DB
			listitem.setValue(tfee);
			listitem.setParent(controller.listbox);
			//KODE
			listcell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			listcell.setParent(listitem);
			//KET
			listcell = new Listcell(
					tfee.getMsTreatment().getVTreatmentName()
					+ " - " 
					+ tfee.getMsTreatment().getMsTreatmentGroup().getVTgroupName() //todo lazy sdh tdk gagal
					);
			listcell.setParent(listitem);
			//QTY
			listcell = new Listcell(SATU);
			listcell.setParent(listitem);
			//SATUAN
			listcell = new Listcell(DASH);
			
			ltd = getLabTestDetail(tfee.getMsTreatment().getNTreatmentId()); 
			if(ltd != null){
				listcell = new Listcell(ltd.getVLabTestQuantify());
			}
			listcell.setParent(listitem);
			
			//HARGA
			Decimalbox dbharga = new Decimalbox();
			dbharga.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			dbharga.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			//DISKON
			Listcell diskon  = new Listcell();
			diskon.setParent(listitem);
			Decimalbox decimalbox = new Decimalbox();
			decimalbox.setValue(new BigDecimal(0));
			decimalbox.setParent(diskon);
			decimalbox.setWidth(PERSEN_50);
			decimalbox.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			decimalbox.setHeight(HEIGHT_14PX);
			Listbox diskonList = new Listbox();
			diskonList.setParent(diskon);
			diskonList.setWidth("42%");
			diskonList.setMold(SELECT);
			diskonList.setStyle("font-size:9pt");
			item = new Listitem();
			item.setValue(MedisafeConstants.RP);
			item.setSelected(true);
			item.setLabel(SELECT_RP);
			item.setParent(diskonList);
			item = new Listitem();
			item.setValue(MedisafeConstants.PERCENT);
			item.setLabel(SELECT_PERSEN);
			item.setParent(diskonList);
			
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Object[] obj = new Object[]{dbharga.getValue(),dbharga.getValue(),"RP",new Integer(1),decimalbox.getValue()};
			listitem.setAttribute("manipulation", obj);
			
			decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1), new Short("0")));
			diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1), new Short("0")));
			
		}
		if (i>0)
			controller.win.detach();

	}

	public void fillTreatment(KimiaController controller, String code, String kelasTarif) throws VONEAppException {
		List list = treatDao.getTreatmentByTreatmentGroup(code, kelasTarif);
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			MsTreatmentFee tfee = (MsTreatmentFee)it.next();
			Listitem item = new Listitem();
			item.setValue(tfee);
			item.setParent(controller.theList);
			
			Listcell cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
		}
		MiscTrxController.setFont(controller.theList);
	}

	public MsTreatmentDAO getTreatDao() {
		return treatDao;
	}

	public void setTreatDao(MsTreatmentDAO treatDao) {
		this.treatDao = treatDao;
	}

	public void getSelectedTreatment(TambahPemeriksaanController controller) throws VONEAppException {
		if(controller.treatmentList.getSelectedItems().size() == 0){
			controller.treatmentList.focus();
			throw new 
				VONEAppException(MessagesService.getKey("treatment.not.selected"));
		}
		Listitem listitem;
		Listcell listcell;
		Label harga;
		Iterator it = controller.treatmentList.getSelectedItems().iterator();
		MsLabTestDetail ltd;
		while(it.hasNext()){
			Listitem item = (Listitem)it.next();
			listitem = new Listitem();
			MsTreatmentFee tfee = (MsTreatmentFee)item.getValue();
			listitem.setValue(tfee);
			listitem.setParent(controller.listbox);
			
			listcell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			listcell.setParent(listitem);
			
			listcell = new Listcell(
					tfee.getMsTreatment().getVTreatmentName() 
					+ " - " 
					+ tfee.getMsTreatment().getMsTreatmentGroup().getVTgroupName());
			listcell.setParent(listitem);
			
			listcell = new Listcell(SATU);
			listcell.setParent(listitem);
			
			//satuan: 
			
			listcell = new Listcell(DASH);
			
			//ltd = Service.getLaboratManager().getLabTestDetail(tfee.getMsTreatment().getNTreatmentId());
			ltd = noteDao.getLabTestDetail(tfee.getMsTreatment().getNTreatmentId());
			if(ltd != null){
				listcell = new Listcell(ltd.getVLabTestQuantify());
			}
			listcell.setParent(listitem);
			
			
			Decimalbox dbharga = new Decimalbox();
			dbharga.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			dbharga.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Listcell diskon  = new Listcell();
			diskon.setParent(listitem);
			Decimalbox decimalbox = new Decimalbox();
			decimalbox.setValue(new BigDecimal(0));
			decimalbox.setParent(diskon);
			decimalbox.setWidth(PERSEN_50);
			decimalbox.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			decimalbox.setHeight(HEIGHT_14PX);
			Listbox diskonList = new Listbox();
			diskonList.setParent(diskon);
			diskonList.setWidth(PERSEN_40);
			diskonList.setMold(SELECT);
			diskonList.setStyle(MedisafeConstants.FONT_SIZE_8PT);
			item = new Listitem();
			item.setValue(MedisafeConstants.RP);
			item.setSelected(true);
			item.setLabel(SELECT_RP);
			item.setParent(diskonList);
			item = new Listitem();
			item.setValue(MedisafeConstants.PERCENT);
			item.setLabel(SELECT_PERSEN);
			item.setParent(diskonList);
			
//			Label finalPrice = new Label();
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Object[] obj = new Object[]{dbharga.getValue(),dbharga.getValue(),"RP",new Integer(1),decimalbox.getValue()};
			listitem.setAttribute("manipulation", obj);
//			listitem.setAttribute("doctor",(MsStaff)examinerDoctorList.getSelectedItem().getValue());
			
			decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1), new Short("0")));
			diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1), new Short("0")));
		}
		controller.win.detach();
		
	}

	public void searchTreatment(TambahPemeriksaanController controller) throws VONEAppException {
		controller.unit = (MsUnit)controller.location.getSelectedItem().getValue();
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
		//    public List getTreatmentByUnit(MsUnit unit, String tclass, String isPacket){

		//add search result to listbox, if result size is 1, add result to selection
		List list = treatDao.getSearchTreatmentByTreatmentUnit(controller.unit.getNUnitId(),
				"%"+controller.treatmentCode.getText()+"%", 
				"%"+controller.treatmentName.getText()+"%", 
				controller.kelasTarif 
				);
		
		if(list.size() == 0){
			new VONEAppException("NO DATA");
		}
		
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
			
			cell = new Listcell(controller.tfee.getMsTreatment().getMsTreatmentGroup().getVTgroupName());
			cell.setParent(item);
			
			controller.decbox.setValue(new BigDecimal(controller.tfee.getNTrtfeeFee()));
			cell = new Listcell(controller.decbox.getText());
			cell.setParent(item);
			
			controller.treatmentList.addItemToSelection(item);
			controller.treatmentCode.focus();
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
					controller.treatmentCode.focus();
					break;
				}else selected = false;
					
			}
			if(selected){
				//do nothing
				controller.treatmentCode.focus();
			}else{
				item = new Listitem();
				item.setValue(controller.tfee);
				item.setParent(controller.treatmentList);
				
				cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentCode());
				cell.setParent(item);
				
				cell = new Listcell(controller.tfee.getMsTreatment().getVTreatmentName());
				cell.setParent(item);
				
				cell = new Listcell(controller.tfee.getMsTreatment().getMsTreatmentGroup().getVTgroupName());
				cell.setParent(item);
				
				controller.decbox.setValue(new BigDecimal(controller.tfee.getNTrtfeeFee()));
				cell = new Listcell(controller.decbox.getText());
				cell.setParent(item);
				
				selected = false;
				
			}
				
		}
		controller.treatmentCode.focus();
	}

	public Integer getWhoustId(Listbox locationList) throws VONEAppException {
		return ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
	}

	public void getLabDetail(HasilLaboratController controller)
			throws VONEAppException {
		controller.exam = (TbExamination) controller.TransactionNumberList.getSelectedItem()
				.getValue();
		//todo tarik dari db
		controller.exam = noteDao.getNote(controller.exam.getNExamId());
		
		controller.transactionNumber.setValue(controller.exam.getVNoteNo());
		controller.transactionNumber.setAttribute("nota", controller.exam);
		// clear();

//		controller.mr = AdmissionServiceLocator.getMedicalRecordManager()
//				.getMrByPatientId(controller.exam.getMsPatient().getNPatientId());
		
		controller.mr = mrDao.getMedicalRecordByPaitentId(controller.exam.getMsPatient().getNPatientId());
		
		controller.patient = controller.exam.getMsPatient();

		controller.patientName.setValue(controller.patient.getVPatientName());

		if (controller.exam.getTbRegistration() != null) {
			if (controller.exam.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(controller.exam.getTbRegistration().getMsStaff()
						.getVStaffName());
		}

		int[] age = MedisafeUtil.calculateAge(controller.exam.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));

		if (controller.mr != null)
			controller.MRNumber.setValue(controller.mr.getVMrCode());
		if (controller.exam.getTbRegistration() != null)
			controller.registrationNumber.setValue(controller.exam.getTbRegistration()
					.getVRegSecondaryId());

		// AMBIL DETIL NOTA
		controller.anak.getChildren().clear();

		getDetailLabTest(controller, controller.exam);

		if (controller.MRNumber.getValue().trim().equals("")) {
			controller.referencePatient.setChecked(true);
			controller.MRNumber.setVisible(false);
		}

		if (controller.mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(controller.mr.getNMrId());

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

		controller.setFieldDisable(true);
		controller.setButtonActive(true);

	}

	public void getDetailLabTest(HasilLaboratController controller, TbExamination exam) throws VONEAppException {
		exam = getTbExaminationById(exam.getNExamId());
		
		List examDetail = getTreatmentTrx(exam);
		Iterator iter = examDetail.iterator();
		
		TbTreatmentTrx treatTrx;
		MsTreatment treat;
		controller.anak.getChildren().clear();
		

		while (iter.hasNext()) {
			treatTrx = (TbTreatmentTrx)iter.next();
			treat = treatTrx.getMsTreatmentFee().getMsTreatment();
			
			controller.createGroupTree(null, treat.getMsTreatmentGroup().getVTgroupName() + DASH + treat.getVTreatmentName());
				
			Set testSet = treat.getMsLabTreatmentDetil();
			Iterator iter2 = testSet.iterator();
			while (iter2.hasNext()) {
				MsLabTreatmentDetil msLabTreatmentDetil = (MsLabTreatmentDetil) iter2.next();
				controller.createItemTree(msLabTreatmentDetil, msLabTreatmentDetil.getVDetailName(), null, 
						msLabTreatmentDetil.getVNormalRange(),
						null, msLabTreatmentDetil.getVQuantify(), false);
			}
			

		}
		
		
	}

	public void getRegistration(HasilLaboratController controller, int type) throws VONEAppException {
		controller.mr = null;
		if (type == MedisafeConstants.INPUT_BY_MANUAL) {
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);

			controller.mr = mrDao.getMrRegistered(code);
			if (controller.mr == null) {
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
			controller.mr = (TbMedicalRecord) controller.item.getValue();
			
//			item = controller.patientList.getSelectedItem();
//			mr = (TbMedicalRecord)item.getValue();
			controller.patient = controller.mr.getMsPatient();
		}
		
//		controller.reg = regDao.getLastRegistrationByMrId(controller.mr.getNMrId());
		
		controller.exam = getExaminationByMr(controller.mr);
		if (controller.exam == null) {
			try {
				Messagebox.show("TIDAK ADA TEST LAB");
				controller.clear();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		//todo ambil inputan hasil lab berdasar mr
//		controller.getDetailLabTest(controller.mr.getMsPatient());
//		 AMBIL DETIL NOTA
		controller.anak.getChildren().clear();

		getDetailLabTest(controller, controller.exam);

		controller.transactionNumber.setValue(controller.exam.getVNoteNo());
		
		if (controller.mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(controller.mr.getNMrId());
		
		controller.MRNumber.setValue(controller.mr.getVMrCode());

		int[] umurSkrg = MedisafeUtil.calculateAge(controller.mr.getMsPatient()
				.getDPatientDob());
		controller.age.setValue(umurSkrg[0] + " thn " + umurSkrg[1] + " bln "
				+ umurSkrg[2] + " hr");

		if (controller.reg.getMsStaff() != null) {
			controller.mainDoctor.setValue(controller.reg.getMsStaff().getVStaffCode() + DASH
					+ controller.reg.getMsStaff().getVStaffName());
		}

		controller.patientName.setValue(controller.mr.getMsPatient().getVPatientName());
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

		controller.patient = controller.mr.getMsPatient();
		controller.labResultNumber.setValue(null);
		
		controller.session.setAttribute("mr", controller.mr);
		
		controller.session.setAttribute("location", controller.locationList);

		controller.setFieldDisable(true);

	}

	public void searchNote(HasilLaboratController controller) throws VONEAppException {
		controller.TransactionNumberList.getItems().clear();

		MsUnit unit = (MsUnit) controller.locationList.getSelectedItem().getValue();

		List result = noteDao.searchNoteLab(
				"%" + controller.searchNota.getValue() + "%",
				"%" + controller.searchName.getValue() + "%", unit);

		Listitem item;
		Listcell cell;
		if (result == null) {
			try {
				Messagebox.show(TIDAK_ADA_DATA);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		Iterator it = result.iterator();
		while (it.hasNext()) {
			controller.exam = (TbExamination) it.next();
			item = new Listitem();
			item.setValue(controller.exam);
			item.setParent(controller.TransactionNumberList);

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(controller.exam.getVNoteNo());

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(controller.exam.getMsPatient().getVPatientName());

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(MedisafeUtil.getNoteStatus(controller.exam.getNExamStatus()
					.intValue()));

		}

	}

	public void searchRegisteredPatient(HasilLaboratController controller, Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList) throws VONEAppException {
		controller.mr = null;
		Listitem item = null;
		String code = "";
		if (crNoMR.getText().trim().length() == 6) {
			code = MedisafeUtil.convertToMrCode(crNoMR.getText());
			crNoMR.setValue(code);
		} else {
			code = crNoMR.getText();
		}

		patientSearchList.getItems().clear();
		if (crNoMR.getText().trim().equals("")
				&& crNama.getText().trim().equals("")
				&& crAlamat.getText().trim().equals("")) {
			try {
				Messagebox.show(MessagesService
						.getKey("salah.satu.field.harus.diisi"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		List list = serachRegisteredPatient("%" + code + "%", "%"
				+ crNama.getText() + "%", "%" + crAlamat.getText() + "%");

		if (list.size() == 0) {
			crNoMR.focus();
			return;
		}

		Iterator it = list.iterator();

		while (it.hasNext()) {
			controller.mr = (TbMedicalRecord) it.next();
			item = new Listitem();
			item.setValue(controller.mr);
			item.setParent(patientSearchList);

			Listcell mrCode = new Listcell(controller.mr.getVMrCode());
			mrCode.setParent(item);
			Listcell nama = new Listcell(controller.mr.getMsPatient().getVPatientName());
			nama.setParent(item);
			Listcell alamat = new Listcell(controller.mr.getMsPatient()
					.getVPatientMainAddr());
			alamat.setParent(item);
		}
	}

	public void searchHasilLab(HasilLaboratController controller) throws VONEAppException {
		controller.labResultList.getItems().clear();
		
		List result = searchHasilLab(
				"%" + controller.searchResult.getValue() + "%",
				"%" + controller.nameSearch.getValue() + "%");

		Listitem item;
		Listcell cell;
		if (result == null) {
			try {
				Messagebox.show(TIDAK_ADA_DATA);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		Iterator it = result.iterator();
		while (it.hasNext()) {
			controller.labResult = (TbLaboratoryResult) it.next();
			controller.exam = getTbExaminationById(controller.labResult.getTbExamination().getNExamId());
			
			item = new Listitem();
			item.setValue(controller.labResult);
			item.setParent(controller.labResultList);

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(controller.labResult.getVLabRsltCode());

			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(controller.exam.getMsPatient().getVPatientName());
		}

	}

	public void getLabResult(HasilLaboratController controller) throws VONEAppException {
		controller.reg = null;
		controller.item = controller.labResultList.getSelectedItem();
		controller.labResult = (TbLaboratoryResult)controller.item.getValue();
		
		controller.exam = controller.labResult.getTbExamination();
		controller.exam = getTbExaminationById(controller.exam.getNExamId());//biar narik lagi?
		
		controller.patient = controller.exam.getMsPatient();
		//controller.mr = AdmissionServiceLocator.getMedicalRecordManager().getMrByPatientId(controller.patient.getNPatientId());
		controller.mr = mrDao.getMedicalRecordByPaitentId(controller.patient.getNPatientId());
		
		controller.patientName.setValue(controller.exam.getMsPatient().getVPatientName());
		controller.labResultNumber.setValue(controller.labResult.getVLabRsltCode());
		controller.transactionNumber.setValue(controller.exam.getVNoteNo());
		
		if (controller.exam.getTbRegistration() != null) {
			if (controller.exam.getTbRegistration().getMsStaff() != null)
				controller.mainDoctor.setValue(controller.exam.getTbRegistration().getMsStaff()
						.getVStaffName());
		}

		int[] age = MedisafeUtil.calculateAge(controller.exam.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(age));

		if (controller.mr != null)
			controller.MRNumber.setValue(controller.mr.getVMrCode());
		if (controller.exam.getTbRegistration() != null)
			controller.registrationNumber.setValue(controller.exam.getTbRegistration()
					.getVRegSecondaryId());

		if (controller.MRNumber.getValue().trim().equals("")) {
			controller.referencePatient.setChecked(true);
			controller.MRNumber.setVisible(false);
		}

		//todo reg
		if (controller.mr != null)
			controller.reg = regDao.getLastRegistrationByMrId(controller.mr.getNMrId());
		
		if(controller.reg != null)
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
		
		// AMBIL DETIL NOTA
		controller.takeTime.setText(controller.labResult.getVJam());
		controller.escortDoctor.setText(controller.labResult.getVDrPengirim());
		controller.laboratNo.setText(controller.labResult.getVResep());
		controller.anak.getChildren().clear();
		
		controller.getDetailLabTestResult(controller.labResult);

		controller.setFieldDisable(true);
		controller.setButtonActive(false);

	}

	public void CreateReport(TbExamination exam, StringBuffer sb) throws VONEAppException {
		exam = getTbExaminationById(exam.getNExamId());
		this.noNota = exam.getVNoteNo();
		this.tglNota = PrintClient.getDate(exam.getDWhnCreate(), MedisafeConstants.DATE_FORMAT);

		//labDao.save(exam);
		brs = 1;
		
		//ranap ok
		if(exam.getVNoteNo().substring(0, 1).equals("I")){
			sbAppendHeader(exam, sb);
			
			sbAppendBedDetail(exam, sb);
			sbAppendTreatmentDetail(exam, sb);
			sbAppendItemDetail(exam, sb);
			sbAppendBundledDetail(exam, sb);
			
			sbAppendFooter(exam, sb);
		}else{
			//rajal
			sbAppendHeader(exam, sb);
			
			sbAppendBedDetail(exam, sb);
			sbAppendTreatmentDetail(exam, sb);
			sbAppendItemDetail(exam, sb);
			sbAppendBundledDetail(exam, sb);
			
			sbAppendFooter(exam, sb);
			
		}
	}

	public void CreateCopyResep(TbExamination exam, StringBuffer sb) throws VONEAppException {
		exam = getTbExaminationById(exam.getNExamId());
		this.noNota = exam.getVNoteNo();
		this.tglNota = PrintClient.getDate(exam.getDWhnCreate(), MedisafeConstants.DATE_FORMAT);

		//labDao.save(exam);
		brs = 1;
		
		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(exam.getMsPatient().getNPatientId());
		
		String dokter = "";
		String noRegister = "";
		String noMr = "";
		String jenisPasien = "";

		if(exam.getTbRegistration() != null){
			if(exam.getTbRegistration().getMsStaff() != null)
				dokter = exam.getTbRegistration().getMsStaff().getVStaffName();
		}
		
		String namaPasien = exam.getMsPatient().getVPatientName();
		if(exam.getTbRegistration() != null)
			noRegister = exam.getTbRegistration().getVRegSecondaryId();
		
		if(mr != null)
			noMr = mr.getVMrCode();
		
		String unit = exam.getMsUnit().getVUnitName();
		if (exam.getMsPatient().getMsPatientType() != null)
			jenisPasien = exam.getMsPatient().getMsPatientType().getVTpatientDesc();
		// bed = exam.gettb
		// hall ??
		// kelasPasien = exam.getv ??
		
		sb.append(MedisafeConstants.CHAR_CONDENSED);
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_YAYASAN + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_NPWP + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_PKP + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_RS + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(NO_REGISTER 
				+ PrintClient.padLeft(noRegister, COLUMN_HEAD) 
				+ NOMOR 
				+ this.noNota 
				+ MedisafeConstants.NEWLINE);
		
		sb.append(NO_REKAM_MEDIK 
				+ PrintClient.padLeft(noMr, COLUMN_HEAD) 
				+ UNIT 
				+ unit
				+ MedisafeConstants.NEWLINE);
		
		sb.append(NAMA_PASIEN 
				+ PrintClient.padLeft(namaPasien, COLUMN_HEAD) 
				+ DOKTER 
				+ dokter
				+ MedisafeConstants.NEWLINE);
		
		sb.append(TGL 
				+ PrintClient.padLeft(tglNota, COLUMN_HEAD) 
				+ JENIS 
				+ jenisPasien
				+ MedisafeConstants.NEWLINE);
		
		sb.append(NO_RESEP 
				+ PrintClient.padLeft(exam.getVRecipeNo(), COLUMN_HEAD) 
				+ MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		//judul
		sb.append(PrintClient.padLeft(HEAD_NO, COLUMN_01));//NOMOR
		sb.append(PrintClient.padLeft(HEAD_KODE, COLUMN_02));//KODE
		sb.append(PrintClient.padLeft(HEAD_KET, COLUMN_03));//KET
		
		sb.append(PrintClient.padRight(HEAD_QTY, COLUMN_04));//QTY (?)
		sb.append(PrintClient.padRight("DOSIS", COLUMN_05));//SUBTTL
		
		sb.append(MedisafeConstants.NEWLINE);
		 
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		
		sbAppendItemDetailResep(exam, sb);
		sbAppendBundledDetailResep(exam, sb);
		
		sbAppendFooter(exam, sb);
	}

	private void sbAppendFooter(TbExamination exam, StringBuffer sb) {
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(PrintClient.padRight(TOTAL_NOTA, PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(exam.getTotal().doubleValue(), COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);

		sb.append(MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.NEWLINE);
		String ttd = KURUNG_BUKA + exam.getVWhoCreate() + KURUNG_TUTUP;
		String grs = PrintClient.repl(DASH, ttd.length());
		sb.append(PrintClient.padRight(ttd,PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(PrintClient.padRight(grs,PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(PRINT_DATE + PrintClient.getDate(new Date(),MedisafeConstants.DATETIME_FORMAT));
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.CHAR_NORMAL);
		sb.append(MedisafeConstants.NEWLINE);
		
		

	}

	private void sbAppendBundledDetail(TbExamination exam, StringBuffer sb) throws VONEAppException{
		List listBundle = polyDao.getBundleTrx(exam);
		Iterator iter = listBundle.iterator();
		TbBundledTrx tbBundledTrx;
		String sisa = null;
		String ket;
		
		while (iter.hasNext()) {
			tbBundledTrx = (TbBundledTrx) iter.next();
			sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			sb.append(PrintClient.padLeft(tbBundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode(), COLUMN_02));//KODE
			sb.append(PrintClient.padLeft(tbBundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName(), COLUMN_03));//KET
			sb.append(PrintClient.padRight(tbBundledTrx.getNQty(), COLUMN_04));//QTY
			sb.append(PrintClient.padRight(tbBundledTrx.getNAmountAfterDisc(), COLUMN_05));//SUBTTL
			sb.append(MedisafeConstants.NEWLINE);
			
			ket = tbBundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName();
			if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
			brs++;
		}
		//misc here
		Set setMisc = exam.getTbMiscTrx();
		iter = setMisc.iterator();
		TbMiscTrx tbMiscTrx; 
		while (iter.hasNext()) {
			tbMiscTrx = (TbMiscTrx) iter.next();
			sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			sb.append(PrintClient.padLeft(MedisafeConstants.MISC_CODE, COLUMN_02));//KODE
			sb.append(PrintClient.padLeft(tbMiscTrx.getVMiscName(), COLUMN_03));//KET
			sb.append(PrintClient.padRight(tbMiscTrx.getNQty(), COLUMN_04));//QTY
			sb.append(PrintClient.padRight(tbMiscTrx.getNAmountAfterDisc(), COLUMN_05));//SUBTTL
			sb.append(MedisafeConstants.NEWLINE);
			
			ket = tbMiscTrx.getVMiscName();
			if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
			brs++;
		}
		//drug
		
		Set drugSet = exam.getTbDrugIngredients();
		iter = drugSet.iterator();
		TbDrugIngredients tbDrugIngredients; 
		while (iter.hasNext()) {
			tbDrugIngredients = (TbDrugIngredients) iter.next();
			sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			sb.append(PrintClient.padLeft(tbDrugIngredients.getVDingrId(), COLUMN_02));//KODE
			sb.append(PrintClient.padLeft(tbDrugIngredients.getVItemComposition(), COLUMN_03));//KET
			sb.append(PrintClient.padRight(tbDrugIngredients.getNDingrQty(), COLUMN_04));//QTY
			sb.append(PrintClient.padRight(tbDrugIngredients.getNAmountAfterDisc()+ tbDrugIngredients.getNEr()* new Integer(MessagesService.getKey("jasa.apotik")), COLUMN_05));//SUBTTL
			sb.append(MedisafeConstants.NEWLINE);
			
			ket = tbDrugIngredients.getVItemComposition();
			if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
			brs++;
		}
		
		//bed
//		Set bedSet = exam.getTbBedTrx();
//		iter = bedSet.iterator();
//		TbBedTrx tbBedTrx; 
//		while (iter.hasNext()) {
//			tbBedTrx = (TbBedTrx) iter.next();
//			ket = PrintClient.getDate(tbBedTrx.getDDateFrom(), MedisafeConstants.DATETIME_FORMAT) +
//			" - " +
//			PrintClient.getDate(tbBedTrx.getDDateFrom(), MedisafeConstants.DATETIME_FORMAT);
//			sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
//			sb.append(PrintClient.padLeft(tbBedTrx.getMsBed().getVBedCode(), COLUMN_02));//KODE
//			sb.append(PrintClient.padLeft(ket, COLUMN_03));//KET
//			sb.append(PrintClient.padRight(null, COLUMN_04));//QTY
//			sb.append(PrintClient.padRight(tbBedTrx.getNAmountAfterDisc(), COLUMN_05));//SUBTTL
//			sb.append(MedisafeConstants.NEWLINE);
//			
//			//ket = tbDrugIngredients.getVItemComposition();
//			if(ket.length()>COLUMN_03)
//				 sisa = ket.substring(COLUMN_03);
//			 if(sisa!=null){
//				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
//				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
//				 sb.append(MedisafeConstants.NEWLINE);
//				 sisa = null;
//			 }
//			brs++;
//		}
	}

	private void sbAppendBundledDetailResep(TbExamination exam, StringBuffer sb) throws VONEAppException{
		List listBundle = polyDao.getBundleTrx(exam);
		Iterator iter = listBundle.iterator();
		TbBundledTrx tbBundledTrx;
		String sisa = null;
		String ket;
		
		while (iter.hasNext()) {
			tbBundledTrx = (TbBundledTrx) iter.next();
			sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			sb.append(PrintClient.padLeft(tbBundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode(), COLUMN_02));//KODE
			sb.append(PrintClient.padLeft(tbBundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName(), COLUMN_03));//KET
			sb.append(PrintClient.padRight(tbBundledTrx.getNQty(), COLUMN_04));//QTY
			sb.append(PrintClient.padRight("", COLUMN_05));//SUBTTL
			sb.append(MedisafeConstants.NEWLINE);
			
			ket = tbBundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName();
			if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
			brs++;
		}

	}
	private void sbAppendItemDetail(TbExamination exam, StringBuffer sb) throws VONEAppException {
			//itemTrxs = cashierManager.getItemsTrxBaseOnNote(exam);
			List itemTrxs = polyDao.getItemTrx(exam);
			Iterator itr = itemTrxs.iterator();
			Object[] obj;
			Double subttl;
			Double total = 0.0;
			String sisa = null;
			String ket;
			int jasaR = 0;
			while(itr.hasNext()){
				obj = (Object[])itr.next();
				ket = obj[2].toString();
				jasaR = (Short)obj[11];
				subttl = (jasaR * new Integer(MessagesService.getKey("jasa.apotik")).doubleValue());
//				query.addScalar("id", Hibernate.INTEGER); 0
//				query.addScalar("code", Hibernate.STRING);1
//				query.addScalar("name", Hibernate.STRING);2
//				query.addScalar("qty", Hibernate.INTEGER);3
//				query.addScalar("value", Hibernate.DOUBLE);4
//				query.addScalar("discount", Hibernate.DOUBLE);5
//				query.addScalar("total", Hibernate.DOUBLE);6
				subttl = subttl + (Double)obj[7];
				sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
				sb.append(PrintClient.padLeft(obj[1].toString(), COLUMN_02));//KODE
				sb.append(PrintClient.padLeft(obj[2].toString(), COLUMN_03));//KET
				sb.append(PrintClient.padRight(obj[4].toString(), COLUMN_04));//QTY
				sb.append(PrintClient.padRight(subttl, COLUMN_05));//SUBTTL
				sb.append(MedisafeConstants.NEWLINE);
				total = total + subttl;
				 if(ket.length()>COLUMN_03)
					 sisa = ket.substring(COLUMN_03);
				 if(sisa!=null){
					 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
					 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
					 sb.append(MedisafeConstants.NEWLINE);
					 sisa = null;
				 }
				brs++;
			}
			//COMMENT 24 OCT 2010
			/**
			sb.append(PrintClient.padLeft("" + SPACE, COLUMN_01));//NOMOR
			sb.append(PrintClient.padLeft("", COLUMN_02));//KODE
			sb.append(PrintClient.padLeft("", COLUMN_03));//KET
			sb.append(PrintClient.padRight("", COLUMN_04));//QTY
			sb.append(PrintClient.padRight(total, COLUMN_05));//SUBTTL
			sb.append(MedisafeConstants.NEWLINE); */
			/**
			sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			sb.append(PrintClient.padLeft("R", COLUMN_02));//KODE
			sb.append(PrintClient.padLeft("JASA APOTIK", COLUMN_03));//KET
			sb.append(PrintClient.padRight(jasaR, COLUMN_04));//QTY
			sb.append(PrintClient.padRight((jasaR * new Integer(MessagesService.getKey("jasa.apotik")).intValue()), COLUMN_05));//SUBTTL
			sb.append(MedisafeConstants.NEWLINE); */
		

	}
	
	private double sbAppendItemDetailCMC(TbExamination exam, StringBuffer sb) throws VONEAppException {
		//itemTrxs = cashierManager.getItemsTrxBaseOnNote(exam);
		List itemTrxs = polyDao.getItemTrx(exam);
		Iterator itr = itemTrxs.iterator();
		Object[] obj;
		Double subttl;
		Double total = 0.0;
		String sisa = null;
		String ket;
		int jasaR = 0;

		while(itr.hasNext()){
			obj = (Object[])itr.next();
			ket = obj[2].toString();
			jasaR = (Short)obj[11];
			subttl = (jasaR * new Integer(MessagesService.getKey("jasa.apotik")).doubleValue());

			subttl = subttl + (Double)obj[7];
			total = total + subttl;
		}
		
		Set drugSet = exam.getTbDrugIngredients();
		Iterator iter = drugSet.iterator();
		TbDrugIngredients tbDrugIngredients; 
		while (iter.hasNext()) {
			tbDrugIngredients = (TbDrugIngredients) iter.next();
			subttl = tbDrugIngredients.getNAmountAfterDisc()+ tbDrugIngredients.getNEr()* new Integer(MessagesService.getKey("jasa.apotik"));
			total = total + subttl;
			
			
		}
		
		return total;
}

	private void sbAppendItemDetailResep(TbExamination exam, StringBuffer sb) throws VONEAppException {
		//itemTrxs = cashierManager.getItemsTrxBaseOnNote(exam);
		List itemTrxs = polyDao.getItemTrx(exam);
		Iterator itr = itemTrxs.iterator();
		Object[] obj;
//		Double subttl;
		String sisa = null;
		String ket;
		while(itr.hasNext()){
			obj = (Object[])itr.next();
			ket = obj[2].toString();
			
//			query.addScalar("id", Hibernate.INTEGER); 0
//			query.addScalar("code", Hibernate.STRING);1
//			query.addScalar("name", Hibernate.STRING);2
//			query.addScalar("qty", Hibernate.INTEGER);3
//			query.addScalar("value", Hibernate.DOUBLE);4
//			query.addScalar("discount", Hibernate.DOUBLE);5
//			query.addScalar("total", Hibernate.DOUBLE);6
//			subttl = (Double)obj[7];
			sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			sb.append(PrintClient.padLeft(obj[1].toString(), COLUMN_02));//KODE
			sb.append(PrintClient.padLeft(obj[2].toString(), COLUMN_03));//KET
			sb.append(PrintClient.padRight(obj[4].toString(), COLUMN_04));//QTY
			sb.append(PrintClient.padRight("", COLUMN_05));//SUBTTL
			sb.append(MedisafeConstants.NEWLINE);

			 if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
			brs++;
		}
	}
	
	private void sbAppendTreatmentDetail(TbExamination exam, StringBuffer sb) {
		Set list = exam.getTbTreatmentTrx();
		Iterator iter = list.iterator();
		TbTreatmentTrx treatTrx;
		String sisa = null;
		String ket;
		while(iter.hasNext()){
			 treatTrx = (TbTreatmentTrx)iter.next();
			 ket = treatTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName(); 
			 if(treatTrx.getMsDoctor() != null && treatTrx.getMsTreatmentFee().getNDoctorFee() > 0)
				 ket += " - " + treatTrx.getMsDoctor().getVStaffName();
			 sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			 sb.append(PrintClient.padLeft(treatTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode(), COLUMN_02));//KODE
			 sb.append(PrintClient.padLeft(ket, COLUMN_03));//KET
			 sb.append(PrintClient.padRight(treatTrx.getNQty(), COLUMN_04));//QTY (?)
			 sb.append(PrintClient.padRight(treatTrx.getNAmountTrx(), COLUMN_05));//SUBTTL
			 sb.append(MedisafeConstants.NEWLINE);
			 
			 if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
			 brs++;
		}

		
	}

	private void sbAppendBedDetail(TbExamination exam, StringBuffer sb) {
		Set list = exam.getTbBedTrx();
		Iterator iter = list.iterator();
		TbBedTrx bedTrx;
		String sisa = null;
		String ket;
		while(iter.hasNext()){
			 bedTrx = (TbBedTrx)iter.next();
			 sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			 sb.append(PrintClient.padLeft(bedTrx.getMsBed().getVBedCode(), COLUMN_02));//KODE
			 sb.append(PrintClient.padLeft(bedTrx.getMsBed().getVBedDesc(), COLUMN_03));//KET
			 
			 sb.append(PrintClient.padRight(bedTrx.getNTotalHour(), COLUMN_04));//QTY (?)
			 sb.append(PrintClient.padRight(bedTrx.getNAmountAfterDisc(), COLUMN_05));//SUBTTL
			 sb.append(MedisafeConstants.NEWLINE);
			 
			 ket = bedTrx.getMsBed().getVBedDesc();
			 if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
			 brs++;
		}

	}
	
	private void sbAppendBedDetailCMC(TbExamination exam, StringBuffer sb){
		
		Set list = exam.getTbBedTrx();
		Iterator iter = list.iterator();
		TbBedTrx bedTrx;
		String sisa = null;
		String ket;
		while(iter.hasNext()){
			 bedTrx = (TbBedTrx)iter.next();
			 sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
			 sb.append(PrintClient.padLeft(bedTrx.getMsBed().getVBedCode(), COLUMN_02));//KODE
			 sb.append(PrintClient.padLeft(bedTrx.getMsBed().getVBedDesc(), COLUMN_03));//KET
			 
			 sb.append(PrintClient.padRight(bedTrx.getNTotalHour(), COLUMN_04));//QTY (?)
			 sb.append(PrintClient.padRight(bedTrx.getNAmountAfterDisc(), COLUMN_05));//SUBTTL
			 sb.append(MedisafeConstants.NEWLINE);
			 
			 ket = bedTrx.getMsBed().getVBedDesc();
			 if(ket.length()>COLUMN_03)
				 sisa = ket.substring(COLUMN_03);
			 if(sisa!=null){
				 sb.append(PrintClient.padLeft(" ", COLUMN_01 + COLUMN_02));
				 sb.append(PrintClient.padLeft(sisa, COLUMN_03));
				 sb.append(MedisafeConstants.NEWLINE);
				 sisa = null;
			 }
//			 brs++;
		}
		brs++;
	}

	private void sbAppendHeader(TbExamination exam, StringBuffer sb) throws VONEAppException {
		//TbMedicalRecord mr = AdmissionServiceLocator.getMedicalRecordManager().getMrByPatientId(exam.getMsPatient().getNPatientId());
		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(exam.getMsPatient().getNPatientId());
		
		String dokter = "";
		String noRegister = "";
		String noMr = "";
		String jenisPasien = "";
		String unit = "";

		if(exam.getTbRegistration() != null){
			if(exam.getTbRegistration().getMsStaff() != null)
				dokter = exam.getTbRegistration().getMsStaff().getVStaffName();
		}
		
		String namaPasien = exam.getMsPatient().getVPatientName();
		if(exam.getTbRegistration() != null)
			noRegister = exam.getTbRegistration().getVRegSecondaryId();
		
		if(mr != null)
			noMr = mr.getVMrCode();
		if(exam.getMsUnit() != null)
			unit = exam.getMsUnit().getVUnitName();
		if (exam.getMsPatient().getMsPatientType() != null)
			jenisPasien = exam.getMsPatient().getMsPatientType().getVTpatientDesc();
		// bed = exam.gettb
		// hall ??
		// kelasPasien = exam.getv ??
		
		sb.append(MedisafeConstants.CHAR_CONDENSED);
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_YAYASAN + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_NPWP + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_PKP + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_RS + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.NEWLINE);
		
		//modified for CMC Printing, edit date 2010-02-20
		
		sb.append("                  KWITANSI PEMBAYARAN                 ");
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(NO_REGISTER 
				+ PrintClient.padLeft(noRegister, COLUMN_HEAD) 
				+ NOMOR 
				+ this.noNota 
				+ MedisafeConstants.NEWLINE);
		
		sb.append(NO_REKAM_MEDIK 
				+ PrintClient.padLeft(noMr, COLUMN_HEAD) 
				+ UNIT 
				+ unit
				+ MedisafeConstants.NEWLINE);
		
		sb.append(NAMA_PASIEN 
				+ PrintClient.padLeft(namaPasien, COLUMN_HEAD) 
				+ DOKTER 
				+ dokter
				+ MedisafeConstants.NEWLINE);
		
		sb.append(TGL 
				+ PrintClient.padLeft(tglNota, COLUMN_HEAD) 
				+ JENIS 
				+ jenisPasien
				+ MedisafeConstants.NEWLINE);
		
//		sb.append(NO_RESEP 
//				+ PrintClient.padLeft(exam.getVRecipeNo(), COLUMN_HEAD) 
//				+ MedisafeConstants.NEWLINE);
		
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		
		//judul
		sb.append(PrintClient.padLeft(HEAD_NO, COLUMN_01));//NOMOR
		sb.append(PrintClient.padLeft(HEAD_KODE, COLUMN_02));//KODE
		sb.append(PrintClient.padLeft(HEAD_KET, COLUMN_03));//KET
		
		sb.append(PrintClient.padRight(HEAD_QTY, COLUMN_04));//QTY (?)
		sb.append(PrintClient.padRight(HEAD_SUBTTL, COLUMN_05));//SUBTTL
		
		sb.append(MedisafeConstants.NEWLINE);
		 
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
	}

	public void CreateReport(TbReturPharmacyTrx tbReturPharmacyTrx, StringBuffer sb) throws VONEAppException {
		MsPatient msPatient = tbReturPharmacyTrx.getMsPatient();
		
		this.noNota = tbReturPharmacyTrx.getVReturCode();
		this.tglNota = PrintClient.getDate(tbReturPharmacyTrx.getDWhnCreate(), MedisafeConstants.DATE_FORMAT);
		sb = new StringBuffer();
		brs = 1;
		//sbAppendHeader(exm);
		sbAppendHeader(msPatient, sb);
		
		try {
			//List list = apotikManager.getReturDetil(tbReturPharmacyTrx);
			List list = apotikDao.getReturDetil(tbReturPharmacyTrx.getNReturId()); //TbReturPharmacyDetailTrx
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				TbReturPharmacyDetailTrx tbReturPharmacyDetailTrx = (TbReturPharmacyDetailTrx) iter.next();
				sb.append(PrintClient.padLeft(brs + SPACE, COLUMN_01));//NOMOR
				sb.append(PrintClient.padLeft(tbReturPharmacyDetailTrx.getMsItem().getVItemCode(), COLUMN_02));//KODE
				sb.append(PrintClient.padLeft(tbReturPharmacyDetailTrx.getMsItem().getVItemName(), COLUMN_03));//KET
				sb.append(PrintClient.padLeft(tbReturPharmacyDetailTrx.getNQty(), COLUMN_04));//QTY (?)
				sb.append(PrintClient.padRight(tbReturPharmacyDetailTrx.getNValue(), COLUMN_05));//SUBTTL
				sb.append(MedisafeConstants.NEWLINE);
				brs++;
			}
		} catch (VONEAppException e) {
			e.printStackTrace();
		}
		
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(PrintClient.padLeft(TOTAL_NOTA, PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(tbReturPharmacyTrx.getNTrxValue(), COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(PRINT_DATE + PrintClient.getDate(new Date(),MedisafeConstants.DATETIME_FORMAT));
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.CHAR_NORMAL);
		sb.append(MedisafeConstants.NEWLINE);
	}
	
	private void sbAppendHeader(MsPatient msPatient, StringBuffer sb) throws VONEAppException {
		//TbMedicalRecord mr = AdmissionServiceLocator.getMedicalRecordManager().getMrByPatientId(msPatient.getNPatientId());
		TbMedicalRecord mr = mrDao.getMedicalRecordByPaitentId(msPatient.getNPatientId());
		
		String namaPasien = msPatient.getVPatientName();
		String noMr = mr.getVMrCode();
		
		sb.append(MedisafeConstants.CHAR_CONDENSED);
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.OEN_RS + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(
				  NO_RETUR 
				+ this.noNota 
				+ MedisafeConstants.NEWLINE);
		
		sb.append(NO_REKAM_MEDIK 
				+ PrintClient.padLeft(noMr, COLUMN_HEAD) 
				+ MedisafeConstants.NEWLINE);
		
		sb.append(NAMA_PASIEN 
				+ PrintClient.padLeft(namaPasien, COLUMN_HEAD) 
				+ MedisafeConstants.NEWLINE);
		sb.append(TGL 
				+ PrintClient.padLeft(tglNota, COLUMN_HEAD) 
				+ MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
//		judul
		sb.append(PrintClient.padLeft(HEAD_NO, COLUMN_01));//NOMOR
		sb.append(PrintClient.padLeft(HEAD_KODE, COLUMN_02));//KODE
		sb.append(PrintClient.padLeft(HEAD_KET, COLUMN_03));//KET
		sb.append(PrintClient.padRight(HEAD_QTY, COLUMN_04));//QTY (?)
		sb.append(PrintClient.padRight(HEAD_SUBTTL, COLUMN_05));//SUBTTL
		sb.append(MedisafeConstants.NEWLINE);
		 
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
	
	}

	public ApotikDAO getApotikDao() {
		return apotikDao;
	}

	public void setApotikDao(ApotikDAO apotikDao) {
		this.apotikDao = apotikDao;
	}

	public void CreateReport(Collection notas, String noKwitansi, Date tgl, double total, double jumlahDiskon, double persentasiPajak, double nilaiPajak, double totalAkhir, double bayarTunai, double bayarKredit, double bayarDeposit, double retur, StringBuffer sb) throws VONEAppException {
		this.noNota = noKwitansi;
		this.tglNota = PrintClient.getDate(tgl, MedisafeConstants.DATE_FORMAT);
		TbExamination nota = null;
		double totalTrx = 0;
		
		brs = 1;
		boolean printHeader = true;
		//uncomment if not CMC
		
		Iterator it = notas.iterator();
		while(it.hasNext()){
			nota = (TbExamination)it.next();
			nota = getTbExaminationById(nota.getNExamId());//refresh detail rincian
			
			if(printHeader){
				sbAppendHeader(nota, sb);
				printHeader = false;
			}
			//utk kebutuhan CMC ini baris di bawah di comment, kalau yang lain harus di uncomment
			sb.append(MedisafeConstants.NEWLINE);
			sb.append(DOUBLE_STAR + nota.getVNoteNo() + MedisafeConstants.NEWLINE);
			
			sbAppendBedDetail(nota, sb);
//			sbAppendBedDetailCMC(nota, sb);
			sbAppendTreatmentDetail(nota, sb);
			sbAppendItemDetail(nota, sb);
//			sbAppendItemDetailCMC(nota, sb);
			sbAppendBundledDetail(nota, sb);
			totalTrx += nota.getTotal();
		}
		/**
		double totalItem = 0;
		
		it = notas.iterator();
		while(it.hasNext()){
			nota = (TbExamination)it.next();
			nota = getTbExaminationById(nota.getNExamId());//refresh detail rincian
			
			if(printHeader){
				sbAppendHeader(nota, sb);
				printHeader = false;
			}
			//utk kebutuhan CMC ini baris di bawah di comment, kalau yang lain harus di uncomment
			
			
			sbAppendBedDetail(nota, sb);
			sbAppendTreatmentDetail(nota, sb);

			totalItem = totalItem + sbAppendItemDetailCMC(nota, sb);
			
			sbAppendBundledDetail(nota, sb);
			totalTrx += nota.getTotal();
		}
		sb.append(DOUBLE_STAR + "BIAYA OBAT" + MedisafeConstants.NEWLINE);
		sb.append(MedisafeConstants.NEWLINE);
		sb.append(PrintClient.padLeft("" + SPACE, COLUMN_01));//NOMOR
		sb.append(PrintClient.padLeft("", COLUMN_02));//KODE
		sb.append(PrintClient.padLeft("", COLUMN_03));//KET
		sb.append(PrintClient.padRight("", COLUMN_04));//QTY
		sb.append(PrintClient.padRight(totalItem, COLUMN_05));//SUBTTL
		sb.append(MedisafeConstants.NEWLINE);*/
	}

	public void getListboxDetail(Listbox list, MsTreatmentGroup msTreatmentGroup) throws VONEAppException {
		list.getItems().clear();
		msTreatmentGroup = treatDao.reloadTreatmentGroup(msTreatmentGroup);
		
		Iterator iter = msTreatmentGroup.getMsTreatments().iterator();
		
		MsTreatment msTreatment;
		Listitem item;
		Listcell cell;
		while (iter.hasNext()) {
			msTreatment = (MsTreatment) iter.next();
			item = new Listitem();
			item.setParent(list);
			item.setValue(msTreatment);
			item.setLabel(msTreatment.getVTreatmentCode());
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(msTreatment.getVTreatmentName());
		}
	}

	public MsLabTreatmentDetil getMsLabTreatmentDetilById(Integer labDetilId) throws VONEAppException {
		return labDao.getMsLabTreatmentDetilById(labDetilId);
	}

}

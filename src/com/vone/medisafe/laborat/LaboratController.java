package com.vone.medisafe.laborat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.report.NoteReport;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.master.PatientTypeManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.PatientEscortController;
import com.vone.medisafe.ui.polyclinic.EditListener;

public class LaboratController extends BaseController {

	MsPatient patient;

	TbRegistration reg;

	MsUser user;

	MsStaffInUnit siu;

	TbExamination nota;

	TbTreatmentTrx treatmentTrx;

	boolean isRanap;

	Session session;

	Checkbox referencePatient;
	
	Listbox locationList;

	Bandbox transactionNumber;
	
	Bandbox dokterId;

	Textbox searchNota;

	Textbox searchName;

	Listbox notaList;

	Bandbox MRNumber;

	Textbox crNoMR;

	Textbox crNama;

	Textbox crNoAlamat;

	Listbox MRNumberList;

	Textbox registrationNumber;

	Textbox patientName;

	Radiogroup gender;

	Radio male;

	Radio female;

	Datebox dateOfBirth;

	Textbox age;

	Textbox address;

	Listbox patientTypeList;

	Textbox mainDoctor;

	Listbox patientEscort;

	Textbox hall;

	Textbox bed;

	Label labelStatus;

	Label statusNota;

	Listbox laboratoryList;

	Decimalbox total;

	Button calculateBtn;

	Button btnDelete;

	Button btnLabPanel;

	Button btnTreatmentAdd;

	Button btnSave;

	Button btnModify;

	Button btnCancel;

	Button btnNew;

	Button btnCancelNote;

	Button btnValidation;

	Button btnCetak;

//	Button btnEnd;

	Listitem item;

	Listcell cell;

	String ranapKelas;

	ZulConstraint constraints = new ZulConstraint();

	boolean isUpdate;

	TbItemTrx itemTrx;

	TbBundledTrx bundledTrx;

	Button btnMiscAdd;
	
	LaboratManager labManager = MasterServiceLocator.getLaboratManager(); 
	UnitManager unitService = MasterServiceLocator.getUnitManager();
	PatientTypeManager patientTypeService = MasterServiceLocator.getPatientTypeManager();
	NoteManager notaManager  = MasterServiceLocator.getNoteManager();

	private UserInfoBean uib;
	
	public void init(Component win) throws InterruptedException,
			VONEAppException {

		super.init(win);

		referencePatient = (Checkbox) win.getFellow("referencePatient");
		locationList = (Listbox) win.getFellow("locationList");
		transactionNumber = (Bandbox) win.getFellow("transactionNumber");
		dokterId = (Bandbox) win.getFellow("dokterId");
		searchNota = (Textbox) win.getFellow("searchNota");
		searchName = (Textbox) win.getFellow("searchName");
		notaList = (Listbox) win.getFellow("notaList");
		MRNumber = (Bandbox) win.getFellow("MRNumber");
		crNoMR = (Textbox) win.getFellow("crNoMR");
		crNama = (Textbox) win.getFellow("crNama");
		crNoAlamat = (Textbox) win.getFellow("crNoAlamat");
		MRNumberList = (Listbox) win.getFellow("MRNumberList");
		registrationNumber = (Textbox) win.getFellow("registrationNumber");
		patientName = (Textbox) win.getFellow("patientName");
		gender = (Radiogroup) win.getFellow("gender");
		male = (Radio) win.getFellow("male");
		female = (Radio) win.getFellow("female");
		dateOfBirth = (Datebox) win.getFellow("dateOfBirth");
		age = (Textbox) win.getFellow("age");
		address = (Textbox) win.getFellow("address");
		patientTypeList = (Listbox) win.getFellow("patientTypeList");
		mainDoctor = (Textbox) win.getFellow("mainDoctor");
		patientEscort = (Listbox) win.getFellow("patientEscort");

		btnMiscAdd = (Button) win.getFellow("btnMiscAdd");
		hall = (Textbox) win.getFellow("hall");
		bed = (Textbox) win.getFellow("bed");

		labelStatus = (Label) win.getFellow("labelStatus");
		statusNota = (Label) win.getFellow("statusNota");

		laboratoryList = (Listbox) win.getFellow("laboratoryList");
		total = (Decimalbox) win.getFellow("total");

		calculateBtn = (Button) win.getFellow("calculateBtn");
		btnDelete = (Button) win.getFellow("btnDelete");

		btnLabPanel = (Button) win.getFellow("btnLabPanel");
		btnTreatmentAdd = (Button) win.getFellow("btnTreatmentAdd");

		btnSave = (Button) win.getFellow("btnSave");
		btnModify = (Button) win.getFellow("btnModify");
		//btnCancel = (Button) win.getFellow("btnCancel");
		btnNew = (Button) win.getFellow("btnNew");
		btnCancelNote = (Button) win.getFellow("btnCancelNote");
		btnValidation = (Button) win.getFellow("btnValidation");

		btnCetak = (Button) win.getFellow("btnCetak");
//		btnEnd = (Button) win.getFellow("btnEnd");
		
		constraints.registerComponent(patientName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(address, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);

		constraints.registerComponent(patientName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(gender, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(dateOfBirth, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(address, ZulConstraint.NO_EMPTY);

		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);

		constraints.validateComponent(false);

		session = win.getDesktop().getSession();
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_LABORAT);

		MRNumberList.getItems().clear();
		laboratoryList.getItems().clear();
		notaList.getItems().clear();
		locationList.getItems().clear();
		
//		user = getUserInfoBean().getMsUser();
		uib = (UserInfoBean) session.getAttribute(Constant.USER_INFO);
		user = uib.getMsUser();
		Service.getRadiologyManager().getMsStaffInUnits(user, locationList);

		patientTypeService.getPatientTypeForSelect(patientTypeList);
		PatientEscortController.getPatientEscortForSelect(patientEscort);

		btnModify.setDisabled(true);
		// btnNew.setDisabled(true);
		btnCancelNote.setDisabled(true);
		btnValidation.setDisabled(true);
		btnCetak.setDisabled(true);
//		btnEnd.setDisabled(true);

		statusNota.setVisible(false);
		labelStatus.setVisible(false);

		MRNumber.focus();
		laboratoryList.setAttribute("jumlah", total);
		total.setFormat("#,###.##");
		
		

	}

	public void setMRVisible(Checkbox rujukan, Bandbox mr, Textbox regNumber) {
		if (rujukan.isChecked()) {
			mr.setVisible(false);
			mainDoctor.setVisible(false);
			regNumber.setVisible(false);
			hall.setVisible(false);
			bed.setVisible(false);
			patientName.focus();

		} else {
			mr.setVisible(true);
			mainDoctor.setVisible(true);
			regNumber.setVisible(true);
			hall.setVisible(true);
			bed.setVisible(true);
			MRNumber.focus();
		}
	}

	public void checkRegistration(Component win) {
		total.setValue(null);
		String ranap = "NO";
		session.setAttribute("location", locationList);
		session.setAttribute("listbox", laboratoryList);
		if (referencePatient.isChecked()) {
			session.setAttribute("isRanap", ranap);
			session.setAttribute("listbox", laboratoryList);
			// patientName.focus();
			isRanap = false;
		} else {
			
			if(patientTypeList.getSelectedItem() != null && patientTypeList.getSelectedIndex() != 0){
				MsPatientType type = (MsPatientType)patientTypeList.getSelectedItem().getValue();
				if(type.getVTpatient().equalsIgnoreCase("4")){
					ranapKelas = "BPJS";
					session.setAttribute("BPJS", "YES");
				}
				else {
					session.setAttribute("BPJS", "NO");
				}
					
			}

			
			if (isRanap) {
				// ambilKelas ranap
				ranap = "YES";
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", laboratoryList);
				session.setAttribute("kelasTarif", ranapKelas);

			} else {
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", laboratoryList);
			}
		}

	}

	public void getRegistration(int type) throws VONEAppException {
		labManager.getRegistration(this, type);
	}

	public void setFieldDisable(boolean isDisable) {
		referencePatient.setDisabled(isDisable);
		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
		registrationNumber.setReadonly(isDisable);
		patientName.setReadonly(isDisable);
		((Radio) gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio) gender.getChildren().get(1)).setDisabled(isDisable);
		dateOfBirth.setDisabled(isDisable);
		age.setReadonly(isDisable);
		mainDoctor.setReadonly(isDisable);
		patientEscort.setDisabled(isDisable);
		address.setReadonly(isDisable);
		patientTypeList.setDisabled(isDisable);
		laboratoryList.setDisabled(isDisable);
		total.setReadonly(isDisable);
	}

	public void clear() {

		locationList.setSelectedIndex(0);
		transactionNumber.setValue(null);
		MRNumber.setValue(null);
		registrationNumber.setValue(null);
		patientName.setValue(null);
		((Radio) gender.getChildren().get(0)).setSelected(false);
		((Radio) gender.getChildren().get(1)).setSelected(false);
		dateOfBirth.setValue(null);
		age.setValue(null);
		mainDoctor.setValue(null);
		patientEscort.setSelectedIndex(0);
		address.setValue(null);
		patientTypeList.setSelectedIndex(0);
		total.setValue(null);
		bed.setValue(null);
		hall.setValue(null);

		laboratoryList.getItems().clear();
		// laboratoryList.setSelectedIndex(0);

		referencePatient.setChecked(false);
		MRNumber.setVisible(true);
		mainDoctor.setVisible(true);
		registrationNumber.setVisible(true);
		registrationNumber.setReadonly(true);
		transactionNumber.setDisabled(false);

		this.patient = null;

		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		MRNumberList.getItems().clear();
		notaList.getItems().clear();
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		crNoMR.setValue(null);
		searchName.setValue(null);
		searchNota.setValue(null);
		dokterId.setValue(null);
		dokterId.removeAttribute("doctor");
		
		MRNumber.focus();

		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);

	}

	public void getNewTransaction() {
		statusNota.setVisible(false);
		setFieldDisable(false);
		total.setDisabled(true);
		setInnerButtonDisable(false);
		setButtonActive(true);
		bed.setVisible(true);
		hall.setVisible(true);
		MRNumber.focus();
		clear();
		this.reg = null;
		this.patient = null;
		this.itemTrx = null;
		this.treatmentTrx = null;
		this.nota = null;
	}

	public void setInnerButtonDisable(boolean isDisable) {
		// btnBundleAdd.setDisabled(isDisable);
		// btnItemAdd.setDisabled(isDisable);

		btnLabPanel.setDisabled(isDisable);
		btnTreatmentAdd.setDisabled(isDisable);
		/*btnModify.setDisabled(isDisable);*/
		/*btnCancel.setDisabled(isDisable);*/
		btnDelete.setDisabled(isDisable);
		calculateBtn.setDisabled(isDisable);
		btnMiscAdd.setDisabled(isDisable);
	}

	public void setButtonActive(boolean isActive) {
		this.btnCancelNote.setDisabled(isActive);
		this.btnValidation.setDisabled(isActive);
		// this.btnNew.setDisabled(isActive);
		this.btnModify.setDisabled(isActive);
		this.btnCetak.setDisabled(isActive);
		this.btnSave.setDisabled(!isActive);
	}

	public void delete() throws InterruptedException {

		if (laboratoryList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		int index = laboratoryList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		laboratoryList.removeItemAt(index);
		total.setValue(null);
	}

	public void calculateTotalAmount() throws InterruptedException {
		List jumlah = laboratoryList.getItems();
		double jumlahTotal = 0;
		Object[] obj = null;

		Iterator it = jumlah.iterator();
		while (it.hasNext()) {
			item = (Listitem) it.next();
			// obj[0]= harga sebelum diskon -> BigDecimal
			// obj[1]= harga setelah diskon -> BigDecimal
			// obj[2]= tipe diskon -> RP atau %
			// obj[3] = banyaknya item, selian item jumlah=1

			if (item.getAttribute("manipulation") != null) {
				obj = (Object[]) item.getAttribute("manipulation");
				jumlahTotal = jumlahTotal + ((BigDecimal) obj[1]).doubleValue();
			}
		}
		total.setValue(new BigDecimal(jumlahTotal));
		laboratoryList.setAttribute("jumlah", total);
		MiscTrxController.setFont(laboratoryList);
	}

	public void save() throws InterruptedException, VONEAppException {
		
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}

		if ((patientName.getText().trim() == "")
				|| (laboratoryList.getItems().size() == 0)) {
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if (total.getValue() == null) {
			Messagebox.show(MessagesService
					.getKey("common.transaction.total.not.counted"));
			return;
		}
		if (MRNumber.getValue().trim().equals("")) {
			if (!referencePatient.isChecked()) {
				Messagebox.show(MessagesService.getKey("rujukan.not.selecetd"));
				return;
			}
		}

		// step
		// simpan data pasien (bila rujukan)
		// simpan ke dalam tabel examination, generate note here, assign juga
		// siapa pasiennya
		// masukan ke tabel transaksi, lakukan updating langsung terhadap jumlah
		// item di inventory (paling ruwet)
		
		nota = new TbExamination();
		
		if (patientEscort.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG)
			nota.setMsPatientEscort((MsPatientEscort) patientEscort
					.getSelectedItem().getValue());
		
		nota.setMsUnit((MsUnit) locationList.getSelectedItem().getValue());
		nota.setTotal(new Double(total.getValue().doubleValue()));
		
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		nota.setVWhoCreate(user.getVUserName());
		
		if(reg != null)	nota.setTbRegistration(reg);
		

		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		
		if(dokterId.getValue()!= null && !dokterId.getValue().equals(""))
			laboratoryList.setAttribute("doctorLab", dokterId.getAttribute("doctor"));
		else laboratoryList.removeAttribute("doctorLab");
		
		//MiscTrxController.saveTrx(laboratoryList, user, treatmentSet, itemSet, miscSet);
		notaManager.saveTrx(laboratoryList, user, treatmentSet, itemSet, miscSet);
		
		if (referencePatient.isChecked()) {
			// simpan transaksi poliklinik utk pasien rujukan
			// data pasien : nama, alamat, tanggal lahir, jenis kelamin, type
			// pasien;
			if (!constraints.validateComponent(true))
				return;
			patient = new MsPatient();
			patient.setVPatientName(patientName.getText());
			patient.setDPatientDob(dateOfBirth.getValue());
			patient.setVPatientMainAddr(address.getText());
			patient.setVPatientGender(gender.getSelectedItem().getValue());
			if (patientTypeList.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG)
				patient.setMsPatientType((MsPatientType) patientTypeList
						.getSelectedItem().getValue());

			if(Service.getPolyclinicManager().save(patient, nota, treatmentSet, itemSet, bundledSet , miscSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);

				statusNota.setVisible(true);
				labelStatus.setVisible(true);

				statusNota.setValue(MedisafeUtil.getNoteStatus(nota
						.getNExamStatus().intValue()));

				setFieldDisable(true);
				setButtonActive(false);
				setInnerButtonDisable(true);
				btnModify.setDisabled(true);

			} else {
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		} else {
			if(Service.getPolyclinicManager().save(patient, nota, treatmentSet, itemSet, bundledSet , miscSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);

				statusNota.setVisible(true);
				labelStatus.setVisible(true);

				statusNota.setValue(MedisafeUtil.getNoteStatus(nota
						.getNExamStatus().intValue()));

				setFieldDisable(true);
				setButtonActive(false);
				setInnerButtonDisable(true);
//				btnModify.setDisabled(true);
			} else {
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		}
		transactionNumber.setAttribute("nota", nota);
	}

	// PENCARIAN NOTA
	public void searchNote() throws VONEAppException {
		
		labManager.searchNote(this);

	}

	public void getNote() {
		TbExamination nota = (TbExamination) transactionNumber
				.getAttribute("nota");

		LaboratController pc = new LaboratController();
		Object[] obj = new Object[] { nota, pc,
				new Integer(MedisafeConstants.NOTA), btnValidation, btnModify,
				btnCancelNote, statusNota };
		session.setAttribute("validasi", obj);

	}

	public void cancelNote() throws VONEAppException {

		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_LAB);
		
		TbExamination nota = (TbExamination) transactionNumber.getAttribute("nota");
		
		Integer whouseId = null;
//		whouseId = labManager.getWhoustId(locationList);
		
		//		Object[] obj = new Object[] { nota, laboratoryList.getItems(),
//				new Integer(MedisafeConstants.NOTA), btnValidation, btnModify,
//				btnCancelNote, statusNota, siu };
		Object[] obj = new Object[]{nota,whouseId,
				new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,btnCancelNote,statusNota, permission};
		session.setAttribute("cancelNote", obj);

		labelStatus.setVisible(true);
		statusNota.setVisible(true);
	}

	public void getNoteDetail() throws VONEAppException {
		
		labManager.getNoteDetail(this);
		MiscTrxController.setFont(laboratoryList);
	}
	
	public void cetak() throws InterruptedException, VONEAppException, IOException{
		
		NoteReport nr = new NoteReport(nota);
		//nr.screenOut();
		if(!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU!!!");
		}
			
	}
	
	public boolean cetakKartu() throws VONEAppException{
		
		boolean success = false;
		
		String query = "select mr.v_mr_code, pat.v_patient_name, pat.v_patient_main_addr, pat.d_patient_dob"+
		  " from ms_patient pat, tb_medical_record mr" +
		  " where pat.n_patient_id=mr.n_patient_id and pat.n_patient_id='" + patient.getNPatientId().intValue()+"'";
		
		ReportEngine rep = new ReportEngine(query, ReportService.getKey("kartuPasien"));
		rep.openXls();
		
		success = true;
		
		return success;
	}
	
	@SuppressWarnings("unchecked")
	public void modify() throws VONEAppException{
		
		Listbox polyclinicList = laboratoryList;
		
		Service.getNotaManager().getNoteDetil(nota, polyclinicList);

		List<Listitem> items = polyclinicList.getItems();
		
		Decimalbox diskon = null;
		Intbox jumlah = null;
		Listbox diskonOptionList = null;
		
		Listcell cell = null;
		
		for(Listitem item : items)
		{
			cell = (Listcell)item.getChildren().get(2);
			try {
				jumlah = (Intbox)cell.getChildren().get(0);
			} catch (RuntimeException e) {
				
				jumlah = new Intbox();
				jumlah.setValue(new Integer(cell.getLabel()));
				cell.setLabel("");
				jumlah.setParent(cell);
			}
			jumlah.setDisabled(false);
			jumlah.addEventListener("onChange", new EditListener(item,polyclinicList,total));
			
			cell = (Listcell)item.getChildren().get(5);
			diskon = (Decimalbox)cell.getChildren().get(0);
			diskon.addEventListener("onChange", new EditListener(item,polyclinicList,total));
			
			diskonOptionList = (Listbox)cell.getChildren().get(1);
			diskonOptionList.addEventListener("onSelect", new EditListener(item,polyclinicList,total));
			
			diskon.setDisabled(false);
			diskonOptionList.setDisabled(false);
			
		}
		setButtonActive(true);
		this.btnSave.setDisabled(false);
		isUpdate = true;
	}
	
	
	@SuppressWarnings("unchecked")
	public void saveModify() throws VONEAppException, InterruptedException
	{
		Date tanggal = new Date();
		
		nota.setTotal(this.total.getValue().doubleValue());
		nota.setDWhnChange(tanggal);
		
		user = uib.getMsUser();
		nota.setVWhoChange(user.getVUserName());
		
		
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		
		Decimalbox db = new Decimalbox();
		db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		
		double trxValue = 0;
		Intbox jumlah = null;
		Decimalbox diskon = null;
		Listbox opsiDiskon = null;
		
		Listbox polyclinicList =  this.laboratoryList;
		List<Listitem> items = polyclinicList .getItems();
		for(Listitem item : items){
			if(item.getValue() instanceof Object[]){
				
				itemTrx = new TbItemTrx();
				
				Object[] object = (Object[])item.getValue();
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)object[0]);
				itemTrx.setMsItem(msItem);
				
				cell = (Listcell)item.getChildren().get(2);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				itemTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				itemTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				itemTrx.setNDiscAmount(diskon.getValue().doubleValue());
				
				itemTrx.setNQty(new Float(jumlah.intValue()));
				itemTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				itemTrx.setVWhoCreate(user.getVUserName());
				itemSet.add(itemTrx);
			}
			else if(item.getValue() instanceof TbTreatmentTrx){
				treatmentTrx = (TbTreatmentTrx)item.getValue();
				treatmentTrx.setDWhnChange(tanggal);
				
				cell = (Listcell)item.getChildren().get(2);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				treatmentTrx.setNQty(jumlah.getValue().shortValue());
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				treatmentTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				treatmentTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				treatmentTrx.setNDiscAmount(diskon.getValue().doubleValue());
				treatmentTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				
				if(dokterId.getValue()!= null && !dokterId.getValue().equals(""))
					treatmentTrx.setMsDoctor((MsStaff)dokterId.getAttribute("doctor"));
				
				treatmentSet.add(treatmentTrx);
			}
			else if(item.getValue() instanceof TbBundledTrx){
				bundledTrx = (TbBundledTrx)item.getValue();
				bundledTrx.setDWhnChange(tanggal);
				
				cell = (Listcell)item.getChildren().get(2);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				bundledTrx.setNQty(jumlah.getValue().shortValue());
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				bundledTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				bundledTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				bundledTrx.setNDiscAmount(diskon.getValue().doubleValue());
				bundledTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				
				bundledSet.add(bundledTrx);
			}
		}
		
		Set<TbMiscTrx> miscSet = MiscTrxController.saveModifyMiscTrx(polyclinicList, user);
		
//		if(notaManager.saveModifyNote(nota, itemSet, treatmentSet, bundledSet, miscSet, null, locationList)){
//			
//			Messagebox.show(MessagesService.getKey("common.modify.success"));
//		}
//		
//		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		NoteManager manager = Service.getNotaManager();
//		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, bundledSet, miscSet , null, null)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}


}

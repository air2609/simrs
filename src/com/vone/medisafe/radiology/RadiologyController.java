package com.vone.medisafe.radiology;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
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
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.laborat.LaboratManager;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsPatientType;
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
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.master.PatientTypeManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.PatientEscortController;
import com.vone.medisafe.ui.polyclinic.EditListener;
import com.vone.medisafe.ui.polyclinic.PolyclinicController;

public class RadiologyController extends BaseController{
	//Window RadiologyTransaction;
	Checkbox referencePatient;
	Listbox locationList;
	Bandbox transactionNumber;
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
	Listbox radiologyList;
	Decimalbox total;
	Button calculateBtn;
	Button btnDelete;
	Button btnTreatmentAdd;
	Button btnItemAdd;
	Button btnSave;
	Button btnModify;
	Button btnNew;
	Button btnCancelNote;
	Button btnValidation;
	Button btnCetak;
	//Button btnEnd;
	Button btnMiscAdd;
	
	Session session;
	MsUser user;
	MsStaffInUnit siu;
	Listitem item;
	TbRegistration reg;
	TbExamination nota;
	TbItemTrx itemTrx;
	TbTreatmentTrx treatmentTrx;

	Logger logger = Logger.getLogger(RadiologyController.class);
	boolean isRanap;
	String ranapKelas;
	MsPatient patient;
	
	ZulConstraint constraints = new ZulConstraint();

	Listcell cell;
	boolean isUpdate = false;
	TbBundledTrx bundledTrx;
	
	
	UserInfoBean uib;

	LaboratManager labManager = MasterServiceLocator.getLaboratManager(); 
	NoteManager notaManager = Service.getNotaManager();
	RadiologyManager radiologyManager = Service.getRadiologyManager();
	UnitManager unitService = MasterServiceLocator.getUnitManager();
	PatientTypeManager patientTypeService = MasterServiceLocator.getPatientTypeManager();
	
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		logger.debug("RadiologyController.init()");
		
		//RadiologyTransaction = (Window)win.getFellow("RadiologyTransaction");
		referencePatient = (Checkbox)win.getFellow("referencePatient");
		locationList = (Listbox)win.getFellow("locationList");
		
		transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		searchNota = (Textbox)win.getFellow("searchNota");
		searchName = (Textbox)win.getFellow("searchName");
		notaList = (Listbox)win.getFellow("notaList");
		
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		MRNumberList = (Listbox)win.getFellow("MRNumberList");
		
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		patientName = (Textbox)win.getFellow("patientName");
		gender = (Radiogroup)win.getFellow("gender");
		male = (Radio)win.getFellow("male");
		female = (Radio)win.getFellow("female");
		dateOfBirth = (Datebox)win.getFellow("dateOfBirth");
		age = (Textbox)win.getFellow("age");
		address = (Textbox)win.getFellow("address");
		patientTypeList = (Listbox)win.getFellow("patientTypeList");
		mainDoctor = (Textbox)win.getFellow("mainDoctor");
		patientEscort = (Listbox)win.getFellow("patientEscort");
		hall = (Textbox)win.getFellow("hall");
		bed = (Textbox)win.getFellow("bed");
		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		
		radiologyList = (Listbox)win.getFellow("radiologyList");
		total = (Decimalbox)win.getFellow("total");
		
		calculateBtn = (Button)win.getFellow("calculateBtn");
		btnDelete = (Button)win.getFellow("btnDelete");
		
		btnTreatmentAdd = (Button)win.getFellow("btnTreatmentAdd");
		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
	//	btnEnd = (Button)win.getFellow("btnEnd");
		btnMiscAdd = (Button)win.getFellow("btnMiscAdd");
		
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

		super.init(win);
		
		session = win.getDesktop().getSession(); 

		uib = (UserInfoBean) session.getAttribute(Constant.USER_INFO);
		user = uib.getMsUser();
		Service.getRadiologyManager().getMsStaffInUnits(user, locationList);

		patientTypeService.getPatientTypeForSelect(patientTypeList);
		PatientEscortController.getPatientEscortForSelect(patientEscort);
		
		
		MRNumber.focus();
		MRNumberList.getItems().clear();
		notaList.getItems().clear();
		radiologyList.getItems().clear();
		
		session.setAttribute("location", locationList);
		session.setAttribute("listbox", radiologyList);
		session.setAttribute("isRanap", isRanap);
		session.setAttribute("kelasTarif", ranapKelas);
		radiologyList.setAttribute("jumlah", total);
		
		total.setFormat("#,###.##");
		setInnerButtonDisable(false);
		setButtonActive(true);
		
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY, MedisafeConstants.COMMON_RADIOLOGY);
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		MRNumber.focus();
		
	}

	public void notaSelect() {
		logger.debug("RadiologyController.notaSelect()");
	}

	public void pasienSelect() throws VONEAppException {
		logger.debug("RadiologyController.pasienSelect()");
		getRegistration(MedisafeConstants.INPUT_BY_SEARCH);
	}

	public void cariPasienClick() {
		logger.debug("RadiologyController.cariPasienClick()");
	}

	public void tambahTindakanClick() {
		logger.debug("RadiologyController.tambahTindakanClick()");
	}

	public void cariNotaClick() throws InterruptedException, WrongValueException, VONEAppException {
		
		radiologyManager.cariNotaClick(this);
		
	}

	public void tambahObmClick() {
		logger.debug("RadiologyController.tambahObmClick()");
		//muncul error null pointer, karena n_whouse_id kosong 
	}

	public void cetakClick() throws InterruptedException, VONEAppException {
		logger.debug("RadiologyController.cetakClick()");
		NoteReport nr = new NoteReport(nota);
		
		if(!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU!!!");
		}
			

	}

	public void baruClick() {
		logger.debug("RadiologyController.baruClick()");
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

		radiologyList.getItems().clear();

		referencePatient.setChecked(false);
		MRNumber.setVisible(true);
		mainDoctor.setVisible(true);
		registrationNumber.setVisible(true);
		registrationNumber.setDisabled(true);
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
		
		MRNumber.focus();
		setFieldDisable(false);
		hall.setVisible(true);
		bed.setVisible(true);
		
		setInnerButtonDisable(false);
		setButtonActive(true);
		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
		
		
		
		//clear
		locationList.setSelectedIndex(0);
		transactionNumber.setValue(null);
		MRNumber.setValue(null);
		registrationNumber.setValue(null);
		patientName.setValue(null);
		((Radio)gender.getChildren().get(0)).setSelected(false);
		((Radio)gender.getChildren().get(1)).setSelected(false);
		dateOfBirth.setValue(null);
		age.setValue(null);
		mainDoctor.setValue(null);
		patientEscort.setSelectedIndex(0);
		address.setValue(null);
		patientTypeList.setSelectedIndex(0);
		total.setValue(null);
		referencePatient.setChecked(false);
		MRNumber.setVisible(true);
		mainDoctor.setVisible(true);
		registrationNumber.setVisible(true);
		registrationNumber.setDisabled(true);
		transactionNumber.setDisabled(false);
		this.patient = null;
		this.itemTrx = null;
		this.reg = null;
		/*this.nota = null;*/
		this.treatmentTrx = null;
		
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		MRNumberList.getItems().clear();
		notaList.getItems().clear();
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		crNoMR.setValue(null);
		searchName.setValue(null);
		searchNota.setValue(null);
		total.setDisabled(true);
		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
		//
		mainDoctor.setDisabled(true);
		MRNumber.focus();
		
	}

	public void simpanClick() throws InterruptedException, VONEAppException {
		
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}

		logger.debug("RadiologyController.simpanClick()");
		if ((patientName.getText().trim() == "")
				|| (radiologyList.getItems().size() == 0)) {
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
		
		nota = new TbExamination();
		nota.setVWhoCreate(user.getVUserName());
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		if(patientEscort.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG)
			nota.setMsPatientEscort((MsPatientEscort)patientEscort.getSelectedItem().getValue());
		nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		if(reg != null)	nota.setTbRegistration(reg);
		nota.setMsUnit((MsUnit)locationList.getSelectedItem().getValue());
		nota.setTotal(new Double(total.getValue().doubleValue()));
		
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);

		MiscTrxController.saveTrx(radiologyList, user, treatmentSet, itemSet, miscSet);
		
		if(referencePatient.isChecked()){
			//simpan transaksi poliklinik utk pasien rujukan
			//data pasien : nama, alamat, tanggal lahir, jenis kelamin, type pasien;
			if(!constraints.validateComponent(true))return;
			patient = new MsPatient();
			patient.setVPatientName(patientName.getText());
			patient.setDPatientDob(dateOfBirth.getValue());
			patient.setVPatientMainAddr(address.getText());
//			if(gender.getSelectedItem().getValue() )
			patient.setVPatientGender(gender.getSelectedItem().getValue());
			if(patientTypeList.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG)
				patient.setMsPatientType((MsPatientType)patientTypeList.getSelectedItem().getValue());
			
			if(Service.getPolyclinicManager().save(patient, nota, treatmentSet, itemSet, bundledSet , miscSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);
				
				statusNota.setVisible(true);
				labelStatus.setVisible(true);
				
				statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
				setFieldDisable(true);
				setButtonActive(false);
				setInnerButtonDisable(true);
				this.btnModify.setDisabled(true);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
			
		
		}else{
//			if(radiologyManager.save(patient, nota, treatmentSet, itemSet,  
//					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)){
			if(Service.getPolyclinicManager().save(patient, nota, treatmentSet, itemSet, bundledSet, miscSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);
				
				statusNota.setVisible(true);
				labelStatus.setVisible(true);
				statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
				
				setFieldDisable(true);
				setButtonActive(false);
				setInnerButtonDisable(true);
				this.btnModify.setDisabled(true);
			}else{
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		}
		
		transactionNumber.setAttribute("nota", nota);

	}

	public void getRegistration(int type) throws VONEAppException{
		
		radiologyManager.getRegistration(this, type);

	}

	public void setFieldDisable(boolean isDisable) {
		referencePatient.setDisabled(isDisable);
		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
		registrationNumber.setDisabled(isDisable);
		patientName.setDisabled(isDisable);
		((Radio)gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio)gender.getChildren().get(1)).setDisabled(isDisable);
		dateOfBirth.setDisabled(isDisable);
		age.setDisabled(isDisable);
		mainDoctor.setDisabled(isDisable);
		patientEscort.setDisabled(isDisable);
		address.setDisabled(isDisable);
		patientTypeList.setDisabled(isDisable);
		radiologyList.setDisabled(isDisable);
		total.setDisabled(isDisable);
		mainDoctor.setDisabled(isDisable);
	}
	
	public void setMRVisible(Checkbox rujukan, Bandbox mr, Textbox regNumber) {
		if (rujukan.isChecked()) {
			mr.setVisible(false);
			mainDoctor.setVisible(false);
			regNumber.setVisible(false);
			hall.setVisible(false);
			bed.setVisible(false);
			patientName.focus();
			patient = null;
		} else {
			mr.setVisible(true);
			mainDoctor.setVisible(true);
			regNumber.setVisible(true);
			hall.setVisible(true);
			bed.setVisible(true);
			MRNumber.focus();
		}
	}
	
	public void calculateTotalAmount() throws InterruptedException {
		List jumlah = radiologyList.getItems();
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
		radiologyList.setAttribute("jumlah", total);

	}
	
	public void hapusClick() throws InterruptedException{
		logger.debug("RadiologyController.hapusClick()");
		if (radiologyList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		int index = radiologyList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		radiologyList.removeItemAt(index);
		total.setValue(null);
	}
	
	public void setButtonActive(boolean isActive){
		this.btnCancelNote.setDisabled(isActive);
		this.btnValidation.setDisabled(isActive);
		this.btnModify.setDisabled(isActive);
		this.btnCetak.setDisabled(isActive);
		this.btnSave.setDisabled(!isActive);
	}
	
	
	public void setInnerButtonDisable(boolean isDisable){
		btnItemAdd.setDisabled(isDisable);
		btnTreatmentAdd.setDisabled(isDisable);
		btnDelete.setDisabled(isDisable);
		calculateBtn.setDisabled(isDisable) ;
		btnMiscAdd.setDisabled(isDisable);
	}
	
	public void getNoteDetail() throws VONEAppException{
		
		radiologyManager.getNoteDetail(this);
		
	}
	
	public void getNote(){

		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		
		PolyclinicController pc = new PolyclinicController();
		Object[] obj = new Object[]{nota,pc,new Integer(MedisafeConstants.NOTA), btnValidation,btnModify,btnCancelNote,statusNota};
		session.setAttribute("validasi", obj);
		
	}
	public void cancelNote() throws VONEAppException{
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_RADIOLOGY);
		
		TbExamination nota = (TbExamination) transactionNumber
				.getAttribute("nota");
		Object[] obj = new Object[]{nota,((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId(),
				new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,btnCancelNote,statusNota,permission};
		session.setAttribute("cancelNote", obj);

		labelStatus.setVisible(true);
		statusNota.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public void modify() throws VONEAppException{
		
		Listbox polyclinicList = radiologyList;
		
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
		nota.setVWhoChange(user.getVUserName());
		
		
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		double trxValue = 0;
		Intbox jumlah = null;
		Decimalbox diskon = null;
		Listbox opsiDiskon = null;
		
		Listbox polyclinicList =  this.radiologyList;
		List<Listitem> items = polyclinicList.getItems();
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
				
				itemTrx.setDWhnChange(tanggal);
				itemTrx.setVWhoChange(user.getVUserName());
				
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
				
				treatmentTrx.setDWhnChange(tanggal);
				treatmentTrx.setVWhoChange(user.getVUserName());
				
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
				
				bundledTrx.setDWhnChange(tanggal);
				bundledTrx.setVWhoChange(user.getVUserName());
				
				bundledSet.add(bundledTrx);
			}
		}
		
		NoteManager manager = Service.getNotaManager();
		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		//warehouseId = manager.getWarehouseId()
		Set<TbMiscTrx> miscSet = MiscTrxController.saveModifyMiscTrx(polyclinicList, user);
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, bundledSet, miscSet , null, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}
	
	public void checkRegistration(){
		total.setValue(null);
		String ranap = "NO";
		session.setAttribute("location", locationList);
		session.setAttribute("listbox", radiologyList);
		if(referencePatient.isChecked()){
			session.setAttribute("isRanap", ranap);
			isRanap = false;
		}
		else{
			
//			if(patientTypeList.getSelectedItem() != null && patientTypeList.getSelectedIndex() != 0){
//				MsPatientType type = (MsPatientType)patientTypeList.getSelectedItem().getValue();
//				if(type.getVTpatient().equalsIgnoreCase("4"))
//					ranapKelas = "BPJS";
//					session.setAttribute("BPJS", "YES");
//			}else {
//				session.setAttribute("BPJS", "NO");
//			}
			
			if(isRanap){
				//ambilKelas ranap
				ranap="YES";
				session.setAttribute("isRanap", ranap);
				session.setAttribute("kelasTarif", ranapKelas);
				
			}
			else{
				session.setAttribute("isRanap", ranap);
			}
		}
	}
	
}

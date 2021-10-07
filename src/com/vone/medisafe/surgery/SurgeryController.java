package com.vone.medisafe.surgery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
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

import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsPatient;
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
import com.vone.medisafe.radiology.RadiologyManager;
import com.vone.medisafe.report.NoteReport;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.polyclinic.EditListener;
import com.vone.medisafe.ui.polyclinic.PolyclinicController;

public class SurgeryController extends BaseController{
	//Window RadiologyTransaction;
	Listbox locationList;
	public Bandbox transactionNumber;
	Textbox searchNota;
	Textbox searchName;
	public Listbox notaList;
	public Bandbox MRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	public Listbox MRNumberList;
	public Textbox registrationNumber;
	public Textbox patientName;
	public Radiogroup gender;
	Radio male;
	Radio female;
	public Textbox age;
	public Textbox mainDoctor;
	public Textbox hall;
	public Textbox bed;
	public Label labelStatus;
	public Label statusNota;
	public Listbox surgeryList;
	public Decimalbox total;
	public Textbox antriKelas;
	Button calculateBtn;
	Button btnDelete;
	Button btnTreatmentAdd;
	Button btnItemAdd;
	Button btnSave;
	
	public Button btnModify;
	//Button btnCancel;
	Button btnNew;
	Button btnCancelNote;
	public Button btnValidation;
	Button btnCetak;
//	Button btnEnd;
	Button btnMiscAdd;
//	Checkbox referencePatient;
//	Datebox dateOfBirth;
//	Textbox address;
//	Listbox patientTypeList;
//	Listbox patientEscort;
	
//	referencePatient = (Checkbox)win.getFellow("referencePatient");
//	dateOfBirth = (Datebox)win.getFellow("dateOfBirth");
//	address = (Textbox)win.getFellow("address");
//	patientTypeList = (Listbox)win.getFellow("patientTypeList");
//	patientEscort = (Listbox)win.getFellow("patientEscort");
	
	public Session session;
	MsUser user;
	MsStaffInUnit siu;
	public Listitem item;
	public TbRegistration reg;
	public TbExamination nota;
	TbItemTrx itemTrx;
	TbTreatmentTrx treatmentTrx;

	Logger logger = Logger.getLogger(SurgeryController.class);
	public boolean isRanap;
	public String ranapKelas;
	public MsPatient patient;
	
	ZulConstraint constraints = new ZulConstraint();
	Listcell cell;
	boolean isUpdate;
	
	private RadiologyManager radiologyManager = Service.getRadiologyManager();
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		logger.debug("RadiologyController.init()");
		
		//RadiologyTransaction = (Window)win.getFellow("RadiologyTransaction");
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
		age = (Textbox)win.getFellow("age");
		mainDoctor = (Textbox)win.getFellow("mainDoctor");
		hall = (Textbox)win.getFellow("hall");
		bed = (Textbox)win.getFellow("bed");
		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		antriKelas = (Textbox)win.getFellow("antriKelas");
		
		surgeryList = (Listbox)win.getFellow("surgeryList");
		total = (Decimalbox)win.getFellow("total");
		
		calculateBtn = (Button)win.getFellow("calculateBtn");
		btnDelete = (Button)win.getFellow("btnDelete");
		
		btnTreatmentAdd = (Button)win.getFellow("btnTreatmentAdd");
		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
	//	btnCancel = (Button)win.getFellow("btnCancel");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");
		btnMiscAdd = (Button)win.getFellow("btnMiscAdd");
		constraints.registerComponent(patientName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);

		constraints.registerComponent(patientName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(gender, ZulConstraint.NO_EMPTY);

		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);

		constraints.validateComponent(false);

		super.init(win);
		
		session = win.getDesktop().getSession(); 

		user = getUserInfoBean().getMsUser();
		
		radiologyManager.getMsStaffInUnits(user, locationList);


		MRNumberList.getItems().clear();
		notaList.getItems().clear();
		surgeryList.getItems().clear();
		
		session.setAttribute("location", locationList);
		session.setAttribute("listbox", surgeryList);
		session.setAttribute("isRanap", isRanap);
		session.setAttribute("kelasTarif", ranapKelas);
		surgeryList.setAttribute("jumlah", total);
		session.setAttribute("antriKelas", antriKelas);
		
		total.setFormat("#,###.##");
		setInnerButtonDisable(false);
		setButtonActive(true);
		
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY, MedisafeConstants.COMMON_SURGERY);
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		
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

	public void cariNotaClick() throws VONEAppException {
		logger.debug("RadiologyController.cariNotaClick()");
		notaList.getItems().clear();

		statusNota.setVisible(false);
		labelStatus.setVisible(false);

		MsUnit unit = (MsUnit) locationList.getSelectedItem().getValue();

		//List result = 
		Service.getNotaManager().searchNote(
				"%" + searchNota.getValue() + "%",
				"%" + searchName.getValue() + "%", unit, notaList, MedisafeConstants.ACTIVE_NOTE);

//		Listitem item;
//		Listcell cell;
//		if (result.size()==0) {
//			Messagebox.show("TIDAK ADA DATA");
//			return;
//		}
//		Iterator it = result.iterator();
//		while (it.hasNext()) {
//			nota = (TbExamination) it.next();
//			item = new Listitem();
//			item.setValue(nota);
//			item.setParent(notaList);
//
//			cell = new Listcell();
//			cell.setParent(item);
//			cell.setLabel(nota.getVNoteNo());
//
//			cell = new Listcell();
//			cell.setParent(item);
//			cell.setLabel(nota.getMsPatient().getVPatientName());
//
//			cell = new Listcell();
//			cell.setParent(item);
//			cell.setLabel(MedisafeUtil.getNoteStatus(nota.getNExamStatus()
//					.intValue()));
//
//		}

	}

	public void tambahObmClick() {
		logger.debug("RadiologyController.tambahObmClick()");
		//muncul error null pointer, karena n_whouse_id kosong 
	}

	public void cetakClick() throws VONEAppException, InterruptedException {
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
		age.setValue(null);
		mainDoctor.setValue(null);
		total.setValue(null);
		bed.setValue(null);
		hall.setValue(null);
		antriKelas.setValue(null);

		surgeryList.getItems().clear();

//		referencePatient.setChecked(false);
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
		mainDoctor.setDisabled(true);
		registrationNumber.setDisabled(true);
		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
	}

	public void simpanClick() throws InterruptedException, VONEAppException {
		
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}


		logger.debug("RadiologyController.simpanClick()");
		if ((patientName.getText().trim() == "")
				|| (surgeryList.getItems().size() == 0)) {
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if (total.getValue() == null) {
			Messagebox.show(MessagesService
					.getKey("common.transaction.total.not.counted"));
			return;
		}

		nota = new TbExamination();
		nota.setVWhoCreate(user.getVUserName());
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		if(reg != null)	nota.setTbRegistration(reg);
		nota.setMsUnit((MsUnit)locationList.getSelectedItem().getValue());
		nota.setTotal(new Double(total.getValue().doubleValue()));
		
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		
		MiscTrxController.saveTrx(surgeryList, user, treatmentSet, itemSet, miscSet);
		
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
		}else{
			Messagebox.show(MessagesService.getKey("common.save.fail"));
		}
		transactionNumber.setAttribute("nota", nota);

	}

	public void getRegistration(int type) throws VONEAppException{
		
		radiologyManager.getRegistrationSurgery(this, type);
		
	}

	public void setFieldDisable(boolean isDisable) {
		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
		registrationNumber.setDisabled(isDisable);
		patientName.setDisabled(isDisable);
		((Radio)gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio)gender.getChildren().get(1)).setDisabled(isDisable);
		age.setDisabled(isDisable);
		mainDoctor.setDisabled(isDisable);
		surgeryList.setDisabled(isDisable);
		total.setDisabled(isDisable);		
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
	
	public void calculateTotalAmount() throws InterruptedException {
		List jumlah = surgeryList.getItems();
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
		surgeryList.setAttribute("jumlah", total);

	}
	
	public void hapusClick() throws InterruptedException{
		logger.debug("RadiologyController.hapusClick()");
		if (surgeryList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		int index = surgeryList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		surgeryList.removeItemAt(index);
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
	
	public void getNoteDetail() throws VONEAppException {

		radiologyManager.getNoteDetailSurgery(this);
		
	}
	public void getNote(){
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		
		PolyclinicController pc = new PolyclinicController();
		Object[] obj = new Object[]{nota,pc,new Integer(MedisafeConstants.NOTA), btnValidation,btnModify,btnCancelNote,statusNota};
		session.setAttribute("validasi", obj);
	}
	
	public void cancelNote() throws VONEAppException{
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_SURGERY);

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
		
		Listbox polyclinicList = surgeryList;
		
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
		
		Listbox polyclinicList =  this.surgeryList;
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
				treatmentSet.add(treatmentTrx);
			}
			else if(item.getValue() instanceof TbBundledTrx){
				TbBundledTrx bundledTrx = (TbBundledTrx)item.getValue();
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
		
		NoteManager manager = Service.getNotaManager();
		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		
		Set<TbMiscTrx> miscSet = MiscTrxController.saveModifyMiscTrx(polyclinicList, user);
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, bundledSet, miscSet , null, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}
}

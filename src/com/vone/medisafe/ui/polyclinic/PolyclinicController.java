package com.vone.medisafe.ui.polyclinic;

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


import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsTreatmentFee;
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
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.service.iface.polyclinic.PolyclinicManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.PatientEscortController;
import com.vone.medisafe.ui.master.PatientTypeController;


public class PolyclinicController extends BaseController{
	
	public  MsPatient patient;
	public TbRegistration reg;
	public TbExamination nota;
	public TbItemTrx itemTrx;
	public TbTreatmentTrx treatmentTrx;
	public TbBundledTrx bundledTrx;
	public MsUser user;
	public MsStaffInUnit siu;
	public boolean isRanap;
	public boolean isUpdate;
	
	Session session;
	public Checkbox referencePatient;
	public Listbox locationList;
	public Bandbox transactionNumber;
	public Textbox searchNota;
	public Textbox searchName;
	public Listbox notaList;
	public Bandbox MRNumber;
	public Listbox mRNumberList; 
	public Textbox registrationNumber;
	public Textbox patientName;
	public Radiogroup gender;
	public Datebox dateOfBirth;
	public Textbox age;
	public Bandbox mainDoctor;
	public Listbox patientEscort;
	public Textbox address;
	public Listbox patientTypeList;
	public Textbox crNoAlamat;;
	public Textbox crNama;
	public Textbox crNoMR;
	public Listbox polyclinicList;
	public Decimalbox total;
	public Label statusNota;
	public Label labelStatus;
	public Textbox doctorCode;
	public Textbox doctorName;
	public Listbox doctorList;
	
	public Button btnSave;
	public Button btnModify;
	public Button btnNew;
	public Button btnCancelNote;
	public Button btnValidation;
	public Button btnCetak;
//	public Button btnEnd;
	
	public Button btnBundleAdd;
	public Button btnItemAdd;
	public Button btnTreatmentAdd;
	public Button btnMiscAdd;
	public Button btnDelete;
	public Button calculateBtn;
	
	public Listitem item;
	public Listcell cell;
	
	public String ranapKelas;
	
	ZulConstraint constraints = new ZulConstraint();
	
	
	private PolyclinicManager polyServ = Service.getPolyclinicManager();
	
	UserManager userService = ServiceLocator.getUserManager();
	DoctorManager doctorService = MasterServiceLocator.getDoctorManager();
	
	

	public void init(Component win) throws InterruptedException, VONEAppException {
		
		referencePatient = (Checkbox)win.getFellow("referencePatient");
		locationList = (Listbox) win.getFellow("locationList");
		transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		searchNota = (Textbox)win.getFellow("searchNota");
		searchName = (Textbox)win.getFellow("searchName");
		notaList = (Listbox)win.getFellow("notaList");
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		mRNumberList = (Listbox)win.getFellow("MRNumberList");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		patientName = (Textbox)win.getFellow("patientName");
		gender = (Radiogroup)win.getFellow("gender");
		dateOfBirth = (Datebox)win.getFellow("dateOfBirth");
		age = (Textbox)win.getFellow("age");
		mainDoctor = (Bandbox)win.getFellow("mainDoctor");
		patientEscort = (Listbox)win.getFellow("patientEscort");
		address = (Textbox)win.getFellow("address");
		patientTypeList = (Listbox)win.getFellow("patientTypeList");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		crNama = (Textbox)win.getFellow("crNama");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		polyclinicList = (Listbox)win.getFellow("polyclinicList");
		total = (Decimalbox)win.getFellow("total");
		statusNota = (Label)win.getFellow("statusNota");
		labelStatus = (Label)win.getFellow("labelStatus");
		doctorCode = (Textbox)win.getFellow("doctorCode");
		doctorName = (Textbox)win.getFellow("doctorName");
		doctorList = (Listbox)win.getFellow("doctorList");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");
		
		btnBundleAdd = (Button)win.getFellow("btnBundleAdd");
		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		btnTreatmentAdd = (Button)win.getFellow("btnTreatmentAdd");
		btnMiscAdd = (Button)win.getFellow("btnMiscAdd");
		

		btnDelete = (Button)win.getFellow("btnDelete");
		calculateBtn = (Button)win.getFellow("calculateBtn");
		
		super.init(win);
		
		session = win.getDesktop().getSession(); 
		
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_POLY);
		
		user = getUserInfoBean().getMsUser();
		
		mRNumberList.getItems().clear();
		polyclinicList.getItems().clear();
		notaList.getItems().clear();
		
		constraints.registerComponent(patientName,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(address, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(patientName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(gender, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(dateOfBirth, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(address, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(mainDoctor, ZulConstraint.NO_EMPTY);
		
		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		btnModify.setDisabled(true);

		btnCancelNote.setDisabled(true);
		btnValidation.setDisabled(true);
		btnCetak.setDisabled(true);
//		btnEnd.setDisabled(true);
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		MRNumber.focus();
		
		polyclinicList.setAttribute("jumlah", total);
		
		userService.getUnitUser(user,locationList);
		
		
		
		PatientTypeController.getAllPatientTypeList2(patientTypeList);
		PatientEscortController.getPatientEscortForSelect(patientEscort);
		

	}
	
	public void setMRVisible(Checkbox rujukan, Bandbox mr, Textbox regNumber){
		if(rujukan.isChecked()){
			mr.setVisible(false);
			mainDoctor.setVisible(false);	
			regNumber.setVisible(false);
			patientName.focus();
			
		}
		else{
			mr.setVisible(true);
			mainDoctor.setVisible(true);
			regNumber.setVisible(true);
			MRNumber.focus();
		}
	}
	
	public void checkRegistration(Component win){
		total.setValue(null);
		String ranap = "NO";
		session.setAttribute("location", locationList);
		if(referencePatient.isChecked()){
			session.setAttribute("isRanap", ranap);
			session.setAttribute("listbox", polyclinicList);
//			patientName.focus();
			isRanap = false;
		}
		else{
			if(isRanap){
				//ambilKelas ranap
				ranap="YES";
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", polyclinicList);
				session.setAttribute("kelasTarif", ranapKelas);
				
			}
			else{
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", polyclinicList);
			}
		}
	}
	
	public void calculateTotalAmount() throws InterruptedException{
		List jumlah = polyclinicList.getItems();
		double jumlahTotal = 0;
		Object[] obj = null;
		
		Iterator it = jumlah.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			//obj[0]= harga sebelum diskon -> BigDecimal
			//obj[1]= harga setelah diskon -> BigDecimal
			//obj[2]= tipe diskon  -> RP atau %
			//obj[3] = banyaknya item, selian item jumlah=1
			
			if(item.getAttribute("manipulation") != null){
				obj = (Object[])item.getAttribute("manipulation");
				jumlahTotal = jumlahTotal + ((BigDecimal)obj[1]).doubleValue();
			}
		}
		total.setValue(new BigDecimal(jumlahTotal));
	}
	
	
	public void delete() throws InterruptedException{
		if(polyclinicList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int index = polyclinicList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		polyclinicList.removeItemAt(index);
		total.setValue(null);
		
		calculateTotalAmount();
	}
	
	
	public void save() throws InterruptedException, VONEAppException{
		
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}
		
		if((patientName.getText().trim() == "") || (polyclinicList.getItems().size() == 0)){
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if(total.getValue() == null){
			Messagebox.show(MessagesService.getKey("common.transaction.total.not.counted"));
			return;
		}
		if(MRNumber.getValue().trim().equals("")){
			if(!referencePatient.isChecked()){
				Messagebox.show(MessagesService.getKey("rujukan.not.selecetd"));
				return;
			}
		}
		
		//step
		//simpan data pasien (bila rujukan)
		//simpan ke dalam tabel examination, generate note here, assign juga siapa pasiennya
		//masukan ke tabel transaksi, lakukan updating langsung terhadap jumlah item di inventory (paling ruwet)
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		
		nota = new TbExamination();
		nota.setVWhoCreate(user.getVUserName());
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		if(patientEscort.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG)
			nota.setMsPatientEscort((MsPatientEscort)patientEscort.getSelectedItem().getValue());
		nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		if(reg != null)	nota.setTbRegistration(reg);
		nota.setMsUnit((MsUnit)locationList.getSelectedItem().getValue());
		nota.setTotal(new Double(total.getValue().doubleValue()));
		
		List list = polyclinicList.getItems();
		Iterator it = list.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			Object[] obj = (Object[])item.getAttribute("manipulation");
			if(item.getValue() instanceof MsTreatmentFee){
				treatmentTrx = new TbTreatmentTrx();
				treatmentTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				if(item.getAttribute("doctor") instanceof MsStaff){
					treatmentTrx.setMsDoctor((MsStaff)item.getAttribute("doctor"));
				}
				treatmentTrx.setVWhoCreate(user.getVUserName());
				treatmentTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				treatmentTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				
				Intbox quantity = (Intbox)obj[3];
				
				treatmentTrx.setNQty(quantity.getValue().shortValue());
				
				treatmentTrx.setVDiscType(obj[2].toString());
				treatmentTrx.setVWhoCreate(user.getVUserName());
				treatmentTrx.setMsTreatmentFee((MsTreatmentFee)item.getValue());
				treatmentSet.add(treatmentTrx);
			}
			else if(item.getValue() instanceof Object[]){
				itemTrx = new TbItemTrx();
				Object[] object = (Object[])item.getValue();
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)object[0]);
//				itemTrx.setMsItem(((MsItemSellingPrice)item.getValue()).getMsItem());
				itemTrx.setMsItem(msItem);
				itemTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				itemTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				itemTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
//				itemTrx.setNQty(((Double)obj[3]).floatValue());
				itemTrx.setNQty(new Float(obj[3].toString()));
				itemTrx.setVDiscType(obj[2].toString());
				itemTrx.setVWhoCreate(user.getVUserName());
				itemSet.add(itemTrx);
			}
			else if(item.getValue() instanceof TbMiscTrx){
				TbMiscTrx miscTrx = (TbMiscTrx)item.getValue();
				miscTrx.setVWhoCreate(user.getVUserName());
				miscTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				miscTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				miscTrx.setVDiscType(obj[2].toString());
				miscTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				miscSet.add(miscTrx);
			}
			else{
				//item instance of bundleTreatment
				bundledTrx = (TbBundledTrx) item.getValue();
				bundledTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				bundledTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				bundledTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				bundledTrx.setNQty((short)1);
				bundledTrx.setVDiscType(obj[2].toString());
				bundledTrx.setVWhoCreate(user.getVUserName());
				bundledSet.add(bundledTrx);
			}

		}
		
		
		
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
			
			if(polyServ.save(patient, nota, treatmentSet, itemSet, bundledSet, miscSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);
				
				statusNota.setVisible(true);
				labelStatus.setVisible(true);
				
				statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
				setFieldDisable(true);
				setButtonActive(false);
				this.btnModify.setDisabled(true);
				setInnerButtonDisable(true);
				
			}
			else{
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
			
		
		}else{
			if(polyServ.save(patient, nota, treatmentSet, itemSet, bundledSet, miscSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);
				
				statusNota.setVisible(true);
				labelStatus.setVisible(true);
				statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
				
				setFieldDisable(true);
				setButtonActive(false);
//				this.btnModify.setDisabled(true);
				setInnerButtonDisable(true);
			}else{
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		}
		transactionNumber.setAttribute("nota",nota);
		
		
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	
	
	
	public void getRegistration(int type) throws VONEAppException, InterruptedException{
		
		polyServ.getRegistrationDetil(this, type);
	}
	
	
	public void setFieldDisable(boolean isDisable){
		referencePatient.setDisabled(isDisable);
		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		MRNumber.setReadonly(isDisable);
		registrationNumber.setReadonly(isDisable);
		patientName.setReadonly(isDisable);
		((Radio)gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio)gender.getChildren().get(1)).setDisabled(isDisable);
		dateOfBirth.setDisabled(isDisable);
		age.setReadonly(isDisable);
		mainDoctor.setReadonly(isDisable);
		patientEscort.setDisabled(isDisable);
		address.setReadonly(isDisable);
		patientTypeList.setDisabled(isDisable);
		polyclinicList.setDisabled(isDisable);
		total.setReadonly(isDisable);
	}
	
	public void clear(){
		
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
/*		polyclinicList.setSelectedIndex(0);*/
		total.setValue(null);
		polyclinicList.getItems().clear();
		referencePatient.setChecked(false);
		MRNumber.setVisible(true);
		mainDoctor.setVisible(true);
		registrationNumber.setVisible(true);
		registrationNumber.setReadonly(true);
		transactionNumber.setDisabled(false);
		
		this.patient = null;
		this.bundledTrx = null;
		this.itemTrx = null;
		this.reg = null;
		this.ranapKelas = MedisafeConstants.DEFAULT_TCLASS;
		/*this.nota = null;*/
		this.treatmentTrx = null;
		
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		mRNumberList.getItems().clear();
		notaList.getItems().clear();
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		crNoMR.setValue(null);
		searchName.setValue(null);
		searchNota.setValue(null);
		MRNumber.focus();
		
		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);

	}
	
	public void setButtonActive(boolean isActive){
		this.btnCancelNote.setDisabled(isActive);
		this.btnValidation.setDisabled(isActive);
		/*this.btnNew.setDisabled(isActive);*/
		/*this.btnCancel.setDisabled(isActive);*/
		this.btnModify.setDisabled(isActive);
		this.btnCetak.setDisabled(isActive);
		this.btnSave.setDisabled(!isActive);
		
	}
	
	
	public void setInnerButtonDisable(boolean isDisable){
		btnBundleAdd.setDisabled(isDisable);
		btnItemAdd.setDisabled(isDisable);
		btnTreatmentAdd.setDisabled(isDisable);
		/*btnCancel.setDisabled(isDisable);*/
		btnMiscAdd.setDisabled(isDisable);
		btnDelete.setDisabled(isDisable);
		calculateBtn.setDisabled(isDisable) ;
	}
	
	
	public void getNewTransaction(){
		
		statusNota.setVisible(false);
		setFieldDisable(false);
		total.setReadonly(true);
		setInnerButtonDisable(false);
		setButtonActive(true);
		MRNumber.focus();
		clear();
	}
	
	public void getNote(){
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		
		PolyclinicController pc = new PolyclinicController();
		Object[] obj = new Object[]{nota,pc,new Integer(MedisafeConstants.NOTA), btnValidation,btnModify,btnCancelNote,statusNota};
		session.setAttribute("validasi", obj);
		
		
	}
	
	public void cancelNote() throws VONEAppException, InterruptedException{
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_POLIKLINIK);
				
		
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		Integer warehouseId = null;
		if(((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse() != null)
			warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
//		Object[] obj = new Object[]{nota, polyclinicList.getItems(), new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,btnCancelNote,statusNota,siu};
		Object[] obj = new Object[]{nota,warehouseId, new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,btnCancelNote,statusNota, permission};
		session.setAttribute("cancelNote", obj);
		
		labelStatus.setVisible(true);
		statusNota.setVisible(true);
	}
	
	public void validate(TbExamination nota) throws InterruptedException, VONEAppException{
		
		nota.setNExamStatus(new Integer(MedisafeConstants.VALIDATED_NOTE));
		if(Service.getNotaManager().save(nota)){
			Messagebox.show(MessagesService.getKey("common.validate.success"));

		}
		else{
			Messagebox.show(MessagesService.getKey("common.validate.fail"));
		}
		
	}
	
	
	public void searchNote() throws InterruptedException, VONEAppException{
		
		
		notaList.getItems().clear();
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		Service.getNotaManager().searchNote("%"+searchNota.getValue()+"%", "%"+searchName.getValue()+"%",
				unit,notaList,MedisafeConstants.ACTIVE_NOTE);
				
	}
	
	
	
	public void getNoteDetail() throws VONEAppException{
		
		polyServ.getNoteDetil(this);
		MiscTrxController.setFont(polyclinicList);
				
//		this.nota = note;
		
	}
	
	public void cetak() throws VONEAppException, IOException, InterruptedException{
		NoteReport nr = new NoteReport(nota);
		
		if(!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer TERLEBIH DAHULU...!!!");
		}
	}
	
	
	public void modify() throws VONEAppException{
		
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
	
	
	public void saveModify() throws VONEAppException, InterruptedException
	{
		Date tanggal = new Date();
		
		nota.setTotal(this.total.getValue().doubleValue());
		nota.setDWhnChange(tanggal);
		nota.setVWhoChange(user.getVUserName());
		
		
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		double trxValue = 0;
		Intbox jumlah = null;
		Decimalbox diskon = null;
		Listbox opsiDiskon = null;
		
		List<Listitem> items;
		items = polyclinicList.getItems();
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
			else if(item.getValue() instanceof TbMiscTrx){
				TbMiscTrx miscTrx = (TbMiscTrx)item.getValue();
				miscTrx.setDWhnChange(tanggal);
				miscTrx.setVWhoChange(user.getVUserName());
				
				cell = (Listcell)item.getChildren().get(2);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				miscTrx.setNQty(jumlah.getValue().shortValue());
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				miscTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				miscTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				miscTrx.setNDiscAmount(diskon.getValue().doubleValue());
				miscTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				
				miscSet.add(miscTrx);
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
		
		NoteManager manager = Service.getNotaManager();
		Integer warehouseId = null;
		if(((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse() != null)
				warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, bundledSet, miscSet, null, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}
	
	public void searchDoctor() throws VONEAppException{
		doctorService.searchDoctor(this.doctorCode, this.doctorName,this.doctorList, MedisafeConstants.GRUP_DOKTER);
	}
	
	public void getMsStaff() throws VONEAppException{
		MsStaff doctor = (MsStaff)this.doctorList.getSelectedItem().getValue();
		reg.setMsStaff(doctor);
		polyServ.updateRegistration(reg);
		mainDoctor.setValue(doctor.getVStaffName());
	}
	
}

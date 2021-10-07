package com.vone.medisafe.emergency;

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
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.report.NoteReport;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.PatientEscortController;
import com.vone.medisafe.ui.master.PatientTypeController;
import com.vone.medisafe.ui.polyclinic.EditListener;
import com.vone.medisafe.ui.polyclinic.PolyclinicController;

public class EmergencyController extends BaseController{

	public TbRegistration reg;
	public TbExamination nota;
	public TbItemTrx itemTrx;
	public TbTreatmentTrx treatmentTrx;
	public MsUser user;
	public MsStaffInUnit siu;
	public TbMedicalRecord mr;
	public boolean isUpdate;
	public MsStaff staff;
	
	
	Session session;
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
	
	Bandbox mainDoctor;
	Textbox code;
	Textbox name;
	Listbox doctorList;
	
	public Listbox patientEscortList;
	public Textbox address;
	public Listbox patientTypeList;
	public Textbox crNoAlamat;;
	public Textbox crNama;
	public Textbox crNoMR;
	public Listbox emergencyList;
	public Decimalbox total;
	public Label statusNota;
	public Label labelStatus;
	
	public Button btnSave;
	public Button btnModify;
	public Button btnNew;
	public Button btnCancelNote;
	public Button btnValidation;
	public Button btnCetak;
//	public Button btnEnd;
	public Button btnMiscAdd;

	public Button btnItemAdd;
	public Button btnTreatmentAdd;
	public Button btnDelete;
	public Button calculateBtn;
	
	Listitem item;
	Listcell cell;
	
	String ranapKelas;
	
	ZulConstraint constraints = new ZulConstraint();
	
	UserManager userService = ServiceLocator.getUserManager();
	

	private EmergencyManager emergencyService = Service.getEmergencyManager();

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
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
		code = (Textbox)win.getFellow("code");
		name = (Textbox)win.getFellow("name");
		doctorList = (Listbox)win.getFellow("doctorList");
		
		patientEscortList = (Listbox)win.getFellow("patientEscortList");
		address = (Textbox)win.getFellow("address");
		patientTypeList = (Listbox)win.getFellow("patientTypeList");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		crNama = (Textbox)win.getFellow("crNama");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		emergencyList = (Listbox)win.getFellow("emergencyList");
		total = (Decimalbox)win.getFellow("total");
		statusNota = (Label)win.getFellow("statusNota");
		labelStatus = (Label)win.getFellow("labelStatus");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");
		

		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		btnTreatmentAdd = (Button)win.getFellow("btnTreatmentAdd");
		btnDelete = (Button)win.getFellow("btnDelete");
		calculateBtn = (Button)win.getFellow("calculateBtn");
		btnMiscAdd = (Button)win.getFellow("btnMiscAdd");
		
		super.init(win);
		
		session = win.getDesktop().getSession(); 
		
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_UGD);
		
		UserInfoBean uib = getUserInfoBean();
		user = uib.getMsUser();
		
		mRNumberList.getItems().clear();
		emergencyList.getItems().clear();
		notaList.getItems().clear();
		
		userService.getUnitUser(user, locationList);

		
		PatientTypeController.getAllPatientTypeList2(patientTypeList);
		PatientEscortController.getPatientEscortForSelect(patientEscortList);
//		DoctorController.getDoctorBaseOnUnitId2(((MsUnit)locationList.getSelectedItem().getValue()).
//				getNUnitId(), mainDoctorList);
		
		constraints.registerComponent(patientName,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(address, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(code, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
			

		
		btnModify.setDisabled(true);
		btnCancelNote.setDisabled(true);
		btnValidation.setDisabled(true);
		btnCetak.setDisabled(true);
//		btnEnd.setDisabled(true);
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		
		setFieldDisable(true);
		this.transactionNumber.setDisabled(false);
		locationList.setDisabled(false);
		MRNumber.setDisabled(false);
		mainDoctor.setDisabled(false);
		patientEscortList.setDisabled(false);
		
		emergencyList.setAttribute("jumlah", total);
		
		

	}
	
	public void checkRegistration(){
		String ranap = "NO";
		total.setValue(null);
		session.setAttribute("isRanap", ranap);
		session.setAttribute("listbox", emergencyList);
		session.setAttribute("location", locationList);
		
	}
	
	public void calculateTotalAmount() throws InterruptedException{
		List jumlah = emergencyList.getItems();
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
		if(emergencyList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int index = emergencyList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		emergencyList.removeItemAt(index);
		total.setValue(null);
		
		calculateTotalAmount();
	}
	
	
	public void save() throws InterruptedException, VONEAppException{
		
		emergencyService.save(this);
		transactionNumber.setAttribute("nota",nota);

			
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	
	
	
	
	
	public void getPatientDetail(int type) throws VONEAppException, InterruptedException{
		
		
		emergencyService.getPatientDetil(this,type);
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		setFieldDisable(true);
		mainDoctor.setDisabled(false);
		patientEscortList.setDisabled(false);
		
		session.setAttribute("mr", mr);
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_UGD);
		session.setAttribute("location", locationList);
		
	}
	
	
	public void setFieldDisable(boolean isDisable){
		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
		registrationNumber.setReadonly(isDisable);
		patientName.setReadonly(isDisable);
		((Radio)gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio)gender.getChildren().get(1)).setDisabled(isDisable);
		dateOfBirth.setDisabled(isDisable);
		age.setReadonly(isDisable);
		address.setReadonly(isDisable);
		patientTypeList.setDisabled(isDisable);
		emergencyList.setDisabled(isDisable);
		total.setReadonly(isDisable);
		mainDoctor.setDisabled(isDisable);
		patientEscortList.setDisabled(isDisable);
		this.emergencyList.setDisabled(isDisable);
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
		mainDoctor.removeAttribute("doctor");
		patientEscortList.setSelectedIndex(0);
		address.setValue(null);
		patientTypeList.setSelectedIndex(0);
		total.setValue(null);
		emergencyList.getItems().clear();
		
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		code.setValue(null);
		name.setValue(null);
		doctorList.getItems().clear();
		mRNumberList.getItems().clear();
		notaList.getItems().clear();
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		crNoMR.setValue(null);
		searchNota.setValue(null);
		searchName.setValue(null);
		
		MRNumber.focus();
		
		this.transactionNumber.setDisabled(false);

		this.reg = null;
		this.staff = null;
		this.mr = null;
		
		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
		
	}
	
	public void setButtonActive(boolean isActive){
		this.btnCancelNote.setDisabled(isActive);
		this.btnValidation.setDisabled(isActive);
		/*this.btnNew.setDisabled(isActive);*/
		this.btnCetak.setDisabled(isActive);
		this.btnModify.setDisabled(isActive);
		this.btnSave.setDisabled(!isActive);
	}
	
	
	public void setInnerButtonDisable(boolean isDisable){
		btnItemAdd.setDisabled(isDisable);
		btnTreatmentAdd.setDisabled(isDisable);
		btnMiscAdd.setDisabled(isDisable);
		btnDelete.setDisabled(isDisable);
		calculateBtn.setDisabled(isDisable) ;
		
	}
	
	
	public void getNewTransaction(){
	
		statusNota.setVisible(false);
		locationList.setDisabled(false);
		MRNumber.setDisabled(false);
		mainDoctor.setDisabled(false);
		patientEscortList.setDisabled(false);

		setInnerButtonDisable(false);
		setButtonActive(true);
		clear();
	}
	
	public void getNote(){
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		
		PolyclinicController pc = new PolyclinicController();
		Object[] obj = new Object[]{nota,pc,new Integer(MedisafeConstants.NOTA), btnValidation,btnModify,btnCancelNote,statusNota};
		session.setAttribute("validasi", obj);
		
		
	}
	
	public void cancelNote() throws VONEAppException{
		
		
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_UGD);
		
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		Object[] obj = new Object[]{nota, ((MsUnit)locationList.getSelectedItem().getValue()).
				getMsWarehouse().getNWhouseId(), new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,
				btnCancelNote,statusNota, permission};
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
		
		MiscTrxController.setFont(notaList);
	}
	
	
	
	public void getNoteDetail() throws VONEAppException{
		
		emergencyList.getItems().clear();
		
		emergencyService.getNoteDetil(this);
		
		

	
		
		if(nota.getTotal() != null)this.total.setValue(new BigDecimal(nota.getTotal().doubleValue()));
		
		this.statusNota.setVisible(true);
		labelStatus.setVisible(true);
		this.statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
		
		setFieldDisable(true);
		setButtonActive(false);
		setInnerButtonDisable(true);
		
		if(nota.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			btnModify.setDisabled(true);
			btnValidation.setDisabled(true);
		}
		
		
	}
	
	public void cetak() throws VONEAppException, IOException, InterruptedException{
		NoteReport nr = new NoteReport(nota);
		
		if (!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer TERLEBIH DAHULU...!!!");
		}
	}
	
	
	public void modify() throws VONEAppException{
		
		Service.getNotaManager().getNoteDetil(nota, emergencyList);

		
		List<Listitem> items = emergencyList.getItems();
		
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
			jumlah.addEventListener("onChange", new EditListener(item,emergencyList,total));
			
			cell = (Listcell)item.getChildren().get(5);
			diskon = (Decimalbox)cell.getChildren().get(0);
			diskon.addEventListener("onChange", new EditListener(item,emergencyList,total));
			
			diskonOptionList = (Listbox)cell.getChildren().get(1);
			diskonOptionList.addEventListener("onSelect", new EditListener(item,emergencyList,total));
			
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
				
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		double trxValue = 0;
		Intbox jumlah = null;
		Decimalbox diskon = null;
		Listbox opsiDiskon = null;
		
		List<Listitem> items = emergencyList.getItems();
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
			
		}
		
		NoteManager manager = Service.getNotaManager();
		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		Set<TbMiscTrx> miscSet = MiscTrxController.saveModifyMiscTrx(emergencyList, user);
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, null, miscSet, null, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}
}

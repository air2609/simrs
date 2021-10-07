package com.vone.medisafe.ward;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
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
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaff;
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
import com.vone.medisafe.ui.polyclinic.EditListener;
import com.vone.medisafe.ui.polyclinic.PolyclinicController;

/**
 * WardTransactionController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 09 2006
 * @category user interface (visual)
 */

public class WardTransactionController extends BaseController{

	public TbMedicalRecord mr;
	public TbRegistration reg;
	public TbExamination nota;
	public TbItemTrx itemTrx;
	public TbTreatmentTrx treatmentTrx;
	public MsUser user;
	public boolean isUpdate;
	
	
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
	public Textbox age;
	public Bandbox mainDoctor;
	public Textbox patientType;
	public Textbox crNoAlamat;;
	public Textbox crNama;
	public Textbox crNoMR;
	public Listbox wardList;
	public Decimalbox total;
	public Label statusNota;
	public Label labelStatus;
	public Listbox doctorList;
	
	public Textbox treatmentClass;
	public Textbox hall;
	public Textbox bed;
	
	public Button btnSave;
	public Button btnModify;
	public Button btnNew;
	public Button btnCancelNote;
	public Button btnValidation;
	public Button btnCetak;
	//public Button btnEnd;
	
	
	public Button btnItemAdd;
	public Button btnTreatmentAdd;

	public Button btnDelete;
	public Button calculateBtn;
	public Button btnPatientInv;
	public Button btnMiscAdd;
	
	Listitem item;
	Listcell cell;
	
	String ranapKelas;
	
	ZulConstraint constraints = new ZulConstraint();
	
	
	
	private WardTransactionManager wardServ = Service.getWardTransactionManager();
	
	UserManager userService = ServiceLocator.getUserManager();

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
		age = (Textbox)win.getFellow("age");
		mainDoctor = (Bandbox)win.getFellow("mainDoctor");
		patientType = (Textbox)win.getFellow("patientType");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		crNama = (Textbox)win.getFellow("crNama");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		wardList = (Listbox)win.getFellow("wardList");
		total = (Decimalbox)win.getFellow("total");
		statusNota = (Label)win.getFellow("statusNota");
		labelStatus = (Label)win.getFellow("labelStatus");
		doctorList = (Listbox)win.getFellow("doctorList");
		
		treatmentClass = (Textbox)win.getFellow("treatmentClass");
		hall = (Textbox)win.getFellow("hall");
		bed = (Textbox)win.getFellow("bed");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");
		btnMiscAdd = (Button)win.getFellow("btnMiscAdd");
	
		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		btnTreatmentAdd = (Button)win.getFellow("btnTreatmentAdd");

		btnDelete = (Button)win.getFellow("btnDelete");
		calculateBtn = (Button)win.getFellow("calculateBtn");
		btnPatientInv = (Button)win.getFellow("btnPatientInv");
		
		super.init(win);
		
		session = win.getDesktop().getSession(); 
		
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_BANGSAL);
		
		UserInfoBean uib = getUserInfoBean();
		user = uib.getMsUser();
		
		mRNumberList.getItems().clear();
		wardList.getItems().clear();
		notaList.getItems().clear();
		
		
		userService.getUnitUser(user, locationList);
				
				
		constraints.registerComponent(patientName,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
					
		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		setFieldDisable(true);
		mainDoctor.setDisabled(false);
		
		
		btnModify.setDisabled(true);
		btnCancelNote.setDisabled(true);
		btnValidation.setDisabled(true);
		btnCetak.setDisabled(true);
//		btnEnd.setDisabled(true);
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		MRNumber.setDisabled(false);
		MRNumber.focus();
		locationList.setDisabled(false);
		
		wardList.setAttribute("jumlah", total);
		transactionNumber.setDisabled(false);

	}
	
		
	public void checkRegistration(short type) throws InterruptedException{
		total.setValue(null);
		String ranap = null;
		session.setAttribute("location", locationList);
		
		//ambilKelas ranap
		ranap="YES";
		session.setAttribute("isRanap", ranap);
		session.setAttribute("listbox", wardList);
		session.setAttribute("kelasTarif", ranapKelas);
		
		if(this.patientName.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("ranap.patinet.not.fill"));
			return;
		}
		
		if(type == MedisafeConstants.TREATMENT){
			Window wind = (Window) Executions.createComponents(
 					"/zkpages/common/tambahTindakan.zul",null,null);
			wind.doModal();
		}else if(type == MedisafeConstants.MEDICINE){
			Window wind = (Window) Executions.createComponents(
 					"/zkpages/common/tambahItem.zul",null,null);
			wind.doModal();
		}else{
			Window wind = (Window) Executions.createComponents(
 					"/zkpages/common/tambahMisc.zul",null,null);
			wind.doModal();
		}
	}
	
	public void calculateTotalAmount() throws InterruptedException{
		List jumlah = wardList.getItems();
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
		if(wardList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int index = wardList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		wardList.removeItemAt(index);
		total.setValue(null);
		
		calculateTotalAmount();
	}
	
	
	public void save() throws InterruptedException, VONEAppException{
		
		wardServ.save(this);
		locationList.setDisabled(true);
	}
	

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	

	
	public void getRegistration(int type) throws VONEAppException,InterruptedException{
		
		wardServ.getRegistrationDetil(this,type);

		session.setAttribute("mr", mr);
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_BANGSAL);
		session.setAttribute("location", locationList);
		
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		setFieldDisable(true);
		mainDoctor.setDisabled(false);
		MRNumber.setDisabled(false);
		locationList.setDisabled(false);
	}
	
	
	public void setFieldDisable(boolean isDisable){
//		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
	/*	MRNumber.setDisabled(isDisable);*/
		registrationNumber.setReadonly(isDisable);
		patientName.setReadonly(isDisable);
		((Radio)gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio)gender.getChildren().get(1)).setDisabled(isDisable);
		age.setReadonly(isDisable);
		mainDoctor.setDisabled(isDisable);
		patientType.setReadonly(isDisable);
		wardList.setDisabled(isDisable);
		total.setReadonly(isDisable);
		
		bed.setReadonly(isDisable);
		treatmentClass.setReadonly(isDisable);
		hall.setReadonly(isDisable);
	}
	
	public void clear(){
		this.reg = null;
		this.itemTrx = null;
		this.mr = null;
//		this.nota = null;
		this.treatmentTrx = null;
		
		locationList.setSelectedIndex(0);
		transactionNumber.setValue(null);
		MRNumber.setValue(null);
		registrationNumber.setValue(null);
		patientName.setValue(null);
		((Radio)gender.getChildren().get(0)).setSelected(false);
		((Radio)gender.getChildren().get(1)).setSelected(false);
		age.setValue(null);
		mainDoctor.setValue(null);
		patientType.setValue(null);
		total.setValue(null);
		wardList.getItems().clear();
		MRNumber.setVisible(true);
		mainDoctor.setVisible(true);
		registrationNumber.setVisible(true);
		registrationNumber.setReadonly(true);

		
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
		MRNumber.setDisabled(false);
		bed.setValue(null);
		treatmentClass.setValue(null);
		hall.setValue(null);
		setFieldDisable(true);
		transactionNumber.setDisabled(false);
		locationList.setDisabled(false);
		this.mainDoctor.setDisabled(false);
		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
	//	transactionNumber.setDisabled(false);

	}
	
	public void setButtonActive(boolean isActive){
		this.btnCancelNote.setDisabled(isActive);
		this.btnValidation.setDisabled(isActive);
	//	this.btnNew.setDisabled(!isActive);
		/*this.btnCancel.setDisabled(isActive);*/
		this.btnCetak.setDisabled(isActive);
		this.btnModify.setDisabled(isActive);
		this.btnSave.setDisabled(!isActive);
	}
	
	
	public void setInnerButtonDisable(boolean isDisable){
		btnItemAdd.setDisabled(isDisable);
		btnTreatmentAdd.setDisabled(isDisable);
	/*	btnCancel.setDisabled(isDisable);*/
		btnDelete.setDisabled(isDisable);
		calculateBtn.setDisabled(isDisable) ;
		btnPatientInv.setDisabled(isDisable);
		btnMiscAdd.setDisabled(isDisable);
	}
	
	
	public void getNewTransaction(){
		
		statusNota.setVisible(false);
		setFieldDisable(false);
		setInnerButtonDisable(false);
		setButtonActive(true);
		MRNumber.focus();
		clear();
	}
	
	public void getNote(){
		nota = (TbExamination)transactionNumber.getAttribute("nota");
		
		PolyclinicController pc = new PolyclinicController();
		Object[] obj = new Object[]{nota,pc,new Integer(MedisafeConstants.NOTA), btnValidation,btnModify,btnCancelNote,statusNota};
		session.setAttribute("validasi", obj);
		
		
	}
	
	public void cancelNote() throws VONEAppException{
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_BANGSAL);
		
		Integer wid = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		try {
			 if(wid == null)
				 Messagebox.show(MessagesService.getKey("Unit ini belum memiliki gudang!"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		Object[] obj = new Object[]{nota, ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().
				getNWhouseId(), new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,btnCancelNote,statusNota,
				permission};
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
	
	
	public void searchNote() throws InterruptedException,VONEAppException{
		
		
		
		notaList.getItems().clear();
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		Service.getNotaManager().searchNote("%"+searchNota.getValue()+"%", "%"+searchName.getValue()+"%",
				unit,notaList,MedisafeConstants.ACTIVE_NOTE);
		
			
		
	}
	
	
	
	public void getNoteDetail() throws VONEAppException, InterruptedException{
		
		wardServ.getNoteDetil(this);
		
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
	
	
	public void showInventoryPatient() throws InterruptedException,VONEAppException
	{
		if(patientName.getText().equals("")){
			Messagebox.show(MessagesService.getKey("ranap.patinet.not.fill"));
			return;
		}
		
		Window wind = (Window) Executions.createComponents("/zkpages/bangsal/patientInventory.zul",null,null);
		wind.doModal();
	}
	
	public void cetak() throws VONEAppException, IOException, InterruptedException{
		NoteReport nr = new NoteReport(nota);
		
		if(!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer TERLEBIH DULU..!!");
		}
	}
	
	
	public void modify() throws VONEAppException{
		
		Service.getNotaManager().getNoteDetil(nota, wardList);
		
		List<Listitem> items = wardList.getItems();
		
		
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
			jumlah.addEventListener("onChange", new EditListener(item,wardList,total));
			
			cell = (Listcell)item.getChildren().get(5);
			diskon = (Decimalbox)cell.getChildren().get(0);
			diskon.addEventListener("onChange", new EditListener(item,wardList,total));
			
			diskonOptionList = (Listbox)cell.getChildren().get(1);
			diskonOptionList.addEventListener("onSelect", new EditListener(item,wardList,total));
			
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
		
		List<Listitem> items = wardList.getItems();
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
		Set<TbMiscTrx> miscSet = MiscTrxController.saveModifyMiscTrx(wardList, user);
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, null, miscSet, null, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}
	
	
	
	
	public void setMainDoctor() throws VONEAppException, InterruptedException{
		
		
		if(reg == null){
			Messagebox.show(MessagesService.getKey("registration.is.null"));
			return;
		}
		
		MsStaff staff = (MsStaff)doctorList.getSelectedItem().getValue();
		reg.setMsStaff(staff);
		
		wardServ.updateRegistration(reg);
		mainDoctor.setText(staff.getVStaffCode()+" - "+staff.getVStaffName());
	}
}

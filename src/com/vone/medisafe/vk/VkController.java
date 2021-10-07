package com.vone.medisafe.vk;


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
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.polyclinic.EditListener;

public class VkController extends BaseController{
	
	public MsStaffInUnit siu;
	public MsUser user;
	public TbMedicalRecord mr;
	public TbRegistration reg;
	public String ranapKelas;
	public TbExamination nota;
	public boolean isUpdate;
	
	public TbTreatmentTrx treatmentTrx;
	public TbItemTrx itemTrx;
	
	public Listbox locationList;
	public Bandbox transactionNumber;
	public Textbox searchNota;
	public Textbox searchName;
	public Listbox transactionNumberList;
	public Bandbox MRNumber;
	public Textbox crNoMR;
	public Textbox crNama;
	public Textbox crNoAlamat;
	public Listbox mRNumberList;
	public Textbox registrationNumber;
	public Textbox patientName;
	public Radiogroup gender;
	public Textbox age;
	public Textbox patientType;
	public Textbox mainDoctor;
	public Textbox bed;
	public Textbox tclass;
	public Textbox hall;
	
	public Label labelStatus;
	public Label statusNota;
	public Listbox vkList;
	public Decimalbox total;
	
	public Button calculateBtn;
	public Button btnDelete;
	public Button btnTreatmentAdd;
	public Button btnItemAdd;
	
	public Button btnSave;
	public Button btnModify;
	public Button btnMiscAdd;

	public Button btnNew;
	public Button btnCancelNote;
	public Button btnValidation;
	public Button btnCetak;
//	public Button btnEnd;
	
	Listitem item;
	Listcell cell;
	
	Session session;
	
	ZulConstraint constraints = new ZulConstraint();
	
	private VkManager service = Service.getVkManager();
	
	UserManager userService = ServiceLocator.getUserManager();

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		locationList = (Listbox)win.getFellow("locationList");
		transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		searchNota = (Textbox)win.getFellow("searchNota");
		searchName = (Textbox)win.getFellow("searchName");
		transactionNumberList = (Listbox)win.getFellow("transactionNumberList");
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		mRNumberList = (Listbox)win.getFellow("mRNumberList");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		patientName = (Textbox)win.getFellow("patientName");
		gender = (Radiogroup)win.getFellow("gender");
		age = (Textbox)win.getFellow("age");
		patientType = (Textbox)win.getFellow("patientType");
		mainDoctor = (Textbox)win.getFellow("mainDoctor");
		bed = (Textbox)win.getFellow("bed");
		tclass = (Textbox)win.getFellow("tclass");
		hall = (Textbox)win.getFellow("hall");
		
		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		vkList = (Listbox)win.getFellow("vkList");
		total = (Decimalbox)win.getFellow("total");
		
		calculateBtn = (Button)win.getFellow("calculateBtn");
		btnDelete = (Button)win.getFellow("btnDelete");
		btnTreatmentAdd = (Button)win.getFellow("btnTreatmentAdd");
		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		btnMiscAdd = (Button)win.getFellow("btnMiscAdd");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");
		
		this.user = getUserInfoBean().getMsUser();
		session = win.getDesktop().getSession();
		
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY, MedisafeConstants.COMMON_VK);
		
		
		userService.getUnitUser(user, locationList);
		
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		constraints.registerComponent(patientName,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
					
		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		setFieldDisable(true);
		MRNumber.setDisabled(false);
		locationList.setDisabled(false);
		
		setInnerButtonDisable(true);
		setButtonDisabel(true);
		
//		this.btnEnd.setDisabled(false);
		
		vkList.setAttribute("jumlah", total);
		
	}
	
	
	
	
	public void getRegistration(int type) throws VONEAppException, InterruptedException{
		
		
		service.getRegistration(this,type);
		

		session.setAttribute("mr", mr);
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_VK);
		session.setAttribute("location", locationList);
		
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		setFieldDisable(true);
		MRNumber.setDisabled(false);
		locationList.setDisabled(false);
		
		setInnerButtonDisable(false);
		this.btnNew.setDisabled(false);
		this.btnSave.setDisabled(false);
	}
	
	
	public void setFieldDisable(boolean isDisable){
		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
		registrationNumber.setDisabled(isDisable);
		patientName.setDisabled(isDisable);
		((Radio)gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio)gender.getChildren().get(1)).setDisabled(isDisable);
		age.setDisabled(isDisable);
		mainDoctor.setDisabled(isDisable);
		patientType.setDisabled(isDisable);
		vkList.setDisabled(isDisable);
		total.setDisabled(isDisable);
		
		bed.setDisabled(isDisable);
		tclass.setDisabled(isDisable);
		hall.setDisabled(isDisable);
	}
	
	
	public void clear(){
		
		locationList.setSelectedIndex(0);
		transactionNumber.setValue(null);
		MRNumber.setValue(null);
		registrationNumber.setValue(null);
		patientName.setValue(null);
		gender.setSelectedItem(null);
		age.setValue(null);
		mainDoctor.setValue(null);
		patientType.setValue(null);
		vkList.getItems().clear();
		total.setValue(null);
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		crNoMR.setValue(null);
		crNoAlamat.setValue(null);
		crNama.setValue(null);
		mRNumberList.getItems().clear();
		searchName.setValue(null);
		searchNota.setValue(null);
		transactionNumberList.getItems().clear();
		
		bed.setValue(null);
		tclass.setValue(null);
		hall.setValue(null);
		
		this.mr = null;
		this.nota = null;
		this.ranapKelas = null;
		this.reg = null;
		this.treatmentTrx = null;
		this.itemTrx = null;
		
		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
		
		setInnerButtonDisable(true);
		locationList.setDisabled(false);
		transactionNumber.setDisabled(false);
		MRNumber.setDisabled(false);
		MRNumber.focus();
	}
	
	
	private void setInnerButtonDisable(boolean isDisable){
		calculateBtn.setDisabled(isDisable);
		btnDelete.setDisabled(isDisable);
		btnTreatmentAdd.setDisabled(isDisable);
		btnItemAdd.setDisabled(isDisable);
		btnMiscAdd.setDisabled(isDisable);
		
	}
	
	
	private void setButtonDisabel(boolean isDisable){
		btnSave.setDisabled(isDisable);
		btnModify.setDisabled(isDisable);
//		btnCancel.setDisabled(isDisable);
//		btnNew.setDisabled(isDisable);
		btnCancelNote.setDisabled(isDisable);
		btnValidation.setDisabled(isDisable);
		btnCetak.setDisabled(isDisable);
//		btnEnd.setDisabled(isDisable);
	}
	
	
	public void addTreatment() throws VONEAppException, InterruptedException{
		total.setValue(null);
		Window win = (Window) Executions.createComponents("/zkpages/vk/treatment.zul", null, null);
		win.doModal();

	}
	
	
	public void checkRegistration(){
		total.setValue(null);
	
		session.setAttribute("location", locationList);
		
		
		//ambilKelas ranap
		String ranap="YES";
		session.setAttribute("isRanap", ranap);
		session.setAttribute("listbox", vkList);
		session.setAttribute("kelasTarif", ranapKelas);
				
			
			
		
	}
	
	
	public void delete() throws InterruptedException{
		if(vkList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int index = vkList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		vkList.removeItemAt(index);
		total.setValue(null);
		
		calculateTotalAmount();
	}
	
	
	public void calculateTotalAmount() throws InterruptedException{
		List jumlah = vkList.getItems();
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
	
	
	public void save() throws InterruptedException, VONEAppException{
		
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}

		
		if((patientName.getText().trim() == "") || (vkList.getItems().size() == 0)){
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if(total.getValue() == null){
			Messagebox.show(MessagesService.getKey("common.transaction.total.not.counted"));
			return;
		}
		
		
		//step
		//simpan ke dalam tabel examination, generate note here, assign juga siapa pasiennya
		//masukan ke tabel transaksi, lakukan updating langsung terhadap jumlah item di inventory (paling ruwet)

		nota = new TbExamination();
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		nota.setTbRegistration(reg);
		nota.setMsUnit((MsUnit)locationList.getSelectedItem().getValue());
		nota.setTotal(new Double(total.getValue().doubleValue()));
		nota.setMsPatient(mr.getMsPatient());
				
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);

		MiscTrxController.saveTrx(vkList, user, treatmentSet, itemSet, miscSet);
		


		
		if(Service.getPolyclinicManager().save(mr.getMsPatient(), nota, treatmentSet, itemSet, bundledSet , miscSet,
				(MsUnit)locationList.getSelectedItem().getValue(), true)){
			Messagebox.show(MessagesService.getKey("common.save.success"));
			transactionNumber.setValue(nota.getVNoteNo());
			transactionNumber.setAttribute("nota", nota);
			
			statusNota.setVisible(true);
			labelStatus.setVisible(true);
			statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
			
			setFieldDisable(true);
			setButtonDisabel(false);
			this.btnSave.setDisabled(true);

			setInnerButtonDisable(true);
			
		}else{
			Messagebox.show(MessagesService.getKey("common.save.fail"));
		}
		transactionNumber.setAttribute("nota",nota);
	}
	
	
	public void searchNote() throws InterruptedException, VONEAppException{
		
		transactionNumberList.getItems().clear();
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		Service.getNotaManager().searchNote("%"+searchNota.getValue()+"%", "%"+searchName.getValue()+"%",
				unit,transactionNumberList,MedisafeConstants.ACTIVE_NOTE);
	}
	
	
	
	
	public void getNoteDetail() throws VONEAppException{
		
		service.getNoteDetil(this);
		
				
		this.statusNota.setVisible(true);
		labelStatus.setVisible(true);
		this.statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
		
		setFieldDisable(true);
		setButtonDisabel(false);
		this.btnSave.setDisabled(true);
		setInnerButtonDisable(true);
		
		if(nota.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
			btnModify.setDisabled(true);
			btnValidation.setDisabled(true);
		}
		
	}
	
	public void validate() throws VONEAppException, InterruptedException
	{
		Object[] obj = new Object[]{nota,null,new Integer(MedisafeConstants.NOTA), btnValidation,btnModify,btnCancelNote,statusNota};
		session.setAttribute("validasi", obj);
		
		final Window win = (Window) Executions.createComponents("/zkpages/common/validasi.zul", null, null);
		win.doModal();
	}
	
	public void cancelNote() throws VONEAppException{
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_VK);
		
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		Object[] obj = new Object[]{nota, ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().
				getNWhouseId(), new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,
				btnCancelNote,statusNota,permission};
		session.setAttribute("cancelNote", obj);
		
		labelStatus.setVisible(true);
		statusNota.setVisible(true);
	}
	
	
	
	public void modify() throws VONEAppException{
		
		
		Service.getNotaManager().getNoteDetil(nota, vkList);
		
		List<Listitem> items = vkList.getItems();
		
		
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
			jumlah.addEventListener("onChange", new EditListener(item,vkList,total));
			
			cell = (Listcell)item.getChildren().get(5);
			diskon = (Decimalbox)cell.getChildren().get(0);
			diskon.addEventListener("onChange", new EditListener(item,vkList,total));
			
			diskonOptionList = (Listbox)cell.getChildren().get(1);
			diskonOptionList.addEventListener("onSelect", new EditListener(item,vkList,total));
			
			diskon.setDisabled(false);
			diskonOptionList.setDisabled(false);
			
		}
		setButtonDisabel(true);
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
		
		List<Listitem> items = vkList.getItems();
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
		Set<TbMiscTrx> miscSet = MiscTrxController.saveModifyMiscTrx(vkList, user);
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, null, miscSet, null, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonDisabel(false);
		this.btnSave.setDisabled(true);
	}

}

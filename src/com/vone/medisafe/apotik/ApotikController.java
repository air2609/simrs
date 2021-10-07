package com.vone.medisafe.apotik;



import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.zkoss.zul.Groupbox;
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
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.NoteReport;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.PatientTypeController;
import com.vone.medisafe.ui.polyclinic.PolyclinicController;

/**
 * ApotikController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 15 2006
 * @category user interface (visual and controller - VC)
 */

public class ApotikController extends BaseController{
	
	public MsUser user;
	public boolean isRanap;
	public TbExamination nota;
	public TbRegistration reg;
	public MsPatient patient;
	public TbItemTrx itemTrx;
	public TbDrugIngredients racikan;
	public boolean isUpdate;
	
	
	Listbox locationList;
	Checkbox referencePatient;
	Bandbox MRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox patientList;
	Bandbox transactionNumber;
	Textbox searchNota;
	Textbox searchName;
	Listbox notaList;
	Textbox registrationNumber;
	Textbox receiptNumber;
	Textbox patientName;
	Radiogroup gender;
	Datebox dateOfBirth;
	Textbox age;
	Textbox address;
	Listbox patientType;
	Listbox pharmacyList;
	Decimalbox total;
	Decimalbox discount;
	Decimalbox grandTotal;
	Label labelStatus;
	Label statusNota;
	Listbox discountOpt;
	
	Button btnDelete;
	Button calculateBtn;
	Button btnItemAdd;
	Button btnSave;
	Button btnModify;

	Button btnNew;
	Button btnCancelNote;
	Button btnValidation;
	Button btnCetak;
//	Button btnCetakResep;
	Button btnIngrAdd;
	Button btnMiscAdd;
	Button btnCetakRj;
	
	
	Listitem item;
	Listcell cell;
	Session session;
	
	Textbox mrNumber;
	Textbox recipeNumber;
	
	String ranapKelas;
	
	ZulConstraint constraints = new ZulConstraint();
	
	private ApotikManager apServ = Service.getApotikManager();
	
	UserManager userService = ServiceLocator.getUserManager();
	
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		locationList = (Listbox)win.getFellow("locationList");
		referencePatient = (Checkbox)win.getFellow("referencePatient");
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		patientList = (Listbox)win.getFellow("patientList");
		transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		searchNota = (Textbox) win.getFellow("searchNota");
		searchName = (Textbox)win.getFellow("searchName");
		notaList = (Listbox)win.getFellow("notaList");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		receiptNumber = (Textbox)win.getFellow("receiptNumber");
		patientName = (Textbox)win.getFellow("patientName");
		gender = (Radiogroup)win.getFellow("gender");
		dateOfBirth = (Datebox)win.getFellow("dateOfBirth");
		age = (Textbox)win.getFellow("age");
		address = (Textbox)win.getFellow("address");
		patientType = (Listbox)win.getFellow("patientType");
		pharmacyList = (Listbox)win.getFellow("grupObatId").getFellow("vboxId").getFellow("pharmacyList");
		total = (Decimalbox)win.getFellow("total");
		discount = (Decimalbox)win.getFellow("discount");
		grandTotal = (Decimalbox)win.getFellow("grandTotal");
		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		discountOpt = (Listbox)win.getFellow("discountOpt");
		
		recipeNumber = (Textbox)win.getFellow("recipeNumber");
		mrNumber = (Textbox)win.getFellow("mrNumber");
		
		btnDelete = (Button)win.getFellow("btnDelete");
		calculateBtn = (Button)win.getFellow("calculateBtn");
		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");

		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
		btnCetakRj = (Button)win.getFellow("btnCetakRj");
//		btnCetakResep = (Button)win.getFellow("btnCetakResep");
		btnIngrAdd = (Button)win.getFellow("btnIngrAdd");
		btnMiscAdd = (Button)win.getFellow("btnMiscAdd");
		
		constraints.registerComponent(patientName,ZulConstraint.UPPER_CASE);
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
		
		user = getUserInfoBean().getMsUser();
		
		patientList.getItems().clear();
		pharmacyList.getItems().clear();
		notaList.getItems().clear();
		locationList.getItems().clear();
		
		PatientTypeController.getAllPatientTypeList2(patientType);
		
		userService.getUnitUser(user, locationList);
			
		
		btnModify.setDisabled(true);
		btnCancelNote.setDisabled(true);
		btnValidation.setDisabled(true);
		btnCetak.setDisabled(true);
		btnCetakRj.setDisabled(true);
//		btnCetakResep.setDisabled(true);
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		MRNumber.focus();
		
		
		pharmacyList.setAttribute("jumlah", grandTotal);
	}
	
	
	
	
	public void calculateTotalAmount() throws InterruptedException, VONEAppException{
		List jumlah = pharmacyList.getItems();
		double jumlahTotal = 0;
		Object[] obj = null;
		int jasaR = 0;
		
		Iterator it = jumlah.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			//obj[0]= harga sebelum diskon -> BigDecimal
			//obj[1]= harga setelah diskon -> BigDecimal
			//obj[2]= tipe diskon  -> RP atau %
			//obj[3] = banyaknya item, selian item jumlah=1
			
			if(item.getAttribute("manipulation") != null){
				obj = (Object[])item.getAttribute("manipulation");
//				Messagebox.show("panjang :" +obj.length);
				jumlahTotal = jumlahTotal + ((BigDecimal)obj[1]).doubleValue();
//				jasaR = jasaR + ((BigDecimal)obj[4]).intValue();
				jasaR = jasaR + (Short)obj[5];
			}
		}
		discount.setValue(new BigDecimal(jasaR));
		total.setValue(new BigDecimal(jumlahTotal));
		
		jumlahTotal = jumlahTotal + (jasaR * new Integer(MessagesService.getKey("jasa.apotik")).intValue());
		grandTotal.setValue(new BigDecimal(jumlahTotal));
	}
	
	
	public void checkRegistration() throws VONEAppException{
		grandTotal.setValue(null);
		String ranap = "NO";
		session.setAttribute("location", locationList);
		if(referencePatient.isChecked()){
			session.setAttribute("isRanap", ranap);
			session.setAttribute("listbox", pharmacyList);

			isRanap = false;
		}
		else{
			if(isRanap){
				//ambilKelas ranap
				ranap="YES";
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", pharmacyList);
				session.setAttribute("kelasTarif", ranapKelas);
				
			}
			else{
				session.setAttribute("isRanap", "NO");
				session.setAttribute("listbox", pharmacyList);
				
			}
		}
	}
	
	
	public void setMRVisible() throws VONEAppException{
		if(referencePatient.isChecked()){
			MRNumber.setVisible(false);
			registrationNumber.setVisible(false);
			patientName.focus();
			
		}
		else{
			MRNumber.setVisible(true);
			registrationNumber.setVisible(true);
			MRNumber.focus();
		}
	}
	
	
	public void delete() throws InterruptedException, VONEAppException{
		if(pharmacyList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int index = pharmacyList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		pharmacyList.removeItemAt(index);
		total.setValue(null);
		grandTotal.setValue(null);
	}
	
	
	public void save() throws InterruptedException, VONEAppException{
		
		boolean isWarning = false;
		List<Listitem> items = pharmacyList.getItems();
		for(Listitem item : items){
			Textbox aturanPakai = (Textbox) ((Listcell)item.getChildren().get(5)).getChildren().get(0);
			  if(aturanPakai.getValue() == null || aturanPakai.getValue().trim().equals("")) isWarning = true;
		}
		
		if(isWarning){
			Messagebox.show("ATURAN PAKAI HARUS DIISI!", "INFORMASI", Messagebox.OK, Messagebox.INFORMATION );
			return;
		}
		
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}

		
		if((patientName.getText().trim().equals("")) || (pharmacyList.getItems().size() == 0)){
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if(grandTotal.getValue() == null){
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
		
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbDrugIngredients> racikans = new HashSet<TbDrugIngredients>(0);
		Set<TbMiscTrx> miscs = new HashSet<TbMiscTrx>(0);
		

		
		nota = new TbExamination();
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		nota.setVWhoCreate(user.getVUserName());
		if(reg != null)	nota.setTbRegistration(reg);
		nota.setMsUnit((MsUnit)locationList.getSelectedItem().getValue());
		nota.setTotal(new Double(grandTotal.getValue().doubleValue()));
		if(!receiptNumber.getText().trim().equals(""))nota.setVRecipeNo(receiptNumber.getText());
		
		List list = pharmacyList.getItems();
		double jumlah = 0;
		double diskon = 0;
		double diskon_real = 0;
		if(discount.getValue() != null)
			//diskon = discount.getValue().doubleValue(); 
			diskon = 0;
			discount.setValue(new BigDecimal(0));
		
		String aturanPakai = "";	
		Iterator it = list.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			Object[] obj = (Object[])item.getAttribute("manipulation");
			if(item.getValue() instanceof Object[]){
				itemTrx = new TbItemTrx();
				
				//get aturan pakai
				Listcell cell = (Listcell)item.getChildren().get(5);
//				Messagebox.show(((Textbox)cell.getChildren().get(0)).getText());
				Textbox aturText = (Textbox)cell.getChildren().get(0);
				Listbox aturList = (Listbox)cell.getChildren().get(1);
				aturanPakai = aturText.getText() +"-"+aturList.getSelectedItem().getLabel();
				itemTrx.setAturanPakai(aturanPakai);
				item.setAttribute("aturanPakai", aturanPakai);
				
				Object[] object = (Object[])item.getValue();
				
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)object[0]);
//				itemTrx.setMsItem(((MsItemSellingPrice)item.getValue()).getMsItem());
				itemTrx.setMsItem(msItem);
				itemTrx.setVWhoCreate(user.getVUserName());
				itemTrx.setNQty((new Float(obj[3].toString())));
				itemTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				
				if(!receiptNumber.getText().trim().equals(""))
					itemTrx.setVReceiptNo(receiptNumber.getValue());
				
				if(discount.getValue() != null){
					if(discountOpt.getSelectedItem().getValue().toString().equals(MedisafeConstants.RP)){
						jumlah = ((BigDecimal)obj[0]).doubleValue();
						
						if(jumlah >= diskon){
							jumlah = jumlah - diskon;
							diskon_real = diskon;
							itemTrx.setNAmountAfterDisc(jumlah);
						}
						else{
							diskon = diskon - jumlah;
							diskon_real = jumlah;
							
							itemTrx.setNAmountAfterDisc(0.0);
						}
							
						itemTrx.setNDiscAmount(diskon_real);
						itemTrx.setVDiscType(MedisafeConstants.RP);
						
					}else{
						jumlah = (discount.getValue().doubleValue()/100) * ((BigDecimal)obj[0]).doubleValue();
						jumlah = ((BigDecimal)obj[0]).doubleValue() - jumlah;
						
						itemTrx.setNDiscAmount(discount.getValue().doubleValue());
						itemTrx.setNAmountAfterDisc(jumlah);
						itemTrx.setVDiscType(MedisafeConstants.PERCENT);
					}
				}
				else{
					itemTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
					itemTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
					itemTrx.setVDiscType(obj[2].toString());
				}
						
				itemSet.add(itemTrx);
			}
			else if(item.getValue() instanceof TbMiscTrx){
				
				TbMiscTrx miscTrx = (TbMiscTrx)item.getValue();
				miscTrx.setVWhoCreate(user.getVUserName());
				miscTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				miscTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				miscTrx.setVDiscType(obj[2].toString());
				miscTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				
				miscs.add(miscTrx);

			}
			else{
				//obat racikan
				racikan = (TbDrugIngredients) item.getValue();
				
				//get aturan pakai
				Listcell cell = (Listcell)item.getChildren().get(5);
				Textbox aturText = (Textbox)cell.getChildren().get(0);
				Listbox aturList = (Listbox)cell.getChildren().get(1);
				aturanPakai = aturText.getText() +"-"+aturList.getSelectedItem().getLabel();
				racikan.setAturanPakai(aturanPakai);
				item.setAttribute("aturanPakai", aturanPakai);
				
				//racikan.setVDingrId(VDingrId)
				racikan.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				racikan.setVWhoCreate(user.getVUserName());
				if(!receiptNumber.getText().trim().equals(""))
					racikan.setVReceiptNumber(receiptNumber.getValue());
				
				if(discount.getValue() != null){
					discount.setValue(new BigDecimal(0));
					if(discountOpt.getSelectedItem().getValue().toString().equals(MedisafeConstants.RP)){
						jumlah = ((BigDecimal)obj[0]).doubleValue() - discount.getValue().doubleValue();
						racikan.setNDiscAmount(discount.getValue().doubleValue());
						racikan.setNAmountAfterDisc(jumlah);
						racikan.setVDiscType(MedisafeConstants.RP);
					}else{
						jumlah = (discount.getValue().doubleValue()/100) * ((BigDecimal)obj[0]).doubleValue();
						jumlah = ((BigDecimal)obj[0]).doubleValue() - jumlah;
						
						racikan.setNDiscAmount(discount.getValue().doubleValue());
						racikan.setNAmountAfterDisc(jumlah);
						racikan.setVDiscType(MedisafeConstants.PERCENT);
					}
				}
				else{
					racikan.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
					racikan.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
					racikan.setVDiscType(obj[2].toString());
				}
				
				
				racikans.add(racikan);
			}

		}
		
		if(referencePatient.isChecked()){
			//simpan transaksi aptotik utk pasien rujukan
			//data pasien : nama, alamat, tanggal lahir, jenis kelamin, type pasien;
			if(!constraints.validateComponent(true))return;
			patient = new MsPatient();
			patient.setVPatientName(patientName.getText());
			patient.setDPatientDob(dateOfBirth.getValue());
			patient.setVPatientMainAddr(address.getText());
			patient.setVPatientGender(gender.getSelectedItem().getValue());
			if(patientType.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG)
				patient.setMsPatientType((MsPatientType)patientType.getSelectedItem().getValue());
			
			//save(patient, nota, itemSet, racikans, unit, isRanap
			if(apServ.save(patient, nota, itemSet, racikans, (MsUnit)locationList.getSelectedItem().getValue(), 
					isRanap,miscs)){
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
			
		
		}else{
			
			if(apServ.save(patient, nota, itemSet, racikans, 
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap,miscs)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);
				
				statusNota.setVisible(true);
				labelStatus.setVisible(true);
				statusNota.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
				
				setFieldDisable(true);
				setButtonActive(false);
				setInnerButtonDisable(true);
				
				getRegistration(MedisafeConstants.INPUT_BY_MANUAL);
				
			}else{
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
			
		}
		isRanap = false;
		
	}
	
	
	public void setFieldDisable(boolean isDisable){
		referencePatient.setDisabled(isDisable);
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
		patientType.setDisabled(isDisable);
		pharmacyList.setDisabled(isDisable);
		receiptNumber.setReadonly(isDisable);
		total.setReadonly(isDisable);
		grandTotal.setReadonly(isDisable);
		discount.setReadonly(isDisable);
		discountOpt.setDisabled(isDisable);
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
		address.setValue(null);
		patientType.setSelectedIndex(0);
		total.setValue(null);
		grandTotal.setValue(null);
		pharmacyList.getItems().clear();
		referencePatient.setChecked(false);
		MRNumber.setVisible(true);
		registrationNumber.setVisible(true);
		registrationNumber.setReadonly(true);
		transactionNumber.setDisabled(false);
		receiptNumber.setValue(null);
		discount.setValue(null);
		discount.setDisabled(true);
		discountOpt.getItemAtIndex(0);
		discountOpt.setDisabled(true);
		this.patient = null;
		this.reg = null;
		/*this.nota = null;*/
		this.racikan = null;
		this.itemTrx = null;
		
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		patientList.getItems().clear();
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		crNoMR.setValue(null);
		notaList.getItems().clear();
		searchName.setValue(null);
		searchNota.setValue(null);
		mrNumber.setValue(null);
		recipeNumber.setValue(null);
		
		MRNumber.focus();
		
	}
	
	
	public void setButtonActive(boolean isActive){
		this.btnCancelNote.setDisabled(isActive);
		this.btnValidation.setDisabled(isActive);
		//this.btnNew.setDisabled(isActive);
		this.btnModify.setDisabled(isActive);
		/*this.btnCancel.setDisabled(isActive);*/
		this.btnSave.setDisabled(!isActive);
		this.btnCetak.setDisabled(isActive);
		this.btnCetakRj.setDisabled(isActive);
//		this.btnCetakResep.setDisabled(isActive);
	}
	
	
	public void setInnerButtonDisable(boolean isDisable){
		btnItemAdd.setDisabled(isDisable);
		/*btnCancel.setDisabled(isDisable);*/
		btnDelete.setDisabled(isDisable);
		calculateBtn.setDisabled(isDisable);
		btnIngrAdd.setDisabled(isDisable);
		btnMiscAdd.setDisabled(isDisable);
	}
	
	
	public void getNewTransaction(){
		
		statusNota.setVisible(false);
		setFieldDisable(false);
		total.setReadonly(true);
		grandTotal.setReadonly(true);
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
	
	public void cancelNote() throws VONEAppException{
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_APOTIK);
		
		TbExamination nota = (TbExamination)transactionNumber.getAttribute("nota");
		Object[] obj = new Object[]{nota, ((MsUnit)locationList.getSelectedItem().getValue())
				.getMsWarehouse().getNWhouseId(), new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,
				btnCancelNote,statusNota,permission};
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
		
		apServ.getNoteDetil(this);
		
		
	}
	

	public void getRegistration(int type) throws VONEAppException,InterruptedException{
		
		apServ.getRegistration(this,type);
		session.setAttribute(MedisafeConstants.PATIENT_HISTROY,MedisafeConstants.COMMON_POLY);
		session.setAttribute("location", locationList);
		
		
		discount.setReadonly(false);
		discountOpt.setDisabled(false);
		receiptNumber.setReadonly(false);

	}
	
	
	public void getDiscount(int type) throws VONEAppException{
		double disc = 0;
		double afterDisc = 0;
		Listcell cell = null;
		/**
		Integer jumlah;
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		*/
		
//		double nilaiTotal = 0;
		List<Listitem> items = pharmacyList.getItems();
		if(discount.getValue() != null){
			disc = discount.getValue().doubleValue();
			
			
			
			calculateBtn.setDisabled(true);
			for(Listitem item : items){
				cell = (Listcell)item.getChildren().get(6);
				
				Decimalbox ibox = (Decimalbox)cell.getChildren().get(0);
				ibox.setDisabled(true);
				
				Listbox list = (Listbox)cell.getChildren().get(1);
				list.setDisabled(true);
				 
			}
			
			if(discount.getValue().doubleValue() > total.getValue().doubleValue()){
				discount.setValue(new BigDecimal(0));
			}
				
			if(discountOpt.getSelectedItem().getValue().toString().equals(MedisafeConstants.RP)){
				afterDisc = total.getValue().doubleValue() - disc;
				grandTotal.setValue(new BigDecimal(afterDisc));
				
				
	
				
			}
			else{
				afterDisc = total.getValue().doubleValue() * (disc/100);
				afterDisc = total.getValue().doubleValue() - afterDisc;
				grandTotal.setValue(new BigDecimal(afterDisc));
			}
		}
		else grandTotal.setValue(total.getValue());
	
		
	}
	
	
	public void cetak() throws VONEAppException, InterruptedException, IOException{
	
//		NoteReport nr = new NoteReport(nota);
//		
//		if(!nr.printOut(session.getClientAddr())){
//			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU..!!");
//		}
		String ipTarget = this.getCurrentSession().getClientAddr();
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String[] ptype = patientType.getSelectedItem().getLabel().split("-");
		String jk  = this.gender.getSelectedItem().getLabel().equals("PRIA")? "L" : "P";
		
		//pasien data : noMR|nama|tipePasien|tglLahir|UMUR|JK
		String pasien = MRNumber.getText()+ "|" + patientName.getText()+"|" + ptype[1].trim() +
				        "|" + dateOfBirth.getText() + "|" + age.getText() + "|" + jk + "@";
		sb.append(pasien);
		List<Listitem> items = pharmacyList.getItems();
		//format obat : nama;jumlah;aturanPakai;satuan;expiredDate#
		for(Listitem item : items) {
			//nama obat
			Listcell cell = (Listcell)item.getChildren().get(1);
			sb.append(cell.getLabel()+";");
			
			//jumlah
			cell = (Listcell)item.getChildren().get(4);
			Intbox jml = (Intbox)cell.getChildren().get(0);
			sb.append(jml.getText()+";");
			
			//aturan pakai
			cell = (Listcell)item.getChildren().get(5);
			Textbox aturan = (Textbox)cell.getChildren().get(0);
			sb.append(aturan.getText()+";");
			
			//satuan
			cell = (Listcell)item.getChildren().get(3);
			sb.append(cell.getLabel()+";");
			
			//expired date
			//check if racikan
			cell = (Listcell)item.getChildren().get(2);
			if(cell.getLabel().equals("RACIKAN")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				sb.append("ED: "+sdf.format(cal.getTime())+"#");
			}else {
				sb.append("ED: "+ sdf.format(item.getAttribute("expired")) + "#");
			}
			
			
		}
		sb.append("@");
		
		PrintClient.printStringBuffer(ipTarget, sb);

	}
	
	public void cetakRj() throws VONEAppException, InterruptedException, IOException{
		
//		NoteReport nr = new NoteReport(nota);
//		
//		if(!nr.printOut(session.getClientAddr())){
//			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU..!!");
//		}
		String ipTarget = this.getCurrentSession().getClientAddr();
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String[] ptype = patientType.getSelectedItem().getLabel().split("-");
		String jk  = this.gender.getSelectedItem().getLabel().equals("PRIA")? "L" : "P";
		
		//pasien data : noMR|nama|tipePasien|tglLahir|UMUR|JK
		String pasien = MRNumber.getText()+ "|" + patientName.getText()+"|" + ptype[1].trim() +
				        "|" + dateOfBirth.getText() + "|" + age.getText() + "|" + jk + "@";
		sb.append(pasien);
		List<Listitem> items = pharmacyList.getItems();
		//format obat : nama;jumlah;aturanPakai;satuan;expiredDate#
		for(Listitem item : items) {
			//nama obat
			Listcell cellNama = (Listcell)item.getChildren().get(1);
			Listcell cell = (Listcell)item.getChildren().get(2);
			if(cell.getLabel().equals("RACIKAN"))
				sb.append(cell.getLabel()+";");
			else sb.append(cellNama.getLabel()+";");
			
			//jumlah
			cell = (Listcell)item.getChildren().get(4);
			Intbox jml = (Intbox)cell.getChildren().get(0);
			sb.append(jml.getText()+";");
			
			//aturan pakai
			cell = (Listcell)item.getChildren().get(5);
			Textbox aturan = (Textbox)cell.getChildren().get(0);
			sb.append(aturan.getText()+";");
			
			//aturan pakai detail
			cell = (Listcell)item.getChildren().get(5);
			Listbox aturanL = (Listbox)cell.getChildren().get(1);
			sb.append(aturanL.getSelectedItem().getLabel()+";");
			
			
			//satuan
			cell = (Listcell)item.getChildren().get(3);
			sb.append(cell.getLabel()+";");
			
			//expired date
			//check if racikan
			cell = (Listcell)item.getChildren().get(2);
			if(cell.getLabel().equals("RACIKAN")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				sb.append("ED: "+sdf.format(cal.getTime())+"#");
			}else {
				sb.append("ED: "+ sdf.format(item.getAttribute("expired")) + "#");
			}
			
			
		}
		sb.append("@");
		
		PrintClient.printStringBuffer(ipTarget, sb, (short)7777);

	}
	
	public void cetakResep() throws VONEAppException, InterruptedException, IOException{
		
		NoteReport nr = new NoteReport(this.pharmacyList, this.MRNumber.getText(), this.patientName.getText(), this.dateOfBirth.getText(), session);
		
	/**	if(!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK ATURAN PAKAI\nJALANKAN PROGRAM PrintServer DULU..!!");
		}*/
		
	}
	
	
	public void modify() throws VONEAppException{
		
		List<Listitem> items = pharmacyList.getItems();
		
		Decimalbox diskon = null;
		Intbox jumlah = null;
		Listbox diskonOptionList = null;
		
		Listcell cell = null;
		
		for(Listitem item : items)
		{
			cell = (Listcell)item.getChildren().get(4);
			try {
				jumlah = (Intbox)cell.getChildren().get(0);
			} catch (RuntimeException e) {
				
				jumlah = new Intbox();
				jumlah.setValue(new Integer(cell.getLabel()));
				cell.setLabel("");
				jumlah.setParent(cell);
			}
			if(item.getValue() instanceof TbDrugIngredients)
				jumlah.setDisabled(true);
			else jumlah.setDisabled(false);
			jumlah.addEventListener("onChange", new EditApotikListener(item,pharmacyList,grandTotal,total,discount));
			
			cell = (Listcell)item.getChildren().get(7);
			diskon = (Decimalbox)cell.getChildren().get(0);
			diskon.addEventListener("onChange", new EditApotikListener(item,pharmacyList,grandTotal,total,discount));
			
			diskonOptionList = (Listbox)cell.getChildren().get(1);
			diskonOptionList.addEventListener("onSelect", new EditApotikListener(item,pharmacyList,grandTotal,total,discount));
			
			diskon.setDisabled(false);
			diskonOptionList.setDisabled(false);
			
		}
		setButtonActive(true);
		this.btnSave.setDisabled(false);
		isUpdate = true;
		MiscTrxController.setFont(pharmacyList);
	}
	
	
	public void saveModify() throws VONEAppException, InterruptedException
	{
		Date tanggal = new Date();
		
		nota.setTotal(this.grandTotal.getValue().doubleValue());
		nota.setDWhnChange(tanggal);
		nota.setVWhoChange(user.getVUserName());
		
		
	
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbDrugIngredients> racikanSet = new HashSet<TbDrugIngredients>(0);
		Set<TbMiscTrx> miscs = new HashSet<TbMiscTrx>();
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		double trxValue = 0;
		Intbox jumlah = null;
		Decimalbox diskon = null;
		Listbox opsiDiskon = null;
		
		List<Listitem> items = pharmacyList.getItems();
		for(Listitem item : items){
			if(item.getValue() instanceof Object[]){
				
				itemTrx = new TbItemTrx();
				
				Object[] object = (Object[])item.getValue();
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)object[0]);
				itemTrx.setMsItem(msItem);
				
				if(((Listcell)item.getChildren().get(5)).getChildren().size() == 0){
					itemTrx.setAturanPakai(((Listcell)item.getChildren().get(5)).getLabel());
					item.setAttribute("aturanPakai", ((Listcell)item.getChildren().get(5)).getLabel());
				}
				else{
					Textbox apak = (Textbox) ((Listcell)item.getChildren().get(5)).getChildren().get(0);
					Listbox aturSelect = (Listbox) ((Listcell)item.getChildren().get(5)).getChildren().get(1);
					itemTrx.setAturanPakai(apak.getText()+"-"+aturSelect.getSelectedItem().getLabel());
					item.setAttribute("aturanPakai", itemTrx.getAturanPakai());
					
				}
				
				cell = (Listcell)item.getChildren().get(4);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				itemTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(8);
				db.setText(cell.getLabel());
				itemTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(7);
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
			else if(item.getValue() instanceof TbMiscTrx){
				TbMiscTrx misc = (TbMiscTrx)item.getValue();
				misc.setDWhnChange(tanggal);
				
				cell = (Listcell)item.getChildren().get(4);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				misc.setNQty(jumlah.getValue().shortValue());
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				misc.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(8);
				db.setText(cell.getLabel());
				misc.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(7);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				misc.setNDiscAmount(diskon.getValue().doubleValue());
				misc.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				
				miscs.add(misc);
				
			}
			else if(item.getValue() instanceof TbDrugIngredients){
				racikan = (TbDrugIngredients)item.getValue();
				racikan.setDWhnChange(tanggal);
				
				cell = (Listcell)item.getChildren().get(4);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				racikan.setNDingrQty(jumlah.getValue().shortValue());
				
				if(((Listcell)item.getChildren().get(5)).getChildren().size() == 0){
					racikan.setAturanPakai(((Listcell)item.getChildren().get(5)).getLabel());
					item.setAttribute("aturanPakai", ((Listcell)item.getChildren().get(5)).getLabel());
				}
				else{
					Textbox apak = (Textbox) ((Listcell)item.getChildren().get(5)).getChildren().get(0);
					Listbox aturSelect = (Listbox) ((Listcell)item.getChildren().get(5)).getChildren().get(1);
					racikan.setAturanPakai(apak.getText()+"-"+aturSelect.getSelectedItem().getLabel());
					item.setAttribute("aturanPakai", racikan.getAturanPakai());
				}
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				racikan.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(8);
				db.setText(cell.getLabel());
				racikan.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(7);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				racikan.setNDiscAmount(diskon.getValue().doubleValue());
				racikan.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				racikanSet.add(racikan);
			}
			
		}
		
		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		
		if(transactionNumber.getText().charAt(0) == 'I'){
			if(this.apServ.saveModify(nota, itemSet, racikanSet, warehouseId,miscs)){
				Messagebox.show(MessagesService.getKey("common.modify.success"));
			}
			else Messagebox.show(MessagesService.getKey("common.modify.fail"));
			
			setButtonActive(false);
			this.btnSave.setDisabled(true);
			
			return;
		}
		
		NoteManager manager = Service.getNotaManager();
		
		
		if(manager.saveModifyNote(nota, itemSet, null, null,null, racikanSet, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}
	
	public void convertToMr(Textbox mrNumber){
	
		mrNumber.setValue(MedisafeUtil.convertToMrCode(mrNumber.getText()));
		
	}
	
}

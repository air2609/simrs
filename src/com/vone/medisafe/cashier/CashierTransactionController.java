package com.vone.medisafe.cashier;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsCreditCardType;
import com.vone.medisafe.mapping.MsInsurance;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.mapping.TbPatientDeposit;
import com.vone.medisafe.mapping.TbPatientSettlement;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.report.NoteReport;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class CashierTransactionController extends BaseController
{	
	public MsUser user;
	public MsStaffInUnit siu;
	public TbRegistration reg;
	public TbMedicalRecord mr;
	public MsPatient patient;
	public String kelasTarif;
	public double discounttmp;
	public boolean isRetur;
	public TbPatientBill pbill;
	public Collection<TbExamination> noteCollection;
	public String nomorKwitansi;
	public Date tanggalKwitansi;
	public MsBed msBed;
	public TbBedOccupancy boc;
	public boolean isPembayaran;
	
	
	Listbox locationList;
	Radiogroup transactionType;
	Bandbox MRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox MRList;
	Bandbox noteNumber;
	Textbox searchNote;
	Textbox searchName;
	Listbox noteList;
	Textbox registrationNumber;
	Bandbox transactionNumber;
	Textbox trxNo;
	Textbox name;
	Listbox chooseTrx;
	Listbox transactionNumberList;
	Textbox patientName;
	Textbox address;
	Textbox patientType;
	Textbox bed;
	Textbox nameOnBill;
	Textbox addrOnBill;
	Label labelStatus;
	Label statusNota;
	Listbox cashierList;
	Decimalbox total;
	Decimalbox ppn;
	Decimalbox cashPay;
	Decimalbox returnTtl;
	Textbox returnNo;
	Decimalbox creditPay;
	Decimalbox discount;
	Decimalbox totalAmount;
	Decimalbox depositPaid;
	Decimalbox discountTotal;
	Decimalbox amountOfDeposit;
	Decimalbox amountOfReturn;
	Listbox discountType;
	Listbox billOptionList;
																																					
	Button btnPay;
	Button btnNew;
//Button btnCancel;
	Button btnLock;
	Button btnOut;
	Button btnCetak;
//	Button btnEnd;
	
	Listitem item;
	Listcell cell;
	
	//payment term window
	Window winPayment;
	Listbox paymentList;
	
	ZulConstraint constraints = new ZulConstraint();
	
	private CashierManager serv = Service.getCashierManager();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	UserManager userService = ServiceLocator.getUserManager();
	
	
	Decimalbox db = new Decimalbox();
	
	Window win;
	
	MsCoa coaKas;
	
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
		transactionType = (Radiogroup)win.getFellow("transactionType");
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		MRList = (Listbox)win.getFellow("MRList");
		noteNumber = (Bandbox)win.getFellow("noteNumber");
		searchNote = (Textbox)win.getFellow("searchNote");
		searchName = (Textbox)win.getFellow("searchName");
		noteList = (Listbox)win.getFellow("noteList");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		trxNo = (Textbox)win.getFellow("trxNo");
		name = (Textbox)win.getFellow("name");
		chooseTrx = (Listbox)win.getFellow("chooseTrx");
		transactionNumberList = (Listbox)win.getFellow("transactionNumberList");
		patientName = (Textbox)win.getFellow("patientName");
		address = (Textbox)win.getFellow("address");
		patientType = (Textbox)win.getFellow("patientType");
		bed = (Textbox)win.getFellow("bed");
		nameOnBill = (Textbox)win.getFellow("nameOnBill");
		addrOnBill = (Textbox)win.getFellow("addrOnBill");
		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		cashierList = (Listbox)win.getFellow("cashierList");
		
		total = (Decimalbox)win.getFellow("total");
		ppn = (Decimalbox)win.getFellow("ppn");
		cashPay = (Decimalbox)win.getFellow("cashPay");
		returnTtl = (Decimalbox)win.getFellow("returnTtl");
		returnNo = (Textbox)win.getFellow("returnNo");
		creditPay = (Decimalbox)win.getFellow("creditPay");
		discount = (Decimalbox)win.getFellow("discount");
		totalAmount = (Decimalbox)win.getFellow("totalAmount");
		depositPaid = (Decimalbox)win.getFellow("depositPaid");
		discountTotal = (Decimalbox)win.getFellow("discountTotal");
		amountOfDeposit = (Decimalbox)win.getFellow("amountOfDeposit");
		amountOfReturn = (Decimalbox)win.getFellow("amountOfReturn");
		discountType = (Listbox)win.getFellow("discountType");
		billOptionList = (Listbox)win.getFellow("billOptionList");
		
		
		
		this.win = (Window)win;
																																						
		btnPay = (Button)win.getFellow("btnPay");
		btnNew = (Button)win.getFellow("btnNew");
	//	btnCancel = (Button)win.getFellow("btnCancel");
		btnLock = (Button)win.getFellow("btnLock");
		btnOut = (Button)win.getFellow("btnOut");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");
		
		db.setFormat("#,###.##");
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchNote, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(trxNo, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name, ZulConstraint.UPPER_CASE);
		
		
		user = getUserInfoBean().getMsUser();
		setStatusNotaVisible(false);
		cashierList.getItems().clear();
		
		//noteNumber.setReadonly(true);
		setButtonDisable(true);
		btnPay.setDisabled(false);
//		btnEnd.setDisabled(false);
		btnNew.setDisabled(false);
		
		
		clear();
		
		
		userService.getUnitUser(user, locationList);
		serv.cekCoa(this);
		coaKas = ((MsUnit)locationList.getSelectedItem().getValue()).getCoa();
		
	}
	
	

	
	
	
	
	public void setStatusNotaVisible(boolean isVisible) throws VONEAppException{
		labelStatus.setVisible(isVisible);
		statusNota.setVisible(isVisible);
	}
	
	public void setButtonDisable(boolean isDisable) throws VONEAppException{
		
	//	this.btnCancel.setDisabled(isDisable);
		this.btnCetak.setDisabled(isDisable);
//		this.btnEnd.setDisabled(isDisable);
		this.btnLock.setDisabled(isDisable);
		this.btnNew.setDisabled(isDisable);
		this.btnOut.setDisabled(isDisable);
		this.btnPay.setDisabled(isDisable);

		
	}
	
	
	public void setFieldDisabel(boolean isDisable){
		locationList.setDisabled(isDisable);
		List radios = transactionType.getChildren();
		((Radio)radios.get(0)).setDisabled(isDisable);
		((Radio)radios.get(1)).setDisabled(isDisable);
		((Radio)radios.get(2)).setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
//		noteNumber.setDisabled(isDisable);
	//	registrationNumber.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		trxNo.setDisabled(isDisable);
		name.setDisabled(isDisable);
		chooseTrx.setDisabled(isDisable);
		transactionNumberList.setDisabled(isDisable);
		patientName.setReadonly(isDisable);
		address.setReadonly(isDisable);
		patientType.setReadonly(isDisable);
		bed.setReadonly(isDisable);
		nameOnBill.setReadonly(isDisable);
		addrOnBill.setReadonly(isDisable);
		
	}
	
	public void setManipulatedFiledDisable(boolean isDisable) throws VONEAppException{
		total.setReadonly(isDisable);
		ppn.setReadonly(isDisable);
		cashPay.setReadonly(isDisable);
		returnTtl.setReadonly(isDisable);
		returnNo.setReadonly(isDisable);
		creditPay.setReadonly(isDisable);
		discount.setReadonly(isDisable);
		totalAmount.setReadonly(isDisable);
		depositPaid.setReadonly(isDisable);
		discountTotal.setReadonly(isDisable);
		amountOfDeposit.setReadonly(isDisable);
		amountOfReturn.setReadonly(isDisable);
		discountType.setDisabled(isDisable);
	}
	
	public void setManipulatedFiledVisible(boolean isDisable) throws VONEAppException{
		total.setVisible(isDisable);
		ppn.setVisible(isDisable);
		cashPay.setVisible(isDisable);
		returnTtl.setVisible(isDisable);
		returnNo.setVisible(isDisable);
		creditPay.setVisible(isDisable);
		discount.setVisible(isDisable);
		totalAmount.setVisible(isDisable);
		depositPaid.setVisible(isDisable);
		discountTotal.setVisible(isDisable);
		amountOfDeposit.setVisible(isDisable);
		amountOfReturn.setVisible(isDisable);
		discountType.setVisible(isDisable);
	}
	
	public void clear() throws VONEAppException, InterruptedException{

		MRNumber.setValue(null);
		crNoMR.setValue(null);
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		MRList.getItems().clear();
		noteNumber.setValue(null);
		searchNote.setValue(null);
		searchName.setValue(null);
		noteList.getItems().clear();
		registrationNumber.setValue(null);
		transactionNumber.setValue(null);
		trxNo.setValue(null);
		name.setValue(null);
		chooseTrx.setSelectedIndex(0);
		transactionNumberList.getItems().clear();
		patientName.setValue(null);
		address.setValue(null);
		patientType.setValue(null);
		bed.setValue(null);
		nameOnBill.setValue(null);
		addrOnBill.setValue(null);
		cashierList.getItems().clear();
		billOptionList.setSelectedIndex(0);
		transactionType.setSelectedIndex(0);

		MRNumber.setDisabled(false);
	//	transactionNumber.setDisabled(false);
		nameOnBill.setReadonly(false);
		noteNumber.setDisabled(false);
		addrOnBill.setReadonly(false);
		ppn.setReadonly(false);
		cashPay.setReadonly(false);
		depositPaid.setReadonly(false);
		discount.setReadonly(false);
		discountType.setDisabled(false);
		
		total.setValue(null);
		returnTtl.setValue(null);
		returnNo.setValue(null);
		discount.setValue(null);
		totalAmount.setValue(null);
		discountTotal.setValue(null);
		amountOfDeposit.setValue(null);
		amountOfReturn.setValue(null);
			
		cashPay.setValue(new BigDecimal(0));
		creditPay.setValue(new BigDecimal(0));
		depositPaid.setValue(new BigDecimal(0));
		amountOfDeposit.setValue(new BigDecimal(0));
		ppn.setValue(new BigDecimal(0));
		transactionNumber.setDisabled(false);
		MRNumber.focus();
		
		this.reg = null;
		this.mr = null;
		this.patient = null;
		this.pbill = null;
		this.msBed = null;
		
		setButtonDisable(true);
		btnPay.setDisabled(false);
//		btnEnd.setDisabled(false);
		btnNew.setDisabled(false);
		
		Page page = null;
		try {
			page = win.getDesktop().getPage("pageSettlement");
		} catch (RuntimeException e) {
			
			//DO NOTHIN
		}
		if(page != null){
			winPayment = (Window)page.getFellow("caraPembayaran");
			paymentList = (Listbox)winPayment.getFellow("cashierLists");
			paymentList.getItems().clear();
		}
		
		getTransactionType();
		
	}
	
	public void searchNote() throws VONEAppException, InterruptedException{

		serv.searchNote(this);
		
			
	}
	
	
	public void getNotesDetail() throws VONEAppException, InterruptedException{
		
		
		serv.getNoteDetil(this);
		
		
	}
	
	public void getRegistration(int type) throws VONEAppException,InterruptedException{
		
		serv.getRegistration(this,type);
		this.btnCetak.setDisabled(true);
		this.btnLock.setDisabled(false);
		this.btnOut.setDisabled(false);
		
	}
	
	public void getDiscountValue() throws VONEAppException{
		//manipulated field
//		this.total
//		this.ppn
//		this.returnTtl
//		this.discount
//		discountType
//		totalAmount
//		discountTotal
		
		double totaltmp = 0;
		if(this.total.getValue() != null && this.returnTtl.getValue() != null)
			totaltmp = this.total.getValue().doubleValue() - this.returnTtl.getValue().doubleValue();
		double jumlahDiscount = 0;
		double nilaiPajak = 0;
		double totalDiscount = 0;
		
		double diskon = 0;
		
		if(discount.getText().trim().equals("")){
			diskon = 0;
		}
		else diskon = discount.getValue().doubleValue();
		//total setelah retur
		
		
				
		if(discountType.getSelectedItem().getValue().toString().equals(MedisafeConstants.RP)){
			jumlahDiscount = diskon;
		}else{
			jumlahDiscount = (diskon/100) * totaltmp;
		}
		
		if(jumlahDiscount > totaltmp){
			
			try {
				Messagebox.show(MessagesService.getKey("diskon.melebihi.jumlah.pembayaran"));
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//total setelah discount
		totaltmp = totaltmp - jumlahDiscount;
		
		//nilai pajak
		double pajak = 0;
		if(this.ppn.getValue() == null || this.ppn.getValue().doubleValue() < 0){
			pajak = 0;
			this.ppn.setValue(new BigDecimal(0));
		}
		else pajak = this.ppn.getValue().doubleValue();
			
			
		nilaiPajak = (pajak/100) * totaltmp;
		
		//total setelah pajak
		totaltmp = totaltmp + nilaiPajak;
		
		//total discount
		
		totalDiscount = discounttmp + jumlahDiscount;
		this.discountTotal.setValue(new BigDecimal(totalDiscount));
		this.totalAmount.setValue(new BigDecimal(totaltmp));
		
	}
	
	public void doSave() throws VONEAppException, InterruptedException,IOException{
		Date tanggal = new Date();
		
		List<TbPatientSettlement> settlements = new ArrayList<TbPatientSettlement>();
		TbPatientSettlement patientSettlement = null;
		
				
		
		if(((Radio)transactionType.getSelectedItem()).getLabel().equals("PELUNASAN")){
			if(cashierList.getItems().size() == 0){
				return;
			}
			if(isRetur){
				pbill = new TbPatientBill();
				pbill.setNPaymentStatus(MedisafeConstants.BELUM_LUNAS);
				pbill.setDWhnCreate(tanggal);
				pbill.setNPbillStatus((short)MedisafeConstants.ACTIVE_NOTE);
				pbill.setNPbillTtlPaid(this.totalAmount.getValue().doubleValue());
				pbill.setVWhoCreate(user.getVUserName());
				if(reg != null)pbill.setTbRegistration(reg);
				Integer id = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.SEQ_PBILL);
				pbill.setVPbillCode(MedisafeUtil.convert2BillCode(tanggal, id));
				
				TbReturPharmacyTrx retur = (TbReturPharmacyTrx)transactionNumberList.getSelectedItem().getValue();
				pbill.setVNameOnBill(retur.getMsPatient().getVPatientName());
				pbill.setVAddrOnBill(retur.getMsPatient().getVPatientMainAddr());
				
				//simpan
				if(serv.saveBillRetur(pbill, retur)){
					Messagebox.show(MessagesService.getKey("common.save.success"));
					this.noteNumber.setValue(pbill.getVPbillCode());
				}else Messagebox.show(MessagesService.getKey("common.save.fail"));
				
			}
			else{
				
				
				
//				if(msBed != null){
//					if(msBed.getVBedStatus().equals(MedisafeConstants.BED_TERPAKAI)){
//						Messagebox.show(MessagesService.getKey("bed.transaction.not.closed"));
//						return;
//					}
//				}
				
				if(this.amountOfReturn.getValue() == null){
					Messagebox.show(MessagesService.getKey("payment.cannot.be.procced"));
					return;
				}
				
				if(this.amountOfReturn.getValue().doubleValue() > 0){
					Messagebox.show(MessagesService.getKey("payment.not.enough"));
					this.cashPay.focus();
					return;
				}		
				
				
				
				pbill = new TbPatientBill();
				pbill.setNPaymentStatus(MedisafeConstants.BELUM_LUNAS);
				pbill.setDWhnCreate(tanggal);
				pbill.setNPbillStatus((short)MedisafeConstants.ACTIVE_NOTE);
				pbill.setNPbillTtlPaid(this.totalAmount.getValue().doubleValue());
				pbill.setVWhoCreate(user.getVUserName());
				if(reg != null)pbill.setTbRegistration(reg);
				Integer id = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.SEQ_PBILL);
				pbill.setVPbillCode(MedisafeUtil.convert2BillCode(tanggal, id));
				pbill.setVNameOnBill(this.nameOnBill.getText());
				pbill.setVAddrOnBill(this.addrOnBill.getText());
				pbill.setNPbillSubTtl(this.total.getValue().doubleValue());
				pbill.setNPbillTtlPaid(this.totalAmount.getValue().doubleValue());
				pbill.setNPbillDisc(this.discountTotal.getValue().floatValue());
				
				float pajak = (this.ppn.getValue().floatValue()/100) * (this.total.getValue().floatValue() - this.returnTtl.getValue().floatValue());
				pbill.setNPbillTax(pajak);
				
				
				List<TbExamination> notas = new ArrayList<TbExamination>();
											
				
				Iterator it = transactionNumberList.getSelectedItems().iterator();
				while(it.hasNext()){
					item = (Listitem)it.next();
					TbExamination note = (TbExamination)item.getValue();
					notas.add(note);
				}
				
				this.noteCollection = notas;
				Page page = win.getDesktop().getPage("pageSettlement");
				
				if(cashPay.getValue().doubleValue() > 0){
					
					patientSettlement = new TbPatientSettlement();
					double nilai = totalAmount.getValue().doubleValue() - (depositPaid.getValue().doubleValue() + 
																				creditPay.getValue().doubleValue());
					if(cashPay.getValue().doubleValue() > nilai){
						patientSettlement.setNAmountSettled(nilai);
					}
					else
						patientSettlement.setNAmountSettled(cashPay.getValue().doubleValue());
					patientSettlement.setNPsettlementType(MedisafeConstants.CASH_SETTLEMENT);
					patientSettlement.setVWhoCreate(user.getVUserName());
					
					settlements.add(patientSettlement);
					
				}
				
				
				if(depositPaid.getValue().doubleValue() > 0){
					
					patientSettlement = new TbPatientSettlement();
					patientSettlement.setNAmountSettled(depositPaid.getValue().doubleValue());
					patientSettlement.setNPsettlementType(MedisafeConstants.DEPOSIT_SETTLEMENT);
					patientSettlement.setVWhoCreate(user.getVUserName());
					
					settlements.add(patientSettlement);
					
				}
				
				if(page != null){
					winPayment = (Window)page.getFellow("caraPembayaran");
					paymentList = (Listbox)winPayment.getFellow("cashierLists");
					
					List<Listitem> items = paymentList.getItems();
					
					for(Listitem item : items){
						Object[] obj = (Object[])item.getValue();
						if(obj.length == 3){
							//settlement by bank
//							obj[0] -- > MsCreditCardType 
//							ojb[1] -- > payment value
//							ojb[2] -- > account number
							
							MsCreditCardType cardType = (MsCreditCardType)obj[0];
							
							patientSettlement = new TbPatientSettlement();
							patientSettlement.setMsCreditCardType(cardType);
							patientSettlement.setMsBank(cardType.getMsBank());
							patientSettlement.setNAmountSettled((Double)obj[1]);
							patientSettlement.setNPsettlementType(new Short(MedisafeConstants.BANK_SETTLEMENT));
							patientSettlement.setVPatientAccountNo(obj[2].toString());
							patientSettlement.setVWhoCreate(user.getVUserName());
							
							settlements.add(patientSettlement);
							
							
						}
						else{
							//settlement by assurance
//							obj[0] -- > MsInsurance
//							obj[1] -- > payment value
							
							patientSettlement = new TbPatientSettlement();
							patientSettlement.setMsInsurance((MsInsurance)obj[0]);
							patientSettlement.setNAmountSettled((Double)obj[1]);
							patientSettlement.setNPsettlementType(new Short(MedisafeConstants.INSURANCE_SETTLEMENT));
							patientSettlement.setVWhoCreate(user.getVUserName());
							
							settlements.add(patientSettlement);
						}
					}
				}
				
				int confirm = Messagebox.show(MessagesService.getKey("konfirmasi.bayar.nota"), "KONFIRMASI", Messagebox.YES |
						Messagebox.NO, Messagebox.NONE);
				if(confirm == Messagebox.NO)return;
				
				if(returnTtl.getValue().doubleValue() > 0){
					if(!(returnNo.getText().length() > 1)){
						Messagebox.show(MessagesService.getKey("pasien.belum.melakukan.retur"));
						return;
					}
				}
				
				//save
				coaKas = ((MsUnit)locationList.getSelectedItem().getValue()).getCoa();
				if(serv.saveBillNote(pbill, notas,settlements,this.depositPaid, coaKas)){
					
					Messagebox.show(MessagesService.getKey("common.save.success"));
					this.noteNumber.setValue(pbill.getVPbillCode());
					noteCollection = notas;
					nomorKwitansi = pbill.getVPbillCode();
					tanggalKwitansi = pbill.getDWhnCreate();
					
					cetakKwitansi();
					
					double depositBalance = this.amountOfDeposit.getValue().doubleValue() - this.depositPaid.getValue().doubleValue();
					this.amountOfDeposit.setValue(new BigDecimal(depositBalance));
					
					
					
				}else Messagebox.show(MessagesService.getKey("common.save.fail"));
				
			}
			MRNumber.setDisabled(true);
			transactionNumber.setDisabled(true);
		//	nameOnBill.setDisabled(true);
			nameOnBill.setReadonly(true);
			addrOnBill.setReadonly(true);
			btnPay.setDisabled(true);
			ppn.setReadonly(true);
			cashPay.setReadonly(true);
			depositPaid.setReadonly(true);
			discount.setReadonly(true);
			discountType.setDisabled(true);
			btnNew.focus();
		}
		else if(((Radio)transactionType.getSelectedItem()).getLabel().equals("DEPOSIT")){
			if(reg != null){
				if(this.registrationNumber.getText().charAt(0) == 'I'){
					
					List<TbPatientSettlement> detils = new ArrayList<TbPatientSettlement>();
					
					pbill = new TbPatientBill();
					pbill.setNPaymentStatus(MedisafeConstants.BELUM_LUNAS);
					pbill.setDWhnCreate(tanggal);
					pbill.setNPbillStatus((short)MedisafeConstants.ACTIVE_NOTE);
//					pbill.setNPbillTtlPaid(this.cashPay.getValue().doubleValue());
					pbill.setVWhoCreate(user.getVUserName());
					pbill.setTbRegistration(reg);
					Integer id = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.SEQ_PBILL);
					pbill.setVPbillCode(MedisafeUtil.convert2BillCode(tanggal, id));
					pbill.setVNameOnBill(this.nameOnBill.getValue());
					pbill.setVAddrOnBill(this.addrOnBill.getValue());
					
					
					if(cashPay.getValue().doubleValue() > 0){
						
						patientSettlement = new TbPatientSettlement();
						patientSettlement.setNAmountSettled(cashPay.getValue().doubleValue());
						patientSettlement.setNPsettlementType(MedisafeConstants.CASH_SETTLEMENT);
						patientSettlement.setVWhoCreate(user.getVUserName());
						
						detils.add(patientSettlement);
						
					}
					
					Page page = win.getDesktop().getPage("pageSettlement");
					
					if(page != null){
						winPayment = (Window)page.getFellow("caraPembayaran");
						paymentList = (Listbox)winPayment.getFellow("cashierLists");
						
						List<Listitem> items = paymentList.getItems();
						
						for(Listitem item : items){
							Object[] obj = (Object[])item.getValue();
							if(obj.length == 3){
								//settlement by bank
//								obj[0] -- > MsCreditCardType 
//								ojb[1] -- > payment value
//								ojb[2] -- > account number
								
								MsCreditCardType cardType = (MsCreditCardType)obj[0];
								
								patientSettlement = new TbPatientSettlement();
								patientSettlement.setMsCreditCardType(cardType);
								patientSettlement.setMsBank(cardType.getMsBank());
								patientSettlement.setNAmountSettled((Double)obj[1]);
								patientSettlement.setNPsettlementType(new Short(MedisafeConstants.BANK_SETTLEMENT));
								patientSettlement.setVPatientAccountNo(obj[2].toString());
								patientSettlement.setVWhoCreate(user.getVUserName());
								
								detils.add(patientSettlement);
								
								
							}
							else{
								//settlement by assurance
//								obj[0] -- > MsInsurance
//								obj[1] -- > payment value
								
								patientSettlement = new TbPatientSettlement();
								patientSettlement.setMsInsurance((MsInsurance)obj[0]);
								patientSettlement.setNAmountSettled((Double)obj[1]);
								patientSettlement.setNPsettlementType(new Short(MedisafeConstants.INSURANCE_SETTLEMENT));
								patientSettlement.setVWhoCreate(user.getVUserName());
								
								detils.add(patientSettlement);
							}
						}
					}
//					
					TbPatientDeposit deposit = new TbPatientDeposit();
					deposit.setTbRegistration(reg);
					deposit.setVWhoCreate(user.getVUserName());
					deposit.setDWhnCreate(tanggal);
					
					double jumlahDeposit = this.cashPay.getValue().doubleValue() + this.creditPay.getValue().doubleValue();
					pbill.setNPbillTtlPaid(jumlahDeposit);
					deposit.setNMutation(jumlahDeposit);
					TbPatientDeposit balance = serv.getPatientBalance(reg);
					if(balance != null){
						double newBalance = jumlahDeposit + balance.getNBalance();
						deposit.setNBalance(newBalance);
					}else deposit.setNBalance(jumlahDeposit);
					
					coaKas = ((MsUnit)locationList.getSelectedItem().getValue()).getCoa();
					if(serv.saveDeposit(deposit,pbill, coaKas, detils))
					{
						Messagebox.show(MessagesService.getKey("deposit.save.success"));
						this.noteNumber.setValue(pbill.getVPbillCode());
						this.amountOfDeposit.setValue(new BigDecimal(deposit.getNBalance()));
						
					}else {
						Messagebox.show(MessagesService.getKey("common.save.fail"));
					}
				}
				else Messagebox.show(MessagesService.getKey("bukan.pasient.ranap"));
				
				
			}else{
				Messagebox.show(MessagesService.getKey("no.ranap.patient"));
				return;
			}
			this.btnPay.setDisabled(true);
			this.nameOnBill.setReadonly(true);
			this.addrOnBill.setReadonly(true);
			this.cashPay.setReadonly(true);
			this.MRNumber.setDisabled(true);
			this.btnLock.setDisabled(true);
			this.btnOut.setDisabled(true);
			this.transactionNumber.setDisabled(true);
			btnNew.focus();
		}
		else if(((Radio)transactionType.getSelectedItem()).getLabel().equals("RETUR-DEPOSIT")){
			if(reg != null){
				if(this.registrationNumber.getText().charAt(0) == 'I'){
					
					
//					
					TbPatientDeposit deposit = new TbPatientDeposit();
					deposit.setNMutation(-cashPay.getValue().doubleValue());
					deposit.setTbRegistration(reg);
					deposit.setVWhoCreate(user.getVUserName());
					deposit.setDWhnCreate(tanggal);
					
					TbPatientDeposit balance = serv.getPatientBalance(reg);
					if(balance != null){
						double newBalance = balance.getNBalance() - cashPay.getValue().doubleValue();
						deposit.setNBalance(newBalance);
					}//else deposit.setNBalance(amountOfDeposit.getValue().doubleValue());
					
					pbill = new TbPatientBill();
					pbill.setNPaymentStatus(MedisafeConstants.BELUM_LUNAS);
					pbill.setDWhnCreate(tanggal);
					pbill.setNPbillStatus((short)MedisafeConstants.ACTIVE_NOTE);
					pbill.setNPbillTtlPaid(0 - this.amountOfDeposit.getValue().doubleValue());
					pbill.setVWhoCreate(user.getVUserName());
					pbill.setTbRegistration(reg);
					Integer id = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.SEQ_PBILL);
					pbill.setVPbillCode(MedisafeUtil.convert2BillCode(tanggal, id));
					pbill.setVNameOnBill(this.nameOnBill.getValue());
					pbill.setVAddrOnBill(this.addrOnBill.getValue());
					
					coaKas = ((MsUnit)locationList.getSelectedItem().getValue()).getCoa();					
					if(serv.saveDeposit(deposit,pbill, coaKas, null))
					{
						Messagebox.show(MessagesService.getKey("return.deposit.save.success"));
						this.noteNumber.setValue(pbill.getVPbillCode());
						this.amountOfDeposit.setValue(new BigDecimal(deposit.getNBalance()));
						
					}else Messagebox.show(MessagesService.getKey("common.save.fail"));
				}				
				
			}else{
				Messagebox.show(MessagesService.getKey("no.ranap.patient"));
				return;
			}
			this.btnPay.setDisabled(true);
			this.nameOnBill.setReadonly(true);
			this.addrOnBill.setReadonly(true);
			this.cashPay.setReadonly(true);
			this.MRNumber.setDisabled(true);
			this.btnLock.setDisabled(true);
			this.btnOut.setDisabled(true);
			this.transactionNumber.setDisabled(true);
			btnNew.focus();
		}
		
		
		btnCetak.setDisabled(false);

		
	}
	
	
	public void searchBill() throws VONEAppException, InterruptedException{
//		this.searchNote
//		this.searchName
		
//		this.noteList
		boolean isRetur = false;
		
		this.noteList.getItems().clear();
		if(billOptionList.getSelectedIndex() != 0){
			isRetur = true;
		}
			
		List<TbPatientBill> bills = serv.getPatientBills("%"+searchName.getText()+"%", "%"+searchNote.getText()+"%",
				                    MedisafeConstants.BELUM_LUNAS, isRetur);
		for(TbPatientBill bill : bills){
			item = new Listitem();
			item.setParent(noteList);
			item.setValue(bill);
			
			cell = new Listcell(bill.getVPbillCode());
			cell.setParent(item);
			
			cell = new Listcell(bill.getVNameOnBill());
			cell.setParent(item);
		}
		MiscTrxController.setFont(noteList);
	}
	
	
	public void getBillDetil() throws VONEAppException{
		
		serv.getBillDetil(this);
		

	}
	
		
	
	public void lock() throws VONEAppException, InterruptedException{
		String confirmation = "";
		String status ="";
		
		if(btnLock.getLabel().equals("LOCK")){
			
			reg.setRegStatus(MedisafeConstants.REG_LOCKED);
			reg.setVWhoChange(user.getVUserName());
			reg.setDWhnChange(new Date());
			confirmation = "confirmation.locked";
			status = "registration.is.locked";
			
			
		}else{
			
			reg.setRegStatus(MedisafeConstants.REG_ACTIVE);
			reg.setVWhoChange(user.getVUserName());
			reg.setDWhnChange(new Date());
			confirmation="confirmation.unlock";
			status = "registration.active";
			
		}
		
		
		
		int result = Messagebox.show(MessagesService.getKey(confirmation), 
				"KONFIRMASI", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.NO) return;
		
		if(serv.lockRegistration(reg)){
			Messagebox.show(MessagesService.getKey(status));
			if(btnLock.getLabel().equals("LOCK")) btnLock.setLabel("UNLOCK");
			else btnLock.setLabel("LOCK");
		}
		
	}
	
	
	public void checkOut() throws VONEAppException, InterruptedException{
		
		/*if(msBed != null){
			if(msBed.getVBedStatus().equals(MedisafeConstants.BED_TERPAKAI)){
				Messagebox.show(MessagesService.getKey("bed.transaction.not.closed"));
				return;
			}
		}*/
		if(boc != null && boc.getDCheckOutTime() == null){
			Messagebox.show(MessagesService.getKey("bed.transaction.not.closed"));
			return;
		}
		
		List<TbExamination> notaBelumDiBayar = this.serv.getUnbillNote(reg);
		if(notaBelumDiBayar.size() > 0){
			String notas ="";
			for(TbExamination nota : notaBelumDiBayar){
				notas = notas+nota.getVNoteNo()+",";
			}
			
			if(notas.length() > 1){
				notas.substring(0, notas.length()-1);
			}
			
			Messagebox.show(notas+" BELUM DIBAYAR, CHECK OUT GAGAL!");
			return;
		}
		
		reg.setRegStatus(MedisafeConstants.REG_NON_ACTIVE);
		reg.setVWhoChange(user.getVUserName());
		reg.setDWhnChange(new Date());
		
		
		
		int result = Messagebox.show(MessagesService.getKey("confirmation.check.out"), 
				"KONFIRMASI", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.NO) return;
		
		if(serv.lockRegistration(reg)){
			Messagebox.show(MessagesService.getKey("registration.is.not.active"));
			this.btnOut.setDisabled(true);
			this.btnLock.setDisabled(true);
		}
		
	}
	
	
	public void calculateSettlement() throws VONEAppException, InterruptedException
	{
//		this.totalAmount
//		this.cashPay
//		this.creditPay
//		this.depositPaid
//		this.amountOfReturn
//		this.amountOfDeposit
		if(isPembayaran){
			if(depositPaid.getValue() == null)depositPaid.setValue(new BigDecimal(0)); 
			if(depositPaid.getValue().doubleValue() > amountOfDeposit.getValue().doubleValue()){
				Messagebox.show(MessagesService.getKey("jumlah.pembayaran.deposit.melebihi.deposit"));
				depositPaid.setValue(new BigDecimal(0));
			}
			
			if(totalAmount.getValue() == null)return;
			
			if(cashPay.getValue() == null)cashPay.setValue(new BigDecimal(0));
			
			double returnValue = totalAmount.getValue().doubleValue() - (cashPay.getValue().doubleValue() +
					             creditPay.getValue().doubleValue() + depositPaid.getValue().doubleValue());
			amountOfReturn.setValue(new BigDecimal(returnValue));
		}
		
		
		
		
	}
	
	
	public void getTransactionType() throws VONEAppException,InterruptedException{
		
		if(this.transactionType.getSelectedItem().getLabel().equals("PELUNASAN"))
		{
			isPembayaran = true;
			this.btnPay.setLabel("BAYAR");
			this.depositPaid.setDisabled(false);
			setManipulatedFiledVisible(true);
			this.transactionNumber.setDisabled(false);
			MRNumber.focus();
		}
		else if(this.transactionType.getSelectedItem().getLabel().equals("DEPOSIT"))
		{
			isPembayaran = false;
			this.btnPay.setLabel("DEPOSIT");
			setManipulatedFiledVisible(false);
			this.creditPay.setVisible(true);
			this.cashPay.setVisible(true);
			this.amountOfDeposit.setVisible(true);
			this.transactionNumber.setDisabled(true);
			MRNumber.focus();
		}
		else
		{
			isPembayaran = false;
			this.btnPay.setLabel("RETUR");
			setManipulatedFiledVisible(false);
			this.cashPay.setVisible(true);
			this.amountOfDeposit.setVisible(true);
			this.btnOut.setDisabled(true);
			this.btnLock.setDisabled(true);
			MRNumber.focus();
		}
			
	}
	
	public void cetakKwitansi() throws VONEAppException, InterruptedException, IOException{

		
//		double total = this.discountTotal.getValue().doubleValue() + this.totalAmount.getValue().doubleValue();
		double total = this.total.getValue().doubleValue();
		double nilaiPajak = this.totalAmount.getValue().doubleValue() - 
						(this.total.getValue().doubleValue() - this.returnTtl.getValue().doubleValue() - discountTotal.getValue().doubleValue());
		if(nilaiPajak < 0){
			this.returnTtl.setValue(new BigDecimal(-nilaiPajak));
			nilaiPajak = 0;
		} 
		
		NoteReport np = new NoteReport(this.noteCollection,this.nomorKwitansi,tanggalKwitansi,total,
				this.discountTotal.getValue().doubleValue(), this.ppn.getValue().doubleValue(), nilaiPajak, 
				this.totalAmount.getValue().doubleValue(),this.cashPay.getValue().doubleValue(), 
				this.creditPay.getValue().doubleValue(),this.depositPaid.getValue().doubleValue(),
				this.returnTtl.getValue().doubleValue());
		
		if(!np.printOut(win.getDesktop().getSession().getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU..!!");
		}
		
	}
	
	public void cetak() throws VONEAppException, InterruptedException, IOException{

		
//		double total = this.discountTotal.getValue().doubleValue() + this.totalAmount.getValue().doubleValue();
		double total = this.total.getValue().doubleValue();
		
		double nilaiPajak = this.totalAmount.getValue().doubleValue() - 
						(this.total.getValue().doubleValue() - this.returnTtl.getValue().doubleValue() - discountTotal.getValue().doubleValue());
		if(nilaiPajak < 0){
			this.returnTtl.setValue(new BigDecimal(-nilaiPajak));
			nilaiPajak = 0;
		} 
		
		
		NoteReport np = new NoteReport(this.noteCollection,this.nomorKwitansi,tanggalKwitansi,total,
				this.discountTotal.getValue().doubleValue(), this.ppn.getValue().doubleValue(), nilaiPajak, 
				this.totalAmount.getValue().doubleValue(),this.cashPay.getValue().doubleValue(), 
				this.creditPay.getValue().doubleValue(),this.depositPaid.getValue().doubleValue(),
				this.returnTtl.getValue().doubleValue());
		
		if(!np.printOut(win.getDesktop().getSession().getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU..!!");
		}
		
	}

}

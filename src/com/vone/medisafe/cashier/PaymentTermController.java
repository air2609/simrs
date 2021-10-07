package com.vone.medisafe.cashier;



import java.math.BigDecimal;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.BankController;
import com.vone.medisafe.ui.master.CreditCardTypeController;
import com.vone.medisafe.ui.master.InsuranceController;

public class PaymentTermController extends BaseController{
	
	Listbox typeOfPayList;
	Listbox nameOfBank;
	Listbox typeOfCard;
	Textbox accountNumber;
	Decimalbox amount;
	Listbox insuranceNameList;
	Decimalbox insuranceAmount;
	Listbox cashierLists;
	
	//window utama
	Window winu;
	Decimalbox creditPay;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();
	
	double paymentValue = 0;

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		typeOfPayList = (Listbox)win.getFellow("typeOfPayList");
		nameOfBank = (Listbox)win.getFellow("nameOfBank");
		typeOfCard = (Listbox)win.getFellow("typeOfCard");
		accountNumber = (Textbox)win.getFellow("accountNumber");
		amount = (Decimalbox)win.getFellow("amount");
		insuranceNameList = (Listbox)win.getFellow("insuranceNameList");
		insuranceAmount = (Decimalbox)win.getFellow("insuranceAmount");
		cashierLists = (Listbox)win.getFellow("cashierLists");
		Page page = win.getDesktop().getPage(ScreenConstant.TRANSAKSI_KASIR);
		winu = (Window)page.getFellow("CashierTransaction");
		creditPay = (Decimalbox)winu.getFellow("creditPay");
		this.cashierLists.getItems().clear();
		this.typeOfCard.getItems().clear();
		BankController.getBanks(nameOfBank);
		InsuranceController.getInsuranceForSelect(insuranceNameList);
		clear();
	}
	
	public void clear() throws VONEAppException{
		typeOfPayList.setSelectedIndex(0);
		nameOfBank.setSelectedIndex(0);
		typeOfCard.getItems().clear();
		accountNumber.setValue(null);
		amount.setValue(null);
		insuranceAmount.setValue(null);
		insuranceNameList.setSelectedIndex(0);
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(typeOfCard);
		
		typeOfCard.setSelectedIndex(0);
	}
	
	
	public void getCreditCardType() throws VONEAppException, InterruptedException{
		if(!nameOfBank.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
			if(typeOfPayList.getSelectedItem().getLabel().equals("")){
				Messagebox.show(MessagesService.getKey("card.type.not.selected"));
				nameOfBank.setSelectedIndex(0);
				return;
			}
			else if(typeOfPayList.getSelectedItem().getLabel().equals("KARTU KREDIT")){
				CreditCardTypeController.getCardTypesForSelect(typeOfCard, (MsBank)nameOfBank.getSelectedItem().getValue(), 
						MedisafeConstants.KARTU_KREDIT);
			}else{
				CreditCardTypeController.getCardTypesForSelect(typeOfCard, (MsBank)nameOfBank.getSelectedItem().getValue(), 
						MedisafeConstants.KARTU_DEBET);
			}
		}
		
	}
	
	
	public void addPaymentTerm() throws VONEAppException, InterruptedException{
		
		
		if(!typeOfCard.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
			String pesan = "";
			boolean valid = true;
			if(this.accountNumber.getText().trim().equals("")){
				pesan = pesan +"No Account,";
				valid = false;
//				Messagebox.show(MessagesService.getKey("account.empty.not.allowed"));
//				return;
			}
			if(this.amount.getValue() == null){
				pesan = pesan +"Nilai Pembayaran,";
				valid = false;
//				Messagebox.show(MessagesService.getKey("amount.is.empty"));
//				return;
			}
			if(!valid){
				Messagebox.show(pesan.substring(0, pesan.length()-1) + " Tidak Boleh Kosong!");
				return;
			}
			Object[] obj = new Object[]{typeOfCard.getSelectedItem().getValue(), 
					                      amount.getValue().doubleValue(), accountNumber.getText()};
			item = new Listitem();
			item.setValue(obj);
			item.setAttribute("nilai", amount.getValue().doubleValue());
			item.setParent(this.cashierLists);
			
			cell = new Listcell(typeOfPayList.getSelectedItem().getLabel());
			cell.setParent(item);
			
			cell = new Listcell(typeOfCard.getSelectedItem().getLabel()+";"+nameOfBank.getSelectedItem().getLabel()+";"+
					              accountNumber.getText());
			cell.setParent(item);
			
			cell = new Listcell(this.amount.getText());
			cell.setParent(item);
			
//			paymentValue = paymentValue + this.amount.getValue().doubleValue();
//			amount.setValue(null);
		}
		
		if(!insuranceNameList.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
			if(this.insuranceAmount.getValue() == null){
				Messagebox.show(MessagesService.getKey("insurance.amount.is.empty"));
				return;
			}
			
			Object[] obj = new Object[]{insuranceNameList.getSelectedItem().getValue(), insuranceAmount.getValue().doubleValue()};
			item = new Listitem();
			item.setValue(obj);
			item.setAttribute("nilai", insuranceAmount.getValue().doubleValue());
			item.setParent(this.cashierLists);
			
			cell = new Listcell("ASURANSI");
			cell.setParent(item);
			
			cell = new Listcell(insuranceNameList.getSelectedItem().getLabel());
			cell.setParent(item);
			
			cell = new Listcell(this.insuranceAmount.getText());
			cell.setParent(item);
			
//			paymentValue = paymentValue + this.insuranceAmount.getValue().doubleValue();
//			insuranceAmount.setValue(null);
		}
		
		Listcell cell = null;
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		List<Listitem> list = cashierLists.getItems();
		for(Listitem item : list){
			cell = (Listcell)item.getChildren().get(2);
//			creditPay.setText(cell.getLabel());
			db.setText(cell.getLabel());
			paymentValue = paymentValue + db.getValue().doubleValue();
		}
		MiscTrxController.setFont(cashierLists);
		creditPay.setValue(new BigDecimal(paymentValue));
		paymentValue = 0;
		
		clear();
		
	}
	
	
	public void deletePaymentTerm() throws VONEAppException, InterruptedException{
		if(cashierLists.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("paymentterm.not.selected"));
			return;
		}
		int index = cashierLists.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			Listitem item = cashierLists.getItemAtIndex(index);
			double nilai = (Double)item.getAttribute("nilai");
			paymentValue = paymentValue - nilai;
			creditPay.setValue(new BigDecimal(paymentValue));
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			cashierLists.removeItemAt(index);
		
		}
	}
	
}

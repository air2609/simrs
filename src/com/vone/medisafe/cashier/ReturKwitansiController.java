package com.vone.medisafe.cashier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class ReturKwitansiController extends BaseController{
	
	Bandbox MRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Bandbox noteNumber;
	Textbox searchNote;
	Textbox searchName;
	Listbox kwitansiList;
	Textbox patientName;
	Textbox addr;
	Label labelStatus;
	Label statusNota;
	Listbox billList;
	Decimalbox ppn;
	Decimalbox total;
	Listbox patientList;
	
	Button btnSave;
//	Button btnModify;
	Button btnNew;
	Button btnCetak;
//	Button btnEnd;
	
	
	Listitem item;
	Listcell cell;
	
	private CashierManager serv = Service.getCashierManager();
	public TbPatientBill bill;
	private MsUser user;
	
	ZulConstraint constraints = new ZulConstraint();
		

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		patientList = (Listbox)win.getFellow("patientList");
		noteNumber = (Bandbox)win.getFellow("noteNumber");
		searchNote = (Textbox)win.getFellow("searchNote");
		searchName = (Textbox)win.getFellow("searchName");
		kwitansiList = (Listbox)win.getFellow("kwitansiList");
		patientName = (Textbox)win.getFellow("patientName");
		addr = (Textbox)win.getFellow("addr");
		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		billList = (Listbox)win.getFellow("billList");
		ppn = (Decimalbox)win.getFellow("ppn");
		total = (Decimalbox)win.getFellow("total");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnNew = (Button)win.getFellow("btnNew");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");
		
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchNote, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
		
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		
		setButtonDisable(true);
		btnNew.setDisabled(false);
//		btnEnd.setDisabled(false);
		
		user = getUserInfoBean().getMsUser();
		
		
	}
	
	public void setButtonDisable(boolean isDisable){
		
		btnSave.setDisabled(isDisable);
		btnNew.setDisabled(isDisable);
		btnCetak.setDisabled(isDisable);
//		btnEnd.setDisabled(isDisable);
		
	}
	
	
	public void searchBill() throws VONEAppException, InterruptedException{
//		this.searchNote
//		this.searchName
		
//		this.noteList
		boolean isRetur = false;
		
		this.kwitansiList.getItems().clear();
		
			
		List<TbPatientBill> bills = serv.getPatientBills("%"+searchName.getText()+"%", "%"+searchNote.getText()+"%",
				                    MedisafeConstants.BELUM_LUNAS, isRetur);
		for(TbPatientBill bill : bills){
			item = new Listitem();
			item.setParent(kwitansiList);
			item.setValue(bill);
			
			cell = new Listcell(bill.getVPbillCode());
			cell.setParent(item);
			
			cell = new Listcell(bill.getVNameOnBill());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.getNoteStatus(bill.getNPbillStatus()));
			cell.setParent(item);
		}
	}
	
	
	
	public void getRegistration(int type) throws VONEAppException,InterruptedException{
		
		serv.getReturRegistration(this,type);
		
				

	}
	
	
	public void getNotes() throws VONEAppException{
		
		serv.getBillNote(this);
		
		
	}
	
	
	public void clear() throws VONEAppException{
		
		MRNumber.setValue(null);
		crNoMR.setValue(null);
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		patientList.getItems().clear();
		noteNumber.setValue(null);
		searchNote.setValue(null);
		searchName.setValue(null);
		kwitansiList.getItems().clear();
		patientName.setValue(null);
		addr.setValue(null);
		billList.getItems().clear();
		ppn.setValue(null);
		total.setValue(null);
		
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		
	}
	
	
	
	public void save() throws VONEAppException, InterruptedException{
		bill.setNPbillStatus((short)MedisafeConstants.CANCEL_NOTE);
		bill.setVWhoChange(user.getVUserName());
		bill.setDWhnChange(new Date());
		
		if(serv.cancelBill(bill)){
			Messagebox.show(MessagesService.getKey("bill.cancelation.success"));
			labelStatus.setVisible(true);
			statusNota.setVisible(true);
			
			statusNota.setValue(MedisafeUtil.getNoteStatus(bill.getNPbillStatus()));
		}else Messagebox.show(MessagesService.getKey("bill.cancelation.fail"));
	}

}

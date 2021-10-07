package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.BankManager;
import com.vone.medisafe.ui.base.BaseController;

public class BankController extends BaseController{
	
	Logger logger = Logger.getLogger(BankController.class);
	
	ZulConstraint cst = new ZulConstraint();	
	
	public Textbox bankName = null;
	public Textbox bankAddress = null;
	public Textbox acctNo = null;
	public Bandbox coa = null;
	public Textbox telpNo = null;
	public Textbox altTelpNo = null;
	public Listbox list = null;
	
	BankManager bankManager = MasterServiceLocator.getBankManager();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		this.bankName.setText("");
		this.bankAddress.setText("");
		this.acctNo.setText("");
		this.coa.setValue(null);
		this.coa.removeAttribute("coa");
		this.telpNo.setText("");
		this.altTelpNo.setText("");
		
		this.bankName.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		super.doDelete(cmp);
				
		bankManager.delete((MsBank)list.getSelectedItem().getValue());
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		super.doModify(cmp);
		
		BankManager bankService = MasterServiceLocator.getBankManager();
		bankService.prepareModify(this);
		
		
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!cst.validateComponent(true))
			return;
		
		super.doSave(cmp);
				
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		
		MsBank bankPojo = new MsBank();
		bankPojo.setVBankName(this.bankName.getText());
		bankPojo.setVBankAccNo(this.acctNo.getText());
		bankPojo.setVBankAddr(this.bankAddress.getText());
		bankPojo.setVBankContactNo(this.telpNo.getText());
		bankPojo.setVBank2ndCtcNo(this.altTelpNo.getText());
		
		
		
		bankPojo.setMsCoa((MsCoa)this.coa.getAttribute("coa"));
		
		bankPojo.setVWhoCreate(super.getUserInfoBean().getStUserId());
		bankPojo.setDWhnCreate(new Date());
		
		bankManager.save(bankPojo);
		
		redrawList();		
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		
		doCancel(cmp);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		super.doSaveModify(cmp);
		
		list.setDisabled(false);	
		
		MsBank bankPojo = (MsBank)list.getSelectedItem().getValue();
		bankPojo.setVBankName(this.bankName.getText());
		bankPojo.setVBankAccNo(this.acctNo.getText());
		bankPojo.setVBankAddr(this.bankAddress.getText());
		bankPojo.setVBankContactNo(this.telpNo.getText());
		bankPojo.setVBank2ndCtcNo(this.altTelpNo.getText());
		bankPojo.setMsCoa((MsCoa)this.coa.getAttribute("coa"));		
		
		bankPojo.setVWhoChange(super.getUserInfoBean().getStUserId());
		bankPojo.setDWhnChange(new Date());
		
		bankManager.update(bankPojo);

		Messagebox.show(MessagesService.getKey("common.modify.success"));		
		
		redrawList();
		
		doCancel(cmp);			
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);		
		
		bankName = (Textbox)cmp.getFellow("bankName");
		bankAddress = (Textbox)cmp.getFellow("bankAddress");
		coa = (Bandbox)cmp.getFellow("coa");
		acctNo = (Textbox)cmp.getFellow("acctNo");
		telpNo = (Textbox)cmp.getFellow("telpNo");
		altTelpNo = (Textbox)cmp.getFellow("altTelpNo");	
		
//		CoaController.getCoaForSelect(coa, MedisafeConstants.COA_ALL);
		
		list = (Listbox)cmp.getFellow("list");
		
		cst.registerComponent(bankName);
		cst.registerComponent(acctNo);
		cst.registerComponent(coa);
		cst.registerComponent(bankName, ZulConstraint.UPPER_CASE);
		cst.registerComponent(bankAddress, ZulConstraint.UPPER_CASE);
		cst.registerComponent(acctNo, ZulConstraint.UPPER_CASE);
		cst.registerComponent(telpNo, ZulConstraint.UPPER_CASE);
		cst.registerComponent(altTelpNo, ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);
		
		redrawList();
	}
	
	private void redrawList() throws VONEAppException{
		
		bankManager.redrawList(list);
		MiscTrxController.setFont(list);
//		list.getItems().clear();
//				
//		List listBank = bankManager.findByExample(new MsBank());
//		
//		Iterator it = listBank.iterator();
//		
//		while (it.hasNext()){
//			Listitem item = new Listitem();
//			
//			MsBank bankPojo = (MsBank)it.next();
//			
//			item.appendChild(new Listcell(bankPojo.getVBankName()));			
//			item.appendChild(new Listcell(bankPojo.getVBankAccNo()));			
//			
//			//set COA cell
//			Listcell cellCoa = new Listcell(bankPojo.getMsCoa().getVAcctNo());
//			cellCoa.setValue(bankPojo.getMsCoa());			
//			item.appendChild(cellCoa);
//			
//			item.appendChild(new Listcell(bankPojo.getVBankContactNo()));
//			
//			item.setValue(bankPojo);
//			
//			item.setParent(list);
//		}
	}
	
	/**
	 * @author Arifullah Ibn Rusyd
	 * @param bankList
	 * @throws VONEAppException
	 */
	public static void getBanks(Listbox bankList) throws VONEAppException{
		bankList.getItems().clear();
		MsBank msBank = null;
		
		BankManager service = MasterServiceLocator.getBankManager();
		
		Listitem item = new Listitem();
		item.setParent(bankList);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		bankList.setSelectedIndex(0);
		
		List banks = service.findByExample(new MsBank());
		
		Iterator itr = banks.iterator();
		while(itr.hasNext()){
			msBank = (MsBank)itr.next();
			item = new Listitem();
			item.setValue(msBank);
			item.setLabel(msBank.getVBankName());
			item.setParent(bankList);
			
		}
	}

}

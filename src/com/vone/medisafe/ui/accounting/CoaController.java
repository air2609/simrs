package com.vone.medisafe.ui.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

//import com.sun.mail.imap.protocol.MessageSet;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsCoaType;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class CoaController extends BaseController{

	static Logger logger = Logger.getLogger(CoaController.class);
	
	Tree tree = null;
	Listbox status = null;
	//Add
	Radiogroup radiogrp = null;
	//Fill
	Textbox accType = null;
	Textbox accNo = null;
	Textbox accName = null;
	Checkbox accStatus = null;
	Decimalbox accBalance = null;
	Listbox accSubof = null;
	
	//Attribute - Radio
	final String RADIO_VALUE = "RADIO_VALUE";
	
	ZulConstraint cst = new ZulConstraint();
	
	
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		tree = (Tree)cmp.getFellow("tree");
		status = (Listbox)cmp.getFellow("status");
		
		status.setSelectedIndex(0);
		
		this.status.addEventListener("onSelect", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redraw();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (VONEAppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		//PUT INTO SESSION
		super.getCurrentSession().setAttribute(MedisafeConstants.COA_CMP_SESSION, cmp);
		
		redraw();
	}
	
	public void initAdd(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		radiogrp = (Radiogroup)cmp.getFellow("radio");				
		
		Iterator it = MasterServiceLocator.getCoaManager().getCoaType().iterator();
						
		while (it.hasNext()){
			
			MsCoaType msCT = (MsCoaType)it.next();
			
			Radio radio = new Radio();
			radio.setLabel(msCT.getVCtName());
			radio.setValue(""+msCT.getNCtId().intValue());
			radio.setStyle("font-weight:bold;font-size:8pt");
			radio.setAttribute(this.RADIO_VALUE, msCT);
			
			radio.setParent(radiogrp);
		}
		
		if (radiogrp.getChildren().size() > 0)			
			radiogrp.setSelectedIndex(0);
		
		super.getCurrentSession().setAttribute(MedisafeConstants.COA_RADIOBOX_SESSION, radiogrp);
	}
	
	public void initFill(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		radiogrp = (Radiogroup)super.getCurrentSession().getAttribute(MedisafeConstants.COA_RADIOBOX_SESSION);
			
		accType = (Textbox)cmp.getFellow("accType");
		accNo = (Textbox)cmp.getFellow("accNo");
		accName = (Textbox)cmp.getFellow("accName");
		accStatus = (Checkbox)cmp.getFellow("accStatus");
		accBalance = (Decimalbox)cmp.getFellow("accBalance");
		accSubof = (Listbox)cmp.getFellow("accSubof");
		
		cst.registerComponent(accNo, ZulConstraint.NO_EMPTY);
		cst.registerComponent(accName, ZulConstraint.NO_EMPTY);
		cst.registerComponent(accBalance, ZulConstraint.NO_EMPTY);
		cst.registerComponent(accNo, ZulConstraint.UPPER_CASE);
		cst.registerComponent(accName, ZulConstraint.UPPER_CASE);
		cst.validateComponent(false);
		
		fillCoaType(accSubof);
		
		accBalance.setValue(new BigDecimal(0));		
		accType.setValue(((MsCoaType)radiogrp.getSelectedItem().getAttribute(this.RADIO_VALUE)).getVCtName());
		accStatus.setChecked(true);
		accNo.focus();				
	}
	
	public void initFillMod(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		Component cmpParent = (Component)super.getCurrentSession().getAttribute(MedisafeConstants.COA_CMP_SESSION);
		tree = (Tree)cmpParent.getFellow("tree");
		
		if (tree.getSelectedItem() == null)
			return;
		
		accType = (Textbox)cmp.getFellow("accType");
		accNo = (Textbox)cmp.getFellow("accNo");
		accName = (Textbox)cmp.getFellow("accName");
		accStatus = (Checkbox)cmp.getFellow("accStatus");
		accBalance = (Decimalbox)cmp.getFellow("accBalance");
		accSubof = (Listbox)cmp.getFellow("accSubof");
		
		cst.registerComponent(accNo, ZulConstraint.NO_EMPTY);
		cst.registerComponent(accName, ZulConstraint.NO_EMPTY);
		cst.registerComponent(accBalance, ZulConstraint.NO_EMPTY);
		cst.registerComponent(accNo, ZulConstraint.UPPER_CASE);
		cst.registerComponent(accName, ZulConstraint.UPPER_CASE);
		cst.validateComponent(false);
		
		MsCoa coaPojo = (MsCoa)tree.getSelectedItem().getValue();
		
		accType.setText(coaPojo.getMsCoaType().getVCtName());
		accNo.setText(coaPojo.getVAcctNo());
		accName.setText(coaPojo.getVAcctName());
		
		if (coaPojo.getNStatus() != null && coaPojo.getNStatus() == MedisafeConstants.COA_ACTIVE)
			accStatus.setChecked(true);
		else
			accStatus.setChecked(false);
		
		if (coaPojo.getNBalance() != null)
			accBalance.setValue(new BigDecimal(coaPojo.getNBalance()));
		else
			accBalance.setValue(new BigDecimal(0));
		
		fillCoaType(accSubof, coaPojo.getMsCoaType().getNCtId());
		
		if (coaPojo.getMsCoa() != null){
			Iterator it = accSubof.getItems().iterator();
			
			while (it.hasNext()){
				Listitem item = (Listitem)it.next();
				if (!(item.getValue() instanceof MsCoa)) 
					continue;
				
				MsCoa coa = (MsCoa)item.getValue();
				
				if (coaPojo.getMsCoa().getNCoaId().intValue() == coa.getNCoaId().intValue()){
					accSubof.setSelectedItem(item);
					break;
				}
			}
		}
					
		accNo.focus();		
	}
		
	public void createFill() throws InterruptedException, VONEAppException {
		
		Window win = (Window)Executions.createComponents("/zkpages/accounting/coa/coaFill.zul", null, null);
		win.doModal();
	}

	public void createFillModify() throws InterruptedException, VONEAppException {

		if (tree.getSelectedItem() == null){			
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));		
			return;
		}	
				
		Window win = (Window)Executions.createComponents("/zkpages/accounting/coa/coaFillMod.zul", null, null);
		win.doModal();
	}
	
	private void fillCoaType (Listbox listbox) throws VONEAppException {
		
		if (listbox == null) listbox = new Listbox();
		
		listbox.getItems().clear();
		
		//set empty list
		Listitem item = new Listitem();
		item.setParent(listbox);
		item.appendChild(new Listcell(MedisafeConstants.LABELKOSONG));
		item.setValue(MedisafeConstants.LISTKOSONG);
		
		if (radiogrp == null || radiogrp.getSelectedItem() == null) return;
		
		Iterator it = MasterServiceLocator.getCoaManager().getCoaBaseOnTypeNoChild(
				new Integer(radiogrp.getSelectedItem().getValue()).intValue()).iterator();
		
		while (it.hasNext()){
			MsCoa coa = (MsCoa)it.next();
			
			item = new Listitem();
			item.setParent(listbox);
			item.setValue(coa);
			
			item.appendChild(new Listcell(coa.getVAcctNo() +"-"+ coa.getVAcctName()));
		}
		
		listbox.setSelectedIndex(0);
	}
	
	private void fillCoaType (Listbox listbox, int type) throws VONEAppException {
		
		if (listbox == null) listbox = new Listbox();
		
		listbox.getItems().clear();
		
		//set empty list
		Listitem item = new Listitem();
		item.setParent(listbox);
		item.appendChild(new Listcell(MedisafeConstants.LABELKOSONG));
		item.setValue(MedisafeConstants.LISTKOSONG);
		
		Iterator it = MasterServiceLocator.getCoaManager().getCoaBaseOnTypeNoChild(type).iterator();
		
		while (it.hasNext()){
			MsCoa coa = (MsCoa)it.next();
			
			item = new Listitem();
			item.setParent(listbox);
			item.setValue(coa);
			
			item.appendChild(new Listcell(coa.getVAcctNo() +"-"+ coa.getVAcctName()));
		}
		
		listbox.setSelectedIndex(0);
	}
	
	private void redraw() throws InterruptedException, VONEAppException{
			
		MasterServiceLocator.getCoaManager().redrawCoaController(this.tree, this.status);
		MiscTrxController.setFont(tree);
	}
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		
		if (tree.getSelectedItem() == null){			
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			
			return;
		}	
		
		int respond = 0;
				
		respond = Messagebox.show(MessagesService.getKey("master.coa.delete"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;
		
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		
		try {
			MasterServiceLocator.getCoaManager().delete((MsCoa)tree.getSelectedItem().getValue());
			
			redraw();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			logger.error("SAVE ERROR",e);
			Messagebox.show(MessagesService.getKey("master.coa.delete.fail"));
		}				
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!cst.validateComponent(true))
			return;
		
		super.doSave(cmp);
		
		MsCoa coa = new MsCoa();		
		coa.setMsCoaType((MsCoaType)radiogrp.getSelectedItem().getAttribute(this.RADIO_VALUE));

		coa.setVAcctNo(this.accNo.getText());
		coa.setVAcctName(this.accName.getText());		
		coa.setNBalance(this.accBalance.getValue().floatValue());
		
		//STATUS
		if (this.accStatus.isChecked())
			coa.setNStatus(MedisafeConstants.COA_ACTIVE);
		else
			coa.setNStatus(MedisafeConstants.COA_INACTIVE);
				
		//set subacct
		if (!MedisafeConstants.LISTKOSONG.equals(this.accSubof.getSelectedItem().getValue()))			
			coa.setMsCoa((MsCoa)this.accSubof.getSelectedItem().getValue());
		
		coa.setVWhoCreate((super.getUserInfoBean().getStUserId()));
		coa.setDWhnCreate(new Date());
		
		MasterServiceLocator.getCoaManager().save(coa);
		
		//redraw tree
		init((Component)super.getCurrentSession().getAttribute(MedisafeConstants.COA_CMP_SESSION));
		
		redraw();
		
		this.accNo.setText("");
		this.accName.setText("");
		this.accBalance.setValue(new BigDecimal(0));
		this.accStatus.setChecked(true);
		if (this.accSubof.getSelectedCount() > 0)
			this.accSubof.setSelectedIndex(0);		
		
		Messagebox.show(MessagesService.getKey("common.save.success"));
		
		this.accNo.focus();
	}

	/**
	 * pass COA Listbox and type.. this method would fill the listbox automatically 
	 * @param coaList
	 * @param type
	 * @throws VONEAppException 
	 */
	public static void getCoaForSelect(Listbox coaList, int type) throws VONEAppException{
		coaList.getItems().clear();
		
		MsCoa coa = null;
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(coaList);
		
		List list = MasterServiceLocator.getCoaManager().getCoaBaseOnType(type);
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			coa = (MsCoa)it.next();
			item = new Listitem();
			item.setParent(coaList);
			item.setValue(coa);
			item.setLabel(coa.getVAcctNo()+"-"+coa.getVAcctName());
		}
		if(coaList.getItems().size()>0)coaList.setSelectedIndex(0);
	}

	@Override
	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
		
		if (tree == null) return;
		
		if (!cst.validateComponent(true))
			return;
		
		MsCoa coaPojo = (MsCoa)tree.getSelectedItem().getValue();
		coaPojo.setVAcctNo(this.accNo.getText());
		coaPojo.setVAcctName(this.accName.getText());
		
		if (this.accStatus.isChecked())
			coaPojo.setNStatus(MedisafeConstants.COA_ACTIVE);
		else
			coaPojo.setNStatus(MedisafeConstants.COA_INACTIVE);
		
		coaPojo.setNBalance(this.accBalance.getValue().floatValue());
		
		if (this.accSubof.getSelectedItem() != null && this.accSubof.getSelectedItem().getValue() instanceof MsCoa)
			coaPojo.setMsCoa((MsCoa)this.accSubof.getSelectedItem().getValue());
		else
			coaPojo.setMsCoa(null);
		
		
		//CHECK FOR SELECTING ITSELF AS SUPERIOR ACCOUNT
		if (coaPojo.getMsCoa() != null){
			if (coaPojo.getNCoaId().intValue() == coaPojo.getMsCoa().getNCoaId().intValue()){
				
				Messagebox.show(MessagesService.getKey("master.coa.select.invalid"));
				
				return;
			}
		}
		
		MasterServiceLocator.getCoaManager().update(coaPojo);
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		status = (Listbox)tree.getFellow("status");
				
		redraw();
		
		cmp.getRoot().detach();
	}
	
	
	
	public static void searchCoa(Textbox codeCoa, Textbox coaName, Listbox coaList) throws VONEAppException{
		
		codeCoa.setValue(codeCoa.getText().toUpperCase());
		coaName.setValue(coaName.getText().toUpperCase());
		
		MasterServiceLocator.getCoaManager().searchCoa("%"+codeCoa.getText()+"%", "%"+coaName.getText()+"%",
				coaList);
		MiscTrxController.setFont(coaList);
	}
	
	
	
	public static void getCoa(Listbox coaList, Bandbox coa){
		
		MsCoa msCoa = (MsCoa) coaList.getSelectedItem().getValue();
		coa.setText(msCoa.getVAcctNo() + " - "+ msCoa.getVAcctName());
		coa.setAttribute("coa", msCoa);
		
	}
	
	
	
	public static void checkCoa(Bandbox coa) throws VONEAppException, InterruptedException{
		
		MsCoa msCoa = MasterServiceLocator.getCoaManager().checkCoa(coa.getText());
		if(msCoa == null){
			Messagebox.show(MessagesService.getKey("coa.not.exist"));
			coa.focus();
		}
		else{
			coa.setText(msCoa.getVAcctNo()+"-"+msCoa.getVAcctName());
			coa.setAttribute("coa", msCoa);
		}
	}
}

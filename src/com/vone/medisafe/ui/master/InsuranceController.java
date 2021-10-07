package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsInsurance;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.InsuranceManager;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;

public class InsuranceController extends BaseController{
	
	Logger logger = Logger.getLogger(InsuranceController.class);
	
	ZulConstraint cst = new ZulConstraint();	
	
	public Textbox insuranceName = null;
	public Textbox insuranceAddress = null;
	public Textbox desc = null;
	public Bandbox coa = null;
	public Textbox telpNo = null;
	public Checkbox activeStatus = null;
	public Datebox endOfContract = null;
	public Listbox list = null;
	
	InsuranceManager insuranceManager = MasterServiceLocator.getInsuranceManager();
	

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);	
		
		this.insuranceName.setText("");
		this.insuranceAddress.setText("");
		this.desc.setText("");
		coa.setValue(null);
		coa.removeAttribute("coa");
		this.telpNo.setText("");
		activeStatus.setChecked(false);
		endOfContract.setText("");
		
		this.insuranceName.focus();
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
				
		insuranceManager.delete((MsInsurance)list.getSelectedItem().getValue());
		
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
		
		InsuranceManager service = MasterServiceLocator.getInsuranceManager();
		service.prepareModify(this);
		
		
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
		
		MsInsurance insPojo = new MsInsurance();
		insPojo.setVInsuranceName(this.insuranceName.getText());
		insPojo.setVInsuranceAddr(this.insuranceAddress.getText());
		insPojo.setVInsurancePhNo(this.telpNo.getText());
		insPojo.setVInsuranceDesc(this.desc.getText());
		insPojo.setDDateEndOfPartnership(this.endOfContract.getValue());
		insPojo.setMsCoa((MsCoa)this.coa.getAttribute("coa"));
		insPojo.setNActiveStatus((this.activeStatus.isChecked())?MedisafeConstants.INSURANCE_ACTIVE:MedisafeConstants.INSURANCE_INACTIVE);
		
		insPojo.setVWhoCreate(super.getUserInfoBean().getStUserId());
		insPojo.setDWhnCreate(new Date());
		
		insuranceManager.save(insPojo);
		
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
		
		MsInsurance insPojo = (MsInsurance)list.getSelectedItem().getValue();
		
		insPojo.setVInsuranceName(this.insuranceName.getText());
		insPojo.setVInsuranceAddr(this.insuranceAddress.getText());
		insPojo.setVInsurancePhNo(this.telpNo.getText());
		insPojo.setVInsuranceDesc(this.desc.getText());
		insPojo.setDDateEndOfPartnership(this.endOfContract.getValue());
		insPojo.setMsCoa((MsCoa)this.coa.getAttribute("coa"));
		insPojo.setNActiveStatus((this.activeStatus.isChecked())?MedisafeConstants.INSURANCE_ACTIVE:MedisafeConstants.INSURANCE_INACTIVE);
		
		insPojo.setVWhoChange(super.getUserInfoBean().getStUserId());
		insPojo.setDWhnChange(new Date());
		
		insuranceManager.update(insPojo);

		Messagebox.show(MessagesService.getKey("common.modify.success"));		
		
		redrawList();
		
		doCancel(cmp);			
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);		
		
		insuranceName = (Textbox)cmp.getFellow("insuranceName");
		insuranceAddress = (Textbox)cmp.getFellow("insuranceAddress");
		coa = (Bandbox)cmp.getFellow("coa");
		desc = (Textbox)cmp.getFellow("desc");
		telpNo = (Textbox)cmp.getFellow("telpNo");
		activeStatus = (Checkbox)cmp.getFellow("activeStatus");
		endOfContract = (Datebox)cmp.getFellow("endOfContract");
		
//		CoaController.getCoaForSelect(coa, MedisafeConstants.COA_ALL);
		
		list = (Listbox)cmp.getFellow("list");
		
		cst.registerComponent(insuranceName);
		cst.registerComponent(coa);
		cst.registerComponent(insuranceName, ZulConstraint.UPPER_CASE);
		cst.registerComponent(insuranceAddress, ZulConstraint.UPPER_CASE);
		cst.registerComponent(desc, ZulConstraint.UPPER_CASE);
		cst.registerComponent(telpNo, ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);
		
		activeStatus.setChecked(true);
		
		insuranceName.focus();
		
		redrawList();
	}
	
	private void redrawList() throws VONEAppException{
		
		insuranceManager.redrawList(list);
		MiscTrxController.setFont(list);
//		list.getItems().clear();
//				
//		List listInsurance = insuranceManager.findAll();
//		
//		Iterator it = listInsurance.iterator();
//		
//		while (it.hasNext()){
//			Listitem item = new Listitem();
//			
//			MsInsurance insurancePojo = (MsInsurance)it.next();
//						
//			item.appendChild(new Listcell((insurancePojo.getNActiveStatus() == MedisafeConstants.INSURANCE_ACTIVE)?"ACTIVE":""));
//			item.appendChild(new Listcell(insurancePojo.getVInsuranceName()));			
//			item.appendChild(new Listcell(insurancePojo.getVInsuranceAddr()));			
//			
//			//set COA cell
//			Listcell cellCoa = new Listcell(insurancePojo.getMsCoa().getVAcctNo());
//			cellCoa.setValue(insurancePojo.getMsCoa());			
//			item.appendChild(cellCoa);
//			
//			item.appendChild(new Listcell(insurancePojo.getVInsurancePhNo()));
//			
//			if (insurancePojo.getDDateEndOfPartnership() != null)
//				item.appendChild(new Listcell(MedisafeUtil.convertDateToString(insurancePojo.getDDateEndOfPartnership())));					
//			
//			item.setValue(insurancePojo);
//			
//			item.setParent(list);
//		}
	}
	
	/**
	 * @author Arifullah Ibn Rusyd 
	 * @param insuranceList
	 * @throws VONEAppException
	 */
		
	public static void getInsuranceForSelect(Listbox insuranceList) throws VONEAppException{
		insuranceList.getItems().clear();
		MsInsurance insurance = null;
		
		InsuranceManager service = MasterServiceLocator.getInsuranceManager();
			
		Listitem item = new Listitem();
		item.setParent(insuranceList);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		insuranceList.setSelectedIndex(0);
			
		List list = service.findAll();
			
		Iterator itr = list.iterator();
		while(itr.hasNext()){
			insurance = (MsInsurance)itr.next();
			item = new Listitem();
			item.setValue(insurance);
			item.setLabel(insurance.getVInsuranceName());
			item.setParent(insuranceList);
				
		}
	}	

}

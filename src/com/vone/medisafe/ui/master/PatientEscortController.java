package com.vone.medisafe.ui.master;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.PatientEscortManager;
import com.vone.medisafe.ui.base.BaseController;

public class PatientEscortController extends BaseController{
	
	Logger logger = Logger.getLogger(PatientEscortController.class);
	
	ZulConstraint cst = new ZulConstraint();	
	
	Listbox list = null;
	
	Textbox code = null;
	Textbox type = null;
	Decimalbox commission = null;
	
	static PatientEscortManager patientEscortManager = MasterServiceLocator.getPatientEscortManager();
			

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		this.code.setText("");
		this.type.setText("");
		this.commission.setText("");
		
		this.code.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		patientEscortManager.delete((MsPatientEscort)this.list.getSelectedItem().getValue());
		
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
		
		MsPatientEscort pojo = (MsPatientEscort)this.list.getSelectedItem().getValue();
		
		this.code.setText(pojo.getVEscortCode());
		this.type.setText(pojo.getVEscortType());
		this.commission.setValue(new BigDecimal(pojo.getNEscortCommission()));
			
		this.code.select();
		
		list.setDisabled(true);
			
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
		
		MsPatientEscort pojo = new MsPatientEscort();
		
		pojo.setVEscortCode(this.code.getText());
		pojo.setVEscortType(this.type.getText());
		pojo.setNEscortCommission(this.commission.getValue().doubleValue());
		
		pojo.setVWhoCreate(super.getUserInfoBean().getStUserId());
		pojo.setDWhnCreate(new Date());
		
		patientEscortManager.save(pojo);
		
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
		
		MsPatientEscort pojo = (MsPatientEscort)this.list.getSelectedItem().getValue();
		
		pojo.setVEscortCode(this.code.getText());
		pojo.setVEscortType(this.type.getText());
		pojo.setNEscortCommission(this.commission.getValue().doubleValue());
		
		pojo.setVWhoChange(super.getUserInfoBean().getStUserId());
		pojo.setDWhnChange(new Date());		
		
		patientEscortManager.update(pojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		doCancel(cmp);
		
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);			
		
		list = (Listbox)cmp.getFellow("list");
		code = (Textbox)cmp.getFellow("code");
		type = (Textbox)cmp.getFellow("type");
		commission = (Decimalbox)cmp.getFellow("commission");				
		
		cst.registerComponent(code);
		cst.registerComponent(type);
		cst.registerComponent(commission);
		cst.registerComponent(code,ZulConstraint.UPPER_CASE);
		cst.registerComponent(type,ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);
		
		redrawList();
	}

	private void redrawList() throws VONEAppException{
		
		list.getItems().clear();
				
		List listWarehouse = patientEscortManager.findAll();
		
		Iterator it = listWarehouse.iterator();		
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsPatientEscort pPojo = (MsPatientEscort)it.next();
			
			item.appendChild(new Listcell(pPojo.getVEscortCode()));					
			item.appendChild(new Listcell(pPojo.getVEscortType()));
			item.appendChild(new Listcell(Double.toString(pPojo.getNEscortCommission())));
			
			item.setValue(pPojo);
			
			item.setParent(list);
		}
		MiscTrxController.setFont(list);
	}
	
	/**
	 * Arif nambahin buat select listbox
	 * @param listbox
	 * @throws VONEAppException
	 */
	
	public static void getPatientEscortForSelect(Listbox listbox) throws VONEAppException{
		listbox.getItems().clear();
		MsPatientEscort pe;
		
		List list = patientEscortManager.findAll();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(listbox);
		listbox.setSelectedIndex(0);
			
		Iterator it = list.iterator();
		while(it.hasNext()){
			pe = (MsPatientEscort)it.next();
			item = new Listitem();
			item.setValue(pe);
			item.setLabel(pe.getVEscortType());
			item.setParent(listbox);
			
		}
	}
	
}

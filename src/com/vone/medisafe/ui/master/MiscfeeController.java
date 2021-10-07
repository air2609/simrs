package com.vone.medisafe.ui.master;

import java.math.BigDecimal;

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
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.pojo.MsMiscFee;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;

public class MiscfeeController extends BaseController{
	
	private MsMiscFee mfee;
	
	Listbox pasientType;
	Textbox feeCode;
	Textbox feeName;
	Textbox feeDesc;
	Decimalbox feePrice;
	Listbox coaList;
	Listbox miscFeeList;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		pasientType.setSelectedIndex(0);
		feeCode.setValue(null);
		feeName.setValue(null);
		feeDesc.setValue(null);
		feePrice.setValue(null);
		coaList.setSelectedIndex(0);
		miscFeeList.clearSelection();
		pasientType.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		item = miscFeeList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		mfee = (MsMiscFee)item.getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = miscFeeList.getSelectedIndex();
		
		if(MasterServiceLocator.getMiscFeeManager().delete(mfee)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			miscFeeList.removeItemAt(indexSelected);
					
		}else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
			
		}
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		item = miscFeeList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		mfee = (MsMiscFee)item.getValue();
		
		feeCode.setValue(mfee.getVMfeeCode());
		feeName.setValue(mfee.getVMfeeCode());
		feeDesc.setValue(mfee.getVMfeeDesc());
		feePrice.setValue(new BigDecimal(mfee.getNMfeePrice()));
		
		for(int i=1; i < pasientType.getItems().size(); i++){
			if(mfee.getMsPatientType().getNPatientTypeId().equals(((MsPatientType)pasientType.getItemAtIndex(i).
					getValue()).getNPatientTypeId())){
				pasientType.setSelectedIndex(i);
				break;
			}
		}
		
		for(int i=1; i < coaList.getItems().size(); i++){
			if(mfee.getMsCoa().getNCoaId().equals(((MsCoa)coaList.getItemAtIndex(i).
					getValue()).getNCoaId())){
				coaList.setSelectedIndex(i);
				break;
			}
		}
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		mfee = new MsMiscFee();
		mfee.setVMfeeCode(feeCode.getValue());
		mfee.setVMfeeName(feeName.getValue());
		mfee.setVMfeeDesc(feeDesc.getValue());
		mfee.setNMfeePrice(feePrice.getValue().doubleValue());
		mfee.setMsPatientType((MsPatientType)pasientType.getSelectedItem().getValue());
		mfee.setMsCoa((MsCoa)coaList.getSelectedItem().getValue());
		
		if(MasterServiceLocator.getMiscFeeManager().save(mfee)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setParent(miscFeeList);
			item.setValue(mfee);
			
			cell = new Listcell(mfee.getVMfeeCode());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getVMfeeName());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getVMfeeDesc());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getMsPatientType().getVTpatientDesc());
			cell.setParent(item);
			
			cell = new Listcell(feePrice.getText());
			cell.setParent(item);
						
		}
		else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		
		doCancel(cmp);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
		item = miscFeeList.getSelectedItem();
		mfee = (MsMiscFee)item.getValue();
		mfee.setVMfeeCode(feeCode.getValue());
		mfee.setVMfeeName(feeName.getValue());
		mfee.setVMfeeDesc(feeDesc.getValue());
		mfee.setNMfeePrice(feePrice.getValue().doubleValue());
		mfee.setMsPatientType((MsPatientType)pasientType.getSelectedItem().getValue());
		mfee.setMsCoa((MsCoa)coaList.getSelectedItem().getValue());
		
		if(MasterServiceLocator.getMiscFeeManager().save(mfee)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(mfee);
			
			cell = new Listcell(mfee.getVMfeeCode());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getVMfeeName());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getVMfeeDesc());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getMsPatientType().getVTpatientDesc());
			cell.setParent(item);
			
			cell = new Listcell(feePrice.getText());
			cell.setParent(item);
						
		}
		else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		doCancel(cmp);
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	
	public void init(Component win) throws InterruptedException, VONEAppException {
		pasientType = (Listbox)win.getFellow("pasientType");
		feeCode = (Textbox)win.getFellow("feeCode"); 
		feeName = (Textbox)win.getFellow("feeName");
		feeDesc = (Textbox)win.getFellow("feeDesc");
		feePrice = (Decimalbox)win.getFellow("feePrice");
		coaList = (Listbox)win.getFellow("coaList");
		miscFeeList = (Listbox)win.getFellow("miscFeeList");
		
		super.init(win);
		
		constraints.registerComponent(feeCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(feeCode, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(feeName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(feeName, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(pasientType, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(feeDesc, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(feePrice, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(coaList, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
		PatientTypeController.getPatientTypeForSelect(pasientType);
		CoaController.getCoaForSelect(coaList, MedisafeConstants.COA_ALL);
		getMiscfeeData(miscFeeList);
		
		pasientType.focus();
		
	}
	
	public void getMiscfeeData(Listbox miscFeeList) throws VONEAppException{
		
		MasterServiceLocator.getMiscFeeManager().getMiscFees(miscFeeList);
		MiscTrxController.setFont(miscFeeList);
//		miscFeeList.getItems().clear();
//		
//		Decimalbox fn = new Decimalbox();
//		fn.setFormat(MedisafeConstants.CURRENCY_FORMAT);
//		
//		List list = MasterServiceLocator.getMiscFeeManager().getMiscFees();
//		Iterator it = list.iterator();
//		while(it.hasNext()){
//			mfee = (MsMiscFee)it.next();
//			
//			item = new Listitem();
//			item.setParent(miscFeeList);
//			item.setValue(mfee);
//			
//			cell = new Listcell(mfee.getVMfeeCode());
//			cell.setParent(item);
//			
//			cell = new Listcell(mfee.getVMfeeName());
//			cell.setParent(item);
//			
//			cell = new Listcell(mfee.getVMfeeDesc());
//			cell.setParent(item);
//			
//			cell = new Listcell(mfee.getMsPatientType().getVTpatientDesc());
//			cell.setParent(item);
//			
//			fn.setValue(new BigDecimal(mfee.getNMfeePrice()));
//			cell = new Listcell(fn.getText());
//			cell.setParent(item);
//		}
	}

}

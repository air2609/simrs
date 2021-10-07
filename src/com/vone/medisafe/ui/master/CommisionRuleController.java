package com.vone.medisafe.ui.master;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsDoctorRule;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.base.BaseController;

public class CommisionRuleController  extends BaseController{
	private static final String SEPARATOR = "--";

	private static final String ENTRY_KURANG_LEGKAP = "ENTRY KURANG LEGKAP";

	private static final String DOKTER_TAMU = "DOKTER TAMU";

	private static final String DOKTER_TETAP = "DOKTER TETAP";

	ZulConstraint constraints = new ZulConstraint();
	
	DoctorManager doctorManager = MasterServiceLocator.getDoctorManager();
	
	Listbox unitList;
	Listbox shifList;
	Listbox doctorTypeList;
	Intbox percentage;
	Button btnSave;
	Button btnModify;
	Button btnCancel;
	Button btnDelete;
	Listbox dokterFeeList;
	
	boolean isModify;
	MsDoctorRule msDoctorRule; 

	
	public void init(Component win) throws InterruptedException, VONEAppException {
		super.init(win);
		
		unitList = (Listbox)win.getFellow("unitList");
		shifList = (Listbox)win.getFellow("shifList");
		doctorTypeList = (Listbox)win.getFellow("doctorTypeList");
		
		percentage = (Intbox)win.getFellow("percentage");
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnCancel = (Button)win.getFellow("btnCancel");
		btnDelete = (Button)win.getFellow("btnDelete");
		dokterFeeList = (Listbox)win.getFellow("dokterFeeList");
		
		doctorManager.getAllUnit(unitList);
		doctorManager.getAllShift(shifList);
		doctorManager.getAllRule(dokterFeeList);
		
		doctorTypeList.getItems().clear();
		//add manually
		Listitem item;
		item = new Listitem();
		item.setParent(doctorTypeList);
		
		item = new Listitem();
		item.setParent(doctorTypeList);
		item.setLabel(DOKTER_TETAP);

		item = new Listitem();
		item.setParent(doctorTypeList);
		item.setLabel(DOKTER_TAMU);
		
		isModify = false;
		
	}


	public void doDelete() throws VONEAppException, InterruptedException{
		if (dokterFeeList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		int index = dokterFeeList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		
		msDoctorRule = (MsDoctorRule)dokterFeeList.getSelectedItem().getValue();
		dokterFeeList.removeItemAt(index);
		doctorManager.deleteMsDoctorRule(msDoctorRule);
		clear();
	}


	public void doCancel() throws VONEAppException{
		clear();
		btnCancel.setDisabled(true);
		btnModify.setDisabled(false);
	}


	private void clear() throws VONEAppException{
		unitList.setSelectedIndex(0);
		shifList.setSelectedIndex(0);
		doctorTypeList.setSelectedIndex(0);
		percentage.setValue(null);
		isModify = false;
		
	}


	public void doModify() throws VONEAppException, InterruptedException{
		if (dokterFeeList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}
		btnCancel.setDisabled(false);
		btnModify.setDisabled(true);
		isModify = true;
		
		msDoctorRule = (MsDoctorRule)dokterFeeList.getSelectedItem().getValue();
		Integer intTmp;
		for(int i=1; i < unitList.getItems().size(); i++){
			intTmp =(Integer)unitList.getItemAtIndex(i).getValue(); 
			if(intTmp.equals(msDoctorRule.getNUnitId())){
				unitList.setSelectedIndex(i);
				break;
			}
		}
		
		for(int i=1; i < shifList.getItems().size(); i++){
			intTmp =(Integer)shifList.getItemAtIndex(i).getValue(); 
			if(intTmp.equals(msDoctorRule.getNShiftId())){
				shifList.setSelectedIndex(i);
				break;
			}
		}
		String strTmp;
		for(int i=1; i < doctorTypeList.getItems().size(); i++){
			strTmp =(String)doctorTypeList.getItemAtIndex(i).getLabel(); 
			if(strTmp.equals(msDoctorRule.getVDocStatus())){
				doctorTypeList.setSelectedIndex(i);
				break;
			}
		}
		percentage.setValue(msDoctorRule.getNRsPersen());
		
	}


	public void doSave() throws VONEAppException, InterruptedException{
		if(unitList.getSelectedIndex()==0 
				|| shifList.getSelectedIndex() == 0 
				|| doctorTypeList.getSelectedIndex() == 0 
				|| percentage.getValue() == null){
			Messagebox.show(ENTRY_KURANG_LEGKAP);
			return;
		}
		
		if(isModify){
			//old record, sdh di-set dari doModify()
			isModify = false;
		}
		else{
			//new record
			msDoctorRule = new MsDoctorRule();
		}
		msDoctorRule.setNUnitId((Integer)unitList.getSelectedItem().getValue());
		msDoctorRule.setNShiftId((Integer)shifList.getSelectedItem().getValue());
		msDoctorRule.setNRsPersen(percentage.intValue());
		msDoctorRule.setVDocStatus((String)doctorTypeList.getSelectedItem().getLabel());
		msDoctorRule.setVRule(
				unitList.getSelectedItem().getLabel()
				+ SEPARATOR
				+ shifList.getSelectedItem().getLabel()
				+ SEPARATOR
				+ doctorTypeList.getSelectedItem().getLabel()
				);
		
		doctorManager.saveMsDoctorRule(msDoctorRule);
		clear();
		doctorManager.getAllRule(dokterFeeList);
		//button
		btnCancel.setDisabled(true);
		btnModify.setDisabled(false);
	}

}


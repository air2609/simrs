package com.vone.medisafe.ui.master.treatment;

import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

//todo TreatmentLabDetailController
public class TreatmentLabDetailController extends BaseController {
	Session session;
	MsUser user;
	Listbox list;
	
	Textbox jenisTextbox;
	Textbox quantifyTextbox;
	Textbox rangeTextbox;
	Button btnSave;
	Button btnDelete;
	Button btnCancel;
	Button btnModify;
	Listbox detailListbox;
	
	MsLabTreatmentDetil msLabTreatmentDetil;
	boolean isEditing = false;

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		jenisTextbox = (Textbox)cmp.getFellow("jenisTextbox");
		quantifyTextbox = (Textbox)cmp.getFellow("quantifyTextbox");
		rangeTextbox = (Textbox)cmp.getFellow("rangeTextbox");
		
		btnSave = (Button)cmp.getFellow("btnSave");
		btnDelete = (Button)cmp.getFellow("btnDelete");
		btnCancel = (Button)cmp.getFellow("btnCancel");
		btnModify = (Button)cmp.getFellow("btnModify");
		detailListbox = (Listbox)cmp.getFellow("detailListbox");
		
		ZulConstraint constraints = new ZulConstraint();
	//	constraints.registerComponent(jenisTextbox, ZulConstraint.UPPER_CASE);
	//	constraints.registerComponent(quantifyTextbox, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(rangeTextbox, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(jenisTextbox, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(quantifyTextbox, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(rangeTextbox, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
		session = getCurrentSession();
		user = getUserInfoBean().getMsUser();
		
		list = (Listbox) session.getAttribute("list");
		if(list.getSelectedCount() == 0)
			cmp.detach();
		getDeatilTreatment();
		isEditing = false;
	}
	
	public void ubahClick() throws VONEAppException, InterruptedException{
		if(detailListbox.getSelectedCount()==0)
			return;
		
		msLabTreatmentDetil = (MsLabTreatmentDetil) detailListbox.getSelectedItem().getValue();
		
		jenisTextbox.setValue(msLabTreatmentDetil.getVDetailName());
		quantifyTextbox.setValue(msLabTreatmentDetil.getVQuantify());
		rangeTextbox.setValue(msLabTreatmentDetil.getVNormalRange());
		
		isEditing = true;
		btnCancel.setDisabled(false);
		btnModify.setDisabled(true);
//		if(MasterServiceLocator.getLabTestDetailManager().delete(msLabTreatmentDetil)){
//			getDeatilTreatment();
//		}
		
	}
	
	public void simpanClick() throws VONEAppException, InterruptedException{
		//save data entry
		if(isEditing){
			saveEditing();
			isEditing = false;
			return;
		}
		ZulConstraint constraints = new ZulConstraint();
		constraints.registerComponent(jenisTextbox, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(quantifyTextbox, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(rangeTextbox, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		if (!constraints.validateComponent(true))
			return;
		
		msLabTreatmentDetil = new MsLabTreatmentDetil();
		msLabTreatmentDetil.setDWhnCreate(new Date());
		msLabTreatmentDetil.setVWhoCreate(user.getVUserName());
		msLabTreatmentDetil.setVDetailName(jenisTextbox.getText());
		msLabTreatmentDetil.setVQuantify(quantifyTextbox.getText());
		msLabTreatmentDetil.setVNormalRange(rangeTextbox.getText());
		
		
		MsTreatment msTreatment;
		msTreatment = (MsTreatment) list.getSelectedItem().getValue();
		msLabTreatmentDetil.setMsTreatment(msTreatment);
		
		if(MasterServiceLocator.getLabTestDetailManager().save(msLabTreatmentDetil)){
			getDeatilTreatment();
			clearForm();
		}
	}
	public void saveEditing() throws VONEAppException{
		msLabTreatmentDetil.setDWhnChange(new Date());
		msLabTreatmentDetil.setVWhoChange(user.getVUserName());
		msLabTreatmentDetil.setVDetailName(jenisTextbox.getText());
		msLabTreatmentDetil.setVQuantify(quantifyTextbox.getText());
		msLabTreatmentDetil.setVNormalRange(rangeTextbox.getText());
		if(MasterServiceLocator.getLabTestDetailManager().save(msLabTreatmentDetil)){
			getDeatilTreatment();
			clearForm();
		}
	}
	public void clearForm() throws VONEAppException{
		isEditing = false;
		btnCancel.setDisabled(true);
		btnModify.setDisabled(false);
		jenisTextbox.setValue(null);
		quantifyTextbox.setValue(null);
		rangeTextbox.setValue(null);
		jenisTextbox.focus();
	}

	public void hapusClick() throws VONEAppException, InterruptedException{
		//hapus salah satu item yg ada di listbox
		if(detailListbox.getSelectedCount()==0)
			return;
		
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		
		MsLabTreatmentDetil msLabTreatmentDetil;
		msLabTreatmentDetil = (MsLabTreatmentDetil) detailListbox.getSelectedItem().getValue();
		if(MasterServiceLocator.getLabTestDetailManager().delete(msLabTreatmentDetil)){
			getDeatilTreatment();
		}
	}
	public void getDeatilTreatment() throws VONEAppException{
		//menampilkan daftar detail treament ke listbox 
		MsTreatment msTreatment;
		msTreatment = (MsTreatment) list.getSelectedItem().getValue();
		MasterServiceLocator.getTreatmentGroupManager().getTestLabDetail(detailListbox, msTreatment);
		MiscTrxController.setFont(detailListbox);
		if(detailListbox.getItems().size()>0)
			detailListbox.setSelectedIndex(0);
	}
}

package com.vone.medisafe.ui.master;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Bandbox;
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
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.ui.base.BaseController;

public class LabTestDetailController extends BaseController{
	private MsLabTestDetail labdetil;
	private MsTreatment treatment;
	
	
	Bandbox kodeTreatment;
	Textbox namaTreatment;
	Textbox rangePria;
	Textbox rangeWanita;
	Textbox quantify;
	Textbox searchCode;
	Textbox searchName;
	
	Listbox treatmentList;
	Listbox labTestDetilList;
		
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		kodeTreatment.setValue(null);
		namaTreatment.setValue(null);
		rangePria.setValue(null);
		rangeWanita.setValue(null);
		quantify.setValue(null);
		labTestDetilList.clearSelection();
		kodeTreatment.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		item = labTestDetilList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		labdetil = (MsLabTestDetail)item.getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = labTestDetilList.getSelectedIndex();
		
		if(MasterServiceLocator.getLabTestDetailManager().delete(labdetil)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			labTestDetilList.removeItemAt(indexSelected);
					
		}else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
			
		}
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		item = labTestDetilList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		labdetil = (MsLabTestDetail)item.getValue();
		
		kodeTreatment.setValue(labdetil.getMsTreatment().getVTreatmentCode());
		namaTreatment.setValue(labdetil.getMsTreatment().getVTreatmentName());
		rangePria.setValue(labdetil.getVNrmlRangeGroup());
		rangeWanita.setValue(labdetil.getVNrmlRange());
		quantify.setValue(labdetil.getVLabTestQuantify());
		kodeTreatment.setAttribute("treatment", labdetil.getMsTreatment());
				
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		labdetil = new MsLabTestDetail();
		labdetil.setVLabTestQuantify(quantify.getValue());
		labdetil.setVNrmlRange(rangeWanita.getText());
		labdetil.setVNrmlRangeGroup(rangePria.getValue());
		
		treatment = (MsTreatment)kodeTreatment.getAttribute("treatment");
		labdetil.setMsTreatment(treatment);
		
				
		if(MasterServiceLocator.getLabTestDetailManager().save(labdetil)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setParent(labTestDetilList);
			item.setValue(labdetil);
			
			cell = new Listcell(labdetil.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(labdetil.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell("PRIA : "+labdetil.getVNrmlRangeGroup()+"; WANITA : "+labdetil.getVNrmlRange());
			cell.setParent(item);
			
			cell = new Listcell(labdetil.getVLabTestQuantify());
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
		item = labTestDetilList.getSelectedItem();
		labdetil = (MsLabTestDetail)item.getValue();
		labdetil.setVLabTestQuantify(quantify.getValue());
		labdetil.setVNrmlRange(rangeWanita.getText());
		labdetil.setVNrmlRangeGroup(rangePria.getValue());
		
		treatment = (MsTreatment)kodeTreatment.getAttribute("treatment");
		labdetil.setMsTreatment(treatment);
		
		if(MasterServiceLocator.getLabTestDetailManager().save(labdetil)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(labdetil);
			
			cell = new Listcell(labdetil.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(labdetil.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell("PRIA : "+labdetil.getVNrmlRangeGroup()+"; WANITA : "+labdetil.getVNrmlRange());
			cell.setParent(item);
			
			cell = new Listcell(labdetil.getVLabTestQuantify());
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
		kodeTreatment = (Bandbox)win.getFellow("kodeTreatment");
		namaTreatment = (Textbox)win.getFellow("namaTreatment"); 
		rangePria = (Textbox)win.getFellow("rangePria");
		rangeWanita = (Textbox)win.getFellow("rangeWanita");
		quantify = (Textbox)win.getFellow("quantify");
		searchCode = (Textbox)win.getFellow("searchCode");
		searchName = (Textbox)win.getFellow("searchName");
		
		treatmentList = (Listbox)win.getFellow("treatmentList");
		labTestDetilList = (Listbox)win.getFellow("labTestDetilList");
		
		super.init(win);
		
		constraints.registerComponent(kodeTreatment, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kodeTreatment, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(rangePria, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(rangePria, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(rangeWanita, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(rangeWanita, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(quantify, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(quantify, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(searchCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		getLabTestDeatilData(labTestDetilList);
		
		kodeTreatment.focus();
		treatmentList.getItems().clear();
		
	}
	
	public void getLabTestDeatilData(Listbox labTestDetailList) throws VONEAppException{
		
		MasterServiceLocator.getLabTestDetailManager().getLabtTestDeatils(labTestDetailList);
		
		
	}
	
	public void getTreatment(int type) throws InterruptedException, WrongValueException, VONEAppException{
		if(type == MedisafeConstants.INPUT_BY_SEARCH)
		{
			treatment = (MsTreatment)treatmentList.getSelectedItem().getValue();
			treatmentList.clearSelection();
		}
		
		else
		{
			treatment = TreatmentService.getTreatmentManager().getByTreatmentCode(kodeTreatment.getValue());
			if(treatment == null){
				Messagebox.show(MessagesService.getKey("common.data.notfound"));
				return;
			}
		}
		
		
		kodeTreatment.setValue(treatment.getVTreatmentCode());
		namaTreatment.setValue(treatment.getVTreatmentName());
		kodeTreatment.setAttribute("treatment", treatment);
		
	}
}

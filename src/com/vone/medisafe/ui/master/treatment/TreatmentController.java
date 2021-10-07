package com.vone.medisafe.ui.master.treatment;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
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
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;
import com.vone.medisafe.service.iface.master.treatment.TreatmentManager;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.UnitController;

public class TreatmentController extends BaseController{
	
	private MsTreatmentFee tfee;
	private MsTreatment treatment;
	
	public Textbox treatmentCode;
	public Textbox treatmentName;
//	Listbox unit;
	public Listbox treatmentGroup;
	public Listbox tclass;
	public Decimalbox doctorFee;
	public Decimalbox hospitalFee;
	public Decimalbox paramedicFee;
	public Decimalbox treatmentFee;
	public Bandbox coa;
	public Listbox treatmentList;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		
		super.doCancel(cmp);
		
		treatmentCode.setValue(null);
		doctorFee.setValue(null);
		hospitalFee.setValue(null);
		paramedicFee.setValue(null);
		treatmentFee.setValue(null);
		treatmentName.setValue(null);
//		unit.setSelectedIndex(0);
		treatmentGroup.setSelectedIndex(0);
		tclass.setSelectedIndex(0);
		coa.removeAttribute("coa");
		coa.setValue(null);
		
		
		this.tfee = null;
		this.treatment = null;
		this.treatmentList.clearSelection();
		treatmentCode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		if(treatmentList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.treatment.list.notselected"));
			treatmentCode.focus();
			return;
		}
		tfee = (MsTreatmentFee)treatmentList.getSelectedItem().getValue();
		treatment = tfee.getMsTreatment();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = treatmentList.getSelectedIndex();
		if(TreatmentService.getTreatmentManager().delete(tfee)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			treatmentList.removeItemAt(indexSelected);
			doCancel(cmp);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		
		if(treatmentList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.treatment.list.notselected"));
			btnModify.setDisabled(false);
			btnCancel.setDisabled(true);
			btnDelete.setDisabled(false);
			return;
		}
		
		TreatmentService.getTreatmentManager().prepareModify(this);
		
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	
	public void cekTreatment() throws VONEAppException{
		treatmentCode.setValue(treatmentCode.getValue().toUpperCase());
		treatment = TreatmentService.getTreatmentManager().getByTreatmentCode(treatmentCode.getText());
		
		if(treatment != null){
			treatmentName.setValue(treatment.getVTreatmentName());
			tclass.focus();
			
//			for(int i=1; i < unit.getItems().size(); i++){
//				if(treatment.getMsUnit().getNUnitId().equals(((MsUnit)unit.getItemAtIndex(i).getValue())
//						.getNUnitId()))
//				{
//					unit.setSelectedIndex(i);
//					break;
//				}
//			}
			
			for(int i=1; i < treatmentGroup.getItems().size(); i++){
				if(treatment.getMsTreatmentGroup().getNTgroupId().equals(((MsTreatmentGroup)treatmentGroup
						.getItemAtIndex(i).getValue()).getNTgroupId()))
				{
					treatmentGroup.setSelectedIndex(i);
					break;
				}
			}
		}
		
	}
	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		
		super.doSaveAdd(cmp);
		tfee = new MsTreatmentFee();
		
	
		if(treatment == null){
			treatment = new MsTreatment();
			treatment.setVTreatmentCode(treatmentCode.getValue());
			treatment.setVTreatmentName(treatmentName.getValue());
			
			item = treatmentGroup.getSelectedItem();			
			treatment.setMsTreatmentGroup((MsTreatmentGroup)item.getValue());
					
//			item = unit.getSelectedItem();
//			treatment.setMsUnit((MsUnit)item.getValue());
		}
		
		
		item = tclass.getSelectedItem();
		tfee.setMsTreatmentClass((MsTreatmentClass)item.getValue());
		
//		item = coa.getSelectedItem();
		tfee.setMsCoa((MsCoa)coa.getAttribute("coa"));
		
		tfee.setNTrtfeeFee(treatmentFee.getValue().doubleValue());
		if(doctorFee.getValue() != null)tfee.setNDoctorFee(doctorFee.getValue().doubleValue());
		if(paramedicFee.getValue() != null) tfee.setNMedicFee(paramedicFee.getValue().doubleValue());
		if(hospitalFee.getValue() != null)tfee.setNRsFee(hospitalFee.getValue().doubleValue());
		
		if(TreatmentService.getTreatmentManager().save(treatment, tfee)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			item = new Listitem();
			item.setValue(tfee);
			item.setParent(treatmentList);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell(hospitalFee.getText());
			cell.setParent(item);
			
			cell = new Listcell(doctorFee.getText());
			cell.setParent(item);
			
			cell = new Listcell(paramedicFee.getText());
			cell.setParent(item);
			
			cell = new Listcell(treatmentFee.getText());
			cell.setParent(item);
			
			doCancel(cmp);
			
		}else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		
		MiscTrxController.setFont(treatmentList);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		
		super.doSaveModify(cmp);
		
		tfee = (MsTreatmentFee)treatmentList.getSelectedItem().getValue();
		treatment = tfee.getMsTreatment();
		
		treatment.setVTreatmentCode(treatmentCode.getValue());
		treatment.setVTreatmentName(treatmentName.getValue());
		
		item = treatmentGroup.getSelectedItem();
		treatment.setMsTreatmentGroup((MsTreatmentGroup)item.getValue());
				
//		item = unit.getSelectedItem();
//		treatment.setMsUnit((MsUnit)item.getValue());
		
		item = tclass.getSelectedItem();
		tfee.setMsTreatmentClass((MsTreatmentClass)item.getValue());
		
//		item = coa.getSelectedItem();
		tfee.setMsCoa((MsCoa)coa.getAttribute("coa"));
		
		tfee.setNTrtfeeFee(treatmentFee.getValue().doubleValue());
		
		if(doctorFee.getValue() != null)tfee.setNDoctorFee(doctorFee.getValue().doubleValue());
		if(paramedicFee.getValue() != null) tfee.setNMedicFee(paramedicFee.getValue().doubleValue());
		if(hospitalFee.getValue() != null)tfee.setNRsFee(hospitalFee.getValue().doubleValue());
		
		if(TreatmentService.getTreatmentManager().save(treatment, tfee)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			item = treatmentList.getSelectedItem();
			item.getChildren().clear();
			item.setValue(tfee);
			item.setParent(treatmentList);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell(hospitalFee.getText());
			cell.setParent(item);
			
			cell = new Listcell(doctorFee.getText());
			cell.setParent(item);
			
			cell = new Listcell(paramedicFee.getText());
			cell.setParent(item);
			
			cell = new Listcell(treatmentFee.getText());
			cell.setParent(item);
			
			doCancel(cmp);
			
		}else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		MiscTrxController.setFont(treatmentList);
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		treatmentCode = (Textbox)win.getFellow("treatmentCode");
		treatmentName = (Textbox) win.getFellow("treatmentName");
//		unit = (Listbox) win.getFellow("unit");
		treatmentGroup = (Listbox) win.getFellow("treatmentGroup");
		tclass = (Listbox) win.getFellow("tclass");
		doctorFee = (Decimalbox)win.getFellow("doctorFee");
		hospitalFee = (Decimalbox)win.getFellow("hospitalFee");
		paramedicFee = (Decimalbox)win.getFellow("paramedicFee");
		treatmentFee = (Decimalbox) win.getFellow("treatmentFee");
		coa = (Bandbox)win.getFellow("coa");
		treatmentList = (Listbox)win.getFellow("treatmentList");
		
		super.init(win);
		
		constraints.registerComponent(treatmentCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatmentName, ZulConstraint.NO_EMPTY);
//		constraints.registerComponent(unit, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatmentGroup, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(tclass, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatmentFee, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(coa, ZulConstraint.NO_EMPTY);
				
		constraints.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		TreatmentClassManager tclassService = MasterServiceLocator.getTreatmentClassManager();
		
//		UnitController.getUnitForSelect(unit);
		TreatmentGroupController.getTGroupForSelect(treatmentGroup);
		
		tclassService.getTClassForSelect(tclass);
		//TreatmentClassController.getTClassDataList(tclass); //todo disini kelihatannya binun
		//TreatmentClassController.getTClass(tclass); 
//		CoaController.getCoaForSelect(coa, MedisafeConstants.COA_ALL);
		
		//getTreatmentData(treatmentList);
		
	}
	
	public void getTreatmentData(Listbox treatmentList) throws VONEAppException{
		
		TreatmentService.getTreatmentManager().getTreatments(treatmentList);
		MiscTrxController.setFont(treatmentList);

	}
	
	public static void searchTreatment(Listbox treatmentList, Textbox code, Textbox name) throws VONEAppException{
		treatmentList.getItems().clear();
		MsTreatment treatment = new MsTreatment();
		treatment.setVTreatmentCode("%"+code.getText()+"%");
		treatment.setVTreatmentName("%"+name.getText()+"%");
		
		Listitem item;
		Listcell cell;
		
		List list = TreatmentService.getTreatmentManager().searchTreatment(treatment);
		Iterator it = list.iterator();
		while(it.hasNext()){
			treatment = (MsTreatment)it.next();
			item = new Listitem();
			item.setValue(treatment);
			item.setParent(treatmentList);
			
			cell = new Listcell(treatment.getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(treatment.getVTreatmentName());
			cell.setParent(item);

		}
	}
	
	
	public void calculateTotal() throws VONEAppException{
		double total = 0;
		if(doctorFee.getValue() != null) total = total + doctorFee.getValue().doubleValue();
		if(hospitalFee.getValue() != null)total = total + hospitalFee.getValue().doubleValue();
		if(paramedicFee.getValue()!= null) total = total +  paramedicFee.getValue().doubleValue();
		treatmentFee.setValue(new BigDecimal(total));
	}
	
	
	public void cariClick(Listbox tList, Textbox input) throws VONEAppException{
		
		TreatmentManager treatment = TreatmentService.getTreatmentManager();
		
		input.setValue(input.getText().toUpperCase());
		treatment.search("%"+input.getText()+"%",tList);
		
		MiscTrxController.setFont(tList);
	}

}

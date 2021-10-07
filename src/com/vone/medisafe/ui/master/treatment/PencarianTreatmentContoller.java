package com.vone.medisafe.ui.master.treatment;

import java.math.BigDecimal;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.service.iface.master.treatment.TreatmentManager;
import com.vone.medisafe.ui.base.BaseController;

public class PencarianTreatmentContoller  extends BaseController{
	
	Textbox treatmentCode;
	Textbox treatmentName;
	//Listbox unitList;
	Listbox tclassList;
	Listbox treatmentList;
	
	Window win;
	Window winUtama;
	
	
	ZulConstraint constrains = new ZulConstraint();
	
	@Override
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		treatmentCode = (Textbox)win.getFellow("treatmentCode");
		treatmentName = (Textbox)win.getFellow("treatmentName");
	//	unitList = (Listbox)win.getFellow("unitList");
		tclassList = (Listbox)win.getFellow("tclassList");
		treatmentList = (Listbox)win.getFellow("treatmentList");
		
		this.win = (Window)win;
		
		UnitManager unitService = MasterServiceLocator.getUnitManager();
		TreatmentClassManager tclassService = MasterServiceLocator.getTreatmentClassManager();
		
	//	unitService.getMsUnitForSelect(unitList);
		tclassService.getTClassForSelect(tclassList);
		
		constrains.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		
		
		Page page = win.getDesktop().getPage("treatmentPage");
		
		winUtama = (Window)page.getFellow("masterTreatment");
		
		Listbox treatmentList = (Listbox)winUtama.getFellow("treatmentList");
		treatmentList.clearSelection();
		
		
	}
	
	
	
	public void search() throws VONEAppException{
		
		TreatmentManager service = TreatmentService.getTreatmentManager();
		
		String tclass ="";
		String unit="";
		if(!tclassList.getSelectedItem().getValue().equals(MedisafeConstants.LISTKOSONG))
			tclass = ((MsTreatmentClass) tclassList.getSelectedItem().getValue()).getVTclassDesc();
		
//		if(!unitList.getSelectedItem().getValue().equals(MedisafeConstants.LISTKOSONG))
//			unit = ((MsUnit)unitList.getSelectedItem().getValue()).getVUnitName();
//		
		service.searchTreatment("%"+treatmentCode.getText()+"%", "%"+treatmentName.getText()+"%",
				"%"+unit+"%","%"+tclass,
				treatmentList);
		
		
	}
	
	
	
	
	public void getDetil() throws VONEAppException, InterruptedException{
		
		MsTreatmentFee tfee;
		
		
		Listbox treatmentList = (Listbox)winUtama.getFellow("treatmentList");
		
		treatmentList.clearSelection();
		
		
		
		if(this.treatmentList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.treatment.list.notselected"));
			return;
		}
		tfee = (MsTreatmentFee)this.treatmentList.getSelectedItem().getValue();
		
		for(int i=0; i < treatmentList.getItems().size(); i++){
			
			if(tfee.getNTreatmentFeeId().equals(((MsTreatmentFee)treatmentList.getItemAtIndex(i).getValue())
					.getNTreatmentFeeId())){
				
				treatmentList.setSelectedIndex(i);
			}
		}
		
		win.detach();

	}
	
	
	

}

package com.vone.medisafe.ward;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class BedDisplayController extends BaseController{
	
	Listbox bedList;
	Checkbox cbAll;
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		super.init(cmp);
		
		bedList = (Listbox)cmp.getFellow("bedList");
		cbAll = (Checkbox)cmp.getFellow("cbAll");
		
		MasterServiceLocator.getBedManager().getActiveBed(bedList, cbAll);
	}
	
	public void save() throws VONEAppException, InterruptedException{
		List<MsBed> beds = new ArrayList<MsBed>();
		List<Listitem> items = bedList.getItems();
		for(Listitem item : items){
			Listcell cell = (Listcell)item.getChildren().get(7);
			Checkbox cb = (Checkbox)cell.getChildren().get(0);
			
			MsBed bed = (MsBed)item.getValue();
			if(cb.isChecked()) bed.setIsShown("Y");
			else bed.setIsShown("N");
			
			if("0".equals(bed.getVBedStatus())) {
				cell = (Listcell)item.getChildren().get(8);
				Listbox lb = (Listbox)cell.getChildren().get(0);
				bed.setAvailableStatus(lb.getSelectedItem().getValue().toString());
			}
						
			beds.add(bed);
		}
		
		MasterServiceLocator.getBedManager().saveBulk(beds);
		Messagebox.show(MessagesService.getKey("common.save.success"));
		
	}
	
	public void check() throws InterruptedException{
		
		if(cbAll.isChecked()){
			List<Listitem> items = bedList.getItems();
			for(Listitem item : items){
				Listcell cell = (Listcell)item.getChildren().get(7);
				Checkbox cb = (Checkbox)cell.getChildren().get(0);
				cb.setChecked(true);
			}
		}
		else{
			List<Listitem> items = bedList.getItems();
			for(Listitem item : items){
				Listcell cell = (Listcell)item.getChildren().get(7);
				Checkbox cb = (Checkbox)cell.getChildren().get(0);
				cb.setChecked(false);
			}
		}
		
	}

}

package com.vone.medisafe.ui.accounting;

import java.util.Date;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.mapping.*;

public class AcctDefaultDataInput extends BaseController{

	Listbox inAr = null;
	Listbox outAr = null;
	Listbox ap = null;
	Listbox pph21 = null;
	Listbox miscTrx = null;
	Listbox apPatient = null;
	Listbox apStaff = null;
	
	@Override
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	@Override
	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
//		super.doSave(cmp);
		
		MsGim gim = null;
		MsCoa coa = null;
		
		//Inpatient
		if (!(inAr.getSelectedItem().getValue() instanceof String)){
			
			coa = (MsCoa)inAr.getSelectedItem().getValue();
			gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
			if (gim != null){
				gim.setVValue(coa.getVAcctNo());
				MasterServiceLocator.getGimManager().update(gim);
			}else{
			
				gim = new MsGim();
				gim.setVKey(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
				gim.setVValue(coa.getVAcctNo());
				gim.setVWhoCreate(super.getUserInfoBean().getStUserId());
				gim.setDWhnCreate(new Date());
				
				MasterServiceLocator.getGimManager().save(gim);
			}
		}
		
		//Outpatient
		if (!(outAr.getSelectedItem().getValue() instanceof String)){
			
			coa = (MsCoa)outAr.getSelectedItem().getValue();
			gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_OUTPATIENT_AR);
			if (gim != null){
				gim.setVValue(coa.getVAcctNo());
				MasterServiceLocator.getGimManager().update(gim);
			}else{
			
				gim = new MsGim();
				gim.setVKey(MedisafeConstants.COA_DEFAULT_OUTPATIENT_AR);
				gim.setVValue(coa.getVAcctNo());
				gim.setVWhoCreate(super.getUserInfoBean().getStUserId());
				gim.setDWhnCreate(new Date());
				
				MasterServiceLocator.getGimManager().save(gim);
			}
		}		
		
		//AP
		if (!(ap.getSelectedItem().getValue() instanceof String)){
			
			coa = (MsCoa)ap.getSelectedItem().getValue();
			gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_AP);
			if (gim != null){
				gim.setVValue(coa.getVAcctNo());
				MasterServiceLocator.getGimManager().update(gim);
			}else{
			
				gim = new MsGim();
				gim.setVKey(MedisafeConstants.COA_DEFAULT_AP);
				gim.setVValue(coa.getVAcctNo());
				gim.setVWhoCreate(super.getUserInfoBean().getStUserId());
				gim.setDWhnCreate(new Date());
				
				MasterServiceLocator.getGimManager().save(gim);
			}
		}		
		
		//PPH21
		if (!(pph21.getSelectedItem().getValue() instanceof String)){
			
			coa = (MsCoa)pph21.getSelectedItem().getValue();
			gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_PPH21);
			if (gim != null){
				gim.setVValue(coa.getVAcctNo());
				MasterServiceLocator.getGimManager().update(gim);
			}else{
			
				gim = new MsGim();
				gim.setVKey(MedisafeConstants.COA_DEFAULT_PPH21);
				gim.setVValue(coa.getVAcctNo());
				gim.setVWhoCreate(super.getUserInfoBean().getStUserId());
				gim.setDWhnCreate(new Date());
				
				MasterServiceLocator.getGimManager().save(gim);
			}
		}
		
		//MISC TRX
		if (!(pph21.getSelectedItem().getValue() instanceof String)){
			
			coa = (MsCoa)miscTrx.getSelectedItem().getValue();
			gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_MISC_TRX);
			if (gim != null){
				gim.setVValue(coa.getVAcctNo());
				MasterServiceLocator.getGimManager().update(gim);
			}else{
			
				gim = new MsGim();
				gim.setVKey(MedisafeConstants.COA_DEFAULT_MISC_TRX);
				gim.setVValue(coa.getVAcctNo());
				gim.setVWhoCreate(super.getUserInfoBean().getStUserId());
				gim.setDWhnCreate(new Date());
				
				MasterServiceLocator.getGimManager().save(gim);
			}
		}		
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		
		//PATIENT AP
		if (!(apPatient.getSelectedItem().getValue() instanceof String)){
			
			coa = (MsCoa)apPatient.getSelectedItem().getValue();
			gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_PATIENT_AP);
			if (gim != null){
				gim.setVValue(coa.getVAcctNo());
				MasterServiceLocator.getGimManager().update(gim);
			}else{
			
				gim = new MsGim();
				gim.setVKey(MedisafeConstants.COA_DEFAULT_PATIENT_AP);
				gim.setVValue(coa.getVAcctNo());
				gim.setVWhoCreate(super.getUserInfoBean().getStUserId());
				gim.setDWhnCreate(new Date());
				
				MasterServiceLocator.getGimManager().save(gim);
			}
		}	
		
		//PATIENT AP
		if (!(apStaff.getSelectedItem().getValue() instanceof String)){
			
			coa = (MsCoa)apStaff.getSelectedItem().getValue();
			gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_STAFF_AP);
			if (gim != null){
				gim.setVValue(coa.getVAcctNo());
				MasterServiceLocator.getGimManager().update(gim);
			}else{
			
				gim = new MsGim();
				gim.setVKey(MedisafeConstants.COA_DEFAULT_STAFF_AP);
				gim.setVValue(coa.getVAcctNo());
				gim.setVWhoCreate(super.getUserInfoBean().getStUserId());
				gim.setDWhnCreate(new Date());
				
				MasterServiceLocator.getGimManager().save(gim);
			}
		}
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
	}

	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		inAr = (Listbox)cmp.getFellow("inAr");
		outAr = (Listbox)cmp.getFellow("outAr");
		ap = (Listbox)cmp.getFellow("ap");
		pph21 = (Listbox)cmp.getFellow("pph");
		miscTrx = (Listbox)cmp.getFellow("miscTrx");
		apPatient = (Listbox)cmp.getFellow("apPatient");
		apStaff = (Listbox)cmp.getFellow("apStaff");
		
		CoaController.getCoaForSelect(inAr, MedisafeConstants.COA_ALL);
		CoaController.getCoaForSelect(outAr, MedisafeConstants.COA_ALL);
		CoaController.getCoaForSelect(ap, MedisafeConstants.COA_ALL);
		CoaController.getCoaForSelect(apPatient, MedisafeConstants.COA_ALL);
		CoaController.getCoaForSelect(pph21, MedisafeConstants.COA_ALL);
		CoaController.getCoaForSelect(miscTrx, MedisafeConstants.COA_ALL);
		CoaController.getCoaForSelect(apStaff, MedisafeConstants.COA_ALL);
		
		MsGim gim = null;
		
		//Inpatient
		gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
		if (gim != null)
			selectCOA(inAr, gim.getVValue());
		
		//Outpatient
		gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_OUTPATIENT_AR);
		if (gim != null)
			selectCOA(outAr, gim.getVValue());
		
		//AP
		gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_AP);
		if (gim != null)
			selectCOA(ap, gim.getVValue());
		
		//AP PATIENT
		gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_PATIENT_AP);
		if (gim != null)
			selectCOA(apPatient, gim.getVValue());
		
		//PPH21
		gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_PPH21);
		if (gim != null)
			selectCOA(pph21, gim.getVValue());
		
		//MISC TRX		
		gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_MISC_TRX);
		if (gim != null)
			selectCOA(miscTrx, gim.getVValue());
		
		//MISC TRX		
		gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_STAFF_AP);
		if (gim != null)
			selectCOA(apStaff, gim.getVValue());
	}
	
	private void selectCOA(Listbox listCOA, String coaCode){
		Iterator<Listitem> it = listCOA.getItems().iterator();
		
		while (it.hasNext()){
			Listitem item = it.next();
								
			Object temp = item.getValue();
			
			MsCoa coa = null;
			
			if (temp instanceof String){
				listCOA.setSelectedIndex(0);
				continue;
			}
			else
				coa = (MsCoa)temp;
			
			if (coa.getVAcctNo().equals(coaCode)){
				listCOA.setSelectedItem(item);
				break;
			}
		}
	}
}

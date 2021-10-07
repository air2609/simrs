package com.vone.medisafe.ui.common;

import java.math.BigDecimal;
import java.util.Iterator;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
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
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.ui.base.BaseController;

public class CommonBundledTreatmentController extends BaseController{
	
	MsUnit unit;
	private MsTreatmentFee tfee;
	
	Textbox treatmentCode;
	Textbox treatmentName;
	Bandbox examinerDoctor;
	Listbox treatmentList;
	Decimalbox decbox;
	Component win;
	
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	
	String kelasTarif;
	
	Session session;
	
	ZulConstraint constraint = new ZulConstraint();
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		treatmentCode = (Textbox) win.getFellow("treatmentCode");
		treatmentName = (Textbox)win.getFellow("treatmentName");
		examinerDoctor = (Bandbox)win.getFellow("examinerDoctor");
		treatmentList = (Listbox)win.getFellow("treatmentList");
		decbox = new Decimalbox();
		decbox.setFormat("#,##0.##");
		
		this.win = win;
		
		session = win.getDesktop().getSession();
		
		listbox = (Listbox)session.getAttribute("bundlelist");
		Listbox location = (Listbox)session.getAttribute("location");
		
		super.init(win);
		
		constraint.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		
	
		
		unit = (MsUnit)location.getSelectedItem().getValue();

//		DoctorController.getDoctorForSelect(examinerDoctorList);
		if(session.getAttribute("isRanap").equals("YES")){
			//ambil kelas tarif ranap
			kelasTarif = session.getAttribute("kelasTarif").toString();
		}else{
			//default kelas tarif : Kelas II
			kelasTarif = MedisafeConstants.DEFAULT_TCLASS;
		}
			

		
		treatmentList.getItems().clear();
		
	
	}
	
	
	
	public void getTreatment() throws InterruptedException{
		if(treatmentList.getSelectedItems().size() == 0){
			Messagebox.show(MessagesService.getKey("treatment.not.selected"));
			treatmentList.focus();
			return;
		}
	
		Listitem listitem;
		Listcell listcell;
		Label harga;
		Iterator it = treatmentList.getSelectedItems().iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			listitem = new Listitem();
			tfee = (MsTreatmentFee)item.getValue();
			listitem.setValue(tfee);
			listitem.setParent(listbox);
			
			listcell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			listcell.setParent(listitem);
			
			listcell = new Listcell("1");
			listcell.setParent(listitem);
			
			
			if (examinerDoctor.getText().equals("")){
				
				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
				
			}
			else
			{
			
				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName()+"-"+ examinerDoctor.getText());
			}
			listcell.setParent(listitem);
			
			Decimalbox dbharga = new Decimalbox();
			dbharga.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			this.win.detach();
			
			
		}
		MiscTrxController.setFont(this.listbox);
	}
	
	public void getOut(){
		session.removeAttribute("bundlelist");
	}
	
	
	public void searchTreatment() throws InterruptedException, VONEAppException{
		
		TreatmentService.getTreatmentManager().searchTreatment(unit.getNUnitId(), treatmentCode, 
				treatmentName,kelasTarif, MedisafeConstants.NON_PAKET, treatmentList);
		
		treatmentCode.setValue(null);
		treatmentName.setValue(null);
		treatmentCode.focus();
	}

}

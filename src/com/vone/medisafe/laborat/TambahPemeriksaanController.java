package com.vone.medisafe.laborat;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.ui.base.BaseController;

public class TambahPemeriksaanController extends BaseController{
	
	MsTreatmentFee tfee;
	MsUnit unit;
	
	Textbox treatmentCode;
	Textbox treatmentName;
	Listbox treatmentList;
	Decimalbox decbox;
	Component win;
	
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	
	String kelasTarif;
	
	Session session;
	
	ZulConstraint constraint = new ZulConstraint();
	Logger logger = Logger.getLogger(TambahPemeriksaanController.class);
	
	MsStaffInUnit siu;
	UserInfoBean uib;
	MsUser user;
	Listbox location; 
	
//	private LaboratManager labServ = Service.getLaboratManager();
	

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
		treatmentList = (Listbox)win.getFellow("treatmentList");
		decbox = new Decimalbox();
		decbox.setFormat("#,##0.##");
		
		this.win = win;
		
		session = win.getDesktop().getSession();
		
		listbox = (Listbox)session.getAttribute("listbox");
		location = (Listbox)session.getAttribute("location");
		kelasTarif = (String)session.getAttribute("kelasTarif");
		
		super.init(win);
		
		constraint.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		
		uib = (UserInfoBean) session.getAttribute(Constant.USER_INFO);
		user = uib.getMsUser();
		treatmentCode.focus();
//		siu  = user.getMsStaff().getMsStaffInUnits().toArray()
		
		//todo tentukan unit
//		Object[] obj = user.getMsStaff().getMsStaffInUnits().toArray(); 
//		if(obj.length == 1){
//			siu = (MsStaffInUnit)obj[0];
//			unit = siu.getId().getMsUnit();
//			DoctorController.getDoctorBaseOnUnitId2(unit.getNUnitId(), examinerDoctorList);
			
			if(session.getAttribute("isRanap").equals("YES")){
				//ambil kelas tarif ranap
				kelasTarif = (String)session.getAttribute("kelasTarif");
				if(kelasTarif == null){
					Messagebox.show(MessagesService.getKey("ranap.patinet.not.fill"));
					win.detach();
					return;
				}
			}else{
				//default kelas tarif : Kelas II
				kelasTarif = "KELAS II";
			}
			
//			getTreatmentBaseOnUnitAndClass(siu.getId().getMsUnit(), kelasTarif);
//		}else{
//			unit = (MsUnit)location.getSelectedItem().getValue();
//			
//			if(session.getAttribute("isRanap").equals("YES")){
//				//ambil kelas tarif ranap
//				kelasTarif = session.getAttribute("kelasTarif").toString();
//			}else{
//				//default kelas tarif : Kelas II
//				kelasTarif = "KELAS II";
//			}
			
//			getTreatmentBaseOnUnitAndClass(unit, kelasTarif);
//		}
		
		//treatmentList.getItems().clear();
	}
	
	public void getTreatmentBaseOnUnitAndClass(MsUnit unit, String kelasTarif)throws VONEAppException {
		treatmentList.getItems().clear();
		
//		List list = TreatmentService.getTreatmentManager().getTreatmentByUnitAndClass(unit, kelasTarif, MedisafeConstants.NON_PAKET);
		List list = TreatmentService.getTreatmentManager().getTreatmentByUnit(unit.getNUnitId(),
				"%", 
				"%", 
				kelasTarif 
				);
//		List list = TreatmentService.getTreatmentManager().getTreatmentByUnitAndClass(unit, kelasTarif);
		Iterator it = list.iterator();
		while(it.hasNext()){
			tfee = (MsTreatmentFee)it.next();
			item = new Listitem();
			item.setValue(tfee);
			item.setParent(treatmentList);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			decbox.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			cell = new Listcell(decbox.getText());
			cell.setParent(item);
		}
	}
	
	public void getTreatment() throws InterruptedException, VONEAppException{
		LaboratManager labManager = MasterServiceLocator.getLaboratManager(); 
		labManager.getSelectedTreatment(this);
	}
	
	public void getOut(){
		session.removeAttribute("listbox");
	}
	
	public void searchTreatment() throws InterruptedException, VONEAppException{
		//kalo blm ngisi code/name -> balik
		if(treatmentCode.getText().equals("") && treatmentName.getText().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			treatmentCode.focus();
			return;
		}
		treatmentCode.focus();
		LaboratManager labManager = MasterServiceLocator.getLaboratManager(); 
		labManager.searchTreatment(this);
		MiscTrxController.setFont(treatmentList);


	}
	
	

}

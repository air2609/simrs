package com.vone.medisafe.laborat;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class KimiaController  extends BaseController{
//	private MsTreatmentFee tfee;
//	private MsStaffInUnit siu;
//	private MsUnit unit;
	
	Listbox theList;
	
	String kelasTarif;
	Session session;
	
	Listitem item;
	Listcell cell;
	MsUser user;

	private UserInfoBean uib;
	
	public void init(Component win, String theList, String treatmentGroup) throws InterruptedException, VONEAppException {
		super.init(win);
		this.theList = (Listbox)win.getFellow(theList);
		this.theList.getItems().clear();
		
		session = win.getDesktop().getSession();
		session.setAttribute(theList, this.theList);
//		Listbox location = (Listbox)session.getAttribute("location");
		
		//kelasTarif = "KELAS II";
		
		uib = (UserInfoBean) session.getAttribute(Constant.USER_INFO);
		user = uib.getMsUser();
		
		
		//Object[] obj = user.getMsStaff().getMsStaffInUnits().toArray(); 
		
//		if(obj.length == 1){
////			siu = (MsStaffInUnit)obj[0];
////			unit = siu.getId().getMsUnit();
//			
//			if(session.getAttribute("isRanap").equals("YES")){
//				//ambil kelas tarif ranap
//				kelasTarif = (String)session.getAttribute("kelasTarif");
//				if(kelasTarif == null){
//					Messagebox.show(MessagesService.getKey("ranap.patinet.not.fill"));
//					win.detach();
//					return;
//				}
//			}else{
//				//default kelas tarif : Kelas II
//				kelasTarif = "KELAS II";
//			}
//			
//		}else{
//			unit = (MsUnit)location.getSelectedItem().getValue();			
			if("YES".equals(session.getAttribute("isRanap"))){
				//ambil kelas tarif ranap
				kelasTarif = session.getAttribute("kelasTarif").toString();
			}else{
				//default kelas tarif : Kelas II
				kelasTarif = "KELAS II";
				
				if(session.getAttribute("BPJS").equals("YES")){
					kelasTarif = "BPJS";
				}
			}
			
//		}
		getTreatmentByTreatmentGroup(treatmentGroup, kelasTarif);		
	}
	
	private void getTreatmentByTreatmentGroup(String code, String kelasTarif) throws VONEAppException {
		
		LaboratManager labManager = MasterServiceLocator.getLaboratManager();
		labManager.fillTreatment(this, code, kelasTarif);
		

	}
}

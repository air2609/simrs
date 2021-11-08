package com.vone.medisafe.ui.common;



import java.math.BigDecimal;
import java.util.Iterator;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.DiscontListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.service.iface.master.StaffManager;
import com.vone.medisafe.ui.base.BaseController;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class CommonTreatmentController extends BaseController{
	
	
	private MsTreatmentFee tfee;
	private MsUnit unit;
	Textbox cariTextbox;
	Textbox treatmentCode;
	Textbox treatmentName;
	Bandbox examinerDoctor;
	Listbox treatmentList;
	Decimalbox decbox;
	Component win;
	
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	
	Textbox code;
	Textbox name;
	
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
		code = (Textbox)win.getFellow("code");
		name = (Textbox)win.getFellow("name");
		
		decbox = new Decimalbox();
		decbox.setFormat("#,##0.##");
		
		this.win = win;
		
		session = win.getDesktop().getSession();
		
		listbox = (Listbox)session.getAttribute("listbox");
		Listbox location = (Listbox)session.getAttribute("location");
		
		super.init(win);
		
		constraint.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(code, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(name,ZulConstraint.UPPER_CASE);
		
		UserInfoBean ub = getUserInfoBean();
		if(ub == null){
			Messagebox.show("uib kosong");
			return;
		}
		
		
//		DoctorController.getDoctorForSelect(examinerDoctorList);
		
				
		unit = (MsUnit) location.getSelectedItem().getValue();
		TbRegistration reg = (TbRegistration)session.getAttribute("registration");
		
		StaffManager staffManager = MasterServiceLocator.getStaffManager();
		MsStaff staff = staffManager.getByStaffId(reg.getMsStaff().getNStaffId());
		examinerDoctor.setValue(staff.getVStaffName());
		examinerDoctor.setAttribute("doctor", staff);

		if (session.getAttribute("isRanap").equals("YES")) {
			// ambil kelas tarif ranap
			kelasTarif = session.getAttribute("kelasTarif").toString();
		} else {
			// default kelas tarif : Kelas II
			kelasTarif = "KELAS III";
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
			
			if (tfee.getNDoctorFee() > 0){
				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName()+"-"+examinerDoctor.getText());
			}
			else
			{
				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			}
			
			listcell.setParent(listitem);
			
			listcell = new Listcell();
			Intbox jumlahTindakan = new Intbox();
			jumlahTindakan.setValue(1);
			jumlahTindakan.setParent(listcell);
			listcell.setParent(listitem);
			
			listcell = new Listcell("-");
			listcell.setParent(listitem);
			
			Decimalbox dbharga = new Decimalbox();
			dbharga.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			dbharga.setFormat("#,###");
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Listcell diskon  = new Listcell();
			diskon.setParent(listitem);
			Decimalbox decimalbox = new Decimalbox();
			decimalbox.setValue(new BigDecimal(0));
			decimalbox.setParent(diskon);
			decimalbox.setWidth("50%");
			decimalbox.setStyle("font-size:8pt");
			decimalbox.setFormat("#,###");
			decimalbox.setHeight("14px");
			Listbox diskonList = new Listbox();
			diskonList.setParent(diskon);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			item = new Listitem();
			item.setValue(MedisafeConstants.RP);
			item.setSelected(true);
			item.setLabel("1. RP");
			item.setParent(diskonList);
			item = new Listitem();
			item.setValue(MedisafeConstants.PERCENT);
			item.setLabel("2. %");
			item.setParent(diskonList);
			
//			Label finalPrice = new Label();
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Object[] obj = new Object[]{dbharga.getValue(),dbharga.getValue(),"RP",new Intbox(1),decimalbox.getValue()};
			listitem.setAttribute("manipulation", obj);
			
			MsStaff staff = (MsStaff)examinerDoctor.getAttribute("doctor");
			listitem.setAttribute("doctor",staff);
			
			decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, jumlahTindakan, new Short("0")));
			diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, jumlahTindakan, new Short("0")));
			jumlahTindakan.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, jumlahTindakan, new Short("0")));
			
			
		}
		this.win.detach();
		MiscTrxController.setFont(this.listbox);
	}
	
	public void getOut(){
		session.removeAttribute("listbox");
	}
	
	public void searchTreatment() throws InterruptedException, VONEAppException{
		
				
		TreatmentService.getTreatmentManager().searchTreatment(unit.getNUnitId(), treatmentCode, 
				treatmentName,kelasTarif, MedisafeConstants.NON_PAKET, treatmentList);
		treatmentCode.setValue(null);
		treatmentName.setValue(null);
		treatmentCode.focus();
	}
	
	
	
	
}

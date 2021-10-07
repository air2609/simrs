package com.vone.medisafe.surgery;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.DiscontListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.ui.base.BaseController;

public class TambahTindakanController extends BaseController{
	
//	private MsStaffInUnit siu;
	public MsTreatmentFee tfee;
	public MsUnit unit;
	
	public Textbox treatmentCode;
	public Textbox treatmentName;
	Listbox examinerDoctorList;
	public Listbox treatmentList;
	Listbox radiograferList;
	public Decimalbox decbox;
	Component win;
	
	Bandbox examinerDoctor;
	Textbox code;
	Textbox name;
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	Listbox tclassList;
	
	public String kelasTarif;
	
	Session session;
	
	ZulConstraint constraint = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		super.doClose(cmp);
	}

	
	public UserInfoBean getUserInfoBean() {
		return super.getUserInfoBean();
	}
	
	private void getRadiografer() throws VONEAppException{
		radiograferList.getItems().clear();
		Listitem item = new Listitem();
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(radiograferList);
		radiograferList.setSelectedItem(radiograferList.getItemAtIndex(0));
	
		MsStaff msStaff;
		int i = 1;

		List list = Service.getRadiologyManager().getMedician("RADIOGRAFER");
		
//		System.out.println(list.size());
		
		Iterator it = list.iterator();
		while (it.hasNext()) {
			msStaff = (MsStaff) it.next();
			item = new Listitem();
			item.setValue(msStaff);
			item.setLabel(i + "." + msStaff.getVStaffName());
			item.setParent(radiograferList);
			i++;
		}

	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		treatmentCode = (Textbox) win.getFellow("treatmentCode");
		treatmentName = (Textbox)win.getFellow("treatmentName");
//		examinerDoctorList = (Listbox)win.getFellow("examinerDoctorList");
		treatmentList = (Listbox)win.getFellow("treatmentList");
		radiograferList = (Listbox)win.getFellow("anasthesyList");
		
		tclassList = (Listbox)win.getFellow("tclassList");
		
		examinerDoctor = (Bandbox)win.getFellow("examinerDoctor");
		code = (Textbox)win.getFellow("code");
		name = (Textbox)win.getFellow("name");
		
		decbox = new Decimalbox();
		decbox.setFormat("#,##0.##");
		
		this.win = win;
		
		session = win.getDesktop().getSession();
		
		listbox = (Listbox)session.getAttribute("listbox");
//		Listbox location = (Listbox)session.getAttribute("location");
		
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
		
//		MsUser user = ub.getMsUser();
		
//		Object[] obj;
//		obj = Service.getRadiologyManager().getMsStaffInUnits(user);
//		//obj = user.getMsStaff().getMsStaffInUnits().toArray();
//		if(obj.length == 1){
//			siu = (MsStaffInUnit)obj[0];
//			unit = siu.getId().getMsUnit();
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
//			
//			if(session.getAttribute("isRanap").equals("YES")){
//				//ambil kelas tarif ranap
//				kelasTarif = session.getAttribute("kelasTarif").toString();
//			}else{
//				//default kelas tarif : Kelas II
//				kelasTarif = "KELAS II";
//			}
//			
//		}
		
		kelasTarif = (String)session.getAttribute("kelasTarif");
		treatmentList.getItems().clear();
		getRadiografer();
//		System.out.println("===================");
//		System.out.println(kelasTarif);
//		System.out.println((String)session.getAttribute("kelasTarif"));
//		System.out.println((String)session.getAttribute("ranapKelas"));
//		System.out.println("===================");
		
		//masukkan daftar kelas ke dalam list
		Service.getRadiologyManager().getKelasForSelect(tclassList);
		for(int i = 0; i < tclassList.getItems().size(); i++){
			if(tclassList.getItemAtIndex(i).getLabel().equals(kelasTarif)){
				tclassList.setSelectedIndex(i);
			}
		}

	
	}
	
	public void getTreatmentBaseOnUnitAndClass(MsUnit unit, String kelasTarif) throws VONEAppException{
		treatmentList.getItems().clear();
		
		List list = TreatmentService.getTreatmentManager().getTreatmentByUnitAndClass(unit, kelasTarif,MedisafeConstants.NON_PAKET);
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
	
	public void getTreatment() throws InterruptedException{
		
		/*if(examinerDoctorList.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
			Messagebox.show(MessagesService.getKey("doctor.selectlist.notselected"));
			examinerDoctorList.focus();
			return;
		}*/
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
//			if (examinerDoctorList.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
//				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
//			}else{
//				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName()+"-"+((MsStaff)examinerDoctorList.getSelectedItem()
//					.getValue()).getVStaffName());
//			}
			
			String ket = tfee.getMsTreatment().getVTreatmentName();
			if (examinerDoctor.getAttribute("doctor") instanceof MsStaff)
			{
				ket = ket + "-" + examinerDoctor.getText();
			}

			if (radiograferList.getSelectedItem().getValue() instanceof MsStaff){
				//listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName()+
				MsStaff msStaff = (MsStaff)radiograferList.getSelectedItem().getValue();
				ket = ket + "-"+ msStaff.getVStaffName();
			}
			listcell = new Listcell(ket);
			listcell.setParent(listitem);
			
			listcell = new Listcell("1");
			listcell.setParent(listitem);
			
			listcell = new Listcell("-");
			listcell.setParent(listitem);
			
			Decimalbox dbharga = new Decimalbox();
			dbharga.setFormat("#,##0");
			
			dbharga.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Listcell diskon  = new Listcell();
			diskon.setParent(listitem);
			Decimalbox decimalbox = new Decimalbox();
			decimalbox.setFormat("#,##0");
			
			decimalbox.setValue(new BigDecimal(0));
			decimalbox.setParent(diskon);
			decimalbox.setWidth("50%");
			decimalbox.setFormat("#,###");
			decimalbox.setHeight("14px");
			
			Listbox diskonList = new Listbox();
			diskonList.setParent(diskon);
			diskonList.setWidth("40%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:8pt");
			item = new Listitem();
			item.setValue(MedisafeConstants.RP);
			item.setSelected(true);
			item.setLabel("1. Rp");
			item.setParent(diskonList);
			item = new Listitem();
			item.setValue(MedisafeConstants.PERCENT);
			item.setLabel("2. %");
			item.setParent(diskonList);
			
//			Label finalPrice = new Label();
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Object[] obj = new Object[]{dbharga.getValue(),dbharga.getValue(),"RP",new Integer(1),decimalbox.getValue()};
			listitem.setAttribute("manipulation", obj);
			MsStaff staff = (MsStaff)examinerDoctor.getAttribute("doctor");
			listitem.setAttribute("doctor",staff);
			listitem.setAttribute("radiografer",radiograferList.getSelectedItem().getValue());
			
			decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1),new Short("0")));
			diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1),new Short("0")));
			
			
		}
		
//		Textbox textbox = (Textbox)session.getAttribute("antriKelas");
//		textbox.setText(tclassList.getSelectedItem().getLabel());
		
		this.win.detach();
		MiscTrxController.setFont(treatmentList);
		
	}
	
	public void getOut(){
		session.removeAttribute("listbox");
	}
	
	public void searchTreatment() throws InterruptedException, WrongValueException, VONEAppException{
		kelasTarif = tclassList.getSelectedItem().getLabel();
		Service.getRadiologyManager().searchTreatmentSurgery(this);
		
		//treatmentCode.setValue(null);
		//treatmentName.setValue(null);
		//treatmentCode.focus();
		MiscTrxController.setFont(treatmentList);
	}
	
	
}

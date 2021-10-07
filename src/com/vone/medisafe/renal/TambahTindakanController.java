package com.vone.medisafe.renal;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
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
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.ui.base.BaseController;

public class TambahTindakanController extends BaseController{
	
	public MsStaffInUnit siu;
	public MsTreatmentFee tfee;
	public MsUnit unit;
	
	public Textbox treatmentCode;
	public Textbox treatmentName;
	
	public Listbox terapisList;
	public Listbox treatmentList;
	
	public Decimalbox decbox;
	public Component win;
	
	public Listitem item;
	public Listcell cell;
	
	public Listbox listbox;
	
	public String kelasTarif;
	
	public Session session;
	
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
		terapisList = (Listbox)win.getFellow("examinerDoctorList");
		treatmentList = (Listbox)win.getFellow("treatmentList");
		decbox = new Decimalbox();
		decbox.setFormat("#,##0.##");
		
		this.win = win;
		
		session = win.getDesktop().getSession();
		
		listbox = (Listbox)session.getAttribute("listbox");
		Listbox location = (Listbox)session.getAttribute("location");
		
		super.init(win);
		
		constraint.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		
		UserInfoBean ub = getUserInfoBean();
		if(ub == null){
			Messagebox.show("uib kosong");
			return;
		}
		
		MsUser user = ub.getMsUser();
		
		//Object[] obj = user.getMsStaff().getMsStaffInUnits().toArray(); 
		Object[] obj;
		obj = Service.getRadiologyManager().getMsStaffInUnits(user);

		if(obj.length == 1){
			siu = (MsStaffInUnit)obj[0];
			unit = siu.getId().getMsUnit();
			/*DoctorController.getDoctorBaseOnUnitId2(unit.getNUnitId(), examinerDoctorList);*/
			
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
//			getTreatmentBaseOnUnit(siu.getId().getMsUnit(), kelasTarif);
		}else{
			unit = (MsUnit)location.getSelectedItem().getValue();
//			Messagebox.show()
			/*DoctorController.getDoctorBaseOnUnitId2(unit.getNUnitId(), examinerDoctorList);*/
			
			if(session.getAttribute("isRanap").equals("YES")){
				//ambil kelas tarif ranap
				kelasTarif = session.getAttribute("kelasTarif").toString();
			}else{
				//default kelas tarif : Kelas II
				kelasTarif = "KELAS II";
			}
			
//			getTreatmentBaseOnUnitAndClass(unit, kelasTarif);
		}
		
		//DoctorController.getAllDoctorList(examinerDoctorList); //ambil docter
		getTerapis();
		
		treatmentList.getItems().clear();
	
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
		Decimalbox db = new Decimalbox();
		Decimalbox dbharga = new Decimalbox();
		db.setFormat("#,###.##");
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
			if (terapisList.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			}else{
			listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName()+"-"+((MsStaff)terapisList.getSelectedItem()
					.getValue()).getVStaffName());
			}
			listcell.setParent(listitem);
			
			listcell = new Listcell("1");
			listcell.setParent(listitem);
			
			listcell = new Listcell("-");
			listcell.setParent(listitem);
			
			dbharga.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			harga = new Label(dbharga.getText());
			
			db.setValue(new BigDecimal(harga.getValue()));
			
			listcell = new Listcell(db.getText());
			listcell.setParent(listitem);
			
			Listcell diskon  = new Listcell();
			diskon.setParent(listitem);
			Decimalbox decimalbox = new Decimalbox();
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
			db.setValue(new BigDecimal(harga.getValue()));
			listcell = new Listcell(db.getText());
			listcell.setParent(listitem);
			
			Object[] obj = new Object[]{dbharga.getValue(),dbharga.getValue(),"RP",new Integer(1),decimalbox.getValue()};
			listitem.setAttribute("manipulation", obj);
			listitem.setAttribute("doctor",terapisList.getSelectedItem().getValue());
			
			decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1),new Short("0")));
			diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1),new Short("0")));
			
		}
		this.win.detach();
		MiscTrxController.setFont(treatmentList);
	}
	
	public void getOut(){
		session.removeAttribute("listbox");
	}
	
	public void searchTreatment() throws InterruptedException, WrongValueException, VONEAppException{
		treatmentCode.setValue(null);
		treatmentName.setValue(null);
		treatmentCode.focus();
		Service.getRadiologyManager().searchTreatmentRenal(this);
		
	}
	
	private void getTerapis() throws VONEAppException{
		terapisList.getItems().clear();
		Listitem item = new Listitem();
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(terapisList);
		terapisList.setSelectedItem(terapisList.getItemAtIndex(0));
	
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
			item.setParent(terapisList);
			i++;
		}

	}
}

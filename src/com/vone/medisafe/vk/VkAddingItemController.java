package com.vone.medisafe.vk;

import java.math.BigDecimal;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.ScreenConstant;
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
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.base.BaseController;

public class VkAddingItemController extends BaseController{


	private MsTreatmentFee tfee;
	private MsUnit unit;
	
	Textbox treatmentCode;
	Textbox treatmentName;
	
	Bandbox mainDoctor;
	Textbox code;
	Textbox name;
	Listbox examinerDoctorList;
	
	Bandbox anastesiDoctor;
	Textbox codeAnestesi;
	Textbox nameAnastesi;
	Listbox anasthesyList;
	
	Listbox treatmentList;
	Decimalbox decbox;
	Component win;
	
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	
	String kelasTarif;
	
	//TRANSAKSI WINDOW
	Window transWindow;
	
	
	DoctorManager doctorService = MasterServiceLocator.getDoctorManager();
	
	ZulConstraint constraint = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		
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
		
		mainDoctor = (Bandbox)win.getFellow("mainDoctor");
		code = (Textbox)win.getFellow("code");
		name = (Textbox)win.getFellow("name");
		examinerDoctorList = (Listbox)win.getFellow("examinerDoctorList");
		
		anastesiDoctor = (Bandbox)win.getFellow("anastesiDoctor");
		codeAnestesi = (Textbox)win.getFellow("codeAnestesi");
		nameAnastesi = (Textbox)win.getFellow("nameAnastesi");
		anasthesyList = (Listbox)win.getFellow("anasthesyList");
		
		treatmentList = (Listbox)win.getFellow("treatmentList");
		decbox = new Decimalbox();
		decbox.setFormat("#,##0.##");
		
		this.win = win;
		
	
		
		
		super.init(win);
		
		constraint.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(code, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(name, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(codeAnestesi, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(nameAnastesi, ZulConstraint.UPPER_CASE);
		
		Page page = win.getDesktop().getPage(ScreenConstant.TRANSAKSI_VK);
		transWindow = (Window)page.getFellow("vkTransaction");
		
		kelasTarif = ((Textbox)transWindow.getFellow("tclass")).getValue();
		listbox = (Listbox)transWindow.getFellow("vkList");
		unit = (MsUnit)((Listbox)transWindow.getFellow("locationList")).getSelectedItem().getValue();
		

		
		
		
		
//		doctorService.getDoctorForSelect(examinerDoctorList, MedisafeConstants.GRUP_DOKTER);
//		doctorService.getDoctorForSelect(anasthesyList, MedisafeConstants.GRUP_ANASTESI);
		
		
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
			String staff ="";
			if(!mainDoctor.getText().equals("")){
				staff = staff + mainDoctor.getText() + ";";
			}
			if(!anastesiDoctor.getText().equals("")){
				staff = staff + anastesiDoctor.getText()+";";
			}
			
			if(staff.length() > 0){
				staff = staff.substring(0, staff.length() - 1);
				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName()+"-"+staff);
			}else{
				listcell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			}
			
			
			
			listcell.setParent(listitem);
			
			listcell = new Listcell("1");
			listcell.setParent(listitem);
			
			listcell = new Listcell("-");
			listcell.setParent(listitem);
			
			Decimalbox dbharga = new Decimalbox();
			dbharga.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
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
			

			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			Object[] obj = new Object[]{dbharga.getValue(),dbharga.getValue(),"RP",new Integer(1),decimalbox.getValue()};
			listitem.setAttribute("manipulation", obj);
			
			MsStaff doctorStaff = (MsStaff)mainDoctor.getAttribute("doctor");
			MsStaff anastesiStaff = (MsStaff)anastesiDoctor.getAttribute("doctor");
			
			listitem.setAttribute("doctor",doctorStaff);
			listitem.setAttribute("anastesi", anastesiStaff);
			
			decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1),new Short("0")));
			diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1),new Short("0")));
			
		}
		this.win.detach();
		MiscTrxController.setFont(treatmentList);
	}
	

	
	
	public void searchTreatment() throws InterruptedException, VONEAppException{
		
		
		TreatmentService.getTreatmentManager().searchTreatment(unit.getNUnitId(), treatmentCode, 
				treatmentName,kelasTarif, MedisafeConstants.NON_PAKET, treatmentList);
		treatmentCode.setValue(null);
		treatmentName.setValue(null);
		treatmentCode.focus();
	}
	
	
	public void destroy(){
		this.win.detach();
	}
}

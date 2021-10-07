package com.vone.medisafe.antrian;

import java.util.List;

import org.hibernate.HibernateException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.base.BaseController;

public class MsAntrianController extends BaseController{
	
	Intbox delayAntrian;
	Textbox textAntrian;
	Button btnSimpan;
	Button btnEdit;
	Listbox doctorList;
	Listbox pasienList;
	
	DoctorManager manager = MasterServiceLocator.getDoctorManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		
		super.init(cmp);
		
		delayAntrian = (Intbox)cmp.getFellow("delayAntrian");
		textAntrian = (Textbox)cmp.getFellow("textAntrian");
		btnSimpan = (Button)cmp.getFellow("btnSimpan") ;
		btnEdit = (Button)cmp.getFellow("btnEdit");
		doctorList = (Listbox)cmp.getFellow("doctorList");
		pasienList = (Listbox)cmp.getFellow("pasienList");
		
		manager.getMasterAntrian(delayAntrian, textAntrian, btnSimpan, btnEdit);
		manager.getAntrianDoctorController(doctorList, pasienList);
	}
	
	public void save() throws VONEAppException{
		MsAntrian antrian = (MsAntrian) delayAntrian.getAttribute("antrian");
		if(antrian == null){
			antrian = new MsAntrian();
		}else delayAntrian.removeAttribute("antrian");
		
		antrian.setDelayAntrian(delayAntrian.getValue());
		antrian.setAntrianDokter(textAntrian.getValue());
		manager.saveAntrian(antrian, delayAntrian, textAntrian, btnSimpan, btnEdit);
		
	}
	
	public void edit() throws VONEAppException{
		delayAntrian.setReadonly(false);
		textAntrian.setReadonly(false);
		btnSimpan.setDisabled(false);
		btnEdit.setDisabled(true);
	}
	
	public void changeListPasien() throws HibernateException, VONEAppException{
		
		MsStaff staff = (MsStaff)doctorList.getSelectedItem().getValue();
//		System.out.println(staff.getVStaffName());
		manager.getAntrian(staff, pasienList);
	}
	
	public void takeOutAntrian() throws InterruptedException{
		if(pasienList.getSelectedCount() == 0){
			Messagebox.show("Pilih Data Yang Akan Dikeluarkan Dari Antrian...", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
		else
		{
			TbRegistration reg = (TbRegistration)pasienList.getSelectedItem().getValue();
			MsStaff staff = (MsStaff)doctorList.getSelectedItem().getValue();
			try {
				manager.takeOutFromAntrian(reg, pasienList, pasienList.getSelectedItem());
				manager.getAntrianDoctorController(doctorList, pasienList);
				List<Listitem> items = doctorList.getItems();
				for(Listitem item : items){
					if(((MsStaff)item.getValue()).getNStaffId().intValue() == staff.getNStaffId().intValue()){
						doctorList.setSelectedItem(item);
						manager.getAntrian(staff, pasienList);
					}
				}
			} catch (VONEAppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

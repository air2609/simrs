package com.vone.medisafe.ui.common;


import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.HallManager;
import com.vone.medisafe.service.iface.master.PatientManager;
import com.vone.medisafe.ui.base.BaseController;

public class PencarianPasientRanapController extends BaseController{
	
	public Textbox noMR;
	
	public Textbox name;
	
	public Textbox address;
	
	public Bandbox hall;
	
	public Textbox code;
	
	public Textbox hallName;
	
	public Textbox doctor;
	
	public Listbox hallList;
	
	public Listbox patList;
	
	public MsHall msHall;
	
	
	
	HallManager halService = MasterServiceLocator.getHallManager();
	PatientManager patService = MasterServiceLocator.getPatientManager();
	
	ZulConstraint constraints = new ZulConstraint();

	@Override
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}

	
	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		noMR = (Textbox)win.getFellow("noMR");
		name = (Textbox)win.getFellow("name");
		address = (Textbox)win.getFellow("address");
		hall = (Bandbox)win.getFellow("hall");
		code = (Textbox)win.getFellow("code");
		hallName = (Textbox)win.getFellow("hallName");
		hallList = (Listbox)win.getFellow("hallList");
		patList = (Listbox)win.getFellow("patList");
		doctor = (Textbox)win.getFellow("doctor");
		
		
		constraints.registerComponent(noMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(address,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(hall, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(hallName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(doctor, ZulConstraint.UPPER_CASE);
		
		hallList.getItems().clear();
		patList.getItems().clear();
	
	}
	
	
	
	public void searchHall() throws VONEAppException{
		
		
		
		halService.searchHall(this);
		MiscTrxController.setFont(hallList);
	}
	
	
	
	public void getHallName() throws VONEAppException{
		
		msHall = (MsHall) hallList.getSelectedItem().getValue();
		hall.setValue(msHall.getVHallName());
	}
	
	
	
	
	public void searchRanapPatient() throws VONEAppException
	{
		
		patList.getItems().clear();
		Listitem item;
		Listcell cell;
		
		List list = patService.searchPatient("%"+noMR.getText()+"%", "%"+name.getText()+"%",
						"%"+address.getText()+"%", "%"+hall.getText()+"%", "%"+doctor.getText()+"%"); 
		
		Object[] obj = null;
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			obj = (Object[])it.next();
			
			item = new Listitem();
			item.setParent(patList);
			
			cell = new Listcell(obj[0].toString());
			cell.setParent(item);
			
			cell = new Listcell(obj[1].toString());
			cell.setParent(item);
			
			String pasien = "NON BPJS";
			Integer tipePasein = (Integer)obj[2];
			if(tipePasein != null && tipePasein.intValue() == 8)
				pasien = "BPJS";
			cell = new Listcell(pasien);
			cell.setParent(item);
			
			cell = new Listcell(obj[3].toString());
			cell.setParent(item);
			
			cell = new Listcell(obj[4].toString());
			cell.setParent(item);
			
			cell = new Listcell(obj[5].toString());
			cell.setParent(item);
			
			cell = new Listcell(obj[6].toString());
			cell.setParent(item);
			
			cell = new Listcell(obj[7].toString());
			cell.setParent(item);
		}
		MiscTrxController.setFont(patList);
	}
	
	
	public void clear() throws VONEAppException{
		
		
		noMR.setValue(null);
		name.setValue(null);
		address.setValue(null);
		hall.setValue(null);
		code.setValue(null);
		hallName.setValue(null);
		doctor.setValue(null);
		hallList.getItems().clear();
		patList.getItems().clear();
		
	}
	

}

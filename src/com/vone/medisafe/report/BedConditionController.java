package com.vone.medisafe.report;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.BedManager;
import com.vone.medisafe.ui.base.BaseController;


public class BedConditionController extends BaseController{
	
	Listbox listBed;
	BedManager manager = MasterServiceLocator.getBedManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		listBed = (Listbox)cmp.getFellow("bedWindows").getFellow("listBed");
		manager.getBedByClass(listBed);
	}
	
}

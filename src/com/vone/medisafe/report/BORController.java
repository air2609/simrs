package com.vone.medisafe.report;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.BedManager;
import com.vone.medisafe.ui.base.BaseController;

public class BORController  extends BaseController{
	
	
	Datebox startDate;
	Datebox endDate;
	Listbox listBor;
	Listhead listhead;
	
	BedManager manager = MasterServiceLocator.getBedManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		super.init(cmp);
		
		startDate = (Datebox)cmp.getFellow("startDate");
		endDate = (Datebox)cmp.getFellow("endDate");
		listBor = (Listbox)cmp.getFellow("bedWindows").getFellow("listBor");
		listhead = (Listhead)listBor.getListhead();
		
		manager.getBedByClass(listhead);
		
	}

}

package com.vone.medisafe.laborat;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class PanelLabController extends BaseController{
	
	Component win;
	Session session;
	Listbox listbox;
	
	String kelasTarif;
	
	Listitem item;
	Listcell cell;
	Decimalbox total;
		
	public void init(Component win) throws InterruptedException, VONEAppException {
		super.init(win);
		
		this.win = win;
		
		session = win.getDesktop().getSession();
		kelasTarif = (String)session.getAttribute("kelasTarif");
		
		listbox = (Listbox)session.getAttribute("listbox");
	}
	
	private void getItemsFromList(String list)throws InterruptedException, VONEAppException {
		
		LaboratManager labManager = MasterServiceLocator.getLaboratManager();
		labManager.getItemsFromList(this, list);
		
	}
	
	public void getItems() throws InterruptedException, VONEAppException {
		//mengambil item yg terpilih
		getItemsFromList("hematologiList");
		getItemsFromList("kimiaList");
		getItemsFromList("imunoSerologiLists");
		getItemsFromList("transudatList");
		getItemsFromList("microBiologyList");
		getItemsFromList("pregnancyTestList");
		
		getItemsFromList("elektrolitLists");
		getItemsFromList("jantungList");
		getItemsFromList("feacesLists");
		getItemsFromList("lainLainList");
		
		getItemsFromList("lcsList");
		getItemsFromList("bloodBankList");
		getItemsFromList("narkobaList");
		getItemsFromList("urineList");
	}

}

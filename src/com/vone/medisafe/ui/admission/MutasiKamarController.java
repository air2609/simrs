package com.vone.medisafe.ui.admission;



import org.zkoss.zhtml.Table;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admission.MutasiKamarManager;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;
import com.vone.medisafe.ui.base.BaseController;


public class MutasiKamarController extends BaseController{
	
	public Listbox patientList;
	public Listbox antriKelasList;
	public Listbox antrianKelasList;
	public Listbox availableBedList;
	public Listbox bedMutasiList;
	public Bandbox mutasiBed;
	public Bandbox ruangan;
	
	public Bandbox noMR;
	public Textbox crNoMR;
	public Textbox crNama;
	public Textbox crNoAlamat;
	public Textbox namaPasien;
	public Textbox noRegistrasi;
	
	public Table mutasiTable;
	
	public Button btnNew;
	public Button btnModify;
	public Button btnSave;
	
	public Window win;
	
	private ZulConstraint constraints = new ZulConstraint();
	
	TreatmentClassManager tclassService = MasterServiceLocator.getTreatmentClassManager();
	MutasiKamarManager service = AdmissionServiceLocator.getMutasiKamarManager();
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		patientList = (Listbox)win.getFellow("patientList");
		antriKelasList = (Listbox)win.getFellow("antriKelasList");
		antrianKelasList = (Listbox)win.getFellow("antrianKelasList");
		availableBedList = (Listbox)win.getFellow("availableBedList");
		bedMutasiList = (Listbox)win.getFellow("bedMutasiList");
		ruangan = (Bandbox)win.getFellow("ruangan");
		mutasiBed = (Bandbox)win.getFellow("mutasiBed");
		
		noMR = (Bandbox)win.getFellow("noMR");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		namaPasien = (Textbox)win.getFellow("namaPasien");
		noRegistrasi = (Textbox)win.getFellow("noRegistrasi");
		
		mutasiTable = (Table)win.getFellow("mutasiTable"); 
		
		btnNew = (Button)win.getFellow("btnNew");
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnUbah");
		
		super.init(win);
		
		constraints.registerComponent(crNoMR,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat,ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(noMR, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(mutasiBed, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
		namaPasien.setReadonly(true);
		noRegistrasi.setDisabled(true);
		btnNew.setDisabled(true);
		btnModify.setDisabled(true);
		
		availableBedList.getItems().clear();
		bedMutasiList.getItems().clear();
		patientList.getItems().clear();
		
		tclassService.getTClassForSelect(antrianKelasList);
		tclassService.getTClassForSelect(antriKelasList);
		
		this.win = (Window)win;
	
		
		noMR.focus();
	}


	public void getPatientDetail(Window win, int type) throws InterruptedException, VONEAppException{
		
		service.getPatientRanapDetil(this,type);
		
	}
	
	public void save(Window win) throws InterruptedException, VONEAppException{
		
		
		
		if(!constraints.validateComponent(true))return;
		
		service.save(this);
		
		
	}
	
	
	
	public void clear(Window win){
		
		
		setDisable(win, false);
		
		noMR.setValue(null);
		mutasiBed.setValue(null);
		ruangan.setValue(null);
		namaPasien.setValue(null);
		noRegistrasi.setValue(null);
		bedMutasiList.getItems().clear();
		antrianKelasList.setSelectedItem(antrianKelasList.getItemAtIndex(0));
		antriKelasList.setSelectedItem(antriKelasList.getItemAtIndex(0));
		availableBedList.getItems().clear();
		noMR.focus();
		btnSave.setDisabled(false);
		
		
	}
	
	public void setDisable(Window win, boolean disable){
		
		
		
		
		noMR.setDisabled(disable);
		mutasiBed.setDisabled(disable);
		ruangan.setDisabled(disable);
		antrianKelasList.setDisabled(disable);
		antriKelasList.setDisabled(disable);
		
		
		
	}
	
	public void edit(Window win) throws InterruptedException, VONEAppException{
		
		service.modify(this);
		
		
	}
}

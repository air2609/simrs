package com.vone.medisafe.ui.admission;



import org.zkoss.zhtml.Table;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admission.MedicalRecordManager;
import com.vone.medisafe.service.iface.admission.RanapManager;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;
import com.vone.medisafe.ui.base.BaseController;




public class RanapController extends BaseController{
	
	private ZulConstraint constrainst = new ZulConstraint();
	
	public MsStaff staff;
	
	Listbox antriKelas;
	Listbox kelasTarif;
	Listbox searchList;
	
	Bandbox mainDoctor;
	Textbox code;
	Textbox name;
	Listbox doctorList;
	
	Listbox antriKelasTarif;
	Listbox avaliableBedList;
	Bandbox bed;
	
	Button btnNew;
	Button btnCancel;
	Button btnSave;
	
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Textbox tglMasuk;
	Listbox historyRajal;
	Textbox noRegistrasiLama;
	Textbox namaPasien;
	Textbox jlhRanap;
	Textbox noRegistrasiBaru;
	Bandbox noMR;
	Radiogroup jenisKelamin;
	Bandbox ruangan;
	
	Window win;
	
	static RanapManager regServ = AdmissionServiceLocator.getRegistrationManager();
	static MedicalRecordManager mrServ = AdmissionServiceLocator.getMedicalRecordManager();
	
	TreatmentClassManager tclassService = MasterServiceLocator.getTreatmentClassManager();
	DoctorManager doctorService = MasterServiceLocator.getDoctorManager();



	public void init(Component win) throws InterruptedException, VONEAppException{		
		
		antriKelas = (Listbox)win.getFellow("antriKelas");
		kelasTarif = (Listbox)win.getFellow("kelasTarif");
		searchList = (Listbox)win.getFellow("patientSearchList");
		
		mainDoctor = (Bandbox)win.getFellow("mainDoctor");
		code = (Textbox)win.getFellow("code");
		name = (Textbox)win.getFellow("name");
		doctorList = (Listbox)win.getFellow("doctorList");
		
		antriKelasTarif = (Listbox)win.getFellow("antriKelasTarif");
		avaliableBedList = (Listbox)win.getFellow("avaliableBedList");
		bed = (Bandbox)win.getFellow("bed"); 
		
		btnNew = (Button)win.getFellow("btnNew");
		btnCancel = (Button)win.getFellow("btnCancel");
		btnSave = (Button)win.getFellow("btnSave");
		
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		tglMasuk = (Textbox)win.getFellow("tglMasuk");
		historyRajal = (Listbox)win.getFellow("historyRajal");
		noRegistrasiLama = (Textbox)win.getFellow("noRegistrasiLama");
		namaPasien = (Textbox)win.getFellow("namaPasien");
		jlhRanap = (Textbox)win.getFellow("jlhRanap");
		noRegistrasiBaru = (Textbox)win.getFellow("noRegistrasiBaru");
		noMR = (Bandbox)win.getFellow("noMR");
		jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		ruangan = (Bandbox)win.getFellow("ruangan");
		
		super.init(win);
		
		win = (Window)win;
		

		
		constrainst.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constrainst.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constrainst.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		constrainst.registerComponent(code, ZulConstraint.UPPER_CASE);
		constrainst.registerComponent(name, ZulConstraint.UPPER_CASE);
		
		constrainst.registerComponent(noMR, ZulConstraint.NO_EMPTY);
		constrainst.registerComponent(noRegistrasiLama, ZulConstraint.NO_EMPTY);
		constrainst.registerComponent(bed, ZulConstraint.NO_EMPTY);
		constrainst.registerComponent(mainDoctor, ZulConstraint.NO_EMPTY);
		
		
		constrainst.validateComponent(false);
		
		tglMasuk.setReadonly(true);

		noRegistrasiLama.setReadonly(true);
		namaPasien.setReadonly(true);
		jlhRanap.setReadonly(true);
		noRegistrasiBaru.setReadonly(true);

		noMR.focus();
		
		avaliableBedList.getItems().clear();
		
		tclassService.getTClassForSelect(antriKelas);
		tclassService.getTClassForSelect(kelasTarif);
		tclassService.getTClassForSelect(antriKelasTarif);
		
//		doctorService.getDoctorForSelect(mainDoctor, MedisafeConstants.GRUP_DOKTER);
		searchList.getItems().clear();
		
		
	}
	
	public void getHallList(Listbox kelasTarif, Listbox bedList,Listbox searchKelasTarif) 
		throws InterruptedException, VONEAppException{
		bedList.getItems().clear();
		
		if(kelasTarif.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			getListHallByTClassId(((MsTreatmentClass)kelasTarif.getSelectedItem().getValue()).getNTclassId(),bedList, searchKelasTarif);
		}
		
	}
	
	public void getBedBaseOnHall(Listbox avaliableBedList, Table bedTable, Bandbox ruangan,Bandbox bbbed) throws VONEAppException{
		
		regServ.getBedsBaseOnHall(avaliableBedList, bedTable,ruangan,bbbed);
		
		
	}
	
	public void getPatientDetail(Window win, int type) throws InterruptedException,VONEAppException{
		
		regServ.getPatientDetil(win, type);
		
		mainDoctor.focus();
				
		searchList.clearSelection();

	}
	
	public void save(Component win) throws InterruptedException, VONEAppException{
		
		if(!constrainst.validateComponent(true))return;
		
		regServ.saveRanap(win);
		
		setDisable(win, true);

		btnSave.setDisabled(true);
		btnCancel.setDisabled(false);

		
	}
	
	
	public void createNew(Window win){
		
			
		antriKelas.setSelectedItem(antriKelas.getItemAtIndex(0));
		kelasTarif.setSelectedItem(kelasTarif.getItemAtIndex(0));
		mainDoctor.setValue(null);
		mainDoctor.removeAttribute("doctor");
		antriKelasTarif.setSelectedItem(antriKelasTarif.getItemAtIndex(0));
		
		setDisable(win, false);
		
		noMR.setValue(null);
		noRegistrasiLama.setValue(null);
		namaPasien.setValue(null);
		tglMasuk.setValue(null);
		jlhRanap.setValue(null);
		ruangan.setValue(null);
		bed.setValue(null);
		noRegistrasiBaru.setValue(null);
		btnSave.setDisabled(false);
		btnCancel.setDisabled(true);
		code.setValue(null);
		name.setValue(null);
		doctorList.getItems().clear();
		this.historyRajal.getItems().clear();
		noMR.focus();
		
	
		
	}
	
	public void getListHallByTClassId(Integer id, Listbox antriKelasTarif, Listbox searchKelas) 
		throws InterruptedException, VONEAppException{
		
		regServ.getHallListByTclassId(id,antriKelasTarif,searchKelas);
		
	}
	
	public void setDisable(Component win, boolean disable){
				
		noMR.setDisabled(disable);
		mainDoctor.setDisabled(disable);
		bed.setDisabled(disable);
		antriKelas.setDisabled(disable);
		kelasTarif.setDisabled(disable);
		ruangan.setDisabled(disable);
		
	}
	
	
			
	
	public void cancelRegistration() throws VONEAppException, InterruptedException
	{
		int confirm = Messagebox.show(MessagesService.getKey("konfirmasi.batal.rajal"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		if(regServ.cancelRanapRegistration(this.bed, this.noRegistrasiBaru, this.noMR)){
			
			Messagebox.show(MessagesService.getKey("batal.registrasi.sukses"));
			createNew(win);
			
		}
		
		else 
			Messagebox.show(MessagesService.getKey("batal.registrasi.gagal"));
	}
	
	
	
	public void searchDoctor() throws VONEAppException{
		
		doctorService.searchDoctor(this.code, this.name,this.doctorList, MedisafeConstants.GRUP_DOKTER);
	}
	
	
	
	public void getMsStaff() throws VONEAppException{
		
		staff = (MsStaff)doctorList.getSelectedItem().getValue();
		mainDoctor.setValue(staff.getVStaffName());
		mainDoctor.setAttribute("doctor", staff);
		
	}
}

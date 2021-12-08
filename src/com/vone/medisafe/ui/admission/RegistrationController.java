package com.vone.medisafe.ui.admission;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsRegency;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admission.RajalManager;
import com.vone.medisafe.service.iface.master.PatientManager;
import com.vone.medisafe.service.iface.master.PatientTypeManager;
import com.vone.medisafe.service.iface.master.ProvinceManager;
import com.vone.medisafe.service.iface.master.RegenyManager;
import com.vone.medisafe.service.iface.master.SubDstrictManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.service.iface.master.VillageManager;
import com.vone.medisafe.ui.base.BaseController;


public class RegistrationController extends BaseController {
	

	public MsPatient patient;
	private boolean isOldPatient;
	
	Bandbox noMr;
	Button btnSave;
	Button btnCancel;
	Button btnOld;
	Button btnNew;
	Listbox unitList;
	Textbox noRegistrasi;
	Textbox namaPasien;	
	Radiogroup jenisKelamin;		
	Datebox tglLahir;
	Textbox umur;
	Listbox religionList;
	Listbox wargaNegaraList;		
	Listbox statusKawinList;
	Textbox alamat;
	Listbox kelurahanList;
	Listbox kecamatanList;
	Listbox kabupatenList;
	Listbox propinsiList;
	Textbox noTelp;
	Textbox alamatAlternatif;
	Textbox rt;
	Textbox rw;
	Textbox rw1;
	Textbox rt1;
	Textbox noTelpAlt;
	Listbox pendidikanList;
	Listbox	jenisPekerjaanList;
	Listbox tipePasienList;
	Listbox prioritasPasienList;
	Listbox dokterPemeriksaList;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox patientSearchList;
	Listbox etnisList;
	Listbox languageList;
	Textbox nik;
//	Textbox namaIbu;
//	Textbox namaPasangan;
	
	Window win;
	
	ZulConstraint constraints = new ZulConstraint();
	
	
	
	RajalManager service = AdmissionServiceLocator.getRajalManager();
	
	UnitManager unitService = MasterServiceLocator.getUnitManager();
	PatientTypeManager patientTypeService = MasterServiceLocator.getPatientTypeManager();
	ProvinceManager provinceService = MasterServiceLocator.getProvinceManager();
	RegenyManager regenceyService = MasterServiceLocator.getRegencyManager();
	SubDstrictManager subdistrictService = MasterServiceLocator.getSubDistrictManager();
	VillageManager villageService = MasterServiceLocator.getVillageManager();
	PatientManager patientService = MasterServiceLocator.getPatientManager();
	

		
	public void init(Component win) throws InterruptedException, VONEAppException{
		
		super.init(win);
		
		
		kelurahanList = (Listbox)win.getFellow("kelurahanList");
		kecamatanList = (Listbox)win.getFellow("kecamatanList");
		kabupatenList = (Listbox)win.getFellow("kabupatenList");
		propinsiList = (Listbox)win.getFellow("propinsiList");
		tipePasienList = (Listbox)win.getFellow("tipePasienList");
		tglLahir = (Datebox)win.getFellow("tglLahir");
		btnNew = (Button)win.getFellow("btnNew");
		unitList = (Listbox)win.getFellow("unitList");
		noRegistrasi = (Textbox)win.getFellow("noRegistrasi");
		patientSearchList = (Listbox)win.getFellow("patientSearchList");
		jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		namaPasien = (Textbox)win.getFellow("namaPasien");
		alamat = (Textbox)win.getFellow("alamat");
		dokterPemeriksaList = (Listbox)win.getFellow("dokterPemeriksaList");
		noMr = (Bandbox) win.getFellow("noMR");
		alamatAlternatif = (Textbox)win.getFellow("alamatAlternatif");
		nik = (Textbox)win.getFellow("nik");
//		namaIbu = (Textbox)win.getFellow("namaIbu");
//		namaPasangan = (Textbox)win.getFellow("namaPasangan");
		
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		btnSave = (Button)win.getFellow("btnSave");
		btnCancel = (Button)win.getFellow("btnCancel");
		btnOld = (Button)win.getFellow("btnOld");
		
		
		umur = (Textbox)win.getFellow("umur");
		religionList = (Listbox)win.getFellow("religionList");
		wargaNegaraList = (Listbox)win.getFellow("wargaNegaraList");		
		statusKawinList = (Listbox)win.getFellow("statusKawinList");
		noTelp = (Textbox)win.getFellow("noTelp");
		
		rt = (Textbox)win.getFellow("rt");
		rw = (Textbox)win.getFellow("rw");
		rw1 = (Textbox)win.getFellow("rw1");
		rt1 = (Textbox)win.getFellow("rt1");
		noTelpAlt = (Textbox)win.getFellow("noTelpAlt");
		pendidikanList = (Listbox)win.getFellow("pendidikanList");
		jenisPekerjaanList = (Listbox)win.getFellow("jenisPekerjaanList");
		prioritasPasienList = (Listbox)win.getFellow("prioritasPasienList");
		etnisList = (Listbox)win.getFellow("etnisList");
		languageList = (Listbox) win.getFellow("languageList");
		
		this.win = (Window) win;
		
//		Messagebox.show("perubahan jadi");
//		user = getUserInfoBean().getMsUser();
		
		constraints.registerComponent(nik, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(tglLahir,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaPasien, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaPasien, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(alamat, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(alamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(unitList, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(dokterPemeriksaList, ZulConstraint.NO_EMPTY);
			
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(alamatAlternatif, ZulConstraint.UPPER_CASE);
		
//		constraints.registerComponent(namaIbu, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
//		unitService.getMsUnitForSelect(unitList);
		unitService.getRegistrationUnit(unitList);
		patientTypeService.getPatientTypeForSelect(tipePasienList);
		provinceService.getProvinceForSelect(propinsiList);
//		regenceyService.getRegencyForSelect(kabupatenList);
//		subdistrictService.getSubDistrictForSelect(kecamatanList);
//		villageService.getVillageForSelect(kelurahanList);
		
		patientSearchList.getItems().clear();
		
		jenisKelamin.setSelectedIndex(0);
		noRegistrasi.setReadonly(true);
		btnNew.setDisabled(true);
		noMr.setVisible(false);
		
		
		
		dokterPemeriksaList.getItems().clear();
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(dokterPemeriksaList);
		dokterPemeriksaList.setSelectedItem(item);
		
	}
	
	public void getRegencey() throws VONEAppException {
		regenceyService.getRegencyBaseOnProvince(kabupatenList, propinsiList);
	}
	
	public void getSubdistrict() throws VONEAppException {
		subdistrictService.getSubDistrictByRegency(kecamatanList, kabupatenList);
	}
	
	public void getVillage() throws VONEAppException {
		villageService.getVillageBySubdistrict(kelurahanList, kecamatanList);
	}
	
	public void save(Window win) throws InterruptedException, VONEAppException{
		
		if(!constraints.validateComponent(true))
		{
			if(noMr.isVisible())
				noMr.focus();
			else namaPasien.focus();
			return;
		}
		if(btnNew.isDisabled()){
			isOldPatient = false;
			saveAll(win);
			

		}
		else{
			isOldPatient = true;
			saveRegistrationOnly(win);
			
		}

	}
	
	public void printStciker() {
		String ipTarget = this.getCurrentSession().getClientAddr();
		StringBuffer sb = new StringBuffer();
		
		if(!noMr.getText().equals("")) {
			//noMR;patientName;patientGender;birthDate;
			sb.append(noMr.getText() + ";");
			sb.append(namaPasien.getText() + ";");
			if(jenisKelamin.getSelectedItem().getValue().equals("M"))
				sb.append("L" + ";");
			else sb.append("P" + ";");
			sb.append(tglLahir.getText());
			
			PrintClient.printStringBuffer(ipTarget, sb);
		}
	}
	
	public void setDisable(boolean isDisable){
		

		
		noMr.setVisible(isDisable);	
		noMr.setReadonly(isDisable);
		noRegistrasi.setReadonly(isDisable);
		namaPasien.setReadonly(isDisable);	
		tglLahir.setDisabled(isDisable);
		religionList.setDisabled(isDisable);
		wargaNegaraList.setDisabled(isDisable);		
		statusKawinList.setDisabled(isDisable);
		alamat.setReadonly(isDisable);
//		kelurahanList.setDisabled(isDisable);
//		kecamatanList.setDisabled(isDisable);
//		kabupatenList.setDisabled(isDisable);
//		propinsiList.setDisabled(isDisable);
		noTelp.setReadonly(isDisable);
		alamatAlternatif.setReadonly(isDisable);
		rt.setReadonly(isDisable);
		rw.setReadonly(isDisable);
		rw1.setReadonly(isDisable);
		rt1.setReadonly(isDisable);
		noTelpAlt.setReadonly(isDisable);
		pendidikanList.setDisabled(isDisable);
		jenisPekerjaanList.setDisabled(isDisable);
//		tipePasienList.setDisabled(isDisable);
		prioritasPasienList.setDisabled(isDisable);
		unitList.setDisabled(isDisable);
		umur.setReadonly(isDisable);
		dokterPemeriksaList.setDisabled(isDisable);
	}
	
	private void saveAll(Window win) throws InterruptedException, VONEAppException{
		
		List patients = patientService.getPatientByNik(nik.getValue().trim());
		if(patients.size() > 0) {
			int result = Messagebox.show("No NIK ini telah terdaftar apakah ingin melanjutkan?", 
					"KONFIRMASI", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
			if(result == Messagebox.NO) return;
		}
		
		
		if(service.savePatientAndRegistration(win)){
			
			btnNew.setDisabled(false);
			btnSave.setDisabled(true);
			btnCancel.setDisabled(false);
			btnNew.setDisabled(false);
			btnOld.setDisabled(false);
			setDisable(true);
			
			Messagebox.show(MessagesService.getKey("admission.registration.success"));
		}
		else Messagebox.show(MessagesService.getKey("admission.registration.success"));
		
		

		
	}
	
	private void saveRegistrationOnly(Window win) throws InterruptedException, VONEAppException{
		Map<String, Object> patients = new HashMap<>();
		patients.put("tipePasien", tipePasienList);
		patients.put("etnis", etnisList);
		patients.put("bahasa", languageList);
		patients.put("propinsi", propinsiList);
		patients.put("kabupaten", kabupatenList);
		patients.put("kecamatan", kecamatanList);
		patients.put("kelurahan", kelurahanList);
		patients.put("nik", nik);
		
		if(service.saveRegistrationOnly(noMr, dokterPemeriksaList, unitList, noRegistrasi, patients)){

			btnNew.setDisabled(false);
			btnOld.setDisabled(false);
			btnSave.setDisabled(true);
			btnCancel.setDisabled(false);

			setDisable(true);
			Messagebox.show(MessagesService.getKey("admission.registration.success"));

		}	
		
		else {
			Messagebox.show(MessagesService.getKey("admission.registration.fail"));
		}
		
				
		
	}
	
	public void createNewRegistration(Component win, int type){
		
		setDisable(false);
		
		if(type == MedisafeConstants.PASIEN_BARU){
			noMr.setVisible(false);
			btnNew.setDisabled(true);
			umur.setReadonly(false);
			namaPasien.setReadonly(false);
			namaPasien.focus();
			btnOld.setDisabled(false);
			btnCancel.setDisabled(true);
		}
		else if(type == MedisafeConstants.PASIEN_LAMA){
			noMr.setVisible(true);
			noMr.setDisabled(false);
			noMr.focus();
			btnOld.setDisabled(true);
			btnNew.setDisabled(false);
			btnCancel.setDisabled(true);
		}
		else{
			btnCancel.setDisabled(true);
		}

		
		
		btnSave.setDisabled(false);
		
		
		noMr.setValue(null);
		noMr.removeAttribute("registration");
		noRegistrasi.setValue(null);
		namaPasien.setValue(null);
		jenisKelamin.setSelectedIndex(0);
		tglLahir.setValue(null);
		umur.setValue(null);
		nik.setValue(null);
		religionList.setSelectedItem(religionList.getItemAtIndex(0));
		wargaNegaraList.setSelectedItem(wargaNegaraList.getItemAtIndex(0));
		statusKawinList.setSelectedItem(statusKawinList.getItemAtIndex(0));
		alamat.setValue(null);
		if(kelurahanList.getItems().size() > 0) kelurahanList.setSelectedItem(kelurahanList.getItemAtIndex(0));
		if(kecamatanList.getItems().size() > 0) kecamatanList.setSelectedItem(kecamatanList.getItemAtIndex(0));
		if(kecamatanList.getItems().size() > 0) kabupatenList.setSelectedItem(kabupatenList.getItemAtIndex(0));
		if(propinsiList.getItems().size() > 0 ) propinsiList.setSelectedItem(propinsiList.getItemAtIndex(0));
		noTelp.setValue(null);
		alamatAlternatif.setValue(null);
		rt.setValue(null);
		rw.setValue(null);
		rt1.setValue(null);
		rw1.setValue(null);
		noTelpAlt.setValue(null);
		pendidikanList.setSelectedItem(pendidikanList.getItemAtIndex(0));
		jenisPekerjaanList.setSelectedItem(jenisPekerjaanList.getItemAtIndex(0));
		tipePasienList.setSelectedItem(tipePasienList.getItemAtIndex(0));
		prioritasPasienList.setSelectedItem(prioritasPasienList.getItemAtIndex(0));
		unitList.setSelectedItem(unitList.getItemAtIndex(0));
		dokterPemeriksaList.setSelectedIndex(0);
//		namaIbu.setText(null);
//		namaPasangan.setText(null);
		
	}
	
	
	
	public void getPatientDetail(int input) throws VONEAppException, InterruptedException{
		String code = null;
		
		if(input == MedisafeConstants.INPUT_BY_MANUAL){
			code = MedisafeUtil.convertToMrCode(noMr.getValue());
		}else{
			code = patientSearchList.getSelectedItem().getLabel();
		}
	
		service.getPatientDetail(this.win, code);
				
				
		patientSearchList.clearSelection();
		
		
		unitList.focus();
		
		setDisable(true);
		this.noMr.setDisabled(false);
		unitList.setDisabled(false);
		dokterPemeriksaList.setDisabled(false);
		
		//check active registration
		if(noRegistrasi.getText().length() > 1){
			this.btnCancel.setDisabled(false);
		}
		
		
	}
	
	
	
	
	public static void generateAge(Textbox umur, Datebox tglLahir){
		MedisafeUtil.generateAge(umur, tglLahir);
	
	}
	
	public void cetakKartu() throws VONEAppException, InterruptedException{
		

		Map<String, String> parameters = new HashMap<String, String>(); 
		parameters.put("parMr", this.noMr.getText());
		parameters.put("parNama", this.namaPasien.getText());
		parameters.put("parAlamat", this.alamat.getText());
		
		if(this.kabupatenList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			MsRegency kab = (MsRegency)this.kabupatenList.getSelectedItem().getValue();
			parameters.put("parKota", kab.getVRegencyName());
		}else
			parameters.put("parKota", "");
		
		
		ReportEngine re = new ReportEngine("select * from ms_patient limit 1", 
				ReportService.getKey("kartu.pasien"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
		
		
		
	}
	
	
	public void cancelRegistration() throws VONEAppException, InterruptedException{
		
		int confirm = Messagebox.show(MessagesService.getKey("konfirmasi.batal.rajal"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		
		
		if(service.cancelRegistration(this.noMr)){
			
			Messagebox.show(MessagesService.getKey("batal.registrasi.sukses"));
			
			if(isOldPatient)
				createNewRegistration(win, MedisafeConstants.PASIEN_LAMA);
			else createNewRegistration(win, MedisafeConstants.PASIEN_BARU);
			
		}
		
		else Messagebox.show(MessagesService.getKey("batal.registrasi.gagal"));
		
	}
	
	
}

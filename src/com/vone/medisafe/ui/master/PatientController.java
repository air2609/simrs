package com.vone.medisafe.ui.master;



import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsProvince;
import com.vone.medisafe.mapping.MsRegency;
import com.vone.medisafe.mapping.MsSubDistrict;
import com.vone.medisafe.mapping.MsVillage;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.PatientManager;
import com.vone.medisafe.ui.base.BaseController;


/**
 * PatientController.java
 * @author Arifullah Ibn Rusyd
 * @version 0.1
 * @since 29-07-2006
 * used to controll all data flow from.zul file related to patient data
 */

public class PatientController extends BaseController{
	
	private TbMedicalRecord mr;
	
	Listbox kelurahanList;
	Listbox kecamatanList;
	Listbox kabupatenList;
	Listbox propinsiList;
	
	Button btnSave;
	Button btnCancel;
	Button btnModify;
	Listbox tipePasienList;
	Datebox tglLahir;
	Radiogroup jenisKelamin;
	Textbox namaPasien;
	Textbox umur;
	Textbox alamat;
	Bandbox noMr;
	Listbox religionList;
	Listbox wargaNegaraList;		
	Listbox statusKawinList;
	Textbox noTelp;
	Textbox alamatAlternatif;
	Textbox rt;
	Textbox rw;
	Textbox rw1;
	Textbox rt1;
	Textbox noTelpAlt;
	Listbox pendidikanList;
	Listbox	jenisPekerjaanList;
	Listbox prioritasPasienList;
	
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	
	Window win;
	
	Listbox patientSearchList;

	ZulConstraint constraints = new ZulConstraint();

	private static PatientManager serv = MasterServiceLocator.getPatientManager();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		
		super.doCancel(win);
		setFieldDisable(false);
		
		kelurahanList.setSelectedIndex(0);
		kecamatanList.setSelectedIndex(0);
		kabupatenList.setSelectedIndex(0);
		propinsiList.setSelectedIndex(0);
		tipePasienList.setSelectedIndex(0);
		tglLahir.setValue(null);
		namaPasien.setValue(null);
		alamat.setValue(null);
		noMr.setValue(null);
		umur.setValue(null);
		religionList.setSelectedIndex(0);
		wargaNegaraList.setSelectedIndex(0);
		statusKawinList.setSelectedIndex(0);
		noTelp.setValue(null);
		alamatAlternatif.setValue(null);
		rt.setValue(null);
		rw.setValue(null);
		rw1.setValue(null);
		rt1.setValue(null);
		noTelpAlt.setValue(null);
		pendidikanList.setSelectedIndex(0);
		jenisPekerjaanList.setSelectedIndex(0);
		prioritasPasienList.setSelectedIndex(0);
		noMr.focus();
		btnCancel.setDisabled(false);
		btnModify.setDisabled(true);
		
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
			

		super.doSaveAdd(win);
		
		MsPatientType tipePasien;MsVillage village;MsProvince province;MsRegency regency;MsSubDistrict subdistric;MsPatient patient = new MsPatient();
		if(tipePasienList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			//tipePasien = MasterServiceLocator.getPatientTypeManager().getById((Integer)tipePasienList.getSelectedItem().getValue());
			tipePasien = (MsPatientType)tipePasienList.getSelectedItem().getValue(); 
			patient.setMsPatientType(tipePasien);
		}
		if(kelurahanList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
//			village = MasterServiceLocator.getVillageManager().getVillageById((Integer)kelurahanList.getSelectedItem().getValue());
			village = (MsVillage)kelurahanList.getSelectedItem().getValue();
			patient.setMsVillage(village);
		}
		if(propinsiList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
//			province = MasterServiceLocator.getProvinceManager().getProvinceById((Integer)propinsiList.getSelectedItem().getValue());
			province = (MsProvince)propinsiList.getSelectedItem().getValue();
			patient.setMsProvince(province);
		}
		if(kabupatenList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
//			regency = MasterServiceLocator.getRegencyManager().findById((Integer)kabupatenList.getSelectedItem().getValue());
			regency = (MsRegency) kabupatenList.getSelectedItem().getValue();
			patient.setMsRegency(regency);
		}
		if(kecamatanList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
//			subdistric = MasterServiceLocator.getSubDistrictManager().getSubDistrictbyId((Integer)kecamatanList.getSelectedItem().getValue());
			subdistric = (MsSubDistrict)kecamatanList.getSelectedItem().getValue();
			patient.setMsSubDistrict(subdistric);
		}
		
		patient.setDPatientDob(tglLahir.getValue());
		if(alamatAlternatif.getValue().trim().length() > 0)patient.setVPatientAltAddr(alamatAlternatif.getValue());
		if(noTelpAlt.getValue().trim().length() > 0)patient.setVPatientAltPhNo(noTelpAlt.getValue());
		if(rt1.getValue().trim().length() > 0 && rw1.getValue().trim().length() > 0)patient.setVPatientAltRtRw(rt1.getValue()+"/"+rw1.getValue());
		patient.setVPatientEdu(pendidikanList.getSelectedItem().getValue().toString());
		patient.setVPatientGender(jenisKelamin.getSelectedItem().getValue());
		if(rt.getValue().trim().length() > 0 && rw.getValue().trim().length() > 0)patient.setVPatientMainRtRw(rt.getValue()+"/"+rw.getValue());
		patient.setVPatientJobType(jenisPekerjaanList.getSelectedItem().getValue().toString());
		patient.setVPatientMainAddr(alamat.getValue());
		if(noTelp.getValue().trim().length() > 0)patient.setVPatientMainPhNo(noTelp.getValue());
		patient.setVPatientMaritalStatus(statusKawinList.getSelectedItem().getValue().toString());
		patient.setVPatientName(namaPasien.getValue());
		patient.setVPatientNationality(wargaNegaraList.getSelectedItem().getValue().toString());
		patient.setVPatientReligion(religionList.getSelectedItem().getValue().toString());
		patient.setVPatientPriority(prioritasPasienList.getSelectedItem().getValue().toString());
		patient.setVWhoCreate(getUserInfoBean().getStUserId());
		patient.setDWhnCreate(new Date());
		
		mr = new TbMedicalRecord();
		int status = 0;
		if(noMr.getText().length() > 0){
			TbMedicalRecord oldMr = AdmissionServiceLocator.getMedicalRecordManager().getMedicalRecord(noMr.getText());
			patient.setNPatientId(oldMr.getMsPatient().getNPatientId());
			mr.setNMrId(oldMr.getNMrId());
			mr.setVMrCode(oldMr.getVMrCode());
			status = 1;
		}else{
			Integer pId = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.PATIENT_ID);
			patient.setNPatientId(pId);
			
			Integer mrId = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.MR_NUMBER);
			mr.setVMrCode(MedisafeUtil.convertToMrCode(mrId));
			mr.setNMrId(mrId);
			
			Date tanggal = new Date();
			patient.setDWhnCreate(tanggal);
			mr.setDWhnCreate(tanggal);
		}
		
		mr.setMsPatient(patient);
		
			
		if(serv.save(patient, mr)){
			//set semua field menjadi disable
			String messages = null;
			if(status == 1)messages = "common.modify.success";
			else messages = "common.add.success";
			Messagebox.show(MessagesService.getKey(messages));
			noMr.setValue(mr.getVMrCode());
			btnSave.setDisabled(true);
			btnModify.setDisabled(true);
			btnCancel.setDisabled(false);
			setFieldDisable(true);
		}else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		
		kelurahanList = (Listbox)win.getFellow("kelurahanList");
		kecamatanList = (Listbox)win.getFellow("kecamatanList");
		kabupatenList = (Listbox)win.getFellow("kabupatenList");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnCancel= (Button)win.getFellow("btnCancel");
		btnModify = (Button)win.getFellow("btnModify");
		propinsiList = (Listbox)win.getFellow("propinsiList");
		tipePasienList = (Listbox)win.getFellow("tipePasienList");
		tglLahir = (Datebox)win.getFellow("tglLahir");
		jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		namaPasien = (Textbox)win.getFellow("namaPasien");
		umur = (Textbox)win.getFellow("umur");
		alamat = (Textbox)win.getFellow("alamat");
		noMr = (Bandbox)win.getFellow("noMR");
		religionList = (Listbox)win.getFellow("religionList");
		wargaNegaraList = (Listbox) win.getFellow("wargaNegaraList");		
		statusKawinList = (Listbox)win.getFellow("statusKawinList");
		noTelp = (Textbox)win.getFellow("noTelp");
		alamatAlternatif = (Textbox)win.getFellow("alamatAlternatif");
		rt = (Textbox)win.getFellow("rt");
		rw = (Textbox)win.getFellow("rw");
		rw1 = (Textbox)win.getFellow("rw1");
		rt1 = (Textbox)win.getFellow("rt1");
		noTelpAlt = (Textbox)win.getFellow("noTelpAlt");
		pendidikanList = (Listbox)win.getFellow("pendidikanList");
		jenisPekerjaanList = (Listbox)win.getFellow("jenisPekerjaanList");
		prioritasPasienList = (Listbox)win.getFellow("prioritasPasienList");
		
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		
		patientSearchList = (Listbox)win.getFellow("patientSearchList");
		
		super.init(win);
		
		this.win = (Window)win;
		
		constraints.registerComponent(tglLahir,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaPasien, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaPasien, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(alamat, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(alamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(alamatAlternatif, ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		
		
		//PatientTypeController.getAllPatientTypeList(tipePasienList);
		MasterServiceLocator.getPatientTypeManager().getPatientTypeForSelect(tipePasienList);
		
//		ProvinceController.getAllProvinceList(propinsiList);
//		SubDistrictController.getSubdistrictList(kecamatanList);
//		VillageController.getAllVillageList(kelurahanList);
		
		MasterServiceLocator.getVillageManager().getVillageForSelect(kelurahanList);
		MasterServiceLocator.getSubDistrictManager().getSubDistrictForSelect(kecamatanList);
		MasterServiceLocator.getProvinceManager().getProvinceForSelect(propinsiList);
		MasterServiceLocator.getRegencyManager().getRegencyForSelect(kabupatenList);
		
		patientSearchList.getItems().clear();
		btnModify.setDisabled(true);
		jenisKelamin.setSelectedIndex(0);
		noMr.focus();
		btnCancel.setDisabled(false);
		
	}
	
	public void setDisable(Component win) throws InterruptedException{
		
		
		btnSave.setDisabled(true);
		btnCancel.setDisabled(false);
		btnModify.setDisabled(false);
		setFieldDisable(true);
	}
	
	public void saveOrNew(Component win) throws InterruptedException{
		
		
		btnSave.setDisabled(false);
		btnCancel.setDisabled(false);
		btnModify.setDisabled(true);
		noMr.focus();
		
	}
	
	public void setFieldDisable(boolean disable) throws InterruptedException{
		
		kelurahanList.setDisabled(disable);
		kecamatanList.setDisabled(disable);
		kabupatenList.setDisabled(disable);
		propinsiList.setDisabled(disable);
		tipePasienList.setDisabled(disable);
		tglLahir.setDisabled(disable);
		namaPasien.setDisabled(disable);
		alamat.setDisabled(disable);
		noMr.setDisabled(disable);
		umur.setDisabled(disable);
		religionList.setDisabled(disable);
		wargaNegaraList.setDisabled(disable);
		statusKawinList.setDisabled(disable);
		noTelp.setDisabled(disable);
		alamatAlternatif.setDisabled(disable);
		rt.setDisabled(disable);
		rw.setDisabled(disable);
		rw1.setDisabled(disable);
		rt1.setDisabled(disable);
		noTelpAlt.setDisabled(disable);
		pendidikanList.setDisabled(disable);
		jenisPekerjaanList.setDisabled(disable);
		prioritasPasienList.setDisabled(disable);
		
	}
	
	public static void searchPatient(Textbox crNoMR, Textbox crNama, Datebox crTgl, Textbox crAlamat, Listbox patientSearchList)
		throws InterruptedException,VONEAppException{
		
		serv.cariPasienYgPunyaMr(crNoMR,crNama,crTgl, crAlamat,patientSearchList);
		
	}
	
	public static void searchRegisteredPatient(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList, Listbox unitList)
	throws InterruptedException, VONEAppException{
		
		serv.serachRegisteredPatient(crNoMR, crNama, crAlamat, patientSearchList,unitList);
		
	}
	
	public static void searchRegisteredPatient(Textbox crNoMR, Textbox crNama, Datebox crTgl, Textbox crAlamat, Listbox patientSearchList)
	throws InterruptedException, VONEAppException{
		
		serv.serachRegisteredPatient(crNoMR, crNama, crTgl, crAlamat, patientSearchList);
		
	}
	
	public void getPatientDetail(int type) throws InterruptedException, VONEAppException{
		
		serv.getPatientDetil(this.win,type);
				

		setFieldDisable(true);
		this.btnSave.setDisabled(false);
		this.btnCancel.setDisabled(false);
		this.btnModify.setDisabled(false);
		
	}
	
	public static void searchRanapPatient(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList)
	throws InterruptedException, VONEAppException{
		
		serv.searchRanapPatient(crNoMR, crNama,crAlamat, patientSearchList);
		MiscTrxController.setFont(patientSearchList);
				
	}
	
	
	public static void searchWardPatient(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList,
			Listbox bangsalList)
	throws InterruptedException, VONEAppException
	{
		
		serv.getPatientBaseOnWard(crNoMR,crNama,crAlamat,patientSearchList,bangsalList);
		MiscTrxController.setFont(patientSearchList);
	}
	
	
}

package com.vone.medisafe.service.ifaceimpl.master;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientDAO;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsProvince;
import com.vone.medisafe.mapping.MsRegency;
import com.vone.medisafe.mapping.MsSubDistrict;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsVillage;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.master.PatientManager;

public class PatientManagerImpl implements PatientManager{

	private MsPatientDAO dao;
	private TbMedicalRecordDAO mrDao;
	
	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}
	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}
	public MsPatientDAO getDao() {
		return dao;
	}
	public void setDao(MsPatientDAO dao) {
		this.dao = dao;
	}
	
	
	public boolean save(MsPatient patient, TbMedicalRecord mr) {
		// TODO Auto-generated method stub
		return this.dao.save(patient, mr);
	}
	
	public List searchPatient(String code, String nameP, String addressP) {
		// TODO Auto-generated method stub
		return null;
//		return dao.searchPatient(code, nameP, addressP);
	}
	
	
		
	
	
	public void cariPasienYgPunyaMr(Textbox crNoMR, Textbox crNama, Datebox crTgl, Textbox crAlamat, Listbox patientSearchList)
	throws VONEAppException, InterruptedException {
		
		TbMedicalRecord mr = null;
		Listitem item = null;
		String code = "";
		if(crNoMR.getText().trim().length() == 6){
			code = MedisafeUtil.convertToMrCode(crNoMR.getText());
			crNoMR.setValue(code);
		}
		else
		{
			code = crNoMR.getText();
		}
		
		
				
		patientSearchList.getItems().clear();
		if(crNoMR.getText().trim().equals("") && crNama.getText().trim().equals("") &&
				crAlamat.getText().trim().equals("") && crTgl.getText().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			return;
		}
		
		List list = dao.searchPatient("%"+code+"%","%"+crNama.getText()+"%",
						"%" +crAlamat.getText()+"%", crTgl.getValue());
		
		if(list.size() == 0){
			crNoMR.focus();
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
		Iterator it = list.iterator();
		
		while(it.hasNext()){
			mr = (TbMedicalRecord)it.next();
			item = new Listitem();
			item.setValue(mr);
			item.setParent(patientSearchList);
			
			Listcell mrCode = new Listcell(mr.getVMrCode());
			mrCode.setParent(item);
			
			Listcell nama = new Listcell(mr.getMsPatient().getVPatientName());
			nama.setParent(item);
			
			nama = new Listcell(sdf.format(mr.getMsPatient().getDPatientDob()));
			nama.setParent(item);
			
			Listcell alamat = new Listcell(mr.getMsPatient().getVPatientMainAddr());
			alamat.setParent(item);
		}
		MiscTrxController.setFont(patientSearchList);
		
	}
	
	
	public void serachRegisteredPatient(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList,
			Listbox unitList) 
		throws VONEAppException, InterruptedException {
		
		String code = "";
		if(crNoMR.getText().trim().length() == 6){
			code = MedisafeUtil.convertToMrCode(crNoMR.getText());
			crNoMR.setValue(code);
		}
		else
		{
			code = crNoMR.getText();
		}
		
		
		
		Listitem item = null;
		patientSearchList.getItems().clear();
		if(crNoMR.getText().trim().equals("") && crNama.getText().trim().equals("") &&
				crAlamat.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			return;
		}
		MsUnit unit = (MsUnit)unitList.getSelectedItem().getValue();
		List<TbMedicalRecord> list = dao.searchPatientRegistered("%"+code+"%","%"+crNama.getText()+"%",
											"%" +crAlamat.getText()+"%", unit.getNUnitId());
		
		
		if(list.size() == 0){
			crNoMR.focus();
			return;
		}
				
		
		
		for(TbMedicalRecord mr : list){
			
			item = new Listitem();
			item.setValue(mr);
			item.setParent(patientSearchList);
			
			Listcell mrCode = new Listcell(mr.getVMrCode());
			mrCode.setParent(item);
			Listcell nama = new Listcell(mr.getMsPatient().getVPatientName());
			nama.setParent(item);
			Listcell alamat = new Listcell(mr.getMsPatient().getVPatientMainAddr());
			alamat.setParent(item);
		}
		
		MiscTrxController.setFont(patientSearchList);
		
	}
	
	
	public void serachRegisteredPatient(Textbox crNoMR, Textbox crNama, Datebox crTgl, Textbox crAlamat, Listbox patientSearchList) 
		throws VONEAppException, InterruptedException {
		
		String code = "";
		if(crNoMR.getText().trim().length() == 6){
			code = MedisafeUtil.convertToMrCode(crNoMR.getText());
			crNoMR.setValue(code);
		}
		else
		{
			code = crNoMR.getText();
		}
		
		
		
		Listitem item = null;
		patientSearchList.getItems().clear();
		if(crNoMR.getText().trim().equals("") && crNama.getText().trim().equals("") &&
				crAlamat.getText().trim().equals("") && crTgl.getText().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			return;
		}
		
		List<TbMedicalRecord> list = dao.searchPatientRegistered("%"+code+"%","%"+crNama.getText()+"%",
											"%" +crAlamat.getText()+"%", crTgl.getValue());
		
		
		if(list.size() == 0){
			crNoMR.focus();
			return;
		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		for(TbMedicalRecord mr : list){
			
			item = new Listitem();
			item.setValue(mr);
			item.setParent(patientSearchList);
			
			Listcell mrCode = new Listcell(mr.getVMrCode());
			mrCode.setParent(item);
			Listcell nama = new Listcell(mr.getMsPatient().getVPatientName());
			nama.setParent(item);
			
			Listcell dob = new Listcell(sdf.format(mr.getMsPatient().getDPatientDob()));
			dob.setParent(item);
			
			Listcell alamat = new Listcell(mr.getMsPatient().getVPatientMainAddr());
			alamat.setParent(item);
		}
		
		MiscTrxController.setFont(patientSearchList);
		
	}
	
	
	
	public void searchRanapPatient(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList) throws VONEAppException, InterruptedException {
		
		
		Listitem item = null;
		String code = "";
		if(crNoMR.getText().trim().length() == 6){
			code = MedisafeUtil.convertToMrCode(crNoMR.getText());
			crNoMR.setValue(code);
		}
		else
		{
			code = crNoMR.getText();
		}
				
		patientSearchList.getItems().clear();
		if(crNoMR.getText().trim().equals("") && crNama.getText().trim().equals("") &&
				crAlamat.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			return;
		}
		
		List<TbMedicalRecord> list = dao.searchRanapPatient("%"+code+"%","%"+crNama.getText()+"%",
				"%" +crAlamat.getText()+"%"); 
			
		
		if(list.size() == 0){
			crNoMR.focus();
			return;
		}
				
				
		for(TbMedicalRecord mr : list){
			
			item = new Listitem();
			item.setValue(mr);
			item.setParent(patientSearchList);
			
			Listcell mrCode = new Listcell(mr.getVMrCode());
			mrCode.setParent(item);
			Listcell nama = new Listcell(mr.getMsPatient().getVPatientName());
			nama.setParent(item);
			
			String tipePasien = "NON BPJS";
			if(mr.getMsPatient().getMsPatientType() != null)
				tipePasien = mr.getMsPatient().getMsPatientType().getVTpatientDesc();
			
			Listcell cell = new Listcell(tipePasien);
			cell.setParent(item);
					
			
			Listcell alamat = new Listcell(mr.getMsPatient().getVPatientMainAddr());
			alamat.setParent(item);
		}

		
	}
	
	
	
	public void getPatientDetil(Window win, int type) throws VONEAppException, InterruptedException {
		
		
		Listbox kelurahanList = (Listbox)win.getFellow("kelurahanList");
		Listbox kecamatanList = (Listbox)win.getFellow("kecamatanList");
		Listbox kabupatenList = (Listbox)win.getFellow("kabupatenList");
		Listbox propinsiList = (Listbox)win.getFellow("propinsiList");
		Listbox tipePasienList = (Listbox)win.getFellow("tipePasienList");
		Datebox tglLahir = (Datebox)win.getFellow("tglLahir");
		Radiogroup jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		Textbox namaPasien = (Textbox)win.getFellow("namaPasien");
		Textbox umur = (Textbox)win.getFellow("umur");
		Textbox alamat = (Textbox)win.getFellow("alamat");
		Bandbox noMr = (Bandbox)win.getFellow("noMR");
		Listbox religionList = (Listbox)win.getFellow("religionList");
		Listbox wargaNegaraList = (Listbox) win.getFellow("wargaNegaraList");		
		Listbox statusKawinList = (Listbox)win.getFellow("statusKawinList");
		Textbox noTelp = (Textbox)win.getFellow("noTelp");
		Textbox alamatAlternatif = (Textbox)win.getFellow("alamatAlternatif");
		Textbox rt = (Textbox)win.getFellow("rt");
		Textbox rw = (Textbox)win.getFellow("rw");
		Textbox rw1 = (Textbox)win.getFellow("rw1");
		Textbox rt1 = (Textbox)win.getFellow("rt1");
		Textbox noTelpAlt = (Textbox)win.getFellow("noTelpAlt");
		Listbox pendidikanList = (Listbox)win.getFellow("pendidikanList");
		Listbox jenisPekerjaanList = (Listbox)win.getFellow("jenisPekerjaanList");
		Listbox prioritasPasienList = (Listbox)win.getFellow("prioritasPasienList");
		Listbox patientSearchList = (Listbox)win.getFellow("patientSearchList");
		
	
		TbMedicalRecord mr;
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(noMr.getValue());
			mr = this.mrDao.getPatientMedicalRecord(code);
			if(mr == null){
				Messagebox.show(MessagesService.getKey("admission.mr.notfound"));
				noMr.focus();
				return;
			}
		}
		else{
			mr = (TbMedicalRecord)patientSearchList.getSelectedItem().getValue();
			//tarik lagi dari db, biar dpt session
			mr = this.mrDao.getPatientMedicalRecord(mr.getVMrCode());
		}
	 	
	 	
		noMr.setValue(mr.getVMrCode());
		namaPasien.setValue(mr.getMsPatient().getVPatientName());
		tglLahir.setValue(mr.getMsPatient().getDPatientDob());
		Calendar calSkrg = Calendar.getInstance();
		Calendar calLahir = Calendar.getInstance();
		calSkrg.setTime(new Date());
		calLahir.setTime(mr.getMsPatient().getDPatientDob());
		int[] umurSkrg = MedisafeUtil.getDayDifferent(calSkrg,calLahir);
		umur.setValue(MedisafeUtil.convertAgeToString(umurSkrg));
		alamat.setValue(mr.getMsPatient().getVPatientMainAddr());
		if(mr.getMsPatient().getVPatientGender().equals("M")){
			jenisKelamin.setSelectedIndex(0);
		}
		else{
			jenisKelamin.setSelectedIndex(1);
		}

		alamatAlternatif.setValue(mr.getMsPatient().getVPatientAltAddr());
		  
		if(mr.getMsPatient().getVPatientMainRtRw() != null){
			String[] rtRw = mr.getMsPatient().getVPatientMainRtRw().split("/");
			rt.setValue(rtRw[0]);
			rw.setValue(rtRw[1]);
		}  
		if(mr.getMsPatient().getVPatientAltRtRw() != null){
			String[] rt1rw1 = mr.getMsPatient().getVPatientAltRtRw().split("/");
			rt1.setValue(rt1rw1[0]);
			rw1.setValue(rt1rw1[1]);
		}
		
		noTelp.setValue(mr.getMsPatient().getVPatientMainPhNo());
		noTelpAlt.setValue(mr.getMsPatient().getVPatientAltPhNo());
		
		for(int i=1; i < religionList.getItems().size(); i++){
			if(religionList.getItemAtIndex(i).getValue() != null)
			if(religionList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientReligion()))
				religionList.setSelectedItem(religionList.getItemAtIndex(i));
			
		}
		
		for(int i=1; i < statusKawinList.getItems().size(); i++){
			if(statusKawinList.getItemAtIndex(i).getValue() != null)
			if(statusKawinList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientMaritalStatus()))
				statusKawinList.setSelectedItem(statusKawinList.getItemAtIndex(i));
		}
		for(int i=1; i < wargaNegaraList.getItems().size(); i++){
			if(wargaNegaraList.getItemAtIndex(i).getValue() != null)
			if(wargaNegaraList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientNationality()))
				wargaNegaraList.setSelectedItem(wargaNegaraList.getItemAtIndex(i));
		}

		
		Iterator iterator = kelurahanList.getItems().iterator();
		MsVillage desa = null;
		while (iterator.hasNext()) {
			Listitem item = (Listitem) iterator.next();
			if(item.getValue() instanceof MsVillage){
				desa = (MsVillage)item.getValue();
				if(mr.getMsPatient().getMsVillage() instanceof MsVillage)
				if(mr.getMsPatient().getMsVillage().getNVillageId().equals(desa.getNVillageId()))
					kelurahanList.setSelectedItem(item);
			}
		}
		MsSubDistrict kec = null;
		if(mr.getMsPatient().getMsSubDistrict() != null){
			for(int i=1; i < kecamatanList.getItems().size(); i++){
				kec = (MsSubDistrict)kecamatanList.getItemAtIndex(i).getValue();
				if(kecamatanList.getItemAtIndex(i).getValue() != null)
				if(kec.getNSubdistrictId().equals(mr.getMsPatient().getMsSubDistrict().getNSubdistrictId()))
					kecamatanList.setSelectedItem(kecamatanList.getItemAtIndex(i));
			}
		}
		MsRegency kab = null;
		if(mr.getMsPatient().getMsRegency() != null){
			for(int i=1; i < kabupatenList.getItems().size(); i++){
				kab = (MsRegency)kabupatenList.getItemAtIndex(i).getValue();
				if(kabupatenList.getItemAtIndex(i).getValue() != null)
				if(kab.getNRegencyId().equals(mr.getMsPatient().getMsRegency().getNRegencyId()))
					kabupatenList.setSelectedItem(kabupatenList.getItemAtIndex(i));
			}
		}
		MsProvince prop = null;
		if(mr.getMsPatient().getMsProvince() != null){
			for(int i=1; i < propinsiList.getItems().size(); i++){
				prop = (MsProvince)propinsiList.getItemAtIndex(i).getValue();
				if(propinsiList.getItemAtIndex(i).getValue() != null)
				if(prop.getNProvinceId().equals(mr.getMsPatient().getMsProvince().getNProvinceId()))
					propinsiList.setSelectedItem(propinsiList.getItemAtIndex(i));
			}	
		}
		for(int i=1; i < pendidikanList.getItems().size(); i++){
			if(pendidikanList.getItemAtIndex(i).getValue() != null)
			if(pendidikanList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientEdu()))
				pendidikanList.setSelectedItem(pendidikanList.getItemAtIndex(i));
		}
		for(int i=1; i < jenisPekerjaanList.getItems().size(); i++){
			if(jenisPekerjaanList.getItemAtIndex(i).getValue() != null)
			if(jenisPekerjaanList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientJobType()))
				jenisPekerjaanList.setSelectedItem(jenisPekerjaanList.getItemAtIndex(i));
		}
		MsPatientType patType = null;
		if(mr.getMsPatient().getMsPatientType() != null){
			for(int i=1; i < tipePasienList.getItems().size(); i++){
				patType = (MsPatientType) tipePasienList.getItemAtIndex(i).getValue();
				if(tipePasienList.getItemAtIndex(i).getValue() != null)
				if(patType.getNPatientTypeId().equals(mr.getMsPatient().getMsPatientType().getNPatientTypeId()))
					tipePasienList.setSelectedItem(tipePasienList.getItemAtIndex(i));
			}
		}
		for(int i=1; i < prioritasPasienList.getItems().size(); i++){
			if(prioritasPasienList.getItemAtIndex(i).getValue() != null)
			if(prioritasPasienList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientPriority()))
				prioritasPasienList.setSelectedItem(prioritasPasienList.getItemAtIndex(i));
		} 

		
	}
	
	
	
	
	public List searchPatient(String noMr, String name, String alamat, String ruangan, String doctor) throws VONEAppException {
		
		
		return dao.searchRanapPatient(noMr, name, alamat, ruangan, doctor);
	}
	
	
	
	
	public void getPatientBaseOnWard(Textbox crNoMR, Textbox crNama, Textbox crAlamat, Listbox patientSearchList,
			Listbox unitListbox)
	throws VONEAppException, InterruptedException {
		
		MsUnit unit = (MsUnit)unitListbox.getSelectedItem().getValue();
		
		Listitem item = null;
		String code = "";
		if(crNoMR.getText().trim().length() == 6){
			code = MedisafeUtil.convertToMrCode(crNoMR.getText());
			crNoMR.setValue(code);
		}
		else
		{
			code = crNoMR.getText();
		}
				
		patientSearchList.getItems().clear();
		if(crNoMR.getText().trim().equals("") && crNama.getText().trim().equals("") &&
				crAlamat.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			return;
		}
		
		List<TbMedicalRecord> mrList = this.dao.getPatientBaseOnWard("%"+code+"%","%"+crNama.getText()+"%",
				"%"+crAlamat.getText()+"%",unit.getVUnitName());
		
		
		for(TbMedicalRecord mr : mrList){
			
			item = new Listitem();
			item.setValue(mr);
			item.setParent(patientSearchList);
			
			Listcell mrCode = new Listcell(mr.getVMrCode());
			mrCode.setParent(item);
			Listcell nama = new Listcell(mr.getMsPatient().getVPatientName());
			nama.setParent(item);
			Listcell alamat = new Listcell(mr.getMsPatient().getVPatientMainAddr());
			alamat.setParent(item);
		}
		
	}
	

}

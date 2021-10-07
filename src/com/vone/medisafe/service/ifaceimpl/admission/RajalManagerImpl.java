package com.vone.medisafe.service.ifaceimpl.admission;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
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
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.MsProvince;
import com.vone.medisafe.mapping.MsRegency;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsSubDistrict;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.MsVillage;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.service.iface.admission.RajalManager;

public class RajalManagerImpl implements RajalManager{
	
	private TbRegistrationDAO dao;
	private TbMedicalRecordDAO mrDao;

	public TbRegistrationDAO getDao() {
		return dao;
	}

	public void setDao(TbRegistrationDAO dao) {
		this.dao = dao;
	}
	

	public boolean savePatientAndRegistration(Window win) throws VONEAppException, InterruptedException {
		
		MsUser user = (MsUser)Sessions.getCurrent().getAttribute(MedisafeConstants.USER_SESSION);
		
		Listbox kelurahanList = (Listbox)win.getFellow("kelurahanList");
		Listbox kecamatanList = (Listbox)win.getFellow("kecamatanList");
		Listbox kabupatenList = (Listbox)win.getFellow("kabupatenList");
		Listbox propinsiList = (Listbox)win.getFellow("propinsiList");
		Listbox tipePasienList = (Listbox)win.getFellow("tipePasienList");
		Datebox tglLahir = (Datebox)win.getFellow("tglLahir");
		Listbox unitList = (Listbox)win.getFellow("unitList");
		Textbox noRegistrasi = (Textbox)win.getFellow("noRegistrasi");
		Radiogroup jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		Textbox namaPasien = (Textbox)win.getFellow("namaPasien");
		Textbox alamat = (Textbox)win.getFellow("alamat");
		Listbox dokterPemeriksaList = (Listbox)win.getFellow("dokterPemeriksaList");
		Bandbox noMr = (Bandbox) win.getFellow("noMR");
		Textbox alamatAlternatif = (Textbox)win.getFellow("alamatAlternatif");
		
			
		Listbox religionList = (Listbox)win.getFellow("religionList");
		Listbox wargaNegaraList = (Listbox)win.getFellow("wargaNegaraList");		
		Listbox statusKawinList = (Listbox)win.getFellow("statusKawinList");
		Textbox noTelp = (Textbox)win.getFellow("noTelp");
		
		Textbox rt = (Textbox)win.getFellow("rt");
		Textbox rw = (Textbox)win.getFellow("rw");
		Textbox rw1 = (Textbox)win.getFellow("rw1");
		Textbox rt1 = (Textbox)win.getFellow("rt1");
		Textbox noTelpAlt = (Textbox)win.getFellow("noTelpAlt");
		Listbox pendidikanList = (Listbox)win.getFellow("pendidikanList");
		Listbox jenisPekerjaanList = (Listbox)win.getFellow("jenisPekerjaanList");
		Listbox prioritasPasienList = (Listbox)win.getFellow("prioritasPasienList");
		Listbox etnisList = (Listbox) win.getFellow("etnisList");
		Listbox languageList = (Listbox) win.getFellow("languageList");
		
//		Textbox namaIbu = (Textbox)win.getFellow("namaIbu");
//		Textbox namaPasgnan = (Textbox)win.getFellow("namaPasangan");

		
		MsPatientType tipePasien;MsVillage village;MsProvince province;MsRegency regency;MsSubDistrict subdistric;
		MsPatient patient = new MsPatient();
		if(tipePasienList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			tipePasien = (MsPatientType)tipePasienList.getSelectedItem().getValue();
			patient.setMsPatientType(tipePasien);
		}
		if(kelurahanList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			village = (MsVillage)kelurahanList.getSelectedItem().getValue();
			patient.setMsVillage(village);
		}
		if(propinsiList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			province = (MsProvince)propinsiList.getSelectedItem().getValue();
			patient.setMsProvince(province);
		}
		if(kabupatenList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			regency = (MsRegency)kabupatenList.getSelectedItem().getValue();
			patient.setMsRegency(regency);
		}
		if(kecamatanList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			subdistric = (MsSubDistrict)kecamatanList.getSelectedItem().getValue();
			patient.setMsSubDistrict(subdistric);
		}
		if(etnisList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG)
			patient.setVEtnis(etnisList.getSelectedItem().getValue().toString());
		if(languageList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG)
			patient.setVLanguage(languageList.getSelectedItem().getValue().toString());
						
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
		
		
//		patient.setVPatientPartnerName(namaPasgnan.getText());
//		patient.setVPatientMotherName(namaIbu.getText());
		

		Date tanggal = new Date();
		patient.setDWhnCreate(tanggal);
		patient.setVWhoCreate(user.getVUserName());

			
		TbMedicalRecord mr = new TbMedicalRecord();
		mr.setVWhoCreate(user.getVUserName());
		mr.setDWhnCreate(tanggal);
		TbRegistration registration = new TbRegistration();
		registration.setRegStatus(new Integer(MedisafeConstants.REG_ACTIVE));
		registration.setDWhnCreate(tanggal);
		registration.setVWhoCreate(user.getVUserName());
		registration.setMsUnit((MsUnit)unitList.getSelectedItem().getValue());
		registration.setMsDivision(((MsUnit)unitList.getSelectedItem().getValue()).getMsDivision());
		if(!dokterPemeriksaList.getSelectedItem().getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
			registration.setMsStaff((MsStaff)dokterPemeriksaList.getSelectedItem().getValue());
		}
			
		
				
			if(dao.saveRegistration(patient,mr,registration,null)){
				noMr.setValue(mr.getVMrCode());
				noMr.setAttribute("registration", registration);
				noRegistrasi.setValue(registration.getVRegSecondaryId());
				
				return true;
				
			}
			else{
				
				Messagebox.show(MessagesService.getKey("admission.registration.fail"));
				return false;
			}
	}

	
	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	
	
	

	public boolean saveRegistrationOnly(Bandbox noMr, Listbox dokterPemeriksaList, Listbox unitList, Textbox noRegistrasi, 
			Map<String, Object> pasien) throws VONEAppException, InterruptedException {
		
		TbMedicalRecord mr = mrDao.getPatientMedicalRecord(noMr.getText());
		MsUser user = (MsUser)Sessions.getCurrent().getAttribute(MedisafeConstants.USER_SESSION);	
		
		TbRegistration oldReg = dao.getLastRegistrationByMrId(mr.getNMrId());
		if(oldReg != null){
			if(oldReg.getVRegSecondaryId().length() > 0){
				Messagebox.show(MessagesService.getKey("admission.rajal.not.allowed"));
				return false;
			}
		}
		Listbox tipePasien = (Listbox)pasien.get("tipePasien");
		Listbox etnisList = (Listbox)pasien.get("etnis");
		Listbox bahasaList = (Listbox)pasien.get("bahasa");
		Listbox propinsiList = (Listbox) pasien.get("propinsi");
		Listbox kabupatenList = (Listbox) pasien.get("kabupaten");
		Listbox kecamatanList = (Listbox) pasien.get("kecamatan");
		Listbox kelurahanList = (Listbox) pasien.get("kelurahan");
		
		MsPatient patien = mr.getMsPatient();
		if(!etnisList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG))
			patien.setVEtnis(etnisList.getSelectedItem().getValue().toString());
		if(!bahasaList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG))
			patien.setVLanguage(bahasaList.getSelectedItem().getValue().toString());
		if(!propinsiList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG)) {
			MsProvince province = (MsProvince)propinsiList.getSelectedItem().getValue();
			patien.setMsProvince(province);
		}
		if(!kabupatenList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG)) {
			MsRegency regency = (MsRegency)kabupatenList.getSelectedItem().getValue();
			patien.setMsRegency(regency);
		}
		if(!kecamatanList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG)) {
			MsSubDistrict district = (MsSubDistrict) kecamatanList.getSelectedItem().getValue();
			patien.setMsSubDistrict(district);
		}
		if(!kelurahanList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG)) {
			MsVillage village = (MsVillage) kelurahanList.getSelectedItem().getValue();
			patien.setMsVillage(village);
		}
			
		if(!tipePasien.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG)){
			MsPatientType ptype = (MsPatientType)tipePasien.getSelectedItem().getValue();
			patien.setMsPatientType(ptype);
		}

		TbRegistration reg = new TbRegistration();
		reg.setRegStatus(new Integer(MedisafeConstants.REG_ACTIVE));
		reg.setDWhnCreate(new Date());
		
		if(!dokterPemeriksaList.getSelectedItem().getValue().equals(MedisafeConstants.LISTKOSONG)){
			reg.setMsStaff((MsStaff)dokterPemeriksaList.getSelectedItem().getValue());
		}
		
		reg.setMsDivision(((MsUnit)unitList.getSelectedItem().getValue()).getMsDivision());
		reg.setMsUnit((MsUnit)unitList.getSelectedItem().getValue());
		reg.setVWhoCreate(user.getVUserName());
		
		if(dao.saveRegistrationOnly(reg,null, mr, patien)){
			
			noRegistrasi.setValue(reg.getVRegSecondaryId());
			noMr.setAttribute("registration", reg);
			
			return true;
		}
		
		return false;

	}
	

	public void getPatientDetail(Window win, String code) throws VONEAppException, InterruptedException {
		
		Listbox kelurahanList = (Listbox)win.getFellow("kelurahanList");
		Listbox kecamatanList = (Listbox)win.getFellow("kecamatanList");
		Listbox kabupatenList = (Listbox)win.getFellow("kabupatenList");
		Listbox propinsiList = (Listbox)win.getFellow("propinsiList");
		Listbox tipePasienList = (Listbox)win.getFellow("tipePasienList");
		Datebox tglLahir = (Datebox)win.getFellow("tglLahir");
		Listbox unitList = (Listbox)win.getFellow("unitList");
		Radiogroup jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		Textbox namaPasien = (Textbox)win.getFellow("namaPasien");
		Textbox alamat = (Textbox)win.getFellow("alamat");
		Bandbox noMr = (Bandbox) win.getFellow("noMR");
		Textbox alamatAlternatif = (Textbox)win.getFellow("alamatAlternatif");
		
		Textbox umur = (Textbox)win.getFellow("umur");
		
			
		Listbox religionList = (Listbox)win.getFellow("religionList");
		Listbox wargaNegaraList = (Listbox)win.getFellow("wargaNegaraList");		
		Listbox statusKawinList = (Listbox)win.getFellow("statusKawinList");
		Textbox noTelp = (Textbox)win.getFellow("noTelp");
		
		Textbox rt = (Textbox)win.getFellow("rt");
		Textbox rw = (Textbox)win.getFellow("rw");
		Textbox rw1 = (Textbox)win.getFellow("rw1");
		Textbox rt1 = (Textbox)win.getFellow("rt1");
		Textbox noTelpAlt = (Textbox)win.getFellow("noTelpAlt");
		Listbox pendidikanList = (Listbox)win.getFellow("pendidikanList");
		Listbox jenisPekerjaanList = (Listbox)win.getFellow("jenisPekerjaanList");
		Listbox prioritasPasienList = (Listbox)win.getFellow("prioritasPasienList");
		Listbox etnisList = (Listbox) win.getFellow("etnisList");
		Listbox languageList = (Listbox) win.getFellow("languageList");
		
		TbMedicalRecord mr = mrDao.getPatientMedicalRecord(code);
		if(mr == null){
			Messagebox.show(MessagesService.getKey("admission.mr.notfound"));
			noMr.focus();
			return;
		}
		
		unitList.focus();
		
		noMr.setValue(code);
		
		namaPasien.setValue(mr.getMsPatient().getVPatientName());
		tglLahir.setValue(mr.getMsPatient().getDPatientDob());
		Calendar calSkrg = Calendar.getInstance();
		Calendar calLahir = Calendar.getInstance();
		calSkrg.setTime(new Date());
		calLahir.setTime(mr.getMsPatient().getDPatientDob());
		int[] umurSkrg = MedisafeUtil.getDayDifferent(calSkrg,calLahir);
		umur.setValue(umurSkrg[0]+" thn "+umurSkrg[1]+" bln "+umurSkrg[2]+" hr");
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
			
			if(religionList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientReligion()))
				religionList.setSelectedItem(religionList.getItemAtIndex(i));
			
		}
		
		for(int i=1; i < statusKawinList.getItems().size(); i++){
			if(statusKawinList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientMaritalStatus()))
				statusKawinList.setSelectedItem(statusKawinList.getItemAtIndex(i));
		}
		for(int i=1; i < wargaNegaraList.getItems().size(); i++){
			if(wargaNegaraList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientNationality()))
				wargaNegaraList.setSelectedItem(wargaNegaraList.getItemAtIndex(i));
		}
		if(mr.getMsPatient().getMsVillage() != null){
			for(int i=1; i < kelurahanList.getItems().size(); i++){
				if(((MsVillage)kelurahanList.getItemAtIndex(i).getValue()).getNVillageId()
						.equals(mr.getMsPatient().getMsVillage().getNVillageId()))
					kelurahanList.setSelectedItem(kelurahanList.getItemAtIndex(i));
			}
		}
		if(mr.getMsPatient().getMsSubDistrict() != null){
			for(int i=1; i < kecamatanList.getItems().size(); i++){
				if(((MsSubDistrict)kecamatanList.getItemAtIndex(i).getValue()).getNSubdistrictId().
						equals(mr.getMsPatient().getMsSubDistrict().getNSubdistrictId()))
					kecamatanList.setSelectedItem(kecamatanList.getItemAtIndex(i));
			}
		}
		if(mr.getMsPatient().getMsRegency() != null){
			for(int i=1; i < kabupatenList.getItems().size(); i++){
				if(((MsRegency)kabupatenList.getItemAtIndex(i).getValue()).getNRegencyId()
						.equals(mr.getMsPatient().getMsRegency().getNRegencyId()))
					kabupatenList.setSelectedItem(kabupatenList.getItemAtIndex(i));
			}
		}
		if(mr.getMsPatient().getMsProvince() != null){
			for(int i=1; i < propinsiList.getItems().size(); i++){
				if(((MsProvince)propinsiList.getItemAtIndex(i).getValue()).getNProvinceId()
						.equals(mr.getMsPatient().getMsProvince().getNProvinceId()))
					propinsiList.setSelectedItem(propinsiList.getItemAtIndex(i));
			}	
		}
		for(int i=1; i < pendidikanList.getItems().size(); i++){
			if(pendidikanList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientEdu()))
				pendidikanList.setSelectedItem(pendidikanList.getItemAtIndex(i));
		}
		for(int i=1; i < jenisPekerjaanList.getItems().size(); i++){
			if(jenisPekerjaanList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientJobType()))
				jenisPekerjaanList.setSelectedItem(jenisPekerjaanList.getItemAtIndex(i));
		}
		if(mr.getMsPatient().getMsPatientType() != null){
			for(int i=1; i < tipePasienList.getItems().size(); i++){
				if(((MsPatientType)tipePasienList.getItemAtIndex(i).getValue()).getNPatientTypeId()
						.equals(mr.getMsPatient().getMsPatientType().getNPatientTypeId()))
					tipePasienList.setSelectedItem(tipePasienList.getItemAtIndex(i));
			}
		}
		for(int i=1; i < prioritasPasienList.getItems().size(); i++){
			if(prioritasPasienList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVPatientPriority()))
				prioritasPasienList.setSelectedItem(prioritasPasienList.getItemAtIndex(i));
		}
		if(mr.getMsPatient().getVEtnis() != null) {
			for(int i=1; i < etnisList.getItems().size(); i++) {
				if(etnisList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVEtnis()))
					etnisList.setSelectedItem(etnisList.getItemAtIndex(i));
			}
		}
		
		if(mr.getMsPatient().getVLanguage() != null) {
			for(int i=1; i < languageList.getItems().size(); i++) {
				if(languageList.getItemAtIndex(i).getValue().equals(mr.getMsPatient().getVLanguage()))
					languageList.setSelectedItem(languageList.getItemAtIndex(i));
			}
		}
		
//		Textbox namaIbu = (Textbox)win.getFellow("namaIbu");
//		Textbox namaPasangan = (Textbox)win.getFellow("namaPasangan");
//		namaIbu.setText(mr.getMsPatient().getVPatientMotherName());
//		namaPasangan.setText(mr.getMsPatient().getVPatientPartnerName());
		
		TbRegistration oldReg = this.dao.getLastRegistrationByMrId(mr.getNMrId());
		if(oldReg != null){
			Textbox noRegistrasi = (Textbox)win.getFellow("noRegistrasi");
			noRegistrasi.setValue(oldReg.getVRegSecondaryId());
			noMr.setAttribute("registration", oldReg);
		}

		
	}
	

	public boolean cancelRegistration(Bandbox noMr) throws VONEAppException {
		
		TbRegistration registration = (TbRegistration)noMr.getAttribute("registration");
		List<TbExamination> notaList = dao.getListNoteByRegistration(registration);
		List<TbJournalTrx> journal = dao.getListJournalByCode(registration.getVRegSecondaryId());
		if(notaList.size() > 1){
			try {
				Messagebox.show(MessagesService.getKey("registration.cannot.be.cancelled"), "Konfirmasi", Messagebox.OK, null);
				return false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TbExamination nota = notaList.get(0);
			
		return dao.deleteRegistration(registration, nota, journal);
	}
	
	
	public void getActiveRegistration(Listbox listdata, Date starDate,
			Date endDate) throws VONEAppException {
		listdata.getItems().clear();
		
		List<TbRegistration> regList = this.dao.getActiveRegistration(starDate, endDate);
		Listitem item;
		Listcell cell;
		double jumlahNota;
		double jumlayBayar;
		
		Decimalbox dbox = new Decimalbox();
		dbox.setFormat("#,###");
		
		Set<TbExamination> notaList;
		Set<TbPatientBill> payment;
		
		for(TbRegistration reg : regList){
			jumlahNota = 0;
			jumlayBayar = 0;
			
			item = new Listitem();
			item.setValue(reg);
			item.setParent(listdata);
			
			cell = new Listcell(reg.getVRegSecondaryId());
			cell.setParent(item);
			
			cell = new Listcell(reg.getTbMedicalRecord().getMsPatient().getVPatientName());
			cell.setParent(item);
			
			notaList = reg.getTbExaminations();
			for(TbExamination nota: notaList){
				if(!(nota.getNExamStatus() == MedisafeConstants.CANCEL_NOTE))
					jumlahNota = jumlahNota + nota.getTotal();
				if(nota.getNPaymentStatus() == MedisafeConstants.SUDAH_LUNAS){
					jumlayBayar = jumlayBayar + nota.getTotal();
				}
			}
//			payment = reg.getTbPatientBills();
//			for(TbPatientBill bill : payment){
//				jumlayBayar = jumlayBayar + bill.getNPbillSubTtl();
//			}
			
			dbox.setValue(new BigDecimal(jumlahNota));
			cell = new Listcell(dbox.getText());
			cell.setParent(item);
			
			dbox.setValue(new BigDecimal(jumlayBayar));
			cell = new Listcell(dbox.getText());
			cell.setParent(item);
			
			cell = new Listcell();
			cell.setParent(item);
			if(jumlahNota == jumlayBayar)
				cell.setLabel("LUNAS");
			else cell.setLabel("BELUM LUNAS");
		}
		
	}

	
	public void checkOutRegistraton(Set<Listitem> items)
			throws VONEAppException {
		TbRegistration reg = null;
		for(Listitem item : items){
			reg = (TbRegistration)item.getValue();
			reg.setRegStatus(MedisafeConstants.REG_NON_ACTIVE);
			this.dao.checkOutRegistration(reg);
		}
		
		
	}

	
	public void getRegistrationReport(Date dari, Date sampai, int unitId,
			Listbox result) throws VONEAppException {
		
		result.getItems().clear();
		
		Listitem item = null;
		Listcell cell = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Object[] object;
		
		int jumLaki = 0;
		int jumPer = 0;
		int jumTot = 0;
		int jumLama = 0;
		int jumBaru = 0;
		
		List<Object> obj = this.dao.getLaporanPendaftaran(dari, sampai, unitId);
		for(Object ob : obj){
			item = new Listitem();
			item.setValue(ob);
			item.setParent(result);
			
			object = (Object[])ob;
			Date tgl = (Date)object[0];
			cell = new Listcell(sdf.format(tgl));
			cell.setParent(item);
			
			String unit = (String)object[1];
			cell = new Listcell(unit);
			cell.setParent(item);
			
			Integer laki = (Integer)object[2];
			jumLaki = jumLaki + laki.intValue();
			cell = new Listcell(laki.toString());
			cell.setParent(item);
			
			laki = (Integer)object[3];
			jumPer = jumPer + laki.intValue();
			cell = new Listcell(laki.toString());
			cell.setParent(item);
			
			laki = (Integer)object[4];
			jumLama = jumLama + laki.intValue();
			cell = new Listcell(laki.toString());
			cell.setParent(item);
			
			laki = (Integer)object[5];
			jumBaru = jumBaru + laki.intValue();
			cell = new Listcell(laki.toString());
			cell.setParent(item);
			
			laki = (Integer)object[6];
			jumTot = jumTot + laki.intValue();
			cell = new Listcell(laki.toString());
			cell.setParent(item);
			
			
		}
		item = new Listitem();
		item.setParent(result);
		
		cell = new Listcell();
		cell.setParent(item);
		
		cell = new Listcell(" T O T A L");
		cell.setParent(item);
		
		cell = new Listcell(jumLaki+"");
		cell.setParent(item);
		
		cell = new Listcell(jumPer+"");
		cell.setParent(item);
		
		cell = new Listcell(jumLama+"");
		cell.setParent(item);
		
		cell = new Listcell(jumBaru+"");
		cell.setParent(item);
		
		cell = new Listcell(jumTot+"");
		cell.setParent(item);
	}

	@Override
	public void getPatientOldRegistration(Listbox mrList, int mrStatus)
			throws VONEAppException {
		mrList.getItems().clear();
		
		List<TbRegistration> olds = this.dao.getRegistrationOldPatient(mrStatus);
		for(TbRegistration reg : olds){
			Listitem item = new Listitem();
			item.setValue(reg);
			item.setParent(mrList);
			
			Listcell cell = new Listcell(reg.getTbMedicalRecord().getVMrCode());
			cell.setParent(item);
			
			cell = new Listcell(reg.getTbMedicalRecord().getMsPatient().getVPatientName());
			cell.setParent(item);
			
			cell = new Listcell(reg.getMsUnit() != null ? reg.getMsUnit().getVUnitName():"RAWAT INAP");
			cell.setParent(item);
		}
		
	}

	@Override
	public void updateRegistration(TbRegistration reg) {
		this.dao.updateRegistration(reg);
		
	}
	
	

}

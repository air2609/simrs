package com.vone.medisafe.service.ifaceimpl.master;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.vone.medisafe.antrian.DoctorListener;
import com.vone.medisafe.antrian.MsAntrian;
import com.vone.medisafe.antrian.RemoveAntrianListener;
import com.vone.medisafe.apotik.TakeoutAntrianListener;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsDoctor;
import com.vone.medisafe.mapping.MsDoctorDAO;
import com.vone.medisafe.mapping.MsDoctorRule;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.master.DoctorController;
import com.vone.medisafe.mapping.MsShift;

public class DoctorManagerImpl implements DoctorManager{

	private static final String ALL_SHIFT = "~~ ALL SHIFT ~~";
	private static final String ALL_UNIT = "~~ ALL UNIT ~~";
	private static final String BLANK_STRING = "";
	private MsDoctorDAO dao;
	
	
	public MsDoctorDAO getDao() {
		return dao;
	}

	public void setDao(MsDoctorDAO dao) {
		this.dao = dao;
	}

	public void save(MsDoctor doctor) {
		// TODO Auto-generated method stub
		this.dao.save(doctor);
	}

	public void delete(MsDoctor doctor) {
		// TODO Auto-generated method stub
		this.dao.delete(doctor);
	}

	
	public List getAllDoctor() {
		// TODO Auto-generated method stub
		return this.dao.getAllDoctor(MsDoctor.class);
	}

	public List getDoctorByCriteria(MsDoctor doctor) {
		// TODO Auto-generated method stub
		return this.dao.findByExample(doctor);
	}

	public Integer getTest() throws VONEAppException{
		return this.dao.test();
	}

	public boolean deleteById(Integer id) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}

	public boolean saveDoctor(MsStaff staff, MsDoctor dokterId, Integer unitId) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.saveDoctor(staff, dokterId, unitId);
	}

	public MsDoctor getDoctorByStaff(MsStaff staff) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getDoctorByStaff(staff);
	}

	public List searchDoctor(String code, String name, short role)throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.serarchDoctor(code, name, role);
	}

	

	
	public void getDoctorBaseOnUnit(Integer unitId, Listbox docterListbox) throws VONEAppException {
		
		docterListbox.getItems().clear();
		
		Listitem item = new Listitem();
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(docterListbox);
		
		docterListbox.setSelectedItem(docterListbox.getItemAtIndex(0));

		
		int i = 1;
		
		List<MsDoctor> list = this.dao.getDoctorBaseOnUnitId(unitId);
		
		
		for (MsDoctor doctor : list) {
			
			item = new Listitem();

			item.setValue(doctor.getMsStaff());
			item.setLabel(i + "." + doctor.getMsStaff().getVStaffName());
			item.setParent(docterListbox);
			i++;
			
		}
		
	}
	
	

	public void getDoctorForSelect(Listbox mainDoctor, short grup_dokter) throws VONEAppException {
		
		mainDoctor.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(mainDoctor);

		List<MsDoctor> list = this.dao.getDoctorForSelect(grup_dokter);
		
		for (MsDoctor doctor : list) {
			
			
			item = new Listitem();
			item.setValue(doctor.getMsStaff());
			item.setLabel(doctor.getMsStaff().getVStaffCode()
					+ " " + doctor.getMsStaff().getVStaffName());
			item.setParent(mainDoctor);
			

		}
		
		mainDoctor.setSelectedIndex(0);
		
	}

	public void searchDoctor(String doctorCode, String doctorName, Listbox dokterList, short role) 
		throws VONEAppException {
		dokterList.getItems().clear();
		
		MsStaff staff = null;
		Listitem item = null;
		
		List doctor = searchDoctor(doctorCode, doctorName, role);
		
		Iterator it = doctor.iterator();
		while(it.hasNext()){
			staff = (MsStaff)it.next();
			item = new Listitem();
			item.setValue(staff);
			item.setParent(dokterList);
			
			Listcell code = new Listcell(staff.getVStaffCode());
			code.setParent(item);

			Listcell name = new Listcell(staff.getVStaffName());
			name.setParent(item);

			Set siu = staff.getMsStaffInUnits();
			
			String unit = "";
			Iterator itr = siu.iterator();
			while(itr.hasNext()){
				MsStaffInUnit staffInUnit = (MsStaffInUnit)itr.next();
				unit = unit + staffInUnit.getId().getMsUnit().getVUnitName()+";";
			}
			if(unit.length() > 0){
				unit = unit.substring(0,(unit.length()-1));
			}
			
			Listcell unitcell = new Listcell(unit);
			unitcell.setParent(item);
			/**
			Iterator itr = siu.iterator();
			while(itr.hasNext()){
				MsStaffInUnit staffInUnit = (MsStaffInUnit)itr.next();
				Listcell unitcell = new Listcell(staffInUnit.getId().getMsUnit().getVUnitName());
				unitcell.setValue(staffInUnit.getId().getMsUnit().getNUnitId());
				unitcell.setParent(item);
			}*/
		}
	}

	public MsStaff getStaffByDoctor(MsDoctor doctor) throws VONEAppException {
		return dao.getMsStaff(doctor);
	}

	
	
	public void searchDoctor(Textbox code, Textbox name, Listbox doctorList, short grupMedis) 
		throws VONEAppException {
		
		doctorList.getItems().clear();
		Listitem item;
		Listcell cell;
		
		code.setValue(code.getText().toUpperCase());
		name.setValue(name.getText().toUpperCase());
		
		List<MsDoctor> dokter = dao.searchDocttor("%"+code.getText()+"%", "%"+name.getText()+"%",
								grupMedis);
		
		
		for(MsDoctor msDoctor : dokter){
			
			String unit = "";
			item = new Listitem();
			item.setValue(msDoctor.getMsStaff());
			Set<MsStaffInUnit> sius = msDoctor.getMsStaff().getMsStaffInUnits();
			for(MsStaffInUnit siu : sius ) {
				unit = unit + siu.getId().getMsUnit().getVUnitName()+",";
			}
			item.setAttribute("unit", unit.substring(0, unit.length()-1));
			item.setParent(doctorList);
			
			cell = new Listcell(msDoctor.getMsStaff().getVStaffCode());
			cell.setParent(item);
			
			cell = new Listcell(msDoctor.getMsStaff().getVStaffName());
			cell.setParent(item);
			
		}
		
		
		
	}
	
	

	public void prepareModify(DoctorController controller) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		MsStaff staff = (MsStaff)controller.dokterList.getSelectedItem().getValue();
		MsDoctor doctor = dao.getDoctorByStaff(staff);
		
				
		
		controller.kodeStaff.setValue(staff.getVStaffCode());
		controller.namaStaff.setValue(staff.getVStaffName());
		controller.alamat.setValue(staff.getVStaffAddr());
		controller.noTelp.setValue(staff.getVStaffPhNo());
		
		for(int i=1; i < controller.tingkatKeahlian.getItems().size(); i++){
			if(controller.tingkatKeahlian.getItemAtIndex(i).getValue().equals(doctor.getVDocLvlOfExpertise())){
				controller.tingkatKeahlian.setSelectedIndex(i);
				break;
			}
		}
		for(int i=1; i < controller.medicStaffGroupList.getItems().size(); i++){
			if(controller.medicStaffGroupList.getItemAtIndex(i).getValue().toString()
					.equals(doctor.getNStaffGroup().toString()))
			{
				controller.medicStaffGroupList.setSelectedIndex(i);
				break;
			}
		}
		
		
		if(doctor.getMsStaff().getMsCoa() == null) //do nothing
			System.out.println("coa null");
		else{
			
				controller.coa.setValue(doctor.getMsStaff().getMsCoa().getVAcctNo()+" - "+doctor.getMsStaff().
						getMsCoa().getVAcctName());
			
		}
			
		
		
		
		
		MsStaff doctorStaff = MasterServiceLocator.getDoctorManager().getStaffByDoctor(doctor);
		controller.tglMasuk.setValue(doctorStaff.getDStaffHiredDate());
		controller.tglKeluar.setValue(doctorStaff.getDStaffFiredDate());
		//MasterServiceLocator.getStaffInUnitManager().getStaffInUnitByStaffId(doctor.getMsStaff())
		
		controller.pendapatanRawatJalan.setValue(doctor.getNOutPatientEarnings());
		controller.persentaseRawatInap.setValue(new BigDecimal(doctor.getNPercentageInPatientWage()));
		controller.gaji.setValue(new BigDecimal(staff.getNStaffSalary()));
		controller.noRekening.setValue(doctor.getVDocBankAccNo());
		
		item = controller.dokterList.getSelectedItem();
		cell = (Listcell)item.getChildren().get(2);
		MsUnit unit = (MsUnit)cell.getValue();
		
		for(int i=1; i < controller.unitList.getItems().size(); i++){
			if(((MsUnit)controller.unitList.getItemAtIndex(i).getValue()).getNUnitId().equals(unit.getNUnitId())){
				controller.unitList.setSelectedIndex(i);
			}
		}
//		controller.getDoctorBaseOnUnitId(unit.getNUnitId(), controller.asistenOfList);
		DoctorController.getDoctorBaseOnUnitId(unit.getNUnitId(), controller.asistenOfList);
		
		for(int i=1; i < controller.asistenOfList.getItems().size(); i++){
			if(controller.asistenOfList.getItemAtIndex(i).getValue().toString().equals(BLANK_STRING+doctor.getNAssistenOf())){
				controller.asistenOfList.setSelectedIndex(i);
				break;
			}
		}
		
		for(int i=1; i < controller.statusList.getItems().size(); i++){
			if(controller.statusList.getItemAtIndex(i).getValue().toString().equals(doctor.getVDocStatus())){
				controller.statusList.setSelectedIndex(i);
				break;
			}
		}
		
	}

	public void getAllRule(Listbox dokterFeeList) throws VONEAppException {
		dokterFeeList.getItems().clear();
		List list = dao.getAllRule();
		Iterator iter = list.iterator();
		MsDoctorRule msDoctorRule;
		Listitem item;
		Listcell cell;
		MsUnit msUnit;
		MsShift msShift;
		while (iter.hasNext()) {
			msDoctorRule = (MsDoctorRule) iter.next();
			msShift = dao.getMsShiftById(msDoctorRule.getNShiftId());
			msUnit = dao.getMsUnitById(msDoctorRule.getNUnitId());
			
			item = new Listitem();
			item.setParent(dokterFeeList);
			item.setValue(msDoctorRule);
			
			cell = new Listcell();
			cell.setParent(item);
			if(msUnit != null)
				cell.setLabel(msUnit.getVUnitName());
			else
				cell.setLabel(ALL_UNIT);
			
			cell = new Listcell();
			cell.setParent(item);
			if(msShift != null)
				cell.setLabel(msShift.getVShiftCode());
			else
				cell.setLabel(ALL_SHIFT);
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(msDoctorRule.getVDocStatus());
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(msDoctorRule.getNRsPersen() + BLANK_STRING);
		}
		dokterFeeList.setSelectedIndex(0);
		
	}

	public void getAllShift(Listbox shifList) throws VONEAppException {
		shifList.getItems().clear();
		List list = dao.getAllShift();
		Iterator iter = list.iterator();
		MsShift msShift;
		Listitem item;
		
		item = new Listitem();
		item.setParent(shifList);

		item = new Listitem();
		item.setValue(new Integer(0));
		item.setLabel(ALL_SHIFT);
		item.setParent(shifList);
		
		while (iter.hasNext()) {
			msShift = (MsShift)iter.next();
			item = new Listitem();
			item.setValue(msShift.getNShiftId());
			item.setLabel(msShift.getVShiftCode());
			item.setParent(shifList);
		}
		
	}

	public void getAllUnit(Listbox unitList) throws VONEAppException {
		unitList.getItems().clear();
		List list = dao.getAllUnit();
		Iterator iter = list.iterator();
		MsUnit msUnit;
		Listitem item;
		
		item = new Listitem();
		item.setParent(unitList);

		item = new Listitem();
		item.setValue(new Integer(0));
		item.setLabel(ALL_UNIT);
		item.setParent(unitList);
		
		while (iter.hasNext()) {
			msUnit = (MsUnit)iter.next();
			item = new Listitem();
			item.setValue(msUnit.getNUnitId());
			item.setLabel(msUnit.getVUnitName());
			item.setParent(unitList);
		}
	}

	public void testingCode() throws VONEAppException {
//		getAllRule(new Listbox());
//		List list = dao.getAllRule();
//		Iterator iter = list.iterator();
//		MsDoctorRule msDoctorRule;
//		while (iter.hasNext()) {
//			msDoctorRule = (MsDoctorRule) iter.next();
////			try {
////				System.out.println(msDoctorRule.getMsShift().getVShiftCode());
////				System.out.println(msDoctorRule.getMsUnit().getVUnitName());
////			} catch (RuntimeException e) {
////				// todo Auto-generated catch block
////				e.printStackTrace();
////			}
//		}
	}

	public void saveMsDoctorRule(MsDoctorRule msDoctorRule) throws VONEAppException {
		dao.saveMsDoctorRule(msDoctorRule);
	}

	public void deleteMsDoctorRule(MsDoctorRule msDoctorRule) throws VONEAppException {
		dao.deleteMsDoctorRule(msDoctorRule);
	}

	public List searchDoctor(String code, String name) throws VONEAppException {
		// TODO Auto-generated method stub
		return null;
	}

	public MsStaff getExamDoctor(String doctorExam) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getExamStaff(doctorExam);
	}

	@Override
	public void getMasterAntrian(Intbox delayAntrian, Textbox textAntrian,
			Button btnSimpan, Button btnEdit) throws VONEAppException {
		MsAntrian antrian = this.dao.getAntrian();
		if(antrian != null){
			delayAntrian.setAttribute("antrian", antrian);
			delayAntrian.setValue(antrian.getDelayAntrian());
			textAntrian.setValue(antrian.getAntrianDokter());
			delayAntrian.setReadonly(true);
			textAntrian.setReadonly(true);
			btnSimpan.setDisabled(true);
		}else{
			btnEdit.setDisabled(true);
		}
		
	}

	@Override
	public void saveAntrian(MsAntrian antrian, Intbox delayAntrian,
			Textbox textAntrian, Button btnSimpan, Button btnEdit)
			throws VONEAppException {
		if(this.dao.saveAntrian(antrian)){
			delayAntrian.setAttribute("antrian", antrian);
			btnSimpan.setDisabled(true);
			btnEdit.setDisabled(false);
			delayAntrian.setReadonly(true);
			textAntrian.setReadonly(true);
			
			try {
				Messagebox.show("Data Sukses Disimpan...!", "INFORMATION", Messagebox.OK, null);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void getDelayAntrian(Timer timer, Label stApotik, int i) throws VONEAppException {
		MsAntrian antrian = this.dao.getAntrian();
		int delay = antrian.getDelayAntrian().intValue() * 1000;
		timer.setDelay(delay);
		
		if(i == 1){//text antrian apotik
			stApotik.setValue(antrian.getAntrianApotik());
		}else{
			stApotik.setValue(antrian.getAntrianDokter());
		}
		
	}

	@Override
	public void getAntrianDokter(Window dokterSatu, Window dokterDua,
			Listbox listPasien1, Listbox listPasien2, Label dokterPertama, Label dokterKedua) throws VONEAppException {
		List<MsDoctor> doctors = this.dao.getDoctorForAntrian(0);
		if(doctors.size() == 0){
			doctors = this.dao.getAllDoctor(MsDoctor.class);
			List<MsDoctor> doks = new ArrayList<MsDoctor>();
			for(MsDoctor dok : doctors){
				dok.setFlagAntrian(0);
				doks.add(dok);
			}
			this.dao.resetAntrianDoctor(doks);
			doctors.clear();
			doks.clear();
		}else{
			MsDoctor dokter1 = null;
			MsDoctor dokter2 = null;
			
			if(doctors.size() > 1){
				dokter1 = doctors.get(0);
				dokter2 = doctors.get(1);
			}
			else dokter1 = doctors.get(0);
			
			if(dokter2 == null){
//				dokterDua.setVisible(false);
//				dokterSatu.setTitle(dokter1.getMsStaff().getVStaffName());
				dokterPertama.setValue(dokter1.getMsStaff().getVStaffName());
				getAntrian(dokter1.getMsStaff(), listPasien1);
			}else{
				dokterDua.setVisible(true);
//				dokterSatu.setTitle(dokter1.getMsStaff().getVStaffName());
				dokterPertama.setValue(dokter1.getMsStaff().getVStaffName());
//				dokterDua.setTitle(dokter2.getMsStaff().getVStaffName());
				dokterKedua.setValue(dokter2.getMsStaff().getVStaffName());
				getAntrian(dokter1.getMsStaff(), listPasien1);
				getAntrian(dokter2.getMsStaff(), listPasien2);
			}
			
			List<MsDoctor> dokters = new ArrayList<MsDoctor>();
			dokter1.setFlagAntrian(1);
			dokters.add(dokter1);
			if(dokter2 != null){
				dokter2.setFlagAntrian(1);
				dokters.add(dokter2);
			}
			this.dao.resetAntrianDoctor(dokters);
			dokters.clear();
			
		}
		
	}

	public void getAntrian(MsStaff msStaff, Listbox listPasien) throws HibernateException, VONEAppException {
		listPasien.getItems().clear();
		Listitem item;
		Listcell cell;
		String nomor = "";
		
//		int counter = this.dao.getAntrianPasienDone(msStaff).intValue();
		int counter = 0;
		List<TbRegistration> regs = this.dao.getAntrianPasien(msStaff);
		for(TbRegistration reg : regs){
			item = new Listitem();
			item.setValue(reg);
			item.setParent(listPasien);
//			counter = counter + 1;
			counter = (int)reg.getNEscortPrimaryId();
			if(counter < 10) nomor = "0"+counter;
			else nomor = ""+counter;
			cell = new Listcell(nomor +". "+reg.getTbMedicalRecord().getMsPatient().getVPatientName()+" ("+reg.getTbMedicalRecord().getVMrCode()+ ")");
			cell.setStyle("font-weight:bold;font-size:20pt");
			cell.setParent(item);
//			counter = counter + 1;
			
//			item.addEventListener("onClick", new RemoveAntrianListener(listPasien, item));
		}
		
		if(regs != null)
			regs.clear();
	}

	@Override
	public void getAntrianDoctorController(Listbox doctorList,
			Listbox pasienList) throws VONEAppException {
		doctorList.getItems().clear();
		
		Listitem item;
		List<MsDoctor> doctors = this.dao.getActiveDoctor();
		for(MsDoctor dok : doctors){
			item = new Listitem();
			item.setValue(dok.getMsStaff());
			item.setLabel(dok.getMsStaff().getVStaffName());
			item.setParent(doctorList);
			
		}
		
		if(doctorList.getItems().size() > 0){
			doctorList.setSelectedIndex(0);
			MsStaff staff =(MsStaff)doctorList.getItemAtIndex(0).getValue();
			getAntrian(staff, pasienList);
			
//			doctorList.addEventListener("onSelect", new DoctorListener(doctorList, pasienList));
		}
	}

	@Override
	public void takeOutFromAntrian(TbRegistration reg, Listbox pasList, Listitem item)
			throws VONEAppException {
		reg.setAntrianStatus(1);
		this.dao.updateRegistration(reg);
		
		pasList.removeChild(item);
		
	}
}

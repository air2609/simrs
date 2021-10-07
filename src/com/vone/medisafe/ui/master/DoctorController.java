package com.vone.medisafe.ui.master;

import java.math.BigDecimal;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsDoctor;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.ui.base.BaseController;

public class DoctorController extends BaseController{
	
	private MsStaff staff;
	private MsDoctor doctor;
	
	Listbox doctorSearchList; //listbox dlm bandbox
	
	public Listbox dokterList; //listbox dlm form
	
	public Bandbox kodeStaff;
	public Textbox namaStaff;
	public Textbox alamat;
	public Textbox noTelp;
	public Listbox tingkatKeahlian;
	public Listbox unitList;
	public Listbox medicStaffGroupList;
	public Listbox asistenOfList;
	public Datebox tglMasuk;
	public Datebox tglKeluar;
	public Decimalbox persentaseRawatInap;
	public Intbox pendapatanRawatJalan;
	public Decimalbox gaji;
	public Textbox noRekening;
	public Textbox crKodeStaff;
	public Textbox crNama;
	public Bandbox coa;
	public Listbox statusList; 
	
	Button btnSave;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();
	
	DoctorManager serv = MasterServiceLocator.getDoctorManager();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
				
		super.doCancel(win);
		
		kodeStaff.setValue(null);
		namaStaff.setValue(null);
		alamat.setValue(null);
		noTelp.setValue(null);
		tingkatKeahlian.setSelectedIndex(0);
		medicStaffGroupList.setSelectedIndex(0);
		unitList.setSelectedIndex(0);
		asistenOfList.setSelectedIndex(0);
		tglMasuk.setValue(null);
		tglKeluar.setValue(null);
		persentaseRawatInap.setValue(null);
		pendapatanRawatJalan.setValue(null);
		gaji.setValue(null);
		noRekening.setValue(null);
		kodeStaff.focus();
		coa.setValue(null);
		coa.removeAttribute("coa");
		statusList.setSelectedIndex(0);
		
		dokterList.clearSelection();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		super.doDelete(win);
		if(dokterList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.doctor.doctorlist.notselected"));
			return;
		}
		staff = (MsStaff)dokterList.getSelectedItem().getValue();
		int index = dokterList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getDoctorManager().deleteById(staff.getNStaffId())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				dokterList.removeItemAt(index);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
				
		
		super.doModify(win);
		
		if(dokterList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.doctor.doctorlist.notselected"));
			return;
		}
		
		serv.prepareModify(this);
		

		btnSave.setDisabled(false);
		
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		super.doSaveAdd(win);
		if(MasterServiceLocator.getStaffManager().getStaffByCode(kodeStaff.getText())!= null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		staff = new MsStaff();
		Date createDate = new Date();
		if (tglKeluar.getValue() != null)staff.setDStaffFiredDate(tglKeluar.getValue());
		
		if (tglMasuk.getValue() != null) staff.setDStaffHiredDate(tglMasuk.getValue());
		if (gaji.getValue() != null) staff.setNStaffSalary(gaji.getValue().doubleValue());
		staff.setNStaffRole(MedisafeConstants.DOKTER);
		staff.setVStaffAddr(alamat.getValue());
		staff.setVStaffCode(kodeStaff.getValue());
		staff.setVStaffName(namaStaff.getValue());
		staff.setDWhnCreate(createDate);
		
		MsCoa msCoa = (MsCoa)coa.getAttribute("coa");
		staff.setMsCoa(msCoa);
		
		if (noTelp.getValue().trim().length() > 0)staff.setVStaffPhNo(noTelp.getValue());
		
		
		doctor = new MsDoctor();
		
		Short gid = new Short(medicStaffGroupList.getSelectedItem().getValue().toString());
		doctor.setNStaffGroup(gid);
		
//		Integer gid = (Integer) medicStaffGroupList.getSelectedItem().getValue();
//		doctor.setMsMedicStaffGroup(MasterServiceLocator.getMedicStaffGroupManager().getById(gid));
		
		Integer asId;
		if (asistenOfList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG) {
			asId = (Integer) asistenOfList.getSelectedItem().getValue();
			doctor.setNAssistenOf(asId.longValue());
		}
		if (pendapatanRawatJalan.getValue() != null)
			doctor.setNOutPatientEarnings(pendapatanRawatJalan.getValue());
		if (persentaseRawatInap.getValue() != null)
			doctor.setNPercentageInPatientWage(persentaseRawatInap.getValue().floatValue());
		if (noRekening.getValue().trim().length() > 0)
			doctor.setVDocBankAccNo(noRekening.getValue());
		doctor.setDWhnCreate(createDate);

		doctor.setVDocStatus((String)statusList.getSelectedItem().getValue());

		if(tingkatKeahlian.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG)
			doctor.setVDocLvlOfExpertise(tingkatKeahlian.getSelectedItem().getValue().toString());
		
		MsUnit unit = (MsUnit) unitList.getSelectedItem().getValue();
		if(serv.saveDoctor(staff, doctor, unit.getNUnitId())){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			doCancel(win);
			
			item = new Listitem();
			item.setValue(staff);
			item.setParent(dokterList);

			cell = new Listcell(staff.getVStaffCode());
			cell.setParent(item);

			cell = new Listcell(staff.getVStaffName());
			cell.setParent(item);

			cell = new Listcell(unit.getVUnitName());
			cell.setValue(unit);
			cell.setParent(item);
			
		}else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}

	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
				
		super.doSaveModify(win);
		
		item = dokterList.getSelectedItem();
		staff = (MsStaff)item.getValue();
		
		if (tglKeluar.getValue() != null)staff.setDStaffFiredDate(tglKeluar.getValue());
		if (tglMasuk.getValue() != null) staff.setDStaffHiredDate(tglMasuk.getValue());
		if (gaji.getValue() != null) staff.setNStaffSalary(gaji.getValue().doubleValue());
		staff.setNStaffRole(MedisafeConstants.DOKTER);
		staff.setVStaffAddr(alamat.getValue());
		staff.setVStaffCode(kodeStaff.getValue());
		staff.setVStaffName(namaStaff.getValue());
		
		MsCoa msCoa = (MsCoa)coa.getAttribute("coa");
		staff.setMsCoa(msCoa);
		
		
		
		if (noTelp.getValue().trim().length() > 0)staff.setVStaffPhNo(noTelp.getValue());
		
		
		doctor = MasterServiceLocator.getDoctorManager().getDoctorByStaff(staff);
		
		Short gid = new Short(medicStaffGroupList.getSelectedItem().getValue().toString());
		doctor.setNStaffGroup(gid);
		
//		Integer gid = (Integer) medicStaffGroupList.getSelectedItem().getValue();
//		doctor.setMsMedicStaffGroup(MasterServiceLocator.getMedicStaffGroupManager().getById(gid));
		
		Integer asId;
		if (asistenOfList.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG) {
			asId = (Integer) asistenOfList.getSelectedItem().getValue();
			doctor.setNAssistenOf(asId.longValue());
		}
		if (pendapatanRawatJalan.getValue() != null)
			doctor.setNOutPatientEarnings(pendapatanRawatJalan.getValue());
		if (persentaseRawatInap.getValue() != null)
			doctor.setNPercentageInPatientWage(persentaseRawatInap.getValue().floatValue());
		if (noRekening.getValue().trim().length() > 0)
			doctor.setVDocBankAccNo(noRekening.getValue());
		
		doctor.setVDocStatus((String)statusList.getSelectedItem().getValue());

		doctor.setVDocLvlOfExpertise(tingkatKeahlian.getSelectedItem().getValue().toString());
		
		MsUnit unit = (MsUnit) unitList.getSelectedItem().getValue();
		if(MasterServiceLocator.getDoctorManager().saveDoctor(staff, doctor, unit.getNUnitId())){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			doCancel(win);
			
			item.getChildren().clear();
			item.setValue(staff);
			
			cell = new Listcell(staff.getVStaffCode());
			cell.setParent(item);

			cell = new Listcell(staff.getVStaffName());
			cell.setParent(item);

			cell = new Listcell(unit.getVUnitName());
			cell.setValue(unit);
			cell.setParent(item);
			
		}else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		
		doctorSearchList = (Listbox) win.getFellow("doctorSearchList");

		dokterList = (Listbox) win.getFellow("dokterList");
		kodeStaff = (Bandbox) win.getFellow("kodeStaff");
		namaStaff = (Textbox) win.getFellow("namaStaff");
		alamat = (Textbox) win.getFellow("alamat");
		noTelp = (Textbox) win.getFellow("noTelp");
		tingkatKeahlian = (Listbox) win.getFellow("tingkatKeahlian");
		unitList = (Listbox) win.getFellow("unitList");
		medicStaffGroupList = (Listbox) win.getFellow("medicStaffGroupList");
		asistenOfList = (Listbox) win.getFellow("asistenOfList");
		tglMasuk = (Datebox) win.getFellow("tglMasuk");
		tglKeluar = (Datebox) win.getFellow("tglKeluar");
		persentaseRawatInap = (Decimalbox) win.getFellow("persentaseRawatInap");
		pendapatanRawatJalan = (Intbox) win.getFellow("pendapatanRawatJalan");
		gaji = (Decimalbox) win.getFellow("gaji");
		noRekening = (Textbox) win.getFellow("noRekening");
		crKodeStaff = (Textbox)win.getFellow("crKodeStaff");
		crNama = (Textbox)win.getFellow("crNama");
		coa = (Bandbox)win.getFellow("coa");
		statusList = (Listbox)win.getFellow("statusList");
		
		
		btnSave = (Button)win.getFellow("btnSave");
		
		super.init(win);
		
		constraints.registerComponent(unitList, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(medicStaffGroupList, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kodeStaff, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaStaff, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(alamat, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(statusList, ZulConstraint.NO_EMPTY);

//		constraints.registerComponent(tingkatKeahlian, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kodeStaff, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(namaStaff, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(alamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crKodeStaff, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		asistenOfList.getItems().clear();
		item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(asistenOfList);
		asistenOfList.setSelectedIndex(0);
		
		UnitManager unitService = MasterServiceLocator.getUnitManager();
		
		unitService.getMsUnitForSelect(unitList);
		
//		unitService.getAllUnit(unitList);
//		CoaController.getCoaForSelect(coaList, MedisafeConstants.COA_ALL);
//		MedicStaffGroupController.getMedicStaffGroupList(medicStaffGroupList);
		getDoctorDataList(dokterList);
		kodeStaff.focus();
		
	}
	
	public static void getDoctorBaseOnUnit(Listbox unitListbox, Listbox docterListbox) throws VONEAppException{
		
		
		
		if( unitListbox.getSelectedItem().getValue().equals(MedisafeConstants.LISTKOSONG))return;
		MsUnit unit = (MsUnit) unitListbox.getSelectedItem().getValue();
		
		getDoctorBaseOnUnitId2(unit.getNUnitId(), docterListbox);
	}
	
	public static void getDoctorBaseOnUnitId(Integer unitId, Listbox docterListbox) throws VONEAppException {
		
		MasterServiceLocator.getStaffInUnitManager().getStaffByUnitId(unitId, docterListbox);
		
	}
	
	
	public static void getDoctorBaseOnUnitId2(Integer unitId, Listbox docterListbox) throws VONEAppException{
		
		DoctorManager serv = MasterServiceLocator.getDoctorManager();
		
		
		serv.getDoctorBaseOnUnit(unitId, docterListbox);
		
	}
	
	public static void getDoctorDataList(Listbox dokterList) throws VONEAppException {
		
		MasterServiceLocator.getStaffInUnitManager().getAllDokterInUnit(dokterList);
		MiscTrxController.setFont(dokterList);
	}
	
	public static void getDoctorForSelect(Listbox listbox) throws VONEAppException{
		
		DoctorManager serv = MasterServiceLocator.getDoctorManager();
		
		serv.getDoctorForSelect(listbox, MedisafeConstants.GRUP_DOKTER);
	}
	
	public void doSearchDoctor(Component win) throws WrongValueException, VONEAppException{
		//mengambil daftar dokter berdasar pencarian kode dan nama
		MasterServiceLocator.getDoctorManager().searchDoctor(crKodeStaff.getText(), 
				crNama.getText(), doctorSearchList, MedisafeConstants.DOKTER);
		

	}
	
	public void getDoctorDetail(Component win) throws VONEAppException{
		
		//menampilkan rincian informasi dokter berdasar pilihan dari pencarian 
		//kode dan nama dokter, 
		//dari bandbox
		
		//staff = (MsStaff)dokterList.getSelectedItem().getValue();
		staff = (MsStaff)doctorSearchList.getSelectedItem().getValue();
		doctor = MasterServiceLocator.getDoctorManager().getDoctorByStaff(staff);
		
		
		kodeStaff.setValue(staff.getVStaffCode());
		namaStaff.setValue(staff.getVStaffName());
		alamat.setValue(staff.getVStaffAddr());
		noTelp.setValue(staff.getVStaffPhNo());
		
		for(int i=1; i < tingkatKeahlian.getItems().size(); i++){
			if(tingkatKeahlian.getItemAtIndex(i).getValue().equals(doctor.getVDocLvlOfExpertise())){
				tingkatKeahlian.setSelectedIndex(i);
				break;
			}
		}
		for(int i=1; i < medicStaffGroupList.getItems().size(); i++){
			if(medicStaffGroupList.getItemAtIndex(i).getValue().toString()
					.equals(doctor.getNStaffGroup().toString()))
			{
				medicStaffGroupList.setSelectedIndex(i);
				break;
			}
		}
		//todo KENA LAZY
		
//		tglMasuk.setValue(doctor.getMsStaff().getDStaffHiredDate());
//		tglKeluar.setValue(doctor.getMsStaff().getDStaffFiredDate());
		
		MsStaff doctorStaff = MasterServiceLocator.getDoctorManager().getStaffByDoctor(doctor);
		tglMasuk.setValue(doctorStaff.getDStaffHiredDate());
		tglKeluar.setValue(doctorStaff.getDStaffFiredDate());
		
		
		pendapatanRawatJalan.setValue(doctor.getNOutPatientEarnings());
		persentaseRawatInap.setValue(new BigDecimal(doctor.getNPercentageInPatientWage()));
		gaji.setValue(new BigDecimal(staff.getNStaffSalary()));
		noRekening.setValue(doctor.getVDocBankAccNo());
		
		item = doctorSearchList.getSelectedItem();
//		item = dokterList.getSelectedItem();
		cell = (Listcell)item.getChildren().get(2);
		Integer idUnit = (Integer)cell.getValue();
		MsUnit unit = new MsUnit();
		unit.setNUnitId(idUnit);
		
		for(int i=1; i < unitList.getItems().size(); i++){
			if(((MsUnit)unitList.getItemAtIndex(i).getValue()).getNUnitId().equals(unit.getNUnitId())){
				unitList.setSelectedIndex(i);
			}
		}
		try {
			getDoctorBaseOnUnitId(unit.getNUnitId(), asistenOfList);
		} catch (VONEAppException e) {
			e.printStackTrace();
		}
		
		for(int i=1; i < asistenOfList.getItems().size(); i++){
			if(asistenOfList.getItemAtIndex(i).getValue().toString().equals(""+doctor.getNAssistenOf())){
				asistenOfList.setSelectedIndex(i);
				break;
			}
		}
		
		for(int i=0; i < dokterList.getItems().size(); i++){
			MsStaff staffData = (MsStaff)dokterList.getItemAtIndex(i).getValue();
			if(staffData.getNStaffId().equals(staff.getNStaffId()))
			{
				dokterList.setSelectedIndex(i);
				break;
			}
		}
		btnSave.setDisabled(true);
	}
    
	public static void getAllDoctorList(Listbox allDoctor) throws VONEAppException{
//		allDoctor.getItems().clear();
		Listitem item = new Listitem();
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(allDoctor);
		allDoctor.setSelectedItem(allDoctor.getItemAtIndex(0));
	
//		MsStaffInUnit siu;
		

		//List list = 
		MasterServiceLocator.getStaffInUnitManager().getAllStafInUnit(allDoctor);
	
//		Iterator it = list.iterator();
//		while (it.hasNext()) {
//			siu = (MsStaffInUnit) it.next();
//			item = new Listitem();
//
//			if (siu.getId().getMsStaff().getNStaffRole() == MedisafeConstants.DOKTER) {
//				item.setValue(siu.getId().getMsStaff());
//				item.setLabel(i + "." + siu.getId().getMsStaff().getVStaffName());
//				item.setParent(allDoctor);
//				i++;
//			}
//		}
		
	}
	
	
	public static void searchDoctor(Textbox kodeDokter, Textbox namaDokter, Listbox doctorList, short grup) 
		throws VONEAppException{
		
		DoctorManager serv = MasterServiceLocator.getDoctorManager();
			
		serv.searchDoctor(kodeDokter, namaDokter,doctorList, grup);
		MiscTrxController.setFont(doctorList);
	}
	
	
	
	public static void getDoctor(Bandbox doctor, Listbox doctorList){
		
		MsStaff staff = (MsStaff)doctorList.getSelectedItem().getValue();
		
		doctor.setValue(staff.getVStaffName());
		doctor.setAttribute("doctor", staff);
		
		MiscTrxController.setFont(doctorList);
	}
	
	public void getDoctor(Listbox doctorList, Bandbox doctor){
		
		MsStaff staff = (MsStaff)doctorList.getSelectedItem().getValue();
		
		doctor.setValue(staff.getVStaffCode()+"-"+staff.getVStaffName());
		doctor.setAttribute("doctor", staff);
		
		MiscTrxController.setFont(doctorList);
	}
	

	
	public static void getExamDoctor(Bandbox doctor) throws VONEAppException, InterruptedException{
		
		MsStaff staff = MasterServiceLocator.getDoctorManager().getExamDoctor(doctor.getText());
		
		if (staff == null){
			Messagebox.show(MessagesService.getKey("doctor.not.exist"));
			doctor.focus();
		}
		else
		{
			doctor.setValue(staff.getVStaffName());
			doctor.setAttribute("doctor", staff);
		}
	}
}

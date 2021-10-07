package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.hibernate.HibernateException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.vone.medisafe.antrian.MsAntrian;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsDoctor;
import com.vone.medisafe.mapping.MsDoctorRule;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.ui.master.DoctorController;

public interface DoctorManager {
	
	public void save(MsDoctor doctor) throws VONEAppException;
	
	public void delete(MsDoctor doctor) throws VONEAppException;
	
	public List getAllDoctor() throws VONEAppException;
	
	public List getDoctorByCriteria(MsDoctor doctor) throws VONEAppException;
	
	public Integer getTest() throws VONEAppException;
	
	public boolean deleteById(Integer id) throws VONEAppException;
	
	public boolean saveDoctor(MsStaff staff, MsDoctor dokterId, Integer unitId) throws VONEAppException;
	
	public MsDoctor getDoctorByStaff(MsStaff staff) throws VONEAppException;
	
	public List searchDoctor(String code, String name) throws VONEAppException;
	
	public void getDoctorBaseOnUnit(Integer unitId, Listbox docterListbox) throws VONEAppException;

	public void getDoctorForSelect(Listbox mainDoctor, short grup_dokter) throws VONEAppException;

	public void searchDoctor(String doctorCode, String doctorName, Listbox dokterList, short role) 
		throws VONEAppException;

	public MsStaff getStaffByDoctor(MsDoctor doctor) throws VONEAppException;

	public void searchDoctor(Textbox code, Textbox name, Listbox doctorList, short grupMedis) throws VONEAppException;

	public void prepareModify(DoctorController controller) throws VONEAppException;

	public void getAllUnit(Listbox unitList) throws VONEAppException;

	public void getAllShift(Listbox shifList) throws VONEAppException;

	public void getAllRule(Listbox dokterFeeList) throws VONEAppException;
	
	public void testingCode() throws VONEAppException;

	public void saveMsDoctorRule(MsDoctorRule msDoctorRule) throws VONEAppException;

	public void deleteMsDoctorRule(MsDoctorRule msDoctorRule) throws VONEAppException;

	public MsStaff getExamDoctor(String doctorExam) throws VONEAppException;

	public void getMasterAntrian(Intbox delayAntrian, Textbox textAntrian,
			Button btnSimpan, Button btnEdit) throws VONEAppException;

	public void saveAntrian(MsAntrian antrian, Intbox delayAntrian,
			Textbox textAntrian, Button btnSimpan, Button btnEdit) throws VONEAppException;

	public void getDelayAntrian(Timer timer, Label stApotik, int i) throws VONEAppException;
	
	public void getAntrian(MsStaff msStaff, Listbox listPasien) throws HibernateException, VONEAppException; 

	public void getAntrianDokter(Window dokterSatu, Window dokterDua,
			Listbox listPasien1, Listbox listPasien2, Label dokter1, Label dokter2) throws VONEAppException;

	public void getAntrianDoctorController(Listbox doctorList,
			Listbox pasienList) throws VONEAppException;

	public void takeOutFromAntrian(TbRegistration reg, Listbox pasList, Listitem item) throws VONEAppException;
}

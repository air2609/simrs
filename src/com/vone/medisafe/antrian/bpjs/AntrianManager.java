package com.vone.medisafe.antrian.bpjs;

import java.util.Date;
import java.util.List;

import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsStaff;

public interface AntrianManager {
	
	public void save(MsPolyDoctor pd, Listbox poliDoctorList) throws VONEAppException, InterruptedException;

	public void loadData(Listbox poliDoctorList, Textbox textCariDokter) throws VONEAppException;

	public boolean saveSchedules(List<TbDoctorSchedule> schedules) throws VONEAppException, InterruptedException;

	public List<TbDoctorSchedule> getSchedules(MsStaff staff, String bulan) throws VONEAppException, InterruptedException;

	public List<String> getPolyNames() throws VONEAppException;

	public void getDoctorByPoli(String poli, DaftarController daftarController) throws VONEAppException;
	
	public void register(Button doctorBtn, DaftarController daftarController) throws VONEAppException;

	public void saveQueue(DaftarController daftarController) throws VONEAppException;

	public void getTodayQueue(CounterBpjsController counterBpjsController, Date tanggal) throws VONEAppException;

	public TbAntrian getQueueOnServiceByCounter(Integer bpjsCounter) throws VONEAppException;

	public void saveQueue(TbAntrian current) throws VONEAppException;

	public void displayAntrian(Hbox hboxDisplay, String buttonStyle, String textStyle) throws VONEAppException;

	public void deletePolyDoctor(MsPolyDoctor value) throws VONEAppException;

	public void deleteSchedule(MsStaff staff, Date tgl) throws VONEAppException;

}

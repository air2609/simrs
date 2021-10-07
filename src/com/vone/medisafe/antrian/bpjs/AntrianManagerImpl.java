package com.vone.medisafe.antrian.bpjs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.printing.PrintClient;

public class AntrianManagerImpl implements AntrianManager{
	
	private MsPolyDoctorDAO dao;
	private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat sdfBulan = new SimpleDateFormat("dd MMM yyyy");
	
	String buttonStyle="color:blue;\r\n" + 
			"    border: 1px solid powderblue;\r\n" + 
			"    padding: 5px;\r\n" + 
			"    background-color: #eeeeee;\r\n" + 
			"    font-weight:bold;\r\n" + 
			"    font-size: 20px;\r\n" + 
			"    width : 450px;\r\n" + 
			"    font-family: \"Times New Roman\", Times, serif;";
	String buttonCell="color:blue;\r\n" + 
			"    border: 1px solid powderblue;\r\n" + 
			"    padding: 5px;\r\n" + 
			"    background-color: #eeeeee;\r\n" + 
			"    font-weight:bold;\r\n" + 
			"    font-size: 10px;\r\n" + 
			"    width : 150px;\r\n" + 
			"    font-family: \"Times New Roman\", Times, serif;";
	String rowStyle="padding-left:15px;padding-top:7px; padding-bottom: 7px;background-color: #ffffff;";
	
	String cellStyleHeader = "font-weight:bold;font-size:8pt;color: blue;align:center";
	String cellStyle = "font-size:8pt";

	public MsPolyDoctorDAO getDao() {
		return dao;
	}

	public void setDao(MsPolyDoctorDAO dao) {
		this.dao = dao;
	}

	@Override
	public void save(MsPolyDoctor pd, Listbox poliDoctorList) throws VONEAppException, InterruptedException {
		
		if(this.dao.save(pd)) {
			Messagebox.show("Data sukses disimpan!");
			
			Listitem item = new Listitem();
			item.setValue(pd);
			item.setParent(poliDoctorList);
			
			Listcell cell = new Listcell(pd.getStaff().getVStaffName());
			cell.setParent(item);
			
			cell = new Listcell(pd.getPolyStatus());
			cell.setParent(item);
			
			cell = new Listcell(pd.getMaxPatient().toString());
			cell.setParent(item);
			
			cell = new Listcell(pd.getUnit());
			cell.setParent(item);
			
		}else {
			Messagebox.show("Data gagal disimpan!");
		}
		
			
		
	}

	@Override
	public void loadData(Listbox poliDoctorList, Textbox textCari) throws VONEAppException {
		poliDoctorList.getItems().clear();
		List<MsPolyDoctor> datas = this.dao.getAll("%"+textCari.getValue()+"%");
		for(MsPolyDoctor pd : datas) {
			Listitem item = new Listitem();
			item.setValue(pd);
			item.setParent(poliDoctorList);
			
			Listcell cell = new Listcell(pd.getStaff().getVStaffName());
			cell.setParent(item);
			
			cell = new Listcell(pd.getPolyStatus());
			cell.setParent(item);
			
			cell = new Listcell(pd.getMaxPatient().toString());
			cell.setParent(item);
			
			cell = new Listcell(pd.getUnit());
			cell.setParent(item);
		}
		
	}

	@Override
	public boolean saveSchedules(List<TbDoctorSchedule> schedules) throws VONEAppException, InterruptedException {
		return this.dao.saveSchedules(schedules);
	}

	@Override
	public List<TbDoctorSchedule> getSchedules(MsStaff staff, String bulan)
			throws VONEAppException, InterruptedException {
		// TODO Auto-generated method stub
		return this.dao.getSchedules(staff, bulan);
	}

	@Override
	public List<String> getPolyNames() throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getPolyNames();
	}
	
	private void bookingDoctor(MsPolyDoctor doctor, DaftarController daftarController, Date today) throws VONEAppException{
		//today schedule
		List<TbDoctorSchedule> schedules = this.dao.getDoctorTodaySchedule(doctor.getStaff().getNStaffId(), today);
		if(schedules.size() > 0) {
			Integer jumlahAntrian = this.dao.getAntrianNumber(doctor.getStaff().getNStaffId(), sdf.format(today));
			jumlahAntrian = jumlahAntrian + 1;
			if(jumlahAntrian > doctor.getMaxPatient()) {
				daftarController.rowsDoctor.setVisible(false);
				daftarController.rowsPoli.setVisible(false);
				daftarController.rowsQueueFull.setVisible(true);
				
				daftarController.lblPoli.setVisible(false);
				daftarController.vboxLbDoctor.setVisible(false);
				daftarController.vboxQueueFull.setVisible(true);
				daftarController.lblDoctorName.setValue(doctor.getStaff().getVStaffName());
				
				//get all schedule available after this date
				//then check is already full or not, if not set as next schedule date
				try {
					daftarController.nextDate.getItems().clear();
					List<TbDoctorSchedule> schs = this.dao.getDoctorAllNextSchedule(doctor.getStaff().getNStaffId(), today);
					for(int i=0; i < schs.size(); i++) {
						TbDoctorSchedule sch = schs.get(i);
						jumlahAntrian = this.dao.getAntrianNumber(doctor.getStaff().getNStaffId(), sdf.format(sch.getSchedule()));
						jumlahAntrian = jumlahAntrian + 1;
						if(jumlahAntrian <= doctor.getMaxPatient()) {
							Listitem item = new Listitem();
							item.setValue(sch.getSchedule());
							Listcell cell = new Listcell(sdfBulan.format(sch.getSchedule()));
							cell.setStyle("align:center;font-weight:bold;font-size:30px;height:75px");
							cell.setParent(item);
//							daftarController.nextDate.setValue(sdfBulan.format(sch.getSchedule()));
							item.setParent(daftarController.nextDate);
							daftarController.doctorId = doctor.getStaff().getNStaffId();
							daftarController.doctorScheduleDate = sch.getSchedule();
							daftarController.queueNo = jumlahAntrian;
//							break;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("antrian penuh");
				
				
			}else {
				//create antrian
				TbAntrian antrian = new TbAntrian();
				antrian.setQueueNo(jumlahAntrian);
				antrian.setStaff(doctor.getStaff());
				antrian.setQueueDate(sdf.format(today));
				antrian.setCreatedAt(new Date());
				antrian.setStatus(0);
				antrian.setPoliklinik(daftarController.lblPilihPoli.getValue());
				
				Integer regId = IdsServiceLocator.getIdsManager().getSequence("antrian_bpjs_seq");
				String regNo = getRegNo(regId);
				antrian.setReferralNo(regNo);
				
				//save queue
				if(this.dao.saveQueue(antrian)) {
					//print result;
					//back to queue screen
					System.out.println("nomor antrian : "+antrian.getQueueNo());
					daftarController.rowsPoli.setVisible(true);
					daftarController.rowsDoctor.setVisible(false);
//					daftarController.row
					
					daftarController.lblPoli.setVisible(true);
					daftarController.vboxLbDoctor.setVisible(false);
					
					String ipTarget = daftarController.bpjsNo.getDesktop().getSession().getClientAddr();
					StringBuffer sb = new StringBuffer();
					
					
					
					try {
						//antrian langsung (start with 1) 1;regId;poli;namadokter;jadwalkonsul;nomorantri
						sb.append("1;");
						sb.append(antrian.getReferralNo()+";");
						sb.append(antrian.getPoliklinik()+";");
						sb.append(antrian.getStaff().getVStaffName()+";");
						sb.append(sdfBulan.format(sdf.parse(antrian.getQueueDate()))+";");
						sb.append(antrian.getQueueNo());
						PrintClient.printStringBuffer(ipTarget, sb);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}else {
			daftarController.rowsDoctor.setVisible(false);
			daftarController.rowsPoli.setVisible(false);
			daftarController.rowsQueueFull.setVisible(true);
			
			daftarController.lblPoli.setVisible(false);
			daftarController.vboxLbDoctor.setVisible(false);
			daftarController.vboxQueueFull.setVisible(true);
			daftarController.lblDoctorName.setValue(doctor.getStaff().getVStaffName());
			//next schedules
			
			daftarController.nextDate.getItems().clear();
			List<TbDoctorSchedule> schs = this.dao.getDoctorAllNextSchedule(doctor.getStaff().getNStaffId(), today);
			for(int i=0; i < schs.size(); i++) {
				TbDoctorSchedule sch = schs.get(i);
				Integer jumlahAntrian = this.dao.getAntrianNumber(doctor.getStaff().getNStaffId(), sdf.format(sch.getSchedule()));
				jumlahAntrian = jumlahAntrian + 1;
				if(jumlahAntrian <= doctor.getMaxPatient()) {
					Listitem item = new Listitem();
					item.setValue(sch.getSchedule());
					Listcell cell = new Listcell(sdfBulan.format(sch.getSchedule()));
					cell.setStyle("align:center;font-weight:bold;font-size:30px;height:75px");
					cell.setParent(item);
					item.setParent(daftarController.nextDate);
					daftarController.doctorId = doctor.getStaff().getNStaffId();
					daftarController.doctorScheduleDate = sch.getSchedule();
					daftarController.queueNo = jumlahAntrian;
				}
			}
		}
		
		
	}
	
	@Override
	public void register(Button doctorBtn, DaftarController daftarController) throws VONEAppException{
		Integer staffId = (Integer)doctorBtn.getAttribute("doctorId");
		String tgl = sdf.format(new Date());
		
		MsPolyDoctor doctor = this.dao.getByStaffId(staffId);
		
		if(null != doctor.getBookingFlag() && doctor.getBookingFlag().equals("Y")) {
			try {
				Date today = sdf.parse(tgl);
				bookingDoctor(doctor, daftarController, today);
				return;

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Integer jumlahAntrian = this.dao.getAntrianNumber(staffId, tgl);
		jumlahAntrian = jumlahAntrian + 1;
		if(jumlahAntrian > doctor.getMaxPatient()) {
			//munculkan warning message bahawa antrian penuh
			daftarController.rowsDoctor.setVisible(false);
			daftarController.rowsPoli.setVisible(false);
			daftarController.rowsQueueFull.setVisible(true);
			
			daftarController.lblPoli.setVisible(false);
			daftarController.vboxLbDoctor.setVisible(false);
			daftarController.vboxQueueFull.setVisible(true);
			daftarController.lblDoctorName.setValue(doctor.getStaff().getVStaffName());
			
			//get all schedule available after this date
			//then check is already full or not, if not set as next schedule date
			try {
				Date today = sdf.parse(tgl);
				daftarController.nextDate.getItems().clear();
				List<TbDoctorSchedule> schs = this.dao.getDoctorAllNextSchedule(staffId, today);
				for(int i=0; i < schs.size(); i++) {
					TbDoctorSchedule sch = schs.get(i);
					jumlahAntrian = this.dao.getAntrianNumber(staffId, sdf.format(sch.getSchedule()));
					jumlahAntrian = jumlahAntrian + 1;
					if(jumlahAntrian <= doctor.getMaxPatient()) {
						Listitem item = new Listitem();
						item.setValue(sch.getSchedule());
						Listcell cell = new Listcell(sdfBulan.format(sch.getSchedule()));
						cell.setStyle("align:center;font-weight:bold;font-size:30px;height:75px");
						cell.setParent(item);
//						daftarController.nextDate.setValue(sdfBulan.format(sch.getSchedule()));
						item.setParent(daftarController.nextDate);
						daftarController.doctorId = staffId;
						daftarController.doctorScheduleDate = sch.getSchedule();
						daftarController.queueNo = jumlahAntrian;
//						break;
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("antrian penuh");
			return;
		}
		
		//create antrian
		TbAntrian antrian = new TbAntrian();
		antrian.setQueueNo(jumlahAntrian);
		antrian.setStaff(doctor.getStaff());
		antrian.setQueueDate(tgl);
		antrian.setCreatedAt(new Date());
		antrian.setStatus(0);
		antrian.setPoliklinik(daftarController.lblPilihPoli.getValue());
		
		Integer regId = IdsServiceLocator.getIdsManager().getSequence("antrian_bpjs_seq");
		String regNo = getRegNo(regId);
		antrian.setReferralNo(regNo);
		
		//save queue
		if(this.dao.saveQueue(antrian)) {
			//print result;
			//back to queue screen
			System.out.println("nomor antrian : "+antrian.getQueueNo());
			daftarController.rowsPoli.setVisible(true);
			daftarController.rowsDoctor.setVisible(false);
//			daftarController.row
			
			daftarController.lblPoli.setVisible(true);
			daftarController.vboxLbDoctor.setVisible(false);
			
			String ipTarget = daftarController.bpjsNo.getDesktop().getSession().getClientAddr();
			StringBuffer sb = new StringBuffer();
			
			
			
			try {
				//antrian langsung (start with 1) 1;regId;poli;namadokter;jadwalkonsul;nomorantri
				sb.append("1;");
				sb.append(antrian.getReferralNo()+";");
				sb.append(antrian.getPoliklinik()+";");
				sb.append(antrian.getStaff().getVStaffName()+";");
				sb.append(sdfBulan.format(sdf.parse(antrian.getQueueDate()))+";");
				sb.append(antrian.getQueueNo());
				PrintClient.printStringBuffer(ipTarget, sb);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	private String getRegNo(Integer regId) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		if(regId.toString().length() == 1) return sdf.format(new Date())+"-000"+regId;
		else if(regId.toString().length() == 2) return sdf.format(new Date())+"-00"+regId;
		else if(regId.toString().length() == 3) return sdf.format(new Date())+"-0"+regId;
		else 
			return sdf.format(new Date())+"-"+regId;
	}

	@Override
	public void getDoctorByPoli(String poli, DaftarController daftarController) throws VONEAppException {
		daftarController.rowsDoctor.getChildren().clear();
		List<MsPolyDoctor> bookings = this.dao.getDoctorBookingByPoli(poli);
		List<MsPolyDoctor> doctors = this.dao.getDoctorByPoli(poli);
		
		doctors.addAll(bookings);
		
		int counter = 0;
		Row row = null;
		for(MsPolyDoctor doctor : doctors) {
			if(counter %2 == 0) {
				row = new Row();
				row.setStyle(rowStyle);
				row.setAlign("center");
				row.setParent(daftarController.rowsDoctor);
			}
			Button b = new Button(doctor.getStaff().getVStaffName());
			b.setStyle(buttonStyle);
			b.setAttribute("doctorId", doctor.getStaff().getNStaffId());
			b.addEventListener("onClick", new EventListener() {
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					daftarController.register(b);
				}
			});
			b.setParent(row);
			counter = counter + 1;
		}
	}

	@Override
	public void saveQueue(DaftarController daftarController) throws VONEAppException {
		if(daftarController.bpjsNo.getText().equals("") || daftarController.hpNo.getText().equals("")) {
			return;
		}
		
		
		MsPolyDoctor doctor = this.dao.getByStaffId(daftarController.doctorId);
		TbAntrian antrian = new TbAntrian();
//		antrian.setQueueNo(daftarController.queueNo);
		antrian.setStaff(doctor.getStaff());
//		antrian.setQueueDate(sdf.format(daftarController.doctorScheduleDate));
		Date tnggl = (Date)daftarController.nextDate.getSelectedItem().getValue();
		Integer jumlahAntrian = this.dao.getAntrianNumber(doctor.getStaff().getNStaffId(), sdf.format(tnggl));
		jumlahAntrian = jumlahAntrian + 1;
		antrian.setQueueNo(jumlahAntrian);
		antrian.setQueueDate(sdf.format(tnggl));
		antrian.setCreatedAt(new Date());
		antrian.setStatus(0);
		antrian.setBpjsNo(daftarController.bpjsNo.getValue());
//		antrian.setReferralNo(daftarController.refNo.getValue());
		antrian.setPhoneNo(daftarController.hpNo.getValue());
		
		Integer regId = IdsServiceLocator.getIdsManager().getSequence("antrian_bpjs_seq");
		String regNo = getRegNo(regId);
		antrian.setReferralNo(regNo);
		
		if(this.dao.saveQueue(antrian)) {
			//print result;
			//back to queue screen
			System.out.println("nomor antrian : "+antrian.getQueueNo());
			daftarController.reset();
			daftarController.rowsPoli.setVisible(true);
			daftarController.rowsDoctor.setVisible(false);
//			daftarController.row
			
			daftarController.lblPoli.setVisible(true);
			daftarController.vboxLbDoctor.setVisible(false);
			daftarController.vboxInputBpjs.setVisible(false);
			
			//send data to socket server
			String ipTarget = daftarController.bpjsNo.getDesktop().getSession().getClientAddr();
			StringBuffer sb = new StringBuffer();
			
			try {
				//antrian queue (start with 2) 2;regId;bpjsNo;namadokter;jadwalkonsul;nomorantri
				sb.append("2;");
				sb.append(antrian.getReferralNo()+";");
				sb.append(antrian.getBpjsNo()+";");
				sb.append(doctor.getStaff().getVStaffName()+";");
				sb.append(sdfBulan.format(sdf.parse(antrian.getQueueDate()))+";");
				sb.append(antrian.getQueueNo());
				PrintClient.printStringBuffer(ipTarget, sb);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void getTodayQueue(CounterBpjsController controller, Date tanggal) throws VONEAppException {
		controller.listAntrian.getItems().clear();
		List<TbAntrian> antrians = dao.getTodayQueue(sdf.format(tanggal), "%"+controller.cariDokter.getValue()+"%");
		
		Listitem item2 = new Listitem();
		Listcell cell = new Listcell("NO. URUT");
		cell.setStyle(cellStyleHeader);
		cell.setParent(item2);
		item2.setParent(controller.listAntrian);
		
		cell = new Listcell("DOKTER");
		cell.setStyle(cellStyleHeader);
		cell.setParent(item2);
		
		cell = new Listcell("REG ID");
		cell.setStyle(cellStyleHeader);
		cell.setParent(item2);
		
		cell = new Listcell("NO BPJS");
		cell.setStyle(cellStyleHeader);
		cell.setParent(item2);
		
		cell = new Listcell("NO HP");
		cell.setStyle(cellStyleHeader);
		cell.setParent(item2);
		
		cell = new Listcell("");
		cell.setStyle(cellStyleHeader);
		cell.setParent(item2);
		
		
		for(TbAntrian antrian : antrians) {
			Listitem item = new Listitem();
			item.setValue(antrian);
			cell = new Listcell(antrian.getQueueNo().toString());
			cell.setParent(item);
			cell.setStyle(cellStyle);
			item.setParent(controller.listAntrian);
			
			cell = new Listcell(antrian.getStaff().getVStaffName());
			cell.setStyle(cellStyle);
			cell.setParent(item);
			
			//reg id, need to implement
			cell = new Listcell(antrian.getReferralNo());
			cell.setStyle(cellStyle);
			cell.setParent(item);
			
			cell = new Listcell(antrian.getBpjsNo());
			cell.setStyle(cellStyle);
			cell.setParent(item);
			
			cell = new Listcell(antrian.getPhoneNo());
			cell.setStyle(cellStyle);
			cell.setParent(item);
			
			cell = new Listcell();
			Button b = new Button("LAYANI PASIEN");
			b.setParent(cell);
			b.setStyle(buttonCell);
			if(!sdf.format(new Date()).equals(sdf.format(tanggal)))b.setVisible(false);
			b.addEventListener("onClick", new EventListener() {
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					controller.antrianOnService = antrian;
					controller.changeAntrianToInProgress();
				}
			});
			
			cell.setParent(item);
		}
		
	}

	@Override
	public TbAntrian getQueueOnServiceByCounter(Integer bpjsCounter) throws VONEAppException {
		TbAntrian current = this.dao.getQueueInServices(bpjsCounter, sdf.format(new Date()));
		return current;
	}

	@Override
	public void saveQueue(TbAntrian current) throws VONEAppException {
		this.dao.saveQueue(current);
		
	}

	@Override
	public void displayAntrian(Hbox hboxDisplay, String buttonStyle, String textStyle) throws VONEAppException {
		List<TbAntrian> inServices = this.dao.getQueInProcess(sdf.format(new Date()));
		hboxDisplay.getChildren().clear();
		
		for(TbAntrian antrian : inServices) {
			Vbox vbox = new Vbox();
			vbox.setParent(hboxDisplay);
			
			Button doctor = new Button(antrian.getStaff().getVStaffName());
			doctor.setParent(vbox);
			doctor.setStyle(textStyle);
			
			Button nomor = new Button(antrian.getQueueNo().toString());
			nomor.setParent(vbox);
			nomor.setStyle(buttonStyle);
			
			Button counter = new Button("Di Counter "+antrian.getCounterNo());
			counter.setParent(vbox);
			counter.setStyle(textStyle);
		}
		
	}

	@Override
	public void deletePolyDoctor(MsPolyDoctor value) throws VONEAppException {
		this.dao.deletePolyDoctor(value);
		
	}

	@Override
	public void deleteSchedule(MsStaff staff, Date tgl) throws VONEAppException {
		this.dao.deleteSchedule(staff, tgl);
		
	}

}

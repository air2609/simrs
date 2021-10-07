package com.vone.medisafe.antrian.bpjs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsShift;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.base.BaseController;

public class PoliDokterController extends BaseController{
	
	Bandbox mainDoctor;
	Textbox doctorCode;
	Textbox doctorName;
	Listbox doctorList, poliDoctorList, calList;
	Label lblUnit, lblBulan, gridBulan;
	Listbox statusPoli, bookingList;
	Intbox maxPatient;
	Textbox antrianDari;
	Textbox antrianSampai;
	Textbox deskripsiDokter, textCariDokter;
	Textbox jadwalSenin, jadwalSelasa, jadwalRabu, jadwalKamis, jadwalJumat, jadwalSabtu, jadwalMinggu; 
	Button btnSave, btnModify, btnCancel, btnDelete, btnCari, btnUplaod, btnSaveSchedule;
	Image img;
	MsStaff staff;
	boolean isUpdate = false;
	Popup mailPop, crPopup;
	Rows calCreate;
	 
	
	DoctorManager doctorService = MasterServiceLocator.getDoctorManager();
	AntrianManager antrianService = Service.getAntrianManager();
	List<String> jadwalPraktek = new ArrayList<>();
	SimpleDateFormat bulan = new SimpleDateFormat("MM-yyyy");
	List<Date> jadwals = new ArrayList<>();
	SimpleDateFormat hari = new SimpleDateFormat("d");
	
	Calendar c = Calendar.getInstance();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		mainDoctor = (Bandbox)cmp.getFellow("mainDoctor");
		doctorCode = (Textbox)cmp.getFellow("doctorCode");
		doctorName = (Textbox)cmp.getFellow("doctorName");
		doctorList = (Listbox)cmp.getFellow("doctorList");
		lblUnit = (Label)cmp.getFellow("lblUnit");
		statusPoli = (Listbox)cmp.getFellow("statusPoli");
		bookingList = (Listbox)cmp.getFellow("bookingList");
		maxPatient = (Intbox)cmp.getFellow("maxPatient");
		antrianDari = (Textbox)cmp.getFellow("antrianDari");
		antrianSampai = (Textbox)cmp.getFellow("antrianSampai");
		deskripsiDokter = (Textbox)cmp.getFellow("deskripsiDokter");
		jadwalSenin = (Textbox)cmp.getFellow("jadwalSenin");
		jadwalSelasa = (Textbox)cmp.getFellow("jadwalSelasa");
		jadwalRabu = (Textbox)cmp.getFellow("jadwalRabu");
		jadwalKamis = (Textbox)cmp.getFellow("jadwalKamis");
		jadwalJumat = (Textbox)cmp.getFellow("jadwalJumat");
		jadwalSabtu = (Textbox)cmp.getFellow("jadwalSabtu");
		jadwalMinggu = (Textbox)cmp.getFellow("jadwalMinggu");
		poliDoctorList = (Listbox)cmp.getFellow("poliDoctorList");
		btnSave = (Button)cmp.getFellow("btnSave");
		btnModify = (Button)cmp.getFellow("btnModify");
		btnCancel = (Button)cmp.getFellow("btnCancel");
		btnDelete = (Button)cmp.getFellow("btnDelete");
		btnCari = (Button)cmp.getFellow("btnCari");
		btnSaveSchedule = (Button)cmp.getFellow("btnSaveSchedule");
		img = (Image)cmp.getFellow("img");
		btnUplaod = (Button)cmp.getFellow("btnUplaod");
		mailPop = (Popup)cmp.getFellow("mailPop");
		crPopup = (Popup)cmp.getFellow("crPopup");
		calList = (Listbox)cmp.getFellow("calList");
		lblBulan = (Label)cmp.getFellow("lblBulan");
		calCreate = (Rows)cmp.getFellow("calCreate");
		gridBulan = (Label)cmp.getFellow("gridBulan");
		textCariDokter = (Textbox)cmp.getFellow("textCariDokter");
		
		loadData(); 
		getAllDate(calList);
		getCalCurrentMonth(0);
	}
	
	public void loadData() throws VONEAppException{
		antrianService.loadData(poliDoctorList, textCariDokter);
	}
	
	public void saveSchedule() throws InterruptedException, VONEAppException {
		//check staff id first
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if(staff == null) {
			Messagebox.show("Silahkan pilih data dokter terlebih dahulu!", "Information", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		
		List<String> newList = new ArrayList<String>();
		for(String i : jadwalPraktek)
		{
		    if(!newList.contains(i)) newList.add(i);
		}
		
		jadwals.clear();
		
		for(String jd : newList) {
			try {
				jadwals.add(sdf.parse(jd));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<TbDoctorSchedule> schedules = new ArrayList<>();
		
		for(Date tgl : jadwals) {
			TbDoctorSchedule schedule = new TbDoctorSchedule();
			schedule.setCreatedAt(new Date());
			schedule.setCreatedBy(getUserInfoBean().getStUserName());
			schedule.setSchedule(tgl);
			schedule.setStaff(staff);
			schedule.setMonth(bulan.format(tgl));
			schedules.add(schedule);
		}
		
		if(antrianService.saveSchedules(schedules)) {
			Messagebox.show("Data sukses disimpan!", "Information", Messagebox.OK, Messagebox.INFORMATION);
			jadwals.clear();
			jadwalPraktek.clear();
		}
		
		
	}
	
	private void getDoctorSchedule() throws Exception{
		String bulan = this.bulan.format(c.getTime());
		List<TbDoctorSchedule> schedules = antrianService.getSchedules(staff, bulan);
		
		System.out.println("staff id : "+staff.getNStaffId() + "banyaknya data : "+schedules.size());
		
		if(schedules.size() > 0) {
			btnSaveSchedule.setVisible(false);
			List<Row> rows = calCreate.getChildren();
			for(Row row : rows) {
				List<Button> tgls = row.getChildren();
				for(Button tgl : tgls) {
					for(TbDoctorSchedule sch : schedules) {
						if(tgl.getLabel().equals(hari.format(sch.getSchedule()))) {
//							tgl.setDisabled(true);
							tgl.setStyle("color:blue;font-weight:bold;border: 1px solid powderblue");
						}
					}
				}
			}
		}
		else btnSaveSchedule.setVisible(true);
	}
	
	public void next() throws Exception{
		getCalCurrentMonth(1);
		
		if(poliDoctorList.getSelectedItem() != null) {
			getDoctorSchedule();
		}
		
		
	}
	
	public void prev() throws Exception {
		getCalCurrentMonth(-1);
		

		if(poliDoctorList.getSelectedItem() != null) {
			getDoctorSchedule();
		}
	}
	
	private void getCalCurrentMonth(int prevnext) {
		calCreate.getChildren().clear();
		
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.MONTH,Calendar.SEPTEMBER);
		c.add(Calendar.MONTH, prevnext);
		
	    c.set(Calendar.DAY_OF_MONTH, 1); //hari pertama
	    
	    SimpleDateFormat df = new SimpleDateFormat("EEEE", Locale.ENGLISH);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd");
	    SimpleDateFormat bln = new SimpleDateFormat("MMM-yyyy");
	    gridBulan.setValue(bln.format(c.getTime()).toUpperCase());
	    String hariPertama = df.format(c.getTime());
	    c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH)); //hari terakhir
	    Integer hariTerakhir = new Integer(sdf.format(c.getTime()));
	    
	    
	    int tambah = 0;
	    if(hariPertama.equals("Sunday")) tambah=6;
	    else if(hariPertama.equals("Saturday")) tambah=5;
	    else if(hariPertama.equals("Friday")) tambah=4;
	    else if(hariPertama.equals("Thursday")) tambah=3;
	    else if(hariPertama.equals("Wednesday")) tambah=2;
	    else if(hariPertama.equals("Tuesday")) tambah=1;
	    else if(hariPertama.equals("Monday")) tambah=0;
	    
	    String[] tgls = new String[hariTerakhir+tambah];
	    
	    Row item = null; 
	    boolean isVisible = false;
	    for(int i=0; i < tgls.length;i++) {
	    	if(i < (tambah)) {
	    		tgls[i]="";
	    		isVisible = false;
	    	} 
	    	else {
	    		tgls[i]=""+(i-tambah+1);
	    		isVisible = true;
	    	} 
	    	
	    	if(i%7 == 0) {
	    		item = new Row();
	    		item.setAlign("center");
	    		item.setParent(calCreate);
	    	}
	    	
	    	Button cell = new Button(tgls[i]);
	    	cell.setStyle("border: 1px solid powderblue");
	    	cell.setVisible(isVisible);
	    	if(isVisible) {
	    		cell.addEventListener("onClick", new EventListener() {
					
					@Override
					public void onEvent(Event arg0) throws Exception {
						if(poliDoctorList.getSelectedItem() == null) {return;}
						String tanggl = cell.getLabel()+"-"+bulan.format(c.getTime());
//						cell.setDisabled(true);
						if(btnSaveSchedule.isVisible() == false && cell.getStyle().contains("color:blue") ) {
							System.out.println("menghapus");
							deleteSchedule(tanggl, cell);
							return;
						}
						else if(btnSaveSchedule.isVisible() == false && !cell.getStyle().contains("color:blue") ) {
							System.out.println("menambahkan");
							addSchedule(tanggl, cell);
							return;
						}
						
						if(btnSaveSchedule.isVisible() == true) {
							//save bulk
							System.out.println("save bulk");
							cell.setStyle("color:blue;font-weight:bold;border: 1px solid powderblue");
							jadwalPraktek.add(tanggl);
						}
						
					}
				});
	    	}
	    	cell.setParent(item);
	    }
		
	}
	
	public void deleteSchedule(String tanggal, Button btn) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date tgl = sdf.parse(tanggal);
			int respond = 0;
			
			respond = Messagebox.show("Anda akan menghapus jadwal tanggal "+sdf.format(tgl)+", Anda Yakin?", "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
			
			if (respond != Messagebox.YES)
				return;
			
			antrianService.deleteSchedule(staff, tgl);
			btn.setStyle("color:black;font-weight:bold;border: 1px solid powderblue");
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			
		} catch (ParseException | InterruptedException | VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addSchedule(String tanggal, Button btn) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date tgl = sdf.parse(tanggal);
			int respond = 0;
			
			respond = Messagebox.show("Anda akan menambah jadwal tanggal "+sdf.format(tgl)+", Anda Yakin?", "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
			
			if (respond != Messagebox.YES)
				return;
			
			List<TbDoctorSchedule> schedules = new ArrayList<>();
			
//			for(Date tgl : jadwals) {
				TbDoctorSchedule schedule = new TbDoctorSchedule();
				schedule.setCreatedAt(new Date());
				schedule.setCreatedBy(getUserInfoBean().getStUserName());
				schedule.setSchedule(tgl);
				schedule.setStaff(staff);
				schedule.setMonth(bulan.format(tgl));
				if(!schedules.contains(schedule))schedules.add(schedule);
//			}
			
			if(antrianService.saveSchedules(schedules)) {
				btn.setStyle("color:blue;font-weight:bold;border: 1px solid powderblue");
				Messagebox.show("Data sukses disimpan!", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
			
			
			
		} catch (ParseException | InterruptedException | VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void getAllDate(Listbox calList2) {
		calList2.getItems().clear();
		Calendar c = Calendar.getInstance();   
	    c.set(Calendar.DAY_OF_MONTH, 1); //hari pertama
	    
	    SimpleDateFormat df = new SimpleDateFormat("EEEE", Locale.ENGLISH);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd");
	    SimpleDateFormat bln = new SimpleDateFormat("MMM-yyyy");
	    lblBulan.setValue(bln.format(c.getTime()).toUpperCase());
	    String hariPertama = df.format(c.getTime());
	    c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH)); //hari terakhir
	    Integer hariTerakhir = new Integer(sdf.format(c.getTime()));
	    
	    int tambah = 0;
	    if(hariPertama.equals("Sunday")) tambah=6;
	    else if(hariPertama.equals("Saturday")) tambah=5;
	    else if(hariPertama.equals("Friday")) tambah=4;
	    else if(hariPertama.equals("Thursday")) tambah=3;
	    else if(hariPertama.equals("Wednesday")) tambah=2;
	    else if(hariPertama.equals("Tuesday")) tambah=1;
	    else if(hariPertama.equals("Monday")) tambah=0;
	    
	    String[] tgls = new String[hariTerakhir+tambah];
	    
	    Listitem item = null; 
	    
	    for(int i=0; i < tgls.length;i++) {
	    	if(i < (tambah)) tgls[i]="";
	    	else tgls[i]=""+(i-tambah+1);
	    	
	    	if(i%7 == 0) {
	    		item = new Listitem();
	    		item.setParent(calList2);
	    	}
	    	
	    	Listcell cell = new Listcell(tgls[i]);
	    	cell.setParent(item);
	    }
	    
	    
	    
	    //c.set(Calendar.MONTH, Calendar.AUGUST);
	    //System.out.println(df.format(c.getTime()));
//	    return df.format(c.getTime());
		
	}
	
	public void createSchedule() {
		crPopup.open(crPopup);
	}

	public void open() {
		mailPop.open(mailPop);
		
	}
	
	public void uploadImage() throws InterruptedException {
		Media media = Fileupload.get();
		
		if(media != null && media.getContentType().equals("image/png")) {
			saveFile(media);
			btnUplaod.setVisible(false);
			img.setSrc("/upload/"+ media.getName());
//			img.setContent((org.zkoss.image.Image)media);
			img.setVisible(true);
		}
	}
	
	public void setReadonly(boolean readOnly) {
		mainDoctor.setReadonly(readOnly);
		doctorCode.setReadonly(readOnly);
		doctorName.setReadonly(readOnly);
		statusPoli.setDisabled(readOnly);
		bookingList.setDisabled(readOnly);
		maxPatient.setReadonly(readOnly);
		antrianDari.setReadonly(readOnly);
		antrianSampai.setReadonly(readOnly);
		deskripsiDokter.setReadonly(readOnly);
		jadwalSenin.setReadonly(readOnly);
		jadwalSelasa.setReadonly(readOnly);
		jadwalRabu.setReadonly(readOnly);
		jadwalKamis.setReadonly(readOnly);
		jadwalJumat.setReadonly(readOnly);
		jadwalSabtu.setReadonly(readOnly);
		jadwalMinggu.setReadonly(readOnly);
		btnSave.setDisabled(readOnly);
	}
	
	public void getDoctorData() throws VONEAppException, InterruptedException {
		getCalCurrentMonth(0);
		fillData();
		setReadonly(true);
		String bulan = this.bulan.format(new Date());
		List<TbDoctorSchedule> schedules = antrianService.getSchedules(staff, bulan);
		
		System.out.println("staff id : "+staff.getNStaffId() + "banyaknya data : "+schedules.size());
		
		if(schedules.size() > 0) {
			btnSaveSchedule.setVisible(false);
			List<Row> rows = calCreate.getChildren();
			for(Row row : rows) {
				List<Button> tgls = row.getChildren();
				for(Button tgl : tgls) {
					for(TbDoctorSchedule sch : schedules) {
						if(tgl.getLabel().equals(hari.format(sch.getSchedule()))) {
//							tgl.setDisabled(true);
							tgl.setStyle("color:blue;font-weight:bold;border: 1px solid powderblue");
						}
					}
				}
			}
		}
		else btnSaveSchedule.setVisible(true);
		
		
	}
	
	private void fillData() {
		
		MsPolyDoctor pd = (MsPolyDoctor)poliDoctorList.getSelectedItem().getValue();
		staff = pd.getStaff();
		
		mainDoctor.setText(staff.getVStaffName());
		deskripsiDokter.setValue(pd.getDoctorDescription());
		jadwalJumat.setValue(pd.getFriSchedule());
		maxPatient.setValue(pd.getMaxPatient());
		jadwalSenin.setValue(pd.getMonSchedule());
		if(pd.getPhoto() == null) {
			btnUplaod.setVisible(true);
			img.setVisible(false);
		}else {
			btnUplaod.setVisible(false);
			img.setVisible(true);
			img.setSrc(pd.getPhoto());
		}
		List<Listitem> items = statusPoli.getItems();
		for(Listitem item : items) {
			if(item.getLabel().equals(pd.getPolyStatus()))
				statusPoli.setSelectedItem(item);
		}
		
		if(pd.getBookingFlag() != null) {
			if(pd.getBookingFlag().equals("Y")) bookingList.setSelectedIndex(0);
			else bookingList.setSelectedIndex(1);
		}else {
			bookingList.setSelectedIndex(1);
		}
		
		jadwalSabtu.setValue(pd.getSatSchedule());
		antrianDari.setValue(MedisafeUtil.convertDateToTime(pd.getScheduleFrom()));
		antrianSampai.setValue(MedisafeUtil.convertDateToTime(pd.getScheduleTo()));
		jadwalMinggu.setValue(pd.getSunSchedule());
		jadwalKamis.setValue(pd.getThuSchedule());
		jadwalSelasa.setValue(pd.getTueSchedule());
		lblUnit.setValue(pd.getUnit());
		jadwalRabu.setValue(pd.getWedSchedule());
		
		
	}
	
	public void modify() throws InterruptedException {
		isUpdate = true;
		
		if(poliDoctorList.getSelectedItems().size() == 0) {
			Messagebox.show("Silahkan pilih data yang akan diedit terlebih dahulu!", "Information", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		
		fillData();

		btnModify.setDisabled(true);
		btnCancel.setDisabled(false);
		setReadonly(false);
	}
	
	public void save() throws VONEAppException, InterruptedException {
		
		MsPolyDoctor poli = new MsPolyDoctor();
		
		if(isUpdate) {
			poli = (MsPolyDoctor)poliDoctorList.getSelectedItem().getValue();
		}
		
		poli.setDoctorDescription(deskripsiDokter.getValue());
		poli.setFriSchedule(jadwalJumat.getValue());
		poli.setMaxPatient(maxPatient.getValue());
		poli.setMonSchedule(jadwalSenin.getValue());
		poli.setPhoto(img.getSrc());
		poli.setPolyStatus(statusPoli.getSelectedItem().getLabel());
		poli.setSatSchedule(jadwalSabtu.getValue());
		poli.setScheduleFrom(MedisafeUtil.convertTimeToDate(antrianDari.getText()));
		poli.setScheduleTo(MedisafeUtil.convertTimeToDate(antrianSampai.getText()));
		poli.setStaff(staff);
		poli.setSunSchedule(jadwalMinggu.getValue());
		poli.setThuSchedule(jadwalKamis.getValue());
		poli.setTueSchedule(jadwalSelasa.getValue());
		poli.setUnit(lblUnit.getValue());
		poli.setWedSchedule(jadwalRabu.getValue());
		poli.setBookingFlag(bookingList.getSelectedItem().getValue().toString());
		
		antrianService.save(poli, poliDoctorList);
		loadData();
		reset();
	}
	
	public void reset() {
		mainDoctor.setText(null);
		doctorCode.setText(null);;
		doctorName.setText(null);;
		doctorList.getItems().clear();
		lblUnit.setValue(null);
		statusPoli.setSelectedIndex(0);
		maxPatient.setValue(null);
		antrianDari.setValue(null);
		antrianSampai.setValue(null);;
		deskripsiDokter.setValue(null); 
		jadwalSenin.setValue(null);
		jadwalSelasa.setValue(null);
		jadwalRabu.setValue(null); 
		jadwalKamis.setValue(null);
		jadwalJumat.setValue(null);
		jadwalSabtu.setValue(null);
		jadwalMinggu.setValue(null);
		poliDoctorList.clearSelection();
		jadwalPraktek.clear();
		img.setVisible(false); 
		img.setSrc(null);
		btnUplaod.setVisible(true);
		isUpdate = false;
		btnCancel.setDisabled(true);
		btnModify.setDisabled(false);
		staff = null;
		jadwals.clear();
	}

	private void saveFile(Media media) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream fin = media.getStreamData();			
			in = new BufferedInputStream(fin);
			
			File baseDir = new File(this.getCurrentSession().getWebApp().getRealPath("upload"));

			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			File file = new File(this.getCurrentSession().getWebApp().getRealPath("upload") +"/"+ media.getName());
			
			OutputStream fout = new FileOutputStream(file);
			out = new BufferedOutputStream(fout);
			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) 
					out.close();	
				
				if (in != null)
					in.close();
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
	
	public void searchDoctor() throws VONEAppException{
		doctorService.searchDoctor(this.doctorCode, this.doctorName,this.doctorList, MedisafeConstants.GRUP_DOKTER);
	}
	
	public void getMsStaff() throws VONEAppException{
		
		staff = (MsStaff)doctorList.getSelectedItem().getValue();
		mainDoctor.setValue(staff.getVStaffName());
		
		String unit = (String)doctorList.getSelectedItem().getAttribute("unit");
		lblUnit.setValue(unit);
		mainDoctor.setAttribute("doctor", staff);
		
	}
	
	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		
		if (poliDoctorList.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show("Yakin data yang dipilih akan dihapus?", "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		antrianService.deletePolyDoctor((MsPolyDoctor)this.poliDoctorList.getSelectedItem().getValue());
		
		loadData();
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));
		
	}

}

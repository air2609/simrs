package com.vone.medisafe.service.ifaceimpl.admission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.Bandbox;
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
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.TbRoomReservation;
import com.vone.medisafe.mapping.dao.AntrianKamarDAO;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.admission.AntrianKamarManager;

public class AntrianKamarManagerImpl implements AntrianKamarManager{
	
	private AntrianKamarDAO dao;
	private TbMedicalRecordDAO mrDao;
	private TbRegistrationDAO regDao;
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Listitem item;
	Listcell cell;

	public AntrianKamarDAO getDao() {
		return dao;
	}

	public void setDao(AntrianKamarDAO dao) {
		this.dao = dao;
	}

	
	

	public void save(Window win) throws VONEAppException, InterruptedException {
		
		Bandbox mRNumber = (Bandbox)win.getFellow("mRNumber");
		Textbox name = (Textbox)win.getFellow("name");
		Bandbox ruangan = (Bandbox)win.getFellow("ruangan");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Listbox roomReservationList = (Listbox)win.getFellow("roomReservationList");
		
		TbRegistration reg = (TbRegistration)mRNumber.getAttribute("registration");
		MsHall hall = (MsHall) ruangan.getAttribute("hall");
		
		int cekAntrian = dao.getAntrianBaseOnRegistrationId(reg.getNRegId());
		
		if(cekAntrian > 0){
			Messagebox.show(MessagesService.getKey("sudah.ngantri"));
			return;
		}
		
				
		TbRoomReservation antrian = new TbRoomReservation();
		antrian.setTbRegistration(reg);
		
		if(hall != null)
			antrian.setMsHall(hall);
		
		MsTreatmentClass tclass = (MsTreatmentClass)tclassList.getSelectedItem().getValue();
		
		antrian.setMsTreatmentClass(tclass);
		antrian.setDWhnCreate(new Date());
		
		
		
		if(dao.save(antrian)){
			Messagebox.show(MessagesService.getKey("common.save.success"));
			
			item = new Listitem();
			item.setValue(antrian);
			item.setParent(roomReservationList);
			
			
			cell = new Listcell(mRNumber.getText());
			cell.setParent(item);
			
			cell = new Listcell(name.getText());
			cell.setParent(item);
			
			cell = new Listcell(tclass.getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell();
			cell.setParent(item);
			if(hall != null)
				cell.setLabel(ruangan.getValue());
			else cell.setLabel("-");
			
			cell = new Listcell(sdf.format(antrian.getDWhnCreate()));
			cell.setParent(item);
			
			
			tclassList.setDisabled(true);
			mRNumber.setDisabled(true);
			ruangan.setDisabled(true);
		}
		
//		return dao.save(antrianKamar);
	}

	public boolean delete(TbRoomReservation antrianKamar) throws VONEAppException {
		
		return dao.delete(antrianKamar);
	}

	

	
	
	public void getAntrianList(Listbox roomReservationList) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		List<TbRoomReservation> antrianList = dao.getAntrianList();
		
		for(TbRoomReservation antrian : antrianList){
			item = new Listitem();
			item.setValue(antrian);
			item.setParent(roomReservationList);
			
			cell = new Listcell(antrian.getTbRegistration().getTbMedicalRecord().getVMrCode());
			cell.setParent(item);
			
			cell = new Listcell(antrian.getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName());
			cell.setParent(item);
			
			cell = new Listcell(antrian.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell();
			if(antrian.getMsHall() != null)
				cell.setLabel(antrian.getMsHall().getVHallName());
			else cell.setLabel("-");
			cell.setParent(item);
			
			cell = new Listcell(sdf.format(antrian.getDWhnCreate()));
			cell.setParent(item);
			
			
		}
		
		MiscTrxController.setFont(roomReservationList);
		
	}

	
	public void getRegistrationDetil(Window win, int type) throws VONEAppException, InterruptedException {
		
		Listitem item;
		
		Bandbox mRNumber = (Bandbox)win.getFellow("mRNumber");
		Listbox mrList = (Listbox)win.getFellow("mrList");
		Textbox registrationNumber = (Textbox)win.getFellow("registrationNumber");
		Textbox name = (Textbox)win.getFellow("name");
		Textbox age = (Textbox)win.getFellow("age");
		Radiogroup jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		
		
		TbMedicalRecord mr = null;
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(mRNumber.getText());
			mRNumber.setValue(code);
			
			mr = mrDao.getMrRegistered(code);
			if(mr == null){
				
				Messagebox.show(MessagesService.getKey("mr.not.found"));
				mRNumber.focus();
				
				return;
			}
		}
		
		else{
			item = mrList.getSelectedItem();
			mr = (TbMedicalRecord)item.getValue();
		}
		
		
		TbRegistration reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		
		
		mRNumber.setValue(mr.getVMrCode());
		mRNumber.setAttribute("registration", reg);
		
		name.setValue(mr.getMsPatient().getVPatientName());
		
		int[] umurSkrg = MedisafeUtil.calculateAge(mr.getMsPatient().getDPatientDob());
		age.setValue(umurSkrg[0]+" thn "+umurSkrg[1]+" bln "+umurSkrg[2]+" hr");
		
		if(mr.getMsPatient().getVPatientGender().equals("M")){
			jenisKelamin.setSelectedIndex(0);
		}
		else{
			jenisKelamin.setSelectedIndex(1);
		}
		
		registrationNumber.setValue(reg.getVRegSecondaryId());
		
	}

	
	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	public TbRegistrationDAO getRegDao() {
		return regDao;
	}

	public void setRegDao(TbRegistrationDAO regDao) {
		this.regDao = regDao;
	}
	
	

	public void getAntrianBaseOnHallId(Listbox roomReservationList, Integer id) throws VONEAppException {
		
		roomReservationList.getItems().clear();
		Listitem item;
		Listcell cell;
		
		List<TbRoomReservation> antrianList = dao.getAntrianBaseOnHallId(id);
		
		for(TbRoomReservation antrian : antrianList){
			item = new Listitem();
			item.setValue(antrian);
			item.setParent(roomReservationList);
			
			cell = new Listcell(antrian.getTbRegistration().getTbMedicalRecord().getVMrCode());
			cell.setParent(item);
			
			cell = new Listcell(antrian.getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName());
			cell.setParent(item);
			
			cell = new Listcell(antrian.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell();
			if(antrian.getMsHall() != null)
				cell.setLabel(antrian.getMsHall().getVHallName());
			else cell.setLabel("-");
			cell.setParent(item);
			
			cell = new Listcell(sdf.format(antrian.getDWhnCreate()));
			cell.setParent(item);
			
			
		}
		
	}

}

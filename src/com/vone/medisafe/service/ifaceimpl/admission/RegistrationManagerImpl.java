package com.vone.medisafe.service.ifaceimpl.admission;


import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ListboxListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsBedDAO;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsHallDAO;
import com.vone.medisafe.mapping.MsRoom;
import com.vone.medisafe.mapping.MsRoomDAO;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.TbRoomReservation;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admission.RanapManager;
import com.vone.medisafe.ui.admission.BedListener;



public class RegistrationManagerImpl implements RanapManager{

	private TbRegistrationDAO dao;
	private TbMedicalRecordDAO mrDao;
	private NoteDAO noteDao;
	private MsHallDAO hallDao;
	private MsBedDAO bedDao;
	private MsRoomDAO roomDao;
	private TbBedOccupancyDAO bocDao;
	
	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}

	public MsRoomDAO getRoomDao() {
		return roomDao;
	}

	public void setRoomDao(MsRoomDAO roomDao) {
		this.roomDao = roomDao;
	}

	public MsBedDAO getBedDao() {
		return bedDao;
	}

	public void setBedDao(MsBedDAO bedDao) {
		this.bedDao = bedDao;
	}

	public MsHallDAO getHallDao() {
		return hallDao;
	}

	public void setHallDao(MsHallDAO hallDao) {
		this.hallDao = hallDao;
	}

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}

	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	public TbRegistrationDAO getDao() {
		return dao;
	}

	public void setDao(TbRegistrationDAO dao) {
		this.dao = dao;
	}
	
	

	public void getPatientDetil(Window win, int type) throws VONEAppException, InterruptedException {
		
		Listbox searchList = (Listbox)win.getFellow("patientSearchList");
		
		Textbox tglMasuk = (Textbox)win.getFellow("tglMasuk");
		Listbox historyRajal = (Listbox)win.getFellow("historyRajal");
		Textbox noRegistrasiLama = (Textbox)win.getFellow("noRegistrasiLama");
		Textbox namaPasien = (Textbox)win.getFellow("namaPasien");
		Bandbox noMR = (Bandbox)win.getFellow("noMR");
		Radiogroup jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		
		
		
		TbMedicalRecord mr = null;
		if(type == MedisafeConstants.INPUT_BY_SEARCH){
			
			mr = (TbMedicalRecord)searchList.getSelectedItem().getValue();
			noMR.setAttribute("mr", mr);
			
		}
		else{
			mr = this.mrDao.getPatientMedicalRecord(MedisafeUtil.convertToMrCode(noMR.getText()));
			
			if(mr == null)
			{
				Messagebox.show(MessagesService.getKey("common.data.notfound"));
				noMR.focus();
				return;
			}
			noMR.setAttribute("mr", mr);
			
		}
		
		
		TbRegistration oldReg = dao.getLastRegistrationByMrId(mr.getNMrId());
		if(oldReg != null){
			if(oldReg.getVRegSecondaryId().charAt(0) == 'I'){
				Messagebox.show(MessagesService.getKey("admission.ranap.not.allowed"));
				return;
			}
		
			tglMasuk.setValue(MedisafeUtil.convertDateToString(oldReg.getDRegistrationDate()));
			noRegistrasiLama.setValue(oldReg.getVRegSecondaryId());
			
			List<TbExamination> hasils = noteDao.getNotesByRegistration(oldReg);
			getDetilNotaRajal(hasils,historyRajal);
			
			noMR.setAttribute("oldRegistration", oldReg);
		}
		noMR.setValue(mr.getVMrCode());
		namaPasien.setValue(mr.getMsPatient().getVPatientName());
		if(mr.getMsPatient().getVPatientGender().equals("M"))
			jenisKelamin.setSelectedIndex(0);
		else jenisKelamin.setSelectedIndex(1);
		
		
		
		
	}
	
	
	public void getDetilNotaRajal(List<TbExamination> notas, Listbox historyRajal) throws VONEAppException
	{
		historyRajal.getItems().clear();
		
		Listitem item = null;
		Listcell cell = null;
		
		String keterangan ="";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		for(TbExamination nota : notas){
			
			item = new Listitem();
			item.setValue(nota);
			item.setParent(historyRajal);
			
			cell = new Listcell(sdf.format(nota.getDWhnCreate()));
			cell.setParent(item);
			cell = new Listcell(nota.getVNoteNo());
			cell.setParent(item);
			
			
			List itemTrxs = this.noteDao.getItemTrxBaseOnNote(nota);
			
			Iterator itr = itemTrxs.iterator();
			while(itr.hasNext()){
				Object[] obj = (Object[])itr.next();
				keterangan = keterangan + obj[2].toString() + ", "; 
				
			}
			
			
			Set racikans = nota.getTbDrugIngredients();
			itr = racikans.iterator();
			while(itr.hasNext()){
				TbDrugIngredients drug = (TbDrugIngredients)itr.next();
				keterangan = keterangan + drug.getVDingrId() + ", "; 
			}
			
			
			Set treatmentTrxs = nota.getTbTreatmentTrx();
			itr = treatmentTrxs.iterator();
			while(itr.hasNext()){
		
				TbTreatmentTrx trx = (TbTreatmentTrx)itr.next();
				keterangan = keterangan + trx.getMsTreatmentFee().getMsTreatment().getVTreatmentName() + ", "; 
								
			}
			
			
			Set bundles = nota.getTbBundledTrx();
			itr = bundles.iterator();
			while(itr.hasNext()){
				TbBundledTrx trx = (TbBundledTrx)itr.next();
				
				keterangan = keterangan + trx.getMsTreatmentFee().getMsTreatment()
				.getVTreatmentName() + ", "; 
		
			}
			
								
			Set miscs = nota.getTbMiscTrx();
			itr = miscs.iterator();
			while(itr.hasNext()){
				TbMiscTrx trx = (TbMiscTrx)itr.next();
				keterangan = keterangan + trx.getVMiscName() + ", ";

			}
			if(keterangan.length() > 0)
				keterangan = keterangan.substring(0, keterangan.length()-2);
			
			cell = new Listcell(keterangan);
			cell.setParent(item);
			
			keterangan="";
		}
		
		MiscTrxController.setFont(historyRajal);
		
	}

	
	public void getHallListByTclassId(Integer id, Listbox antriKelasTarif, Listbox searchKelas) 
		throws VONEAppException {
		
		
		 
		Listitem item = null;
		if(searchKelas != null){
			for(int i=0; i < searchKelas.getItems().size(); i++){
				if(!searchKelas.getItemAtIndex(i).getValue().toString().equals(MedisafeConstants.LISTKOSONG)){
					if(((MsTreatmentClass)searchKelas.getItemAtIndex(i).getValue()).getNTclassId().equals(id)){
						searchKelas.setSelectedIndex(i);
					}
				}
			}
		}

		List<MsHall> list = hallDao.getHallByTClassId(id);
		
		
		for(MsHall hall : list){
			
			item = new Listitem();
			item.setValue(hall);
			item.setParent(antriKelasTarif);

			Listcell ruangan = new Listcell(hall.getVHallName());
			ruangan.setParent(item);

			Set rooms =hall.getMsRooms();
			
//			List rooms = MasterServiceLocator.getRoomManager().getRoomsByHall(
//					hall);
			
			Iterator itr = rooms.iterator();
			int jumlahBed = 0;
			int jumlahTerpakai = 0;

			while (itr.hasNext()) {
				MsRoom room = (MsRoom) itr.next();
//				Set beds = room.getMsBeds();
				List beds = MasterServiceLocator.getBedManager().getBedBaseByRoom(room);
				jumlahBed = jumlahBed + beds.size();
				
				Iterator itr2 = beds.iterator();
				int terpakai = 0;
				while (itr2.hasNext()) {
					MsBed bed = (MsBed) itr2.next();
					if (bed.getVBedStatus() != null) {
						if (bed.getVBedStatus().equals("1"))
							terpakai++;
					}
				}
				jumlahTerpakai = jumlahTerpakai + terpakai;
			}
			
			
			int result = jumlahBed - jumlahTerpakai;
			
			Listcell availableBed = new Listcell(""	+ result);
			availableBed.setParent(item);
		}

		
	}
	

	public void saveRanap(Component win) throws VONEAppException,InterruptedException {
		
		MsUser user = (MsUser)Sessions.getCurrent().getAttribute(MedisafeConstants.USER_SESSION);
		
		Listbox antriKelas = (Listbox)win.getFellow("antriKelas");
		Bandbox mainDoctor = (Bandbox)win.getFellow("mainDoctor");
		
		Bandbox bed = (Bandbox)win.getFellow("bed"); 
		Textbox noRegistrasiBaru = (Textbox)win.getFellow("noRegistrasiBaru");
		Bandbox noMR = (Bandbox)win.getFellow("noMR");
		
		
		Textbox tglMasuk = (Textbox)win.getFellow("tglMasuk");
		Textbox jlhRanap = (Textbox)win.getFellow("jlhRanap");
		
		
		
		
		MsStaff staff = (MsStaff) mainDoctor.getAttribute("doctor");
		
		TbMedicalRecord mr = (TbMedicalRecord) noMR.getAttribute("mr");
		
		TbRegistration reg = new TbRegistration();
		
		reg.setRegStatus(new Integer(MedisafeConstants.REG_ACTIVE));
		reg.setMsStaff(staff);
		reg.setVMainDoctorStatus("Y");
		reg.setVWhoCreate(user.getVUserName());
		
		TbRegistration oldReg = (TbRegistration)noMR.getAttribute("oldRegistration");
		
		if(oldReg != null){
			reg.setTbRegistration(oldReg);
			oldReg.setRegStatus(new Integer(MedisafeConstants.REG_NON_ACTIVE));
			oldReg.setVWhoChange(user.getVUserName());
		}
		
		MsBed bedz = bedDao.getBedbyCode(bed.getText());
		if(bedz == null){
			Messagebox.show(MessagesService.getKey("master.bed.notfound"));
			return;
		}
		
		if(bedz.getVBedStatus().equals(MedisafeConstants.BED_TERPAKAI)){
			Messagebox.show(MessagesService.getKey("master.bed.used"));
			return;
		}
		
		TbRoomReservation roomrsv = null;
		
		if(antriKelas.getSelectedItem().getValue() != MedisafeConstants.LISTKOSONG){
			
			
			roomrsv = new TbRoomReservation();
			roomrsv.setMsTreatmentClass((MsTreatmentClass)antriKelas.getSelectedItem().getValue());
			
		}
		
		TbBedOccupancy boc = new TbBedOccupancy();
		
		//check if ranap registration exist and active
		if(this.dao.checkActiveRanap(mr)) {
			Messagebox.show(MessagesService.getKey("admission.ranap.not.allowed"));
			
			return;
		}
		
		if(this.dao.saveRanapRegistration(reg,oldReg,bedz,mr,boc,roomrsv)){
			
			String tanggalMasuk = MedisafeUtil.convertDateToString(boc.getDCheckInTime());
			tglMasuk.setValue(tanggalMasuk);
			
			noRegistrasiBaru.setValue(reg.getVRegSecondaryId());
			
			noRegistrasiBaru.setAttribute("newRegistration", reg);
			bed.setAttribute("boc", boc);
			
			Integer nRanap = dao.countRanap(mr);
			
			if(nRanap != null){
				jlhRanap.setValue(""+nRanap.intValue());
			}
			
						
			Messagebox.show(MessagesService.getKey("common.save.success"));
				
			
		}
		else{
			
			Messagebox.show(MessagesService.getKey("common.save.fail"));
			
		}
	
	}
	

	public boolean cancelRanapRegistration(Bandbox bed, Textbox noRegistrasiBaru, Bandbox noMR) throws VONEAppException {
		
		TbRegistration reg = (TbRegistration)noRegistrasiBaru.getAttribute("newRegistration");
		TbRegistration oldReg = (TbRegistration)noMR.getAttribute("oldRegistration");
		TbBedOccupancy boc = (TbBedOccupancy)bed.getAttribute("boc");
		
		this.dao.cancelRanap(reg, oldReg, boc);
		
		return true;
	}

	
	public void getBedsBaseOnHall(Listbox avaliableBedList, Table bedTable, Bandbox ruangan, Bandbox bbbed)
	throws VONEAppException {
		
		String code = avaliableBedList.getSelectedItem().getLabel();
		
		ruangan.setValue(code);
		
		MsHall hall = (MsHall)avaliableBedList.getSelectedItem().getValue();
		
		getBedBaseOnHallId(hall,bedTable,bbbed);
		
		avaliableBedList.clearSelection();
		
	}
	
	
	public void getBedBaseOnHallId(MsHall hall, Table bedTable, Bandbox bbbed) throws VONEAppException{
	
		List<MsRoom> roomList = this.roomDao.getRoomByHall(hall);
		
		bedTable.getChildren().clear();
		
		bbbed.setValue(null);
		
		for(MsRoom room : roomList){
			
			Tr record = new Tr();
			record.setParent(bedTable);
			
			Set beds = room.getMsBeds();
			
//			List beds = MasterServiceLocator.getBedManager().getBedBaseByRoom(room);
			
			Iterator it2 = beds.iterator();
				while(it2.hasNext()){
					
				MsBed bed = (MsBed)it2.next();
				if(bed.getVBedActiveStatus().equalsIgnoreCase("A")){
					Td column = new Td();
					column.setStyle("width='30%'");
					column.setParent(record);
					
					Listbox listbox = new Listbox();
					listbox.setWidth("130px");
					listbox.setParent(column);
					
					if(bed.getAvailableStatus() == null || bed.getAvailableStatus().equals("A"))
							listbox.addEventListener("onSelect", new BedListener(listbox,bbbed));
									
					Listitem item = new Listitem();
					item.setParent(listbox);
					item.setValue(bed.getVBedDesc());
					Label lbl = new Label(bed.getVBedDesc());
					Listcell bedCode = new Listcell();
					if("B".equals(bed.getAvailableStatus())){
						bedCode.setLabel(bed.getVBedDesc() + " - Dipesan");
					}
					else if("C".equals(bed.getAvailableStatus())){
						bedCode.setLabel(bed.getVBedDesc() + " - Dalam Perbaikan");
					}else bedCode.setLabel(bed.getVBedDesc());
					
					bedCode.setParent(item);

					Table childTable1 = new Table();
					childTable1.setParent(bedCode);
					
					try{
						Tr recordChild = new Tr();
						recordChild.setParent(childTable1);
						
						Td columnChild = new Td();
						columnChild.setParent(recordChild);
						
						Table childTable2 = new Table();
						childTable2.setParent(columnChild);
						
						Tr recordChild2 = new Tr();
						recordChild2.setParent(childTable2);
						Td columnChild2 = new Td();
						columnChild2.setParent(recordChild2);
						Image img = new Image();
						img.setParent(columnChild2);
						img.setSrc("/image/orang2.gif");
						if(bed.getAvailableStatus() == null || bed.getAvailableStatus().equals("A"))
							img.addEventListener("onClick", new ListboxListener(lbl,bbbed));
						
						
						if(bed.getVBedStatus().equals(MedisafeConstants.BED_TERPAKAI)){
							
							// get medical record, patient name, and entry date
							
							TbBedOccupancy boc = bocDao.getBedOccupanciesByBed(bed);

							Tr mrTr = new Tr();
							mrTr.setParent(childTable2);
							Td mrTd = new Td();
							mrTd.setParent(mrTr);
							//								
							Label mrLbl = new Label(boc.getId().getTbRegistration()
									.getTbMedicalRecord().getVMrCode());
							mrLbl.setParent(mrTd);

							Tr trName = new Tr();
							trName.setParent(childTable2);
							Td tdName = new Td();
							tdName.setParent(trName);
							Label lblName = new Label(boc.getId()
									.getTbRegistration().getTbMedicalRecord()
									.getMsPatient().getVPatientName());
							lblName.setParent(tdName);

							Tr trMasuk = new Tr();
							trMasuk.setParent(childTable2);
							Td tdMasuk = new Td();
							tdMasuk.setParent(trMasuk);
							Label lblMasuk = new Label(MedisafeUtil
									.convertDateToString(boc.getDCheckInTime()));
							lblMasuk.setParent(tdMasuk);

							Tr trAntrian = new Tr();
							trAntrian.setParent(childTable2);
							Td tdAntrian = new Td();
							tdAntrian.setParent(trAntrian);
			
								
							
						}
						
						
					}
					catch(Exception e){
						
					}
				}
			}
		}
	}


}

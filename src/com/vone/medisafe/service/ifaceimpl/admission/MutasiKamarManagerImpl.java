package com.vone.medisafe.service.ifaceimpl.admission;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsBedDAO;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.iface.admission.MutasiKamarManager;
import com.vone.medisafe.service.iface.admission.RanapManager;
import com.vone.medisafe.ui.admission.MutasiKamarController;


public class MutasiKamarManagerImpl implements MutasiKamarManager{

	private TbMedicalRecordDAO mrDao;
	private TbRegistrationDAO regDao;
	private TbBedOccupancyDAO bocDao;
	private MsBedDAO bedDao;
	
	Listitem item;
	Listcell cell;
	
	

	public TbRegistrationDAO getRegDao() {
		return regDao;
	}

	public void setRegDao(TbRegistrationDAO regDao) {
		this.regDao = regDao;
	}

	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}
	
	

	public void getPatientRanapDetil(MutasiKamarController controller, int type) 
		throws VONEAppException, InterruptedException{
		
		Integer mrId = null;
		TbMedicalRecord mr = null;
		if(type == MedisafeConstants.INPUT_BY_SEARCH){
			mr = (TbMedicalRecord)controller.patientList.getSelectedItem().getValue();
			mrId = mr.getNMrId();
			
		}
		else{
			mr = mrDao.getMrRegistered(MedisafeUtil.convertToMrCode(controller.noMR.getText()));
			if(mr == null)
			{
				Messagebox.show(MessagesService.getKey("common.data.notfound"));
				controller.noMR.focus();
				return;
			}
			
			mrId = mr.getNMrId();
		}
		
		TbRegistration registration = regDao.getLastRegistrationByMrId(mrId);
		if(registration == null){
			Messagebox.show(MessagesService.getKey("patient.not.registered"));
			return;
		}

		if(registration.getVRegSecondaryId()!= null)
			controller.noRegistrasi.setValue(registration.getVRegSecondaryId());
		controller.noMR.setValue(registration.getTbMedicalRecord().getVMrCode());
		controller.namaPasien.setValue(registration.getTbMedicalRecord().getMsPatient().getVPatientName());
		getHistoryOfBedMove(controller.bedMutasiList,registration);
		controller.patientList.clearSelection();
		controller.btnModify.setDisabled(false);
		controller.antriKelasList.focus(); 
		
	}
	
	
	public void getHistoryOfBedMove(Listbox historyList, TbRegistration reg)
		throws 	InterruptedException, VONEAppException{
		
		historyList.getItems().clear();
		
		
		
		List<TbBedOccupancy> list = bocDao.getHistoryMove(reg);
		
		for(TbBedOccupancy bedOc : list){
			
			item = new Listitem();
			item.setValue(bedOc.getId().getDWhnCreate());
			item.setParent(historyList);
			
			Listcell bedAsal = new Listcell(bedOc.getId().getMsBed().getVBedDesc());
			bedAsal.setParent(item);
			Listcell tglMasuk = new Listcell(MedisafeUtil.convertDateToString(bedOc.getDCheckInTime()));
			tglMasuk.setParent(item);
			Listcell tglKeluar = null;
			Listcell selisih = null;
			if(bedOc.getDCheckOutTime() != null){
				tglKeluar = new Listcell(MedisafeUtil.convertDateToString(bedOc.getDCheckOutTime()));
				tglKeluar.setParent(item);
				
				Calendar calMasuk = Calendar.getInstance();
				Calendar calKeluar = Calendar.getInstance();
				calMasuk.setTime(bedOc.getDCheckInTime());
				calKeluar.setTime(bedOc.getDCheckOutTime());
				int[] calSelisih = MedisafeUtil.getDayDifferent(calKeluar,calMasuk);
				
				selisih= new Listcell((calSelisih[2]+1)+" Hari");
				selisih.setParent(item);
			}
			else{
				tglKeluar = new Listcell("-");
				tglKeluar.setParent(item);
				
				selisih = new Listcell("-");
				selisih.setParent(item);
			}
											
		}
		
		MiscTrxController.setFont(historyList);	
		
	}

	
	
	public void save(MutasiKamarController controller) throws VONEAppException, InterruptedException {
		
				
		MsUser user = (MsUser)Sessions.getCurrent().getAttribute(MedisafeConstants.USER_SESSION);
		
				
		TbRegistration reg = regDao.getRegistrationByRegistrationCode(controller.noRegistrasi.getText());
		
		
		TbBedOccupancy boc = null;
		MsBed bed = bedDao.getBedbyCode(controller.mutasiBed.getText());
		if(bed == null){
			Messagebox.show(MessagesService.getKey("master.bed.notfound"));
			return;
		}
		if(bed.getVBedStatus().equals(MedisafeConstants.BED_TERPAKAI)){
			Messagebox.show(MessagesService.getKey("master.bed.used"));
			return;
		}
		
		
		
		boc = (TbBedOccupancy)controller.win.getPage().getAttribute("boc");
		
		if(boc != null){
				//update
				//update bed dengan input (bed, boc);
			MsBed bedLama = (MsBed)controller.win.getAttribute("bedLama");
			
			
			
			if(bocDao.updateById(bed,bedLama,boc)){
				
				
				getHistoryOfBedMove(controller.bedMutasiList,reg);
				
				
					Messagebox.show(MessagesService.getKey("common.modify.success"));
					controller.setDisable(controller.win, true);
					controller.btnNew.setDisabled(false);
			}
			else{
					Messagebox.show(MessagesService.getKey("common.modify.fail"));
				}
			}
		
		else{
				//insert
//				ambil reg berdasarkan kode registrasi, ambil bed berdasarkan kode bed
			
				
			if(reg != null){
					//ambil bed oc berdasarkan id registrasi
				boc = bocDao.getBedOccupanyByRegId(reg.getNRegId());
				if(boc == null){
					Messagebox.show(MessagesService.getKey("admission.ranap.patient.notregistered"));
					return;
				}	
				
				
					
				//lakukan update terhadap boc dengan input reg, bed dan boc (menggunakan transaksi)
				if(bocDao.updateBocMove(reg,bed,boc, user.getVUserName())){
					
					
//						ambil history move pasien
					getHistoryOfBedMove(controller.bedMutasiList,reg);
					Messagebox.show(MessagesService.getKey("common.modify.success"));
					controller.btnSave.setDisabled(true);
					controller.btnNew.setDisabled(false);
					
					controller.setDisable(controller.win,true);
				}
				else{
					Messagebox.show(MessagesService.getKey("common.modify.fail"));
				}
			}
		}
		
		controller.win.getPage().removeAttribute("boc");
		
		
		
	}
	

	public void modify(MutasiKamarController controller) throws VONEAppException, InterruptedException {
		
		RanapManager ranapService = AdmissionServiceLocator.getRegistrationManager();
		
		controller.setDisable(controller.win, false);
		
		controller.antriKelasList.focus();
		
		controller.btnSave.setDisabled(false);
		
		if(controller.bedMutasiList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("admisssion.bed.mutation.list.notselected"));
			return;
		}

		Date tanggal = (Date) controller.bedMutasiList.getSelectedItem().getValue();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		
		
		TbBedOccupancy boc = bocDao.getByCreateDate(sdf.format(tanggal));
		
		
		
		if(boc.getDCheckOutTime() != null){
			Messagebox.show(MessagesService.getKey("admission.ranap.cannot.modify.bed"));
			return;
		}
		
		controller.noMR.setValue(boc.getId().getTbRegistration().getTbMedicalRecord().getVMrCode());
		controller.mutasiBed.setValue(boc.getId().getMsBed().getVBedDesc());
		controller.ruangan.setValue(boc.getId().getMsBed().getMsRoom().getMsHall().getVHallName());
		controller.noRegistrasi.setValue(boc.getId().getTbRegistration().getVRegSecondaryId());
		controller.namaPasien.setValue(boc.getId().getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName());
		controller.mutasiBed.setValue(boc.getId().getMsBed().getVBedDesc());
		
		for(int i = 1; i < controller.antriKelasList.getItems().size();i++){
			if(((MsTreatmentClass)controller.antriKelasList.getItemAtIndex(i).getValue()).getNTclassId().equals(boc.getId().getMsBed().getMsTreatmentClass().getNTclassId()))
				controller.antriKelasList.setSelectedIndex(i);
		}
		
		
		
		ranapService.getHallListByTclassId(boc.getId().getMsBed().getMsTreatmentClass().getNTclassId(),
				controller.availableBedList, null);
		
		ranapService.getBedBaseOnHallId(boc.getId().getMsBed().getMsRoom().getMsHall(),
				controller.mutasiTable,controller.mutasiBed);
		
		
		
		controller.mutasiBed.setValue(boc.getId().getMsBed().getVBedDesc());

		controller.win.getPage().setAttribute("boc",boc);
		controller.win.setAttribute("bedLama", boc.getId().getMsBed());

		
	}

	public MsBedDAO getBedDao() {
		return bedDao;
	}

	public void setBedDao(MsBedDAO bedDao) {
		this.bedDao = bedDao;
	}

	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}
}

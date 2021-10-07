package com.vone.medisafe.service.ifaceimpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbLaboratoryResult;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.dao.diagnose.TbDiagnoseDAO;
import com.vone.medisafe.service.iface.DiagnoseManager;
import com.vone.medisafe.ui.mr.MedicalRecordDiagnose;

public class DiagnoseManagerImpl implements DiagnoseManager{
	
		private TbMedicalRecordDAO mrDao;
		private TbRegistrationDAO regDao; 
		private TbDiagnoseDAO dao;

		public TbDiagnoseDAO getDao() {
			return dao;
		}

		public void setDao(TbDiagnoseDAO dao) {
			this.dao = dao;
		}

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
		

		public void getMrDetail(Bandbox mrNo, Listbox patientList, Textbox age, Textbox patientName, 
			Radiogroup gender, Textbox patientType, Textbox dateOfBirth, 
			Intbox visitTime, int type) throws VONEAppException,InterruptedException {
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			TbMedicalRecord mr = null;
			Listitem item;
			
			
			if(type == MedisafeConstants.INPUT_BY_MANUAL){
				String code = MedisafeUtil.convertToMrCode(mrNo.getText());
				mrNo.setValue(code);
				
				
				mr = this.mrDao.getPatientMedicalRecord(code);
//					AdmissionServiceLocator.getMedicalRecordManager().getMedicalRecord(code);
				
				if(mr == null){
					try {
						Messagebox.show(MessagesService.getKey("mr.not.found"));
						mrNo.focus();
					} catch (InterruptedException e) {
						
						
					}
					return;
				}
			}
			else{
				item = patientList.getSelectedItem();
				mr = (TbMedicalRecord)item.getValue();
			}
			
			
			
			age.setValue(MedisafeUtil.convertAgeToString(MedisafeUtil.getDateDifferent( new Date(),
					mr.getMsPatient().getDPatientDob())));
			patientName.setValue(mr.getMsPatient().getVPatientName());
			
			if(mr.getMsPatient().getVPatientGender().equalsIgnoreCase("M"))
				gender.setSelectedIndex(0);
			else gender.setSelectedIndex(1);
			
			if(mr.getMsPatient().getMsPatientType() != null)
				patientType.setValue(mr.getMsPatient().getMsPatientType().getVTpatientDesc());
			dateOfBirth.setValue(sdf.format(mr.getMsPatient().getDPatientDob()));
			mrNo.setValue(mr.getVMrCode());
			
			visitTime.setValue(mrDao.countVisit(mr));
			
		}

		public void test() throws VONEAppException{
			

			
		}
		
		

		public void test(Textbox crNoMR, Textbox crNama, Textbox crNoAlamat, Listbox patientList) throws VONEAppException {
			
			Listitem item;
			Listcell cell;
			
			patientList.getItems().clear();
			
			List<TbMedicalRecord> mrList = mrDao.getMrList("%"+crNoMR.getText()+"%","%"+crNama.getText()+"%",
					"%"+crNoAlamat.getText()+"%");
			
			for(TbMedicalRecord mr : mrList){
				
				item = new Listitem();
				item.setValue(mr);
				item.setParent(patientList);
				
				cell = new Listcell(mr.getVMrCode());
				cell.setParent(item);
				
				cell = new Listcell(mr.getMsPatient().getVPatientName());
				cell.setParent(item);
				
				cell = new Listcell(mr.getMsPatient().getVPatientMainAddr());
				cell.setParent(item);
				
			}
		}

		public void testLagi(String string, String string2, String string3) {
			try {
				List<TbMedicalRecord> mrlist = mrDao.getMrList(string, string2, string3);
				
				for(TbMedicalRecord mr : mrlist){
					System.out.println(mr.getVMrCode() + " - "+mr.getMsPatient().getVPatientName());
				}
			} catch (VONEAppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		public void getRegistrationDetail(MedicalRecordDiagnose diagnose, int type) throws VONEAppException{
			
			TbRegistration reg = null;
			String code = null;
			
			if(type == MedisafeConstants.INPUT_BY_MANUAL){
				code = MedisafeUtil.convertToMrCode(diagnose.mrNo.getValue());
				diagnose.mrNo.setValue(code);
			}
			else{
				code = ((TbMedicalRecord)diagnose.patientList.getSelectedItem().getValue()).getVMrCode(); 
				diagnose.mrNo.setValue(code);
			}
			
//			reg = regDao.getRegistrationByCode(code);
			reg = regDao.getLastRegistration(code);
			if(reg == null){
				try {
					Messagebox.show("PASIEN BELUM TERDAFTAR!", "INFORMASI", Messagebox.OK, Messagebox.INFORMATION );
					return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			diagnose.reg = reg;
			diagnose.mr = reg.getTbMedicalRecord();
			diagnose.patient = reg.getTbMedicalRecord().getMsPatient();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			diagnose.dateOfBirth.setValue(sdf.format(reg.getTbMedicalRecord().getMsPatient().getDPatientDob()));
			diagnose.doctorName.setValue(reg.getMsStaff().getVStaffName());
			if(reg.getMsUnit() != null)
				diagnose.doctorName.setAttribute("unitDaftar", reg.getMsUnit().getVUnitName());
			else{
				diagnose.doctorName.setAttribute("unitDaftar", "RAWAT INAP");
			}
			
			if(reg.getTbRegistration() == null){
				diagnose.dignoseType.setSelectedIndex(1);
			}else diagnose.dignoseType.setSelectedIndex(0);
			
			diagnose.patientName.setValue(reg.getTbMedicalRecord().getMsPatient().getVPatientName());
			
			if(reg.getTbMedicalRecord().getMsPatient().getVPatientGender().equalsIgnoreCase("M"))
				diagnose.gender.setSelectedIndex(0);
			else diagnose.gender.setSelectedIndex(1);
			
			List<Listitem> items = diagnose.patientType.getItems();
			for(Listitem item : items){
				if(reg.getTbMedicalRecord().getMsPatient().getMsPatientType() != null)
					if(!item.getValue().equals(MedisafeConstants.LISTKOSONG))
						if(((MsPatientType)item.getValue()).getVTpatientDesc().equals(reg.getTbMedicalRecord().getMsPatient().getMsPatientType().getVTpatientDesc()))
							diagnose.patientType.setSelectedItem(item);
			}
			diagnose.patientType.setDisabled(true);
			
			TbDiagnose diag = this.dao.getDiagnoseByRegId(reg.getNRegId());
			String diagnoseStr = "";
			if(diag != null){
				diagnose.isUpdated = true;
				diagnose.keluahanPasien.setValue(diag.getVSyntom());
				Set<TbIcdDiagnose> icds = diag.getTbIcdDiagnoses();
				for(TbIcdDiagnose icd : icds){
					diagnoseStr = diagnoseStr + icd.getMsIcd().getVIcdName()+",";
				}
				if(diagnoseStr.length() > 1){
					diagnose.diagnoseId.setValue(diagnoseStr.substring(0,diagnoseStr.length()-1));
				}
				diagnose.diagnoseId.setAttribute("diagnose", diag);
			}else{
				//new diagnose
				diagnose.keluahanPasien.setText("BB= KG, TB= CM, Tensimeter= / mmHg");
			}

			
		}

		public void saveDiagnoseAndReceipt(MsPatient patient,TbDiagnose diagnose, TbIcdDiagnose icd, TbExamination nota, List<TbItemTrx> itemTrx, 
				List<TbDrugIngredients> racikans, Integer warehouseId) throws VONEAppException {
			
			//this.dao.save(diagnose);
			//icd.setTbDiagnose(diagnose);
			//this.dao.saveIcdDiagnose(icd);
			
			if(this.dao.saveDiagnoseAndReceipt(patient,diagnose.getNDiagnoseType(), nota, itemTrx,racikans, warehouseId, diagnose, icd)){
				try {
					Messagebox.show("Sukses Menyimpan Data!");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		public void getDiagnoseHistory(TbMedicalRecord mr, Listbox diagoseList) throws VONEAppException {
			
			diagoseList.getItems().clear();
			Listitem item = null;
			Listcell cell = null;
			String strIcd = "";
			String hasilLab = "";
			
			TbRegistration reg;	
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
			if(mr.getNMrId() == null) return;
			
			List<TbDiagnose> diagnoses = this.dao.getDiagnosePatient(mr.getNMrId());
			for(TbDiagnose diagnose : diagnoses){
				item = new Listitem();
				item.setValue(diagnose);
				item.setParent(diagoseList);
				strIcd = "";
				
				//diagnose date
				cell = new Listcell(sdf.format(diagnose.getDWhnCreate()));
				cell.setParent(item);
								
				//unit registered
				cell = new Listcell(diagnose.getVUnitName());
				cell.setParent(item);
				
				//doctor name
				cell = new Listcell(diagnose.getVDoctorName());
				cell.setParent(item);
				
				Set<TbIcdDiagnose> icds = diagnose.getTbIcdDiagnoses();
				for(TbIcdDiagnose icd : icds){
					//syntom
					strIcd = strIcd + icd.getMsIcd().getVIcdName()+",";
				}
				
				cell = new Listcell(diagnose.getVSyntom());
				cell.setParent(item);
				
				//diagnose
				if(strIcd.length() > 1)
					cell = new Listcell(strIcd.substring(0,strIcd.length()-1));
				cell.setParent(item);
				
				//lab result
				cell = new Listcell();
				cell.setParent(item);
				
				hasilLab = "";
				
				reg = this.dao.getRegistrationByCode(diagnose.getNRegId());
				if(reg != null){
					Set<TbExamination> exams = reg.getTbExaminations();
					for(TbExamination exam : exams){
						Set<TbLaboratoryResult> labs = exam.getTbLaboratoryResults();
						
						if(labs.size() > 0){
							for(TbLaboratoryResult lab : labs){
								hasilLab = hasilLab + lab.getVLabRsltCode()+",";
							}
//							hasilLab = hasilLab.substring(0, hasilLab.length()-1);
						}
						
					}
					if(hasilLab.length() > 1){
						cell.setLabel(hasilLab.substring(0, hasilLab.length()-1));
						cell.addEventListener("onClick", new MyLabListiner(cell.getLabel(),reg.getTbMedicalRecord().getVMrCode(),
								reg.getTbMedicalRecord().getMsPatient().getVPatientName(),reg.getTbMedicalRecord().getMsPatient().getVPatientMainAddr(),
								reg.getVRegSecondaryId(), diagoseList));
					}else cell.setLabel("-");
				}else cell.setLabel("-");
				
				
				
				cell = new Listcell();
				cell.setParent(item);
				if(diagnose.getVReceipt() != null){
					if(diagnose.getVReceipt().length() > 0)
						cell.setLabel(diagnose.getVReceipt());
				}
				else{
					cell.setLabel("-");
				}
				
				
			}
			
		}

		
		
}

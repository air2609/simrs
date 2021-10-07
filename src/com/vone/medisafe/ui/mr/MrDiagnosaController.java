package com.vone.medisafe.ui.mr;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbIcd9Diagnose;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbIllness;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbOperation;
import com.vone.medisafe.mapping.TbPregnancy;
import com.vone.medisafe.mapping.TbStillbirth;
import com.vone.medisafe.mapping.TbViolent;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd9cm;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.DiagnoseManager;
import com.vone.medisafe.service.iface.admission.MedicalRecordManager;
import com.vone.medisafe.ui.base.BaseController;

public class MrDiagnosaController extends BaseController{
	
	private TbMedicalRecord mr;
	private boolean isUpdate;
	
	Bandbox mrNo;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox patientList;
	Radiogroup dignoseType;
	Textbox patientName;
	Radiogroup gender;
	Textbox dateOfBirth;
	Textbox age;
	Textbox mainDoctor;
	Textbox unit;
	Textbox patientType;
	Intbox visitTime;
	Listbox conditionList;
	Listbox caraKeluarList;
	Listbox diagnoseList;
	
	Button btnDelete;
	Button btnIcdAdd;
	Button btnIcd9Add;
	
	
	Button btnSave;
	Button btnModify;
	Button btnNew;
	
	Listitem item;
	Listcell cell;
	
	Window win;
	
	Window winroot;
	Tab diagnoseTab;
	Tab codTab;
	
	
	ZulConstraint constrains = new ZulConstraint();
	
	private MsUser user;
	private TbDiagnose diagnose;
	
	private MedicalRecordManager serv = AdmissionServiceLocator.getMedicalRecordManager();

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		mrNo = (Bandbox)win.getFellow("mrNo");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		patientList = (Listbox)win.getFellow("patientList");
		dignoseType = (Radiogroup)win.getFellow("dignoseType");
		patientName = (Textbox)win.getFellow("patientName");
		gender = (Radiogroup)win.getFellow("gender");
		dateOfBirth = (Textbox)win.getFellow("dateOfBirth");
		age = (Textbox)win.getFellow("age");
		mainDoctor = (Textbox)win.getFellow("mainDoctor");
		unit = (Textbox)win.getFellow("unit");
		patientType = (Textbox)win.getFellow("patientType");
		visitTime = (Intbox)win.getFellow("visitTime");
		conditionList = (Listbox)win.getFellow("conditionList");
		caraKeluarList = (Listbox)win.getFellow("caraKeluarList");
		diagnoseList = (Listbox)win.getFellow("diagnoseList");
		
		btnDelete = (Button)win.getFellow("btnDelete");
		btnIcdAdd = (Button)win.getFellow("btnIcdAdd");
		btnIcd9Add = (Button)win.getFellow("btnIcd9Add");
		
		
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnNew = (Button)win.getFellow("btnNew");
		
				
		constrains.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
		constrains.registerComponent(mainDoctor, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(unit, ZulConstraint.UPPER_CASE);
		
		user = getUserInfoBean().getMsUser();
		
		this.win = (Window)win;
		
		clear();
		
	}
	
	
	
	public void clear() throws VONEAppException, InterruptedException{
		this.age.setValue(null);
		this.caraKeluarList.setSelectedIndex(0);
		this.conditionList.setSelectedIndex(0);
		this.crNama.setValue(null);
		this.crNoAlamat.setValue(null);
		this.crNoMR.setValue(null);
		this.dateOfBirth.setValue(null);
		this.diagnoseList.getItems().clear();
		this.dignoseType.setSelectedItem(null);
		this.gender.setSelectedItem(null);
		this.mainDoctor.setValue(null);
		this.mrNo.setValue(null);
		this.patientList.getItems().clear();
		this.unit.setValue(null);
		this.visitTime.setValue(null);
		this.patientName.setValue(null);
		
		setFieldDisable(false);
		
		setButtonDisable(false);
		this.btnModify.setDisabled(true);
		
		mrNo.focus();
		
		this.diagnose = null;
		this.mr = null;
		
		Page page;
		try {
			page = this.win.getDesktop().getPage("codDiagnosePage");
			
			if(page != null)
			{
				Window codWin = (Window)page.getFellow("sebabKematian");
				
				CodController codCtrl = new CodController();
				codCtrl.init(codWin);
			}
			
		} catch (RuntimeException e) {
			//DO NOTHING
		}
		
		
		
		
	}
	
	
	private void setFieldDisable(boolean isDisable){
		
		this.caraKeluarList.setDisabled(isDisable);
		this.conditionList.setDisabled(isDisable);
		this.mainDoctor.setDisabled(isDisable);
		this.mrNo.setDisabled(isDisable);
		this.unit.setDisabled(isDisable);
		
	
	}
	

	public void save() throws VONEAppException, InterruptedException
	{
		List<TbIcdDiagnose> icdList = new ArrayList<TbIcdDiagnose>();
		List<TbIcd9Diagnose> icd9List = new ArrayList<TbIcd9Diagnose>();
		
		List<TbIllness> illness = new ArrayList<TbIllness>();
		List<TbOperation> operation = new ArrayList<TbOperation>();
		List<TbViolent> rudapaksa = new ArrayList<TbViolent>();
		List<TbPregnancy> kehamilan = new ArrayList<TbPregnancy>();
		List<TbStillbirth> keguguran = new ArrayList<TbStillbirth>();
		
		if(this.dignoseType.getSelectedItem() == null || this.diagnoseList.getItems().size() == 0){
			Messagebox.show(MessagesService.getKey("diagnose.type.not.selected"));
			return;
		}
		
		
		if(diagnoseList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("pilih.diagnosa.utama"));
			return;
		}
		
		if(!isUpdate)
			diagnose = new TbDiagnose();
		diagnose.setVWhoCreate(user.getVUserName());
		diagnose.setVDoctorName(this.mainDoctor.getText());
		diagnose.setMr(mr);
		
		if(this.dignoseType.getSelectedItem().getLabel().equalsIgnoreCase("RAWAT INAP"))
			diagnose.setNDiagnoseType(MedisafeConstants.DIAGNOSA_RANAP);
		else 
			diagnose.setNDiagnoseType(MedisafeConstants.DIAGNOSA_RAJAL);
		
		
		if(conditionList.getSelectedItem().getValue().toString().equalsIgnoreCase("Sembuh"))
			diagnose.setNConditionType(MedisafeConstants.SEMBUH);
		
		else if(conditionList.getSelectedItem().getValue().toString().equalsIgnoreCase("Membaik"))
			diagnose.setNConditionType(MedisafeConstants.MEMBAIK);
		
		else if(conditionList.getSelectedItem().getValue().toString().equalsIgnoreCase("Belum Sembuh"))
			diagnose.setNConditionType(MedisafeConstants.BELUM_SEMBUH);
		
		else if(conditionList.getSelectedItem().getValue().toString().equalsIgnoreCase("Mati kurang dari 48 jam"))
			diagnose.setNConditionType(MedisafeConstants.MATI_KURANG_48_JAM);
		
		else if(conditionList.getSelectedItem().getValue().toString().equalsIgnoreCase("Mati lebih dari 48 jam"))
			diagnose.setNConditionType(MedisafeConstants.MATI_LEBIH_48_JAM);
		
		
		
		if(caraKeluarList.getSelectedItem().getValue().toString().equalsIgnoreCase("Diizinkan Pulang"))
		{
			diagnose.setNCheckoutType(MedisafeConstants.DIIZINKAN_PULANG);
		}
		
		else if(caraKeluarList.getSelectedItem().getValue().toString().equalsIgnoreCase("Melarikan Diri")){
			diagnose.setNCheckoutType(MedisafeConstants.MELARIKAN_DIRI);
		}
		
		else if(caraKeluarList.getSelectedItem().getValue().toString().equalsIgnoreCase("Pindah RS Lain")){
			diagnose.setNCheckoutType(MedisafeConstants.PINDAH_KE_RS_LAIN);
		}
		
		else if(caraKeluarList.getSelectedItem().getValue().toString().equalsIgnoreCase("Meninggal")){
			diagnose.setNCheckoutType(MedisafeConstants.MENINGGAL);
			
			Page page = this.win.getDesktop().getPage("codDiagnosePage");
			Window codWin = (Window)page.getFellow("sebabKematian");
			Listbox codList = (Listbox)codWin.getFellow("codList");
			
			if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Karena Penyakit")){
				diagnose.setNDeathType(MedisafeConstants.PENYAKIT);
				
				TbIllness penyakit = null;
				Listbox diseaseList = (Listbox)codWin.getFellow("diseaseList");
				List<Listitem> penyakitList = diseaseList.getItems();
				
				Intbox durasiKematin = (Intbox)codWin.getFellow("duration");
				
				
				for(Listitem item : penyakitList){
					penyakit = new TbIllness();
					penyakit.setVWhoCreate(user.getVUserName());
					penyakit.setDWhnCreate(new Date());
					
					if(durasiKematin.getValue() != null)
						penyakit.setNIllDuration(durasiKematin.getValue().shortValue());
					
					penyakit.setMsIcd((MsIcd)item.getValue());
					
					illness.add(penyakit);
					
				}
			}
			else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Karena Rudapaksa")){
				diagnose.setNDeathType(MedisafeConstants.RUDA_PAKSA);
				Listbox rudaPaksaList = (Listbox)codWin.getFellow("violentList");
				Textbox desc = (Textbox)codWin.getFellow("caraKejadian");
				Textbox damage = (Textbox)codWin.getFellow("bodyDestroy");
				
				TbViolent rudaPaksa = new TbViolent();
				rudaPaksa.setVViolentDesc(desc.getText());
				rudaPaksa.setVViolentDamageDesc(damage.getText());
				rudaPaksa.setVWhoCreate(user.getVUserName());
				rudaPaksa.setDWhnChange(new Date());
				
				if(rudaPaksaList.getSelectedItem().getValue().toString().equalsIgnoreCase("1"))
					rudaPaksa.setNViolentType(MedisafeConstants.BUNUH_DIRI);
				
				else if(rudaPaksaList.getSelectedItem().getValue().toString().equalsIgnoreCase("2"))
					rudaPaksa.setNViolentType(MedisafeConstants.PEMBUNUHAN);
				
				else if(rudaPaksaList.getSelectedItem().getValue().toString().equalsIgnoreCase("3"))
					rudaPaksa.setNViolentType(MedisafeConstants.KECELAKAAN);
				
				
				rudapaksa.add(rudaPaksa);
				
				
			}
			else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Kelahiran Mati")){
				diagnose.setNDeathType(MedisafeConstants.STILBIRTH);
				
				Textbox causeOfStillbirth = (Textbox)codWin.getFellow("causeOfStillbirth");
				
				TbStillbirth stillbirth = new TbStillbirth();
				stillbirth.setVWhoCreate(user.getVUserName());
				stillbirth.setDWhnCreate(new Date());
				stillbirth.setVStillbirthDesc(causeOfStillbirth.getText());
				
				keguguran.add(stillbirth);
			}
			
			else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Persalinan, Kehamilan")){
				diagnose.setNDeathType(MedisafeConstants.PREGNANCY);
				
				Listbox pregnancyList = (Listbox)codWin.getFellow("pregnantList");
				
				TbPregnancy pregnancy = new TbPregnancy();
				pregnancy.setVWhoCreate(user.getVUserName());
				pregnancy.setDWhnCreate(new Date());
				
				if(pregnancyList.getSelectedItem().getValue().toString().equalsIgnoreCase("kehamilan"))
					pregnancy.setNType(MedisafeConstants.KEHAMILAN);
				else if(pregnancyList.getSelectedItem().getValue().toString().equalsIgnoreCase("persalinan"))
					pregnancy.setNType(MedisafeConstants.PERSALINAN);
				
				kehamilan.add(pregnancy);
			}
			else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Operasi")){
				diagnose.setNDeathType(MedisafeConstants.OPERATION);
				
				Textbox operationType = (Textbox)codWin.getFellow("operationType");
				
				TbOperation op = new TbOperation();
				
				op.setVWhoCreate(user.getVUserName());
				op.setDWhnCreate(new Date());
				op.setVTypeOfOperation(operationType.getText());
				
				operation.add(op);
				
			}
			
		}
		
		else if(caraKeluarList.getSelectedItem().getValue().toString().equalsIgnoreCase("Atas Kemauan Sendiri")){
			diagnose.setNCheckoutType(MedisafeConstants.KEMAUAN_SENDIRI);
		}
		
		else if(caraKeluarList.getSelectedItem().getValue().toString().equalsIgnoreCase("Di Rujuk")){
			diagnose.setNCheckoutType(MedisafeConstants.DI_RUJUK);
		}
		
		
		int indeks = this.diagnoseList.getSelectedIndex();
		TbIcdDiagnose icdDiagnose = null;
		TbIcd9Diagnose icd9Diagnose = null;
		
		Listitem selectedItem = this.diagnoseList.getItemAtIndex(indeks);
		if(selectedItem.getValue() instanceof MsIcd9cm){
			Messagebox.show(MessagesService.getKey("diagnosa.utama.harus.icd"));
			return;
		}
		else{
			icdDiagnose = new TbIcdDiagnose();
			icdDiagnose.setMsIcd((MsIcd)selectedItem.getValue());
			icdDiagnose.setNType(MedisafeConstants.DIAGNOSA_UTAMA);
			icdDiagnose.setVWhoCreate(user.getVUserName());
			icdDiagnose.setDWhnCreate(new Date());
			
			icdList.add(icdDiagnose);
			
		}
		
		
		List<Listitem> items = this.diagnoseList.getItems();
		
		for(Listitem item : items){
			if(item.getValue() instanceof MsIcd){
				if(!item.getValue().equals(selectedItem.getValue())){
					
					icdDiagnose = new TbIcdDiagnose();
					icdDiagnose.setMsIcd((MsIcd)item.getValue());
					icdDiagnose.setNType(MedisafeConstants.BUKAN_DIAGNOSA_UTAMA);
					icdDiagnose.setVWhoCreate(user.getVUserName());
					icdDiagnose.setDWhnCreate(new Date());
					
					icdList.add(icdDiagnose);
				}
			}
			
			else{
						
					icd9Diagnose = new TbIcd9Diagnose();
					icd9Diagnose.setMsIcd9cm((MsIcd9cm)item.getValue());
					icd9Diagnose.setVWhoCreate(user.getVUserName());
					icd9Diagnose.setDWhnCreate(new Date());
					
					icd9List.add(icd9Diagnose);
			}
		}
		
		
		if(serv.saveDiagnose(diagnose, icdList,icd9List,illness,operation,rudapaksa,kehamilan,keguguran,isUpdate)){
			
				
			setButtonDisable(true);
			setFieldDisable(true);
			
			if(isUpdate){
				Messagebox.show(MessagesService.getKey("common.modify.success"));
				this.btnModify.setDisabled(true);
			}
			else {
				Messagebox.show(MessagesService.getKey("common.save.success"));
				this.btnModify.setDisabled(false);
			}
			
			
			isUpdate = false;
			
		}
		
		else{
			
			if(isUpdate)Messagebox.show(MessagesService.getKey("common.modify.fail"));
			
			else Messagebox.show(MessagesService.getKey("common.save.fail"));
		}
			
		
		
	}
	
	
	
	private void setButtonDisable(boolean b) {
		
		this.btnDelete.setDisabled(b);
		this.btnIcd9Add.setDisabled(b);
		this.btnIcdAdd.setDisabled(b);
		this.btnModify.setDisabled(b);
		this.btnSave.setDisabled(b);
		
	}

	public void getMr(int type) throws VONEAppException, InterruptedException
	{
		
		DiagnoseManager diagnoseServ = Service.getDiagnoseManager();
		diagnoseServ.getMrDetail(mrNo,patientList, age, patientName,gender,patientType, dateOfBirth,visitTime, type);
		
				
	}
	
	
	
	public void checkIfDeath() throws VONEAppException{
		if(this.caraKeluarList.getSelectedItem().getValue().toString().equalsIgnoreCase("Meninggal")){
		
			Page page = win.getDesktop().getPage("diagnosePage");
			winroot = (Window)page.getFellow("diagnoseRoot");
			diagnoseTab = (Tab)winroot.getFellow("diagnoseTab");
			codTab = (Tab)winroot.getFellow("codTab");
		
			codTab.setSelected(true);
		
		}
	}
	
	
	
	
	public void addIcd() throws VONEAppException, InterruptedException
	{
		Window win = (Window) Executions.createComponents("/zkpages/rekamMedis/tambahICD.zul", null, null);
		win.doModal();
	}
	
	
	public void addIcd9() throws VONEAppException,InterruptedException
	{
		Window win = (Window) Executions.createComponents("/zkpages/rekamMedis/tambahICD-9-CM.zul", null, null);
		win.doModal();
	}
	
	
	
	
	public void modify() throws VONEAppException{
		isUpdate = true;
		setFieldDisable(false);
		
		setButtonDisable(false);
		this.btnModify.setDisabled(true);
	}
	
	
	
	public void deleteList()throws VONEAppException, InterruptedException
	{
		if(diagnoseList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("pilih.data.diagnosa.dari.list"));
			return;
		}
		
		
		int index = diagnoseList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			diagnoseList.removeItemAt(index);
			
		}
	}
	
	public void test() throws VONEAppException{
		
		DiagnoseManager serv = Service.getDiagnoseManager();
		serv.test(crNoMR,crNama,crNoAlamat,patientList);
		
	}

	

}

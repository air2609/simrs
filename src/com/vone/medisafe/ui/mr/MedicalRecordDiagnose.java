package com.vone.medisafe.ui.mr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.Receiver;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.DiagnoseService;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.DiagnoseManager;
import com.vone.medisafe.service.iface.master.PatientTypeManager;
import com.vone.medisafe.service.iface.master.diagnose.IcdManager;
import com.vone.medisafe.ui.base.BaseController;

public class MedicalRecordDiagnose extends BaseController{

	
	public TbMedicalRecord mr;
	public TbRegistration reg;
	public MsPatient patient;	
	
	public Bandbox mrNo;
	public Textbox crNoMR;
	public Textbox crNama;
	public Textbox crNoAlamat;
	public Listbox patientList;
	public Radiogroup dignoseType;
	public Textbox patientName;
	public Radiogroup gender;
	public Textbox dateOfBirth;
	public Textbox doctorName;
	public Textbox keluahanPasien;
	public Bandbox diagnoseId;
	public Textbox diagnoseSearchCode;
	public Textbox diagnoseSearchName;
	public Listbox diagnoseSearchList;
	public Listbox reseplist;
	public Listbox patientType;
	public Decimalbox grandTotal;
	public boolean isUpdated = false;
	
	Button btnDelete;
	Button btnObatAdd;
	Button btnRacikanAdd;
	Button btnSave;
	Button btnNew;
	
	Listitem item;
	Listcell cell;
	
	Window window;
	Session session;
	
	Window winroot;
	Tab diagnosePasienTab;
	Tab diagnosaHistoryTab;
	
	
	ZulConstraint constrains = new ZulConstraint();
	
	private MsUser user;
	private TbDiagnose diagnose;
	
	private DiagnoseManager serv = Service.getDiagnoseManager();  
	private IcdManager icdService = DiagnoseService.getIcdManager();
	PatientTypeManager patientTypeService = MasterServiceLocator.getPatientTypeManager();

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
		doctorName = (Textbox)win.getFellow("doctorName");
		keluahanPasien = (Textbox)win.getFellow("keluahanPasien");
		diagnoseId = (Bandbox) win.getFellow("diagnoseId");
		diagnoseSearchCode = (Textbox)win.getFellow("diagnoseSearchCode");
		diagnoseSearchName = (Textbox)win.getFellow("diagnoseSearchName");
		diagnoseSearchList = (Listbox)win.getFellow("diagnoseSearchList");
		reseplist = (Listbox)win.getFellow("reseplist");
		patientType =(Listbox)win.getFellow("patientType");
		grandTotal = (Decimalbox) win.getFellow("grandTotal");
		
		btnDelete = (Button)win.getFellow("btnDelete");
		btnObatAdd = (Button)win.getFellow("btnObatAdd");
		btnRacikanAdd = (Button)win.getFellow("btnRacikanAdd");
		
		
		btnSave = (Button)win.getFellow("btnSave");
		btnNew = (Button)win.getFellow("btnNew");
		
		patientTypeService.getPatientTypeForSelect(patientType);
				
		constrains.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
		
		user = getUserInfoBean().getMsUser();
		session = win.getDesktop().getSession();
		
		
		
		clear();
 
		
	}
	
	
	
	public void clear() throws VONEAppException, InterruptedException{
		this.crNama.setValue(null);
		this.crNoAlamat.setValue(null);
		this.crNoMR.setValue(null);
		this.dateOfBirth.setValue(null);
		this.dignoseType.setSelectedItem(null);
		this.gender.setSelectedItem(null);
		this.mrNo.setValue(null);
		this.patientList.getItems().clear();
		this.patientName.setValue(null);
		this.diagnoseId.setValue(null);
		this.keluahanPasien.setValue(null);
		this.btnSave.setDisabled(false);
		this.reseplist.getItems().clear();
		this.doctorName.setValue(null);
		this.grandTotal.setValue(null);
		this.patientType.setDisabled(false);
		this.patientType.setSelectedIndex(0);
		diagnoseId.setReadonly(true);
		
	}
	
	
	

	public void save() throws VONEAppException, InterruptedException
	{
		
		
		if(this.mrNo.getText().equalsIgnoreCase("")){
			Messagebox.show("NO MR WAJIB DIISI..", "INFORMASI", Messagebox.OK, Messagebox.INFORMATION );
			mrNo.focus();
			return;
		}
		else if(this.keluahanPasien.getValue().equalsIgnoreCase("")){
			Messagebox.show("KELUHAN PASIEN WAJIB DIISI..", "INFORMASI", Messagebox.OK, Messagebox.INFORMATION );
			keluahanPasien.focus();
			return;
		}
		else if(this.diagnoseId.getText().equalsIgnoreCase("")){
			Messagebox.show("DIAGNOSA WAJIB DIISI..", "INFORMASI", Messagebox.OK, Messagebox.INFORMATION );
			diagnoseId.focus();
			return;
		}
		
		//check aturan pakai
		boolean isWarning = false;
		if(reseplist.getItems().size() > 0){
			List<Listitem> items = this.reseplist.getItems();
			for(Listitem item : items){
			  Textbox aturanPakai = (Textbox) ((Listcell)item.getChildren().get(6)).getChildren().get(0);
			  if(aturanPakai.getValue() == null || aturanPakai.getValue().trim().equals("")) isWarning = true;
			}
		}
		
		if(isWarning){
			Messagebox.show("ATURAN PAKAI HARUS DIISI!", "INFORMASI", Messagebox.OK, Messagebox.INFORMATION );
			return;
		}
		
		Date createdDate = new Date();
		List<TbItemTrx> itemTrx = new ArrayList<TbItemTrx>();
		List<TbDrugIngredients> racikans = new ArrayList<TbDrugIngredients>();
		MsUnit unit = MasterServiceLocator.getUnitManager().getUnitByCode("APTK");
		TbIcdDiagnose icd = null;
		TbDiagnose diagnose = null;
		
		if(isUpdated){
			diagnose = (TbDiagnose)this.diagnoseId.getAttribute("diagnose");
			diagnose.setVSyntom(this.keluahanPasien.getText());
			
			if(this.reseplist.getItems().size() > 0){
				String resep = diagnose.getVReceipt();
				if(resep == null) resep="";
				List<Listitem> items = reseplist.getItems();
				for(Listitem item:items){
					resep = resep + ((Listcell)item.getChildren().get(1)).getLabel()+" "+((Listcell)item.getChildren().get(4)).getLabel()+"\n";
					
				}
				diagnose.setVReceipt(resep);
			}
			
		}else{
			diagnose = new TbDiagnose();
			Set<TbIcdDiagnose> icdDag = new HashSet<TbIcdDiagnose>();
			
			diagnose.setMr(this.mr);
			diagnose.setVDoctorName(this.doctorName.getValue());
			diagnose.setVUnitName(this.doctorName.getAttribute("unitDaftar").toString());
			diagnose.setDWhnCreate(createdDate);
			diagnose.setVWhoCreate(user.getVUserName());
			diagnose.setNRegId(this.reg.getNRegId());
			
			if(this.reseplist.getItems().size() > 0){
				String resep = "";
				List<Listitem> items = reseplist.getItems();
				for(Listitem item:items){
					resep = resep + ((Listcell)item.getChildren().get(1)).getLabel()+" "+((Listcell)item.getChildren().get(4)).getLabel()+",";
					
				}
				diagnose.setVReceipt(resep.substring(0, resep.length()-1));
			}
			
			Set<Listitem> items = this.diagnoseSearchList.getSelectedItems();
			for(Listitem item : items){
				icd = new TbIcdDiagnose();
				icd.setMsIcd((MsIcd)item.getValue());
				icd.setDWhnCreate(createdDate);
				icd.setVWhoChange(user.getVUserName());
				icdDag.add(icd);
			}
			diagnose.setTbIcdDiagnoses(icdDag);
			diagnose.setVSyntom(keluahanPasien.getText());
		}
		
		
		TbExamination nota = null;
		if(reseplist.getItems().size() > 0){
			nota = new TbExamination();
			nota.setDWhnCreate(createdDate);
			nota.setVWhoCreate(user.getVUserName());
			nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
			nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
			nota.setTbRegistration(this.reg);
			
			
			nota.setMsUnit(unit);
			nota.setTotal(this.grandTotal.getValue().doubleValue());
			
			TbDrugIngredients racikan = null;
			TbItemTrx obat = null;
			MsItem msItem = null;
			Textbox aturanPakai=null;
			Listbox aturSelect = null;
			Decimalbox decbox = new Decimalbox();
			decbox.setFormat("#,###.##");
			
			List<Listitem> items = this.reseplist.getItems();
			for(Listitem item : items){
				if(item.getValue() instanceof Object[]){
					obat = new TbItemTrx();
					Object[] object = (Object[])item.getValue();
					msItem = new MsItem();
					msItem.setNItemId((Integer)object[0]);
					
					obat.setMsItem(msItem);
					obat.setVWhoCreate(user.getVUserName());
					obat.setNQty((new Float(((Listcell)item.getChildren().get(4)).getLabel())));
					
					decbox.setText(((Listcell)item.getChildren().get(7)).getLabel());
					obat.setNAmountTrx(decbox.getValue().doubleValue());
					obat.setNAmountAfterDisc(decbox.getValue().doubleValue());
					obat.setNDiscAmount(0.0);
					obat.setVDiscType(MedisafeConstants.RP);
					obat.setDWhnCreate(createdDate);
					
					aturanPakai = (Textbox) ((Listcell)item.getChildren().get(6)).getChildren().get(0);
					aturSelect = (Listbox) ((Listcell)item.getChildren().get(6)).getChildren().get(1);
					obat.setAturanPakai(aturanPakai.getText()+"-"+aturSelect.getSelectedItem().getLabel());
					
					itemTrx.add(obat);
				}else{
					
					racikan = (TbDrugIngredients) item.getValue();
					decbox.setText(((Listcell)item.getChildren().get(7)).getLabel());
					racikan.setNAmountTrx(decbox.getValue().doubleValue());
					racikan.setVWhoCreate(user.getVUserName());
					racikan.setNDiscAmount(0);
					racikan.setNDingrQty((new Float(((Listcell)item.getChildren().get(4)).getLabel())).shortValue());
					racikan.setNDingrQuantify(((Listcell)item.getChildren().get(3)).getLabel());
					racikan.setNAmountAfterDisc(decbox.getValue().doubleValue());
					racikan.setVDiscType(MedisafeConstants.RP);
					racikan.setDWhnCreate(createdDate);
					
					aturanPakai = (Textbox) ((Listcell)item.getChildren().get(6)).getChildren().get(0);
					aturSelect = (Listbox) ((Listcell)item.getChildren().get(6)).getChildren().get(1);
					racikan.setAturanPakai(aturanPakai.getText()+"-"+aturSelect.getSelectedItem().getLabel());
					
					racikans.add(racikan);
				}
				
				
			}

			
		}
		
		if(this.dignoseType.getSelectedItem().getLabel().equalsIgnoreCase("RAWAT INAP"))
			diagnose.setNDiagnoseType(MedisafeConstants.DIAGNOSA_RANAP);
		else 
			diagnose.setNDiagnoseType(MedisafeConstants.DIAGNOSA_RAJAL);
		
		
		
		this.doctorName.removeAttribute("unitDaftar");
		this.diagnoseId.removeAttribute("diagnose");
		
		serv.saveDiagnoseAndReceipt(patient,diagnose,icd, nota, itemTrx, racikans, unit.getMsWarehouse().getNWhouseId());
		getDiagnoseHistory(window);
		this.btnSave.setDisabled(true);
		
	}

	public void getMr(int type) throws VONEAppException, InterruptedException
	{
		
		DiagnoseManager diagnoseServ = Service.getDiagnoseManager();
		diagnoseServ.getRegistrationDetail(this, type);
		
		Page page = mrNo.getDesktop().getPage("rmDiagnoseHistoryPage");
		this.window = (Window)page.getFellow("historyDiagnose");
		
		getDiagnoseHistory(window);
		
		
				
	}
	
	private void getDiagnoseHistory(Window win) throws VONEAppException{
		
		if(this.reg == null) return;
		
		Label mrNo = (Label)win.getFellow("mrNo");
		Label sex = (Label)win.getFellow("sex");
		Label patientName = (Label)win.getFellow("patientName");
		Label tglLahir = (Label)win.getFellow("tglLahir");
		Listbox diagoseList = (Listbox)win.getFellow("diagoseList");
		
		mrNo.setValue(this.mrNo.getText());
		sex.setValue(this.gender.getSelectedItem().getLabel());
		patientName.setValue(this.patientName.getText());
		tglLahir.setValue(this.dateOfBirth.getText());
		
		
		
		this.serv.getDiagnoseHistory(this.mr, diagoseList);
		
	}

	public void getIcd(){
		String diagnose = "";
		Set<Listitem> items = this.diagnoseSearchList.getSelectedItems();
		if(items.size() > 1){
			for(Listitem item:items){
				diagnose = diagnose + ((MsIcd)item.getValue()).getVIcdName()+",";
			}
			this.diagnoseId.setValue(diagnose.substring(0, diagnose.length()-1));
			
		}else if(items.size() == 1){
			this.diagnoseId.setValue(((MsIcd)this.diagnoseSearchList.getSelectedItem().getValue()).getVIcdName());
		}else{
			this.diagnoseId.setValue(null);
		}
		
//		this.diagnoseId.setAttribute("diagnosa", this.diagnoseSearchList.getSelectedItems());
		
	}
	
	public void serachDiagnose() throws VONEAppException, InterruptedException
	{
		icdService.serachIcdByCodeAndName(this.diagnoseSearchList, this.diagnoseSearchCode, this.diagnoseSearchName);
//		this.diagnoseSearchList.clearSelection();
	}
	
	
	public void addObat() throws VONEAppException, InterruptedException
	{
		Boolean isRajal = true;
		if(this.dignoseType.getSelectedItem().getLabel().equalsIgnoreCase("RAWAT INAP"))
			isRajal = false;
			
		this.reseplist.setAttribute("isRajal", isRajal);
		session.setAttribute("listObatDokter", this.reseplist);
		Window win = (Window) Executions.createComponents("/zkpages/rekamMedis/tambahObatRM.zul", null, null);
		win.doModal();
	}
	
	
	public void addRacikan() throws VONEAppException,InterruptedException
	{
		Boolean isRajal = true;
		if(this.dignoseType.getSelectedItem().getLabel().equalsIgnoreCase("RAWAT INAP"))
			isRajal = false;
		this.reseplist.setAttribute("isRajal", isRajal);
		session.setAttribute("listObatDokter", this.reseplist);
		Window win = (Window) Executions.createComponents("/zkpages/rekamMedis/tambahRacikanRM.zul", null, null);
		win.doModal();
	}
	
	
	
	public void calculateTotal() throws WrongValueException, InterruptedException{
		
		List<Listitem> items = this.reseplist.getItems();
		Listcell cell = null;
		Decimalbox dec = new Decimalbox();
		double total = 0;
		dec.setFormat("#,###.##");
		for(Listitem item : items){
			cell = (Listcell)item.getChildren().get(7);
			dec.setText(cell.getLabel());
			total = total + dec.getValue().doubleValue();
		}
		this.grandTotal.setValue(new BigDecimal(total));
		
	}
	
	public void deleteList()throws VONEAppException, InterruptedException
	{
		if(this.reseplist.getSelectedItems().size() == 0){
			
			Messagebox.show("PILIH DATA YANG AKAN DIHAPUS");
			return;
			
		}
		this.reseplist.removeItemAt(reseplist.getSelectedIndex());
		this.calculateTotal();
	}
	
	public void test() throws VONEAppException{
		
		DiagnoseManager serv = Service.getDiagnoseManager();
		serv.test(crNoMR,crNama,crNoAlamat,patientList);
		
	}

	



}

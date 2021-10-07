package com.vone.medisafe.ui.common;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Window;


import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.HistoryPatientListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.CommonPatientHistoryManager;
import com.vone.medisafe.ui.base.BaseController;

public class PatientHistoryController extends BaseController{
	
	public TbMedicalRecord mr;
	public MsUnit unit;
	
	public Label patientMrAndName;
	public Radiogroup rgoption;
	public Radio divisi;
	public Radio global;
	public Tab notaTab;
	public Tab perawatanTab;
	public Treecell treatment;
	public Treecell medicine;
	public Tree treeListPerawatan;
	public Tree treeListNota;
	public Listbox historyList;
	public Tabbox tboption;
	public Decimalbox grandTotal;
	public Label totalLabel;
	
	//ROOT
	Page rootPage;
	Tabbox tbroot;
	Tab transaksi;
	Tab history;
	
	Window mainWindow;
	Listbox locationList;

	Session session;
	
	
	CommonPatientHistoryManager service = Service.getPatientHistory();

	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		patientMrAndName = (Label)win.getFellow("patientMrAndName");
		divisi = (Radio)win.getFellow("divisi");
		global = (Radio)win.getFellow("global");	
		notaTab = (Tab)win.getFellow("notaTab");
		perawatanTab = (Tab)win.getFellow("perawatanTab");
		treatment = (Treecell)win.getFellow("treatment");
		medicine = (Treecell)win.getFellow("medicine");
		treeListPerawatan = (Tree)win.getFellow("treeListPerawatan");
		treeListNota = (Tree)win.getFellow("treeListNota");
		historyList = (Listbox)win.getFellow("historyList");
		rgoption = (Radiogroup)win.getFellow("rgoption");
		tboption = (Tabbox)win.getFellow("tboption");
		grandTotal = (Decimalbox)win.getFellow("grandTotal");
		totalLabel = (Label)win.getFellow("totalLabel");
		grandTotal.setVisible(false);
		totalLabel.setVisible(false);
		
		session = win.getDesktop().getSession();
		
		if(session.getAttribute(MedisafeConstants.PATIENT_HISTROY) != null)
		{
			String root = (String)session.getAttribute(MedisafeConstants.PATIENT_HISTROY);
			if(root.equals(MedisafeConstants.COMMON_POLY)){
				
				rootPage = win.getDesktop().getPage("Polyroot");
				tbroot = (Tabbox)rootPage.getFellow("polyclinic").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("polyPage");
				mainWindow = (Window)mainPage.getFellow("PolyclinicTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
																
			}
			else if(root.equals(MedisafeConstants.COMMON_UGD)){
				rootPage = win.getDesktop().getPage(ScreenConstant.TRANSAKSI_UGD);
				tbroot = (Tabbox)rootPage.getFellow("emergencyRoot").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("ugdPage");
				mainWindow = (Window)mainPage.getFellow("emergencyTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
				
			}
			else if(root.equals(MedisafeConstants.COMMON_BANGSAL)){
				rootPage = win.getDesktop().getPage(ScreenConstant.TRANSAKSI_BANGSAL);
				tbroot = (Tabbox)rootPage.getFellow("ward").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("wardPage");
				mainWindow = (Window)mainPage.getFellow("WardTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
			}
			else if(root.equals(MedisafeConstants.COMMON_VK)){
				rootPage = win.getDesktop().getPage("vkRoot");
				tbroot = (Tabbox)rootPage.getFellow("vk").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage(ScreenConstant.TRANSAKSI_VK);
				mainWindow = (Window)mainPage.getFellow("vkTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
			}
			else if(root.equals(MedisafeConstants.COMMON_RADIOLOGY)){
				rootPage = win.getDesktop().getPage("radiologyRoot");
				tbroot = (Tabbox)rootPage.getFellow("radiology").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("radiologyPage");
				mainWindow = (Window)mainPage.getFellow("RadiologyTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
				
			}
			else if(root.equals(MedisafeConstants.COMMON_LABORAT)){
				rootPage = win.getDesktop().getPage("laboratRoot");
				tbroot = (Tabbox)rootPage.getFellow("laborat").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("labPage");
				mainWindow = (Window)mainPage.getFellow("LaboratoryTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
			}
			else if(root.equals(MedisafeConstants.COMMON_SURGERY)){
				rootPage = win.getDesktop().getPage("surgeryRoot");
				tbroot = (Tabbox)rootPage.getFellow("surgery").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("surgeryPage");
				mainWindow = (Window)mainPage.getFellow("SurgeryTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
			}
			else if(root.equals(MedisafeConstants.COMMON_RENAL)){
				rootPage = win.getDesktop().getPage("renalRoot");
				tbroot = (Tabbox)rootPage.getFellow("renal").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("renalPage");
				mainWindow = (Window)mainPage.getFellow("RenalUnitTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
							
			}
			else if(root.equals(MedisafeConstants.COMMON_FISIOTERAPI)){
				rootPage = win.getDesktop().getPage("physiotherapyRoot");
				tbroot = (Tabbox)rootPage.getFellow("physiotherapy").getFellow("tbroot");
				
				Page mainPage = win.getDesktop().getPage("physioPage");
				mainWindow = (Window)mainPage.getFellow("PhysiotherapyTransaction");
				locationList = (Listbox)mainWindow.getFellow("locationList");
			}
			
			tbroot.addEventListener("onSelect", new HistoryPatientListener(tbroot, patientMrAndName,win,historyList));
			
								
					
			
			unit = (MsUnit)locationList.getSelectedItem().getValue();
			
			
			
			historyList.getChildren().clear();
		}else{
			historyList.getChildren().clear();
			patientMrAndName.setValue(null);
		}
		

	}
	
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	
	
	public void getTreatmentData() throws InterruptedException, VONEAppException{
		
		service.getTreatmentData(this);
		MiscTrxController.setFont(historyList);

	}
	
	public void getOption() throws InterruptedException, VONEAppException{
		
		service.getOption(this);
		

	}
	
}

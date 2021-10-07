package com.vone.medisafe.ui.mr;



import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.ui.base.BaseController;

public class CodController extends BaseController{
	
	Listbox codList;
	Groupbox dataPenyakitGBox;
	Listbox diseaseList;
	Intbox duration;
	
	Button btnDelete;
	Button btnIcdAdd;
	
	
	Groupbox rudapaksaGBox;
	Listbox violentList;
	Textbox caraKejadian;
	Textbox bodyDestroy;
	
	Groupbox pregnancyGBox;
	Listbox pregnantList;
	
	Groupbox stillbirhtGBox;
	Textbox causeOfStillbirth;
	
	Groupbox operationGBox;
	Textbox operationType;
	
	Button btnSave;
	Button btnEnd;
	
	Window win;
	
	
	ZulConstraint constrains = new ZulConstraint();

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		codList = (Listbox)win.getFellow("codList");
		dataPenyakitGBox = (Groupbox)win.getFellow("dataPenyakitGBox");
		diseaseList = (Listbox)win.getFellow("diseaseList");
		duration = (Intbox)win.getFellow("duration");
		
		btnDelete = (Button)win.getFellow("btnDelete");
		btnIcdAdd = (Button)win.getFellow("btnIcdAdd");
		
		
		rudapaksaGBox = (Groupbox)win.getFellow("rudapaksaGBox");
		violentList = (Listbox)win.getFellow("violentList");
		caraKejadian = (Textbox)win.getFellow("caraKejadian");
		bodyDestroy = (Textbox)win.getFellow("bodyDestroy");
		
		pregnancyGBox = (Groupbox)win.getFellow("pregnancyGBox");
		pregnantList = (Listbox)win.getFellow("pregnantList");
		
		stillbirhtGBox = (Groupbox)win.getFellow("stillbirhtGBox");
		causeOfStillbirth = (Textbox)win.getFellow("causeOfStillbirth");
		
		operationGBox = (Groupbox)win.getFellow("operationGBox");
		operationType = (Textbox)win.getFellow("operationType");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnEnd = (Button)win.getFellow("btnEnd");
		
		
		diseaseList.getItems().clear();
		
		setGroubBoxFisible(false);
		
		constrains.registerComponent(caraKejadian, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(bodyDestroy, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(causeOfStillbirth, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(operationType, ZulConstraint.UPPER_CASE);
		
		this.win = (Window)win;
		
		clear();
		
	}
	
	

	public void clear() {
		
		codList.setSelectedIndex(0);
		diseaseList.getItems().clear();
		duration.setValue(null);
		
		caraKejadian.setValue(null);
		bodyDestroy.setValue(null);
		causeOfStillbirth.setValue(null);
		operationType.setValue(null);
		
	}
	
	
	public void save() throws VONEAppException
	{
		Page page = win.getDesktop().getPage("diagnosePage");
		Window winroot = (Window)page.getFellow("diagnoseRoot");
		Tab diagnoseTab = (Tab)winroot.getFellow("diagnoseTab");
//		Tab codTab = (Tab)winroot.getFellow("codTab");
	
		diagnoseTab.setSelected(true);
	}

	private void setGroubBoxFisible(boolean visible) throws VONEAppException {
		
//		dataPenyakitGBox.setVisible(visible);
//		rudapaksaGBox.setVisible(visible);
//		pregnancyGBox.setVisible(visible);
//		stillbirhtGBox.setVisible(visible);
//		operationGBox.setVisible(visible);
		
	}
	
	
	public void getCodDetil() throws VONEAppException
	{
//		if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Karena Penyakit")){
//			setGroubBoxFisible(false);
//			this.dataPenyakitGBox.setVisible(true);
//		}
//		else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Karena Rudapaksa")){
//			setGroubBoxFisible(false);
//			this.rudapaksaGBox.setVisible(true);
//		}
//		else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Kelahiran Mati")){
//			setGroubBoxFisible(false);
//			this.stillbirhtGBox.setVisible(true);
//		}
//		else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Persalinan, Kehamilan")){
//			setGroubBoxFisible(false);
//			this.pregnancyGBox.setVisible(true);
//		}
//		else if(codList.getSelectedItem().getValue().toString().equalsIgnoreCase("Operasi")){
//			setGroubBoxFisible(false);
//			this.operationGBox.setVisible(true);
//		}
	}
	
	
	public void deleteList() throws VONEAppException, InterruptedException
	{
		if(diseaseList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("pilih.data.diagnosa.dari.list"));
			return;
		}
		
		
		int index = diseaseList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			diseaseList.removeItemAt(index);
			
		}
	}

	
}

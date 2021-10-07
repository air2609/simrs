package com.vone.medisafe.ui.admission;


import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.TbRoomReservation;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.admission.AntrianKamarManager;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;
import com.vone.medisafe.ui.base.BaseController;



public class AntrianKamarController extends BaseController{
	

	private MsHall hall;
	
	
	Bandbox mRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox mrList;
	Textbox registrationNumber;
	Textbox name;
	Textbox age;
	Radiogroup jenisKelamin;
	Listbox tclassList;
	Bandbox ruangan;
	Listbox kelasTarifList;
	Listbox avaliableBedList;
	Listbox roomReservationList;
	Radio male;
	Radio female;	
	Button btnSave;
	Window win;

	Button btnNew;
	Button btnDelete;
	//Button btnEnd;
	
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();
	
	
	private AntrianKamarManager serv = Service.getAntrianKamarManager();
	
	TreatmentClassManager tclassService = MasterServiceLocator.getTreatmentClassManager();

	@Override
	public UserInfoBean getUserInfoBean() {
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		
		mRNumber = (Bandbox)win.getFellow("mRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		mrList = (Listbox)win.getFellow("mrList");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		name = (Textbox)win.getFellow("name");
		age = (Textbox)win.getFellow("age");
		jenisKelamin = (Radiogroup)win.getFellow("jenisKelamin");
		male = (Radio)win.getFellow("male");
		female = (Radio)win.getFellow("female");
		ruangan = (Bandbox)win.getFellow("ruangan");
		kelasTarifList = (Listbox)win.getFellow("kelasTarifList");
		avaliableBedList = (Listbox)win.getFellow("avaliableBedList");
		roomReservationList = (Listbox)win.getFellow("roomReservationList");
		tclassList = (Listbox)win.getFellow("tclassList");
		
		btnSave = (Button)win.getFellow("btnSave");

		btnNew = (Button)win.getFellow("btnNew");
		btnDelete = (Button)win.getFellow("btnDelete");
	//	btnEnd = (Button)win.getFellow("");
		
		this.win = (Window)win;
		
		
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(tclassList, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
		tclassService.getTClassForSelect(tclassList);
		tclassService.getTClassForSelect(kelasTarifList);
		
		
		getAntrianKamarList();
		
		mRNumber.focus();
		name.setReadonly(true);
		age.setReadonly(true);
		male.setDisabled(true);
		female.setDisabled(true);
		registrationNumber.setReadonly(true);
	}
	
	
	public void clear(){
		
		mRNumber.setValue(null);
		crNoMR.setValue(null);
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		mrList.getItems().clear();
		registrationNumber.setValue(null);
		name.setValue(null);
		age.setValue(null);
		jenisKelamin.setSelectedItem(null);
		tclassList.setSelectedIndex(0);
		ruangan.setValue(null);
		kelasTarifList.setSelectedIndex(0);
		avaliableBedList.getItems().clear();
		
		
		
		btnSave.setDisabled(false);
		mRNumber.setDisabled(false);
		ruangan.setDisabled(false);
		tclassList.setDisabled(false);
		mRNumber.focus();
	}
	
	
	
	
	public void getRegistration(int type) throws VONEAppException, InterruptedException
	
	{
		
		serv.getRegistrationDetil(this.win,type);
		
		
	}
	
	
	
	public void getAntrianKamarList() throws VONEAppException
	{
		
		serv.getAntrianList(roomReservationList);	
		MiscTrxController.setFont(roomReservationList);
			
	}
	
	
	public void save() throws VONEAppException, InterruptedException{
		
		if(!constraints.validateComponent(true))return;
		
		serv.save(this.win);
		
		
		btnSave.setDisabled(true);
		
	}
	
	
	public void delete() throws VONEAppException, InterruptedException{
	
		if(roomReservationList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("antrianlist.not.selected"));
			return;
		}
		
		int index = roomReservationList.getSelectedIndex();
		
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(serv.delete((TbRoomReservation)roomReservationList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				roomReservationList.removeItemAt(index);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}
	
	
	
	
	public void getHall() throws VONEAppException, InterruptedException{
		
		
		item = this.avaliableBedList.getSelectedItem();
		
		cell = (Listcell)item.getChildren().get(1);
		Integer jumlahBed = new Integer(cell.getLabel());
		
		if(jumlahBed > 0){
			Messagebox.show(MessagesService.getKey("antrian.not.valid"));
			return;
			
		}
		else{
			hall = (MsHall)item.getValue();
						
			cell = (Listcell)item.getChildren().get(0);
			ruangan.setValue(cell.getLabel());
			ruangan.setAttribute("hall", hall);
			
			getAntrianBaseOnHallId(hall.getNHallId());
			
		}
		
	}
	
	
	public void getAntrianBaseOnHallId(Integer id) throws VONEAppException
	{
		
		serv.getAntrianBaseOnHallId(roomReservationList, id);
				
	}
	
	
	
//	public void modify() throws VONEAppException, InterruptedException{
//		
//		if(roomReservationList.getSelectedItem() == null){
//			Messagebox.show(MessagesService.getKey("antrianlist.not.selected"));
//			return;
//		}
//		
//		
//		antrian = (TbRoomReservation)roomReservationList.getSelectedItem().getValue();
//		
//		
//	}
	

}

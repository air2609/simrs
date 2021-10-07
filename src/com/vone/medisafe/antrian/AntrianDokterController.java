package com.vone.medisafe.antrian;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.base.BaseController;

public class AntrianDokterController extends BaseController{
	
	Window dokterSatu;
	Window dokterDua;
	Listbox listPasien1;
	Listbox listPasien2;
	Label dokter1;
	Label dokter2;
	Timer timer;
	Label myLabel;
	int counter = 0;
	Label hospitalName;
	
	DoctorManager manager = MasterServiceLocator.getDoctorManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		dokterSatu = (Window)cmp.getFellow("dokterSatu");
//		dokterDua = (Window)cmp.getFellow("dokterDua");
		listPasien1 = (Listbox)dokterSatu.getFellow("listPasien1");
//		listPasien2 = (Listbox)dokterDua.getFellow("listPasien2");
		dokter1 = (Label)cmp.getFellow("dokter1");
//		dokter2 = (Label)dokterDua.getFellow("dokter2");
		timer = (Timer)cmp.getFellow("timer");
		myLabel = (Label)cmp.getFellow("myLabel");
		hospitalName = (Label)cmp.getFellow("hospitalName");
		
		hospitalName.setValue(MessagesService.getKey("hospital.name.short").toUpperCase());
		manager.getDelayAntrian(timer, myLabel, 0);
		manager.getAntrianDokter(dokterSatu, dokterDua, listPasien1, listPasien2, dokter1, dokter2);
	}
	
	public void onTimer() throws VONEAppException{
		manager.getAntrianDokter(dokterSatu, dokterDua, listPasien1, listPasien2, dokter1, dokter2);
	}

}

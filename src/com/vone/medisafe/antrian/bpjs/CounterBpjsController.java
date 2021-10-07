package com.vone.medisafe.antrian.bpjs;

import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class CounterBpjsController extends BaseController{
	
	Vbox vboxCounter;
	Vbox vboxAntrian;
	
	Listbox listAntrian;
	Textbox cariDokter;
	Datebox tanggalPraktek;
	
	Row rowCounter;
	Integer bpjsCounter = null;
	TbAntrian antrianOnService = null;
	TbAntrian antrianDone = null;
	
	String buttonStyle=" color:blue;\r\n" + 
			"    border: 1px solid powderblue;\r\n" + 
			"    padding: 5px;\r\n" + 
			"    background-color: #eeeeee;\r\n" + 
			"    font-weight:bold;\r\n" + 
			"    font-size: 30px;\r\n" + 
			"    width : 150px;\r\n" + 
			"    font-family: \"Times New Roman\", Times, serif";
	
	AntrianManager antrianService = Service.getAntrianManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		vboxCounter = (Vbox)cmp.getFellow("vboxCounter");
		vboxAntrian = (Vbox)cmp.getFellow("vboxAntrian");
		rowCounter = (Row)cmp.getFellow("rowCounter");
		listAntrian = (Listbox)cmp.getFellow("listAntrian");
		cariDokter = (Textbox)cmp.getFellow("cariDokter");
		tanggalPraktek = (Datebox)cmp.getFellow("tanggalPraktek");
		
		rowCounter.getChildren().clear();
		Integer counter = new Integer(MessagesService.getKey("counter.bjps"));
		setCounter(counter);
		
		
		vboxAntrian.setVisible(false);
		getTodayQueue();
	}
	
	public void getTodayQueue() throws VONEAppException{
		if(tanggalPraktek.getValue() == null) tanggalPraktek.setValue(new Date());
		antrianService.getTodayQueue(this, tanggalPraktek.getValue());
	}

	private void setCounter(Integer counter) {
		for(int i=1; i <= counter; i++) {
			Button b = new Button("Counter "+i);
			b.setAttribute("counter", i);
			b.setParent(rowCounter);
			b.setStyle(buttonStyle);
			b.addEventListener("onClick", new EventListener() {
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					vboxCounter.setVisible(false);
					vboxAntrian.setVisible(true);
					bpjsCounter = new Integer(b.getAttribute("counter").toString());
				}
			});
		}
	}
	
	
	public void changeAntrianToInProgress() throws VONEAppException{
		System.out.println("antrian on service id : "+this.antrianOnService.getId());
		//get the current queue in progress, update to done
		TbAntrian current = antrianService.getQueueOnServiceByCounter(bpjsCounter);
		if(current != null) {
			current.setStatus(2); //done
			current.setDoneAt(new Date());
			current.setDoneBy(this.getUserInfoBean().getStUserName());
			antrianService.saveQueue(current);
		}
		
		antrianOnService.setCounterNo(bpjsCounter);
		antrianOnService.setStatus(1); //inprogress
		antrianService.saveQueue(antrianOnService);
		
		getTodayQueue();
	}

}

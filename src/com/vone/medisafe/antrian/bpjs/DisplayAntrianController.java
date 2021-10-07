package com.vone.medisafe.antrian.bpjs;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Timer;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;

public class DisplayAntrianController {
	
	AntrianManager antrianService = Service.getAntrianManager();
	
	String buttonStyle=" color:blue;\r\n" + 
			"    border: 1px solid powderblue;\r\n" + 
			"    padding: 5px;\r\n" + 
			"    background-color: #eeeeee;\r\n" + 
			"    font-weight:bold;\r\n" + 
			"    font-size: 100px;\r\n" + 
			"    width : 200px;\r\n" + 
			"    height: 200px;\r\n" + 
			"    font-family: \"Times New Roman\", Times, serif;";
	
	String textStyle = " border: 1px solid powderblue;\r\n" + 
			"    padding: 5px;\r\n" + 
			"    background-color: #eeeeee;\r\n" + 
			"    font-weight:bold;\r\n" + 
			"    font-size: 15px;\r\n" + 
			"    width : 200px;\r\n" + 
			"    font-family: \"Trebuchet MS\", Helvetica, sans-serif;";
	
	
	Hbox hboxDisplay;
	Timer timer;
			

	public void init(Component cmp) throws VONEAppException{
		hboxDisplay = (Hbox)cmp.getFellow("hboxDisplay");
		timer = (Timer)cmp.getFellow("timer");
		
		timer.setDelay(3 * 1000);
		onTimer();
		
	}
	
	public void onTimer() throws VONEAppException{
		antrianService.displayAntrian(hboxDisplay, buttonStyle, textStyle);
	}
	
	

}

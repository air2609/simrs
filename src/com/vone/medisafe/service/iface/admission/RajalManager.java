package com.vone.medisafe.service.iface.admission;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbRegistration;


public interface RajalManager {

	public boolean savePatientAndRegistration(Window win) throws VONEAppException, InterruptedException;

	public boolean saveRegistrationOnly(Bandbox noMr, Listbox dokterPemeriksaList, Listbox unitList, Textbox noRegistrasi, Map<String, Object> tipePasien)
		throws VONEAppException, InterruptedException;

	public void getPatientDetail(Window win, String code) throws VONEAppException, InterruptedException;

	public boolean cancelRegistration(Bandbox noMr) throws VONEAppException;
	
	public void getActiveRegistration(Listbox listdata, Date starDate, Date endDate) throws VONEAppException;

	public void checkOutRegistraton(Set<Listitem> items) throws VONEAppException;
	
	public void getRegistrationReport(Date dari, Date sampai, int unitId, Listbox result) throws VONEAppException;

	public void getPatientOldRegistration(Listbox mrList, int mrStatus) throws VONEAppException;

	public void updateRegistration(TbRegistration reg);

}

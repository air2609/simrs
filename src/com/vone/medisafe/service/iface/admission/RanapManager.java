package com.vone.medisafe.service.iface.admission;

import org.zkoss.zhtml.Table;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsHall;






public interface RanapManager {

	public void getPatientDetil(Window win, int type) throws VONEAppException, InterruptedException;

	public void getHallListByTclassId(Integer id, Listbox antriKelasTarif, Listbox searchKelas) 
		throws VONEAppException;

	public void saveRanap(Component win) throws VONEAppException, InterruptedException;

	

	public void getBedsBaseOnHall(Listbox avaliableBedList, Table bedTable, Bandbox ruangan, Bandbox bbbed)
		throws VONEAppException;

	public boolean cancelRanapRegistration(Bandbox bed, Textbox noRegistrasiBaru, Bandbox noMR)
		throws VONEAppException;
	
	
	public void getBedBaseOnHallId(MsHall hall, Table bedTable, Bandbox bbbed) throws VONEAppException;

}

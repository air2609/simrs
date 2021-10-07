package com.vone.medisafe.ui.purchasing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.PurchaseServiceLocator;

public class BufferController extends PurchaseController{
	

	
	Logger logger = Logger.getLogger(PORController.class);
	
	Listbox location = null;
	Listbox list = null;		
	
	Session session = null;
	
	// shall be kept null at initial!!!
	// purpose : get Measurement List once.
	List listMeasurement = null;
	
	public List getListMeasurement() {
		return listMeasurement;
	}

	
	
	
	
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		//set common list
		setList(this.list);
		
		//init
		location = (Listbox)cmp.getFellow("location");
		list = (Listbox)cmp.getFellow("list");
		
		
		//fill location
		List listLoc = super.getUserInfoBean().getMsUnitByScreenCode(ScreenConstant.ORDER_PERMINTAAN_PEMBELIAN);
		
		Iterator it = listLoc.iterator();
		
		location.getItems().clear();		
		
		while (it.hasNext()){
			MsUnit unitPojo = (MsUnit)it.next();
			
			if(unitPojo.getMsWarehouse() != null){
				Listitem item = new Listitem();
				item.setParent(location);
				
				item.appendChild(new Listcell(unitPojo.getVUnitCode()+" - "+unitPojo.getVUnitName()));
				item.setValue(unitPojo);
			}
			
			
		}
		if (location.getItemCount() > 0)
			location.setSelectedIndex(0);
		
		//fill oppNo
		
		//not yet
		
		//fill issuedBy
		
		//fill session
		session = Sessions.getCurrent();
		session.setAttribute(MedisafeConstants.PURCHASING_SESSION, this);
		
		//draw list
		//comment by arif;
		redraw();
		
		location.addEventListener("onSelect", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redraw();
				} catch (VONEAppException e) {
					// TODO Auto-generated catch block
					
				}
			}
			
		});
		
		
		//SETFOCUS
		
		if (list.getItemCount() > 0){
			Listitem item = list.getItemAtIndex(0);
			
			Listcell cell = (Listcell)item.getChildren().get(6);
			((Intbox)cell.getChildren().get(0)).focus();
		}
		
		setInitButton();
				
	}	
	
	

	
	

	
	public void setListMeasurement() throws VONEAppException {
		if (this.listMeasurement != null)
			return;

		this.listMeasurement = MasterServiceLocator.getItemMeasurementManager()
				.getMeasurementType();

		if (this.listMeasurement == null)
			this.listMeasurement = new ArrayList();
	}

	/**
	 * inject ItemMeasurement Data to listM
	 * @param listM
	 * @throws VONEAppException
	 */
	public void setListboxMeasurement(Listbox listM) throws VONEAppException {
		if (listM == null)
			listM = new Listbox();

		if (this.listMeasurement == null)
			setListMeasurement();

		Iterator it = this.listMeasurement.iterator();

		while (it.hasNext()) {
			Listitem item = new Listitem();
			item.setParent(listM);

			String value = (String)it.next();

			item.appendChild(new Listcell(value));
			item.setValue(MasterServiceLocator.getItemMeasurementManager()
					.getMsItemMeasurementByCode(value));
		}

		listM.setSelectedIndex(0);
	}
	
	public void selectListboxMeasurement(Listbox listM, String value) {
		if (listM == null || !StringUtils.hasValue(value))
			return;
		
		Iterator it = listM.getItems().iterator();
		
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			
			MsItemMeasurement msItemM = (MsItemMeasurement)item.getValue();
			
			if (value.equals(msItemM.getVMitemEndQuantify())){
				listM.setSelectedItem(item);
				return;
			}
		}
	}

	
	private void redraw() throws VONEAppException{
		
		list.getItems().clear();
		
		
		
		PurchaseServiceLocator.getPORManager().redrawPORController(this);

		setInitButton();
	}
	
	

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}
	
	public void setInitButton(){

		this.list.setDisabled(false);
		
	}
	
		
	
	
	
	public Listbox getLocation() {
		return location;
	}

	public void setLocation(Listbox location) {
		this.location = location;
	}

	
	
	public void setListMeasurement(List listMeasurement) {
		this.listMeasurement = listMeasurement;
	}
	


}

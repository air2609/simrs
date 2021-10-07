package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsScreen;
import com.vone.medisafe.mapping.MsScreenDAO;
import com.vone.medisafe.mapping.MsScreenInUnit;
import com.vone.medisafe.mapping.MsSubsystem;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUnitDAO;
import com.vone.medisafe.service.iface.admin.ScreenManager;
import com.vone.medisafe.ui.admin.ScreenController;


public class ScreenManagerImpl implements ScreenManager{

	MsScreenDAO dao;
	MsUnitDAO unitdao;
	
	public List getAllScreen(MsScreen screen) throws VONEAppException {
		// TODO Auto-generated method stub
		return this.dao.findByExample(screen);
	}

	public void saveScreen(MsScreen screen) throws VONEAppException{
		
		this.dao.saveOnly(screen);
	}

	public Integer getScreenIdByCode (String screenCode) throws VONEAppException {
		
		return this.dao.getScreenIdByCode(screenCode);
	}
	
	public void deleteScreen(MsScreen screen) throws VONEAppException {
		// TODO Auto-generated method stub
		this.dao.delete(screen);
	}

	public MsScreen getScreenById(Integer id) throws VONEAppException {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public MsScreen getScreen(MsScreen screen) throws VONEAppException {
		// TODO Auto-generated method stub
		Collection list = this.dao.findByExample(screen);
		MsScreen usr = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			usr = (MsScreen)it.next();
		}
		return usr;
	}

	public MsScreenDAO getDao() {
		return dao;
	}

	public void setDao(MsScreenDAO dao) {
		this.dao = dao;
	}
	
	public String[] getScreenCodeName (int iScreenId) throws VONEAppException{
		return this.dao.getScreenCodeName(iScreenId);
	}
	
	public String[] getScreenCodeName (Integer iScreenId) throws VONEAppException{
		
		return this.dao.getScreenCodeName(iScreenId.intValue());
	}

	public void updateScreen(int iScreenId, String stScreenCode, String stScreenName, int iSubsystemId) throws VONEAppException {
		// TODO Auto-generated method stub
		this.dao.update(iScreenId, stScreenCode, stScreenName, iSubsystemId);
		
	}

	public List searchByCriteria(String screenCode, String screenName) throws VONEAppException {
		// TODO Auto-generated method stub
		return this.dao.searchByCriteria(screenCode, screenName);
	}	
	
	public MsScreen getScreenByCode(String code) throws VONEAppException{
		return this.dao.getScreenByCode(code);
	}
	
	public boolean save(MsScreen screen) throws VONEAppException {
		return this.dao.save(screen);
	}
	
	public boolean update(MsScreen screen) throws VONEAppException{
		return this.dao.update(screen);
	}

	public MsUnitDAO getUnitdao() {
		return unitdao;
	}

	public void setUnitdao(MsUnitDAO unitdao) {
		this.unitdao = unitdao;
	}
	
	public void redrawListScreenByCriteria(ScreenController ctr)throws VONEAppException {
		ctr.getList().getItems().clear();
		
		List listScreen = this.dao.searchByCriteriaOr(ctr.getCariTextbox().getText(),ctr.getCariTextbox().getText());
		
		Iterator it = listScreen.iterator(); 
		
		while (it.hasNext()){
			Listitem listItem = new Listitem();
			MsScreen screenPojo = (MsScreen)it.next();
			
			listItem.appendChild(new Listcell(screenPojo.getVScreenCode()));
			listItem.appendChild(new Listcell(screenPojo.getVDesc()));
			
			Listcell cellSubsystem = new Listcell();
			cellSubsystem.setLabel(screenPojo.getMsSubsystem().getVDesc());
			cellSubsystem.setValue(screenPojo.getMsSubsystem().getNSubsystemId());
			
			listItem.appendChild(cellSubsystem);
			
			listItem.setValue(screenPojo);
			
			listItem.setParent(ctr.getList());
		}		
		
	}
	
	public void redrawListScreen(ScreenController ctr) throws VONEAppException {
		ctr.getList().getItems().clear();
		
		List listScreen = this.dao.findByExample(new MsScreen());
		
		Iterator it = listScreen.iterator(); 
		
		while (it.hasNext()){
			Listitem listItem = new Listitem();
			MsScreen screenPojo = (MsScreen)it.next();
			
			listItem.appendChild(new Listcell(screenPojo.getVScreenCode()));
			listItem.appendChild(new Listcell(screenPojo.getVDesc()));
			
			Listcell cellSubsystem = new Listcell();
			cellSubsystem.setLabel(screenPojo.getMsSubsystem().getVDesc());
			cellSubsystem.setValue(screenPojo.getMsSubsystem().getNSubsystemId());
			
			listItem.appendChild(cellSubsystem);
			
			listItem.setValue(screenPojo);
			
			listItem.setParent(ctr.getList());
		}		
		
		List listUnit = this.unitdao.getAllUnit();
		
		it = listUnit.iterator();
		
		while (it.hasNext()){
			MsUnit unitPojo = (MsUnit)it.next();
			Listitem listItem = new Listitem();
			
			listItem.appendChild(new Listcell(unitPojo.getVUnitCode()+" - "+unitPojo.getVUnitName()));
			
			listItem.setValue(unitPojo);
			
			listItem.setParent(ctr.getUnitList());
		}
	}
	
	public void doModify(ScreenController ctr) throws VONEAppException {	
		ctr.getKodeScreen().select();
		
		Listitem listItem = ctr.getList().getSelectedItem();
		
		MsScreen screenPojo = (MsScreen)listItem.getValue();
		
		screenPojo = this.dao.getScreenByCode(screenPojo.getVScreenCode());
		
		//kodeScreen
		ctr.getKodeScreen().setText(screenPojo.getVScreenCode());

		//namaScreen
		ctr.getNamaScreen().setText(screenPojo.getVDesc());
		//subsystem
				
		for (int i=0; i<ctr.getSubsystem().getItemCount(); i++){
			Listitem item = ctr.getSubsystem().getItemAtIndex(i);
			
			if (((MsSubsystem)item.getValue()).getNSubsystemId().intValue() ==
				screenPojo.getMsSubsystem().getNSubsystemId().intValue()){
					ctr.getSubsystem().selectItem(item);
					break;
				}
		}
		
		
		//filling unit list
		ArrayList al = new ArrayList(); 
		
		Set set = screenPojo.getMsScreenInUnits();			
		
		Iterator it = set.iterator();
		
		while (it.hasNext()){
			MsScreenInUnit siuPojo = (MsScreenInUnit)it.next();

			
			al.add(siuPojo.getId().getMsUnit().getNUnitId());								
		}
				
		for (int i=0; i<ctr.getUnitList().getChildren().size(); i++){
			
			Listitem item = (Listitem)ctr.getUnitList().getChildren().get(i);
			
			MsUnit unitPojo = (MsUnit)item.getValue();
			
			if (al.contains(unitPojo.getNUnitId())){
				ctr.getUnitList().addItemToSelection(item);
			}
		}
		
		//end of filling unit list
		
		ctr.getKodeScreen().select();
		
		ctr.getList().setDisabled(true);
	}

}

package com.vone.medisafe.ward;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.sun.corba.se.pept.transport.InboundConnectionCache;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

/**
 * PatientInventoryController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 23 2006
 * @category user interface (visual)
 */

public class PatientInventoryController extends BaseController{
	
	public MsPatient patient;
	public TbPatientInventory inventory;
	public MsUser user;
	public TbRegistration reg;
	
	public List<MsItem> msItems;
	public List<TbPatientInventory> inventories;
	
	public Tree drugTree;
	public Treechildren child;
	public Listbox patientInventoryList;
	
	public Treeitem treeItem;
	public Treerow treeRow;
	public Treechildren children;
	public Treecell treeCell;
	
	Session session;
	Listitem item;
	Listcell cell;
	
	private PatientInventoryManager serv = Service.getPatientInventoryManager();
	
	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		user = getUserInfoBean().getMsUser();
		
		drugTree = (Tree)win.getFellow("drugTree");
		child = (Treechildren)win.getFellow("child");
		patientInventoryList = (Listbox)win.getFellow("patientInventoryList");
		
		patientInventoryList.getItems().clear();
		
		session = win.getDesktop().getSession();
		
		serv.getPatientInventory(this);
		serv.getHistoryInventory(this);
		MiscTrxController.setFont(patientInventoryList);
		MiscTrxController.setFont(drugTree);
	}
	
	
	
	
	
	public void save() throws VONEAppException, InterruptedException
	{
		Intbox intbox;
		boolean valid = true;
		List<TbPatientInventory> patientInventory = new ArrayList<TbPatientInventory>();
		
		Date tanggal = new Date();
		
		List listitems = patientInventoryList.getItems();
		Iterator it = listitems.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			MsItem msItem = (MsItem)item.getValue();
			
			cell = (Listcell)item.getChildren().get(5);
			Integer total = new Integer(((Listcell)item.getChildren().get(2)).getLabel());
			Integer terpakai = new Integer(((Listcell)item.getChildren().get(3)).getLabel());
			
			intbox = (Intbox)cell.getChildren().get(0);
			if(intbox.getValue() != null && intbox.getValue().intValue() != 0){
				inventory = new TbPatientInventory();
				inventory.setDWhnCreate(tanggal);
				inventory.setMsPatient(patient);
				inventory.setTbRegistration(reg);
				inventory.setVWhoCreate(user.getVUserName());
				inventory.setMsItem(msItem);
				if(intbox.getValue().intValue() > 0){
					int sisa = total - terpakai;
					if(sisa < intbox.getValue().intValue()) valid = false;
					inventory.setNQtyOut(intbox.getValue().shortValue());
				}else{
					Messagebox.show(MessagesService.getKey("common.input.negatif.notallowed"));
					
					intbox.focus();
					return;
					/**
					int qin = 0 - (intbox.getValue().intValue());
					inventory.setNQty((short)qin); */
				}
				patientInventory.add(inventory);
				
			}
			intbox.setValue(null);
			intbox.setStyle("font-size:8;font-weight:bold;color:#82FFA8");
			
		}
		
		if(!valid){
			Messagebox.show(MessagesService.getKey("patient.inventory.input.not.valid"));
			return;
		}
		
		if(patientInventory.size()>0)
		if(serv.save(patientInventory)){
			Messagebox.show(MessagesService.getKey("common.save.success"));
			child.getChildren().clear();
			patientInventoryList.getItems().clear();
			
			serv.getPatientInventory(this);
			serv.getHistoryInventory(this);
			
			
		}else{
			Messagebox.show(MessagesService.getKey("common.save.fail"));
		}
		
		MiscTrxController.setFont(patientInventoryList);
		MiscTrxController.setFont(drugTree);
	
	}
	
	
	public void delete() throws InterruptedException,VONEAppException{
		
		if(drugTree.getSelectedItems().size() == 0){
			Messagebox.show(MessagesService.getKey("patient.inventory.history.not.selected"));
			return;
		}
		
		if(drugTree.getSelectedItem().getValue() instanceof MsItem){
			return;
		}
		
		Set patInv = drugTree.getSelectedItems();
		Set<TbPatientInventory> patInvs = new HashSet<TbPatientInventory>();
		
		Iterator it = patInv.iterator();
		while(it.hasNext()){
			treeItem = (Treeitem)it.next();
			patInvs.add((TbPatientInventory)treeItem.getValue());
		}
		
		if(serv.delete(patInvs)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			child.getChildren().clear();
			patientInventoryList.getItems().clear();
			
			serv.getPatientInventory(this);
			serv.getHistoryInventory(this);
			
		}else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}

	}

}

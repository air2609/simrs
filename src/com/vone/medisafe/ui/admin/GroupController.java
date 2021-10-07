package com.vone.medisafe.ui.admin;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsGroup;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.admin.GroupManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class GroupController extends BaseController {

	UserManager userManager = ServiceLocator.getUserManager();
	GroupManager groupManager = ServiceLocator.getGroupManager();
	
	UserInfoBean uib = null;		
	
	ZulConstraint cst = new ZulConstraint();
	
	Textbox groupId = null;
	Textbox groupName = null;
	Listbox list = null;
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		groupId.setText("");
		groupName.setText("");
		
		groupId.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;		
		
		super.doDelete(cmp);
		
		groupManager.delete((MsGroup)list.getSelectedItem().getValue());
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));	
		MiscTrxController.setFont(list);
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		super.doModify(cmp);
		
		Listitem item = list.getSelectedItem();
		
		groupId.setText(((Listcell)item.getChildren().get(0)).getLabel());
		groupName.setText(((Listcell)item.getChildren().get(1)).getLabel());		
		
		list.setDisabled(true);
		
		groupId.select();
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (!cst.validateComponent(true))
			return;
		
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		super.doSaveAdd(cmp);		
		MsGroup groupPojo = new MsGroup();
		groupPojo.setVGroupName(groupId.getText());
		groupPojo.setVDesc(groupName.getText());
		
		groupPojo.setVWhocreate(uib.getStUserId());
		groupPojo.setDWhncreate(new Date());		
		
		groupManager.save(groupPojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		MiscTrxController.setFont(list);
		doCancel(cmp);
		
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub

		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;		
		
		super.doSaveModify(cmp);
		
		list.setDisabled(false);		
		
		MsGroup groupPojo = (MsGroup)list.getSelectedItem().getValue();
		
		groupPojo.setVGroupName(groupId.getText());
		groupPojo.setVDesc(groupName.getText());
		
		groupPojo.setVWhocreate(uib.getStUserId());
		groupPojo.setDWhncreate(new Date());
		
		groupManager.update(groupPojo);

		Messagebox.show(MessagesService.getKey("common.modify.success"));		
		
		redrawList();
		MiscTrxController.setFont(list);
		doCancel(cmp);				
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		uib = super.getUserInfoBean();
		
		groupId = (Textbox)cmp.getFellow("groupId");
		groupName = (Textbox)cmp.getFellow("groupName");

		list = (Listbox)cmp.getFellow("list");			
		
		cst.registerComponent(groupId);
		cst.registerComponent(groupName);
		cst.registerComponent(groupId,ZulConstraint.UPPER_CASE);
		cst.registerComponent(groupName,ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);				
		
		redrawList();
		
		groupId.focus();
		MiscTrxController.setFont(list);
	}

	public void gotoPriv(Component cmp) throws InterruptedException, VONEAppException{
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}		
		
		Page pageTab = (Page)cmp.getDesktop().getPage(ScreenConstant.GROUP_MASTER);
		
		Page pagePriv = (Page)cmp.getDesktop().getPage(ScreenConstant.GROUP_MASTER+"B");
		
		Listitem item = list.getSelectedItem();
		
		//set Group Id
		Textbox textGroup = (Textbox)pagePriv.getFellow("root").getFellow("groupId");
		textGroup.setText(((Listcell)item.getChildren().get(0)).getLabel());
		
		//setup privilege controller
		GroupPrivController privController = new GroupPrivController();
		privController.init(pagePriv.getFellow("root"));				
		
		Tab tab = (Tab)pageTab.getFellow("root").getFellow("tabGroupPriv");
		
		tab.setSelected(true);			
	}	
	
	private void redrawList() throws VONEAppException{
		
		list.getItems().clear();
		
		List listGroup = groupManager.getAllGroup(new MsGroup());
		
		Iterator it = listGroup.iterator();
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsGroup groupPojo = (MsGroup)it.next();
			
			item.appendChild(new Listcell(groupPojo.getVGroupName()));
			item.appendChild(new Listcell(groupPojo.getVDesc()));			
			
			item.setValue(groupPojo);
			
			item.setParent(list);
		}
		MiscTrxController.setFont(list);
	}
}

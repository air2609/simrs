package com.vone.medisafe.ui.admin;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsScreen;
import com.vone.medisafe.mapping.TbGroupPrivilege;
import com.vone.medisafe.mapping.TbGroupPrivilegeId;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.admin.GroupManager;
import com.vone.medisafe.service.iface.admin.GroupPrivilegeManager;
import com.vone.medisafe.service.iface.admin.ScreenManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class GroupPrivController extends BaseController {
	
	Logger logger = Logger.getLogger(UserPrivController.class);
	
	GroupPrivilegeManager groupPrivManager = ServiceLocator.getGroupPrivilegeManager();
	GroupManager groupManager = ServiceLocator.getGroupManager();
	ScreenManager screenManager = ServiceLocator.getScreenManager();
	
	UserInfoBean uib = null; 
	
	ZulConstraint cst = new ZulConstraint();
	
	Iterator it = null;
	
	Textbox groupCode = null;
	Bandbox screenCode = null;
	Listbox accessType = null;
	Textbox crScreenCode = null;
	Textbox crScreenName = null;
	Listbox list = null;
	Listbox listCr = null;
	
	MsScreen screenPojo = null;

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		screenCode.setDisabled(false);
		screenCode.setText("");
		screenCode.focus();
		accessType.setSelectedIndex(0);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!StringUtils.hasValue(groupCode.getText())){
			Messagebox.show(MessagesService.getKey("admin.userpriv.noaction"));
			return;
		}		
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;		
		
		super.doDelete(cmp);		
		
		groupPrivManager.delete((TbGroupPrivilege)list.getSelectedItem().getValue());
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));
		MiscTrxController.setFont(list);
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (!StringUtils.hasValue(groupCode.getText())){
			Messagebox.show(MessagesService.getKey("admin.userpriv.noaction"));
			return;
		}		
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}		
		
		super.doModify(cmp);
		
		Listitem item = list.getSelectedItem();
		
		screenCode.setText(((Listcell)item.getChildren().get(0)).getLabel());	
		
		screenCode.setDisabled(true);
		
		for (int i=0; i<accessType.getItemCount(); i++){
			Listitem temp = accessType.getItemAtIndex(i);
			
			if (temp.getValue().equals(((Listcell)item.getChildren().get(2)).getLabel())){
				accessType.selectItem(temp);
			}
		}	
		
		accessType.focus();
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!StringUtils.hasValue(groupCode.getText())){
			Messagebox.show(MessagesService.getKey("admin.userpriv.noaction"));
			return;
		}
		
		if (!cst.validateComponent(true))
			return;
		
		if (!checkScreen(screenCode.getText()) || screenPojo == null){
			Messagebox.show(MessagesService.getKey("admin.userpriv.screen.wrongcode"));
			screenCode.select();
			return;
		}			
		
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub		
		if (screenPojo == null){
			Messagebox.show(MessagesService.getKey("master.user.staff.internalerror"));
			return;
		}		
		
		super.doSaveAdd(cmp);
		
		TbGroupPrivilege privPojo = new TbGroupPrivilege();
		TbGroupPrivilegeId privPojoId = new TbGroupPrivilegeId();
		privPojo.setId(privPojoId);
		
		//set MsGroup		
		privPojoId.setMsGroup(groupManager.getGroupByCode(groupCode.getText()));
		privPojoId.setMsScreen(screenPojo);
		privPojo.setVAccessType((String)accessType.getSelectedItem().getValue());
		
		//set Date
		privPojo.setVWhocreate(uib.getStUserId());
		privPojo.setDWhncreate(new Date());		
		
		groupPrivManager.save(privPojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		MiscTrxController.setFont(list);
		doCancel(cmp);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (screenPojo == null){
			Messagebox.show(MessagesService.getKey("master.user.staff.internalerror"));
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;		
		
		super.doSaveModify(cmp);
		
		TbGroupPrivilege privPojo = new TbGroupPrivilege();
		TbGroupPrivilegeId privPojoId = new TbGroupPrivilegeId();
		privPojo.setId(privPojoId);
		
		//set MsGroup		
		privPojoId.setMsGroup(groupManager.getGroupByCode(groupCode.getText()));
		privPojoId.setMsScreen(screenPojo);
		privPojo.setVAccessType((String)accessType.getSelectedItem().getValue());
		
		//set Date
		privPojo.setVWhochange(uib.getStUserId());
		privPojo.setDWhnchange(new Date());
		
		groupPrivManager.update(privPojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		MiscTrxController.setFont(list);
		doCancel(cmp);
		
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
				
		uib = super.getUserInfoBean();
		
		groupCode = (Textbox)cmp.getFellow("groupId");
		screenCode = (Bandbox)cmp.getFellow("screenCode");
		accessType = (Listbox)cmp.getFellow("accessType");
		crScreenCode = (Textbox)cmp.getFellow("crScreenCode");
		crScreenName = (Textbox)cmp.getFellow("crScreenName");
		list = (Listbox)cmp.getFellow("list");
		listCr = (Listbox)cmp.getFellow("listCr");
		
		accessType.setSelectedIndex(0);
		
		cst.registerComponent(groupCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(screenCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(crScreenCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(crScreenName, ZulConstraint.UPPER_CASE);
		cst.registerComponent(screenCode);
		cst.validateComponent(false);
		
		groupCode.setReadonly(true);			
		
		screenCode.setText("");		
		
		redrawList();
		
		screenCode.focus();
		MiscTrxController.setFont(list);
	}
	
	public void updateScreenList(String screenCode, String screenName) throws InterruptedException, VONEAppException{
		
		if (listCr == null) return;
		
		listCr.getItems().clear();
		
		List list = screenManager.searchByCriteria(screenCode, screenName);
		
		it = list.iterator();
		
		while (it.hasNext()){
			
			MsScreen screenPojo = (MsScreen)it.next();
			
			Listitem item = new Listitem();
			item.appendChild(new Listcell(screenPojo.getVScreenCode()));
			item.appendChild(new Listcell(screenPojo.getVDesc()));
				
			item.setParent(listCr);
		}
		MiscTrxController.setFont(listCr);
	}

	private void redrawList() throws InterruptedException, VONEAppException{
		
		groupPrivManager.redrawList(this);
	}
	
	public boolean checkScreen(String screenCode) throws VONEAppException{
		
		List listScreen = screenManager.searchByCriteria(screenCode, "");
		
		if (listScreen == null || listScreen.size() < 1)
			return false;
		else
			screenPojo = (MsScreen)listScreen.get(0);
		
		return true;
	}

	public Listbox getAccessType() {
		return accessType;
	}

	public void setAccessType(Listbox accessType) {
		this.accessType = accessType;
	}

	public Textbox getCrScreenCode() {
		return crScreenCode;
	}

	public void setCrScreenCode(Textbox crScreenCode) {
		this.crScreenCode = crScreenCode;
	}

	public Textbox getCrScreenName() {
		return crScreenName;
	}

	public void setCrScreenName(Textbox crScreenName) {
		this.crScreenName = crScreenName;
	}

	public Textbox getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(Textbox groupCode) {
		this.groupCode = groupCode;
	}

	public Iterator getIt() {
		return it;
	}

	public void setIt(Iterator it) {
		this.it = it;
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	public Listbox getListCr() {
		return listCr;
	}

	public void setListCr(Listbox listCr) {
		this.listCr = listCr;
	}

	public Bandbox getScreenCode() {
		return screenCode;
	}

	public void setScreenCode(Bandbox screenCode) {
		this.screenCode = screenCode;
	}

	public MsScreen getScreenPojo() {
		return screenPojo;
	}

	public void setScreenPojo(MsScreen screenPojo) {
		this.screenPojo = screenPojo;
	}
	
}

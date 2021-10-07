package com.vone.medisafe.ui.admin;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
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
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbUserPrivilege;
import com.vone.medisafe.mapping.TbUserPrivilegeId;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.admin.ScreenManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.service.iface.admin.UserPrivilegeManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class UserPrivController extends BaseController {

	Logger logger = Logger.getLogger(UserPrivController.class);

	UserPrivilegeManager userPrivManager = ServiceLocator
			.getUserPrivilegeManager();

	ScreenManager screenManager = ServiceLocator.getScreenManager();

	UserManager userManager = ServiceLocator.getUserManager();

	UserInfoBean uib = null;

	ZulConstraint cst = new ZulConstraint();

	Iterator it = null;

	Textbox userCode = null;

	Bandbox screenCode = null;

	Listbox accessType = null;

	Textbox crScreenCode = null;

	Textbox crScreenName = null;

	Listbox list = null;

	Listbox listCr = null;

	MsScreen screenPojo = null;

	public void doCancel(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);

		screenCode.setDisabled(false);
		screenCode.setText("");
		screenCode.focus();
		accessType.setSelectedIndex(0);
	}

	public void doClose(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		if (!StringUtils.hasValue(userCode.getText())) {
			Messagebox.show(MessagesService.getKey("admin.userpriv.noaction"));
			return;
		}

		int respond = 0;

		respond = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "Question", Messagebox.YES
				| Messagebox.NO, Messagebox.QUESTION);

		if (respond != Messagebox.YES)
			return;

		super.doDelete(cmp);

		MsUser userPojo = userManager.getMsUser(userCode.getText());

		Listitem itemList = list.getSelectedItem();
		MsScreen screenPojo = screenManager.getScreenById((Integer) itemList
				.getValue());

		userPrivManager.delete(userPojo.getNUserId().intValue(), screenPojo
				.getNScreenId().intValue());

		redrawList();

		Messagebox.show(MessagesService.getKey("common.delete.success"));
		MiscTrxController.setFont(list);
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub

		if (!StringUtils.hasValue(userCode.getText())) {
			Messagebox.show(MessagesService.getKey("admin.userpriv.noaction"));
			return;
		}

		if (list.getSelectedCount() < 1) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		super.doModify(cmp);

		Listitem item = list.getSelectedItem();

		screenCode.setText(((Listcell) item.getChildren().get(0)).getLabel());

		screenCode.setDisabled(true);

		for (int i = 0; i < accessType.getItemCount(); i++) {
			Listitem temp = accessType.getItemAtIndex(i);

			if (temp.getValue().equals(
					((Listcell) item.getChildren().get(2)).getLabel())) {
				accessType.selectItem(temp);
			}
		}

		accessType.focus();
	}

	public void doSave(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		if (!StringUtils.hasValue(userCode.getText())) {
			Messagebox.show(MessagesService.getKey("admin.userpriv.noaction"));
			return;
		}

		if (!cst.validateComponent(true))
			return;

		if (!checkScreen(screenCode.getText()) || screenPojo == null) {
			Messagebox.show(MessagesService
					.getKey("admin.userpriv.screen.wrongcode"));
			screenCode.select();
			return;
		}

		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		if (screenPojo == null) {
			Messagebox.show(MessagesService
					.getKey("master.user.staff.internalerror"));
			return;
		}

		super.doSaveAdd(cmp);

		TbUserPrivilege privPojo = new TbUserPrivilege();
		TbUserPrivilegeId privPojoId = new TbUserPrivilegeId();
		privPojo.setId(privPojoId);

		// set MsUser
		privPojoId.setMsUser(userManager.getMsUser(userCode.getText()));
		privPojoId.setMsScreen(screenPojo);
		privPojo.setVAccessType((String) accessType.getSelectedItem()
				.getValue());

		// set Date
		privPojo.setVWhocreate(uib.getStUserId());
		privPojo.setDWhncreate(new Date());

		userPrivManager.save(privPojo);

		redrawList();

		Messagebox.show(MessagesService.getKey("common.add.success"));
		MiscTrxController.setFont(list);
		doCancel(cmp);
	}

	public void doSaveModify(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub

		if (screenPojo == null) {
			Messagebox.show(MessagesService
					.getKey("master.user.staff.internalerror"));
			return;
		}

		int respond = 0;

		respond = Messagebox.show(MessagesService
				.getKey("common.modify.confirm"), "Question", Messagebox.YES
				| Messagebox.NO, Messagebox.QUESTION);

		if (respond != Messagebox.YES)
			return;

		super.doSaveModify(cmp);

		TbUserPrivilege privPojo = new TbUserPrivilege();
		TbUserPrivilegeId privPojoId = new TbUserPrivilegeId();
		privPojo.setId(privPojoId);

		// set MsUser
		privPojoId.setMsUser(userManager.getMsUser(userCode.getText()));
		privPojoId.setMsScreen(screenPojo);
		privPojo.setVAccessType((String) accessType.getSelectedItem()
				.getValue());

		// set Date
		privPojo.setVWhochange(uib.getStUserId());
		privPojo.setDWhnchange(new Date());

		userPrivManager.update(privPojo);

		redrawList();

		Messagebox.show(MessagesService.getKey("common.modify.success"));

		doCancel(cmp);
		MiscTrxController.setFont(list);
	}

	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);

		userCode = (Textbox) cmp.getFellow("userId");
		screenCode = (Bandbox) cmp.getFellow("screenCode");
		accessType = (Listbox) cmp.getFellow("accessType");
		crScreenCode = (Textbox) cmp.getFellow("crScreenCode");
		crScreenName = (Textbox) cmp.getFellow("crScreenName");
		list = (Listbox) cmp.getFellow("list");
		listCr = (Listbox) cmp.getFellow("listCr");

		accessType.setSelectedIndex(0);

		cst.registerComponent(userCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(screenCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(crScreenCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(crScreenName, ZulConstraint.UPPER_CASE);
		cst.registerComponent(screenCode);
		cst.validateComponent(false);

		userCode.setReadonly(true);

		screenCode.setText("");

		try {
			uib = super.getUserInfoBean();
		} catch (Exception e) {
			logger.error("UIB ERROR", e);
			throw new VONEAppException(MessagesService.getKey("error.internal"));
		}

		redrawList();
		
		screenCode.focus();
		MiscTrxController.setFont(list);
	}

	public void updateScreenList(String screenCode, String screenName)
			throws InterruptedException, VONEAppException {

		if (listCr == null)
			return;

		listCr.getItems().clear();

		List list = screenManager.searchByCriteria(screenCode, screenName);

		it = list.iterator();

		while (it.hasNext()) {

			MsScreen screenPojo = (MsScreen) it.next();

			Listitem item = new Listitem();
			item.appendChild(new Listcell(screenPojo.getVScreenCode()));
			item.appendChild(new Listcell(screenPojo.getVDesc()));

			item.setParent(listCr);
		}
		MiscTrxController.setFont(listCr);
	}

	private void redrawList() throws InterruptedException, VONEAppException {

		userPrivManager.redrawList(this);
	}

	public boolean checkScreen(String screenCode) throws VONEAppException {

		List listScreen = screenManager.searchByCriteria(screenCode, "");

		if (listScreen == null || listScreen.size() < 1)
			return false;
		else
			screenPojo = (MsScreen) listScreen.get(0);

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

	public ZulConstraint getCst() {
		return cst;
	}

	public void setCst(ZulConstraint cst) {
		this.cst = cst;
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

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Bandbox getScreenCode() {
		return screenCode;
	}

	public void setScreenCode(Bandbox screenCode) {
		this.screenCode = screenCode;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public void setScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}

	public MsScreen getScreenPojo() {
		return screenPojo;
	}

	public void setScreenPojo(MsScreen screenPojo) {
		this.screenPojo = screenPojo;
	}

	public Textbox getUserCode() {
		return userCode;
	}

	public void setUserCode(Textbox userCode) {
		this.userCode = userCode;
	}

}

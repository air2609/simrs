package com.vone.medisafe.ui.admin;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsBranch;
import com.vone.medisafe.mapping.MsGroup;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admin.BranchManager;
import com.vone.medisafe.service.iface.admin.GroupManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.service.iface.master.StaffManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class UserController extends BaseController {

	UserManager userManager = ServiceLocator.getUserManager();
	GroupManager groupManager = ServiceLocator.getGroupManager();
	BranchManager branchManager = ServiceLocator.getBranchManager();
	StaffManager staffManager = MasterServiceLocator.getStaffManager();
	
	UserInfoBean uib = null;		
	
	ZulConstraint cst = new ZulConstraint();
	
	Textbox userId = null;
	Textbox userName = null;
	Listbox group = null;
	Bandbox staff = null;
	Listbox branch = null;
	Listbox list = null;
	
	Textbox crStaffCode = null;
	Textbox crStaffName = null;
	Listbox listCr = null;
	
	private MsStaff staffPojo = null;
	
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		userId.setText("");
		userName.setText("");
		if (group.getItemCount() > 0)
			group.setSelectedIndex(0);
		if (branch.getItemCount() > 0)
			branch.setSelectedIndex(0);
		staff.setText("");
		
		userId.focus();
	}

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
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
		
		MsUser userPojo = userManager.getUserById((Integer)list.getSelectedItem().getValue());
		
		userManager.delete(userPojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));	
		
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
		
		userId.setText(((Listcell)item.getChildren().get(0)).getLabel());
		userName.setText(((Listcell)item.getChildren().get(1)).getLabel());
		staff.setText(((Listcell)item.getChildren().get(3)).getLabel());
		
		Listcell groupCell = (Listcell)item.getChildren().get(2);		
		
		Listcell branchCell = new Listcell();		
		if (item.getChildren().size() > 4)
			branchCell = (Listcell)item.getChildren().get(4);
		else
			branch.setSelectedIndex(0);
		
		for (int i=0; i<group.getItemCount(); i++){
			Listitem temp = group.getItemAtIndex(i);
			
			if (temp.getValue().equals(groupCell.getValue())){
				group.selectItem(temp);
			}
		}
		
		for (int i=0; i<branch.getItemCount(); i++){
			Listitem temp = branch.getItemAtIndex(i);
			
			if (temp.getValue().equals(branchCell.getValue())){
				branch.selectItem(temp);
			}
		}		
				
		list.setDisabled(true);
		
		userId.select();
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (!cst.validateComponent(true))
			return;
		
		if (!checkStaff(staff.getText())){
			Messagebox.show(MessagesService.getKey("admin.user.staff.wrongcode"));
			staff.select();
			return;
		}
		
		
		try{
			super.doSave(cmp);
		}catch(VONEAppException e){
			throw new VONEAppException(e.getMessage());
		}
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub

		staffPojo = staffManager.getStaffByCode(this.staff.getValue());
		
		if (staffPojo == null){
			Messagebox.show(MessagesService.getKey("admin.user.staff.wrongcode"));
			staff.select();
			return;
		}		
		
		super.doSaveAdd(cmp);		
		MsUser userPojo = new MsUser();
		userPojo.setVUserName(userId.getText());
		userPojo.setVUserFullName(userName.getText());
		
		userPojo.setVWhocreate(uib.getStUserId());
		userPojo.setDWhncreate(new Date());		
		MsGroup groupPojo = new MsGroup();
		groupPojo.setNGroupId((Integer)group.getSelectedItem().getValue());							
		
		userPojo.setMsGroup(groupPojo);
		
		userPojo.setMsStaff(staffPojo);		
				
		
		if (!MedisafeConstants.LISTKOSONG.equals(branch.getSelectedItem().getValue())){
			MsBranch branchPojo = new MsBranch();
			branchPojo.setNBranchId((Integer)branch.getSelectedItem().getValue());
			
			userPojo.setMsBranch(branchPojo);
		}
		
		//set Password
		userPojo.setVPassword(new MD5(userId.getText()).toString());
		
		userManager.save(userPojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		
		doCancel(cmp);
		MiscTrxController.setFont(list);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		staffPojo = staffManager.getStaffByCode(this.staff.getValue());
		
		if (staffPojo == null){
			Messagebox.show(MessagesService.getKey("admin.user.staff.wrongcode"));
			staff.select();
			return;
		}				
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;		
		
		super.doSaveModify(cmp);
		
		list.setDisabled(false);
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		MsUser userPojo = userManager.getUserById((Integer)list.getSelectedItem().getValue());
		
		userPojo.setVUserName(userId.getText());
		userPojo.setVUserFullName(userName.getText());
		
		userPojo.setVWhocreate(uib.getStUserId());
		userPojo.setDWhncreate(new Date());
		
		MsGroup groupPojo = new MsGroup();
		groupPojo.setNGroupId((Integer)group.getSelectedItem().getValue());
		userPojo.setMsGroup(groupPojo);
		
		userPojo.setMsStaff(staffPojo);
		
		if (!MedisafeConstants.LISTKOSONG.equals(branch.getSelectedItem().getValue())){
			MsBranch branchPojo = new MsBranch();
			branchPojo.setNBranchId((Integer)branch.getSelectedItem().getValue());
			
			userPojo.setMsBranch(branchPojo);
		}		
		
		userManager.update(userPojo);
		
		redrawList();
		MiscTrxController.setFont(list);
		doCancel(cmp);				
	}
	
	public void initCP(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		Textbox oldP = (Textbox)cmp.getFellow("oldP");
		Textbox newP = (Textbox)cmp.getFellow("newP");
		Textbox retypeP = (Textbox)cmp.getFellow("retypeP");
		
		uib = super.getUserInfoBean();
		ZulConstraint cst = new ZulConstraint();
		cst.registerComponent(oldP);
		cst.registerComponent(newP);
		cst.registerComponent(retypeP);
		cst.validateComponent(false);
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		uib = super.getUserInfoBean();			
		
		userId = (Textbox)cmp.getFellow("userId");
		userName = (Textbox)cmp.getFellow("userName");
		group = (Listbox)cmp.getFellow("group");
		staff = (Bandbox)cmp.getFellow("staffCode");
		branch = (Listbox)cmp.getFellow("branch");
		list = (Listbox)cmp.getFellow("list");			
		
		crStaffCode = (Textbox)cmp.getFellow("crStaffCode");
		crStaffName = (Textbox)cmp.getFellow("crStaffName");
		listCr = (Listbox)cmp.getFellow("listCr");
		
		cst.registerComponent(userId);
		cst.registerComponent(userName);
		cst.registerComponent(group);
		cst.registerComponent(staff);
		cst.registerComponent(userId,ZulConstraint.UPPER_CASE);
		cst.registerComponent(userName,ZulConstraint.UPPER_CASE);
//		cst.registerComponent(staff,ZulConstraint.UPPER_CASE);
		cst.registerComponent(crStaffCode,ZulConstraint.UPPER_CASE);
		cst.registerComponent(crStaffName,ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);
		
		//draw GroupSubsystem
		List listGroup = groupManager.getAllGroup(new MsGroup());
		
		for (int i=0; i<listGroup.size(); i++){
			MsGroup groupPojo = (MsGroup)listGroup.get(i);
			
			Listitem item = new Listitem();
			item.setLabel(groupPojo.getVGroupName() +"-"+groupPojo.getVDesc());
			item.setValue(groupPojo.getNGroupId());
			
			item.setParent(group);
			
			if (i == 0)
				group.setSelectedItem(item);
		}
		
		//draw BranchSubsystem
		List listBreanch = branchManager.getAllBranch(new MsBranch());
		
		//set empty item
		Listitem itemEmpty = new Listitem();
		itemEmpty.setLabel("");
		itemEmpty.setValue(MedisafeConstants.LISTKOSONG);
		itemEmpty.setParent(branch);
		branch.setSelectedItem(itemEmpty);
		
		for (int i=0; i<listBreanch.size(); i++){
			MsBranch branchPojo = (MsBranch)listBreanch.get(i);
			
			Listitem item = new Listitem();
			item.setLabel(branchPojo.getVBranchName());
			item.setValue(branchPojo.getNBranchId());
			
			item.setParent(branch);
			
		}		
		
		redrawList();
		MiscTrxController.setFont(list);
		userId.focus();
	}

	public void doCP(Component cmp) throws InterruptedException  {
		
		Window win = (Window)Executions.createComponents("/zkpages/admin/user/changePassword.zul", null, null);
		
		win.doModal();
	}
	
	public UserInfoBean getUib() {
		return uib;
	}

	public boolean executeCP(String stOldP, String stNewP) throws InterruptedException, VONEAppException{
		
		if (uib == null) return false;
		
		MsUser userPojo = userManager.getMsUser(uib.getStUserId());		
		
		if (userPojo.getVPassword().equals(new MD5(stOldP).toString())){
			userPojo.setVPassword(new MD5(stNewP).toString());
			userPojo.setVWhochange(uib.getStUserId());
			userPojo.setDWhnchange(new Date());
			
			userManager.update(userPojo);
			
			Messagebox.show(MessagesService.getKey("admin.user.changepassword.success"));
			
			return true;
		}	
		
		Messagebox.show(MessagesService.getKey("admin.user.changepassword.fail"));
		
		return false;
	}
	
	public void gotoPriv(Component cmp) throws InterruptedException, VONEAppException{
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}		
		
		Page pageTab = (Page)cmp.getDesktop().getPage(ScreenConstant.USER_MAINTENANCE);
		
		Page pagePriv = (Page)cmp.getDesktop().getPage(ScreenConstant.USER_MAINTENANCE+"B");
		
		Listitem item = list.getSelectedItem();
		
		//set User Id
		Textbox textUser = (Textbox)pagePriv.getFellow("root").getFellow("userId");
		textUser.setText(((Listcell)item.getChildren().get(0)).getLabel());
		
		//setup privilege controller
		UserPrivController privController = new UserPrivController();
		privController.init(pagePriv.getFellow("root"));				
		
		Tab tab = (Tab)pageTab.getFellow("root").getFellow("tabUserPriv");
		
		tab.setSelected(true);			
		MiscTrxController.setFont(list);
	}
	
	public void redrawStaff() throws VONEAppException{
		
		listCr.getItems().clear();
		
		staffManager.searchStaff(this.crStaffCode.getText(), this.crStaffName.getText(), listCr);
			
		MiscTrxController.setFont(listCr);
	}
	
	private void redrawList() throws VONEAppException{
		
		userManager.redrawList(this);
	}
	
	/**
	 * check availbility of staffCode in MsStaff 
	 * and put value to staffPojo (Global Variable) 
	 * @param staffCode
	 * @return
	 * @throws VONEAppException
	 */
	public boolean checkStaff(String staffCode) throws VONEAppException {
		
		List listStaff = staffManager.searchStaff(staffCode, "");
		
		if (listStaff == null || listStaff.size() < 1) return false; else{
			
			staffPojo = (MsStaff)listStaff.get(0);						
		}
		
		return true;
	}
	
	public void cariClick(Listbox tList, Textbox input) throws VONEAppException{
		
		//TreatmentManager treatment = TreatmentService.getTreatmentManager();
		UserManager user = ServiceLocator.getUserManager();
		input.setValue(input.getText().toUpperCase());
		user.search("%"+input.getText()+"%",tList);
		
		MiscTrxController.setFont(tList);
	}

	public Listbox getBranch() {
		return branch;
	}

	public void setBranch(Listbox branch) {
		this.branch = branch;
	}

	public Textbox getCrStaffCode() {
		return crStaffCode;
	}

	public void setCrStaffCode(Textbox crStaffCode) {
		this.crStaffCode = crStaffCode;
	}

	public Textbox getCrStaffName() {
		return crStaffName;
	}

	public void setCrStaffName(Textbox crStaffName) {
		this.crStaffName = crStaffName;
	}

	public ZulConstraint getCst() {
		return cst;
	}

	public void setCst(ZulConstraint cst) {
		this.cst = cst;
	}

	public Listbox getGroup() {
		return group;
	}

	public void setGroup(Listbox group) {
		this.group = group;
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

	public Bandbox getStaff() {
		return staff;
	}

	public void setStaff(Bandbox staff) {
		this.staff = staff;
	}

	public Textbox getUserName() {
		return userName;
	}

	public void setUserName(Textbox userName) {
		this.userName = userName;
	}
}

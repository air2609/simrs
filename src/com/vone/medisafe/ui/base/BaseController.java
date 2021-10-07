package com.vone.medisafe.ui.base;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.general.authorization.UserInfoBean;

/**
 * Base Controller of ZK Screen Controller
 * CRUD
 * FORM ACTION
 * @author James Pang
 *
 */
public abstract class BaseController {
	
	UserInfoBean uib = null;
	
	protected Button btnSave;
	protected Button btnModify;
	protected Button btnCancel;
	protected Button btnDelete;
	protected Button btnClose;
	
	private boolean isComponentExist (String stId, Component componentFrom){
		
		if (componentFrom.getFellowIfAny(stId) == null)
			return false;
		
		return true;
	}	
	
	public void init(Component cmp) throws InterruptedException, VONEAppException{		
			
		if (Sessions.getCurrent().getAttribute(Constant.USER_INFO) == null || !(Sessions.getCurrent().getAttribute(Constant.USER_INFO) instanceof UserInfoBean)){
			Sessions.getCurrent().invalidate();
		}			
			
		VONEAppException exc = (VONEAppException)Sessions.getCurrent().getAttribute(Constant.EXCEPTION);
		
		uib = (UserInfoBean)Sessions.getCurrent().getAttribute(Constant.USER_INFO);
		
		cmp.getPage().setVariable(Constant.stFormAction, Constant.NEW_MODE);		
		
		if (isComponentExist("btnSave",cmp) && cmp.getFellow("btnSave") instanceof Button) 
			btnSave = (Button)cmp.getFellow("btnSave"); 
		else btnSave = null;

		if (isComponentExist("btnModify",cmp) && cmp.getFellow("btnModify") instanceof Button) 
			btnModify = (Button)cmp.getFellow("btnModify"); 
		else btnModify = null;
		
		if (isComponentExist("btnCancel",cmp) && cmp.getFellow("btnCancel") instanceof Button) 
			btnCancel = (Button)cmp.getFellow("btnCancel"); 
		else btnCancel = null;
		
		if (isComponentExist("btnDelete",cmp) && cmp.getFellow("btnDelete") instanceof Button) 
			btnDelete = (Button)cmp.getFellow("btnDelete"); 
		else btnDelete = null;
		
		if (isComponentExist("btnClose",cmp) && cmp.getFellow("btnClose") instanceof Button) 
			btnClose = (Button)cmp.getFellow("btnClose"); 
		else btnClose = null;		
		
		setInitButton();
		
	}
	
	public void doModify(Component cmp) throws InterruptedException, VONEAppException {		
		
		cmp.getPage().setVariable(Constant.stFormAction, Constant.MODIFY_ACTION);
		
		if (btnModify != null) btnModify.setDisabled(true);
		if (btnCancel != null) btnCancel.setDisabled(false);
		if (btnDelete != null) btnDelete.setDisabled(true);
	}
	
	public void doSave(Component cmp) throws InterruptedException, VONEAppException {						
		
		if (Constant.MODIFY_ACTION.equals(cmp.getPage().getVariable(Constant.stFormAction)))
			doSaveModify(cmp);
		else
			doSaveAdd(cmp);
		
	}
	
	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		cmp.getPage().setVariable(Constant.stFormAction, Constant.VIEW_ACTION);
		setInitButton();
	}
	
	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {		
		cmp.getPage().setVariable(Constant.stFormAction, Constant.VIEW_ACTION);
		setInitButton();
	}
	
	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		
		cmp.getPage().setVariable(Constant.stFormAction, Constant.VIEW_ACTION);
		
		setInitButton();
	}
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		
		cmp.getPage().setVariable(Constant.stFormAction, Constant.VIEW_ACTION);
				
		setInitButton();
	}
	
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		
//		cmp.getRoot().detach();
	}
	
	private void setInitButton(){
		
		if (btnSave != null) btnSave.setDisabled(false);
		if (btnModify != null) btnModify.setDisabled(false);
		if (btnCancel != null) btnCancel.setDisabled(true);
		if (btnDelete != null) btnDelete.setDisabled(false);	
		if (btnClose != null) btnClose.setDisabled(false);
	}

	public UserInfoBean getUserInfoBean() {
		return uib;
	}
	
	public Session getCurrentSession(){
		return Sessions.getCurrent();
	}
}

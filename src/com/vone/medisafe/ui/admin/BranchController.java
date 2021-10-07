package com.vone.medisafe.ui.admin;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsBranch;
import com.vone.medisafe.service.iface.admin.BranchManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class BranchController extends BaseController{
	
	BranchManager branchManager = ServiceLocator.getBranchManager();
	
	ZulConstraint cst = new ZulConstraint();
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		Textbox kodeBranch  = (Textbox)cmp.getFellow("kodeBranch");		
		
		kodeBranch.setText("");
		
		kodeBranch.focus();
	}

	public void doClose(Component cmp)throws InterruptedException, VONEAppException{
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException{
		// TODO Auto-generated method stub
		Listbox listBox = (Listbox)cmp.getFellow("listTable");
		
		if (listBox.getSelectedItem() == null){			
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			
			return;
		}	
		
		int respond = 0;
				
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;
		
		super.doDelete(cmp);
	
		MsBranch branchPojo = branchManager.getMsBranch((Integer)listBox.getSelectedItem().getValue());
		
		branchManager.deleteBranch(branchPojo);
		
		redrawList(cmp);
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));		
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException{
		// TODO Auto-generated method stub		
		
		Listbox listBox = (Listbox)cmp.getFellow("listTable");
		
		if (listBox.getSelectedItem() == null){

			Messagebox.show(MessagesService.getKey("admin.branch.listscreen.notselected"));

			return;
		}
		
		super.doModify(cmp);
				
		Textbox kodeBranch = (Textbox)cmp.getFellow("kodeBranch");				
		
		Listitem listItem = listBox.getSelectedItem();
		
		//kodeScreen
		Listcell listCell = (Listcell) listItem.getChildren().get(0);
		kodeBranch.setText(listCell.getLabel());
		
		kodeBranch.select();
		
		listBox.setDisabled(true);
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		
		cst.validateComponent(true);
		// TODO Auto-generated method stub
		super.doSave(cmp);
		
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub							
		
		Textbox kodeBranch  = (Textbox)cmp.getFellow("kodeBranch");
		
		super.doSaveAdd(cmp);
		
		MsBranch branchPojo = new MsBranch();
		
		branchPojo.setVBranchName(kodeBranch.getText());
		
		branchManager.saveBranch(branchPojo);
		
		redrawList(cmp);
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		
		kodeBranch.setText("");
		kodeBranch.focus();
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException{
		// TODO Auto-generated method stub
		
		Textbox kodeBranch  = (Textbox)cmp.getFellow("kodeBranch");		
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);	
		
		if (respond != Messagebox.YES)
			return;				
		
		super.doSaveModify(cmp);		
		
		Listbox listBox = (Listbox)cmp.getFellow("listTable");
		
		listBox.setDisabled(false);
		
		Listitem item = listBox.getSelectedItem();		
		
		branchManager.updateBranch(((Integer)item.getValue()).intValue(), kodeBranch.getText());
		
		redrawList(cmp);
		

		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		kodeBranch.setText("");
		kodeBranch.focus();

	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		redrawList(cmp);
		
		Textbox kodeBranch  = (Textbox)cmp.getFellow("kodeBranch");
		
		cst.registerComponent(kodeBranch);
		cst.registerComponent(kodeBranch, ZulConstraint.UPPER_CASE);
		cst.validateComponent(false);
	}

	public void redrawList(Component win) throws VONEAppException{
		
		Listbox listBox = (Listbox)win.getFellow("listTable");		
		
		listBox.getItems().clear();
		
		List listBranch = branchManager.getAllBranch(new MsBranch());
		
		Iterator it = listBranch.iterator();
		
		while (it.hasNext()){
			Listitem listItem = new Listitem();
			MsBranch branchPojo = (MsBranch)it.next();
			
			Listcell cellSubsystem = new Listcell();
			cellSubsystem.setLabel(branchPojo.getVBranchName());
			
			listItem.appendChild(cellSubsystem);
			
			listItem.setValue(branchPojo.getNBranchId());
			
			listItem.setParent(listBox);
		}		
	}
	
}

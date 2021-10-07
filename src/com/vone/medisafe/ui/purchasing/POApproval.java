package com.vone.medisafe.ui.purchasing;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.NumberUtil;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.TbPurchaseRequestDetail;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.PurchaseServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class POApproval extends BaseController{
	
	Logger logger = Logger.getLogger(POApproval.class);

	Listbox list = null;
	Button btnApprove = null;
		
	Bandbox opNo = null;
	Textbox issuedBy = null;
	Textbox approvedBy = null;
	Button btnSearch = null;
	
	Listbox listOp = null;
	Textbox opNoSearch = null;
	
	Label status = null;
	
	Checkbox isvalid = null;
	
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		list = (Listbox)cmp.getFellow("list");
		btnApprove = (Button)cmp.getFellow("btnApprove");
		opNo = (Bandbox)cmp.getFellow("opNo");
		issuedBy = (Textbox)cmp.getFellow("issuedBy");
		approvedBy = (Textbox)cmp.getFellow("approvedBy");
		
		this.btnSearch = (Button)cmp.getFellow("search");
		this.listOp = (Listbox)cmp.getFellow("listOp");
		this.opNoSearch = (Textbox)cmp.getFellow("opNoSearch");
		
		this.isvalid = (Checkbox)cmp.getFellow("isvalid");
		
		status = (Label)cmp.getFellow("status");
		
		opNo.addEventListener("onChange", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redraw();
				} catch (Exception e) {
					// TODO Auto-generated catch block					
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR",e);					
				}
			}
			
		});
		listOp.addEventListener("onSelect", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redraw();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR",e);
				}
			}
			
		});		
		
		this.opNo.focus();
	}

	public void doSearch(Component cmp) throws InterruptedException, VONEAppException {
	
		PurchaseServiceLocator.getPOManager().doSearchApproval(cmp);
	}
	
	public void doApprove(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub		
		
		TbPurchaseOrder pojo = null;
		
		try{
			pojo = PurchaseServiceLocator.getPOManager().getPOByCode(this.opNo.getValue());
			
			if (pojo == null)
				pojo = (TbPurchaseOrder)this.listOp.getSelectedItem().getValue();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.opNo.select();			
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("pch.order.approve.question"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;		
		
		PurchaseServiceLocator.getPOManager().doApprove(this, pojo);
	}
	
	private void redraw() throws Exception {						
		
		TbPurchaseOrder pojo = null;
		
		try {
			pojo = PurchaseServiceLocator.getPOManager().getPOByCode(this.opNo.getValue());
			
			if (pojo == null)
				pojo = (TbPurchaseOrder)this.listOp.getSelectedItem().getValue();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block			
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.opNo.select();	
		}
		
		if (pojo == null) return;
		
		PurchaseServiceLocator.getPOManager().redraw(this, pojo);
	}

	public Textbox getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Textbox approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Button getBtnApprove() {
		return btnApprove;
	}

	public void setBtnApprove(Button btnApprove) {
		this.btnApprove = btnApprove;
	}

	public Button getBtnSearch() {
		return btnSearch;
	}

	public void setBtnSearch(Button btnSearch) {
		this.btnSearch = btnSearch;
	}

	public Textbox getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(Textbox issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	public Listbox getListOp() {
		return listOp;
	}

	public void setListOp(Listbox listOp) {
		this.listOp = listOp;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Bandbox getOpNo() {
		return opNo;
	}

	public void setOpNo(Bandbox opNo) {
		this.opNo = opNo;
	}

	public Textbox getOpNoSearch() {
		return opNoSearch;
	}

	public void setOpNoSearch(Textbox opNoSearch) {
		this.opNoSearch = opNoSearch;
	}

	public Label getStatus() {
		return status;
	}

	public void setStatus(Label status) {
		this.status = status;
	}
	
}

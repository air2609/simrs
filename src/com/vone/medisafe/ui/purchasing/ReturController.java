package com.vone.medisafe.ui.purchasing;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.service.PurchaseServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class ReturController extends BaseController{

	Bandbox doNo = null;
	Textbox doNoSearch = null;
	Textbox batchNoSearch = null;
	Listbox doList = null;
	Listbox list = null;
	Textbox poNo = null;
	Textbox recDate = null;
	Textbox recBy = null;
	Textbox approvedBy = null;
	Textbox supCode = null;
	Textbox supName = null;
	Textbox supAddress = null;
	Textbox supTelp = null;
	Button btnRetur1 = null;
	Button btnRetur2 = null;
	
	public static final String itemName = "ITEMNAME";
	public static final String batchNo = "BATCHNO";
	public static final String qtyReturn = "QTYRETURN";
	public static final String totalPrice = "TOTALPRICE";
	public static final String qtyInventory = "QTYINVENTORY";
	public static final String tbInventory = "TBINVENTORY";
	
	public static final String returSession = "RETURSESSION";
	
	Logger logger = Logger.getLogger(this.toString());
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		this.doNo = (Bandbox)cmp.getFellow("doNo");
		this.doNoSearch = (Textbox)cmp.getFellow("doNoSearch");
		this.batchNoSearch = (Textbox)cmp.getFellow("batchNoSearch");
		this.doList = (Listbox)cmp.getFellow("doList");
		this.list = (Listbox)cmp.getFellow("list");		
		this.poNo = (Textbox)cmp.getFellow("poNo");
		this.recDate = (Textbox)cmp.getFellow("recDate");
		this.recBy = (Textbox)cmp.getFellow("recBy");
		this.approvedBy = (Textbox)cmp.getFellow("approvedBy");
		this.supCode = (Textbox)cmp.getFellow("supCode");
		this.supName = (Textbox)cmp.getFellow("supName");
		this.supAddress = (Textbox)cmp.getFellow("supAddress");
		this.supTelp = (Textbox)cmp.getFellow("supTelp");
		this.btnRetur1 = (Button)cmp.getFellow("btnRetur1");
		this.btnRetur2 = (Button)cmp.getFellow("btnRetur2");
		
		ZulConstraint cst = new ZulConstraint();
		cst.registerComponent(this.doNoSearch, ZulConstraint.UPPER_CASE);
		cst.registerComponent(this.batchNoSearch, ZulConstraint.UPPER_CASE);
		cst.validateComponent(false);
		
		setInit();	
		
		doNo.addEventListener(Events.ON_CHANGE, new EventListener(){

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
					e.printStackTrace();
					logger.error(e);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e);
				}
			}
			
		});
		
		this.doNo.focus();
	}
	
	public boolean isValidInput(){
		
		if (list.getSelectedCount() < 1)
			return false;
		
		Iterator<Listitem> it = list.getItems().iterator();
		
		while (it.hasNext()){
			Listitem item = it.next();
			
			if (item.isSelected()){
				
				Intbox box = (Intbox)item.getAttribute(qtyReturn);			
			
				if (box.getValue() == null || box.getValue() <= 0){
					box.select();
					return false;
				}
				
				Float qtyInv = (Float)item.getAttribute(qtyInventory);
				
				if (box.getValue() > qtyInv){
					box.select();
					return false;
				}
			}
			
		}
		
		return true;
	}
	
	public void doClickBtn1() throws InterruptedException {
		
		if (!isValidInput()){
			Messagebox.show(MessagesService.getKey("pch.do.invalid.input"));
			
			return;
		}
		
		//set controller to session
		super.getCurrentSession().setAttribute(returSession, this);
		
		final Window win = (Window) Executions.createComponents(
 					"/zkpages/purchasing/returDetail.zul",null,null);
		win.doModal();
	}
	
	public void redraw() throws VONEAppException, InterruptedException{
		
		if (PurchaseServiceLocator.getDOManager().getDObyCode(this.doNo.getText()) == null){
			
			Messagebox.show(MessagesService.getKey("pch.do.bpp.error"));
			
			this.doNo.select();
			
			return;
		}
		
		PurchaseServiceLocator.getReturManager().redraw(this);
	}
	
	public void doSearch() throws VONEAppException {
		
		PurchaseServiceLocator.getReturManager().doSearch(this);
	}
	
	public void setInit(){
		
		this.list.getItems().clear();
		this.doNo.setText("");
		this.poNo.setText("");
		this.approvedBy.setText("");
		this.recDate.setText("");
		this.recBy.setText("");
		this.supAddress.setText("");
		this.supCode.setText("");
		this.supName.setText("");
		this.supTelp.setText("");
		this.doList.getItems().clear();
		this.doNoSearch.setText("");
		this.batchNoSearch.setText("");
	}

	public Textbox getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Textbox approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Textbox getBatchNoSearch() {
		return batchNoSearch;
	}

	public void setBatchNoSearch(Textbox batchNoSearch) {
		this.batchNoSearch = batchNoSearch;
	}

	public Listbox getDoList() {
		return doList;
	}

	public void setDoList(Listbox doList) {
		this.doList = doList;
	}

	public Bandbox getDoNo() {
		return doNo;
	}

	public void setDoNo(Bandbox doNo) {
		this.doNo = doNo;
	}

	public Textbox getDoNoSearch() {
		return doNoSearch;
	}

	public void setDoNoSearch(Textbox doNoSearch) {
		this.doNoSearch = doNoSearch;
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	public Textbox getPoNo() {
		return poNo;
	}

	public void setPoNo(Textbox poNo) {
		this.poNo = poNo;
	}

	public Textbox getRecBy() {
		return recBy;
	}

	public void setRecBy(Textbox recBy) {
		this.recBy = recBy;
	}

	public Textbox getRecDate() {
		return recDate;
	}

	public void setRecDate(Textbox recDate) {
		this.recDate = recDate;
	}

	public Textbox getSupAddress() {
		return supAddress;
	}

	public void setSupAddress(Textbox supAddress) {
		this.supAddress = supAddress;
	}

	public Textbox getSupCode() {
		return supCode;
	}

	public void setSupCode(Textbox supCode) {
		this.supCode = supCode;
	}

	public Textbox getSupName() {
		return supName;
	}

	public void setSupName(Textbox supName) {
		this.supName = supName;
	}

	public Textbox getSupTelp() {
		return supTelp;
	}

	public void setSupTelp(Textbox supTelp) {
		this.supTelp = supTelp;
	}

}

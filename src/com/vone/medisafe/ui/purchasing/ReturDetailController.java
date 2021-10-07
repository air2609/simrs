package com.vone.medisafe.ui.purchasing;

import java.math.BigDecimal;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;
import com.vone.medisafe.service.PurchaseServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class ReturDetailController extends BaseController{

	Listbox list = null;
	Listbox apList = null;
	Decimalbox total = null;
	
	ReturController retCtr = null;
	
	Textbox cancelReason = null;
	
	Window window = null;
	
	public static final String qtyReturChild = "QTYRETURCHILD";
	public static final String totalPriceChild = "TOTALPRICECHILD";
	
	public static final String RETURDETAILSESSION = "RETURDETAILSESSION";
	public static final String RETURDETAILPRICE = "RETURDETAILPRICE";
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		this.list = (Listbox)cmp.getFellow("list");
		this.apList = (Listbox)cmp.getFellow("aplist");
		this.total = (Decimalbox)cmp.getFellow("total");
		this.total.setFormat("#,###.##");
		
		retCtr = (ReturController)super.getCurrentSession().getAttribute(ReturController.returSession);
		
		this.window = (Window)cmp.getFellow("returDetail");
		
		redraw();
		
		Iterator<Listitem> it = this.getList().getItems().iterator();
		while (it.hasNext()){
			Listitem item = it.next();
			Object obj = item.getAttribute(ReturController.tbInventory);
		}
		
		calculatePrice();
		
		generateAPList();
	}		
	
	private void generateAPList() throws VONEAppException{
		
		PurchaseServiceLocator.getReturManager().generateAPList(this);
	}
	
	public void doClick() throws InterruptedException {
		
		super.getCurrentSession().setAttribute(this.RETURDETAILSESSION, this);
		
		final Window win = (Window) Executions.createComponents(
				"/zkpages/purchasing/returKonfirmasi.zul",null,null);
		
		win.doModal();
	}
	
	public void initConfirm(Component cmp) throws InterruptedException, VONEAppException {
		
		this.cancelReason = (Textbox)cmp.getFellow("cancelReason");
		
		this.cancelReason.focus();
	}

	public void redraw() {
		
		Listbox listParent = retCtr.getList();
		Iterator<Listitem> it = listParent.getItems().iterator();
		
		while (it.hasNext()){
			Listitem item = it.next();			
			
			if (!item.isSelected())
				continue;
			
			
			//GET ALL ATTRIBUTES FROM PARENTS (ReturManagerImpl)
			String itemName = (String)item.getAttribute(ReturController.itemName);
			String batchNo = (String)item.getAttribute(ReturController.batchNo);
			Intbox qty = (Intbox)item.getAttribute(ReturController.qtyReturn);
			Double price = (Double)item.getAttribute(ReturController.totalPrice);
			TbBatchItem tbBI = (TbBatchItem)item.getValue();
			TbItemInventory tbII = (TbItemInventory)item.getAttribute(ReturController.tbInventory);
			//END OF GETTING
			
			Listitem itemChild = new Listitem();			
			itemChild.appendChild(new Listcell(itemName));
			itemChild.appendChild(new Listcell(batchNo));
			itemChild.appendChild(new Listcell(""+qty.getValue()));
			
			Listcell cell = new Listcell();
			Decimalbox box = new Decimalbox();
			box.setFormat("#,###.##");
			box.setValue(new BigDecimal(price * qty.getValue()));
			box.setParent(cell);			
			box.setStyle("background-color:"+MedisafeConstants.COLOR_INPUT+";width:95%");			
			cell.setParent(itemChild);
			
			//ADD PRICE INFORMATION TO LISTITEM SESSION
			itemChild.setAttribute(this.RETURDETAILPRICE, box);
			//ADD BATCH INFORMATION TO LISTITEM SESSION
			itemChild.setValue(tbBI);
			itemChild.setAttribute(ReturController.tbInventory, tbII);
			itemChild.setAttribute(this.qtyReturChild, qty);
			itemChild.setAttribute(this.totalPriceChild, box.getValue().doubleValue());
			
			box.addEventListener(Events.ON_BLUR, new EventListener(){

				public boolean isAsap() {
					// TODO Auto-generated method stub
					return true;
				}

				public void onEvent(Event arg0) {
					// TODO Auto-generated method stub
					calculatePrice();
				}
				
			});
			
			itemChild.setParent(this.list);
		}
	}
	
	public void calculatePrice(){
		
		Iterator it = this.list.getItems().iterator();
		
		Double totalPrice = new Double(0);
		
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			
			Decimalbox price  = (Decimalbox)item.getAttribute(this.RETURDETAILPRICE);
						
			totalPrice += price.getValue().doubleValue();			
		}
		
		this.total.setValue(new BigDecimal(totalPrice));
		
	}
	
	public Window getWindow(){
		return this.window;
	}
	
	public void doSave() throws VONEAppException, InterruptedException{
		
		ReturDetailController ctr = (ReturDetailController)super.getCurrentSession().getAttribute(ReturDetailController.RETURDETAILSESSION);
		ctr.setCancelReason(this.getCancelReason());		
						
		PurchaseServiceLocator.getReturManager().save(ctr);
	}

	public Textbox getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(Textbox cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	public ReturController getRetCtr() {
		return retCtr;
	}

	public void setRetCtr(ReturController retCtr) {
		this.retCtr = retCtr;
	}

	public Decimalbox getTotal() {
		return total;
	}

	public void setTotal(Decimalbox total) {
		this.total = total;
	}

	public Listbox getApList() {
		return apList;
	}

	public void setApList(Listbox apList) {
		this.apList = apList;
	}
}

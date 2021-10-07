package com.vone.medisafe.ui.purchasing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.NumberUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbDeliveryOrder;
import com.vone.medisafe.mapping.TbDeliveryOrderDetail;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.PurchaseServiceLocator;

public class DOController extends PurchaseController{
	
	Logger logger = Logger.getLogger(DOController.class);

	ZulConstraint cst = new ZulConstraint();
		
	Listbox location = null;
	Listbox listOp = null;
	Bandbox poNo = null;
	Textbox poNoSearch = null;
	Textbox supNameSearch = null;
	
	Textbox doNoSearch = null;
	Textbox whouseSearch = null;
	Listbox listDo = null;
	
	Datebox recDate = null;
	Textbox recBy = null;
	Bandbox bppNo = null;
	Textbox approvedBy = null;
	
	Textbox supCode = null;
	Textbox supName = null;
	Textbox supAddress = null;
	Textbox supTelp = null;
		
	Label status = null;
	Listbox list = null;
	
	Decimalbox total = null;
	Decimalbox discount = null;
	Listbox discountType = null;
	
	Decimalbox ppn = null;
	Listbox ppnType = null;
	
	Decimalbox gtotal = null;
	
	Button btnNew = null;
	Button btnRevoke = null;
	Button btnApprove = null;
	Button btnPrint = null;
	
	//SESSION ITEM LIST
	public static final String ITEMARRIVEDINTBOX = "ITEM_ARRIVED";
	public static final String BONUSARRIVEDINTBOX = "BONUS_ARRIVED";
	public static final String ITEM_CODE = "ITEM_CODE";
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		//CLEAR SESSION
		super.getCurrentSession().removeAttribute(MedisafeConstants.DO_SESSION);		
				
		this.location = (Listbox)cmp.getFellow("location");
		this.listOp = (Listbox)cmp.getFellow("listOp");
		this.poNo = (Bandbox)cmp.getFellow("poNo");
		this.poNoSearch = (Textbox)cmp.getFellow("poNoSearch");
		this.supNameSearch = (Textbox)cmp.getFellow("supNameSearch");
		
		this.recDate = (Datebox)cmp.getFellow("recDate");
		this.recDate.setValue(new Date());
		
		doNoSearch = (Textbox)cmp.getFellow("doNoSearch");
		whouseSearch = (Textbox)cmp.getFellow("whouseSearch");
		listDo = (Listbox)cmp.getFellow("listDo");
		
		this.recBy = (Textbox)cmp.getFellow("recBy");
		this.recBy.setDisabled(true);
		this.recBy.setValue(super.getUserInfoBean().getStUserName());
		
		this.bppNo = (Bandbox)cmp.getFellow("bppNo");
		this.approvedBy = (Textbox)cmp.getFellow("approvedBy");
		
		this.supCode = (Textbox)cmp.getFellow("supCode");
		this.supName = (Textbox)cmp.getFellow("supName");
		this.supAddress = (Textbox)cmp.getFellow("supAddress");
		this.supTelp = (Textbox)cmp.getFellow("supTelp");
			
		this.status = (Label)cmp.getFellow("status");
		this.list = (Listbox)cmp.getFellow("list");
		
		this.total = (Decimalbox)cmp.getFellow("total");
		this.discount = (Decimalbox)cmp.getFellow("discount");
		this.discountType = (Listbox)cmp.getFellow("discountType");
		MedisafeUtil.setListPaymentOptions(this.discountType);
		
		this.ppn = (Decimalbox)cmp.getFellow("ppn");
		this.ppnType = (Listbox)cmp.getFellow("ppnType");
		MedisafeUtil.setListPaymentOptions(this.ppnType);
		this.ppnType.setSelectedIndex(1);
		
		this.gtotal = (Decimalbox)cmp.getFellow("gtotal");
		
		this.btnNew = (Button)cmp.getFellow("btnNew");
		this.btnRevoke = (Button)cmp.getFellow("btnRevoke");
		this.btnApprove = (Button)cmp.getFellow("btnApprove");
		this.btnPrint = (Button)cmp.getFellow("btnPrint");
		
		cst.registerComponent(poNoSearch,ZulConstraint.UPPER_CASE);
		cst.registerComponent(supNameSearch,ZulConstraint.UPPER_CASE);
		cst.registerComponent(doNoSearch, ZulConstraint.UPPER_CASE);
		cst.registerComponent(whouseSearch, ZulConstraint.UPPER_CASE);
		cst.registerComponent(this.bppNo,ZulConstraint.UPPER_CASE);
		cst.registerComponent(this.bppNo,ZulConstraint.NO_EMPTY);
		cst.registerComponent(this.recDate, ZulConstraint.NO_EMPTY);
		cst.validateComponent(false);
		
		//PUT CONTROLLER TO SESSION
		super.getCurrentSession().setAttribute(MedisafeConstants.PURCHASING_SESSION, this);
		
		//fill warehouse						
		location.getItems().clear();
		List listWhouse = MasterServiceLocator.getWarehouseManager().getWhouseByStaffId(super.getUserInfoBean().getMsUser().getMsStaff().getNStaffId());
		
		if (listWhouse == null){
			setErrorButton();
			
			return;
		}		
		
		Iterator it = listWhouse.iterator();
		
		while (it.hasNext()){			
			
			MsWarehouse whousePojo = (MsWarehouse)it.next();
			
			if (whousePojo == null)
				continue;			
			
			Listitem item = new Listitem();
			item.setParent(location);
			
			item.appendChild(new Listcell(whousePojo.getVWhouseCode()+" - "+whousePojo.getVWhouseName()));
			item.setValue(whousePojo);
		}
		
		if (location.getItemCount() > 0)
			location.setSelectedIndex(0);
		//end filling warehouse
		
		poNo.addEventListener("onChange", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					listOp.clearSelection();
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
				
		this.ppn.addEventListener("onChange", new PPNDiscountListener());
		this.ppnType.addEventListener("onSelect", new PPNDiscountListener());
		
		listDo.addEventListener("onSelect", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					doSelectDO();
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
		
		setInitButton();
	}
	
	public void doSearch(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub	
		
		PurchaseServiceLocator.getDOManager().doSearch(this);
	}	
	
	public void doSearchDO() throws InterruptedException, VONEAppException { 
		
		PurchaseServiceLocator.getDOManager().doSearchDO(this);
	}
	
	public void redraw() throws InterruptedException, VONEAppException {
		
		TbPurchaseOrder poPojo = null;
		
		try{	
			if (listOp.getSelectedItem() == null){
				poPojo = PurchaseServiceLocator.getPOManager().getPOByCode(this.poNo.getText());
				if (poPojo == null)
					throw new NullPointerException();
			}else			
				poPojo = (TbPurchaseOrder)listOp.getSelectedItem().getValue();
			
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block			
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.poNo.select();	
			return;
		}
		
		if (MedisafeConstants.RP.equals(poPojo.getVDiscountType())){
			this.discountType.setSelectedIndex(0);
		}else{
			this.discountType.setSelectedIndex(1);
		}
		
		this.discountType.setDisabled(true);		
		this.ppnType.setSelectedIndex(1);
		this.discount.setValue(new BigDecimal(poPojo.getNDiscount()));
		this.discount.setDisabled(true);
		this.ppn.setValue(new BigDecimal(10));
						
		this.list.getItems().clear();
		
		PurchaseServiceLocator.getDOManager().redraw(this, poPojo);
	}
	
	@Override
	public void addItem(MsItem item) throws VONEAppException {
		// TODO Auto-generated method stub
		super.addItem(item);
	}

	@Override
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		setAfterSaveButtons();
	}
	
	public void doNew() throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		//remove DO Session
		super.getCurrentSession().removeAttribute(MedisafeConstants.DO_SESSION);
		Page pageBatch = (Page)this.list.getDesktop().getPage(ScreenConstant.DELIVERY_ORDER+"B");		
		DOBatchController batchCtr = new DOBatchController();
		batchCtr.init(pageBatch.getFellow("batchItem"));
		
		this.poNo.setText("");
		this.recDate.setValue(new Date());
		this.bppNo.setText("");
		this.status.setValue("");
		this.ppn.setValue(new BigDecimal(10));
		this.ppnType.setSelectedIndex(1);
		this.list.getItems().clear();
		this.listOp.getItems().clear();	
		this.listDo.getItems().clear();
		this.total.setText("");
		this.gtotal.setText("");
		this.supCode.setText("");
		this.supAddress.setText("");
		this.supName.setText("");
		this.supTelp.setText("");
		
		setInitButton();
	}

	@Override
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	@Override
	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
	}

	@Override
	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		
		setModifyButton();
	}

	@Override
	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!cst.validateComponent(true))
			return;
		
		if (!PurchaseServiceLocator.getDOManager().isValidInput(this))
			return;		
		
		super.doSave(cmp);		
	}

	@Override
	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);		
		
		TbPurchaseOrder tbPO = null;
		
		try{	
			if (listOp.getSelectedItem() == null){
				tbPO = PurchaseServiceLocator.getPOManager().getPOByCode(poNo.getText());
				if (tbPO == null)
					throw new NullPointerException();
			}else			
				tbPO = (TbPurchaseOrder)listOp.getSelectedItem().getValue();
			
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block			
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.poNo.select();	
			return;
		}
		
		//check for Duplicate BPP;
		if (PurchaseServiceLocator.getDOManager().getDObyCode(this.bppNo.getText()) != null){
			Messagebox.show(MessagesService.getKey("pch.do.duplicate.bpp"));
			this.bppNo.select();
			return;
		}
		
		//setup recieve date
		if (this.recDate.getValue() == null)
			this.recDate.setValue(new Date());

		TbDeliveryOrder tbDO = new TbDeliveryOrder();
		PurchaseServiceLocator.getDOManager().doSaveAdd(this, tbPO, tbDO);
		
		setAfterSaveButtons();
		
		//save DO to session
		Page pageBatch = (Page)cmp.getDesktop().getPage(ScreenConstant.DELIVERY_ORDER+"B");		
		super.getCurrentSession().setAttribute(MedisafeConstants.DO_SESSION, tbDO);
		DOBatchController batchCtr = new DOBatchController();
		batchCtr.init(pageBatch.getFellow("batchItem"));
	}

	@Override
	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);	
		
		if (respond != Messagebox.YES)
			return;				
		
		super.doSaveModify(cmp);
		
		TbDeliveryOrder tbDO = PurchaseServiceLocator.getDOManager().getDObyCode(this.bppNo.getText());
		
		if (tbDO == null){
			Messagebox.show(MessagesService.getKey("pch.do.bpp.error"));			
			return;
		}
		
		//set Warehouse
		tbDO.setMsWarehouse((MsWarehouse)this.location.getSelectedItem().getValue());
		//set Tax
		tbDO.setNDoTax(this.ppn.getValue().doubleValue());
		//set Tax Type
		tbDO.setVTaxType((String)this.ppnType.getSelectedItem().getValue());
		//set RecDate
		tbDO.setDRecDate(this.recDate.getValue());
		
		//get priceAfterDiscount
		double priceAfterDiscount = 0;
		if (MedisafeConstants.RP.equals(this.discountType.getSelectedItem().getValue())){
			priceAfterDiscount = this.total.getValue().doubleValue() - this.discount.getValue().doubleValue();
		}else{
			priceAfterDiscount = this.total.getValue().doubleValue() - (this.total.getValue().doubleValue() * this.discount.getValue().doubleValue() / 100);
		}
		
		tbDO.setNTotal(new Double(this.total.getValue().doubleValue()));
		tbDO.setNTotalAfterDisc(new Double(priceAfterDiscount));
		tbDO.setNTotalAfterPpn(new Double(this.gtotal.getValue().doubleValue()));
		
		//set WhoCreate & WhnCreate
		tbDO.setVWhoChange(super.getUserInfoBean().getStUserId());
		tbDO.setDWhnChange(new Date());
		
		Set<TbDeliveryOrderDetail> set = new HashSet<TbDeliveryOrderDetail>();
		tbDO.setTbDeliveryOrderDetails(set);
		
		Iterator it = this.list.getItems().iterator();
		
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			TbPurchaseOrderDetail tbPOD = (TbPurchaseOrderDetail)item.getValue();
			
			TbDeliveryOrderDetail tbDOD = new TbDeliveryOrderDetail();
			
			//set DODetail
			tbDOD.setTbDeliveryOrder(tbDO);
			
			//set Item
			tbDOD.setMsItem(tbPOD.getMsItem());
			
			//set PO Detail
			tbDOD.setTbPurchaseOrderDetail(tbPOD);
			
			//set qty arrived
			Listcell cell = (Listcell)item.getChildren().get(7);
			Intbox ibox = (Intbox)cell.getChildren().get(0);
			tbDOD.setNDoDetQty(ibox.getValue().shortValue());
			//set bonus arrived
			cell = (Listcell)item.getChildren().get(9);
			ibox = (Intbox)cell.getChildren().get(0);
			tbDOD.setNDoBonusQty(ibox.getValue().shortValue());
			
			Double subtotal = (Double)item.getAttribute(MedisafeConstants.PURCHASING_SESSION);
			tbDOD.setNSubtotal(subtotal);			
			
			//set log
			tbDOD.setVWhoCreate(super.getUserInfoBean().getStUserId());
			tbDOD.setDWhnCreate(new Date());
			
			set.add(tbDOD);
		}						
		
		PurchaseServiceLocator.getDOManager().update(tbDO);
		
		this.status.setValue(tbDO.getVDoStatus());
		
		Messagebox.show(MessagesService.getKey("pch.do.modify.success")+this.bppNo.getText());
		
		setAfterSaveButtons();
		
		//save DO to session
		Page pageBatch = (Page)cmp.getDesktop().getPage(ScreenConstant.DELIVERY_ORDER+"B");		
		super.getCurrentSession().setAttribute(MedisafeConstants.DO_SESSION, tbDO);
		DOBatchController batchCtr = new DOBatchController();
		batchCtr.init(pageBatch.getFellow("batchItem"));
	}

	public class DiscountListener implements EventListener{				
		
		double hargaSat = 0;
		double discount = 0;
		String discountType = "";
		Label result = null;
		Intbox qty = null;
		int qtyOrdered = 0;
		Listitem item = null;
		
		private DiscountListener(){};
		
		public DiscountListener(double hargaSat, double discount, String discountType,
				Label result, Intbox qty, Listitem item, int qtyOrdered){
			
			this.hargaSat = hargaSat;
			this.discount = discount;
			this.discountType = discountType;
			this.result = result;
			this.qty = qty;
			this.item = item;
			this.qtyOrdered = qtyOrdered;
		}

		public boolean isAsap() {
			// TODO Auto-generated method stub
			return true;
		}

		public void onEvent(Event arg0) {
			// TODO Auto-generated method stub			
							
			if (qty == null || result == null){
				try {
					Messagebox.show(MessagesService.getKey("pch.do.invalid.input"));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				qty.select();
				return;
			}
			
			if (qty.getValue() == null || qty.getValue() < 1)
				qty.setValue(new Integer(0));
			
			if (MedisafeConstants.RP.equals(discountType)){
								
				double disc = 0;
				if (qtyOrdered != 0)
					disc = discount / qtyOrdered * qty.intValue();
				
				double res = hargaSat * qty.intValue() - disc;				
				
				result.setValue(NumberUtil.format(res));
			
				item.setAttribute(MedisafeConstants.PURCHASING_SESSION, new Double(res));
			}else{
				double res = hargaSat * qty.intValue();
				
				double disVal = discount / 100 * res;
				
				res -= disVal;
				
				result.setValue(NumberUtil.format(res));
				
				item.setAttribute(MedisafeConstants.PURCHASING_SESSION, new Double(res));
			}
			
			doCalculateTotal();
			doCalculateGrandTotal();
		}		
	}
	
	class PPNDiscountListener implements EventListener{

		public boolean isAsap() {
			// TODO Auto-generated method stub
			return true;
		}

		public void onEvent(Event arg0) {
			// TODO Auto-generated method stub
			
			doCalculateGrandTotal();
		}		
	}
	
	public void doCalculateTotal(){
		
		Iterator it = this.list.getItems().iterator();
		double gTotal = 0;
		
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			
			Double total = (Double)item.getAttribute(MedisafeConstants.PURCHASING_SESSION);

			gTotal += total.doubleValue();
		}
		
		this.total.setValue(new BigDecimal(gTotal));
				
	}
	
	public void doCalculateGrandTotal(){		
		
		if (this.ppn.getValue() == null || this.discount.getValue() == null || this.ppnType.getSelectedItem() == null || this.discountType.getSelectedItem() == null)
			return;
		
		double priceAfterDiscount = 0;
		double grandTotal = 0;
		
		//set priceAfterDiscount
		if (MedisafeConstants.RP.equals(this.discountType.getSelectedItem().getValue())){
			priceAfterDiscount = this.total.getValue().doubleValue() - this.discount.getValue().doubleValue();
		}else{
			priceAfterDiscount = this.total.getValue().doubleValue() - (this.total.getValue().doubleValue() * this.discount.getValue().doubleValue() / 100);
		}
		
		if (MedisafeConstants.RP.equals(this.ppnType.getSelectedItem().getValue())){
			grandTotal = priceAfterDiscount + this.ppn.getValue().doubleValue();
		}else{
			grandTotal = priceAfterDiscount + (priceAfterDiscount * this.ppn.getValue().doubleValue() / 100);
		}
		
		this.gtotal.setValue(new BigDecimal(grandTotal));
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	private double returnPPN(){
		if (this.ppn.getValue() == null || this.discount.getValue() == null || this.ppnType.getSelectedItem() == null || this.discountType.getSelectedItem() == null)
			return 0;
		
		double priceAfterDiscount = 0;
		double grandTotal = 0;
		
		//set priceAfterDiscount
		if (MedisafeConstants.RP.equals(this.discountType.getSelectedItem().getValue())){
			priceAfterDiscount = this.total.getValue().doubleValue() - this.discount.getValue().doubleValue();
		}else{
			priceAfterDiscount = this.total.getValue().doubleValue() - (this.total.getValue().doubleValue() * this.discount.getValue().doubleValue() / 100);
		}
		
		if (MedisafeConstants.RP.equals(this.ppnType.getSelectedItem().getValue())){
			return this.ppn.getValue().doubleValue();
		}else{
			return (priceAfterDiscount * this.ppn.getValue().doubleValue() / 100);
		}		
	}
	
	private void setAfterSaveButtons(){
		
		this.location.setDisabled(true);
		this.poNo.setDisabled(true);
		this.recDate.setDisabled(true);
		this.bppNo.setDisabled(true);
		this.list.setDisabled(true);
		this.ppn.setDisabled(true);
		this.ppnType.setDisabled(true);
		this.btnSave.setDisabled(true);
		this.btnModify.setDisabled(false);
		this.btnCancel.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnRevoke.setDisabled(false);
		this.btnApprove.setDisabled(false);
		this.btnPrint.setDisabled(false);
	}
	
	private void setInitButton(){
		
		this.location.setDisabled(false);
		this.poNo.setDisabled(false);
		this.recDate.setDisabled(false);
		this.bppNo.setDisabled(false);
		this.list.setDisabled(false);
		this.ppn.setDisabled(false);
		this.ppnType.setDisabled(false);
		this.btnSave.setDisabled(false);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnRevoke.setDisabled(true);
		this.btnApprove.setDisabled(true);
		this.btnPrint.setDisabled(true);
	}
	
	private void setModifyButton(){
		
		this.location.setDisabled(false);
		this.poNo.setDisabled(true);
		this.recDate.setDisabled(false);
		this.bppNo.setDisabled(true);
		this.list.setDisabled(false);
		this.ppn.setDisabled(false);
		this.ppnType.setDisabled(false);
		this.btnSave.setDisabled(false);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(false);
		this.btnNew.setDisabled(true);
		this.btnRevoke.setDisabled(true);
		this.btnApprove.setDisabled(true);
		this.btnPrint.setDisabled(true);
	}
	
	private void setErrorButton(){
		
		this.location.setDisabled(true);
		this.poNo.setDisabled(true);
		this.recDate.setDisabled(true);
		this.bppNo.setDisabled(true);
		this.list.setDisabled(true);
		this.ppn.setDisabled(true);
		this.ppnType.setDisabled(true);
		this.btnSave.setDisabled(true);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnRevoke.setDisabled(true);
		this.btnApprove.setDisabled(true);
		this.btnPrint.setDisabled(true);
	}
	
	private void setSearchButton(){
		
		this.location.setDisabled(true);
		this.poNo.setDisabled(true);
		this.recDate.setDisabled(true);
		this.bppNo.setDisabled(false);
		this.list.setDisabled(true);
		this.ppn.setDisabled(true);
		this.ppnType.setDisabled(true);
		this.btnSave.setDisabled(true);
		this.btnModify.setDisabled(false);
		this.btnCancel.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnRevoke.setDisabled(false);
		this.btnApprove.setDisabled(false);
		this.btnPrint.setDisabled(true);
	}
	
	public void doApprove() throws InterruptedException, VONEAppException{
		
		if (!PurchaseServiceLocator.getDOManager().isValidInput(this))
			return;
		
		TbDeliveryOrder tbDO = PurchaseServiceLocator.getDOManager().getDObyCode(this.bppNo.getText());
		Listbox listBatch;
		
		if (tbDO == null){
			Messagebox.show(MessagesService.getKey("pch.do.bpp.error"));			
			return;
		}
		
		//check for batchItem.. should all item has not been registered then no further process will be executed!
		Page pageBatch = (Page)this.list.getDesktop().getPage(ScreenConstant.DELIVERY_ORDER+"B");		
		super.getCurrentSession().setAttribute(MedisafeConstants.DO_SESSION, tbDO);
		DOBatchController batchCtr = new DOBatchController();
		batchCtr.setTbDO(tbDO);
		batchCtr.init(pageBatch.getFellow("batchItem"));
		if (!batchCtr.isAllItemRegistered()){
			Messagebox.show(MessagesService.getKey("pch.do.batchitem.registration.incomplete"));
			
			return;
		}
		listBatch = batchCtr.getList();

		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("pch.do.approve.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		tbDO.setMsStaffByNApproverId(super.getUserInfoBean().getMsUser().getMsStaff());
		tbDO.setVWhoChange(super.getUserInfoBean().getStUserId());
		tbDO.setDWhnChange(new Date());
		
		PurchaseServiceLocator.getDOManager().executeApproval(tbDO, listBatch);
		
		setErrorButton();
		
		Messagebox.show(MessagesService.getKey("pch.do.approve.sukses"));
		
		this.listDo.getItems().clear();
		this.listOp.getItems().clear();
		
		//remove DO Session
		super.getCurrentSession().removeAttribute(MedisafeConstants.DO_SESSION);
		pageBatch = (Page)this.list.getDesktop().getPage(ScreenConstant.DELIVERY_ORDER+"B");		
		batchCtr = new DOBatchController();
		batchCtr.init(pageBatch.getFellow("batchItem"));
		
		//set status
		this.status.setValue(tbDO.getVDoStatus());
	}
		
	public void doRevoke() throws InterruptedException, VONEAppException {
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("pch.do.revoke.question"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		TbDeliveryOrder tbDO = PurchaseServiceLocator.getDOManager().getDObyCode(this.bppNo.getText());
		
		//check for BPP;
		if (tbDO == null){
			Messagebox.show(MessagesService.getKey("pch.do.bpp.error"));
			this.bppNo.select();
			return;
		}
		
		if (!MedisafeConstants.PURCHASING_OPEN.equals(tbDO.getVDoStatus())){
			Messagebox.show(MessagesService.getKey("pch.do.revoke.invalid.status"));			
			return;
		}
		
		PurchaseServiceLocator.getDOManager().delete(tbDO);
		
		this.status.setValue(MedisafeConstants.PURCHASING_REVOKED);		
		
		this.listDo.getItems().clear();
		
		setErrorButton();
		
		//remove DO Session
		super.getCurrentSession().removeAttribute(MedisafeConstants.DO_SESSION);
		Page pageBatch = (Page)this.list.getDesktop().getPage(ScreenConstant.DELIVERY_ORDER+"B");		
		DOBatchController batchCtr = new DOBatchController();
		batchCtr.init(pageBatch.getFellow("batchItem"));
		
		Messagebox.show(MessagesService.getKey("pch.do.revoke.success"));
	}
	
	private void doSelectDO() throws InterruptedException, VONEAppException {
				
		this.listOp.clearSelection();
		
		TbDeliveryOrder tbDO = (TbDeliveryOrder)this.listDo.getSelectedItem().getValue();
				
		//set location
		Iterator it = this.location.getItems().iterator();
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			
			MsWarehouse whouse = (MsWarehouse)item.getValue();
			if (whouse.getNWhouseId() == tbDO.getMsWarehouse().getNWhouseId()){
				this.location.setSelectedItem(item);
				break;
			}
		}
		
		//set status
		this.status.setValue(tbDO.getVDoStatus());
		
		redrawExistingDo();
		
		setSearchButton();
		
		this.listDo.clearSelection();
	}
	
	public void redrawExistingDo() throws InterruptedException, VONEAppException {
		
		TbDeliveryOrder doPojo = null;
		TbPurchaseOrder poPojo = null;
		
		doPojo = PurchaseServiceLocator.getDOManager().getDObyCode(this.bppNo.getText());
		if (doPojo == null){
			// TODO Auto-generated catch block			
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.poNo.select();	
			return;			
		}
		
		//save DO to session
		Page pageBatch = (Page)this.list.getDesktop().getPage(ScreenConstant.DELIVERY_ORDER+"B");		
		super.getCurrentSession().setAttribute(MedisafeConstants.DO_SESSION, doPojo);
		DOBatchController batchCtr = new DOBatchController();
		//REDRAW BATCH PAGE!!!
		batchCtr.init(pageBatch.getFellow("batchItem"));
		
		PurchaseServiceLocator.getDOManager().redrawExistingDO(this);

	}


	public Textbox getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Textbox approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Bandbox getBppNo() {
		return bppNo;
	}

	public void setBppNo(Bandbox bppNo) {
		this.bppNo = bppNo;
	}

	public Button getBtnApprove() {
		return btnApprove;
	}

	public void setBtnApprove(Button btnApprove) {
		this.btnApprove = btnApprove;
	}

	public Button getBtnNew() {
		return btnNew;
	}

	public void setBtnNew(Button btnNew) {
		this.btnNew = btnNew;
	}

	public Button getBtnPrint() {
		return btnPrint;
	}

	public void setBtnPrint(Button btnPrint) {
		this.btnPrint = btnPrint;
	}

	public Button getBtnRevoke() {
		return btnRevoke;
	}

	public void setBtnRevoke(Button btnRevoke) {
		this.btnRevoke = btnRevoke;
	}

	public ZulConstraint getCst() {
		return cst;
	}

	public void setCst(ZulConstraint cst) {
		this.cst = cst;
	}

	public Decimalbox getDiscount() {
		return discount;
	}

	public void setDiscount(Decimalbox discount) {
		this.discount = discount;
	}

	public Listbox getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Listbox discountType) {
		this.discountType = discountType;
	}

	public Textbox getDoNoSearch() {
		return doNoSearch;
	}

	public void setDoNoSearch(Textbox doNoSearch) {
		this.doNoSearch = doNoSearch;
	}

	public Decimalbox getGtotal() {
		return gtotal;
	}

	public void setGtotal(Decimalbox gtotal) {
		this.gtotal = gtotal;
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	public Listbox getListDo() {
		return listDo;
	}

	public void setListDo(Listbox listDo) {
		this.listDo = listDo;
	}

	public Listbox getListOp() {
		return listOp;
	}

	public void setListOp(Listbox listOp) {
		this.listOp = listOp;
	}

	public Listbox getLocation() {
		return location;
	}

	public void setLocation(Listbox location) {
		this.location = location;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Bandbox getPoNo() {
		return poNo;
	}

	public void setPoNo(Bandbox poNo) {
		this.poNo = poNo;
	}

	public Textbox getPoNoSearch() {
		return poNoSearch;
	}

	public void setPoNoSearch(Textbox poNoSearch) {
		this.poNoSearch = poNoSearch;
	}

	public Decimalbox getPpn() {
		return ppn;
	}

	public void setPpn(Decimalbox ppn) {
		this.ppn = ppn;
	}

	public Listbox getPpnType() {
		return ppnType;
	}

	public void setPpnType(Listbox ppnType) {
		this.ppnType = ppnType;
	}

	public Textbox getRecBy() {
		return recBy;
	}

	public void setRecBy(Textbox recBy) {
		this.recBy = recBy;
	}

	public Datebox getRecDate() {
		return recDate;
	}

	public void setRecDate(Datebox recDate) {
		this.recDate = recDate;
	}

	public Label getStatus() {
		return status;
	}

	public void setStatus(Label status) {
		this.status = status;
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

	public Textbox getSupNameSearch() {
		return supNameSearch;
	}

	public void setSupNameSearch(Textbox supNameSearch) {
		this.supNameSearch = supNameSearch;
	}

	public Textbox getSupTelp() {
		return supTelp;
	}

	public void setSupTelp(Textbox supTelp) {
		this.supTelp = supTelp;
	}

	public Decimalbox getTotal() {
		return total;
	}

	public void setTotal(Decimalbox total) {
		this.total = total;
	}

	public Textbox getWhouseSearch() {
		return whouseSearch;
	}

	public void setWhouseSearch(Textbox whouseSearch) {
		this.whouseSearch = whouseSearch;
	}
}

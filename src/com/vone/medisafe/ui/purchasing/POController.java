package com.vone.medisafe.ui.purchasing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
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
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.IdsManager;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.PurchaseServiceLocator;
import com.vone.medisafe.ui.common.CommonGlobalDiscountListener;
import com.vone.medisafe.ui.common.CommonSimpleDiscountListener;

public class POController extends PurchaseController {

	Logger logger = Logger.getLogger(POController.class);

	ZulConstraint cst = new ZulConstraint();

	Listbox location = null;

	Bandbox poNo = null;

	Textbox issuedBy = null;

	Textbox approvedBy = null;

	Bandbox oppNo = null;

	Textbox searchOpp = null;
	
	Textbox poNoSearch = null;
	
	Textbox supNameSearch = null;

	Listbox listOp = null;
	
	Listbox listOpp = null;	

	Bandbox supplierCode = null;

	Textbox searchSupCode = null;

	Textbox searchSupName = null;

	Listbox listSupplier = null;

	Textbox supName = null;

	Textbox supAddress = null;

	Textbox supTelp = null;

	Label status = null;

	Listbox list = null;

	Listbox discountType = null;

	Session session = null;

	// temporary list of list object
	// if supplier is emptied then, listTemp will be shown.
	// purpose : no hit to database everytime changing supplier
	Listbox listTemp = null;

	Button btnDelete = null;

	//Button btnAdd = null;

	Button btnCalculate = null;

	Decimalbox subTotal = null;

	Decimalbox discount = null;

	public Decimalbox total = null;

	Button btnNew = null;

	Button btnRevoke = null;

	public Button btnPrint = null;

	// shall be kept null at initial!!!
	// purpose : get Measurement List once.
	List listMeasurement = null;

	// Payment Due Date
	Datebox dueDate = null;

	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);

		this.location = (Listbox) cmp.getFellow("location");
		this.poNo = (Bandbox) cmp.getFellow("poNo");
		this.issuedBy = (Textbox) cmp.getFellow("issuedBy");
		this.approvedBy = (Textbox) cmp.getFellow("approvedBy");
		this.oppNo = (Bandbox) cmp.getFellow("oppNo");
		this.searchOpp = (Textbox) cmp.getFellow("searchOpp");
		this.listOp = (Listbox) cmp.getFellow("listOp");
		this.listOpp = (Listbox) cmp.getFellow("listOpp");
		this.supplierCode = (Bandbox) cmp.getFellow("supCode");
		this.listSupplier = (Listbox) cmp.getFellow("listSupplier");
		this.supName = (Textbox) cmp.getFellow("supName");
		this.searchSupCode = (Textbox) cmp.getFellow("searchSupCode");
		this.searchSupName = (Textbox) cmp.getFellow("searchSupName");
		this.supAddress = (Textbox) cmp.getFellow("supAddress");
		this.poNoSearch = (Textbox) cmp.getFellow("poNoSearch");
		this.supNameSearch = (Textbox) cmp.getFellow("supNameSearch");
		this.supTelp = (Textbox) cmp.getFellow("supTelp");
		this.status = (Label) cmp.getFellow("status");
		this.list = (Listbox) cmp.getFellow("list");
		this.dueDate = (Datebox) cmp.getFellow("dueDate");

		// set common list
		setList(this.list);

		this.listTemp = new Listbox();
		this.btnDelete = (Button) cmp.getFellow("btnDelete");
//		this.btnAdd = (Button) cmp.getFellow("btnAdd");
		this.btnCalculate = (Button) cmp.getFellow("btnCalculate");
		this.subTotal = (Decimalbox) cmp.getFellow("subTotal");
		this.discount = (Decimalbox) cmp.getFellow("discount");
		this.discountType = (Listbox) cmp.getFellow("discountType");
		this.total = (Decimalbox) cmp.getFellow("total");
		this.btnNew = (Button) cmp.getFellow("btnNew");
		this.btnRevoke = (Button) cmp.getFellow("btnRevoke");
		this.btnPrint = (Button) cmp.getFellow("btnPrint");

		this.subTotal.setValue(new BigDecimal(0));
		this.subTotal.setDisabled(true);
		this.discount.setValue(new BigDecimal(0));
		MedisafeUtil.setListPaymentOptions(discountType);
		discountType.setSelectedIndex(0);
		this.total.setDisabled(true);
		this.total.setValue(new BigDecimal(0));

		this.subTotal.addEventListener("onChange",
				new CommonSimpleDiscountListener(this.subTotal, this.discount,
						this.discountType, this.total));
		this.discount.addEventListener("onChange",
				new CommonSimpleDiscountListener(this.subTotal, this.discount,
						this.discountType, this.total));
		this.discountType.addEventListener("onSelect",
				new CommonSimpleDiscountListener(this.subTotal, this.discount,
						this.discountType, this.total));

		this.dueDate.setValue(new Date());

		cst.registerComponent(dueDate, ZulConstraint.NO_EMPTY);
		cst.registerComponent(searchOpp, ZulConstraint.UPPER_CASE);
		cst.registerComponent(searchSupCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(searchSupName, ZulConstraint.UPPER_CASE);
		cst.registerComponent(oppNo, ZulConstraint.UPPER_CASE);
		cst.registerComponent(supAddress, ZulConstraint.UPPER_CASE);
		
		cst.registerComponent(poNoSearch, ZulConstraint.UPPER_CASE);
		cst.registerComponent(supNameSearch, ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);

		// fill location
		List listLoc = super.getUserInfoBean().getMsUnitByScreenCode(
				ScreenConstant.ORDER_PEMBELIAN);

		Iterator it = listLoc.iterator();

		location.getItems().clear();

		while (it.hasNext()) {
			MsUnit unitPojo = (MsUnit) it.next();

			Listitem item = new Listitem();
			item.setParent(location);

			item.appendChild(new Listcell(unitPojo.getVUnitCode() + " - "
					+ unitPojo.getVUnitName()));
			item.setValue(unitPojo);
		}
		if (location.getItemCount() > 0)
			location.setSelectedIndex(0);
		// end filling location

		oppNo.addEventListener("onChange", new EventListener() {

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					listOpp.clearSelection();
					redraw();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR", e);
				}
			}

		});
		listOpp.addEventListener("onSelect", new EventListener() {

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
					logger.error("REDRAW ERROR", e);
				}
			}

		});
		
		poNo.addEventListener("onChange", new EventListener() {

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					listOp.clearSelection();
					redrawPO();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR", e);
				}
			}

		});
		listOp.addEventListener("onSelect", new EventListener() {

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redrawPO();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR", e);
				}
			}

		});		

		supplierCode.addEventListener("onChange", new EventListener() {

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					listSupplier.clearSelection();
					doFilterSup();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR", e);
				}
			}

		});
		listSupplier.addEventListener("onSelect", new EventListener() {

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					doFilterSup();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR", e);
				}
			}

		});

		this.issuedBy.setDisabled(true);
		this.approvedBy.setDisabled(true);

		// fill session

		session = Sessions.getCurrent();
		session.setAttribute(MedisafeConstants.PURCHASING_SESSION, this);

		this.oppNo.focus();

		setInitButton();
	}

	public void doCancel(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);

		setAfterSaveButton();
	}

	public void doClose(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub

		if (this.list.getSelectedCount() >= 1)
			this.list.removeChild(this.list.getSelectedItem());
	}

	private String generateSQ() throws VONEAppException {

		IdsManager idsManager = IdsServiceLocator.getIdsManager();
		Integer seq = idsManager
				.getSequence(MedisafeConstants.PURCHASE_ORDER_CODE);

		Date date = new Date();
		StringBuffer stSQ = new StringBuffer();
		stSQ.append("OP-")
				.append(
						((MsUnit) location.getSelectedItem().getValue())
								.getVUnitCode()).append("-").append(
						MedisafeUtil.convertDateToString(date, "yyMM")).append(
						"" + seq.intValue());

		return stSQ.toString();
	}

	public void doRevoke() throws InterruptedException, VONEAppException {
		if (!StringUtils.hasValue(this.poNo.getText())) {
			Messagebox.show(MessagesService.getKey("pch.order.no.op.notfound"));
			return;
		}

		TbPurchaseOrder pojo = PurchaseServiceLocator.getPOManager()
				.getPOByCode(this.poNo.getText());

		if (pojo == null) {
			Messagebox.show(MessagesService.getKey("pch.order.no.op.notfound"));
			setErrorButton();
			return;
		}

		if (!pojo.getVPoStatus().equals(MedisafeConstants.PURCHASING_OPEN)) {
			Messagebox.show(MessagesService
					.getKey("pch.order.modify.status.invalid"));
			this.status.setValue(pojo.getVPoStatus());
			setErrorButton();
			return;
		}

		int respond = 0;

		respond = Messagebox.show(MessagesService
				.getKey("pch.order.revoke.question"), "Question",
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);

		if (respond != Messagebox.YES)
			return;

		pojo.setVPoStatus(MedisafeConstants.PURCHASING_REVOKED);
		PurchaseServiceLocator.getPOManager().updateHeaderOnly(pojo);

		setErrorButton();

		this.status.setValue(pojo.getVPoStatus());

		Messagebox.show(MessagesService.getKey("pch.order.revoke.success"));

	}

	@Override
	public void doModify(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);

		setModifyButton();
	}

	public void doSave(Component cmp) throws InterruptedException,
			VONEAppException {

		if (!cst.validateComponent(true))
			return;
		
		if(!isValidQty()){
			Messagebox.show("OP tidak dapat disimpan, nilai order lebih besar dari yang telah diapproved...!");
			return;
		}

		Component cmpResult = isValidInput();

		if (cmpResult != null) {
			Messagebox.show(MessagesService.getKey("pch.order.invalid.input"));

			if (cmpResult instanceof Decimalbox)
				((Decimalbox) cmpResult).select();
			if (cmpResult instanceof Intbox)
				((Intbox) cmpResult).select();
			if (cmpResult instanceof Listitem)
				((Listitem) cmpResult).setSelected(true);
		} else {
			doCalculate();
			super.doSave(cmp);
		}

	}

	public void doClosePOR(Component cmp) throws InterruptedException,
			VONEAppException {

		if (!StringUtils.hasValue(this.oppNo.getText())) {
			Messagebox.show(MessagesService.getKey("pch.order.request.no.opp"));
			return;
		}

		TbPurchaseRequest pojo = PurchaseServiceLocator.getPORManager()
				.getTbPurchaseRequestByCode(this.oppNo.getText());

		if (pojo == null) {
			Messagebox.show(MessagesService.getKey("pch.order.request.no.opp"));
			setErrorButton();
			return;
		}

		int respond = 0;

		respond = Messagebox.show(
				MessagesService.getKey("pch.order.close.por"), "Question",
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);

		if (respond != Messagebox.YES)
			return;

		pojo.setVPrStatus(MedisafeConstants.PURCHASING_CLOSED);

		PurchaseServiceLocator.getPORManager().updatePojoOnly(pojo);

		Messagebox.show(MessagesService.getKey("pch.order.close.success"));

		this.listOpp.getItems().clear();
		this.oppNo.setText("");
		setErrorButton();
	}

	public void doSaveAdd(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);

		TbPurchaseOrder tbPO = new TbPurchaseOrder();
		tbPO.setMsStaffByNIssuerId(super.getUserInfoBean().getMsUser()
				.getMsStaff());

		MsVendor msVendor = MasterServiceLocator.getVendorManager()
				.getVendorByCode(this.supplierCode.getText());
		if (msVendor == null) {
			Messagebox.show(MessagesService
					.getKey("pch.order.supplier.invalid"));
			this.supplierCode.select();
			return;
		}
		tbPO.setMsVendor(msVendor);

		// dueDate
		tbPO.setDPaymentDue(this.dueDate.getValue());

		TbPurchaseRequest tbPR = PurchaseServiceLocator.getPORManager()
				.getTbPurchaseRequestByCode(this.oppNo.getText());
		if (tbPR == null) {
			Messagebox.show(MessagesService.getKey("pch.order.por.invalid"));
			this.oppNo.select();
			return;
		}
		tbPO.setTbPurchaseRequest(tbPR);

		// set Subtotal, discount, and GrandTotal
		tbPO.setNSubtotal(this.subTotal.getValue().doubleValue());
		tbPO.setNDiscount(this.discount.getValue().doubleValue());
		tbPO.setVDiscountType(this.discountType.getSelectedItem().getValue()
				.toString());
		tbPO.setNTotal(this.total.getValue().doubleValue());

		tbPO.setVWhoCreate(super.getUserInfoBean().getStUserId());
		tbPO.setDWhnCreate(new Date());

		tbPO.setVPoStatus(MedisafeConstants.PURCHASING_OPEN);

		Iterator it = list.getItems().iterator();

		Set set = new HashSet();
		tbPO.setTbPurchaseOrderDetails(set);

		while (it.hasNext()) {
			Listitem item = (Listitem) it.next();
			MsItem msItem = (MsItem) item.getValue();

			TbPurchaseOrderDetail tbPOD = new TbPurchaseOrderDetail();
			set.add(tbPOD);
			tbPOD.setTbPurchaseOrder(tbPO);

			tbPOD.setMsItem(msItem);

			// Harga Satuan
			Listcell cell = (Listcell) item.getChildren().get(5);
			Decimalbox qtyHS = (Decimalbox) cell.getChildren().get(0);
			tbPOD.setNPoDetCost(qtyHS.getValue().doubleValue());

			// Jumlah Order
			cell = (Listcell) item.getChildren().get(6);
			Intbox qtyJO = (Intbox) cell.getChildren().get(0);
			tbPOD.setNPoDetQtyOrdered(qtyJO.getValue().shortValue());
			tbPOD.setNPoDetQtyReceived(new Integer(0).shortValue());
			tbPOD.setNPoDetQtyRemark(new Integer(0).shortValue());

			// measurement
			cell = (Listcell) item.getChildren().get(7);
			Listbox itemMList = (Listbox) cell.getChildren().get(0);		
			
			tbPOD.setMsItemMeasurement((MsItemMeasurement) itemMList
					.getSelectedItem().getValue());

			// bonus
			cell = (Listcell) item.getChildren().get(8);
			Intbox qtyB = (Intbox) cell.getChildren().get(0);
			tbPOD.setNBonus(qtyB.getValue().shortValue());

			// Diskon
			cell = (Listcell) item.getChildren().get(9);
			Decimalbox qtyD = (Decimalbox) cell.getChildren().get(0);
			tbPOD.setNDiscount(qtyD.getValue().doubleValue());

			// Diskon Type
			cell = (Listcell) item.getChildren().get(9);
			Listbox typeD = (Listbox) cell.getChildren().get(1);
			tbPOD.setVDiscountType(typeD.getSelectedItem().getValue()
					.toString());

			tbPOD.setVWhoCreate(super.getUserInfoBean().getStUserId());
			tbPOD.setDWhnCreate(new Date());

			// Subtotal
			if (item.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE) != null) {
				Object obj = item
						.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE);

				BigDecimal bigD = CommonGlobalDiscountListener
						.getFinalAmount(obj);
				tbPOD.setNSubtotal(bigD.doubleValue());
			}
		}

		String stPOCode = generateSQ();

		tbPO.setVPoCode(stPOCode);

		PurchaseServiceLocator.getPOManager().save(tbPO);

		Messagebox.show(MessagesService.getKey("pch.order.add.success")
				+ stPOCode);

		this.issuedBy.setText(super.getUserInfoBean().getStUserName());

		this.poNo.setText(stPOCode);

		this.status.setValue(tbPO.getVPoStatus());

		setAfterSaveButton();
	}

	public void doSaveModify(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);

		// getPO
		TbPurchaseOrder tbPO = PurchaseServiceLocator.getPOManager()
				.getPOByCode(this.poNo.getText());
		if (tbPO == null) {
			Messagebox.show(MessagesService.getKey("internal.error"));
			setErrorButton();
			return;
		}

		tbPO.setVWhoChange(super.getUserInfoBean().getStUserId());
		tbPO.setDWhnChange(new Date());

		// dueDate
		tbPO.setDPaymentDue(this.dueDate.getValue());

		// set Subtotal, discount, and GrandTotal
		tbPO.setNSubtotal(this.subTotal.getValue().doubleValue());
		tbPO.setNDiscount(this.discount.getValue().doubleValue());
		tbPO.setVDiscountType(this.discountType.getSelectedItem().getValue()
				.toString());
		tbPO.setNTotal(this.total.getValue().doubleValue());

		Iterator it = list.getItems().iterator();

		Set<TbPurchaseOrderDetail> set = new HashSet<TbPurchaseOrderDetail>();
		tbPO.setTbPurchaseOrderDetails(set);

		while (it.hasNext()) {
			Listitem item = (Listitem) it.next();
			MsItem msItem = (MsItem) item.getValue();

			TbPurchaseOrderDetail tbPOD = new TbPurchaseOrderDetail();
			set.add(tbPOD);
			tbPOD.setTbPurchaseOrder(tbPO);

			tbPOD.setMsItem(msItem);

			// Harga Satuan
			Listcell cell = (Listcell) item.getChildren().get(5);
			Decimalbox qtyHS = (Decimalbox) cell.getChildren().get(0);
			tbPOD.setNPoDetCost(qtyHS.getValue().doubleValue());

			// Jumlah Order
			cell = (Listcell) item.getChildren().get(6);
			Intbox qtyJO = (Intbox) cell.getChildren().get(0);
			tbPOD.setNPoDetQtyOrdered(qtyJO.getValue().shortValue());
			tbPOD.setNPoDetQtyReceived(new Integer(0).shortValue());
			tbPOD.setNPoDetQtyRemark(new Integer(0).shortValue());

			// measurement
			cell = (Listcell) item.getChildren().get(7);
			Listbox itemMList = (Listbox) cell.getChildren().get(0);
			tbPOD.setMsItemMeasurement((MsItemMeasurement) itemMList
					.getSelectedItem().getValue());

			// bonus
			cell = (Listcell) item.getChildren().get(8);
			Intbox qtyB = (Intbox) cell.getChildren().get(0);
			tbPOD.setNBonus(qtyB.getValue().shortValue());

			// Diskon
			cell = (Listcell) item.getChildren().get(9);
			Decimalbox qtyD = (Decimalbox) cell.getChildren().get(0);
			tbPOD.setNDiscount(qtyD.getValue().doubleValue());

			// Diskon Type
			cell = (Listcell) item.getChildren().get(9);
			Listbox typeD = (Listbox) cell.getChildren().get(1);
			tbPOD.setVDiscountType(typeD.getSelectedItem().getValue()
					.toString());

			tbPOD.setVWhoCreate(super.getUserInfoBean().getStUserId());
			tbPOD.setDWhnCreate(new Date());

			// Subtotal
			if (item.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE) != null) {
				Object obj = item
						.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE);

				BigDecimal bigD = CommonGlobalDiscountListener
						.getFinalAmount(obj);
				tbPOD.setNSubtotal(bigD.doubleValue());
			}
		}

		PurchaseServiceLocator.getPOManager().update(tbPO);

		Messagebox.show(MessagesService.getKey("common.modify.success"));

		setAfterSaveButton();
	}

	public void redrawHeader() throws InterruptedException, VONEAppException {

		this.listSupplier.setSelectedIndex(0);
		this.supName.setText("");
		this.supAddress.setText("");
		this.supTelp.setText("");

		this.issuedBy.setText("");
		this.approvedBy.setText("");
	}

	public void redraw() throws InterruptedException, VONEAppException {
		TbPurchaseRequest porPojo = null;
		
		try {
			if (listOpp.getSelectedItem() == null) {
				porPojo = PurchaseServiceLocator.getPORManager().getTbPurchaseRequestByCode(oppNo.getText());
				if (porPojo == null)
					throw new NullPointerException();
			} else
				porPojo = (TbPurchaseRequest) listOpp.getSelectedItem()
						.getValue();

		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			oppNo.select();
			return;
		}
		
		PurchaseServiceLocator.getPOManager().redraw(this, porPojo, this.list);

		listTemp = (Listbox) list.clone();
		
		MsVendor supplier = MasterServiceLocator.getVendorManager().getVendorById(porPojo.getSupplierId());
		
		this.supplierCode.setText(supplier.getVVendorCode());
		this.supAddress.setText(supplier.getVVendorAddress());
		this.supName.setText(supplier.getVVendorName());
		this.supTelp.setText(supplier.getVVendorContactNo());
//		this.listSupplier.clearSelection();
	}
	
	public void redrawPO() throws InterruptedException, VONEAppException {
		
		TbPurchaseOrder tbPO = null;
		
		try {
			if (listOp.getSelectedItem() == null) {
				tbPO= PurchaseServiceLocator.getPOManager().getPOByCode(poNo.getText());
				if (tbPO == null)
					throw new NullPointerException();
			} else
				tbPO = (TbPurchaseOrder) listOp.getSelectedItem()
						.getValue();

		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			poNo.select();
			return;
		}
				
		PurchaseServiceLocator.getPOManager().redrawPO(this, tbPO, this.list);
		
		setErrorButton();
		this.btnPrint.setDisabled(false);
		this.btnRevoke.setDisabled(false);
	}

	public void doSearch(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		this.listOpp.getItems().clear();
		
		PurchaseServiceLocator.getPOManager().doSearchPOR(cmp);
	}

	public void doSearchSup(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		this.listSupplier.getItems().clear();

		PurchaseServiceLocator.getPOManager().doSearchSupPOController(cmp);
	}

	public void doFilterSup() throws InterruptedException, VONEAppException {
		/**
		if (listTemp == null || list.getItemCount() < 1 || !StringUtils.hasValue(this.oppNo.getText()))
			Messagebox.show("masuk sini");
			return;
		
		if (!StringUtils.hasValue(this.supplierCode.getText())) {
//			doRetrieveOriginalList();
			this.supAddress.setText("");
			this.supName.setText("");
			this.supTelp.setText("");
			return;
		} */

		MsVendor supPojo = null;

		try {
			if (listSupplier.getSelectedItem() == null) {
				supPojo = MasterServiceLocator.getVendorManager()
						.getVendorByCode(this.supplierCode.getText());
				if (supPojo == null)
					throw new NullPointerException();
			} else
				supPojo = (MsVendor) listSupplier.getSelectedItem().getValue();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.supplierCode.select();
			return;
		}
		
//		doRetrieveOriginalList();
		
		PurchaseServiceLocator.getPOManager().doFilterSup(this, supPojo, this.listTemp, this.list);
		
	}

	public void doRetrieveOriginalList() {

		Component parent = list.getParent();

		parent.removeChild(list);

		list = (Listbox) listTemp.clone();

		setList(list);

		list.setParent(parent);

		// set EventListener

		Iterator it = list.getItems().iterator();

		while (it.hasNext()) {
			Listitem item = (Listitem) it.next();

			List listChildren = item.getChildren();

			Listcell listcellHS = (Listcell) listChildren.get(5);
			Listcell listcellQty = (Listcell) listChildren.get(6);
			Listcell listcellDisc = (Listcell) listChildren.get(9);
			Listcell listcellTotal = (Listcell) listChildren.get(10);

			Decimalbox dBoxHS = (Decimalbox) listcellHS.getChildren().get(0);
			Intbox qtyOrder = (Intbox) listcellQty.getChildren().get(0);
			Decimalbox dBoxD = (Decimalbox) listcellDisc.getChildren().get(0);
			Listbox boxType = (Listbox) listcellDisc.getChildren().get(1);
			Label lblTotal = (Label) listcellTotal.getChildren().get(0);

			boxType.addEventListener("onSelect",
					new CommonGlobalDiscountListener(item, boxType, dBoxHS,
							qtyOrder, dBoxD, lblTotal));

			item.addEventListener("onSelect", new CommonGlobalDiscountListener(
					item, boxType, dBoxHS, qtyOrder, dBoxD, lblTotal));

			dBoxHS.addEventListener("onChange",
					new CommonGlobalDiscountListener(item, boxType, dBoxHS,
							qtyOrder, dBoxD, lblTotal));

			qtyOrder.addEventListener("onChange",
					new CommonGlobalDiscountListener(item, boxType, dBoxHS,
							qtyOrder, dBoxD, lblTotal));

			dBoxD.addEventListener("onChange",
					new CommonGlobalDiscountListener(item, boxType, dBoxHS,
							qtyOrder, dBoxD, lblTotal));
		}

	}

	public void setListMeasurement() throws VONEAppException {
		if (this.listMeasurement != null)
			return;

		this.listMeasurement = MasterServiceLocator.getItemMeasurementManager()
				.getMeasurementType();

		if (this.listMeasurement == null)
			this.listMeasurement = new ArrayList();
	}

	public void setListboxMeasurement(Listbox listM) throws VONEAppException {
		if (listM == null)
			listM = new Listbox();

		if (this.listMeasurement == null)
			setListMeasurement();

		Iterator it = this.listMeasurement.iterator();

		while (it.hasNext()) {
			Listitem item = new Listitem();
			item.setParent(listM);

			String value = (String) it.next();

			item.appendChild(new Listcell(value));
			item.setValue(MasterServiceLocator.getItemMeasurementManager()
					.getMsItemMeasurementByCode(value));
		}

		listM.setSelectedIndex(0);
	}
	
	public void selectListboxMeasurement(Listbox listM, String value) {
		if (listM == null || !StringUtils.hasValue(value))
			return;
		
		Iterator it = listM.getItems().iterator();
		
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			
			MsItemMeasurement msItemM = (MsItemMeasurement)item.getValue();
			
			if (value.equals(msItemM.getVMitemEndQuantify())){
				listM.setSelectedItem(item);
				return;
			}
		}
	}

	public void doCalculate() throws VONEAppException {

		BigDecimal bigD = new BigDecimal(0);

		Iterator it = this.list.getItems().iterator();

		while (it.hasNext()) {
			Listitem item = (Listitem) it.next();

			if (item.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE) != null) {
				Object obj = item
						.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE);

				bigD = bigD.add(CommonGlobalDiscountListener
						.getFinalAmount(obj));
			}
		}

		this.subTotal.setValue(bigD);

		CommonSimpleDiscountListener.manualCalculate(this.subTotal,
				this.discount, this.discountType, this.total);
	}
	
	private boolean isValidQty(){
		boolean isValid = true;
		Iterator it = list.getItems().iterator();
		while(it.hasNext()){
			Listitem item = (Listitem) it.next();
			
			// Jumlah Order
			Listcell cell = (Listcell) item.getChildren().get(6);
			Intbox qty = (Intbox) cell.getChildren().get(0);
			
			cell = (Listcell)item.getChildren().get(2);
			Integer approved = new Integer(cell.getLabel());
			
			if(qty.intValue() > approved.intValue()) isValid = false;
		}
		return isValid;
	}

	private Component isValidInput() {

		Iterator it = list.getItems().iterator();

		while (it.hasNext()) {
			Listitem item = (Listitem) it.next();

			// Harga Satuan
			Listcell cell = (Listcell) item.getChildren().get(5);
			Decimalbox qtyD = (Decimalbox) cell.getChildren().get(0);

			if (qtyD.getValue() == null || qtyD.getValue().intValue() < 1
					|| qtyD.getValue().doubleValue() > Double.MAX_VALUE) {
				return qtyD;
			}

			// bonus
			cell = (Listcell) item.getChildren().get(8);
			Intbox qtyBonus = (Intbox) cell.getChildren().get(0);

			if (qtyBonus.getValue() == null
					|| qtyBonus.getValue().shortValue() > Short.MAX_VALUE) {
				return qtyBonus;
			}

			// Jumlah Order
			cell = (Listcell) item.getChildren().get(6);
			Intbox qty = (Intbox) cell.getChildren().get(0);

			if (qty.getValue() == null
					|| qty.getValue().intValue()
							+ qtyBonus.getValue().intValue() < 1
					|| qty.getValue().shortValue() > Short.MAX_VALUE) {
				return qty;
			}

			// Diskon
			cell = (Listcell) item.getChildren().get(9);
			qtyD = (Decimalbox) cell.getChildren().get(0);

			if (qtyD.getValue() == null
					|| qtyD.getValue().doubleValue() > Double.MAX_VALUE) {
				return qtyD;
			}

			// Subtotal
			if (item.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE) != null) {
				Object obj = item
						.getAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE);

				BigDecimal bigD = CommonGlobalDiscountListener
						.getFinalAmount(obj);
				if (bigD.doubleValue() <= 0) {
					return item;
				}
			}

		}

		if (this.discount.getValue() == null
				|| this.discount.getValue().doubleValue() < 0)
			return this.discount;

		return null;
	}

	public void doNew() {

		setInitButton();

		listTemp = null;
		poNo.setText("");
		oppNo.setText("");
		listOpp.clearSelection();
		supplierCode.setText("");
		listSupplier.clearSelection();
		list.getItems().clear();
		this.subTotal.setText("");
		this.discount.setValue(new BigDecimal(0));
		this.total.setText("");
		this.issuedBy.setText("");
		this.approvedBy.setText("");
		this.dueDate.setValue(new Date());
	}

	public void setInitButton() {

		//this.btnAdd.setDisabled(false);
		this.btnNew.setDisabled(false);
		this.btnDelete.setDisabled(false);
		this.btnRevoke.setDisabled(true);
		this.btnSave.setDisabled(false);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(true);
		this.btnCalculate.setDisabled(false);
		this.discount.setDisabled(false);
		this.discountType.setDisabled(false);
		this.dueDate.setDisabled(false);
		this.list.setDisabled(false);
		this.oppNo.setDisabled(false);
		this.poNo.setDisabled(false);
		this.supplierCode.setDisabled(false);
		this.location.setDisabled(false);
		this.status.setValue("");
		this.supName.setText("");
		this.supAddress.setText("");
		this.supTelp.setText("");
		this.btnPrint.setDisabled(true);
	}

	public void setAfterSaveButton() {

		//this.btnAdd.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnDelete.setDisabled(true);
		this.btnRevoke.setDisabled(false);
		this.btnSave.setDisabled(true);
		this.btnCancel.setDisabled(true);
		this.btnModify.setDisabled(false);
		this.btnCalculate.setDisabled(true);
		this.discount.setDisabled(true);
		this.discountType.setDisabled(true);
		this.list.setDisabled(true);
		this.oppNo.setDisabled(true);
		this.poNo.setDisabled(true);
		this.supplierCode.setDisabled(true);
		this.location.setDisabled(true);
		this.btnPrint.setDisabled(true);
		this.dueDate.setDisabled(true);
	}

	public void setModifyButton() {
//		this.btnAdd.setDisabled(false);
		this.btnNew.setDisabled(true);
		this.btnCalculate.setDisabled(false);
		this.btnDelete.setDisabled(false);
		this.btnRevoke.setDisabled(true);
		this.btnCancel.setDisabled(false);
		this.btnSave.setDisabled(false);
		this.btnModify.setDisabled(true);
		this.btnPrint.setDisabled(true);
		this.list.setDisabled(false);
		this.oppNo.setDisabled(true);
		this.poNo.setDisabled(true);
		this.supplierCode.setDisabled(true);
		this.location.setDisabled(true);
		this.btnPrint.setDisabled(true);
		this.discount.setDisabled(false);
		this.discountType.setDisabled(false);
		this.dueDate.setDisabled(false);
	}

	public void setErrorButton() {
//		this.btnAdd.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnDelete.setDisabled(true);
		this.btnRevoke.setDisabled(true);
		this.btnSave.setDisabled(true);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(true);
		this.btnCalculate.setDisabled(true);
		this.discount.setDisabled(true);
		this.discountType.setDisabled(true);
		this.list.setDisabled(true);
		this.oppNo.setDisabled(true);
		this.poNo.setDisabled(true);
		this.supplierCode.setDisabled(true);
		this.location.setDisabled(true);
		this.btnPrint.setDisabled(true);
		this.dueDate.setDisabled(true);
	}

	@Override
	public void addItem(MsItem itemPojo) throws VONEAppException {
		// TODO Auto-generated method stub
		super.addItem(itemPojo);

		PurchaseServiceLocator.getPOManager().addItem(this, itemPojo);
	}
	
	public void doSearchPO(Component cmp) throws InterruptedException,VONEAppException {
		
//		if (!StringUtils.hasValue(this.poNoSearch.getText()) &&
//				!StringUtils.hasValue(this.supNameSearch.getText())){
//			
//			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
//			
//			return;
//		}
//						
		PurchaseServiceLocator.getPOManager().doSearchPO(cmp);
	}

	public Textbox getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Textbox approvedBy) {
		this.approvedBy = approvedBy;
	}

//	public Button getBtnAdd() {
//		return btnAdd;
//	}
//
//	public void setBtnAdd(Button btnAdd) {
//		this.btnAdd = btnAdd;
//	}

	public Button getBtnCalculate() {
		return btnCalculate;
	}

	public void setBtnCalculate(Button btnCalculate) {
		this.btnCalculate = btnCalculate;
	}

	public Button getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(Button btnDelete) {
		this.btnDelete = btnDelete;
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

	public Datebox getDueDate() {
		return dueDate;
	}

	public void setDueDate(Datebox dueDate) {
		this.dueDate = dueDate;
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

	public List getListMeasurement() {
		return listMeasurement;
	}

	public void setListMeasurement(List listMeasurement) {
		this.listMeasurement = listMeasurement;
	}

	public Listbox getListOp() {
		return listOp;
	}

	public void setListOp(Listbox listOp) {
		this.listOp = listOp;
	}

	public Listbox getListOpp() {
		return listOpp;
	}

	public void setListOpp(Listbox listOpp) {
		this.listOpp = listOpp;
	}

	public Listbox getListSupplier() {
		return listSupplier;
	}

	public void setListSupplier(Listbox listSupplier) {
		this.listSupplier = listSupplier;
	}

	public Listbox getListTemp() {
		return listTemp;
	}

	public void setListTemp(Listbox listTemp) {
		this.listTemp = listTemp;
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

	public Bandbox getOppNo() {
		return oppNo;
	}

	public void setOppNo(Bandbox oppNo) {
		this.oppNo = oppNo;
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

	public Textbox getSearchOpp() {
		return searchOpp;
	}

	public void setSearchOpp(Textbox searchOpp) {
		this.searchOpp = searchOpp;
	}

	public Textbox getSearchSupCode() {
		return searchSupCode;
	}

	public void setSearchSupCode(Textbox searchSupCode) {
		this.searchSupCode = searchSupCode;
	}

	public Textbox getSearchSupName() {
		return searchSupName;
	}

	public void setSearchSupName(Textbox searchSupName) {
		this.searchSupName = searchSupName;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Label getStatus() {
		return status;
	}

	public void setStatus(Label status) {
		this.status = status;
	}

	public Decimalbox getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Decimalbox subTotal) {
		this.subTotal = subTotal;
	}

	public Textbox getSupAddress() {
		return supAddress;
	}

	public void setSupAddress(Textbox supAddress) {
		this.supAddress = supAddress;
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

	public Bandbox getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(Bandbox supplierCode) {
		this.supplierCode = supplierCode;
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
	
	public void cetakPO(Bandbox poNo) throws InterruptedException, VONEAppException{
		
		String purchNo = poNo.getText();
		String user = this.getUserInfoBean().getMsUser().getVUserName();
		
//		Messagebox.show(poNo.getText());
		
		StringBuffer sb = new StringBuffer();
		sb.append("select");
		sb.append(" o.v_po_code as kode,");
		sb.append("	v.v_vendor_name as supplier,");
		sb.append(" i.v_item_code || ' ' || i.v_item_name as item,");
		sb.append("	rd.n_pr_det_qty_requested as quantity_request, ");
		sb.append("	m.v_mitem_early_quantify as satuan_request,");
		sb.append(" d.n_po_det_qty_ordered as quantity_realisasi,");
		sb.append(" m.v_mitem_early_quantify as satuan_realisasi,");
		sb.append(" d.n_po_det_cost as harga_satuan,");
		sb.append("	d.n_subtotal");

		sb.append(" from");
		sb.append("	tb_purchase_order o,");
		sb.append("	tb_purchase_request r,");
		sb.append(" tb_purchase_request_detail rd,");
		sb.append(" ms_vendor v,");
		sb.append(" ms_item i,");
		sb.append(" tb_purchase_order_detail d,");
		sb.append(" ms_item_measurement m ");
		
		sb.append(" where");
		sb.append(" o.v_po_code= ");
		sb.append("'"+purchNo+"'");
		sb.append("	and o.n_pr_id=r.n_pr_id ");
		sb.append(" and r.n_pr_id=rd.n_pr_id");
		sb.append("	and rd.n_mitem_id=m.n_mitem_id");
		sb.append(" and o.n_vendor_id=v.n_vendor_id");
		sb.append(" and o.n_po_id=d.n_po_id");
		sb.append(" and d.n_item_id=i.n_item_id");
		sb.append(" and rd.n_item_id=i.n_item_id");
		sb.append(" and d.n_measurement_id=m.n_mitem_id");
		
		//Messagebox.show(sb.toString());
		
		Map<String, String> parameters = new HashMap<String, String>(); 
		parameters.put("PO_CODE", purchNo);
		parameters.put("PO_REQUESTOR", user);
		
		ReportEngine re = new ReportEngine(sb.toString(), 
				ReportService.getKey("cetak.po"), parameters);
		if(!re.printOut(poNo.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}

}

package com.vone.medisafe.service.ifaceimpl.purchasing;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.NumberUtil;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.TbPurchaseRequestDAO;
import com.vone.medisafe.mapping.TbPurchaseRequestDetail;
import com.vone.medisafe.mapping.dao.item.ItemDAO;
import com.vone.medisafe.mapping.dao.item.ItemMeasurementDAO;
import com.vone.medisafe.mapping.dao.item.VendorDAO;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.mapping.pojo.item.MsItemSupplied;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.service.iface.purchasing.POManager;
import com.vone.medisafe.ui.common.CommonGlobalDiscountListener;
import com.vone.medisafe.ui.purchasing.POApproval;
import com.vone.medisafe.ui.purchasing.POController;
import com.vone.medisafe.ui.purchasing.TbPurchaseOrderDAO;

public class POManagerImpl implements POManager{

	Logger logger = Logger.getLogger(POManagerImpl.class);
	
	TbPurchaseOrderDAO dao;
	VendorDAO supdao;
	TbPurchaseRequestDAO pordao;
	ItemDAO itemdao;
	ItemMeasurementDAO measuredao;

	public TbPurchaseRequestDAO getPordao() {
		return pordao;
	}

	public void setPordao(TbPurchaseRequestDAO pordao) {
		this.pordao = pordao;
	}

	public TbPurchaseOrderDAO getDao() {
		return dao;
	}

	public void setDao(TbPurchaseOrderDAO dao) {
		this.dao = dao;
	}
	
	public Integer getQtyOrderArrived(Integer prId, Integer itemId) throws VONEAppException{
		return this.dao.getQtyOrderArrived(prId, itemId);
	}
	
	public void save (TbPurchaseOrder pojo) throws VONEAppException{
		this.dao.save(pojo);
	}
	
	public void update (TbPurchaseOrder pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public TbPurchaseOrder getPOByCode(String code) throws VONEAppException {
		return this.dao.getPOByCode(code);
	}
	
	public void updateHeaderOnly (TbPurchaseOrder pojo) throws VONEAppException{
		this.dao.updateHeaderOnly(pojo);
	}
	
	public List searchPOByCodeStatus(String code, String status) throws VONEAppException {
		return this.dao.searchPOByCodeStatus(code, status);
	}
	
	public BigDecimal getLastPrice (Integer itemId, MsVendor vendorId) throws VONEAppException {
		return this.dao.getLastPrice(itemId, vendorId);
	}
	
	public List searchPOByCodeSupStatus(String poCode, String supCode, String status) throws VONEAppException {
		return this.dao.searchPOByCodeSupStatus(poCode, supCode, status);
	}
	
	public List searchPOActiveByCodeSup(String poCode, String supCode) throws VONEAppException{
		return this.dao.searchPOActiveByCodeSup(poCode, supCode);
	}
	
	public void updateChild(TbPurchaseOrderDetail pojo) throws VONEAppException{
		this.dao.updateChild(pojo);
	}
	
	public void doSearchPOR(Component cmp) throws VONEAppException{
		Textbox searchOpp = (Textbox) cmp.getFellow("searchOpp");
		Listbox listOpp = (Listbox) cmp.getFellow("listOpp");
		
		
		
		
		List list = this.pordao.searchTbPurchaseRequestByCode(
				"%" + searchOpp.getText() + "%",
				MedisafeConstants.PURCHASING_APPROVED);

			if (list == null || list.size() < 1)
				return;
			
			Iterator it = list.iterator();
			
			while (it.hasNext()) {
				TbPurchaseRequest pojo = (TbPurchaseRequest) it.next();
			
				Listitem item = new Listitem();
				item.setParent(listOpp);
			
				item.appendChild(new Listcell(pojo.getVPrCode()));
				if (pojo.getMsUnitId() != null)
					item
							.appendChild(new Listcell(pojo.getMsUnitId()
									.getVUnitName()));
			
				item.setValue(pojo);
			}		
	}
	
	public void doSearchPO(Component cmp) throws VONEAppException{
		
		Textbox searchOp = (Textbox) cmp.getFellow("poNoSearch");
		Textbox supName = (Textbox) cmp.getFellow("supNameSearch");		
		Listbox listOp = (Listbox) cmp.getFellow("listOp");
		
		listOp.getItems().clear();
		
		
		
		List list = this.dao.searchPOByCodeSup(
				"%" + searchOp.getText() + "%", "%" + supName.getText() + "%");

			if (list == null || list.size() < 1)
				return;
			
			Iterator it = list.iterator();
			
			while (it.hasNext()) {
				TbPurchaseOrder pojo = (TbPurchaseOrder) it.next();
			
				Listitem item = new Listitem();
				item.setParent(listOp);
			
				item.appendChild(new Listcell(pojo.getVPoCode()));
				item.appendChild(new Listcell(pojo.getMsVendor().getVVendorName()));
				item.appendChild(new Listcell(MedisafeUtil.convertDateToString(pojo.getDWhnCreate())));
				
				item.setValue(pojo);
			}		
	}	
	
	public void doSearchSupPOController(Component cmp) throws VONEAppException {
		Textbox searchSupCode = (Textbox)cmp.getFellow("searchSupCode");
		Textbox searchSupName = (Textbox)cmp.getFellow("searchSupName");
		Listbox listSupplier = (Listbox)cmp.getFellow("listSupplier");
		
		List list = this.supdao.searchVendor(
				"%" + searchSupCode.getText() + "%",
				"%" + searchSupName.getText() + "%");

		if (list == null || list.size() < 1)
			return;

		Iterator it = list.iterator();

		while (it.hasNext()) {
			MsVendor pojo = (MsVendor) it.next();

			Listitem item = new Listitem();
			item.setParent(listSupplier);

			item.appendChild(new Listcell(pojo.getVVendorCode()));
			item.appendChild(new Listcell(pojo.getVVendorName()));

			item.setValue(pojo);
		}
	}

	public VendorDAO getSupdao() {
		return supdao;
	}

	public void setSupdao(VendorDAO supdao) {
		this.supdao = supdao;
	}
	
	public void redraw(POController poC, TbPurchaseRequest porPojo, Component cmp) throws VONEAppException, InterruptedException {

		Listbox list = (Listbox)cmp.getFellow("list");
			
		list.getItems().clear();

		porPojo = (TbPurchaseRequest)this.pordao.searchTbPurchaseRequestByCode(porPojo.getVPrCode()).get(0);
		
		Iterator it = porPojo.getTbPurchaseRequestDetails().iterator();

		try {

			while (it.hasNext()) {
				Listitem item = new Listitem();

				TbPurchaseRequestDetail pordetPojo = (TbPurchaseRequestDetail) it
						.next();

				item.setValue(pordetPojo.getMsItem());

				item.appendChild(new Listcell(pordetPojo.getMsItem()
						.getVItemCode()));
				item.appendChild(new Listcell(pordetPojo.getMsItem()
						.getVItemName()));
				
				Listcell cellQtyRequest = new Listcell(""+pordetPojo.getNPrDetQtyRequested());
				item.appendChild(cellQtyRequest);
				
				Listcell cellMeasureRequest = new Listcell(pordetPojo.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
				item.appendChild(cellMeasureRequest);
				
				// ORD/S
				Integer qtyArrived = this.dao.getQtyOrderArrived(porPojo.getNPrId(),
								pordetPojo.getMsItem().getNItemId());
				int qtyRemaining = new Integer(pordetPojo
						.getNPrDetQtyRequested()).intValue()
						- qtyArrived.intValue();
				item.appendChild(new Listcell("" + qtyRemaining));

				// harga satuan

				Listcell cell = new Listcell();
				Decimalbox dBoxHS = new Decimalbox();
				dBoxHS
						.setValue(this.dao.getLastPrice(
										pordetPojo.getMsItem().getNItemId(),
										null));
				dBoxHS.setFormat("#,###.##");
				cell.appendChild(dBoxHS);
				item.appendChild(cell);

				// jml order
				cell = new Listcell();
				Intbox qtyOrder = new Intbox(0);
				cell.appendChild(qtyOrder);
				item.appendChild(cell);
				// satuan order
				poC.setListMeasurement();
				Listbox boxM = new Listbox();
				boxM.setMold("select");
				boxM.setStyle("width:100%;font-size:8pt");
				poC.setListboxMeasurement(boxM);
				
				poC.selectListboxMeasurement(boxM,pordetPojo.getMsItemMeasurement().getVMitemEndQuantify());
				
//				boxM.setDisabled(true);
				
				
				cell = new Listcell();
				cell.appendChild(boxM);
				item.appendChild(cell);

				// bonus
				cell = new Listcell();
				cell.appendChild(new Intbox(0));
				item.appendChild(cell);

				// diskon
				cell = new Listcell();
				Decimalbox dBoxD = new Decimalbox();
				dBoxD.setFormat("#,###.##");
				dBoxD.setStyle("width:50%");
				dBoxD.setValue(new BigDecimal(0));
				cell.appendChild(dBoxD);

				Listbox boxType = new Listbox();
				MedisafeUtil.setListPaymentOptions(boxType);
				boxType.setSelectedIndex(0);
				boxType.setStyle("width:40%;font-size:8pt");
				cell.appendChild(boxType);

				item.appendChild(cell);

				// subtotal
				cell = new Listcell();

				Label lblTotal = new Label("0");
				cell.appendChild(lblTotal);
				item.appendChild(cell);

				item.setParent(list);

				boxType.addEventListener("onSelect",
						new CommonGlobalDiscountListener(item, boxType, dBoxHS,
								qtyOrder, dBoxD, lblTotal));

				item.addEventListener("onSelect",
						new CommonGlobalDiscountListener(item, boxType, dBoxHS,
								qtyOrder, dBoxD, lblTotal));

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

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new VONEAppException(MessagesService
					.getKey("common.internal.error"));
		}
	}
	
	public void redrawPO(POController poC, TbPurchaseOrder tbPO, Component cmp) throws VONEAppException, InterruptedException {

		Listbox list = (Listbox)cmp.getFellow("list");
		Label status = (Label)cmp.getFellow("status");
			
		list.getItems().clear();

		tbPO = this.dao.getPOByCode(tbPO.getVPoCode());
		
		//set POR
		poC.getOppNo().setText(tbPO.getTbPurchaseRequest().getVPrCode());
		
		//set supplier
		poC.getSupplierCode().setValue(tbPO.getMsVendor().getVVendorCode());
		poC.getSupName().setValue(tbPO.getMsVendor().getVVendorName());
		poC.getSupAddress().setValue(tbPO.getMsVendor().getVVendorAddress());
		poC.getSupTelp().setValue(tbPO.getMsVendor().getVVendorContactNo());
		
		status.setValue(tbPO.getVPoStatus());
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		double total = 0;
		
		Iterator it = tbPO.getTbPurchaseOrderDetails().iterator();

		try {

			while (it.hasNext()) {
				Listitem item = new Listitem();

				TbPurchaseOrderDetail tbPOD = (TbPurchaseOrderDetail) it
						.next();

				item.setValue(tbPOD.getMsItem());

				item.appendChild(new Listcell(tbPOD.getMsItem()
						.getVItemCode()));
				item.appendChild(new Listcell(tbPOD.getMsItem()
						.getVItemName()));
				item.appendChild(new Listcell(""));
				item.appendChild(new Listcell(tbPOD.getMsItem()
						.getMsItemMeasurement().getVMitemEndQuantify()));
				// ORD/S
//				Integer qtyArrived = this.dao.getQtyOrderArrived(porPojo.getNPrId(),
//								pordetPojo.getMsItem().getNItemId());
//				int qtyRemaining = new Integer(pordetPojo
//						.getNPrDetQtyRequested()).intValue()
//						- qtyArrived.intValue();
				item.appendChild(new Listcell(""));

				// harga satuan

				Listcell cell = new Listcell();
				Decimalbox dBoxHS = new Decimalbox();
				dBoxHS
						.setValue(this.dao.getLastPrice(
								tbPOD.getMsItem().getNItemId(),
										null));
				dBoxHS.setFormat("#,###.##");
				cell.appendChild(dBoxHS);
				item.appendChild(cell);

				// jml order
				cell = new Listcell();
				Intbox qtyOrder = new Intbox(tbPOD.getNPoDetQtyOrdered());
				cell.appendChild(qtyOrder);
				item.appendChild(cell);
				// satuan order
				poC.setListMeasurement();
				Listbox boxM = new Listbox();
				boxM.setMold("select");
				boxM.setStyle("width:100%;font-size:8pt");
				poC.setListboxMeasurement(boxM);
				
				poC.selectListboxMeasurement(boxM,tbPOD.getMsItemMeasurement().getVMitemEndQuantify());

				cell = new Listcell();
				cell.appendChild(boxM);
				item.appendChild(cell);

				// bonus
				cell = new Listcell();
				cell.appendChild(new Intbox(0));
				item.appendChild(cell);

				// diskon
				cell = new Listcell();
				Decimalbox dBoxD = new Decimalbox();
				dBoxD.setFormat("#,###.##");
				dBoxD.setStyle("width:50%");
				dBoxD.setValue(new BigDecimal(0));
				cell.appendChild(dBoxD);

				Listbox boxType = new Listbox();
				MedisafeUtil.setListPaymentOptions(boxType);
				boxType.setSelectedIndex(0);
				boxType.setStyle("width:40%;font-size:8pt");
				cell.appendChild(boxType);

				item.appendChild(cell);

				// subtotal
				cell = new Listcell();
				total = total + tbPOD.getNSubtotal();
				db.setValue(new BigDecimal(tbPOD.getNSubtotal()));
				Label lblTotal = new Label(db.getText());
				cell.appendChild(lblTotal);
				item.appendChild(cell);

				item.setParent(list);

				boxType.addEventListener("onSelect",
						new CommonGlobalDiscountListener(item, boxType, dBoxHS,
								qtyOrder, dBoxD, lblTotal));

				item.addEventListener("onSelect",
						new CommonGlobalDiscountListener(item, boxType, dBoxHS,
								qtyOrder, dBoxD, lblTotal));

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
			
			poC.total.setValue(new BigDecimal(total));
//			poC.btnPrint.setDisabled(false);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new VONEAppException(MessagesService
					.getKey("common.internal.error"));
		}
	}	
	
	public void doFilterSup(POController poC, MsVendor supPojo, Listbox listTemp, Listbox list) throws VONEAppException, InterruptedException {
		
		Textbox supName = (Textbox)list.getFellow("supName");
		Textbox supAddress = (Textbox)list.getFellow("supAddress");
		Textbox supTelp = (Textbox)list.getFellow("supTelp");
//		Listbox list = poC.getList();
		
		supPojo = this.supdao.getVendorByCode(supPojo.getVVendorCode());
		
		// set Latest Price from Supplier
		// getVendor
		/**
		Iterator it = listTemp.getItems().iterator();

		while (it.hasNext()) {
			Listitem item = (Listitem) it.next();
			MsItem itemPojo = (MsItem) item.getValue();

			Listcell cell = (Listcell) item.getChildren().get(5);
			Decimalbox dBoxHS = (Decimalbox) cell.getChildren().get(0);
			dBoxHS.setValue(this.dao.getLastPrice(
					itemPojo.getNItemId(), supPojo));

		}*/
		// end of setting

		supName.setText(supPojo.getVVendorName());
		supAddress.setText(supPojo.getVVendorAddress());
		supTelp.setText(supPojo.getVVendorContactNo());

//		poC.doRetrieveOriginalList();
		/**
		Iterator it2 = null;
		boolean found;
		
		for (int i = 0; i < list.getItemCount(); i++) {
			Listitem item = list.getItemAtIndex(i);
			found = false;

			MsItem msItem = (MsItem) item.getValue();
			int item_id = msItem.getNItemId().intValue();

			it2 = supPojo.getMsItemSupplieds().iterator();

			while (it2.hasNext()) {
				MsItemSupplied itemsupPojo = (MsItemSupplied) it2.next();
				
				if (itemsupPojo.getMsItem().getNItemId().intValue() == item_id) {
					found = true;
					break;
				}
			}
			
			if (!found) {			
				list.removeItemAt(i);
			} 
		}*/

	}
	
	public void doSearchApproval(Component cmp) throws VONEAppException {
		
		Listbox listOp = (Listbox)cmp.getFellow("listOp");
		Textbox opNoSearch = (Textbox)cmp.getFellow("opNoSearch");
		Checkbox isvalid = (Checkbox)cmp.getFellow("isvalid");
		
		List list = null;
		
		if (isvalid.isChecked())
			list = this.dao.searchPOByCodeStatus("%"+opNoSearch.getText()+"%",MedisafeConstants.PURCHASING_APPROVED);
		else
			list = this.dao.searchPOByCodeStatus("%"+opNoSearch.getText()+"%",MedisafeConstants.PURCHASING_OPEN);
		
		if (list == null || list.size() < 1) return;
		
		Iterator it = list.iterator();
		
		listOp.getItems().clear();		
		
		while (it.hasNext()){
			TbPurchaseOrder pojo = (TbPurchaseOrder)it.next();
			
			Listitem item = new Listitem();
			item.setParent(listOp);
			
			item.appendChild(new Listcell(pojo.getVPoCode()));
			
			item.appendChild(new Listcell(pojo.getMsVendor().getVVendorName()));
			
			item.setValue(pojo);
		}
	}
	
	public void doApprove(POApproval ctr, TbPurchaseOrder pojo) throws InterruptedException, VONEAppException {
			
		pojo.setVPoStatus(MedisafeConstants.PURCHASING_APPROVED);
		pojo.setMsStaffByNApproverId(ctr.getUserInfoBean().getMsUser().getMsStaff());
		
		this.dao.updateHeaderOnly(pojo);
				
		Messagebox.show(MessagesService.getKey("pch.order.approve.success"));

		ctr.getStatus().setValue("STATUS : "+ pojo.getVPoStatus());
		ctr.getApprovedBy().setText(pojo.getMsStaffByNApproverId().getVStaffCode()+"-"+pojo.getMsStaffByNApproverId().getVStaffName());
		
		if (ctr.getListOp().getSelectedItem() != null)
			ctr.getListOp().removeChild(ctr.getListOp().getSelectedItem());						

		ctr.getBtnApprove().setDisabled(true);
	}
	
	public void redraw(POApproval ctr, TbPurchaseOrder pojo) throws InterruptedException, VONEAppException {
		
		pojo = this.dao.getPOByCode(pojo.getVPoCode());
		
		ctr.getApprovedBy().setText("");
		
		ctr.getIssuedBy().setText(pojo.getMsStaffByNIssuerId().getVStaffCode()+"-"+pojo.getMsStaffByNIssuerId().getVStaffName());
		
		ctr.getStatus().setValue("STATUS : "+ pojo.getVPoStatus());
		
		ctr.getList().getItems().clear();
				
		Iterator it = pojo.getTbPurchaseOrderDetails().iterator();
		
		while (it.hasNext()){
			TbPurchaseOrderDetail pojoDet = (TbPurchaseOrderDetail)it.next();
						
			Listitem item = new Listitem();
			item.setValue(pojoDet);
			item.setParent(ctr.getList());
			
			//Kode						
			item.appendChild(new Listcell(pojoDet.getMsItem().getVItemCode()));
			item.appendChild(new Listcell(pojoDet.getMsItem().getVItemName()));
						
			//harga satuan
			item.appendChild(new Listcell(NumberUtil.format(new Double(pojoDet.getNPoDetCost()))));
			
			//jml order
			item.appendChild(new Listcell(""+pojoDet.getNPoDetQtyOrdered()));
			
			//satuan
			item.appendChild(new Listcell(pojoDet.getMsItemMeasurement().getVMitemEndQuantify()));

			//bonus
			item.appendChild(new Listcell(""+pojoDet.getNBonus()));						
			
			//discount
			item.appendChild(new Listcell(""+pojoDet.getNDiscount()+" "+pojoDet.getVDiscountType()));			
	
			//subtotal
			item.appendChild(new Listcell(""+NumberUtil.format(new Double(pojoDet.getNSubtotal()))));
		}
		ctr.getBtnApprove().setDisabled(false);		
	}
	
	public void addItem(POController ctr, MsItem itemPojo) throws VONEAppException {
		
		itemPojo = this.itemdao.getById(itemPojo.getNItemId());
		
		if (itemPojo == null)
			throw new VONEAppException(MessagesService.getKey("error.internal"));
		
		Listitem item = new Listitem();

		item.setValue(itemPojo);

		// kode
		item.appendChild(new Listcell(itemPojo.getVItemCode()));

		// name
		item.appendChild(new Listcell(itemPojo.getVItemName()));

		// ordA
		item.appendChild(new Listcell("0"));

		// satuan
		item.appendChild(new Listcell(itemPojo.getMsItemMeasurement()
				.getVMitemEndQuantify()));

		// ordS
		item.appendChild(new Listcell("0"));

		// hargaSatuan
		// getVendor
		MsVendor supPojo = this.supdao.getVendorByCode(ctr.getSupplierCode().getText());

		Listcell cell = new Listcell();
		Decimalbox dBoxHS = new Decimalbox();
		dBoxHS.setValue(this.dao.getLastPrice(
				itemPojo.getNItemId(), supPojo));
		dBoxHS.setFormat("#,###.##");
		cell.appendChild(dBoxHS);
		item.appendChild(cell);

		// jml order
		cell = new Listcell();
		Intbox qtyOrder = new Intbox(0);
		cell.appendChild(qtyOrder);
		item.appendChild(cell);

		// satuan order
		ctr.setListMeasurement();
		Listbox boxM = new Listbox();
		boxM.setMold("select");
		boxM.setStyle("width:100%;font-size:8pt");
		ctr.setListboxMeasurement(boxM);

		cell = new Listcell();
		cell.appendChild(boxM);
		item.appendChild(cell);

		// bonus
		cell = new Listcell();
		cell.appendChild(new Intbox(0));
		item.appendChild(cell);

		// diskon
		cell = new Listcell();
		Decimalbox dBoxD = new Decimalbox();
		dBoxD.setFormat("#,###.##");
		dBoxD.setStyle("width:50%");
		dBoxD.setValue(new BigDecimal(0));
		cell.appendChild(dBoxD);

		Listbox boxType = new Listbox();
		MedisafeUtil.setListPaymentOptions(boxType);
		boxType.setSelectedIndex(0);
		boxType.setStyle("width:40%;font-size:8pt");
		cell.appendChild(boxType);

		item.appendChild(cell);

		// subtotal
		cell = new Listcell();

		Label lblTotal = new Label("0");
		cell.appendChild(lblTotal);
		item.appendChild(cell);

		item.setParent(ctr.getList());

		boxType.addEventListener("onSelect", new CommonGlobalDiscountListener(
				item, boxType, dBoxHS, qtyOrder, dBoxD, lblTotal));

		item.addEventListener("onSelect", new CommonGlobalDiscountListener(
				item, boxType, dBoxHS, qtyOrder, dBoxD, lblTotal));

		dBoxHS.addEventListener("onChange", new CommonGlobalDiscountListener(
				item, boxType, dBoxHS, qtyOrder, dBoxD, lblTotal));

		qtyOrder.addEventListener("onChange", new CommonGlobalDiscountListener(
				item, boxType, dBoxHS, qtyOrder, dBoxD, lblTotal));

		dBoxD.addEventListener("onChange", new CommonGlobalDiscountListener(
				item, boxType, dBoxHS, qtyOrder, dBoxD, lblTotal));
	}

	public ItemDAO getItemdao() {
		return itemdao;
	}

	public void setItemdao(ItemDAO itemdao) {
		this.itemdao = itemdao;
	}

	public class MeasureConvertListener implements EventListener{
		
		private MeasureConvertListener(){}
		
		TbPurchaseRequestDetail detPojo = null;
		Listcell qtyInit = null;
		Listcell measureInit = null;		
		Listbox mList = null;
		ItemMeasurementDAO dao = null;
		
		
		public MeasureConvertListener(ItemMeasurementDAO dao, TbPurchaseRequestDetail detPojo, Listcell qtyInit, Listcell measureInit, Listbox mList){
			
			this.detPojo = detPojo;
			this.qtyInit = qtyInit;
			this.measureInit = measureInit;
			this.mList = mList;
			this.dao = dao;
		}

		public boolean isAsap() {
			// TODO Auto-generated method stub
			return true;
		}

		public void onEvent(Event arg0) {
			// TODO Auto-generated method stub
			
			MsItemMeasurement mii = (MsItemMeasurement)mList.getSelectedItem().getValue();
			
			try {
				System.out.println(dao.getMsItemMeasurementByCode(mii.getVMitemEndQuantify()));
				
				dao.getMsItemMeasurementByCode(mii.getVMitemEndQuantify());
				System.out.println(mii.getMsItems());
			} catch (VONEAppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
	}

	public ItemMeasurementDAO getMeasuredao() {
		return measuredao;
	}

	public void setMeasuredao(ItemMeasurementDAO measuredao) {
		this.measuredao = measuredao;
	}
	
}


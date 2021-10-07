package com.vone.medisafe.service.ifaceimpl.purchasing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.NumberUtil;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbDeliveryOrder;
import com.vone.medisafe.mapping.TbDeliveryOrderDetail;
import com.vone.medisafe.mapping.TbPurchaseOrder;
import com.vone.medisafe.mapping.TbPurchaseOrderDetail;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;
import com.vone.medisafe.service.iface.purchasing.DOManager;
import com.vone.medisafe.ui.purchasing.DOBatchController;
import com.vone.medisafe.ui.purchasing.DOController;
import com.vone.medisafe.ui.purchasing.TbDeliveryOrderDAO;
import com.vone.medisafe.ui.purchasing.TbPurchaseOrderDAO;

public class DOManagerImpl implements DOManager{

	Logger logger = Logger.getLogger(DOManagerImpl.class);
	
	TbDeliveryOrderDAO dao;
	TbPurchaseOrderDAO podao;

	public TbDeliveryOrderDAO getDao() {
		return dao;
	}

	public void setDao(TbDeliveryOrderDAO dao) {
		this.dao = dao;
	}
	
	public void save (TbDeliveryOrder pojo) throws VONEAppException{
		this.dao.save(pojo);
	}
	
	public void update (TbDeliveryOrder pojo) throws VONEAppException {
		this.dao.update(pojo);
	}	
	
	public void updateHeaderOnly (TbDeliveryOrder pojo) throws VONEAppException{
		this.dao.updateHeaderOnly(pojo);		
	}
	
	public TbDeliveryOrder getDObyCode (String code) throws VONEAppException {
		return this.dao.getDObyCode(code);
	}	
	
	public List searchDObyCodeWhouse(String code, String whouseCode) throws VONEAppException{
		return this.dao.searchDObyCodeWhouse(code, whouseCode);
	}
	
	public void executeApproval (TbDeliveryOrder tbDO, Listbox listBatch) throws VONEAppException{
		this.dao.executeApproval(tbDO, listBatch);
	}
	
	public void delete (TbDeliveryOrder pojo) throws VONEAppException{
		this.dao.delete(pojo);
	}
	
	public TbBatchItem getBatchItemByBatchCode (String code) throws VONEAppException{
		return this.dao.getBatchItemByBatchCode(code);
	}
	
	public void doSearch(DOController ctr) throws VONEAppException {
		ctr.getListOp().getItems().clear();	
		
		List list = this.podao.searchPOActiveByCodeSup("%"+ctr.getPoNoSearch().getText()+"%", 
				"%"+ctr.getSupNameSearch().getText()+"%");		
		
		if (list == null || list.size() < 1) return;
		
		Iterator it = list.iterator();	
		
		while (it.hasNext()){
			TbPurchaseOrder pojo = (TbPurchaseOrder)it.next();
			
			Listitem item = new Listitem();
			item.setParent(ctr.getListOp());
			
			item.appendChild(new Listcell(pojo.getVPoCode()));
			if (pojo.getMsVendor() != null)
				item.appendChild(new Listcell(pojo.getMsVendor().getVVendorName()));
						
			item.appendChild(new Listcell(MedisafeUtil.convertDateToString(pojo.getDWhnCreate())));
			
			item.setValue(pojo);
		}
	}

	public TbPurchaseOrderDAO getPodao() {
		return podao;
	}

	public void setPodao(TbPurchaseOrderDAO podao) {
		this.podao = podao;
	}
	
	public void doSearchDO(DOController ctr) throws VONEAppException {
		ctr.getListDo().getItems().clear();
		
		List list = this.dao.searchDObyCodeWhouse(
				"%"+ctr.getDoNoSearch().getText()+"%", "%"+ctr.getWhouseSearch().getText()+"%");
		
		Iterator it = list.iterator();
		
		while (it.hasNext()){
			
			TbDeliveryOrder tbDO = (TbDeliveryOrder)it.next();
			
			Listitem item = new Listitem();
			item.setValue(tbDO);
			item.setParent(ctr.getListDo());
			
			//No Do
			item.appendChild(new Listcell(tbDO.getVDoCode()));
			//Nama Gudang
			item.appendChild(new Listcell(tbDO.getMsWarehouse().getVWhouseName()));
			//Date			
			item.appendChild(new Listcell(MedisafeUtil.convertDateToString(tbDO.getDWhnCreate())));
		}
	}
	
	public void redraw(DOController ctr, TbPurchaseOrder poPojo) throws VONEAppException {
		poPojo = this.podao.getPOByCode(poPojo.getVPoCode());
		
		Iterator it = poPojo.getTbPurchaseOrderDetails().iterator();
		
		//SET SUPPLIER DATA
		ctr.getSupCode().setText(poPojo.getMsVendor().getVVendorCode());
		ctr.getSupName().setText(poPojo.getMsVendor().getVVendorName());
		ctr.getSupAddress().setText(poPojo.getMsVendor().getVVendorAddress());
		ctr.getSupTelp().setText(poPojo.getMsVendor().getVVendorContactNo());
		
		try{
			while (it.hasNext()){
				Listitem item = new Listitem();
				item.setParent(ctr.getList());
				
				TbPurchaseOrderDetail podPojo = (TbPurchaseOrderDetail)it.next();
				
				item.setValue(podPojo);
				
				//PUT ITEMCODE TO ITEMLIST SESSION
				item.setAttribute(DOController.ITEM_CODE, podPojo.getMsItem().getVItemCode());
				
				item.appendChild(new Listcell(podPojo.getMsItem().getVItemCode()));
				item.appendChild(new Listcell(podPojo.getMsItem().getVItemName()));
				item.appendChild(new Listcell(""+podPojo.getNPoDetQtyOrdered()));
				item.appendChild(new Listcell(""+podPojo.getNBonus()));
				item.appendChild(new Listcell(podPojo.getMsItemMeasurement().getVMitemEndQuantify()));	
				item.appendChild(new Listcell(NumberUtil.format(podPojo.getNPoDetCost())));
				
				int qtySisa= new Integer(podPojo.getNPoDetQtyOrdered()).intValue() 
				- new Integer(podPojo.getNPoDetQtyReceived()).intValue();
				int qtyBonusSisa = new Integer(podPojo.getNBonus())
				- new Integer(podPojo.getNBonusRecieved());
				
				item.appendChild(new Listcell(""+qtySisa));
				
				//qty arrived
				Listcell cell = new Listcell();
				Intbox qtyOrder = new Intbox(qtySisa);
				cell.appendChild(qtyOrder);
				item.appendChild(cell);
				//PUT TO SESSION
				item.setAttribute(DOController.ITEMARRIVEDINTBOX, qtyOrder);
				
				//bonus diterima
				item.appendChild(new Listcell(""+qtyBonusSisa));
				//bonus arrived
				cell = new Listcell();
				Intbox qtyBonusOrder = new Intbox(qtyBonusSisa);
				cell.appendChild(qtyBonusOrder);
				item.appendChild(cell);			
				//PUT TO SESSION
				item.setAttribute(DOController.BONUSARRIVEDINTBOX, qtyBonusOrder);
				
				//discount				
				cell = new Listcell();
				cell.setParent(item);
				cell.appendChild(new Label(""+podPojo.getNDiscount()));
				//discount type
				cell.appendChild(new Label(podPojo.getVDiscountType()));
				
				//subtotal
				Label total = new Label();
				cell = new Listcell();
				cell.appendChild(total);
				item.appendChild(cell);
				
				Double itemSession = new Double(0);				

				if (MedisafeConstants.RP.equals(podPojo.getVDiscountType())){
					double disc = podPojo.getNDiscount() / podPojo.getNPoDetQtyOrdered() * qtyOrder.intValue();					
					
					double res = podPojo.getNPoDetCost() * qtyOrder.intValue() - disc;
					 
					
					total.setValue(NumberUtil.format(res));
						itemSession = new Double(res);
				}else{					
					
					double res = podPojo.getNPoDetCost() * qtyOrder.intValue();
					
					double disVal = podPojo.getNDiscount() / 100 * res;
					
					res -= disVal;
					
					total.setValue(NumberUtil.format(res));
					
					itemSession = new Double(res);

				}
				
				//set Subtotal to Session
				item.setAttribute(MedisafeConstants.PURCHASING_SESSION, itemSession);
				
				qtyOrder.addEventListener("onChange", new DiscountListener(ctr, podPojo.getNPoDetCost(), podPojo.getNDiscount(),
						podPojo.getVDiscountType(), total, qtyOrder, item, podPojo.getNPoDetQtyOrdered()));
								
			}
			
			ctr.doCalculateTotal();
			ctr.doCalculateGrandTotal();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new VONEAppException(MessagesService.getKey("common.internal.error"));			
		}				

	}
	
	class DiscountListener implements EventListener{				
		
		double hargaSat = 0;
		double discount = 0;
		String discountType = "";
		Label result = null;
		Intbox qty = null;
		int qtyOrdered = 0;
		Listitem item = null;
		DOController ctr = null;
		
		private DiscountListener(){};
		
		public DiscountListener(DOController ctr, double hargaSat, double discount, String discountType,
				Label result, Intbox qty, Listitem item, int qtyOrdered){
			
			this.hargaSat = hargaSat;
			this.discount = discount;
			this.discountType = discountType;
			this.result = result;
			this.qty = qty;
			this.item = item;
			this.qtyOrdered = qtyOrdered;
			this.ctr = ctr;
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
			
			ctr.doCalculateTotal();
			ctr.doCalculateGrandTotal();
		}		
	}	
	
	public void redrawExistingDO(DOController ctr) throws VONEAppException {
		TbDeliveryOrder doPojo = null;
		TbPurchaseOrder poPojo = null;
	
		doPojo = this.dao.getDObyCode(ctr.getBppNo().getText());

		poPojo = doPojo.getTbPurchaseOrder();		
		
		ctr.getPoNo().setText(poPojo.getVPoCode());
		
		poPojo = doPojo.getTbPurchaseOrder();
		
		if (MedisafeConstants.RP.equals(poPojo.getVDiscountType())){
			ctr.getDiscountType().setSelectedIndex(0);
		}else{
			ctr.getDiscountType().setSelectedIndex(1);
		}
		
		ctr.getDiscountType().setDisabled(true);		
		ctr.getPpnType().setSelectedIndex(1);
		ctr.getDiscount().setValue(new BigDecimal(poPojo.getNDiscount()));
		ctr.getDiscount().setDisabled(true);
		ctr.getPpn().setValue(new BigDecimal(doPojo.getNDoTax()));		
		ctr.getRecBy().setValue(doPojo.getVWhoCreate());
		ctr.getRecDate().setValue(doPojo.getDRecDate());
		
		//SET SUPPLIER DATA
		ctr.getSupCode().setText(poPojo.getMsVendor().getVVendorCode());
		ctr.getSupName().setText(poPojo.getMsVendor().getVVendorName());
		ctr.getSupAddress().setText(poPojo.getMsVendor().getVVendorAddress());
		ctr.getSupTelp().setText(poPojo.getMsVendor().getVVendorContactNo());
						
		ctr.getList().getItems().clear();
		
		Iterator it = doPojo.getTbDeliveryOrderDetails().iterator();
		
		try{
			while (it.hasNext()){
				Listitem item = new Listitem();
				item.setParent(ctr.getList());
				
				TbDeliveryOrderDetail dodPojo = (TbDeliveryOrderDetail)it.next();
				TbPurchaseOrderDetail podPojo = dodPojo.getTbPurchaseOrderDetail();
				
				item.setValue(podPojo);
		
				//PUT ITEMCODE TO ITEMLIST SESSION
				item.setAttribute(DOController.ITEM_CODE, podPojo.getMsItem().getVItemCode());
				
				item.appendChild(new Listcell(dodPojo.getMsItem().getVItemCode()));
				item.appendChild(new Listcell(dodPojo.getMsItem().getVItemName()));
				item.appendChild(new Listcell(""+podPojo.getNPoDetQtyOrdered()));
				item.appendChild(new Listcell(""+podPojo.getNBonus()));
				item.appendChild(new Listcell(podPojo.getMsItemMeasurement().getVMitemEndQuantify()));				
				item.appendChild(new Listcell(NumberUtil.format(podPojo.getNPoDetCost())));
				
				int qtySisa= new Integer(podPojo.getNPoDetQtyOrdered()).intValue() 
				- new Integer(podPojo.getNPoDetQtyReceived()).intValue();
				int qtyBonusSisa = new Integer(podPojo.getNBonus())
				- new Integer(podPojo.getNBonusRecieved());
				
				item.appendChild(new Listcell(""+qtySisa));
				
				//qty arrived
				Listcell cell = new Listcell();
				Intbox qtyOrder = new Intbox(dodPojo.getNDoDetQty());
				cell.appendChild(qtyOrder);
				item.appendChild(cell);				
				//PUT TO SESSION
				item.setAttribute(DOController.ITEMARRIVEDINTBOX, qtyOrder);
				
				//bonus diterima
				item.appendChild(new Listcell(""+qtyBonusSisa));
				//bonus arrived
				cell = new Listcell();
				Intbox qtyBonusOrder = new Intbox(dodPojo.getNDoBonusQty());
				cell.appendChild(qtyBonusOrder);
				item.appendChild(cell);
				//PUT TO SESSION
				item.setAttribute(DOController.BONUSARRIVEDINTBOX, qtyBonusOrder);
				
				
				//discount				
				cell = new Listcell();
				cell.setParent(item);
				cell.appendChild(new Label(""+podPojo.getNDiscount()));
				//discount type
				cell.appendChild(new Label(podPojo.getVDiscountType()));
				
				//subtotal
				Label total = new Label();
				cell = new Listcell();
				cell.appendChild(total);
				item.appendChild(cell);
				
				Double itemSession = new Double(0);				
				if (MedisafeConstants.RP.equals(podPojo.getVDiscountType())){
					double disc = podPojo.getNDiscount() / podPojo.getNPoDetQtyOrdered() * qtyOrder.intValue();					
					
					double res = podPojo.getNPoDetCost() * qtyOrder.intValue() - disc;
					 
					
					total.setValue(NumberUtil.format(res));
						itemSession = new Double(res);
				}else{					
					
					double res = podPojo.getNPoDetCost() * qtyOrder.intValue();
					
					double disVal = podPojo.getNDiscount() / 100 * res;
					
					res -= disVal;
					
					total.setValue(NumberUtil.format(res));
					
					itemSession = new Double(res);

				}
				
				//set Subtotal to Session
				item.setAttribute(MedisafeConstants.PURCHASING_SESSION, itemSession);
				
				qtyOrder.addEventListener("onChange", new DiscountListener(ctr,podPojo.getNPoDetCost(), podPojo.getNDiscount(),
						podPojo.getVDiscountType(), total, qtyOrder, item, podPojo.getNPoDetQtyOrdered()));
												
			}
			
			ctr.doCalculateTotal();
			ctr.doCalculateGrandTotal();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new VONEAppException(MessagesService.getKey("common.internal.error"));			
		}
		
	}
	
	public boolean isValidInput(DOController ctr) throws InterruptedException, VONEAppException {
		boolean result = true;
		
		TbPurchaseOrderDetail tbPOD = null;				
		
		Iterator it = ctr.getList().getItems().iterator();	
		
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			
			tbPOD = (TbPurchaseOrderDetail)item.getValue();
			
			int qtySisa= new Integer(tbPOD.getNPoDetQtyOrdered()).intValue() 
			- new Integer(tbPOD.getNPoDetQtyReceived()).intValue();
			int qtyBonusSisa = new Integer(tbPOD.getNBonus())
			- new Integer(tbPOD.getNBonusRecieved());
			
			//qty arrived
			Listcell cell = (Listcell)item.getChildren().get(7);
			Intbox ibox = (Intbox)cell.getChildren().get(0);
			if (ibox.getValue() > qtySisa || ibox.getValue() < 0){
				Messagebox.show(MessagesService.getKey("pch.do.qty.order.over"));
				ibox.select();
				return false;
			}
			
			//bonus arrived
			cell = (Listcell)item.getChildren().get(9);
			ibox = (Intbox)cell.getChildren().get(0);
			
			if (ibox.getValue() == null)
				ibox.setValue(new Integer(0));
			
			if (ibox.getValue() > qtyBonusSisa || ibox.getValue() < 0){
				Messagebox.show(MessagesService.getKey("pch.do.qty.bonus.over"));
				ibox.select();
				return false;
			}
		}
		
		return result;
	}
	
	public void redraw(DOBatchController ctr) throws VONEAppException {
		ctr.getDoNumber().setText(ctr.getTbDO().getVDoCode());
		ctr.getExpiredDate().setValue(new Date());
		ctr.getList().getItems().clear();
		ctr.getQty().setValue(new Integer(0));
		
		TbDeliveryOrder tbDO = this.dao.getDObyCode(ctr.getTbDO().getVDoCode());
		
		//setItemList
		ctr.getItemList().getItems().clear();
		Iterator<TbDeliveryOrderDetail> it = tbDO.getTbDeliveryOrderDetails().iterator();
		
		while (it.hasNext()){
			TbDeliveryOrderDetail tbDOD = it.next();
			
			Listitem item = new Listitem();
			item.setParent(ctr.getItemList());
			
			item.setValue(tbDOD.getMsItem());	
			
			item.appendChild(new Listcell(tbDOD.getMsItem().getVItemCode()+"-"+tbDOD.getMsItem().getVItemName()));
			
			item.setAttribute(DOBatchController.INITQTY, tbDOD.getNDoDetQty()+tbDOD.getNDoBonusQty());
			
			item.setAttribute(DOBatchController.INITM, tbDOD.getTbPurchaseOrderDetail().getMsItemMeasurement().getVMitemEndQuantify());
		}
		
		if (ctr.getItemList().getItemCount() > 0)
			ctr.getItemList().setSelectedIndex(0);
	}
	
	public boolean doSave(DOBatchController ctr) throws VONEAppException, InterruptedException {
		ctr.setTbDO(this.dao.getDObyCode(ctr.getDoNumber().getText()));
		
		//check DOController
		DOController doC= (DOController)Sessions.getCurrent().getAttribute(MedisafeConstants.PURCHASING_SESSION);
		if (doC == null){
			Messagebox.show(MessagesService.getKey("internal.error"));
			return false;
		}
		
		//check Qty
		if (ctr.getQty().getValue() < 1){
			Messagebox.show(MessagesService.getKey("pch.do.batch.qty.less"));
			ctr.getQty().select();
			return false;
		}
		
		//check for max qty
		//totalQty is total qty on List + qty that will be added
		Iterator it = ctr.getList().getItems().iterator();
		Integer totalQty = 0;
		
		MsItem msItemHead = (MsItem)ctr.getItemList().getSelectedItem().getValue();
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			
			MsItem msItemList = (MsItem)item.getValue();			
			
			if (msItemList.getVItemCode().equals(msItemHead.getVItemCode())){
				Integer qty = (Integer)item.getAttribute(DOBatchController.QTY);
				Short multiplier = (Short)item.getAttribute(DOBatchController.MULTIPLIER);
				
				//THE IDEA IS TO DIVIDE QTY BY MULTIPLIER -> SEE isAllItemRegistered(DoBatchController) on this class
				Integer qtyFinal = qty/multiplier;
				
				totalQty += qtyFinal;
			}						
		}
		totalQty += ctr.getQty().getValue();
		
		
		//totalQtyDO is qty on DO Detail that will be compared to totalQty
		Integer totalQtyDO = 0;					
		it = doC.getList().getItems().iterator();
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			String itemCode = (String)item.getAttribute(DOController.ITEM_CODE);
			Intbox itemArrived = (Intbox)item.getAttribute(DOController.ITEMARRIVEDINTBOX);
			Intbox bonusArrived = (Intbox)item.getAttribute(DOController.BONUSARRIVEDINTBOX);
			
			if (itemCode.equals(msItemHead.getVItemCode())){
				totalQtyDO = itemArrived.getValue() + bonusArrived.getValue();
				break;
			}
		}
		
//		//totalQtyDO is qty on DO Detail that will be compared to totalQty
//		Integer totalQtyDO = 0;		
//		it = ctr.getTbDO().getTbDeliveryOrderDetails().iterator();
//		while (it.hasNext()){
//			TbDeliveryOrderDetail tbDOD = (TbDeliveryOrderDetail)it.next();
//			
//			if (tbDOD.getMsItem().getNItemId().intValue() == msItemHead.getNItemId().intValue()){		
//				totalQtyDO = tbDOD.getNDoDetQty() + tbDOD.getNDoBonusQty();
//				break;
//			}
//		}
				
		if (totalQty > totalQtyDO){
			Messagebox.show(MessagesService.getKey("pch.do.batch.qty.over"));
			
			ctr.getQty().select();
			return false;
		}
		
		//check for duplicate BatchNo
		it = ctr.getList().getItems().iterator();
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();				
			
			//local check
			Object obj = item.getAttribute(DOBatchController.BATCH_NO);
			
			if (obj.equals(ctr.getBatchNo().getText())){
				Messagebox.show(MessagesService.getKey("pch.do.batch.duplicate.local"));
				ctr.getBatchNo().select();
				return false;
			}
			
		}
		
		//DB check		
		Object obj = this.dao.getBatchItemByBatchCode(ctr.getBatchNo().getText());
		if (obj != null){
			Messagebox.show(MessagesService.getKey("pch.do.batch.duplicate.db"));
			ctr.getBatchNo().select();
			return false;
		}
		
		return true;
	}
	
	public boolean isAllItemRegistered(DOBatchController ctr) throws InterruptedException, VONEAppException {
		ctr.setTbDO(this.dao.getDObyCode(ctr.getDoNumber().getText()));
		
		if (ctr.getTbDO() == null) return false;
		
		//check DOController
		DOController doC= (DOController)Sessions.getCurrent().getAttribute(MedisafeConstants.PURCHASING_SESSION);
		if (doC == null){
			Messagebox.show(MessagesService.getKey("internal.error"));
			return false;
		}
		
		//totalQtyDO is qty on DO Detail that will be compared to totalQty
//		Integer totalQtyDO = 0;					
//		Iterator it = doC.getList().getItems().iterator();
//		while (it.hasNext()){
//			Listitem item = (Listitem)it.next();
//			String itemCode = (String)item.getAttribute(DOController.ITEM_CODE);
//			Intbox itemArrived = (Intbox)item.getAttribute(DOController.ITEMARRIVEDINTBOX);
//			Intbox bonusArrived = (Intbox)item.getAttribute(DOController.BONUSARRIVEDINTBOX);
//			
//			if (itemCode.equals(msItemHead.getVItemCode())){
//				totalQtyDO = itemArrived.getValue() + bonusArrived.getValue();
//				break;
//			}
//		}
//		
		
		Iterator<Listitem> it = doC.getList().getItems().iterator();

		
		//Qty on List
		Integer totalQtyLocal = 0;
		
		while (it.hasNext()){
			Listitem item = it.next();
			String itemCode = (String)item.getAttribute(DOController.ITEM_CODE);
			Intbox itemArrived = (Intbox)item.getAttribute(DOController.ITEMARRIVEDINTBOX);
			Intbox bonusArrived = (Intbox)item.getAttribute(DOController.BONUSARRIVEDINTBOX);

			//LIST ITERATOR (has many batch no)
			Iterator<Listitem> it2 = ctr.getList().getItems().iterator();
			while (it2.hasNext()){
				Listitem item2 = it2.next();
				
				MsItem msItemList = (MsItem)item2.getValue();									
				
				if (msItemList.getVItemCode().equals(itemCode)){
					Integer qty = (Integer)item2.getAttribute(DOBatchController.QTY);
					Short multiplier = (Short)item2.getAttribute(DOBatchController.MULTIPLIER);
					
					// THE IDEA IS TO DIVIDE QTY BY ITS MULTIPLIER
					// SO THE QTY WILL BE THE SAME AS THE ORIGINAL (BEFORE WE MULTIPLY IT RECORDING TO UNIT MEASUREMENT)
					
					Integer qtyFinal = qty/multiplier;
					
					totalQtyLocal += qtyFinal;
				}
			}
			if (itemArrived.getValue() + bonusArrived.getValue() != totalQtyLocal)
				return false; 
			
			totalQtyLocal = 0;
		}
		
//		Iterator<TbDeliveryOrderDetail> it = ctr.getTbDO().getTbDeliveryOrderDetails().iterator();
//
//		//Qty on List
//		Integer totalQtyLocal = 0;
//		
//		//POJO ITERATOR (tb_delivery_order_detail)
//		while (it.hasNext()){
//			TbDeliveryOrderDetail pojo = it.next();
//			
//			//LIST ITERATOR (has many batch no)
//			Iterator<Listitem> it2 = ctr.getList().getItems().iterator();
//			while (it2.hasNext()){
//				Listitem item = it2.next();
//				
//				MsItem msItemList = (MsItem)item.getValue();									
//				
//				if (msItemList.getVItemCode().equals(pojo.getMsItem().getVItemCode())){
//					Integer qty = (Integer)item.getAttribute(DOBatchController.QTY);
//					Short multiplier = (Short)item.getAttribute(DOBatchController.MULTIPLIER);
//					
//					// THE IDEA IS TO DIVIDE QTY BY ITS MULTIPLIER
//					// SO THE QTY WILL BE THE SAME AS THE ORIGINAL (BEFORE WE MULTIPLY IT RECORDING TO UNIT MEASUREMENT)
//					
//					Integer qtyFinal = qty/multiplier;
//					
//					totalQtyLocal += qtyFinal;
//				}
//			}
//			
//			if (pojo.getNDoDetQty() + pojo.getNDoBonusQty() != totalQtyLocal)
//				return false;
//			
//			totalQtyLocal = 0;
//		}
		
		return true;		
	}
	
	public void doSaveAdd(DOController ctr, TbPurchaseOrder tbPO, TbDeliveryOrder tbDO) throws VONEAppException, InterruptedException {
		
		tbPO = this.podao.getPOByCode(tbPO.getVPoCode());		
		
		tbDO.setTbPurchaseOrder(tbPO);
		//set Staff Issuer / Diterima
		tbDO.setMsStaffByNIssuerId(ctr.getUserInfoBean().getMsUser().getMsStaff());
		//set Warehouse
		tbDO.setMsWarehouse((MsWarehouse)ctr.getLocation().getSelectedItem().getValue());
		//set BPP No
		tbDO.setVDoCode(ctr.getBppNo().getText());		
		//set Tax
		tbDO.setNDoTax(ctr.getPpn().getValue().doubleValue());
		//set Tax Type
		tbDO.setVTaxType((String)ctr.getPpnType().getSelectedItem().getValue());
		//set Status
		tbDO.setVDoStatus(MedisafeConstants.PURCHASING_OPEN);
		//set RecDate
		tbDO.setDRecDate(ctr.getRecDate().getValue());
		
		//get priceAfterDiscount
		double priceAfterDiscount = 0;
		if (MedisafeConstants.RP.equals(ctr.getDiscountType().getSelectedItem().getValue())){
			priceAfterDiscount = ctr.getTotal().getValue().doubleValue() - ctr.getDiscount().getValue().doubleValue();
		}else{
			priceAfterDiscount = ctr.getTotal().getValue().doubleValue() - (ctr.getTotal().getValue().doubleValue() * ctr.getDiscount().getValue().doubleValue() / 100);
		}
		
		tbDO.setNTotal(new Double(ctr.getTotal().getValue().doubleValue()));
		tbDO.setNTotalAfterDisc(new Double(priceAfterDiscount));
		tbDO.setNTotalAfterPpn(new Double(ctr.getGtotal().getValue().doubleValue()));	
		
		//set WhoCreate & WhnCreate
		tbDO.setVWhoCreate(ctr.getUserInfoBean().getStUserId());
		tbDO.setDWhnCreate(new Date());
		
		Set<TbDeliveryOrderDetail> set = new HashSet<TbDeliveryOrderDetail>();
		tbDO.setTbDeliveryOrderDetails(set);
		
		Iterator it = ctr.getList().getItems().iterator();
		
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
			tbDOD.setVWhoCreate(ctr.getUserInfoBean().getStUserId());
			tbDOD.setDWhnCreate(new Date());
			
			set.add(tbDOD);
		}				
		
		this.dao.save(tbDO);
		
		ctr.getStatus().setValue(tbDO.getVDoStatus());
		
		Messagebox.show(MessagesService.getKey("pch.do.add.success")+ctr.getBppNo().getText());
		
	}
}

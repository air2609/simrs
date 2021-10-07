package com.vone.medisafe.service.ifaceimpl.purchasing;

import java.util.Iterator;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.JournalTrxDAO;
import com.vone.medisafe.mapping.TbAccountPayable;
import com.vone.medisafe.mapping.TbDeliveryOrder;
import com.vone.medisafe.mapping.TbDeliveryOrderDetail;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.dao.ItemInventoryDAO;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;
import com.vone.medisafe.service.iface.purchasing.ReturManager;
import com.vone.medisafe.ui.purchasing.ReturController;
import com.vone.medisafe.ui.purchasing.ReturDAO;
import com.vone.medisafe.ui.purchasing.ReturDetailController;
import com.vone.medisafe.ui.purchasing.TbDeliveryOrderDAO;

public class ReturManagerImpl implements ReturManager{

	ReturDAO dao;
	TbDeliveryOrderDAO dodao;
	ItemInventoryDAO inventorydao;
	JournalTrxDAO journaldao;

	public JournalTrxDAO getJournaldao() {
		return journaldao;
	}

	public void setJournaldao(JournalTrxDAO journaldao) {
		this.journaldao = journaldao;
	}

	public ReturDAO getDao() {
		return dao;
	}

	public void setDao(ReturDAO dao) {
		this.dao = dao;
	}
	
	public TbDeliveryOrderDAO getDodao() {
		return dodao;
	}

	public void setDodao(TbDeliveryOrderDAO dodao) {
		this.dodao = dodao;
	}
	
	public void save(ReturDetailController ctr) throws VONEAppException, InterruptedException {
						
		ctr.getWindow().detach();
		
		ReturController returCtr = (ReturController)Sessions.getCurrent().getAttribute(ReturController.returSession);
				
		Object obj = this.dodao.getDObyCode(returCtr.getDoNo().getValue());
		if (obj == null){
			Messagebox.show(MessagesService.getKey("pch.do.bpp.error"));
			return;
		}	
		
		this.dao.save(returCtr, ctr, (TbDeliveryOrder)obj);
		
		Messagebox.show(MessagesService.getKey("pch.retur.cashback.success"));
		
		redraw(returCtr);
	}
	
	public void generateAPList(ReturDetailController ctr) throws VONEAppException {
		
		ctr.getApList().getItems().clear();
		//add empty listitem
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(ctr.getApList());
		
		ReturController returCtr = (ReturController)Sessions.getCurrent().getAttribute(ReturController.returSession);
		String doNo = returCtr.getDoNo().getText();
		
		Iterator<TbAccountPayable> it = this.journaldao.getAllAp().iterator();
		
		while (it.hasNext()){
			TbAccountPayable tbAP = it.next();			
			
			String voucherNo = tbAP.getTbJournalTrx().getVVoucherNo();
			
			if (doNo.equals(voucherNo)){
				item = new Listitem();
				item.setValue(tbAP);
				item.appendChild(new Listcell(doNo+" - "+tbAP.getMsVendor().getVVendorCode()+" - "+tbAP.getTbJournalTrx().getVJournalBatchId()));
				item.setParent(ctr.getApList());
			}
		}
	}
	
	public void redraw(ReturController ctr) throws VONEAppException {
		
		ctr.getList().getItems().clear();
		
		TbDeliveryOrder tbDO = this.dodao.getDObyCode(ctr.getDoNo().getText());
		
		ctr.getPoNo().setText(tbDO.getTbPurchaseOrder().getVPoCode());
		ctr.getRecDate().setText(MedisafeUtil.convertDateToString(tbDO.getDRecDate()));
		ctr.getRecBy().setText(tbDO.getMsStaffByNIssuerId().getVStaffName());
		ctr.getApprovedBy().setText(tbDO.getMsStaffByNApproverId().getVStaffName());
		ctr.getSupCode().setText(tbDO.getTbPurchaseOrder().getMsVendor().getVVendorCode());
		ctr.getSupName().setText(tbDO.getTbPurchaseOrder().getMsVendor().getVVendorName());
		ctr.getSupAddress().setText(tbDO.getTbPurchaseOrder().getMsVendor().getVVendorAddress());
		ctr.getSupTelp().setText(tbDO.getTbPurchaseOrder().getMsVendor().getVVendorContactNo());		
		
		Iterator<TbDeliveryOrderDetail> it = tbDO.getTbDeliveryOrderDetails().iterator();
		
		int count = 0;
		
		while (it.hasNext()){
			TbDeliveryOrderDetail tbDOD = it.next();
			
			Iterator<TbBatchItem> itDetail = this.dao.getBatchItemByDOD(tbDOD).iterator();
			
			//TB_BATCH_ITEM ITERATOR
			while (itDetail.hasNext()){
				TbBatchItem tbBI = itDetail.next();
				
				//TB_ITEM_INVENTORY ITERATOR
				
				Iterator<TbItemInventory> itII = tbBI.getTbItemInventories().iterator();
				
				while (itII.hasNext()){
					TbItemInventory tbII = itII.next();
					
					Listitem item = new Listitem();
					item.appendChild(new Listcell(tbBI.getMsItem().getVItemCode()));
					item.appendChild(new Listcell(tbBI.getMsItem().getVItemName()));
					item.appendChild(new Listcell(tbBI.getVBatchNo()));
					item.appendChild(new Listcell(tbBI.getTbDeliveryOrderDetail().getTbPurchaseOrderDetail().getMsItemMeasurement().getVMitemEndQuantify()));
			
					//GET DISCOUNT PER ITEM
					Double discItem = tbDOD.getTbPurchaseOrderDetail().getNDiscount();
			
					//GET DISCOUNT TYPE PER ITEM
					String discItemType = tbDOD.getTbPurchaseOrderDetail().getVDiscountType();
					//GET DISCOUNT TOTAL
					Double discTotal = tbDOD.getTbPurchaseOrderDetail().getTbPurchaseOrder().getNDiscount();
			
					//GET DISCOUNT TYPE TOTAL
					String discTotalType = tbDOD.getTbPurchaseOrderDetail().getTbPurchaseOrder().getVDiscountType();
					//GET TAX
					Double tax = tbDOD.getTbDeliveryOrder().getNDoTax();
					//GET ITEM COST
					Double itemCost = tbBI.getTbDeliveryOrderDetail().getTbPurchaseOrderDetail().getNPoDetCost();
			
					//GET QTY
					Short qty = tbBI.getNBatchItemQty();
					//GET QTY WITHOUT BONUS
					int qtyNoBonus = qty - new Integer(tbBI.getTbDeliveryOrderDetail().getTbPurchaseOrderDetail().getNBonus());

					//USE QTY WITHOUT BONUS, COZ IT'S MORE ACCURATE 				
					
					//GET PRICE AFTER ITEM DISCOUNT				
					Double priceAfterItemDisc;
					if (discItemType.equals(MedisafeConstants.RP))
						priceAfterItemDisc = itemCost - discItem;
					else
						priceAfterItemDisc = itemCost - itemCost * discItem / 100;				
									
					//GET PRICE AFTER TOTAL DISCOUNT
					Double priceAfterTotalDisc = priceAfterItemDisc;
					if (!discTotalType.equals(MedisafeConstants.RP))
						priceAfterTotalDisc = priceAfterItemDisc - priceAfterItemDisc * discTotal / 100;
					
					
					//GET PRICE AFTER TAX (SUBTOTAL)
//					Double priceAfterTax = priceAfterTotalDisc + priceAfterTotalDisc * tax / 100; 
					
					//GET QTY AVAILABLE ON ALL INVENTORY
//					Double qtyInventory = this.inventorydao.getQtyByBatchNo(tbBI.getVBatchNo());
					
					item.appendChild(new Listcell(""+itemCost));
					item.appendChild(new Listcell(""+qty));												
					item.appendChild(new Listcell(""+(itemCost  - priceAfterTotalDisc)));
					item.appendChild(new Listcell(""+tax));
//					item.appendChild(new Listcell(""+priceAfterTax));
					item.appendChild(new Listcell(""+tbBI.getNCogsPrice()));
					item.appendChild(new Listcell(""+tbII.getMsWarehouse().getVWhouseCode()));
					item.appendChild(new Listcell(""+tbII.getNItemInvQty()));					
							
					Listcell cell = new Listcell();
					final Intbox box = new Intbox();			
					box.setStyle("background-color:"+MedisafeConstants.COLOR_INPUT+";width:90%");			
					cell.appendChild(box);
					item.appendChild(cell);
					
					item.setParent(ctr.getList());
					
					item.setValue(tbBI);
					item.setAttribute(ReturController.itemName, tbBI.getMsItem().getVItemCode() + "-" + tbBI.getMsItem().getVItemName());
					item.setAttribute(ReturController.batchNo, tbBI.getVBatchNo());
					item.setAttribute(ReturController.qtyReturn, box);
//					item.setAttribute(ReturController.totalPrice, priceAfterTax);
					item.setAttribute(ReturController.totalPrice, tbBI.getNCogsPrice() );
					item.setAttribute(ReturController.qtyInventory, tbII.getNItemInvQty());
					item.setAttribute(ReturController.tbInventory, tbII);
				
					
					if (count == 0)
						box.focus();
					
					count++;
					
					//add Listener
					item.addEventListener(Events.ON_CLICK, new EventListener(){

						public boolean isAsap() {
							// TODO Auto-generated method stub
							return true;
						}

						public void onEvent(Event arg0) {
							// TODO Auto-generated method stub
							box.select();
						}
						
					});
					
				}
				
			}
		}
	}
	
	public void doSearch(ReturController ctr) throws VONEAppException {
		
		ctr.getDoList().getItems().clear();
		
		String doNo = ctr.getDoNoSearch().getText();
		String batchNo = ctr.getBatchNoSearch().getText();
		
		Iterator<TbDeliveryOrder> it = this.dao.searchDOandBatch(doNo, batchNo).iterator();
			
		while (it.hasNext()){
			TbDeliveryOrder tbDO = it.next();
			
			Listitem item = new Listitem();
			
			item.appendChild(new Listcell(tbDO.getVDoCode()));
			
			item.appendChild(new Listcell(tbDO.getTbPurchaseOrder().getMsVendor().getVVendorName()));
			
			item.setParent(ctr.getDoList());
		}	
			
	}

	public ItemInventoryDAO getInventorydao() {
		return inventorydao;
	}

	public void setInventorydao(ItemInventoryDAO inventorydao) {
		this.inventorydao = inventorydao;
	}
}

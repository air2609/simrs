package com.vone.medisafe.ui.purchasing;

import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.TbDeliveryOrder;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.PurchaseServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class DOBatchController extends BaseController {
	
	Textbox doNumber = null;
	Listbox itemList = null;
	Textbox batchNo = null;	
	Listbox endM = null;
	Textbox initQty = null;
	Textbox initM = null;
	Intbox qty = null;
	Datebox expiredDate = null;
	Listbox list = null;
	
	
	
	TbDeliveryOrder tbDO = null;
	
	ZulConstraint cst = new ZulConstraint();
	
	//Local Session Attribute
	public static final String DO_NO = "DO_NO";
	public static final String BATCH_NO = "BATCH_NO";
	public static final String QTY = "QTY";
	public static final String EXP_DATE = "EXP_DATE";
	public static final String FINAL_M = "FINAL_MEASUREMENT";
	public static final String MULTIPLIER = "MULTIPLIER";
	
	//List Item Session Attribute
	public static final String INITQTY = "INIT_QTY";
	public static final String INITM = "INIT_M";
	
	//ENDMEASUREMENT ATTRIBUTE
	public static final String VALUE = "VALUE";
		

	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		doNumber = (Textbox)cmp.getFellow("doNumber");
		itemList = (Listbox)cmp.getFellow("itemList");
		batchNo = (Textbox)cmp.getFellow("batchNo");
		endM = (Listbox)cmp.getFellow("endM");
		initQty = (Textbox)cmp.getFellow("initQty");
		initM = (Textbox)cmp.getFellow("initM");
		qty = (Intbox)cmp.getFellow("qty");
		expiredDate = (Datebox)cmp.getFellow("expiredDate");
		list = (Listbox)cmp.getFellow("list");
		
		cst.registerComponent(batchNo, ZulConstraint.NO_EMPTY);
		cst.registerComponent(qty, ZulConstraint.NO_EMPTY);				
		cst.registerComponent(expiredDate, ZulConstraint.NO_EMPTY);
		cst.registerComponent(batchNo, ZulConstraint.UPPER_CASE);
		cst.validateComponent(false);
		
		this.itemList.focus();
		
		TbDeliveryOrder tbDONew = (TbDeliveryOrder)super.getCurrentSession().getAttribute(MedisafeConstants.DO_SESSION);
				
		if (tbDONew == null){
			list.getItems().clear();
			doNumber.setText("");
			itemList.getItems().clear();
			batchNo.setText("");
			initQty.setText("");
			initM.setText("");
			endM.getItems().clear();
			qty.setValue(null);
			expiredDate.setValue(null);	
			btnSave.setDisabled(true);
			btnDelete.setDisabled(true);
		}else{		
			if (tbDO == null || !tbDONew.getVDoCode().equals(tbDO.getVDoCode())){
				tbDO = tbDONew;
				redraw();		
				btnSave.setDisabled(false);
				btnDelete.setDisabled(false);
			}			
		}				
				
		this.itemList.addEventListener("onSelect", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redrawStatus();
				} catch (VONEAppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}
	
	public TbDeliveryOrder getTbDO() {
		return tbDO;
	}

	public void setTbDO(TbDeliveryOrder tbDO) {
		this.tbDO = tbDO;
	}

	private void redraw() throws InterruptedException, VONEAppException{	

		PurchaseServiceLocator.getDOManager().redraw(this);
			
		if (this.itemList.getItemCount() > 0)
			redrawStatus();
	}
	
	private void redrawStatus() throws VONEAppException {
		
		this.initQty.setValue(((Integer)itemList.getSelectedItem().getAttribute(this.INITQTY)).toString());
		
		this.initM.setText(itemList.getSelectedItem().getAttribute(this.INITM).toString());
		
		//REDRAW MEASUREMENT VALUE
		this.endM.getItems().clear();
		
		Iterator<MsItemMeasurement> it = MasterServiceLocator.getItemMeasurementManager().getMsItemMeasurementListByCode(this.initM.getValue()).iterator();
		
		while (it.hasNext()){
			MsItemMeasurement mim = it.next();
			
			Listitem item = new Listitem();
			item.appendChild(new Listcell(mim.getVMitemEndQuantify() + " ("+mim.getNMitemEndQty()+"x)"));
			item.setValue(mim.getVMitemEndQuantify());
			item.setAttribute(this.VALUE, mim.getNMitemEndQty());
			
			item.setParent(endM);
		}
		
				
		if(this.endM.getItemCount() == 0){
			Listitem item = new Listitem();
			item.appendChild(new Listcell(initM.getText() + " (1x)"));
			item.setValue(initM.getText());
			item.setAttribute(this.VALUE, (short)1);
			
			item.setParent(endM);
		}
		
		if (this.endM.getItemCount() > 0)
			this.endM.setSelectedIndex(0);
	}
	
	@Override
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
	}

	@Override
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	@Override
	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (list.getSelectedItem() == null){			
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			
			return;
		}	
		
		super.doDelete(cmp);
		
		this.list.removeChild(this.list.getSelectedItem());
	}

	@Override
	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!cst.validateComponent(true))
			return;
		
		//END MEASUREMENT SHOULD EXIST
		if (this.endM.getItemCount() < 1)
			return;
		
		if (!PurchaseServiceLocator.getDOManager().doSave(this))
			return;
		
		super.doSave(cmp);
	}

	@Override
	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
						
		//GET MEASUREMENT MULTIPLIERS FROM ENDM LISTBOX
		Short vMultiplier = ((Short)this.endM.getSelectedItem().getAttribute(this.VALUE));
		
		//PUT INTO ATTRIBUTE
		Listitem item = new Listitem();
		item.setValue(this.itemList.getSelectedItem().getValue());
		item.setAttribute(this.DO_NO, this.doNumber.getText());
		item.setAttribute(this.BATCH_NO, this.batchNo.getText());
		item.setAttribute(this.QTY, this.qty.getValue() * vMultiplier);		
		item.setAttribute(this.EXP_DATE, this.expiredDate.getValue());
		item.setAttribute(this.FINAL_M, this.endM.getSelectedItem().getValue());
		item.setAttribute(this.MULTIPLIER, vMultiplier);
		item.setParent(this.list);
		
		//set facial 
		//PUT INTO LIST (MAKE OVER)
		MsItem selectedItem = (MsItem)this.itemList.getSelectedItem().getValue();
		item.appendChild(new Listcell(selectedItem.getVItemCode()));
		item.appendChild(new Listcell(selectedItem.getVItemName()));
		item.appendChild(new Listcell(this.batchNo.getValue()));
		item.appendChild(new Listcell(this.endM.getSelectedItem().getValue().toString()));
		item.appendChild(new Listcell("" + this.qty.getValue() * vMultiplier));		
		item.appendChild(new Listcell(MedisafeUtil.convertDateToString(this.expiredDate.getValue())));
		
		this.batchNo.select();
	}

	@Override
	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
	}

	public boolean isAllItemRegistered() throws InterruptedException, VONEAppException{
		
		return PurchaseServiceLocator.getDOManager().isAllItemRegistered(this);
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	public Textbox getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Textbox batchNo) {
		this.batchNo = batchNo;
	}

	public ZulConstraint getCst() {
		return cst;
	}

	public void setCst(ZulConstraint cst) {
		this.cst = cst;
	}

	public Textbox getDoNumber() {
		return doNumber;
	}

	public void setDoNumber(Textbox doNumber) {
		this.doNumber = doNumber;
	}

	public Datebox getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Datebox expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Listbox getItemList() {
		return itemList;
	}

	public void setItemList(Listbox itemList) {
		this.itemList = itemList;
	}

	public Intbox getQty() {
		return qty;
	}

	public void setQty(Intbox qty) {
		this.qty = qty;
	}
}

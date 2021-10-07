package com.vone.medisafe.ui.purchasing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
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
import com.vone.medisafe.common.util.IdsManager;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.PurchaseServiceLocator;

public class PORController extends PurchaseController {
	
	Logger logger = Logger.getLogger(PORController.class);
	
	Listbox location = null;
	
	Bandbox oppNo = null;
	Bandbox supCode;
	Textbox searchSupCode;
	Textbox searchSupName;
	Listbox listSupplier;
	Textbox supAddress;
	Textbox supTelp;
	Textbox searchOpp = null;
	Listbox listOpp = null;
	
	Textbox issuedBy = null;
	Listbox list = null;		
	
	Session session = null;
	
	Button btnAdd = null;
	Button btnRevoke = null;
	Button btnNew = null;
	
	Label status = null;
	
	// shall be kept null at initial!!!
	// purpose : get Measurement List once.
	List listMeasurement = null;
	
	
	
	public List getListMeasurement() {
		return listMeasurement;
	}

	private String generateSQ() throws VONEAppException {
		
		IdsManager idsManager =  IdsServiceLocator.getIdsManager();
		Integer seq = idsManager.getSequence(MedisafeConstants.PURCHASE_ORDER_REQUEST_CODE);
		
		Date date = new Date();
		StringBuffer stSQ = new StringBuffer();
		stSQ.append(new SimpleDateFormat("dd").format(date))
		.append(seq)
		.append("/IF/RSTS/")
		.append(MedisafeUtil.convertToMonthName(MedisafeUtil.convertDateToString(date, "MM")))
		.append("/")
		.append(MedisafeUtil.convertDateToString(date,"yyyy"));
//		.append(""+seq.intValue());
		
		return stSQ.toString();
	}
	
	
	
	public void doSearch(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub		
						
		PurchaseServiceLocator.getPORManager().doSearchPORController("%"+this.searchOpp.getText()+"%", listOpp);
		
	}
	
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		//set common list
		setList(this.list);
		
		//init
		location = (Listbox)cmp.getFellow("location");
		oppNo = (Bandbox)cmp.getFellow("oppNo");
		issuedBy = (Textbox)cmp.getFellow("issuedBy");
		list = (Listbox)cmp.getFellow("list");
		
		searchOpp = (Textbox)cmp.getFellow("searchOpp");
		listOpp = (Listbox)cmp.getFellow("listOpp");
		
		supCode = (Bandbox)cmp.getFellow("supCode");
		searchSupCode = (Textbox)cmp.getFellow("searchSupCode");
		searchSupName = (Textbox)cmp.getFellow("searchSupName");
		listSupplier = (Listbox)cmp.getFellow("listSupplier");
		supAddress = (Textbox)cmp.getFellow("supAddress");
		supTelp = (Textbox)cmp.getFellow("supTelp");
		
		ZulConstraint cst = new ZulConstraint();
		cst.registerComponent(searchSupCode, ZulConstraint.UPPER_CASE);
		cst.registerComponent(searchSupName, ZulConstraint.UPPER_CASE);
		
		this.btnAdd = (Button)cmp.getFellow("btnAdd");
		this.btnRevoke = (Button)cmp.getFellow("btnRevoke");
		this.btnNew = (Button)cmp.getFellow("btnNew");
		
		this.status = (Label)cmp.getFellow("status");
		
		//fill location
		List listLoc = super.getUserInfoBean().getMsUnitByScreenCode(ScreenConstant.ORDER_PERMINTAAN_PEMBELIAN);
		
		Iterator it = listLoc.iterator();
		
		location.getItems().clear();		
		
		while (it.hasNext()){
			MsUnit unitPojo = (MsUnit)it.next();
			
			Listitem item = new Listitem();
			item.setParent(location);
			
			item.appendChild(new Listcell(unitPojo.getVUnitCode()+" - "+unitPojo.getVUnitName()));
			item.setValue(unitPojo);
		}
		if (location.getItemCount() > 0)
			location.setSelectedIndex(0);
		
		//fill oppNo
		
		//not yet
		
		//fill issuedBy
		issuedBy.setText(super.getUserInfoBean().getStUserName());
		issuedBy.setDisabled(true);
		
		//fill session
		session = Sessions.getCurrent();
		session.setAttribute(MedisafeConstants.PURCHASING_SESSION, this);
		
		//draw list
		//comment by arif;
		//redraw();
		
		location.addEventListener("onSelect", new EventListener(){

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
					
				}
			}
			
		});
		
		this.status.setValue("STATUS :");
		
		//SETFOCUS
		
		if (list.getItemCount() > 0){
			Listitem item = list.getItemAtIndex(0);
			
			Listcell cell = (Listcell)item.getChildren().get(6);
			((Intbox)cell.getChildren().get(0)).focus();
		}
		
		setInitButton();
				
	}	
	
	public void doNew(Component cmp) throws InterruptedException, VONEAppException {
		
		setInitButton();
		this.oppNo.setText("");
		this.status.setValue("STATUS :");
		redraw();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		throw new VONEAppException("error");
//		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (list.getSelectedCount() < 1)
			return;
		
		list.removeChild(list.getSelectedItem());
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
				
		
		setModifyButton();				
	}
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		setAfterSaveButton();
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!StringUtils.hasValue(this.oppNo.getText())){
			Messagebox.show(MessagesService.getKey("pch.order.request.no.opp"));
			return;
		}
		
		super.doSaveModify(cmp);
		
		boolean result = PurchaseServiceLocator.getPORManager().doSaveModifyPORController(this.oppNo, this.status, this.list, super.getUserInfoBean());
				
		if (!result){
			setErrorButton();
			return;
		}
		
		setAfterSaveButton();
	}

	public void doRevoke(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		if (!StringUtils.hasValue(this.oppNo.getText())){
			Messagebox.show(MessagesService.getKey("pch.order.request.no.opp"));
			return;
		}
		
		TbPurchaseRequest pojo = PurchaseServiceLocator.getPORManager().getTbPurchaseRequestByCode(this.oppNo.getText());
		
		if (pojo == null){
			Messagebox.show(MessagesService.getKey("pch.order.request.no.opp"));
			setErrorButton();
			return;
		}
		
		if (!pojo.getVPrStatus().equals(MedisafeConstants.PURCHASING_OPEN)){
			Messagebox.show(MessagesService.getKey("pch.order.request.modify.status.invalid"));
			this.status.setValue("STATUS : "+pojo.getVPrStatus());
			setErrorButton();
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("pch.order.request.revoke.question"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		pojo.setVPrStatus(MedisafeConstants.PURCHASING_REVOKED);
		PurchaseServiceLocator.getPORManager().updatePojoOnly(pojo);
		
		setErrorButton();
		
		this.status.setValue("STATUS : "+pojo.getVPrStatus());

		Messagebox.show(MessagesService.getKey("pch.order.request.revoke.success"));
		
		this.listOpp.getItems().clear();		
	}	

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub

		if (this.list.getItemCount() < 1){
			Messagebox.show(MessagesService.getKey("pch.order.request.noitem"));
			
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("pch.order.request.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;	

		super.doSaveAdd(cmp);
		
		String sq = generateSQ();
		
		PurchaseServiceLocator.getPORManager().doSaveAddPORController(this.list, this.oppNo, this.status, this.location, sq, super.getUserInfoBean());				
				
		setAfterSaveButton();
		
	}
	
	public void setListMeasurement() throws VONEAppException {
		if (this.listMeasurement != null)
			return;

		this.listMeasurement = MasterServiceLocator.getItemMeasurementManager()
				.getMeasurementType();

		if (this.listMeasurement == null)
			this.listMeasurement = new ArrayList();
	}

	/**
	 * inject ItemMeasurement Data to listM
	 * @param listM
	 * @throws VONEAppException
	 */
	public void setListboxMeasurement(Listbox listM) throws VONEAppException {
		if (listM == null)
			listM = new Listbox();

		if (this.listMeasurement == null)
			setListMeasurement();

		Iterator it = this.listMeasurement.iterator();

		while (it.hasNext()) {
			Listitem item = new Listitem();
			item.setParent(listM);

			String value = (String)it.next();

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

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Intbox iResult = isValidInput();
		boolean invalid = false;
		
		if (iResult != null){
			Messagebox.show(MessagesService.getKey("pch.order.request.invalid.input"));
			
			iResult.select();
			
			return;
		}
		
		if(supCode.getText().equals("")){
			Messagebox.show("Isi Data Supplier Terlebih Dahulu...!");
			return;
		}
		List<Listitem> items = this.list.getItems();
		Integer stock = null;
		Integer buffer = null;
		Integer maxOrder = null;
		Listcell cell = null;
		Intbox jmlhOrder = null;
		for(Listitem item : items){
			cell = (Listcell)item.getChildren().get(3);
			stock = new Integer(cell.getLabel());
			cell = (Listcell)item.getChildren().get(4);
			buffer = new Integer(cell.getLabel());
			cell = (Listcell)item.getChildren().get(5);
			maxOrder = new Integer(cell.getLabel());
			cell = (Listcell)item.getChildren().get(7);
			if (cell.getChildren().get(0) instanceof Intbox)
				jmlhOrder = (Intbox)cell.getChildren().get(0);
			if((stock.intValue() >= buffer.intValue()) || (jmlhOrder.getValue().intValue() > maxOrder.intValue()))
				invalid = true;
		}
		if(invalid){
			Messagebox.show("Gagal menyimpan, Stock diatas Buffer atau Pemesanan diatas Max Order", 
					"Informasi", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		super.doSave(cmp);						
	}

	private void redraw() throws VONEAppException{
		
		list.getItems().clear();
		
		//get WarehouseId
		if (location.getSelectedItem() == null){
			setErrorButton();
			return;
		}
		
		PurchaseServiceLocator.getPORManager().redrawPORController(this);

		setInitButton();
	}
	
	
	public void redrawSearch() throws VONEAppException, InterruptedException {						
		
		TbPurchaseRequest pojo = null;
		
		try {
			pojo = (TbPurchaseRequest)this.listOpp.getSelectedItem().getValue();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block			
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.oppNo.select();	
		}
		
		if (pojo == null) return;		
		
		PurchaseServiceLocator.getPORManager().redrawSearchPORController(pojo,this);

		setAfterSaveButton();
	}
	
	
			
	public void addItem(MsItem itemPojo) throws VONEAppException{
		
		super.addItem(itemPojo);
		
		PurchaseServiceLocator.getPORManager().addItem(itemPojo, list, this);
	}
	
	public void addItemRequest(Object[] itemPojo) throws VONEAppException{
		super.addItemRequest(itemPojo);
		
		PurchaseServiceLocator.getPORManager().addItemRequest(itemPojo, list, this);
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}
	
	public void setInitButton(){

		this.btnAdd.setDisabled(false);
		this.btnNew.setDisabled(false);
		this.btnDelete.setDisabled(false);
		this.btnRevoke.setDisabled(true);
		this.btnSave.setDisabled(false);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(true);
		this.list.setDisabled(false);
		this.oppNo.setDisabled(false);
	}
	
	public void searchSupplier() throws WrongValueException, VONEAppException{
		List supps = MasterServiceLocator.getVendorManager().searchVendor("%"+searchSupCode.getText()+"%", "%"+searchSupName.getText()+"%");
		Iterator it = supps.iterator();
		listSupplier.getItems().clear();
		Listitem item;
		while(it.hasNext()){
			MsVendor sup = (MsVendor)it.next();
			item = new Listitem();
			item.setValue(sup);
			item.setParent(listSupplier);
			
			item.appendChild(new Listcell(sup.getVVendorCode()));
			item.appendChild(new Listcell(sup.getVVendorName()));
		}
	}
	
	public void getSupplier(){
		MsVendor sup = (MsVendor)listSupplier.getSelectedItem().getValue();
		supCode.setValue(sup.getVVendorCode()+"-"+sup.getVVendorName());
		this.list.setAttribute("supplier", sup);
		supAddress.setValue(sup.getVVendorAddress());
		supTelp.setValue(sup.getVVendorContactNo());
	}
	
	public void setAfterSaveButton(){

		this.btnAdd.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnDelete.setDisabled(true);
		this.btnRevoke.setDisabled(false);
		this.btnSave.setDisabled(true);
		this.btnModify.setDisabled(false);
		this.btnCancel.setDisabled(true);
		this.oppNo.setDisabled(true);
		
		this.list.setDisabled(true);
	}
	
	public void setModifyButton(){
		this.btnAdd.setDisabled(false);
		this.btnNew.setDisabled(true);
		this.btnDelete.setDisabled(false);
		this.btnRevoke.setDisabled(true);
		this.btnSave.setDisabled(false);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(false);
		this.list.setDisabled(false);		
	}
	
	public void setErrorButton(){
		this.btnAdd.setDisabled(true);
		this.btnNew.setDisabled(false);
		this.btnDelete.setDisabled(true);
		this.btnRevoke.setDisabled(true);
		this.btnSave.setDisabled(true);
		this.btnModify.setDisabled(true);
		this.btnCancel.setDisabled(true);
		this.list.setDisabled(true);			
		this.oppNo.setDisabled(true);
	}
	
	private Intbox isValidInput(){
		
		Iterator it = list.getItems().iterator();
		
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
						
			Listcell cell = (Listcell)item.getChildren().get(7);
			Intbox qty = (Intbox)cell.getChildren().get(0);
			
			if (qty.getValue() == null || qty.getValue().intValue() < 1 || qty.getValue().shortValue() > Short.MAX_VALUE){
				qty.select();
				return qty;
			}
		}
		
		return null;
	}

	public Textbox getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(Textbox issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Listbox getListOpp() {
		return listOpp;
	}

	public void setListOpp(Listbox listOpp) {
		this.listOpp = listOpp;
	}

	public Listbox getLocation() {
		return location;
	}

	public void setLocation(Listbox location) {
		this.location = location;
	}

	public Bandbox getOppNo() {
		return oppNo;
	}

	public void setOppNo(Bandbox oppNo) {
		this.oppNo = oppNo;
	}

	public Textbox getSearchOpp() {
		return searchOpp;
	}

	public void setSearchOpp(Textbox searchOpp) {
		this.searchOpp = searchOpp;
	}

	public Label getStatus() {
		return status;
	}

	public void setStatus(Label status) {
		this.status = status;
	}

	public void setListMeasurement(List listMeasurement) {
		this.listMeasurement = listMeasurement;
	}
	
}
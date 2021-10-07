package com.vone.medisafe.apotik;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
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

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.report.NoteReport;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

/**
 * ReturObatController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 21 2006
 * @category user interface (visual and controller - VC)
 */

public class ReturObatController extends BaseController{
	
	public MsUser user;
	public TbReturPharmacyTrx apotikReturn;
	public TbReturPharmacyDetailTrx returDetil;
	public TbItemTrx itemTrx;
	public boolean isRanap;
	public MsPatient patient;
	public boolean isUpdate;

	public double hargaSatuan;

	Listbox locationList;
	Bandbox MRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox patientList;
	Bandbox returnNoteNumber;
	Textbox searchReturnNota;
	Textbox searchReturnName;
	Listbox returnList;
	Bandbox transactionNumber;
	Textbox searchNota;
	Textbox searchName;
	Listbox notaList;
	Bandbox receiptNumber;
	Textbox searchRecieptNo;
	Listbox recieptList;
	Textbox patientName;
	Textbox registrationNumber;
	Textbox address;
	Label labelStatus;
	Label statusNota;
	Listbox pharmacyList;
	Decimalbox total;
	
	
	//button
	Button btnSave;
	Button btnModify;
	Button btnNew;
	Button btnCancelNote;
	Button btnValidation;
	Button btnCetak;
	//Button btnEnd;
	
	Listitem item;
	Listcell cell;
	Session session;
	
	private ApotikManager apServ = Service.getApotikManager();
	
	UserManager userService = ServiceLocator.getUserManager();
	NoteManager noteService = Service.getNotaManager();
	
	ZulConstraint constraints = new ZulConstraint();
		
	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		locationList = (Listbox)win.getFellow("locationList");
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		patientList = (Listbox)win.getFellow("patientList");
		returnNoteNumber = (Bandbox)win.getFellow("returnNoteNumber");
		searchReturnNota = (Textbox)win.getFellow("searchReturnNota");
		searchReturnName = (Textbox)win.getFellow("searchReturnName");
		returnList = (Listbox)win.getFellow("returnList");
		transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		searchNota = (Textbox)win.getFellow("searchNota");
		searchName = (Textbox)win.getFellow("searchName");
		notaList = (Listbox)win.getFellow("notaList");
		receiptNumber = (Bandbox)win.getFellow("receiptNumber");
		searchRecieptNo = (Textbox)win.getFellow("searchRecieptNo");
		recieptList = (Listbox)win.getFellow("recieptList");
		patientName = (Textbox)win.getFellow("patientName");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		address = (Textbox)win.getFellow("address");
		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		pharmacyList = (Listbox)win.getFellow("pharmacyList");
		total = (Decimalbox)win.getFellow("total");
		
		//button
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
	//	btnEnd = (Button)win.getFellow("btnEnd");
		
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchRecieptNo, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchReturnName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchReturnNota,ZulConstraint.UPPER_CASE);
		
		session = win.getDesktop().getSession(); 

		user = getUserInfoBean().getMsUser();
		locationList.getItems().clear();
		pharmacyList.getItems().clear();
		returnList.getItems().clear();
		
		userService.getUnitUser(user, locationList);
		
				
		setButtonDisable(true);
		btnSave.setDisabled(false);
		btnNew.setDisabled(false);
		
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		patientName.setReadonly(true);	
		registrationNumber.setReadonly(true);
		address.setReadonly(true);
		
		MRNumber.focus();
		
		
		
	}
	
		
	
	public void searchNote() throws VONEAppException{
					
			notaList.getItems().clear();
			
			statusNota.setVisible(false);
			labelStatus.setVisible(false);
			
			MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
			
			noteService.searchNote("%"+searchNota.getValue()+"%", "%"+searchName.getValue()+"%",
					unit, notaList, MedisafeConstants.VALIDATED_NOTE);
			
			
			MiscTrxController.setFont(notaList);
	}
	
	
	public void getNoteDetail(int type) throws VONEAppException{
		
		apServ.getReturNoteDetil(this,type);
						
		isRanap = false;
		receiptNumber.setDisabled(true);
		MiscTrxController.setFont(pharmacyList);
	}
	
	
	public void save() throws InterruptedException,VONEAppException
	{
		
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}
		
		if((patientName.getText().trim().equals("")) || (pharmacyList.getItems().size() == 0)){
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		
		if(isRanap){
			returPatientInventory();
			return;
		}
		
		List itemTrxs = pharmacyList.getItems();
		
		Intbox itemBack;
		Date tanggal = new Date();
		
//		double nilaiTransaksi = 0;
//		double hargaSatuan = 0;
		Set<TbReturPharmacyDetailTrx> detil = new HashSet<TbReturPharmacyDetailTrx>();
		Set<TbItemTrx> itemsTrx = new HashSet<TbItemTrx>();
				
		Iterator it = itemTrxs.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			itemTrx = (TbItemTrx)item.getValue();
			hargaSatuan = itemTrx.getNAmountAfterDisc()/itemTrx.getNQty();
			
			cell = (Listcell)item.getChildren().get(6);
			itemBack = (Intbox)cell.getChildren().get(0);
			if(itemBack.getValue() != null){
//				nilaiTransaksi = nilaiTransaksi + (hargaSatuan * itemBack.intValue());
				
				cell = (Listcell)item.getChildren().get(4);
				
				returDetil = new TbReturPharmacyDetailTrx();
				returDetil.setNTotalQty(new Short(cell.getLabel()));
				returDetil.setVWhoCreate(user.getVUserName());
				returDetil.setMsItem(itemTrx.getMsItem());
				returDetil.setNQty(itemBack.getValue().shortValue());
				returDetil.setDWhnCreate(tanggal);
				returDetil.setTbBatchItem(itemTrx.getTbBatchItem());
				returDetil.setNValue((hargaSatuan * itemBack.intValue()));
				itemTrx.setNQty(new Float(itemBack.getValue()));
				detil.add(returDetil);
				itemsTrx.add(itemTrx);
			}
			
		}
		
		apotikReturn = new TbReturPharmacyTrx();
		apotikReturn.setVWhoCreate(user.getVUserName());
		apotikReturn.setTbExamination((TbExamination)transactionNumber.getAttribute("nota"));
		apotikReturn.setNTrxValue(total.getValue().doubleValue());
		apotikReturn.setNStatus((short)MedisafeConstants.ACTIVE_NOTE);
		apotikReturn.setNPaymentStatus(MedisafeConstants.BELUM_LUNAS);
		apotikReturn.setMsPatient(patient);
		apotikReturn.setDWhnCreate(tanggal);
		
		Integer nomorNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RETUR);
		String codeRetur = MedisafeUtil.generateNotaNumber(nomorNota, tanggal,((MsUnit)locationList.
							getSelectedItem().getValue()).getVUnitCode(), MedisafeConstants.RETUR_ITEM);
		apotikReturn.setVReturCode(codeRetur);
		
//		save (apotikReturn, detil)
		
		int result = Messagebox.show(MessagesService.getKey("common.add.confirm"), "KONFIRMASI", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
	
		if(result == Messagebox.YES){
			if(apServ.saveItemRetur(apotikReturn, detil,null,itemsTrx,(MsUnit)locationList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				this.returnNoteNumber.setValue(apotikReturn.getVReturCode());
				this.returnNoteNumber.setAttribute("retur",apotikReturn);
				
				this.labelStatus.setVisible(true);
				this.statusNota.setVisible(true);
				statusNota.setValue(MedisafeUtil.getNoteStatus(apotikReturn.getNStatus()));
				
				setButtonDisable(false);
				btnSave.setDisabled(true);
				
	//			List<TbReturPharmacyDetailTrx> detils = apServ.getReturDetil(apotikReturn);
				
				apServ.getReturDetil(this, apotikReturn.getNReturId());
				
	//			for()
				
			}else{
				Messagebox.show(MessagesService.getKey("common.save.fail"));
		}
		
		isRanap = false;
		MiscTrxController.setFont(pharmacyList);
		}
	}
	
	public void setButtonDisable(boolean isDisable){
		
		btnModify.setDisabled(isDisable);
		btnNew.setDisabled(isDisable);
		btnCancelNote.setDisabled(isDisable);
		btnValidation.setDisabled(isDisable);
		btnCetak.setDisabled(isDisable);
//		btnEnd.setDisabled(isDisable);
		btnSave.setDisabled(isDisable);
		
	}
	
	
	public void getPatientItems(int type) throws VONEAppException, InterruptedException{
		
		apServ.getPatientItems(this,type);
		
		this.transactionNumber.setDisabled(true);		
		this.receiptNumber.setDisabled(true);
	//	this.transactionNumber.setDisabled(false);
		MiscTrxController.setFont(pharmacyList);
		isRanap=true;
		
	}
	
	
	public void returPatientInventory() throws VONEAppException, InterruptedException{
		
		int result = Messagebox.show(MessagesService.getKey("common.add.confirm"), "KONFIRMASI", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			apServ.saveReturPatientInventory(this);
			apServ.getReturDetil(this, apotikReturn.getNReturId());
			
		}
	}
	
	
	public void searchReceipt() throws VONEAppException
	{
		List<TbExamination> notes = apServ.getNoteBaseOnReceiptNumber("%"+searchRecieptNo.getText()+"%");
		recieptList.getItems().clear();
		
		for(TbExamination nota: notes){
			item = new Listitem();
			item.setValue(nota);
			item.setParent(recieptList);
			
			cell = new Listcell(nota.getVRecipeNo());
			cell.setParent(item);
			
			cell = new Listcell(nota.getVNoteNo());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
			cell.setParent(item);
		}
		statusNota.setVisible(false);
		labelStatus.setVisible(false);
		
		MiscTrxController.setFont(recieptList);
	}
	
	
	
	public void searchReturNote(Datebox startDate, Datebox endDate) throws VONEAppException{
		
		apServ.searchReturItems("%"+searchReturnNota.getText()+"%", "%"+searchReturnName.getText()+"%", 
				returnList, startDate, endDate);
		MiscTrxController.setFont(returnList);
		
	}
	
	public void getReturDetil() throws VONEAppException, InterruptedException
	{
		
		
		
		TbReturPharmacyTrx retur = (TbReturPharmacyTrx)returnList.getSelectedItem().getValue();
		
		apServ.getReturDetil(this, retur.getNReturId());
		
		setButtonDisable(false);
		this.btnSave.setDisabled(true);
		MiscTrxController.setFont(pharmacyList);
	}
	
	
	public void createNew() throws VONEAppException{
		locationList.setSelectedIndex(0);
		MRNumber.setValue(null);
		crNoMR.setValue(null);
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		patientList.getItems().clear();
		returnNoteNumber.setValue(null);
		searchReturnNota.setValue(null);
		searchReturnName.setValue(null);
		returnList.getItems().clear();
		transactionNumber.setValue(null);
		searchNota.setValue(null);
		searchName.setValue(null);
		notaList.getItems().clear();
		receiptNumber.setValue(null);
		searchRecieptNo.setValue(null);
		recieptList.getItems().clear();
		patientName.setValue(null);
		registrationNumber.setValue(null);
		address.setValue(null);
		labelStatus.setVisible(false);
		statusNota.setVisible(false);
		pharmacyList.getItems().clear();
		this.total.setValue(new BigDecimal(0));
		
		MRNumber.focus();
		
		setButtonDisable(true);
		btnSave.setDisabled(false);
		btnNew.setDisabled(false);
		this.receiptNumber.setDisabled(false);
		this.transactionNumber.setDisabled(false);
		this.registrationNumber.setReadonly(true);
		
	}
	
	public void cetak() throws VONEAppException, InterruptedException, IOException{
		NoteReport nr = new NoteReport(apotikReturn);
		
		if(!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU..!!");
		}
	}
	
	
	
	public void modify() throws VONEAppException{
		
		List<Listitem> items = pharmacyList.getItems();
		
		
		Intbox jumlah = null;
		Listcell cell = null;
		
				
		for(Listitem item : items)
		{
			cell = (Listcell)item.getChildren().get(6);
			jumlah = (Intbox)cell.getChildren().get(0);
			jumlah.setDisabled(false);
			
			jumlah.addEventListener("onChange", new EditReturListener(item,pharmacyList,total));
			
		}
		
		this.setButtonDisable(true);
		this.btnSave.setDisabled(false);
		this.btnNew.setDisabled(false);
		isUpdate = true;
		MiscTrxController.setFont(pharmacyList);
	}
	
	
	public void saveModify() throws VONEAppException, InterruptedException
	{
		Date tanggal = new Date();
		
		apotikReturn.setNTrxValue(this.total.getValue().doubleValue());
		apotikReturn.setDWhnChange(tanggal);
		apotikReturn.setVWhoChange(user.getVUserName());
		
		
		
		Set<TbReturPharmacyDetailTrx> detiljuga = new HashSet<TbReturPharmacyDetailTrx>();		
		
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		
		Intbox jumlah = null;
		
		
		TbReturPharmacyDetailTrx detil = null;
		
		List<Listitem> items = pharmacyList.getItems();
		for(Listitem item : items){
			detil = (TbReturPharmacyDetailTrx)item.getValue();
			cell =  (Listcell)item.getChildren().get(6);
			jumlah = (Intbox)cell.getChildren().get(0);
			
			cell = (Listcell)item.getChildren().get(7);
			db.setText(cell.getLabel());
			detil.setNValue(db.getValue().doubleValue());
			
			if(jumlah.getValue() == null)
				detil.setNQty(new Short("0"));
			else
				detil.setNQty(jumlah.getValue().shortValue());
			detiljuga.add(detil);
			
		}
		
		
		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		if(apServ.saveReturModify(apotikReturn, detiljuga, warehouseId)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
//		
		
		
//		NoteManager manager = Service.getNotaManager();
//		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
//		
//		if(manager.saveModifyNote(nota, itemSet, treatmentSet, bundledSet,null, null, warehouseId)){
//			
//			Messagebox.show(MessagesService.getKey("common.modify.success"));
//		}
//		
//		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
//		
//		setButtonActive(false);
		this.btnSave.setDisabled(true);
		MiscTrxController.setFont(pharmacyList);
	}
	
	
	public void getLocation() throws VONEAppException{
	
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		Sessions.getCurrent().setAttribute("lokasiretur", unit);
		
	}
	
}

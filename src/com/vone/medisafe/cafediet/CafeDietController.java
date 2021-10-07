package com.vone.medisafe.cafediet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
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
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.laborat.LaboratController;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.radiology.RadiologyManager;
import com.vone.medisafe.report.NoteReport;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.polyclinic.EditListener;

public class CafeDietController  extends BaseController {
	
	public MsUser user;
	public MsStaffInUnit siu;
	public Listitem item;

	ZulConstraint constraints = new ZulConstraint();

	public Session session;
	
	public Listbox locationList;
	public Checkbox referencePatient;
	public Bandbox MRNumber;
	public Textbox crNoMR;
	public Textbox crNama;
	public Textbox crNoAlamat;
	public Listbox MRNumberList;
	public Bandbox transactionNumber;
	public Textbox searchNota;
	public Textbox searchName;
	public Listbox notaList;
	public Textbox patientName;
	public Textbox registrationNumber;
	public Label labelStatus;
	public Label statusNota;

	public Listbox cafeDietList;

	public Decimalbox total;
	public Button calculateBtn;
	public Button btnDelete;
	public Button btnItemAdd;
	public Button btnSave;
	public Button btnModify;
	public Button btnCancel;
	public Button btnNew;
	public Button btnCancelNote;
	public Button btnValidation;
	public Button btnCetak;
//	public Button btnEnd;

	public boolean isRanap;
	public String ranapKelas;
	public TbRegistration reg;
	public MsPatient patient;
	public TbExamination nota;
	public TbTreatmentTrx treatmentTrx;
	public Listcell cell;
	public TbItemTrx itemTrx;
	public boolean isUpdate;
	public TbBundledTrx bundledTrx;;

	private RadiologyManager radiologyManager = Service.getRadiologyManager();

	
	public void init(Component win) throws InterruptedException, VONEAppException {
		referencePatient = (Checkbox)win.getFellow("freeBuyer");
		locationList = (Listbox)win.getFellow("locationList");
		transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		searchNota = (Textbox)win.getFellow("searchNota");
		searchName = (Textbox)win.getFellow("searchName");
		notaList = (Listbox)win.getFellow("notaList");
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		MRNumberList = (Listbox)win.getFellow("MRNumberList");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		patientName = (Textbox)win.getFellow("patientName");

		labelStatus = (Label)win.getFellow("labelStatus");
		statusNota = (Label)win.getFellow("statusNota");
		cafeDietList = (Listbox)win.getFellow("cafeDietList");
		total = (Decimalbox)win.getFellow("total");
		calculateBtn = (Button)win.getFellow("calculateBtn");
		btnDelete = (Button)win.getFellow("btnDelete");
		btnItemAdd = (Button)win.getFellow("btnItemAdd");
		btnSave = (Button)win.getFellow("btnSave");
		btnModify = (Button)win.getFellow("btnModify");
		btnCancel = (Button)win.getFellow("btnCancel");
		btnNew = (Button)win.getFellow("btnNew");
		btnCancelNote = (Button)win.getFellow("btnCancelNote");
		btnValidation = (Button)win.getFellow("btnValidation");
		btnCetak = (Button)win.getFellow("btnCetak");
//		btnEnd = (Button)win.getFellow("btnEnd");

		super.init(win);
		
		constraints.registerComponent(patientName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);

		constraints.registerComponent(patientName, ZulConstraint.NO_EMPTY);

		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);

		constraints.validateComponent(false);

		session = win.getDesktop().getSession(); 

		user = getUserInfoBean().getMsUser();
		radiologyManager.getMsStaffInUnits(user, locationList);
		
		MRNumberList.getItems().clear();
		cafeDietList.getItems().clear();
		notaList.getItems().clear();

		
		btnModify.setDisabled(true);
		// btnNew.setDisabled(true);
		btnCancelNote.setDisabled(true);
		btnValidation.setDisabled(true);
		btnCetak.setDisabled(true);
//		btnEnd.setDisabled(true);

		statusNota.setVisible(false);
		labelStatus.setVisible(false);

		MRNumber.focus();
		cafeDietList.setAttribute("jumlah", total);
		total.setFormat("#,###.##");


	}
	
	public void setMRVisible(Checkbox rujukan, Bandbox mr, Textbox regNumber) {
		if (rujukan.isChecked()) {
			//todo ambil pasien free buyer
			/**
			 *  jika blm ada buat pasient satu record !!!
			 *  
			 */
			mr.setVisible(false);
			regNumber.setVisible(false);
			//patientName.focus();
			patientName.setValue(MedisafeConstants.FREE_BUYER_NAME);
			patientName.setReadonly(true);

		} else {
			patientName.setValue(null);
			patientName.setReadonly(false);
			mr.setVisible(true);
			regNumber.setVisible(true);
			MRNumber.focus();
		}
	}
	
	public void checkRegistration(Component win) {
		total.setValue(null);
		String ranap = "NO";
		session.setAttribute("location", locationList);
		if (referencePatient.isChecked()) {
			session.setAttribute("isRanap", ranap);
			session.setAttribute("listbox", cafeDietList);
			// patientName.focus();
			isRanap = false;
		} else {
			if (isRanap) {
				// ambilKelas ranap
				ranap = "YES";
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", cafeDietList);
				session.setAttribute("kelasTarif", ranapKelas);

			} else {
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", cafeDietList);
			}
		}

	}
	public void getRegistration(int type) throws VONEAppException {
		
		radiologyManager.getRegistrationCafe(this, type);
		
	}

	public void setFieldDisable(boolean isDisable) {
		referencePatient.setDisabled(isDisable);
		locationList.setDisabled(isDisable);
		transactionNumber.setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
		registrationNumber.setDisabled(isDisable);
		patientName.setDisabled(isDisable);
		cafeDietList.setDisabled(isDisable);
		total.setDisabled(isDisable);
	
	}
	
	public void calculateTotalAmount() throws InterruptedException {
		List jumlah = cafeDietList.getItems();
		double jumlahTotal = 0;
		Object[] obj = null;

		Iterator it = jumlah.iterator();
		while (it.hasNext()) {
			item = (Listitem) it.next();
			// obj[0]= harga sebelum diskon -> BigDecimal
			// obj[1]= harga setelah diskon -> BigDecimal
			// obj[2]= tipe diskon -> RP atau %
			// obj[3] = banyaknya item, selian item jumlah=1

			if (item.getAttribute("manipulation") != null) {
				obj = (Object[]) item.getAttribute("manipulation");
				jumlahTotal = jumlahTotal + ((BigDecimal) obj[1]).doubleValue();
			}
		}
		total.setValue(new BigDecimal(jumlahTotal));
	}
	
	// PENCARIAN NOTA
	public void searchNote() throws InterruptedException, WrongValueException, VONEAppException {

		notaList.getItems().clear();

		statusNota.setVisible(false);
		labelStatus.setVisible(false);

		MsUnit unit = (MsUnit) locationList.getSelectedItem().getValue();
		
		Service.getNotaManager().searchNote(
				"%" + searchNota.getValue() + "%",
				"%" + searchName.getValue() + "%", unit, notaList, MedisafeConstants.ACTIVE_NOTE);


	}

	public void getNote() {
		TbExamination nota = (TbExamination) transactionNumber
				.getAttribute("nota");

		LaboratController pc = new LaboratController();
		Object[] obj = new Object[] { nota, pc,
				new Integer(MedisafeConstants.NOTA), btnValidation, btnModify,
				btnCancelNote, statusNota };
		session.setAttribute("validasi", obj);

	}

	public void cancelNote() throws VONEAppException{
		
		String permission = super.getUserInfoBean().getScreenPermission(ScreenConstant.TRANSAKSI_CAFE_DIET);
		

		TbExamination nota = (TbExamination) transactionNumber
				.getAttribute("nota");
//		Object[] obj = new Object[] { nota, laboratoryList.getItems(),
//				new Integer(MedisafeConstants.NOTA), btnValidation, btnModify,
//				btnCancelNote, statusNota, siu };
		Object[] obj = new Object[]{nota,((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId(),
				new Integer(MedisafeConstants.NOTA),btnValidation,btnModify,btnCancelNote,statusNota,permission};
		session.setAttribute("cancelNote", obj);

		labelStatus.setVisible(true);
		statusNota.setVisible(true);
		System.out.println("Cancel sukses");
	}

	public void getNoteDetail() throws VONEAppException {
		
		radiologyManager.getNoteDetailCafe(this);
		
	}

	public void setInnerButtonDisable(boolean isDisable) {
		// btnBundleAdd.setDisabled(isDisable);
		// btnItemAdd.setDisabled(isDisable);

		btnItemAdd.setDisabled(isDisable);
		/*btnModify.setDisabled(isDisable);*/
		/*btnCancel.setDisabled(isDisable);*/
		btnDelete.setDisabled(isDisable);
		calculateBtn.setDisabled(isDisable);
	}

	public void setButtonActive(boolean isActive) {
		this.btnCancelNote.setDisabled(isActive);
		this.btnValidation.setDisabled(isActive);
		// this.btnNew.setDisabled(isActive);
		this.btnModify.setDisabled(isActive);
		this.btnCetak.setDisabled(isActive);
		this.btnSave.setDisabled(!isActive);
	}
	
	public void delete() throws InterruptedException {
		if (cafeDietList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		int index = cafeDietList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		cafeDietList.removeItemAt(index);
		total.setValue(null);
	}

	public void clear() {
	
			locationList.setSelectedIndex(0);
			transactionNumber.setValue(null);
			MRNumber.setValue(null);
			registrationNumber.setValue(null);
			patientName.setValue(null);
			total.setValue(null);
	
			cafeDietList.getItems().clear();
			// laboratoryList.setSelectedIndex(0);
	
			referencePatient.setChecked(false);
			MRNumber.setVisible(true);
			registrationNumber.setVisible(true);
			registrationNumber.setDisabled(true);
			transactionNumber.setDisabled(false);
	
			this.patient = null;
			this.reg = null;
			this.treatmentTrx = null;
			this.itemTrx = null;
	
			labelStatus.setVisible(false);
			statusNota.setVisible(false);
			MRNumberList.getItems().clear();
			notaList.getItems().clear();
			crNama.setValue(null);
			crNoAlamat.setValue(null);
			crNoMR.setValue(null);
			searchName.setValue(null);
			searchNota.setValue(null);
			
			MRNumber.focus();
	
			session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
	
		}

	public void getNewTransaction() {
		statusNota.setVisible(false);
		setFieldDisable(false);
		total.setDisabled(true);
		setInnerButtonDisable(false);
		setButtonActive(true);
		MRNumber.focus();
		clear();
	}

	public void save() throws InterruptedException, VONEAppException {
	
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}

		if ((patientName.getText().trim() == "")
				|| (cafeDietList.getItems().size() == 0)) {
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if (total.getValue() == null) {
			Messagebox.show(MessagesService
					.getKey("common.transaction.total.not.counted"));
			return;
		}
		if (MRNumber.getValue().trim().equals("")) {
			if (!referencePatient.isChecked()) {
				Messagebox.show(MessagesService.getKey("rujukan.not.selecetd"));
				return;
			}
		}
	
		// step
		// simpan data pasien (bila rujukan)
		// simpan ke dalam tabel examination, generate note here, assign juga
		// siapa pasiennya
		// masukan ke tabel transaksi, lakukan updating langsung terhadap jumlah
		// item di inventory (paling ruwet)
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);

	
		nota = new TbExamination();
	
		nota.setMsUnit((MsUnit) locationList.getSelectedItem().getValue());
		nota.setTotal(new Double(total.getValue().doubleValue()));
		
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		nota.setNExamStatus(new Integer(MedisafeConstants.ACTIVE_NOTE));
		nota.setVWhoCreate(user.getVUserName());
		
		if(reg != null)	nota.setTbRegistration(reg);
		
	
		List list = cafeDietList.getItems();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			item = (Listitem) it.next();
			Object[] obj = (Object[]) item.getAttribute("manipulation");
			if (item.getValue() instanceof MsTreatmentFee) {
				treatmentTrx = new TbTreatmentTrx();
				treatmentTrx.setNAmountTrx(((BigDecimal) obj[0]).doubleValue());
				treatmentTrx.setMsDoctor((MsStaff) item.getAttribute("doctor"));
				treatmentTrx.setNAmountAfterDisc(((BigDecimal) obj[1])
						.doubleValue());
				treatmentTrx
						.setNDiscAmount(((BigDecimal) obj[4]).doubleValue());
				treatmentTrx.setNQty((short) 1);
				treatmentTrx.setVDiscType(obj[2].toString());
				treatmentTrx.setVWhoCreate(user.getVUserName());
				treatmentTrx
						.setMsTreatmentFee((MsTreatmentFee) item.getValue());
				treatmentSet.add(treatmentTrx);
			}
			else if(item.getValue() instanceof Object[]){
				itemTrx = new TbItemTrx();
				Object[] object = (Object[])item.getValue();
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)object[0]);
//				itemTrx.setMsItem(((MsItemSellingPrice)item.getValue()).getMsItem());
				itemTrx.setMsItem(msItem);
				itemTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				itemTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				itemTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
//				itemTrx.setNQty(((Double)obj[3]).floatValue());
				itemTrx.setNQty(new Float(obj[3].toString()));
				itemTrx.setVDiscType(obj[2].toString());
				itemTrx.setVWhoCreate(user.getVUserName());
				itemSet.add(itemTrx);
			}

		}
		if (referencePatient.isChecked()) {
			// simpan transaksi poliklinik utk pasien rujukan
			// data pasien : nama, alamat, tanggal lahir, jenis kelamin, type
			// pasien;
			if (!constraints.validateComponent(true))
				return;
			//todo get freebuyer pasien
			patient = Service.getLaboratManager().getFreeBuyer();
			
			if (Service.getPolyclinicManager().save(patient, nota, treatmentSet, itemSet, bundledSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)) {
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);
	
				statusNota.setVisible(true);
				labelStatus.setVisible(true);
	
				statusNota.setValue(MedisafeUtil.getNoteStatus(nota
						.getNExamStatus().intValue()));
	
				setFieldDisable(true);
				setButtonActive(false);
				setInnerButtonDisable(true);
				btnModify.setDisabled(true);
	
			} else {
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		} else {
			if (Service.getPolyclinicManager().save(patient, nota, treatmentSet, itemSet, bundledSet,
					(MsUnit)locationList.getSelectedItem().getValue(), isRanap)) {
				Messagebox.show(MessagesService.getKey("common.save.success"));
				transactionNumber.setValue(nota.getVNoteNo());
				transactionNumber.setAttribute("nota", nota);
	
				statusNota.setVisible(true);
				labelStatus.setVisible(true);
	
				statusNota.setValue(MedisafeUtil.getNoteStatus(nota
						.getNExamStatus().intValue()));
	
				setFieldDisable(true);
				setButtonActive(false);
				setInnerButtonDisable(true);
//				btnModify.setDisabled(true);
			} else {
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		}
		transactionNumber.setAttribute("nota", nota);
	}
	
	public void cetakClick() throws InterruptedException, VONEAppException {
		NoteReport nr = new NoteReport(nota);
		
		if(!nr.printOut(session.getClientAddr())){
			Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU!!!");
		}
	}

	@SuppressWarnings("unchecked")
	public void modify() throws VONEAppException{
		
		Listbox polyclinicList = cafeDietList;
		List<Listitem> items = polyclinicList.getItems();
		
		Decimalbox diskon = null;
		Intbox jumlah = null;
		Listbox diskonOptionList = null;
		
		Listcell cell = null;
		
		for(Listitem item : items)
		{
			cell = (Listcell)item.getChildren().get(2);
			try {
				jumlah = (Intbox)cell.getChildren().get(0);
			} catch (RuntimeException e) {
				
				jumlah = new Intbox();
				jumlah.setValue(new Integer(cell.getLabel()));
				cell.setLabel("");
				jumlah.setParent(cell);
			}
			jumlah.setDisabled(false);
			jumlah.addEventListener("onChange", new EditListener(item,polyclinicList,total));
			
			cell = (Listcell)item.getChildren().get(5);
			diskon = (Decimalbox)cell.getChildren().get(0);
			diskon.addEventListener("onChange", new EditListener(item,polyclinicList,total));
			
			diskonOptionList = (Listbox)cell.getChildren().get(1);
			diskonOptionList.addEventListener("onSelect", new EditListener(item,polyclinicList,total));
			
			diskon.setDisabled(false);
			diskonOptionList.setDisabled(false);
			
		}
		setButtonActive(true);
		this.btnSave.setDisabled(false);
		isUpdate = true;
	}
	
	
	@SuppressWarnings("unchecked")
	public void saveModify() throws VONEAppException, InterruptedException
	{
		Date tanggal = new Date();
		
		nota.setTotal(this.total.getValue().doubleValue());
		nota.setDWhnChange(tanggal);
		nota.setVWhoChange(user.getVUserName());
		
		
		Set<TbTreatmentTrx> treatmentSet = new HashSet<TbTreatmentTrx>(0);
		Set<TbItemTrx> itemSet = new HashSet<TbItemTrx>(0);
		Set<TbBundledTrx> bundledSet = new HashSet<TbBundledTrx>(0);
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		double trxValue = 0;
		Intbox jumlah = null;
		Decimalbox diskon = null;
		Listbox opsiDiskon = null;
		
		Listbox polyclinicList =  this.cafeDietList;
		List<Listitem> items = polyclinicList .getItems();
		for(Listitem item : items){
			if(item.getValue() instanceof Object[]){
				
				itemTrx = new TbItemTrx();
				
				Object[] object = (Object[])item.getValue();
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)object[0]);
				itemTrx.setMsItem(msItem);
				
				cell = (Listcell)item.getChildren().get(2);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				itemTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				itemTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				itemTrx.setNDiscAmount(diskon.getValue().doubleValue());
				
				itemTrx.setNQty(new Float(jumlah.intValue()));
				itemTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				itemTrx.setVWhoCreate(user.getVUserName());
				itemSet.add(itemTrx);
			}
			else if(item.getValue() instanceof TbTreatmentTrx){
				treatmentTrx = (TbTreatmentTrx)item.getValue();
				treatmentTrx.setDWhnChange(tanggal);
				
				cell = (Listcell)item.getChildren().get(2);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				treatmentTrx.setNQty(jumlah.getValue().shortValue());
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				treatmentTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				treatmentTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				treatmentTrx.setNDiscAmount(diskon.getValue().doubleValue());
				treatmentTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				treatmentSet.add(treatmentTrx);
			}
			else if(item.getValue() instanceof TbBundledTrx){
				bundledTrx = (TbBundledTrx)item.getValue();
				bundledTrx.setDWhnChange(tanggal);
				
				cell = (Listcell)item.getChildren().get(2);
				jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				bundledTrx.setNQty(jumlah.getValue().shortValue());
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				bundledTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				bundledTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				bundledTrx.setNDiscAmount(diskon.getValue().doubleValue());
				bundledTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				
				bundledSet.add(bundledTrx);
			}
		}
		
		NoteManager manager = Service.getNotaManager();
		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
		
		if(manager.saveModifyNote(nota, itemSet, treatmentSet, bundledSet, null, null, warehouseId)){
			
			Messagebox.show(MessagesService.getKey("common.modify.success"));
		}
		
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
		
		setButtonActive(false);
		this.btnSave.setDisabled(true);
	}

}

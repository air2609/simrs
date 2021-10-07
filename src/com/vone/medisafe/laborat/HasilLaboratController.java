package com.vone.medisafe.laborat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbLaboratoryResult;
import com.vone.medisafe.mapping.TbLaboratoryResultDetail;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.master.PatientTypeManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.ui.base.BaseController;

public class HasilLaboratController extends BaseController {

	MsUser user;
	boolean isRanap;
	
	TbLaboratoryResult labResult;
	TbLaboratoryResultDetail labResultDetail;
	TbExamination exam;
	TbMedicalRecord mr;
	TbRegistration reg;
	MsPatient patient;

	Session session;
	
	Textbox takeTime;
		
	Checkbox referencePatient;
	
	Listbox locationList;

	//NO. MR
	Bandbox MRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox MRNumberList;
	
	//NO. NOTA
	Bandbox transactionNumber;
	Textbox searchNota;
	Textbox searchName;
	Listbox TransactionNumberList;

	//NO. HASIL LAB
	Bandbox labResultNumber;
	Textbox searchResult;
	Textbox nameSearch;
	Listbox labResultList;

	Textbox patientName;
	Textbox registrationNumber;
	Textbox age;
	Textbox mainDoctor;
	Textbox hall;
	Textbox bed;
	Tree hasilLabTree;
	Treechildren anak;
	Treeitem detailTreeitem;
	Treeitem groupTreeitem;

	Button btnSave;
	Button btnModify;
	Button btnCancel;
	Button btnNew;
	Button btnPrint;
//	Button btnEnd;

	Listitem item;
	Listcell cell;
	String ranapKelas;
	
	Textbox escortDoctor;
	Textbox laboratNo;

	ZulConstraint constraints = new ZulConstraint();

	Decimalbox total;
	Decimalbox discount;
	Decimalbox grandTotal;

	Label labelStatus;
	Label statusNota;
	
	Treechildren child;
	Treeitem treeItem;
	Treerow treeRow;
	Treechildren children;
	Treecell treeCell;
	boolean isUpdate;
	
	//LaboratManager labServ = Service.getLaboratManager();

	LaboratManager labManager = MasterServiceLocator.getLaboratManager(); 
	UnitManager unitService = MasterServiceLocator.getUnitManager();
	PatientTypeManager patientTypeService = MasterServiceLocator.getPatientTypeManager();
	NoteManager notaManager  = MasterServiceLocator.getNoteManager();


	public void init(Component win) throws InterruptedException,
			VONEAppException {
		
		referencePatient = (Checkbox) win.getFellow("referencePatient");
		locationList = (Listbox) win.getFellow("locationList");
		
		MRNumber = (Bandbox) win.getFellow("MRNumber");

		//NO. HASIL LAB
		labResultNumber = (Bandbox) win.getFellow("labResultNumber");
		searchResult = (Textbox) win.getFellow("searchResult");
		nameSearch = (Textbox) win.getFellow("nameSearch");
		labResultList = (Listbox) win.getFellow("labResultList");

		// NO. MR
		MRNumberList = (Listbox) win.getFellow("MRNumberList");
		crNoMR = (Textbox) win.getFellow("crNoMR");
		crNama = (Textbox) win.getFellow("crNama");
		crNoAlamat = (Textbox) win.getFellow("crNoAlamat");
		transactionNumber = (Bandbox) win.getFellow("transactionNumber");
		takeTime = (Textbox) win.getFellow("takeTime");

		// NO. NOTA
		searchNota = (Textbox) win.getFellow("searchNota");
		searchName = (Textbox) win.getFellow("searchName");
		TransactionNumberList = (Listbox) win.getFellow("TransactionNumberList");
		
		patientName = (Textbox) win.getFellow("patientName");
		registrationNumber = (Textbox) win.getFellow("registrationNumber");
		age = (Textbox) win.getFellow("age");
		mainDoctor = (Textbox) win.getFellow("mainDoctor");
		hall = (Textbox) win.getFellow("hall");
		bed = (Textbox) win.getFellow("bed");

		hasilLabTree = (Tree) win.getFellow("hasilLabTree");
		anak = (Treechildren) win.getFellow("anak");
		detailTreeitem = (Treeitem) win.getFellow("detailTreeitem");
		groupTreeitem = (Treeitem) win.getFellow("groupTreeitem");

		btnSave = (Button) win.getFellow("btnSave");
		btnModify = (Button) win.getFellow("btnModify");
		//btnCancel = (Button) win.getFellow("btnCancel");
		btnNew = (Button) win.getFellow("btnNew");
		btnPrint = (Button) win.getFellow("btnPrint");
//		btnEnd = (Button) win.getFellow("btnEnd");
		
		escortDoctor = (Textbox) win.getFellow("escortDoctor");
		laboratNo = (Textbox) win.getFellow("laboratNo");


		super.init(win);

		constraints.registerComponent(patientName, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);

		constraints.registerComponent(patientName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(searchNota, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(searchName, ZulConstraint.UPPER_CASE);

		constraints.registerComponent(searchResult, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(nameSearch, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);

		session = win.getDesktop().getSession();

		user = getUserInfoBean().getMsUser();

		Service.getRadiologyManager().getMsStaffInUnits(user, locationList);
		
		btnModify.setDisabled(true);
//		btnEnd.setDisabled(true);

		MRNumber.focus();
		
		// hasilLabTree.setAttribute("jumlah", total);
		MRNumberList.getItems().clear();
		TransactionNumberList.getItems().clear();
		labResultList.getItems().clear();
		
		//rincian
		anak.getChildren().clear();
		takeTime.setValue(PrintClient.getDate(new Date(), "hh:mm"));
		labResultNumber.setDisabled(false);

	}

	public void setMRVisible(Checkbox rujukan, Bandbox mr, Textbox regNumber) {
		if (rujukan.isChecked()) {
			mr.setVisible(false);
			mainDoctor.setVisible(false);
			regNumber.setVisible(false);
			patientName.focus();
			hall.setVisible(false);
			bed.setVisible(false);

		} else {
			mr.setVisible(true);
			mainDoctor.setVisible(true);
			regNumber.setVisible(true);
			MRNumber.focus();
			hall.setVisible(true);
			bed.setVisible(true);
		}
	}

	public void checkRegistration(Component win) {
		total.setValue(null);
		String ranap = "NO";
		session.setAttribute("location", locationList);
		if (referencePatient.isChecked()) {
			session.setAttribute("isRanap", ranap);
			session.setAttribute("listbox", hasilLabTree);
			// patientName.focus();
			isRanap = false;
		} else {
			if (isRanap) {
				// ambilKelas ranap
				ranap = "YES";
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", hasilLabTree);
				session.setAttribute("kelasTarif", ranapKelas);

			} else {
				session.setAttribute("isRanap", ranap);
				session.setAttribute("listbox", hasilLabTree);
			}
		}

	}

	// dipakai untuk mengambil hasil lab berdasarkan pencarian no. nota
	public void getLabDetail() throws VONEAppException {
		labManager.getLabDetail(this);
		MiscTrxController.setFont(hasilLabTree);
	}
	
	public void getDetailLabTest(MsPatient pat) throws VONEAppException {
		// tb_examination berisi nota dari transaksi lab = 51
		//
		List list = labManager.getTreatment(pat);
		Iterator iter = list.iterator();
		MsTreatment treat;
		String kepala1 = "", kepala2 = "";
		anak.getChildren().clear();
		String rangeWoman = "-", rangeMan = "-", satuan = "-";
		MsLabTestDetail msLabTestDetail;

		while (iter.hasNext()) {
			treat = (MsTreatment) iter.next();
			kepala2 = treat.getMsTreatmentGroup().getVTgroupName();
			if (!kepala1.equals(kepala2) ) {
				createGroupTree(null, kepala2);
			}
			msLabTestDetail = labManager.getLabTestDetail(
					treat.getNTreatmentId());
			if (msLabTestDetail != null) {
				rangeMan = msLabTestDetail.getVNrmlRange();
				rangeWoman = msLabTestDetail.getVNrmlRangeGroup();
				satuan = msLabTestDetail.getVLabTestQuantify();
			}
			createItemTree(treat, treat.getVTreatmentName(), "", rangeMan,
					rangeWoman, satuan, false);

			kepala1 = treat.getMsTreatmentGroup().getVTgroupName();
			rangeWoman = "-";
			rangeMan = "-";
			satuan = "-";
		}
	}

	public void getDetailLabTestResult(TbLaboratoryResult labResult) throws VONEAppException {
		// tb_examination berisi nota dari transaksi lab = 51
		//
//		System.out.println("HasilLaboratController.getDetailLabTestResult()");
		//user ambil dari 
		//ui: NO. NOTA:
		List list = labManager.getTbLabResultDetail(labResult.getNLabRsltId());
		Iterator iter = list.iterator();
		MsTreatment treat;
		String kepala1 = "", kepala2="";
		
		anak.getChildren().clear();
		String rangeWoman = "-", rangeMan = "-", satuan = "-", hasil = "-";

		while (iter.hasNext()) {
			labResultDetail = (TbLaboratoryResultDetail) iter.next();
			
			//pakai manager
			treat = labManager.getTreatmentByLrd(labResultDetail);
			kepala2 = treat.getMsTreatmentGroup().getVTgroupName() + "-" +treat.getVTreatmentName(); 
			
			if (!kepala1.equals(kepala2)) {
				createGroupTree(null, kepala2);
			}
			if (labResultDetail != null) {
				rangeMan = labResultDetail.getVNrmlRangeMan();
				rangeWoman = labResultDetail.getVNrmlRangeWoman();
				satuan = labResultDetail.getVLabRsltQuantify();
				hasil = labResultDetail.getVLabRsltDesc();
			}
			MsLabTreatmentDetil msLabTreatmentDetil = labManager.getMsLabTreatmentDetilById(labResultDetail.getNLabDetilId());
			createItemTree(labResultDetail, msLabTreatmentDetil.getVDetailName(), hasil, rangeMan,
					rangeWoman, satuan, true);

//			kepala1 = treat.getMsTreatmentGroup().getVTgroupName();
			kepala1 = treat.getMsTreatmentGroup().getVTgroupName() + "-" +treat.getVTreatmentName();
			rangeWoman = "-";
			rangeMan = "-";
			satuan = "-";
			hasil = "-";

		}
		MiscTrxController.setFont(hasilLabTree);
	}

	public void getRegistration(int type) throws VONEAppException,
			InterruptedException {
		
		labManager.getRegistration(this, type);
		
	}

	public void setFieldDisable(boolean isDisable) {
		referencePatient.setReadonly(isDisable);
//		locationList.setReadonly(isDisable);
		
		transactionNumber.setReadonly(isDisable);
		MRNumber.setReadonly(isDisable);
		labResultNumber.setReadonly(isDisable);
		
		registrationNumber.setReadonly(isDisable);
		patientName.setReadonly(isDisable);
		age.setReadonly(isDisable);
		mainDoctor.setReadonly(isDisable);
		hall.setReadonly(isDisable);
		bed.setReadonly(isDisable);
		//
		transactionNumber.setDisabled(isDisable);
		MRNumber.setDisabled(isDisable);
		labResultNumber.setDisabled(isDisable);
		
		referencePatient.setDisabled(isDisable);
		locationList.setDisabled(isDisable);
		
		registrationNumber.setDisabled(false);
		patientName.setDisabled(false);
		age.setDisabled(false);
		mainDoctor.setDisabled(false);
		hall.setDisabled(false);
		bed.setDisabled(false);
		
	}

	public void clear() {

		locationList.setSelectedIndex(0);
		transactionNumber.setValue(null);
		MRNumber.setValue(null);
		registrationNumber.setValue(null);
		patientName.setValue(null);
		age.setValue(null);
		mainDoctor.setValue(null);
		labResultNumber.setValue(null);
		
		laboratNo.setValue(null);
		escortDoctor.setValue(null);
		takeTime.setValue(PrintClient.getDate(new Date(), "hh:mm"));
		
		// patientEscort.setSelectedIndex(0);
		// address.setValue(null);
		// patientTypeList.setSelectedIndex(0);
		// laboratoryList.setSelectedIndex(0);
		// total.setValue(null);

		// hasilLabTree.getItems().clear();
		anak.getChildren().clear();
		referencePatient.setChecked(false);
		MRNumber.setVisible(true);
		mainDoctor.setVisible(true);
		
		registrationNumber.setVisible(true);
//		registrationNumber.setDisabled(true);
//		transactionNumber.setDisabled(false);
		
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		crNoMR.setValue(null);
		MRNumberList.getItems().clear();
		
		this.nameSearch.setValue(null);
		this.searchNota.setValue(null);
		this.searchName.setValue(null);
		this.searchResult.setValue(null);
		this.labResultList.getItems().clear();
		this.TransactionNumberList.getItems().clear();
		
		
		hall.setVisible(true);
		bed.setVisible(true);
		
		hall.setValue(null);
		bed.setValue(null);


		// labelStatus.setVisible(false);
		// statusNota.setVisible(false);
		MRNumber.focus();

		session.removeAttribute(MedisafeConstants.PATIENT_HISTROY);
		this.reg = null;
		this.patient = null;
		//this.

	}

	public void getNewTransaction() {
		// statusNota.setVisible(false);
		setFieldDisable(false);
		// total.setDisabled(true);
		setButtonActive(true);
		MRNumber.focus();
		clear();
	}

	public void setButtonActive(boolean isActive) {
		this.btnModify.setDisabled(isActive);
		this.btnSave.setDisabled(!isActive);
	}

	public void createGroupTree(Object value, String caption) {
		treeItem = new Treeitem();
		treeItem.setValue(value);
		treeItem.setParent(anak);

		treeRow = new Treerow();
		treeRow.setParent(treeItem);
		treeCell = new Treecell(caption);
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt");
		children = new Treechildren();
		children.setParent(treeItem);
	}

	private void createItemTree() {
		createItemTree(null, "TEST", "", "10-20", "20-30", "ml/gr", true);
	}

	public void createItemTree(Object value, String itemCaption, String hasilTest,
			String rangePria, String rangeWanita, String satuan, boolean readOnly) {
		//rangeWanita tdk dipakai lagi, digabung jd 1 cell
		
		Textbox textbox;
//		Label lbl;
		Treeitem treeitem;
		treeitem = new Treeitem();
		treeitem.setValue(value);
		treeitem.setStyle("font-size:8pt");
		treeitem.setParent(children);
		treeRow = new Treerow();
		treeRow.setStyle("font-size:8pt");
		treeRow.setParent(treeitem);
		treeCell = new Treecell(itemCaption);
		treeCell.setStyle("font-size:8pt");
		treeCell.setParent(treeRow);
		treeCell = new Treecell("");
		treeCell.setStyle("font-size:8pt");
		treeCell.setParent(treeRow);
		textbox = new Textbox();
		textbox.setWidth("96%");
		textbox.setText(hasilTest);
		textbox.setStyle("font-size:9pt");
		textbox.setParent(treeCell);
		textbox.setReadonly(readOnly);
		treeitem.setAttribute("hasil", textbox);

		treeCell = new Treecell();
		treeCell.setParent(treeRow);

		textbox = new Textbox();
		
		textbox.setWidth("98%");
		textbox.setParent(treeCell);
		textbox.setStyle("font-size:9pt");
		textbox.setText(rangePria);
		textbox.setReadonly(true);
		textbox.setDisabled(true);
		treeitem.setAttribute("pria", textbox);

//		lbl = new Label("WANITA");
//		lbl.setParent(treeCell);
//
//		tb = new Textbox();
//		tb.setWidth("35%");
//		tb.setParent(treeCell);
//		tb.setText(rangeWanita);
//		tb.setReadonly(readOnly);
//		it.setAttribute("wanita", tb);

		treeCell = new Treecell(satuan);
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt");
		treeitem.setAttribute("satuan", satuan);

	}

	public void test() {
		for (int i = 0; i < 5; i++) {
			createGroupTree("kosong", "abc ");
			for (int j = 0; j < 3; j++) {
				createItemTree();
			}
		}
	}

	public void calculateTotalAmount() throws InterruptedException {
		List jumlah = (List) hasilLabTree.getItems();
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
	public void searchNote() throws VONEAppException {
		
		labManager.searchNote(this);

	}

	public void save() throws VONEAppException, InterruptedException {
		if(isUpdate){
			saveModify();
			isUpdate = false;
			return;
		}


		if ((patientName.getText().trim() == "")
				|| (anak.getItems().size() == 0)) {
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if (MRNumber.getValue().trim().equals("")) {
			if (!referencePatient.isChecked()) {
				Messagebox.show(MessagesService.getKey("rujukan.not.selecetd"));
				return;
			}
		}

		labResult = new TbLaboratoryResult();
		Set<TbLaboratoryResultDetail> labResultDetailSet = new HashSet<TbLaboratoryResultDetail>(0);
		
		labResult.setVWhoCreate(user.getVUserName());
		labResult.setDWhnCreate(new Date());
		labResult.setVJam(takeTime.getText());
		labResult.setVDrPengirim(escortDoctor.getText());
		labResult.setVResep(laboratNo.getText());


		Treeitem treeitem;
		Treechildren rincian;
//		Treecell tc;
//		Textbox textbox;
//		Iterator iterGroup, iterRow, iterCell, iterRange;
		Iterator iterGroup, iterRow;
//		String hasilTest, normalPria, normalWanita, satuan;
		iterGroup = anak.getChildren().iterator();
//		MsTreatment treat;
		
		MsLabTreatmentDetil msLabTreatmentDetil;
		Textbox normalRangeTextbox;
		Textbox hasilTextbox;
		String satuan;
		MsTreatment treat;
		Treeitem elementGroup;
		
		while (iterGroup.hasNext()) {
			elementGroup = (Treeitem) iterGroup.next();
			rincian = elementGroup.getTreechildren();
			iterRow = rincian.getItems().iterator();
			while (iterRow.hasNext()) {
				treeitem = (Treeitem) iterRow.next(); 
				//asalnya:
				//it.setAttribute("satuan", treeCell);
				//it.setAttribute("pria", tb);
				//it.setAttribute("hasil", tb);
				//dibaca:
				msLabTreatmentDetil = (MsLabTreatmentDetil)treeitem.getValue();
				normalRangeTextbox = (Textbox)treeitem.getAttribute("pria");
				hasilTextbox = (Textbox)treeitem.getAttribute("hasil");
				satuan = (String) treeitem.getAttribute("satuan");
				treat = msLabTreatmentDetil.getMsTreatment();
				
//				iterCell = brs.getTreerow().getChildren().iterator();
//				treat = (MsTreatment) brs.getValue();
//
//				tc = (Treecell) iterCell.next(); // NAMA PEMERIKSAAN
//
//				tc = (Treecell) iterCell.next(); // HASIL PEMERIKSAAN
//				textbox = (Textbox) tc.getChildren().iterator().next();
//				hasilTest = textbox.getText();
//
//				tc = (Treecell) iterCell.next(); // NORMAL RANGE
//
//				iterRange = tc.getChildren().iterator();// ITERATOR DLM RANGE
//														// NORMAL
//				textbox = (Textbox) iterRange.next();
//				normalPria = textbox.getText();
//
//				iterRange.next(); // label
//				textbox = (Textbox) iterRange.next();
//				normalWanita = textbox.getText();
//
//				tc = (Treecell) iterCell.next(); // SATUAN
//				satuan = tc.getLabel();
//
//				// yg hasil lab kosong tdk usah disimpan
				if (hasilTextbox.getText().length() > 0) {
					labResultDetail = new TbLaboratoryResultDetail();
					labResultDetail.setVLabRsltDesc(hasilTextbox.getText());
					labResultDetail.setVNrmlRangeMan(normalRangeTextbox.getText());
					labResultDetail.setVNrmlRangeWoman("x");
					labResultDetail.setVLabRsltQuantify(satuan);
					labResultDetail.setMsTreatment(treat);
					labResultDetail.setVWhoCreate(user.getVUserName());
					labResultDetail.setDWhnCreate(new Date());
					
					labResultDetail.setNLabDetilId(msLabTreatmentDetil.getNLabDetilId());
					

					labResultDetailSet.add(labResultDetail);
				}
			}
		}
		if (labResultDetailSet.size() == 0) {
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		if (referencePatient.isChecked()) {
			// simpan transaksi poliklinik utk pasien rujukan
			// data pasien : nama, alamat, tanggal lahir, jenis kelamin, type
			// pasien;
			if (!constraints.validateComponent(true))
				return;

			if (labManager
					.saveHasilLab(patient, labResult, exam, labResultDetailSet,
							(MsUnit) locationList.getSelectedItem().getValue(),
							isRanap)) {
				Messagebox.show(MessagesService.getKey("common.save.success"));
				labResultNumber.setValue(labResult.getVLabRsltCode());
				labResultNumber.setAttribute("nota", labResult);

				setFieldDisable(true);
				setButtonActive(false);

			} else {
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		} else {
			if (labManager
					.saveHasilLab(patient, labResult, exam, labResultDetailSet,
							(MsUnit) locationList.getSelectedItem().getValue(),
							isRanap)) {
				Messagebox.show(MessagesService.getKey("common.save.success"));
				labResultNumber.setValue(labResult.getVLabRsltCode());
				labResultNumber.setAttribute("nota", labResult);

				// statusNota.setVisible(true);
				// labelStatus.setVisible(true);

				// statusNota.setValue(MedisafeUtil.getNoteStatus(nota
				// .getNExamStatus().intValue()));

				setFieldDisable(true);
				setButtonActive(false);
			} else {
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
		}
	}

	public void searchRegisteredPatient(Textbox crNoMR, Textbox crNama,
			Textbox crAlamat, Listbox patientSearchList)
			throws InterruptedException, VONEAppException {
		labManager.searchRegisteredPatient(this, crNoMR, crNama,
				crAlamat, patientSearchList);
		MiscTrxController.setFont(patientSearchList);
	}
	//
	public void searchHasilLab() throws VONEAppException, InterruptedException{
		//klik tombol cari dari bandbox labResultNumber
		//mencari data hasil lab yg telah disimpan
		//dimasukkan ke labResultList
		//untuk entri dan/atau edit (?)
		
		labManager.searchHasilLab(this);
		MiscTrxController.setFont(this.labResultList);
	}
	
	public void getLabResult() throws VONEAppException {
		//memasukkan hasil pilihan dari labResultList ke form
		//user mengambil data hasil lab yg sudah dientry
		//NO. HASIL LAB:
		labManager.getLabResult(this);
		
	}
	
	public void cetak() throws VONEAppException{
		
		if(labResultNumber.getText().length() == 0)
			return;
		
		 

		String query = "select * from report.hasil_lab('"+labResultNumber.getText()+"')";
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("nama", patient.getVPatientName());
		parameters.put("dokter", "");
		parameters.put("alamat", patient.getVPatientMainAddr());
		parameters.put("tanggal", PrintClient.getDate(exam.getDWhnCreate(), "dd-MM-yyyy"));
		parameters.put("umur", age.getText());
		if(reg == null)parameters.put("rawat", "RUJUKAN");
		else{
			if (reg.getVRegSecondaryId().substring(0, 1).equals("I"))
				parameters.put("rawat", "RANAP");
			else
				parameters.put("rawat", "RAJAL");
		}
		
		parameters.put("noRM", MRNumber.getText());
		parameters.put("register", registrationNumber.getText());
		parameters.put("noHasil", labResultNumber.getText());
		parameters.put("ruangBed", hall.getText() + "/" + bed.getText());
		parameters.put("jamPengambilan", takeTime.getText());
		parameters.put("jamDikerjakan", "");
		parameters.put("noLaborat", laboratNo.getText());//no resep

		parameters.put("keterangan", "");
		parameters.put("drPengirim", escortDoctor.getText());//diisi waktu buat hasil
		
		ReportEngine re = new ReportEngine(query,ReportService.getKey("hasilLab"), parameters);
		if(!re.printOut(locationList.getParent().getDesktop().getSession().getClientAddr()))
			re.openPdf();
	}

	public String getCurrentDateTime(){
		return PrintClient.getDate(new Date(), "dd-MM-yyyy");
	}
	
	public void modify(){
		Iterator iterGroup = anak.getChildren().iterator();
		while (iterGroup.hasNext()) {
			Treeitem elementGroup = (Treeitem) iterGroup.next();
			Treechildren rincian = elementGroup.getTreechildren();
			Iterator iterRow = rincian.getItems().iterator();
			while (iterRow.hasNext()) {
				Treeitem brs = (Treeitem) iterRow.next();
				Textbox textbox;
				textbox = (Textbox) brs.getAttribute("hasil");
				textbox.setReadonly(false);
				textbox = (Textbox) brs.getAttribute("pria");
				textbox.setReadonly(true);
				textbox.setDisabled(true);
//				textbox = (Textbox) brs.getAttribute("wanita");
//				textbox.setReadonly(false);
			}
		}
		setButtonActive(true);
		this.btnSave.setDisabled(false);
		isUpdate = true;
		
	}
	public void saveModify() throws InterruptedException, VONEAppException{

		//labResult = new TbLaboratoryResult();
		Set<TbLaboratoryResultDetail> labResultDetailSet = new HashSet<TbLaboratoryResultDetail>(0);
		
		labResult.setVWhoChange(user.getVUserName());
		labResult.setDWhnChange(new Date());
		
		labResult.setVJam(takeTime.getText());
		labResult.setVDrPengirim(escortDoctor.getText());
		labResult.setVResep(laboratNo.getText());



		Treeitem treeitem;
		Treechildren rincian;
		Iterator iterGroup, iterRow;
//		String hasilTest, normalPria, normalWanita, satuan;
		
		MsLabTreatmentDetil msLabTreatmentDetil;
		//TbLaboratoryResultDetail labResultDetail;
		Textbox normalRangeTextbox;
		Textbox hasilTextbox;
		String satuan;
		MsTreatment treat;
		Treeitem elementGroup;
		
		iterGroup = anak.getChildren().iterator();
		while (iterGroup.hasNext()) {
			elementGroup = (Treeitem) iterGroup.next();
			rincian = elementGroup.getTreechildren();
			iterRow = rincian.getItems().iterator();
			while (iterRow.hasNext()) {
				treeitem = (Treeitem) iterRow.next();
				// yg hasil lab kosong tdk usah disimpan
				normalRangeTextbox = (Textbox)treeitem.getAttribute("pria");
				hasilTextbox = (Textbox)treeitem.getAttribute("hasil");
				satuan = (String) treeitem.getAttribute("satuan");
				
				if (hasilTextbox.getText().length() > 0) {
					labResultDetail = (TbLaboratoryResultDetail) treeitem.getValue();
					
					//msLabTreatmentDetil = (MsLabTreatmentDetil)treeitem.getValue();
					msLabTreatmentDetil = labManager.getMsLabTreatmentDetilById(labResultDetail.getNLabDetilId());
					treat = msLabTreatmentDetil.getMsTreatment();
					
					labResultDetail.setVLabRsltDesc(hasilTextbox.getText());
					labResultDetail.setVNrmlRangeMan(normalRangeTextbox.getText());
					labResultDetail.setVNrmlRangeWoman("x");
					labResultDetail.setVLabRsltQuantify(satuan);
					labResultDetail.setMsTreatment(treat);
					labResultDetail.setVWhoChange(user.getVUserName());
					labResultDetail.setDWhnChange(new Date());
					
					labResultDetail.setNLabDetilId(msLabTreatmentDetil.getNLabDetilId());
					

					labResultDetailSet.add(labResultDetail);
				}
//				if (hasilTest != "") {
//					labResultDetail = (TbLaboratoryResultDetail) treeitem.getValue();
//					labResultDetail.setVLabRsltDesc(hasilTest);
//					labResultDetail.setVNrmlRangeMan(normalPria);
//					labResultDetail.setVNrmlRangeWoman(normalWanita);
//					labResultDetail.setVLabRsltQuantify(satuan);
//					labResultDetail.setTbLaboratoryResult(labResult);
//					
//
//					labResultDetailSet.add(labResultDetail);
//				}
			}
		}
		if (labResultDetailSet.size() == 0) {
			Messagebox.show(MessagesService.getKey("common.save.not.allowed"));
			return;
		}
		
		if (labManager.saveModifyHasilLab(patient, labResult, exam, labResultDetailSet)) {
			Messagebox.show(MessagesService.getKey("common.save.success"));

			labResultNumber.setValue(labResult.getVLabRsltCode());
			labResultNumber.setAttribute("nota", labResult);

			setFieldDisable(true);
			setButtonActive(false);

		} else {
			Messagebox.show(MessagesService.getKey("common.save.fail"));
		}

	}
}

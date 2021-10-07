package com.vone.medisafe.service.ifaceimpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
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
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsShift;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbPatientSettlement;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.mapping.dao.PolyclinicDAO;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.NoteManager;

public class NoteManagerImpl implements NoteManager{
	
	private NoteDAO dao;
	private PolyclinicDAO polyDao;
	

	public NoteDAO getDao() {
		return dao;
	}

	public void setDao(NoteDAO dao) {
		this.dao = dao;
	}

	public boolean save(TbExamination exam) throws VONEAppException{
		// TODO Auto-generated method stub
		//notaJournal = (TbExamination) getHibernateTemplate().get(TbExamination.class, note.getNExamId());
		MsShift msShift = exam.getMsShift();
//		Integer examStatus = exam.getNExamStatus();
//		Date tgl = exam.getDValidationDate();
//		
//		exam = dao.getNote(exam.getNExamId());
//		exam.setMsShift(msShift);
//		exam.setNExamStatus(examStatus);
//		exam.setDValidationDate(tgl);
		Integer stat = exam.getNExamStatus();
		Date valDate = exam.getDValidationDate();
		String validatedBy = exam.getVWhoChange();
		
		exam = dao.getNote(exam.getNExamId());
		dao.save(exam);
		
		exam.setNExamStatus(stat);
		exam.setDValidationDate(valDate);
		exam.setMsShift(msShift);
		exam.setVWhoChange(validatedBy);
		dao.saveOrUpdateNote(exam);
		
		return true;
	}

	

	
	public TbExamination getNoteById(Integer id) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getNote(id);
	}


	public boolean saveModifyNote(TbExamination note, Set<TbItemTrx> itemTrxs, Set<TbTreatmentTrx> treatTrxs, Set<TbBundledTrx> bundleTrxs, Set<TbMiscTrx> miscTrxs, Set<TbDrugIngredients> racikans, Integer warehouseId) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.saveModifyNote(note, itemTrxs, treatTrxs, bundleTrxs, miscTrxs, racikans, warehouseId);
	}


	public List searchNoteLab(String noteNumber, String patientName, MsUnit unit) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.searchNoteLab(noteNumber, patientName, unit);
	}

	public List<TbExamination> getNotesByRegistration(TbRegistration reg) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getNotesByRegistration(reg);
	}

	public void saveTrx(Listbox theList, MsUser user, Set<TbTreatmentTrx> treatmentSet, Set<TbItemTrx> itemSet, Set<TbMiscTrx> miscSet) throws VONEAppException {
		List list = theList.getItems();
		Iterator it = list.iterator();
		
		MsStaff doctorLab = (MsStaff)theList.getAttribute("doctorLab");
		
		while(it.hasNext()){
			Listitem item = (Listitem)it.next();
			Object[] obj = (Object[])item.getAttribute("manipulation");
			if(item.getValue() instanceof MsTreatmentFee){
				TbTreatmentTrx treatmentTrx = new TbTreatmentTrx();
				treatmentTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
//				listitem.setAttribute("doctor",examinerDoctorList.getSelectedItem().getValue());
//				listitem.setAttribute("radiografer",radiograferList.getSelectedItem().getValue());
				if(item.getAttribute("doctor") instanceof MsStaff){
					treatmentTrx.setMsDoctor((MsStaff)item.getAttribute("doctor"));
				}
				if(item.getAttribute("radiografer") instanceof MsStaff){
					treatmentTrx.setAnastesiStaff((MsStaff)item.getAttribute("radiografer"));
				}

				if(doctorLab != null)treatmentTrx.setMsDoctor(doctorLab);
				treatmentTrx.setVWhoCreate(user.getVUserName());
				treatmentTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				treatmentTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				treatmentTrx.setNQty((short)1);
				treatmentTrx.setVDiscType(obj[2].toString());
				treatmentTrx.setVWhoCreate(user.getVUserName());
				treatmentTrx.setMsTreatmentFee((MsTreatmentFee)item.getValue());
				treatmentSet.add(treatmentTrx);
			}
			else if(item.getValue() instanceof Object[]){
				TbItemTrx itemTrx = new TbItemTrx();
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
			else if(item.getValue() instanceof TbMiscTrx){
				TbMiscTrx miscTrx = (TbMiscTrx)item.getValue();
				miscTrx.setVWhoCreate(user.getVUserName());
				miscTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				miscTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				miscTrx.setVDiscType(obj[2].toString());
				miscTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				miscSet.add(miscTrx);
			}
		}
	}

//	public boolean saveModifyNote(TbExamination nota, Set<TbItemTrx> itemSet, Set<TbTreatmentTrx> treatmentSet, Set<TbBundledTrx> bundledSet, Set<TbMiscTrx> miscSet, Object object, Listbox locationList) throws VONEAppException {
//		Integer warehouseId = ((MsUnit)locationList.getSelectedItem().getValue()).getMsWarehouse().getNWhouseId();
//		//return dao.saveModifyNote(note, itemTrxs, treatTrxs, bundleTrxs, miscTrxs, racikans, warehouseId);
//		return dao.saveModifyNote(nota, itemSet, treatmentSet, bundledSet, miscSet, null, warehouseId);
//	}
	
	
	
	public void searchNote(String noteNumber, String patientName, MsUnit unit, Listbox notaList, int statusNota)
		throws VONEAppException {
		
		List<TbExamination> notas = this.dao.searchNote(noteNumber, patientName, unit, statusNota);
		
		Listitem item;
		Listcell cell;
		
		
		for(TbExamination note : notas){
			
			item = new Listitem();
			item.setValue(note);
			item.setParent(notaList);
			
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(note.getVNoteNo());
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(note.getMsPatient().getVPatientName());
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(MedisafeUtil.getNoteStatus(note.getNExamStatus().intValue()));
			
		}
		
	}

	
	public void cancelNote(String alasanBatal) throws VONEAppException, InterruptedException {
		
		Object[] cancel = (Object[])Sessions.getCurrent().getAttribute("cancelNote");
		Integer type = (Integer)cancel[2];
		if(type.intValue() == MedisafeConstants.NOTA){
			TbExamination nota = (TbExamination)cancel[0];

			Button btnValidation = (Button)cancel[3];
			Button btnModify = (Button)cancel[4];
			Button btnCancelNote = (Button)cancel[5];
			Label label = (Label)cancel[6];
			
			String akses = cancel[7].toString();

			
			if(nota.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
			
				if(!akses.equalsIgnoreCase("SPV")){
					Messagebox.show(MessagesService.getKey("tidak.berhak.batal"));
					return;
				}
				
			}
			
			if(nota.getNExamStatus().intValue() == MedisafeConstants.CANCEL_NOTE){
				Messagebox.show(MessagesService.getKey("common.nota.has.been.validated"));
				return;
			}
			
		
			
			
			
			
			if(this.dao.cancelNote(nota, (Integer)cancel[1],alasanBatal)){
				Messagebox.show(MessagesService.getKey("common.nota.cancelatioan.success"));
				label.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
				btnCancelNote.setDisabled(true);
				btnValidation.setDisabled(true);
				btnModify.setDisabled(true);
				
			}
			else{
				Messagebox.show(MessagesService.getKey("common.nota.cancelatioan.fail"));
			}
					
			
		}
		
	}
	
	

	public void getNoteDetil(TbExamination nota, Listbox notalist) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		notalist.getItems().clear();
		
		nota = this.dao.getNote(nota.getNExamId());
		
		Set<TbTreatmentTrx> treatmentTrxList = nota.getTbTreatmentTrx();
		List itemTrxList = this.polyDao.getItemTrx(nota);
		Set<TbBundledTrx> bundletTrxList = nota.getTbBundledTrx();
		Set<TbMiscTrx> miscTrxList = nota.getTbMiscTrx();

		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		double hargaSatuan = 0;
		
		
		for(TbTreatmentTrx treatmentTrx : treatmentTrxList){
		
			item = new Listitem();
			item.setValue(treatmentTrx);
			item.setParent(notalist);
			
			//tratment code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(treatmentTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode());
			
			//treatment name
			cell = new Listcell();
			cell.setParent(item);
			if(treatmentTrx.getMsDoctor() != null){
				cell.setLabel(treatmentTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName()+"-"+
						treatmentTrx.getMsDoctor().getVStaffName());
			}
			else
				cell.setLabel(treatmentTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
			
			//treatment quantity
			cell = new Listcell();
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(treatmentTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);
//			cell.setLabel("1");
			
			//satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel("-");
			
			hargaSatuan = treatmentTrx.getNAmountTrx() / treatmentTrx.getNQty();
			
			db.setValue(new BigDecimal(hargaSatuan));
			//treatment price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			
			//discount
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(treatmentTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(treatmentTrx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			
			
			db.setValue(new BigDecimal(treatmentTrx.getNAmountAfterDisc()));
			
			//treatment price after discount			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
		
		
		
		for(TbBundledTrx bundledTrx : bundletTrxList){
			  
			item = new Listitem();
			item.setValue(bundledTrx);
			item.setParent(notalist);
			
			//bundle code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(bundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode());
			
			//bundle name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(bundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
			
			//bundle quantity
			cell = new Listcell();
			
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(bundledTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);
			
			cell.setParent(item);
//			cell.setLabel("1");
			
//			satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel("-");
			
			hargaSatuan = bundledTrx.getNAmountTrx()/bundledTrx.getNQty();
			db.setValue(new BigDecimal(hargaSatuan));
			//bundle price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			
			//discount
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(bundledTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(bundledTrx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);

			
			
			db.setValue(new BigDecimal(bundledTrx.getNAmountAfterDisc()));
			
			//item price after discount			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
		
		Iterator it = itemTrxList.iterator();

		
		while(it.hasNext()){
			//obj[0] = item id
			//obj[1] = item code
			//ojb[2] = item name
			//obj[3] = item quantify
			//obj[4] = item qty
			//obj[5] = trx value
			//obj[6] = discount value
			//obj[7] = subtotal value
			
			Object[] obj = (Object[])it.next();
			item = new Listitem();
			item.setValue(obj);
			item.setParent(notalist);
			
			//item code
			cell = new Listcell(obj[1].toString());
			cell.setParent(item);
//			cell.setLabel(itemTrx.getMsItem().getVItemCode());
			
			//item name
			cell = new Listcell(obj[2].toString());
			cell.setParent(item);

			
			//item quantity
			cell = new Listcell();
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(obj[4].toString()));
			jumlah.setParent(cell);
			cell.setParent(item);

			
//			satuan
			cell = new Listcell(obj[3].toString());
			cell.setParent(item);

			
			
			hargaSatuan = (Double) obj[5] / (Integer)obj[4];
			db.setValue(new BigDecimal(hargaSatuan));
			//item price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			

			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal((Double)obj[6]));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(obj[9].toString().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
//			cell.setLabel();
//			cell.setLabel(db.getText());
			
			

			db.setValue(new BigDecimal((Double)obj[7]));
			
			//item price after discount			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
		
		//MISC TRX
		
		for(TbMiscTrx tbMiscTrx : miscTrxList){
			
			item = new Listitem();
			item.setValue(tbMiscTrx);
			item.setParent(notalist);
			
			//tratment code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(MedisafeConstants.MISC_CODE);
			
			//treatment name
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(tbMiscTrx.getVMiscName());
			
			//treatment quantity
			cell = new Listcell();
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(tbMiscTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);
//			cell.setLabel("1");
			
			//satuan
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel("-");
			
			hargaSatuan = tbMiscTrx.getNAmountTrx() / tbMiscTrx.getNQty();
			
			db.setValue(new BigDecimal(hargaSatuan));
			//treatment price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			
			//discount
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(tbMiscTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(tbMiscTrx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			
			
			db.setValue(new BigDecimal(tbMiscTrx.getNAmountAfterDisc()));
			
			//treatment price after discount			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
		
		MiscTrxController.setFont(notalist);

		
	}

	public PolyclinicDAO getPolyDao() {
		return polyDao;
	}

	public void setPolyDao(PolyclinicDAO polyDao) {
		this.polyDao = polyDao;
	}

	
	
	public MsUnit getUnitNote(TbExamination exam) throws VONEAppException {
		
		
		
		
		MsUnit unit = null;
		if(exam.getMsUnit() != null)
			unit = dao.getUnitById(exam.getMsUnit().getNUnitId());
		
		return unit;
	
	}

	
	public void searchNote(Textbox nomorNota, Textbox namaPasien, Datebox tglMulai, Datebox tglAkhir, 
			Listbox hasilList, Listbox locationList) throws VONEAppException {
		
		nomorNota.setText(nomorNota.getText().toUpperCase());
		namaPasien.setText(namaPasien.getText().toUpperCase());
		
		hasilList.getItems().clear();
		
		Listcell cell;
		Listitem item;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(tglMulai.getValue() == null){
			
			tglMulai.setValue(new Date());
//			
//			try {
//				Messagebox.show(MessagesService.getKey("tanggal.mulai.tidak.boleh.kosong"));
//				return;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		if(tglAkhir.getValue() == null){
			
			tglAkhir.setValue(tglMulai.getValue());
		}
		
//		String tanggalMulai = tglMulai.getText() +" "+"00:00:00";
//		String tanggalAkhir = tglAkhir.getText()+" "+"23:59:59";
		
	
		String unitCode = ((MsUnit)locationList.getSelectedItem().getValue()).getVUnitCode();
		List<TbExamination> list = null;
		
		try {
			 Date tgl1 = sdf.parse(sdf.format(tglMulai.getValue()));
			 Date tgl2 = sdf.parse(sdf.format(tglAkhir.getValue()));
			 list = dao.searchNote("%"+nomorNota.getText()+"%","%"+namaPasien.getText()+"%",
					tgl1,tgl2, "%"+unitCode+"%");
			 
			 
		} catch (WrongValueException e) {} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		for(TbExamination nota : list){
			System.out.println(nota.getVNoteNo());
			item = new Listitem();
			item.setValue(nota);
			item.setParent(hasilList);
			
			cell = new Listcell(nota.getVNoteNo());
			cell.setParent(item);
			
			cell = new Listcell(nota.getMsPatient().getVPatientName());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.getNoteStatus(nota.getNExamStatus()));
			cell.setParent(item);
		}
		MiscTrxController.setFont(hasilList);
	}

	public void searchNote(Textbox nomorNota, Textbox namaPasien, Textbox noMr, Textbox nomorResep, 
		Datebox tglMulai, Datebox tglAkhir, Listbox hasilList, Listbox locationList) throws VONEAppException {
		
		
		nomorNota.setText(nomorNota.getText().toUpperCase());
		namaPasien.setText(namaPasien.getText().toUpperCase());
		
		hasilList.getItems().clear();
		
		Listcell cell;
		Listitem item;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		if(tglMulai.getValue() == null){
			
			tglMulai.setValue(new Date());
//			
//			try {
//				Messagebox.show(MessagesService.getKey("tanggal.mulai.tidak.boleh.kosong"));
//				return;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		if(tglAkhir.getValue() == null){
			
			tglAkhir.setValue(tglMulai.getValue());
		}
		
		String tanggalMulai = tglMulai.getText() +" "+"00:00:00";
		String tanggalAkhir = tglAkhir.getText()+" "+"00:00:00";
		
	
		String unitCode = ((MsUnit)locationList.getSelectedItem().getValue()).getVUnitCode();
		List<TbExamination> list = null;
		
		try {
			 Date tgl1 = sdf.parse(tanggalMulai);
			 Date tgl2 = sdf.parse(tanggalAkhir);
			 tgl2.setDate(tgl2.getDate()+1);
			 
			 
			 list = dao.searchNote("%"+nomorNota.getText()+"%","%"+namaPasien.getText()+"%",
					 noMr.getText(), "%"+nomorResep.getText()+"%",tgl1,tgl2, "%"+unitCode+"%");
			 
			 
		} catch (WrongValueException e) {
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		for(TbExamination nota : list){
			System.out.println(nota.getVNoteNo());
			item = new Listitem();
			item.setValue(nota);
			item.setParent(hasilList);
			
			cell = new Listcell(nota.getVNoteNo());
			cell.setParent(item);
			
			cell = new Listcell(nota.getMsPatient().getVPatientName());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.getNoteStatus(nota.getNExamStatus()));
			cell.setParent(item);
		}
		MiscTrxController.setFont(hasilList);

		
	}

	public void getPendapatanDokter(MsStaff staff, String tipeLaporan, Date dateFrom, Date dateTo, Listbox pendapatanDokterList, Decimalbox db, String patientType) {
		pendapatanDokterList.getItems().clear();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		String tgl1 = sdf.format(dateFrom)+" 00:00:00";
		String tgl2 = sdf.format(dateTo) + " 23:59:59";
		
		
		
		double jumlah = 0;
		
		Listitem item;
		Listcell cell;
		
		try {
			if(tipeLaporan.equalsIgnoreCase("PD")){
				jumlah = 0;
				
				List l = this.dao.getPendapatanDokter(staff.getNStaffId(), tipeLaporan, dateFrom, dateTo, patientType);
				Iterator itr = l.iterator();
				while (itr.hasNext()){
					Object[] o = (Object[])itr.next();
					
					item = new Listitem();
					item.setValue(o);
					item.setParent(pendapatanDokterList);
					
					//no nota
					cell = new Listcell((String)o[0]);
					cell.setParent(item);
					
					//kode
					cell = new Listcell((String)o[7]);
					cell.setParent(item);
					
					//tindakan
					cell = new Listcell((String)o[1]);
					cell.setParent(item);
					
					//validate by
					cell = new Listcell((String)o[8]);
					cell.setParent(item);
					
					//pasien
					cell = new Listcell((String)o[2]);
					cell.setParent(item);
					
					//tipe
					cell = new Listcell((String)o[3]);
					cell.setParent(item);
					
					//kelas tarif
					cell = new Listcell((String)o[4]);
					cell.setParent(item);
					
					//tgl tindakan
					cell = new Listcell(sdf2.format((Date)o[5]));
					cell.setParent(item);
					
					//nilai jasa dokter
					db.setValue(new BigDecimal((Double)o[6]));
					cell = new Listcell(db.getText());
					jumlah = jumlah + db.getValue().doubleValue();
					cell.setParent(item);
					
					
				}
				/*
				List<TbTreatmentTrx> treats = this.dao.getPendapatanDokter(staff.getNStaffId(), tipeLaporan, tgl1, tgl2, patientType);
				for(TbTreatmentTrx treat : treats){
					
					item = new Listitem();
					item.setValue(treat);
					item.setParent(pendapatanDokterList);
					
					//no nota
					cell = new Listcell(treat.getTbExamination().getVNoteNo());
					cell.setParent(item);
					
					//treatment name
					cell = new Listcell(treat.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
					cell.setParent(item);
					
					//pasien name
					cell = new Listcell(treat.getTbExamination().getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName());
					cell.setParent(item);
					
					//tipe pasien
					String tipePasien="NON BPJS";
					if(treat.getTbExamination().getTbRegistration().getTbMedicalRecord().getMsPatient().getMsPatientType() != null && 
							treat.getTbExamination().getTbRegistration().getTbMedicalRecord().getMsPatient().getMsPatientType().getVTpatient().equalsIgnoreCase("4"))
						tipePasien = "BPJS";
					cell = new Listcell(tipePasien);
					cell.setParent(item);
					
					//kelas tarif
					cell = new Listcell(treat.getMsTreatmentFee().getMsTreatmentClass().getVTclassDesc());
					cell.setParent(item);
					
					//tgl tindakan
					cell = new Listcell(sdf2.format(treat.getDWhnCreate()));
					cell.setParent(item);
					
					//nilai jasa dokter
					db.setValue(new BigDecimal(treat.getMsTreatmentFee().getNDoctorFee()));
					cell = new Listcell(db.getText());
					jumlah = jumlah + treat.getMsTreatmentFee().getNDoctorFee().doubleValue();
					cell.setParent(item);
					
					
				}
				
				List<TbTreatmentTrx> treatments = this.dao.getPendapatanDokter(staff.getNStaffId(), tgl1, tgl2, patientType);
				for(TbTreatmentTrx treat : treatments){
					
					item = new Listitem();
					item.setValue(treat);
					item.setParent(pendapatanDokterList);
					
					//no nota
					cell = new Listcell(treat.getTbExamination().getVNoteNo());
					cell.setParent(item);
					
					//treatment name
					cell = new Listcell(treat.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
					cell.setParent(item);
					
					//pasien name
					cell = new Listcell(treat.getTbExamination().getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName());
					cell.setParent(item);
					
					//tipe pasien
					String tipePasien="NON BPJS";
					if(treat.getTbExamination().getTbRegistration().getTbMedicalRecord().getMsPatient().getMsPatientType() != null && 
							treat.getTbExamination().getTbRegistration().getTbMedicalRecord().getMsPatient().getMsPatientType().getVTpatient().equalsIgnoreCase("4"))
						tipePasien = "BPJS";
					cell = new Listcell(tipePasien);
					cell.setParent(item);
					
					//kelas tarif
					cell = new Listcell(treat.getMsTreatmentFee().getMsTreatmentClass().getVTclassDesc());
					cell.setParent(item);
					
					//tgl tindakan
					cell = new Listcell(sdf2.format(treat.getDWhnCreate()));
					cell.setParent(item);
					
					//nilai jasa dokter
					db.setValue(new BigDecimal(treat.getMsTreatmentFee().getNDoctorFee()));
					cell = new Listcell(db.getText());
					jumlah = jumlah + treat.getMsTreatmentFee().getNDoctorFee().doubleValue();
					cell.setParent(item);
					
					
				}
				
				*/
				
				db.setValue(new BigDecimal(jumlah));
			
			}
	
			else{ //penjualan obat
				List<TbExamination> notas = this.dao.getNoteByDokter(staff.getNStaffId(), tgl1, tgl2, patientType);
				
				Set<TbItemTrx> items = null;
				Set<TbDrugIngredients> racikans = null;
				
				jumlah = 0;
				double jumlahLokal = 0;
				
				for(TbExamination nota : notas){
					//nota, nama pasien, tgl transaksi, nilai
					
					jumlahLokal = 0;
					items = nota.getTbItemTrx();
					for(TbItemTrx itrx : items){
						jumlahLokal = jumlahLokal + itrx.getNAmountAfterDisc();
						jumlah = jumlah + itrx.getNAmountAfterDisc();
					}
					
					racikans = nota.getTbDrugIngredients();
					for(TbDrugIngredients drug : racikans){
						jumlahLokal = jumlahLokal + drug.getNAmountAfterDisc();
						jumlah = jumlah + drug.getNAmountAfterDisc();
					}
					
					if(jumlahLokal > 0){
					
					item = new Listitem();
					item.setValue(nota);
					item.setParent(pendapatanDokterList);
					
					cell = new Listcell(nota.getVNoteNo());
					cell.setParent(item);
					
					cell = new Listcell(nota.getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName());
					cell.setParent(item);
					
					cell = new Listcell(sdf2.format(nota.getDWhnCreate()));
					cell.setParent(item);
					
					
					db.setValue(new BigDecimal(jumlahLokal));
					cell = new Listcell(db.getText());
					cell.setParent(item);
					}
					
				}
				
				db.setValue(new BigDecimal(jumlah));
				
			}
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void getNoteByRangeDate(Datebox date, Datebox date2, Listbox notalist, Intbox jumR, Intbox nilR) throws VONEAppException {
		
		if(date.getValue() == null || date2.getValue() == null){
			try {
				Messagebox.show("KEDUA KOLOM TANGGAL HARUS DIISI!");
				return;
			} catch (InterruptedException e) {
				
			}
		}
		
		notalist.getItems().clear();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		String tgl1 = sdf.format(date.getValue()) + " 00:00:00";
		String tgl2 = sdf.format(date2.getValue()) + " 23:59:59";
		
		Listitem item;
		Listcell cell;
		Integer r=0;
		int nilaiR = 0;
		int totalR = 0;
		
		List<TbExamination> notas = this.dao.getNoteBaseOnRangeDate(tgl1, tgl2);
		
		for (TbExamination nota : notas) {
			nilaiR = 0;
			if(nota.getTbItemTrx().size()> 0 || nota.getTbDrugIngredients().size()> 0){
				item = new Listitem();
				item.setParent(notalist);
				
				cell = new Listcell(nota.getVNoteNo());
				cell.setParent(item);
				
				cell = new Listcell(sdf2.format(nota.getDWhnCreate()));
				cell.setParent(item);
				
				
				Set<TbItemTrx> items = nota.getTbItemTrx();
				for(TbItemTrx itm :items){
					if(itm.getNQty() > 0){
						nilaiR = nilaiR + itm.getMsItem().getNR();
						totalR = totalR + itm.getMsItem().getNR();
					}
				}
				
				Set<TbDrugIngredients> detils = nota.getTbDrugIngredients();
				for(TbDrugIngredients drug : detils){
					nilaiR = nilaiR + drug.getNEr();
					totalR = totalR + drug.getNEr();
				}
				
				cell = new Listcell(""+nilaiR);
				cell.setParent(item);
				
				r = new Integer(MessagesService.getKey("jasa.apotik"));
				
				cell = new Listcell(nilaiR * r.intValue() + "");
				cell.setParent(item);
			}
		}
		
		jumR.setValue(totalR);
		nilR.setValue(totalR * r);
		
		
		
	}
	
	/*
	 * 
	 * added by aziiz
	 * 30-03-2010
	 * 
	 */ 
	public void getRawatInapJalan(Date dateFrom, Date dateTo, String tipeLaporan, Listbox resultList, Decimalbox dBox) throws VONEAppException
	{
		resultList.getItems().clear();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		String tgl1 = sdf.format(dateFrom)+" 00:00:00";
		String tgl2 = sdf.format(dateTo) + " 23:59:59";
		Decimalbox dbox = new Decimalbox();
		dbox.setFormat("#,###.#");
		
		Listitem item;
		Listcell cell;
		
		double umur = 0;
		String etnis = null;
		String language = null;
		
		//rawat inap
		if(tipeLaporan.equalsIgnoreCase("RI"))
		{
			List<TbRegistration> regs = this.dao.getBedTransaction(tgl1, tgl2);
			for(TbRegistration reg : regs)
			{
				String bedCode = null;
				String bedClassTariff = null;
				int days = 0;
				String bedTmp = null;
				
				Date tglKeluar = null;
				
				Set<TbExamination> exams = reg.getTbExaminations();
				for(TbExamination exam : exams)
				{
					Set<TbBedTrx> beds = exam.getTbBedTrx();
					if (beds.size() > 0){
						for(TbBedTrx bed : beds)
						{
							if(tglKeluar != null){
							   if(bed.getDWhoCreate().compareTo(tglKeluar) > 0) tglKeluar = bed.getDWhoCreate();
							}else tglKeluar = bed.getDWhoCreate(); 
							
							bedCode = bed.getMsBed().getVBedDesc();
							bedClassTariff = bed.getMsBed().getMsTreatmentClass().getVTclassDesc();
							days += bed.getNTotalHour();
							//total += bed.getNAmountAfterDisc();
						}
					}
				}
				
				String diagnosa = "";
				
				List<TbDiagnose> diags = this.dao.getDiagnose(reg.getNRegId());
				for(TbDiagnose diag : diags){
					Set<TbIcdDiagnose> icds = diag.getTbIcdDiagnoses();
					for(TbIcdDiagnose icd : icds){
						diagnosa = diagnosa +icd.getMsIcd().getVIcdName()+",";
					}
				}
				if(diagnosa.length() > 1) diagnosa = diagnosa.substring(0, diagnosa.length()-1);
				
				item = new Listitem();
				item.setValue(reg);
				item.setParent(resultList);
				
				//no medical record
				cell = new Listcell(reg.getTbMedicalRecord().getVMrCode());
				cell.setParent(item);
				
				//nama pasien
				cell = new Listcell(reg.getTbMedicalRecord().getMsPatient().getVPatientName());				
				cell.setParent(item);
				
				//JK
				cell = new Listcell(reg.getTbMedicalRecord().getMsPatient().getVPatientGender().equalsIgnoreCase("M")? "L" : "P");
				cell.setParent(item);
				
				//tanggal lahir
				cell = new Listcell(sdf2.format(reg.getTbMedicalRecord().getMsPatient().getDPatientDob()));
				cell.setParent(item);
				
				//umur
				umur = calculateAge(reg.getTbMedicalRecord().getMsPatient().getDPatientDob());
				dbox.setValue(new BigDecimal(umur));
				cell = new Listcell(dbox.getText());
				cell.setParent(item);
				
				//tipe pasien
				//get tipe pasien berdasarkan cara bayar
				List<TbPatientSettlement> settlements = dao.getBpjsSettlement(reg.getNRegId());
				cell = new Listcell(settlements.size() > 0 ? "BPJS" : "NONBPJS");
				cell.setParent(item);
				
				//get status pasie lama/baru
				List<TbRegistration> tbRegs = dao.getStatusPasien(reg.getTbMedicalRecord().getNMrId(), 1);
				cell = new Listcell(tbRegs.size() > 1 ? "PASIEN LAMA" : "PASIEN BARU");
				cell.setParent(item);
				
				//agama
				cell = new Listcell(reg.getTbMedicalRecord().getMsPatient().getVPatientReligion());
				cell.setParent(item);
				
				//suku
				if (reg.getTbMedicalRecord().getMsPatient().getVEtnis() == null 
						|| reg.getTbMedicalRecord().getMsPatient().getVEtnis().equalsIgnoreCase(MedisafeConstants.LISTKOSONG))
					etnis = "-";
				else etnis = reg.getTbMedicalRecord().getMsPatient().getVEtnis();
				
				cell = new Listcell(etnis);
				cell.setParent(item);
				
				//bahasa
				if(reg.getTbMedicalRecord().getMsPatient().getVLanguage() == null 
						|| reg.getTbMedicalRecord().getMsPatient().getVLanguage().equalsIgnoreCase(MedisafeConstants.LISTKOSONG))
					language = "-";
				else language = reg.getTbMedicalRecord().getMsPatient().getVLanguage();
				cell = new Listcell(language);
				cell.setParent(item);
				
				//dokter utama
				cell = new Listcell(reg.getMsStaff().getVStaffName());
				cell.setParent(item);
				
				//bed
				cell = new Listcell(bedCode);
				cell.setParent(item);
				
				//kelas tarif
				cell = new Listcell(bedClassTariff);
				cell.setParent(item);
				
				//tgl masuk
				cell = new Listcell(sdf2.format(reg.getDWhnCreate()));
				cell.setParent(item);
				
				//tgl keluar
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(reg.getDWhnCreate());
//				cal.add(Calendar.DATE, days - 1);
				cell = new Listcell(tglKeluar == null ? "-" : sdf2.format(tglKeluar));
				cell.setParent(item);
				
				//lama perawatan
				cell = new Listcell(days+"");
				cell.setParent(item);
				
				cell = new Listcell(diagnosa);
				cell.setParent(item);
				
			}
			
		}
		else
		{
			//laporan rawat jalan
//			List<TbRegistration> regs = this.dao.getRegistration("J", tgl1, tgl2);
			List<Object> objs = this.dao.getLaporanDiagnosaRajal(tgl1, tgl2);
			for(Object reg : objs)
			{
				item = new Listitem();
				item.setValue(reg);
				item.setParent(resultList);
				
				Object[] o = (Object[])reg;
				
				//no mr
				cell = new Listcell((String)o[4]);
				cell.setParent(item);
				
				//nama pasien
				cell = new Listcell((String)o[5]);
				cell.setParent(item);
				
				//jk
				String jk = (String)o[9];
				cell = new Listcell(jk.equalsIgnoreCase("M")? "L" : "P");
				cell.setParent(item);
				
				Date tglLahir = (Date)o[7];
				umur = calculateAge(tglLahir);
				dbox.setValue(new BigDecimal(umur));
				
				//tanggal lahir
				cell = new Listcell(sdf2.format(tglLahir));
				cell.setParent(item);
				
				//umur
				cell = new Listcell(dbox.getText());
				cell.setParent(item);
				
				//tipe pasien
				List<TbPatientSettlement> settlements = dao.getBpjsSettlement((Integer)o[0]);
				cell = new Listcell(settlements.size() > 0 ? "BPJS" : "NONBPJS");
				cell.setParent(item);
				
				//status pasie lama/baru
				List<TbRegistration> tbRegs = dao.getStatusPasien((Integer)o[12], 0);
				cell = new Listcell(tbRegs.size() > 1 ? "PASIEN LAMA" : "PASIEN BARU");
				cell.setParent(item);
				
				//agama
				cell = new Listcell((String)o[8]);
				cell.setParent(item);
				
				//etnis
				etnis = (String)o[10];
				cell = new Listcell(etnis == null || etnis.equalsIgnoreCase(MedisafeConstants.LISTKOSONG) ? "-" : etnis );
				cell.setParent(item);
				
				//bahasa
				language = (String)o[11];
				cell = new Listcell(language == null || language.equalsIgnoreCase(MedisafeConstants.LISTKOSONG) ? "-" : language );
				cell.setParent(item);
				
				//tgl daftar
				cell = new Listcell(sdf2.format((Date)o[1]));
				cell.setParent(item);
				
				//unit
				cell = new Listcell((String)o[2]);
				cell.setParent(item);
				
				//dokter
				cell = new Listcell((String)o[3]);
				cell.setParent(item);
				
				//Diagnosa
				cell = new Listcell((String)o[6]);
				cell.setParent(item);
				
				
			}
			
		}
	}
	
	public double calculateAge(Date tglLahir){
		Date sekarang = new Date();
		long diff = sekarang.getTime() - tglLahir.getTime();
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return (diffDays/(double)365);
	}
	/*
	 * added by aziiz
	 * 30-03-2010
	 */
	public void getRekapObat(Date dateFrom, Date dateTo, Listbox resultList, Decimalbox dBox) throws VONEAppException
	{
		resultList.getItems().clear();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		String tgl1 = sdf.format(dateFrom)+" 00:00:00";
		String tgl2 = sdf.format(dateTo) + " 23:59:59";
		
		Decimalbox dbox = new Decimalbox();
		dbox.setFormat("#,###.##");
		
		Listitem item;
		Listcell cell;
		
//		List recap = this.dao.getRekapObat(tgl1, tgl2);
		List recap = this.dao.getRekapObatNew(tgl1, tgl2);
		double totalPenjualan = 0;
		double hargaSatuan = 0;
		int i = 0;
		
		Iterator it = recap.iterator();
		while(it.hasNext())
		{
			Object[] obj = (Object[])it.next();
			item = new Listitem();
			item.setValue(obj);
			item.setParent(resultList);
			
			//no urut
			cell = new Listcell(Integer.toString(++i));
			cell.setParent(item);
			
			
			//nama obat
			cell = new Listcell(obj[3].toString());
			cell.setParent(item);
		
			/**
			Integer itemid = new Integer(obj[2].toString());
//			total transaksi dari TbDrugIngredientsDetail
			List<TbDrugIngredientsDetail> ings = this.dao.getDrugIngredientsDetail(itemid, tgl1, tgl2);
			int totalqty = 0;
			double totalracikan = 0;
			for(TbDrugIngredientsDetail ing : ings)
			{
				
				totalqty += ing.getNDingrDetQty();
				//totalracikan += ing.getTbDrugIngredients().getNAmountAfterDisc();
			} */
			
			
			//qty penjualan
			Double qty = new Double(obj[0].toString());
//			totalqty += qty;
			dbox.setValue(new BigDecimal(qty));
			cell = new Listcell(dbox.getText());
			cell.setParent(item);
			
			//total penjualan
			double d = (Double)obj[1];
//			hargaSatuan = d/qty;
			
//			totalPenjualan += totalqty * hargaSatuan;
//			d += totalracikan;
//			dbox.setValue(new BigDecimal(totalqty * hargaSatuan));
			dbox.setValue(new BigDecimal(d));
			cell = new Listcell(dbox.getText());
			cell.setParent(item);
			
			totalPenjualan += d;
			
			
			
			
			
			
			
		}
		dBox.setValue(new BigDecimal(totalPenjualan));
	}

	
	public void getDoctorReportAll(Listbox doctorList, Date from, Date to,
			Listbox laporan, Decimalbox totalPendapatan, String patientType)
			throws VONEAppException {
		laporan.getItems().clear();
		
		
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		String tgl1 = sdf.format(from)+" 00:00:00";
		String tgl2 = sdf.format(to) + " 23:59:59";
		
		
		double totalJasa = 0;
		double totalObat = 0;
		
		Listitem itm = null;
		Listcell cell = null;
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		Set<TbItemTrx> itemsTrx = null;
		Set<TbDrugIngredients> racikans = null;
		
		List<Listitem> items = doctorList.getItems();
		for(Listitem item : items){
			//the value of list a staff object
			MsStaff staff = (MsStaff)item.getValue();
			totalJasa = 0;
			totalObat = 0;
			
			itm = new Listitem();
			itm.setParent(laporan);
			cell = new Listcell(staff.getVStaffName());
			cell.setParent(itm);
			
			List<Object[]> l = this.dao.getPendapatanDokter(staff.getNStaffId(), null, from, to, patientType);
			for(Object[] o : l){
				db.setValue(new BigDecimal((Double)o[6]));
				totalJasa = totalJasa + db.getValue().doubleValue();
			}
				
			//calculate doctor fee
			/*
			List<TbTreatmentTrx> treats = this.dao.getPendapatanDokter(staff.getNStaffId(), null, tgl1, tgl2, patientType);
			for(TbTreatmentTrx treat : treats){
				totalJasa = totalJasa + treat.getMsTreatmentFee().getNDoctorFee().doubleValue();
			}
			
			List<TbTreatmentTrx> jasas = this.dao.getPendapatanDokter(staff.getNStaffId(), tgl1, tgl2, patientType);
			for(TbTreatmentTrx jasa : jasas){
				totalJasa = totalJasa + jasa.getMsTreatmentFee().getNDoctorFee().doubleValue();
			}*/
			
			db.setValue(new BigDecimal(totalJasa));
			cell = new Listcell(db.getText());
			cell.setParent(itm);
			
			List<TbExamination> notas = this.dao.getNoteByDokter(staff.getNStaffId(), tgl1, tgl2, patientType);
			
			for(TbExamination nota : notas){
				itemsTrx = nota.getTbItemTrx();
				for(TbItemTrx itrx : itemsTrx){
					totalObat = totalObat + itrx.getNAmountAfterDisc();
				}
				
				racikans = nota.getTbDrugIngredients();
				for(TbDrugIngredients drug : racikans){
					totalObat = totalObat + drug.getNAmountAfterDisc();
				}
			
			}
			db.setValue(new BigDecimal(totalObat));
			cell = new Listcell(db.getText());
			cell.setParent(itm);
			
		}
		
	}
}

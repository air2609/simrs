package com.vone.medisafe.cashier;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.mapping.TbPatientDeposit;
import com.vone.medisafe.mapping.TbPatientSettlement;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRegistrationDAO;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.mapping.dao.TbKwitansiDAO;
import com.vone.medisafe.mapping.pojo.TbKwitansi;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.ui.purchasing.PORApproval;



public class CashierManagerImpl implements CashierManager{
	
	private CashierDAO dao;
	private NoteDAO noteDao;
	private TbMedicalRecordDAO mrDao;
	private TbRegistrationDAO regDao;
	private TbBedOccupancyDAO bocDao;
	private TbKwitansiDAO kwitansiDao;
	
	
	
	public TbKwitansiDAO getKwitansiDao() {
		return kwitansiDao;
	}

	public void setKwitansiDao(TbKwitansiDAO kwitansiDao) {
		this.kwitansiDao = kwitansiDao;
	}

	public TbBedOccupancyDAO getBocDao() {
		return bocDao;
	}

	public void setBocDao(TbBedOccupancyDAO bocDao) {
		this.bocDao = bocDao;
	}

	public TbRegistrationDAO getRegDao() {
		return regDao;
	}

	public void setRegDao(TbRegistrationDAO regDao) {
		this.regDao = regDao;
	}

	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}

	public CashierDAO getDao() {
		return dao;
	}
	
	public void setDao(CashierDAO dao) {
		this.dao = dao;
	}
	
	
	
	public List<TbBedTrx> getBedTrx(List<TbExamination> notes) throws VONEAppException {
		List<TbBedTrx> hasil = new ArrayList<TbBedTrx>();
		List result = null;
		for(TbExamination note : notes){
			result = dao.getBedTrx(note);
			Iterator it = result.iterator();
			while(it.hasNext()){
				hasil.add((TbBedTrx)it.next());
			}
		}
		
		return hasil;
	}
	
	public List<TbBundledTrx> getBundlesTrx(List<TbExamination> notes) throws VONEAppException {
		List<TbBundledTrx> hasil = new ArrayList<TbBundledTrx>();
		List result = null;
		for(TbExamination nota : notes){
			result = dao.getBundleTrx(nota);
			Iterator it = result.iterator();
			while(it.hasNext()){
				hasil.add((TbBundledTrx)it.next());
			}
		}
		return hasil;
	}
	
	public List<TbItemTrx> getItemsTrx(List<TbExamination> notes) throws VONEAppException {
		List<TbItemTrx> hasil = new ArrayList<TbItemTrx>();
		List result = null;
		for(TbExamination nota : notes){
			result = dao.getItemTrx(nota);
			Iterator it = result.iterator();
			while(it.hasNext()){
				hasil.add((TbItemTrx)it.next());
			}
		}
		return hasil;
	}
	
	public List<TbTreatmentTrx> getTreatmentTrx(List<TbExamination> notes) throws VONEAppException {
		List<TbTreatmentTrx> hasil = new ArrayList<TbTreatmentTrx>();
		List result = null;
		for(TbExamination nota : notes){
			result = dao.getTreatmentTrx(nota);
			Iterator it = result.iterator();
			while(it.hasNext()){
				hasil.add((TbTreatmentTrx)it.next());
			}
		}
		return hasil;
	}

	public List<TbDrugIngredients> getRacikanTrx(List<TbExamination> notes) throws VONEAppException {
		List<TbDrugIngredients> hasil = new ArrayList<TbDrugIngredients>();
		List result = null;
		for(TbExamination nota : notes){
			result = dao.getRacikanTrx(nota);
			Iterator it = result.iterator();
			while(it.hasNext()){
				hasil.add((TbDrugIngredients)it.next());
			}
		}
		return hasil;
	}

	
	public List<TbExamination> getNotes(MsPatient patient) throws VONEAppException {
		List<TbExamination> hasil = new ArrayList<TbExamination>();
		List result = dao.getNotes(patient);
		Iterator itr = result.iterator();
		while(itr.hasNext()){
			hasil.add((TbExamination)itr.next());
		}
		return hasil;
	}

	

	
	

	public List<TbReturPharmacyDetailTrx> getRetursDetail(List<TbReturPharmacyTrx> returs) throws VONEAppException {
		// TODO Auto-generated method stub
		List<TbReturPharmacyDetailTrx> hasil = new ArrayList<TbReturPharmacyDetailTrx>();
		List result = null;
		for(TbReturPharmacyTrx retur : returs){
			result = dao.getReturDetail(retur);
			Iterator it = result.iterator();
			while(it.hasNext()){
				hasil.add((TbReturPharmacyDetailTrx)it.next());
			}
		}
		return hasil;
	}

	public boolean saveBillRetur(TbPatientBill pbill, TbReturPharmacyTrx retur) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.saveBillRetur(pbill, retur);
	}

	public boolean saveBillNote(TbPatientBill pbill, List<TbExamination> notas, List<TbPatientSettlement> 
			settlement, Decimalbox depositPaid, MsCoa coaKas) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.saveBillNotas(pbill, notas, settlement, depositPaid, coaKas);
	}

	public boolean saveDeposit(TbPatientDeposit deposit, TbPatientBill pbill, MsCoa coaKas, 
			List<TbPatientSettlement> list) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.saveDepost(deposit, pbill, coaKas, list);
	}

	public TbPatientDeposit getPatientBalance(TbRegistration reg) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getBalancet(reg);
	}

	public List<TbPatientBill> getPatientBills(String nameOnBill, String billCode,
			                    short paymentStatus, boolean isRetur) throws VONEAppException {
		List<TbPatientBill> hasil = new ArrayList<TbPatientBill>();
		List result = dao.getPatientBill(nameOnBill, billCode, paymentStatus, isRetur);
		
		Iterator it = result.iterator();
		while(it.hasNext()){
			hasil.add((TbPatientBill)it.next());
		}
		return hasil;
	}

	

	public boolean lockRegistration(TbRegistration reg) throws VONEAppException {
		
		return dao.lockOrCheckOut(reg);
	}

	
	public boolean cancelBill(TbPatientBill bill) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.cancelBill(bill);
	}

	
	
	public void cekCoa(CashierTransactionController controller) throws VONEAppException, InterruptedException {
		
		if(((MsUnit)controller.locationList.getSelectedItem().getValue()).getCoa() == null){
			controller.setButtonDisable(true);
			controller.setFieldDisabel(true);
			controller.locationList.setDisabled(false);
			Messagebox.show(MessagesService.getKey("coa.is.empty"));
			return;
		}else{
			controller.noteNumber.setDisabled(true);
			controller.setButtonDisable(true);
			controller.btnPay.setDisabled(false);
//			controller.btnEnd.setDisabled(false);
			controller.btnNew.setDisabled(false);
			controller.clear();
		}
	}

	
	
	public void searchNote(CashierTransactionController controller) throws VONEAppException, InterruptedException {
		
		Listitem item;
		Listcell cell;
		
		controller.transactionNumberList.getItems().clear();
		
		if(controller.trxNo.getText().trim().equals("") && controller.name.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			return;
		}
		
		List<TbExamination> hasils = null;
		List<TbReturPharmacyTrx> returs = null;

			
		Short jenis = new Short(controller.chooseTrx.getSelectedItem().getValue().toString());
			

			if(jenis.shortValue() == MedisafeConstants.BELUM_LUNAS_SUDAH_DIVALIDASI){
				hasils = dao.getNotes("%"+controller.name.getText().trim()+"%", "%"+controller.trxNo.getText().
						trim()+"%", MedisafeConstants.BELUM_LUNAS_SUDAH_DIVALIDASI);
			}
	
			else{
				returs = dao.getReturNotes("%"+controller.name.getText().trim()+"%", 
												"%"+controller.trxNo.getText().trim()+"%");
			}

		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		if(hasils != null){
			for(TbExamination nota : hasils){
				item = new Listitem();
				item.setValue(nota);
				item.setParent(controller.transactionNumberList);
				
				cell = new Listcell(nota.getVNoteNo());
				cell.setParent(item);
				
				cell = new Listcell(nota.getMsPatient().getVPatientName());
				cell.setParent(item);
				
				cell = new Listcell(controller.sdf.format(nota.getDWhnCreate()));
				cell.setParent(item);
				
				cell = new Listcell(MedisafeUtil.convert2PaymentStatus(nota.getNPaymentStatus().shortValue()));
				cell.setParent(item);
				
				db.setValue(new BigDecimal(nota.getTotal()));
				cell = new Listcell(db.getText());
				cell.setParent(item);
			}
	
		}else{
			for(TbReturPharmacyTrx retur : returs){
				item = new Listitem();
				item.setValue(retur);
				item.setParent(controller.transactionNumberList);
				
				cell = new Listcell(retur.getVReturCode());
				cell.setParent(item);
				
				cell = new Listcell(retur.getTbExamination().getMsPatient().getVPatientName());
				cell.setParent(item);
				
				cell = new Listcell(controller.sdf.format(retur.getDWhnCreate()));
				cell.setParent(item);
				
				cell = new Listcell(MedisafeUtil.convert2PaymentStatus(retur.getNPaymentStatus().shortValue()));
				cell.setParent(item);
				
				db.setValue(new BigDecimal(retur.getNTrxValue()));
				cell = new Listcell(db.getText());
				cell.setParent(item);
			}
		}
				
		MiscTrxController.setFont(controller.transactionNumberList);
		
	}

	public void getNoteDetil(CashierTransactionController controller) 
		throws VONEAppException, InterruptedException {
		
		Listitem item;
		Listcell cell;
		
		//note : operationTrx, misctrx belum dimasukin
		
		controller.cashierList.getItems().clear();
		
		List<TbExamination> notas = new ArrayList<TbExamination>();
		List<TbReturPharmacyTrx> returItems = new ArrayList<TbReturPharmacyTrx>();
		String notaNumbers = "";
		
//		double totalTrx = 0;
//		double totalDisc = 0;
		
		
		
		Iterator it = controller.transactionNumberList.getSelectedItems().iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			if(item.getValue() instanceof TbExamination){
				TbExamination note = (TbExamination)item.getValue();
				notaNumbers = notaNumbers+note.getVNoteNo()+";" ;
				notas.add(note);
			}else{
				TbReturPharmacyTrx retur = (TbReturPharmacyTrx)item.getValue();
				notaNumbers = notaNumbers+retur.getVReturCode()+";" ;
				returItems.add(retur);
			}
			
		}
		
		if(notas.size() > 0){
			controller.isRetur = false;
			
			TbExamination notaPasien = notas.get(0);
			
			notaPasien = noteDao.getNote(notaPasien.getNExamId());
			
			controller.address.setValue(notaPasien.getMsPatient().getVPatientMainAddr());
			controller.patientName.setValue(notaPasien.getMsPatient().getVPatientName());
			if(notaPasien.getMsPatient().getMsPatientType() != null)
				controller.patientType.setValue(notaPasien.getMsPatient().getMsPatientType().getVTpatientDesc());
			
			if(notaPasien.getTbRegistration() != null)
				controller.registrationNumber.setValue(notaPasien.getTbRegistration().getVRegSecondaryId());
			
			getListDetil(notas, controller);
			
			controller.noteCollection = notas;
			controller.btnCetak.setDisabled(true);
						
			
			if(notaNumbers.length() > 0){
				notaNumbers = notaNumbers.substring(0, (notaNumbers.length()-1));
				controller.transactionNumber.setValue(notaNumbers);
			}
			
		
			controller.discounttmp = controller.discountTotal.getValue().doubleValue();
			
			
			if(controller.reg != null){
				
				//cek registrasi
				if(controller.reg.getVRegSecondaryId().charAt(0) == 'I'){
					
					//cek sudah melakukan retur belum
					List<TbReturPharmacyTrx> returs = dao.getReturNotes(controller.reg);
					double totalRetur = 0;
					String noRetur ="";

					if(controller.transactionNumberList.getItems().size() == 
						controller.transactionNumberList.getSelectedItems().size())
					{
						
						//sudah melakukan retur
						if(returs.size() > 0)
						{
							for(TbReturPharmacyTrx retur : returs){
								totalRetur = totalRetur + retur.getNTrxValue();
								noRetur = noRetur + retur.getVReturCode()+";";
							}
							if(noRetur.length() > 0){
								controller.returnTtl.setValue(new BigDecimal(totalRetur));
							controller.returnNo.setValue(noRetur.substring(0, (noRetur.length() - 1)));
								
							}
						}
						else
						{
							//belum melakukan retur
							List retur = dao.getItemReturnable(controller.patient.getNPatientId(), 
													controller.kelasTarif);
							Iterator itr = retur.iterator();
							while(itr.hasNext()){
								
								Object[] obj = (Object[])itr.next();
								//obj[0] --> item id
								//obj[1] --> selling price
								//obj[2] --> masuk
								//obj[3] --> keluar
								Integer masuk = (Integer)obj[2];
								Integer keluar = (Integer)obj[3];
								Double hargaJual = (Double)obj[1];
								totalRetur = totalRetur + ((masuk.intValue() - keluar.intValue()) * hargaJual); 
							}
							controller.returnTtl.setValue(new BigDecimal(totalRetur));
						}
					}else controller.returnTtl.setValue(new BigDecimal(0));
					
				
				}else{
					//untuk pasien rawat jalan
					controller.returnTtl.setValue(new BigDecimal(0));
				}
				
			}else{
				//untuk rujukan
				controller.returnTtl.setValue(new BigDecimal(0));
			}
			
			//total setelah dikurangi retur
			double totaltmp = controller.total.getValue().doubleValue() - controller.returnTtl.
																			getValue().doubleValue();
			
			double nilaiPajak = 0;
			
			if(controller.ppn.getValue() != null)
				nilaiPajak = (controller.ppn.getValue().doubleValue()/100) * totaltmp;
			
			//total setelah ditambah pajak
			totaltmp = totaltmp + nilaiPajak;
			controller.totalAmount.setValue(new BigDecimal(totaltmp));
			
		}
		
		else
		{
			controller.isRetur = true;
//			List<TbReturPharmacyDetailTrx> detils = this.getRetursDetail(returItems);
			
			int jumlah = 0;
			Decimalbox db = new Decimalbox();
			db.setFormat("#,###.##");
			
			double returValue = 0;
			controller.total.setValue(new BigDecimal(0));
			
			TbReturPharmacyTrx returLagi = null;
			
			for(TbReturPharmacyTrx retur : returItems){
				
				retur = this.dao.getReturById(retur.getNReturId());
				returLagi = retur;
				
				returValue = returValue + retur.getNTrxValue();
				if(retur.getTbExamination() != null)
					controller.reg = retur.getTbExamination().getTbRegistration();
				
				Set<TbReturPharmacyDetailTrx> detils = retur.getTbReturPharmacyDetailTrxes();
//				List<TbReturPharmacyDetailTrx> detils = dao.getReturDetail(retur);
				
				for(TbReturPharmacyDetailTrx returDetil : detils){
					item = new Listitem();
					item.setValue(returDetil);
					item.setParent(controller.cashierList);
					
					cell = new Listcell(returDetil.getTbReturPharmacyTrx().getVReturCode());
					cell.setParent(item);
					
					cell = new Listcell(returDetil.getMsItem().getVItemCode());
					cell.setParent(item);
					
					cell = new Listcell(returDetil.getMsItem().getVItemName());
					cell.setParent(item);
					
					jumlah = (int)returDetil.getNQty();
					cell = new Listcell(""+jumlah);
					cell.setParent(item);
					
					if(returDetil.getNValue() != null){
						db.setValue(new BigDecimal(returDetil.getNValue()));
					}
					else db.setValue(new BigDecimal(0));
					cell = new Listcell(db.getText());
					cell.setParent(item);
					
					cell = new Listcell("0");
					cell.setParent(item);
					
					cell = new Listcell(db.getText());
					cell.setParent(item);
				}
				
			}
			
			if(returLagi != null){
				controller.patientName.setValue(returLagi.getMsPatient().getVPatientName());
				controller.address.setValue(returLagi.getMsPatient().getVPatientMainAddr());
				if(returLagi.getMsPatient().getMsPatientType() != null)
					controller.patientType.setValue(returLagi.getMsPatient().getMsPatientType().getVTpatientDesc());
			}
			
//			this.total
//			this.returnTtl
//			this.discount
//			discountTotal
//			totalAmount
			
//			double returValue = 0;
//			controller.total.setValue(new BigDecimal(0));
//			for(TbReturPharmacyTrx returItem : returItems){
////				returValue = returValue + returItem.getNTrxValue();
////				if(returItem.getTbExamination() != null)
////					controller.reg = returItem.getTbExamination().getTbRegistration();
//			}
			
			controller.returnTtl.setValue(new BigDecimal(returValue));
			controller.discount.setValue(new BigDecimal(0));
			controller.discountTotal.setValue(new BigDecimal(0));
			returValue = controller.total.getValue().doubleValue() - returValue;
			controller.totalAmount.setValue(new BigDecimal(returValue));
			
			if(notaNumbers.length() > 0){
				notaNumbers = notaNumbers.substring(0, (notaNumbers.length()-1));
				controller.transactionNumber.setValue(notaNumbers);
			}
			
		}
		
		controller.noteCollection = notas;
		MiscTrxController.setFont(controller.cashierList);
		
	}
	
	
	public void getListDetil(Collection notas, CashierTransactionController controller) 
		throws VONEAppException{

			Listitem item;
			Listcell cell;
			
			TbExamination nota = null;
			double totalDisc = 0;
			double totalTrx = 0;
			
			Iterator it = notas.iterator();
			while(it.hasNext()){
				nota = (TbExamination)it.next();
								
				List itemTrxs = noteDao.getItemTrxBaseOnNote(nota);
				double harga;
				double hargaAkhir;
				double jasaR;
				int r;
				Iterator itr = itemTrxs.iterator();
				while(itr.hasNext()){
					harga = 0;
					jasaR = 0;
					hargaAkhir = 0;
					Object[] obj = (Object[])itr.next();
					item = new Listitem();
					item.setValue(obj);
					item.setParent(controller.cashierList);
					
					cell = new Listcell(nota.getVNoteNo());
					cell.setParent(item);
					
					cell = new Listcell(obj[1].toString());
					cell.setParent(item);
					
					cell = new Listcell(obj[2].toString());
					cell.setParent(item);
					
					cell = new Listcell(obj[3].toString());
					cell.setParent(item);
					jasaR = (((Short)obj[8]).doubleValue() * new Integer(MessagesService.getKey("jasa.apotik")).doubleValue());
					harga = ((Double)obj[4]).doubleValue()+ jasaR;
					controller.db.setValue(new BigDecimal(harga));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					hargaAkhir = (Double)obj[6] + jasaR;
					
					controller.db.setValue(new BigDecimal(harga-hargaAkhir));
					totalDisc = totalDisc + controller.db.getValue().doubleValue();
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					totalTrx = totalTrx + hargaAkhir;
					controller.db.setValue(new BigDecimal(hargaAkhir));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
				}
				
				
				List racikans = noteDao.getRacikan(nota);
					
				itr = racikans.iterator();
				while(itr.hasNext()){
					TbDrugIngredients drug = (TbDrugIngredients)itr.next();
					r = drug.getNEr();
					jasaR = 0;
					harga = 0;
					hargaAkhir = 0;
					
					item = new Listitem();
					item.setValue(drug);
					item.setParent(controller.cashierList);
					
					cell = new Listcell(drug.getTbExamination().getVNoteNo());
					cell.setParent(item);
					
					cell = new Listcell(drug.getVDingrId());
					cell.setParent(item);
					
					cell = new Listcell(drug.getVDingrId());
					cell.setParent(item);
					
					cell = new Listcell(""+drug.getNDingrQty());
					cell.setParent(item);
					
					Set<TbDrugIngredientsDetail> detil = drug.getTbDrugIngredientsDetails();
					for(TbDrugIngredientsDetail det : detil){
//						r = r + det.getMsItem().getNR();
					}
					
					jasaR = r * new Integer(MessagesService.getKey("jasa.apotik")).doubleValue();
					harga = drug.getNAmountTrx() + jasaR;
					hargaAkhir = drug.getNAmountAfterDisc() + jasaR;
					
					controller.db.setValue(new BigDecimal(harga));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					
					
					controller.db.setValue(new BigDecimal(harga-hargaAkhir));
					totalDisc = totalDisc + controller.db.getValue().doubleValue();
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					totalTrx = totalTrx + hargaAkhir;
					controller.db.setValue(new BigDecimal(hargaAkhir));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
				}
				
				
				List treatmentTrxs = noteDao.getTreatmentTrx(nota); 

				itr = treatmentTrxs.iterator();
				while(itr.hasNext()){
					TbTreatmentTrx trx = (TbTreatmentTrx)itr.next();
					
					item = new Listitem();
					item.setValue(trx);
					item.setParent(controller.cashierList);
					
					cell = new Listcell(nota.getVNoteNo());
					cell.setParent(item);
					
					cell = new Listcell(trx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode());
					cell.setParent(item);
					
					cell = new Listcell(trx.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
					cell.setParent(item);
					
					cell = new Listcell("1");
					cell.setParent(item);
					
					controller.db.setValue(new BigDecimal(trx.getNAmountTrx()));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					controller.db.setValue(new BigDecimal((trx.getNAmountTrx()-trx.getNAmountAfterDisc())));
					totalDisc = totalDisc + controller.db.getValue().doubleValue();
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					totalTrx = totalTrx + trx.getNAmountAfterDisc();
					controller.db.setValue(new BigDecimal(trx.getNAmountAfterDisc()));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
				}
				
				
				List bundles = noteDao.getBundleTrx(nota); 
					
				itr = bundles.iterator();
				while(itr.hasNext()){
					TbBundledTrx trx = (TbBundledTrx)itr.next();
					
					item = new Listitem();
					item.setValue(trx);
					item.setParent(controller.cashierList);
					
					cell = new Listcell(nota.getVNoteNo());
					cell.setParent(item);
					
					cell = new Listcell(trx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode());
					cell.setParent(item);
					
					cell = new Listcell(trx.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
					cell.setParent(item);
					
					cell = new Listcell(trx.getNQty()+"");
					cell.setParent(item);
					
					controller.db.setValue(new BigDecimal(trx.getNAmountTrx()));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					controller.db.setValue(new BigDecimal((trx.getNAmountTrx()-trx.getNAmountAfterDisc())));
					totalDisc = totalDisc + controller.db.getValue().doubleValue();
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					totalTrx = totalTrx + trx.getNAmountAfterDisc();
					controller.db.setValue(new BigDecimal(trx.getNAmountAfterDisc()));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
				}
				
				
				
				List bedTrxs = noteDao.getBedTrx(nota); 
					nota.getTbBedTrx();
				itr = bedTrxs.iterator();
				while(itr.hasNext()){
					TbBedTrx trx = (TbBedTrx)itr.next();
					
					item = new Listitem();
					item.setValue(trx);
					item.setParent(controller.cashierList);
					
					cell = new Listcell(nota.getVNoteNo());
					cell.setParent(item);
					
					cell = new Listcell(trx.getMsBed().getVBedDesc());
					cell.setParent(item);
					
					cell = new Listcell(trx.getMsBed().getVBedDesc());
					cell.setParent(item);
					
					cell = new Listcell(trx.getNTotalHour() +" HARI");
					cell.setParent(item);
					
					controller.db.setValue(new BigDecimal(trx.getNFee()));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					cell = new Listcell("0");
					cell.setParent(item);
					
					totalTrx = totalTrx + trx.getNFee();
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
				}
				
				
				List miscs = noteDao.getMiscTrx(nota); 
					
				itr = miscs.iterator();
				while(itr.hasNext()){
					TbMiscTrx trx = (TbMiscTrx)itr.next();
					
					item = new Listitem();
					item.setValue(trx);
					item.setParent(controller.cashierList);
					
					cell = new Listcell(nota.getVNoteNo());
					cell.setParent(item);
					
					cell = new Listcell(MedisafeConstants.MISC_CODE);
					cell.setParent(item);
					
					cell = new Listcell(trx.getVMiscName());
					cell.setParent(item);
					
					cell = new Listcell(trx.getNQty()+"");
					cell.setParent(item);
					
					controller.db.setValue(new BigDecimal(trx.getNAmountTrx()));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					controller.db.setValue(new BigDecimal(trx.getNDiscAmount()));
					totalDisc = totalDisc + controller.db.getValue().doubleValue();
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
					
					totalTrx = totalTrx + trx.getNAmountAfterDisc();
					controller.db.setValue(new BigDecimal(trx.getNAmountAfterDisc()));
					cell = new Listcell(controller.db.getText());
					cell.setParent(item);
				}
				
				MiscTrxController.setFont(controller.cashierList);
				
			}
			
			controller.total.setValue(new BigDecimal(totalTrx));
			controller.discountTotal.setValue(new BigDecimal(totalDisc));
			
	}
	
	

	public void getRegistration(CashierTransactionController controller, int type) 
		throws VONEAppException, InterruptedException {
		controller.boc = null;
		Listitem item;
		Listcell cell;

		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			controller.mr = mrDao.getMrRegistered(code);
			if(controller.mr == null){
				
				Messagebox.show(MessagesService.getKey("mr.tidak.terdaftar"));
				controller.MRNumber.focus();
				return;
			}
			controller.patient = controller.mr.getMsPatient();
		}
		else{
			item = controller.MRList.getSelectedItem();
			controller.mr = (TbMedicalRecord)item.getValue();
			controller.mr = mrDao.getMrRegistered(controller.mr.getVMrCode());
			controller.patient = controller.mr.getMsPatient();
		}
		
		controller.reg = regDao.getLastRegistrationByMrId(controller.mr.getNMrId());
		
		controller.MRNumber.setValue(controller.mr.getVMrCode());
		controller.address.setValue(controller.mr.getMsPatient().getVPatientMainAddr());
		controller.patientName.setValue(controller.mr.getMsPatient().getVPatientName());
		
		if(controller.mr.getMsPatient().getMsPatientType() != null)
			controller.patientType.setValue(controller.mr.getMsPatient().getMsPatientType().getVTpatientDesc());
		
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
		if(controller.reg.getVRegSecondaryId().charAt(0) == 'I')
		{
			TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
			controller.boc = boc;
			controller.msBed = boc.getId().getMsBed();
			controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
			controller.kelasTarif = boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc();
			
			TbPatientDeposit balance = this.getPatientBalance(controller.reg);
			if(balance != null){
				controller.amountOfDeposit.setValue(new BigDecimal(balance.getNBalance()));
			}
		}
		else controller.kelasTarif=MedisafeConstants.DEFAULT_TCLASS;
		
		controller.addrOnBill.setValue(controller.mr.getMsPatient().getVPatientMainAddr());
		controller.nameOnBill.setValue(controller.mr.getMsPatient().getVPatientName());
				
		controller.statusNota.setVisible(false);
		controller.labelStatus.setVisible(false);
		
		controller.transactionNumberList.getItems().clear();
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		if(controller.reg.getTbRegistration() != null){
			List<TbExamination> hasils = dao.getNotes(controller.reg.getTbRegistration());
			
			for(TbExamination nota : hasils){
				item = new Listitem();
				item.setValue(nota);
				item.setParent(controller.transactionNumberList);
				
				cell = new Listcell(nota.getVNoteNo());
				cell.setParent(item);
				
				cell = new Listcell(nota.getMsPatient().getVPatientName());
				cell.setParent(item);
				
				cell = new Listcell(controller.sdf.format(nota.getDWhnCreate()));
				cell.setParent(item);
				
				cell = new Listcell(MedisafeUtil.convert2PaymentStatus(nota.getNPaymentStatus().shortValue()));
				cell.setParent(item);
				
				db.setValue(new BigDecimal(nota.getTotal()));
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
				controller.transactionNumberList.addItemToSelection(item);
			}
			
			
		}
		
		List<TbExamination> results = dao.getNotes(controller.reg);
		
		for(TbExamination nota : results){
			
			item = new Listitem();
			item.setValue(nota);
			item.setParent(controller.transactionNumberList);
			
			cell = new Listcell(nota.getVNoteNo());
			cell.setParent(item);
			
			cell = new Listcell(nota.getMsPatient().getVPatientName());
			cell.setParent(item);
			
			cell = new Listcell(controller.sdf.format(nota.getDWhnCreate()));
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.convert2PaymentStatus(nota.getNPaymentStatus().shortValue()));
			cell.setParent(item);
			
			db.setValue(new BigDecimal(nota.getTotal()));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
			controller.transactionNumberList.addItemToSelection(item);
		}
		
		if(controller.transactionType.getSelectedItem().getLabel().equalsIgnoreCase("PELUNASAN")){
			controller.isPembayaran=true;
			
			getNoteDetil(controller);
			
			controller.depositPaid.setValue(controller.amountOfDeposit.getValue());
			
			double selisih = 
				controller.totalAmount.getValue().doubleValue() - controller.depositPaid.getValue().doubleValue();
			
			controller.amountOfReturn.setValue(new BigDecimal(selisih));
			
		}
			
		MiscTrxController.setFont(controller.transactionNumberList);

		
	}

	
	
	public void getBillDetil(CashierTransactionController controller) throws VONEAppException {
		
		if(controller.billOptionList.getSelectedIndex() == 0){
			//nota
			TbPatientBill bill = (TbPatientBill) controller.noteList.getSelectedItem().getValue();
			
			
			controller.pbill = this.dao.getBillById(bill.getNPbillId());
			
			Set notas = controller.pbill.getTbExamination();
			getListDetil(notas, controller);
			controller.noteNumber.setValue(controller.pbill.getVPbillCode());
			controller.nameOnBill.setValue(controller.pbill.getVNameOnBill());
			controller.addrOnBill.setValue(controller.pbill.getVAddrOnBill());
			
			controller.noteCollection = notas;
			controller.nomorKwitansi = controller.pbill.getVPbillCode();
			controller.tanggalKwitansi = controller.pbill.getDWhnCreate();
			//added by arif 28-06-2010
			
			controller.discountTotal.setValue(new BigDecimal(controller.pbill.getNPbillDisc()));
			controller.discount.setValue(new BigDecimal(controller.pbill.getNPbillDisc()));
			
			Iterator itr = notas.iterator();
			String nomorNota="";
			
			while(itr.hasNext()){
				TbExamination nota = (TbExamination)itr.next();
				nomorNota = nomorNota + nota.getVNoteNo()+";";
			}
			if(nomorNota.length() > 0){
				nomorNota = nomorNota.substring(0, (nomorNota.length()-1));
				controller.transactionNumber.setValue(nomorNota);
			}
			
			controller.reg = controller.pbill.getTbRegistration();
			if(controller.reg != null){
				controller.MRNumber.setValue(controller.reg.getTbMedicalRecord().getVMrCode());
				controller.patientName.setValue(controller.reg.getTbMedicalRecord().
						getMsPatient().getVPatientName());
				
				if(controller.reg.getTbMedicalRecord().getMsPatient().getMsPatientType() != null)
					controller.patientType.setValue(controller.reg.getTbMedicalRecord().getMsPatient().
							getMsPatientType().	getVTpatientDesc());
				controller.address.setValue(controller.reg.getTbMedicalRecord().getMsPatient().
						getVPatientMainAddr());
				
				if(controller.reg.getVRegSecondaryId().charAt(0) == 'I')
				{
					TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.reg.getNRegId());
					controller.bed.setValue(boc.getId().getMsBed().getVBedDesc());
					
					
					TbPatientDeposit balance = this.getPatientBalance(controller.reg);
					if(balance != null){
						controller.amountOfDeposit.setValue(new BigDecimal(balance.getNBalance()));
					}
				}
			}
			
			Set<TbPatientSettlement> settles = controller.pbill.getTbPatientSettlements();
			double kas = 0;
			double noncash = 0;
			
			for(TbPatientSettlement set : settles){
				if(set.getNPsettlementType().shortValue() == MedisafeConstants.CASH_SETTLEMENT)
					kas = kas + set.getNAmountSettled();
				else
					noncash = noncash + set.getNAmountSettled();
			}
			controller.cashPay.setValue(new BigDecimal(kas));
			controller.returnTtl.setValue(new BigDecimal(0));
			controller.totalAmount.setValue(new BigDecimal(controller.pbill.getNPbillTtlPaid()));
			controller.creditPay.setValue(new BigDecimal(noncash));
			controller.btnCetak.setDisabled(false);
			controller.btnPay.setDisabled(true);
		}
		else{
			//retur;
		}
		MiscTrxController.setFont(controller.noteList);
	}
	
	

	public void getReturRegistration(ReturKwitansiController controller, int type) throws VONEAppException, InterruptedException {
		
		Listitem item;
		Listcell cell;
		
		TbMedicalRecord mr = null;
		MsPatient patient = null;
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			mr = mrDao.getMrRegistered(code);
			if(mr == null){
				Messagebox.show(MessagesService.getKey("mr.tidak.terdaftar"));
				controller.MRNumber.focus();
				return;
			}
			patient = mr.getMsPatient();
		}
		else{
			item = controller.patientList.getSelectedItem();
			mr = (TbMedicalRecord)item.getValue();
			patient = mr.getMsPatient();
		}
		
		TbRegistration reg = regDao.getLastRegistrationByMrId(mr.getNMrId());
		
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.addr.setValue(patient.getVPatientMainAddr());
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		
		controller.billList.getItems().clear();
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		Set<TbPatientBill> pbills = reg.getTbPatientBills();
		
		controller.kwitansiList.getItems().clear();
		
		for(TbPatientBill bill : pbills){
			
			item = new Listitem();
			item.setParent(controller.kwitansiList);
			item.setValue(bill);
			
			cell = new Listcell(bill.getVPbillCode());
			cell.setParent(item);
			
			cell = new Listcell(bill.getVNameOnBill());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.getNoteStatus(bill.getNPbillStatus()));
			cell.setParent(item);
		}

		
	}

	public void testPersen() throws VONEAppException {
		dao.testPersen();
		
	}

	
	
	public void getBillNote(ReturKwitansiController controller) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		controller.bill = (TbPatientBill)controller.kwitansiList.getSelectedItem().getValue();
		controller.billList.getItems().clear();
		
		controller.bill = this.dao.getBillById(controller.bill.getNPbillId());
		
		controller.noteNumber.setValue(controller.bill.getVPbillCode());
		controller.addr.setValue(controller.bill.getVAddrOnBill());
		controller.patientName.setValue(controller.bill.getVNameOnBill());
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		controller.labelStatus.setVisible(true);
		controller.statusNota.setVisible(true);
		
		controller.statusNota.setValue(MedisafeUtil.getNoteStatus(controller.bill.getNPbillStatus()));
		
		Set<TbExamination> notas = controller.bill.getTbExamination();
		for(TbExamination nota : notas){
			
			item = new Listitem();
			item.setParent(controller.billList);
			item.setValue(nota);
			
			cell = new Listcell(nota.getVNoteNo());
			cell.setParent(item);
			
			db.setValue(new BigDecimal(nota.getTotal()));
			cell = new Listcell(db.getText());
			cell.setParent(item);
				
		}
		
		
		controller.ppn.setValue(new BigDecimal(controller.bill.getNPbillTax()));
		controller.total.setValue(new BigDecimal(controller.bill.getNPbillSubTtl()+controller.bill.getNPbillTax()));
		controller.btnSave.setDisabled(false);
		controller.btnCetak.setDisabled(false);
		
	}

	/****************************************************************************
	 * 
	 * implementasi PrintController
	 * 
	 ****************************************************************************/
	public void btnAllClick(PrintController controller) throws VONEAppException {
		// todo Auto-generated method stub
		
	}

	public void btnItemsClick(PrintController controller) throws VONEAppException {
		// todo Auto-generated method stub
		
	}

	public void btnUnitClick(PrintController controller) throws VONEAppException {
		// todo Auto-generated method stub
		
	}

	public void getRegistration(PrintController controller, int input_type) throws VONEAppException {
		TbMedicalRecord mr = null;
		if(input_type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			mr = mrDao.getMrRegistered(code);
			if(mr == null){
				
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
				} catch (InterruptedException e) {
					// todo Auto-generated catch block
					e.printStackTrace();
				}
				controller.MRNumber.focus();
				
				return;
			}
			
			controller.setTbRegistration(regDao.getLastRegistrationByMrId(mr.getNMrId()));
		}
		else{
			Listitem item = controller.getMRNumberList().getSelectedItem();
			mr = (TbMedicalRecord)item.getValue();
			controller.setTbRegistration(regDao.getLastRegistrationByMrId(mr.getNMrId()));
			mr = controller.getTbRegistration().getTbMedicalRecord();
		}
		
		 
		
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.address.setValue(mr.getMsPatient().getVPatientMainAddr());
		

		
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.regNo.setValue(controller.getTbRegistration().getVRegSecondaryId());
		if(mr.getMsPatient().getMsPatientType() != null)
			controller.tipePasien.setValue(mr.getMsPatient().getMsPatientType().getVTpatientDesc());
	
		controller.setMsPatient(mr.getMsPatient());
		if(controller.getTbRegistration().getVRegSecondaryId().substring(0, 1).equals("I")){
			
			controller.isRanap = true;
			TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.getTbRegistration().getNRegId());
				
			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc();
			
			controller.getBed().setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());
//					+ " / " +
//					boc.getId().getMsBed().getVBedDesc());
			controller.getTClass().setValue(boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc());
			controller.getStartDate().setValue(mr.getDWhnCreate());
			controller.getEndDate().setValue(new Date());
		}
		else{
			controller.isRanap = false;
			controller.getBed().setValue(null);
			controller.getTClass().setValue(null);
		}
		
		
		
	}

	public void cariNotaClick(PrintController controller) throws VONEAppException {
		
		List list = dao.getTbxamination(controller.getMsPatient(),
				controller.getStartDate().getValue(),
				controller.getEndDate().getValue());
		
		Iterator iter = list.iterator();
		Listitem listitem;
		Listcell listcell;
		TbExamination tbExamination;
		controller.getPrintList().getItems().clear();
		double totNota = 0;
		double lunas = 0;
		String stat="";
		String kwitansi = "";
		while (iter.hasNext()) {
			tbExamination = (TbExamination) iter.next();
			
			
			//list all treatment in this note..
			Set<TbTreatmentTrx> treatments = tbExamination.getTbTreatmentTrx();
			for(TbTreatmentTrx treat:treatments){
				listitem = new Listitem();
				listitem.setParent(controller.getPrintList());
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.getDate(tbExamination.getDWhnCreate(), MedisafeConstants.DATE_FORMAT));
				
				listcell = new Listcell(treat.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(tbExamination.getVNoteNo());
				
				listcell = new Listcell(treat.getVWhoCreate());
				listcell.setParent(listitem);
				
				if(tbExamination.getTbPatientBill() != null){
					stat="LUNAS";
					lunas = lunas + treat.getNAmountAfterDisc();
					kwitansi = tbExamination.getTbPatientBill().getVPbillCode();
				}
				else if(tbExamination.getNExamStatus().intValue()==1){
					stat = "BARU";
					kwitansi = "-";
				}
				else if(tbExamination.getNExamStatus().intValue()==2){
					stat = "VALIDASI";
					kwitansi = "-";
				}
				
				listcell = new Listcell(stat);
				listcell.setParent(listitem);
				
				listcell = new Listcell(kwitansi);
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.formatDouble(treat.getNAmountAfterDisc()));

				totNota += treat.getNAmountAfterDisc();	
				
			}
			
			Set<TbItemTrx> items = tbExamination.getTbItemTrx();
			for(TbItemTrx item : items){
				if(item.getNQty() > 0){
					
				
				listitem = new Listitem();
				listitem.setParent(controller.getPrintList());
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.getDate(tbExamination.getDWhnCreate(), MedisafeConstants.DATE_FORMAT));
				
				
					listcell = new Listcell(item.getMsItem().getVItemName()+ " QTY "+item.getNQty());
					listcell.setParent(listitem);
				
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(tbExamination.getVNoteNo());
				
				listcell = new Listcell(item.getVWhoCreate());
				listcell.setParent(listitem);
				
				if(tbExamination.getTbPatientBill() != null){
					stat="LUNAS";
					lunas = lunas + item.getNAmountAfterDisc();
					kwitansi = tbExamination.getTbPatientBill().getVPbillCode();
				}
				else if(tbExamination.getNExamStatus().intValue()==1){
					stat = "BARU";
					kwitansi="-";
				}
				else if(tbExamination.getNExamStatus().intValue()==2){
					stat = "VALIDASI";
					kwitansi="-";
				}
				
				listcell = new Listcell(stat);
				listcell.setParent(listitem);
				
				listcell = new Listcell(kwitansi);
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.formatDouble(item.getNAmountAfterDisc()));

				totNota += item.getNAmountAfterDisc();	
				}
			}
			
			Set<TbDrugIngredients> drugs = tbExamination.getTbDrugIngredients();
			for(TbDrugIngredients drug : drugs){
				listitem = new Listitem();
				listitem.setParent(controller.getPrintList());
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.getDate(tbExamination.getDWhnCreate(), MedisafeConstants.DATE_FORMAT));
				
				listcell = new Listcell(drug.getVDingrId());
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(tbExamination.getVNoteNo());
				
				listcell = new Listcell(drug.getVWhoCreate());
				listcell.setParent(listitem);
				
				if(tbExamination.getTbPatientBill() != null){
					stat="LUNAS";
					lunas = lunas + drug.getNAmountAfterDisc();
					kwitansi = tbExamination.getTbPatientBill().getVPbillCode();
				}
				else if(tbExamination.getNExamStatus().intValue()==1){
					stat = "BARU";
					kwitansi="-";
				}
				else if(tbExamination.getNExamStatus().intValue()==2){
					stat = "VALIDASI";
					kwitansi="-";
				}
				
				listcell = new Listcell(stat);
				listcell.setParent(listitem);
				
				listcell = new Listcell(kwitansi);
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.formatDouble(drug.getNAmountAfterDisc()));

				totNota += drug.getNAmountAfterDisc();	
			}
			
			Set<TbBedTrx> beds = tbExamination.getTbBedTrx();
			for(TbBedTrx bed : beds){
				
				listitem = new Listitem();
				listitem.setParent(controller.getPrintList());
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.getDate(tbExamination.getDWhnCreate(), MedisafeConstants.DATE_FORMAT));
				
				listcell = new Listcell(bed.getMsBed().getVBedDesc() +" - " + bed.getNTotalHour() + " HARI");
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(tbExamination.getVNoteNo());
				
				listcell = new Listcell(bed.getVWhoCreate());
				listcell.setParent(listitem);
				
				if(tbExamination.getTbPatientBill() != null){
					stat="LUNAS";
					lunas = lunas + bed.getNAmountAfterDisc();
					kwitansi = tbExamination.getTbPatientBill().getVPbillCode();
				}
				else if(tbExamination.getNExamStatus().intValue()==1){
					stat = "BARU";
					kwitansi="-";
				}
				else if(tbExamination.getNExamStatus().intValue()==2){
					stat = "VALIDASI";
					kwitansi="-";
				}
				
				listcell = new Listcell(stat);
				listcell.setParent(listitem);
				
				listcell = new Listcell(kwitansi);
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.formatDouble(bed.getNAmountAfterDisc()));

				totNota += bed.getNAmountAfterDisc();	
			}
			
			Set<TbMiscTrx> miscs = tbExamination.getTbMiscTrx();
			for(TbMiscTrx bed : miscs){
				
				listitem = new Listitem();
				listitem.setParent(controller.getPrintList());
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.getDate(tbExamination.getDWhnCreate(), MedisafeConstants.DATE_FORMAT));
				
				listcell = new Listcell(bed.getVMiscName());
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(tbExamination.getVNoteNo());
				
				listcell = new Listcell(bed.getVWhoCreate());
				listcell.setParent(listitem);
				
				if(tbExamination.getTbPatientBill() != null){
					stat="LUNAS";
					lunas = lunas + bed.getNAmountAfterDisc();
					kwitansi = tbExamination.getTbPatientBill().getVPbillCode();
				}
				else if(tbExamination.getNExamStatus().intValue()==1){
					stat = "BARU";
					kwitansi="-";
				}
				else if(tbExamination.getNExamStatus().intValue()==2){
					stat = "VALIDASI";
					kwitansi="-";
				}
				
				listcell = new Listcell(stat);
				listcell.setParent(listitem);
				
				listcell = new Listcell(kwitansi);
				listcell.setParent(listitem);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(PrintClient.formatDouble(bed.getNAmountAfterDisc()));

				totNota += bed.getNAmountAfterDisc();	
			}
		}
		//cek retur
		double totalRetur = 0;
		if(controller.getTbRegistration() != null){
			
			//cek registrasi
			if(controller.getTbRegistration().getVRegSecondaryId().charAt(0) == 'I'){
				
				//cek sudah melakukan retur belum
				List<TbReturPharmacyTrx> returs = dao.getReturNotes(controller.getTbRegistration());
//				TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.getTbRegistration().getNRegId());
//				
//				controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc();

				String noRetur ="";

					//sudah melakukan retur
					if(returs.size() > 0)
					{
						for(TbReturPharmacyTrx retur : returs){
							totalRetur = totalRetur + retur.getNTrxValue();
							noRetur = noRetur + retur.getVReturCode()+";";
						}
						if(noRetur.length() > 0){
							controller.getReturDecimalbox().setValue(new BigDecimal(totalRetur));
						}
					}
					else
					{
						//belum melakukan retur
						List retur = dao.getItemReturnable(controller.getMsPatient().getNPatientId(), 
												controller.ranapKelas);
						Iterator itr = retur.iterator();
						while(itr.hasNext()){
							
							Object[] obj = (Object[])itr.next();
							//obj[0] --> item id
							//obj[1] --> selling price
							//obj[2] --> masuk
							//obj[3] --> keluar
							Integer masuk = (Integer)obj[2];
							Integer keluar = (Integer)obj[3];
							Double hargaJual = (Double)obj[1];
							totalRetur = totalRetur + ((masuk.intValue() - keluar.intValue()) * hargaJual); 
						}
						controller.getReturDecimalbox().setValue(new BigDecimal(totalRetur));
					}
			
			}else{
				//untuk pasien rawat jalan
				controller.getReturDecimalbox().setValue(new BigDecimal(0));
			}
			
		}else{
			//untuk rujukan
			controller.getReturDecimalbox().setValue(new BigDecimal(0));
		}
		controller.getTotal().setValue(new BigDecimal(totNota));
		
		//arif modify calculation of sisa
//		double sisa = totNota - totalRetur;
		double sisa = totNota - (totalRetur + lunas);
		controller.getDeposit().setValue(null);
		//deposit
		if(controller.getTbRegistration().getVRegSecondaryId().charAt(0) == 'I')
		{
			TbPatientDeposit balance = getPatientBalance(controller.getTbRegistration());
			if(balance != null){
				controller.getDeposit().setValue(new BigDecimal(balance.getNBalance()));
				
				//arif comment here...
//				sisa -= balance.getNBalance();
			}
		}
		controller.getSisa().setValue(new BigDecimal(sisa));
	}

	
	public List<TbExamination> getNota(TbRegistration reg, int active_note) throws VONEAppException {
		
		return this.dao.getNotes(reg,active_note);
	}
	
	public List<TbExamination> getUnbillNote(TbRegistration reg) throws VONEAppException{
		return this.dao.getUnbillNotes(reg);
	}

	public void getPatientSettlement(Datebox transDate, Listbox shiftList, Listbox kwitansiList, Decimalbox tunai, Decimalbox nonTunai) {
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		//String dateFrom = transDate.getText()+" 00:00";
//		String dateTo = sdf1.format(transDate.getValue())+" 23:59";
		
//		String dateFrom = sdf1.format(transDate.getValue()) +" 00:00"; 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		Date dateFrom = null;
		try {
			dateFrom = sdf.parse(transDate.getText());
		} catch (WrongValueException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Date dateTo = transDate.getValue();
		dateTo.setDate(dateTo.getDate()+1);
		
//		System.out.println("dari : "+sdf.format(dateFrom) + " sampai : "+sdf.format(dateTo));
		
		
		kwitansiList.getItems().clear();
		tunai.setValue(new BigDecimal("0"));
		nonTunai.setValue(new BigDecimal("0"));
		
		String shiftId = shiftList.getSelectedItem().getValue().toString();
		
		List<TbPatientBill> pbills = null;
		
		try {
			pbills = this.dao.getPatientBill(dateFrom,dateTo,shiftId);
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
			
			
		
		Listitem item;
		Listcell cell;
		double vtunai=0;
		double vnontunai=0;
		double vtotaltunai = 0;
		double vtotalNontunai = 0;
		
		String whoCreated = "";
		double tunaiPerCashier = 0;
		double nontTunaiPercashier = 0;
		int index = 0;
		for(TbPatientBill bill : pbills){
			if(index == 0) whoCreated = bill.getVWhoCreate();
						
			if(!bill.getVWhoCreate().equalsIgnoreCase(whoCreated)){
				if(tunaiPerCashier > 0){
					item = new Listitem();
					item.setValue(bill);
					item.setParent(kwitansiList);
					
					cell = new Listcell();
					cell.setParent(item);
					
					cell = new Listcell("TOTAL");
					cell.setParent(item);
					
					db.setValue(new BigDecimal(tunaiPerCashier));
					cell = new Listcell(db.getText());
					cell.setParent(item);
					
					db.setValue(new BigDecimal(nontTunaiPercashier));
					cell = new Listcell(db.getText());
					cell.setParent(item);
					
					cell = new Listcell();
					cell.setParent(item);
					
					tunaiPerCashier = 0;
					nontTunaiPercashier = 0;
				}
				whoCreated = bill.getVWhoCreate();
			}
				item = new Listitem();
				item.setValue(bill);
				item.setParent(kwitansiList);

				vtunai=0;
				vnontunai=0;
				
				cell = new Listcell(sdf.format(bill.getDSettlementDate()));
				cell.setParent(item);
				
				cell = new Listcell(bill.getVPbillCode());
				cell.setParent(item);
				
				Set<TbPatientSettlement> settle = bill.getTbPatientSettlements();
				
				for(TbPatientSettlement stl : settle){
					if(stl.getNPsettlementType().intValue() == 3){
						vtunai = vtunai + stl.getNAmountSettled().doubleValue();
					}else{
						vnontunai = vnontunai + stl.getNAmountSettled().doubleValue();
					}
				}
				
				tunaiPerCashier = tunaiPerCashier + vtunai;
				nontTunaiPercashier = nontTunaiPercashier + vnontunai;
				
				db.setValue(new BigDecimal(vtunai));
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
				db.setValue(new BigDecimal(vnontunai));
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
				
				cell = new Listcell(bill.getVWhoCreate());
				cell.setParent(item);
				vtotaltunai = vtotaltunai + vtunai;
				vtotalNontunai = vtotalNontunai + vnontunai;
				
			
			index++;
		}
		
		item = new Listitem();
		item.setParent(kwitansiList);
		
		cell = new Listcell();
		cell.setParent(item);
		
		cell = new Listcell("TOTAL");
		cell.setParent(item);
		
		db.setValue(new BigDecimal(tunaiPerCashier));
		cell = new Listcell(db.getText());
		cell.setParent(item);
		
		db.setValue(new BigDecimal(nontTunaiPercashier));
		cell = new Listcell(db.getText());
		cell.setParent(item);
		
		cell = new Listcell();
		cell.setParent(item);
		
		tunai.setValue(new BigDecimal(vtotaltunai));
		nonTunai.setValue(new BigDecimal(vtotalNontunai));

	}

	@Override
	public void getRekapKasir(Listbox pendapatanList, Listbox tipeList,
			Datebox dateFrom, Datebox dateTo, Listbox rekapList,
			Decimalbox tunai, Decimalbox nontunai,Decimalbox card, String path ) {
		
		Calendar dari = Calendar.getInstance();
		dari.setTime(dateFrom.getValue());
		
		Calendar sampai = Calendar.getInstance();
		sampai.setTime(dateTo.getValue());
		sampai.add(Calendar.DATE, 1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###");
		
		double vtunai=0;
		double vcard =0;
		double vnontunai=0;
		double totalTunai = 0;
		double totalNonTunai = 0;
		double totalCard = 0;
		
		String typeLaporan = (String)pendapatanList.getSelectedItem().getValue();
		String typePasien = (String)tipeList.getSelectedItem().getValue();
		
		try {
			rekapList.getItems().clear();
			List<Object[]> l = this.dao.getRekapBillFunction(dateFrom.getValue(), dateTo.getValue(), typeLaporan, typePasien);
			for(Object[] o : l){
				Listitem item = new Listitem();
				item.setParent(rekapList);
				Date tgl = (Date)o[0];
				Listcell cell = new Listcell(sdf.format(tgl));
				cell.setParent(item);
				
				Listcell cellKwitansi = new Listcell((String)o[1]);
				cellKwitansi.setStyle("text-decoration:none; color:blue");
				cellKwitansi.setParent(item);
				
				Listcell cellNama = new Listcell((String)o[2]);
				cellNama.setParent(item);
				
				Listcell cellMr = new Listcell((String)o[3]);
				cellMr.setParent(item);
				
				Listcell cellBpjs = new Listcell((String)o[4]);
				cellBpjs.setParent(item);
				
				Listcell cellKls = new Listcell((String)o[5]);
				cellKls.setParent(item);
				
				Listcell masuk = new Listcell(sdf.format((Date)o[6]));
				masuk.setParent(item);
				
				Date tglKeluar = (Date)o[7];
				Listcell keluar = new Listcell(tglKeluar == null ? "":sdf.format(tglKeluar));
				keluar.setParent(item);
				
				Listcell dokter = new Listcell((String)o[8]);
				dokter.setParent(item);
				
				db.setValue(new BigDecimal((Double)o[9]));
				Listcell cellTotal = new Listcell(db.getText());
				cellTotal.setParent(item);
				
				vtunai = (Double)o[10];
				vcard = (Double)o[11];
				vnontunai = (Double)o[12];
				totalTunai = totalTunai + vtunai;
				totalCard = totalCard + vcard;
				totalNonTunai = totalNonTunai + vnontunai;
				
				db.setValue(new BigDecimal(vtunai));
				Listcell cellTunai = new Listcell(db.getText());
				cellTunai.setParent(item);
				
				db.setValue(new BigDecimal(vcard));
				Listcell cellCard = new Listcell(db.getText());
				cellCard.setParent(item);
				
				db.setValue(new BigDecimal(vnontunai));
				Listcell cellNontunai = new Listcell(db.getText());
				cellNontunai.setParent(item);
				
				Listcell bank = new Listcell((String)o[13]);
				bank.setParent(item);
				
				Listcell ins = new Listcell((String)o[14]);
				ins.setParent(item);
				
				
				
			}
			/*
			List<TbPatientBill> bills = this.dao.getRekapBill(dari.getTime(), sampai.getTime(), typeLaporan, typePasien);
			
			rekapList.getItems().clear();
			
			for(TbPatientBill bill : bills){
				Listitem item = new Listitem();
				item.setParent(rekapList);
				
				Listcell cell = new Listcell(sdf.format(bill.getDWhnCreate()));
				cell.setParent(item);
				
				Listcell cellKwitansi = new Listcell(bill.getVPbillCode());
				cellKwitansi.setParent(item);
				
				Listcell cellNama = new Listcell(bill.getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName());
				cellNama.setParent(item);
				
				Listcell cellMr = new Listcell(bill.getTbRegistration().getTbMedicalRecord().getVMrCode());
				cellMr.setParent(item);
				
				if(bill.getTbRegistration().getTbMedicalRecord().getMsPatient().getMsPatientType() == null){
					Listcell cellPtype = new Listcell("NON BPJS");
					cellPtype.setParent(item);
				}
				else{
					if(bill.getTbRegistration().getTbMedicalRecord().getMsPatient().getMsPatientType().getVTpatient().equalsIgnoreCase("4")){
						Listcell cellBpjs = new Listcell("BPJS");
						cellBpjs.setParent(item);
					}
					else {
						Listcell cellNonBpjs = new Listcell("NON BPJS");
						cellNonBpjs.setParent(item);
					}
				}
				
				if(bill.getTbRegistration().getVRegSecondaryId().substring(0, 1).equalsIgnoreCase("J")){
					Listcell cellTarifJalan = new Listcell("-");
					cellTarifJalan.setParent(item);
					
					Listcell cellRegistrasiJalan = new Listcell(sdf.format(bill.getTbRegistration().getDRegistrationDate()));
					cellRegistrasiJalan.setParent(item);
					
					Listcell cellKeluarJalan = new Listcell(sdf.format(bill.getTbRegistration().getDRegistrationDate()));
					cellKeluarJalan.setParent(item);
				}
				else {
					TbBedOccupancy bedYangDiPakai = this.bocDao.getBedOccupanyByRegId(bill.getTbRegistration().getNRegId());
					
//					Set<TbBedOccupancy> bocs = bill.getTbRegistration().getTbBedOccupancies();
//					TbBedOccupancy bedYangDiPakai = null;
//					for(TbBedOccupancy boc : bocs){
//						if(boc.getVOutNote().equalsIgnoreCase("K")){
//							bedYangDiPakai = boc;
//						}
//					}
					
					if(bedYangDiPakai != null){
						Listcell cellTarifInap = new Listcell(bedYangDiPakai.getId().getMsBed().getMsTreatmentClass().getVTclassDesc());
						cellTarifInap.setParent(item);
						
						Listcell cellMasukInap = new Listcell(sdf.format(bill.getTbRegistration().getDRegistrationDate()));
						cellMasukInap.setParent(item);
						
						if(bedYangDiPakai.getDCheckOutTime() != null){
							Listcell cellKeluarInap = new Listcell(sdf.format(bedYangDiPakai.getDCheckOutTime()));
							cellKeluarInap.setParent(item);
						}else {
							Listcell cellKeluarInap = new Listcell("-");
							cellKeluarInap.setParent(item);
						}
						
					}
					else{
						Listcell cellTarifInap = new Listcell("-");
						cellTarifInap.setParent(item);
						
						Listcell cellMasukInap = new Listcell(sdf.format(bill.getTbRegistration().getDRegistrationDate()));
						cellMasukInap.setParent(item);
						
						Listcell cellKeluarInap = new Listcell(sdf.format(bill.getDSettlementDate()));
						cellKeluarInap.setParent(item);
					}
				}
				
				String staff = "-";
				if(bill.getTbRegistration().getMsStaff() != null)
					staff = bill.getTbRegistration().getMsStaff().getVStaffName();
				Listcell cellDokter = new Listcell(staff);
				cellDokter.setParent(item);
				
				db.setValue(new BigDecimal(bill.getNPbillTtlPaid()));
				Listcell cellBiaya = new Listcell(db.getText());
				cellBiaya.setParent(item);
				
				vtunai = 0;
				vnontunai = 0;
				String perusahaan = "-";
				
				Set<TbPatientSettlement> settle = bill.getTbPatientSettlements();
				
				for(TbPatientSettlement stl : settle){
					if(stl.getNPsettlementType().intValue() == 3){
						vtunai = vtunai + stl.getNAmountSettled().doubleValue();
					}else{
						vnontunai = vnontunai + stl.getNAmountSettled().doubleValue();
						
						if(stl.getMsCreditCardType() != null)
							perusahaan = stl.getMsCreditCardType().getNCcTypeDesc();
						else if(stl.getMsInsurance() != null)
							perusahaan = stl.getMsInsurance().getVInsuranceName();
					}
				}
				
				totalTunai = totalTunai + vtunai;
				totalNonTunai = totalNonTunai + vnontunai;
				
				db.setValue(new BigDecimal(vtunai));
				Listcell cellTunai = new Listcell(db.getText());
				cellTunai.setParent(item);
				
				db.setValue(new BigDecimal(vnontunai));
				Listcell cellNontunai = new Listcell(db.getText());
				cellNontunai.setParent(item);
				
				Listcell cellPerusahaan = new Listcell(perusahaan);
				cellPerusahaan.setParent(item);
				

				
			}
			*/
			tunai.setValue(new BigDecimal(totalTunai));
			nontunai.setValue(new BigDecimal(totalNonTunai));
			card.setValue(new BigDecimal(totalCard));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String generateKwitansi(String noKwitansi, String path) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		TbPatientBill bill = dao.getBillByCode(noKwitansi);
		
		String filename = bill.getTbRegistration().getTbMedicalRecord().getVMrCode()+"_"+sdf.format(bill.getDSettlementDate())+".pdf";
		path = path + filename;
		try {
			createKwitansiPdf(path, bill);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return path;
	}

	private void createKwitansiPdf(String filePath, TbPatientBill bill) throws Exception {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
//		writer.setPageEvent(new PDFBackground());
		document.open();
		
		Font font12b = new Font(Font.TIMES_ROMAN,12, Font.BOLD | Font.UNDERLINE);
		
		PdfPTable header = createHeader();
		document.add(header);
		
		//create header title
		Phrase headPh = new Phrase("KWITANSI PEMBAYARAN",font12b);
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		document.add(table);
		//end create header title
		
		Paragraph par = new Paragraph();
		addEmptyLine(par, 1);
		document.add(par);
		
		PdfPTable patient = getPatientIdentity(bill);
		document.add(patient);
		par = new Paragraph();
		addEmptyLine(par, 1);
		document.add(par);
		
		PdfPTable tagihan = getRincian(bill);
		document.add(tagihan);
		document.close();
		
	}
	private PdfPTable getRincian(TbPatientBill bill) throws Exception{
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{3,8,1,2});
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###");
		
		Font font10b = new Font(Font.TIMES_ROMAN,10, Font.BOLD);
		Font font10n = new Font(Font.TIMES_ROMAN,10, Font.NORMAL);
		PdfPCell cell = new PdfPCell(new Phrase("NOTA",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("KETERANGAN",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("QTY",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("SUBTOTAL",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		Set<TbExamination> notas = bill.getTbExamination();
		for(TbExamination nota : notas) {
			if(nota.getTotal() > 0 ) {
								
				List itemTrxs = noteDao.getItemTrxBaseOnNote(nota);
				double harga;
				double hargaAkhir;
				double jasaR;
				int r;
				Iterator itr = itemTrxs.iterator();
				while(itr.hasNext()){
					harga = 0;
					jasaR = 0;
					hargaAkhir = 0;
					Object[] obj = (Object[])itr.next();
					cell = new PdfPCell(new Phrase(nota.getVNoteNo(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(obj[1].toString()+" - "+obj[2].toString(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(obj[3].toString(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					jasaR = (((Short)obj[8]).doubleValue() * new Integer(MessagesService.getKey("jasa.apotik")).doubleValue());
					harga = ((Double)obj[4]).doubleValue()+ jasaR;
					
					hargaAkhir = (Double)obj[6] + jasaR;
					
					db.setValue(new BigDecimal(hargaAkhir));
					cell = new PdfPCell(new Phrase(db.getText(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
				}
				
				
				List racikans = noteDao.getRacikan(nota);
					
				itr = racikans.iterator();
				while(itr.hasNext()){
					TbDrugIngredients drug = (TbDrugIngredients)itr.next();
					r = drug.getNEr();
					jasaR = 0;
					harga = 0;
					hargaAkhir = 0;
					
					cell = new PdfPCell(new Phrase(drug.getTbExamination().getVNoteNo(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(drug.getVDingrId() +" "+drug.getVItemComposition(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(""+drug.getNDingrQty(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					Set<TbDrugIngredientsDetail> detil = drug.getTbDrugIngredientsDetails();
					for(TbDrugIngredientsDetail det : detil){
//						r = r + det.getMsItem().getNR();
					}
					
					jasaR = r * new Integer(MessagesService.getKey("jasa.apotik")).doubleValue();
					harga = drug.getNAmountTrx() + jasaR;
					hargaAkhir = drug.getNAmountAfterDisc() + jasaR;
					
					db.setValue(new BigDecimal(hargaAkhir));
					cell = new PdfPCell(new Phrase(db.getText(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

				}
				
				
				List treatmentTrxs = noteDao.getTreatmentTrx(nota); 

				itr = treatmentTrxs.iterator();
				while(itr.hasNext()){
					TbTreatmentTrx trx = (TbTreatmentTrx)itr.next();
					
					cell = new PdfPCell(new Phrase(nota.getVNoteNo(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(trx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode()+" - "+
							trx.getMsTreatmentFee().getMsTreatment().getVTreatmentName(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(""+trx.getNQty(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					db.setValue(new BigDecimal(trx.getNAmountAfterDisc()));
					cell = new PdfPCell(new Phrase(db.getText(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
				}
				
				
				List bundles = noteDao.getBundleTrx(nota); 
					
				itr = bundles.iterator();
				while(itr.hasNext()){
					TbBundledTrx trx = (TbBundledTrx)itr.next();
					
					cell = new PdfPCell(new Phrase(trx.getTbExamination().getVNoteNo(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(trx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode()+" - "+
							trx.getMsTreatmentFee().getMsTreatment().getVTreatmentName(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(trx.getNQty()+"", font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					db.setValue(new BigDecimal(trx.getNAmountAfterDisc()));
					cell = new PdfPCell(new Phrase(db.getText(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
				}
				
				
				
				List bedTrxs = noteDao.getBedTrx(nota); 
					nota.getTbBedTrx();
				itr = bedTrxs.iterator();
				while(itr.hasNext()){
					TbBedTrx trx = (TbBedTrx)itr.next();
					cell = new PdfPCell(new Phrase(nota.getVNoteNo(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase("BED - "+trx.getMsBed().getVBedDesc(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(trx.getNTotalHour() +" HARI", font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					db.setValue(new BigDecimal(trx.getNFee()));
					cell = new PdfPCell(new Phrase(db.getText(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
				}
				
				
				List miscs = noteDao.getMiscTrx(nota); 
					
				itr = miscs.iterator();
				while(itr.hasNext()){
					TbMiscTrx trx = (TbMiscTrx)itr.next();
					
					cell = new PdfPCell(new Phrase(nota.getVNoteNo(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase (MedisafeConstants.MISC_CODE +" - "+trx.getVMiscName(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(trx.getNQty()+"", font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					db.setValue(new BigDecimal(trx.getNAmountAfterDisc()));
					cell = new PdfPCell(new Phrase(db.getText(), font10n));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
				}
				
			}
		}
		
		cell = new PdfPCell(new Phrase("TOTAL", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		db.setValue(new BigDecimal(bill.getNPbillSubTtl()));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("NILAI RETUR", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		Double d = bill.getNPbillSubTtl()-bill.getNPbillDisc()-bill.getNPbillTtlPaid();
		if(d > 0)
			db.setValue(new BigDecimal(d));
		else db.setValue(new BigDecimal(0));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("JUMLAH DISKON", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		db.setValue(new BigDecimal(bill.getNPbillDisc()));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		
		Double pembagiPajak = bill.getNPbillSubTtl() - d;
		Double persenPajak = (bill.getNPbillTax()/pembagiPajak) * 100; 
		
		db.setValue(new BigDecimal(persenPajak));
		
		cell = new PdfPCell(new Phrase("PAJAK( "+db.getText()+" % )", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		db.setValue(new BigDecimal(bill.getNPbillTax()));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		
		cell = new PdfPCell(new Phrase("TOTAL AKHIR", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		db.setValue(new BigDecimal(bill.getNPbillTtlPaid()));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		Double cash = 0d;
		Double nonCash = 0d;
		
		Set<TbPatientSettlement> settlements = bill.getTbPatientSettlements();
		for(TbPatientSettlement settle : settlements) {
			if(settle.getNPsettlementType().equals(MedisafeConstants.CASH_SETTLEMENT))
				cash = cash + settle.getNAmountSettled();
			else nonCash = nonCash + settle.getNAmountSettled();
		}
		
		cell = new PdfPCell(new Phrase("TUNAI", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		if (cash <= 0) cash = 0d;
		
		db.setValue(new BigDecimal(cash));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("KREDIT", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		db.setValue(new BigDecimal(nonCash));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("DEPOSIT", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("0",font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		Double kembali = cash + nonCash - bill.getNPbillTtlPaid();
		if(kembali <= 0) kembali = 0d;
		
		cell = new PdfPCell(new Phrase("KEMBALI", font10n));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		db.setValue(new BigDecimal(kembali));
		cell = new PdfPCell(new Phrase(db.getText(),font10n));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		return table;
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private PdfPTable getPatientIdentity(TbPatientBill bill) throws Exception{
		Font font12 = new Font(Font.TIMES_ROMAN,10, Font.NORMAL);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{10,1,10,10,1,10});
		

		Phrase headPh = new Phrase("NO REGISTER",font12);
		PdfPCell cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(bill.getTbRegistration().getVRegSecondaryId(),font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase("NO REKAM MEDIS",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(bill.getTbRegistration().getTbMedicalRecord().getVMrCode(),font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		//second row
		headPh = new Phrase("NAMA PASIEN",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(bill.getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName(),font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase("DOKTER",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		headPh = new Phrase(bill.getTbRegistration().getMsStaff().getVStaffName(),font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		//third row
		headPh = new Phrase("UNIT",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
				
		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		String lbl = "";
		if(null != bill.getTbRegistration().getMsUnit()) lbl = 	bill.getTbRegistration().getMsUnit().getVUnitName();	
		headPh = new Phrase(lbl,font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
				
		headPh = new Phrase("TIPE PASIEN",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
				
		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
				
		headPh = new Phrase(bill.getTbRegistration().getTbMedicalRecord().getMsPatient().getMsPatientType().getVTpatientDesc(),font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		//4th row
		headPh = new Phrase("NO KWITANSI",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		headPh = new Phrase(bill.getVPbillCode(),font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		headPh = new Phrase("TANGGAL",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		headPh = new Phrase(":",font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		headPh = new Phrase(sdf.format(bill.getDSettlementDate()),font12);
		cell = new PdfPCell(headPh);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		
		return table;
	}

	private PdfPTable createHeader() throws Exception{
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{1,2});
		
		URL  url = CashierManagerImpl.class.getResource("TS.jpg");
		Image img = Image.getInstance(url.getPath());
		img.setAbsolutePosition(80f, 80f);
		PdfPCell cell = new PdfPCell(img,true);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(getHeaderInformation());
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		return table;
	}
	
	private PdfPTable getHeaderInformation(){
		Font font18b = new Font(Font.TIMES_ROMAN,18, Font.BOLD);
		Font font10b = new Font(Font.TIMES_ROMAN,10, Font.BOLD);
		
		PdfPTable table = new PdfPTable(1);
		
		
		Phrase headPh = new Phrase(" ",font18b);
		PdfPCell cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		headPh = new Phrase(MessagesService.getKey("hospital.name"),font18b);
		cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		headPh = new Phrase(MessagesService.getKey("hospital.address"),font10b);
		cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		headPh = new Phrase(MessagesService.getKey("hospital.phone"),font10b);
		cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		return table;
		
	}

	@Override
	public void getRegistration(RekapObatController controller, int input_type)
			throws VONEAppException {
		

		TbMedicalRecord mr = null;
		if(input_type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			mr = mrDao.getMrRegistered(code);
			if(mr == null){
				
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
				} catch (InterruptedException e) {
					// todo Auto-generated catch block
					e.printStackTrace();
				}
				controller.MRNumber.focus();
				
				return;
			}
			
			controller.setTbRegistration(regDao.getLastRegistrationByMrId(mr.getNMrId()));
		}
		else{
			Listitem item = controller.getMRNumberList().getSelectedItem();
			mr = (TbMedicalRecord)item.getValue();
			controller.setTbRegistration(regDao.getLastRegistrationByMrId(mr.getNMrId()));
			mr = controller.getTbRegistration().getTbMedicalRecord();
		}
		
		 
		
		controller.MRNumber.setValue(mr.getVMrCode());
		controller.address.setValue(mr.getMsPatient().getVPatientMainAddr());
		

		
		controller.patientName.setValue(mr.getMsPatient().getVPatientName());
		controller.regNo.setValue(controller.getTbRegistration().getVRegSecondaryId());
		if(mr.getMsPatient().getMsPatientType() != null)
			controller.tipePasien.setValue(mr.getMsPatient().getMsPatientType().getVTpatientDesc());
	
		controller.setMsPatient(mr.getMsPatient());
		if(controller.getTbRegistration().getVRegSecondaryId().substring(0, 1).equals("I")){
			
			controller.isRanap = true;
			TbBedOccupancy boc= bocDao.getBedOccupanyByRegId(controller.getTbRegistration().getNRegId());
				
			controller.ranapKelas = boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc();
			
			controller.getBed().setValue(boc.getId().getMsBed().getMsRoom().getVRoomName());

			controller.getTClass().setValue(boc.getId().getMsBed().getMsTreatmentClass().getVTclassDesc());
			
		}
		else{
			controller.isRanap = false;
			controller.getBed().setValue(null);
			controller.getTClass().setValue(null);
		}
		
		
	
	}

	@Override
	public void getRekapObat(RekapObatController controller) throws VONEAppException{
		controller.printList.getItems().clear();
		Integer type = new Integer(controller.drugTypeList.getSelectedItem().getValue().toString());
		List listObat = this.dao.getItemTrx(controller.tbRegistration, type);
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###");
		
		
		
		Double totTransaksi = 0.0;
		Double totRetur = 0.0;
		
		Iterator it = listObat.iterator();
		while(it.hasNext()){
			Object[] o = (Object[])it.next();
			Listitem item = new Listitem();
			item.setParent(controller.printList);
			
			Listcell cell = new Listcell(o[1].toString());
			cell.setParent(item);
			
			cell = new Listcell(o[2].toString());
			cell.setParent(item);
			
			cell = new Listcell(o[3].toString());
			cell.setParent(item);
			
			Integer jml = (Integer)o[4];
			Double trx = (Double)o[5];
			
			//cek apakah ketika rawat jalan beli obat yg sama
			/*
			List listObatRajal = this.dao.getItemTrx(controller.tbRegistration.getTbRegistration(), (Integer)o[0], type);
			if(listObatRajal.size() > 0){
				Object[] o2 = (Object[])listObatRajal.get(0);
				jml = jml + (Integer)o2[4];
				trx = trx + (Double)o2[5];
			}*/
			totTransaksi = totTransaksi + trx;
			
			db.setValue(new BigDecimal(jml));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
			db.setValue(new BigDecimal(trx));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
			List listRetur = this.dao.getRetur(controller.tbRegistration, (Integer)o[0]);
			if(listRetur.size() > 0){
				Object[] obj = (Object[])listRetur.get(0);
				db.setValue(new BigDecimal((Integer)obj[1]));
				
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
				db.setValue(new BigDecimal((Double)obj[2]));
				totRetur = totRetur + db.getValue().doubleValue();
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
				db.setValue(new BigDecimal(trx-(Double)obj[2]));
				cell = new Listcell(db.getText());
				cell.setParent(item);
			}else{
				cell = new Listcell("0");
				cell.setParent(item);
				
				cell = new Listcell("0");
				cell.setParent(item);
				
				db.setValue(new BigDecimal(trx));
				cell = new Listcell(db.getText());
				cell.setParent(item);
			}
			
		}
		
		controller.deposit.setValue(new BigDecimal(totTransaksi));
		controller.returDecimalbox.setValue(new BigDecimal(totRetur));
		controller.total.setValue(new BigDecimal(totTransaksi-totRetur));
		
	}

	@Override
	public List<TbKwitansi> getAllKwitansi() throws VONEAppException {
		
		List<TbKwitansi> kwitansi = this.kwitansiDao.getKwitansiList();
		
		return kwitansi;
		
	}

	@Override
	public TbKwitansi getKwitansi(Date value, String tipe) throws VONEAppException {
		
		return this.kwitansiDao.getKwitansi(tipe, value);
	}

	@Override
	public boolean saveKwitansi(TbKwitansi kwitansi) throws VONEAppException {
		return this.kwitansiDao.save(kwitansi);
	}


	
}

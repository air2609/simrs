package com.vone.medisafe.ward;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.dao.NoteDAO;

/**
 * BedTransactionManagerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 13 2006
 * @category Service (implemented model)
 */

public class BedTransactionManagerImpl implements BedTransactionManager{
	
	private BedTransactionDAO dao;
	private WardTransactionDAO wardao;
	private NoteDAO noteDao;

	public NoteDAO getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}

	public WardTransactionDAO getWardao() {
		return wardao;
	}

	public void setWardao(WardTransactionDAO wardao) {
		this.wardao = wardao;
	}

	public BedTransactionDAO getDao() {
		return dao;
	}

	public void setDao(BedTransactionDAO dao) {
		this.dao = dao;
	}

		
	public TbBedTrx getBTrxByNota(TbExamination nota) throws VONEAppException { 
		return this.dao.getBTrxByNota(nota);
	}

	public boolean save(TbExamination nota, TbBedTrx bedTrx, 
			boolean isClosed, TbBedOccupancy boc, List<Date> bors) throws VONEAppException {
		boolean hasil = false;
		
		dao.save(nota, bedTrx,isClosed, boc, bors);
		noteDao.save(noteDao.getNote(nota.getNExamId()));
		hasil = true;
		return hasil;
		
		
	}

	public List<TbBedTrx> getBedTrxBaseOnPatient(MsPatient patient, MsBed bed) throws VONEAppException {
		
		List<TbBedTrx> hasil = new ArrayList<TbBedTrx>();
		List result = dao.getBedTrxBaseOnPatient(patient, bed);
		Iterator it = result.iterator();
		while(it.hasNext()){
			hasil.add((TbBedTrx)it.next());
		}
		
		return hasil;
	}

	
	
	public void getPatitentRegistration(BedTransactionController controller, int type) 
		throws VONEAppException, InterruptedException {
		
		Listitem item;
		
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.MRNumber.getText());
			controller.MRNumber.setValue(code);
			
			controller.reg = wardao.getRanapByMrCode(code);
			
			if(controller.reg == null){
				
				Messagebox.show(MessagesService.getKey("mr.not.found"));
				controller.MRNumber.focus();
				
				return;
			}
			controller.mr = controller.reg.getTbMedicalRecord();
		}
		else{
			
			item = controller.patientList.getSelectedItem();
			controller.mr = (TbMedicalRecord)item.getValue();
			controller.reg = wardao.getLastRanap(controller.mr);
			controller.mr = controller.reg.getTbMedicalRecord();
			controller.MRNumber.setValue(controller.mr.getVMrCode());
			
		}
		
		
		
				
		int[] umurSkrg = MedisafeUtil.calculateAge(controller.mr.getMsPatient().getDPatientDob());
		controller.age.setValue(MedisafeUtil.convertAgeToString(umurSkrg));
		
		if(controller.mr.getMsPatient().getVPatientGender().equals("M")){
			controller.gender.setSelectedIndex(0);
		}
		else{
			controller.gender.setSelectedIndex(1);
		}
		
		controller.patient = controller.mr.getMsPatient();
				
		controller.patientName.setValue(controller.mr.getMsPatient().getVPatientName());
		controller.registrationNumber.setValue(controller.reg.getVRegSecondaryId());
		
		getBedsOccupancy(controller.reg, controller.childTree, controller.patient);
		
		if(controller.mr.getMsPatient().getMsPatientType() != null)
			controller.patientType.setValue(controller.mr.getMsPatient().getMsPatientType().getVTpatientDesc());
		
	}
	
	
	
	public void getBedsOccupancy(TbRegistration reg, Treechildren childTree, MsPatient patient) 
		throws InterruptedException, VONEAppException {
		
		SimpleDateFormat ftgl = new SimpleDateFormat("dd/MM/yyyy");
		
		
		List<TbBedOccupancy> hasil = dao.getBocsBaseOnRegistration(reg); 
			
		String jam = null;
		
		int hours = 0;
		int minutes = 0;
		double hargaTotal = 0;
		
		String[] jams = null;
		
		Treecell tcell = null;
		Decimalbox decbox = new Decimalbox();
		decbox.setFormat("#,###.##");
		
		childTree.getChildren().clear();
		
		Treeitem treeItem = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		
		for(TbBedOccupancy boc : hasil){
			Treeitem treeitem = new Treeitem();
			treeitem.setValue(boc);
			treeitem.setParent(childTree);
			
			Treerow treerow = new Treerow();
			treerow.setParent(treeitem);
			
			
			tcell = new Treecell(boc.getId().getMsBed().getVBedDesc());
			tcell.setParent(treerow);
			
			Treechildren child = new Treechildren();
			child.setParent(treeitem);
			
			Treeitem item2 = new Treeitem();
			item2.setValue(boc.getDCheckInTime());
			item2.setParent(child);
			
			Treerow row = new Treerow();
			row.setParent(item2);
						
			tcell = new Treecell(ftgl.format(boc.getDCheckInTime()));
			tcell.setParent(row);
			
			if(boc.getDCheckOutTime() != null){
				if(ftgl.format(boc.getDCheckInTime()).equals(ftgl.format(boc.getDCheckOutTime()))){
					//jam = MedisafeUtil.calculateTimeDifferent(sdf.format(boc.getDCheckOutTime()),
							//sdf.format(boc.getDCheckInTime()));
					jam = "1 HARI";
					
					tcell = new Treecell(jam);
					tcell.setParent(row);
					
									
					decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					/**
					jams = jam.split(":");
					hours = new Integer(jams[0]).intValue();
					minutes = new Integer(jams[1]).intValue();
					if(minutes > 0) hours = hours + 1;
					
					hargaTotal = hours * decbox.getValue().doubleValue(); */
					hargaTotal = boc.getId().getMsBed().getNBedPrice();
					decbox.setValue(new BigDecimal(hargaTotal));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					item2.setAttribute("total", new Double(hargaTotal));
					item2.setAttribute("jam", new Integer(1));
					
					
					tcell = new Treecell("-");
					tcell.setParent(row);
					
				}else{
					//jam = MedisafeUtil.calculateTimeDifferent("23:59:59",sdf.format(boc.getDCheckInTime()));
					jam = "1 HARI";
					
					tcell = new Treecell(jam);
					tcell.setParent(row);
					
					decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					
					/**
					jams = jam.split(":");
					hours = new Integer(jams[0]).intValue();
					minutes = new Integer(jams[1]).intValue();
					if(minutes > 0) hours = hours + 1;
					
					hargaTotal = hours * decbox.getValue().doubleValue(); */
					hargaTotal = boc.getId().getMsBed().getNBedPrice();
					decbox.setValue(new BigDecimal(hargaTotal));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					
					item2.setAttribute("total", new Double(hargaTotal));
					item2.setAttribute("jam", new Integer(1));
					
					tcell = new Treecell("-");
					tcell.setParent(row);
					
					List<Date> tanggals = MedisafeUtil.getDateCollectionBetween2Date(boc.getDCheckInTime(), boc.getDCheckOutTime());
					
					for(Date tanggal : tanggals){
						
						treeItem = new Treeitem();
						treeItem.setValue(tanggal);
						treeItem.setParent(child);
						
						Treerow row2 = new Treerow();
						row2.setParent(treeItem);
											
						tcell = new Treecell(ftgl.format(tanggal));
						tcell.setParent(row2);
						
								
						//jam = "24:00:00";
						jam = "1 HARI";
												
						tcell = new Treecell(jam);
						tcell.setParent(row2);
						
									
						decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
						
						tcell = new Treecell(decbox.getText());
						tcell.setParent(row2);
						
						/**
						jams = jam.split(":");
						hours = new Integer(jams[0]).intValue();
						minutes = new Integer(jams[1]).intValue();
						if(minutes > 0) hours = hours + 1;
						
						hargaTotal = hours * decbox.getValue().doubleValue();*/
						hargaTotal = boc.getId().getMsBed().getNBedPrice();
						decbox.setValue(new BigDecimal(hargaTotal));
						
						tcell = new Treecell(decbox.getText());
						tcell.setParent(row2);
						
						treeItem.setAttribute("total", new Double(hargaTotal));
						treeItem.setAttribute("jam", new Integer(1));
						
						tcell = new Treecell("-");
						tcell.setParent(row2);
					}
					
					treeItem = new Treeitem();
					treeItem.setValue(boc.getDCheckOutTime());
					treeItem.setParent(child);
					
					Treerow row2 = new Treerow();
					row2.setParent(treeItem);
									
					tcell = new Treecell(ftgl.format(boc.getDCheckOutTime()));
					tcell.setParent(row2);
					
					jam = sdf.format(new Date());
					tcell = new Treecell(jam);
					tcell.setParent(row2);
					
								
					decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row2);
					/**
					jams = jam.split(":");
					hours = new Integer(jams[0]).intValue();
					minutes = new Integer(jams[1]).intValue();
					if(minutes > 0) hours = hours + 1;
					
					hargaTotal = hours * decbox.getValue().doubleValue(); */
					hargaTotal = boc.getId().getMsBed().getNBedPrice();
					decbox.setValue(new BigDecimal(hargaTotal));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row2);
					
					treeItem.setAttribute("total", new Double(hargaTotal));
					treeItem.setAttribute("jam", new Integer(1));
					
					tcell = new Treecell("-");
					tcell.setParent(row2);
				}
				
				
			}
			else{
				
				if(ftgl.format(boc.getDCheckInTime()).equals(ftgl.format(new Date()))){
					/**jam = MedisafeUtil.calculateTimeDifferent(sdf.format(new Date()),
							sdf.format(boc.getDCheckInTime()));*/
					jam="1 HARI";
					tcell = new Treecell(jam);
					tcell.setParent(row);
					
					decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					/**
					jams = jam.split(":");
					hours = new Integer(jams[0]).intValue();
					minutes = new Integer(jams[1]).intValue();
					if(minutes > 0) hours = hours + 1;
					
					hargaTotal = hours * decbox.getValue().doubleValue();*/
					hargaTotal = boc.getId().getMsBed().getNBedPrice();
					decbox.setValue(new BigDecimal(hargaTotal));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					
					item2.setAttribute("total", new Double(hargaTotal));
					item2.setAttribute("jam", new Integer(1));
					
					tcell = new Treecell("-");
					tcell.setParent(row);
				}else{
					/**jam = MedisafeUtil.calculateTimeDifferent("23:59:59",sdf.format(boc.getDCheckInTime()));*/
					jam = "1 HARI";
					
					tcell = new Treecell(jam);
					tcell.setParent(row);
					
					decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					/**
					jams = jam.split(":");
					hours = new Integer(jams[0]).intValue();
					minutes = new Integer(jams[1]).intValue();
					if(minutes > 0) hours = hours + 1;
					
					hargaTotal = hours * decbox.getValue().doubleValue(); */
					hargaTotal = boc.getId().getMsBed().getNBedPrice();
					decbox.setValue(new BigDecimal(hargaTotal));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row);
					
					item2.setAttribute("total", new Double(hargaTotal));
					item2.setAttribute("jam", new Integer(1));
					
					tcell = new Treecell("-");
					tcell.setParent(row);
					
					List<Date> tanggals = MedisafeUtil.getDateCollectionBetween2Date(boc.
												getDCheckInTime(), new Date());
					for(Date tanggal : tanggals){
						
						treeItem = new Treeitem();
						treeItem.setValue(tanggal);
						treeItem.setParent(child);
						
						Treerow row2 = new Treerow();
						row2.setParent(treeItem);
											
						tcell = new Treecell(ftgl.format(tanggal));
						tcell.setParent(row2);
						
								
						//jam = "24:00:00";
						jam = "1 HARI";
						tcell = new Treecell(jam);
						tcell.setParent(row2);
						
									
						decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
						
						tcell = new Treecell(decbox.getText());
						tcell.setParent(row2);
						
						/**
						jams = jam.split(":");
						hours = new Integer(jams[0]).intValue();
						minutes = new Integer(jams[1]).intValue();
						if(minutes > 0) hours = hours + 1;
						
						hargaTotal = hours * decbox.getValue().doubleValue();*/
						hargaTotal = boc.getId().getMsBed().getNBedPrice();
						decbox.setValue(new BigDecimal(hargaTotal));
						
						tcell = new Treecell(decbox.getText());
						tcell.setParent(row2);
						
						treeItem.setAttribute("total", new Double(hargaTotal));
						treeItem.setAttribute("jam", new Integer(1));
																		
						tcell = new Treecell("-");
						tcell.setParent(row2);
					}
					
					treeItem = new Treeitem();
					treeItem.setValue(new Date());
					treeItem.setParent(child);
					
					Treerow row2 = new Treerow();
					row2.setParent(treeItem);
									
					tcell = new Treecell(ftgl.format(new Date()));
					tcell.setParent(row2);
					
					//jam = sdf.format(new Date());
					jam = "1 HARI";
					tcell = new Treecell(jam);
					tcell.setParent(row2);
					
								
					decbox.setValue(new BigDecimal(boc.getId().getMsBed().getNBedPrice()));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row2);
					/**
					jams = jam.split(":");
					hours = new Integer(jams[0]).intValue();
					minutes = new Integer(jams[1]).intValue();
					if(minutes > 0) hours = hours + 1;
					
					hargaTotal = hours * decbox.getValue().doubleValue();*/
					hargaTotal = boc.getId().getMsBed().getNBedPrice();
					decbox.setValue(new BigDecimal(hargaTotal));
					
					tcell = new Treecell(decbox.getText());
					tcell.setParent(row2);
					
					treeItem.setAttribute("total", new Double(hargaTotal));
					treeItem.setAttribute("jam", new Integer(1));
					
					tcell = new Treecell("-");
					tcell.setParent(row2);
				}
				
			}
			
			Object obj = null;
			
			List<TbBedTrx> trxs = dao.getBedTrxBaseOnPatient(patient,boc.getId().getMsBed());
						
			for(TbBedTrx trx : trxs){
				
				List<Date> tgls = MedisafeUtil.getDateCollectionBetween2Date(trx.getDDateFrom(), trx.getDDateTo());
				tgls.add(0, trx.getDDateFrom());
				tgls.add(trx.getDDateTo());
				
				for(Date tgl : tgls){
					
					Iterator it = child.getChildren().iterator();
					while(it.hasNext()){
						obj = it.next();
						treeItem = (Treeitem)obj;
						if(ftgl.format(tgl).equals(ftgl.format((Date)treeItem.getValue()))){
							tcell = (Treecell)treeItem.getTreerow().getChildren().get(4);
							tcell.setLabel(trx.getTbExamination().getVNoteNo());
						}
						
					}
					
				}
					
			}
					
		}
			
	}

}

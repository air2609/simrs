package com.vone.medisafe.mapping.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsGim;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbBundledItemUsedTrx;
import com.vone.medisafe.mapping.TbBundledTreatUsedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbPatientSettlement;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.Service;


public class NoteDAO extends HibernateDaoSupport{
	
	private static final Logger log = Logger.getLogger(NoteDAO.class);
	StringBuffer stQuery = new StringBuffer();
	
	public boolean save(TbExamination notaJournal) throws VONEAppException{
		
		boolean success = false;
		
		JournalBeanHandler jhandler;
		
		MsCoa coaAR;
		MsCoa coaBed;
		MsCoa coaInv = null;
		MsCoa coaIncome = null;
		MsCoa coaCogs = null;
		MsCoa coaTreatment;
		MsCoa coaBundled;
		
		String currentUser = "JIM";
		String voucherNo;
		StringBuffer memo;
		Double totCogs;

		PlatformTransactionManager txManager = Service.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = txManager.getTransaction(def);
				
		try {	
			
			//getHibernateTemplate().saveOrUpdate(notaJournal);
			//notaJournal = this.getNote(notaJournal.getNExamId());
			
			//Messagebox.show("treatment "+notaJournal.getTbTreatmentTrx().size());
			
			
			jhandler = new JournalBeanHandler(MedisafeConstants.ACCT_AR_STR);
			coaAR = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
			memo = new StringBuffer();
			
			//todo bed journal entry
			//Iterator<TbBedTrx> itBedTrx;
			TbBedTrx bedTrx;
			Iterator itBedTrx = notaJournal.getTbBedTrx().iterator();		
			voucherNo = notaJournal.getVNoteNo();
			while (itBedTrx.hasNext()){
				bedTrx = (TbBedTrx) itBedTrx.next();
				
				coaBed = bedTrx.getMsBed().getMsCoa();
				if (coaBed == null){
					throw new VONEAppException(MessagesService.getKey("trx.bed.coa.null"));
				}
				
				//setMemo
				memo.setLength(0);
				memo.append("BEDCODE:")
					.append(bedTrx.getMsBed().getVBedCode())
					.append(";TOTALHOUR:")
					.append(bedTrx.getNTotalHour())
					.append(";FEE:")
					.append(bedTrx.getNFee())
					.append(";DISCOUNT:")
					.append(bedTrx.getNDiscAmount());
				
				//voucherNo = bedTrx.getTbExamination().getVNoteNo();
				jhandler.addJournal(voucherNo, memo.toString(), bedTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
				jhandler.addJournal(voucherNo, memo.toString(), bedTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaBed);
			}
			
			// todo bundled journal entry
			//Iterator<TbBundledTrx> itBundledTrx;
			TbBundledTrx bundledTrx;
			Iterator itBundledTrx = notaJournal.getTbBundledTrx().iterator();
			
			while (itBundledTrx.hasNext()){
				bundledTrx = (TbBundledTrx) itBundledTrx.next();
				
				coaBundled = bundledTrx.getMsTreatmentFee().getMsCoa();
				
				
				if (coaBundled == null)				
					throw new VONEAppException(MessagesService.getKey("trx.bundled.coa.null"));
				
				double hargaPokok = 0;
				
				//voucherNo = bundledTrx.getTbExamination().getVNoteNo();
				
				//treatment journaling
//				Iterator itTreatUsed = bundledTrx.getTbBundledTreatUsedTrxes().iterator(); //tdk sampai
				Iterator itTreatUsed = getBundledTreat(bundledTrx).iterator();
				
				while (itTreatUsed.hasNext()){
					TbBundledTreatUsedTrx treatUsed = (TbBundledTreatUsedTrx) itTreatUsed.next();
					MsCoa coaBundledTreat = treatUsed.getMsCoa();
					//treatment journaling
					memo.setLength(0);
					memo.append("TCODE:")
						.append(treatUsed.getMsTreatment().getVTreatmentCode());
					
					//jhandler.addJournal(voucherNo, memo.toString(), treatUsed.getNSellingPrice(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
					
					//jurnal hanya dari bundle trx, tidak dari detailnya, jadi tidak ada accounting utk tindakannya.
					//jhandler.addJournal(voucherNo, memo.toString(), treatUsed.getNSellingPrice(), MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaBundledTreat);
					//hargaPokok += treatUsed.getNSellingPrice();
				}
				
				//item journaling
				Iterator itItemUsed = getItemBundle(bundledTrx).iterator();
					
				while (itItemUsed.hasNext()){
					TbBundledItemUsedTrx itemUsed = (TbBundledItemUsedTrx) itItemUsed.next();
					coaInv = notaJournal.getMsUnit().getMsWarehouse().getMsCoa();
					coaIncome = itemUsed.getMsItem().getItemSellAcctNo();
					coaCogs = itemUsed.getMsItem().getItemCogsNo();
					
					if (coaInv == null)
						throw new VONEAppException(MessagesService.getKey("trx.coa.inventory.warehouse.not.found"));
					if (coaIncome == null)
						throw new VONEAppException(MessagesService.getKey("trx.item.sell.coa.null"));
					if (coaCogs == null)
						throw new VONEAppException(MessagesService.getKey("trx.item.cogs.coa.null"));
					
					memo.setLength(0);
					memo.append("KODEPAKET:")
					.append(bundledTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode())					
					.append(";ITEMCODEUSED:")
					.append(itemUsed.getMsItem().getVItemCode())
					.append(";QTY:")
					.append(itemUsed.getNQty());					
					
					Double cogsAmount = itemUsed.getTbBatchItem().getNCogsPrice() * itemUsed.getNQty();
					//Double cogsAmount = new Double(0);
										
					//jhandler.addJournal(voucherNo, memo.toString(), itemUsed.getNSellingPrice(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
					
					//jhandler.addJournal(voucherNo, memo.toString(), itemUsed.getNSellingPrice(), MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaIncome);					
					jhandler.addJournal(voucherNo, memo.toString(), cogsAmount, MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaInv);
					jhandler.addJournal(voucherNo, memo.toString(), cogsAmount, MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaCogs);
					
					//bundle hanya melibatkan cogs saja, harga penjualan tidak masuk.
					//hargaPokok += itemUsed.getNSellingPrice();
				}
				hargaPokok=0;
				//untung or rugi
				//AR
				jhandler.addJournal(voucherNo, memo.toString(), bundledTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
				jhandler.addJournal(voucherNo, memo.toString(), bundledTrx.getNAmountAfterDisc() - hargaPokok, MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaBundled);
			}
			
			//added new function to add racikan transaction into journal entry
//			notaJournal.getTbDrugIngredients();
			
			//todo item journal entry
			//Iterator<TbItemTrx> itItemTrx;
			TbItemTrx itemTrx;
			Iterator itItemTrx = notaJournal.getTbItemTrx().iterator();
			MsWarehouse wh;
			while (itItemTrx.hasNext()){
				itemTrx = (TbItemTrx) itItemTrx.next();			
				
				//getHibernateTemplate().initialize(notaJournal.getMsUnit().getMsWarehouse());
				wh = notaJournal.getMsUnit().getMsWarehouse();
				coaInv = wh.getMsCoa();
				coaIncome = itemTrx.getMsItem().getItemSellAcctNo();
				coaCogs = itemTrx.getMsItem().getItemCogsNo();
				
				if(itemTrx.getTbBatchItem().getNCogsPrice() == null)
					throw new VONEAppException(MessagesService.getKey("trx.item.cogs.coa.null"));
				if (coaInv == null)
					throw new VONEAppException(MessagesService.getKey("trx.coa.inventory.unit.not.found"));				
				if (coaIncome == null)
					throw new VONEAppException(MessagesService.getKey("trx.item.sell.coa.null"));
				if (coaCogs == null)
					throw new VONEAppException(MessagesService.getKey("trx.item.cogs.coa.null"));				

				totCogs = itemTrx.getTbBatchItem().getNCogsPrice() * itemTrx.getNQty();
				
				memo.setLength(0);				
				memo.append("ITEMCODE:")
					.append(itemTrx.getMsItem().getVItemCode())
					.append(";QTY:")
					.append(itemTrx.getNQty())
					.append(";DISCOUNT:")
					.append(itemTrx.getNDiscAmount());
				
				//voucherNo = itemTrx.getTbExamination().getVNoteNo();
				
				jhandler.addJournal(voucherNo, memo.toString(), itemTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
				jhandler.addJournal(voucherNo, memo.toString(), itemTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaIncome);
				jhandler.addJournal(voucherNo, memo.toString(), totCogs, MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaInv);
				jhandler.addJournal(voucherNo, memo.toString(), totCogs, MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaCogs);
			}
			
			//todo drugingredients journal entry
			
			double totalCogs = 0;
			Set<TbDrugIngredients> racikans = notaJournal.getTbDrugIngredients();
			for(TbDrugIngredients racikan : racikans){
				totalCogs = 0;
				Set<TbDrugIngredientsDetail> detils = racikan.getTbDrugIngredientsDetails();
				for(TbDrugIngredientsDetail detil : detils){
					coaInv = notaJournal.getMsUnit().getMsWarehouse().getMsCoa();
					coaIncome = detil.getMsItem().getItemSellAcctNo();
					coaCogs = detil.getMsItem().getItemCogsNo();
					
					totalCogs = totalCogs + (detil.getTbBatchItem().getNCogsPrice().doubleValue() * detil.getNDingrDetQty());
				}
				
				memo.setLength(0);				
				memo.append("ITEMCODE:")
					.append(racikan.getVDingrId())
					.append(";QTY:")
					.append(racikan.getNDingrQty())
					.append(";DISCOUNT:")
					.append(racikan.getNDiscAmount());
				
				jhandler.addJournal(voucherNo, memo.toString(), racikan.getNAmountAfterDisc(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
				jhandler.addJournal(voucherNo, memo.toString(), racikan.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaIncome);
				jhandler.addJournal(voucherNo, memo.toString(), totalCogs, MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaInv);
				jhandler.addJournal(voucherNo, memo.toString(), totalCogs, MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaCogs);
			}
			
//			todo misc journal entry
			//Iterator<TbMiscTrx> itMiscTrx;
			TbMiscTrx miscTrx;
			Iterator itMiscTrx = notaJournal.getTbMiscTrx().iterator();
			
			while (itMiscTrx.hasNext()){
				miscTrx = (TbMiscTrx) itMiscTrx.next();
				
				//MsGim gim = MasterServiceLocator.getGimManager().getGimByCode(MedisafeConstants.COA_DEFAULT_MISC_TRX);
				MsGim gim = getGimByCode(MedisafeConstants.COA_DEFAULT_MISC_TRX);
				
				if (gim == null || gim.getVValue() == null){
					throw new VONEAppException(MessagesService.getKey("trx.misc.coa.null"));
				}
				
				//MsCoa coaMisc = MasterServiceLocator.getCoaManager().getCoaByCode(gim.getVValue());
				MsCoa coaMisc = getCoaByCode(gim.getVValue());
				
				if (coaMisc == null){
					throw new VONEAppException(MessagesService.getKey("trx.misc.coa.null"));
				}				
				
				memo.setLength(0);				
				memo.append("MISC:")
					.append(miscTrx.getVMiscName())
					.append(";QTY:")
					.append(miscTrx.getNQty());
				
				//voucherNo = miscTrx.getTbExamination().getVNoteNo();
				jhandler.addJournal(voucherNo, memo.toString(), miscTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
				jhandler.addJournal(voucherNo, memo.toString(), miscTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaMisc);
			}			
			
			//todo treatment journal entry
			//Iterator<TbTreatmentTrx> itTreatTrx;
			TbTreatmentTrx treatTrx;
			Iterator itTreatTrx = notaJournal.getTbTreatmentTrx().iterator();
			
			
			
			while (itTreatTrx.hasNext()){
				treatTrx = (TbTreatmentTrx) itTreatTrx.next();
				
				coaTreatment = treatTrx.getMsTreatmentFee().getMsCoa();
				if (coaTreatment == null)
					throw new VONEAppException(MessagesService.getKey("trx.treatment.coa.null"));
				
				memo.setLength(0);				
				memo.append("TCODE:")
					.append(treatTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode())
					.append(";QTY:")
					.append(treatTrx.getNQty())
					.append(";DISCOUNT:")
					.append(treatTrx.getNDiscAmount());					
					if (treatTrx.getMsDoctor() != null && treatTrx.getMsDoctor().getVStaffCode() != null){
						memo.append(";DOCTOR-PRIME-ID:")
						.append(treatTrx.getMsDoctor().getVStaffCode());
					}
					if (treatTrx.getAnastesiStaff() != null && treatTrx.getAnastesiStaff().getVStaffCode() != null){
						memo.append(";DOCTOR-SECOND-ID:")
						.append(treatTrx.getAnastesiStaff().getVStaffCode());
					}
				
				//voucherNo = treatTrx.getTbExamination().getVNoteNo();
				jhandler.addJournal(voucherNo, memo.toString(), 
						treatTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_DEBIT, new Date(), currentUser, coaAR);
				jhandler.addJournal(voucherNo, memo.toString(), 
						treatTrx.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), currentUser, coaTreatment);
				
			}								
						
			//final check on Journal
			if (!jhandler.isBalance()){
				Messagebox.show("jurnal tidak balance");
				throw new VONEAppException(MessagesService.getKey("master.coa.journal.discrepancy.internal"));
				
			}
			
			//prosess saving...
			
			Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
			while (it.hasNext()){
				getHibernateTemplate().save(it.next());
				
			}
			//simpan status validasi

			txManager.commit(status);
			success = true;
			
		}catch (Exception e){
//			System.out.println("ini ga dieksekusi");
//			StackTraceElement[] trc = e.getStackTrace();
//			StringBuffer sb = new StringBuffer();
//			for(int i = 0; i<trc.length; i++)
//				sb.append(trc[i]);
			txManager.rollback(status);
			
			logger.error("Save Error", e);
			throw new VONEAppException(e.getMessage(),e);
//			throw new VONEAppException(sb.toString());
		}
	
		
		return success;
	}
	
	public MsGim getGimByCode(String code) throws VONEAppException {
        logger.debug("finding MsGim instance by getGimByCode");
        
        Session session = null;
        MsGim pojo = null; 
                
        try {
        	session = getCurrentSession();        	       
        	
        	pojo = (MsGim)session.createQuery("from MsGim where v_key = :code").
        			setString("code", code)
        			.uniqueResult();        	
        	  
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        
        return pojo;
    }

	private List<TbBundledTreatUsedTrx> getBundledTreat(TbBundledTrx bundledTrx) throws VONEAppException{
		
		List<TbBundledTreatUsedTrx> list = null;
		Session session = null;
		try {
			session = getCurrentSession();
			String sqlBun = "select {trx.*} " +
								"from tb_bundled_trx bun, tb_bundled_treat_used_trx trx " +
								"where " +
								"bun.n_bundled_trx_id = trx.n_tbundled_id " +
								"and trx.n_tbundled_id = :id";
			list = session
				.createSQLQuery(sqlBun)
				.addEntity("trx", TbBundledTreatUsedTrx.class)
				.setInteger("id", bundledTrx.getNBundledTrxId())
				.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return list;
	}


	public List<TbExamination> searchNote(String noteNumber, String patientName, MsUnit unit, int status) throws VONEAppException {
		List<TbExamination> result = null;
		Session session = getCurrentSession();
		
		log.debug("ARIF DEBUG : searchNote() -- ENTER");
		
		String sql =" select " +
						
							" {note.*} " +
							
					" from " +
							" tb_examination note, " +
							" ms_patient pat " +
							
					" where " +
					
							" note.n_patient_id=pat.n_patient_id "+
							" and note.v_note_no like :noteNumber " +
							" and pat.v_patient_name like :patientName " +
							" and note.n_unit_id=:unitId "+
							" and note.n_exam_status=:noteStatus";
		
		try {
			
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("note", TbExamination.class);
			query.setString("noteNumber", noteNumber);
			query.setString("patientName", patientName);
			query.setInteger("unitId", unit.getNUnitId().intValue());
			query.setInteger("noteStatus", status);
			
			result = query.list();
			
			log.debug("ARIF DEBUG : searchNote() -- BANYAKNYA DATA DARI DATABASE : "+result.size());
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		log.debug("ARIF DEBUG : searchNote() -- EXIT");
		return result;
	}
	
	
	public List searchNoteLab(String noteNumber, String patientName, MsUnit unit) throws VONEAppException{
		List result = null;
		Session session = null;
		
		log.debug("ARIF DEBUG : searchNote() -- ENTER");
		
		String sql ="select {note.*} " +
				" from tb_examination note, ms_patient pat " +
				" where note.n_patient_id=pat.n_patient_id "+
				" AND note.v_note_no LIKE :noteNumber " +
				" AND pat.v_patient_name LIKE :patientName " +
				" AND note.n_unit_id = :unitId " +
				" AND (note.n_exam_status = :noteActive " +
				" OR note.n_exam_status = :noteValidate) ";
		
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("note", TbExamination.class);
			query.setString("noteNumber", noteNumber);
			query.setString("patientName", patientName);
			query.setInteger("unitId", unit.getNUnitId().intValue());
			query.setInteger("noteActive", MedisafeConstants.ACTIVE_NOTE);
			query.setInteger("noteValidate", MedisafeConstants.VALIDATED_NOTE);
			
			result = query.list();
			
			log.debug("ARIF DEBUG : searchNote() -- BANYAKNYA DATA DARI DATABASE : "+result.size());
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		log.debug("ARIF DEBUG : searchNote() -- EXIT");
		return result;
	}
	
	
	public TbExamination getNote(Integer id) throws VONEAppException{
		Session session = null;
		TbExamination note = null;
		
		String sql = "select {note.*} from tb_examination note where note.n_exam_id=:noteId";
				
		try {
			
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("note", TbExamination.class);
			query.setInteger("noteId", id.intValue());
			note = (TbExamination)query.uniqueResult();
			
		} catch (HibernateException e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return note;
	}
	
	
	public boolean cancelNote(TbExamination note, Integer warehouseId, String alasan) throws VONEAppException{
		boolean success = false;
		
		
		Session session = getCurrentSession();
		Transaction trans = session.beginTransaction();
		
		Date tanggal = new Date();
		
		try {
			
			if(note.getNExamStatus().intValue() == MedisafeConstants.VALIDATED_NOTE){
				createCancellationJournal(note.getVNoteNo());
				note.setNExamStatus(MedisafeConstants.VALIDATED_CANCEL);
			}
			else
				note.setNExamStatus(MedisafeConstants.CANCEL_NOTE);
			note.setVCancelationNote(alasan);
			note.setDWhnChange(tanggal);
			session.update(note);
		
			Criteria crit = session.createCriteria(TbExamination.class);
			crit.add(Restrictions.eq("NExamId", note.getNExamId()));
			
			note = (TbExamination)crit.uniqueResult();
			
						
			
			
			Set<TbItemTrx> itemsTrx = note.getTbItemTrx();
			TbItemInventory itemInventory = null;
			
			String sql = "select {inv.*} from tb_item_inventory inv where inv.n_item_id=:idItem"+
            " AND inv.n_batch_id=:batchId AND inv.n_whouse_id=:wId";
			
			for(TbItemTrx trx : itemsTrx){
				
								
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity("inv", TbItemInventory.class);
				query.setInteger("idItem", trx.getMsItem().getNItemId().intValue());
				query.setInteger("batchId", trx.getTbBatchItem().getNBatchId().intValue());
				query.setInteger("wId", warehouseId.intValue());
				
				itemInventory = (TbItemInventory)query.uniqueResult();
				float qty = trx.getNQty() + itemInventory.getNItemInvQty();
				itemInventory.setNItemInvQty(qty);
				
				session.update(itemInventory);
				
				Set<TbPatientInventory> patInvs = trx.getTbPatientInventories();
				for(TbPatientInventory patin : patInvs){
					session.delete(patin);
				}
			}
			
			Set<TbBundledTrx> bundlesTrx = note.getTbBundledTrx();
			
			
			
			for(TbBundledTrx trx : bundlesTrx){
				
				
//				Set<TbBundledItemUsedTrx> itemUsed = trx.getTbBundledItemUsedTrxes();
				
				List<TbBundledItemUsedTrx> itemUsed = getItemBundle(trx);
				
									
				for(TbBundledItemUsedTrx bundleItem : itemUsed){
					
										
					SQLQuery query = session.createSQLQuery(sql);
					query.addEntity("inv", TbItemInventory.class);
					query.setInteger("idItem", bundleItem.getMsItem().getNItemId().intValue());
					query.setInteger("batchId", bundleItem.getTbBatchItem().getNBatchId().intValue());
					query.setInteger("wId", warehouseId.intValue());
		
					itemInventory = (TbItemInventory)query.uniqueResult();
					float qty = bundleItem.getNQty() + itemInventory.getNItemInvQty();
					
					itemInventory.setNItemInvQty(qty);
					
					
		
					session.update(itemInventory);
				}
			}
			
			Set<TbDrugIngredients> racikans = note.getTbDrugIngredients();
			for(TbDrugIngredients racikan : racikans){
				
				Set<TbDrugIngredientsDetail> detils = racikan.getTbDrugIngredientsDetails();
				
								
				for(TbDrugIngredientsDetail detil : detils){
					
										
					SQLQuery query = session.createSQLQuery(sql);
					query.addEntity("inv", TbItemInventory.class);
					query.setInteger("idItem", detil.getMsItem().getNItemId().intValue());
					query.setInteger("batchId", detil.getTbBatchItem().getNBatchId().intValue());
					query.setInteger("wId", warehouseId.intValue());
					
					itemInventory = (TbItemInventory)query.uniqueResult();
					float qty = detil.getNDingrDetQty() + itemInventory.getNItemInvQty();
					itemInventory.setNItemInvQty(qty);
		
					session.update(itemInventory);
				}
			}
			
			
			trans.commit();
			
			success = true;

		} catch (HibernateException e) {
			
			logger.error(e.getMessage());
			
			trans.rollback();
			
			throw new VONEAppException(e.getMessage());

		}
		
		
		return success;
	}
	
	private void createCancellationJournal(String vNoteNo) throws VONEAppException {
		List<TbJournalTrx> journalList = null;
		Session session = getCurrentSession();
		
		Criteria crit = session.createCriteria(TbJournalTrx.class);
		crit.add(Restrictions.eq("VVoucherNo", vNoteNo));
		double amt;
		int balance;
		
		journalList = crit.list();
		
		JournalBeanHandler jhandler = new JournalBeanHandler("B"+MedisafeConstants.ACCT_AR_STR);
		for(TbJournalTrx jbalik : journalList){
			if(jbalik.getNCredit() == 0){
				amt = jbalik.getNDebit();
				balance = MedisafeConstants.ACCT_CREDIT;
			}else{
				amt = jbalik.getNCredit();
				balance = MedisafeConstants.ACCT_DEBIT;
			}
			
			jhandler.addJournal(jbalik.getVVoucherNo()+"C", "Cancellation of "+jbalik.getVVoucherNo(), amt, balance, new Date(), "ARIF", jbalik.getMsCoa());
		}
		
		Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
		while (it.hasNext()){
			getHibernateTemplate().save(it.next());
			
		}
		
		
	}

	private List<TbBundledItemUsedTrx> getItemBundle(TbBundledTrx bundlesTrx) throws VONEAppException{
		
		List<TbBundledItemUsedTrx> list = null;
		
		try {
			Criteria crit = getCurrentSession().createCriteria(TbBundledItemUsedTrx.class);
			crit.add(Restrictions.eq("tbBundledTrx", bundlesTrx));
			
			list = crit.list();
			
		} catch (HibernateException e) {
			throw new VONEAppException(e.getMessage());
		}
		
		return list;
		
	}


	public boolean saveModifyNote(TbExamination note, Set<TbItemTrx> itemTrxs, Set<TbTreatmentTrx> treatTrxs,
								   Set<TbBundledTrx> bundleTrxs, Set<TbMiscTrx> miscTrxs, Set<TbDrugIngredients> 
									racikans, Integer warehouseId) throws VONEAppException
	{
		boolean success = false;
		Session session = null;
		TbItemInventory itemInventory = null;
		
		session = getCurrentSession();
		
		Transaction trans = session.beginTransaction();
		
		try{
			
			
			
			session.update(note);
			
			List<TbItemTrx> itmTrxs = getItemTrxNote(note);
			
			String sql = "select {inv.*} from tb_item_inventory inv where inv.n_item_id=:idItem"+
				 		 " AND inv.n_batch_id=:batchId AND inv.n_whouse_id=:wId";
				
			for(TbItemTrx itmTrx : itmTrxs){
				
				
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity("inv", TbItemInventory.class);
				query.setInteger("idItem", itmTrx.getMsItem().getNItemId().intValue());
				query.setInteger("batchId", itmTrx.getTbBatchItem().getNBatchId().intValue());
				query.setInteger("wId", warehouseId.intValue());
				
				itemInventory = (TbItemInventory)query.uniqueResult();
				float qty = itmTrx.getNQty() + itemInventory.getNItemInvQty();
				itemInventory.setNItemInvQty(qty);
	
				session.update(itemInventory);
				session.delete(itmTrx);
			}
			
			float qtyUsed = 0;
			double disc_1 = 0;
			double trans_val_1 = 0;
			double trans_aft_disc_1 = 0;
			
			TbItemTrx realTrx = null;
			
			String sql2 = "select {inv.*} from tb_item_inventory inv where inv.n_whouse_id=:wId "+
	 			"AND inv.n_item_id=:itemId AND inv.n_item_inv_qty > :qty";
			
			if(itemTrxs != null){
				for(TbItemTrx trx : itemTrxs){
					trx.setTbExamination(note);
					trx.setDWhnCreate(note.getDWhnCreate());
					MsItem item = trx.getMsItem();
					qtyUsed = trx.getNQty();
					
					disc_1 = trx.getNDiscAmount()/trx.getNQty();
					trans_val_1 = trx.getNAmountTrx()/trx.getNQty();
					trans_aft_disc_1 = trx.getNAmountAfterDisc()/trx.getNQty();
					
					
		
					SQLQuery query = session.createSQLQuery(sql2);
					query.addEntity("inv", TbItemInventory.class);
					query.setInteger("wId", warehouseId.intValue());
					query.setInteger("itemId", item.getNItemId().intValue());
					query.setShort("qty", (short)0);
					
					List<TbItemInventory> itemInvList = query.list();
					for(TbItemInventory inv : itemInvList){
						if(qtyUsed == 0)break;
						
						if(inv.getNItemInvQty() <= qtyUsed){
							qtyUsed = qtyUsed - inv.getNItemInvQty();
							
							realTrx = trx.clone();
							realTrx.setAturanPakai(trx.getAturanPakai());
							realTrx.setTbBatchItem(inv.getTbBatchItem());
							realTrx.setNQty(inv.getNItemInvQty());
							realTrx.setNAmountAfterDisc(inv.getNItemInvQty() * trans_aft_disc_1);
							realTrx.setNAmountTrx(inv.getNItemInvQty() * trans_val_1);
							realTrx.setNDiscAmount(inv.getNItemInvQty() * disc_1);
							
							session.save(realTrx);
							
							inv.setNItemInvQty((short)0);
							session.update(inv);
							
						}else{
							float sub = inv.getNItemInvQty() - qtyUsed;
							
							realTrx = trx.clone();
							realTrx.setAturanPakai(trx.getAturanPakai());
							realTrx.setTbBatchItem(inv.getTbBatchItem());
							realTrx.setNQty(qtyUsed);
							realTrx.setNAmountAfterDisc(qtyUsed * trans_aft_disc_1);
							realTrx.setNAmountTrx(qtyUsed * trans_val_1);
							realTrx.setNDiscAmount(qtyUsed * disc_1);
							
							session.save(realTrx);
							
							inv.setNItemInvQty(sub);
							session.update(inv);
							qtyUsed = 0;
						}
					}
				}
			}
			

			
			if(treatTrxs != null){
				for(TbTreatmentTrx trx : treatTrxs){
					session.update(trx);
				}
			}
			
			if(bundleTrxs != null){
				for(TbBundledTrx trx : bundleTrxs){
					session.update(trx);
				}
			}
			
			
			if(miscTrxs != null){
				for(TbMiscTrx trx : miscTrxs){
					session.update(trx);
				}
			}
			
			if(racikans != null){
				for(TbDrugIngredients racikan : racikans){
					session.update(racikan);
				}
			}
			
			
			trans.commit();
			
			success = true;
			
		} catch (HibernateException e){
			
			 logger.error(e.getMessage());
			 
			 trans.rollback();
			 
			 throw new VONEAppException(e.getMessage());
		}
		
		return success;
	}
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	
	public List<TbExamination> getNotesByRegistration(TbRegistration reg) throws VONEAppException
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {nota.*} ");
		
		sql.append(" from ");
		sql.append(" tb_examination nota ");
		
		sql.append(" where ");
		sql.append(" nota.n_reg_id=:regId ");
		sql.append(" and (nota.n_exam_status =:status1 ");
		sql.append(" or nota.n_exam_status =:status2) ");
		

		
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addEntity("nota", TbExamination.class);
			query.setInteger("regId", reg.getNRegId().intValue());
			query.setInteger("status1", MedisafeConstants.VALIDATED_NOTE);
			query.setInteger("status2", MedisafeConstants.ACTIVE_NOTE);
			
			List<TbExamination> notas = query.list();
					
		return notas;
	}
	
	
	public List getItemTrxBaseOnNote(TbExamination nota) throws VONEAppException{
		
		StringBuffer sb = new StringBuffer();		
		
		sb.append(" select ");
		sb.append(" trx.n_item_id as id, ");
		sb.append(" item.v_item_code as code, ");
		sb.append(" item.v_item_name as name, ");
		sb.append(" sum(trx.n_qty) as qty, ");
		sb.append(" sum(trx.n_amount_trx) as value, ");
		sb.append(" sum(trx.n_disc_amount) as discount, ");
		sb.append(" sum(trx.n_amount_after_disc) as total, ");
		sb.append(" sum(trx.n_amount_trx) as total2, ");
		sb.append(" item.n_r as jasaR ");
							
		
		sb.append(" from " );
		sb.append(" tb_item_trx trx, ");
		sb.append(" ms_item item ");
		
		           
		sb.append(" where " );
		sb.append(" trx.n_item_id=item.n_item_id ");
		sb.append(" and trx.n_note_id=:noteId ");
		
		
		sb.append( " group by ");
		sb.append(" id, code, name, jasaR ");
			      
		
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("code", Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("qty", Hibernate.INTEGER);
			query.addScalar("value", Hibernate.DOUBLE);
			query.addScalar("discount", Hibernate.DOUBLE);
			query.addScalar("total", Hibernate.DOUBLE);
			query.addScalar("total2", Hibernate.DOUBLE);
			query.addScalar("jasaR", Hibernate.SHORT);
			query.setInteger("noteId", nota.getNExamId().intValue());
			
		List result = query.list();
		
		return result;
	}
	
	
	public List getItemTrxNote(TbExamination nota) throws VONEAppException{
		
		Criteria crit = getCurrentSession().createCriteria(TbItemTrx.class);
		crit.add(Restrictions.eq("tbExamination", nota));
		
			
		return crit.list();
	}



	


	public MsLabTestDetail getLabTestDetail(int code) throws VONEAppException {
			MsLabTestDetail ltd = null;
			List list = null;
			Session session = null;
			String stQuery = "select {ltd.*} " 
					+ "from ms_lab_test_detail ltd "
					+ "where ltd.n_treatment_id = :code ";

			try {
				session = getCurrentSession();
				list = session.createSQLQuery(stQuery)
						.addEntity("ltd",MsLabTestDetail.class)
						.setInteger("code", code)
						.list();
				if (list.iterator().hasNext()) {
					ltd = (MsLabTestDetail) list.iterator().next();
				}
			} catch (Exception e){
				logger.error(e.getMessage());
				throw new VONEAppException(e.getMessage());
			}
			return ltd;
	}


	
	public MsUnit getUnitById(Integer unitId)  throws VONEAppException{
		
		Criteria crit = getCurrentSession().createCriteria(MsUnit.class);
		crit.add(Restrictions.eq("NUnitId", unitId));
		
		MsUnit unit = (MsUnit)crit.uniqueResult();
		
		return unit;
	}
	
	public MsCoa getCoaByCode(String code) throws VONEAppException {
		Session session = null;
		MsCoa coa = null;
		
		try{
			stQuery.setLength(0);
			stQuery.append("from MsCoa ")
			.append(" where v_acct_no = :code");
			
			session = getCurrentSession();
			
			coa = (MsCoa)session.createQuery(stQuery.toString())					
					.setString("code", code)
					.uniqueResult();
			
        } catch (Exception re) {
            log.error("get failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"));
		}
		return coa;		
	}
	

	public List getRacikan(TbExamination nota) throws VONEAppException {
		
		List list = null;
		Criteria crit = getCurrentSession().createCriteria(TbDrugIngredients.class);
		crit.add(Restrictions.eq("tbExamination", nota));
		
		list = crit.list();
		
		return list;
	}


	
	public List getTreatmentTrx(TbExamination nota) throws VONEAppException{
		List list = null;
		
		Criteria crit = getCurrentSession().createCriteria(TbTreatmentTrx.class);
		crit.add(Restrictions.eq("tbExamination", nota));
		
		list = crit.list();
		
		return list;
	}


	public List getBundleTrx(TbExamination nota) throws VONEAppException{
		List list = null;
		
		Criteria crit = getCurrentSession().createCriteria(TbBundledTrx.class);
		crit.add(Restrictions.eq("tbExamination", nota));
		
		list = crit.list();
		
		
		return list;
	}


	public List getBedTrx(TbExamination nota) throws VONEAppException{
		
		List list = null;
		
		Criteria crit = getCurrentSession().createCriteria(TbBedTrx.class);
		crit.add(Restrictions.eq("tbExamination", nota));
		
		list = crit.list();
		
		
		return list;
	}


	public List getMiscTrx(TbExamination nota) throws VONEAppException{
		List list = null;
		
		Criteria crit = getCurrentSession().createCriteria(TbMiscTrx.class);
		crit.add(Restrictions.eq("tbExamination", nota));
		
		list = crit.list();
		
		
		return list;
	}

	public List<TbExamination> searchNote(String nomorNota, String namaPasien, Date mulai, Date sampe, 
			String unitCode) throws VONEAppException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TbExamination> list = null;
		
		sampe.setDate(sampe.getDate()+1);
				
		Session session = getCurrentSession();
		
		log.debug("ARIF DEBUG : searchNote() -- ENTER");
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {note.*} ");
		
		sql.append(" from ");
		sql.append(" tb_examination note, ");
		sql.append(" ms_patient pat ");
		
		sql.append(" where ");
		sql.append(" note.n_patient_id=pat.n_patient_id ");
		sql.append(" and note.v_note_no like :noteNumber ");
		sql.append(" and note.v_note_no like :unitCode ");
		sql.append(" and pat.v_patient_name like :patientName ");
		sql.append(" and note.n_exam_status !=:noteStatus ");
		sql.append(" and note.d_whn_create between :tanggalMulai and :tglTerakhir ");
		sql.append(" limit 100 ");
		
		try {
			
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("note", TbExamination.class);
			query.setString("noteNumber", nomorNota);
			query.setString("unitCode", unitCode);
			query.setString("patientName", namaPasien);
			query.setShort("noteStatus", (short)MedisafeConstants.CANCEL_NOTE);
//			query.setString("tanggalMulai", sdf.format(mulai));
			query.setTimestamp("tanggalMulai", mulai);
//			query.setString("tglTerakhir", sdf.format(sampe));
			query.setTimestamp("tglTerakhir", sampe);
			
			
			list = query.list();
			
//			System.out.println("ARIF DEBUG : searchNote() -- BANYAKNYA DATA DARI DATABASE : "+list.size());
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		log.debug("ARIF DEBUG : searchNote() -- EXIT");
		
		
		return list;
	}

	
	
	public List<TbExamination> searchNote(String nomorNota, String namaPasien, String noMr, String nomorResep,
			Date tgl1, Date tgl2, String kodeUnit) throws VONEAppException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TbExamination> list = null;
		
//		tgl2.setDate(tgl2.getDate()+1);
		
		Session session = getCurrentSession();
		
		log.debug("ARIF DEBUG : searchNote() -- ENTER");
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {note.*} ");
		
		sql.append(" from ");
		sql.append(" tb_examination note, ");
		if(noMr.length() > 0)
		{
			sql.append(" tb_medical_record mr, ");
		}
		sql.append(" ms_patient pat ");
		
		sql.append(" where ");
		sql.append(" note.n_patient_id=pat.n_patient_id ");
		if(noMr.length() > 0){
			sql.append("and pat.n_patient_id=mr.n_patient_id ");
		}
		sql.append(" and note.v_note_no like :noteNumber ");
		sql.append(" and note.v_note_no like :unitCode ");
		if(noMr.length() > 0){
			sql.append(" and mr.v_mr_code=:mrCode ");
		}
		sql.append(" and pat.v_patient_name like :patientName ");
		if(nomorResep.length() > 2){
//			System.out.println("nomor resep : "+ nomorResep.length());
			sql.append(" and note.v_recipe_no like :nomorResep ");
		}
			
		sql.append(" and note.n_exam_status !=:noteStatus ");
		sql.append(" and note.d_whn_create between :tanggalMulai and :tglTerakhir ");
//		sql.append(" limit 100 ");
		
		try {
//			System.out.println(sql.toString());
//			System.out.println(nomorResep);
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("note", TbExamination.class);
			query.setString("noteNumber", nomorNota);
			query.setString("unitCode", kodeUnit);
			query.setString("patientName", namaPasien);
			if(nomorResep.length() > 2){
				System.out.println("nomor resep :"+nomorResep);
				query.setString("nomorResep", nomorResep);
			}
				
			if(noMr.length()> 0){
				query.setString("mrCode", noMr);
			}
			query.setShort("noteStatus", (short)MedisafeConstants.CANCEL_NOTE);
//			query.setString("tanggalMulai", sdf.format(tgl1));
//			query.setString("tglTerakhir", sdf.format(tgl2));
			
			
			query.setTimestamp("tanggalMulai", tgl1);
			query.setTimestamp("tglTerakhir", tgl2);
			
			System.out.println("tgl 1 "+sdf.format(tgl1));
			System.out.println("tgl 2" +sdf.format(tgl2));
			
			
			list = query.list();
			
			
			
//			System.out.println("ARIF DEBUG : searchNote() -- BANYAKNYA DATA DARI DATABASE : "+list.size());
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		log.debug("ARIF DEBUG : searchNote() -- EXIT");
		
		
		return list;

		
		
	}
	
	public void saveOrUpdateNote(TbExamination note){
		getHibernateTemplate().saveOrUpdate(note);
	}
	
	public List getPendapatanDokter(Integer staffId, String tipeLaporan, Date tgl1, Date tgl2, String patientType) throws VONEAppException{
		Session session = getCurrentSession();
		
		StringBuffer query = new StringBuffer();
		query.append("select nomor_nota as nota, ");
		query.append("nama_tindakan as tindakan, ");
		query.append("nama_pasien as pasien, ");
		query.append("tipe_pasien as tipe, ");
		query.append("kelas_tarif as kelas, ");
		query.append("tgl_tindakan as tgl, ");
		query.append("jasa_dokter as jasa, ");
		query.append("kode as kode, ");
		query.append("validate_by as validasi ");
		query.append("from report.get_doctor_report(:dari , :sampai) ");
		query.append("where ");
		query.append("n_staff_id=:staff ");
		
		if(!patientType.equalsIgnoreCase("ALL"))
			query.append("and tipe_pasien=:tipe ");
		
		SQLQuery qry = session.createSQLQuery(query.toString());
		qry.setDate("dari", tgl1);
		qry.setDate("sampai", tgl2);
		qry.setInteger("staff", staffId);
		if(!patientType.equalsIgnoreCase("ALL"))
			qry.setString("tipe", patientType);

		qry.addScalar("nota", Hibernate.STRING);
		qry.addScalar("tindakan", Hibernate.STRING);
		qry.addScalar("pasien", Hibernate.STRING);
		qry.addScalar("tipe", Hibernate.STRING);
		qry.addScalar("kelas", Hibernate.STRING);
		qry.addScalar("tgl", Hibernate.DATE);
		qry.addScalar("jasa", Hibernate.DOUBLE);
		qry.addScalar("kode", Hibernate.STRING);
		qry.addScalar("validasi", Hibernate.STRING);
		
		
		return qry.list();
	}

	public List<TbTreatmentTrx> getPendapatanDokter(Integer staffId, String tipeLaporan, String tgl1, String tgl2, String patientType) throws VONEAppException {
		Session session = getCurrentSession();
		
		Date dari = null;
		Date sampai = null;
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
			System.out.println("DARI : "+dari.toString()+" SAMPAI : "+sampai.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {t.*}");
		
		sb.append(" from ");
		sb.append(" tb_treatment_trx t,");
		sb.append(" tb_examination nota,");
		sb.append(" ms_patient patient,");
		sb.append(" tb_registration r ");
		
		sb.append(" where ");
		sb.append(" t.n_note_id=nota.n_exam_id");
		sb.append(" and nota.n_patient_id=patient.n_patient_id");
		sb.append(" and nota.n_reg_id=r.n_reg_id");
		sb.append(" and r.n_staff_id=:stafId");
		sb.append(" and t.n_doctor_id is null");
		sb.append(" and t.d_whn_create between :vdate1 and :vdate2");
		
		
		if(patientType.equalsIgnoreCase("BPJS")) sb.append(" and patient.n_patient_type_id=8 ");
		else if(patientType.equalsIgnoreCase("NONBPJS")) sb.append(" and (patient.n_patient_type_id <> 8 or patient.n_patient_type_id is null) ");
		
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		
		query.addEntity("t", TbTreatmentTrx.class);
		query.setInteger("stafId", staffId.intValue());
		query.setTimestamp("vdate1", dari);
		query.setTimestamp("vdate2", sampai);
		
		
		return query.list();
	}
	
	
	public List<TbTreatmentTrx> getPendapatanDokter(Integer staffId, String tgl1, String tgl2, String patientType) throws VONEAppException {
		Session session = getCurrentSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dari = null;
		Date sampai = null;
		try {
			 dari = sdf.parse(tgl1);
			 sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {t.*}");
		
		sb.append(" from ");
		sb.append(" tb_treatment_trx t,");
		sb.append(" tb_examination nota,");
		sb.append(" ms_patient patient");
				
		sb.append(" where ");
		sb.append(" t.n_note_id=nota.n_exam_id");
		sb.append(" and nota.n_patient_id=patient.n_patient_id");
		sb.append(" and t.n_doctor_id=:stafId");
		sb.append(" and t.d_whn_create between :vdate1 and :vdate2");
		
		if(patientType.equalsIgnoreCase("BPJS")) sb.append(" and patient.n_patient_type_id=8 ");
		else if(patientType.equalsIgnoreCase("NONBPJS")) sb.append(" and (patient.n_patient_type_id <> 8 or patient.n_patient_type_id is null) ");
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		
		query.addEntity("t", TbTreatmentTrx.class);
		query.setInteger("stafId", staffId.intValue());
		query.setTimestamp("vdate1", dari);
		query.setTimestamp("vdate2", sampai);
		
		
		
		return query.list();
	}
	
	
	public List<TbExamination> getNoteBaseOnRangeDate(String tgl1, String tgl2) throws VONEAppException{
		List<TbExamination> nota = null;
		
		Session session = getCurrentSession();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dari = null;
		Date sampai = null;
		
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {nota.*}");
		sb.append(" from tb_examination nota ");
		sb.append(" where nota.d_whn_create ");
		sb.append(" between :from and :to");
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("nota", TbExamination.class);
//		query.setString("from", tgl1);
//		query.setString("to", tgl2);
		query.setTimestamp("from", dari);
		query.setTimestamp("to", sampai);
		
		nota = query.list();
		
		return nota;
	}
	
	public List<TbExamination> getNoteByDokter(Integer dokterId, String tgl1, String tgl2, String patientType) throws VONEAppException{
		List<TbExamination> nota = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dari = null;
		Date sampai = null;
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer(100);
		sb.append("select {e.*}");
		sb.append(" from tb_examination e,");
		sb.append(" ms_patient patient,");
		sb.append(" tb_registration r ");
		sb.append(" where ");
		sb.append(" e.n_patient_id=patient.n_patient_id");
		sb.append(" and e.n_reg_id=r.n_reg_id ");
		sb.append(" and r.n_staff_id=:stafId ");
		sb.append(" and e.d_whn_create between :from and :to");
		
		if(patientType.equalsIgnoreCase("BPJS")) sb.append(" and patient.n_patient_type_id=8 ");
		else if(patientType.equalsIgnoreCase("COVID")) sb.append(" and patient.n_patient_type_id=9 ");
		else if(patientType.equalsIgnoreCase("NONBPJS")) sb.append(" and (patient.n_patient_type_id not in(8,9) or patient.n_patient_type_id is null) ");
		
		
		Session session = getCurrentSession();
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("e", TbExamination.class);
		query.setInteger("stafId", dokterId.intValue());
		query.setTimestamp("from", dari);
		query.setTimestamp("to", sampai);
		
		nota = query.list();

		return nota;
	}
	
	
	public List<TbDiagnose> getDiagnose(Integer registrationId) throws VONEAppException{
		Criteria crit = getCurrentSession().createCriteria(TbDiagnose.class);
		crit.add(Restrictions.eq("NRegId", registrationId));
		
		return crit.list();
	}
	
	
	/*
	 *  Added by Aziiz Surahman
	 *  30-03-2010
	 */
	public List<TbRegistration> getBedTransaction(String tgl1, String tgl2) throws VONEAppException {
		Session session = getCurrentSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dari = null;
		Date sampai = null;
		
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {r.*}");
		
		sb.append(" from ");
		sb.append(" tb_registration r");
				
		sb.append(" where ");
		sb.append(" r.d_whn_create between :vdate1 and :vdate2");
		sb.append(" and substr(trim(r.v_reg_secondary_id),1,1)=:vtype");
		//sb.append(" and r.n_reg_id=e.n_reg_id and e.n_exam_id=t.n_note_id");
		SQLQuery query = session.createSQLQuery(sb.toString());
		
		query.addEntity("r", TbRegistration.class);
//		query.setString("vdate1", tgl1);
//		query.setString("vdate2", tgl2);
		query.setTimestamp("vdate1", dari);
		query.setTimestamp("vdate2", sampai);
		query.setString("vtype", "I");
		
		return query.list();
	}
	
	public List<TbPatientSettlement> getBpjsSettlement(Integer registrationId) throws VONEAppException {
		Session session = getCurrentSession();
		StringBuffer sb = new StringBuffer();
		sb.append("select {s.*}");
		
		sb.append(" from ");
		sb.append(" tb_patient_settlement s,");
		sb.append(" tb_patient_bill bill ");
		sb.append(" where bill.n_pbill_id=s.n_pbill_id");
		sb.append(" and bill.n_reg_id=:regId and s.n_insurance_id=75");
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("s", TbPatientSettlement.class);
		query.setInteger("regId", registrationId);
		
		return query.list();
	}
	
	public List<TbRegistration> getStatusPasien(Integer mrId, int rajalRanap) throws VONEAppException {
		Session session = getCurrentSession();
		StringBuffer sb = new StringBuffer();
		sb.append("select {reg.*}");
		
		sb.append(" from ");
		sb.append(" tb_registration reg");
		sb.append(" where reg.n_mr_id=:mrId and ");
		//rajal 0 ranap 1
		if(rajalRanap == 1) sb.append(" reg.n_transfer_reg_id is not null ");
		else sb.append(" reg.n_transfer_reg_id is null" );
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("reg", TbRegistration.class);
		query.setInteger("mrId", mrId);
		
		return query.list();
	}
	
	public List<Object> getLaporanDiagnosaRajal(String tgl1, String tgl2) throws VONEAppException{
		Session session = getCurrentSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dari = null;
		Date sampai = null;
		
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql = "select r.n_reg_id as id, r.d_registration_date as tgl, u.v_unit_name as unit, s.v_staff_name as dokter, v_mr_code as mrNo, p.v_patient_name as nama, "
				+	 "(select i.v_icd_name from  tb_icd_diagnose id, ms_icd i, tb_diagnose d where i.n_icd_id=id.n_icd_id and d.n_diagnose_id=id.n_diagnose_id and "
				+    "d.n_reg_id=r.n_reg_id limit 1) as diagnosa, p.d_patient_dob as tglLahir, p.v_patient_religion as agama, p.v_patient_gender as jk, "
				+    "p.v_patient_etnis as etnis, p.v_patient_language as bahasa, mr.n_mr_id as mrId "
				+    "from tb_registration r, ms_unit u, ms_staff s,tb_medical_record mr, ms_patient p "
				+    "where r.d_registration_date between :from and :to and r.n_transfer_reg_id is null and u.n_unit_id=r.n_unit_id and s.n_staff_id=r.n_staff_id "
				+    "and mr.n_mr_id=r.n_mr_id and p.n_patient_id=mr.n_patient_id order by tgl ";
		
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		
		query.addScalar("id", Hibernate.INTEGER);
		query.addScalar("tgl", Hibernate.TIMESTAMP);
		query.addScalar("unit", Hibernate.STRING);
		query.addScalar("dokter", Hibernate.STRING);
		query.addScalar("mrNo", Hibernate.STRING);
		query.addScalar("nama", Hibernate.STRING);
		query.addScalar("diagnosa", Hibernate.STRING);
		query.addScalar("tglLahir", Hibernate.DATE);
		query.addScalar("agama", Hibernate.STRING);
		query.addScalar("jk", Hibernate.STRING);
		query.addScalar("etnis", Hibernate.STRING);
		query.addScalar("bahasa", Hibernate.STRING);
		query.addScalar("mrId", Hibernate.INTEGER);
		
		query.setTimestamp("from", dari);
		query.setTimestamp("to", sampai);
		
		return query.list();
	}
	
	/*
	 *  Added by Aziiz Surahman
	 *  30-03-2010
	 */
	public List<TbRegistration> getRegistration(String type, String tgl1, String tgl2) throws VONEAppException {
		Session session = getCurrentSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dari = null;
		Date sampai = null;
		
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {t.*}");
		sb.append(" from ");
		sb.append(" tb_registration t");		
		sb.append(" where");
		sb.append(" t.d_whn_create between :vdate1 and :vdate2");
		sb.append(" and substr(trim(t.v_reg_secondary_id),1,1) like :vtype");
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("t", TbRegistration.class);
//		query.setString("vdate1", tgl1);
//		query.setString("vdate2", tgl2);
		query.setTimestamp("vdate1", dari);
		query.setTimestamp("vdate2", sampai);
		query.setString("vtype", type);
		
		return query.list();
	}
	
	public List getRekapObatNew(String tgl1, String tgl2) throws VONEAppException{
		StringBuffer sb = new StringBuffer();		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date dari = null;
		Date sampai = null;
		
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("select  sum(qty) as jumlah, sum(n_amount_trx) as trx , id, v_item_name as name from ( ");
		sb.append("select item.v_item_name, item.n_item_id as id, trx.n_qty as qty, trx.n_amount_trx from tb_examination nota "); 
		sb.append("join tb_item_trx trx on nota.n_exam_id=trx.n_note_id ");
		sb.append("join ms_item item on item.n_item_id=trx.n_item_id ");
		sb.append("where nota.d_whn_create between :trxFrom and :trxTo and nota.n_exam_status=2 ");
		sb.append("union all "); 
		sb.append("select item.v_item_name, item.n_item_id as id, rd.n_dingr_det_qty as qty, ");
		sb.append("(select n_selling_price from ms_item_selling_price where n_item_id=rd.n_item_id limit 1) * rd.n_dingr_det_qty as n_amount_trx "); 
		sb.append("from tb_examination nota ");
		sb.append("join tb_drug_ingredients r on nota.n_exam_id=r.n_note_id ");
		sb.append("join tb_drug_ingredients_detail rd on r.n_dingr_id=rd.n_dingr_id ");
		sb.append("join ms_item item on item.n_item_id=rd.n_item_id ");
		sb.append("where nota.d_whn_create between :racFrom and :racTo and nota.n_exam_status=2) q ");
		sb.append("group by q.id, q.v_item_name ");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addScalar("jumlah", Hibernate.DOUBLE);
		query.addScalar("trx", Hibernate.DOUBLE);
		query.addScalar("id", Hibernate.INTEGER);
		query.addScalar("name", Hibernate.STRING);
		query.setTimestamp("trxFrom", dari);
		query.setTimestamp("trxTo", sampai);
		query.setTimestamp("racFrom", dari);
		query.setTimestamp("racTo", sampai);
		
		List result = query.list();
		return result;
	}
	/*
	 *  Added by Aziiz Surahman
	 *  30-03-2010
	 */
	public List getRekapObat(String tgl1, String tgl2) throws VONEAppException {
		
		StringBuffer sb = new StringBuffer();		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date dari = null;
		Date sampai = null;
		
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sb.append(" select ");
		sb.append(" sum(trx.n_qty) as tqty, ");
		sb.append(" sum(trx.n_amount_after_disc) as tamount, ");
		sb.append(" trx.n_item_id as id, ");
		sb.append(" item.v_item_name as name ");
									
		sb.append(" from " );
		sb.append(" tb_item_trx trx, ");
		sb.append(" tb_examination nota,");
		sb.append(" ms_item item ");
		
		           
		sb.append(" where " );
		sb.append(" trx.n_note_id=nota.n_exam_id");
		sb.append(" and nota.n_exam_status=2");
		sb.append(" and trx.n_item_id=item.n_item_id ");
		sb.append(" and trx.d_whn_create between :vdate1 and :vdate2 ");
		
		sb.append(" group by ");
		sb.append(" id, name ");
			      
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addScalar("tqty", Hibernate.INTEGER);
			query.addScalar("tamount", Hibernate.DOUBLE);
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("name", Hibernate.STRING);
//			query.setString("vdate1", tgl1);
//			query.setString("vdate2", tgl2);
			query.setTimestamp("vdate1", dari);
			query.setTimestamp("vdate2", sampai);
		
			
		List result = query.list();
		
		
		return result;
	}
	/*
	 *  Added by Aziiz Surahman
	 *  30-03-2010
	 */
	public List<TbDrugIngredientsDetail> getDrugIngredientsDetail(Integer id, String tgl1, String tgl2) throws VONEAppException {
		Session session = getCurrentSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date dari = null;
		Date sampai = null;
		
		try {
			dari = sdf.parse(tgl1);
			sampai = sdf.parse(tgl2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {t.*}");
		sb.append(" from ");
		sb.append(" tb_drug_ingredients_detail t,");
		sb.append(" tb_drug_ingredients h, ");
		sb.append(" tb_examination nota ");
		
		sb.append(" where");
		sb.append(" t.n_dingr_id=h.n_dingr_id and h.n_note_id=nota.n_exam_id");
		sb.append(" and nota.n_exam_status=2 and   ");
		sb.append(" t.d_whn_create between :vdate1 and :vdate2");
		sb.append(" and n_item_id=:vid");
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("t", TbDrugIngredientsDetail.class);
//		query.setString("vdate1", tgl1);
//		query.setString("vdate2", tgl2);
		query.setTimestamp("vdate1", dari);
		query.setTimestamp("vdate2", sampai);
		query.setInteger("vid", id);
		
		return query.list();
	}

	public TbExamination getReceiptFromDoctor(Integer regId, Integer unitId) throws VONEAppException{
		TbExamination nota = null;
		Session session = getCurrentSession();

		StringBuffer sb = new StringBuffer();
		sb.append(" select {nota.*} ");
		sb.append(" from ");
		sb.append(" tb_examination nota");
		
		sb.append(" where ");
		sb.append(" nota.n_reg_id=:regId ");
		sb.append(" and nota.n_exam_status=:status ");
		sb.append(" and n_unit_id=:unit ");
		sb.append(" limit 1 " );
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("nota", TbExamination.class);
		query.setInteger("regId", regId);
		query.setInteger("status", MedisafeConstants.ACTIVE_NOTE);
		query.setInteger("unit",unitId);
		
		nota = (TbExamination)query.uniqueResult();
		
		return nota;
	}

}

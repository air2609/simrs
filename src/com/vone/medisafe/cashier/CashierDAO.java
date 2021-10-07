package com.vone.medisafe.cashier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.zkoss.zul.Decimalbox;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsCreditCardType;
import com.vone.medisafe.mapping.MsDoctor;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsShift;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.mapping.TbPatientDeposit;
import com.vone.medisafe.mapping.TbPatientSettlement;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.service.Service;

public class CashierDAO extends HibernateDaoSupport{
	
	
	private static final String RETUR_DEPOSIT = "RETUR DEPOSIT: ";
	private static final String DEPOSIT_PASIEN = "DEPOSIT PASIEN: ";


	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	

	public List<TbExamination> getNotes(String patientName, String noteNumber, Short type) throws VONEAppException
	{
		List<TbExamination> result = null;
		Session session = null;
		
		StringBuffer sql = new StringBuffer();
		
		
		
		
		try {
			session = getCurrentSession();
			
			if(type.shortValue() == MedisafeConstants.BELUM_LUNAS_SUDAH_DIVALIDASI){
				
				sql.delete(0, sql.length());
				
				sql.append(" select ");
				sql.append(" {nota.*} ");
				
				sql.append(" from ");
				sql.append(" tb_examination nota, ");
				sql.append(" ms_patient pat ");
				
				sql.append(" where ");
				sql.append(" nota.n_patient_id=pat.n_patient_id ");
				sql.append(" and nota.n_exam_status=:status ");
				sql.append(" and nota.n_payment_status=:payStatus ");
				sql.append(" and pat.v_patient_name like :name ");
				sql.append(" and nota.v_note_no like :noteNumber ");
			
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.addEntity("nota", TbExamination.class);
				query.setShort("status", (short)MedisafeConstants.VALIDATED_NOTE);
				query.setShort("payStatus", MedisafeConstants.BELUM_LUNAS);
				query.setString("name", patientName);
				query.setString("noteNumber", noteNumber);
				
				result = query.list();
			}else{
				
				sql.delete(0, sql.length());
				
				sql.append(" select ");
				sql.append(" {nota.*} ");
				
				sql.append(" from ");
				sql.append(" tb_examination nota, ");
				sql.append(" ms_patient pat ");
				
				sql.append(" where ");
				sql.append(" nota.n_patient_id=pat.n_patient_id ");
				sql.append(" and nota.n_payment_status=:payStatus ");
				sql.append(" and pat.v_patient_name LIKE :name ");
				sql.append(" and nota.v_note_no like :noteNumber ");;
			
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.addEntity("nota", TbExamination.class);
				query.setShort("payStatus", MedisafeConstants.BELUM_LUNAS);
				query.setString("name", patientName);
				query.setString("noteNumber", noteNumber);
				
				result = query.list();
			}
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
			
		}
		
		
		return result;
	}
	
	public List<TbReturPharmacyTrx> getReturNotes(String patientName, String returNote) throws VONEAppException{
		List<TbReturPharmacyTrx> hasil = null;
		Session session = null;
		
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {retur.*} ");
		
		
		sql.append(" from ");
		sql.append(" tb_retur_pharmacy_trx retur, ");
		sql.append(" ms_patient patient ");
		
		sql.append(" where ");
		sql.append(" retur.n_patient_id=patient.n_patient_id ");
		sql.append(" and retur.v_retur_code like :codeRetur ");
		sql.append(" and patient.v_patient_name like :patientName ");
		sql.append(" and retur.n_status =:status ");
		sql.append(" and retur.n_payment_status=:paymentStatus ");;
		
		try {
			session = getCurrentSession();
			
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("retur", TbReturPharmacyTrx.class);
			query.setString("codeRetur", returNote);
			query.setString("patientName", patientName);
			query.setShort("status", (short)MedisafeConstants.VALIDATED_NOTE);
			query.setShort("paymentStatus", MedisafeConstants.BELUM_LUNAS);
			hasil = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		
		}
		
		return hasil;
	}
	
	
	
	public List getItemTrx(TbExamination nota) throws VONEAppException{
		Session session = null;
		List result = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbItemTrx.class);
			crit.add(Restrictions.eq("tbExamination", nota));
			result = crit.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			throw new VONEAppException(e.getMessage());
		}
		
		
		return result;
	}
	
	
	public List getTreatmentTrx(TbExamination nota) throws VONEAppException{
		Session session = null;
		List result = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbTreatmentTrx.class);
			crit.add(Restrictions.eq("tbExamination", nota));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(),e);
		}
		return result;
	}
	
	public List getBundleTrx(TbExamination nota) throws VONEAppException{
		Session session = null;
		List result = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbBundledTrx.class);
			crit.add(Restrictions.eq("tbExamination", nota));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(), e);
		}
		return result;
	}
	
	public List getBedTrx(TbExamination nota) throws VONEAppException{
		Session session = null;
		List result = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbBedTrx.class);
			crit.add(Restrictions.eq("tbExamination", nota));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	public List getRacikanTrx(TbExamination nota) throws VONEAppException{
		Session session = null;
		List result = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbDrugIngredients.class);
			crit.add(Restrictions.eq("tbExamination", nota));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	public List<TbReturPharmacyDetailTrx> getReturDetail(TbReturPharmacyTrx retur) throws VONEAppException{
		Session session = null;
		List<TbReturPharmacyDetailTrx> result = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbReturPharmacyDetailTrx.class);
			crit.add(Restrictions.eq("tbReturPharmacyTrx", retur));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	public List getNotes(MsPatient patient) throws VONEAppException{
		Session session = null;
		List result = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbExamination.class);
			crit.add(Restrictions.eq("msPatient", patient));
			crit.add(Restrictions.ne("NPaymentStatus", MedisafeConstants.BELUM_LUNAS));
			crit.add(Restrictions.ne("NExamStatus", MedisafeConstants.CANCEL_NOTE));
			crit.add(Restrictions.ne("NExamStatus", MedisafeConstants.CLOSE_NOTE));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	public List<TbExamination> getNotes(TbRegistration reg) throws VONEAppException{
	
		List<TbExamination> result = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("select {nota.*} from tb_examination nota");
		sb.append(" where n_reg_id=:regId");
		sb.append(" and n_payment_status=:payStat");
		sb.append(" and n_exam_status=:examStat");
		
		try {
			
			/**Criteria crit = getCurrentSession().createCriteria(TbExamination.class);
			crit.add(Restrictions.eq("tbRegistration", reg));
			crit.add(Restrictions.eq("NPaymentStatus", MedisafeConstants.BELUM_LUNAS));
			crit.add(Restrictions.eq("NExamStatus", MedisafeConstants.VALIDATED_NOTE)); */
//			crit.add(Restrictions.ne("NExamStatus", MedisafeConstants.CLOSE_NOTE));
			
			//result = crit.list();
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addEntity("nota", TbExamination.class);
			query.setInteger("regId", reg.getNRegId());
			query.setShort("payStat", MedisafeConstants.BELUM_LUNAS);
			query.setInteger("examStat", MedisafeConstants.VALIDATED_NOTE);
			
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());		
		}
		
		return result;
	}
	
	
	public List<TbReturPharmacyTrx> getReturNotes(TbRegistration patient) throws VONEAppException{
		
		List<TbReturPharmacyTrx> result = null;
		
		try {
			
			
			Criteria crit = getCurrentSession().createCriteria(TbReturPharmacyTrx.class);
			crit.add(Restrictions.eq("tbRegistration", patient));
			crit.add(Restrictions.eq("NStatus", (short)MedisafeConstants.VALIDATED_NOTE));
//			crit.add(Restrictions.ne("NStatus", (short)MedisafeConstants.CANCEL_NOTE));
//			crit.add(Restrictions.ne("NStatus", (short)MedisafeConstants.CLOSE_NOTE));
			
			result = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
			
		}
		
		return result;
	}
	
	public List getItemReturnable(int patientId, String kelas) throws VONEAppException{
	
		List result = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" inv.n_item_id as itemid, ");
		sql.append(" sell.n_selling_price as harga, ");
		sql.append(" sum(inv.n_qty)as masuk, ");
		sql.append(" sum (inv.n_qty_out) as keluar ");
		
		sql.append(" from ");
		sql.append(" tb_patient_inventory inv, ");
		sql.append(" ms_item item, ");
		sql.append(" ms_item_selling_price sell, ");
		sql.append(" ms_treatment_class tclass ");
		
		sql.append(" where inv.n_pat_id=:patId ");
		sql.append(" and inv.n_item_id=item.n_item_id ");
		sql.append(" and item.v_item_returnable=:retur ");
		sql.append(" and sell.n_item_id=item.n_item_id ");
		sql.append(" and sell.n_tclass_id=tclass.n_tclass_id ");
		sql.append(" and tclass.v_tclass_desc=:kelas ");
		
		sql.append(" group by ");
		sql.append(" inv.n_item_id, ");
		sql.append(" sell.n_selling_price ");;
		
		try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addScalar("itemid", Hibernate.INTEGER);
			query.addScalar("harga", Hibernate.DOUBLE);
			query.addScalar("masuk", Hibernate.INTEGER);
			query.addScalar("keluar", Hibernate.INTEGER);
			query.setInteger("patId", patientId);
			query.setString("retur", "Y");
			query.setString("kelas", kelas);
			
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	
	public boolean saveBillRetur(TbPatientBill pbill, TbReturPharmacyTrx retur) throws VONEAppException{
		boolean success = false;
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = txManager.getTransaction(def);
		
		try {
			
			
			getHibernateTemplate().save(pbill);
			retur.setTbPatitentBill(pbill);
			getHibernateTemplate().update(retur);
			
			txManager.commit(status);
			success = true;
			// coa ar -
			// coa inv +
			// retur.get
			
		}catch (Exception e){
			txManager.rollback(status);
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return success;
	}
	
	public boolean saveBillNotas(TbPatientBill pbill, List<TbExamination> notas, List<TbPatientSettlement> 
		settlements, Decimalbox depositPaid, MsCoa coaKas) throws VONEAppException{
		boolean success = false;
//		Session session = getCurrentSession();
		
		Date tanggal = new Date();
		
		JournalBeanHandler jhandler = new JournalBeanHandler(MedisafeConstants.ACCT_PMT_STR);
		StringBuffer memo = new StringBuffer();
		
		MsCoa coaAR = null;
//		MsCoa coaKas = null;
		MsCoa coaDeposit = null;
		MsCoa coaInsurance = null;
		MsCoa coaBank = null;
		
		double totCash = 0;
		double totAr =  0;
		double totDeposit =  0;
		double totInsurance =  0;
		double totBank =  0;
		
		MsCoa coaBiayaDokter;
		MsCoa coaBiayaAnestasi;
		MsCoa coaApStaff = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_STAFF_AP);


		String voucherNo = "";
//		double totTrans;
		String currentUser = pbill.getVWhoCreate();
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = txManager.getTransaction(def);

		
		try {
//			session = getCurrentSession();
//			trans = session.beginTransaction();
			
//			List<TbExamination> notes = new ArrayList<TbExamination>();
//			for(TbExamination nota : notas){
//				if(nota.getTbPatientBill() == null)notes.add(nota);
//			}
//			
			pbill.setDWhnCreate(tanggal);
			pbill.setDSettlementDate(tanggal);
			pbill.setIdShift(this.getShift());
			getHibernateTemplate().save(pbill);
			
			
			for(TbPatientSettlement settlment : settlements){
				settlment.setDWhnCreate(tanggal);
				settlment.setTbPatientBill(pbill);
				settlment.setNShifId(this.getShift());
				getHibernateTemplate().save(settlment);
				
				if(settlment.getNPsettlementType() == MedisafeConstants.CASH_SETTLEMENT){
					//bayar dengan cash
					totCash += settlment.getNAmountSettled();
				}
				if(settlment.getNPsettlementType() == MedisafeConstants.BANK_SETTLEMENT){
					//bayar dengan deposit/bank
					totBank += settlment.getNAmountSettled();
					MsCreditCardType kartu = settlment.getMsCreditCardType();
					getHibernateTemplate().saveOrUpdate(kartu);
					coaBank = kartu.getMsBank().getMsCoa();
				}
				if(settlment.getNPsettlementType() == MedisafeConstants.INSURANCE_SETTLEMENT){
					//bayar dengan insurance
					totInsurance += settlment.getNAmountSettled();
					coaInsurance = settlment.getMsInsurance().getMsCoa();
				}
				if(settlment.getNPsettlementType() == MedisafeConstants.DEPOSIT_SETTLEMENT){
					//bayar dengan insurance
					totDeposit += settlment.getNAmountSettled();
//					coaDeposit = settlment.getMsCreditCardType().getMsCoa();
				}
			}
//			System.out.println("tot deposit");
//			System.out.println(totDeposit);
			for(TbExamination nota : notas){
				nota.setTbPatientBill(pbill);
				nota.setDWhnChange(tanggal);
				nota.setNPaymentStatus(MedisafeConstants.SUDAH_LUNAS);
				getHibernateTemplate().update(nota);
				
				//ar k
				//kas d
				memo.setLength(0);				
				memo.append("PATIENT:")
					.append(nota.getMsPatient().getVPatientName());
				
				voucherNo = nota.getVNoteNo();
				if(voucherNo.substring(0, 1).equals("I"))
					coaAR = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
				else
					coaAR = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_OUTPATIENT_AR);
				
//				coaKas = nota.getMsUnit().getCoa();
				
				totAr += nota.getTotal();
				//todo bagi-bagi ke rekening dokter, blm ke-handle
				/**
				 * 
				 * step:
				 * - ambil dafar treatment, looping
				 * - ambil rp bagian dokter/anestasi
				 * - ambil coa hutang dokter/anestasi (kredit)
				 * - ambil coa biaya dokter	(debit)
				 * 
				 * - shift blm ke-handle
				 * 
				 */
				Set treatementList = nota.getTbTreatmentTrx();
				Iterator iterTreat = treatementList.iterator();
				Double rpFeeStaff = null;
				Double persenRs = null;
				TbTreatmentTrx treat = null;
				MsDoctor msDoctor = null;
				while (iterTreat.hasNext()) {
					treat = (TbTreatmentTrx) iterTreat.next();
					msDoctor = getMsDoctor(treat);
					if(msDoctor != null){
						
						persenRs = getPersenRs(nota.getMsUnit(), nota.getMsShift(), msDoctor);
							
						coaBiayaDokter = treat.getMsDoctor().getMsCoa(); //coa biaya -> debit
//						rpFeeStaff = treat.getMsTreatmentFee().getNTrtfeeFee() * (1 - persenRs/100);
						rpFeeStaff = treat.getMsTreatmentFee().getNDoctorFee();
						// AP DOKTER   : K
						// BIAYA DOKTER: D
						jhandler.addJournal(voucherNo , memo.toString(), rpFeeStaff, MedisafeConstants.ACCT_CREDIT, tanggal, currentUser , coaApStaff);
						jhandler.addJournal(voucherNo , memo.toString(), rpFeeStaff, MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaBiayaDokter);
						
					}
					if ( treat.getAnastesiStaff() != null) {
						coaBiayaAnestasi = treat.getAnastesiStaff().getMsCoa(); //coa biaya -> debit
						rpFeeStaff = treat.getMsTreatmentFee().getNMedicFee();
						// AP ANESTASI   : K
						// BIAYA ANESTASI: D
						jhandler.addJournal(voucherNo, memo.toString(), rpFeeStaff, MedisafeConstants.ACCT_CREDIT, tanggal, currentUser, coaApStaff);
						jhandler.addJournal(voucherNo, memo.toString(), rpFeeStaff, MedisafeConstants.ACCT_DEBIT, tanggal, currentUser, coaBiayaAnestasi);
					}					
					
				}
				
			}
			
			if(depositPaid.getValue().doubleValue() > 0){
				String sql = "select " +
								"{deposit.*} " +
							 "from " +
							 	"tb_patient_deposit deposit " +
							 "where " +
							 	"deposit.n_reg_id=:regId"+
							 " order by " +
							 	"deposit.d_whn_create desc limit 1";
				
				SQLQuery query = getCurrentSession().createSQLQuery(sql);
    			query.addEntity("deposit", TbPatientDeposit.class);
    			query.setInteger("regId",pbill.getTbRegistration().getNRegId().intValue());
    			
    			TbPatientDeposit balance = (TbPatientDeposit)query.uniqueResult();
    			
    			double newBalance = balance.getNBalance() - depositPaid.getValue().doubleValue();
    			TbPatientDeposit newDeposit = new TbPatientDeposit();
    			newDeposit.setTbRegistration(pbill.getTbRegistration());
    			newDeposit.setNBalance(newBalance);
    			newDeposit.setNMutation(0 - depositPaid.getValue().doubleValue());
    			newDeposit.setVWhoCreate(pbill.getVWhoCreate());
    			newDeposit.setDWhnCreate(tanggal);
    			
    			getHibernateTemplate().save(newDeposit);
//    			coaDeposit = newDeposit.get
    			
    			//kas d (+) , hutang k (+)
    			memo.setLength(0);				
				memo.append("DEPOSIT:")
					.append(newDeposit.getNPdId());
				
				voucherNo = "??";
				
			}
			
//			todo blm bisa ngambil ???
			coaDeposit = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_PATIENT_AP);
			voucherNo = pbill.getVPbillCode();
//			if(coaDeposit == null){
//				coaDeposit = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_PATIENT_AP);
//			}
			if(pbill.getNPbillTtlPaid()<0){
				jhandler.addJournal(voucherNo , memo.toString(), pbill.getNPbillTtlPaid(), MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaKas);
			}
			
			MsCoa coaDisc = jhandler.getCoaFromGim("COA_BONUS_MEDIC");
			double tagihanPasien = 0;
			if(pbill.getNPbillDisc() > 0){
				tagihanPasien = pbill.getNPbillDisc() + pbill.getNPbillTtlPaid();
				jhandler.addJournal(voucherNo , memo.toString(), pbill.getNPbillDisc(), MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaDisc);
			}else tagihanPasien = pbill.getNPbillTtlPaid();
			
			
			if(totCash>0){
				//bayar dengan cash
				jhandler.addJournal(voucherNo , memo.toString(), totCash, MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaKas);
			}
			if(totBank>0){
				//bayar dengan deposit/bank
				jhandler.addJournal(voucherNo , memo.toString(), totBank, MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaBank);
			}
			if(totInsurance>0){
				//bayar dengan insurance
				jhandler.addJournal(voucherNo , memo.toString(), totInsurance, MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaInsurance);
			}
			if(totDeposit>0){
				jhandler.addJournal(voucherNo , memo.toString(), totDeposit, MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaDeposit);
			}
			
			jhandler.addJournal(voucherNo , memo.toString(), tagihanPasien, MedisafeConstants.ACCT_CREDIT, tanggal, currentUser , coaAR);
			
			//final check on Journal
			System.out.println(totInsurance);
			if (!jhandler.isBalance())
				throw new VONEAppException(MessagesService.getKey("master.coa.journal.discrepancy.internal"));
						
			Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
			while (it.hasNext()){
				getHibernateTemplate().save(it.next());
			}
			
			txManager.commit(status);
			success = true;
			
		}catch (Exception e){
			txManager.rollback(status);
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return success;
	}
	
	

	public Double getPersenRs(MsUnit msUnit, MsShift msShift, MsDoctor msDoctor) throws VONEAppException{
		Double persenRs = new Double(0);
		
		Integer nShift = null;
		if(msShift != null)
			nShift = msShift.getNShiftId();
		else
			nShift = new Integer(0);
			
			
		String sqlQuery =
			" select n_rs_persen as rs " +
			" from ms_doctor_rule " +
			" where n_unit_id = :n_unit_id " +
			" and v_doc_status = :status " +
			" and (n_shift_id = 0 or n_shift_id = :n_shift_id or n_shift_id is null) " +
			" order by n_priority ";
		Iterator iter = getCurrentSession()
			.createSQLQuery(sqlQuery)
			.addScalar("rs",Hibernate.DOUBLE)
			.setInteger("n_unit_id", msUnit.getNUnitId())
			.setInteger("n_shift_id", nShift)
			.setString("status", msDoctor.getVDocStatus())
			.list()
			.iterator();
		
		if(iter.hasNext()){
			persenRs = (Double)iter.next();
		}
		else{
			//bila tdk ada rule
			iter = getCurrentSession()
				.createSQLQuery(sqlQuery)
				.addScalar("rs",Hibernate.DOUBLE)
				.setInteger("n_unit_id", new Integer(0))
				.setInteger("n_shift_id", nShift)
				.setString("status", msDoctor.getVDocStatus())
				.list()
				.iterator();
			if(iter.hasNext()){
				persenRs = (Double)iter.next();
			}
		}
		return persenRs;
	}


	public MsDoctor getMsDoctor(TbTreatmentTrx treat) throws VONEAppException{
		MsDoctor msDoctor = null;
		String sqlQuery = "" +
				"select {doctor.*} " +
				"	from ms_doctor doctor," +
				"		tb_treatment_trx treat" +
				"	where" +
				"		doctor.n_staff_id = treat.n_doctor_id" +
				"		and treat.n_treatment_id = :id";
		
		try {
			Iterator iter = getCurrentSession().createSQLQuery(sqlQuery)
				.addEntity("doctor", MsDoctor.class)
				.setInteger("id", treat.getNTreatmentId())
				.list()
				.iterator();
			if(iter.hasNext())
				msDoctor = (MsDoctor)iter.next();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(), e);
		}
			
		return msDoctor;
	}


	public boolean saveDepost(TbPatientDeposit deposit, TbPatientBill pbill, MsCoa coaKas,
			List<TbPatientSettlement> list) throws VONEAppException{
		boolean success = false;

		JournalBeanHandler jhandler = new JournalBeanHandler(MedisafeConstants.ACCT_DEPOSIT_STR);

		MsCoa coaDeposit;
		coaDeposit = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_PATIENT_AP);
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = txManager.getTransaction(def);
		Date tanggal = new Date();
		String currentUser = deposit.getVWhoCreate();
		String desc;
		
		try {
			
			getHibernateTemplate().save(deposit);
			
			getHibernateTemplate().save(pbill);
			
			if(list != null) // pasien melakukan deposit
			{
				desc =  DEPOSIT_PASIEN + pbill.getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName();
				for(TbPatientSettlement detil : list){
					
					detil.setTbPatientBill(pbill);
					detil.setDWhnCreate(tanggal);
					
					getHibernateTemplate().save(detil);
					
					if(detil.getNPsettlementType() == MedisafeConstants.CASH_SETTLEMENT){
						//bayar tunai
						jhandler.addJournal(pbill.getVPbillCode() , desc, detil.getNAmountSettled(), 
								MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaKas);
					}else if(detil.getNPsettlementType() == MedisafeConstants.INSURANCE_SETTLEMENT){
						//bayar pakai insurance
						jhandler.addJournal(pbill.getVPbillCode() , desc, detil.getNAmountSettled(), 
								MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , 
								detil.getMsInsurance().getMsCoa());
					}else if(detil.getNPsettlementType() == MedisafeConstants.BANK_SETTLEMENT){
						//bayar pakai kartu kredit bank
						jhandler.addJournal(pbill.getVPbillCode() , desc, detil.getNAmountSettled(), 
								MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , 
								detil.getMsCreditCardType().getMsCoa());
					}
				}
				jhandler.addJournal(pbill.getVPbillCode() , desc, deposit.getNMutation(), 
						MedisafeConstants.ACCT_CREDIT, tanggal, currentUser , coaDeposit);
			}

			//pasien menerima retur deposit
			else{
				desc =  RETUR_DEPOSIT + pbill.getTbRegistration().getTbMedicalRecord().getMsPatient().getVPatientName();
				jhandler.addJournal(pbill.getVPbillCode() , desc, deposit.getNMutation()* (-1), 
						MedisafeConstants.ACCT_DEBIT, tanggal, currentUser , coaDeposit);
				jhandler.addJournal(pbill.getVPbillCode() , desc, deposit.getNMutation()* (-1), 
						MedisafeConstants.ACCT_CREDIT, tanggal, currentUser , coaKas);
			}
			
			
//			final check on Journal
			if (!jhandler.isBalance())
				throw new VONEAppException(MessagesService.getKey("master.coa.journal.discrepancy.internal"));
						
			Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
			while (it.hasNext()){
				getHibernateTemplate().save(it.next());
			}
			
			txManager.commit(status);
			success = true;
			
		}catch (Exception e){
			txManager.rollback(status);
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return success;
	}
	
	public TbPatientDeposit getBalancet(TbRegistration reg) throws VONEAppException{
		
		TbPatientDeposit balance = null;
		Session session = null;
		
		String sql = "select {deposit.*} from tb_patient_deposit deposit where deposit.n_reg_id=:regId"+
		               " order by deposit.d_whn_create desc limit 1";
		
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("deposit", TbPatientDeposit.class);
			query.setInteger("regId",reg.getNRegId().intValue());
			balance = (TbPatientDeposit)query.uniqueResult();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return balance;
	}
	
	
	public List getPatientBill(String nameOnBill, String billCode, 
			short paymentStatus, boolean isRetur) throws VONEAppException{
		List result = null;
		Session session = null;
		String sql = null;
		
		if(isRetur){
			sql = "select {bill.*} from tb_patient_bill bill, tb_retur_pharmacy_trx retur where "+
			       "bill.n_pbill_id=retur.n_pbill_id AND bill.v_name_on_bill LIKE :name AND "+
			       "bill.v_pbill_code LIKE :code AND bill.n_payment_status=:status";
		}
		else{
			sql = "select {bill.*} from tb_patient_bill bill where "+
			      " bill.v_name_on_bill LIKE :name AND "+
                  "bill.v_pbill_code LIKE :code AND bill.n_payment_status=:status";
		}
				
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("bill", TbPatientBill.class);
			query.setString("name", nameOnBill);
			query.setString("code", billCode);
			query.setShort("status", paymentStatus);
			
			result = query.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return result;
	}
	
	
	
	
	
	
	public boolean lockOrCheckOut(TbRegistration reg) throws VONEAppException
	{
		boolean success = false;
		
		try {
			getHibernateTemplate().update(reg);
			success = true;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(), e);
		}
		
		return success;
	}
	
	
	public boolean cancelBill(TbPatientBill bill) throws VONEAppException{
		boolean success = false;
		Session session = null;
		Transaction trans = null;
		
		try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			
			Set<TbExamination> notes = bill.getTbExamination();
			
			session.update(bill);
			
			for(TbExamination note : notes){
				note.setNPaymentStatus(MedisafeConstants.BELUM_LUNAS);
				note.setVWhoChange(bill.getVWhoChange());
				note.setDWhnChange(bill.getDWhnChange());
//				note.setTbPatientBill(null);
				session.update(note);
			}
			
			trans.commit();
			success = true;
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return success;
	}


	public void testPersen() throws VONEAppException{
		Session session = getCurrentSession();
		TbTreatmentTrx trx;//= (TbTreatmentTrx)session.get(TbTreatmentTrx.class,796);
		Iterator iter = session
			.createSQLQuery("select {t.*} from Tb_Treatment_Trx t where t.N_Doctor_Id is not null")
			.addEntity("t", TbTreatmentTrx.class)
			.list()
			.iterator();
		String sp = " -- ";
		while (iter.hasNext()) {
			
			trx = (TbTreatmentTrx) iter.next();
			System.out.println("-- SHIFT: " + trx.getTbExamination().getMsShift());
			System.out.println(
					trx.getTbExamination().getMsUnit().getVUnitName() + sp +
					getMsDoctor(trx).getVDocStatus() + sp +
					trx.getMsTreatmentFee(). getMsTreatment().getVTreatmentName() + sp +
					getPersenRs(
							trx.getTbExamination().getMsUnit(), 
							trx.getTbExamination().getMsShift(), 
							getMsDoctor(trx)));
		}
		
	}


	public TbPatientBill getBillById(Integer pbillId) throws VONEAppException{
		
		Criteria crit = getCurrentSession().createCriteria(TbPatientBill.class);
		crit.add(Restrictions.eq("NPbillId", pbillId));
		
		return (TbPatientBill)crit.uniqueResult();
	}


	public TbReturPharmacyTrx getReturById(Integer returId) throws VONEAppException{
		
		Criteria crit = getCurrentSession().createCriteria(TbReturPharmacyTrx.class);
		crit.add(Restrictions.eq("NReturId", returId));
		
		
		return (TbReturPharmacyTrx)crit.uniqueResult();
	}


	public List getTbxamination(MsPatient msPatient, Date dateFrom, Date dateTo) throws VONEAppException {
		List list = null;
		Session session = getCurrentSession();
		list = session
			.createQuery("" +
					"from TbExamination " +
					"where n_patient_id = :msPatient " +
					"and date_trunc('day', d_whn_create) between date_trunc('day', :dateFrom) and date_trunc('day', :dateTo) " +
					"and n_exam_status>0 " +
//					"and d_whn_create between :dateFrom and :dateTo " +
					"order by d_whn_create,n_exam_id ")
			.setDate("dateFrom", dateFrom)
			.setDate("dateTo", dateTo)
			.setInteger("msPatient", msPatient.getNPatientId())
			.list();
		return list;
	}


	public List<TbExamination> getNotes(TbRegistration reg, int active_note) throws VONEAppException{
		List<TbExamination> result = null;
		
		try {
			
			Criteria crit = getCurrentSession().createCriteria(TbExamination.class);
			crit.add(Restrictions.eq("tbRegistration", reg));
//			crit.add(Restrictions.eq("NPaymentStatus", MedisafeConstants.BELUM_LUNAS));
			crit.add(Restrictions.eq("NExamStatus", active_note));
//			crit.add(Restrictions.ne("NExamStatus", MedisafeConstants.CLOSE_NOTE));
			
			result = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());		
		}
		
		return result;
	}
	
	public List<TbExamination> getUnbillNotes(TbRegistration reg) throws VONEAppException{
		List<TbExamination> result = null;
		
		String sql = "select {nota.*} from tb_examination nota where nota.n_reg_id=:regId and " +
				"(nota.n_exam_status=:baru or nota.n_exam_status=:valid) and nota.n_payment_status=:belumLunas";
				
	
			
	try {
			Session session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("nota", TbExamination.class);
			query.setInteger("regId", reg.getNRegId());
			query.setInteger("baru", MedisafeConstants.ACTIVE_NOTE);
			query.setInteger("valid", MedisafeConstants.VALIDATED_NOTE);
			query.setShort("belumLunas", MedisafeConstants.BELUM_LUNAS);
		
			result = query.list();
			
		}catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());		
		}
		
		return result;
	}
	
	public Short getShift() throws VONEAppException{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String jam = sdf.format(new Date());
		
		StringBuffer sb = new StringBuffer();
//		sb.append("select {sh.*} from ms_shift sh where :vshift between sh.d_shift_from and sh.d_shift_to");
		sb.append("select {sh.*} from ms_shift sh where current_time between sh.d_shift_from and sh.d_shift_to");
		Session session = getCurrentSession();
		MsShift shift = (MsShift)session.createSQLQuery(sb.toString())
		.addEntity("sh", MsShift.class)
//		.setString("vshift", jam)
		.uniqueResult();
		
		return shift.getNShiftId().shortValue();
	}


	public List<TbPatientBill> getPatientBill(Date dateFrom, Date dateTo, String shiftId) throws VONEAppException {
		List<TbPatientBill> bill = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {b.*}");
		sb.append(" from tb_patient_bill b");
		sb.append(" where ");
		sb.append("b.d_whn_create between :dari and :from ");
		if(!shiftId.equalsIgnoreCase("A"))
			sb.append(" and b.id_shift =:shift");
		sb.append(" order by b.v_who_create");
		
		Session session = getCurrentSession();
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("b", TbPatientBill.class);
//		query.setString("dari", dateFrom);
		query.setDate("dari", dateFrom);
		query.setDate("from", dateTo);
//		query.setString("from", dateTo);
		if(!shiftId.equalsIgnoreCase("A"))
			query.setShort("shift", new Short(shiftId));
		
		bill = query.list();
		
		/**
		bill = session.createSQLQuery(sb.toString())
		.addEntity("b", TbPatientBill.class)
		.setString("dari", dateFrom)
		.setString("from", dateTo)
		.setShort("shift", new Short(shiftId))
		.list();*/
		
		
		
		return bill;
	}
	
	public List<Object[]> getRekapBillFunction(Date dateFrom, Date dateTo, String laporanType, String pasienType) throws VONEAppException{
		StringBuffer sb = new StringBuffer();
		
		sb.append("select tanggal as tgl,");
        sb.append("nomor_kwitansi as kwitansi, ");
        sb.append("nama_pasien as nama,");
        sb.append("nomor_mr as mr, ");
        sb.append("tipe_pasien as tipe,");
        sb.append("kelas_tarif as kelas, ");
        sb.append("tanggal_masuk as masuk,");
        sb.append("tanggal_keluar as keluar, ");
        sb.append("nama_dokter as dokter,");
        sb.append("total as ttl,");
        sb.append("tunai as tunai,");
        sb.append("non_tunai_bank as card,");
        sb.append("non_tunai_perusahaan as ins,");
        sb.append("nama_bank as bank,");
        sb.append("nama_perusahaan as perusahaan ");
        sb.append("from report.get_rekap_kasir(:mulai , :sampai) ");
        
        if(laporanType.equalsIgnoreCase("ALL")){
        	if(!pasienType.equalsIgnoreCase("ALL"))
        		sb.append("where tipe_pasien=:tipePasien ");
        }else{
        	sb.append("where tipe_registrasi=:reg ");
        	if(!pasienType.equalsIgnoreCase("ALL"))
        		sb.append("and tipe_pasien=:tipePasien ");

        }
        
        sb.append("order by tgl,nama ");
        
        Session session = getCurrentSession();
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.setDate("mulai", dateFrom);
		query.setDate("sampai", dateTo);
		
		if(laporanType.equalsIgnoreCase("ALL")){
        	if(!pasienType.equalsIgnoreCase("ALL"))
        		query.setString("tipePasien", pasienType);
        }else{
        	query.setString("reg", laporanType);
        	if(!pasienType.equalsIgnoreCase("ALL"))
        		query.setString("tipePasien", pasienType);
        }

		query.addScalar("tgl", Hibernate.DATE);
		query.addScalar("kwitansi", Hibernate.STRING);
		query.addScalar("nama", Hibernate.STRING);
		query.addScalar("mr", Hibernate.STRING);
		query.addScalar("tipe", Hibernate.STRING);
		query.addScalar("kelas", Hibernate.STRING);
		query.addScalar("masuk", Hibernate.DATE);
		query.addScalar("keluar", Hibernate.DATE);
		query.addScalar("dokter", Hibernate.STRING);
		query.addScalar("ttl", Hibernate.DOUBLE);
		query.addScalar("tunai", Hibernate.DOUBLE);
		query.addScalar("card", Hibernate.DOUBLE);
		query.addScalar("ins", Hibernate.DOUBLE);
		query.addScalar("bank", Hibernate.STRING);
		query.addScalar("perusahaan", Hibernate.STRING);
		
		return query.list();
	}
	
	public List<TbPatientBill> getRekapBill(Date dateFrom, Date dateTo, String laporanType, String pasienType) throws VONEAppException{
		List<TbPatientBill> bill = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {bill.*} ");
		sb.append(" from tb_patient_bill bill, tb_registration reg, tb_medical_record mr, ms_patient pat");
		sb.append(" where bill.d_whn_create between :dari and :sampai");
		sb.append(" and bill.n_pbill_ttl_paid > 0");
		sb.append(" and bill.n_reg_id=reg.n_reg_id");
		sb.append(" and reg.n_mr_id=mr.n_mr_id");
		sb.append(" and mr.n_patient_id=pat.n_patient_id");
		
		if(!laporanType.equalsIgnoreCase("ALL")){
			if(laporanType.equalsIgnoreCase("RJ"))
				sb.append(" and reg.v_reg_secondary_id like 'J%'");
			else if(laporanType.equalsIgnoreCase("RI"))
				sb.append(" and reg.v_reg_secondary_id like 'I%'");
		}
		
		if(!pasienType.equalsIgnoreCase("ALL")){
			if(pasienType.equalsIgnoreCase("BPJS")){
				sb.append(" and pat.n_patient_type_id=8 ");
			}
			else if(pasienType.equalsIgnoreCase("NONBPJS")){
				sb.append(" and (pat.n_patient_type_id <> 8 or pat.n_patient_type_id is null) ");
			}
		}
		

		Session session = getCurrentSession();
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("bill", TbPatientBill.class);
		query.setDate("dari", dateFrom);
		query.setDate("sampai", dateTo);
		
		bill = query.list();
		
		return bill;
	}


	public List getItemTrx(TbRegistration tbRegistration, Integer type) throws VONEAppException{
		
		List result = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select item.n_item_id id,item.v_item_code kode, item.v_item_name nama, ");
		sb.append("case ");
			sb.append("when item.n_type = 1 then 'PSIKOTROPIKA' ");
			sb.append("when item.n_type = 2 then 'NARKOTIKA' ");
			sb.append("when item.n_type = 3 then 'GENERIK' ");
			sb.append("when item.n_type = 4 then 'PATEN' ");
			sb.append("when item.n_type = 5 then 'BPJS' ");
		sb.append("end as tipeObat, ");
		sb.append("sum(trx.n_qty) as qty ,sum(trx.n_amount_after_disc) as total ");
		sb.append("from tb_item_trx trx, ms_item item, tb_examination nota, tb_registration reg ");
		sb.append("where ");
			sb.append("trx.n_item_id=item.n_item_id ");
			sb.append("and trx.n_note_id=nota.n_exam_id ");
			sb.append("and nota.n_reg_id=reg.n_reg_id ");
			sb.append("and nota.n_exam_status not in(0,4) ");
			sb.append("and reg.n_reg_id in(:regId, :regJalan) ");
			if(type.intValue() != 10) sb.append("and item.n_type=:tipeItem ");
		sb.append("group by id, kode, nama, tipeObat");
		
		
		try{
			Session session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sb.toString());
			
			query.setInteger("regId", tbRegistration.getNRegId());
			query.setInteger("regJalan", tbRegistration.getTbRegistration().getNRegId());
			
			if(type.intValue() != 10) query.setInteger("tipeItem", type);
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("kode", Hibernate.STRING);
			query.addScalar("nama", Hibernate.STRING);
			query.addScalar("tipeObat", Hibernate.STRING);
			query.addScalar("qty", Hibernate.INTEGER);
			query.addScalar("total", Hibernate.DOUBLE);
			
			result = query.list();
		}catch(Exception e){
			throw new VONEAppException(e.getMessage());
		}
	
		return result;
	}
	
public List getItemTrx(TbRegistration tbRegistration, Integer itemId, Integer type) throws VONEAppException{
		
		List result = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select item.n_item_id id,item.v_item_code kode, item.v_item_name nama, ");
		sb.append("case ");
			sb.append("when item.n_type = 1 then 'PSIKOTROPIKA' ");
			sb.append("when item.n_type = 2 then 'NARKOTIKA' ");
			sb.append("when item.n_type = 3 then 'GENERIK' ");
			sb.append("when item.n_type = 4 then 'PATEN' ");
			sb.append("when item.n_type = 5 then 'BPJS' ");
		sb.append("end as tipeObat, ");
		sb.append("sum(trx.n_qty) as qty ,sum(trx.n_amount_after_disc) as total ");
		sb.append("from tb_item_trx trx, ms_item item, tb_examination nota, tb_registration reg ");
		sb.append("where ");
			sb.append("trx.n_item_id=item.n_item_id ");
			sb.append("and trx.n_note_id=nota.n_exam_id ");
			sb.append("and nota.n_reg_id=reg.n_reg_id ");
			sb.append("and reg.n_reg_id=:regId ");
			sb.append("and item.n_item_id=:itemId ");
			if(type.intValue() != 10) sb.append("and item.n_type=:tipeItem ");
		sb.append("group by id, kode, nama, tipeObat");
		
		try{
			Session session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sb.toString());
			
			query.setInteger("regId", tbRegistration.getNRegId());
			query.setInteger("itemId", itemId);
			if(type.intValue() != 10) query.setInteger("tipeItem", type);
			
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("kode", Hibernate.STRING);
			query.addScalar("nama", Hibernate.STRING);
			query.addScalar("tipeObat", Hibernate.STRING);
			query.addScalar("qty", Hibernate.INTEGER);
			query.addScalar("total", Hibernate.DOUBLE);
			
			result = query.list();
		}catch(Exception e){
			throw new VONEAppException(e.getMessage());
		}
	
		return result;
	}


    public List getRetur(TbRegistration tbRegistration, Integer itemId) throws VONEAppException{
    	List result = null;
    	
    	StringBuffer sb = new StringBuffer();
		sb.append("select trx.n_item_id as item_id,sum(n_qty) as qty, sum(n_value) as total ");
		sb.append("from tb_retur_pharmacy_detail_trx trx, tb_retur_pharmacy_trx retur ");
		sb.append("where ");
			sb.append("trx.n_retur_id=retur.n_retur_id ");
			sb.append("and retur.n_reg_id=:regId ");
			sb.append("and trx.n_item_id=:itemId ");
		sb.append("group by item_id");
		
		try{
			Session session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sb.toString());
			
			query.setInteger("regId", tbRegistration.getNRegId());
			query.setInteger("itemId", itemId);
			
			query.addScalar("item_id", Hibernate.INTEGER);
			query.addScalar("qty", Hibernate.INTEGER);
			query.addScalar("total", Hibernate.DOUBLE);
			
			result = query.list();
		}catch(Exception e){
			throw new VONEAppException(e.getMessage());
		}
	
		return result;
    }
	
	
    /**public TbPatientBill getBillByCode(String billCode) throws VONEAppException{
    	try {
    	  	Criteria crit = getCurrentSession().createCriteria(TbPatientBill.class);
        	crit.add(Restrictions.eq("VPbillCode", billCode));
        	
        	return (TbPatientBill)crit.uniqueResult();
      
    	}catch(HibernateException e){
    		e.printStackTrace();
    	}
    	return null;
    }*/
    
    public TbPatientBill getBillByCode(String billCode) throws VONEAppException {
		TbPatientBill bill = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {b.*}");
		sb.append(" from tb_patient_bill b");
		sb.append(" where ");
		sb.append("b.v_pbill_code = :billCd ");
		
		Session session = getCurrentSession();
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("b", TbPatientBill.class);
		query.setString("billCd", billCode);
		
		bill = (TbPatientBill)query.uniqueResult();
		
		
		
		return bill;
	}
} 

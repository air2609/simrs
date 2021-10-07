package com.vone.medisafe.mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;







import org.hibernate.HibernateException;


import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.dao.NoteDAO;
import com.vone.medisafe.mapping.pojo.item.MsItem;




/**
 * Data access object (DAO) for domain model class TbRegistration.
 * @see com.vone.medisafe.mapping.TbRegistration
 * @author MyEclipse - Hibernate Tools
 */
public class TbRegistrationDAO extends NoteDAO {

 

	protected void initDao() {
		//do nothing
	}
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
                
    public boolean saveRegistration(MsPatient patient, TbMedicalRecord mr, TbRegistration registration,
    		Integer dokterId) throws VONEAppException{
    	boolean success = false;
    		
    	Session session = getCurrentSession();
    	
    	Transaction trans = session.beginTransaction();
    	
    	Integer pId = IdsServiceLocator.getIdsManager().getSequence(
				MedisafeConstants.PATIENT_ID);
		while (pId == null) {
			pId = IdsServiceLocator.getIdsManager().getSequence(
					MedisafeConstants.PATIENT_ID);
		}
		patient.setNPatientId(pId);
		
		try {
			
			session.save(patient);

			mr.setMsPatient(patient);

			Integer mrId = IdsServiceLocator.getIdsManager().getSequence(
					MedisafeConstants.MR_NUMBER);
			while (mrId == null) {
				mrId = IdsServiceLocator.getIdsManager().getSequence(
						MedisafeConstants.MR_NUMBER);
			}
			
			mr.setNMrId(mrId);
			mr.setVMrCode(MedisafeUtil.convertToMrCode(mrId));
			
			session.save(mr);

			Integer registrationNumber = IdsServiceLocator.getIdsManager()
					.getSequence(MedisafeConstants.REGISTRATION_NUMBER);
			while (registrationNumber == null) {
				registrationNumber = IdsServiceLocator.getIdsManager().getSequence(
						MedisafeConstants.REGISTRATION_NUMBER);
			}
			Date registrationDate = new Date();
			Integer registrationId = IdsServiceLocator.getIdsManager().getSequence(
					MedisafeConstants.REGISTRATION_ID);
			while (registrationId == null) {
				registrationId = IdsServiceLocator.getIdsManager().getSequence(
						MedisafeConstants.REGISTRATION_ID);
			}
			registration.setNRegId(registrationId);
			registration.setTbMedicalRecord(mr);
			registration.setDRegistrationDate(registrationDate);
			registration.setVRegSecondaryId(MedisafeUtil.convertToRegistrationCode(
					registrationNumber, registrationDate, registration.getMsUnit()
							.getVUnitCode()));
			
			session.save(registration);
			
//			Double biaya = new Double(MessagesService.getKey("biaya.pendaftaran"));
			Double biayaKartu = new Double(MessagesService.getKey("biaya.kartu"));
		    Double biaya = new Double(registration.getMsDivision().getNRegistrationCharge());
			
			TbExamination nota = new TbExamination();
			nota.setTbRegistration(registration);
			nota.setDWhnCreate(registrationDate);
			nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
			nota.setNExamStatus(new Integer(MedisafeConstants.VALIDATED_NOTE));
			nota.setVWhoCreate(registration.getVWhoCreate());
			nota.setMsPatient(mr.getMsPatient());
			nota.setTotal(biaya.doubleValue() + biayaKartu.doubleValue());
			Integer noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RAJAL);
			nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, registrationDate, "ADM", MedisafeConstants.NON_RANAP));
			session.save(nota);
			
			
			TbMiscTrx daftar = new TbMiscTrx();
			Integer miscId = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.MISC_ID);
			daftar.setNMiscTrxId(miscId);
			daftar.setTbExamination(nota);
			daftar.setDWhnCreate(registrationDate);
			daftar.setVWhoCreate(registration.getVWhoCreate());
			daftar.setNQty((short) 1);
			daftar.setNAmountTrx(biaya.doubleValue());
			daftar.setNAmountAfterDisc(biaya.doubleValue());
			daftar.setVMiscName("BIAYA PENDAFTARAN");
			daftar.setNItemPrice(biaya.doubleValue());
			session.save(daftar);
			
			JournalBeanHandler jhandler = new JournalBeanHandler(MedisafeConstants.ACCT_AR_STR);
			MsCoa coaAR = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
//			MsGim gim = getGimByCode(MedisafeConstants.COA_DEFAULT_MISC_TRX);
			MsGim gim = getGimByCode(MessagesService.getKey("coa.pendaftaran"));
			StringBuffer memo = new StringBuffer();
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
			.append(daftar.getVMiscName())
			.append(";QTY:")
			.append(daftar.getNQty());
			jhandler.addJournal(nota.getVNoteNo(), memo.toString(), nota.getTotal(), MedisafeConstants.ACCT_DEBIT, new Date(), registration.getVWhoCreate(), coaAR);
			jhandler.addJournal(nota.getVNoteNo(), memo.toString(), daftar.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), registration.getVWhoCreate(), coaMisc);

			
			
			
			daftar = new TbMiscTrx();
			miscId = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.MISC_ID);
			daftar.setNMiscTrxId(miscId);
			daftar.setTbExamination(nota);
			daftar.setDWhnCreate(registrationDate);
			daftar.setVWhoCreate(registration.getVWhoCreate());
			daftar.setNQty((short) 1);
			daftar.setNAmountTrx(biayaKartu.doubleValue());
			daftar.setNAmountAfterDisc(biayaKartu.doubleValue());
			daftar.setVMiscName("BIAYA PEMBUATAN KARTU");
			daftar.setNItemPrice(biayaKartu.doubleValue());
			session.save(daftar);
			
			memo.setLength(0);				
			memo.append("MISC:")
			.append(daftar.getVMiscName())
			.append(";QTY:")
			.append(daftar.getNQty());
			
			jhandler.addJournal(nota.getVNoteNo(), memo.toString(), daftar.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), registration.getVWhoCreate(), coaMisc);
			
			Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
			while (it.hasNext()){
				getHibernateTemplate().save(it.next());
				
			}
			
			
			trans.commit();
			success = true;
			
		} catch (HibernateException e) {
			
			trans.rollback();
			
			throw new VONEAppException(e.getMessage());
		}
    		
    			
    	
    	return success;
    }
    
    
    public void updateRegistration(TbRegistration reg){
    	
    	getHibernateTemplate().saveOrUpdate(reg);
    	
    }
    
    public boolean saveRegistrationOnly(TbRegistration registration, Integer dokterId, TbMedicalRecord mr, MsPatient patient)
    throws VONEAppException{
    	boolean success = false;
    	
    	Session session = getCurrentSession();
    	
    	Transaction trans = session.beginTransaction();
    		

		try {
			Integer registrationNumber = IdsServiceLocator.getIdsManager()
					.getSequence(MedisafeConstants.REGISTRATION_NUMBER);

			Date registrationDate = new Date();
			Integer registrationId = IdsServiceLocator.getIdsManager().getSequence(
					MedisafeConstants.REGISTRATION_ID);

			registration.setNRegId(registrationId);
			registration.setTbMedicalRecord(mr);
			registration.setDRegistrationDate(registrationDate);
			registration.setVRegSecondaryId(MedisafeUtil.convertToRegistrationCode(
					registrationNumber, registrationDate, registration.getMsUnit()
							.getVUnitCode()));

			session.save(registration);
			
			if(patient != null) session.save(patient);
			
//			Double biaya = new Double(MessagesService.getKey("biaya.pendaftaran"));
			Double biaya = new Double(registration.getMsDivision().getNRegistrationCharge());
			
			TbExamination nota = new TbExamination();
			nota.setTbRegistration(registration);
			nota.setDWhnCreate(registrationDate);
			nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
			nota.setNExamStatus(new Integer(MedisafeConstants.VALIDATED_NOTE));
			nota.setVWhoCreate(registration.getVWhoCreate());
			nota.setMsPatient(mr.getMsPatient());
			nota.setTotal(biaya);
			Integer noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RAJAL);
			nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, registrationDate, "ADM", MedisafeConstants.NON_RANAP));
			session.save(nota);
			
			TbMiscTrx daftar = new TbMiscTrx();
			Integer miscId = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.MISC_ID);
			daftar.setNMiscTrxId(miscId);
			daftar.setTbExamination(nota);
			daftar.setDWhnCreate(registrationDate);
			daftar.setVWhoCreate(registration.getVWhoCreate());
			daftar.setNQty((short) 1);
			daftar.setNAmountTrx(biaya.doubleValue());
			daftar.setNAmountAfterDisc(biaya.doubleValue());
			daftar.setVMiscName("BIAYA PENDAFTARAN");
			daftar.setNItemPrice(biaya.doubleValue());
			session.save(daftar);
			
			JournalBeanHandler jhandler = new JournalBeanHandler(MedisafeConstants.ACCT_AR_STR);
			MsCoa coaAR = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
			MsGim gim = getGimByCode(MedisafeConstants.COA_DEFAULT_MISC_TRX);
			StringBuffer memo = new StringBuffer();
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
			.append(daftar.getVMiscName())
			.append(";QTY:")
			.append(daftar.getNQty());
			jhandler.addJournal(nota.getVNoteNo(), memo.toString(), daftar.getNAmountAfterDisc(), MedisafeConstants.ACCT_DEBIT, new Date(), registration.getVWhoCreate(), coaAR);
			jhandler.addJournal(nota.getVNoteNo(), memo.toString(), daftar.getNAmountAfterDisc(), MedisafeConstants.ACCT_CREDIT, new Date(), registration.getVWhoCreate(), coaMisc);

			Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
			while (it.hasNext()){
				getHibernateTemplate().save(it.next());
				
			}
			
			success = true;
			
			trans.commit();
			
		} catch (HibernateException e) {
			trans.rollback();
			throw new VONEAppException(e.getMessage());
		}
    		
    	    	
    	return success;
    }
    
    public List getAllPatient(boolean closed){
    	return super.getHibernateTemplate().find(" from "+TbMedicalRecord.class.getName());
    }
  
    
    
    
    public TbRegistration getLastRegistrationByMrId(Integer id) throws VONEAppException{
    	
    	String query = "select {reg.*} from tb_registration reg where reg.n_mr_id=:id AND "+
    	               "reg.reg_status=:status order by d_registration_date desc limit 1";
    	
		TbRegistration	registration = (TbRegistration)getCurrentSession().createSQLQuery(query)
							.addEntity("reg",TbRegistration.class)
							.setInteger("id",id.intValue())
							.setInteger("status", MedisafeConstants.REG_ACTIVE)
							.uniqueResult();
		
		return registration;
    }
    
    public List getObatRajal(TbRegistration regRajal) throws VONEAppException{
    	List hasil = null;
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("select {trx.*} ");
    	sb.append("from tb_item_trx trx, ms_item item, tb_examination nota, tb_registration reg ");
    	sb.append("where trx.n_item_id=item.n_item_id and trx.n_note_id=nota.n_exam_id and nota.n_reg_id=reg.n_reg_id ");
    	sb.append("and reg.n_reg_id=:regId ");
    	
    	Session session = getCurrentSession();
    	
    	SQLQuery query = session.createSQLQuery(sb.toString());
    	query.setInteger("regId", regRajal.getNRegId());
    	query.addEntity("trx", TbItemTrx.class);
    	
    	hasil = query.list();
    	
    	return hasil;
    }
    
    
    public boolean saveRanapRegistration(TbRegistration reg, TbRegistration oldReg, MsBed bed, 
    		TbMedicalRecord mr, TbBedOccupancy boc, TbRoomReservation roomrsv) throws VONEAppException{
    	
    		boolean sukses = false;
    		
    	Session session = getCurrentSession();
    	
    	Date tanggal = new Date();

		reg.setDRegistrationDate(tanggal);
		reg.setNRegId(IdsServiceLocator.getIdsManager().getSequence(
				MedisafeConstants.REGISTRATION_ID));
		reg.setTbMedicalRecord(mr);
		reg.setDWhnCreate(tanggal);
		reg.setVRegSecondaryId(MedisafeUtil.convertToRanapCode(
				IdsServiceLocator.getIdsManager().getSequence(
						MedisafeConstants.RANAP_NUMBER), tanggal, null));

		if (oldReg != null)
			
			session.update(oldReg);
		
		session.save(reg);

		if (roomrsv != null) {

			roomrsv.setDWhnCreate(tanggal);
			roomrsv.setTbRegistration(reg);
			roomrsv.setNRoomRsvId(IdsServiceLocator.getIdsManager()
					.getSequence(MedisafeConstants.ROOM_RSV_ID));

			session.save(roomrsv);

		}

		boc.setDCheckInTime(tanggal);

		TbBedOccupancyId id = new TbBedOccupancyId();

		id.setDWhnCreate(tanggal);
		id.setMsBed(bed);
		id.setTbRegistration(reg);
		id.setDWhnCreate(tanggal);

		boc.setId(id);

		session.save(boc);

		bed.setVBedStatus("1");

		session.update(bed);
		
		List listObat = getObatRajal(oldReg);
		if(listObat.size() > 0){
			Iterator it = listObat.iterator();
			while(it.hasNext()){
				TbItemTrx trx = (TbItemTrx)it.next();
				
				TbPatientInventory patInv = new TbPatientInventory();
				patInv.setMsItem(trx.getMsItem());
				patInv.setMsPatient(reg.getTbMedicalRecord().getMsPatient());
				patInv.setNQty(trx.getNQty().shortValue());
				patInv.setDWhnCreate(tanggal);
				patInv.setVWhoCreate(reg.getVWhoCreate());
				patInv.setTbRegistration(reg);
				patInv.setTbItemTrx(trx);
				session.save(patInv);
			}
		}
		
				
		sukses = true;
				
		return sukses;
    }
    
    public Integer countRanap(TbMedicalRecord mr) throws VONEAppException{
    	
    	Integer nRanap = null;
    	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" {reg.*} ");
    	
    	query.append(" from " );
    	query.append(" tb_registration reg ");
    	
    	query.append(" where " );
    	query.append(" reg.n_mr_id=:id ");
    	query.append(" and reg.v_reg_secondary_id like :code ");
    	
		List list = getCurrentSession().createSQLQuery(query.toString())
						.addEntity("reg",TbRegistration.class)
						.setInteger("id",mr.getNMrId().intValue())
						.setString("code","I%")
						.list();
		
		nRanap = new Integer(list.size());
		
    	
    	return nRanap;
    	
    }
    
    public TbRegistration getLastRegistration(String mrCode) throws VONEAppException{
    	
	TbRegistration reg = null;
    	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" {reg.*} ");
    							
    	query.append(" from ");
    	query.append(" tb_registration reg, ");
    	query.append(" tb_medical_record mr ");
    	
    	query.append(" where " );
    	query.append(" mr.n_mr_id=reg.n_mr_id ");
    	query.append(" and mr.v_mr_code=:mrCode ");
    	
    	query.append(" order by d_registration_date desc limit 1");

    		try {
    			
				reg = (TbRegistration)getCurrentSession().createSQLQuery(query.toString())
					.addEntity("reg",TbRegistration.class)
					.setString("mrCode",mrCode)
					.uniqueResult();
				
			} catch (HibernateException e) {
				
				throw new VONEAppException(e.getMessage());
			}
    		
    		
    	return reg;
    	
    }
    
    public TbRegistration getRegistrationByCode(String mrCode) throws VONEAppException{
    	
    	TbRegistration reg = null;
    	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" {reg.*} ");
    							
    	query.append(" from ");
    	query.append(" tb_registration reg, ");
    	query.append(" tb_medical_record mr ");
    	
    	query.append(" where " );
    	query.append(" reg.n_mr_id=mr.n_mr_id ");
    	query.append(" and mr.v_mr_code=:mrCode ");
    	query.append(" and reg.reg_status=:status ");
    	
    	query.append(" order by d_registration_date desc limit 1");

    		try {
    			
				reg = (TbRegistration)getCurrentSession().createSQLQuery(query.toString())
					.addEntity("reg",TbRegistration.class)
					.setString("mrCode",mrCode)
					.setInteger("status", MedisafeConstants.REG_ACTIVE)
					.uniqueResult();
				
			} catch (HibernateException e) {
				
				throw new VONEAppException(e.getMessage());
			}
    		
    		
    	return reg;
    }
    
    
    
    public TbRegistration getRegistrationByRegistrationCode(String regCode) throws VONEAppException{
    	
    	Criteria crit = getCurrentSession().createCriteria(TbRegistration.class);
    	crit.add(Restrictions.eq("VRegSecondaryId", regCode));
    	
    	TbRegistration reg = (TbRegistration)crit.uniqueResult();
    	
    	return reg;
    	
    }
    
    public boolean deleteRegistration(TbRegistration reg, TbExamination nota, List<TbJournalTrx> journal) throws VONEAppException{
    	boolean success = false;
    	
    	try {
    		getHibernateTemplate().deleteAll(journal);
    		getHibernateTemplate().deleteAll(nota.getTbMiscTrx());
    		if(nota.getTbPatientBill() != null){
    			getHibernateTemplate().deleteAll(nota.getTbPatientBill().getTbPatientBillDetails());
        		getHibernateTemplate().delete(nota.getTbPatientBills());
    		}
    		
    		getHibernateTemplate().delete(nota);
			getHibernateTemplate().delete(reg);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return success;
    }
    
    
    public void cancelRanap(TbRegistration reg, TbRegistration oldReg, TbBedOccupancy boc)
    	throws VONEAppException
    {
    	
    		Session session = getCurrentSession(); 
			
			
			MsBed bed = boc.getId().getMsBed();
			bed.setVBedStatus(MedisafeConstants.BED_KOSONG);
			
			session.update(bed);
			
			session.delete(boc);
			
			oldReg.setRegStatus(MedisafeConstants.REG_ACTIVE);
			session.update(oldReg);
			
			session.delete(reg);
	
    	
    }


	public TbRegistration getRegistrationBaseOnWard(String code, String unitName) throws VONEAppException{
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select ");
		sb.append(" {reg.*} ");
		
		sb.append(" from ");
		sb.append(" tb_registration reg, ");
		sb.append(" tb_medical_record mr, ");
		sb.append("	tb_bed_occupancy boc, ");
		sb.append(" ms_bed bed, ");
		sb.append(" ms_room room, ");
		sb.append(" ms_hall hall, ");
		sb.append(" ms_ward bangsal ");
    	
		sb.append(" where ");
		sb.append(" mr.n_mr_id=reg.n_mr_id ");
		sb.append(" and reg.reg_status =:status ");
		sb.append(" and reg.n_reg_id=boc.n_reg_primary_id ");
		sb.append(" and boc.d_check_out_time is null ");
		sb.append(" and boc.n_bed_primary_id=bed.n_bed_id ");
		sb.append(" and bed.n_room_id=room.n_room_id ");
		sb.append(" and room.n_hall_id=hall.n_hall_id ");
		sb.append(" and bangsal.n_ward_id=hall.n_ward_id ");
		sb.append(" and mr.v_mr_code=:mrCode ");
		sb.append(" and bangsal.v_ward_name =:bangsalName ");
		
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		
		query.addEntity("reg", TbRegistration.class);
		query.setInteger("status", MedisafeConstants.REG_ACTIVE);
		query.setString("mrCode", code);
		query.setString("bangsalName", unitName);
		
		
		
		return (TbRegistration)query.uniqueResult();
	}
	
	public List<TbExamination> getListNoteByRegistration(TbRegistration reg) throws VONEAppException{
		List<TbExamination> notaList = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {nota.*} from tb_examination nota");
		sb.append(" where nota.n_reg_id=:regId");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("nota", TbExamination.class);
		query.setInteger("regId", reg.getNRegId());
		
		notaList = query.list();
		
		return notaList;
	}


	public List<TbJournalTrx> getListJournalByCode(String vRegSecondaryId) throws HibernateException, VONEAppException {
		List<TbJournalTrx> notaList = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {nota.*} from tb_journal_trx nota");
		sb.append(" where nota.v_voucher_no=:regId");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("nota", TbJournalTrx.class);
		query.setString("regId", vRegSecondaryId);
		
		notaList = query.list();
		
		return notaList;
	}
	
	public List<TbRegistration> getActiveRegistration(Date starDate, Date endDate) throws HibernateException, VONEAppException 
	{
		List<TbRegistration> regList = null;
		
		endDate.setDate(endDate.getDate()+1);
		
		StringBuffer sb = new StringBuffer();
		sb.append("select {reg.*} from tb_registration reg");
		sb.append(" where reg.reg_status=:status");
		sb.append(" and reg.d_registration_date between :vfrom and :vto");
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("reg", TbRegistration.class);
		query.setInteger("status", 1);
		query.setDate("vfrom", starDate);
		query.setDate("vto", endDate);
		regList = query.list();
		
		return regList;
	}


	public void checkOutRegistration(TbRegistration reg) {
		getHibernateTemplate().saveOrUpdate(reg);
	}


	public List<Object> getLaporanPendaftaran(Date dari, Date sampai, int unitId) throws HibernateException, VONEAppException {
		StringBuffer sb = new StringBuffer();
		sb.append("select lap.tanggal as tgl, lap.unit_name as unit, ");
		sb.append("lap.jumlah_laki_laki as laki,");
		sb.append("lap.jumlah_perempuan as perempuan, lap.jumlah_lama as lama, lap.jumlah_baru as baru, lap.total as ttl");
		sb.append(" from ");
		if(unitId == 0)
			sb.append("report.generate_laporan_pendaftaran(:vfrom,:vto) lap");
		else
		{
			sb.append("report.laporan_pendaftaran_perunit(:vfrom, :vto, :vunit) lap");
		}
		sb.append(" order by tgl, unit");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addScalar("tgl", Hibernate.DATE);
		query.addScalar("unit", Hibernate.STRING);
		query.addScalar("laki", Hibernate.INTEGER);
		query.addScalar("perempuan", Hibernate.INTEGER);
		query.addScalar("lama", Hibernate.INTEGER);
		query.addScalar("baru", Hibernate.INTEGER);
		query.addScalar("ttl", Hibernate.INTEGER);
		
		query.setDate("vfrom", dari);
		query.setDate("vto", sampai);
		if(unitId != 0)
			query.setShort("vunit", (short)unitId);
		return query.list();
	}
	
	
	public List<TbRegistration> getRegistrationOldPatient(int mrStatus) throws HibernateException, VONEAppException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		Calendar cal = Calendar.getInstance();
		String skrg = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 1);
		String besok = sdf.format(cal.getTime());
		
		try {
			Date tglSkrg = sdf.parse(skrg);
			Date tglBesok = sdf.parse(besok);
			
			String query = "select {oldr.*} from tb_registration oldr where oldr.n_reg_id in (select n_reg_id from "
					+      "(select reg.n_reg_id,reg.n_mr_id,  (select count(1) from tb_registration r where r.n_mr_id = reg.n_mr_id and r.d_registration_date <  :dfrom) as flag "
					+      "from tb_registration reg where reg.d_registration_date between :dfrom and :dto) q where q.flag > 0) ";
			
			if(mrStatus == MedisafeConstants.MR_NOT_READY) query = query + " and oldr.mr_status is null ";
			else query = query + " and oldr.mr_status='"+MedisafeConstants.MR_READY+"'";
			
			query = query + " order by oldr.d_registration_date asc ";
			
			System.out.println("query persiapan mr : "+query);
			
			SQLQuery sql = getCurrentSession().createSQLQuery(query);
			sql.addEntity("oldr", TbRegistration.class);
			sql.setDate("dfrom", tglSkrg);
			sql.setDate("dto", tglBesok);
			
			return sql.list();
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	public boolean checkActiveRanap(TbMedicalRecord mr) throws HibernateException, VONEAppException {
		String sql = "select {reg.*} from tb_registration reg where n_mr_id=:mrId and reg_status=:status and n_transfer_reg_id is not null";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		
		query.addEntity("reg", TbRegistration.class);
		query.setInteger("mrId", mr.getNMrId());
		query.setInteger("status", MedisafeConstants.REG_ACTIVE);
		
		List result = query.list();
		if(result.size() > 0)
			return true;
		
		return false;
	}
      
}
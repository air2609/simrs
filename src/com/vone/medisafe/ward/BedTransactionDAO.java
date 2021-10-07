package com.vone.medisafe.ward;

import java.util.Date;
import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbBor;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbRegistration;

public class BedTransactionDAO extends HibernateDaoSupport{
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	
	public MsBed getBedById(Integer id) throws VONEAppException{
		
		return (MsBed) getCurrentSession().get(MsBed.class, id);
	}
	
	
	public List<TbBedOccupancy> getBocsBaseOnRegistration(TbRegistration reg) throws VONEAppException{
		
		List<TbBedOccupancy> result = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select "); 
		sb.append(" {boc.*} ");
		
		sb.append(" from ");
		sb.append(" tb_bed_occupancy boc ");
		
		sb.append(" where ");
		sb.append(" boc.n_reg_primary_id=:regId ");
		
		sb.append(" order by boc.d_whn_create asc ");
		
		try {
		
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addEntity("boc", TbBedOccupancy.class);
			query.setInteger("regId", reg.getNRegId().intValue());
			
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	public TbBedTrx getBTrxByNota(TbExamination nota){
		TbBedTrx result = null;
		Session session = null;
		
		String sql = "select {btrx.*} from tb_bed_trx btrx where n_note_id = :nota";
		
		try {
			session = super.getSessionFactory().openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("btrx", TbBedTrx.class);
			query.setEntity("nota", nota);			
			result = (TbBedTrx)query.uniqueResult();
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		
		return result;
	}
	
	public boolean save(TbExamination nota, TbBedTrx bedTrx, 
			boolean isClosed, TbBedOccupancy boc, List<Date> bors) throws VONEAppException
	{
		boolean success = false;
		Session session = null;
		
		Transaction trans = null;
		
		try {
			session = getSessionFactory().openSession();
			trans = session.beginTransaction();
			
			session.save(nota);
			
			bedTrx.setTbExamination(nota);
			
			session.save(bedTrx);
			
			MsBed  msBed = getBedById(bedTrx.getMsBed().getNBedId());
			for(Date tgl : bors) {
				TbBor bor = new TbBor();
				bor.setBedDate(tgl);
				bor.setBedId(msBed.getNBedId());
				bor.setBedTrxId(bedTrx.getNBtrxId());
				bor.setHallId(msBed.getMsRoom().getMsHall().getNHallId());
				bor.setTclassId(msBed.getMsTreatmentClass().getNTclassId());
				
				session.save(bor);
			}
			
			if(isClosed){
				boc.setDCheckOutTime(new Date());
				boc.setVOutNote("K");
				
				session.update(boc);
				
				MsBed bed = bedTrx.getMsBed();
				bed.setVBedStatus(MedisafeConstants.BED_KOSONG);
				session.update(bed);
				
			}
			
			//create bed journal
			
			trans.commit();
			success = true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			trans.rollback();
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		
		
		return success;
	}
	
	
	public List<TbBedTrx> getBedTrxBaseOnPatient(MsPatient patient, MsBed bed) throws VONEAppException{
		List<TbBedTrx> result = null;
		Session session = null;
		
		String sql = "select {trx.*} from tb_bed_trx trx where trx.n_patient_id=:patId AND trx.n_bed_id=:bedId";
		
		try {
			
			SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
			
			query.addEntity("trx", TbBedTrx.class);
			query.setInteger("patId", patient.getNPatientId().intValue());
			query.setInteger("bedId", bed.getNBedId().intValue());
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		
		return result;
	}
}

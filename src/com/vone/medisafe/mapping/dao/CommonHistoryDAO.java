package com.vone.medisafe.mapping.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbTreatmentTrx;

public class CommonHistoryDAO extends HibernateDaoSupport{
	
	private static final Logger log = Logger.getLogger(CommonHistoryDAO.class);
	
	public List getPatientNote(MsPatient patient, MsUnit unit)throws VONEAppException{
		log.debug("CommonHistoryDAO -- getPatientTreatmentHistory(MsPatient patient, MsUnit unit) -- ENTER");
		List result = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbExamination.class);
			crit.add(Restrictions.eq("msPatient", patient));
			crit.add(Restrictions.ne("NExamStatus", MedisafeConstants.CANCEL_NOTE));
			crit.addOrder(Order.desc("DWhnCreate"));
			if(unit != null)crit.add(Restrictions.eq("msUnit", unit));
			
			result = crit.list();
			
			log.debug("BANYAKNYA NOTA PASIENT : "+result.size());
		}catch (Exception e){
			
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		log.debug("CommonHistoryDAO -- getPatientTreatmentHistory(MsPatient patient, MsUnit unit) -- EXIT");
		return result;
	}
	
	public List getPatientTreatment(TbExamination note)throws VONEAppException{
		
		log.debug("CommonHistoryDAO -- getPatientTreatment(TbExamination note) -- ENTER");
		List result = null;
		Session session = null;
		
		
		try {
			
			session = getSessionFactory().getCurrentSession();
			
			Criteria crit = session.createCriteria(TbTreatmentTrx.class);
			crit.add(Restrictions.eq("tbExamination", note));
			crit.addOrder(Order.desc("DWhnCreate"));
			
			result = crit.list();
			
			log.debug("BANYAKNYA DATA TREATMENT : "+result.size());
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		log.debug("CommonHistoryDAO -- getPatientTreatment(TbExamination note) -- EXIT");
		return result;
		
	}
	
	public List getPatientItem(TbExamination note)throws VONEAppException{
		
		log.debug("CommonHistoryDAO -- getPatientTreatment(TbExamination note) -- ENTER");
		List result = null;
		Session session = null;
		
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(TbItemTrx.class);
			crit.add(Restrictions.eq("tbExamination", note));
			result = crit.list();
			
			log.debug("BANYAKNYA DATA TREATMENT : "+result.size());
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		log.debug("CommonHistoryDAO -- getPatientTreatment(TbExamination note) -- EXIT");
		return result;
		
	}
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }

	public List getRekapTreatment(MsPatient msPatient, MsUnit unit) throws VONEAppException{
		List result = null;
		
		StringBuffer query = new StringBuffer();
		
		query.append(" select ");
		query.append(" a.id as idTreatment, ");
		query.append(" a.keterangan as treatmentName, ");
		query.append(" sum(a.jumlah) as banyak ");
		
		query.append(" from ");
		
			if(unit != null){
				query.append("(	select * from fungsi_rekap_pasien('"
						+msPatient.getNPatientId()+"','"+unit.getNUnitId()+"'))");
			}
			else{
				query.append("(	select * from fungsi_rekap_pasien('"
						+msPatient.getNPatientId()+"',"+ null+"))");
			}
			
		query.append(" as a ");
		query.append(" group by a.id,a.keterangan ");
		query.append(" order by treatmentName");
			
		SQLQuery q = getCurrentSession().createSQLQuery(query.toString());
		q.addScalar("idTreatment", Hibernate.INTEGER);
		q.addScalar("treatmentName", Hibernate.STRING);
		q.addScalar("banyak", Hibernate.INTEGER);
		
		
		result = q.list();
		
		return result;
	}

	public List getRekapItem(MsPatient msPatient, MsUnit unit) throws VONEAppException{
		
		List result = null;
		
		StringBuffer query = new StringBuffer();
		
		query.append(" select ");
		query.append(" a.id as idTreatment, ");
		query.append(" a.keterangan as treatmentName, ");
		query.append(" sum(a.jumlah) as banyak ");
		
		query.append(" from ");
		
			if(unit != null){
				query.append("(	select * from fungsi_rekap_pasien_obm('"
						+msPatient.getNPatientId()+"','"+unit.getNUnitId()+"'))");
			}
			else{
				query.append("(	select * from fungsi_rekap_pasien_obm('"
						+msPatient.getNPatientId()+"',"+ null+"))");
			}
			
		query.append(" as a ");
		query.append(" group by a.id,a.keterangan ");
		query.append(" order by treatmentName");
			
		SQLQuery q = getCurrentSession().createSQLQuery(query.toString());
		q.addScalar("idTreatment", Hibernate.INTEGER);
		q.addScalar("treatmentName", Hibernate.STRING);
		q.addScalar("banyak", Hibernate.INTEGER);
		
		
		result = q.list();
		
		return result;
	}
    
}

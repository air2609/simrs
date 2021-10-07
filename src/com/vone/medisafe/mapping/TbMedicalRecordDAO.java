package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;



/**
 * Data access object (DAO) for domain model class TbMedicalRecord.
 * @see com.vone.medisafe.mapping.TbMedicalRecord
 * @author MyEclipse - Hibernate Tools
 */
public class TbMedicalRecordDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbMedicalRecordDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	
    
    public void save(TbMedicalRecord transientInstance) {
        log.debug("saving TbMedicalRecord instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TbMedicalRecord persistentInstance) {
        log.debug("deleting TbMedicalRecord instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	public TbRegistration getLastActiveRegistration(Integer mrId) throws VONEAppException{
		TbRegistration reg = null;
		String query ="select {reg.*} from tb_registration reg where reg.n_mr_id=:mr AND reg.reg_status=:status";
		
		try{
		
		reg = (TbRegistration)getCurrentSession().createSQLQuery(query)
		.addEntity("reg",TbRegistration.class)
		.setInteger("mr",mrId)
		.setInteger("status", MedisafeConstants.REG_ACTIVE)
		.uniqueResult();

		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return reg;
	}
    
    public TbMedicalRecord findById( java.lang.Integer id) {
        log.debug("getting TbMedicalRecord instance with id: " + id);
        try {
            TbMedicalRecord instance = (TbMedicalRecord) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.TbMedicalRecord", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TbMedicalRecord instance) {
        log.debug("finding TbMedicalRecord instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.TbMedicalRecord")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public TbMedicalRecord merge(TbMedicalRecord detachedInstance) {
        log.debug("merging TbMedicalRecord instance");
        try {
            TbMedicalRecord result = (TbMedicalRecord) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbMedicalRecord instance) {
        log.debug("attaching dirty TbMedicalRecord instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TbMedicalRecord instance) {
        log.debug("attaching clean TbMedicalRecord instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public Integer getMedicalRecordId() throws VONEAppException {
    	Session session = getCurrentSession();
    	Integer integer = null;
//    	SQLQuery sql = super.getSession().createSQLQuery(query);
    	try {
			integer = (Integer)session.createSQLQuery("select nextval('mr_code') as id")
								.addScalar("id", Hibernate.INTEGER)
								.uniqueResult();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
    	}
    	
    	return integer;
    }
   
    public TbMedicalRecord getPatientMedicalRecord(String medicCode) throws VONEAppException{
    	
    	 
    	
    	String query ="select {mr.*} from tb_medical_record mr where mr.v_mr_code = :mrCode";
    	
    	TbMedicalRecord mr = (TbMedicalRecord)getCurrentSession().createSQLQuery(query)
						 		.addEntity("mr",TbMedicalRecord.class)
						 		.setString("mrCode",medicCode)
						 		.uniqueResult();
		
    	return mr;
    }
    

    public TbMedicalRecord getMrRegistered(String medicCode) throws VONEAppException {
    	 	

    	String query ="select distinct {mr.*} from tb_medical_record mr, tb_registration reg"+
    	              " where mr.v_mr_code = :mrCode AND mr.n_mr_id=reg.n_mr_id AND reg.reg_status=:status";
    	
    	TbMedicalRecord	 mr = null;
    	
    	try{
    		
    		mr = (TbMedicalRecord)getCurrentSession().createSQLQuery(query)
			 .addEntity("mr",TbMedicalRecord.class)
			 .setString("mrCode",medicCode)
			 .setInteger("status", MedisafeConstants.REG_ACTIVE)
			 .uniqueResult();

    	}
    	
		catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}

    	return mr;
    }
    

    public TbMedicalRecord getMedicalRecordByPaitentId(Integer id) throws VONEAppException {
    	
    	Session session = getCurrentSession();

    	TbMedicalRecord mr = null;
    	
    	String query ="select {mr.*} from tb_medical_record mr where mr.n_patient_id = :patientId";
    	
    	
    	try {
			 mr = (TbMedicalRecord)session.createSQLQuery(query)
								 .addEntity("mr",TbMedicalRecord.class)
								 .setInteger("patientId", id.intValue())
								 .uniqueResult();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
    	return mr;
    }
    
    public List<TbMedicalRecord> getMROut() throws VONEAppException{
    	List<TbMedicalRecord> result = null;
    	Session session = null;
    	
    	try {
			
    		session = getSessionFactory().getCurrentSession();
			
			Criteria crit = session.createCriteria(TbMedicalRecord.class);
			crit.add(Restrictions.ne("VMrStatus", MedisafeConstants.TERSEDIA));
			
			result = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
    	return result;
    }
    
    
    
    public List<TbMedicalRecord> getMrByStatus(String status) throws VONEAppException
    {
    	List<TbMedicalRecord> mrList = null;
    	
    	try {
			
			Criteria crit = getCurrentSession().createCriteria(TbMedicalRecord.class);
			crit.add(Restrictions.eq("VMrStatus", status));
			
			mrList = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
			
		}
		
    	return mrList;
    }

    
	public Integer countVisit(TbMedicalRecord mr) throws VONEAppException{
		Session session = null;
		
		String sql = " select count (*) as jumlah from tb_registration where n_mr_id=:mrId "+
					 " and v_reg_secondary_id like :rajal";
		
		try {
			session = getSessionFactory().openSession();
			SQLQuery query = session.createSQLQuery(sql);
			
			query.addScalar("jumlah", Hibernate.INTEGER);
			query.setInteger("mrId", mr.getNMrId());
			query.setString("rajal", "J%");
			
			return (Integer)query.uniqueResult();
			
		} catch (HibernateException e) {
			
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		
		return null;
	}
	

	public boolean saveDiagnose(TbDiagnose diagnose, List<TbIcdDiagnose> icdList, List<TbIcd9Diagnose> icd9List, 
			List<TbIllness> illness, List<TbOperation> operation, List<TbViolent> rudapaksa, 
			List<TbPregnancy> kehamilan, List<TbStillbirth> keguguran, boolean isUpdate) throws VONEAppException{
		
		boolean success = false;
		Session session = null;
		Transaction trans = null;
		
		try {
			session = getSessionFactory().openSession();
			trans = session.beginTransaction();
			
			if(isUpdate){
				session.createQuery("delete from TbIcdDiagnose where tbDiagnose=:diagnose")
					.setEntity("diagnose", diagnose)
					.executeUpdate();
				
				session.createQuery("delete from TbIcd9Diagnose where tbDiagnose=:diagnose")
					.setEntity("diagnose", diagnose)
					.executeUpdate();
				
				session.createQuery("delete from TbIllness where tbDiagnose=:diagnose")
					.setEntity("diagnose", diagnose)
					.executeUpdate();
				
				session.createQuery("delete from TbOperation where tbDiagnose=:diagnose")
					.setEntity("diagnose", diagnose)
					.executeUpdate();
				
				session.createQuery("delete from TbViolent where tbDiagnose=:diagnose")
					.setEntity("diagnose", diagnose)
					.executeUpdate();
				
				session.createQuery("delete from TbPregnancy where tbDiagnose=:diagnose")
					.setEntity("diagnose", diagnose)
					.executeUpdate();
				
				session.createQuery("delete from TbStillbirth where tbDiagnose=:diagnose")
					.setEntity("diagnose", diagnose)
					.executeUpdate();
				
			}
			
			session.saveOrUpdate(diagnose);
			
			for(TbIcdDiagnose icdDagnose : icdList){
				icdDagnose.setTbDiagnose(diagnose);
				
				session.save(icdDagnose);
			}
			
			for(TbIcd9Diagnose icd9Diagnose : icd9List){
				icd9Diagnose.setTbDiagnose(diagnose);
				
				session.save(icd9Diagnose);
			}
			
			for(TbIllness ilnes : illness){
				ilnes.setTbDiagnose(diagnose);
				
				session.save(ilnes);
			}
			
			
			for(TbOperation op : operation){
				op.setTbDiagnose(diagnose);
				
				session.save(op);
				
			}
			
			for(TbViolent ruda : rudapaksa){
				ruda.setTbDiagnose(diagnose);
				
				session.save(ruda);
			}
			
			for(TbPregnancy preganancy : kehamilan){
				preganancy.setTbDiagnose(diagnose);
				
				session.save(preganancy);
			}
			
						
			for(TbStillbirth stilbirth : keguguran){
				stilbirth.setTbDiagnose(diagnose);
				
				session.save(stilbirth);
			}
			
			
			trans.commit();
			success = true;
		} catch (HibernateException e) {
			
			trans.rollback();
			
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		
		return success;
	}

	
	public List<TbMedicalRecord> getMrList(String code, String name, String address) throws VONEAppException{
		List<TbMedicalRecord> mrList = null;
			
		String query = "select " +
							"{mr.*} " +
						"from " +
							"tb_medical_record mr, " +
							"ms_patient p " +
						"where " +
							" mr.v_mr_code like :mrCode " +
							" and (mr.n_patient_id=p.n_patient_id)"+ 
							" and p.v_patient_name like :name " +
							" and p.v_patient_main_addr like :address";
		
			SQLQuery sql = getCurrentSession().createSQLQuery(query);
			sql.addEntity("mr", TbMedicalRecord.class);
			sql.setString("mrCode", code);
			sql.setString("name", name);
			sql.setString("address",address);
			
			mrList = sql.list();
	
			
		
		return mrList;
	}
	
	public Session getCurrentSession() throws VONEAppException {
	        try {
	            return getHibernateTemplate().getSessionFactory().getCurrentSession();
	        } catch (Exception e) {
	            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
	            throw new VONEAppException("Failed to get current session");
	        }
	}
    
//    public TbMedicalRecord getByRegistration(TbRegistration reg){
//    	TbMedicalRecord mr = null;
//    	Session session = null;
//    	String query="select {mr.*} from tb_medical_record mr where mr.v_mr_code = :mrCode";
//    	return mr;
//    }
}
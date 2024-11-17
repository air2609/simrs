package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;




import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;





/**
 * Data access object (DAO) for domain model class MsPatient.
 * @see com.vone.medisafe.mapping.MsPatient
 * @author MyEclipse - Hibernate Tools
 */
public class MsPatientDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsPatientDAO.class);

	protected void initDao() {
		//do nothing
	}
    
    public boolean save(MsPatient patient, TbMedicalRecord mr) {
        boolean sukses = false;
        Session session = null; 
        Transaction trans = null;
        
        try {
			session = super.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.saveOrUpdate(patient);
			session.saveOrUpdate(mr);
			trans.commit();
			sukses = true;
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			sukses = false;
			trans.rollback();
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
        
        
        return sukses;
    }
    
	public void delete(MsPatient persistentInstance) {
        log.debug("deleting MsPatient instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public MsPatient findById( java.lang.Integer id) {
        log.debug("getting MsPatient instance with id: " + id);
        try {
            MsPatient instance = (MsPatient) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.MsPatient", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(MsPatient instance) {
        log.debug("finding MsPatient instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsPatient")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsPatient merge(MsPatient detachedInstance) {
        log.debug("merging MsPatient instance");
        try {
            MsPatient result = (MsPatient) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsPatient instance) {
        log.debug("attaching dirty MsPatient instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsPatient instance) {
        log.debug("attaching clean MsPatient instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public List searchPatient(String code, String nameP, String addressP, Date tglLahir) throws InterruptedException, VONEAppException{
    	
    	List list = null;
    	
    	    	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" {mr.*} ");
    	
    	query.append(" from ");
    	query.append(" tb_medical_record mr, ");
    	query.append(" ms_patient p ");
    	
    	query.append(" where ");
    	query.append(" mr.v_mr_code like :mrCode ");
    	query.append(" and (mr.n_patient_id=p.n_patient_id) ");
    	query.append(" and p.v_patient_name like :name ");
    	query.append(" and p.v_patient_main_addr like :address ");
    	if(tglLahir != null) query.append(" and p.d_patient_dob = :tgl ");
    	query.append(" limit 100 ");
    	
    	
    	
    	SQLQuery qry = getCurrentSession().createSQLQuery(query.toString());
    			 qry.addEntity("mr",TbMedicalRecord.class);
		         qry.setString("mrCode",code);
		         qry.setString("name",nameP);
		         qry.setString("address",addressP);
		if(tglLahir != null) qry.setDate("tgl", tglLahir);
		
		list = qry.list();
		
//		list = getCurrentSession().createSQLQuery(query.toString())
//						.addEntity("mr",TbMedicalRecord.class)
//						.setString("mrCode",code)
//						.setString("name",nameP)
//						.setString("address",addressP)
//						.list();
//			
			
		return list;
			
		
    }
    
    public List searchPatient(String code, String nameP, String nik, String addressP, Date tglLahir) throws InterruptedException, VONEAppException{
    	
    	List list = null;
    	
    	    	
    	StringBuilder query = new StringBuilder();
    	
    	query.append(" select ");
    	query.append(" {mr.*} ");
    	
    	query.append(" from ");
    	query.append(" tb_medical_record mr, ");
    	query.append(" ms_patient p ");
    	
    	query.append(" where ");
    	query.append(" mr.v_mr_code like :mrCode ");
    	query.append(" and (mr.n_patient_id=p.n_patient_id) ");
    	query.append(" and p.v_patient_name like :name ");
    	query.append(" and p.nik like :ktp ");
    	query.append(" and p.v_patient_main_addr like :address ");
    	
    	if(tglLahir != null) query.append(" and p.d_patient_dob = :tgl ");
    	query.append(" limit 100 ");
    	
    	
    	
    	SQLQuery qry = getCurrentSession().createSQLQuery(query.toString());
    			 qry.addEntity("mr",TbMedicalRecord.class);
		         qry.setString("mrCode",code);
		         qry.setString("name",nameP);
		         qry.setString("ktp", nik);
		         qry.setString("address",addressP);
		if(tglLahir != null) qry.setDate("tgl", tglLahir);
		
		list = qry.list();
		
			
		return list;
			
		
    }
    
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
    
    
    public List<TbMedicalRecord> searchPatientRegistered(String code, String nameP, String addressP, Integer unitId) throws VONEAppException{
    	
        	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" distinct ");
    	query.append(" {mr.*} ");
    	
    	query.append(" from ");
    	query.append(" tb_medical_record mr, ");
    	query.append(" ms_patient p, ");
    	query.append(" tb_registration reg ");
    	
    	query.append(" where ");
    	query.append(" mr.v_mr_code like :mrCode ");
    	query.append(" and (mr.n_patient_id=p.n_patient_id) ");
    	query.append(" and p.v_patient_name like :name ");
    	query.append(" and p.v_patient_main_addr like :address ");
    	query.append(" and mr.n_mr_id=reg.n_mr_id ");
    	query.append(" and reg.reg_status=:status ");
    	query.append(" and reg.n_unit_id=:unit ");
    	query.append(" limit 100 ");
    	
    		
		SQLQuery q = getCurrentSession().createSQLQuery(query.toString());
		
			q.addEntity("mr",TbMedicalRecord.class);
			q.setString("mrCode",code);
			q.setString("name",nameP);
			q.setString("address",addressP);
			q.setInteger("status", MedisafeConstants.REG_ACTIVE);
			q.setInteger("unit", unitId);
			
			List<TbMedicalRecord> list = q.list();
		
    	return list;
    }
    
    
    public List<TbMedicalRecord> searchPatientRegistered(String code, String nameP, String addressP, Date tgl) throws VONEAppException{
    	
    	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" distinct ");
    	query.append(" {mr.*} ");
    	
    	query.append(" from ");
    	query.append(" tb_medical_record mr, ");
    	query.append(" ms_patient p, ");
    	query.append(" tb_registration reg ");
    	
    	query.append(" where ");
    	query.append(" mr.v_mr_code like :mrCode ");
    	query.append(" and (mr.n_patient_id=p.n_patient_id) ");
    	query.append(" and p.v_patient_name like :name ");
    	query.append(" and p.v_patient_main_addr like :address ");
    	if(tgl != null) query.append(" and p.d_patient_dob = :dob ");
    	query.append(" and mr.n_mr_id=reg.n_mr_id ");
    	query.append(" and reg.reg_status=:status ");
    	query.append(" limit 100 ");
    	
    		
		SQLQuery q = getCurrentSession().createSQLQuery(query.toString());
		
			q.addEntity("mr",TbMedicalRecord.class);
			q.setString("mrCode",code);
			q.setString("name",nameP);
			q.setString("address",addressP);
			if(tgl != null) q.setDate("dob", tgl);
			q.setInteger("status", MedisafeConstants.REG_ACTIVE);
			
			
			List<TbMedicalRecord> list = q.list();
		
    	return list;
    }
    
    public List<TbMedicalRecord> searchPatientRegisteredWithNik(String code, String nameP, String nik, String addressP, Date tgl) throws VONEAppException{
    	
    	
    	StringBuilder query = new StringBuilder();
    	
    	query.append(" select ");
    	query.append(" distinct ");
    	query.append(" {mr.*} ");
    	
    	query.append(" from ");
    	query.append(" tb_medical_record mr, ");
    	query.append(" ms_patient p, ");
    	query.append(" tb_registration reg ");
    	
    	query.append(" where ");
    	query.append(" mr.v_mr_code like :mrCode ");
    	query.append(" and (mr.n_patient_id=p.n_patient_id) ");
    	query.append(" and p.v_patient_name like :name ");
    	query.append(" and p.nik like :ktp ");
    	query.append(" and p.v_patient_main_addr like :address ");
    	if(tgl != null) query.append(" and p.d_patient_dob = :dob ");
    	query.append(" and mr.n_mr_id=reg.n_mr_id ");
    	query.append(" and reg.reg_status=:status ");
    	query.append(" limit 100 ");
    	
    		
		SQLQuery q = getCurrentSession().createSQLQuery(query.toString());
		
			q.addEntity("mr",TbMedicalRecord.class);
			q.setString("mrCode",code);
			q.setString("name",nameP);
			q.setString("ktp", nik);
			q.setString("address",addressP);
			if(tgl != null) q.setDate("dob", tgl);
			q.setInteger("status", MedisafeConstants.REG_ACTIVE);
			
			
			List<TbMedicalRecord> list = q.list();
		
    	return list;
    }
    
    public List<TbMedicalRecord> searchRanapPatient(String code, String nameP, String addressP) throws VONEAppException{
    	
    	List<TbMedicalRecord> list = null;
    	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" distinct ");
    	query.append(" {mr.*} ");
    	
    	query.append(" from ");
    	query.append(" tb_medical_record mr, ");
    	query.append(" ms_patient p, ");
    	query.append(" tb_registration reg ");
    	
    	query.append(" where ");
    	query.append(" mr.v_mr_code like :mrCode ");
    	query.append(" and (mr.n_patient_id=p.n_patient_id) ");
    	query.append(" and p.v_patient_name like :name ");
    	query.append(" and p.v_patient_main_addr like :address ");
    	query.append(" and mr.n_mr_id=reg.n_mr_id ");
    	query.append(" and reg.v_reg_secondary_id like :regNumber ");
    	query.append(" and reg.reg_status=:status ");
    	query.append(" limit 100 ");
    	
    	try {
    		
    		SQLQuery q = getCurrentSession().createSQLQuery(query.toString());
    				 q.addEntity("mr",TbMedicalRecord.class);
					 q.setString("mrCode",code);
					 q.setString("name",nameP);
					 q.setString("address",addressP);
					 q.setString("regNumber", "I%");
					 q.setInteger("status", MedisafeConstants.REG_ACTIVE);
			
		   
		   list = q.list();
		
		} catch (Exception e) {
			
			throw new VONEAppException(e.getMessage());
		}
    	
    	return list;
    }
    
    
    public List searchRanapPatient(String noMr, String name, String address, String ruangan, String doctor) 
    	throws VONEAppException
    {
    	List list = null;
    	
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append(" select ");
    	sql.append(" mr.v_mr_code as kode , ");
    	sql.append(" pat.v_patient_name as namaPasien , ");
    	sql.append(" pat.n_patient_type_id as tipePasien, ");
    	sql.append(" pat.v_patient_main_addr as alamat, ");
    	sql.append(" hall.v_hall_name as ruangan, ");
    	sql.append(" bed.v_bed_desc as namabed, ");
    	sql.append(" staff.v_staff_name as doctor, ");
    	sql.append(" date_part('day',now()-reg.d_registration_date) as durasi ");
    	
    	sql.append(" from ");
    	sql.append(" ms_patient pat, ");
    	sql.append(" tb_medical_record mr, ");
    	sql.append(" tb_registration reg, ");
    	sql.append(" ms_staff staff, ");
    	sql.append(" tb_bed_occupancy boc, ");
    	sql.append(" ms_bed bed, ");
    	sql.append(" ms_room room, ");
    	sql.append(" ms_hall hall ");
    	
    	sql.append(" where ");
    	sql.append(" mr.n_patient_id=pat.n_patient_id ");
    	sql.append(" and mr.n_mr_id=reg.n_mr_id ");
    	sql.append(" and reg.reg_status = :status ");
    	sql.append(" and staff.n_staff_id=reg.n_staff_id ");
    	sql.append(" and reg.n_reg_id=boc.n_reg_primary_id ");
    	sql.append(" and boc.d_check_out_time is null ");
    	sql.append(" and boc.n_bed_primary_id=bed.n_bed_id ");
    	sql.append(" and bed.n_room_id=room.n_room_id ");
    	sql.append(" and room.n_hall_id=hall.n_hall_id ");
    	sql.append(" and mr.v_mr_code like :mrCode ");
    	sql.append(" and pat.v_patient_name like :patientName ");
    	sql.append(" and pat.v_patient_main_addr like :pataddr ");
    	sql.append(" and hall.v_hall_name like :hallName ");
    	sql.append(" and staff.v_staff_name like :staffName ");
    	
    	
    	try {
    		
			SQLQuery q = getCurrentSession().createSQLQuery(sql.toString());
			q.addScalar("kode", Hibernate.STRING);
			q.addScalar("namaPasien" , Hibernate.STRING);
			q.addScalar("tipePasien", Hibernate.INTEGER);
			q.addScalar("alamat", Hibernate.STRING);
			q.addScalar("ruangan", Hibernate.STRING);
			q.addScalar("namabed", Hibernate.STRING);
			q.addScalar("doctor", Hibernate.STRING);
			q.addScalar("durasi", Hibernate.INTEGER);
			
			q.setInteger("status", MedisafeConstants.REG_ACTIVE);
			q.setString("mrCode", noMr);
			q.setString("patientName", name);
			q.setString("pataddr", address);
			q.setString("hallName", ruangan);
			q.setString("staffName", doctor);
			
			list = q.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}

    	
    	
    	return list;
    }

	@SuppressWarnings("unchecked")
	public List<TbMedicalRecord> getPatientBaseOnWard(String mrNo, String patName, String patAlamat, String unitName)
	throws VONEAppException
	{
		List<TbMedicalRecord> mrList = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select ");
		sb.append(" {mr.*} ");
		
		sb.append(" from ");
		sb.append(" ms_patient pat, ");
		sb.append(" tb_medical_record mr, ");
		sb.append(" tb_registration reg, ");
		sb.append(" tb_bed_occupancy boc, ");
		sb.append(" ms_bed bed, ");
		sb.append(" ms_room room, ");
		sb.append(" ms_hall hall, ");
		sb.append(" ms_ward bangsal ");
		
		sb.append(" where ");
		sb.append(" mr.n_patient_id=pat.n_patient_id ");
		sb.append(" and mr.n_mr_id=reg.n_mr_id ");
		sb.append(" and reg.reg_status =:status ");
		sb.append(" and reg.n_reg_id=boc.n_reg_primary_id ");
		sb.append(" and boc.d_check_out_time is null ");
		sb.append(" and boc.n_bed_primary_id=bed.n_bed_id ");
		sb.append(" and bed.n_room_id=room.n_room_id ");
		sb.append(" and room.n_hall_id=hall.n_hall_id ");
		sb.append(" and bangsal.n_ward_id=hall.n_ward_id ");
		sb.append(" and mr.v_mr_code like :mrCode ");
		sb.append(" and pat.v_patient_name like :patName ");
		sb.append("	and pat.v_patient_main_addr like :alamat ");
		sb.append(" and bangsal.v_ward_name =:namaBangsal ");
		
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("mr", TbMedicalRecord.class);
		
		query.setInteger("status", MedisafeConstants.REG_ACTIVE);
		query.setString("mrCode", mrNo);
		query.setString("patName", patName);
		query.setString("alamat", patAlamat);
		query.setString("namaBangsal", unitName);
		
		mrList = query.list();
		
		return mrList;
	}

	public List getPatientByNik(String nik) throws VONEAppException{
		List list = null;
		StringBuilder query = new StringBuilder();
    	
    	query.append(" select ");
    	query.append(" {p.*} ");
    	
    	query.append(" from ");
    	query.append(" ms_patient p ");
    	
    	query.append(" where ");
    	query.append(" p.nik = :ktp ");
    	query.append(" limit 100 ");
    	
    	
    	
    	SQLQuery qry = getCurrentSession().createSQLQuery(query.toString());
    			 qry.addEntity("p",MsPatient.class);
		         qry.setString("ktp",nik);
		
		list = qry.list();
		
			
		return list;
	}

	public void updateMr(TbMedicalRecord mr) {
		getHibernateTemplate().saveOrUpdate(mr);
	}
}
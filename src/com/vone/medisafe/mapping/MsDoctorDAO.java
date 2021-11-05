package com.vone.medisafe.mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.antrian.MsAntrian;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;



/**
 * Data access object (DAO) for domain model class MsDoctor.
 * @see com.vone.medisafe.mapping.MsDoctor
 * @author MyEclipse - Hibernate Tools
 */
public class MsDoctorDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsDoctorDAO.class);

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
	
	
	public List getAllDoctor(Class clazz){
		return super.getHibernateTemplate().find(" from "+clazz.getName());
	}
	
	public boolean saveDoctor(MsStaff staff, MsDoctor doctor, Integer unitId) throws VONEAppException{
		boolean sukses = false;
		Transaction trans = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			
			session.saveOrUpdate(staff);
			
			doctor.setMsStaff(staff);
			
			session.saveOrUpdate(doctor);
			
//			dokterId.setMsStaff(staff);
//			MsDoctor dokter = new MsDoctor();
//			dokter.setId(dokterId);
			
			MsUnit unit = new MsUnit();
			unit.setNUnitId(unitId);
			MsStaffInUnitId siui = new MsStaffInUnitId(unit, staff);
			MsStaffInUnit siu = new MsStaffInUnit(siui);
			siu.setDWhnCreate(staff.getDWhnCreate());
			
			session.saveOrUpdate(siu);
			
			trans.commit();
			sukses = true;
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return sukses;
	}
    
    public void save(MsDoctor transientInstance) {
        log.debug("saving MsDoctor instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(MsDoctor persistentInstance) {
        log.debug("deleting MsDoctor instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
       
    
    public List findByExample(MsDoctor instance) {
        log.debug("finding MsDoctor instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.MsDoctor")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public MsDoctor merge(MsDoctor detachedInstance) {
        log.debug("merging MsDoctor instance");
        try {
            MsDoctor result = (MsDoctor) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MsDoctor instance) {
        log.debug("attaching dirty MsDoctor instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(MsDoctor instance) {
        log.debug("attaching clean MsDoctor instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public Integer test() throws VONEAppException{
    	
    	StringBuffer stQuery = new StringBuffer();
    	
    	stQuery.append(" select nextval('ms_division_n_division_id_seq') as nextv");    
    	
    	Session session = getCurrentSession();
    	
    	List list = null;
    	
    	try {
			list = session.createSQLQuery(stQuery.toString())
			.addScalar("nextv", Hibernate.INTEGER)
			.list();
			
			if (list == null || list.size()== 0 || !(list.get(0) instanceof Integer))
				return null;
			
			return (Integer)list.get(0);
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	
    }
    
    public boolean deleteById(Integer id) throws VONEAppException{
    	Session session = null;
    	boolean sukses = false;
    	String hql = "delete from MsStaff where NStaffId=:divId";
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setInteger("divId", id.intValue());
			int hasil = query.executeUpdate();
			if(hasil > 0)sukses = true;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return sukses;
    }
    
    public MsDoctor getDoctorByStaff(MsStaff staff) throws VONEAppException{
    	
    	MsDoctor doctor = null;
    	Session session = null;
    	    	
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsDoctor.class);
			crit.add(Restrictions.eq("msStaff", staff));
			doctor = (MsDoctor) crit.uniqueResult(); 
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return doctor;
    }
    
    public List serarchDoctor(String code, String name, short role)throws VONEAppException{
    	List result = null;
    	Session session = null;
    	String hql = "from MsStaff where VStaffCode like :staffCode and VStaffName like :staffName and NStaffRole=:staffRole";
    	
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setString("staffCode", "%"+code+"%");
			query.setString("staffName", "%"+name+"%");
			query.setShort("staffRole", role);
			
			result = query.list();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return result;
    }
    
    
    public List<MsDoctor> getDoctorForSelect(short type) throws VONEAppException{
    	
    	String sql =" select " +
		
						"{dr.*}" +
	
					" from " +
	
						" ms_doctor dr," +
						" ms_staff staff " +
		
				    " where " +
	
				    	" dr.n_staff_id=staff.n_staff_id " +
				    	" and dr.n_msgroup_id=:grup";
    	
    		SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
    		query.addEntity("dr", MsDoctor.class);
			query.setShort("grup", type);
			
			List<MsDoctor> doctorList = query.list();
			
		
    	return doctorList;
    	
    }

	public List<MsDoctor> getDoctorBaseOnUnitId(Integer unitId) throws VONEAppException{
		
		List<MsDoctor> list = null;
		
		String sql =" select " +
		
						"distinct {dr.*}" +
					
					" from " +
					
						" ms_doctor dr," +
						" ms_staff staff, " +
						" ms_staff_in_unit siu " +
					
					" where " +
					
						" dr.n_staff_id=staff.n_staff_id " +
						" and staff.n_staff_id=siu.n_staff_id " +
						" and siu.n_unit_id=:id " +
						" and dr.n_msgroup_id=:grup "+
						" and staff.d_staff_fired_date is null";
					
		
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
			q.addEntity("dr", MsDoctor.class);
			q.setInteger("id", unitId);
			q.setShort("grup", MedisafeConstants.GRUP_DOKTER);
			
		list = q.list();
		
		return list;
	}


	
	public MsStaff getMsStaff(MsDoctor doctor) throws VONEAppException{
		Session session = getCurrentSession();
		MsDoctor dokter = (MsDoctor) session.get(MsDoctor.class, doctor.getNDoctorId());
		getHibernateTemplate().initialize(dokter.getMsStaff());
		return dokter.getMsStaff();
	}


	public List<MsDoctor> searchDocttor(String kode, String namaDokter, short group) throws VONEAppException{
		
		List<MsDoctor> list = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {dr.*} "); 
		
		sql.append(" from ");
		sql.append(" ms_doctor dr, ");
		sql.append(" ms_staff staff ");
		
		sql.append(" where ");
		sql.append(" dr.n_staff_id=staff.n_staff_id ");
		sql.append(" and staff.v_staff_code like :staffCode ");
		sql.append(" and staff.v_staff_name like :staffName");
		sql.append(" and dr.n_msgroup_id=:grup ");
		
		
		try {
			
			SQLQuery q = getCurrentSession().createSQLQuery(sql.toString());
			q.addEntity("dr", MsDoctor.class);
			
			q.setString("staffCode", kode);
			q.setString("staffName", namaDokter);
			q.setShort("grup", group);
			
			list = q.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		
		return list;
	}


	public List getAllRule() throws VONEAppException{
		return getCurrentSession().createQuery("from MsDoctorRule order by NUnitId, NShiftId, VDocStatus").list();
	}


	public MsShift getMsShiftById(Integer shiftId) throws VONEAppException{
		return (MsShift) getCurrentSession().get(MsShift.class, shiftId);
	}


	public MsUnit getMsUnitById(Integer unitId) throws VONEAppException{
		return (MsUnit) getCurrentSession().get(MsUnit.class, unitId);
	}


	public List getAllShift() throws VONEAppException{
		return getCurrentSession().createQuery("from MsShift order by NShiftId").list();
	}


	public List getAllUnit() throws VONEAppException{
		return getCurrentSession().createQuery("from MsUnit order by VUnitName").list();
	}


	public void saveMsDoctorRule(MsDoctorRule msDoctorRule) throws VONEAppException{
		getHibernateTemplate().saveOrUpdate(msDoctorRule);
	}


	public void deleteMsDoctorRule(MsDoctorRule msDoctorRule) throws VONEAppException{
		getHibernateTemplate().delete(msDoctorRule);
	}


	public MsStaff getExamStaff(String doctorExam) throws VONEAppException {
		// TODO Auto-generated method stub
		
		
		return (MsStaff)getCurrentSession().createQuery("from MsStaff where VStaffCode=:doctorExam")
									.setString("doctorExam", doctorExam).uniqueResult();
	}


	public MsAntrian getAntrian() throws VONEAppException{
		MsAntrian antrian = (MsAntrian) getCurrentSession().createQuery("from MsAntrian").uniqueResult();
		return antrian;
	}


	public boolean saveAntrian(MsAntrian antrian) {
		getHibernateTemplate().saveOrUpdate(antrian);
		return true;
	}


	public List<MsDoctor> getDoctorForAntrian(Integer flag) throws VONEAppException {
		List<MsDoctor> list = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {dr.*} "); 
		
		sql.append(" from ");
		sql.append(" ms_doctor dr");
				
		sql.append(" where ");
		sql.append(" flag_antrian = :flag");
		sql.append(" and dr.n_staff_id in(select n_staff_id from doctor_view) ");
		sql.append(" limit 1");
		
		
		try {
			
			SQLQuery q = getCurrentSession().createSQLQuery(sql.toString());
			q.addEntity("dr", MsDoctor.class);
			
			q.setInteger("flag", flag);
			
			list = q.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		
		return list;
	}
	
	public List<MsDoctor> getActiveDoctor() throws VONEAppException {
		List<MsDoctor> list = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {dr.*} "); 
		
		sql.append(" from ");
		sql.append(" ms_doctor dr");
				
		sql.append(" where ");
	    sql.append(" dr.n_staff_id in(select n_staff_id from doctor_view) ");
		
		
		
		try {
			
			SQLQuery q = getCurrentSession().createSQLQuery(sql.toString());
			q.addEntity("dr", MsDoctor.class);
			
			list = q.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		
		return list;
	}


	public void resetAntrianDoctor(List<MsDoctor> doks) {
		getHibernateTemplate().saveOrUpdateAll(doks);
		
	}


	public List<TbRegistration> getAntrianPasien(MsStaff msStaff) throws HibernateException, VONEAppException {
		List<TbRegistration> regs = null;
		StringBuffer sb = new StringBuffer();
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			
			
			Date tgl1 = sdf.parse(sdf.format(new Date()));
			Date tgl2 = new Date();
			sb.append("select {reg.*} from tb_registration reg");
			sb.append(" where reg.d_registration_date between :tgl and :tgl2");
			sb.append(" and reg.n_staff_id=:staffId ");
			sb.append(" and reg.n_transfer_reg_id is null");
			sb.append(" and reg.antrian_status is null");
			sb.append(" and reg.v_reg_secondary_id not like '%IGD%'");
			sb.append(" order by reg.d_registration_date asc");
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addEntity("reg", TbRegistration.class);
			
			query.setTimestamp("tgl", tgl1);
			query.setTimestamp("tgl2", tgl2);
			query.setInteger("staffId", msStaff.getNStaffId());
			
			
			
			regs = query.list();
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return regs;
	}
	
	public Integer getAntrianPasienDone(MsStaff msStaff) throws HibernateException, VONEAppException {
		Integer regs = 0;
		StringBuffer sb = new StringBuffer();
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			
			
			Date tgl1 = sdf.parse(sdf.format(new Date()));
			Date tgl2 = new Date();
			sb.append("select count(1) as jumlah from tb_registration reg");
			sb.append(" where reg.d_registration_date between :tgl and :tgl2");
			sb.append(" and reg.n_staff_id=:staffId ");
			sb.append(" and reg.n_transfer_reg_id is null");
			sb.append(" and reg.antrian_status='1'");
			sb.append(" and reg.v_reg_secondary_id not like '%IGD%'");
			
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addScalar("jumlah", Hibernate.INTEGER);
			//query.addEntity("reg", TbRegistration.class);
			
			query.setTimestamp("tgl", tgl1);
			query.setTimestamp("tgl2", tgl2);
			query.setInteger("staffId", msStaff.getNStaffId());
			
			
			
			regs = (Integer)query.uniqueResult();
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return regs;
	}


	public void updateRegistration(TbRegistration reg) {
		getHibernateTemplate().update(reg);
		
	}
	
   
}
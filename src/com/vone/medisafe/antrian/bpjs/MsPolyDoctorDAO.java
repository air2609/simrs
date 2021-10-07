package com.vone.medisafe.antrian.bpjs;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsStaff;

public class MsPolyDoctorDAO extends HibernateDaoSupport{
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public boolean save(MsPolyDoctor pd) throws VONEAppException{
		boolean success = false;
		
		try {
			getHibernateTemplate().saveOrUpdate(pd);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}

	public List<MsPolyDoctor> getAll(String cari) throws HibernateException, VONEAppException {
		Query q = getCurrentSession().createQuery("from MsPolyDoctor doc where doc.staff.VStaffName like :stafname");
		q.setParameter("stafname", cari);
		
		List<MsPolyDoctor> result = q.list();
		
		return result;
		
	}

	public boolean saveSchedules(List<TbDoctorSchedule> schedules) {
		boolean success = false;
		try {
			getHibernateTemplate().saveOrUpdateAll(schedules);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	public List<String> getPolyNames() throws HibernateException, VONEAppException{
		String sql = "select distinct(unit.v_unit_name) as unitName from ms_unit unit, ms_division div " + 
					 "where unit.n_division_id=div.n_division_id " + 
					 "and div.v_registration_unit='YES'";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addScalar("unitName", Hibernate.STRING);
		return query.list();
	}

	public List<TbDoctorSchedule> getSchedules(MsStaff staff, String bulan) throws HibernateException, VONEAppException {
		String hql = "FROM TbDoctorSchedule s WHERE s.staff = :doctor and s.month = :smonth";
		Query query = getCurrentSession().createQuery(hql);
		query.setParameter("doctor",staff);
		query.setParameter("smonth", bulan);
		List<TbDoctorSchedule> results = query.list();
		return results;
	}
	
	public List<MsPolyDoctor> getDoctorBookingByPoli(String poli) throws VONEAppException{
		List doctors = getCurrentSession().createCriteria(MsPolyDoctor.class)
				   .add(Restrictions.eq("bookingFlag", "Y"))
			       .add(Restrictions.like("unit", "%"+poli+"%"))
			       .list();
		
		return doctors;
		
	}

	public List<MsPolyDoctor> getDoctorByPoli(String poli) throws VONEAppException {
//		List doctors = getCurrentSession().createCriteria(MsPolyDoctor.class)
//				       .add(Restrictions.like("unit", "%"+poli+"%"))
//				       .list();
		
		String sql = "select {doc.*} from ms_poly_doctor doc, tb_doctor_schedules s " + 
				     "where doc.doctor_id=s.doctor_id and doc.booking_flag <> 'Y' and doc.unit like :polyname " + 
				     "and s.schedule=:tgl and (select current_time) between " +
				     "doc.schedule_from and doc.schedule_to";
		
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addEntity("doc", MsPolyDoctor.class);
		query.setString("polyname", "%"+poli+"%");
		query.setDate("tgl", new Date());
		
		List doctors = query.list();
		return doctors;
	}

	public MsPolyDoctor getByStaffId(Integer staffId) throws VONEAppException {
		String sql = "select {doc.*} from ms_poly_doctor doc where doc.doctor_id=:docId";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addEntity("doc", MsPolyDoctor.class);
		query.setInteger("docId", staffId);
		
		
		MsPolyDoctor doctor = (MsPolyDoctor)query.uniqueResult();
		return doctor;
		
	}

	public Integer getAntrianNumber(Integer staffId, String tgl) throws VONEAppException {
		String sql = "select count(1) as jumlah from tb_antrian where doctor_id=:doctor and queue_date=:tanggal";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addScalar("jumlah", Hibernate.INTEGER);
		query.setInteger("doctor", staffId);
		query.setString("tanggal", tgl);
		
		return (Integer)query.uniqueResult();
	}

	public boolean saveQueue(TbAntrian antrian) throws VONEAppException{
		boolean success = false;
		try {
//			Integer nomorAntrian = this.getAntrianNumber(antrian.getStaff().getNStaffId(), antrian.getQueueDate());
//			antrian.setQueueNo(nomorAntrian+1);
			getHibernateTemplate().saveOrUpdate(antrian);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	public List<TbDoctorSchedule> getDoctorTodaySchedule(Integer staffId, Date today) throws VONEAppException {
		String sql = "select {sch.*} from tb_doctor_schedules sch where doctor_id=:id and schedule = :tgl order by schedule asc";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addEntity("sch", TbDoctorSchedule.class);
		query.setInteger("id", staffId);
		query.setDate("tgl", today);
		
		return query.list();
	}

	public List<TbDoctorSchedule> getDoctorAllNextSchedule(Integer staffId, Date today) throws VONEAppException {
		String sql = "select {sch.*} from tb_doctor_schedules sch where doctor_id=:id and schedule > :tgl order by schedule asc";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addEntity("sch", TbDoctorSchedule.class);
		query.setInteger("id", staffId);
		query.setDate("tgl", today);
		
		return query.list();
	}

	public List<TbAntrian> getTodayQueue(String format, String doctorName) throws VONEAppException{
//		List antrians = getCurrentSession().createCriteria(TbAntrian.class)
//				        .add(Restrictions.eq("queueDate", format))
//				        .add(Restrictions.like("staff.VStaffName", doctorName))
//				        .list();
		String hql = "FROM TbAntrian s WHERE s.staff.VStaffName like :doctor and s.queueDate = :smonth and s.status = :stat order by s.staff, s.queueNo asc";
		Query q = getCurrentSession().createQuery(hql);
		q.setParameter("doctor", doctorName);
		q.setParameter("smonth", format);
		q.setParameter("stat", 0);
		return q.list();
	}

	public TbAntrian getQueueInServices(Integer bpjsCounter, String format) throws VONEAppException {
		String hql = "FROM TbAntrian s WHERE s.queueDate=:tgl and s.counterNo=:coun and s.status=1";
		Query q = getCurrentSession().createQuery(hql);
		q.setParameter("tgl", format);
		q.setParameter("coun", bpjsCounter);
		return (TbAntrian)q.uniqueResult();
	}

	public List<TbAntrian> getQueInProcess(String format) throws VONEAppException{
		String hql = "FROM TbAntrian s WHERE s.queueDate=:tgl and s.status=1";
		Query q = getCurrentSession().createQuery(hql);
		q.setParameter("tgl", format);
		return q.list();
	}

	public void deletePolyDoctor(MsPolyDoctor value) throws VONEAppException {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(value);
	}

	public void deleteSchedule(MsStaff staff, Date tgl) throws HibernateException, VONEAppException {
		Query q = getCurrentSession().createQuery("delete from TbDoctorSchedule where staff=:stf and schedule=:sch");
		q.setParameter("stf", staff);
		q.setParameter("sch", tgl);
		
		q.executeUpdate();
		
	}

}

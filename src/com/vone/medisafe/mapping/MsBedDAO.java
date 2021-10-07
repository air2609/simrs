package com.vone.medisafe.mapping;

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
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;



/**
 * Data access object (DAO) for domain model class MsBed.
 * @see com.vone.medisafe.mapping.MsBed
 * @author MyEclipse - Hibernate Tools
 */
public class MsBedDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsBedDAO.class);

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
	
	
	public List getAllBed()throws VONEAppException{
		//return super.getHibernateTemplate().find(" from "+MsBed.class.getName());
		return getCurrentSession().createQuery("from MsBed order by VBedCode").list();
	}
    
    public void save(MsBed transientInstance) throws VONEAppException{
        log.debug("saving MsBed instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
	public void delete(MsBed persistentInstance) throws VONEAppException{
        log.debug("deleting MsBed instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public MsBed findById( java.lang.Integer id) throws VONEAppException{
        log.debug("getting MsBed instance with id: " + id);
        try {
        	Session session = getCurrentSession();
            MsBed instance = (MsBed) session
                    .get("com.vone.medisafe.mapping.MsBed", id);
            instance.getMsRoom();
            return instance;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    
    public List findByExample(MsBed instance) throws VONEAppException{
        log.debug("finding MsBed instance by example");
        try {
            List results = getCurrentSession()
                    .createCriteria("com.vone.medisafe.mapping.MsBed")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }    
    
    public MsBed merge(MsBed detachedInstance) throws VONEAppException{
        log.debug("merging MsBed instance");
        try {
            MsBed result = (MsBed) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }

    public void attachDirty(MsBed instance) throws VONEAppException{
        log.debug("attaching dirty MsBed instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public void attachClean(MsBed instance) throws VONEAppException{
        log.debug("attaching clean MsBed instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    
    public MsBed getBedbyCode(String code) throws VONEAppException{
    	
    	String query = "select {bed.*} from ms_bed bed where bed.v_bed_desc=:bedCode and bed.v_bed_active_status=:status";
    
		MsBed	bed = (MsBed)getCurrentSession().createSQLQuery(query)
						.addEntity("bed",MsBed.class)
						.setString("bedCode",code)
						.setString("status", "A")
						.uniqueResult();
		
    	
    	return bed;
    }
    public List getBedByRoom(MsRoom room)throws VONEAppException{
    	Session session = null;
    	List list = null;
    	try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsBed.class);
			crit.add(Restrictions.eq("msRoom",room));
			crit.add(Restrictions.eq("VBedActiveStatus", "A"));
			list = crit.list();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return list;
    }
    
    public boolean deleteById(Integer id)throws VONEAppException{
    	boolean sukses = false;
    	Session session = null;
    	String hql = "delete from MsBed where NBedId=:tclassId";
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setInteger("tclassId", id.intValue());
			int hasil = query.executeUpdate();
			if(hasil > 0)sukses = true;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return sukses;
    }
    
    public boolean isBedExist(MsBed bed)throws VONEAppException{
    	boolean success = false;
    	Session session = null;
    	String hql = "from MsBed where n_room_id=:roomId and n_tclass_id=:tclass and v_bed_code=:bedCode";
    	try {
			session = getCurrentSession();
			Query query = session.createQuery(hql);
			query.setInteger("roomId", bed.getMsRoom().getNRoomId().intValue());
			query.setInteger("tclass", bed.getMsTreatmentClass().getNTclassId().intValue());
			query.setString("bedCode", bed.getVBedCode());
			
			if(query.list().size() > 0)success = true;
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return success;
    }


	public MsBed getBedById(Integer integer) throws VONEAppException{
		
		return (MsBed) getCurrentSession().get(MsBed.class, integer);
	}


	public List<MsBed> searchBeds(String textCari) throws VONEAppException{
		
		List<MsBed> list = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {bed.*} ");
		
		sql.append(" from ");
		sql.append(" ms_bed bed, ");
		sql.append(" ms_treatment_class tclass, ");
		sql.append(" ms_room room, ");
		sql.append(" ms_hall hall ");
		
		sql.append(" where ");
		sql.append(" bed.n_tclass_id=tclass.n_tclass_id ");
		sql.append(" and bed.n_room_id=room.n_room_id ");
		sql.append(" and room.n_hall_id=hall.n_hall_id ");
		sql.append(" and (tclass.v_tclass_desc like :kelasTarif ");
		sql.append(" or bed.v_bed_code like :bedCode ");
		sql.append(" or bed.v_bed_desc like :bedDesc ");
		sql.append(" or room.v_room_code like :roomCode ");
		sql.append(" or hall.v_hall_name like :hallName )");
		
		
		 
		
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			
			query.addEntity("bed", MsBed.class);
			query.setString("kelasTarif", textCari);
			query.setString("bedCode", textCari);
			query.setString("bedDesc", textCari);
			query.setString("roomCode", textCari);
			query.setString("hallName", textCari);
			
			list = query.list();
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return list;
	}


	public List<MsTreatmentClass> getBedByClass() throws VONEAppException {
		String hql = "from MsTreatmentClass";
		Session session = null;
		
		session = getCurrentSession();
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	
	public List getTotalBedByClass(){
		StringBuffer sb = new StringBuffer();
		sb.append("select count(c.*) as jumlah , v_tclass_desc ");
		sb.append(" from ms_treatment_class c, ms_bed b ");
		sb.append(" where c.n_tclass_id=b.n_tclass_id and v_bed_active_status='A' ");
		sb.append( " group by v_tclass_desc order by v_tclass_desc asc ");
		
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			
			query.addScalar("jumlah", Hibernate.INTEGER);
			query.addScalar("v_tclass_desc", Hibernate.STRING);
			
			return query.list();
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public List getDuplicateBed() {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		try{
			session = super.getSessionFactory().getCurrentSession();
			sb.append("select count(1) as jumlah, v_bed_desc as bed from ms_bed ");
			sb.append(" where v_bed_active_status='A' group by v_bed_desc having count(1) > 1 ");
			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.addScalar("jumlah", Hibernate.INTEGER);
			query.addScalar("bed", Hibernate.STRING);
			
			return query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public List<MsBed> getActiveBed() throws VONEAppException {
		String hql = "from MsBed where VBedActiveStatus=:active order by msTreatmentClass";
		
		Session session = getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("active", MedisafeConstants.BED_ACTIVE);
		
		return query.list();
	}


	public void saveBulk(List<MsBed> beds) {
		getHibernateTemplate().saveOrUpdateAll(beds);
		
	}
	
	public List getBorInfo(Date startDate, Date endDate) {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		try{
			session = super.getSessionFactory().getCurrentSession();
			
			sb.append("select q.kelas,q.ruangan, q.total, q.terpakai, ");
			sb.append("cast(terpakai as float4) / cast(total as float4) as bor ");
			sb.append("from (select c.v_tclass_desc as kelas, h.v_hall_name as ruangan, ");
			sb.append("hb.tclass_id, hb.hall_id, sum(hb.quantity) as total, ");
			sb.append("(select count(1) from tb_bor where tclass_id=hb.tclass_id and hall_id=hb.hall_id and bed_date ");
			sb.append("between :borStart and :borEnd) as terpakai from tb_hall_bed hb join ms_treatment_class c ");
			sb.append("on c.n_tclass_id=hb.tclass_id join ms_hall h on h.n_hall_id=hb.hall_id ");
			sb.append("where hb.record_date between :recStart and :recEnd ");
			sb.append("group by kelas, ruangan, hb.tclass_id, hb.hall_id) q ");

			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.setDate("borStart", startDate);
			query.setDate("borEnd", endDate);
			query.setDate("recStart", startDate);
			query.setDate("recEnd", endDate);
			
			query.addScalar("kelas", Hibernate.STRING);
			query.addScalar("ruangan", Hibernate.STRING);
			query.addScalar("total", Hibernate.INTEGER);
			query.addScalar("terpakai", Hibernate.INTEGER);
			query.addScalar("bor",Hibernate.DOUBLE);
			
			return query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public List getBedInfo() {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		try{
			session = super.getSessionFactory().getCurrentSession();
			sb.append("select t.v_tclass_desc as kelas,  h.v_hall_name as ruangan, count(1) as jml ");
			sb.append("from ms_bed b, ms_treatment_class t, ms_room r, ms_hall h ");
			sb.append("where t.n_tclass_id=b.n_tclass_id and r.n_room_id=b.n_room_id ");
			sb.append("and h.n_hall_id=r.n_hall_id and b.v_bed_active_status='A' and b.is_shown='Y' ");
			sb.append("group by kelas, ruangan order by kelas");
			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.addScalar("kelas", Hibernate.STRING);
			query.addScalar("ruangan", Hibernate.STRING);
			query.addScalar("jml", Hibernate.INTEGER);
			
			return query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getBedTerisi(String kelas, String ruangan) {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		try{
			session = super.getSessionFactory().getCurrentSession();
			sb.append("select count(1) as jml ");
			sb.append("from ms_bed b, ms_treatment_class t, ms_room r, ms_hall h ");
			sb.append("where t.n_tclass_id=b.n_tclass_id and r.n_room_id=b.n_room_id ");
			sb.append("and h.n_hall_id=r.n_hall_id and b.v_bed_active_status='A' and b.is_shown='Y' and b.v_bed_status='1' ");
			sb.append("and t.v_tclass_desc=:tclass and  h.v_hall_name=:hall");
			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.setString("tclass", kelas);
			query.setString("hall", ruangan);
			
			query.addScalar("jml", Hibernate.INTEGER);
			return (Integer)query.uniqueResult();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getBedBooked(String kelas, String ruangan) {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		try{
			session = super.getSessionFactory().getCurrentSession();
			sb.append("select count(1) as jml ");
			sb.append("from ms_bed b, ms_treatment_class t, ms_room r, ms_hall h ");
			sb.append("where t.n_tclass_id=b.n_tclass_id and r.n_room_id=b.n_room_id ");
			sb.append("and h.n_hall_id=r.n_hall_id and b.v_bed_active_status='A' and b.is_shown='Y' and b.available_status='B' ");
			sb.append("and t.v_tclass_desc=:tclass and  h.v_hall_name=:hall");
			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.setString("tclass", kelas);
			query.setString("hall", ruangan);
			
			query.addScalar("jml", Hibernate.INTEGER);
			return (Integer)query.uniqueResult();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getBedInservice(String kelas, String ruangan) {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		try{
			session = super.getSessionFactory().getCurrentSession();
			sb.append("select count(1) as jml ");
			sb.append("from ms_bed b, ms_treatment_class t, ms_room r, ms_hall h ");
			sb.append("where t.n_tclass_id=b.n_tclass_id and r.n_room_id=b.n_room_id ");
			sb.append("and h.n_hall_id=r.n_hall_id and b.v_bed_active_status='A' and b.is_shown='Y' and b.available_status='C' ");
			sb.append("and t.v_tclass_desc=:tclass and  h.v_hall_name=:hall");
			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.setString("tclass", kelas);
			query.setString("hall", ruangan);
			
			query.addScalar("jml", Hibernate.INTEGER);
			return (Integer)query.uniqueResult();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
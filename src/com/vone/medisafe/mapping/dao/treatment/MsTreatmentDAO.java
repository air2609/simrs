package com.vone.medisafe.mapping.dao.treatment;

import java.util.Date;
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
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.mapping.MsUnit;



/**
 * Data access object (DAO) for domain model class MsTreatment.
 * @see com.vone.medisafe.mapping.MsTreatment
 * @author MyEclipse - Hibernate Tools
 */
public class MsTreatmentDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MsTreatmentDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public List getTreatments() throws VONEAppException{
		Session session = getCurrentSession();
		return session.createQuery("from MsTreatmentFee order by NTreatmentFeeId").list();
		//return super.getHibernateTemplate().find(" from "+ MsTreatmentFee.class.getName());
	}
    
    public boolean save(MsTreatment treatment, MsTreatmentFee tfee) throws VONEAppException {
        Session session = null;
        boolean success = false;
        Transaction trans = null;
        
        try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			
			session.saveOrUpdate(treatment);
			
			tfee.setMsTreatment(treatment);
			
			session.saveOrUpdate(tfee);
			
			trans.commit();
			success=true;
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
         
        return success;
    }
    
	public boolean delete(MsTreatmentFee persistentInstance) throws VONEAppException {
        boolean success = false;
        try {
        	Session session = getCurrentSession();
        	session.delete(persistentInstance);
            success = true;
            log.debug("delete successful");
        } catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
        }
        return success;
    }
    
    public MsTreatment findById( java.lang.Integer id)throws VONEAppException  {
        log.debug("getting MsTreatment instance with id: " + id);
        try {
        	Session session = getCurrentSession();
            MsTreatment instance = (MsTreatment)session
                    .get("com.vone.medisafe.mapping.MsTreatment", id);
            return instance;
        } catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
        }
    }
    
    
    public List findByExample(MsTreatment instance) throws VONEAppException {
        Session session = null;
        try {
        	session = getCurrentSession();
            List results = session
                    .createCriteria(MsTreatment.class)
                    .add(Restrictions.like("VTreatmentCode", instance.getVTreatmentCode()))
                    .add(Restrictions.like("VTreatmentName", instance.getVTreatmentName()))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
        }
    }    
    
    public MsTreatment getTreatmentByCode(String code) throws VONEAppException  {
        MsTreatment treatment = null;
        Session session = null;
        
        try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsTreatment.class);
			crit.add(Restrictions.eq("VTreatmentCode", code));
			List list = crit.list();
			if(list.size() > 0)
				treatment = (MsTreatment)list.get(0);
		}catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
        return treatment;
    }

    public void attachDirty(MsTreatment instance) throws VONEAppException {
        log.debug("attaching dirty MsTreatment instance");
        try {
        	Session session = getCurrentSession();
        	session.saveOrUpdate(instance);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public void attachClean(MsTreatment instance) throws VONEAppException {
        log.debug("attaching clean MsTreatment instance");
        try {
        	Session session = getCurrentSession();
        	session.lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
    }
    
    public List<MsTreatmentFee> getTreatmentByUnit(MsUnit unit, String tclass, String isPacket)
    	throws VONEAppException {
    	
    	Session session = null;
    	List<MsTreatmentFee> result = null;
    	
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append(" select ");
    	sql.append(" {tfee.*} ");
    	
    	
    	sql.append(" from ");
    	sql.append(" ms_treatment_fee tfee, ");
    	sql.append(" ms_treatment treat, ");
    	sql.append(" ms_treatment_class tclass, ");
    	sql.append(" ms_treatment_group grup ");
    	
    	sql.append(" where ");
    	sql.append(" tfee.n_treatment_id=treat.n_treatment_id ");
    	sql.append(" and tfee.n_tclass_id=tclass.n_tclass_id ");
    	sql.append(" and tclass.v_tclass_desc=:tdesc "); 
    	sql.append(" and treat.n_tgroup_id=grup.n_tgroup_id ");
    	sql.append(" and grup.v_tgroup_name=:paket ");
    	sql.append(" limit 100 ");
    	
    	try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("tfee", MsTreatmentFee.class);
//			query.setInteger("unitId", unit.getNUnitId().intValue());
			query.setString("tdesc", tclass);
			query.setString("paket", isPacket);
			result = query.list();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
    	
    	return result;
    }
    
    
    
    public List<MsTreatmentFee> getSearchTreatmentByUnit(Integer unitId, String code, String name, String tclass, String isPacket) 
    	throws VONEAppException {
    	Session session = null;
    	List<MsTreatmentFee> result = null;
    	
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select ");
    	sql.append(" {tfee.*} ");
    	
    	sql.append(" from ");
    	sql.append(" ms_treatment_fee tfee, ");
    	sql.append(" ms_treatment treat, ");
    	sql.append(" ms_treatment_class tclass, ");
    	sql.append(" ms_treatment_group grup ");
    	
    	sql.append(" where ");
    	sql.append(" tfee.n_treatment_id=treat.n_treatment_id ");
   		sql.append(" and tfee.n_trtfee_fee > 0 ");
    	sql.append(" and treat.v_treatment_code like :tcode ");
    	sql.append(" and treat.v_treatment_name like :tname ");
    	sql.append(" and tfee.n_tclass_id=tclass.n_tclass_id ");
    	sql.append(" and tclass.v_tclass_desc=:tdesc ");
    	sql.append(" and treat.n_tgroup_id=grup.n_tgroup_id ");
    	sql.append(" and grup.v_tgroup_name=:paket ");
    	sql.append(" limit 100 ");
    	
    	try {
    		
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("tfee", MsTreatmentFee.class);
//			query.setInteger("unitId", unitId.intValue());
			query.setString("tcode", code);
			query.setString("tname", name);
			query.setString("tdesc", tclass);
			query.setString("paket", isPacket);
			
			result = query.list();
			
		}catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
    	
    	return result;
    }
    //kun
    public List getSearchTreatmentByTreatmentUnit(Integer unitId, String code, String name, String tclass)throws VONEAppException {
    	Session session = null;
    	List result = null;
    	
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select ");
    	sql.append(" {tfee.*} ");
    	
    	sql.append(" from ");
    	sql.append(" ms_treatment_fee tfee, ");
    	sql.append(" ms_treatment treat, ");
    	sql.append(" ms_treatment_class tclass, ");
    	sql.append(" ms_treatment_group grup ");
    	
    	sql.append(" where ");
    	sql.append(" tfee.n_treatment_id=treat.n_treatment_id ");
    	sql.append(" and treat.v_treatment_code like :tcode ");
    	sql.append(" and treat.v_treatment_name like :tname ");
    	sql.append(" and tfee.n_tclass_id=tclass.n_tclass_id ");
    	sql.append(" and tclass.v_tclass_desc=:tdesc ");
    	sql.append(" and treat.n_tgroup_id=grup.n_tgroup_id ");
    	
    	try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("tfee", MsTreatmentFee.class);
//			query.setInteger("unitId", unitId.intValue());
			query.setString("tcode", code);
			query.setString("tname", name);
			query.setString("tdesc", tclass);
			result = query.list();
		}catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
    	
    	return result;
    }
    //kun
    public List getTreatmentByTreatmentGroup(String code, String kelasTarif) throws VONEAppException {
    	Session session = null;
    	List result = null;
    	try{
    		StringBuffer stQuery = new StringBuffer();
    		stQuery.append(" select {tfee.*} ")
    			.append(" from ms_treatment_fee tfee, ms_treatment tr, ms_treatment_class tclass, ms_treatment_group trg ")
    			.append(" where tr.n_tgroup_id = trg.n_tgroup_id ")
    			.append(" and tfee.n_treatment_id = tr.n_treatment_id")
    			.append(" and tfee.n_tclass_id = tclass.n_tclass_id ")
    			.append(" and tfee.n_trtfee_fee > 0 ")
    			.append(" and trg.v_tgroup_code = :code ")
    			.append(" and tclass.v_tclass_desc = :tdesc ")
    			.append(" order by tr.v_treatment_code");
    		
    		session = getCurrentSession();
    		result = session.createSQLQuery(stQuery.toString())
    					.addEntity("tfee", MsTreatmentFee.class)
    					.setString("code", code)
    					.setString("tdesc", kelasTarif)
    					.list();
    	}catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
    	}
    	
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

	public MsTreatmentGroup reloadTreatmentGroup(MsTreatmentGroup msTreatmentGroup) throws VONEAppException{
		return (MsTreatmentGroup) getCurrentSession().get(MsTreatmentGroup.class, msTreatmentGroup.getNTgroupId());
	}

	public List<MsTreatmentFee> searchTreatement(String tcode) 
		throws VONEAppException{
		
		List<MsTreatmentFee> list = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select ");
		sb.append(" {fee.*} ");
		
		sb.append(" from ");
		sb.append(" ms_treatment_fee fee, ");
		sb.append("	ms_treatment treat, ");
//		sb.append(" ms_unit unit, ");
		sb.append(" ms_treatment_class tclass ");
		
		sb.append(" where ");
		sb.append(" fee.n_treatment_id=treat.n_treatment_id ");
		sb.append(" and fee.n_tclass_id=tclass.n_tclass_id ");
//		sb.append(" and treat.n_unit_id=unit.n_unit_id ");
		sb.append(" and (treat.v_treatment_code like :tcode ");
		sb.append(" or treat.v_treatment_name like :tname ");
//		sb.append("	and unit.v_unit_name like :unitname ");
		sb.append("	or tclass.v_tclass_desc like :tclass )");
		sb.append(" limit 100 ");
		
		
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			
			query.addEntity("fee", MsTreatmentFee.class);
			query.setString("tcode", tcode);
			query.setString("tname", tcode);
//			query.setString("unitname", unitname);
			query.setString("tclass", tcode);
			
			list = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return list;
	}
	
	
	public MsTreatmentFee getTreatmentFeeById( java.lang.Integer id)throws VONEAppException  {
        log.debug("getting MsTreatment instance with id: " + id);
        try {
        	Session session = getCurrentSession();
        	MsTreatmentFee instance = (MsTreatmentFee)session
                    .get("com.vone.medisafe.mapping.MsTreatmentFee", id);
            return instance;
        } catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
        }
    }

	public List<MsTreatmentFee> getAllTreatmentFee() throws HibernateException, VONEAppException {
		StringBuffer sb = new StringBuffer();
		List<MsTreatmentFee> fees = null;
		sb.append("select {fee.*} from ms_treatment_fee fee, ms_treatment trt");
		sb.append(" where fee.n_treatment_id=trt.n_treatment_id");
		sb.append(" and fee.n_coa is not null");
		sb.append(" and trt.v_treatment_name not in ('-','0') ");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("fee", MsTreatmentFee.class);
		
		fees = query.list();
		
		return fees;
	}

	public MsTreatmentFee getTreatmentFee(String kode, String klsTarif) throws HibernateException, VONEAppException {
		MsTreatmentFee fee = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select {fee.*} from ms_treatment_fee fee, ms_treatment trt, ms_treatment_class cl");
		sb.append(" where fee.n_treatment_id=trt.n_treatment_id and fee.n_tclass_id=cl.n_tclass_id ");
		sb.append(" and trt.v_treatment_code=:tcode and cl.v_tclass_desc=:tclass");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("fee", MsTreatmentFee.class);
		query.setString("tcode", kode);
		query.setString("tclass",klsTarif);
		
		fee = (MsTreatmentFee)query.uniqueResult();
		return fee;
	}
	
	public MsTreatmentClass getClassTarif(String klsTarif) throws HibernateException, VONEAppException {
		MsTreatmentClass fee = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select {cl.*} from ms_treatment_class cl");
		sb.append(" where ");
		sb.append(" cl.v_tclass_desc=:tclass");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("cl", MsTreatmentClass.class);
		query.setString("tclass",klsTarif);
		
		fee = (MsTreatmentClass)query.uniqueResult();
		return fee;
	}

	public MsCoa getMsCoa(String account) throws HibernateException, VONEAppException {
		MsCoa coa = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select {coa.*} from ms_coa coa where v_acct_no=:acc");
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("coa", MsCoa.class);
		query.setString("acc", account);
		
		coa = (MsCoa) query.uniqueResult();
		return coa;
	}

	public boolean updateFees(List<MsTreatmentFee> fees) {
		getHibernateTemplate().saveOrUpdateAll(fees);
		return true;
	}
	
	
	public List<Object> getTreatmentReport(Date from, Date to, String tipePasien) throws HibernateException, VONEAppException{
		/**String sql = "select t.v_treatment_code as code, t.v_treatment_name as name, sum(trx.n_qty) as qty, sum(trx.n_amount_after_disc) as amount "
				   + "from tb_treatment_trx trx, tb_examination nota, ms_treatment_fee fee, ms_treatment t "
				   + "where trx.d_whn_create between :dfrom and :dto and nota.n_exam_id=trx.n_note_id and nota.n_exam_status=2 "
				   + "and fee.n_treatment_fee_id=trx.n_treatment_fee_id and t.n_treatment_id=fee.n_treatment_id "
				   + "group by t.v_treatment_code, t.v_treatment_name order by t.v_treatment_name "; */
		
		
		String sql = "select kode as code,nama_tindakan as name, count(1) as qty, sum(jasa_dokter) as amount " 
				    +"from report.get_rekap_tindakan(:dfrom,:dto)  ";
					if(!tipePasien.equals("ALL")) {
						sql = sql + "where tipe_pasien=:tipe ";
					} 
	   sql = sql + "group by kode, nama_tindakan order by nama_tindakan";

		
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addScalar("code", Hibernate.STRING);
		query.addScalar("name", Hibernate.STRING);
		query.addScalar("qty", Hibernate.BIG_DECIMAL);
		query.addScalar("amount", Hibernate.BIG_DECIMAL);
		
		query.setDate("dfrom", from);
		query.setDate("dto", to);
		
		if(!tipePasien.equals("ALL")) query.setString("tipe", tipePasien);
		
		return query.list();
	}
	
	

}
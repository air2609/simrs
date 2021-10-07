package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.List;




import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;

public class TbBedOccupancyDAO extends HibernateDaoSupport {

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
	  
	
	

	
	public TbBedOccupancy getBedOccupanyByRegId(Integer id) throws VONEAppException {

		TbBedOccupancy boc = null;
		
		StringBuffer query = new StringBuffer();
		
		query.append(" select " );
		query.append(" {boc.*} ");
		
		query.append(" from " );
		query.append(" tb_bed_occupancy boc " );
		
		query.append(" where ");
		query.append(" boc.n_reg_primary_id=:regId ");
		
		query.append(" order by ");
		query.append(" boc.d_check_in_time desc limit 1");
		
		try {
			
			boc = (TbBedOccupancy)getCurrentSession().createSQLQuery(query.toString())
			
					.addEntity("boc",TbBedOccupancy.class)
					.setInteger("regId",id.intValue())
					.uniqueResult();
			
		} catch (Exception e){
			
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
			 
		}
		return boc;
	}

	
	
	public TbBedOccupancy getBedOccupanciesByBed(MsBed bed) throws VONEAppException{
		
		
		String sql = " select " +
				
						" {boc.*} " +
						
					" from " +
					
						" tb_bed_occupancy boc " +
					
					" where " +
					
						" boc.n_bed_primary_id=:bedId " +
						" and boc.d_check_out_time is null order by boc.d_whn_create desc limit 1";
		
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		
		q.addEntity("boc", TbBedOccupancy.class);
		q.setInteger("bedId", bed.getNBedId());
		
		TbBedOccupancy list = (TbBedOccupancy)q.uniqueResult();
		
		return list;
		
	}
		
		

	public boolean updateBocMove(TbRegistration reg, MsBed bed,TbBedOccupancy bocWillBeUpdated,
			String username) throws VONEAppException {
		boolean sukses = true;
		Session session = null;
		try {
			
			session = getCurrentSession();
			Date tglTransaksi = new Date();
			//update boc lama
			
			bocWillBeUpdated.setDCheckOutTime(tglTransaksi);
			bocWillBeUpdated.setVOutNote("P");
			session.update(bocWillBeUpdated);
			
			//update status bed lama menjadi available
			MsBed bedLama = bocWillBeUpdated.getId().getMsBed();
			bedLama.setVBedStatus("0");
			session.update(bedLama);
			
			//buat boc baru
			TbBedOccupancy bocBaru = new TbBedOccupancy();
			TbBedOccupancyId id = new TbBedOccupancyId(reg,bed,tglTransaksi);
			bocBaru.setId(id);
			bocBaru.setVOutNote("P");
			bocBaru.setDCheckInTime(tglTransaksi);
			bocBaru.setVWhoCreate(username);
			
			session.save(bocBaru);
			
			//update status bed yg baru menjadi terpakai
			bed.setVBedStatus("1");
			session.update(bed);
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return sukses;
	}
	
	public List<TbBedOccupancy> getHistoryMove(TbRegistration reg) throws VONEAppException {
		
		List<TbBedOccupancy> list = null;
		
		String query = "select {boc.*} from tb_bed_occupancy boc where boc.n_reg_primary_id=:regId order by boc.d_whn_create";
		try {
			
			SQLQuery q = getCurrentSession().createSQLQuery(query);
					 q.addEntity("boc",TbBedOccupancy.class);
					 q.setInteger("regId",reg.getNRegId().intValue());
			list = q.list();
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return list;
	}
	
	public TbBedOccupancy getByCreateDate(String date) throws VONEAppException {
		Session session = null;
		TbBedOccupancy boc = null;
		
		String query = "select {boc.*} from tb_bed_occupancy boc where boc.d_whn_create=:createDate";
		
		try {
			session = getCurrentSession();
			boc = (TbBedOccupancy)session.createSQLQuery(query)
					.addEntity("boc",TbBedOccupancy.class)
					.setString("createDate",date)
					.uniqueResult();
			
			if(boc == null)
				Messagebox.show("boc null");
					
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		
		return boc;
	}
	
	
	public boolean updateById(MsBed bed, MsBed bedLama, TbBedOccupancy boc) throws VONEAppException {
		boolean sukses = true;
		Session session = null;
		try {
			session = getCurrentSession();
			
			bedLama.setVBedStatus("0");
			session.update(bedLama);
			
			TbBedOccupancyId idLama = boc.getId();
			TbBedOccupancyId idBaru = boc.getId();
			
			idBaru.setMsBed(bed);
			String hql = "update TbBedOccupancy set n_bed_primary_id=:idBaru where d_whn_create=:idLama";
			Query query = session.createQuery(hql);
			
			query.setInteger("idBaru",bed.getNBedId().intValue());
			query.setString("idLama",idLama.getDWhnCreate().toString());
			query.executeUpdate();
						
			bed.setVBedStatus("1");
			session.update(bed);
			
			
		} catch (HibernateException e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return sukses;
	}
}

package com.vone.medisafe.mapping.dao;

import java.util.List;


import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbRoomReservation;

public class AntrianKamarDAO extends HibernateDaoSupport{
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	
	public List<TbRoomReservation> getAntrianList() throws VONEAppException{
		
		Query q = getCurrentSession().createQuery("from TbRoomReservation");
			
		List<TbRoomReservation> result = q.list();
		
		return result;
	}
	
	
	public boolean save(TbRoomReservation antrianKamar) throws VONEAppException{
		boolean success = false;
		
		try {
			getHibernateTemplate().save(antrianKamar);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	
	
	public boolean delete(TbRoomReservation antrianKamar) throws VONEAppException
	{
		boolean success = false;
		
		 try {
			getHibernateTemplate().delete(antrianKamar);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	
	public List<TbRoomReservation> getAntrianBaseOnHallId(Integer id) throws VONEAppException{
		List<TbRoomReservation> hasil = null;
		
		
		String sql = "select " +
						"{antrian.*} " +
					"from " +
						"tb_room_reservation antrian " +
					"where " +
						"antrian.n_hall_id=:hallId";
		
		
		
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addEntity("antrian", TbRoomReservation.class);
			query.setInteger("hallId", id.intValue());
			
			hasil = query.list();
			
		
		
		return hasil;
	}
	
	public int getAntrianBaseOnRegistrationId(Integer id) throws VONEAppException{
		
		int hasil = 0;
	
		
		String sql = "select " +
						"{antrian.*} " +
					"from " +
						"tb_room_reservation antrian " +
					"where " +
						"antrian.n_reg_id=:hallId";
		
		
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			
			query.addEntity("antrian", TbRoomReservation.class);
			query.setInteger("hallId", id.intValue());
			
			hasil = query.list().size();
			
		
		
		return hasil;
	}


	public TbRoomReservation getAntrianById(Integer roomRsvId)  throws VONEAppException{
		
		String sql = "select " +
						"{antrian.*} " +
					 "from " +
					 	"tb_room_reservation antrian " +
					 "where " +
					 	"antrian.n_room_rsv_id=:antrianId";
		
		try{
			
			SQLQuery q = getCurrentSession().createSQLQuery(sql);
				q.addEntity("antrian", TbRoomReservation.class);
				q.setInteger("antrianId", roomRsvId);
				
				TbRoomReservation result = (TbRoomReservation)q.uniqueResult();
				
				return result;
			
		}
		catch (Exception e) {
			throw new VONEAppException(e.getMessage());
		}
		
		
	}

}

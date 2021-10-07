package com.vone.medisafe.mapping.dao.treatment;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


import com.vone.medisafe.mapping.MsTreatmentRoom;
import com.vone.medisafe.mapping.MsTreatmentRoomFee;

public class TreatmentRoomDAO extends HibernateDaoSupport{

	private static final Log log = LogFactory.getLog(MsTreatmentDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public List getTreatmentRooms(){
		return super.getHibernateTemplate().find(" from "+ MsTreatmentRoomFee.class.getName());
	}
    
    public boolean save(MsTreatmentRoom troom, MsTreatmentRoomFee troomFee) {
        Session session = null;
        boolean success = false;
        Transaction trans = null;
        
        try {
			session = super.getSessionFactory().openSession();
			trans = session.beginTransaction();
			
			session.saveOrUpdate(troom);
			
			troomFee.setMsTreatmentRoom(troom);
			
			session.saveOrUpdate(troomFee);
			
			trans.commit();
			success=true;
			
		} catch (HibernateException e) {
			trans.rollback();
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
         
        return success;
    }
    
	public boolean delete(MsTreatmentRoom persistentInstance) {
        boolean success = false;
        try {
            getHibernateTemplate().delete(persistentInstance);
            success = true;
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
        return success;
    }
}

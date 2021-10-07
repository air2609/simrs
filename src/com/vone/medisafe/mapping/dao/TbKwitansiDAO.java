package com.vone.medisafe.mapping.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.TbKwitansi;

public class TbKwitansiDAO extends HibernateDaoSupport{
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public boolean save(TbKwitansi kwitansi) throws VONEAppException{
		boolean success = false;
		
		try {
			getHibernateTemplate().save(kwitansi);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	public List<TbKwitansi> getKwitansiList() throws VONEAppException{
		
		Query q = getCurrentSession().createQuery("from TbKwitansi");
			
		List<TbKwitansi> result = q.list();
		
		return result;
	}
	
	public TbKwitansi getKwitansi(String tipe, Date tanggal) throws VONEAppException {
		Query q = getCurrentSession().createQuery("from TbKwitansi where tanggal=:tgl and tipe=:tp");
		q.setDate("tgl", tanggal);
		q.setString("tp", tipe);
		
		TbKwitansi kwitansi = (TbKwitansi)q.uniqueResult();
		
		return kwitansi;
	}
	


}

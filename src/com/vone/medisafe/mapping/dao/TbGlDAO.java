package com.vone.medisafe.mapping.dao;

import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.TbGl;

public class TbGlDAO extends HibernateDaoSupport{
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	
	public List<TbGl> getGlList() throws VONEAppException{
		
		Query q = getCurrentSession().createQuery("from TbGl");
			
		List<TbGl> result = q.list();
		
		return result;
	}
	
	
	public boolean save(TbGl gl) throws VONEAppException{
		boolean success = false;
		
		try {
			getHibernateTemplate().save(gl);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	
}

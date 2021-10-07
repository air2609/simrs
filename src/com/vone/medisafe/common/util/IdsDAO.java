package com.vone.medisafe.common.util;


import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;

public class IdsDAO extends HibernateDaoSupport {
	

	protected void initDao() {
		// do nothing
	}
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public Integer getSequenceValue(String sequenceName) throws VONEAppException{
		Integer integer = null;
		
		Session session = getCurrentSession();

		try {
			integer = (Integer) session.createSQLQuery(
					"select nextval('"+sequenceName+"') as id")
					.addScalar("id", Hibernate.INTEGER).uniqueResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new VONEAppException(e.getMessage());
		} 

		return integer;
	}
	
}

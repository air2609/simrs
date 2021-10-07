package com.vone.medisafe.mapping.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsCreditCardType;

public class CreditCardTypeDAO extends HibernateDaoSupport{
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
	public List getCreditCardTypes() throws VONEAppException {
		List result = null;
		
		Session session = null;
		
		try {
			session = getCurrentSession();
			result = session.createQuery("from MsCreditCardType").list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	public boolean save(MsCreditCardType ctype) throws VONEAppException{
		boolean success = false;
		
		try {
			getHibernateTemplate().saveOrUpdate(ctype);
			success = true;
		} catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return success;
	}
	
	public boolean delete(MsCreditCardType ctype) throws VONEAppException{
		boolean success = false;
		
		try {
			getHibernateTemplate().delete(ctype);
			success = true;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}

		return success;
	}
	
	
	public List getCreditCardTypeBaseOnBank(MsBank bank, short type) throws VONEAppException
	{
		List result = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsCreditCardType.class);
			crit.add(Restrictions.eq("msBank", bank));
			crit.add(Restrictions.eq("NPaymentType", new Short(type)));
			result = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}

}

package com.vone.medisafe.mapping.dao.common;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.service.iface.common.NavigationManager;

public class NavigationDAO extends HibernateDaoSupport{
	
	private static final Logger log = Logger.getLogger(NavigationDAO.class);
	
	public int getMaxResult(String stQuery, int queryType) throws VONEAppException{
		
		Session session = null;	
		Integer result = null;
		
		try{
			session = super.getSessionFactory().openSession();
			
			if (queryType == NavigationManager.HQL){
				
				result = (Integer)session.createQuery("SELECT COUNT(*) FROM("+stQuery+") as q1")	
							.uniqueResult();
				
			}else if (queryType == NavigationManager.SQL){
				result = (Integer)session.createSQLQuery("select COUNT(*) as res FROM("+stQuery+") as q1")
							.addScalar("res", Hibernate.INTEGER)
							.uniqueResult();
			}
			
        } catch (Exception re) {
        	re.printStackTrace();
        	logger.error("query max result failed", re);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), re.getMessage());
        } finally{
        	if (session != null)
        		session.close();
        }
        
        return result.intValue();
		
	}
}

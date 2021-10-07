package com.vone.medisafe.mapping.dao.item;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.item.MsItemGroup;





public class ItemGroupDAO extends HibernateDaoSupport{

	private static final Logger log = Logger.getLogger(ItemGroupDAO.class);
	
	protected void initDao(){
		
	}
	
	public List getAllItemGroup()throws VONEAppException{
		List itemGroups = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsItemGroup.class);
			itemGroups = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return itemGroups;
	}
	
	public boolean save(MsItemGroup transientInstance)throws VONEAppException {
        
        boolean success = false;
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            success = true;
            log.debug("save successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        return success;
    }
	
	public boolean delete(MsItemGroup itemGroup)throws VONEAppException{
		boolean success = false;
		try {
            getHibernateTemplate().delete(itemGroup);
            success = true;
            log.debug("delete successful");
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
		return success;
	}
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
}

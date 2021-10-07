package com.vone.medisafe.mapping.dao.item;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;




public class ItemMeasurementDAO extends HibernateDaoSupport{
	private static final Logger log = Logger.getLogger(ItemMeasurementDAO.class);
	
	protected void initDao(){
		
	}
	
	public List getAllItemMeasurement() throws VONEAppException{
		List itemGroups = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsItemMeasurement.class);
			itemGroups = crit.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return itemGroups;
	}
	
	public boolean save(MsItemMeasurement transientInstance) throws VONEAppException{
        
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
	
	public boolean delete(MsItemMeasurement itemGroup)throws VONEAppException{
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

	public List getMeasurementType() throws VONEAppException{
		Session session = null;
		List list = null;
		
		try{
			session = getCurrentSession();
			
			list = session.createSQLQuery("select distinct m.v_mitem_early_quantify from ms_item_measurement m")
				.addScalar("v_mitem_early_quantify", Hibernate.STRING)
				.list();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());		
		}
		
		return list;
	}
	
	public MsItemMeasurement getMsItemMeasurementByCode(String code) throws VONEAppException {
		Session session = null;
		MsItemMeasurement result = null;
		
		try{
			session = getCurrentSession();
			
			result = (MsItemMeasurement)session.createQuery("" +
					"from MsItemMeasurement where VMitemEarlyQuantify = :code")
					.setString("code", code)
					.list().get(0);
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());		
		}
		
		return result;
	}

	/**
	 * get MsItemMeasurement from EarlyQuantify
	 * @param code
	 * @return
	 * @throws VONEAppException
	 */
	public List getMsItemMeasurementListByCode(String code) throws VONEAppException {
		Session session = null;
		List result = null;
		
		try{
			session = getCurrentSession();
			
			result = session.createSQLQuery("" +
					"select {mim.*} from ms_item_measurement mim where mim.v_mitem_early_quantify = :code")
					.addEntity("mim", MsItemMeasurement.class)
					.setString("code", code)
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
    
	public MsItemMeasurement getMsItemMeasurementByCode(String codeFrom, String codeTo) throws VONEAppException {
		Session session = null;
		MsItemMeasurement result = null;
		
		try{
			session = getCurrentSession();
			
			result = (MsItemMeasurement)session.createQuery("" +
					"from MsItemMeasurement where VMitemEarlyQuantify = :codeFrom" +
					"and VMitemEndQuantify = :codeTo")
					.setString("codeFrom", codeFrom)
					.setString("codeTo", codeTo)
					.uniqueResult();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());		
		}
		
		return result;
	}
}

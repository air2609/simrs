package com.vone.medisafe.mapping.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.MsMiscFee;


public class MiscfeeDAO extends HibernateDaoSupport{
	 	
		private static final Logger log = Logger.getLogger(MiscfeeDAO.class);

		protected void initDao() {
			//do nothing
		}
	    
		public List getMiscfees()throws VONEAppException{
			return super.getHibernateTemplate().find(" from "+ MsMiscFee.class.getName());
		}
	    public boolean save(MsMiscFee transientInstance) throws VONEAppException{
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
	    
		public boolean delete(MsMiscFee persistentInstance) throws VONEAppException{
	        boolean success = false;
	        try {
	            getHibernateTemplate().delete(persistentInstance);
	            success = true;
	            log.debug("delete successful");
	        }catch (Exception e){
				logger.error(e.getMessage());
				throw new VONEAppException(e.getMessage());
	        }
	        return success;
	    }
	    
}

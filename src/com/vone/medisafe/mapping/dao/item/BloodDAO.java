package com.vone.medisafe.mapping.dao.item;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.mapping.pojo.item.MsBlood;


public class BloodDAO extends HibernateDaoSupport{
	private static final Logger log = Logger.getLogger(BloodDAO.class);

	protected void initDao() {
		//do nothing
	}
    
	public List getBloods(){
		return super.getHibernateTemplate().find(" from "+ MsBlood.class.getName());
	}
    public boolean save(MsBlood transientInstance) {
        boolean success = false;
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            success = true;
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
        return success;
    }
    
	public boolean delete(MsBlood persistentInstance) {
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

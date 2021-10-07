package com.vone.medisafe.mapping.dao.item;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;



public class ItemSellingPriceDAO extends HibernateDaoSupport{
	public void initDao(){
		
	}
	
	public boolean save(MsItemSellingPrice itemSellingPrice)throws VONEAppException{
		boolean success = false;
		Session session = null;
		Transaction trans = null;
		try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			session.saveOrUpdate(itemSellingPrice);
			success = true;
			trans.commit();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return success;
	}
	
		

	public boolean delete(MsItemSellingPrice itemSellingPrice)throws VONEAppException{
		boolean success = false;
		Session session = null;
		Transaction trans = null;
		try {
			session = getCurrentSession();
			trans = session.beginTransaction();
			session.delete(itemSellingPrice);
			success = true;
			trans.commit();
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

	public List getItemSellingPrices(String value) throws VONEAppException{
		List list = null;
		
		try {
			value = "%" + value + "%";
			list = getCurrentSession().createSQLQuery("" +
					" select {itemprice.*} " +
					" from " +
					" ms_item_selling_price itemprice, " +
					" ms_item item" +
					" where" +
					" itemprice.n_item_id = item.n_item_id" +
					" and (item.v_item_code like :vcode " +
					" or item.v_item_name like :vname) " +
					" order by item.v_item_code " +
					" limit 100")
			.addEntity("itemprice", MsItemSellingPrice.class)
			.setString("vcode", value)
			.setString("vname", value)
			.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(), e);
		}
		
		return list;	
		
	}
    
}

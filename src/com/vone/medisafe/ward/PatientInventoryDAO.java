package com.vone.medisafe.ward;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.item.MsItem;

/**
 * PatientInventoryDAO.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 23 2006
 * @category persistence (data model)
 */

public class PatientInventoryDAO extends HibernateDaoSupport{
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	
	public List<MsItem> getPatientItems(TbRegistration patient) throws VONEAppException{
		List<MsItem> result = null;
				
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct {item.*} ");
		
		sql.append(" from ");
		sql.append(" ms_item item, ");
		sql.append(" tb_patient_inventory patInv ");
		
		sql.append(" where ");
		sql.append(" item.n_item_id=patInv.n_item_id ");
		sql.append(" and patInv.n_reg_id=:patId ");
		
		try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addEntity("item", MsItem.class);
			query.setInteger("patId", patient.getNRegId());
			
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	public List<TbPatientInventory> getPatientInventory(MsItem item,TbRegistration patient) throws VONEAppException{
		
		List<TbPatientInventory> result = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select ");
		sb.append(" {patInv.*} ");
		
		sb.append(" from ");
		sb.append(" tb_patient_inventory patInv ");
		
		sb.append(" where ");					 
		sb.append(" patInv.n_item_id=:item ");
		sb.append(" and patInv.n_reg_id=:patient ");
		
		sb.append(" order by ");
		sb.append(" patInv.d_whn_create asc ");
		
		try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			
			query.addEntity("patInv", TbPatientInventory.class);
			query.setInteger("item", item.getNItemId());
			query.setInteger("patient", patient.getNRegId());
			
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
			
		}
		
		return result;
	}
	
	
	public List<TbPatientInventory> getPatientInventoryTrx(MsItem item,TbRegistration patient) throws VONEAppException{
		
		List<TbPatientInventory> result = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select ");
		sb.append(" {patInv.*} ");
		
		sb.append(" from ");
		sb.append(" tb_patient_inventory patInv ");
		
		sb.append(" where ");					 
		sb.append(" patInv.n_item_id=:item ");
		sb.append(" and patInv.n_reg_id=:patient ");
		sb.append(" and patInv.n_qty > 0");
		
		sb.append(" order by ");
		sb.append(" patInv.d_whn_create asc ");
		
		try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			
			query.addEntity("patInv", TbPatientInventory.class);
			query.setInteger("item", item.getNItemId());
			query.setInteger("patient", patient.getNRegId());
			
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
			
		}
		
		return result;
	}
	
	public List getPatientInventory(TbRegistration patient) throws VONEAppException{
		
		String sql = "select patInv from TbPatientInventory as patInv where patInv.tbRegistration=:patient"+
					 " order by patInv.DWhnCreate asc";
		String field = "patient";
		Object obj = patient;
		List result = null;
		try {
			result = getHibernateTemplate().findByNamedParam(sql,field, obj);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean save(List<TbPatientInventory> inventory) throws VONEAppException{
		boolean success = false;
		
		try {
			getHibernateTemplate().saveOrUpdateAll(inventory);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	
	public boolean delete(Set<TbPatientInventory> patInv) throws VONEAppException{
		boolean success = false;
		
		try {
			
			getHibernateTemplate().deleteAll(patInv);
			success = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
		
	}
}

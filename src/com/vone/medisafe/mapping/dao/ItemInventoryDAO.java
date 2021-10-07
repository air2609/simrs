package com.vone.medisafe.mapping.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;

public class ItemInventoryDAO extends HibernateDaoSupport{
	
	StringBuffer stQuery = new StringBuffer();
	
	public List getItemInventoryBaseOnUnit(MsItem item, MsUnit unit) throws VONEAppException{
		List list = null;	
		Session session = null;
		String sql = "select {inv.*} from tb_item_inventory inv where inv.n_whouse_id=:wId "+
					 "AND inv.n_item_id=:itemId AND inv.n_item_inv_qty > :qty";
		
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("inv", TbItemInventory.class);
			query.setInteger("wId", unit.getMsWarehouse().getNWhouseId().intValue());
			query.setInteger("itemId", item.getNItemId().intValue());
			query.setShort("qty", (short)0);
			list = query.list();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return list;
	}
	
	public Double getQtyByBatchNo (String batchNo) throws VONEAppException {
		
		try {
			Session session = getCurrentSession();
			
			stQuery.setLength(0);
			stQuery.append(" select sum(tbII.n_item_inv_qty) as result from tb_item_inventory tbII, tb_batch_item tbBI")
				.append(" where")
				.append(" tbII.n_batch_id = tbBI.n_batch_id and ")
				.append(" tbBI.v_batch_no = :batchNo ")
				.append(" group by tbBI.v_batch_no");
			
			return (Double)session.createSQLQuery(stQuery.toString())
				.addScalar("result", Hibernate.DOUBLE)
				.setString("batchNo", batchNo)
				.uniqueResult();
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());		
		}
	}
	
	public TbItemInventory getInventory(MsItem item, MsUnit unit)throws VONEAppException{
		TbItemInventory inventory = null;
		
		Session session = null;
		String sql = "select {inv.*} from tb_item_inventory inv where inv.n_whouse_id=:wId "+
					 "AND inv.n_item_id=:itemId order by inv.n_item_inv_qty asc limit 1";
		
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("inv", TbItemInventory.class);
			query.setInteger("wId", unit.getMsWarehouse().getNWhouseId().intValue());
			query.setInteger("itemId", item.getNItemId().intValue());
			inventory = (TbItemInventory)query.uniqueResult();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
				
		return inventory;
	}

	@SuppressWarnings("unchecked")
	public List getInventoryOnWhouse (Integer whouseId) throws VONEAppException {
		
		Session session = null;
		List<TbItemInventory> listRes = null;
		
		try{
			session = getCurrentSession();
			
			stQuery.setLength(0);
			
			stQuery.append(" select {inv.*} from tb_item_inventory inv ")
				.append(" where inv.n_whouse_id=:whouseId ")
				.append(" and inv.n_item_inv_qty > 0 ")
				.append(" order by inv.n_item_id ");
			
			listRes = session.createSQLQuery(stQuery.toString())
						.addEntity("inv", TbItemInventory.class)
						.setInteger("whouseId", whouseId.intValue())
						.list();
			
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        
        return listRes;
	}
	
	@SuppressWarnings("unchecked")
	public List getInventoryAll () throws VONEAppException {
		
		Session session = null;
		List<TbItemInventory> listRes = null;
		
		try{
			session = getCurrentSession();
			
			stQuery.setLength(0);
			
			stQuery.append(" select {inv.*} from tb_item_inventory inv ")
				.append(" where inv.n_item_inv_qty > 0 ")
				.append(" order by inv.n_item_id ");
			
			listRes = session.createSQLQuery(stQuery.toString())
						.addEntity("inv", TbItemInventory.class)
						.list();
			
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        
        return listRes;
	}
	
	public List getInventoryOnWhouseByCriteria (Integer whouseId, String crit) throws VONEAppException {
		
		Session session = null;
		List listRes =null;
		
		try {

			session = getCurrentSession();
			
			stQuery.setLength(0);
			stQuery.append(" select {inv.*}  ")
				.append(" from tb_item_inventory inv, tb_batch_item batch, ms_item item")
				.append(" where inv.n_whouse_id=:whouseId ")
				.append(" and inv.n_batch_id = batch.n_batch_id ")
				.append(" and inv.n_item_id = item.n_item_id")
				.append(" and inv.n_item_inv_qty > 0 ")
				.append(" and ")
				.append(" (batch.v_batch_no like :batchNo")
				.append(" or item.v_item_code like :itemCode")
				.append(" or item.v_item_name like :itemName)")
				.append(" order by inv.n_item_id ")
				.append(" limit 100");
				
				listRes = session.createSQLQuery(stQuery.toString())
				.addEntity("inv", TbItemInventory.class)
				.setInteger("whouseId", whouseId.intValue())
				.setString("batchNo", crit)
				.setString("itemCode", crit)
				.setString("itemName", crit)
				.list();
			
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        
        return listRes;

	}
	
	public List getInventoryOnWhouseItem (Integer whouseId, String itemCode, String itemName, String batchNo) throws VONEAppException {
		
		Session session = null;
		List listRes = null;
		
		try{
			session = getCurrentSession();
			
			stQuery.setLength(0);
			
			if (StringUtils.hasValue(batchNo)){
				
				stQuery.append(" select {inv.*}  ")
					.append(" from tb_item_inventory inv, tb_batch_item batch, ms_item item")
					.append(" where inv.n_whouse_id=:whouseId ")
					.append(" and inv.n_batch_id = batch.n_batch_id ")
					.append(" and batch.v_batch_no like :batchNo")
					.append(" and inv.n_item_id = item.n_item_id")
					.append(" and item.v_item_code like :itemCode")
					.append(" and item.v_item_name like :itemName")
					.append(" and inv.n_item_inv_qty > 0 ")
					.append(" order by inv.n_item_id ")
					.append(" limit 100");
				
				listRes = session.createSQLQuery(stQuery.toString())
							.addEntity("inv", TbItemInventory.class)
							.setInteger("whouseId", whouseId.intValue())
							.setString("batchNo", batchNo)
							.setString("itemCode", itemCode)
							.setString("itemName", itemName)
							.list();
				
				
			}else{
				
				stQuery.append(" select {inv.*}  ")
				.append(" from tb_item_inventory inv, ms_item item")
				.append(" where inv.n_whouse_id=:whouseId ")
				.append(" and inv.n_item_id = item.n_item_id")
				.append(" and item.v_item_code like :itemCode")
				.append(" and item.v_item_name like :itemName")
				.append(" and inv.n_item_inv_qty > 0 ")
				.append(" order by inv.n_item_id ")
				.append(" limit 100");
			
			listRes = session.createSQLQuery(stQuery.toString())
						.addEntity("inv", TbItemInventory.class)
						.setInteger("whouseId", whouseId.intValue())
						.setString("itemCode", itemCode)
						.setString("itemName", itemName)
						.list();				
			
			}
			
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
        
        return listRes;
	}
	
    public void save (TbItemInventory pojo) throws VONEAppException {
    	    	
    	try {
			TbBatchItem tbBI = new TbBatchItem();
			pojo.setTbBatchItem(tbBI);
			tbBI.setMsItem(pojo.getMsItem());
			tbBI.setDBatchExpDate(new Date());
			tbBI.setNBatchItemQty(new Integer(5000).shortValue());
			
			getHibernateTemplate().save(tbBI);
			
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}    
    }
    
    public void update (TbItemInventory pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (TbItemInventory pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
    
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }

	public List getLaporanInventory(Integer warehouseid) throws VONEAppException{
		Session session = getCurrentSession();
		String sql = "select nomor, kode_obat, nama_obat, harga_standar, jumlah, satuan from report.laporan_persediaan_obat(:id)";
		
		SQLQuery query = session.createSQLQuery(sql);
		query.setInteger("id", warehouseid);
		
		query.addScalar("nomor", Hibernate.INTEGER);
		query.addScalar("kode_obat", Hibernate.STRING);
		query.addScalar("nama_obat", Hibernate.STRING);
		query.addScalar("harga_standar", Hibernate.DOUBLE);
		query.addScalar("jumlah", Hibernate.DOUBLE);
		query.addScalar("satuan", Hibernate.STRING);
		return query.list();
		
	}

	public List<TbItemInventory> getExpiredItem() throws VONEAppException{
		Session session = getCurrentSession();
		String sql = "select {i.*} from tb_item_inventory i, tb_batch_item b  where b.n_batch_id=i.n_batch_id "
				   + "and (date_part('day',b.d_batch_exp_date-now()) <= 0 or date_part('day',b.d_batch_exp_date-now()) between 0 and 180) "
				   + "and i.n_item_inv_qty > 0 order by b.d_batch_exp_date asc";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("i", TbItemInventory.class);
		return query.list();
	}

	public TbItemInventory getLastInventory(MsItem item) throws HibernateException, VONEAppException {
		String sql = "select {i.*} from tb_item_inventory i where n_item_id=:itemId and n_whouse_id=8 order by n_item_inventory_id desc limit 1";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addEntity("i", TbItemInventory.class);
		query.setInteger("itemId", item.getNItemId());
		return (TbItemInventory)query.uniqueResult();
	}
    
}

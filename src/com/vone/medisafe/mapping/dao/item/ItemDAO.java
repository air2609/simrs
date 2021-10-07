package com.vone.medisafe.mapping.dao.item;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.mapping.pojo.item.MsItemSupplied;
import com.vone.medisafe.mapping.pojo.item.MsVendor;


/**
 * ItemDAO.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Sep, 30 2006
 * @category persistence (data model)
 */

public class ItemDAO extends HibernateDaoSupport{
	
	private static final Logger log = Logger.getLogger(ItemDAO.class);
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public void initDao(){
		
	}
	
	public List getAllItem() throws VONEAppException{
		List itemGroups = null;
		Session session = null;
		
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsItem.class);
			itemGroups = crit.list();
		} catch (HibernateException e) {
			log.error("terjadi exception "+ e);
			throw new VONEAppException(e.getMessage());
		}
		return itemGroups;
	}
	
	public boolean save(MsItem transientInstance, Set supplier)throws VONEAppException {
        
        boolean success = false;
        Session session = null;
        
        try {
			session = getCurrentSession();
			
			session.save(transientInstance);
			
			Listitem suplierList = null;
			MsItemSupplied itemSupplied = null;
			
			Iterator itr = supplier.iterator();
			while(itr.hasNext()){
				suplierList = (Listitem)itr.next();
				itemSupplied = new MsItemSupplied();
				itemSupplied.setMsItem(transientInstance);
				itemSupplied.setMsVendor((MsVendor)suplierList.getValue());
				session.save(itemSupplied);
			}
			 
			success = true;
			
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
        
        return success;
    }

	public boolean update(MsItem transientInstance, Set supplier)throws VONEAppException {
        
        boolean success = false;
        Session session = null;

        try {
			session = getCurrentSession();
			
			session.update(transientInstance);
			
			Listitem suplierList = null;
			MsItemSupplied itemSupplied = null;
			
			String hql = "delete from MsItemSupplied where n_item_id=:itemId";
			Query query = session.createQuery(hql);
			query.setInteger("itemId", transientInstance.getNItemId().intValue());
			query.executeUpdate();
			
			Iterator itr = supplier.iterator();
			while(itr.hasNext()){
				suplierList = (Listitem)itr.next();
				itemSupplied = new MsItemSupplied();
				itemSupplied.setMsItem(transientInstance);
				itemSupplied.setMsVendor((MsVendor)suplierList.getValue());
				session.save(itemSupplied);
			}
			 
			success = true;
			
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
        
        return success;
    }
	
	public boolean delete(MsItem itemGroup) throws VONEAppException{
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
	
	public List searchItem(String itemCode,String itemName)throws VONEAppException{
		List list = null;
		Session session = null;
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsItem.class);
			Criterion code = Restrictions.like("VItemCode", itemCode+"%");
			Criterion name = Restrictions.like("VItemName", itemName+"%");
			Conjunction con = Restrictions.conjunction();
			con.add(code);
			con.add(name);
			crit.add(con);
			list = crit.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return list;
	}
	
	public MsItem getById (Integer id)throws VONEAppException {
		
        try {
        	MsItem instance = (MsItem) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.pojo.item.MsItem", id);
            
            return instance;
        } catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
	}
	
	public MsItem getByCode(String itemCode)throws VONEAppException{
		MsItem item = null;
		Session session = null;
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsItem.class);
			crit.add(Restrictions.eq("VItemCode", itemCode));
			item = (MsItem)crit.uniqueResult();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return item;
	}
	
	public MsItem getBloodItem(String blood)throws VONEAppException{
		MsItem item = null;
		Session session = null;
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsItem.class);
			crit.add(Restrictions.eq("VItemName",blood));
			item = (MsItem)crit.uniqueResult();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return item;
	}
	
	public List getItemByWarehouseUnitAndTclass(Integer warehouseUnitId,String tclass)throws VONEAppException{
		Session session = null;
    	List result = null;
    	
    	String sql = "select distinct {itemselling.*} from ms_item_selling_price itemselling, ms_item item, ms_treatment_class tclass,"+
    				 " tb_item_inventory invent where itemselling.n_item_id=item.n_item_id "+
    	             " AND itemselling.n_tclass_id=tclass.n_tclass_id AND item.n_item_id=invent.n_item_id AND"+
    	             " invent.n_whouse_id=:wId AND tclass.v_tclass_desc=:tdesc";
    	
    	try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("itemselling", MsItemSellingPrice.class);
			query.setInteger("wId", warehouseUnitId.intValue());
			query.setString("tdesc", tclass);
			result = query.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());

		}
    	return result;
	}
	
	public float getItemQuantity(MsItem item, String kelasTarif, Integer wId)throws VONEAppException {
		float quantity = 0;
		
		String sql = "select {inventory.*} from tb_item_inventory inventory, ms_item item, ms_treatment_class tclass,"+
		 " ms_item_selling_price sellprice where inventory.n_item_id=item.n_item_id AND item.n_item_id=sellprice.n_item_id"+
         " AND sellprice.n_tclass_id=tclass.n_tclass_id AND item.n_item_id=:itemId AND tclass.v_tclass_desc=:tdesc"+
         " AND inventory.n_item_inv_qty >:jumlah AND inventory.n_whouse_id=:wId";
        
		
		Session session = null;
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("inventory", TbItemInventory.class);
			query.setInteger("itemId", item.getNItemId().intValue());
			query.setString("tdesc", kelasTarif);
			query.setInteger("jumlah", 0);
			query.setInteger("wId", wId.intValue());
			
			List hasil = query.list();
			
			TbItemInventory itemInv = null;
			Iterator it = hasil.iterator();
			while(it.hasNext()){
				itemInv = (TbItemInventory)it.next();
				quantity = quantity + itemInv.getNItemInvQty();
			}
			
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());

		}
		
		return quantity;
	}
	
	public List searchItemByUnitAndTclass(Integer warehouseUnitId,String tclass, String itemCode, String itemName)throws VONEAppException{
		Session session = null;
    	List result = null;
    	
//    	String sql = "select distinct {itemselling.*} from ms_item_selling_price itemselling, ms_item item,
//    	             ms_treatment_class tclass,"+
//    				 " tb_item_inventory invent where itemselling.n_item_id=item.n_item_id "+
//    	             " AND itemselling.n_tclass_id=tclass.n_tclass_id AND item.n_item_id=invent.n_item_id AND"+
//    	             " invent.n_whouse_id=:wId AND tclass.v_tclass_desc=:tdesc AND item.v_item_code LIKE :code"+
//    	             " AND item.v_item_name LIKE :name";
    	
    	String sqlBaru ="select "+
    						
    						" inv.n_item_id as id, "+
    						" item.v_item_code as code, "+
    						" item.v_item_name as name, "+
    						" item.n_r as r, "+
    						" sell.n_selling_price as harga, "+
    						" sat.v_mitem_end_quantify as satuan, "+
    						" sum(inv.n_item_inv_qty) as jumlah,"+
    						" item.n_type as tipe "+
    						
    	                "from " +
    	                
    	                	" tb_item_inventory inv, "+
    	                	" ms_item item, "+
    	                	" ms_item_selling_price sell, " +
    	                	" ms_item_measurement sat, " +
    	                	" ms_treatment_class tclass "+
    	                
    	                "where "+
    	                
    	                	" inv.n_whouse_id=:wId "+
    	                	" and inv.n_item_id=item.n_item_id "+
    	                	" and inv.n_item_inv_qty > 0 " +
    	                	" and item.v_item_code like :code "+
    	                	" and item.v_item_name like :itemName "+
    	                	" and item.n_item_id=sell.n_item_id "+
    	                	" and item.n_mitem_id=sat.n_mitem_id "+
    	                	" and sell.n_tclass_id=tclass.n_tclass_id "+
    	                	" and tclass.v_tclass_desc=:kelas "+
    	                	
    	                "group by "+
    	                
    	                	" id, code, name, r, harga, satuan, tipe";
    	                
    	
    	try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sqlBaru);
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("code",Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("r", Hibernate.SHORT);
			query.addScalar("harga", Hibernate.DOUBLE);
			query.addScalar("satuan", Hibernate.STRING);
			query.addScalar("jumlah", Hibernate.DOUBLE);
			query.addScalar("tipe", Hibernate.SHORT);
			query.setInteger("wId", warehouseUnitId.intValue());
			query.setString("code", itemCode);
			query.setString("itemName", itemName);
			query.setString("kelas", tclass);
									
			result = query.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
    	return result;
	}

	public List searchItemByWarehouese(Integer warehouseUnitId, String itemCode, String itemName)throws VONEAppException{
		Session session = null;
    	List result = null;
    	
    	String sqlBaru =
    		" select distinct " +
    		"	inv.n_item_id as id, " +
    		" 	item.v_item_code as code, " +
    		"	item.v_item_name as name, " +
    		"	sat.v_mitem_end_quantify as satuan " +

    		" from " +
    		"	ms_item item, " +
    		"	tb_item_inventory inv, " +
    		"	ms_item_measurement sat " +

    		" where " +
    		"	item.n_item_id = inv.n_item_id " +
    		"	and item.n_mitem_id = sat.n_mitem_id " +
    		"	and inv.n_whouse_id = :wId " +
    		"	and item.v_item_name like :itemName " +
    		"	and item.v_item_code like :code " +
    		
    		" order by inv.n_item_id	";
    	
    	try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sqlBaru);
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("code",Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("satuan", Hibernate.STRING);
			query.setInteger("wId", warehouseUnitId.intValue());
			query.setString("code", itemCode);
			query.setString("itemName", itemName);
									
			result = query.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
    	return result;
		
	}
	
	public List getStok(Integer id)throws VONEAppException{
		Session session = null;
		List result = null;
    	
    	String sqlBaru =
    		"select sum(inv.n_batch_item_qty) as jml " +
    		" from " +
    		"	tb_batch_item inv " +
    		" where " +
    		"	inv.n_item_id = :id ";
    	
    	try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sqlBaru);
			query.addScalar("jml", Hibernate.INTEGER);
			query.setInteger("id", id);
									
			result = query.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());

		}
    	
		return result;
	}
	
	public boolean saveItemRequest(Set tir)throws VONEAppException{
        boolean success = false;
		Session session = null;

    	try {
			session = getCurrentSession();
			
			Iterator iterator = tir.iterator();
			while (iterator.hasNext()) {
				TbItemRequest element = (TbItemRequest) iterator.next();
				session.save(element);
			}
			
			success = true;
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return success;
	}
	
	public MsWarehouse getWarehouseByCode(String warehouseCode)throws VONEAppException{
		MsWarehouse item = null;
		Session session = null;
		try {
			session = getCurrentSession();
			Criteria crit = session.createCriteria(MsWarehouse.class);
			crit.add(Restrictions.eq("VWhouseCode", warehouseCode));
			item = (MsWarehouse)crit.uniqueResult();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return item;
	}

	public MsItem getMsItem(Integer itemId) throws VONEAppException{
		Session session = getCurrentSession();
		return (MsItem) session.get(MsItem.class, itemId);
	}

	public List<MsItem> cariItem(String input) throws VONEAppException {
		List<MsItem> list = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select ");
		sb.append(" distinct ");
		sb.append(" {item.*} ");
		
		sb.append(" from ");
		sb.append("	ms_item item, ");
		sb.append("	ms_item_supplied supplied, ");
		sb.append(" ms_item_group igroup, ");
		sb.append(" ms_item_measurement measure ");
		
		sb.append(" where ");
		sb.append(" item.n_item_id = supplied.n_item_id ");
		sb.append(" and item.n_item_group_id=igroup.n_item_group_id ");
		sb.append(" and item.n_mitem_id=measure.n_mitem_id ");
		sb.append(" and (item.v_item_code like :tcode ");
		sb.append(" or item.v_item_name like :tname ");
		sb.append(" or igroup.v_item_group_name like :tgroup ");
		sb.append(" or measure.v_mitem_end_quantify like :tquantify )");
		sb.append(" limit 100 "); 
		
		
		
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			
			query.addEntity("item", MsItem.class);
			query.setString("tcode", input);
			query.setString("tname", input);
			query.setString("tgroup", input);
			query.setString("tquantify", input);
			list = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return list;
	}

	public List<MsItem> getObat() throws HibernateException, VONEAppException {
		StringBuffer sb = new StringBuffer();
		sb.append("select {item.*} from ms_item item");
		sb.append(" where item.n_item_group_id=15");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("item", MsItem.class);
		return query.list();
	}
	
	public List getObatDetail() throws HibernateException, VONEAppException{
		String sql = "select i.n_item_id as id, i.v_item_code as code, i.v_item_name as name, i.n_item_buffer_limit as buffer, "
				   + "i.n_max_order as max, (select (d.n_subtotal/d.n_do_det_qty) * (1.1) from tb_delivery_order_detail d  where "
				   + "d.n_item_id=i.n_item_id order by d.n_do_det_id desc limit 1) as hargabeli, (select s.n_selling_price from "
				   + "ms_item_selling_price s where s.n_item_id=i.n_item_id limit 1) as hargajual from ms_item i where "
				   + "i.n_item_group_id=15 and i.n_item_buffer_limit > 0 and i.n_max_order is not null";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addScalar("id", Hibernate.INTEGER)
			 .addScalar("code", Hibernate.STRING)
		     .addScalar("name", Hibernate.STRING)
		     .addScalar("buffer", Hibernate.INTEGER)
		     .addScalar("max", Hibernate.INTEGER)
		     .addScalar("hargabeli", Hibernate.DOUBLE)
		     .addScalar("hargajual", Hibernate.DOUBLE);
		return query.list();
	}

	public void updateBatchItem(List<MsItem> msItems, List<MsItemSellingPrice> sellingPrice) {
		getHibernateTemplate().saveOrUpdateAll(msItems);
		getHibernateTemplate().saveOrUpdateAll(sellingPrice);
		
	}

	public List getRajalReport(String mulai, String sampai, String kodeRajal,
			String shift) throws HibernateException, VONEAppException {
		
    	List result = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select r.nomor as no, r.no_nota as nota, r.nama_pasien as pasien, r.total as total, r.diskon as diskon, r.ppn as ppn, r.total_akhir as tot FROM ");
		if(shift.equalsIgnoreCase("ALL")){
			sb.append("report.laporan_penjualan_all");
		}else sb.append("report.laporan_penjualan_rajal");
		
		sb.append("(:dari, :sampai, :kode, :shift) r");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addScalar("no", Hibernate.INTEGER);
		query.addScalar("nota", Hibernate.STRING);
		query.addScalar("pasien", Hibernate.STRING);
		query.addScalar("total", Hibernate.DOUBLE);
		query.addScalar("diskon", Hibernate.DOUBLE);
		query.addScalar("ppn", Hibernate.DOUBLE);
		query.addScalar("tot", Hibernate.DOUBLE);
		
		query.setString("dari", mulai);
		query.setString("sampai", sampai);
		query.setString("kode",kodeRajal);
		query.setString("shift", shift);
		
		result = query.list();
		
		return result;
	}

	public List getRanapReport(String mulai, String sampai, String kodeRajal,
			String shift) throws HibernateException, VONEAppException {
		List result = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select l.nomor as no, l.no_transaksi as nota, l.no_registrasi as reg, l.nama_pasien as pasien, " +
				"l.bed as bed, l.ruangan as ruangan, l.r as r, l.total as total, l.diskon as disc, l.total_akhir as akhir, l.grup as group ");
		sb.append(" from ");
		if(shift.equalsIgnoreCase("ALL")){
			sb.append("report.laporan_Penjualan_Ranap_all");
			sb.append("(:dari, :sampai, :kode) l");
		}else{ 
			sb.append("report.laporan_penjualan_ranap");
			sb.append("(:dari, :sampai, :kode, :shift) l");
		}
		
//		sb.append("(:dari, :sampai, :kode, :shift) l");
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addScalar("no", Hibernate.INTEGER);
		query.addScalar("nota", Hibernate.STRING);
		query.addScalar("reg", Hibernate.STRING);
		query.addScalar("pasien", Hibernate.STRING);
		query.addScalar("bed", Hibernate.STRING);
		query.addScalar("ruangan", Hibernate.STRING);
		query.addScalar("r", Hibernate.INTEGER);
		query.addScalar("total", Hibernate.DOUBLE);
		query.addScalar("disc", Hibernate.DOUBLE);
		query.addScalar("akhir", Hibernate.DOUBLE);
		query.addScalar("group", Hibernate.STRING);
		
		query.setString("dari", mulai);
		query.setString("sampai", sampai);
		query.setString("kode",kodeRajal);
		if(!shift.equalsIgnoreCase("ALL"))
			query.setString("shift", shift);
		
		result = query.list();
		return result;
	}

	public List serachItemUnderBuffer(String code, String name) {
		StringBuffer sb = new StringBuffer();
		sb.append("select coalesce(sum(inv.n_item_inv_qty), 0) as jumlah,");
		sb.append("i.n_item_id as id,i.n_item_buffer_limit as buffer,");
		sb.append("i.v_item_code as kode,i.v_item_name as nama,");
		sb.append("m.v_mitem_early_quantify as satuan,g.v_item_group_code as grup ");
		
		sb.append("from ms_item i left join tb_item_inventory inv on i.n_item_id=inv.n_item_id ");
		sb.append("inner join ms_item_measurement m on m.n_mitem_id=i.n_mitem_id ");
		sb.append("inner join ms_item_group g on g.n_item_group_id=i.n_item_group_id ");
		
		sb.append("where '1' ");
		if(!code.trim().equalsIgnoreCase("")) sb.append("and i.v_item_code like :tcode ");
		if(!name.trim().equalsIgnoreCase(""))sb.append("and i.v_item_name like :tname ");
        
        sb.append("group by i.n_item_id, i.n_item_buffer_limit, i.v_item_code, i.v_item_name,");
        sb.append("m.v_mitem_early_quantify,  g.v_item_group_code ");
        sb.append("having coalesce(sum(inv.n_item_inv_qty), 0) <= i.n_item_buffer_limit " );
        
        try {
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addScalar("jumlah", Hibernate.INTEGER);
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("buffer", Hibernate.INTEGER);
			query.addScalar("kode", Hibernate.STRING);
			query.addScalar("nama", Hibernate.STRING);
			query.addScalar("satuan", Hibernate.STRING);
			query.addScalar("grup", Hibernate.STRING);
			
			if(!code.trim().equalsIgnoreCase("")) query.setString("tcode", "%"+code+"%");
			if(!name.trim().equalsIgnoreCase("")) query.setString("tname", "%"+name+"%");
			
			return query.list();
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public List<MsItemSellingPrice> getItemSellingPrice(MsItem msItem) throws HibernateException, VONEAppException {
		Query qry = getCurrentSession().createQuery("from MsItemSellingPrice where msItem = :item");
		qry.setParameter("item", msItem);
		return qry.list();
	}

	public List getReturReport(String mulai, String sampai, String kode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nomor, no_retur, no_nota, no_resep, nama_pasien, total, total_akhir, jenis ");
		sql.append(" from ");
		sql.append(" report.laporan_retur_obat");
		sql.append("(:start, :end, :code) order by jenis, nomor asc");
		
		SQLQuery query;
		try {
			query = getCurrentSession().createSQLQuery(sql.toString());
			
			query.addScalar("nomor", Hibernate.INTEGER);
			query.addScalar("no_retur", Hibernate.STRING);
			query.addScalar("no_nota", Hibernate.STRING);
			query.addScalar("no_resep", Hibernate.STRING);
			query.addScalar("nama_pasien", Hibernate.STRING);
			query.addScalar("total", Hibernate.DOUBLE);
			query.addScalar("total_akhir", Hibernate.DOUBLE);
			query.addScalar("jenis", Hibernate.STRING);
			
			query.setString("start", mulai);
			query.setString("end", sampai);
			query.setString("code", kode);
			
			return query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
}

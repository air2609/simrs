package com.vone.medisafe.mapping.dao;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBundledItemUsedTrx;
import com.vone.medisafe.mapping.TbBundledTreatUsedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;


/**
 * PolyclinicDAO.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Oct, 19 2006
 * @category persistence (data model)
 */


public class PolyclinicDAO extends HibernateDaoSupport{
	
	private static final Logger log = Logger.getLogger(PolyclinicDAO.class);
	
	
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	
	public boolean saveTransaction(MsPatient patient, TbExamination nota, Set treatmentTrx, Set itemTrx, 
			Set bundleTrx, Set miscTrx, MsUnit unit, boolean isRanap) throws VONEAppException
	{
		log.debug("PoliclinicDAO - saveTransaction - ENTER");
		log.debug(miscTrx.size());
		
		boolean success = false;
		
		boolean isRujukan = false;
				
		Date tanggal = new Date();
		
		Session session = getCurrentSession();
		
//		Transaction trans = session.beginTransaction();
		
		if(patient.getNPatientId() == null) 
			isRujukan = true;
		try {
			
			
			
			if(isRujukan)
			{
				patient.setDWhnCreate(tanggal);
				patient.setNPatientId(IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.PATIENT_ID));
				session.save(patient);
			}
			
			nota.setMsPatient(patient);
			nota.setDWhnCreate(tanggal);
			Integer noNota = null;
			if(isRanap){
				noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RANAP);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, tanggal, unit.getVUnitCode(), 
						MedisafeConstants.RANAP));
			}
			
			else 
			{
				noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RAJAL);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, tanggal, unit.getVUnitCode(), 
						MedisafeConstants.NON_RANAP));
			}
						
			session.save(nota);
			
			//TREATMENT
			Iterator it = treatmentTrx.iterator();
			while(it.hasNext()){
				TbTreatmentTrx treatTrx = (TbTreatmentTrx)it.next();
				treatTrx.setTbExamination(nota);
				treatTrx.setDWhnCreate(tanggal);
				session.save(treatTrx);
			}
			
			//MISC
			it = miscTrx.iterator();
			while(it.hasNext()){
				TbMiscTrx tbMiscTrx = (TbMiscTrx)it.next();
				tbMiscTrx.setTbExamination(nota);
				tbMiscTrx.setDWhnCreate(tanggal);
				session.save(tbMiscTrx);
				
			}
			
			//ITEM
			it = itemTrx.iterator();
			
			float qtyUsed = 0;
			double disc_1 = 0;
			double trans_val_1 = 0;
			double trans_aft_disc_1 = 0;
			
			
			MsItem item = null;
			TbItemTrx realTrx = null;
		
			while(it.hasNext()){
				TbItemTrx itmTrx = (TbItemTrx)it.next();
				itmTrx.setTbExamination(nota);
				itmTrx.setDWhnCreate(tanggal);
				item = itmTrx.getMsItem();
				qtyUsed = itmTrx.getNQty();
				
				disc_1 = itmTrx.getNDiscAmount()/itmTrx.getNQty();
				trans_val_1 = itmTrx.getNAmountTrx()/itmTrx.getNQty();
				trans_aft_disc_1 = itmTrx.getNAmountAfterDisc()/itmTrx.getNQty();
				

				
				String sql = "select {inv.*} from tb_item_inventory inv where inv.n_whouse_id=:wId "+
				 			 "AND inv.n_item_id=:itemId AND inv.n_item_inv_qty > :qty";
				
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity("inv", TbItemInventory.class);
				query.setInteger("wId", unit.getMsWarehouse().getNWhouseId().intValue());
				query.setInteger("itemId", item.getNItemId().intValue());
				query.setShort("qty", (short)0);
				
				
				List itemInvList = query.list();

				Iterator itr = itemInvList.iterator();
				
				while(itr.hasNext()){
					TbItemInventory inv = (TbItemInventory)itr.next();
					
					if(qtyUsed == 0){
						break;
					}
					if(inv.getNItemInvQty() <= qtyUsed){
						qtyUsed = qtyUsed - inv.getNItemInvQty();
						
						realTrx = itmTrx.clone();
						realTrx.setTbBatchItem(inv.getTbBatchItem());
						realTrx.setNQty(inv.getNItemInvQty());
						realTrx.setNAmountAfterDisc(inv.getNItemInvQty() * trans_aft_disc_1);
						realTrx.setNAmountTrx(inv.getNItemInvQty() * trans_val_1);
						realTrx.setNDiscAmount(inv.getNItemInvQty() * disc_1);
						
						session.save(realTrx);
						
						inv.setNItemInvQty((short)0);
						session.update(inv);
						
					}else{
						float sub = inv.getNItemInvQty() - qtyUsed;
						
						realTrx = itmTrx.clone();
						realTrx.setTbBatchItem(inv.getTbBatchItem());
						realTrx.setNQty(qtyUsed);
						realTrx.setNAmountAfterDisc(qtyUsed * trans_aft_disc_1);
						realTrx.setNAmountTrx(qtyUsed * trans_val_1);
						realTrx.setNDiscAmount(qtyUsed * disc_1);
						
						session.save(realTrx);
						
						inv.setNItemInvQty(sub);
						session.update(inv);
						qtyUsed = 0;
					}
				}
			}
		
			
			log.debug("UPDATE DATA ITEM INVENTORY BERDASARKAN ITEM TRX SUKSES");
			
			//BUNDELD
			it = bundleTrx.iterator();
			while(it.hasNext())
			{
				TbBundledTrx bundle = (TbBundledTrx)it.next();
							
				
				bundle.setTbExamination(nota);
				bundle.setDWhnCreate(tanggal);
				session.save(bundle);
				
			
				
				Set bundleTreatTrx = bundle.getTbBundledTreatUsedTrxes();
				Set bundleItemTrx = bundle.getTbBundledItemUsedTrxes();
				
				Iterator itr2 = bundleTreatTrx.iterator();
				while(itr2.hasNext()){
					TbBundledTreatUsedTrx btutrx = (TbBundledTreatUsedTrx)itr2.next();
					btutrx.setTbBundledTrx(bundle);
					btutrx.setDWhnCreate(tanggal);
					session.save(btutrx);
				}
				
				itr2 = bundleItemTrx.iterator();
				float bundleUsed = 0;
				TbBundledItemUsedTrx itemUsedTrx = null;
							
				while(itr2.hasNext()){
					TbBundledItemUsedTrx bitrx = (TbBundledItemUsedTrx)itr2.next();
					bitrx.setTbBundledTrx(bundle);
					bundleUsed = bitrx.getNQty();
					item = bitrx.getMsItem();
					bitrx.setDWhnCreate(tanggal);
				

					String sql = "select {inv.*} from tb_item_inventory inv where inv.n_whouse_id=:wId "+
								 "AND inv.n_item_id=:itemId AND inv.n_item_inv_qty > :qty";
		
					SQLQuery query = session.createSQLQuery(sql);
					query.addEntity("inv", TbItemInventory.class);
					query.setInteger("wId", unit.getMsWarehouse().getNWhouseId().intValue());
					query.setInteger("itemId", item.getNItemId().intValue());
					query.setShort("qty", (short)0);
					
					List invlist = query.list();
					
					it = invlist.iterator();
					while(it.hasNext()){
						Object obj = it.next();
						

						
						TbItemInventory itemInv = null;
						if(obj instanceof TbItemInventory){
							itemInv= (TbItemInventory)obj;
						}
						else{
							
						}
						
						
						if(bundleUsed == 0){
							break;
						}
						if(itemInv.getNItemInvQty() <= bundleUsed){
							bundleUsed = bundleUsed - itemInv.getNItemInvQty();
							
							itemUsedTrx = bitrx.clone();
							itemUsedTrx.setTbBatchItem(itemInv.getTbBatchItem());
							itemUsedTrx.setNQty((short)itemInv.getNItemInvQty());
							
							session.save(itemUsedTrx);
							
							itemInv.setNItemInvQty((short)0);
							session.update(itemInv);
							
						}else{
							float sub = itemInv.getNItemInvQty() - bundleUsed;
							
							itemUsedTrx = bitrx.clone();
							itemUsedTrx.setTbBatchItem(itemInv.getTbBatchItem());
							itemUsedTrx.setNQty((short)bundleUsed);
							
							session.save(itemUsedTrx);
							
							itemInv.setNItemInvQty(sub);
							session.update(itemInv);
							bundleUsed = 0;
						}
					}
				}
				
			}
			
//			trans.commit();
			
			success = true;
			
		} catch (HibernateException e) {
//			trans.rollback();
			throw new VONEAppException(e.getMessage());
		}
		
		log.debug("PoliclinicDAO - saveTransaction - EXIT");
		return success;
		
	}
	
	
	public List  getTreatmentTrx(TbExamination note) throws VONEAppException{
		List result = null;
		
		
		try {
			
			Criteria crit = getCurrentSession().createCriteria(TbTreatmentTrx.class);
			crit.add(Restrictions.eq("tbExamination", note));
			result = crit.list();
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	
	public List getItemTrx(TbExamination note) throws VONEAppException{
		List result = null;
				
		String sql="select " +
		
						" trx.n_item_id as id, " +
						" item.v_item_code as code, " +
						" item.v_item_name as name,"+
						" sat.v_mitem_end_quantify as satuan, " +
						" sum(trx.n_qty) as qty, " +
						" sum(trx.n_amount_trx) as value, " +
						" sum(trx.n_disc_amount) as discount, " +
						" sum(trx.n_amount_after_disc) as total, " +
						" item.n_type as tipe, "+
						" trx.v_disc_type as tipeDiskon, "+
						" trx.n_disc_amount as jumlahDiskon, "+
						" item.n_r as r, "+
						" trx.aturan_pakai as aturan "+
						
				   "from " +
				   
				   		" tb_item_trx trx, " +
				   		" ms_item item, " +
				   		" ms_item_measurement sat " +
				   		
				   "where "+
				   
		           		" trx.n_item_id=item.n_item_id " +
		           		" and item.n_mitem_id=sat.n_mitem_id " +
		           		" and trx.n_note_id=:noteId " +
		           		
		           "group by id, code, name, satuan, tipe, tipeDiskon, jumlahDiskon, r, aturan";
	      
		try {
			
			
	
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("code", Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("satuan", Hibernate.STRING);
			query.addScalar("qty", Hibernate.INTEGER);
			query.addScalar("value", Hibernate.DOUBLE);
			query.addScalar("discount", Hibernate.DOUBLE);
			query.addScalar("total", Hibernate.DOUBLE);
			query.addScalar("tipe",Hibernate.SHORT);
			query.addScalar("tipeDiskon", Hibernate.STRING);
			query.addScalar("jumlahDiskon", Hibernate.DOUBLE);
			query.addScalar("r", Hibernate.SHORT);
			query.addScalar("aturan", Hibernate.STRING);
			query.setInteger("noteId", note.getNExamId().intValue());
	
			result = query.list();
		}
		catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	
	public List getBundleTrx(TbExamination note) throws VONEAppException{
		List result = null;
		
		
		
		try {
			
			Criteria crit = getCurrentSession().createCriteria(TbBundledTrx.class);
			crit.add(Restrictions.eq("tbExamination", note));
			
			result = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	


	public List getMiscTrx(TbExamination nota) throws VONEAppException{
		List result = null;
		
		
		try {
			
			Criteria crit = getCurrentSession().createCriteria(TbMiscTrx.class);
			crit.add(Restrictions.eq("tbExamination", nota));
			
			result = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
		
	}


	public List getRacikan(TbExamination exam) throws VONEAppException {
		List result = null;
		
		try{
			Criteria crit = getCurrentSession().createCriteria(TbDrugIngredients.class);
			crit.add(Restrictions.eq("tbExamination", exam));
			result = crit.list();
		}
		catch(HibernateException e){
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	

}
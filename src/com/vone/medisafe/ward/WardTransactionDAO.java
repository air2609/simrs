package com.vone.medisafe.ward;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
//import com.vone.medisafe.service.Service;

/**
 * WardTransactionDAO.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 09 2006
 * @category persistence
 */

public class WardTransactionDAO extends HibernateDaoSupport{
	
	public TbRegistration getLastRanap(TbMedicalRecord mr) throws VONEAppException{
		
    	TbRegistration registration = null;
    	
    	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    	
    	String query = " select " +
    				
    							" {reg.*} " +
    							
    					" from " +
    					
    							" tb_registration reg " +
    							
    					" where " +
    					
    							" reg.n_mr_id=:id " +
    							" and reg.v_reg_secondary_id like :regNumber " +
    							
    					" order by d_registration_date desc limit 1";
    	try {
    		
			registration = (TbRegistration)session.createSQLQuery(query)
							.addEntity("reg",TbRegistration.class)
							.setInteger("id",mr.getNMrId().intValue())
							.setString("regNumber", "I%")
							.uniqueResult();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
			
		}
		
    	return registration;
    }
	
	public TbRegistration getRanapByMrCode(String mrCode) throws VONEAppException{
		
    	TbRegistration registration = null;
    	
    	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    	
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append(" select ");
    	sb.append(" {reg.*} ");
    	
    	sb.append(" from " );
    	sb.append(" tb_registration reg, ");
    	sb.append(" tb_medical_record mr ");
    	
    	sb.append(" where ");
    	sb.append(" reg.n_mr_id=mr.n_mr_id ");
    	sb.append(" and reg.reg_status=:status ");
    	sb.append(" and reg.v_reg_secondary_id like :regNumber ");
    	sb.append(" and mr.v_mr_code=:mrCode ");
    	sb.append(" order by d_registration_date desc limit 1 ");
    	try {
    		
			registration = (TbRegistration)session.createSQLQuery(sb.toString())
							.addEntity("reg",TbRegistration.class)
							.setInteger("status", MedisafeConstants.REG_ACTIVE)
							.setString("regNumber", "I%")
							.setString("mrCode", mrCode)
							.uniqueResult();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
    	return registration;
    }
	
	
	public boolean save(TbExamination nota, Set<TbTreatmentTrx> treatmentSet, Set<TbItemTrx> itemSet) throws VONEAppException{
	
		boolean success = false;
		Session session = null;
		Transaction trans = null;
		Date tanggal = new Date();
		
		Iterator<TbTreatmentTrx> it = null;
		Iterator<TbItemTrx> itr = null;
		
		try {
			session = super.getSessionFactory().openSession();
			trans = session.beginTransaction();
			
			
			nota.setDWhnCreate(tanggal);
			Integer noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RANAP);
			nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, tanggal, nota.getMsUnit().getVUnitCode(), MedisafeConstants.RANAP));
			
			session.save(nota);
			
			it = treatmentSet.iterator();
			while(it.hasNext()){
				TbTreatmentTrx treat = it.next();
				treat.setDWhnCreate(tanggal);
				treat.setTbExamination(nota);
				session.save(treat);
			}
			
			float qtyUsed = 0;
			
			double disc_1 = 0;
			double trans_val_1 = 0;
			double trans_aft_disc_1 = 0;
			
			TbItemTrx realTrx = null;
			
			itr = itemSet.iterator();
			while(itr.hasNext()){
				TbItemTrx itmTrx = itr.next();
				itmTrx.setTbExamination(nota);
				itmTrx.setDWhnCreate(tanggal);
				
				qtyUsed = itmTrx.getNQty();
				
				disc_1 = itmTrx.getNDiscAmount()/itmTrx.getNQty();
				trans_val_1 = itmTrx.getNAmountTrx()/itmTrx.getNQty();
				trans_aft_disc_1 = itmTrx.getNAmountAfterDisc()/itmTrx.getNQty();
				
//				session.save(itmTrx);
				
				String sql = "select {inv.*} from tb_item_inventory inv where inv.n_whouse_id=:wId "+
	 			 			 "AND inv.n_item_id=:itemId AND inv.n_item_inv_qty > :qty";
	
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity("inv", TbItemInventory.class);
				query.setInteger("wId", nota.getMsUnit().getMsWarehouse().getNWhouseId().intValue());
				query.setInteger("itemId", itmTrx.getMsItem().getNItemId().intValue());
				query.setShort("qty", (short)0);
				
				List itemInvList = query.list();
//				List itemInvList = Service.getItemInventoryManager().getItemBaseOnUnit(itmTrx.getMsItem(), nota.getMsUnit());
				Iterator iter = itemInvList.iterator();
				
				while(iter.hasNext()){
					TbItemInventory inv = (TbItemInventory)iter.next();
					
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
			
			trans.commit();
			success = true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			success = false;
			trans.rollback();
			e.printStackTrace();
		}finally{
			if(session != null)session.close();
		}
		
		return success;
	}
	
	
	
	
	
}

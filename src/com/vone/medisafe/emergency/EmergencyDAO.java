package com.vone.medisafe.emergency;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;


/**
 * EmergencyDAO.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 06 2006
 * @category persistence (data model)
 */

public class EmergencyDAO extends HibernateDaoSupport{
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
	public boolean save(TbRegistration reg, TbExamination nota, Set<TbTreatmentTrx> treatTrx, Set<TbItemTrx> 
		itemTrx, MsUnit unit, Set<TbMiscTrx> miscTrx) throws VONEAppException
	{
		boolean success = false;
//		Session session = null;
//		Transaction trans = null;
		Date tanggal = new Date();
		
		Iterator<TbTreatmentTrx> it = null;
		Iterator<TbItemTrx> itr = null;
		
		try {
//			session = getCurrentSession();
//			trans = session.beginTransaction();
			
			nota.setDWhnCreate(tanggal);
			
			
			
			
			
			//jika belum daftar set registraton number
			if(reg.getNRegId() == null){
				
				reg.setNRegId(IdsServiceLocator.getIdsManager().
						getSequence(MedisafeConstants.REGISTRATION_ID));
				
				Integer registrationNumber = IdsServiceLocator.getIdsManager().getSequence(
						MedisafeConstants.REGISTRATION_NUMBER);
				reg.setVRegSecondaryId(MedisafeUtil.convertToRegistrationCode(registrationNumber,new Date(),
						unit.getVUnitCode()));
				reg.setDRegistrationDate(tanggal);
				getHibernateTemplate().save(reg);
				
			}
			
			
				
			
			
						
			nota.setTbRegistration(reg);
			Integer noNota;
			if(reg.getVRegSecondaryId().charAt(0) == 'I'){
				noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RANAP);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, tanggal, unit.getVUnitCode(), 
						MedisafeConstants.RANAP));
			}
			else{
				noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RAJAL);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, tanggal, unit.getVUnitCode(), 
						MedisafeConstants.NON_RANAP));
			}
			
			
//			session.save(nota);
			getHibernateTemplate().save(nota);
			
			it = treatTrx.iterator();
			while(it.hasNext()){
				TbTreatmentTrx treat = it.next();
				treat.setDWhnCreate(tanggal);
				treat.setTbExamination(nota);
				getHibernateTemplate().save(treat);
//				session.save(treat);
			}
			
			float qtyUsed = 0;
			double disc_1 = 0;
			double trans_val_1 = 0;
			double trans_aft_disc_1 = 0;
			
			TbItemTrx realTrx = null;
			
			itr = itemTrx.iterator();
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
	
				SQLQuery query = getCurrentSession().createSQLQuery(sql);
				query.addEntity("inv", TbItemInventory.class);
				query.setInteger("wId", unit.getMsWarehouse().getNWhouseId().intValue());
				query.setInteger("itemId", itmTrx.getMsItem().getNItemId().intValue());
				query.setShort("qty", (short)0);
				
				List itemInvList = query.list();
				
//				List itemInvList = Service.getItemInventoryManager().getItemBaseOnUnit(itmTrx.getMsItem(), unit);
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
						
//						session.save(realTrx);
						getHibernateTemplate().save(realTrx);
						
						inv.setNItemInvQty((short)0);
						
						getHibernateTemplate().update(inv);
						
//						session.update(inv);
						
					}else{
						float sub = inv.getNItemInvQty() - qtyUsed;
						
						realTrx = itmTrx.clone();
						realTrx.setTbBatchItem(inv.getTbBatchItem());
						realTrx.setNQty(qtyUsed);
						realTrx.setNAmountAfterDisc(qtyUsed * trans_aft_disc_1);
						realTrx.setNAmountTrx(qtyUsed * trans_val_1);
						realTrx.setNDiscAmount(qtyUsed * disc_1);
						
						getHibernateTemplate().save(realTrx);
						
//						session.save(realTrx);
						
						inv.setNItemInvQty(sub);
						
						getHibernateTemplate().update(inv);
						
//						session.update(inv);
						qtyUsed = 0;
					}
				}
				
				
			}
			
			for(TbMiscTrx misctrx : miscTrx){
				
				misctrx.setDWhnCreate(tanggal);
				misctrx.setTbExamination(nota);
				
				getHibernateTemplate().save(misctrx);
				
//				session.save(misctrx);
			}
			
//			trans.commit();
			success = true;
		}catch (Exception e){
			
			
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		return success;
	}

}

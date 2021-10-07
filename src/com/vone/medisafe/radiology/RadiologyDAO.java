package com.vone.medisafe.radiology;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbRoomReservation;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;

public class RadiologyDAO extends HibernateDaoSupport{
	
	Logger log = Logger.getLogger(RadiologyDAO.class);
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    

	
	public List getMedician(String sMedicCategory) throws VONEAppException {
		List result = null;
		String sqlRadiografer = 
			" select  " +
			" 	{staff.*} " +
			" from  " +
			" 	ms_doctor doctor,  " +
			" 	ms_staff staff " +
			" where  " +
			" 	doctor.n_staff_id = staff.n_staff_id " +
			" 	and doctor.n_msgroup_id =:gid ";
		Session session = null;
		log.debug(sqlRadiografer);
		try {
			session = getCurrentSession();
						
			result = session.createSQLQuery(sqlRadiografer)
					.addEntity("staff", MsStaff.class)
					.setShort("gid", MedisafeConstants.GRUP_RADIOGRAFER)
//					.setString("v_msgroup_name", sMedicCategory)
					.list();
			
		} catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}

		return result;
	}
	
	public boolean saveTransaction(MsPatient patient, TbExamination nota, Set treatmentTrx, Set itemTrx, 
			MsUnit unit, boolean isRanap)throws VONEAppException 
	{
		log.debug("RadiologyDAO - saveTransaction - ENTER");
		boolean success = false;
		Session session = null;
		boolean isRujukan = false;
				
		Date tanggal = new Date();
		
		if(patient.getNPatientId() == null) 
			isRujukan = true;
		try {
			session = getCurrentSession();
			if(isRujukan)
			{
				patient.setDWhnCreate(tanggal);
				patient.setNPatientId(IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.PATIENT_ID));
				session.save(patient);
			}
			
			nota.setMsPatient(patient);
			nota.setDWhnCreate(tanggal);
			Integer noNota = null;
			String sCode = unit.getVUnitCode();
			if (sCode.equals("TREATMILL"))
				sCode = "TRD";
			else if(sCode.equals("RONTGEN"))
				sCode = "RAD";
			else if(sCode.equals("ENDOSKLERO"))
				sCode = "ENDO";
			
			
			if(isRanap){
				noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RANAP);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, tanggal, sCode, MedisafeConstants.RANAP));
			}
			
			else 
			{
				noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RAJAL);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, tanggal, sCode, MedisafeConstants.NON_RANAP));
			}
						
			session.save(nota);
			
			Iterator it = treatmentTrx.iterator();
			while(it.hasNext()){
				TbTreatmentTrx treatTrx = (TbTreatmentTrx)it.next();
				treatTrx.setTbExamination(nota);
				treatTrx.setDWhnCreate(tanggal);
				session.save(treatTrx);
			}
			
			
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
			
				
			success = true;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		
		log.debug("RadiologyDAO - saveTransaction - EXIT");
		return success;
		
	}



	public MsUser getUser(MsUser user) throws VONEAppException{
		return (MsUser) getCurrentSession().get(MsUser.class, user.getNUserId());
	}



	public List getKelasForSelect() throws VONEAppException{
		Session session = getCurrentSession();
		return session.createQuery("from MsTreatmentClass order by NTclassId").list(); 
	}



	public TbRoomReservation getTbRoomReservation(TbRegistration reg) throws VONEAppException{
		List list = getCurrentSession()
			.createQuery("from TbRoomReservation where n_reg_id = :reg")
			.setInteger("reg", reg.getNRegId())
			.list();
		if(list.size()>0)
			return (TbRoomReservation)list.iterator().next();
		else
			return null;
	}
}

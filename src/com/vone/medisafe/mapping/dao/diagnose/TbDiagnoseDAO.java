package com.vone.medisafe.mapping.dao.diagnose;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.item.MsItem;





/**
 * Data access object (DAO) for domain model class TbDiagnose.
 * @see com.vone.medisafe.mapping.TbDiagnose
 * @author MyEclipse - Hibernate Tools
 */
public class TbDiagnoseDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TbDiagnoseDAO.class);

	protected void initDao() {
		//do nothing
	}
	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	public void saveIcdDiagnose(TbIcdDiagnose icdDiagnose) {
        log.debug("saving TbDiagnose instance");
        try {
            getHibernateTemplate().saveOrUpdate(icdDiagnose);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    public void save(TbDiagnose transientInstance) {
        log.debug("saving TbDiagnose instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TbDiagnose persistentInstance) {
        log.debug("deleting TbDiagnose instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TbDiagnose findById( java.lang.String id) {
        log.debug("getting TbDiagnose instance with id: " + id);
        try {
            TbDiagnose instance = (TbDiagnose) getHibernateTemplate()
                    .get("com.vone.medisafe.mapping.TbDiagnose", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TbDiagnose instance) {
        log.debug("finding TbDiagnose instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.vone.medisafe.mapping.TbDiagnose")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public TbDiagnose merge(TbDiagnose detachedInstance) {
        log.debug("merging TbDiagnose instance");
        try {
            TbDiagnose result = (TbDiagnose) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbDiagnose instance) {
        log.debug("attaching dirty TbDiagnose instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TbDiagnose instance) {
        log.debug("attaching clean TbDiagnose instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public boolean saveDiagnoseAndReceipt(MsPatient patient,short diagnoseType, TbExamination nota, List<TbItemTrx> itemTrx, List<TbDrugIngredients> racikans,
			Integer warehouseId, TbDiagnose diagnose, TbIcdDiagnose icd) throws VONEAppException
	{
		Session session = getCurrentSession();
		boolean sukses = false;
		
		Transaction trans = session.beginTransaction();
		try{
			Set<TbIcdDiagnose> icds = diagnose.getTbIcdDiagnoses();
			session.saveOrUpdate(diagnose);
			
			for(TbIcdDiagnose icdd : icds){
				icdd.setTbDiagnose(diagnose);
				session.saveOrUpdate(icdd);
			}
			
			Integer noNota = null;
			if(nota != null){
				if(diagnoseType == MedisafeConstants.DIAGNOSA_RAJAL){
					noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RAJAL);
					nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, nota.getDWhnCreate(), "APTK",
									MedisafeConstants.NON_RANAP));
				}else{
					noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RANAP);
					nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, nota.getDWhnCreate(), "APTK", 
							MedisafeConstants.RANAP));
				}
				nota.setMsPatient(patient);
				session.save(nota);
				
				MsItem item = null;
				double trans_val_1 = 0;
				TbItemTrx realTrx = null;
				TbPatientInventory patInv = null;
				float qtyUsed = 0;
				
				String sql = " select {itminv.*} from tb_item_inventory itminv where "+
				 " itminv.n_item_id=:itemId and itminv.n_whouse_id=:wid and itminv.n_item_inv_qty > 0";
				
				
				for(TbItemTrx trx : itemTrx){
					
					trx.setTbExamination(nota);			
					item = trx.getMsItem();
					qtyUsed = trx.getNQty();
					
									
					trans_val_1 = trx.getNAmountTrx()/trx.getNQty();
					
					SQLQuery query = session.createSQLQuery(sql);
					query.addEntity("itminv", TbItemInventory.class);
					query.setInteger("itemId", item.getNItemId().intValue());
					query.setInteger("wid", warehouseId.intValue());
					
					List<TbItemInventory> itemInvList = query.list();
					for(TbItemInventory inv : itemInvList){
						if(qtyUsed == 0){
							break;
						}
						
						if(inv.getNItemInvQty() <= qtyUsed && inv.getNItemInvQty() > 0){
							qtyUsed = qtyUsed - inv.getNItemInvQty();
							
							realTrx = trx.clone();
							realTrx.setTbBatchItem(inv.getTbBatchItem());
							realTrx.setNQty(inv.getNItemInvQty());
							realTrx.setNAmountAfterDisc(inv.getNItemInvQty() * trans_val_1);
							realTrx.setNAmountTrx(inv.getNItemInvQty() * trans_val_1);
							realTrx.setNDiscAmount(0.0);
							realTrx.setAturanPakai(trx.getAturanPakai());
							
													
							session.save(realTrx);
							
							inv.setNItemInvQty(0);
							session.save(inv);
							
							if(diagnoseType == MedisafeConstants.DIAGNOSA_RANAP){
								patInv = new TbPatientInventory();
								patInv.setMsItem(item);
								patInv.setMsPatient(patient);
								patInv.setNQty(realTrx.getNQty().shortValue());
								patInv.setTbItemTrx(realTrx);
								patInv.setDWhnCreate(realTrx.getDWhnCreate());
								patInv.setVWhoCreate(trx.getVWhoCreate());
								patInv.setTbRegistration(nota.getTbRegistration());
								session.save(patInv);
								
							}
						}
						else if(inv.getNItemInvQty() > qtyUsed){
							float sub = inv.getNItemInvQty() - qtyUsed;
							
							realTrx = trx.clone();
							realTrx.setTbBatchItem(inv.getTbBatchItem());
							realTrx.setNQty(qtyUsed);
							realTrx.setNAmountAfterDisc(qtyUsed * trans_val_1);
							realTrx.setNAmountTrx(qtyUsed * trans_val_1);
							realTrx.setNDiscAmount(0.0);
							realTrx.setAturanPakai(trx.getAturanPakai());
							
							session.save(realTrx);
							
							inv.setNItemInvQty(sub);

							session.save(inv);
							qtyUsed = 0;
							
							if(diagnoseType == MedisafeConstants.DIAGNOSA_RANAP){
								patInv = new TbPatientInventory();
								patInv.setMsItem(item);
								patInv.setMsPatient(patient);
								patInv.setNQty(realTrx.getNQty().shortValue());
								patInv.setTbItemTrx(realTrx);
								patInv.setDWhnCreate(realTrx.getDWhnCreate());
								patInv.setVWhoCreate(trx.getVWhoCreate());
								patInv.setTbRegistration(nota.getTbRegistration());

								session.save(patInv);
								
							}
						}
					}
				}
				

				TbDrugIngredientsDetail realDetil = null;
				float itemUsed = 0;
				
				for(TbDrugIngredients racikan : racikans){
					racikan.setTbExamination(nota);
					session.save(racikan);
					
					Set<TbDrugIngredientsDetail> detils = racikan.getTbDrugIngredientsDetails();
					for(TbDrugIngredientsDetail detil : detils){
						itemUsed = detil.getNDingrDetQty();
						item = detil.getMsItem();
						
						SQLQuery query = session.createSQLQuery(sql);
						query.addEntity("itminv", TbItemInventory.class);
						query.setInteger("itemId", item.getNItemId().intValue());
						query.setInteger("wid", warehouseId.intValue());
										
						List<TbItemInventory> invlist = query.list();
						for(TbItemInventory itemInv : invlist){
							if(itemUsed == 0){
								break;
							}
							
							if(itemInv.getNItemInvQty() <= itemUsed && itemInv.getNItemInvQty() > 0){
								itemUsed = itemUsed - itemInv.getNItemInvQty();
								
								realDetil = detil.clone();
								realDetil.setTbDrugIngredients(racikan);
								realDetil.setDWhnCreate(racikan.getDWhnCreate());
								realDetil.setVDingrDetQuantify(racikan.getNDingrQuantify());
								realDetil.setNDingrDetQty(itemInv.getNItemInvQty());
								realDetil.setTbBatchItem(itemInv.getTbBatchItem());
								
								
								session.save(realDetil);
									
								itemInv.setNItemInvQty(0);
								session.update(itemInv);
							}
							else if(itemInv.getNItemInvQty() > itemUsed){
								float sub = itemInv.getNItemInvQty() - itemUsed;
								
								realDetil = detil.clone();
								realDetil.setTbDrugIngredients(racikan);
								realDetil.setDWhnCreate(racikan.getDWhnCreate());
								realDetil.setVDingrDetQuantify(racikan.getNDingrQuantify());
								realDetil.setNDingrDetQty(itemUsed);
								realDetil.setTbBatchItem(itemInv.getTbBatchItem());
								
								session.save(realDetil);
								
								
								itemInv.setNItemInvQty(sub);
								
								session.save(itemInv);
								itemUsed = 0;
							}
						}
					}
				}
				
			}
			
			TbRegistration reg = getRegistrationByCode(diagnose.getNRegId());
			reg.setAntrianStatus(1);
			session.update(reg);
			
			
			trans.commit();
			sukses = true;
		}
		catch(Exception e){
			trans.rollback();
		}
		
		return sukses;

	}

	public List<TbDiagnose> getDiagnosePatient(Integer mrId) throws VONEAppException{
		List<TbDiagnose> diagnose = null;
		
		Session session = getCurrentSession();
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select {diag.*} ");
		sb.append(" from ");
		sb.append(" tb_diagnose diag ");
		
		sb.append(" where ");
		sb.append(" diag.n_mr_id=:mrId");
		sb.append(" order by d_whn_create asc");
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("diag", TbDiagnose.class);
		query.setInteger("mrId", mrId);
		
		diagnose = query.list();
		return diagnose;
	}

	public TbDiagnose getDiagnoseByRegId(Integer nRegId) throws VONEAppException {
		TbDiagnose diagnose = null;
		Session session = getCurrentSession();
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select {diag.*} ");
		sb.append(" from ");
		sb.append(" tb_diagnose diag ");
		
		sb.append(" where ");
		sb.append(" diag.n_reg_id=:mrId");
		
		
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.addEntity("diag", TbDiagnose.class);
		query.setInteger("mrId", nRegId);
		
		
		diagnose = (TbDiagnose)query.uniqueResult();;
		return diagnose;
	}
	
	public TbRegistration getRegistrationByCode(Integer regId) throws VONEAppException{
    	
    	TbRegistration reg = null;
    	
    	StringBuffer query = new StringBuffer();
    	
    	query.append(" select ");
    	query.append(" {reg.*} ");
    							
    	query.append(" from ");
    	query.append(" tb_registration reg ");
    	
    	
    	query.append(" where " );
    	query.append(" reg.n_reg_id=:id ");
    	
    		try {
    			
				reg = (TbRegistration)getCurrentSession().createSQLQuery(query.toString())
					.addEntity("reg",TbRegistration.class)
					.setInteger("id",regId)
					.uniqueResult();
				
			} catch (HibernateException e) {
				
				throw new VONEAppException(e.getMessage());
			}
    		
    		
    	return reg;
    }


}
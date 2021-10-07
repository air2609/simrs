package com.vone.medisafe.apotik;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.antrian.MsAntrian;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemInventory;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.service.Service;


/**
 * ApotikDAO.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 15 2006
 * @category persistance (Data Model - M)
 */

public class ApotikDAO extends HibernateDaoSupport implements PlatformTransactionManager{
	
	private static final Logger log = Logger.getLogger(ApotikDAO.class);

	
	public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
	
	
	public boolean save(MsPatient patient,TbExamination nota, Set<TbItemTrx> itemTrx, Set<TbDrugIngredients> 
		racikans, MsUnit unit, boolean isRanap, Set<TbMiscTrx> miscs) throws VONEAppException
	{
		
		boolean success = false;
		boolean isRujukan = false;
		
		
		Session session = getCurrentSession();
		
		
		Transaction trans = null;
				
		Date tanggal = new Date();
		

		
		if(patient.getNPatientId() == null) 
			isRujukan = true;
		try {
			
		
			trans = session.beginTransaction();

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
			
			float qtyUsed = 0;
			double disc_1 = 0;
			double trans_val_1 = 0;
			double trans_aft_disc_1 = 0;
			
			MsItem item = null;
			Iterator itr = null;
			TbItemTrx realTrx = null;
			
			String sql = " select {itminv.*} from tb_item_inventory itminv where "+
			 " itminv.n_item_id=:itemId and itminv.n_whouse_id=:wid and itminv.n_item_inv_qty > 0";
			
			TbPatientInventory patInv = null;
			
			for(TbItemTrx trx : itemTrx){
				trx.setTbExamination(nota);
				trx.setDWhnCreate(tanggal);
				item = trx.getMsItem();
				qtyUsed = trx.getNQty();
				
				
				disc_1 = trx.getNDiscAmount()/trx.getNQty();
				trans_val_1 = trx.getNAmountTrx()/trx.getNQty();
				trans_aft_disc_1 = trx.getNAmountAfterDisc()/trx.getNQty();

				
				
				
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity("itminv", TbItemInventory.class);
				query.setInteger("itemId", item.getNItemId().intValue());
				query.setInteger("wid", unit.getMsWarehouse().getNWhouseId().intValue());
				
				List itemInvList = query.list();
				
				itr = itemInvList.iterator();
				
				while(itr.hasNext()){
					TbItemInventory inv = (TbItemInventory)itr.next();
					
					if(qtyUsed == 0){
						break;
					}
					if(inv.getNItemInvQty() <= qtyUsed){
						qtyUsed = qtyUsed - inv.getNItemInvQty();
						
						realTrx = trx.clone();
						realTrx.setTbBatchItem(inv.getTbBatchItem());
						realTrx.setAturanPakai(trx.getAturanPakai());
						realTrx.setNQty(inv.getNItemInvQty());
						realTrx.setNAmountAfterDisc(inv.getNItemInvQty() * trans_aft_disc_1);
						realTrx.setNAmountTrx(inv.getNItemInvQty() * trans_val_1);
						realTrx.setNDiscAmount(inv.getNItemInvQty() * disc_1);
						
						session.save(realTrx);
						
						inv.setNItemInvQty(0);


						session.save(inv);
						
						if(isRanap){
							patInv = new TbPatientInventory();
							patInv.setMsItem(item);
							patInv.setMsPatient(patient);
							patInv.setNQty(realTrx.getNQty().shortValue());
							patInv.setTbItemTrx(realTrx);
							patInv.setDWhnCreate(tanggal);
							patInv.setVWhoCreate(trx.getVWhoCreate());
							patInv.setTbRegistration(nota.getTbRegistration());
							session.save(patInv);
							
						}
	
						
					}else{
						float sub = inv.getNItemInvQty() - qtyUsed;
						
						realTrx = trx.clone();
						realTrx.setTbBatchItem(inv.getTbBatchItem());
						realTrx.setNQty(qtyUsed);
						realTrx.setAturanPakai(trx.getAturanPakai());
						realTrx.setNAmountAfterDisc(qtyUsed * trans_aft_disc_1);
						realTrx.setNAmountTrx(qtyUsed * trans_val_1);
						realTrx.setNDiscAmount(qtyUsed * disc_1);
						
						session.save(realTrx);
						
						inv.setNItemInvQty(sub);

						session.save(inv);
						qtyUsed = 0;
						
						if(isRanap){
							patInv = new TbPatientInventory();
							patInv.setMsItem(item);
							patInv.setMsPatient(patient);
							patInv.setNQty(realTrx.getNQty().shortValue());
							patInv.setTbItemTrx(realTrx);
							patInv.setDWhnCreate(tanggal);
							patInv.setVWhoCreate(trx.getVWhoCreate());
							patInv.setTbRegistration(nota.getTbRegistration());
							session.save(patInv);
							
						}
						
						
					}
				}
				
			}
			
			log.debug("UPDATE DATA ITEM INVENTORY BERDASARKAN ITEM TRX SUKSES");
			
			TbDrugIngredientsDetail detil = null;
			TbDrugIngredientsDetail realDetil = null;
			
			for(TbDrugIngredients racikan : racikans){
				racikan.setDWhnCreate(tanggal);
				racikan.setTbExamination(nota);
				

				session.save(racikan);
								
				float itemUsed = 0;
				
				Set racikanDetil = racikan.getTbDrugIngredientsDetails();
				
				itr = racikanDetil.iterator();
				while(itr.hasNext()){
					
					detil = (TbDrugIngredientsDetail)itr.next();
					
					detil.setTbDrugIngredients(racikan);
					detil.setDWhnCreate(tanggal);
					itemUsed = detil.getNDingrDetQty();
					item = detil.getMsItem();
					
					SQLQuery query = session.createSQLQuery(sql);
					query.addEntity("itminv", TbItemInventory.class);
					query.setInteger("itemId", item.getNItemId().intValue());
					query.setInteger("wid", unit.getMsWarehouse().getNWhouseId().intValue());
					
					List invlist = query.list();
					
					Iterator iter = invlist.iterator();
					while(iter.hasNext()){
						Object obj = iter.next();
						
						
						TbItemInventory itemInv = null;
						if(obj instanceof TbItemInventory){
							itemInv= (TbItemInventory)obj;
							
							
						}
						else{
							
						}
						
						
						if(itemUsed == 0){
							break;
						}
						if(itemInv.getNItemInvQty() <= itemUsed){
							itemUsed = itemUsed - itemInv.getNItemInvQty();
							
							realDetil = detil.clone();
							realDetil.setNDingrDetQty(itemInv.getNItemInvQty());
							realDetil.setTbBatchItem(itemInv.getTbBatchItem());
							
							session.save(realDetil);
								
							itemInv.setNItemInvQty(0);
							session.update(itemInv);
							
						}else{
							float sub = itemInv.getNItemInvQty() - itemUsed;
							
							realDetil = detil.clone();
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
			
			for(TbMiscTrx trx : miscs){
				trx.setTbExamination(nota);
				trx.setDWhnCreate(tanggal);
				
				session.save(trx);
			}
			
			trans.commit();
			success = true;
			

		} catch (HibernateException e) {
			
			trans.rollback();
			return false;
		}
			

		return success;
	}
	
	
	public List<TbItemTrx> getItemTrxReturnable(TbExamination nota) throws VONEAppException{
		List<TbItemTrx> result = null;
		
		
		String sql = "select {trx.*} from tb_item_trx trx, ms_item item where trx.n_item_id=item.n_item_id"+
					   " AND item.v_item_returnable=:isReturnable AND trx.n_note_id=:notaId";
		
		try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addEntity("trx", TbItemTrx.class);
			query.setString("isReturnable", "Y");
			query.setInteger("notaId",nota.getNExamId().intValue());
			
			result = query.list();
			
		} catch (HibernateException e) {
			
			new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	
	public List<TbDrugIngredients> getRacikanTrx(TbExamination nota) throws VONEAppException{
		
		List<TbDrugIngredients> result = null;
				
		try {
			
			Criteria crit = getCurrentSession().createCriteria(TbDrugIngredients.class);
			crit.add(Restrictions.eq("tbExamination", nota));
			
			result = crit.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	
	
	
	
	public List getDetilRacikan(TbDrugIngredients racikan) throws VONEAppException
	{
		String query = "select detil from TbDrugIngredientsDetail as detil where detil.tbDrugIngredients=:racikan";
		String field = "racikan";
		List result = getHibernateTemplate().findByNamedParam(query, field, racikan);
		return result;
	}
	
	
	public boolean saveItemRetur(TbReturPharmacyTrx retur, Set<TbReturPharmacyDetailTrx> detilRetur, 
			Set<TbPatientInventory> patInvs, Set<TbItemTrx> itemsTrx, MsUnit unit)
		throws VONEAppException
	{
		boolean success = false;
		TbItemInventory inventory;
		Session session = null;
		Transaction trans = null;
		float jumlah =0;
		

		
		try {
			
			session = getCurrentSession();
			trans = session.beginTransaction();

			session.save(retur);
		
			for(TbReturPharmacyDetailTrx detil : detilRetur){
				
				detil.setTbReturPharmacyTrx(retur);
				

				session.save(detil);
				
			}
			
			StringBuffer sql = new StringBuffer();
			
			sql.append(" select ");
			sql.append(" {itminv.*} ");
			
			sql.append(" from " );
			sql.append(" tb_item_inventory itminv ");
			
			sql.append(" where ");
			sql.append(" itminv.n_item_id=:itemId ");
			sql.append(" and itminv.n_batch_id=:batchId ");
			sql.append(" and itminv.n_whouse_id=:wid ");

			
			for(TbItemTrx trx : itemsTrx){
				
				String params = sql.toString() + " item id = "+ trx.getMsItem().getNItemId().toString() + " batch id ="+ 
				trx.getTbBatchItem().getNBatchId().toString()
				+" warehouse id="+unit.getMsWarehouse().getNWhouseId().toString();
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.addEntity("itminv", TbItemInventory.class);
				query.setInteger("itemId", trx.getMsItem().getNItemId().intValue());
				query.setInteger("batchId", trx.getTbBatchItem().getNBatchId());
				query.setInteger("wid", unit.getMsWarehouse().getNWhouseId().intValue());
				
				inventory =(TbItemInventory) query.uniqueResult();
				
				System.out.println(params);
				
				if(trx.getNQty() != null){
					jumlah = inventory.getNItemInvQty() + trx.getNQty();
					inventory.setNItemInvQty(jumlah);
						

					session.update(inventory);

				}
								
			}
			
			if(patInvs != null){

				for(TbPatientInventory pat : patInvs){
					pat.setDWhnCreate(new Date());
					session.save(pat);
				}
			}
			
			/**
			 * accounting occurs when data is validated..
			//coaAr -
			//coaInv +
			double totRetur = retur.getNTrxValue();

			JournalBeanHandler jhandler = new JournalBeanHandler(MedisafeConstants.ACCT_PMT_STR);
			getHibernateTemplate().saveOrUpdate(unit);
			MsCoa coaInv = unit.getMsWarehouse().getMsCoa();
			MsCoa coaAR = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
			
			Date tanggal = new Date();
			jhandler.addJournal(retur.getVReturCode() , "",totRetur, MedisafeConstants.ACCT_CREDIT, tanggal, retur.getVWhoCreate() , coaAR);
			jhandler.addJournal(retur.getVReturCode() , "",totRetur, MedisafeConstants.ACCT_DEBIT, tanggal, retur.getVWhoCreate() , coaInv);

			if (!jhandler.isBalance())
				throw new VONEAppException(MessagesService.getKey("master.coa.journal.discrepancy.internal"));
						
			Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
			while (it.hasNext()){
				getHibernateTemplate().save(it.next());
			}*/
						
			trans.commit();
			success = true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			
		}

		return success;
	}
	
	
	public List getReturnableItems(TbRegistration patient) throws VONEAppException{
	
		List result = null;
		Session session = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select ");
		sb.append(" patin.n_item_id as id, ");
		sb.append(" item.v_item_code as code, ");
		sb.append(" item.v_item_name as name, ");
		sb.append(" item.n_type as tipe, ");
		sb.append(" sat.v_mitem_end_quantify as satuan, ");
		sb.append(" sum(patin.n_qty) as masuk, ");
		sb.append(" sum(patin.n_qty_out) as keluar ");
		
		
		sb.append(" from ");
		sb.append(" tb_patient_inventory patin, ");
		sb.append(" ms_item item, ");
		sb.append(" ms_item_measurement sat ");
		
		sb.append(" where " );
		sb.append(" patin.n_item_id = item.n_item_id ");
		sb.append(" and item.n_mitem_id=sat.n_mitem_id ");
		sb.append(" and item.v_item_returnable=:isReturnable ");
		sb.append(" and patin.n_reg_id=:patientId ");
		
		sb.append(" group by id,code,name,tipe,satuan ");;

		try {
			session = getCurrentSession();
			
			SQLQuery query = session.createSQLQuery(sb.toString());
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("code", Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("tipe", Hibernate.SHORT);
			query.addScalar("satuan", Hibernate.STRING);
			query.addScalar("masuk", Hibernate.INTEGER);
			query.addScalar("keluar", Hibernate.INTEGER);
			
			query.setInteger("patientId", patient.getNRegId());
			query.setString("isReturnable", "Y");
			result = query.list();

		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		} 

		return result;
		
	}
	
	public List getExaminationBaseOnReceipt(String receiptNumber) throws VONEAppException
	{
		List result = null;
	
		
		String sql = "select {nota.*} from tb_examination nota where nota.v_recipe_no"+
					 " LIKE :receipt";
		
		try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql);
			query.addEntity("nota", TbExamination.class);
			query.setString("receipt", receiptNumber);
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}
	
	
	public List<TbReturPharmacyTrx> searchReturItems(String returNote, String patientName, 
			Date startDate, Date endDate) throws VONEAppException
	{
		List result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" {retur.*} ");
		
		sql.append(" from ");
		sql.append(" tb_retur_pharmacy_trx retur, ");
		sql.append(" ms_patient pat ");
		
		sql.append(" where ");
		sql.append(" retur.n_patient_id=pat.n_patient_id ");
		sql.append(" and retur.v_retur_code like :code ");
		sql.append(" and pat.v_patient_name like :name ");
		sql.append(" and retur.n_status!=:status ");
		sql.append(" and retur.d_whn_create between :tanggalMulai and :tglTerakhir ");
		sql.append(" limit 100 ");
		             
		try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addEntity("retur", TbReturPharmacyTrx.class);
			query.setString("code", returNote);
			query.setString("name",patientName);
			query.setShort("status", (short)MedisafeConstants.CANCEL_NOTE);
			query.setTimestamp("tanggalMulai", startDate);
			query.setTimestamp("tglTerakhir", endDate);
			result = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
		
	}
	
	public List<TbReturPharmacyDetailTrx> getReturDetil(Integer returId) throws VONEAppException{
		List<TbReturPharmacyDetailTrx> result = null;
		
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ");
		sql.append(" {detil.*} ");
		
		sql.append(" from ");
		sql.append(" tb_retur_pharmacy_detail_trx detil ");
		
		sql.append(" where ");
		sql.append(" detil.n_retur_id = :returId ");
		
		try {
		
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setInteger("returId", returId.intValue());
			query.addEntity("detil",TbReturPharmacyDetailTrx.class);
			
			result = query.list();
			
		} catch (HibernateException e) {
		
			throw new VONEAppException(e.getMessage());
		}
		
		return result;
	}

	public void commit(TransactionStatus arg0) throws TransactionException {
		// TODO Auto-generated method stub
		
	}


	public TransactionStatus getTransaction(TransactionDefinition arg0) throws TransactionException {
		// TODO Auto-generated method stub
		return null;
	}


	public void rollback(TransactionStatus arg0) throws TransactionException {
		// TODO Auto-generated method stub
		
	}
	
	public boolean cancelRetur(TbReturPharmacyTrx retur, List<TbReturPharmacyDetailTrx> detils,
			MsUnit unit) throws VONEAppException
	{
		
		boolean success = false;
								
		PlatformTransactionManager txManager = Service.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		
		
		TransactionStatus status = txManager.getTransaction(def);
		
		retur.setNStatus((short)MedisafeConstants.CANCEL_NOTE);
		
		try {
			getHibernateTemplate().update(retur);
			
			String query = "select inv from TbItemInventory as inv where inv.msItem=:item and "+
			"inv.tbBatchItem=:batch and inv.msWarehouse=:wId";
			String[] field = new String[]{"item","batch","wId"};
			
			detils = getReturDetil(retur.getNReturId());
			
			float qty = 0;
			if(detils.size() != 0){
				
				for(TbReturPharmacyDetailTrx detil : detils){
					
//					qtyUsed = detil.getNQty();
					
					
					
					List itemInvList = getHibernateTemplate().findByNamedParam(query, field, 
							new Object[]{detil.getMsItem(),detil.getTbBatchItem(),unit.getMsWarehouse()});
					
					TbItemInventory inv = (TbItemInventory)itemInvList.get(0);
					qty = inv.getNItemInvQty() + detil.getNQty();
					inv.setNItemInvQty(qty);
					
					getHibernateTemplate().update(inv);
					
					
					
				}
			}
				
			
		} catch (DataAccessException e) {
			txManager.rollback(status);
			e.printStackTrace();
			return false;
		}
		
		txManager.commit(status);
		success = true;
		return success;
	}
	
	public boolean validateRetur(TbReturPharmacyTrx retur, MsUnit unit) throws VONEAppException{
		boolean success = false;
		
		StringBuffer memo = new StringBuffer();
		
		JournalBeanHandler jhandler = new JournalBeanHandler(MedisafeConstants.ACCT_AR_STR);

		MsCoa coaAR;
		MsCoa coaIncome;
//		getHibernateTemplate().saveOrUpdate(unit);
		MsCoa coaInv;
		MsCoa coaCogs;
		MsItem item;
		
		String voucherNo;
		String currentUser = retur.getVWhoCreate();
		
		Double totTrans;
		Double totCogs;
		Date tgl = new Date();

		coaAR = jhandler.getCoaFromGim(MedisafeConstants.COA_DEFAULT_INPATIENT_AR);
		
		PlatformTransactionManager txManager = Service.getTrxManager();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = txManager.getTransaction(def);
		try {
			getHibernateTemplate().saveOrUpdate(retur);
			getHibernateTemplate().saveOrUpdate(unit);
			coaInv = unit.getMsWarehouse().getMsCoa();
			
			//todo save journal retur begin
			
			List<TbReturPharmacyDetailTrx> detilList = getReturDetil(retur.getNReturId());
			for(TbReturPharmacyDetailTrx detil : detilList){
				item = detil.getMsItem();
				coaCogs = item.getItemCogsNo();
				coaIncome = item.getItemSellAcctNo();
				
				if (coaInv == null)
					throw new VONEAppException(MessagesService.getKey("trx.coa.inventory.unit.not.found"));				
				if (coaIncome == null)
					throw new VONEAppException(MessagesService.getKey("trx.item.sell.coa.null"));
				if (coaCogs == null)
					throw new VONEAppException(MessagesService.getKey("trx.item.cogs.coa.null"));				
				
				totTrans = detil.getNValue(); //todo please check this one
				totCogs = detil.getNQty() * detil.getTbBatchItem().getNCogsPrice(); //todo please check this one
				voucherNo = retur.getVReturCode();
				
				memo.setLength(0);				
				memo.append("ITEMCODE:")
					.append(item.getVItemCode())
					.append(";QTY:")
					.append(detil.getNQty())
					.append(";VALUE:")
					.append(totTrans.doubleValue());
				
				//ar (-) (k)
				jhandler.addJournal(voucherNo, memo.toString(), totTrans, MedisafeConstants.ACCT_CREDIT, tgl, currentUser, coaAR);
				//income (-) (d)
				jhandler.addJournal(voucherNo , memo.toString(), totTrans, MedisafeConstants.ACCT_DEBIT, tgl, currentUser, coaIncome);
				
				//inv (+) (d)
				jhandler.addJournal(voucherNo, memo.toString(), totCogs, MedisafeConstants.ACCT_DEBIT, tgl, currentUser, coaInv);
				//cogs(hpp) (-) (k)
				jhandler.addJournal(voucherNo, memo.toString(), totCogs, MedisafeConstants.ACCT_CREDIT, tgl, currentUser, coaCogs);
			}
			
//			final check on Journal
			if (!jhandler.isBalance())
				throw new VONEAppException(MessagesService.getKey("master.coa.journal.discrepancy.internal"));
						
			Iterator<TbJournalTrx> it = jhandler.getListJournal().iterator();
			while (it.hasNext()){
				getHibernateTemplate().save(it.next());
			}
			
			txManager.commit(status);
			success = true;
			
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	
	public List getRacikanItems(Integer warehouseUnitId,String tclass) throws VONEAppException{
		
    	List result = null;
    	
    	StringBuffer sql = new StringBuffer();
    
    	sql.append(" select distinct ");
    	sql.append(" {itemselling.*} ");
    
    	sql.append(" from ");
    	sql.append(" ms_item_selling_price itemselling, ");
    	sql.append(" ms_item item, ");
    	sql.append(" ms_treatment_class tclass, ");
    	sql.append(" ms_item_group igrup, ");
    	sql.append(" tb_item_inventory invent ");
    
    	sql.append(" where ");
    	sql.append(" itemselling.n_item_id=item.n_item_id ");
    	sql.append(" and item.n_item_group_id=igrup.n_item_group_id ");
    	sql.append(" and itemselling.n_tclass_id=tclass.n_tclass_id ");
    	sql.append(" and item.n_item_id=invent.n_item_id ");
    	sql.append(" and invent.n_whouse_id=:wId ");
    	sql.append(" and tclass.v_tclass_desc=:tdesc ");
    	sql.append(" and igrup.v_item_group_name=:grup ");
    	
    	try {
			
    		
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.addEntity("itemselling", MsItemSellingPrice.class);
			query.setInteger("wId", warehouseUnitId.intValue());
			query.setString("tdesc", tclass);
			query.setString("grup","OBAT");
			result = query.list();
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
		}
    	
    	return result;
	}
	
	
	public List searchRacikanItems(Integer warehouseUnitId,String tclass, String itemCode, String itemName)
		throws VONEAppException{
		
    	List result = null;
    	

    	StringBuffer sql = new StringBuffer();
    	
    	sql.append(" select ");
    	sql.append(" inv.n_item_id as id, ");
    	sql.append(" item.v_item_code as code, ");
    	sql.append(" item.v_item_name as name, ");
    	sql.append(" sell.n_selling_price as harga, ");
    	sql.append(" sat.v_mitem_end_quantify as satuan, ");
    	sql.append(" sum(inv.n_item_inv_qty) as jumlah, ");
    	sql.append(" item.n_type as tipe, ");
    	sql.append(" item.n_r as r ");
    	
    	sql.append(" from " );
    	sql.append(" tb_item_inventory inv, ");
    	sql.append(" ms_item item, ");
    	sql.append(" ms_item_selling_price sell, ");
    	sql.append(" ms_item_measurement sat, ");
    	sql.append(" ms_treatment_class tclass ");
    	//sql.append(" ms_item_group igrup ");
    
		sql.append(" where ");
		sql.append(" inv.n_whouse_id=:wId ");
		sql.append(" and inv.n_item_id=item.n_item_id ");
		sql.append(" and item.v_item_code like :code ");
		sql.append(" and item.v_item_name like :itemName ");
		sql.append(" and item.n_item_id=sell.n_item_id ");
		sql.append(" and item.n_mitem_id=sat.n_mitem_id ");
		sql.append(" and sell.n_tclass_id=tclass.n_tclass_id ");
		sql.append(" and tclass.v_tclass_desc=:kelas ");
		//sql.append(" and item.n_item_group_id=igrup.n_item_group_id ");
		//sql.append(" and igrup.v_item_group_name=:grup ");
    	
		sql.append(" group by ");
		sql.append(" id, ");
		sql.append(" code, ");
		sql.append(" name, ");
		sql.append(" harga, ");
		sql.append(" satuan, ");
		sql.append(" tipe, ");
		sql.append("r");
    	
    	try {
			
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			
			query.addScalar("id", Hibernate.INTEGER);
			query.addScalar("code",Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("harga", Hibernate.DOUBLE);
			query.addScalar("satuan", Hibernate.STRING);
			query.addScalar("jumlah", Hibernate.DOUBLE);
			query.addScalar("tipe", Hibernate.SHORT);
			query.addScalar("r", Hibernate.SHORT);
			query.setInteger("wId", warehouseUnitId.intValue());
			query.setString("code", itemCode);
			query.setString("itemName", itemName);
			query.setString("kelas", tclass);
			//query.setString("grup", "OBAT");
			
			result = query.list();
		} catch (HibernateException e) {
			
			e.printStackTrace();
		}
		
    	return result;
	}
	
	
	public TbReturPharmacyTrx getTbReturPharmacyTrx(Integer id) throws VONEAppException{
		TbReturPharmacyTrx tbReturPharmacyTrx = null;
		Session session = null;
    	try {
			session = getCurrentSession();
			
			tbReturPharmacyTrx = (TbReturPharmacyTrx)
				session
					.createQuery(
							"from TbReturPharmacyTrx trx " +
							"where trx.NReturId = :id")
					.setInteger("id", id)
					.uniqueResult();
			
		} catch (HibernateException e) {
            log.error("find by example failed", e);
            throw new VONEAppException(e.getMessage());
		}
		
		return tbReturPharmacyTrx;
	}
	
	
	
	public boolean saveModify(TbExamination nota, Set<TbItemTrx> itemTrxs, Set<TbDrugIngredients> 
		racikans, Integer warehouseId, Set<TbMiscTrx> miscs) throws VONEAppException
	{
		boolean success = false;
		Session session = null;
//		Transaction trans = null;
		
		
		
		TbItemInventory itemInventory = null;
		
		try {
			session = getCurrentSession();
//			trans = session.beginTransaction();
			
			session.update(nota);
			
						
			
			
			List<TbItemTrx> itmTrxs = getItemTrxBaseOnNote(nota);
			
			String sql = "select {inv.*} from tb_item_inventory inv where inv.n_item_id=:idItem"+
				 " AND inv.n_batch_id=:batchId AND inv.n_whouse_id=:wId";
				
			for(TbItemTrx itmTrx : itmTrxs){
				
				
				
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity("inv", TbItemInventory.class);
				query.setInteger("idItem", itmTrx.getMsItem().getNItemId().intValue());
				query.setInteger("batchId", itmTrx.getTbBatchItem().getNBatchId().intValue());
				query.setInteger("wId", warehouseId.intValue());
				
				itemInventory = (TbItemInventory)query.uniqueResult();
				float qty = itmTrx.getNQty() + itemInventory.getNItemInvQty();
				itemInventory.setNItemInvQty(qty);
	
				session.update(itemInventory);
				
				
				
				Set<TbPatientInventory> patins = itmTrx.getTbPatientInventories();
				
				for(TbPatientInventory patin : patins){
					
					session.delete(patin);
				}
								
				session.delete(itmTrx);
			}
			
			float qtyUsed = 0;
			double disc_1 = 0;
			double trans_val_1 = 0;
			double trans_aft_disc_1 = 0;
			
			TbItemTrx realTrx = null;
			TbPatientInventory patInv = null;
			
			String sql2 = "select {inv.*} from tb_item_inventory inv where inv.n_whouse_id=:wId "+
	 			"AND inv.n_item_id=:itemId AND inv.n_item_inv_qty > :qty";

			
			for(TbItemTrx trx : itemTrxs){
				
				
				trx.setTbExamination(nota);
				trx.setDWhnCreate(nota.getDWhnCreate());
				MsItem item = trx.getMsItem();
				qtyUsed = trx.getNQty();
				
				disc_1 = trx.getNDiscAmount()/trx.getNQty();
				trans_val_1 = trx.getNAmountTrx()/trx.getNQty();
				trans_aft_disc_1 = trx.getNAmountAfterDisc()/trx.getNQty();
				
				
				SQLQuery query = session.createSQLQuery(sql2);
				query.addEntity("inv", TbItemInventory.class);
				query.setInteger("wId", warehouseId.intValue());
				query.setInteger("itemId", item.getNItemId().intValue());
				query.setShort("qty", (short)0);
				
				List<TbItemInventory> itemInvList = query.list();
				for(TbItemInventory inv : itemInvList){
					if(qtyUsed == 0)break;
					
					if(inv.getNItemInvQty() <= qtyUsed){
						qtyUsed = qtyUsed - inv.getNItemInvQty();
						
						realTrx = trx.clone();
						realTrx.setAturanPakai(trx.getAturanPakai());
						realTrx.setTbBatchItem(inv.getTbBatchItem());
						realTrx.setNQty(inv.getNItemInvQty());
						realTrx.setNAmountAfterDisc(inv.getNItemInvQty() * trans_aft_disc_1);
						realTrx.setNAmountTrx(inv.getNItemInvQty() * trans_val_1);
						realTrx.setNDiscAmount(inv.getNItemInvQty() * disc_1);
						
						session.save(realTrx);
						
						inv.setNItemInvQty((short)0);
						session.update(inv);
						
						
						patInv = new TbPatientInventory();
						patInv.setMsItem(item);
						patInv.setMsPatient(nota.getMsPatient());
						patInv.setNQty(realTrx.getNQty().shortValue());
						patInv.setTbItemTrx(realTrx);
						patInv.setDWhnCreate(nota.getDWhnCreate());
						patInv.setVWhoCreate(trx.getVWhoCreate());

						session.save(patInv);
						
						
						
					}else{
						float sub = inv.getNItemInvQty() - qtyUsed;
						
						realTrx = trx.clone();
						realTrx.setAturanPakai(trx.getAturanPakai());
						realTrx.setTbBatchItem(inv.getTbBatchItem());
						realTrx.setNQty(qtyUsed);
						realTrx.setNAmountAfterDisc(qtyUsed * trans_aft_disc_1);
						realTrx.setNAmountTrx(qtyUsed * trans_val_1);
						realTrx.setNDiscAmount(qtyUsed * disc_1);
						
						session.save(realTrx);
						
						inv.setNItemInvQty(sub);
						session.update(inv);
						qtyUsed = 0;
						
						patInv = new TbPatientInventory();
						patInv.setMsItem(item);
						patInv.setMsPatient(nota.getMsPatient());
						patInv.setNQty(realTrx.getNQty().shortValue());
						patInv.setTbItemTrx(realTrx);
						patInv.setDWhnCreate(nota.getDWhnCreate());
						patInv.setVWhoCreate(trx.getVWhoCreate());

						session.save(patInv);
					}
				}
			}
			
			if(racikans != null){
				for(TbDrugIngredients racikan : racikans){
					session.update(racikan);
				}
			}
			
			
			for(TbMiscTrx misc : miscs){
				session.update(misc);
			}
			
//			trans.commit();
			success = true;
		} catch (HibernateException e) {
//			trans.rollback();
			e.printStackTrace();
		} 
			
		
		
		return success;
	}
	
	public boolean saveReturModify(TbReturPharmacyTrx retur,Set<TbReturPharmacyDetailTrx> detilRetur, 
			Integer warehouseId) throws VONEAppException{
		
		boolean success = false;
		Session session = null;
		Transaction trans = null;
		
		try {
			session = getSessionFactory().getCurrentSession();
			
			trans = session.beginTransaction();
			session.update(retur);
			
			String query = "select {detil.*} from tb_retur_pharmacy_detail_trx detil " +
							"where detil.n_retur_id=:returId";
			
			SQLQuery sqlQuery = session.createSQLQuery(query);
			
			sqlQuery.addEntity("detil", TbReturPharmacyDetailTrx.class);
			sqlQuery.setInteger("returId", retur.getNReturId().intValue());
			
			List<TbReturPharmacyDetailTrx> listDetail = sqlQuery.list();
			
			String query2 = "select {inv.*} from tb_item_inventory inv" +
	        				" where inv.n_item_id=:itemId and inv.n_batch_id=:batchId " +
	        				" and inv.n_whouse_id=:wareId";
			
			for(TbReturPharmacyDetailTrx returDet:listDetail){
				
				
				SQLQuery sqlQuery2 = session.createSQLQuery(query2);
				sqlQuery2.addEntity("inv", TbItemInventory.class);
				sqlQuery2.setInteger("itemId", returDet.getMsItem().getNItemId().intValue());
				sqlQuery2.setInteger("batchId", returDet.getTbBatchItem().getNBatchId().intValue());
				sqlQuery2.setInteger("wareId", warehouseId.intValue());
				
				TbItemInventory itemInv = (TbItemInventory)sqlQuery2.uniqueResult();
				float jlhInv = itemInv.getNItemInvQty();
				jlhInv = jlhInv - returDet.getNQty();
				itemInv.setNItemInvQty(jlhInv);
				
				session.update(itemInv);
				session.delete(returDet);
				
			}
			
			for(TbReturPharmacyDetailTrx returDetail:detilRetur){
				
				SQLQuery sqlQuery2 = session.createSQLQuery(query2);
				sqlQuery2.addEntity("inv", TbItemInventory.class);
				sqlQuery2.setInteger("itemId", returDetail.getMsItem().getNItemId().intValue());
				sqlQuery2.setInteger("batchId", returDetail.getTbBatchItem().getNBatchId().intValue());
				sqlQuery2.setInteger("wareId", warehouseId.intValue());
				
				TbItemInventory itemInv = (TbItemInventory)sqlQuery2.uniqueResult();
				float jlhInv = itemInv.getNItemInvQty();
				jlhInv = jlhInv + returDetail.getNQty();
				itemInv.setNItemInvQty(jlhInv);
				
				
				session.update(itemInv);
				session.save(returDetail);
					
			}
			
			trans.commit();
			success=true;
			
			
		} catch (HibernateException e) {
			
			throw new VONEAppException(e.getMessage());
			
		}
		
		return success;
		
	}
	
	public TbItemTrx getExpiredDateItemTrx(Integer noteId, Integer ItemId) throws VONEAppException{
		StringBuffer sb = new StringBuffer();		
		
		sb.append(" select ");
		sb.append(" {trx.*} ");
							
		
		sb.append(" from " );
		sb.append(" tb_item_trx trx ");
		
				 
		sb.append(" where " );
		sb.append(" trx.n_note_id=:nNoteId and n_item_id=:nItemId limit 1");
		
		
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("trx", TbItemTrx.class);
		query.setInteger("nNoteId", noteId);
		query.setInteger("nItemId", ItemId);
		
		return (TbItemTrx)query.uniqueResult();
	}
	
	
	public List getItemTrxBaseOnNote(TbExamination nota) throws VONEAppException{
		
		
		StringBuffer sb = new StringBuffer();		
		
		sb.append(" select ");
		sb.append(" {trx.*} ");
							
		
		sb.append(" from " );
		sb.append(" tb_item_trx trx ");
		
				 
		sb.append(" where " );
		sb.append(" trx.n_note_id=:noteId ");
		
		
		      
		
			
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("trx", TbItemTrx.class);
		query.setInteger("noteId", nota.getNExamId().intValue());
			
		List result = query.list();
		
		return result;
	}


	public void save(TbReturPharmacyTrx retur) {
		
		getHibernateTemplate().saveOrUpdate(retur);
		
	}


	public List<TbExamination> getValidatedNoteToday() throws HibernateException, VONEAppException {
		List<TbExamination> notas = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		StringBuffer sb = new StringBuffer();
		try {
			Date tgl = sdf.parse(sdf.format((new Date())));
			sb.append("select {nota.*} from tb_examination nota");
			sb.append(" where nota.d_whn_create between :tgl1 and :tgl2");
			sb.append(" and nota.n_exam_status=:status");
			sb.append(" and nota.v_note_no like :note_no");
			sb.append(" and nota.antrian_status is null");
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addEntity("nota", TbExamination.class);
			
			query.setTimestamp("tgl1", tgl);
			query.setTimestamp("tgl2", new Date());
			query.setInteger("status",MedisafeConstants.VALIDATED_NOTE);
			query.setString("note_no", "J-APTK%");
			
			notas = query.list();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notas;
	}


	public List<TbExamination> getObatJadi() throws HibernateException, VONEAppException {
		List<TbExamination> notas = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		StringBuffer sb = new StringBuffer();
		try {
			Date tgl = sdf.parse(sdf.format((new Date())));
			sb.append("select {nota.*} from tb_examination nota");
			sb.append(" where nota.d_whn_create between :tgl1 and :tgl2");
			sb.append(" and nota.n_exam_status=:status");
			sb.append(" and nota.v_note_no like :note_no");
			sb.append(" and nota.antrian_status=:antrian");
			sb.append(" order by nota.d_whn_change desc");
			
			SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
			query.addEntity("nota", TbExamination.class);
			
			query.setTimestamp("tgl1", tgl);
			query.setTimestamp("tgl2", new Date());
			query.setInteger("status",MedisafeConstants.VALIDATED_NOTE);
			query.setString("note_no", "J-APTK%");
			query.setInteger("antrian", 1);
			
			notas = query.list();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notas;
	}


	public void updateAntrianNote(TbExamination nota) {
		getHibernateTemplate().update(nota);
		
	}


	public MsAntrian getMasterAntrian() throws HibernateException, VONEAppException {
		MsAntrian antrian = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select {antri.*} from ms_antrian antri");
		SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
		query.addEntity("antri",MsAntrian.class);
		
		antrian = (MsAntrian)query.uniqueResult();
		return antrian;
	}


	public void updateAntrianNote(MsAntrian antrian) {
		getHibernateTemplate().update(antrian);
		
	}


	public double getHargaSatuan(MsItem msItem, TbRegistration reg) throws HibernateException, VONEAppException {
		String query = "select sum(trx.n_amount_after_disc)/sum(trx.n_qty) as hargaSatuan "
				     + "from tb_item_trx trx, ms_item item, tb_examination nota, tb_registration reg "
				     + "where trx.n_item_id=item.n_item_id and trx.n_note_id=nota.n_exam_id and nota.n_reg_id=reg.n_reg_id "
				     + "and reg.n_reg_id in(:regInap, :regJalan) and item.n_item_id=:itemId ";
		SQLQuery q = getCurrentSession().createSQLQuery(query);
		q.setInteger("regInap", reg.getNRegId());
		q.setInteger("regJalan", reg.getTbRegistration().getNRegId());
		q.setInteger("itemId", msItem.getNItemId());
		
		q.addScalar("hargaSatuan", Hibernate.DOUBLE);
		
		
		return (Double)q.uniqueResult();
	}

}

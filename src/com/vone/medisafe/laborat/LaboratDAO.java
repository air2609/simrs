package com.vone.medisafe.laborat;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbLaboratoryResult;
import com.vone.medisafe.mapping.TbLaboratoryResultDetail;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;

public class LaboratDAO extends HibernateDaoSupport {

	Logger log = Logger.getLogger(LaboratDAO.class);

    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }
    
	public MsLabTestDetail getLabTestDetail(int code) throws VONEAppException {
		MsLabTestDetail ltd = null;
		List list = null;
		Session session = null;
		String stQuery = "select {ltd.*} " 
				+ "from ms_lab_test_detail ltd "
				+ "where ltd.n_treatment_id = :code ";
		

		try {
			session = getCurrentSession();
			list = session.createSQLQuery(stQuery)
					.addEntity("ltd",MsLabTestDetail.class)
					.setInteger("code", code)
					.list();
			// session.getEntityName(ltd);
			if (list.iterator().hasNext()) {
				ltd = (MsLabTestDetail) list.iterator().next();
			}
		} catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
		}
		return ltd;
	}
	
	public List getTbLabResultDetail(int code) throws VONEAppException {
		List list = null;
		Session session = null;

		try {
			session = getCurrentSession();
			
			//Query query = session.getNamedQuery("SEL_INVESTMENT_BY_CIF_ID");
			
			//Criteria crit = session.createCriteria(TbLaboratoryResultDetail.class);
			//crit.add(Restrictions.eq("tbExamination", note));
			
			list = session.createSQLQuery(
					" select {t.*} " +
					" from tb_laboratory_result_detail t, ms_lab_treatment_detil det " +
					" where t.n_lab_rslt_id = :id " +
					" and t.n_treatment_id = det.n_treatment_id " +
					" and t.n_lab_detil_id = det.n_lab_detil_id " +
					" order by " +
					" t.n_lab_rslt_id, "+ 
					" det.n_treatment_id, "+ 
					" det.v_detail_name "  )
					.addEntity("t", TbLaboratoryResultDetail.class)
					.setInteger("id", code)
					.list();
			
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());

		}
		return list;
	}

	public List getTreatmentTrx(TbExamination note) throws VONEAppException {
		List result = null;
		Session session = null;

		try {
			session = getCurrentSession();
			String sql = "select " +
							"	{t.*} " +
							"from " +
							"	tb_treatment_trx t, " +
							"	ms_treatment_fee tf " +
							"where " +
							"	t.n_note_id = :id " +
							"	and t.n_treatment_fee_id = tf.n_treatment_fee_id " +
							"order by " +
							"	tf.n_treatment_id ";
			result = session.createSQLQuery(sql)
				.addEntity("t", TbTreatmentTrx.class)
				.setInteger("id", note.getNExamId())
				.list();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());

		}
		return result;
	}

	public TbExamination getTbExaminationById(int id) throws VONEAppException {
		TbExamination exam = null;
		Session session = null;
		try {
			session = getCurrentSession();
			exam = (TbExamination)session.createCriteria(TbExamination.class)
				.add(Expression.eq("NExamId", id))
				.uniqueResult();

		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return exam;
	}
	
	public TbExamination getExaminationByMr(TbMedicalRecord mr) throws VONEAppException {
		TbExamination exam = null;
		Session session = null;
		List list = null;

		String stQuery = "select {exam.*}  "
				+ "from tb_examination exam, ms_patient pat, tb_medical_record mr, tb_registration reg "
				+ "where exam.n_patient_id = pat.n_patient_id "
				+ "and pat.n_patient_id = mr.n_patient_id "
				+ "and mr.n_mr_id = reg.n_mr_id " 
				+ "and mr.n_mr_id = :id "
				+ "and reg.reg_status = :status "
				+ "and exam.n_unit_id = :lokasi "
				+ "order by exam.n_exam_id desc "; //ambil nota yg terakhir
//				+ "and exam.n_exam_status = 1 "; //todo benarkah?

		try {
			session = getCurrentSession();
			list = session.createSQLQuery(stQuery)
					.addEntity("exam",TbExamination.class).setInteger("id", mr.getNMrId())
					.setInteger("status", MedisafeConstants.REG_ACTIVE)
					.setInteger("lokasi", MedisafeConstants.LOKASI_LAB)
					.list();
			//todo SEMENTARA AMBIL RECORD YG PERTAMA == nota terakhir
			if (list.iterator().hasNext())
				exam = (TbExamination) list.iterator().next();

		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return exam;
	}

	public List getTreatment(MsPatient pat) throws VONEAppException {
		Session session = null;
		List list = null;

		String stQuery = "select {t.*}  " 
				+ "from "
				+ " ms_treatment_group g, "
				+ " ms_treatment t, " 
				+ " ms_treatment_fee tf, "
				+ " tb_treatment_trx trx, " 
				+ " tb_examination exam, "
				+ " ms_patient pat, " 
				+ " tb_medical_record mr, "
				+ " tb_registration reg "
				+ "where " 
				+ " g.n_tgroup_id= t.n_tgroup_id "
				+ " and t.n_treatment_id = tf.n_treatment_id "
				+ " and tf.n_treatment_fee_id = trx.n_treatment_fee_id "
				+ " and exam.n_exam_id = trx.n_note_id "
				+ " and exam.n_patient_id = pat.n_patient_id "
				+ " and pat.n_patient_id = mr.n_patient_id "
				+ " and mr.n_mr_id = reg.n_mr_id "
				+ " and t.v_treatment_code like 'LAB%' "
				+ " and pat.n_patient_id = :id "
				+ " and reg.reg_status = :status "
				+ " and exam.n_unit_id = :lokasi "
				+ "order by g.n_tgroup_id, t.n_treatment_id ";

		try {
			session = getCurrentSession();
			list = session.createSQLQuery(stQuery).addEntity("t",
					MsTreatment.class).setInteger("id", pat.getNPatientId())
					.setInteger("lokasi", MedisafeConstants.LOKASI_LAB)
					.setInteger("status", MedisafeConstants.REG_ACTIVE).list();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return list;
	}

	public MsTreatment getTreatmentByLrd(TbLaboratoryResultDetail lrd) throws VONEAppException {
		MsTreatment treat = null;
		Session session = null;
		List list = null;

		String stQuery = "select {t.*}  " 
				+ "from "
				+ " ms_treatment t, tb_laboratory_result_detail dt " 
				+ "where t.n_treatment_id = dt.n_treatment_id " 
				+ " and dt.n_lab_rslt_det_id = :id ";

		try {
			session = getCurrentSession();
			list = session.createSQLQuery(stQuery)
					.addEntity("t",MsTreatment.class)
					.setInteger("id", lrd.getNLabRsltDetId())
					.list();
			
			
			if(list.iterator().hasNext()){
				treat = (MsTreatment) list.iterator().next();
			}
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		
		return treat;
	}
	
	public List getTreatmentResult(MsPatient pat) throws VONEAppException {
		Session session = null;
		List list = null;

		String stQuery = "select {lrd.*}  " 
				+ "from " 
				+ " ms_treatment_group g, "
				+ " ms_treatment t, "
				+ "	tb_laboratory_result_detail lrd, "
				+ "	tb_laboratory_result lr, " 
				+ "	tb_examination exam, "
				+ "	ms_patient pat, " 
				+ "	tb_medical_record mr, "
				+ "	tb_registration reg " 
				+ "where "
				+ " g.n_tgroup_id = t.n_tgroup_id "
				+ " and t.n_treatment_id = lrd.n_treatment_id "
				+ "	and lrd.n_lab_rslt_id = lr.n_lab_rslt_id "
				+ "	and lr.n_exam_id = exam.n_exam_id "
				+ "	and exam.n_patient_id = pat.n_patient_id "
				+ "	and pat.n_patient_id = mr.n_patient_id "
				+ "	and mr.n_mr_id = reg.n_mr_id "
				+ "	and t.v_treatment_code like 'LAB%' "
				+ "	and pat.n_patient_id = :id "
				+ "	and reg.reg_status = :status "
				+ "	and exam.n_unit_id = :lokasi "
				+ "order by g.n_tgroup_id, t.n_treatment_id ";

		try {
			session = getCurrentSession();
			list = session.createSQLQuery(stQuery)
					.addEntity("lrd",TbLaboratoryResultDetail.class)
					.setInteger("id", pat.getNPatientId())
					.setInteger("lokasi", MedisafeConstants.LOKASI_LAB)
					.setInteger("status", MedisafeConstants.REG_ACTIVE)
					.list();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return list;
	}

	public boolean saveHasilLab(MsPatient patient, TbLaboratoryResult nota,
			TbExamination exam, Set resultDetailSet, MsUnit unit,
			boolean isRanap) throws VONEAppException {
		
		log.debug("LaboratDAO - saveHasilLab - ENTER");
		boolean success = false;
		Session session = null;
		boolean isRujukan = false;

		Date tanggal = new Date();

		if (patient.getNPatientId() == null)
			isRujukan = true;

		try {
			session = getCurrentSession();
			if (isRujukan) {
				patient.setDWhnCreate(tanggal);
				// todo get sequence
				patient.setNPatientId(IdsServiceLocator.getIdsManager()
						.getSequence(MedisafeConstants.PATIENT_ID));
				session.save(patient);
			}

			// nota.setMsPatient(patient);

			nota.setDWhnCreate(tanggal);
			Integer noNota = null;
			
			noNota = IdsServiceLocator.getIdsManager().getSequence(
					MedisafeConstants.NOTA_HALAB);

			nota.setVLabRsltCode(MedisafeUtil.generateNoHLab(noNota,
					tanggal, unit.getVUnitCode()));
			
			nota.setNLabRsltId(noNota);
			nota.setTbExamination(exam);
			session.save(nota);

			Iterator it = resultDetailSet.iterator();
			while (it.hasNext()) {
				// TbTreatmentTrx treatTrx = (TbTreatmentTrx)it.next();
				// treatTrx.setTbExamination(nota);
				// treatTrx.setDWhnCreate(tanggal);
				// session.save(treatTrx);
				TbLaboratoryResultDetail resultTrx = (TbLaboratoryResultDetail) it
						.next();
				resultTrx.setTbLaboratoryResult(nota);
				resultTrx.setDWhnCreate(tanggal);
				resultTrx.setNLabRsltDetId(IdsServiceLocator.getIdsManager()
						.getSequence(MedisafeConstants.HASIL_LAB_DETAIL));
				// resultTrx.setNLabRsltDetId(NLabRsltDetId)
				// resultTrx.setVLabRsltDesc(resultTrx.getVLabRsltDesc());
				// resultTrx.setVLabRsltQuantify(resultTrx.getVLabRsltQuantify());
				// resultTrx.setVNrmlRangeMan(resultTrx.getVNrmlRangeMan());
				// resultTrx.setVNrmlRangeWoman(resultTrx.getVNrmlRangeWoman());
				session.save(resultTrx);
			}
			success = true;
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}

		log.debug("LaboratDAO - saveHasilLab - EXIT");
		return success;
	}

	public boolean saveTransaction(MsPatient patient, TbExamination nota,
			Set treatmentTrx, MsUnit unit, boolean isRanap) throws VONEAppException {
		log.debug("LaboratDAO - saveTransaction - ENTER");
		boolean success = false;
		Session session = null;
		boolean isRujukan = false;

		Date tanggal = new Date();

		if (patient.getNPatientId() == null)
			isRujukan = true;

		try {
			session = getCurrentSession();
			if (isRujukan) {
				patient.setDWhnCreate(tanggal);
				patient.setNPatientId(IdsServiceLocator.getIdsManager()
						.getSequence(MedisafeConstants.PATIENT_ID));
				session.save(patient);
			}

			nota.setMsPatient(patient);
			nota.setDWhnCreate(tanggal);
			Integer noNota = null;
			if (isRanap) {
				noNota = IdsServiceLocator.getIdsManager().getSequence(
						MedisafeConstants.NOTA_RANAP);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota,
						tanggal, unit.getVUnitCode(), MedisafeConstants.RANAP));
			}

			else {
				noNota = IdsServiceLocator.getIdsManager().getSequence(
						MedisafeConstants.NOTA_RAJAL);
				nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota,
						tanggal, unit.getVUnitCode(),
						MedisafeConstants.NON_RANAP));
			}

			session.save(nota);

			Iterator it = treatmentTrx.iterator();
			TbTreatmentTrx treatTrx;
			while (it.hasNext()) {
				treatTrx = (TbTreatmentTrx) it.next();
				treatTrx.setTbExamination(nota);
				treatTrx.setDWhnCreate(tanggal);
				session.save(treatTrx);
			}

			success = true;
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}

		log.debug("LaboratDAO - saveTransaction - EXIT");
		return success;

	}

	// hanya cari nota dari lab
	// pasien yg punya nota di lab
	public List searchPatientRegistered(String code, String nameP,
			String addressP) throws VONEAppException {
		Session session = null;
		List list = null;

		String query = "select distinct {mr.*} from tb_medical_record mr, ms_patient p, tb_registration reg "
				+ " where mr.v_mr_code like :mrCode AND (mr.n_patient_id=p.n_patient_id) "
				+ " AND p.v_patient_name like :name AND p.v_patient_main_addr LIKE :address "
				+ " AND mr.n_mr_id=reg.n_mr_id AND reg.reg_status=:status";
		try {
			session = getCurrentSession();
			list = session.createSQLQuery(query).addEntity("mr",
					TbMedicalRecord.class).setString("mrCode", code).setString(
					"name", nameP).setString("address", addressP).setInteger(
					"status", MedisafeConstants.REG_ACTIVE).list();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return list;
	}
	
	public List searchHasilLab(String code, String name) throws VONEAppException {
		Session session = null;
		List list = null;

		String query = "select {lr.*} "
				+ "from tb_laboratory_result lr, "
				+ " tb_examination exam, "
				+ " ms_patient p " 
				+ "where lr.n_exam_id = exam.n_exam_id "
				+ " and exam.n_patient_id = p.n_patient_id "
				+ " and lr.v_lab_rslt_code like :code "
				+ " and p.v_patient_name like :name ";
				
		try {
			session = getCurrentSession();
			list = session.createSQLQuery(query)
					.addEntity("lr",TbLaboratoryResult.class)
					.setString("code", code)
					.setString("name", name)
					.list();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		
		return list;
	}
	
	public List getResultset(String sql) throws VONEAppException {
		Session session = null;
		List list = null;

		try {
			session = getCurrentSession();
			list = session.createSQLQuery(sql).list();
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}
		return list;
	}

	public MsPatient getFreeBuyer() throws VONEAppException {
		MsPatient pat = null;
		List list = null;
		Session session = null;

		try {
			session = getCurrentSession();
			
			list = session.createQuery("from MsPatient where VPatientName = '" +
					MedisafeConstants.FREE_BUYER_NAME +
					"'")
					.list();
			if(list.size() == 0){
				//buat pasien baru
				pat = new MsPatient();
				pat.setVPatientName(MedisafeConstants.FREE_BUYER_NAME);
				pat.setVPatientGender("M");
				pat.setDPatientDob(new Date());
				pat.setVPatientMainAddr("Solo");
				session.save(pat);
			}
			else{
				Iterator iterator = list.iterator();
				if (iterator.hasNext()) {
					pat = (MsPatient) iterator.next();
				}
			}
				
		} catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());

		}
		return pat;
	}

	public boolean saveModifyHasilLab(MsPatient patient, TbLaboratoryResult nota, TbExamination exam, Set<TbLaboratoryResultDetail> labResultDetailSet) throws VONEAppException {
		log.debug("LaboratDAO - saveModifyHasilLab - ENTER");
		boolean success = false;
		Session session = null;

		Date tanggal = new Date();

		try {
			session = getCurrentSession();
			
			patient.setDWhnChange(tanggal);
			session.update(patient);

			nota.setDWhnChange(tanggal);
			
			nota.setTbExamination(exam);
			session.update(nota);

			Iterator it = labResultDetailSet.iterator();
			while (it.hasNext()) {
				TbLaboratoryResultDetail resultTrx = (TbLaboratoryResultDetail) it.next();
				//resultTrx.setTbLaboratoryResult(nota);
				resultTrx.setDWhnChange(tanggal);
				session.update(resultTrx);
			}
			success = true;
		}catch (Exception e){
			 logger.error(e.getMessage());
			 throw new VONEAppException(e.getMessage());
		}

		log.debug("LaboratDAO - saveModifyHasilLab - EXIT");
		return success;
		
	}

	public MsTreatmentFee getMsTreatmentFee(MsTreatmentFee tfee) throws VONEAppException{
		return (MsTreatmentFee) getCurrentSession().get(MsTreatmentFee.class, tfee.getNTreatmentFeeId());
	}

	public MsLabTreatmentDetil getMsLabTreatmentDetilById(Integer labDetilId) throws VONEAppException{
		return (MsLabTreatmentDetil) getCurrentSession().get(MsLabTreatmentDetil.class, labDetilId);
	}

}

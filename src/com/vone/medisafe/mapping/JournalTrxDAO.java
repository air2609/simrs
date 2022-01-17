package com.vone.medisafe.mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.exception.VONEAppException;

public class JournalTrxDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(JournalTrxDAO.class);
	
	public void saveList (List<TbJournalTrx> list) throws VONEAppException {
		
		Session session = null;
		Transaction trx = null;
		
        try {
        	session = getCurrentSession();
        	trx = session.beginTransaction();
        	
        	Iterator<TbJournalTrx> it = list.iterator();
        	
        	while (it.hasNext()){
        		TbJournalTrx pojo = it.next();
        		
        		session.save(pojo);        		
        	}
        	
        	trx.commit();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
	}
	
    public void save (TbJournalTrx pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
    	}
    }
    
    public void delete (TbJournalTrx pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
    	}
    }

	public List getAllAp() throws VONEAppException {
		// todo ambil semua ap dari db
		Session session = null;
		List list;
		//if(tbAccountPayable.getTbJournalTrx() != null){
        try {
        	session = getCurrentSession();
        	list = session.createQuery("from TbAccountPayable" +
        			" where NTotalRemaining > 0 ").list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
	}

	public List getJournalByBatch(String journalBatchId) throws VONEAppException{
		// todo ambil jurnal berdasar batch id
		Session session = null;
		List list;
		
        try {
        	session = getCurrentSession();
        	list = session.createQuery("from TbJournalTrx where VJournalBatchId = :journalBatchId" )
        			.setString("journalBatchId", journalBatchId)
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
	}

	public boolean savePayment(JournalBeanHandler jbHandler, TbAccountPayable tbAccountPayable, TbAccountPayableDetail tbAccountPayableDetail) throws VONEAppException {
		// todo save payment
		boolean result = false;
		
		Session session = null;
		Transaction trx = null;
		
        try {
        	session = getCurrentSession();
        	trx = session.beginTransaction();
        	
        	Iterator it = jbHandler.getListJournal().iterator();
        	
        	while (it.hasNext()){
        		TbJournalTrx pojo = (TbJournalTrx) it.next();
        		
        		session.save(pojo);     
        		tbAccountPayableDetail.setVJournalBatchId(pojo.getVJournalBatchId());
        	}
        	session.update(tbAccountPayable);
        	session.save(tbAccountPayableDetail);
        	
        	trx.commit();
        	result = true;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
		return result;
	}

	public List getJournalByApId(Integer apId) throws VONEAppException{
		Session session = null;
		List list;
		String sql =
			" select {journal.*} " + 
			" from " + 
			" 	tb_journal_trx journal, " +
			" 	tb_account_payable_detail apd " +
			" where " +
			" 	journal.v_journal_batch_id = apd.v_journal_batch_id " +
			" 	and apd.n_ap_id = :n_ap_id " ;
		
        try {
        	session = getCurrentSession();
        	list = session.createSQLQuery(sql)
        			.addEntity("journal", TbJournalTrx.class)
        			.setInteger("n_ap_id", apId)
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
	}

	public List getGeneralLedger(int n_coa_id) throws VONEAppException{
		Session session = null;
		List list;
		String sql =
			" select " +
			" n_row, n_coa_id, d_apl_date, " +
			" n_journal_id, v_journal_batch_id, " +
			" v_voucher_no, v_desc, n_debit, " +
			" n_credit, n_balance, v_acct_name " +
			" from report.func_gl(:n_coa_id) "; 
		
        try {
        	session = getCurrentSession();
        	list = session.createSQLQuery(sql)
        			.addScalar("n_row",Hibernate.INTEGER)
        			.addScalar("n_coa_id",Hibernate.INTEGER)
        			.addScalar("d_apl_date",Hibernate.DATE)
        			.addScalar("n_journal_id",Hibernate.INTEGER)
        			.addScalar("v_journal_batch_id",Hibernate.STRING)
        			.addScalar("v_voucher_no",Hibernate.STRING)
        			.addScalar("v_desc",Hibernate.STRING)
        			.addScalar("n_debit",Hibernate.FLOAT)
        			.addScalar("n_credit",Hibernate.FLOAT)
        			.addScalar("n_balance",Hibernate.FLOAT)
        			.addScalar("v_acct_name",Hibernate.STRING)

        			.setInteger("n_coa_id", n_coa_id)
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
	}
	
	public List getGeneralLedgerByRange(int n_coa_id, Date from, Date to) throws VONEAppException{
		Session session = null;
		List list;
		String sql =
			" select " +
			" n_row, n_coa_id, d_apl_date, " +
			" n_journal_id, v_journal_batch_id, " +
			" v_voucher_no, v_desc, n_debit, " +
			" n_credit, n_balance, v_acct_name " +
			" from report.func_gl_bydate_arif(:n_coa_id, to_date(:vfrom,'yyyy-MM-dd'), to_date(:vto, 'yyyy-MM-dd'))"; 
		
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	session = getCurrentSession();
        	list = session.createSQLQuery(sql)
        			.addScalar("n_row",Hibernate.INTEGER)
        			.addScalar("n_coa_id",Hibernate.INTEGER)
        			.addScalar("d_apl_date",Hibernate.DATE)
        			.addScalar("n_journal_id",Hibernate.INTEGER)
        			.addScalar("v_journal_batch_id",Hibernate.STRING)
        			.addScalar("v_voucher_no",Hibernate.STRING)
        			.addScalar("v_desc",Hibernate.STRING)
        			.addScalar("n_debit",Hibernate.FLOAT)
        			.addScalar("n_credit",Hibernate.FLOAT)
        			.addScalar("n_balance",Hibernate.FLOAT)
        			.addScalar("v_acct_name",Hibernate.STRING)

        			.setInteger("n_coa_id", n_coa_id)
        			.setString("vfrom", sdf.format(from))
        			.setString("vto", sdf.format(to))
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
	}
	
	public List getGeneralLedgerAll(Date from, Date to) throws VONEAppException{
		Session session = null;
		List list;
		String sql =
			" select " +
			" n_row, n_coa_id, d_apl_date, " +
			" n_journal_id, v_journal_batch_id, " +
			" v_voucher_no, v_desc, n_debit, " +
			" n_credit, n_balance, v_acct_name " +
			" from report.func_gl_all_bydate(to_date(:vfrom,'yyyy-MM-dd'), to_date(:vto, 'yyyy-MM-dd'))"; 
		
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	session = getCurrentSession();
        	list = session.createSQLQuery(sql)
        			.addScalar("n_row",Hibernate.INTEGER)
        			.addScalar("n_coa_id",Hibernate.INTEGER)
        			.addScalar("d_apl_date",Hibernate.DATE)
        			.addScalar("n_journal_id",Hibernate.INTEGER)
        			.addScalar("v_journal_batch_id",Hibernate.STRING)
        			.addScalar("v_voucher_no",Hibernate.STRING)
        			.addScalar("v_desc",Hibernate.STRING)
        			.addScalar("n_debit",Hibernate.FLOAT)
        			.addScalar("n_credit",Hibernate.FLOAT)
        			.addScalar("n_balance",Hibernate.FLOAT)
        			.addScalar("v_acct_name",Hibernate.STRING)

//        			.setInteger("n_coa_id", n_coa_id)
        			.setString("vfrom", sdf.format(from))
        			.setString("vto", sdf.format(to))
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
	}

	public boolean saveJournal(JournalBeanHandler jbHandler) throws VONEAppException {
		//todo save journal
		boolean result = false;
		
		Session session = null;
		Transaction trx = null;
		
        try {
        	session = getCurrentSession();
        	trx = session.beginTransaction();
        	
        	Iterator it = jbHandler.getListJournal().iterator();
        	
        	while (it.hasNext()){
        		TbJournalTrx pojo = (TbJournalTrx) it.next();
        		
        		session.save(pojo);     
        	}
        	
        	trx.commit();
        	result = true;
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
		return result;
	}
	
    public Session getCurrentSession() throws VONEAppException {
        try {
            return getHibernateTemplate().getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            logger.error("[getCurrentSession]: Failed to get current session, e = " + e.toString());
            throw new VONEAppException("Failed to get current session");
        }
    }

	public List getAllPostingHistory() throws VONEAppException{
		return getCurrentSession()
			.createSQLQuery("select {tb.*} from history.tb_move tb order by n_flag desc limit 30")
			.addEntity("tb", TbHistoryPosting.class)
			.list();
	}

	public void postingJournal(String user, String ket, String reportName) throws VONEAppException{
		//
		//cuma dipakai utk mengeksekusi store procedure
		//
		String sql =
			" select " +
			" 1 as a " +
			" from " +
			" history.posting_journal(:user, :ket, :rpt) "; 
			//" history.posting_journal(:user, :ket) ";
		
        try {
        	getCurrentSession().createSQLQuery(sql)
        			.addScalar("a",Hibernate.INTEGER)
        			.setString("user", user)
        			.setString("ket", ket)
        			.setString("rpt", reportName)
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

	}

	public void postingJournalBack(Integer flag, String user, String ket) throws VONEAppException{
		//di controller di check TbHistoryPosting.NStatus = 0, jk tdk maka ditolak 
		String sql =
			" select " +
			" 1 as a " +
			" from " +
			" history.posting_journal_back(:flag, :user, :ket) "; 
			//" history.posting_journal(:user, :ket) ";
		
        try {
        	getCurrentSession().createSQLQuery(sql)
        			.addScalar("a",Hibernate.INTEGER)
        			.setInteger("flag", flag)
        			.setString("user", user)
        			.setString("ket", ket)
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
	}

	public List getJournalByVoucher(String text) throws VONEAppException{
		Session session = null;
		List list = null;
		
        try {
        	session = getCurrentSession();
        	list = session.createQuery("from TbJournalTrx where VVoucherNo like :journalBatchId" )
        			.setString("journalBatchId", "%" + text + "%")
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
		
		
	}

	public List getNeraca() throws VONEAppException{
		List list = null;
		String sql = "select n_row, n_coa_id, v_acct_name, n_balance from report.balance_sheet()";
		try {
        	Session session = getCurrentSession();
        	list = session.createSQLQuery(sql)
        			.addScalar("n_row",Hibernate.INTEGER)
        			.addScalar("n_coa_id",Hibernate.INTEGER)
        			.addScalar("v_acct_name",Hibernate.STRING)
        			.addScalar("n_balance",Hibernate.FLOAT)
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
		return list;
	}

	public MsCoa getMsCoa(Integer n_coa_id) throws VONEAppException{
//		return (MsCoa) getHibernateTemplate().get("MsCoa", n_coa_id);
		MsCoa coa = (MsCoa) getCurrentSession().get(MsCoa.class, n_coa_id);
		getHibernateTemplate().initialize(coa.getMsCoaType());
		
		return coa;
	}

	public List getLabarugi(Date startDate, Date endDate) throws VONEAppException{
		List list = null;
		String sql = "select n_row, n_coa_id, v_acct_name, n_balance from report.profit_loss_bydate(:startDate, :endDate)";
		try {
        	Session session = getCurrentSession();
        	list = session.createSQLQuery(sql)
        			.addScalar("n_row",Hibernate.INTEGER)
        			.addScalar("n_coa_id",Hibernate.INTEGER)
        			.addScalar("v_acct_name",Hibernate.STRING)
        			.addScalar("n_balance",Hibernate.FLOAT)
        			.setDate("startDate", startDate)
        			.setDate("endDate", endDate)
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
		return list;
	}

	public List<TbJournalTrx> getJournalByDate(Textbox voucherNo, Date from, Date to) throws VONEAppException {
		Session session = null;
		List list = null;
		
		to.setDate(to.getDate()+1);
		
        try {
        	session = getCurrentSession();
        	String q = "select {t.*} from tb_journal_trx t where t.v_voucher_no like :voucher and t.d_apl_date between :vfrom and :vend";
        	SQLQuery query = session.createSQLQuery(q);
        	
        	query.addEntity("t", TbJournalTrx.class);
        	query.setString("voucher", "%" + voucherNo.getText() + "%");
        	query.setDate("vfrom", from);
        	query.setDate("vend", to);
//        	query.setString("vfrom", from);
//        	query.setString("vend",to);
        	list = query.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;
	}

	public List getTrialBalance(Date value) throws VONEAppException{
		Session session = null;
		List list;
		String sql =
			" select " +
			" v_acct_name, n_debit, n_credit, n_balance " +
			" from report.get_trial_balance(to_date(:tgl, 'yyyy-MM-dd')) where n_debit<>0 or n_credit<>0"; 
		
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	session = getCurrentSession();
        	list = session.createSQLQuery(sql)
        		    .addScalar("v_acct_name",Hibernate.STRING)
        			.addScalar("n_debit",Hibernate.FLOAT)
        			.addScalar("n_credit",Hibernate.FLOAT)
        			.addScalar("n_balance",Hibernate.FLOAT)
        			
        			.setString("tgl", sdf.format(value))

        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }

		return list;

	}

	public List getNeracaByDate(Date value) throws VONEAppException {
		List list = null;
		String sql = "select n_row, n_coa_id, v_acct_name, n_balance from report.get_neraca_by_date(to_date(:tgl,'dd/MM/yyyy'))";
		try {
        	Session session = getCurrentSession();
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	list = session.createSQLQuery(sql)
        			.addScalar("n_row",Hibernate.INTEGER)
        			.addScalar("n_coa_id",Hibernate.INTEGER)
        			.addScalar("v_acct_name",Hibernate.STRING)
        			.addScalar("n_balance",Hibernate.FLOAT)
        			
        			.setString("tgl", sdf.format(value))
        			.list();
        }catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage());
        }
		return list;
	}

	public List<MsCoa> getMsCoas(String text) throws VONEAppException {
		Session session = null;
		String criteria = "%"+text+"%";
		List list = null;
		 try {
	        	session = getCurrentSession();
	        	String q = "select {coa.*} from ms_coa coa where coa.n_status=1 and coa.v_acct_name like :crit";
	        	SQLQuery query = session.createSQLQuery(q);
	        	
	        	query.addEntity("coa", MsCoa.class);
	        	query.setString("crit", criteria);
	        	list = query.list();
	        }catch (Exception e){
				logger.error(e.getMessage());
				throw new VONEAppException(e.getMessage());
	        }
		return list;
	}

	public void saveJournalHistroy(List<TbJournalTrxHistory> history) throws VONEAppException {
		Session session = getCurrentSession();
		for(TbJournalTrxHistory hist : history) {
			session.save(hist);
		}
		
	}
    
}

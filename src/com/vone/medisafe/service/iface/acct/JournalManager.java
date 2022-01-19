package com.vone.medisafe.service.iface.acct;

import java.util.Date;
import java.util.List;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbAccountPayable;
import com.vone.medisafe.mapping.TbAccountPayableDetail;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.pojo.TbGl;
import com.vone.medisafe.mapping.MsCoa;

public interface JournalManager {
	
	public void saveList (List<TbJournalTrx> list) throws VONEAppException;
	
	public void save(TbJournalTrx pojo) throws VONEAppException;

	public List getAllAp() throws VONEAppException;

	public List getJournalByBatch(String journalBatchId) throws VONEAppException;

	public boolean savePayment(JournalBeanHandler jbHandler, TbAccountPayable tbAccountPayable, TbAccountPayableDetail tbAccountPayableDetail) throws VONEAppException;

	public List getJournalByApId(Integer apId) throws VONEAppException;

	public List getGeneralLedger(int n_coa_id) throws VONEAppException;
	
	public List getGeneralLedgerByRange(int n_cod_id, Date dfrom, Date dto) throws VONEAppException;

	public boolean saveJournal(JournalBeanHandler jbHandler)  throws VONEAppException ;

	public void getJournalByBatch(String journalBatchId, Listbox detailList)throws VONEAppException;

	public void getAllAp(Listbox apList) throws VONEAppException;

	public void getJournalByApId(Integer apId, Listbox detailList) throws VONEAppException;

	/**
	 * 
	 * ambil history posting dari db, lalu masukin ke historyList
	 * table: history.tb_move
	 * 
	 */
	public void getAllPostingHistory(Listbox historyList) throws VONEAppException;
	
	/**
	 * 
	 * utk posting execute sql berikut:
	 * select * from history.posting_journal('tukang posting', 'alesan posting');
	 * 
	 * utk balikin posting execute sql berikut:
	 * select * from history.posting_journal_back(62, 'tukang balikin posting');
	 * 
	 */
	public void postingJournal(String user, String ket, String reportName) throws VONEAppException;
	
	public void postingJournalBack(Integer flag, String user, String ket) throws VONEAppException;

	public void getJournal(Listbox journalList, Textbox voucherNo) throws VONEAppException;

	public List getNeraca() throws VONEAppException;

	public MsCoa getMsCoa(Integer n_coa_id) throws VONEAppException;

	public List getLabarugi(Date startDate, Date endDate) throws VONEAppException;

	public void getJournalByRange(Listbox journalList, Textbox voucherNo, Datebox dateFrom, Datebox dateTo);

	public List getGeneralLedgerAll(Date value, Date value2) throws VONEAppException;

	public List getTrialBalance(Date value) throws VONEAppException;

	public List getNeracaByDate(Date value) throws VONEAppException;
	
	public void saveRekapGl(TbGl gl) throws VONEAppException;
	
	public List getRekapGlList() throws VONEAppException;

	public void searchCoa(String text, Listbox listbox) throws VONEAppException;
	
	public void deleteJournal(String batchNo, String username, Listbox journalList) throws VONEAppException;

	public void saveEditJournal(String batchNo, String username, Listbox detailList) throws VONEAppException;
	
}

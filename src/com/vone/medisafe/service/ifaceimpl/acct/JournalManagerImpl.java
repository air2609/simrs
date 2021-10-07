package com.vone.medisafe.service.ifaceimpl.acct;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.accounting.JournalBeanHandler;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.JournalTrxDAO;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.TbAccountPayable;
import com.vone.medisafe.mapping.TbAccountPayableDetail;
import com.vone.medisafe.mapping.TbHistoryPosting;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.mapping.dao.TbGlDAO;
import com.vone.medisafe.mapping.pojo.TbGl;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.service.iface.acct.JournalManager;

public class JournalManagerImpl implements JournalManager{
	
	private static final String ROLLBACK = "Rollback";
	private static final String OLD_POSTING = "Old posting";
	private static final String NEW_POSTING = "New posting";
	private JournalTrxDAO dao;
	private TbGlDAO glDao;
	

	public TbGlDAO getGlDao() {
		return glDao;
	}

	public void setGlDao(TbGlDAO glDao) {
		this.glDao = glDao;
	}

	public JournalTrxDAO getDao() {
		return dao;
	}

	public void setDao(JournalTrxDAO dao) {
		this.dao = dao;
	}

	public void save(TbJournalTrx pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void saveList (List<TbJournalTrx> list) throws VONEAppException{
		this.dao.saveList(list);
	}
	//todo buat implementasi utk ap

	public List getAllAp() throws VONEAppException {
		return dao.getAllAp();
	}

	public List getJournalByBatch(String journalBatchId) throws VONEAppException {
		return dao.getJournalByBatch(journalBatchId);
	}

	public boolean savePayment(JournalBeanHandler jbHandler, TbAccountPayable tbAccountPayable, TbAccountPayableDetail tbAccountPayableDetail) throws VONEAppException {
		return dao.savePayment(jbHandler, tbAccountPayable, tbAccountPayableDetail);
		
	}

	public List getJournalByApId(Integer apId) throws VONEAppException {
		return dao.getJournalByApId(apId);
	}

	public List getGeneralLedger(int n_coa_id) throws VONEAppException {
		return dao.getGeneralLedger(n_coa_id);
	}

	public boolean saveJournal(JournalBeanHandler jbHandler)  throws VONEAppException {
		return dao.saveJournal(jbHandler);
	}

	public void getJournalByBatch(String journalBatchId, Listbox detailList) throws VONEAppException{
		List list = getJournalByBatch(journalBatchId);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			TbJournalTrx tbJournalTrx = (TbJournalTrx) iterator.next();
			
			String jurnalBatchId = tbJournalTrx.getVJournalBatchId();
			Listitem listItem = new Listitem();
			listItem.setParent(detailList);
			listItem.setLabel(jurnalBatchId);// + " [ " + tbJournalTrx.getMsCoa().getVAcctName() + " ]");
			listItem.setValue(tbJournalTrx);
			
			//voucher no
			Listcell cell = new Listcell(tbJournalTrx.getVVoucherNo());
			cell.setParent(listItem);
			
			
			String rek = "";
			if(tbJournalTrx.getMsCoa() != null)
				rek = tbJournalTrx.getMsCoa().getVAcctName();
			Listcell listCell = new Listcell(rek);
			listCell.setParent(listItem);
			
			String desc = tbJournalTrx.getVDesc();
			listCell = new Listcell(desc);
			listCell.setParent(listItem);
			
			String debet = PrintClient.formatDouble((tbJournalTrx.getNDebit()));
			listCell = new Listcell(debet);
			listCell.setParent(listItem);
			
			String kredit = PrintClient.formatDouble((tbJournalTrx.getNCredit()));
			listCell = new Listcell(kredit);
			listCell.setParent(listItem);
			
			String aplDate = PrintClient.getDate(tbJournalTrx.getDAplDate(),MedisafeConstants.DATE_FORMAT);
			listCell = new Listcell(aplDate);
			listCell.setParent(listItem);
			
		}
		
	}

	public void getAllAp(Listbox apList) throws VONEAppException {
		List list = dao.getAllAp();
		Iterator iterator = list.iterator();
		Listcell listcell;
		Listitem listitem;
		
		//System.out.println(list.size());

		while (iterator.hasNext()) {
			TbAccountPayable tbAccountPayable = (TbAccountPayable) iterator.next();
			
			listitem = new Listitem();
			listitem.setParent(apList);
			String suplierName = tbAccountPayable.getMsVendor().getVVendorName();
			listitem.setLabel(suplierName);
			listitem.setValue(tbAccountPayable);
			if(tbAccountPayable.getTbJournalTrx() != null){
				String jurnalBatchId = tbAccountPayable.getTbJournalTrx().getVJournalBatchId();
				listcell = new Listcell(jurnalBatchId);
				listcell.setParent(listitem);
			}else{
				listcell = new Listcell("-");
				listcell.setParent(listitem);
			}
			
			String totalTerhutang = PrintClient.formatDouble(tbAccountPayable.getNTotalRemaining().doubleValue());
			listcell = new Listcell(totalTerhutang);
			listcell.setParent(listitem);
			
			String dueDate = PrintClient.getDate(tbAccountPayable.getDDueDate(), MedisafeConstants.DATE_FORMAT);
			listcell = new Listcell(dueDate);
			listcell.setParent(listitem);
			
		}
	}

	public void getJournalByApId(Integer apId, Listbox detailList) throws VONEAppException {
		List list = getJournalByApId(apId);
		
		System.out.println("apId : "+apId + " banyaknya list : "+list.size());
		
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			TbJournalTrx tbJournalTrx = (TbJournalTrx) iterator.next();
			
			String jurnalBatchId = tbJournalTrx.getVJournalBatchId();
			Listitem listitem = new Listitem();
			listitem.setParent(detailList);
			listitem.setLabel(jurnalBatchId + " [ " + tbJournalTrx.getMsCoa().getVAcctName() + " ]");
			listitem.setValue(tbJournalTrx);
			
			
			String voucherNo = tbJournalTrx.getVVoucherNo();
			Listcell listcell = new Listcell(voucherNo);
			listcell.setParent(listitem);
			
			String desc = tbJournalTrx.getVDesc();
			listcell = new Listcell(desc);
			listcell.setParent(listitem);
			
			String debet = PrintClient.formatDouble((tbJournalTrx.getNDebit()));
			listcell = new Listcell(debet);
			listcell.setParent(listitem);
			
			String kredit = PrintClient.formatDouble((tbJournalTrx.getNCredit()));
			listcell = new Listcell(kredit);
			listcell.setParent(listitem);
			
			String aplDate = PrintClient.getDate(tbJournalTrx.getDAplDate(),MedisafeConstants.DATE_FORMAT);
			listcell = new Listcell(aplDate);
			listcell.setParent(listitem);
			
		}
		MiscTrxController.setFont(detailList);
	}

	public void getAllPostingHistory(Listbox historyList) throws VONEAppException {
		historyList.getItems().clear();
		List list = dao.getAllPostingHistory();
		Iterator iterator = list.iterator();
		String statusPosting;
		while (iterator.hasNext()) {
			TbHistoryPosting tbHistoryPosting = (TbHistoryPosting) iterator.next();
			
			Listitem listitem = new Listitem();
			listitem.setLabel(tbHistoryPosting.getNFlag().toString());
			listitem.setValue(tbHistoryPosting);
			listitem.setParent(historyList);
			
			String postingDate = PrintClient.getDate(tbHistoryPosting.getDMove(),MedisafeConstants.DATE_FORMAT);
			Listcell listcell = new Listcell(postingDate);
			listcell.setParent(listitem);
			
			listcell = new Listcell(tbHistoryPosting.getVWhoMove());
			listcell.setParent(listitem);
			
			listcell = new Listcell(tbHistoryPosting.getVDesc());
			listcell.setParent(listitem);
			statusPosting = "";
			if(tbHistoryPosting.getNStatus()==0){
				statusPosting = NEW_POSTING;
			}else if(tbHistoryPosting.getNStatus()==1){
				statusPosting = OLD_POSTING;
			}else if(tbHistoryPosting.getNStatus()==2){
				statusPosting = ROLLBACK;
			}
				
			listcell = new Listcell(statusPosting);
			listcell.setParent(listitem);
			
		}
		if(historyList.getItems().size()>0)
			historyList.setSelectedIndex(0);
		MiscTrxController.setFont(historyList);

	}

	public void postingJournal(String user, String ket, String reportName) throws VONEAppException {
		dao.postingJournal(user, ket, reportName);
	}

	public void postingJournalBack(Integer flag, String user, String ket) throws VONEAppException {
		dao.postingJournalBack(flag, user, ket);
	}

	public void getJournal(Listbox journalList, Textbox voucherNo) throws VONEAppException {
		journalList.getItems().clear();
		List list = dao.getJournalByVoucher(voucherNo.getText());
		Iterator iter = list.iterator();
		TbJournalTrx tbJournalTrx = null;
		Listitem listitem;
		Listcell listcell;
		String strCell;
		while (iter.hasNext()) {
			tbJournalTrx = (TbJournalTrx) iter.next();
			
			listitem = new Listitem(tbJournalTrx.getVVoucherNo());
			listitem.setValue(tbJournalTrx);
			listitem.setParent(journalList);
			
			listcell = new Listcell();
			listcell.setParent(listitem);
			listcell.setLabel(tbJournalTrx.getVJournalBatchId());
			
			listcell = new Listcell();
			listcell.setParent(listitem);
			if(tbJournalTrx.getMsCoa() != null)
				listcell.setLabel(tbJournalTrx.getMsCoa().getVAcctName());
			
			strCell = PrintClient.getDate(tbJournalTrx.getDAplDate(), MedisafeConstants.DATE_FORMAT);
			listcell = new Listcell();
			listcell.setParent(listitem);
			listcell.setLabel(strCell);
			
			strCell = PrintClient.formatDouble(tbJournalTrx.getNDebit());
			listcell = new Listcell();
			listcell.setParent(listitem);
			listcell.setLabel(strCell);
			
			strCell = PrintClient.formatDouble(tbJournalTrx.getNCredit());
			listcell = new Listcell();
			listcell.setParent(listitem);
			listcell.setLabel(strCell);
			
			
		}
		
	}

	public List getNeraca() throws VONEAppException {
		return dao.getNeraca();
	}

	public MsCoa getMsCoa(Integer n_coa_id) throws VONEAppException {
		return dao.getMsCoa(n_coa_id);
	}

	public List getLabarugi(Date startDate, Date endDate) throws VONEAppException {
		return dao.getLabarugi(startDate, endDate);
	}

	public void getJournalByRange(Listbox journalList, Textbox voucherNo, Datebox dateFrom, Datebox dateTo) {
		
		journalList.getItems().clear();
		Listitem listitem;
		Listcell listcell;
		String strCell;
		
		try {
			List<TbJournalTrx> journals = dao.getJournalByDate(voucherNo, dateFrom.getValue(), dateTo.getValue());
			
			for(TbJournalTrx tbJournalTrx : journals){
				
				listitem = new Listitem(tbJournalTrx.getVVoucherNo());
				listitem.setValue(tbJournalTrx);
				listitem.setParent(journalList);
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(tbJournalTrx.getVJournalBatchId());
				
				listcell = new Listcell();
				listcell.setParent(listitem);
				if(tbJournalTrx.getMsCoa() != null)
					listcell.setLabel(tbJournalTrx.getMsCoa().getVAcctName());
				
				strCell = PrintClient.getDate(tbJournalTrx.getDAplDate(), MedisafeConstants.DATE_FORMAT);
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(strCell);
				
				strCell = PrintClient.formatDouble(tbJournalTrx.getNDebit());
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(strCell);
				
				strCell = PrintClient.formatDouble(tbJournalTrx.getNCredit());
				listcell = new Listcell();
				listcell.setParent(listitem);
				listcell.setLabel(strCell);
			}
			
		} catch (VONEAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public List getGeneralLedgerByRange(int n_cod_id, Date dfrom, Date dto)
			throws VONEAppException {
		
		return dao.getGeneralLedgerByRange(n_cod_id, dfrom, dto);
	}

	
	public List getGeneralLedgerAll(Date from, Date to)
			throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getGeneralLedgerAll(from, to);
	}

	
	public List getTrialBalance(Date value) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getTrialBalance(value);
	}


	public List getNeracaByDate(Date value) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getNeracaByDate(value);
	}

	@Override
	public void saveRekapGl(TbGl gl) throws VONEAppException {
		this.glDao.save(gl);
	}

	@Override
	public List getRekapGlList() throws VONEAppException {
		// TODO Auto-generated method stub
		return this.glDao.getGlList();
	}

	@Override
	public void searchCoa(String text, Listbox listbox) throws VONEAppException{
		List<MsCoa> coas = this.dao.getMsCoas(text);
		listbox.getItems().clear();
		
		System.out.println("banyaknya data : "+coas.size());
		
		for (MsCoa coa : coas) {
			Listitem item = new Listitem();
			item.setValue(coa);
			item.setParent(listbox);
			
			Listcell cell = new Listcell();
			cell.setLabel(coa.getVAcctNo());
			cell.setParent(item);
			
			cell = new Listcell();
			cell.setLabel(coa.getVAcctName());
			cell.setParent(item);
		}
		
	}

}

package com.vone.medisafe.accounting;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsManager;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsGim;
import com.vone.medisafe.mapping.TbJournalTrx;
import com.vone.medisafe.service.MasterServiceLocator;

/**
 * This Class will help you to generate Journal Transaction
 * @author James Pang
 *
 */
public class JournalBeanHandler {

	private List<TbJournalTrx> listJournal;
	//journal type : AP,AR,GJ or ELSE
	private String journalType;
	private String journalBatchId;
	
	private JournalBeanHandler(){}
	
	public JournalBeanHandler(String journalType) throws VONEAppException{		
		listJournal = new ArrayList<TbJournalTrx>();
		
		this.journalType = journalType;
		
		createJournalBatchId();
	}
	
	public JournalBeanHandler(String journalType, String journalBatchId){
		listJournal = new ArrayList<TbJournalTrx>();
		
		this.journalType = journalType;
		this.journalBatchId = journalBatchId;
	}
	
	private void createJournalBatchId() throws VONEAppException{
		
		IdsManager idsManager =  IdsServiceLocator.getIdsManager();
		Integer sq = idsManager.getSequence(MedisafeConstants.SQ_JOURNAL_TRX);
		
		this.journalBatchId = this.journalType + StringUtils.addDigit(sq.toString(), 15, "0", StringUtils.LEFT);		
		
	}
	
	public String getJournalBatchId() {
		return journalBatchId;
	}

	public void setJournalBatchId(String journalBatchId) {
		this.journalBatchId = journalBatchId;
	}

	public String getJournalType() {
		return journalType;
	}

	public void setJournalType(String journalType) {
		this.journalType = journalType;
	}

	public void addJournal(String voucherNo, String memo, double amt, int balance, Date aplDate, String user, MsCoa coa){
		//todo sebaiknya dlm satu jbHandler tdk ada coa yg duplicate
		if (amt == 0) return;
		
		TbJournalTrx journalTrx = new TbJournalTrx();
		if (journalBatchId != null)
			journalTrx.setVJournalBatchId(this.journalBatchId);
		journalTrx.setVVoucherNo(voucherNo);
		journalTrx.setVDesc(memo);
		if (balance == MedisafeConstants.ACCT_DEBIT){
			journalTrx.setNDebit(amt);
			journalTrx.setNCredit(0);
		}else if (balance == MedisafeConstants.ACCT_CREDIT){
			journalTrx.setNDebit(0);
			journalTrx.setNCredit(amt);			
		}
		journalTrx.setDAplDate(aplDate);
		journalTrx.setMsCoa(coa);
		journalTrx.setVWhoCreate(user);
		journalTrx.setDWhnCreate(new Date());
		
		this.listJournal.add(journalTrx);
	}

	public List<TbJournalTrx> getListJournal() {
		return listJournal;
	}

	public void setListJournal(List<TbJournalTrx> listJournal) {
		this.listJournal = listJournal;
	}
	
	public boolean isBalance(){
		double debitAmt = 0;
		double creditAmt = 0;
		boolean result = false;
		
		Iterator<TbJournalTrx> it = this.listJournal.iterator();
		
		while (it.hasNext()){
			TbJournalTrx pojo = it.next();
			
			//test
			System.out.println("===============================");
			System.out.println("BATCH ID : "+pojo.getVJournalBatchId());
			System.out.println("VOUCHER NO : "+pojo.getVVoucherNo());
//			System.out.println("COA : "+pojo.getMsCoa());
			System.out.println("DEBIT AMT : "+pojo.getNDebit()+" ----- CREDIT AMT : "+pojo.getNCredit());
			
			System.out.println("===============================");
			//test
			
			//get Debit Amount
			debitAmt += pojo.getNDebit();
			//get Credit Amount
			creditAmt += pojo.getNCredit();
		}
		
		debitAmt = Math.round(debitAmt*100)/100.0;
		creditAmt = Math.round(creditAmt*100)/100.0;
		
		if (debitAmt == creditAmt)
			result = true;
		
		return result;
	}
	
	public MsCoa getCoaFromGim(String code) throws VONEAppException { 
		MsGim gim = MasterServiceLocator.getGimManager().getGimByCode(code);		
		if (gim == null)
			throw new VONEAppException(MessagesService.getKey("trx.coa.ar.null"));
		
		MsCoa coa = MasterServiceLocator.getCoaManager().getCoaByCode(gim.getVValue());
		
		if (coa == null)
			throw new VONEAppException(MessagesService.getKey("trx.coa.not.found")+" "+gim.getVValue());
		
		return coa;
	}
}

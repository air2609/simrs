package com.vone.medisafe.accounting;

import java.math.BigDecimal;
import java.util.Date;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbAccountPayable;
import com.vone.medisafe.mapping.TbAccountPayableDetail;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;

public class AccountPayableController extends BaseController {

	private static final String ZK_HISTORY = "/zkpages/accounting/paymentHistory.zul";
	private static final String ZK_JOURNAL = "/zkpages/accounting/journalDetail.zul";
	private static final String ZK_PAYMENT = "/zkpages/accounting/journalPayment.zul";
	private static final String PEMBAYARAN_MELEBIHI_HUTANG = "PEMBAYARAN TDK BOLEH MELEBIHI HUTANG";
	private static final String ISILAH_JUMLAH_PEMBAYARAN = "ISILAH JUMLAH PEMBAYARAN DENGAN BAIK DAN BENAR";
	private static final String ISILAH_REKENING = "ISILAH REKENING PEMBAYAR DENGAN BAIK DAN BENAR";
	private static final String PILIH_ITEM = "PILIH SALAH SATU ITEM DULU!!!";
	
	//	Window apScreen;
	Listbox apList;
	Button btnDetail;
	Button btnPaid;
	Button btnJournal;

	
	JournalManager journalManager = Service.getJournalManager();
	
	private Session session;
	private MsUser user;
	private TbAccountPayable tbAccountPayable;
	
	public void init(Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
//		apScreen = (Window)cmp.getFellow("apScreen"); 
		apList = (Listbox)cmp.getFellow("apList");
		btnDetail = (Button)cmp.getFellow("btnDetail");
		btnPaid = (Button)cmp.getFellow("btnPaid");
		btnJournal = (Button)cmp.getFellow("btnJournal");
		
		session = cmp.getDesktop().getSession();
		session.setAttribute("listbox", apList);

		user = getUserInfoBean().getMsUser();

		getAllAp();
		
	}

	/*
	 * mengisi apList dari tb_account_payable
	 * 1. buat pojo + hbm (blm ada?)
	 * 2. buat dao
	 * 
	 */
	private void getAllAp() throws VONEAppException {
		// todo done ambil semua ap masukkan ke apList
		  
		apList.getItems().clear();
		
		journalManager.getAllAp(apList);

		MiscTrxController.setFont(apList);

	}
	/*
	 * menampilkan daftar jurnal berdasar satu ap
	 * 1. tampilkan journalDetail.zul
	 * 2. isi detailList dengan 
	 * 
	 */
	public void lihatJurnalClick() throws VONEAppException, InterruptedException {
		if(apList.getSelectedCount()==0){
			Messagebox.show(PILIH_ITEM);
			return;
		}
		tbAccountPayable = (TbAccountPayable) apList.getSelectedItem().getValue();
		final Window win = (Window) Executions.createComponents(
				ZK_JOURNAL, null, null);
		Listbox detailList = (Listbox)win.getFellow("detailList");
		detailList.getItems().clear();
		
		journalManager.getJournalByBatch(tbAccountPayable.getTbJournalTrx().getVJournalBatchId(), detailList);

		MiscTrxController.setFont(detailList);

		win.doModal();
		
		apList.clearSelection();
		
	}
	
	public void pembayaranClick() throws VONEAppException, InterruptedException{
		//todo bayar ap
		if(apList.getSelectedCount()==0){
			Messagebox.show(PILIH_ITEM);
			return;
		}

		if(apList.getSelectedItem().getValue() == null){
			Messagebox.show(ISILAH_REKENING);
			return;
		}
		
		tbAccountPayable = (TbAccountPayable) apList.getSelectedItem().getValue();
		

		boolean munculLagi = true;
		while(munculLagi){
			final Window win = (Window) Executions.createComponents(
					ZK_PAYMENT, null, null);
			
			win.setAttribute("modalResult", 0);
			Listbox viaList = (Listbox) win.getFellow("viaList");
			
			Textbox memo = (Textbox) win.getFellow("memo");
			Decimalbox total = (Decimalbox) win.getFellow("total");
			
			total.setFormat(MedisafeConstants.CURRENCY_FORMAT);
			total.setValue(new BigDecimal(tbAccountPayable.getNTotalRemaining()));
				
			CoaController.getCoaForSelect(viaList, MedisafeConstants.COA_ALL);

			ZulConstraint constraints = new ZulConstraint();
			constraints.registerComponent(memo, ZulConstraint.UPPER_CASE);
			constraints.validateComponent(false);
			win.doModal();
			munculLagi = false;
			
			Integer a = (Integer) win.getAttribute("modalResult");
			if(a.intValue() == 1){
				//cek jika ada yg null maka ditolak mentah-2
				if(total.getValue() == null){
					Messagebox.show(ISILAH_JUMLAH_PEMBAYARAN);
					munculLagi = true;
				}
				if(!munculLagi)
				if((viaList.getSelectedItem().getValue() == null)||(viaList.getSelectedIndex() == 0)){
					Messagebox.show(ISILAH_REKENING);
					munculLagi = true;
				}
				String desc = memo.getValue();
				double totBayar = total.getValue().doubleValue();
				if(!munculLagi)
				if(tbAccountPayable.getNTotalRemaining() < totBayar){
					Messagebox.show(PEMBAYARAN_MELEBIHI_HUTANG);
					munculLagi = true;
				}
				if(!munculLagi)
					if(totBayar<=0){
						Messagebox.show("PEMBAYARAN HARUS POSITIP");
						munculLagi = true;
					}
				
				//todo save pembayaran
				/*
				 * step:
				 * 1. buat jurnal balik (2 row) (TbJournalTrx)
				 * 2. update n_total_remaining (TbAccountPayable)
				 * 3. 
				 * 
				 */
				
				if(!munculLagi){
					JournalBeanHandler jbHandler = new JournalBeanHandler(MedisafeConstants.ACCT_PMT_STR);
					String voucherNo = MedisafeUtil.generateVoucherNo();
					
					Date aplDate = new Date();
					MsCoa coa;
					
					//bikin jurnal balik
					coa = (MsCoa)viaList.getSelectedItem().getValue();
					jbHandler.addJournal(voucherNo, desc, totBayar, MedisafeConstants.ACCT_CREDIT, aplDate, user.getVUserName(), coa);
					
					coa = (MsCoa) tbAccountPayable.getTbJournalTrx().getMsCoa(); 
					jbHandler.addJournal(voucherNo, desc, totBayar, MedisafeConstants.ACCT_DEBIT, aplDate, user.getVUserName(), coa);
					
					//kurangi sisa hutang
					tbAccountPayable.setNTotalRemaining(tbAccountPayable.getNTotalRemaining() - totBayar);
					
					//todo add tb_account_payable_detail
					TbAccountPayableDetail tbAccountPayableDetail = new TbAccountPayableDetail();
					
					tbAccountPayableDetail.setTbAccountPayable(tbAccountPayable);
					tbAccountPayableDetail.setDWhnCreate(new Date());
					tbAccountPayableDetail.setVWhoCreate(user.getVUserName());
					//tbAccountPayableDetail.setVJournalBatchId(VJournalBatchId);
					
					boolean success = false;
					if(jbHandler.isBalance())
						success = journalManager.savePayment(jbHandler, tbAccountPayable, tbAccountPayableDetail);
					if(success) getAllAp();
				}
			}
		}
	}
	
	public void historyClick() throws VONEAppException, InterruptedException{
		if(apList.getSelectedCount()==0){
			Messagebox.show(PILIH_ITEM);
			return;
		}

		tbAccountPayable = (TbAccountPayable) apList.getSelectedItem().getValue();
		final Window win = (Window) Executions.createComponents(
				ZK_HISTORY, null, null);
		Listbox detailList = (Listbox)win.getFellow("detailList");
		detailList.getItems().clear();
		
		journalManager.getJournalByApId(tbAccountPayable.getNApId(), detailList);
		
		MiscTrxController.setFont(detailList);
		win.doModal();
		
		
	}
}

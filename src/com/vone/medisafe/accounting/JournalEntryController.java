package com.vone.medisafe.accounting;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;

public class JournalEntryController  extends BaseController {
	private static final String LIST_FONT = "font-size:8pt";

	private static final String VOUCHER_ATTR = "VOUCHER";
	
	private static final String DESCRIPTION = "DESCRIPTION";

	private static final String UNBALANCE_TRANSACTION = "TRANSAKSI TIDAK BALANCE";

	private static final String SAVE_FAILURE = "TRANSAKSI GAGAL DISIMPAN";

	private static final String SAVE_SUCCESS = "TRANSAKSI TELAH BERHASIL DISIMPAN";

	private static final String CURRENCY_FORMAT = MedisafeConstants.CURRENCY_FORMAT;

	private static final String CREDIT_ATTR = "kredit";

	private static final String DEBET_ATTR = "debet";
	//zk: accounting/journalEntry.zul
	
	JournalManager journalManager = Service.getJournalManager();
	Datebox aplDate;
	Textbox voucherTextbox;
	Textbox descTextbox;
	Listbox coaList;
	Listbox itemList;
	Intbox intDebet;
	Intbox intCredit;
	Button btnSave;
	Button btnNew;
	Button btnPrint;
	Button btnDelete;
	int sumDebet;
	int sumCredit;
	
	
	ZulConstraint constraints = new ZulConstraint();

	public void init(Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
		aplDate = (Datebox)cmp.getFellow("aplDate");
		voucherTextbox = (Textbox)cmp.getFellow("voucherTextbox");
		descTextbox = (Textbox)cmp.getFellow("descTextbox");
		coaList = (Listbox)cmp.getFellow("coaList");
		intDebet = (Intbox)cmp.getFellow("intDebet");
		intCredit = (Intbox)cmp.getFellow("intCredit");
		itemList = (Listbox)cmp.getFellow("itemList");
		btnSave = (Button)cmp.getFellow("btnSave");
		btnDelete = (Button)cmp.getFellow("btnDelete");
		btnNew = (Button)cmp.getFellow("btnNew");
		btnPrint = (Button)cmp.getFellow("btnPrint");
		
		CoaController.getCoaForSelect(coaList, MedisafeConstants.COA_ALL);
		itemList.getItems().clear();
		intDebet.setFormat(CURRENCY_FORMAT);
		intCredit.setFormat(CURRENCY_FORMAT);
		
		constraints.registerComponent(descTextbox, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(voucherTextbox, ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		
		clear();
		voucherTextbox.focus();
	}
	
	public void tambahClick() throws InterruptedException, VONEAppException{
		if(coaList.getSelectedCount()==0){
			return;
		}
		if(intDebet.getValue() == null && intCredit.getValue()==null){
			return;
		}
		if(!(coaList.getSelectedItem().getValue() instanceof MsCoa)){
			return;
		}
		MsCoa coa = (MsCoa)coaList.getSelectedItem().getValue();
		
//		MsCoaType msCoaType = coa.getMsCoaType();
//		System.out.println(msCoaType.getVCtName());
		
		Listitem listItem = new Listitem();
		listItem.setParent(itemList);
		listItem.setValue(coa);
		
		//arif added description on april 11, 2011
		listItem.setAttribute(DESCRIPTION, this.descTextbox.getValue());
		
		Listcell listCell = new Listcell();
		listCell.setLabel(coa.getVAcctNo());
		listCell.setParent(listItem);
		listCell.setStyle(LIST_FONT);
		
		listCell = new Listcell();
		listCell.setLabel(coa.getVAcctName());
		listCell.setParent(listItem);
		listCell.setStyle(LIST_FONT);
		
		Intbox iDebit = new Intbox();
		iDebit.setValue(intDebet.getValue());
		listItem.setAttribute(DEBET_ATTR, iDebit);
		iDebit.setFormat(CURRENCY_FORMAT);
		
		
		Intbox iCredit= new Intbox();
		iCredit.setValue(intCredit.getValue());
		listItem.setAttribute(CREDIT_ATTR, iCredit);
		iCredit.setFormat(CURRENCY_FORMAT);
		
		listItem.setAttribute(VOUCHER_ATTR, voucherTextbox.getText());
		
		listCell = new Listcell(voucherTextbox.getText());
		listCell.setParent(listItem);
		listCell.setStyle(LIST_FONT);
		
		listCell = new Listcell(iDebit.getText());
		listCell.setParent(listItem);
		listCell.setStyle(LIST_FONT);
		
		listCell = new Listcell(iCredit.getText());
		listCell.setParent(listItem);
		listCell.setStyle(LIST_FONT);
		
		sumDebet = sumDebet + intDebet.intValue();
		sumCredit = sumCredit + intCredit.intValue();
		
		intDebet.setValue(null);
		intCredit.setValue(null);
//		if(sumDebet>sumCredit){
//			intCredit.setValue(sumDebet-sumCredit);
//			intDebet.setValue(null);
//		}else{
//			intDebet.setValue(sumCredit - sumDebet);
//			intCredit.setValue(null);
//		}
//		if (sumDebet==sumCredit) {
//			intDebet.setValue(null);
//			intCredit.setValue(null);
//		}		
		//coaList.selectItem(null);
		coaList.setSelectedIndex(0);
		coaList.focus();
	}
	
	public void saveClick() throws InterruptedException, VONEAppException{
		if(itemList.getItems().size()==0){
			Messagebox.show("ISILAH TRANSAKSINYA");
			return;
		}
			
		JournalBeanHandler jbHandler = new JournalBeanHandler(MedisafeConstants.ACCT_GJ_STR);
		MsUser user = getUserInfoBean().getMsUser();
		String voucherNo = voucherTextbox.getValue();
		String memo = "";//descTextbox.getValue(); uncomment by Arif on 11 april 2011
		
		double amt= 0;
		MsCoa coa = null;
		
		
		List list = itemList.getItems();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Listitem listItem = (Listitem) iter.next();
			coa = (MsCoa) listItem.getValue();
			memo = (String)listItem.getAttribute(DESCRIPTION);
			Intbox debet = (Intbox) listItem.getAttribute(DEBET_ATTR);
			Intbox credit = (Intbox) listItem.getAttribute(CREDIT_ATTR);
			voucherNo = (String)listItem.getAttribute(VOUCHER_ATTR);
			if(debet.intValue()>0){
				amt = debet.intValue();
				jbHandler.addJournal(voucherNo, memo, amt, MedisafeConstants.ACCT_DEBIT, aplDate.getValue(), user.getVUserName(), coa);
			}else{
				amt = credit.intValue();
				jbHandler.addJournal(voucherNo, memo, amt, MedisafeConstants.ACCT_CREDIT, aplDate.getValue(), user.getVUserName(), coa);
			}
			listItem.removeAttribute(DESCRIPTION);
		}
		boolean success = false;
		if(jbHandler.isBalance())
			success = journalManager.saveJournal(jbHandler);
		else{
			Messagebox.show(UNBALANCE_TRANSACTION);
			return;
		}
		if(success){
			Messagebox.show(SAVE_SUCCESS);
			clear();
			btnPrint.setVisible(true);
		}else{
			Messagebox.show(SAVE_FAILURE);
		}
		voucherTextbox.focus();
	}
	
	public void clear() {
		itemList.getItems().clear();
		intDebet.setValue(null);
		intCredit.setValue(null);
//		voucherTextbox.setValue(null);
		descTextbox.setValue(null);
		aplDate.setValue(new Date());
		sumDebet = 0;
		sumCredit = 0;
		//coaList.selectItem(null);
		coaList.setSelectedIndex(0);
		voucherTextbox.focus();
		btnPrint.setVisible(false);
	}
	
	public void deleteClick() throws InterruptedException, VONEAppException{
		if (itemList.getSelectedItem() == null) {
			Messagebox.show(MessagesService
					.getKey("common.modify.list.notselected"));
			return;
		}

		int index = itemList.getSelectedIndex();
		int confirm = Messagebox.show(MessagesService
				.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
				| Messagebox.NO, Messagebox.NONE);
		if (confirm == Messagebox.NO)
			return;
		itemList.removeItemAt(index);
	}
	
	
	
	public void printToPdf() throws VONEAppException{
		
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" j.v_voucher_no,");
		sb.append(" j.v_desc,");
		sb.append(" c.v_acct_name || '['|| c.v_acct_no || ']' as account,");
		sb.append(" j.n_debit,");
		sb.append(" j.n_credit,");
		sb.append(" j.d_whn_create,");
		sb.append(" j.v_who_create");
		
		sb.append(" from");
		sb.append(" tb_journal_trx j");
		sb.append(" left join ms_coa c on j.n_coa_id=c.n_coa_id");
		
		sb.append(" where j.v_voucher_no='"+this.voucherTextbox.getText()+"'");
		
		Map<String, String> parameters = new HashMap<String, String>(); 
		parameters.put("VOUCHER_NO", voucherTextbox.getText());
		
		ReportEngine re = new ReportEngine(sb.toString(), 
				ReportService.getKey("manual.jurnal"), parameters);
		if(!re.printOut(voucherTextbox.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
		
	}
	
}

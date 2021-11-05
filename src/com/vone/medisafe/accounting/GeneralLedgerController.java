package com.vone.medisafe.accounting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

public class GeneralLedgerController extends BaseController {
	
	private static final String ZK_JOURNAL = "/zkpages/accounting/journalDetail.zul";
	private static final String PILIHLAH_ITEM_JOURNAL = "PILIHLAH ITEM JOURNAL DULU!!!";
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	JournalManager journalManager = Service.getJournalManager();
	Listbox coaList;
	Listbox detailList;
	Datebox dfrom;
	Datebox dto;
	private MsCoa coa;
	private Listitem listItem;
	private Listcell listCell;
	
	public void init(Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
		coaList = (Listbox)cmp.getFellow("coaList");
		detailList = (Listbox)cmp.getFellow("detailList");
		dfrom = (Datebox)cmp.getFellow("dfrom");
		dto = (Datebox)cmp.getFellow("dto");
		CoaController.getCoaForSelect(coaList, MedisafeConstants.COA_ALL);
		detailList.getItems().clear();
	}
	
	public void getGLAll() throws VONEAppException, InterruptedException{
		if(dfrom.getValue() == null || dto.getValue() == null){
			Messagebox.show("TANGGAL HARUS DI ISI!");
			return;
		}
		
		detailList.getItems().clear();
		List list = journalManager.getGeneralLedgerAll(dfrom.getValue(), dto.getValue());
		
//		List list = journalManager.getGeneralLedgerByRange(coa.getNCoaId(), dfrom.getValue(), dto.getValue());
		Iterator it = list.iterator();
		Decimalbox db = new Decimalbox();
		db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
//			Integer n_row = (Integer)obj[0];
//			Integer n_coa_id = (Integer)obj[1];
			Date d_apl_date = (Date)obj[2];
//			Integer n_journal_id = (Integer)obj[3];
			String v_journal_batch_id = (String)obj[4];
			String v_voucher_no = (String)obj[5];
			String v_desc = (String)obj[6];
			Float n_debit = (Float)obj[7];
			Float n_credit = (Float)obj[8];
			Float n_balance = (Float)obj[9];
			String v_acct_name = (String)obj[10];
			
			String actNo = null;
			String actName = null;
			if(v_acct_name != null){
				actNo = this.getAccount(v_acct_name, 1);
				actName = this.getAccount(v_acct_name, 0);
			}
			
			
			listItem = new Listitem();
			listItem.setLabel(actNo);
			listItem.setParent(detailList);
			listItem.setValue(v_journal_batch_id);
			
			listCell = new Listcell();
			listCell.setLabel(actName);
			listCell.setParent(listItem);
			
			listCell = new Listcell();
			listCell.setLabel(v_voucher_no);
			listCell.setParent(listItem);

			listCell = new Listcell();
			listCell.setLabel(v_desc);
			listCell.setParent(listItem);
			
			listCell = new Listcell();
			listCell.setLabel(PrintClient.getDate(d_apl_date,DATE_FORMAT));
			listCell.setParent(listItem);
			
			listCell = new Listcell();
			listCell.setParent(listItem);
			if(n_debit != null){
				listCell.setLabel(n_debit.toString());
			}
			listCell = new Listcell();
			listCell.setParent(listItem);
			if(n_credit != null){
				listCell.setLabel(n_credit.toString());
			}
			listCell = new Listcell();
			if(n_balance != null){
				db.setValue(new BigDecimal(n_balance));
				listCell.setLabel(db.getText());
			}
			listCell.setParent(listItem);
		}
		MiscTrxController.setFont(detailList);

	}
	
	public void coaClick() throws VONEAppException, InterruptedException {
		if(!(coaList.getSelectedItem().getValue() instanceof MsCoa)){
			detailList.getItems().clear();
			return;
		}
		
		if(dfrom.getValue() == null || dto.getValue() == null){
			Messagebox.show("TANGGAL HARUS DI ISI!");
			return;
		}
		
		coa = (MsCoa)coaList.getSelectedItem().getValue();
		detailList.getItems().clear();
//		List list = journalManager.getGeneralLedger(coa.getNCoaId());
		List list = journalManager.getGeneralLedgerByRange(coa.getNCoaId(), dfrom.getValue(), dto.getValue());
		Iterator it = list.iterator();
		Decimalbox db = new Decimalbox();
		db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
//			Integer n_row = (Integer)obj[0];
//			Integer n_coa_id = (Integer)obj[1];
			Date d_apl_date = (Date)obj[2];
//			Integer n_journal_id = (Integer)obj[3];
			String v_journal_batch_id = (String)obj[4];
			String v_voucher_no = (String)obj[5];
			String v_desc = (String)obj[6];
			Float n_debit = (Float)obj[7];
			Float n_credit = (Float)obj[8];
			Float n_balance = (Float)obj[9];
			String v_acct_name = (String)obj[10];
			String actNo = null;
			String actName = null;
			if(v_acct_name != null){
				actNo = this.getAccount(v_acct_name, 1);
				actName = this.getAccount(v_acct_name, 0);
			}
			listItem = new Listitem();
//			listItem.setLabel(v_journal_batch_id);
			listItem.setLabel(actNo);
			listItem.setParent(detailList);
			listItem.setValue(v_journal_batch_id);
			
			listCell = new Listcell();
			listCell.setLabel(actName);
			listCell.setParent(listItem);
			
			
			listCell = new Listcell();
			listCell.setLabel(v_voucher_no);
			listCell.setParent(listItem);

			listCell = new Listcell();
			listCell.setLabel(v_desc);
			listCell.setParent(listItem);
			
			listCell = new Listcell();
			listCell.setLabel(PrintClient.getDate(d_apl_date,DATE_FORMAT));
			listCell.setParent(listItem);
			
			listCell = new Listcell();
			listCell.setParent(listItem);
			if(n_debit != null){
				listCell.setLabel(n_debit.toString());
			}
			listCell = new Listcell();
			listCell.setParent(listItem);
			if(n_credit != null){
				listCell.setLabel(n_credit.toString());
			}
			listCell = new Listcell();
			if(n_balance != null){
				db.setValue(new BigDecimal(n_balance));
				listCell.setLabel(db.getText());
			}
			listCell.setParent(listItem);
		}
		MiscTrxController.setFont(detailList);

	}
	
	public void cetakClick() throws VONEAppException, InterruptedException {
		String query;
		//todo buat zk range tgl
		
		final Window win = (Window) Executions.createComponents(
				"/zkpages/common/dateRange.zul", null, null);
		win.setAttribute("aksi", 0);
		Datebox dateFrom = (Datebox) win.getFellow("dateFrom");
		Datebox dateTo = (Datebox) win.getFellow("dateTo");
		
		dateFrom.setValue(new Date());
		dateTo.setValue(new Date());
		
		win.doModal();
		Object aksi = win.getAttribute("aksi");
		
		if(aksi.equals(1)){
			String d1 = PrintClient.getDate(dateFrom.getValue(), "MM/dd/yyyy");
			String d2 = PrintClient.getDate(dateTo.getValue(), "MM/dd/yyyy");
			
			query = "select * from report.func_gl_bydate(" +
				coa.getNCoaId().intValue() + 
				",'" +
				d1 +
				"','" +
				d2 +
				"'" +
				")";
			
			Map<String, String> parameters = new HashMap<String, String>();
			String dateParam = "DATE RANGE: " + PrintClient.getDate(dateFrom.getValue(), "dd/MM/yyyy") +
				" S/D " +
				PrintClient.getDate(dateTo.getValue(), "dd/MM/yyyy");
			parameters.put("dateParam", dateParam );
			
			ReportEngine re = new ReportEngine(query,ReportService.getKey("generalLedger"), parameters);
			re.openPdf();
		}
	}
	
	public void cetakAllClick() throws VONEAppException, InterruptedException {
		String query;
		
		query = "select * from report.func_gl_all()";

		Map<String, String> parameters = new HashMap<String, String>();
		
		ReportEngine re = new ReportEngine(query,ReportService.getKey("generalLedger"), parameters);
		re.openPdf();

	}
	
	public void journalEntryClick() throws InterruptedException, VONEAppException{
		if(detailList.getSelectedCount()==0){
			Messagebox.show(PILIHLAH_ITEM_JOURNAL);
			return;
		}
		listItem = (Listitem)detailList.getSelectedItem();
		
		Window win = null;
		win = (Window) Executions.createComponents(
				ZK_JOURNAL, null, null);
		//Label labelCaption = (Label) win.getFellow("labelCaption");
		Listbox detailList = (Listbox)win.getFellow("detailList");
		detailList.getItems().clear();
		//labelCaption.setValue("JOURNAL ENTRY");
		
		String journalBatchId = (String) listItem.getValue();
		
		journalManager.getJournalByBatch(journalBatchId, detailList);
		
		MiscTrxController.setFont(detailList);
		win.doModal();
	}
	
	public void exportToXls() throws VONEAppException,InterruptedException{
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		
		HSSFRow row = sheet.createRow((short)1);
		
		Decimalbox dbox = new Decimalbox();
    	dbox.setFormat("#,###,##");
    	
    	int i = 1;
		for (Object head : detailList.getHeads()) {
	          String h = "";
	          int j = 0;
	          row = sheet.createRow((short)i++);
	          for (Object header : ((Listhead) head).getChildren()) {
	            h = ((Listheader) header).getLabel();
	            row.createCell((short)j).setCellValue(h);
	            row.getCell((short)j).setCellStyle(style);
	            j++;
	          }
		}
		
		row = sheet.createRow((short)0);
		row.createCell((short)0).setCellValue("GENERAL LEDGER : " + coaList.getSelectedItem().getLabel()+ " PERIODE "+dfrom.getText()+" - "+ dto.getText());
		row.getCell((short)0).setCellStyle(style);
		
		List<Listitem> items = detailList.getItems();
		for(Listitem item : items){
			List<Listcell> cells = item.getChildren();
			int j = 0;
			row = sheet.createRow((short)i++);
			for(Listcell cell : cells){
				if(j==5 || j== 6 || j==7){
					if(cell.getLabel().equalsIgnoreCase(""))
						dbox.setText("0");
					else dbox.setText(cell.getLabel());
					row.createCell((short)j++).setCellValue(dbox.getValue().doubleValue());
				}
				else
					row.createCell((short)j++).setCellValue(cell.getLabel());
			}
			
		}
		
		try
		{
			String filename = this.getUserInfoBean().getStUserId()+".xls";
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"\\"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		
		
	}
	
	private String getAccount(String str, int type){
		String al = str.replace("[", "&");
		al = al.replace("]", "");
		String[] ar = al.split("&");
		String acct = "";
		if(type == 0)
			acct = ar[0];
		else acct = ar[1];
		
		return acct;
	}
}

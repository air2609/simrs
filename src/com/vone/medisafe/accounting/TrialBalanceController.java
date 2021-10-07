package com.vone.medisafe.accounting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.base.BaseController;

public class TrialBalanceController extends BaseController{

	
	JournalManager journalManager = Service.getJournalManager();
	Listbox detailList;
	Datebox dto;
	private Listitem listItem;
	private Listcell listCell;
	
	public void init(Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
		detailList = (Listbox)cmp.getFellow("detailList");
		dto = (Datebox)cmp.getFellow("dto");
		detailList.getItems().clear();
	}
	
	public void getGLAll() throws VONEAppException, InterruptedException{
		if(dto.getValue() == null){
			Messagebox.show("TANGGAL HARUS DI ISI!");
			return;
		}
		
		detailList.getItems().clear();
		List list = journalManager.getTrialBalance(dto.getValue());
		Iterator it = list.iterator();
		Decimalbox db = new Decimalbox();
		db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			Float n_debit = (Float)obj[1];
			Float n_credit = (Float)obj[2];
			Float n_balance = (Float)obj[3];
			String v_acct_name = (String)obj[0];
			
			String actNo = null;
			String actName = null;
			if(v_acct_name != null){
				actNo = this.getAccount(v_acct_name, 1);
				actName = this.getAccount(v_acct_name, 0);
			}
			
			listItem = new Listitem();
			listItem.setLabel(actNo);
			listItem.setParent(detailList);
			listItem.setValue(v_acct_name);
			
			listCell = new Listcell(actName);
			listCell.setParent(listItem);
						
			listCell = new Listcell();
			listCell.setParent(listItem);
			if(n_debit != null){
				db.setValue(new BigDecimal(n_debit));
				listCell.setLabel(db.getText());
			}
			listCell = new Listcell();
			listCell.setParent(listItem);
			if(n_credit != null){
				db.setValue(new BigDecimal(n_credit));
				listCell.setLabel(db.getText());
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
		row.createCell((short)0).setCellValue("TRIAL BALANCE PERIODE : "+ dto.getText());
		row.getCell((short)0).setCellStyle(style);
		
		List<Listitem> items = detailList.getItems();
		for(Listitem item : items){
			List<Listcell> cells = item.getChildren();
			int j = 0;
			row = sheet.createRow((short)i++);
			for(Listcell cell : cells){
				if(j==2 || j== 3 || j==4){
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

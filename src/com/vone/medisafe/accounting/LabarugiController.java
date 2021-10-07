package com.vone.medisafe.accounting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.base.BaseController;

public class LabarugiController  extends BaseController {
	JournalManager journalManager = Service.getJournalManager();
	Session session;
	
	Tree labaRugiTree;
	Treechildren child;
	Treeitem treeItem;
	Treerow treeRow;
	Treechildren children;
	Treecell treeCell;
	Treechildren labaRugiTreeChild;
	Datebox endDate;
	Datebox startDate;

	public void init(Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
		labaRugiTree = (Tree)cmp.getFellow("labaRugiTree");
		labaRugiTreeChild = (Treechildren)cmp.getFellow("labaRugiTreeChild");
		startDate = (Datebox)cmp.getFellow("startDate");
		endDate = (Datebox)cmp.getFellow("endDate");
		session = cmp.getDesktop().getSession();
		endDate.setValue(new Date());
		startDate.setValue(new Date());
		openLr();
	}
	
	private void openLr() throws VONEAppException{
		labaRugiTreeChild.getChildren().clear();
	}
	
	public void printLabarugi() throws VONEAppException{
//		session = win.getDesktop().getSession(); 
		String plQuery = "select * from report.profit_loss_bydate('" +
			PrintClient.getDate(startDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL) +
			"','" +
			PrintClient.getDate(endDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL) +
			"')";
		System.out.println(plQuery);
		Map<String, String> parameters = new HashMap<String, String>();
		String dateParam = "DATE RANGE: ";
		parameters.put("dateParam", dateParam );
		ReportEngine re = new ReportEngine(plQuery,ReportService.getKey("profitLoss"), parameters);
		if(!re.printOut(session.getClientAddr()))
			re.openPdf();
		
	}
	
	public String getCaption(int i) {
		if(i == 1)
			return "AKTIVA";
		else if (i == 2)
			return "KEWAJIBAN";
		else if (i == 3)
			return "MODAL";
		else 
			return ""+i;
		
	}

	public void cariClick() throws VONEAppException{
		List list = journalManager.getLabarugi(startDate.getValue(), endDate.getValue());
		
		labaRugiTreeChild.getChildren().clear();
		
		Iterator iter = list.iterator();
		Object[] obj;
		Integer n_row;
		Integer n_coa_id;
		String v_acct_name;
		Float n_balance;
		int x1 = 0; 
		MsCoa msCoa;
		while (iter.hasNext()) {
			obj = (Object[])iter.next();
			n_row = (Integer)obj[0];
			n_coa_id = (Integer)obj[1];
			v_acct_name = (String)obj[2];
			n_balance = (Float)obj[3];
			msCoa = journalManager.getMsCoa(n_coa_id);
			if(x1 != n_row.intValue()){
				//buat group
				if(n_row.intValue()<6)
					createGroupTree(null,msCoa.getMsCoaType().getVCtName());
				else
					createGroupTree(null,"RINGKASAN");
			}
			if(n_row.intValue()<6)
				createItemTree(null,msCoa.getVAcctNo(),msCoa.getVAcctName(), PrintClient.formatDouble(n_balance));
			else
				createItemTree(null,v_acct_name,"", PrintClient.formatDouble(n_balance));
			x1 = n_row.intValue();
		}
		MiscTrxController.setFont((Tree)labaRugiTreeChild.getParent());
	}
	
	public void createGroupTree(Object value, String nomorPermintaan) {
		treeItem = new Treeitem();
		treeItem.setValue(value);
		treeItem.setParent(labaRugiTreeChild);
		treeItem.setAttribute("tipe", "group");

		treeRow = new Treerow();
		treeRow.setParent(treeItem);
		
		treeCell = new Treecell(nomorPermintaan);
		treeCell.setParent(treeRow);
		
		children = new Treechildren();
		children.setParent(treeItem);
		
	}

	public void createItemTree(Object value, String itemCaption, String coa,String satuan) {
		Treeitem it;
		it = new Treeitem();
		it.setValue(value);
		it.setParent(children);
		it.setAttribute("tipe", "anak");

		treeRow = new Treerow();
		treeRow.setParent(it);
		treeCell = new Treecell(itemCaption);
		treeCell.setParent(treeRow);
		it.setAttribute("nama", itemCaption);

		treeCell = new Treecell(coa);
		treeCell.setParent(treeRow);

		treeCell = new Treecell(satuan);
		treeCell.setParent(treeRow);
		it.setAttribute("satuan", satuan);
		

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
		
		HSSFRow row = sheet.createRow((short)1);
		
		Decimalbox dbox = new Decimalbox();
    	dbox.setFormat("#,###,##");
		
		String lbl = "";
		int columns = 0;
		
		Treecols head = this.labaRugiTree.getTreecols();
		int i = 1;
		row = sheet.createRow((short)i++);
		for(Object obj : head.getChildren()){
			lbl = ((Treecol) obj).getLabel();
			row.createCell((short)columns).setCellValue(lbl);
			row.getCell((short)columns).setCellStyle(style);
			columns++;
		}
		
		row = sheet.createRow((short)0);
		row.createCell((short)0).setCellValue("LAPORAN LABA RUGI PERIODE : "+this.startDate.getText() +" - "+ this.endDate.getText());
		row.getCell((short)0).setCellStyle(style);
		
		
		
		Treeitem item = null;
		Treeitem item2 = null;
		Treerow trow = null;
		Treerow trow2 = null;
		
		for(Object obj : this.labaRugiTreeChild.getChildren()){
			
			item = (Treeitem)obj;
			trow = item.getTreerow();
			row = sheet.createRow((short)i++);
			treeCell = (Treecell)trow.getChildren().get(0);
			row.createCell((short)0).setCellValue(treeCell.getLabel());
			row.getCell((short)0).setCellStyle(style);
			
			for(Object titem : item.getTreechildren().getChildren()){
				item2 = (Treeitem)titem;
				trow2 = item2.getTreerow();
				row = sheet.createRow((short)i++);
				columns = 0;
				for(Object tcell : trow2.getChildren()){
					if(columns == 2){
						dbox.setText(((Treecell)tcell).getLabel());
						row.createCell((short)columns).setCellValue(dbox.getValue().doubleValue());
					}
					else{
						row.createCell((short)columns).setCellValue(((Treecell)tcell).getLabel());
					}
					
					row.getCell((short)columns).setCellStyle(style);
					columns++;
				}
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
}

package com.vone.medisafe.accounting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.misc.MiscTrxController;


public class NeracaController  extends BaseController {
	JournalManager journalManager = Service.getJournalManager();
	Session session;
	
	Treechildren child;
	Treeitem treeItem;
	Tree neracaTree;
	Treerow treeRow;
	Treechildren children;
	Treecell treeCell;
	Treechildren neracaTreechildren;
	
	Datebox periode;

	public void init(Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
		neracaTree = (Tree)cmp.getFellow("neracaTree");
		periode = (Datebox)cmp.getFellow("periode");
		neracaTreechildren = (Treechildren)cmp.getFellow("neracaTreechildren");
		session = cmp.getDesktop().getSession();
		neracaTreechildren.getChildren().clear();
//		openNeraca();
	}
	
	public void openNeraca() throws VONEAppException{
		neracaTreechildren.getChildren().clear();
//		List list = journalManager.getNeraca();
		List list = journalManager.getNeracaByDate(this.periode.getValue());
		Iterator iter = list.iterator();
		Object[] obj;
		Integer n_row;
		Integer n_coa_id;
//		String v_acct_name;
		Float n_balance;
		int x1 = 0; 
		MsCoa msCoa;
		while (iter.hasNext()) {
			obj = (Object[])iter.next();
			n_row = (Integer)obj[0];
			n_coa_id = (Integer)obj[1];
//			v_acct_name = (String)obj[2];
			n_balance = (Float)obj[3];
			msCoa = journalManager.getMsCoa(n_coa_id);
			if(x1 != n_row.intValue()){
				//buat group
				createGroupTree(null,getCaption(n_row.intValue()));
			}
			createItemTree(null,msCoa.getVAcctNo(),msCoa.getVAcctName(), PrintClient.formatDouble(n_balance));
			x1 = n_row.intValue();
		}
		MiscTrxController.setFont((Tree)neracaTreechildren.getParent());
	}

	private String getCaption(int i) {
		if(i == 1)
			return "AKTIVA";
		else if (i == 2)
			return "KEWAJIBAN";
		else if (i == 3)
			return "MODAL";
		else 
			return "";
		
	}

	public void createGroupTree(Object value, String nomorPermintaan) {
		treeItem = new Treeitem();
		treeItem.setValue(value);
		treeItem.setParent(neracaTreechildren);
		treeItem.setAttribute("tipe", "group");

		treeRow = new Treerow();
		treeRow.setParent(treeItem);
		
		treeCell = new Treecell(nomorPermintaan);
		treeCell.setParent(treeRow);
//		treeCell.addEventListener("onClick", new GroupItemListener());
		
//		treeCell = new Treecell(namaGudang);
//		treeCell.setParent(treeRow);
//		treeCell.addEventListener("onClick", new GroupItemListener());
		
		children = new Treechildren();
		children.setParent(treeItem);
		
	}

	public void createItemTree(Object value, String itemCaption, String coaName, String satuan) {

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

		treeCell = new Treecell(coaName);
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
		
		Treecols head = this.neracaTree.getTreecols();
		int i = 1;
		row = sheet.createRow((short)i++);
		for(Object obj : head.getChildren()){
			lbl = ((Treecol) obj).getLabel();
			row.createCell((short)columns).setCellValue(lbl);
			row.getCell((short)columns).setCellStyle(style);
			columns++;
		}
		
		row = sheet.createRow((short)0);
		row.createCell((short)0).setCellValue("NERACA");
		row.getCell((short)0).setCellStyle(style);
		
		Treeitem item = null;
		Treeitem item2 = null;
		Treerow trow = null;
		Treerow trow2 = null;
		
		for(Object obj : this.neracaTreechildren.getChildren()){
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

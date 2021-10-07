package com.vone.medisafe.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.cashier.CashierManager;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbPatientBill;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class LaporanRekapKasir extends BaseController{
	
	Listbox pendapatanList;
	Datebox dateFrom;
	Datebox dateTo;
	Decimalbox tunai;
	Decimalbox nontunai;
	Decimalbox totalCard;
	Listbox rekapList;
	Listbox tipeList;
	
	
//	NoteManager service = Service.getNotaManager();
	CashierManager manager = Service.getCashierManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		pendapatanList = (Listbox)cmp.getFellow("pendapatanList");
		dateFrom = (Datebox) cmp.getFellow("dateFrom");
		dateTo = (Datebox)cmp.getFellow("dateTo");
		rekapList = (Listbox)cmp.getFellow("rekapList");
		tipeList = (Listbox)cmp.getFellow("tipeList");
		tunai = (Decimalbox)cmp.getFellow("tunai");
		nontunai = (Decimalbox)cmp.getFellow("nontunai");
		totalCard = (Decimalbox)cmp.getFellow("totalCard");
	}
	
	public void generateKwitansi() throws Exception {
		Listitem item = rekapList.getSelectedItem();
		String billCode = ((Listcell)item.getChildren().get(1)).getLabel();
		String path = this.getCurrentSession().getWebApp().getRealPath("tmp")+"/";
		
		String filename = manager.generateKwitansi(billCode, path);
		
		String name = filename.substring(path.length(), filename.length());
		
		System.out.println("path : " +name);
		
		
		try
		{
			
			FileInputStream in = new FileInputStream(filename);
			Filedownload.save(in, "application/pdf", name);
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	
	}
	
	
	public void selectview(){
	}
	
	public void openClick() throws InterruptedException,VONEAppException{
		
		//check if date already filled..if no stop process and give a message warning
		if(dateFrom.getValue() == null || dateTo.getValue() == null){
			Messagebox.show("Kedua tanggal harus diisi....!");
			return;
		}
				
		
		String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/";
				
		manager.getRekapKasir(pendapatanList, tipeList, dateFrom, dateTo, rekapList, tunai, nontunai,totalCard, path);
		
		
	}
	
	public void saveToXLS(Listbox list){

		
		//if(laporan.getItems().size() <= 0)return;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");
		
//        HSS
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		
		
		Decimalbox dbox = new Decimalbox();
    	dbox.setFormat("#,###,##");
    	
    	HSSFRow row = sheet.createRow((short)1);
    	
    	int i = 1;
		for (Object head : rekapList.getHeads()) {
	          String h = "";
	          int j = 0;
	          row = sheet.createRow((short)i++);
	          for (Object header : ((Listhead) head).getChildren()) {
	            h = ((Listheader) header).getLabel();
	            row.createCell(j).setCellValue(h);
	            row.getCell(j).setCellStyle(style);
	            j++;
	          }
		}
		
		//title
		row = sheet.createRow((short)0);
		HSSFCell mycell = row.createCell(0);
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":O$"+1+""));
		mycell.setCellValue("LAPORAN REKAP KASIR PENDAPATAN "+pendapatanList.getSelectedItem().getLabel()+" TIPE PASIEN "+ tipeList.getSelectedItem().getLabel() 
				+" "+ sdf2.format(dateFrom.getValue()) + " - " + sdf2.format(dateTo.getValue()));
		mycell.setCellStyle(style);

//		row.createCell((short)0).setCellValue("LAPORAN REKAP KASIR PENDAPATAN "+pendapatanList.getSelectedItem().getLabel()+" TIPE PASIEN "+ tipeList.getSelectedItem().getLabel() 
		//		+" "+ sdf2.format(dateFrom.getValue()) + " - " + sdf2.format(dateTo.getValue()));
	//	row.getCell((short)0).setCellStyle(style);
		
		
		for (Object item : rekapList.getItems()) {
	          String s = "";
	          int j = 0;
	          row = sheet.createRow((short)i++);
	          for (Object cell : ((Listitem) item).getChildren()) {
	            s = ((Listcell) cell).getLabel();
	            if(j==9 || j==10 || j==11 ||j==12 )
	            	{
	            	dbox.setText(s);
	            	row.createCell(j++).setCellValue(dbox.getValue().doubleValue());
	            	}
	            else
	            	row.createCell(j++).setCellValue(s);
	            
	          }  
	    }
		
		System.out.println("banyak kolom header " +rekapList.getHeads().size());
		
		
		for(int a=0; a < 16;a++){
			sheet.autoSizeColumn(a);
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

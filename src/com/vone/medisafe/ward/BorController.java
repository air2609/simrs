package com.vone.medisafe.ward;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class BorController extends BaseController{
	Datebox dateStart;
	Datebox endDate;
	Listbox borList;
	Label hospitalName;
	
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
	// TODO Auto-generated method stub
		super.init(cmp);
		
		dateStart = (Datebox)cmp.getFellow("dateStart");
		endDate = (Datebox)cmp.getFellow("endDate");
		borList = (Listbox)cmp.getFellow("borList");
		hospitalName = (Label)cmp.getFellow("hospitalName");
		
		dateStart.setValue(new Date());
		endDate.setValue(new Date());
		
		hospitalName.setValue(MessagesService.getKey("hospital.name.short").toUpperCase());
		
		MasterServiceLocator.getBedManager().getBorReport(dateStart, endDate, borList);
	}
	
	public void getReport() throws VONEAppException{
		MasterServiceLocator.getBedManager().getBorReport(dateStart, endDate, borList);
	}
	
	public void exportToXls() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		HSSFRow row = sheet.createRow((short)1);
		
		int i = 1;
//		for (Object head : borList.getHeads()) {
//	          String h = "";
//	          int j = 0;
//	          row = sheet.createRow((short)i++);
//	          for (Object header : ((Listhead) head).getChildren()) {
//	            h = ((Listheader) header).getLabel();
//	            row.createCell(j).setCellValue(h);
//	            row.getCell(j).setCellStyle(style);
//	            j++;
//	          }
//		}
		
		//title
		row = sheet.createRow((short)0);
		
		HSSFCell mycell = row.createCell(0);
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":E$"+1+""));
		mycell.setCellValue("LAPORAN BOR RS TIARA SELLA PERIODE " + dateStart.getText() + " s.d " + endDate.getText()); 
		mycell.setCellStyle(style);
		
		int index = 0;
		i++;
		
		for (Object item : borList.getItems()) {
			 
			String s = "";
		    int j = 0;
		    row = sheet.createRow((short)i++);
		    for (Object cell : ((Listitem) item).getChildren()) {
		          s = ((Listcell) cell).getLabel();
		          row.createCell(j).setCellValue(s);
		          if(index == 0) row.getCell(j).setCellStyle(style);
		          j++;
		    }
			 
			 index = index + 1;
	            
	    }
		
		for(int a=0; a < 5;a++){
			sheet.autoSizeColumn(a);
		}
		
		try
		{
			String filename = this.getUserInfoBean().getStUserId()+".xls";
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
			FileInputStream in = new FileInputStream(path);
			Filedownload.save(in, "application/vnd.ms-excel", "bor.xls");
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
	}

}

package com.vone.medisafe.report;

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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.service.iface.master.treatment.TreatmentManager;
import com.vone.medisafe.ui.base.BaseController;

public class TreatmentReportController extends BaseController{
	
	
	Datebox dateFrom;
	Datebox dateTo;
	Listbox treatmentList;
	Listbox tipeList;
	
	
	TreatmentManager service = TreatmentService.getTreatmentManager();
	
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		dateFrom = (Datebox)cmp.getFellow("dateFrom");
		dateTo = (Datebox)cmp.getFellow("dateTo");
		treatmentList = (Listbox)cmp.getFellow("treatmentList");
		tipeList = (Listbox)cmp.getFellow("tipeList");
	}
	
	public void createReport() throws VONEAppException{
		service.getTreatmentReport(dateFrom, dateTo, tipeList, treatmentList);
	}
	
	
	public void export(){
		
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
		for (Object head : treatmentList.getHeads()) {
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
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":D$"+1+""));
		mycell.setCellValue("LAPORAN REKAP TINDAKAN PERIODE  " 
				+" "+ sdf2.format(dateFrom.getValue()) + " - " + sdf2.format(dateTo.getValue()) +" TIPE PASIEN "+tipeList.getSelectedItem().getLabel());
		mycell.setCellStyle(style);

		
		
		for (Object item : treatmentList.getItems()) {
	          String s = "";
	          int j = 0;
	          row = sheet.createRow((short)i++);
	          for (Object cell : ((Listitem) item).getChildren()) {
	            s = ((Listcell) cell).getLabel();
	            if(j==2 || j==3 || j==4 ||j==5 || j==6 )
	            	{
	            	dbox.setText(s);
	            	row.createCell(j++).setCellValue(dbox.getValue().doubleValue());
	            	}
	            else
	            	row.createCell(j++).setCellValue(s);
	            
	          }  
	    }
		
		
		
		for(int a=0; a < 4; a++){
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

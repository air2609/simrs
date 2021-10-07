package com.vone.medisafe.report;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.ui.base.BaseController;

/*
 * Created by aziiz 
 * on 30-03-2010
 */
public class RekapObat extends BaseController {

	Datebox dateFrom;
	Datebox dateTo;
	Caption captionId;
	Listbox obatList;
	Listhead headObat;
	Decimalbox nilaiPenjualan;
	Listbox laporan;
	
	NoteManager service = Service.getNotaManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		captionId = (Caption)cmp.getFellow("captionId");
		dateFrom = (Datebox) cmp.getFellow("dateFrom");
		dateTo = (Datebox)cmp.getFellow("dateTo");
		headObat = (Listhead) cmp.getFellow("obatList").getFellow("headObat");
		nilaiPenjualan = (Decimalbox)cmp.getFellow("nilaiPenjualan");
		obatList = (Listbox)cmp.getFellow("obatList");
		laporan = null;
	}
	
	public void openClick() throws InterruptedException {
//		check if date already filled..if no stop process and give a message warning
		if(dateFrom.getValue() == null || dateTo.getValue() == null){
			
			Messagebox.show("Kedua tanggal harus diisi....!");
			return;
		}
		
		try
		{
		laporan = this.obatList;
		service.getRekapObat(dateFrom.getValue(), dateTo.getValue(), laporan, nilaiPenjualan);
		}
		catch(VONEAppException e)
		{
			e.printStackTrace();
		}
		
		
		
	}
	
public void saveToXls() throws InterruptedException {
		
		if(laporan.getItems().size() <= 0)return;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		//title
		HSSFRow row = sheet.createRow((short)0);
		row.createCell((short)0).setCellValue("REKAP OBAT " + sdf2.format(dateFrom.getValue()) +
				" - " + sdf2.format(dateTo.getValue()));
		row.getCell((short)0).setCellStyle(style);
		
		row = sheet.createRow((short)1);
		
		int i = 1;
		for (Object head : laporan.getHeads()) {
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
		
		/*
		 *  Columns:
		 *  0 => no urut (String)
		 *  1 => nama obat (String)
		 *  2 => qty (Integer)
		 *  3 => total penjualan (Double)
		 */
		for (Object item : laporan.getItems()) {
	          String s = "";
	          int j = 0;
	          row = sheet.createRow((short)i++);
	          for (Object cell : ((Listitem) item).getChildren()) {
	            s = ((Listcell) cell).getLabel();
	            if(j==0)
	            	row.createCell((short)j++).setCellValue(new Integer(s));
	            else if(j==2 || j==3)
	            {
	            	Decimalbox dbox = new Decimalbox();
	            	dbox.setFormat("#,###.##");
	            	dbox.setText(s);
	            	row.createCell((short)j++).setCellValue(dbox.getValue().doubleValue());
	            }
	            else
	            	row.createCell((short)j++).setCellValue(s);
	            
	          }  
	    }
//		sum
		int idx = 2 + laporan.getItemCount();
		row = sheet.createRow((short)i++);
		row.createCell((short)3).setCellFormula("SUM(D3:D"+idx+")");
		
		try
		{
			String filename = this.getUserInfoBean().getStUserId()+".xls";
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"\\"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
		}
		catch(Exception ioe)
		{
			ioe.printStackTrace();
		}
		
		
	}

}

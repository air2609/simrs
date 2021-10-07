package com.vone.medisafe.report;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.ui.base.BaseController;

import org.zkoss.zul.Filedownload; 
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.text.SimpleDateFormat;
/*
 *  Created by Aziiz Surahman
 *  30-03-2010
 */
public class LaporanRawatInapJalan extends BaseController {
	
	Listbox rawatInapList;
	Listbox rawatJalanList;
	Datebox dateFrom;
	Datebox dateTo;
	Listhead headRawatInap;
	Listhead headRawatJalan;
	Decimalbox nilaiTransaksi;
	Listbox rawatList;
	Caption captionId;
	Listbox laporan;
	
	NoteManager service = Service.getNotaManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		rawatInapList = (Listbox)cmp.getFellow("rawatInapList");
		rawatJalanList = (Listbox)cmp.getFellow("rawatJalanList");
		dateFrom = (Datebox) cmp.getFellow("dateFrom");
		dateTo = (Datebox)cmp.getFellow("dateTo");
		headRawatInap = (Listhead) cmp.getFellow("rawatInapList").getFellow("headRawatInap");
		headRawatJalan = (Listhead)cmp.getFellow("rawatJalanList").getFellow("headRawatJalan");
		nilaiTransaksi = (Decimalbox)cmp.getFellow("nilaiTransaksi");
		rawatList = (Listbox)cmp.getFellow("rawatList");
		captionId = (Caption)cmp.getFellow("captionId");
		laporan = null;
		
	}
	
	public void selectview(){
		if(rawatList.getSelectedItem().getValue().equals("RI"))
		{
			rawatInapList.setVisible(true);
			rawatJalanList.setVisible(false);
			headRawatInap.setVisible(true);
			headRawatJalan.setVisible(false);
			captionId.setLabel("LAPORAN RAWAT INAP");
		}
		else
		{
			rawatJalanList.setVisible(true);
			rawatInapList.setVisible(false);
			headRawatJalan.setVisible(true);
			headRawatInap.setVisible(false);
			captionId.setLabel("LAPORAN RAWAT JALAN");
		}
	}
	
	public void openClick() throws InterruptedException {
//		check if date already filled..if no stop process and give a message warning
		if(dateFrom.getValue() == null || dateTo.getValue() == null){
			
			Messagebox.show("Kedua tanggal harus diisi....!");
			return;
		}
		
		//Listbox laporan = null;
		String tipeLaporan = this.rawatList.getSelectedItem().getValue().toString();
		if(tipeLaporan.equalsIgnoreCase("RI")) laporan = this.rawatInapList;
		else laporan = this.rawatJalanList;
		
		try
		{
			service.getRawatInapJalan(dateFrom.getValue(), dateTo.getValue(), tipeLaporan, laporan, nilaiTransaksi);
		}
		catch(VONEAppException e)
		{
			//e.printStackTrace();
			Messagebox.show(e.getMessage());
		}
	}
	
	public void saveToXls(Listbox list) throws InterruptedException {
		
		//if(laporan.getItems().size() <= 0)return;
		
		String tipeLaporan = list.getSelectedItem().getValue().toString();
      	if(tipeLaporan.equalsIgnoreCase("RI")) laporan = rawatInapList;
		else laporan = rawatJalanList;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		
		HSSFRow row = sheet.createRow((short)1);
		
		int i = 1;
		for (Object head : laporan.getHeads()) {
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
		if(tipeLaporan.equalsIgnoreCase("RI"))
		{
			//title
			row = sheet.createRow((short)0);
			
			HSSFCell mycell = row.createCell(0);
			sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":J$"+1+""));
			mycell.setCellValue("LAPORAN RAWAT INAP PERIODE " 
					+" "+ sdf2.format(dateFrom.getValue()) + " - " + sdf2.format(dateTo.getValue()));
			mycell.setCellStyle(style);
			
			/*
			 *  Columns:
			 *  0 => no mr (String)
			 *  1 => nama pasien (String)
			 *  2 => dokter utama (String)
			 *  3 => bed (String)
			 *  4 => kelas tarif (String)
			 *  5 => tgl masuk (String)
			 *  6 => tgl keluar (String)
			 *  7 => nilai transaksi (Double)
			 */
			
			for (Object item : laporan.getItems()) {
		          String s = "";
		          int j = 0;
		          row = sheet.createRow((short)i++);
		          for (Object cell : ((Listitem) item).getChildren()) {
		            s = ((Listcell) cell).getLabel();
		            row.createCell(j++).setCellValue(s);
		          }  
		    }
			
			for(int a=0; a < 14;a++){
				sheet.autoSizeColumn(a);
			}
		
			
			
		}
		else
		{
//			title
			row = sheet.createRow((short)0);
			HSSFCell mycell = row.createCell(0);
			sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":G$"+1+""));
			mycell.setCellValue("LAPORAN RAWAT JALAN PERIODE " 
					+" "+ sdf2.format(dateFrom.getValue()) + " - " + sdf2.format(dateTo.getValue()));
			mycell.setCellStyle(style);
			/*
			 *  Columns:
			 *  0 => no mr (String)
			 *  1 => nama pasien (String)
			 *  2 => tgl daftar (String)
			 *  3 => nilai transaksi (Double)
			 */
			
			for (Object item : laporan.getItems()) {
		          String s = "";
		          int j = 0;
		          row = sheet.createRow((short)i++);
		          for (Object cell : ((Listitem) item).getChildren()) {
		            s = ((Listcell) cell).getLabel();
		            row.createCell(j++).setCellValue(s);
		            
		          }  
		    }
			
			for(int a=0; a < 12;a++){
				sheet.autoSizeColumn(a);
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

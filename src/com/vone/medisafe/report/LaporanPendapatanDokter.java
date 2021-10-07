package com.vone.medisafe.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.DoctorController;

public class LaporanPendapatanDokter extends BaseController{
	
	Bandbox dokterId;
	Textbox doctorCode;
	Textbox doctorName;
	Listbox doctorList;
	Listbox pendapatanList;
	Datebox dateFrom;
	Datebox dateTo;
	Listbox pendapatanDokterList;
	Listbox penjualanList;
	Decimalbox totalPendapatan;
	Caption captionId;
	Listhead headPendapatan;
	Listhead headPenjualan;
	Listhead headReportAll;
	Listbox laporan;
	Listbox reportAll;
	Label lblTotal;
	Listbox patientTypeList;
	
	NoteManager service = Service.getNotaManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		dokterId = (Bandbox)cmp.getFellow("dokterId");
		doctorCode = (Textbox) cmp.getFellow("doctorCode");
		doctorName = (Textbox)cmp.getFellow("doctorName");
		doctorList = (Listbox)cmp.getFellow("doctorList");
		pendapatanList = (Listbox)cmp.getFellow("pendapatanList");
		dateFrom = (Datebox) cmp.getFellow("dateFrom");
		dateTo = (Datebox)cmp.getFellow("dateTo");
		pendapatanDokterList = (Listbox)cmp.getFellow("pendapatanDokterList");
		penjualanList = (Listbox)cmp.getFellow("penjualanList");
		reportAll = (Listbox)cmp.getFellow("reportAll");
		headReportAll = (Listhead) cmp.getFellow("headReportAll");
		totalPendapatan = (Decimalbox)cmp.getFellow("totalPendapatan");
		captionId = (Caption)cmp.getFellow("captionId");
		headPendapatan = (Listhead) cmp.getFellow("pendapatanDokterList").getFellow("headPendapatan");
		headPenjualan = (Listhead)cmp.getFellow("penjualanList").getFellow("headPenjualan");
		laporan = null;
		penjualanList.setVisible(false);
		lblTotal = (Label)cmp.getFellow("lblTotal");
		patientTypeList = (Listbox)cmp.getFellow("patientTypeList");
		
	}
	
	public void getDoctor(){
		
		MsStaff staff = (MsStaff)doctorList.getSelectedItem().getValue();
		dokterId.setValue(staff.getVStaffCode()+"-"+staff.getVStaffName());
	}
	
	public void selectview(){
		
		lblTotal.setVisible(true);
		totalPendapatan.setVisible(true);
		
		if(pendapatanList.getSelectedItem().getValue().equals("PD")){
			pendapatanDokterList.setVisible(true);
			penjualanList.setVisible(false);
			reportAll.setVisible(false);
			headReportAll.setVisible(false);
			headPenjualan.setVisible(false);
			headPendapatan.setVisible(true);
		}else if(pendapatanList.getSelectedItem().getValue().equals("OBAT")){
			pendapatanDokterList.setVisible(false);
			penjualanList.setVisible(true);
			reportAll.setVisible(false);
			headReportAll.setVisible(false);
			headPenjualan.setVisible(true);
			headPendapatan.setVisible(false);
		}
		else{
			reportAll.setVisible(true);
			headReportAll.setVisible(true);
			pendapatanDokterList.setVisible(false);
			headPendapatan.setVisible(false);
			penjualanList.setVisible(false);
			headPenjualan.setVisible(false);
			lblTotal.setVisible(false);
			totalPendapatan.setVisible(false);
		}
		
		totalPendapatan.setValue(null);
	}
	
	public void openClick() throws InterruptedException,VONEAppException{
		
		//check if date already filled..if no stop process and give a message warning
		if(dateFrom.getValue() == null || dateTo.getValue() == null){
			
			Messagebox.show("Kedua tanggal harus diisi....!");
			return;
		}
		
		

		MsStaff staff = null;
		String tipeLaporan = pendapatanList.getSelectedItem().getValue().toString();
		if(!tipeLaporan.equalsIgnoreCase("ALL")){
			if(dokterId.getValue().equalsIgnoreCase("")){
				Messagebox.show("Pilih data dokter terlebih dahulu");
				return;
			}else
				staff = (MsStaff)doctorList.getSelectedItem().getValue();
		}
		
		Listbox laporan = null;
		
		String patientType = (String)this.patientTypeList.getSelectedItem().getValue();
		
		if(tipeLaporan.equalsIgnoreCase("PD")) laporan = this.pendapatanDokterList;
		else if(tipeLaporan.equalsIgnoreCase("OBAT"))laporan = this.penjualanList;
		else laporan = this.reportAll;
		if(!tipeLaporan.equalsIgnoreCase("ALL"))
			service.getPendapatanDokter(staff, tipeLaporan, dateFrom.getValue(), dateTo.getValue(),laporan, totalPendapatan, patientType);
		else{
			doctorCode.setText("%%");
			doctorName.setText("%%");
			dokterId.setValue(null);
			DoctorController.searchDoctor(doctorCode, doctorName, doctorList, MedisafeConstants.GRUP_DOKTER);
			service.getDoctorReportAll(doctorList, dateFrom.getValue(),dateTo.getValue(),laporan,totalPendapatan, patientType);
		}
		
		
	}
	
	public void saveToXLS(Listbox list){

		
		//if(laporan.getItems().size() <= 0)return;
		
		String tipeLaporan = list.getSelectedItem().getValue().toString();
      	if(tipeLaporan.equalsIgnoreCase("PD")) laporan = this.pendapatanDokterList;
      	else if(tipeLaporan.equalsIgnoreCase("ALL")) laporan = this.reportAll;
		else laporan = this.penjualanList;
		
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
		if(tipeLaporan.equalsIgnoreCase("PD"))
		{
			//title
			row = sheet.createRow((short)0);
			row.createCell((short)0).setCellValue("LAPORAN PENDAPATAN "+dokterId.getValue()+" " + sdf2.format(dateFrom.getValue()) +
					" - " + sdf2.format(dateTo.getValue()) + " TIPE PASIEN : "+patientTypeList.getSelectedItem().getLabel());
			row.getCell((short)0).setCellStyle(style);
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
		            if(j==8)
		            	{
		            	dbox.setText(s);
		            	row.createCell((short)j++).setCellValue(dbox.getValue().doubleValue());
		            	}
		            else
		            	row.createCell((short)j++).setCellValue(s);
		            
		          }  
		    }
		
			//sum
			int idx = 2 + laporan.getItemCount();
			row = sheet.createRow((short)i++);
			row.createCell((short)8).setCellFormula("SUM(I3:H"+idx+")");
			
			
		}
		else if(tipeLaporan.equalsIgnoreCase("ALL")){
			
			//title
			row = sheet.createRow((short)0);
			row.createCell((short)0).setCellValue("LAPORAN PENDAPATAN DOKTER ALL "+ sdf2.format(dateFrom.getValue())+ 
					" - " + sdf2.format(dateTo.getValue()) +" TIPE PASIEN : "+patientTypeList.getSelectedItem().getLabel());
			row.getCell((short)0).setCellStyle(style);
						
			/**
			 *  columns :
			 *  0 => doctor name
			 *  1 => doctor earning
			 *  2 => doctor drug's selling
			 */
			
			List<Listitem> items = laporan.getItems();
			
			for(Listitem item : items){
				List<Listcell> cells = item.getChildren();
				row = sheet.createRow((short)i++);
				int j = 0;
				for(Listcell cell : cells){
					if(j == 1 || j == 2){
						dbox.setText(cell.getLabel());
						row.createCell((short)j++).setCellValue(dbox.getValue().doubleValue());
					}else{
						row.createCell((short)j++).setCellValue(cell.getLabel());
					}
				}
			}
		}
		else
		{
//			title
			row = sheet.createRow((short)0);
			row.createCell((short)0).setCellValue("LAPORAN SUMBANGSIH PENJUALAN OBAT "+ dokterId.getValue()+" "+ sdf2.format(dateFrom.getValue()) +
					" - " + sdf2.format(dateTo.getValue()) + " TIPE PASIEN : "+patientTypeList.getSelectedItem().getLabel());
			row.getCell((short)0).setCellStyle(style);
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
		            if(j==3)
		            {
		            	dbox.setText(s);
		            	row.createCell((short)j++).setCellValue(dbox.getValue().doubleValue());
		            }
		            else
		            	row.createCell((short)j++).setCellValue(s);
		            
		          }  
		    }
			
//			sum
			int idx = 2 + laporan.getItemCount();
			row = sheet.createRow((short)i++);
			row.createCell((short)3).setCellFormula("SUM(D3:D"+idx+")");
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

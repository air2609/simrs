package com.vone.medisafe.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admission.RajalManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.ui.base.BaseController;

public class LaporanPendaftaran extends BaseController{
	Datebox dfrom;
	Datebox dto;
	Listbox registrationList;
	Listbox unitList;
	
	UnitManager unitServices = MasterServiceLocator.getUnitManager();
	RajalManager service = AdmissionServiceLocator.getRajalManager();
	
	public void bind(Component cmp) throws InterruptedException, VONEAppException{
		
		super.init(cmp);
		
		dfrom = (Datebox)cmp.getFellow("dfrom");
		dto = (Datebox)cmp.getFellow("dto");
		registrationList = (Listbox)cmp.getFellow("registrationList");
		unitList = (Listbox)cmp.getFellow("unitList");
		
		unitServices.getRegistrationUnit(unitList);
		
	}
	
	public void getLaporanPendaftaran() throws WrongValueException, VONEAppException{
		if(unitList.getSelectedItem().getValue().toString().equalsIgnoreCase(MedisafeConstants.LISTKOSONG))
			service.getRegistrationReport(dfrom.getValue(), dto.getValue(), 0, registrationList);
		else{
			MsUnit unit = (MsUnit) unitList.getSelectedItem().getValue();
			service.getRegistrationReport(dfrom.getValue(), dto.getValue(), unit.getNUnitId(), registrationList);
		}
	}
	
	public void cetak() throws Exception{
		String filename = "rekapPendaftaran.pdf";
		String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/"+ filename;
		createRekap(path);
		
		InputStream is = new FileInputStream(path);
		
		AMedia media = new AMedia("laporanPendaftaran", "pdf", "application/pdf", is);
		final Window win = (Window) Executions.createComponents(
				"/zkpages/laboratory/preview.zul", null, null);
		
		Iframe myIframe = (Iframe)win.getFellow("myIframe");
		myIframe.setContent(media);
		win.doModal();
		
		/**
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
		for (Object head : registrationList.getHeads()) {
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
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":G$"+1+""));
		mycell.setCellValue("LAPORAN PENDAFTARAN PERIODE " 
				+" "+ sdf2.format(dfrom.getValue()) + " - " + sdf2.format(dto.getValue()));
		mycell.setCellStyle(style);

		
		
		for (Object item : registrationList.getItems()) {
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
		
		
		
		for(int a=0; a < 7;a++){
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
		
	*/
	}
	
	private void createRekap(String path) throws Exception{
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path));
		document.open();
		
		PdfPTable header = createHeader();
		document.add(header);
		
		Paragraph par = new Paragraph();
		document.add(par);
		
		par = new Paragraph();
		addEmptyLine(par, 2);
		document.add(par);
		
		PdfPTable laproan = createLaporan();
		document.add(laproan);
		
		document.close();
		
	}
	
	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private PdfPTable createLaporan() throws Exception {
		PdfPTable table = new PdfPTable(5);
		float[] width = new float[]{1f,2f,1f,1f,1f};
		table.setWidthPercentage(90);
		table.setWidths(width);
		createColumnHeader(table);
		
		
		
		List<Listitem> items = this.registrationList.getItems();
		PdfPCell cell = null;
		Font font10n = new Font(Font.TIMES_ROMAN,10, Font.NORMAL);
		for(Listitem item : items){
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(0)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(1)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(2)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(3)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(4)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
		}
		return table;
	}

	private void createColumnHeader(PdfPTable table) {
		Font font10b = new Font(Font.TIMES_ROMAN,10, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase("TANGGAL",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("UNIT",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("LAKI-LAKI",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("PEREMPUAN",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("TOTAL",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
	}

	private PdfPTable createHeader() {
		PdfPTable table = new PdfPTable(1);
//		float[] width = new float[]{4f,2f};
		table.setWidthPercentage(100);
		Font font12b = new Font(Font.TIMES_ROMAN,12, Font.BOLD);
		Font font10b = new Font(Font.TIMES_ROMAN,10, Font.BOLD);
		
		Phrase headPh = new Phrase("LAPORAN PENDAFTARAN",font12b);
		PdfPCell cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		headPh = new Phrase("PERIODE "+this.dfrom.getText()+ " SD "+this.dto.getText(),font10b);
		cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		return table;
	}

}

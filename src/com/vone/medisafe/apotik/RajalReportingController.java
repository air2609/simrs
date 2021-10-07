package com.vone.medisafe.apotik;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;






import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class RajalReportingController extends BaseController{

	public Listbox locationList;
	public Listbox shiftList;
	public Datebox startDate;
	public Datebox endDate;
	public Radiogroup reportOption;
	public Caption lapCap;
	public Listbox ranapList;
	public Listbox rajalList;
	
	Window win;
	
	
	UserManager userService = ServiceLocator.getUserManager();
	
	@Override
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		locationList = (Listbox)win.getFellow("locationList");
		shiftList = (Listbox)win.getFellow("shiftList");
		startDate = (Datebox)win.getFellow("startDate");
		endDate = (Datebox)win.getFellow("endDate");
		reportOption = (Radiogroup)win.getFellow("reportOption");
		lapCap = (Caption)win.getFellow("lapCap");
		ranapList = (Listbox)win.getFellow("ranapList");
		rajalList = (Listbox)win.getFellow("rajalList");
		
		this.win = (Window)win;
		
		Date tanggal = new Date();
		startDate.setValue(tanggal);
		endDate.setValue(tanggal);
		
		userService.getUnitUser(getUserInfoBean().getMsUser(), locationList);
	}
	
	public void downloadXls(){
		if(reportOption.getSelectedItem().getLabel().equals("RAWAT INAP")){
			downloadRanapRepport();
			return;
		}
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String shift = shiftList.getSelectedItem().getLabel();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		//SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		
		HSSFRow row = sheet.createRow((short)1);
		
		Decimalbox dbox = new Decimalbox();
    	dbox.setFormat("#,###,##");
    	
    	int i = 5;
		for (Object head : rajalList.getHeads()) {
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
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("LAPORAN PENJUALAN");
		row.createCell((short)2).setCellValue(reportOption.getSelectedItem().getLabel());
		
		row = sheet.createRow(1);
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("NAMA UNIT ");
		row.createCell((short)2).setCellValue(unit.getVUnitName());
		
		row = sheet.createRow(2);
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("SHIFT");
		row.createCell((short)1).setCellValue(shift);
		
		row = sheet.createRow(2);
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("PERIODE");
		row.createCell((short)2).setCellValue(startDate.getText()+ " s.d "+ endDate.getText());
		
		
		List<Listitem> items = rajalList.getItems();
		for(Listitem item : items){
			List<Listcell> cells = item.getChildren();
			int j = 0;
			row = sheet.createRow((short)i++);
			for(Listcell cell : cells){
				if(j==3){
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
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
			FileInputStream in = new FileInputStream(path);
			Filedownload.save(in, "application/vnd.ms-excel", "laporan_penjualan.xls");
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	
	private void downloadRanapRepport() {
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String shift = shiftList.getSelectedItem().getLabel();
		
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
    	
    	int i = 5;
		for (Object head : ranapList.getHeads()) {
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
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("LAPORAN PENJUALAN");
		row.createCell((short)2).setCellValue(reportOption.getSelectedItem().getLabel());
		
		row = sheet.createRow(1);
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("NAMA UNIT ");
		row.createCell((short)2).setCellValue(unit.getVUnitName());
		
		row = sheet.createRow(2);
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("SHIFT");
		row.createCell((short)1).setCellValue(shift);
		
		row = sheet.createRow(2);
		row.createCell((short)0).setCellValue("");
		row.createCell((short)1).setCellValue("PERIODE");
		row.createCell((short)2).setCellValue(startDate.getText()+ " s.d "+ endDate.getText());
		
		
		List<Listitem> items = ranapList.getItems();
		for(Listitem item : items){
			List<Listcell> cells = item.getChildren();
			int j = 0;
			row = sheet.createRow((short)i++);
			for(Listcell cell : cells){
				if(j==7){
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
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
			FileInputStream in = new FileInputStream(path);
			Filedownload.save(in, "application/vnd.ms-excel", "laporan_penjualan.xls");
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		
		
	
	}

	public void getType() throws Exception{
		if(reportOption.getSelectedItem().getLabel().equals("RAWAT INAP")){
			lapCap.setLabel("LAPORAN PENJUALAN RAWAT INAP");
			ranapList.setVisible(true);
			rajalList.setVisible(false);
			ranapList.getItems().clear();
		}else{
			lapCap.setLabel("LAPORAN PENJUALAN RAWAT JALAN");
			ranapList.setVisible(false);
			rajalList.setVisible(true);
			rajalList.getItems().clear();
		}
	}
	
	public void showReport() throws Exception{
		if(reportOption.getSelectedItem().getLabel().equals("RAWAT INAP")){
			showRanapRepport();
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String kodeRajal = "J-"+unit.getVUnitCode()+"%";
		String shift = shiftList.getSelectedItem().getValue().toString();
		rajalList.getItems().clear();
		
		String mulai = sdf.format(startDate.getValue());
		mulai = mulai + " 00:00:00";
		
		String sampai = sdf.format(endDate.getValue());
		sampai = sampai + " 23:59:59";
		List rajalList = MasterServiceLocator.getItemManager().getRajalReport(mulai, sampai, kodeRajal, shift);
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		Listcell cell = null;
		
		Iterator it = rajalList.iterator();
		while(it.hasNext()){
			Object[] obj = (Object[])it.next();
			Listitem item = new Listitem();
			item.setParent(this.rajalList);
			//nomor
			Integer i = (Integer)obj[0];
			if(i != null)
				cell = new Listcell(i.toString());
			else cell = new Listcell("");
			cell.setParent(item);
			
			//nota
			String nota = (String)obj[1];
			if(nota != null)
				cell = new Listcell(nota);
			else cell = new Listcell("");
			cell.setParent(item);
			
			//pasien
			String pasien = (String)obj[2];
			cell = new Listcell(pasien);
			cell.setParent(item);
			
			//total
			Double total = (Double)obj[3];
			db.setValue(new BigDecimal(total));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
		}
	}
	
	
	private void showRanapRepport() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String kodeRajal = "I-"+unit.getVUnitCode()+"%";
		String shift = shiftList.getSelectedItem().getValue().toString();
		
		String shiftName = shiftList.getSelectedItem().getLabel();
		
		
		
		
		String mulai = sdf.format(startDate.getValue());
		mulai = mulai + " 00:00:00";
		
		String sampai = sdf.format(endDate.getValue());
		sampai = sampai + " 23:59:59";
		
		List ranaps = MasterServiceLocator.getItemManager().getRanapReport(mulai, sampai, kodeRajal, shift);
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		Listcell cell = null;
		
		Iterator it = ranaps.iterator();
		while(it.hasNext()){
			Object[] obj = (Object[])it.next();
			Listitem item = new Listitem();
			item.setParent(ranapList);
			//nomor
			Integer i = (Integer)obj[0];
			if(i != null)
				cell = new Listcell(i.toString());
			else cell = new Listcell("");
			cell.setParent(item);
			
			//nota
			String nota = (String)obj[1];
			cell = new Listcell(nota);
			cell.setParent(item);
			
			//no registrasi
			String reg = (String)obj[2];
			cell = new Listcell(reg);
			cell.setParent(item);
			
			//pasien
			String pasien = (String)obj[3];
			cell = new Listcell(pasien);
			cell.setParent(item);
			
			//bed
			String bed = (String)obj[4];
			cell = new Listcell(bed);
			cell.setParent(item);
			
			//ruangan
			String ruangan = (String)obj[5];
			cell = new Listcell(ruangan);
			cell.setParent(item);
			
			Integer r = (Integer)obj[6];
			if(i != null)
				cell = new Listcell(r.toString());
			else cell = new Listcell("");
			cell.setParent(item);
			
			//total
			Double total = (Double)obj[7];
			db.setValue(new BigDecimal(total));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
		}
		
	}

	public void createRepport() throws VONEAppException{
		
		if(reportOption.getSelectedItem().getLabel().equals("RAWAT INAP")){
			
			createRanapRepport();
			return;
		}
		
		Map<String, String> parameters = new HashMap<String, String>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String kodeRajal = "J-"+unit.getVUnitCode()+"%";
		String shift = shiftList.getSelectedItem().getValue().toString();
		
		String shiftName = shiftList.getSelectedItem().getLabel();
		
		
		
		
		String mulai = sdf.format(startDate.getValue());
		mulai = mulai + " 00:00:00";
		
		String sampai = sdf.format(endDate.getValue());
		sampai = sampai + " 23:59:59";
		
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * ");
		sql.append(" from ");
		if(shift.equalsIgnoreCase("ALL"))
			sql.append(" report.laporan_penjualan_all");
		else sql.append(" report.laporan_penjualan_rajal");
		sql.append("('"+mulai+"','"+sampai+"','"+kodeRajal+"','"+shift+"')");
		
		
		
		parameters.put("stardDate", sdf2.format(startDate.getValue()));
		parameters.put("endDate", sdf2.format(endDate.getValue()));
		parameters.put("unitName", unit.getVUnitName());
		parameters.put("shiftName", shiftName);
		
		
		
		ReportEngine re = new ReportEngine(sql.toString(), 
				ReportService.getKey("apotik.penjualan.rajal"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
			
	}

	
	
	private void createRanapRepport() throws VONEAppException {
		Map<String, String> parameters = new HashMap<String, String>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String kodeRajal = "I-"+unit.getVUnitCode()+"%";
		String shift = shiftList.getSelectedItem().getValue().toString();
		
		String shiftName = shiftList.getSelectedItem().getLabel();
		
		
		
		
		String mulai = sdf.format(startDate.getValue());
		mulai = mulai + " 00:00:00";
		
		String sampai = sdf.format(endDate.getValue());
		sampai = sampai + " 23:59:59";
		
		
		StringBuffer sql = new StringBuffer();
		/**String query = " select * from report.laporan_Penjualan_Ranap ('"+mulai+"','"+sampai+"','"+kodeRajal+"','"+shift+"')" +
						" order by grup, nomor asc";
		
		*/
		sql.append(" select * ");
		sql.append(" from ");
		if(shift.equalsIgnoreCase("ALL")){
			sql.append(" report.laporan_Penjualan_Ranap_all");
			sql.append("('"+mulai+"','"+sampai+"','"+kodeRajal+"') order by grup, nomor asc");
		}
		else{
			sql.append(" report.laporan_penjualan_Ranap");
			sql.append("('"+mulai+"','"+sampai+"','"+kodeRajal+"','"+shift+"') order by grup, nomor asc");
		}	
		
		//sql.append(" report.laporan_Penjualan_Ranap");
		//sql.append("('"+mulai+"','"+sampai+"','"+kodeRajal+"','"+shift+"') order by grup, nomor asc");
		
		
		
		
		parameters.put("startDate", sdf2.format(startDate.getValue()));
		parameters.put("endDate", sdf2.format(endDate.getValue()));
		parameters.put("unitName", unit.getVUnitName());
		parameters.put("shift", shiftName);
		parameters.put("userName", getUserInfoBean().getStUserName());
		
		ReportEngine re = new ReportEngine(sql.toString(), 
				ReportService.getKey("apotik.penjualan.ranap"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}
	
	
	public void initReturReport(Component win) throws VONEAppException, InterruptedException{
		super.init(win);
		Listbox locationList = (Listbox)win.getFellow("locationList");
		Datebox startDate = (Datebox)win.getFellow("startDate");
		Datebox endDate = (Datebox)win.getFellow("endDate");
		
		UserManager userService = ServiceLocator.getUserManager();
		
		MsUser user = getUserInfoBean().getMsUser();
		if(user == null){
			try {
				Messagebox.show("user null");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		userService.getUnitUser(getUserInfoBean().getMsUser(), locationList);
		startDate.setValue(new Date());
		endDate.setValue(startDate.getValue());
		
	}
	
	
	public void createReturReport(Listbox locationList, Listbox shiftList, 
			Datebox mulai, Datebox sampai, Window win) throws VONEAppException {
		
		Map<String, String> parameters = new HashMap<String, String>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		String startDate = null;
		String endDate = null;
		
		if(mulai.getValue() == null){
			mulai.setValue(new Date());
		}
		
		if(sampai.getValue() == null){
			sampai.setValue(new Date());
		}
		
//		if(shiftList.getSelectedItem().getLabel().equalsIgnoreCase("SHIFT PAGI")){
//			startDate = sdf.format(mulai.getValue()) + MedisafeConstants.SHIFT_PAGI_MULAI;
//			endDate = sdf.format(sampai.getValue()) + MedisafeConstants.SHIFT_PAGI_SELESAI;
//		}
//		else if(shiftList.getSelectedItem().getLabel().equalsIgnoreCase("SHIFT SORE")){
//			startDate = sdf.format(mulai.getValue()) + MedisafeConstants.SHIFT_SORE_MULAI;
//			endDate = sdf.format(sampai.getValue()) + MedisafeConstants.SHIFT_SORE_SELESAI;
//		}
//		else if(shiftList.getSelectedItem().getLabel().equalsIgnoreCase("SHIFT MALAM")){
//			startDate = sdf.format(mulai.getValue()) + MedisafeConstants.SHIFT_MALAM_MULAI;
//			endDate = sdf.format(sampai.getValue()) + MedisafeConstants.SHIFT_MALAM_SELESAI;
//		}
		startDate = sdf.format(mulai.getValue()) + " 00:00:00";
		endDate = sdf.format(sampai.getValue()) + " 23:59:59";
		
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String kodeRajal = "R-"+unit.getVUnitCode()+"%";
//		String shiftName = shiftList.getSelectedItem().getLabel();
		
		
		
		
		
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * ");
		sql.append(" from ");
		sql.append(" report.laporan_retur_obat");
		sql.append("('"+startDate+"','"+endDate+"','"+kodeRajal+"') order by jenis, nomor asc");
		
		System.out.println(sql.toString());
		
		parameters.put("startDate", sdf2.format(mulai.getValue()));
		parameters.put("endDate", sdf2.format(sampai.getValue()));
		parameters.put("unitName", unit.getVUnitName());
		parameters.put("shiftName", "ALL SHIFT");
		parameters.put("userName", getUserInfoBean().getStUserName());
		
		ReportEngine re = new ReportEngine(sql.toString(), 
				ReportService.getKey("apotik.retur.obat"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}
	
	public void createReturReportToXls(Listbox locationList, Listbox shiftList, 
			Datebox mulai, Datebox sampai, Window win) throws VONEAppException {
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		String startDate = null;
		String endDate = null;
		
		if(mulai.getValue() == null){
			mulai.setValue(new Date());
		}
		
		if(sampai.getValue() == null){
			sampai.setValue(new Date());
		}
		

		startDate = sdf.format(mulai.getValue()) + " 00:00:00";
		endDate = sdf.format(sampai.getValue()) + " 23:59:59";
		
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String kodeRajal = "R-"+unit.getVUnitCode()+"%";
		StringBuffer sql = new StringBuffer();
		
		List returs = MasterServiceLocator.getItemManager().getReturReport(startDate, endDate, kodeRajal);
		String[] heads = {"No", "No Retur", "No Nota", "No Resep", "Nama Pasien", "Total", "Total Akhir", "Jenis"};
		
		
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
		for(int i=0; i < heads.length; i++){
			row.createCell(i).setCellValue(heads[i]);
            row.getCell(i).setCellStyle(style);
		}
		
		//title
		row = sheet.createRow((short)0);
		HSSFCell mycell = row.createCell(0);
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":H$"+1+""));
		mycell.setCellValue("LAPORAN RETUR OBAT "+unit.getVUnitName()+" PERIODE "+sdf2.format(mulai.getValue()) 
				+ " - " + sdf2.format(sampai.getValue()));
		mycell.setCellStyle(style);
		
		int i=2;
		
		Iterator it = returs.iterator();
		while(it.hasNext()){
			row = sheet.createRow((short)i);
			Object[] objs = (Object[])it.next();
			for(int j=0; j < objs.length; j++){
				if(j==0){
					if(objs[j] != null)
						row.createCell(j).setCellValue((Integer)objs[j]);
					else row.createCell(j).setCellValue("");
				}
				else if(j==5 || j==6){
					row.createCell(j).setCellValue((Double)objs[j]);
				}else {
					if(objs[j] != null)
						row.createCell(j).setCellValue((String)objs[j]);
					else row.createCell(j).setCellValue("");
				}
			}
			i = i + 1;
		}
		
		for(int a=0; a < heads.length;a++){
			sheet.autoSizeColumn(a);
		}
		
		
		try
		{
			String filename = this.getUserInfoBean().getStUserId()+".xls";
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
			FileInputStream fin = new FileInputStream(path);
			Filedownload.save(fin,"application/vnd.ms-excel", filename);
			
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
	}
	
	

}

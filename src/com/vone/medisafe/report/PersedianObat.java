package com.vone.medisafe.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.ItemInventoryManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class PersedianObat extends BaseController{

	Listbox locationList;
	Window win;
	
	UserManager userService = ServiceLocator.getUserManager();
	ItemInventoryManager inventoryService = Service.getItemInventoryManager();
	
	@Override
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		
		super.init(cmp);
		locationList = (Listbox)cmp.getFellow("locationList");
		
		win = (Window)cmp;
		userService.getUnitUser(getUserInfoBean().getMsUser(), locationList);
	}
	
	public void exportToXls() throws InterruptedException, VONEAppException{
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		if(unit.getMsWarehouse() == null){
			Messagebox.show(unit.getVUnitName()+" TIDAK MEMILIKI GUDANG OBAT-BAHAN MEDIS", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		Integer id = unit.getMsWarehouse().getNWhouseId();
		
		String[] heads = {"No", "Kode Obat", "Nama Obat","Harga", "Jumlah", "Satuan"};
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		
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
    	
    	
    	row = sheet.createRow(4);
    	int j = 0;
		for (String head : heads) {
	          row.createCell(j).setCellValue(head);
	          row.getCell(j).setCellStyle(style);
	          j++;
		}
		
		row = sheet.createRow((short)0);
		HSSFCell mycell = row.createCell(0);
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+1+":F$"+1+""));
		mycell.setCellValue("RUMAH SAKIT TIARA SELLA UNIT "+unit.getVUnitName());
		mycell.setCellStyle(style);
		
		row = sheet.createRow((short)1);
		mycell = row.createCell(0);
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+2+":F$"+2+""));
		mycell.setCellValue("LAPORAN PERSEDIAAN OBAT dan BAHAN MEDIS");
		mycell.setCellStyle(style);
		
		row = sheet.createRow((short)2);
		mycell = row.createCell(0);
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+3+":F$"+3+""));
		mycell.setCellValue("PER "+sdf.format(new Date()));
		mycell.setCellStyle(style);
		
		row = sheet.createRow((short)3);
		mycell = row.createCell(0);
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+4+":F$"+4+""));
		mycell.setCellValue("");
		mycell.setCellStyle(style);
		
		int i = 5;
		
		List<Object[]> rekaps = inventoryService.getLaporanPersediaanObat(id);
		for(Object[] o : rekaps){
			row = sheet.createRow((short)i++);
			Integer nomor = (Integer)o[0];
			row.createCell(0).setCellValue(nomor.doubleValue());
			
			row.createCell(1).setCellValue(o[1].toString());
			row.createCell(2).setCellValue(o[2].toString());
			
			Double harga = (Double)o[3];
			row.createCell(3).setCellValue(harga.doubleValue());
			Double jumlah = (Double)o[4];
			row.createCell(4).setCellValue(jumlah.doubleValue());
			
			row.createCell(5).setCellValue(o[5].toString());
		}
		
		for(int a=0; a<6; a++){
			sheet.autoSizeColumn(a);
		}
		
		
		try
		{
			String filename = this.getUserInfoBean().getStUserId()+".xls";
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"\\"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
			//String filename = getUserInfoBean().getStUserId()+".xls";
			//String path = desktop.getWebApp().getRealPath("tmp") +"\\"+ filename;
						
			FileInputStream in = new FileInputStream(path);
			Filedownload.save(in, "application/vnd.ms-excel", "laporan_persediaan_obat.xls");
			
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	
	public void createReport() throws InterruptedException, VONEAppException {
		
		Map<String, String> parameters = new HashMap<String, String>(); 
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		if(unit.getMsWarehouse() == null){
			Messagebox.show(unit.getVUnitName()+" TIDAK MEMILIKI GUDANG OBAT-BAHAN MEDIS", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		Integer id = unit.getMsWarehouse().getNWhouseId();
		
		String month = new SimpleDateFormat("MM").format(new Date()); 
		month = MedisafeUtil.convertToMonthName(month);
		
		String tahun = new SimpleDateFormat("yyyy").format(new Date());
		
		String sql = "select * from report.laporan_persediaan_obat('"+id+"')";
		
		parameters.put("lokasi", unit.getVUnitName());
		parameters.put("bulan", month+" "+tahun);
		
		
		ReportEngine re = new ReportEngine(sql, 
				ReportService.getKey("laporan.persediaan.obat"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}
}

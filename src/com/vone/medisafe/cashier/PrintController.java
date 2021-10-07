package com.vone.medisafe.cashier;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class PrintController  extends BaseController{
	Bandbox MRNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox MRNumberList;
	
	Textbox regNo;
	Textbox patientName;
	Textbox address;
	Textbox tClass;
	Textbox bed;
	Textbox tipePasien;
	
	Datebox startDate;
	Datebox endDate;
	
	Listbox printList;
	Decimalbox total;
	Decimalbox deposit;
	Decimalbox sisa;
	Button btnItems;
	Button btnUnit;
	Button btnAll;
//	Button btnEnd;
	
	Decimalbox returDecimalbox;

	private CashierManager cashierManager = Service.getCashierManager();
	
	private ZulConstraint constraints = new ZulConstraint();
	private MsUser msUser;
	private TbRegistration tbRegistration;
	private MsPatient msPatient;
	public boolean isRanap;
	public String ranapKelas;
	private Window win;
		
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		MRNumber = (Bandbox)cmp.getFellow("MRNumber");
		crNoMR = (Textbox)cmp.getFellow("crNoMR");
		crNama = (Textbox)cmp.getFellow("crNama");
		crNoAlamat = (Textbox)cmp.getFellow("crNoAlamat");
		MRNumberList = (Listbox)cmp.getFellow("MRNumberList");
		
		regNo = (Textbox)cmp.getFellow("regNo");
		patientName = (Textbox)cmp.getFellow("patientName");
		address = (Textbox)cmp.getFellow("address");
		tClass = (Textbox)cmp.getFellow("tClass");
		bed = (Textbox)cmp.getFellow("bed");
		tipePasien = (Textbox)cmp.getFellow("tipePasien");
		startDate = (Datebox)cmp.getFellow("startDate");
		endDate = (Datebox)cmp.getFellow("endDate");
		printList = (Listbox)cmp.getFellow("printList");
		total = (Decimalbox)cmp.getFellow("total");
		deposit = (Decimalbox)cmp.getFellow("deposit");
		sisa = (Decimalbox)cmp.getFellow("sisa");
		btnItems = (Button)cmp.getFellow("btnItems");
		btnUnit = (Button)cmp.getFellow("btnUnit");
		btnAll = (Button)cmp.getFellow("btnAll");
//		btnEnd = (Button)cmp.getFellow("btnEnd");
		
		returDecimalbox = (Decimalbox)cmp.getFellow("returDecimalbox");
		
		msUser = getUserInfoBean().getMsUser();
		
		printList.getItems().clear();
		
		startDate.setValue(new Date());
		endDate.setValue(new Date());
		
		//uppercase
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		this.win = (Window)cmp;
		msPatient = null;

		
		
//		getRegistration(MedisafeConstants.INPUT_BY_MANUAL);
//		btnItemsClick();
//		btnUnitClick();
//		btnAllClick();
//		cariNotaClick();
	}

	public void cariNotaClick()  throws VONEAppException{
		if(getMsPatient() != null){
			cashierManager.cariNotaClick(this);
			MiscTrxController.setFont(printList);
		}else{
			total.setValue(null);
			returDecimalbox.setValue(null);
			sisa.setValue(null);
			deposit.setValue(null);
			printList.getItems().clear();

		}
		
	}
	
	public void downloadXls(){
		
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
    	
    	int i = 4;
		for (Object head : printList.getHeads()) {
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
		row.createCell((short)0).setCellValue("NAMA");
		row.createCell((short)1).setCellValue(patientName.getValue() + " ("+MRNumber.getValue()+")");
		
		row = sheet.createRow(1);
		row.createCell((short)0).setCellValue("NO REGISTRAI");
		row.createCell((short)1).setCellValue(regNo.getValue());
		
		row = sheet.createRow(2);
		row.createCell((short)0).setCellValue("KAMAR");
		row.createCell((short)1).setCellValue(bed.getValue());
		
		
		List<Listitem> items = printList.getItems();
		for(Listitem item : items){
			List<Listcell> cells = item.getChildren();
			int j = 0;
			row = sheet.createRow((short)i++);
			for(Listcell cell : cells){
				if(j==6){
					if(cell.getLabel().equalsIgnoreCase(""))
						dbox.setText("0");
					else dbox.setText(cell.getLabel());
					row.createCell((short)j++).setCellValue(dbox.getValue().doubleValue());
				}
				else
					row.createCell((short)j++).setCellValue(cell.getLabel());
			}
			
		}
		//create total
		row = sheet.createRow(i++);
		row.createCell((short)5).setCellValue("TOTAL");
		dbox.setText(total.getText());
		row.createCell((short)6).setCellValue(dbox.getValue().doubleValue());
		
		row = sheet.createRow(i++);
		row.createCell((short)5).setCellValue("DEPOSIT");
		if(deposit.getText().equalsIgnoreCase(""))dbox.setText("0");
		else dbox.setText(deposit.getText());
		row.createCell((short)6).setCellValue(dbox.getValue().doubleValue());
		
		row = sheet.createRow(i++);
		row.createCell((short)5).setCellValue("RETUR");
		if(returDecimalbox.getText().equalsIgnoreCase(""))dbox.setText("0");
		else dbox.setText(returDecimalbox.getText());
		row.createCell((short)6).setCellValue(dbox.getValue().doubleValue());
		
		row = sheet.createRow(i++);
		row.createCell((short)5).setCellValue("SISA TAGIHAN");
		if(sisa.getText().equalsIgnoreCase(""))dbox.setText("0");
		else dbox.setText(sisa.getText());
		row.createCell((short)6).setCellValue(dbox.getValue().doubleValue());
		
		try
		{
			String filename = this.getUserInfoBean().getStUserId()+".xls";
			String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/"+ filename;
			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
			
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		
		
	}

	public void btnAllClick() throws VONEAppException{
		if(getMsPatient() == null){
			return;
		}

		Map<String, String> parameters; 
		parameters = getParam();

		StringBuffer sbQuery = bikinSql(3);
		
		String dateParam = "DATE RANGE: ";
		parameters.put("dateParam", dateParam );
		
		ReportEngine re = new ReportEngine(sbQuery.toString(), 
				ReportService.getKey("kasir.tagihan_pasien"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}

	private StringBuffer bikinSql(int typeLaporan) {
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append("select * from report.laporan_tagihan_fn(");
		sbQuery.append(msPatient.getNPatientId());
//		sbQuery.append(tbRegistration.getNRegId().intValue());
		sbQuery.append(", '");
		sbQuery.append(PrintClient.getDate(startDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL));
		sbQuery.append("', '");
		sbQuery.append(PrintClient.getDate(endDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL));
		sbQuery.append("', " + typeLaporan + ");");
//		System.out.println(sbQuery.toString());
		return sbQuery;
	}

	public void btnUnitClick() throws VONEAppException{
		if(getMsPatient() == null){
			return;
		}

		cashierManager.btnUnitClick(this);
		
		Map<String, String> parameters; 
		parameters = getParam();

		
		StringBuffer sbQuery = bikinSql(1);
		
		String dateParam = "DATE RANGE: ";
		parameters.put("dateParam", dateParam );
		
		ReportEngine re = new ReportEngine(sbQuery.toString(), 
				ReportService.getKey("kasir.tagihan_pasien"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}

	private Map<String, String> getParam() {
		Map<String, String> parameters;
		parameters = new HashMap<String, String>();
		parameters.put("noRekamMedik",  MRNumber.getValue());
		parameters.put("noRegister",regNo.getValue());
		parameters.put("namaPasien", patientName.getValue());
		parameters.put("alamatPasien", msPatient.getVPatientMainAddr());
		parameters.put("ruangRawat",bed.getValue());
		parameters.put("kelas",tClass.getValue());
		parameters.put("namaPng","");
		parameters.put("alamatPng","");
		parameters.put("jatahKelas","");
		parameters.put("jumlahBiaya",total.getText());
		parameters.put("jumlahRetur",returDecimalbox.getText());
		parameters.put("jumlahTunai",deposit.getText());
		parameters.put("jumlahPiutang","");
		parameters.put("jumlahKekurangan",sisa.getText());
		return parameters;
	}

	public void btnItemsClick() throws VONEAppException, InterruptedException{
		if(getMsPatient() == null){
			return;
		}
		cashierManager.btnItemsClick(this);
		
		Map<String, String> parameters; 
		parameters = getParam();

		
		StringBuffer sbQuery;
		//= bikinSql(2);
		
		sbQuery = new StringBuffer();
		sbQuery.append("select * from report.laporan_tagihan_unit_fn(");
		sbQuery.append(msPatient.getNPatientId());
		sbQuery.append(", '");
		sbQuery.append(PrintClient.getDate(startDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL));
		sbQuery.append("', '");
		sbQuery.append(PrintClient.getDate(endDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL));
		sbQuery.append("');");

		
		String dateParam = "DATE RANGE: ";
		parameters.put("dateParam", dateParam );
		
//		System.out.println(sbQuery.toString());
		
		ReportEngine re = new ReportEngine(sbQuery.toString(), 
				ReportService.getKey("kasir.tagihan_pasien"), parameters);
	
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}

	public void getRegistration(int input_type) throws VONEAppException{
		setMsPatient(null);
		total.setValue(null);
		returDecimalbox.setValue(null);
		sisa.setValue(null);
		deposit.setValue(null);
		cashierManager.getRegistration(this, input_type);
		printList.getItems().clear();
		cariNotaClick();
	}

	/*
	 * 
	 * field getter
	 * 
	 */
	public MsUser getMsUser() {
		return msUser;
	}

	public Textbox getAddress() {
		return address;
	}

	public Textbox getBed() {
		return bed;
	}

	public Button getBtnAll() {
		return btnAll;
	}

/*	public Button getBtnEnd() {
		return btnEnd;
	}*/

	public Button getBtnItems() {
		return btnItems;
	}

	public Button getBtnUnit() {
		return btnUnit;
	}

	public CashierManager getCashierManager() {
		return cashierManager;
	}

	public ZulConstraint getConstraints() {
		return constraints;
	}

	public Textbox getCrNama() {
		return crNama;
	}

	public Textbox getCrNoAlamat() {
		return crNoAlamat;
	}

	public Textbox getCrNoMR() {
		return crNoMR;
	}

	public Decimalbox getDeposit() {
		return deposit;
	}

	public Datebox getEndDate() {
		return endDate;
	}

	public Bandbox getMRNumber() {
		return MRNumber;
	}

	public Listbox getMRNumberList() {
		return MRNumberList;
	}

	public Textbox getPatientName() {
		return patientName;
	}

	public Listbox getPrintList() {
		return printList;
	}

	public Textbox getRegNo() {
		return regNo;
	}

	public Decimalbox getSisa() {
		return sisa;
	}

	public Datebox getStartDate() {
		return startDate;
	}

	public Textbox getTClass() {
		return tClass;
	}

	public Decimalbox getTotal() {
		return total;
	}

	public TbRegistration getTbRegistration() {
		return tbRegistration;
	}

	public void setTbRegistration(TbRegistration tbRegistration) {
		this.tbRegistration = tbRegistration;
	}

	public MsPatient getMsPatient() {
		return msPatient;
	}

	public void setMsPatient(MsPatient msPatient) {
		this.msPatient = msPatient;
	}

	public Decimalbox getReturDecimalbox() {
		return returDecimalbox;
	}

}

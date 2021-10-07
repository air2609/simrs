package com.vone.medisafe.ui.purchasing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.TbPurchaseRequest;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.PurchaseServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class PORApproval extends BaseController{
	
	Logger logger = Logger.getLogger(PORApproval.class);

	Listbox list = null;
	Button btnApprove = null;
		
	Bandbox oppNo = null;
	Textbox issuedBy = null;
	Textbox approvedBy = null;
	Button btnSearch = null;
	
	Listbox listOpp = null;
	Textbox oppNoSearch = null;
	
	Label status = null;
	
	Textbox supCode;
	Textbox supAddress;
	Textbox supTelp;
	
	Window win;
	
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}
	
	public void print() throws Exception{
		String filename = "opp.pdf";
		String path = this.getCurrentSession().getWebApp().getRealPath("tmp") +"/"+ filename;
		createOppPdf(path);
		
		InputStream is = new FileInputStream(path);
		
		AMedia media = new AMedia("opp", "pdf", "application/pdf", is);
		final Window win = (Window) Executions.createComponents(
				"/zkpages/laboratory/preview.zul", null, null);
		
		Iframe myIframe = (Iframe)win.getFellow("myIframe");
		myIframe.setContent(media);
		win.doModal();
		/*
		Map<String, String> parameters = new HashMap<String, String>(); 
		parameters.put("oppNo", oppNo.getText());
		parameters.put("supplierName", supCode.getText());
		
		String query = "select item.v_item_name as namaobat, "
			         + "d.n_pr_det_qty_requested as jmlpemesanan, " 
			         + "sat.v_mitem_early_quantify as satuan " 
			         + "from tb_purchase_request_detail d, "
			         + "tb_purchase_request req, "
			         + "ms_item item, "
			         + "ms_item_measurement sat "
			         + "where " 
			         + "req.n_pr_id=d.n_pr_id "
			         + "and item.n_item_id=d.n_item_id "
			         + "and sat.n_mitem_id=d.n_mitem_id "
			         + "and req.v_pr_code='"+oppNo.getText()+"'";
		
		ReportEngine re = new ReportEngine(query, 
				ReportService.getKey("cetak.opp"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		*/
	}

	private void createOppPdf(String path) throws Exception{
		Font font12b = new Font(Font.TIMES_ROMAN,12);
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path));
		document.open();
		
		PdfPTable header = createHeader();
		document.add(header);
		
		Paragraph par = new Paragraph();
		addEmptyLine(par, 3);
		document.add(par);
		
		PdfPTable titleTable = createTitle();
		document.add(titleTable);
		
		par = new Paragraph();
		addEmptyLine(par, 2);
		document.add(par);
		
		par = new Paragraph("Yang bertanda tangan dibawah ini :",font12b);
		addEmptyLine(par, 2);
		document.add(par);
		
		PdfPTable requestor = createRequestor();
		document.add(requestor);
		
		par = new Paragraph();
		addEmptyLine(par, 1);
		document.add(par);
		
		par = new Paragraph("Mengajukan permohonan kepada :",font12b);
		addEmptyLine(par, 1);
		document.add(par);
		
		
		
		PdfPTable supplier = getSupplier();
		document.add(supplier);
//		par = new Paragraph("Nama Perusahaan : "+sup[1], font12b);
		par = new Paragraph();
		addEmptyLine(par, 2);
		document.add(par);
		
		PdfPTable contentOpp = getOppData();
		document.add(contentOpp);
		
		par = new Paragraph();
		addEmptyLine(par, 1);
		document.add(par);
		
		par = new Paragraph("Untuk keperluan Instalasi Farmasi "+MessagesService.getKey("hospital.name.short") +" dengan alamat " +
							MessagesService.getKey("hospital.address2") +" " +MessagesService.getKey("hospital.city")+".",font12b);
		addEmptyLine(par, 2);
		document.add(par);
		
		PdfPTable dateTbl = createDate();
		document.add(dateTbl);
		
		par = new Paragraph();
		addEmptyLine(par, 1);
		document.add(par);
		
		document.add(createMengetahui());
		
		par = new Paragraph();
		addEmptyLine(par, 4);
		document.add(par);
		
		document.add(createSignature());
		
		document.close();
		
	}
	
	

	private PdfPTable getSupplier() throws Exception{
		PdfPTable table = new PdfPTable(2);
		Font font12b = new Font(Font.TIMES_ROMAN,12);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{1.5f,5.5f});
		
		Phrase ph = new Phrase("Nama Perusahaan",font12b);
		PdfPCell cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		String[] sup = supCode.getText().split("-");
		
		ph = new Phrase(": "+sup[1],font12b);
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		ph = new Phrase("Alamat",font12b);
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		ph = new Phrase(": "+this.supAddress.getText(),font12b); 
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		ph = new Phrase("No. Telp",font12b);
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		ph = new Phrase(": "+this.supTelp.getText(),font12b); 
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		return table;
	}

	private PdfPTable createDate() throws Exception{
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{7,2,1});
		
		Font font10b = new Font(Font.TIMES_ROMAN,12);
		PdfPCell cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.city")+",",font10b));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("",font10b));
		cell.setBorder(PdfPCell.BOTTOM);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("20",font10b));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		return table;
	}
	
	private PdfPTable createMengetahui() throws Exception{
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{1,1,1,1});
		
		Font font10b = new Font(Font.TIMES_ROMAN,8);
		PdfPCell cell = new PdfPCell(new Phrase("Penanggung Jawab",font10b));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Mengetahui",font10b));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Menyetujui",font10b));
		cell.setColspan(2);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);
		
//		cell = new PdfPCell(new Phrase("Menyetujui:",font10b));
//		cell.setBorder(PdfPCell.NO_BORDER);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table.addCell(cell);
		return table;
	}
	
	private PdfPTable createSignature() throws Exception{
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{1,1,1,1});
		
		Font font10b = new Font(Font.TIMES_ROMAN,8, Font.UNDERLINE);
		Font font10n = new Font(Font.TIMES_ROMAN,8);
		Font fontSIPA = new Font(Font.TIMES_ROMAN,6);
		
		PdfPCell cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.apoteker"),font10b));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.purchasing.admin.name"),font10b));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);
		
//		cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.commissioner.name"),font10b));
		cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.director.name"),font10b));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
//		cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.director.name"),font10b));
		cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);
		
//		cell = new PdfPCell(new Phrase("",font10b));
//		cell.setBorder(PdfPCell.NO_BORDER);
//		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		table.addCell(cell);
//		
//		cell = new PdfPCell(new Phrase("MEGA SURYA FUTRI, S.Farm, Apt",font10b));
		
		cell = new PdfPCell(new Phrase(MessagesService.getKey("sipa.apoteker"),fontSIPA));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.purchasing.admin"),font10n));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.director"),font10n));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
//		cell = new PdfPCell(new Phrase(MessagesService.getKey("hospital.director"),font10n));
		cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);
		

		
//		cell = new PdfPCell(new Phrase("",font10b));
//		cell.setBorder(PdfPCell.NO_BORDER);
//		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		table.addCell(cell);
		                                     
//		cell = new PdfPCell(new Phrase("SIPA 500/1504/SIPA/DPMPTSP/II/2017",fontSIPA));
		return table;
	}

	private PdfPTable getOppData() throws Exception{
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{1,2,6,2,2});
		
		Font font10b = new Font(Font.TIMES_ROMAN,12, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase("No",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Kode Obat",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Nama Obat",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Jumlah \n Pemesanan",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Satuan",font10b));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		List<Listitem> items = this.list.getItems();
		cell = null;
		Font font10n = new Font(Font.TIMES_ROMAN,12, Font.NORMAL);
		int nomor = 1;
		for(Listitem item : items){
			cell = new PdfPCell(new Phrase(nomor+"",font10n));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(0)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(1)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(7)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(((Listcell)item.getChildren().get(6)).getLabel(),font10n));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			nomor = nomor + 1;
		}
		
		
		return table;
	}

	private PdfPTable createRequestor() throws Exception{
		PdfPTable table = new PdfPTable(2);
		Font font12b = new Font(Font.TIMES_ROMAN,12);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{1,6});
		
		Phrase ph = new Phrase("Nama",font12b);
		PdfPCell cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		                   
//		ph = new Phrase(": Mega Surya Futri, S.Farm, Apt",font12b);
		ph = new Phrase(MessagesService.getKey("nama.apoteker"),font12b);
		
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		ph = new Phrase("Alamat",font12b);
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		                   
//		ph = new Phrase(": Jalan Timur Indah 2B Nomor 52 RT 26 RW 3 Kelurahan Timur Indah Kota Bengkulu",font12b);
		ph = new Phrase(MessagesService.getKey("alamat.apoteker"),font12b);
		
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		ph = new Phrase("Jabatan",font12b);
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
//		ph = new Phrase(": Apoteker",font12b);
		ph = new Phrase(MessagesService.getKey("jabatan.apoteker"),font12b);
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		return table;
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private PdfPTable createTitle() {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		Font font12b = new Font(Font.TIMES_ROMAN,12, Font.BOLD | Font.UNDERLINE);
		Font font10 = new Font(Font.TIMES_ROMAN,10);
		
		Phrase ph = new Phrase("SURAT PEMESANAN",font12b);
		PdfPCell cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		ph = new Phrase("No. "+oppNo.getText(),font10);
		cell = new PdfPCell(ph);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		return table;
	}

	private PdfPTable createHeader() throws Exception{
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new int[]{1,2});
		
		
		
		URL  url = PORApproval.class.getResource(MessagesService.getKey("logo.file"));
		
		Image img = Image.getInstance(url.getPath());
		img.setAbsolutePosition(80f, 80f);
		PdfPCell cell = new PdfPCell(img,true);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(getHeaderInformation());
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		return table;
	}
	
	private PdfPTable getHeaderInformation(){
		Font font18b = new Font(Font.TIMES_ROMAN,18, Font.BOLD);
		Font font10b = new Font(Font.TIMES_ROMAN,10, Font.BOLD);
		
		PdfPTable table = new PdfPTable(1);
		Phrase headPh = new Phrase("INSTALASI FARMASI",font18b);
		PdfPCell cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		headPh = new Phrase(MessagesService.getKey("hospital.name"),font18b);
		cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		headPh = new Phrase(MessagesService.getKey("hospital.address"),font10b);
		cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		headPh = new Phrase(MessagesService.getKey("hospital.phone"),font10b);
		cell = new PdfPCell(headPh);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		return table;
		
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		list = (Listbox)cmp.getFellow("list");
		btnApprove = (Button)cmp.getFellow("btnApprove");
		oppNo = (Bandbox)cmp.getFellow("oppNo");
		issuedBy = (Textbox)cmp.getFellow("issuedBy");
		approvedBy = (Textbox)cmp.getFellow("approvedBy");
		
		supCode = (Textbox)cmp.getFellow("supCode");
		supAddress = (Textbox)cmp.getFellow("supAddress") ;
		supTelp = (Textbox)cmp.getFellow("supTelp");
		
		
		this.btnSearch = (Button)cmp.getFellow("search");
		this.listOpp = (Listbox)cmp.getFellow("listOpp");
		this.oppNoSearch = (Textbox)cmp.getFellow("oppNoSearch");
		
		this.win = (Window)cmp;
		
		status = (Label)cmp.getFellow("status");
		
		oppNo.addEventListener("onChange", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redraw();
				} catch (Exception e) {
					// TODO Auto-generated catch block					
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR",e);					
				}
			}
			
		});
		listOpp.addEventListener("onSelect", new EventListener(){

			public boolean isAsap() {
				// TODO Auto-generated method stub
				return true;
			}

			public void onEvent(Event arg0) {
				// TODO Auto-generated method stub
				try {
					redraw();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						Messagebox.show(e.getMessage());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("REDRAW ERROR",e);
				}
			}
			
		});		
		
		this.oppNo.focus();
	}

	public void doSearch(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub		

		PurchaseServiceLocator.getPORManager().doSearchPORApproval(cmp);
	}
	
	public void doApprove(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub		
		
		TbPurchaseRequest pojo = null;
		
		try{
			pojo =  (TbPurchaseRequest)PurchaseServiceLocator.getPORManager().searchTbPurchaseRequestByCodeForApproval(this.oppNo.getText()).get(0);
			
			if (pojo == null)				
				pojo = (TbPurchaseRequest)this.listOpp.getSelectedItem().getValue();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.oppNo.select();			
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("pch.order.request.approve.question"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;		
		
		PurchaseServiceLocator.getPORManager().doApprove(pojo, super.getUserInfoBean(), this.list);
		
		status.setValue("STATUS : "+ MedisafeConstants.PURCHASING_APPROVED);
		
//		PurchaseServiceLocator.getPORManager().setStaffModify(super.getUserInfoBean(), this.list);
		
		this.btnApprove.setDisabled(true);
	}
	
	private void redraw() throws VONEAppException, InterruptedException {						
		
		if ("".equals(oppNo.getText()) || oppNo.getValue() == null){
			this.list.getItems().clear();
			
			return;
		}
		
		TbPurchaseRequest pojo = null;
		
		try {
						
			pojo =  (TbPurchaseRequest)PurchaseServiceLocator.getPORManager().searchTbPurchaseRequestByCodeForApproval(this.oppNo.getText()).get(0);
			
			if (pojo == null)
			pojo = (TbPurchaseRequest)this.listOpp.getSelectedItem().getValue();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block			
			Messagebox.show(MessagesService.getKey("common.data.notfound"));
			this.oppNo.select();	
		}
		
		if (pojo == null) return;
		
		PurchaseServiceLocator.getPORManager().redrawPORApproval(pojo, this.list);
		
		if(pojo.getSupplierId() != null){
			MsVendor supp = MasterServiceLocator.getVendorManager().getVendorById(pojo.getSupplierId());
			supCode.setValue(supp.getVVendorCode()+"-"+supp.getVVendorName());
			supAddress.setValue(supp.getVVendorAddress());
			supTelp.setValue(supp.getVVendorContactNo());
		}else{
			supAddress.setValue(null);
			supCode.setValue(null);
			supTelp.setValue(null);
		}
		
				
		this.btnApprove.setDisabled(false);
	}
	
}

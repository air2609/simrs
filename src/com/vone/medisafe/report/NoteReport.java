package com.vone.medisafe.report;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.laborat.LaboratManager;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.service.Service;

public class NoteReport {
	private static final String DASH = "-";

	StringBuffer sb;

	private LaboratManager laboratManager = Service.getLaboratManager();

//	private final short COLUMN_01 = 3;
//	private final short COLUMN_02 = 10;
//	private final short COLUMN_03 = 30;
//	private final short COLUMN_04 = 5;
	private final short COLUMN_05 = 13;
	private final short PAGE_WIDTH = 60;
//	private final short COLUMN_HEAD = 20;
	
	public NoteReport(TbReturPharmacyTrx tbReturPharmacyTrx) throws VONEAppException{
		
		sb = new StringBuffer();

		laboratManager.CreateReport(tbReturPharmacyTrx, sb);
		
		screenOut();
		
	}
	
	public NoteReport(TbExamination exam) throws VONEAppException {
		
		sb = new StringBuffer();

		laboratManager.CreateReport(exam, sb);
		
		screenOut();
		
	}
	
	public NoteReport(TbExamination exam, int resep) throws VONEAppException {
		
		sb = new StringBuffer();

		laboratManager.CreateCopyResep(exam, sb);
		
		screenOut();
		
	}
	
	public NoteReport(Collection notas, String noKwitansi, Date tgl, double total, double jumlahDiskon,
			double persentasiPajak, double nilaiPajak, double totalAkhir, double bayarTunai, double bayarKredit,
			double bayarDeposit, double retur) throws VONEAppException
	{
		sb = new StringBuffer();
		
		laboratManager.CreateReport(notas, noKwitansi, tgl, total, jumlahDiskon,
				persentasiPajak, nilaiPajak, totalAkhir, bayarTunai, bayarKredit,
				bayarDeposit, retur, sb);
		
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		//getFooter(nota);
		sb.append(PrintClient.padLeft("TOTAL: ", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(total, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		if(noKwitansi == null){
			sb.append(PrintClient.padLeft("NILAI RETUR (SEMENTARA): ", PAGE_WIDTH - COLUMN_05));
			sb.append(PrintClient.padRight(retur, COLUMN_05));
			sb.append(MedisafeConstants.NEWLINE);
		}
		else{
			sb.append(PrintClient.padLeft("NILAI RETUR : ", PAGE_WIDTH - COLUMN_05));
			sb.append(PrintClient.padRight(retur, COLUMN_05));
			sb.append(MedisafeConstants.NEWLINE);
		}
			
		sb.append(PrintClient.padLeft("JUMLAH DISKON: ", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(jumlahDiskon, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.padLeft("PAJAK (" + persentasiPajak + " % ):", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(nilaiPajak, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.padLeft("TOTAL AKHIR: ", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(totalAkhir, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);

		sb.append(PrintClient.padLeft("TUNAI: ", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(bayarTunai, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.padLeft("KREDIT: ", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(bayarKredit, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.padLeft("DEPOSIT: ", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight(bayarDeposit, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(PrintClient.padLeft("KEMBALI: ", PAGE_WIDTH - COLUMN_05));
		sb.append(PrintClient.padRight((bayarTunai+bayarDeposit+bayarKredit)-totalAkhir, COLUMN_05));
		sb.append(MedisafeConstants.NEWLINE);
		
		
		sb.append(PrintClient.repl(DASH, PAGE_WIDTH));
		sb.append(MedisafeConstants.NEWLINE);
		
		sb.append(MedisafeConstants.NEWLINE);
		sb.append("Print date: " + PrintClient.getDate(new Date(),MedisafeConstants.DATETIME_FORMAT));
		sb.append(MedisafeConstants.CHAR_NORMAL);
		sb.append(MedisafeConstants.NEWLINE);
		
		screenOut();

	}
	
	
	public NoteReport(Listbox pharmacyList, String mrNo, String name, String umur, Session ses) {
		
		printAturanPakai(pharmacyList, mrNo, name, umur, ses);
		
	}

	private void printAturanPakai(Listbox pharmacyList, String mrNo, String name, String umur, Session ses) {
		
		umur = umur.replace("/", "-");
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat jf = new SimpleDateFormat("HH:mm");
		
		this.sb = new StringBuffer();
		
		String[] nama = name.split(" ");
		if(nama.length > 2) name = nama[0]+" "+nama[1];
		
		sb.append(""+name +" ("+mrNo+")/"+umur);
		sb.append(MedisafeConstants.NEWLINE);
								
		List<Listitem> items = pharmacyList.getItems();
		
//		String header = " GRAHA BERNOZA JL SPARMAN ";
		String alamat = "     BENGKULU 0736-20350    ";
		String rs     = "       RS TIARA SELLA       "+jf.format(new Date());
		

		Listcell cell = null;
		Intbox text = null;
		int index = 0;
		for(Listitem item : items){
			index = index + 1;
			sb2 = new StringBuffer();
			sb2.append(rs);
			sb2.append(MedisafeConstants.NEWLINE);
//			sb2.append(header);
//			sb2.append(MedisafeConstants.NEWLINE);
			sb2.append(alamat);
			sb2.append(MedisafeConstants.NEWLINE);
			sb2.append(MedisafeConstants.NEWLINE);
			sb2.append(sb.toString());
						
			cell = (Listcell)item.getChildren().get(2);
			if(cell.getLabel().equalsIgnoreCase("RACIKAN")){
				sb2.append(""+ cell.getLabel());
			}
			else{
				cell = (Listcell)item.getChildren().get(1);
				sb2.append(""+ cell.getLabel());
			}
			
			sb2.append(" : ");
			cell = (Listcell)item.getChildren().get(4);
			if(cell.getChildren().size() > 0){
				text = (Intbox)cell.getChildren().get(0);
				sb2.append("" + text.getText());
			}else{
				sb2.append(cell.getLabel());
			}
			
			sb2.append(MedisafeConstants.NEWLINE);
			cell = (Listcell)item.getChildren().get(5);
			Textbox tbox = (Textbox)cell.getChildren().get(0);
			Listbox lb = (Listbox)cell.getChildren().get(1);
			sb2.append(tbox.getText().toUpperCase()+" "+lb.getSelectedItem().getLabel().toUpperCase());
//			if(item.getAttribute("aturanPakai") != null)
//				sb2.append(""+item.getAttribute("aturanPakai").toString());
//			else sb.append(" ");
			
			sb2.append(MedisafeConstants.NEWLINE);
			sb2.append(sdf.format(new Date()));
			if(item.getAttribute("expired") != null){
//				sb2.append(MedisafeConstants.NEWLINE);
				sb2.append(", ED : " + sdf.format(item.getAttribute("expired")));
			}
			sb2.append(MedisafeConstants.NEWLINE);
			sb2.append(MedisafeConstants.NEWLINE);
			sb2.append(MedisafeConstants.NEWLINE);
			
			
			this.sb.append(sb2.toString());
					
			
		
		}
		
		if(!this.printOut(ses.getClientAddr())){
			try {
				Messagebox.show("GAGAL MENCETAK NOTA\nJALANKAN PROGRAM PrintServer DULU..!!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		 
	}

	public void screenOut(){
		System.out.println(sb.toString());
	}
	
	
	
	public boolean printOut(String ipTarget){
		boolean success = false;
		try {
			success = PrintClient.printStringBuffer(ipTarget, sb);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return success;
	}
	
}

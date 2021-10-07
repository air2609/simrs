package com.vone.medisafe.service.ifaceimpl;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.mapping.dao.diagnose.TbDiagnoseDAO;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;

public class MyLabListiner implements EventListener {
	
	String hasilLab;
	String noMr;
	String namaPasien;
	String alamat;
	String noRegistrasi;
	Listbox diagnosaList;
	
	public MyLabListiner(String noLab, String noMr, String nama, String alamat, String noReg, Listbox dglist){
//		this.dao = dao;
		this.hasilLab = noLab;
		this.noMr = noMr;
		this.alamat = alamat;
		this.namaPasien = nama;
		this.noRegistrasi = noReg;
		this.diagnosaList = dglist;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		String hasil[] = this.hasilLab.split(",");
		if(hasil.length > 0){
			for(int i=0; i < hasil.length; i++){
				int pilihan = Messagebox.show("TAMPILKAN HASIL LAB NO : "+hasil[i], "KONFIRMASI", Messagebox.YES|Messagebox.NO, null);
				if(pilihan == Messagebox.YES){
					tampilkanHasil(hasil[i]);
					return;
				}
			}
		}
		
	}
	
	public void tampilkanHasil(String hasil) throws Exception{
		String query = "select * from report.hasil_lab('"+hasil+"')";
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("nama", this.namaPasien);
		parameters.put("dokter", "");
		parameters.put("alamat", this.alamat);
		parameters.put("tanggal", "");
		parameters.put("umur", "");
		
		if (this.noRegistrasi.substring(0, 1).equals("I"))
				parameters.put("rawat", "RANAP");
		else
				parameters.put("rawat", "RAJAL");
		
		
		parameters.put("noRM", this.noMr);
		parameters.put("register", this.noRegistrasi);
		parameters.put("noHasil", hasil);
		parameters.put("ruangBed", "");
		parameters.put("jamPengambilan", "");
		parameters.put("jamDikerjakan", "");
		parameters.put("noLaborat", "");//no resep

		parameters.put("keterangan", "");
		parameters.put("drPengirim", "");//diisi waktu buat hasil
		
		ReportEngine re = new ReportEngine(query,ReportService.getKey("hasilLab"), parameters);
		if(!re.printOut(diagnosaList.getParent().getDesktop().getSession().getClientAddr()))
			re.openPdf();

	}

}

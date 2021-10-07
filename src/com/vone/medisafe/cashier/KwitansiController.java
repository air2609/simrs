package com.vone.medisafe.cashier;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.TbKwitansi;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class KwitansiController extends BaseController{
	
	CashierManager service = Service.getCashierManager();
	
	Datebox tanggal;
	Listbox tipeList;
	Datebox crTanggal;
	Listbox crTipeList;
	Listbox listKwitansi;
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		tanggal = (Datebox)cmp.getFellow("tanggal");
		tipeList = (Listbox)cmp.getFellow("tipeList");
		crTanggal = (Datebox)cmp.getFellow("crTanggal");
		crTipeList = (Listbox)cmp.getFellow("crTipeList");
		listKwitansi = (Listbox)cmp.getFellow("listKwitansi");
		
		tipeList.setSelectedIndex(0);
		crTipeList.setSelectedIndex(0);
		getAllKwitansi();
		
	}
	
	public void getAllKwitansi() throws VONEAppException {
		listKwitansi.getItems().clear();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fileNameFormat = new SimpleDateFormat("ddMMyyyy");
		
		List<TbKwitansi> kwitansi = service.getAllKwitansi();
		for(TbKwitansi kw : kwitansi) {
			Listitem item = new Listitem();
			item.setValue(kw);
			item.setParent(listKwitansi);
			
			Listcell cell = new Listcell(sdf.format(kw.getTanggal()));
			cell.setParent(item);
			
			cell = new Listcell(kw.getTipe());
			cell.setParent(item);
			
			cell = new Listcell();
			if(kw.getFileLocation().equals("-")) cell.setLabel("-");
			else {

				final String fileName = kw.getTipe()+"_"+fileNameFormat.format(kw.getTanggal())+".zip";
				cell.setLabel("DOWNLOAD");
				cell.setStyle("color:blue");
				cell.addEventListener("onClick", new EventListener() {
					
					@Override
					public void onEvent(Event arg0) throws Exception {
						FileInputStream fin = new FileInputStream(kw.getFileLocation());
						Filedownload.save(fin,"application/zip", fileName);
						
					}
				});
			
			}
			cell.setParent(item);
			
		}
		
	}
	
	public void save() throws InterruptedException, WrongValueException, VONEAppException {
		if(null == tanggal.getValue()) {
			Messagebox.show("Isi Tanggal Terlebih Dahulu!!!");
			return;
		}
		
		String tipe = tipeList.getSelectedItem().getValue().toString();
		
		TbKwitansi kwitansi = service.getKwitansi(tanggal.getValue(), tipe);
		if(null == kwitansi) {
			kwitansi = new TbKwitansi();
			kwitansi.setTanggal(tanggal.getValue());
			kwitansi.setTipe(tipe);
			kwitansi.setStatus(0);
			kwitansi.setFileLocation("-");
			
			if(service.saveKwitansi(kwitansi)) {
				Messagebox.show("Data sukses disimpan!");
				getAllKwitansi();
			}
			
		}else {
			Messagebox.show("Data sudah ada di sistem...!");
		}
	}
	
	
	public void search() throws InterruptedException, WrongValueException, VONEAppException {
		if(null == crTanggal.getValue()) {
			Messagebox.show("Isi Tanggal Pencarian Terlebih Dahulu!!!");
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fileNameFormat = new SimpleDateFormat("ddMMyyyy");
		String tipe = crTipeList.getSelectedItem().getValue().toString();
		TbKwitansi kwitansi = service.getKwitansi(crTanggal.getValue(), tipe);
		if(null == kwitansi) listKwitansi.getItems().clear();
		else {
			listKwitansi.getItems().clear();
			Listitem item = new Listitem();
			item.setValue(kwitansi);
			item.setParent(listKwitansi);
			
			Listcell cell = new Listcell(sdf.format(kwitansi.getTanggal()));
			cell.setParent(item);
			
			cell = new Listcell(kwitansi.getTipe());
			cell.setParent(item);
			
			cell = new Listcell();
			if(kwitansi.getFileLocation().equals("-")) cell.setLabel("-");
			else {

				final String fileName = kwitansi.getTipe()+"_"+fileNameFormat.format(kwitansi.getTanggal())+".zip";
				cell.setLabel("DOWNLOAD");
				cell.setStyle("color:blue");
				cell.addEventListener("onClick", new EventListener() {
					
					@Override
					public void onEvent(Event arg0) throws Exception {
						FileInputStream fin = new FileInputStream(kwitansi.getFileLocation());
						Filedownload.save(fin,"application/zip", fileName);
						
					}
				});
			
			}
			cell.setParent(item);
		}
	}

}

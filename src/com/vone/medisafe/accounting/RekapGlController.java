package com.vone.medisafe.accounting;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.TbGl;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.base.BaseController;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class RekapGlController extends BaseController{
	JournalManager journalManager = Service.getJournalManager();
	Listbox listGl;
	Datebox dfrom;
	Datebox dto;
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		this.listGl = (Listbox)cmp.getFellow("listGl");
		this.dfrom = (Datebox)cmp.getFellow("dfrom");
		this.dto = (Datebox) cmp.getFellow("dto");
		
		getRekapList();
	}
	
	public void save() throws VONEAppException, InterruptedException {
		
		long tgl1 = dfrom.getValue().getTime();
		long tgl2 = dto.getValue().getTime();
		
		long selisih = tgl2 - tgl1;
		//convert into day
		long hari = selisih/(24*60*60*1000);
		
		if(tgl1 > tgl2){
			org.zkoss.zul.Messagebox.show("Rentang tanggal salah, silahkan diperbaiki!");
			return;
		}
		
		if(hari > 93){
			org.zkoss.zul.Messagebox.show("Rentang waktu yang diizinkan hanya 3 bulan!");
			return;
		}
		
		TbGl rekapGl = new TbGl();
		rekapGl.setFrom(dfrom.getValue());
		rekapGl.setTo(dto.getValue());
		rekapGl.setStatus(0);
		
		journalManager.saveRekapGl(rekapGl);
		org.zkoss.zul.Messagebox.show("Data Successfully Saved!");
		this.getRekapList();
		
	}

	private void getRekapList() throws VONEAppException {
		listGl.getItems().clear();
		
		Listitem item;
		Listcell cell;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fileNameFormat = new SimpleDateFormat("ddMMyyyy");
		
		List<TbGl> gls = journalManager.getRekapGlList();
		for(final TbGl gl : gls) {
			item = new Listitem();
			item.setValue(gl);
			item.setParent(listGl);
			
			cell = new Listcell(sdf.format(gl.getFrom()));
			cell.setParent(item);
			
			cell = new Listcell(sdf.format(gl.getTo()));
			cell.setParent(item);
			
			cell = new Listcell();
			if(gl.getFileLocation() == null) {
				cell.setLabel("-");
			}else {
				final String fileName = fileNameFormat.format(gl.getFrom())+"_"+fileNameFormat.format(gl.getTo())+".xlsx";
				cell.setLabel("DOWNLOAD");
				cell.setStyle("color:blue");
				cell.addEventListener("onClick", new EventListener() {
					
					@Override
					public void onEvent(Event arg0) throws Exception {
						FileInputStream fin = new FileInputStream(gl.getFileLocation());
						Filedownload.save(fin,"application/vnd.ms-excel", fileName);
						
					}
				});
			}
			cell.setParent(item);
		}
		
	}

}

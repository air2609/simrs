package com.vone.medisafe.service.ifaceimpl.master;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsBedDAO;
import com.vone.medisafe.mapping.MsRoom;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.master.BedManager;

public class BedManagerImpl implements BedManager{
	
	private MsBedDAO dao;

	public MsBedDAO getDao() {
		return dao;
	}

	public void setDao(MsBedDAO dao) {
		this.dao = dao;
	}

	public void save(MsBed bed) throws VONEAppException {
		
		dao.save(bed);
	}

	public void delete(MsBed bed) throws VONEAppException {
		
		dao.delete(bed);
	}

	public MsBed getBedById(Integer id) throws VONEAppException {
		
		return dao.findById(id);
	}

	
	public MsBed getBedByCode(String code) throws VONEAppException {
		
		return dao.getBedbyCode(code);
	}

	public List getBedBaseByRoom(MsRoom room) throws VONEAppException {
		
		return dao.getBedByRoom(room);
	}

	public boolean deleteById(Integer id) throws VONEAppException {
		
		return dao.deleteById(id);
	}

	public boolean isBedExist(MsBed bed) throws VONEAppException {
		
		return dao.isBedExist(bed);
	}

	public void getAllBed(Listbox bedList) throws VONEAppException {
		List list = dao.getAllBed();
		bedList.getItems().clear();
		MsBed bed = null;
		Decimalbox price = new Decimalbox();
		price.setFormat("#,###.00");
				
		Listitem item;
		Listcell cell;
		Iterator it = list.iterator();
		while(it.hasNext()){
			bed = (MsBed)it.next();
			item = new Listitem();
			item.setValue(bed.getNBedId());
			item.setParent(bedList);
			
			String ruang[] = bed.getMsRoom().getVRoomName().split("-");
			
			cell = new Listcell(ruang[0]);
			cell.setParent(item);
			
			cell = new Listcell(bed.getVBedDesc());
			cell.setParent(item);
			
			if(bed.getMsTreatmentClass() != null){
				cell = new Listcell(bed.getMsTreatmentClass().getVTclassDesc());
			}
			else cell = new Listcell("-");
					
			cell.setParent(item);
			
			cell = new Listcell(ruang[2]);
			cell.setParent(item);
			
			cell = new Listcell(bed.getVBedCode());
			cell.setParent(item);

			price.setValue(new BigDecimal(bed.getNBedPrice()));
			cell = new Listcell(price.getText());
			cell.setParent(item);
			
			cell = new Listcell();
			if(bed.getVBedActiveStatus().equalsIgnoreCase("A"))
				cell.setLabel("ACTIVE");
			else cell.setLabel("INACTIVE");
			cell.setParent(item);
		}

	}

	public void doModify(Component win) throws VONEAppException {
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Bandbox namaKamar = (Bandbox)win.getFellow("namaKamar");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Decimalbox price = (Decimalbox)win.getFellow("price");
		Listbox bedList = (Listbox)win.getFellow("bedList");
		Bandbox coa = (Bandbox)win.getFellow("coa");
		Listbox activeStatus = (Listbox)win.getFellow("activeStatus");
		Listbox conditionStatus = (Listbox)win.getFellow("conditionStatus");
		if(bedList.getSelectedItem() == null){
			try {
				Messagebox.show(MessagesService.getKey("master.bed.bedlist.notselected"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		MsBed bed = dao.getBedById((Integer)bedList.getSelectedItem().getValue());
		
		win.setAttribute("room", bed.getMsRoom());
		win.setAttribute("tclass", bed.getMsTreatmentClass());
		namaKamar.setValue(bed.getMsRoom().getVRoomName());
		kode.setValue(bed.getVBedCode());
		nama.setValue(bed.getVBedDesc());
		price.setValue(new BigDecimal(bed.getNBedPrice()));
		
		if(bed.getMsCoa() != null){
			coa.setText(bed.getMsCoa().getVAcctNo()+" - "+bed.getMsCoa().getVAcctName());
			coa.setAttribute("coa", bed.getMsCoa());
		}
		
		if(bed.getVBedActiveStatus().equalsIgnoreCase("A"))
			activeStatus.setSelectedIndex(0);
		else activeStatus.setSelectedIndex(1);
		
		if("1".equals(bed.getVBedStatus())){
			conditionStatus.setSelectedIndex(1);
		}else conditionStatus.setSelectedIndex(0);
		
		for(int i=1; i < tclassList.getItems().size(); i++){
			if(((MsTreatmentClass)tclassList.getItemAtIndex(i).getValue()).getNTclassId().equals(bed.getMsTreatmentClass().getNTclassId())){
				tclassList.setSelectedIndex(i);
				return;
			}
		}
		
		
			
		
	}

	
	
	public void searchBed(String textCari, Listbox bedList) throws VONEAppException {
		
		bedList.getItems().clear();
		
		
		List<MsBed> beds = this.dao.searchBeds(textCari);
		
		
		Decimalbox price = new Decimalbox();
		price.setFormat("#,###.00");
				
		Listitem item;
		
		for(MsBed bed : beds){
		
			item = new Listitem();
			item.setValue(bed.getNBedId());
			item.setParent(bedList);
			
			String ruang[] = bed.getMsRoom().getVRoomName().split("-");
			
			Listcell namaRuang = new Listcell(ruang[0]);
			namaRuang.setParent(item);
			
			Listcell cell = new Listcell(bed.getVBedDesc());
			cell.setParent(item);
			
			Listcell tclass = new Listcell(bed.getMsTreatmentClass().getVTclassDesc());
			tclass.setParent(item);
			
			Listcell roomNo = new Listcell(ruang[2]);
			roomNo.setParent(item);
			
			Listcell id = new Listcell(bed.getVBedCode());
			id.setParent(item);

			price.setValue(new BigDecimal(bed.getNBedPrice()));
			Listcell harga = new Listcell(price.getText());
			harga.setParent(item);
			
			cell = new Listcell();
			if(bed.getVBedActiveStatus().equalsIgnoreCase("A"))
				cell.setLabel("ACTIVE");
			else cell.setLabel("INACTIVE");
			cell.setParent(item);
			
		}

		MiscTrxController.setFont(bedList);
	}

	@Override
	public void getBedByClass(Listbox listbed) throws VONEAppException {
		listbed.getItems().clear();
		
		int terpakai = 0;
		int kosong = 0;
		int total = 0;
		
		int subtotal = 0;
		int totalterisi = 0;
		
		Listitem item;
		Listcell cell;
		
		Set<MsBed> beds;
		List<MsTreatmentClass> tarifClass = this.dao.getBedByClass();
		for(MsTreatmentClass classTreatment : tarifClass){
			beds = classTreatment.getMsBeds();
			
			terpakai = 0;
			kosong = 0;
			total = 0;
			
			if(beds.size() > 0){
				for(MsBed bed : beds){
					if(bed.getVBedActiveStatus().equalsIgnoreCase("A") && bed.getVBedStatus().equalsIgnoreCase(MedisafeConstants.BED_TERPAKAI)){
						terpakai = terpakai + 1;
					}  
					else if(bed.getVBedActiveStatus().equalsIgnoreCase("A") && bed.getVBedStatus().equalsIgnoreCase(MedisafeConstants.BED_KOSONG)) kosong = kosong + 1;
				}
				
				
			}
			
			total = terpakai + kosong;
			totalterisi = totalterisi + terpakai;
			subtotal = subtotal + total;
			
			if(total > 0){
				item = new Listitem();
				item.setParent(listbed);
				
				cell = new Listcell(classTreatment.getVTclassDesc());
				cell.setStyle("font-size:20pt");
				cell.setParent(item);
				
				cell = new Listcell(total +"");
				cell.setStyle("font-size:20pt");
				cell.setParent(item);
				
				cell = new Listcell(terpakai +"");
				cell.setStyle("font-size:20pt");
				cell.setParent(item);
				
			}
		}
		
		item = new Listitem();
		item.setParent(listbed);
		
		cell = new Listcell("TOTAL");
		cell.setStyle("font-weight:bold;font-size:20pt");
		cell.setParent(item);
		
		cell = new Listcell(subtotal +"");
		cell.setStyle("font-weight:bold;font-size:20pt");
		cell.setParent(item);
		
		cell = new Listcell(totalterisi +"");
		cell.setStyle("font-weight:bold;font-size:20pt");
		cell.setParent(item);
		
	}

	@Override
	public void getBedByClass(Listhead listhead) {
		listhead.getChildren().clear();
		
		List<Object[]> objs = this.dao.getTotalBedByClass();
		
		Listheader header = new Listheader("Tanggal");
		header.setParent(listhead);
		
		for(Object[] obj : objs){
			header = new Listheader((String)obj[1] +" ("+obj[0]+")");
			header.setParent(listhead);
		}
		
	}

	@Override
	public List getDuplicateBed() {
		// TODO Auto-generated method stub
		return dao.getDuplicateBed();
	}

	@Override
	public void getActiveBed(Listbox bedList, final Checkbox cbox) throws VONEAppException {
		bedList.getItems().clear();
		
		Listitem item = null;
		Listcell cell = null;
		
		int nomor = 1;
		
		List<MsBed> beds = this.dao.getActiveBed();
		for(MsBed bed : beds){
			item = new Listitem();
			item.setValue(bed);
			item.setParent(bedList);
			
			cell = new Listcell(""+nomor);
			cell.setParent(item);
			
			String ruang[] = bed.getMsRoom().getVRoomName().split("-");
			
			cell = new Listcell(bed.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell(ruang[0]);
			cell.setParent(item);
			
			cell = new Listcell(ruang[2]);
			cell.setParent(item);
								
			cell = new Listcell(bed.getVBedCode());
			cell.setParent(item);
			
			cell = new Listcell(bed.getVBedDesc());
			cell.setParent(item);
			
			cell = new Listcell("1".equals(bed.getVBedStatus())? "Terisi" : "Kosong");
			cell.setParent(item);
			
			cell = new Listcell();
			final Checkbox cb = new Checkbox();
			cb.setParent(cell);
			cb.addEventListener("onCheck", new EventListener() {
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					if(!cb.isChecked()){
						cbox.setChecked(false);
					}
					
				}
			});
			if("Y".equals(bed.getIsShown())) cb.setChecked(true);
			cell.setParent(item);
			
			cell = new Listcell();
			Listbox listBox = new Listbox();
			listBox.setMold("select");
			
			Listitem li = new Listitem("Available");
			li.setValue("A");
			listBox.appendChild(li);

			li = new Listitem("Dipesan");
			li.setValue("B");
			listBox.appendChild(li);
			
			li = new Listitem("Perbaikan");
			li.setValue("C");
			listBox.appendChild(li);
			listBox.setParent(cell);
			if("A".equals(bed.getAvailableStatus()) || bed.getAvailableStatus() == null)
					listBox.setSelectedIndex(0);
			else if("B".equals(bed.getAvailableStatus())) listBox.setSelectedIndex(1);
			else listBox.setSelectedIndex(2);
			cell.setParent(item);
			
			nomor = nomor + 1;
		}
		
	}

	@Override
	public void saveBulk(List<MsBed> beds) throws VONEAppException {
		this.dao.saveBulk(beds);
		
	}

	@Override
	public void getBedInfo(Listbox bedInfoList) throws VONEAppException {
		List<Object[]> listBed = this.dao.getBedInfo();
		bedInfoList.getItems().clear();
		
		Listitem item = new Listitem();
		Listcell cell = new Listcell("KELAS");
		cell.setStyle("font-weight:bold;font-size:15pt; color : green");
		cell.setParent(item);
		item.setParent(bedInfoList);
		
		cell = new Listcell("RUANGAN");
		cell.setStyle("font-weight:bold;font-size:15pt;color : green");
		cell.setParent(item);
		
		cell = new Listcell("TOTAL BED");
		cell.setStyle("font-weight:bold;font-size:15pt;text-align: center;color : green");
		cell.setParent(item);
		
		cell = new Listcell("TERISI");
		cell.setStyle("font-weight:bold;font-size:15pt;text-align: center;color : green");
		cell.setParent(item);
		
		cell = new Listcell("DI PESAN");
		cell.setStyle("font-weight:bold;font-size:15pt;text-align: center;color : green");
		cell.setParent(item);
		
		cell = new Listcell("PER BAIKAN");
		cell.setStyle("font-weight:bold;font-size:15pt;text-align: center;color : green");
		cell.setParent(item);
		
		cell = new Listcell("BED KOSONG");
		cell.setStyle("font-weight:bold;font-size:15pt;text-align: center;color : green");
		cell.setParent(item);
		
		int total = 0;
		int totalTerpakai = 0;
		int totalBooked = 0;
		int totalInservice = 0;
		
		for(Object[] o : listBed){
			item = new Listitem();
			cell = new Listcell(o[0].toString());
			cell.setStyle("font-weight:bold;font-size:14pt;");
			cell.setParent(item);
			item.setParent(bedInfoList);
			
			cell = new Listcell(o[1].toString());
			cell.setStyle("font-weight:bold;font-size:14pt;");
			cell.setParent(item);
			
			cell = new Listcell(o[2].toString());
			cell.setStyle("font-weight:bold;font-size:14pt;text-align: center");
			cell.setParent(item);
			
			Integer jmlBed = (Integer)o[2];
			total = total + jmlBed;
			Integer terpakai = this.dao.getBedTerisi(o[0].toString(), o[1].toString());
			totalTerpakai = totalTerpakai + terpakai;
			
			cell = new Listcell(terpakai.toString());
			cell.setStyle("font-weight:bold;font-size:14pt;text-align: center");
			cell.setParent(item);
			
			Integer booked = this.dao.getBedBooked(o[0].toString(), o[1].toString());
			totalBooked = totalBooked + booked;
			
			cell = new Listcell(booked.toString());
			cell.setStyle("font-weight:bold;font-size:14pt;text-align: center");
			cell.setParent(item);
			
			Integer inService = this.dao.getBedInservice(o[0].toString(), o[1].toString());
			totalInservice = totalInservice + inService;
			
			cell = new Listcell(inService.toString());
			cell.setStyle("font-weight:bold;font-size:14pt;text-align: center");
			cell.setParent(item);
			
			cell = new Listcell((jmlBed-terpakai-booked-inService)+"");
			cell.setStyle("font-weight:bold;font-size:14pt;text-align: center");
			cell.setParent(item);
			
		}
		
		item = new Listitem();
		cell = new Listcell();
		cell.setStyle("font-weight:bold;font-size:14pt;");
		cell.setParent(item);
		item.setParent(bedInfoList);
		
		cell = new Listcell("TOTAL");
		cell.setStyle("font-weight:bold;font-size:14pt; color :blue");
		cell.setParent(item);
		
		cell = new Listcell(total+"");
		cell.setStyle("font-weight:bold;font-size:14pt;text-align: center;color :blue");
		cell.setParent(item);
		
		cell = new Listcell(totalTerpakai+"");
		cell.setStyle("font-weight:bold;font-size:14pt;text-align: center;color :blue");
		cell.setParent(item);
		
		cell = new Listcell(totalBooked+"");
		cell.setStyle("font-weight:bold;font-size:14pt;text-align: center;color :blue");
		cell.setParent(item);
		
		cell = new Listcell(totalInservice+"");
		cell.setStyle("font-weight:bold;font-size:14pt;text-align: center;color :blue");
		cell.setParent(item);
		
		cell = new Listcell((total-totalTerpakai-totalBooked-totalInservice)+"");
		cell.setStyle("font-weight:bold;font-size:14pt;text-align: center;color :blue");
		cell.setParent(item);
		
		
	}

	@Override
	public void getBorReport(Datebox startDate, Datebox endDate, Listbox borList) throws VONEAppException {
		List<Object[]> listBed = this.dao.getBorInfo(startDate.getValue(), endDate.getValue());
		borList.getItems().clear();
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#.##");
		
		Listitem item = new Listitem();
		Listcell cell = new Listcell("KELAS TARIF");
		cell.setStyle("font-weight:bold;font-size:18pt; color : green");
		cell.setParent(item);
		item.setParent(borList);
		
		cell = new Listcell("RUANGAN");
		cell.setStyle("font-weight:bold;font-size:18pt;color : green");
		cell.setParent(item);
		
		cell = new Listcell("TOTAL BED");
		cell.setStyle("font-weight:bold;font-size:18pt;text-align: center;color : green");
		cell.setParent(item);
		
		cell = new Listcell("TOTAL TERISI");
		cell.setStyle("font-weight:bold;font-size:18pt;text-align: center;color : green");
		cell.setParent(item);
		
		cell = new Listcell("BOR");
		cell.setStyle("font-weight:bold;font-size:18pt;text-align: center;color : green");
		cell.setParent(item);
		
		int totalBed = 0;
		int totalBedTerisi = 0;
		
		for(Object[] o : listBed){
			item = new Listitem();
			cell = new Listcell(o[0].toString());
			cell.setStyle("font-weight:bold;font-size:16pt");
			cell.setParent(item);
			item.setParent(borList);
			
			cell = new Listcell(o[1].toString());
			cell.setStyle("font-weight:bold;font-size:16pt");
			cell.setParent(item);
			
			cell = new Listcell(o[2].toString());
			cell.setStyle("font-weight:bold;font-size:16pt;text-align: center");
			cell.setParent(item);
			
			Integer jmlBed = (Integer)o[2];
			totalBed = totalBed + jmlBed;
			Integer jmlTerisi = (Integer)o[3];
			totalBedTerisi = totalBedTerisi + jmlTerisi;
			
			cell = new Listcell(o[3].toString());
			cell.setStyle("font-weight:bold;font-size:16pt;text-align: center");
			cell.setParent(item);
			
			
			Double bor = (Double)o[4] * 100;
			db.setValue(new BigDecimal(bor));
			cell = new Listcell(db.getText()+"%");
			cell.setStyle("font-weight:bold;font-size:16pt;text-align: center");
			cell.setParent(item);
			
		}
		
		item = new Listitem();
		cell = new Listcell();
		cell.setStyle("font-weight:bold;font-size:16pt");
		cell.setParent(item);
		item.setParent(borList);
		
		cell = new Listcell("TOTAL");
		cell.setStyle("font-weight:bold;font-size:16pt; color :blue");
		cell.setParent(item);
		
		cell = new Listcell(totalBed+"");
		cell.setStyle("font-weight:bold;font-size:16pt;text-align: center;color :blue");
		cell.setParent(item);
		
		cell = new Listcell(totalBedTerisi+"");
		cell.setStyle("font-weight:bold;font-size:16pt;text-align: center;color :blue");
		cell.setParent(item);
		
		if(totalBed > 0) {
			double borV = ((double)totalBedTerisi/(double)totalBed) * 100;
			db.setValue(new BigDecimal(borV));
			System.out.println(borV);
		}else db.setValue(new BigDecimal(0));
		
		cell = new Listcell(db.getText()+ "%");
		cell.setStyle("font-weight:bold;font-size:16pt;text-align: center;color :blue");
		cell.setParent(item);
		
	}
	
	

}

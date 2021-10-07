package com.vone.medisafe.service.ifaceimpl.master.treatment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;




import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.dao.treatment.MsTreatmentDAO;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.master.treatment.TreatmentManager;
import com.vone.medisafe.ui.master.treatment.TreatmentController;

public class TreatmentManagerImpl implements TreatmentManager{
	
	private MsTreatmentDAO dao;

	public MsTreatmentDAO getDao() {
		return dao;
	}

	public void setDao(MsTreatmentDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsTreatmentFee treatment) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.delete(treatment);
	}

	public List getTreatments() throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getTreatments();
	}

	public boolean save(MsTreatment treatment, MsTreatmentFee tfee) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.save(treatment, tfee);
	}

	public List searchTreatment(MsTreatment treatment) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.findByExample(treatment);
	}

	public MsTreatment getByTreatmentCode(String code) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getTreatmentByCode(code);
	}

	public List getTreatmentByUnitAndClass(MsUnit unit, String tclass, String isPacket) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getTreatmentByUnit(unit, tclass, isPacket);
	}

	public List searchTreatment(Integer unitId, String tcode, String tname, String tclass, String isPacket) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getSearchTreatmentByUnit(unitId, tcode, tname, tclass, isPacket);
	}

	public List getPacketTreatment(MsUnit unit, String tclass, String isPacket)throws VONEAppException  {
		// TODO Auto-generated method stub
		return dao.getTreatmentByUnit(unit, tclass, isPacket);
	}
	//kun
	public List getTreatmentByTreatmentGroup(String code, String kelasTarif) throws VONEAppException {
		return dao.getTreatmentByTreatmentGroup(code, kelasTarif);
	}
	//kun
	public List getTreatmentByUnit(Integer unitId, String tcode, String tname, String tclass) 
	throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getSearchTreatmentByTreatmentUnit(unitId, tcode, tname, tclass);
	}

	
	public void searchTreatment(Integer unitId, Textbox code, Textbox treatmentName, String kelasTarif,
			String non_paket, Listbox treatmentList) throws VONEAppException, InterruptedException {
		
		boolean selected = false;
		Listitem item;
		Listcell cell;
		
		Set treatments = treatmentList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		treatmentList.getItems().clear();
		
		
		for(int i=0; i < listitem.length; i++){
			item = listitem[i];
			item.setParent(treatmentList);
		}
		
			
		//add search result to listbox, if result size is 1, add result to selection
		if(code.getText().trim().equals("") && treatmentName.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			code.focus();
			return;
		}
		
		
		Decimalbox decbox = new Decimalbox();
		decbox.setFormat("#,###.##");
		
		List<MsTreatmentFee> list = this.dao.getSearchTreatmentByUnit(unitId, "%"+code.getText()+"%", 
				"%"+treatmentName.getText()+"%", kelasTarif, non_paket);
			

		
		if(list.size() == 1){
	
			MsTreatmentFee tfee = list.get(0);
	
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				if(((MsTreatmentFee)item.getValue()).getNTreatmentFeeId().equals(tfee.getNTreatmentFeeId()))
				{		
					code.focus();
					return;
				
				}
			}
			
			item = new Listitem();
			item.setValue(tfee);
			item.setParent(treatmentList);
	
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
	
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
	
			decbox.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			cell = new Listcell(decbox.getText());
			cell.setParent(item);
	
			treatmentList.addItemToSelection(item);
			MiscTrxController.setFont(treatmentList);
			code.focus();
			return;
	
		}

	
	
		for(MsTreatmentFee tfee : list){
	
			for(int i=0; i < listitem.length; i++){
			
				item = listitem[i];
			
				if(((MsTreatmentFee)item.getValue()).getNTreatmentFeeId().equals(tfee.getNTreatmentFeeId()))
				{
					selected = true;
					break;
				}else selected = false;
			
			}
			
			if(!selected){
		
				item = new Listitem();
				item.setValue(tfee);
				item.setParent(treatmentList);
		
				cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
				cell.setParent(item);
		
				cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
				cell.setParent(item);
		
				decbox.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
				cell = new Listcell(decbox.getText());
				cell.setParent(item);
		
				selected = false;
		
			}
		
		}
		
		MiscTrxController.setFont(treatmentList);
		code.focus();
	}

	
	public void getTreatmentPacketForSelect(Listbox treatmentNameList, MsUnit unit, String kelasTarif,
			String paket) throws VONEAppException {
		
		Listitem item;
		
		treatmentNameList.getItems().clear();
		
		item = new Listitem();
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(treatmentNameList);
		treatmentNameList.setSelectedIndex(0);
		
		List<MsTreatmentFee> list = dao.getTreatmentByUnit(unit, kelasTarif,MedisafeConstants.PAKET);
		
		
		for(MsTreatmentFee tfee : list){
			
			item = new Listitem();
			item.setValue(tfee);
			item.setLabel(tfee.getMsTreatment().getVTreatmentName() +" - " + tfee.getMsTreatment().getVTreatmentCode());
			item.setParent(treatmentNameList);
			
		}
		
	}

	public void getTreatments(Listbox treatmentList) throws VONEAppException {
		treatmentList.getItems().clear();
		
		Decimalbox fee = new Decimalbox();
		fee.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		
		List list = dao.getTreatments();
		Iterator it = list.iterator();
		while(it.hasNext()){
			MsTreatmentFee tfee = (MsTreatmentFee)it.next();
			Listitem item = new Listitem();
			item.setValue(tfee);
			item.setParent(treatmentList);
			
			Listcell cell = new Listcell(tfee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell(tfee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			if(tfee.getNRsFee() != null){
				fee.setValue(new BigDecimal(tfee.getNRsFee()));
				cell = new Listcell(fee.getText());
			}else{
				cell = new Listcell("-");
			}
			cell.setParent(item);
			
			if(tfee.getNDoctorFee() != null){
				fee.setValue(new BigDecimal(tfee.getNDoctorFee()));
				cell = new Listcell(fee.getText());
			}else{
				cell = new Listcell("-");
			}
			cell.setParent(item);
			
			if(tfee.getNMedicFee() != null){
				fee.setValue(new BigDecimal(tfee.getNMedicFee()));
				cell = new Listcell(fee.getText());
			}else{
				cell = new Listcell("-");
			}
			cell.setParent(item);
			
			
			fee.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
			cell = new Listcell(fee.getText());
			cell.setParent(item);
			
		}
	}

	
	public void searchTreatment(String tcode, String tname, String unitname, String tclass, 
			Listbox treatmentList) throws VONEAppException {
		
				
		
	}

	public void prepareModify(TreatmentController controller) throws VONEAppException {
		
		MsTreatmentFee tfee = (MsTreatmentFee)controller.treatmentList.getSelectedItem().getValue();
		tfee = this.dao.getTreatmentFeeById(tfee.getNTreatmentFeeId());
		
		controller.treatmentCode.setValue(tfee.getMsTreatment().getVTreatmentCode());
		controller.treatmentName.setValue(tfee.getMsTreatment().getVTreatmentName());
		if(tfee.getNDoctorFee() != null) controller.doctorFee.setValue(new BigDecimal(tfee.getNDoctorFee()));
		if(tfee.getNRsFee() != null) controller.hospitalFee.setValue(new BigDecimal(tfee.getNRsFee()));
		if(tfee.getNMedicFee() != null)controller.paramedicFee.setValue(new BigDecimal(tfee.getNMedicFee()));
		controller.treatmentFee.setValue(new BigDecimal(tfee.getNTrtfeeFee()));
		
		
		for(int i=1; i < controller.treatmentGroup.getItems().size(); i++){
			if(tfee.getMsTreatment().getMsTreatmentGroup().getNTgroupId().equals(((MsTreatmentGroup)
					controller.treatmentGroup.getItemAtIndex(i).getValue()).getNTgroupId()))
			{
				controller.treatmentGroup.setSelectedIndex(i);
				break;
			}
		}
		
		
		if(tfee.getMsCoa() == null){
			controller.coa.setText("");
		}
		else{
			controller.coa.setValue(tfee.getMsCoa().getVAcctNo()+" - "+tfee.getMsCoa().getVAcctName());
			controller.coa.setAttribute("coa", tfee.getMsCoa());
		}
		
		
		for(int i=1; i < controller.tclass.getItems().size(); i++){
			if(tfee.getMsTreatmentClass().getNTclassId().equals(((MsTreatmentClass)
					controller.tclass.getItemAtIndex(i).getValue()).getNTclassId()))
			{
				controller.tclass.setSelectedIndex(i);
				break;
			}
		}
		
	}

	public void search(String input, Listbox hasilCari) throws VONEAppException {


		hasilCari.getItems().clear();
		Decimalbox db = new Decimalbox();
		db.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		Listitem item;
		Listcell cell;
		
		List<MsTreatmentFee> list = dao.searchTreatement(input);
		
		for(MsTreatmentFee fee : list){
			
			item = new Listitem();
			item.setValue(fee);
			item.setParent(hasilCari);
			
			cell = new Listcell(fee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(fee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell(fee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			if(fee.getNRsFee() == null)
				cell = new Listcell("-");
			else{
				db.setValue(new BigDecimal(fee.getNRsFee()));
				cell = new Listcell(db.getText());
			}
			cell.setParent(item);
			
			if(fee.getNDoctorFee() == null)
				cell = new Listcell("-");
			else {
				db.setValue(new BigDecimal(fee.getNDoctorFee()));
				cell = new Listcell(db.getText());
			}
			cell.setParent(item);
			
			
			if(fee.getNMedicFee() == null)
				cell = new Listcell("-");
			else{
				db.setValue(new BigDecimal(fee.getNMedicFee()));
				cell = new Listcell(db.getText());
			}
			cell.setParent(item);
			
			db.setValue(new BigDecimal(fee.getNTrtfeeFee()));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
			
		}

		
	}

	@Override
	public void getAllTreatment(Listbox treatmentList) throws VONEAppException {
		List<MsTreatmentFee> fees = this.dao.getAllTreatmentFee();
		treatmentList.getItems().clear();
		
		Listitem item;
		Listcell cell;
		Decimalbox db = new Decimalbox();
	
		db.setFormat("###");
		for(MsTreatmentFee fee : fees){
			item = new Listitem();
			item.setParent(treatmentList);
			
			cell = new Listcell(fee.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(fee.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell(fee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell();
			if(fee.getNRsFee() != null){
				db.setValue(new BigDecimal(fee.getNRsFee()));
				cell.setLabel(db.getText());
			}
			else cell.setLabel("0");
			cell.setParent(item);
			
			cell = new Listcell();
			if(fee.getNDoctorFee() != null){
			   db.setValue(new BigDecimal(fee.getNDoctorFee()));
			   cell.setLabel(db.getText());
			}
			else cell.setLabel("-");
			cell.setParent(item);
			
			cell = new Listcell();
			if(fee.getNMedicFee() != null){
				db.setValue(new BigDecimal(fee.getNMedicFee()));
				cell.setLabel(db.getText());
			}
			else cell.setLabel("0");
			cell.setParent(item);
			
			cell = new Listcell();
			if(fee.getNTrtfeeFee() != null){
				db.setValue(new BigDecimal(fee.getNTrtfeeFee()));
				cell.setLabel(db.getText());
			}else cell.setLabel("0");
			cell.setParent(item);
			
			cell = new Listcell(fee.getMsCoa().getVAcctNo());
			cell.setParent(item);
		}
	}

	@Override
	public void updateTreatmentFee(Listbox treatmentList)
			throws VONEAppException {
		List<MsTreatmentFee> fees = new ArrayList<MsTreatmentFee>();
		List<Listitem> items = treatmentList.getItems();
		MsTreatmentFee fee = null;
		String kode = null;
		String klsTarif = null;
		String coa = "";
		boolean valid = true;
		for(Listitem item : items){
			kode = ((Listcell)item.getChildren().get(0)).getLabel();
			klsTarif = ((Listcell)item.getChildren().get(2)).getLabel();
			fee = this.dao.getTreatmentFee(kode, klsTarif);
			if(fee == null){
				MsTreatment treat = this.dao.getTreatmentByCode(kode);
				if(treat == null){
					treat = new MsTreatment();
					treat.setVTreatmentCode(kode);
					treat.setVTreatmentName(((Listcell)item.getChildren().get(1)).getLabel());
					
					MsTreatmentGroup group = new MsTreatmentGroup();
					group.setNTgroupId(9);
					treat.setMsTreatmentGroup(group);
				}
				fee = new MsTreatmentFee();
				fee.setMsTreatment(treat);
				
				MsTreatmentClass kls = this.dao.getClassTarif(klsTarif);
				fee.setMsTreatmentClass(kls);
				
				this.dao.save(treat, fee);

			}
			fee.setNRsFee(new Double(((Listcell)item.getChildren().get(3)).getLabel()));
			fee.setNDoctorFee(new Double(((Listcell)item.getChildren().get(4)).getLabel()));
			fee.setNMedicFee(new Double(((Listcell)item.getChildren().get(5)).getLabel()));
			fee.setNTrtfeeFee(new Double(fee.getNMedicFee()+fee.getNDoctorFee()+fee.getNRsFee()));
			fee.setMsCoa(this.dao.getMsCoa(((Listcell)item.getChildren().get(7)).getLabel()));
			if(fee.getMsCoa() == null){
				coa = coa + ((Listcell)item.getChildren().get(7)).getLabel() + ", ";
				valid = false;
			}
			fees.add(fee);
		}
		
		if(valid){
			if(this.dao.updateFees(fees)){
				try {
					Messagebox.show("Sukses Mengupdate Data", "Konfirmasi", Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			try {
				Messagebox.show("Invalid Kode COA : "+coa, "Konfirmasi", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	@Override
	public void getTreatmentReport(Datebox dateFrom, Datebox dateTo, Listbox tipeList,
			Listbox treatmentList) throws VONEAppException {
		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(dateTo.getValue());
//		cal.add(Calendar.DATE, 1);
		
//		System.out.println(cal.getTime());
		String tipePasien = tipeList.getSelectedItem().getValue().toString();
		
		List<Object> objs = this.dao.getTreatmentReport(dateFrom.getValue(), dateTo.getValue(), tipePasien);
		
		treatmentList.getItems().clear();
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###");
		
		for(Object o : objs){
			Object[] obj = (Object[])o;
			Listitem item = new Listitem();
			item.setParent(treatmentList);
			
			Listcell cell = new Listcell((String)obj[0]);
			cell.setParent(item);
			
			cell = new Listcell((String)obj[1]);
			cell.setParent(item);
			
			db.setValue((BigDecimal)obj[2]);
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
			db.setValue((BigDecimal)obj[3]);
			cell = new Listcell(db.getText());
			cell.setParent(item);
		}
		
	}

}

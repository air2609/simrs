package com.vone.medisafe.misc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.Service;

public class MiscTrxController {

	private static final String CURRENCTY_FORMAT = MedisafeConstants.CURRENCY_FORMAT;
	private static Listitem item;
	private static Listcell cell;
	private static Decimalbox db = new Decimalbox();

	public static void getNoteDetailItemTrx(TbExamination nota, Listbox transList) throws VONEAppException {
		List itemTrxList = Service.getPolyclinicManager().getItemTrx(nota);
		Iterator it = itemTrxList.iterator();

		
		while(it.hasNext()){
			//obj[0] = item id
			//obj[1] = item code
			//ojb[2] = item name
			//obj[3] = item quantify
			//obj[4] = item qty
			//obj[5] = trx value
			//obj[6] = discount value
			//obj[7] = subtotal value
			
			Object[] obj = (Object[])it.next();
			item = new Listitem();
			item.setValue(obj);
			item.setParent(transList);
			
			//item code
			cell = new Listcell(obj[1].toString());
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
//			cell.setLabel(itemTrx.getMsItem().getVItemCode());
			
			
			//item name
			cell = new Listcell(obj[2].toString());
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);

			
			//item quantity
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(obj[4].toString()));
			jumlah.setParent(cell);
			cell.setParent(item);

			
//			satuan
			cell = new Listcell(obj[3].toString());
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);

			
			
			double hargaSatuan = (Double) obj[5] / (Integer)obj[4];
			db.setValue(new BigDecimal(hargaSatuan));
			//item price before discount
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
			

			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal((Double)obj[6]));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("42%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(obj[9].toString().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
//			cell.setLabel();
//			cell.setLabel(db.getText());
			
			

			db.setValue(new BigDecimal((Double)obj[7]));
			
			//item price after discount			
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
		}		
	}

	public static void getNoteDetailMiscTrx(TbExamination nota, Listbox transList) throws VONEAppException {
		List miscTrxList = Service.getPolyclinicManager().getMiscTrx(nota);

		Iterator it = miscTrxList.iterator();
		while(it.hasNext()){
			TbMiscTrx tbMiscTrx = (TbMiscTrx)it.next();
			item = new Listitem();
			item.setValue(tbMiscTrx);
			item.setParent(transList);
			
			//tratment code
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(MedisafeConstants.MISC_CODE);
			
			//treatment name
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(tbMiscTrx.getVMiscName());
			
			//treatment quantity
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(tbMiscTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);
			
			//satuan
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel("-");
			
			double hargaSatuan = tbMiscTrx.getNAmountTrx() / tbMiscTrx.getNQty();
			
			db.setFormat(CURRENCTY_FORMAT);
			db.setValue(new BigDecimal(hargaSatuan));
			//treatment price before discount
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
			
			
			//discount
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat(CURRENCTY_FORMAT);
			diskon.setStyle("font-size:8pt");
			diskon.setHeight("14px");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(tbMiscTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("40%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(tbMiscTrx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			
			
			db.setValue(new BigDecimal(tbMiscTrx.getNAmountAfterDisc()));
			
			//treatment price after discount			
			cell = new Listcell();
			cell.setStyle(MedisafeConstants.LIST_FONT);
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
	}

	public static void getNoteDetailTreatmentTrx(TbExamination nota, Listbox transList) throws VONEAppException  {
		List treatmentTrxList = Service.getLaboratManager().getTreatmentTrx(nota);
		Iterator it = treatmentTrxList.iterator();
		MsLabTestDetail ltd;
		while (it.hasNext()) {
			TbTreatmentTrx treatmentTrx = (TbTreatmentTrx) it.next();

			item = new Listitem();
			item.setValue(treatmentTrx);
			item.setParent(transList);

			// tratment code
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(treatmentTrx.getMsTreatmentFee().getMsTreatment()
					.getVTreatmentCode());

			// treatment name
			// ambil radiografer-nya
			MsStaff radiografer = treatmentTrx.getAnastesiStaff();
			MsStaff dokter = treatmentTrx.getMsDoctor();
			
			String ket = treatmentTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName();
			if(dokter != null)
				ket = ket + " - " + dokter.getVStaffName();
			if(radiografer != null)
				ket = ket + " - " + radiografer.getVStaffName();
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(ket);	

//			treatment quantity
			cell = new Listcell();
			Intbox jumlah = new Intbox();
			jumlah.setDisabled(true);
			jumlah.setValue(new Integer(treatmentTrx.getNQty()));
			jumlah.setParent(cell);
			cell.setParent(item);

			// satuan
			cell = new Listcell("-");
			ltd = Service.getLaboratManager().getLabTestDetail(treatmentTrx.getNTreatmentId()); 
			if(ltd != null){
				cell = new Listcell(ltd.getVLabTestQuantify());
			}
			cell.setParent(item);

			db.setValue(new BigDecimal(treatmentTrx.getNAmountTrx()));
			// treatment price before discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());

			//discount
			cell = new Listcell();
			
			Decimalbox diskon = new Decimalbox();
			diskon.setDisabled(true);
			diskon.setWidth("50%");
			diskon.setFormat("#,###.##");
			diskon.setHeight("14px");
			diskon.setStyle("font-size:8pt");
			diskon.setParent(cell);
			diskon.setValue(new BigDecimal(treatmentTrx.getNDiscAmount()));
			
			Listbox diskonList = new Listbox();
			diskonList.setDisabled(true);
			diskonList.setParent(cell);
			diskonList.setWidth("49%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:9pt");
			Listitem listitem = new Listitem();
			listitem.setValue(MedisafeConstants.RP);
			listitem.setLabel("1. RP");
			listitem.setParent(diskonList);
			listitem = new Listitem();
			listitem.setValue(MedisafeConstants.PERCENT);
			listitem.setLabel("2. %");
			listitem.setParent(diskonList);
			
			if(treatmentTrx.getVDiscType().equals(MedisafeConstants.RP)){
				diskonList.setSelectedIndex(0);
			}else diskonList.setSelectedIndex(1);
			
			cell.setParent(item);
			

			db.setValue(new BigDecimal(treatmentTrx.getNAmountAfterDisc()));

			// treatment price after discount
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(db.getText());
		}
	}

	/**
	 * save modified transaction
	 */
	@SuppressWarnings("unchecked")
	public static Set<TbMiscTrx> saveModifyMiscTrx(Listbox transList, MsUser user) {
		Set<TbMiscTrx> miscSet = new HashSet<TbMiscTrx>(0);
		List<Listitem> items = transList.getItems();;
		for(Listitem item : items){

			if(item.getValue() instanceof TbMiscTrx){
				TbMiscTrx miscTrx = (TbMiscTrx)item.getValue();
				miscTrx.setDWhnChange(new Date());
				miscTrx.setVWhoChange(user.getVUserName());
				
				cell = (Listcell)item.getChildren().get(2);
				Intbox jumlah = (Intbox)cell.getChildren().get(0);
				jumlah.setDisabled(true);
				miscTrx.setNQty(jumlah.getValue().shortValue());
				
				cell = (Listcell)item.getChildren().get(4);
				db.setText(cell.getLabel());
				
				double trxValue = jumlah.getValue().intValue() * db.getValue().doubleValue();
				miscTrx.setNAmountTrx(trxValue);
				
				cell = (Listcell)item.getChildren().get(6);
				db.setText(cell.getLabel());
				miscTrx.setNAmountAfterDisc(db.getValue().doubleValue());
				
				cell = (Listcell)item.getChildren().get(5);
				Decimalbox diskon = (Decimalbox)cell.getChildren().get(0);
				diskon.setDisabled(true);
				Listbox opsiDiskon = (Listbox)cell.getChildren().get(1);
				opsiDiskon.setDisabled(true);
				miscTrx.setNDiscAmount(diskon.getValue().doubleValue());
				miscTrx.setVDiscType(opsiDiskon.getSelectedItem().getValue().toString());
				
				miscSet.add(miscTrx);
			}

		}
		return miscSet;
	}

	public static void saveTrx(Listbox transList, MsUser user, Set<TbTreatmentTrx> treatmentSet, Set<TbItemTrx> itemSet, Set<TbMiscTrx> miscSet) {
		List list = transList.getItems();
		Iterator it = list.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			Object[] obj = (Object[])item.getAttribute("manipulation");
			if(item.getValue() instanceof MsTreatmentFee){
				TbTreatmentTrx treatmentTrx = new TbTreatmentTrx();
				treatmentTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
//				listitem.setAttribute("doctor",examinerDoctorList.getSelectedItem().getValue());
//				listitem.setAttribute("radiografer",radiograferList.getSelectedItem().getValue());
				if(item.getAttribute("doctor") instanceof MsStaff){
					treatmentTrx.setMsDoctor((MsStaff)item.getAttribute("doctor"));
				}
				if(item.getAttribute("radiografer") instanceof MsStaff){
					treatmentTrx.setAnastesiStaff((MsStaff)item.getAttribute("radiografer"));
				}

				treatmentTrx.setVWhoCreate(user.getVUserName());
				treatmentTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				treatmentTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				treatmentTrx.setNQty((short)1);
				treatmentTrx.setVDiscType(obj[2].toString());
				treatmentTrx.setVWhoCreate(user.getVUserName());
				treatmentTrx.setMsTreatmentFee((MsTreatmentFee)item.getValue());
				treatmentSet.add(treatmentTrx);
			}
			else if(item.getValue() instanceof Object[]){
				TbItemTrx itemTrx = new TbItemTrx();
				Object[] object = (Object[])item.getValue();
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)object[0]);
//				itemTrx.setMsItem(((MsItemSellingPrice)item.getValue()).getMsItem());
				itemTrx.setMsItem(msItem);
				itemTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				itemTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				itemTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
//				itemTrx.setNQty(((Double)obj[3]).floatValue());
				itemTrx.setNQty(new Float(obj[3].toString()));
				itemTrx.setVDiscType(obj[2].toString());
				itemTrx.setVWhoCreate(user.getVUserName());
				itemSet.add(itemTrx);
			}
			else if(item.getValue() instanceof TbMiscTrx){
				TbMiscTrx miscTrx = (TbMiscTrx)item.getValue();
				miscTrx.setVWhoCreate(user.getVUserName());
				miscTrx.setNAmountAfterDisc(((BigDecimal)obj[1]).doubleValue());
				miscTrx.setNDiscAmount(((BigDecimal)obj[4]).doubleValue());
				miscTrx.setVDiscType(obj[2].toString());
				miscTrx.setNAmountTrx(((BigDecimal)obj[0]).doubleValue());
				miscSet.add(miscTrx);
			}
		}
	}

	public static void setFont(Listbox theList) {
		Iterator iter1 = theList.getItems().iterator();
		while (iter1.hasNext()) {
			Listitem listitem = (Listitem) iter1.next();
			Iterator iter2 = listitem.getChildren().iterator();
			listitem.setStyle(MedisafeConstants.LIST_FONT);
			while (iter2.hasNext()) {
				Object obj = iter2.next();
				if (obj instanceof Listcell) {
					Listcell listcell = (Listcell) obj;
					listcell.setStyle(MedisafeConstants.LIST_FONT);
					
					Iterator iter3 = listcell.getChildren().iterator();
					while(iter3.hasNext()){
						Object objlagi = iter3.next();
						if(objlagi instanceof Intbox){
							Intbox ibox = (Intbox)objlagi;
							ibox.setStyle("background-color:"+MedisafeConstants.COLOR_INPUT+";width:95%;height:15px;font-weight:bold;font-size:8pt");
							
						}
						else if (objlagi instanceof Decimalbox) {
							Decimalbox dbox = (Decimalbox)objlagi;
							dbox.setStyle("background-color:"+MedisafeConstants.COLOR_INPUT+";height:16px;font-weight:bold;font-size:8pt");
						}
					}
				}				
			}
		}
	}

	
	public static void setFont(Tree tree) {
		
		Iterator iter1 = tree.getItems().iterator();
		while (iter1.hasNext()) {
			Treeitem treeitem = (Treeitem) iter1.next();
			Iterator iter2 = treeitem.getChildren().iterator();
			while (iter2.hasNext()) {
				Object obj = (Object) iter2.next();
				if (obj instanceof Treerow) {
					Treerow treerow = (Treerow) obj;
					Iterator iter3 = treerow.getChildren().iterator();
					while (iter3.hasNext()) {
						obj = iter3.next();
						if(obj instanceof Treecell){
							Treecell tc = (Treecell) obj;
							tc.setStyle(MedisafeConstants.LIST_FONT);
						}
					}
				}
			}
		}
	}
	
}

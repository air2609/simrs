package com.vone.medisafe.service.ifaceimpl;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;



import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.TreeHistoryPatientListener;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.CommonPatientHistoryManager;
import com.vone.medisafe.ui.common.HistoryMonthListener;
import com.vone.medisafe.ui.common.PatientHistoryController;
import com.vone.medisafe.mapping.dao.CommonHistoryDAO;

public class CommonPatientHistoryManagerImpl implements CommonPatientHistoryManager{
	
	private CommonHistoryDAO dao;

	public List getPatientNote(MsPatient patient, MsUnit unit) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.getPatientNote(patient, unit);
	}
	
	

	public CommonHistoryDAO getDao() {
		return dao;
	}

	public void setDao(CommonHistoryDAO dao) {
		this.dao = dao;
	}



	public List getTreatmentBaseOnNote(TbExamination note) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getPatientTreatment(note);
	}



	public List getItemBaseOnNote(TbExamination note) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getPatientItem(note);
	}



	public void getTreatmentData(PatientHistoryController controller) 
		throws VONEAppException, InterruptedException {
		
		
		Listheader header;
		Listitem item;
		Listcell cell;
		List list = null;
		
		controller.historyList.getChildren().clear();
		
		controller.mr = (TbMedicalRecord)Sessions.getCurrent().getAttribute("mr");
		if(controller.mr != null){
			if(controller.rgoption.getSelectedItem() != null){
				
				if(controller.rgoption.getSelectedItem().getId().equals("divisi")){
									
					list = this.getPatientNote(controller.mr.getMsPatient(), controller.unit);
					
				}else{
					
					list = this.getPatientNote(controller.mr.getMsPatient(), null);
				}
			}else{
				
				
				Messagebox.show(MessagesService.getKey("unit.or.global.not.selected"),"KONFIRMASI",
									Messagebox.OK,Messagebox.NONE);
				return;
				
			}
			
			
			if(controller.treeListPerawatan.getSelectedItem() != null){
				if(controller.treeListPerawatan.getSelectedItem().getId().equals("pemeriksaan")){
					controller.grandTotal.setVisible(false);
					controller.totalLabel.setVisible(false);
					
					
					Listhead head = new Listhead();
					head.setParent(controller.historyList);
					
					header = new Listheader("KETERANGAN");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("45%");
					header.setStyle("font-weight:bold;font-size:8pt");
					
					header = new Listheader("SUB DIVISI");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("32%");
					header.setStyle("font-weight:bold;font-size:8pt");
					
					header = new Listheader("TANGGAL");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("15%");
					header.setStyle("font-weight:bold;font-size:8pt");
							
					Iterator it = list.iterator();
							
					while(it.hasNext()){
						TbExamination exam = (TbExamination)it.next();
						List treatmentList = this.getTreatmentBaseOnNote(exam);
						Iterator itr = treatmentList.iterator();
						while(itr.hasNext()){
							TbTreatmentTrx treatTrx =(TbTreatmentTrx)itr.next();
							item = new Listitem();
							item.setParent(controller.historyList);
							item.setValue(treatTrx);
									
							cell = new Listcell(treatTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
							cell.setParent(item);
									
							cell = new Listcell(exam.getMsUnit().getVUnitName());
							cell.setParent(item);
									
							cell = new Listcell(MedisafeUtil.convertDateToString(treatTrx.getDWhnCreate()));
							cell.setParent(item);
						}
					}
				}
				else if(controller.treeListPerawatan.getSelectedItem().getId().equals("bahanMedis")){
					
					Listhead head = new Listhead();
					head.setParent(controller.historyList);
					
					header = new Listheader("KETERANGAN");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("35%");
					header.setStyle("font-weight:bold;font-size:8pt");
					
					header = new Listheader("SUB DIVISI");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("28%");
					header.setStyle("font-weight:bold;font-size:8pt");
					
					header = new Listheader("JUMLAH");
					header.setParent(head);
					header.setWidth("9%");
					header.setStyle("font-weight:bold;font-size:8pt");
					
					header = new Listheader("TANGGAL");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("13%");
					header.setStyle("font-weight:bold;font-size:8pt");
					
					Iterator it = list.iterator();
					
					while(it.hasNext()){
						TbExamination exam = (TbExamination)it.next();
						List treatmentList = this.getItemBaseOnNote(exam);
						Iterator itr = treatmentList.iterator();
						while(itr.hasNext()){
							TbItemTrx itemTrx =(TbItemTrx)itr.next();
							item = new Listitem();
							item.setParent(controller.historyList);
							item.setValue(itemTrx);
									
							cell = new Listcell(itemTrx.getMsItem().getVItemName());
							cell.setParent(item);
									
							cell = new Listcell(exam.getMsUnit().getVUnitName());
							cell.setParent(item);
							
							cell = new Listcell(""+itemTrx.getNQty());
							cell.setParent(item);
									
							cell = new Listcell(MedisafeUtil.convertDateToString(itemTrx.getDWhnCreate()));
							cell.setParent(item);
						}
					}
					
				}
				
				else if (controller.treeListPerawatan.getSelectedItem().getId().equals("rekap")){
					controller.grandTotal.setVisible(false);
					controller.totalLabel.setVisible(false);
					
					Listhead head = new Listhead();
					head.setParent(controller.historyList);
					
					header = new Listheader();
					header.setLabel("KETERANGAN");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("88%");
					header.setStyle("font-size:8pt;font-weight:bold");
					
					header = new Listheader();
					header.setLabel("JUMLAH");
					header.setParent(head);
					header.setSort("auto");
					header.setWidth("12%");
					header.setStyle("font-size:8pt;font-weight:bold");
					
					List treatList = null;
					List obatBm = null;
					
					if(controller.rgoption.getSelectedItem().getId().equals("divisi")){
						
						treatList = dao.getRekapTreatment(controller.mr.getMsPatient(), controller.unit);
						obatBm = dao.getRekapItem(controller.mr.getMsPatient(), controller.unit);
						
					}else{
						
						treatList = dao.getRekapTreatment(controller.mr.getMsPatient(), null);
						obatBm = dao.getRekapItem(controller.mr.getMsPatient(), null);
					}
					
						
					
					if(treatList.size() > 0){
						item = new Listitem();
						item.setParent(controller.historyList);
						
						cell = new Listcell("PEMERIKSAAN");
						cell.setStyle("font-size:8pt;font-weight:bold");
						cell.setParent(item);
						
						cell = new Listcell("");
						cell.setParent(item);
						
						item = new Listitem();
						item.setParent(controller.historyList);
						
						cell = new Listcell("-----------------");
						cell.setStyle("font-size:8pt;font-weight:bold");
						cell.setParent(item);
						
						cell = new Listcell("");
						cell.setParent(item);
						
						Iterator iter = treatList.iterator();
						while(iter.hasNext()){
							
							Object[] obj = (Object[])iter.next();
							item = new Listitem();
							item.setParent(controller.historyList);
							
							cell = new Listcell(obj[1].toString());
							cell.setParent(item);
							
							cell = new Listcell(obj[2].toString());
							cell.setParent(item);
							
						}
						
						item = new Listitem();
						item.setParent(controller.historyList);
						
						cell = new Listcell("");
						cell.setParent(item);
						
						cell = new Listcell("");
						cell.setParent(item);
					}
					
					if(obatBm.size() > 0){
						item = new Listitem();
						item.setParent(controller.historyList);
						
						cell = new Listcell("OBAT & BAHAM MEDIS");
						cell.setStyle("font-size:8pt;font-weight:bold");
						cell.setParent(item);
						
						cell = new Listcell("");
						cell.setParent(item);
						
						item = new Listitem();
						item.setParent(controller.historyList);
						
						cell = new Listcell("--------------------------");
						cell.setStyle("font-size:8pt;font-weight:bold");
						cell.setParent(item);
						
						cell = new Listcell("");
						cell.setParent(item);
						
						Iterator iter = obatBm.iterator();
						while(iter.hasNext()){
							
							Object[] obj = (Object[])iter.next();
							item = new Listitem();
							item.setParent(controller.historyList);
							
							cell = new Listcell(obj[1].toString());
							cell.setParent(item);
							
							cell = new Listcell(obj[2].toString());
							cell.setParent(item);
							
						}
					}	
				}
			}
			
		}
		
	}


	

	public void getOption(PatientHistoryController controller) throws VONEAppException, InterruptedException {
		
		
		if(controller.tboption.getSelectedTab().getId().equals("notaTab")){
			
			controller.grandTotal.setVisible(true);
			controller.totalLabel.setVisible(true);
			
			controller.historyList.getChildren().clear();
			
			Listhead head = new Listhead();
			head.setParent(controller.historyList);
			
			Listheader header = null;
			
			header = new Listheader("NO. NOTA");
			header.setParent(head);
			header.setSort("auto");
			header.setWidth("30%");
			header.setStyle("font-weight:bold;font-size:8pt");
						
			header = new Listheader("SUB DIVISI");
			header.setParent(head);
			header.setSort("auto");
			header.setWidth("40%");
			header.setStyle("font-weight:bold;font-size:8pt");
			
			header = new Listheader("NILAI TRANSAKSI");
			header.setParent(head);
			header.setWidth("20%");
			header.setStyle("font-weight:bold;font-size:8pt");
			
			List list = null;
			controller.treeListNota.getChildren().clear();
			controller.mr = (TbMedicalRecord)Sessions.getCurrent().getAttribute("mr");
			if(controller.mr == null){
				Messagebox.show(MessagesService.getKey("mr.not.found"));
				return;
			}
			if(controller.rgoption.getSelectedItem() != null){
				
				if(controller.rgoption.getSelectedItem().getId().equals("divisi")){
					list = this.getPatientNote(controller.mr.getMsPatient(), controller.unit);
				}else{
					list = Service.getPatientHistory().getPatientNote(controller.mr.getMsPatient(), null);
				}
			}
			else{
				Messagebox.show(MessagesService.getKey("unit.or.global.not.selected"));
				return;
			}
			String thntmp = "";
			String thn = "";
			String blntmp = "";
			String bln= "";
			String tgltmp ="";
			String tgl= "";
			
			Treeitem treeItem = null;
			Treeitem treeItem1 = null;
			Treeitem treeItem2 = null;
			
			Treerow treerow = null;
			Treerow treerow1 = null;
			Treerow treerow2 = null;
			
			Treecell tcell = null;
			Treecell tcell1 = null;
			Treecell tcell2 = null;
			
			Treechildren treeChil1 = null;
			Treechildren treeChil2 = null;
			
			Treechildren treeChil = new Treechildren();
			treeChil.setParent(controller.treeListNota);
									
			Iterator it = list.iterator();
			while(it.hasNext()){
				TbExamination exam = (TbExamination)it.next();
				thn = MedisafeUtil.getYearMonthDay(exam.getDWhnCreate(), MedisafeConstants.YEAR);
				bln = MedisafeUtil.getYearMonthDay(exam.getDWhnCreate(), MedisafeConstants.MONTH);
				tgl = MedisafeUtil.getYearMonthDay(exam.getDWhnCreate(), MedisafeConstants.DAY);
				

				if(!thn.equals(thntmp)){
					
					treeItem = new Treeitem();
					treeItem.setParent(treeChil);
					
					treerow = new Treerow();
					treerow.setParent(treeItem);
					
					tcell = new Treecell(thn);
					tcell.setStyle("font-size:8pt");
					tcell.setParent(treerow);
					thntmp = thn;
					
					treeChil1 = new Treechildren();
					treeChil1.setParent(treeItem);
					
					treeItem1 = new Treeitem();
					treeItem1.setParent(treeChil1);
					
					treerow1 = new Treerow();
					treerow1.setParent(treeItem1);
					
					Label lbl1 = new Label(MedisafeUtil.convertToMonthName(bln));
					lbl1.setStyle("font-size:8pt");
					tcell1 = new Treecell();
					tcell1.appendChild(lbl1);
					tcell1.setParent(treerow1);
					lbl1.addEventListener("onClick", new HistoryMonthListener(
																controller.historyList,list,bln,thn,controller.grandTotal));
					blntmp = bln;
					
					treeChil2 = new Treechildren();
					treeChil2.setParent(treeItem1);
					
					treeItem2 = new Treeitem();
					treeItem2.setParent(treeChil2);
					
					treerow2 = new Treerow();
					treerow2.setParent(treeItem2);
					
					Label lbl = new Label(tgl);
					lbl.setStyle("font-size:8pt");
					tcell2 = new Treecell();
					tcell2.appendChild(lbl);
					tcell2.setParent(treerow2);
					

					lbl.addEventListener("onClick", new TreeHistoryPatientListener(
													controller.historyList,list,tgl,bln,thn,controller.grandTotal));
					tgltmp = tgl;
					
					
				}
				else{
					if(!bln.equals(blntmp)){
					
						
						treeItem1 = new Treeitem();
						treeItem1.setParent(treeChil1);
						
						treerow1 = new Treerow();
						treerow1.setParent(treeItem1);
						
						Label lbl1 = new Label(MedisafeUtil.convertToMonthName(bln));
						lbl1.setStyle("font-size:8pt");
						tcell1 = new Treecell();
						tcell1.setParent(treerow1);
						tcell1.appendChild(lbl1);
						lbl1.addEventListener("onClick", new HistoryMonthListener(
																controller.historyList,list,bln,thn,controller.grandTotal));
						blntmp = bln;
						
					//	Messagebox.show("bulan "+bln +" tanggal : "+tgl);
						
						if(!tgl.equals(tgltmp)){
							
							treeChil2 = new Treechildren();
							treeChil2.setParent(treeItem1);
							
							treeItem2 = new Treeitem();
							treeItem2.setParent(treeChil2);
							
							treerow2 = new Treerow();
							treerow2.setParent(treeItem2);
							
							Label lbl = new Label(tgl);
							lbl.setStyle("font-size:8pt");
							tcell2 = new Treecell();
							tcell2.appendChild(lbl);
							tcell2.setParent(treerow2);
							lbl.addEventListener("onClick", new TreeHistoryPatientListener(
																controller.historyList,list,tgl,bln,thn,controller.grandTotal));
							tgltmp = tgl;
							
						}
					
												
					}
					else{
//						tcell1 = new Treecell(MedisafeUtil.convertToMonthName(bln));
//						tcell1.setParent(treerow1);
//						blntmp = bln;
						
						if(!tgl.equals(tgltmp)){
							
//							treeChil2 = new Treechildren();
//							treeChil2.setParent(treeItem1);
							
							treeItem2 = new Treeitem();
							treeItem2.setParent(treeChil2);
							
							treerow2 = new Treerow();
							treerow2.setParent(treeItem2);
							
							Label lbl = new Label(tgl);
							tcell2 = new Treecell();
							tcell2.appendChild(lbl);
							lbl.setStyle("font-size:8pt");
							tcell2.setParent(treerow2);
							lbl.addEventListener("onClick", new TreeHistoryPatientListener(
											controller.historyList,list,tgl,bln,thn,controller.grandTotal));
							tgltmp = tgl;
							
						}
					}
									
				}
				thntmp = thn;
				
			}
			
			
			
		}else{
			
			controller.treeListPerawatan.clearSelection();
			controller.grandTotal.setVisible(false);
			controller.totalLabel.setVisible(false);
			
		}

	}

}

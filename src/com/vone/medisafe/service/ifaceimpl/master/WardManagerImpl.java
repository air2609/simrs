package com.vone.medisafe.service.ifaceimpl.master;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsWard;
import com.vone.medisafe.mapping.MsWardDAO;
import com.vone.medisafe.service.iface.master.WardManager;

public class WardManagerImpl implements WardManager{
	
	private MsWardDAO dao;

	public MsWardDAO getDao() {
		return dao;
	}

	public void setDao(MsWardDAO dao) {
		this.dao = dao;
	}

	public void save(MsWard ward) {
		// TODO Auto-generated method stub
		this.dao.save(ward);
	}

	public MsWard findById(Integer id) {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public void delete(MsWard ward) {
		// TODO Auto-generated method stub
		this.dao.delete(ward);
	}

	public List getAllWard() {
		// TODO Auto-generated method stub
		return this.dao.getAllWard(MsWard.class);
	}

	public List getWardByCriteria(MsWard ward) {
		// TODO Auto-generated method stub
		return null;
	}

	public MsWard getWardByCode(String code) {
		// TODO Auto-generated method stub
		return dao.getWardByCode(code);
	}

	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}

	public List<MsWard> ambilSemuaDataWard() throws VONEAppException{
		// TODO Auto-generated method stub
		
		return null;
//		return dao.ambilSemuaDataWard();
	}

	
	
	public void test(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		System.out.println("MULAI" +sdf.format(new Date()));
		
//		List<MsWard> wardList = null;
//		try {
//			wardList = dao.ambilSemuaDataWard();
//		} catch (VONEAppException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//				
//		System.out.println("BANYAKNYA DATA : "+wardList.size());
//		
//		for(MsWard ward : wardList){
//			System.out.println(ward.getVWardName());
//			System.out.println("\t");
//			Set<MsHall> setHall = ward.getMsHalls();
//			
//			for(MsHall hall : setHall){
//				System.out.println(hall.getVHallName());
//			}
//		}
//		
//		System.out.println("SELESAI :" +sdf.format(new Date()));
	}
	

}

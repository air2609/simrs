package com.vone.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vone.medisafe.common.util.MedisafeUtil;

public class TestTanggal {
	
	public static void main(String[] args) {
		String tgl = "30/12/2011";
		Date sekarang = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			Date awal = sdf.parse(tgl);
			
			List<Date> tgls = MedisafeUtil.getDateCollectionBetween2Date(awal, sekarang);
			System.out.println("banyaknya data : "+tgls.size());
			for(Date tanggal : tgls){
				System.out.println(sdf.format(tanggal));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

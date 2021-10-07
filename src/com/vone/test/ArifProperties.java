package com.vone.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vone.medisafe.common.exception.ScreenImageURLException;
import com.vone.medisafe.ui.component.util.ScreenImageURL;

public class ArifProperties {
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date tanggal = new Date();
		
		tanggal.setDate(tanggal.getDate()+1);
		System.out.println(tanggal.toLocaleString());
		System.out.println(sdf.format(tanggal));
	}

}

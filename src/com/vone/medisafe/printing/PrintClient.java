package com.vone.medisafe.printing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vone.medisafe.common.util.MedisafeConstants;

public class PrintClient {
	
	private static final String DASH = "-";
	private static final String SPACE = " ";

	public static void main(String[] arg){
//		System.out.println(padRight(1234567112, 20));
//		StringBuffer sb = new StringBuffer();
//		String abc = "abcdefghi";
//		System.out.println(abc.substring(3));
//		System.out.println(abc.substring(0,3));
////		sb.append("test 2\n");
////		sb.append("test 3\n");
//		//Character c = new Character();
//		
//		sb.append(CHAR_CONDENSED);
//		for (int i = 0; i < 10; i++) {
//			sb.append("baris: " + i +"\n");
//		}
//		sb.append(CHAR_NORMAL);
//		sb.append("normal");
//		sb.append("\n");
//		sb.append("\n");
//		sb.append(repl("-",50));sb.append("\n");
//		sb.append(padLeft("Kode", 30));
//		sb.append(padRight("10000", 20));
//		sb.append("\n");
//		sb.append(repl("-",50));sb.append("\n");
//		sb.append("TGL: " + getDate(new Date()));
//		PrintClient.printStringBuffer("192.168.1.234",sb);
	}
	
	public static boolean printStringBuffer(String ipServer, StringBuffer sb){
		boolean result = false;
		try {
			Socket socket = new Socket(ipServer, PrintServer.PORT);
			
			BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			outputWriter.write(sb.toString());
			outputWriter.close();
					
			socket.close();
			result = true;
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean printStringBuffer(String ipServer, StringBuffer sb, Short port){
		boolean result = false;
		try {
			Socket socket = new Socket(ipServer, port);
			
			BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			outputWriter.write(sb.toString());
			outputWriter.close();
					
			socket.close();
			result = true;
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public static String repl(String s, int n){
		StringBuffer sbf = new StringBuffer();
		if(s == null)
			s = DASH;
		for (int i = 0; i < n; i++) {
			sbf.append(s);
		}
		return sbf.toString();
	}
	
	public static String padLeft(String s, int n){
		if(s == null)
			s = DASH;
		String result = s + repl(SPACE,100);
		result = result.substring(0,n);
		return result;
	}
	
	public static String padLeft(double d, int n){
		NumberFormat formatter = new DecimalFormat(MedisafeConstants.CURRENCY_FORMAT);
		String s = formatter.format(d);
		return padLeft(s, n);
	}
	
	public static String padRight(String s, int n){
		String result = repl(SPACE,100) + s;
		result = result.substring(result.length() - n);
		return result;
	}
	
	public static String padRight(double d, int n){
		return padRight(formatDouble(d), n);
	}
	
	public static String formatDouble(double d){
		NumberFormat formatter = new DecimalFormat(MedisafeConstants.CURRENCY_FORMAT);
		String s = formatter.format(d);
		return s;
	}
	
	public static String getDate(Date date, String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		if(date != null)
			return formatter.format(date);
		else
			return DASH;
	}

}
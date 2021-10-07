package com.vone.medisafe.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;


public class MedisafeUtil {
	
	static Logger logger = Logger.getLogger(MedisafeUtil.class);
	
	/**
	 * purpose : convert input of integer to MR Nubmer
	 * ex  input: 1 will be converted into 00-00-01,
	 * 100 will be converted into 00-01-00, 
	 * 111111 will be converted into 11-11-11
	 * @param id
	 * @return MRNumber/MRCode
	 * @author Arifullah Ibn Rusyd Al-Bantani
	 */

	public static String convertToMrCode(Integer id){
		String strId = null;
		if(id.toString().length() == 1){
			strId = "00-00-0"+id.toString();
		}
		else if(id.toString().length() == 2){
			strId = "00-00-"+id.toString();
		}
		else if(id.toString().length() == 3){
			strId = "00-0"+id.toString().substring(0,1)+"-"+id.toString().substring(1,3);
		}
		else if(id.toString().length() == 4){
			strId = "00-"+id.toString().substring(0,2)+"-"+id.toString().substring(2,4);
		}
		else if(id.toString().length() == 5){
			strId = "0"+id.toString().substring(0,1)+"-"+id.toString().substring(1,3)+"-"+id.toString().substring(3,5);
		}
		else{
			strId = id.toString().substring(0,2)+"-"+id.toString().substring(2,4)+"-"+id.toString().substring(4,6);
		}
		return strId;
	}
	
	
	
	/**
	 * purpose : convert raw MR Code into standarized MR Code. 
	 * ex. Input = 111213, Output will be 11-12-13
	 * Input = 11-12-13 will be returned intact
	 * @param stInput
	 * @return null if error 
	 * @author James Pang
	 */
	public static String convertToMrCode(String stInput){

		if (StringUtils.hasValue(stInput) && (stInput.length() == 6 || stInput.length() == 8)){
			try{

				for (int i=0; i<stInput.length(); i++){

					if (stInput.length() == 8 && (i == 2 || i == 5)) 
						continue;
					
					if (stInput.charAt(i) < '0' || stInput.charAt(i) > '9')
						return null;
					}
				
				if (stInput.length() == 6)
					return convertToMrCode(new Integer(stInput));
				else
					return stInput;
			}catch (Exception e){
				logger.error("Convert Error! Not a valid input");
				
				return null;
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * purpose convert parameter into Rajal Registration Number
	 * ex input : number=2, date = 22/10/2006, unitCode=PAN
	 * will be converted into J-PAN-20061022-002
	 * @param number
	 * @param date
	 * @param unitCode
	 * @return registtraionCode
	 * @author Arifullah Ibn Rusyd Al-Bantani
	 */
	
	public static String convertToRegistrationCode(Integer number, Date date, String unitCode)
	{
		String registrationCode = null;
		String tanggal = new SimpleDateFormat("yyyyMMdd").format(date);
		registrationCode = "J-"+unitCode+"-"+tanggal+"-"+convertToRegistrationNumber(number);
		
		return registrationCode;
	}
	
	
	/**
	 * purpose convert parameter into Ranap Registration Number
	 * ex input : number=11, date = 22/10/2006, unitCode=PAN
	 * will be converted into I-PAN-20061022-011
	 * @param number
	 * @param date
	 * @param unitCode
	 * @return registtraionCode
	 * @author Arifullah Ibn Rusyd Al-Bantani
	 */
	
	public static String convertToRanapCode(Integer number, Date date, String unitCode)
	{
		String registrationCode = null;
		String tanggal = new SimpleDateFormat("yyyyMMdd").format(date);
		registrationCode = "I-"+tanggal+"-"+convertToRegistrationNumber(number);
		
		return registrationCode;
	}
	
	public static String convertDateToString(Date date){
		if (date == null)
			return "";
		
		String dt = new SimpleDateFormat(MedisafeConstants.DATE_PATTERN).format(date);
		return dt;
	}	
	
	public static String convertDateToString(Date date, String format){
		String dt = new SimpleDateFormat(format).format(date);
		return dt;
	}		
	
	public static Date convertTimeToDate(String time){
		Date dt;
		try {
			dt = new SimpleDateFormat(MedisafeConstants.TIME_PATTERN).parse(time);
		} catch (ParseException e) {
			logger.error("ConvertTimeToDate : PARSE ERROR!!");
			return null;
		}
		
		return dt;
	}
	
	public static String convertDateToTime(Date date){
		String dt = new SimpleDateFormat(MedisafeConstants.TIME_PATTERN).format(date);
		return dt;
	}	
	
	public static String convertToRegistrationNumber(Integer id){
		String strId = null;
		
		if(id.toString().length() == 1){
			strId = "00"+id.toString();
		}
		else if(id.toString().length() == 2){
			strId = "0"+id.toString();
		}
		else if(id.toString().length() == 3){
			strId = id.toString();
		}
		
		
		return strId;
	}
	
	public static String convertToNotaNumber(Integer id){
		String strId = null;
		
		if(id.toString().length() == 1){
			strId = "00000"+id.toString();
		}
		else if(id.toString().length() == 2){
			strId = "0000"+id.toString();
		}
		else if(id.toString().length() == 3){
			strId = "000" +id.toString();
		}
		else if(id.toString().length() == 4){
			strId = "00"+ id.toString();
		}
		else if(id.toString().length() == 5){
			strId = "0"+ id.toString();
		}
		else if(id.toString().length() == 6){
			strId = id.toString();
		}
		
		return strId;
	}
	
	
	public static String generateNotaNumber(Integer notaNumber, Date tanggal, String unitCode, int type){
		String nota = null;
		if(type == MedisafeConstants.RANAP)nota = "I-";
		else if(type == MedisafeConstants.NON_RANAP)nota = "J-";
		else nota="R-";
		
		String tgl = new SimpleDateFormat("yyMM").format(tanggal);
		nota = nota+unitCode+"-"+tgl+"-"+convertToNotaNumber(notaNumber);
		
		return nota;
	}
	
	//kun: buat nomor hasil lab
	public static String generateNoHLab(Integer notaNumber, Date tanggal, String unitCode){
		String nota = null;
		nota = "H";
		
		String tgl = new SimpleDateFormat("yyMM").format(tanggal);
		nota = nota+unitCode+"-"+tgl+"-"+convertToNotaNumber(notaNumber);
		
		return nota;
	}
	
	public static String generateNoRequest(Integer notaNumber, Date tanggal){
		String nota = null;
		NumberFormat ourForm = new DecimalFormat("000");
		
		String tgl = new SimpleDateFormat("yyMMdd").format(tanggal);
		nota = tgl+"-"+ourForm.format(notaNumber);
		
		return nota;
	}
	
	/**
	 * 
	 * @param cal
	 * @param cal2
	 * @return int array {thn, bln, hari}
	 */
	public static int[] getDayDifferent(Calendar cal, Calendar cal2){
		
		int month2 = cal2.get(Calendar.MONTH);
		int month = cal.get(Calendar.MONTH);
		
		int selisihTahun = 0;
		int hariSkrg = cal.get(Calendar.DAY_OF_MONTH);
		int selisihBulan = 0;
		int selisihHari = 0;
		if(month > month2){
			selisihTahun = cal.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
						
			if(cal2.get(Calendar.DAY_OF_MONTH) > hariSkrg){
				hariSkrg = hariSkrg + getDayOfMonth((month-1),cal.get(Calendar.YEAR));
				month = month - 1;
			}
			selisihBulan = month - month2;
			selisihHari = hariSkrg - cal2.get(Calendar.DAY_OF_MONTH);
			
		}else if(month2 > month){
			int thnSkrg = cal.get(Calendar.YEAR) - 1;
			month = month + 12;
			
			if(cal2.get(Calendar.DAY_OF_MONTH) > hariSkrg){
				hariSkrg = hariSkrg + getDayOfMonth((cal.get(Calendar.MONTH)-1),cal.get(Calendar.YEAR));
				month = month - 1;
			}
			selisihTahun = thnSkrg - cal2.get(Calendar.YEAR);
			selisihBulan = month - month2;
			selisihHari = hariSkrg - cal2.get(Calendar.DAY_OF_MONTH);
			
		}
		else{
			int thnSkrg = cal.get(Calendar.YEAR);
			
			if(cal2.get(Calendar.DAY_OF_MONTH) > hariSkrg){
				hariSkrg = hariSkrg + getDayOfMonth((cal.get(Calendar.MONTH)-1),cal.get(Calendar.YEAR));
				month = month + 11;
				thnSkrg = thnSkrg - 1;
			}
			selisihTahun = thnSkrg - cal2.get(Calendar.YEAR);
			selisihBulan = month - month2;
			selisihHari = hariSkrg - cal2.get(Calendar.DAY_OF_MONTH);
		}
		int[] tahun = new int[]{selisihTahun,selisihBulan,selisihHari};
		
		return tahun;
	}
	
	public static int getDayOfMonth(int month, int year){
		int days = 0;
		switch(month){
			case 0:
				days = 31;
				break;
			case 1:
				if(year % 4 == 0) days = 29;
				else days = 28;
				break;
			case 2:
				days = 31;
				break;
			case 3:
				days = 30;
				break;
			case 4:
				days = 31;
				break;
			case 5:
				days = 30;
				break;
			case 6:
				days = 31;
				break;
			case 7:
				days = 31;
				break;
			case 8:
				days = 30;
				break;
			case 9:
				days = 31;
				break;
			case 10:
				days = 30;
				break;
			case 11:
				days = 31;
				break;
			}
		
		return days;
	}
	
	public static String convertToMonthName(String month){
		String days = null;
		if(month.equals("01")) days = "JANUARI";
		else if(month.equals("02"))days = "FEBRUARI";
		else if(month.equals("03"))days = "MARET";
		else if(month.equals("04"))days = "APRIL";
		else if(month.equals("05"))days = "MEI";
		else if(month.equals("06"))days = "JUNI";
		else if(month.equals("07"))days = "JULI";
		else if(month.equals("08"))days = "AGUSTUS";
		else if(month.equals("09"))days = "SEPTEMBER";
		else if(month.equals("10"))days = "OKTOBER";
		else if(month.equals("11"))days = "NOVEMBER";
		else days = "DESEMBER";
		return days;
	}
	
	public static String getYearMonthDay(Date tanggal, String type){
		String result = null;
		String format = null;
		if(type.equals(MedisafeConstants.YEAR)) format = MedisafeConstants.YEAR;
		else if(type.equals(MedisafeConstants.MONTH)) format = MedisafeConstants.MONTH;
		else format = MedisafeConstants.DAY;
		
		result = new SimpleDateFormat(format).format(tanggal);
		return result;
	}
	
	public static void generateAge(Textbox umur, Datebox tglLahir){
		Date tglSkrg = new Date();
		Calendar calSkrg = Calendar.getInstance();
		Calendar calLahir = Calendar.getInstance();
		calSkrg.setTime(tglSkrg);
		
		if(tglLahir.getText().trim().length() > 1)
		{
			calLahir.setTime(tglLahir.getValue());
			int[] umurSkrg = MedisafeUtil.getDayDifferent(calSkrg,calLahir);
			umur.setValue(umurSkrg[0]+" thn "+umurSkrg[1]+" bln "+ umurSkrg[2]+" hr");
		}
		else{
			int umr = 0;
			try {
				umr = Integer.parseInt(umur.getValue());
			} catch (WrongValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				try {
					Messagebox.show(MessagesService.getKey("input.bukan.angka"));
					umur.setValue(null);
					return;
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			int thn = calSkrg.get(Calendar.YEAR);
			int tahunLahir = thn - umr;
			calLahir.set(tahunLahir,calSkrg.get(Calendar.MONTH),calSkrg.get(Calendar.DAY_OF_MONTH));
			tglLahir.setValue(calLahir.getTime());
		}
	
	}
	
	public static int[] calculateAge(Date dob){
		Calendar calSkrg = Calendar.getInstance();
		Calendar calLahir = Calendar.getInstance();
		calSkrg.setTime(new Date());
		calLahir.setTime(dob);
		
		return getDayDifferent(calSkrg, calLahir);
	}
	
	
	public static int[] getDateDifferent(Date tanggalAwal, Date tanggalAkhir){
		Calendar calAwal = Calendar.getInstance();
		Calendar calAkhir = Calendar.getInstance();
		
		calAwal.setTime(tanggalAwal);
		calAkhir.setTime(tanggalAkhir);
		
		return getDayDifferent(calAwal, calAkhir);
	}
	
	
	public static String convertAgeToString(int[] age){
		String strAge = age[0]+" thn "+age[1]+" bln " + age[2]+" hr"; 
		return strAge;
	}
	
	//give you Listbox of Rp or %
	public static void setListPaymentOptions(Listbox box){
		box.getItems().clear();
		box.setMold("select");
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.RP);
		item.setLabel("1.Rp");
		item.setParent(box);
		item = new Listitem();
		item.setValue(MedisafeConstants.PERCENT);
		item.setLabel("2.%");
		item.setParent(box);
	}
	
	
	public static String getNoteStatus(int status){
		String noteStatus = null;
		if(status == MedisafeConstants.ACTIVE_NOTE) noteStatus ="BARU";
		else if(status == MedisafeConstants.CANCEL_NOTE) noteStatus="BATAL";
		else if(status == MedisafeConstants.VALIDATED_NOTE)noteStatus="SUDAH DIVALIDASI";
		else if(status == MedisafeConstants.VALIDATED_CANCEL)noteStatus="SUDAH DIVALIDASI TAPI DIBATALKAN";
		else noteStatus="TIDAK AKTIF";
		return noteStatus;
	}
	
	/**
	 * purpose convert time different in same date into string
	 * ex input : 22/01/2006 17:22:00 and 22/01/2006 17:32:00
	 * will be converted into 0:10:0 (10 minutes)
	 * @param jamBesar
	 * @param jamKecil
	 * @return selisihJam
	 * @author Arifullah Ibn Rusyd Al-Bantani
	 */
	
	public static String calculateTimeDifferent(String jamBesar, String jamKecil){
		String[] jam1 = jamKecil.split(":");
		String[] jam2 = jamBesar.split(":");
		
		int hour2 = Integer.parseInt(jam2[0]);
		int minute2 = Integer.parseInt(jam2[1]);
		int second2 = Integer.parseInt(jam2[2]);
		
		int hour = Integer.parseInt(jam1[0]);
		int minute = Integer.parseInt(jam1[1]);
		int second = Integer.parseInt(jam1[2]);
		
		int detikBaru = 0;
		int menitBaru = 0;
		int jamBaru = 0;
		
		if(second > second2){
			second2 = second2 + 60;
			minute2 = minute2 - 1;
			detikBaru = second2 - second;
		}else{
			detikBaru = second2 - second;
		}
		
		
		if(minute > minute2){
			minute2 = minute2 + 60;
			hour2 = hour2 - 1;
			menitBaru = minute2 - minute;
		}else{
			menitBaru = minute2 - minute;
		}
		
		jamBaru = hour2 - hour;
		
		String selisihJam = jamBaru+":"+menitBaru+":"+detikBaru;
		
		
		return selisihJam;
	}
	
	
	
	/**
	 * purpose : convert drug type from short into string
	 * type = 1 will be converted into PSIKOTROPIKA
	 * type = 2 will be converted into NARKOTIKA
	 * type = 3 will be converted into GENERIK
	 * @param type
	 * @return result
	 * @author Arifullah Ibn Rusyd Al-Bantani 
	 * @since Nov, 16 2006
	 */
	public static String convertIntoDrugsType(short type){
		String result = null;
		if(type == MedisafeConstants.PSIKOTROPIKA){
			result = "PSIKOTROPIKA";
		}
		else if(type == MedisafeConstants.NARKOTIKA){
			result = "NARKOTIKA";
		}
		else if(type == MedisafeConstants.PATEN){
			result = "PATEN";
		}
		else if(type == MedisafeConstants.GENERIK){
			result = "GENERIK";
		}
		else result = "GENERIK";
		
		return result;
	}
	
	public static String convert2CardType(Short type){
		if(type.shortValue() == 1) return "KARTU KREDIT";
		else return "KARTU DEBET";
	}
	
	
	/**
	 * purpose : get date object collection between two date
	 * ex input : 22/01/2006 and 30/01/2006
	 * output : a collection of date object from 23/01/2006 to 29/01/2006
	 * @param comein
	 * @param today
	 * @return dateCollection
	 * @author Arifullah Ibn Rusyd Al-Bantani 
	 * @since Nov, 30 2006
	 */
	
	public static List<Date> getDateCollectionBetween2Date(Date comein, Date today){
		List<Date> dateCollection = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		cal.setTime(comein);
		cal2.setTime(today);
		
		int tanggalLama = cal.get(Calendar.DAY_OF_YEAR);
		int tanggalHariIni = cal2.get(Calendar.DAY_OF_YEAR);
		
		if(cal2.get(Calendar.YEAR) > cal.get(Calendar.YEAR)){
			if(cal.get(Calendar.YEAR)%4 == 0)
				tanggalHariIni = tanggalHariIni + 366;
			else tanggalHariIni = tanggalHariIni + 365;
		}
			
		
		for(int i=1; i < (tanggalHariIni - tanggalLama); i++){
			cal.add(Calendar.DAY_OF_MONTH, 1);
			dateCollection.add(cal.getTime());
		}
		
		return dateCollection;
	}
	
	public static String convert2PaymentStatus(short status){
		if(status == MedisafeConstants.BELUM_LUNAS)return "BELUM LUNAS";
		else return "SUDAH LUNAS";
	}
	
	public static String convert2BillCode(Date tanggal, Integer id){
		SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
		return "KPJ-"+sdf.format(tanggal)+"-"+convertToBillNumber(id);
	}
	
	public static String convertToBillNumber(Integer id){
		String strId = null;
		
		if(id.toString().length() == 1){
			strId = "0000"+id.toString();
		}
		else if(id.toString().length() == 2){
			strId = "000"+id.toString();
		}
		else if(id.toString().length() == 3){
			strId = "00" +id.toString();
		}
		else if(id.toString().length() == 4){
			strId = "0"+ id.toString();
		}
		else if(id.toString().length() == 5){
			strId = id.toString();
		}
		
		return strId;
	}
		
    /**
     * this method will create a string of sequence Alphabet by reference of count
     * eg. if count = 0 then this method would return A
     * eg. if count = 27 then this method would return AA
     * eg. if count = 28 then this method would return AB
     * eg. if count = 18277 then this method would return ZZZ
     * using Character class, and forDigit() method. 'a' starts from digit of 10
     * @author James Pang
     * @param count
     * @return
     */
    public static String getSequenceString(int count){
        String stRes = "";

        int intDiv = count / 26;
        int intMod = count % 26;
        int intDiv2 = intDiv;

        while (intDiv2 > 26){
            stRes += getSequenceString(--intDiv2);
            intDiv2 = intDiv2 / 26;
        }

        if ((intDiv > 0) && (intDiv < 27))
            stRes += Character.forDigit(intDiv+9,Character.MAX_RADIX);

        stRes += Character.forDigit(intMod+10,Character.MAX_RADIX);

        return stRes.toUpperCase();
    }



	public static String generateVoucherNo() throws VONEAppException {
		// todo generateVoucherNo
		String nota = null;
		nota = "AP-PAYMENT";
		Integer noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.VOUCHER_NO);
		
//		String tgl = new SimpleDateFormat("yyMM").format(date);
		nota = nota+"-"+convertToNotaNumber(noNota);
		
		return nota;
	}
	
}

package com.vone.medisafe.common.util;

public interface MedisafeConstants {
	
	//COMMON
	public static final int RESULT_PER_PAGE = 10;
	public static final String TIME_PATTERN = "HH:mm";
	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String COMMON_DISCOUNT_ATTRIBUTE = "DISC_ATTR";
	
	//COLOR SCHEME
	public static final String COLOR_INPUT = "#82FFA8";
	
	//staff input
	public static final short STAFF = 1;
	public static final short DOKTER = 2;
	
	//MEDIC STAFF GROUP
	public static final short GRUP_DOKTER = 4;
	public static final short GRUP_ANASTESI = 5;
	public static final short GRUP_RADIOGRAFER = 10;
	
		
	//drugs type
	public static final short PSIKOTROPIKA = 1;
	public static final short NARKOTIKA = 2;
	public static final short GENERIK = 3;
	public static final short PATEN = 4;
	public static final short LAIN_LAIN = 5;
	
	//list kosong
	public static final String LISTKOSONG = "kosong";
	public static final String LABELKOSONG="";
	
	//bed status
	public static final String BED_TERPAKAI = "1";
	public static final String BED_KOSONG = "0";
	public static final String BED_ACTIVE = "A";
	
	//sequence
	public static final String PATIENT_ID = "ms_patient_n_patient_id_seq";
	public static final String MR_NUMBER = "tb_medical_record_n_mr_id_seq";
	public static final String REGISTRATION_NUMBER = "registration_number";
	public static final String REGISTRATION_ID = "tb_registration_n_reg_id_seq";
	public static final String ROOM_RSV_ID = "tb_room_reservation_n_room_rsv_id_seq";
	public static final String RANAP_NUMBER = "ranap_number";
	public static final String STAFF_ID = "ms_staff_n_staff_id_seq";
	public static final String PURCHASE_ORDER_REQUEST_CODE = "sq_purchase_order_req_code";
	public static final String PURCHASE_ORDER_CODE = "sq_purchase_order_code";
	public static final String NOTA_RANAP="nota_ranap_seq";
	public static final String NOTA_RAJAL="nota_rajal_seq";
	public static final String NOTA_HALAB="nota_halab_seq";
	public static final String NOTA_REQUEST="nota_request_seq";
	public static final String RACIKAN_ID="tb_drug_ingredients_n_dingr_id_seq";
	public static final String SQ_JOURNAL_TRX="sq_journal_trx";
	public static final String NOTA_RETUR="returseq";
	public static final String SEQ_PBILL ="kwintansi_seq";
	public static final String HASIL_LAB="tb_laboratory_result_n_lab_rslt_id_seq";
	public static final String HASIL_LAB_DETAIL="tb_laboratory_result_detail_n_lab_rslt_det_id_seq";
	public static final String VOUCHER_NO="voucher_no_seq";
	public static final String MISC_ID = "tb_misc_trx_n_misc_trx_id_seq";

	
	// rawat jalan input
	public static final int PASIEN_BARU = 3;
	public static final int PASIEN_LAMA = 4;
	public static final int CANCEL = 5;
	
	//ranap input
	public static final int INPUT_BY_SEARCH = 6;
	public static final int INPUT_BY_MANUAL = 7;
	
	//STATUS MR
	public static final String SEDANG_DIPINJAM = "8";
	public static final String AKAN_DIPINJAM = "9";
	public static final String TERSEDIA = "10";
	public static final int MR_NOT_READY = 0;
	public static final int MR_READY = 1;
	
	public static final String MR_OUT = "MR_OUT";
	public static final String MR_BACK = "MR_BACK";
	public static final String MR_CANCEL = "MR_CANCEL";
	
	//COA ACCOUNT NUMBER TYPE
	public static final int COA_ALL = 0;
	public static final int SELLING_ACCOUNT = 1;
	public static final int COGS_ACCOUNT = 5;
	public static final int INVENTORY_ACCOUNT = 9;
	
	//ITEM MEDICINE DESCRIPTION
	public static final String BISA_DIRETUR = "BISA DIRETUR";

	//INSURANCE MASTER
	public static final short INSURANCE_ACTIVE = 1;	
	public static final short INSURANCE_INACTIVE = 0;
	
	//PURCHASING STATUS
	public static final String PURCHASING_OPEN = "OPEN";	
	public static final String PURCHASING_BACKORDER = "BACK-ORDER";
	public static final String PURCHASING_REVOKED = "REVOKED";
	public static final String PURCHASING_APPROVED = "APPROVED";
	public static final String PURCHASING_CLOSED = "CLOSED";	
	public static final String PURCHASING_SESSION = "PCH_CONTROLLER";	
	public static final String PURCHASING_ORDER_SESSION = "PO_SESSION";
	public static final String DO_SESSION = "DO_CONTROLLER";
	
	//MASTER
	public static final String ITEM_INVENTORY_SESSION = "ITEM_INVENTORY_SESSION";
	public static final String COA_CMP_SESSION = "COA_CMP_SESSION";
	public static final String COA_RADIOBOX_SESSION = "COA_RADIOBOX_SESSION";
	
	//TREATMENT TYPE
	public static final int RANAP = 1;
	public static final int NON_RANAP = 2;
	
	public static final int RETUR_ITEM = 3;
	
	//NOTE STATUS
	public static final int ACTIVE_NOTE = 1;
	public static final int CANCEL_NOTE = 0;
	public static final int VALIDATED_NOTE = 2;
	public static final int CLOSE_NOTE = 3;
	public static final int VALIDATED_CANCEL = 4;
	
	//TRANSACTION
	public static final String RP = "RP";
	public static final String PERCENT = "%";
	
	public static final String YEAR="yyyy";
	public static final String MONTH="MM";
	public static final String DAY="dd";
	
	
	public static final int NOTA = 1;
	public static final int RESEP = 2;
	
	public static final String PATIENT_HISTROY="history";
	
	public static final String COMMON_POLY = "POLY";
	public static final String COMMON_UGD = "UGD";
	public static final String COMMON_BANGSAL="BANGSAL";
	public static final String COMMON_VK="VK";
	public static final String COMMON_RADIOLOGY="RADIOLOGY";
	public static final String COMMON_LABORAT="LABORAT";
	public static final String COMMON_SURGERY="SURGERY";
	public static final String COMMON_RENAL = "RENAL";
	public static final Object COMMON_FISIOTERAPI = "FISIOTERAPI";

	
	//TEATMENT GROUP
	public static final String PAKET = "PAKET";
	public static final String NON_PAKET = "NON-PAKET";
	
	//REGISTRATION
	public static final String MAIN_DOCTOR="Y";
	public static final String NO_DOCTOR="N";
	
	//TREATMENT CLASS
	public static final String DEFAULT_TCLASS = "KELAS II";

	
	//ACCOUNTING
	
	//COMMON
	public static final int ACCT_DEBIT = 1;
	public static final int ACCT_CREDIT = 2;
	
	public static final int ACCT_AP = 1;
	public static final int ACCT_AR = 2;
	public static final int ACCT_GJ = 8;
	
	public static final String ACCT_AP_STR = "AP";
	public static final String ACCT_AR_STR = "AR";
	public static final String ACCT_GJ_STR = "GJ";
	public static final String ACCT_PMT_STR = "PM";
	public static final String ACCT_DEPOSIT_STR = "DP";
	
	//COA
	public static final short COA_ACTIVE = 1;
	public static final short COA_INACTIVE = 2;
	public static final String COA_ACTIVE_STR = "ACTIVE";
	public static final String COA_INACTIVE_STR = "INACTIVE";
	
	public static final int COA_INCOME = 1;
	public static final int COA_OTHER_INCOME = 2;
	public static final int COA_EXPENSE = 3;
	public static final int COA_OTHER_EXPENSE = 4;
	public static final int COA_COST_OF_GOODS_SOLD = 5;
	public static final int COA_CASH = 6;
	public static final int COA_BANK = 7;
	public static final int COA_OTHER_CURRENT_ASSETS = 8;
	public static final int COA_INVENTORY_ASSETS = 9;
	public static final int COA_OTHER_ASSETS = 10;
	public static final int COA_FIXED_ASSETS = 11;
	public static final int COA_CREDIT_CARD = 12;
	public static final int COA_CURRENT_LIABILITY = 13;
	public static final int COA_LONG_TERM_LIABILITY = 14;
	public static final int COA_EQUITY = 15;
	
	public static final String COA_INCOME_STR = "INCOME";
	public static final String COA_OTHER_INCOME_STR = "OTHER INCOME";
	public static final String COA_EXPENSE_STR = "EXPENSE";
	public static final String COA_OTHER_EXPENSE_STR = "OTHER EXPENSE";
	public static final String COA_COST_OF_GOODS_SOLD_STR = "COST OF GOODS SOLD";
	public static final String COA_CASH_STR = "CASH";
	public static final String COA_BANK_STR = "BANK";
	public static final String COA_OTHER_CURRENT_ASSETS_STR = "OTHER CURRENT ASSETS";
	public static final String COA_INVENTORY_ASSETS_STR = "INVENTORY ASSETS";
	public static final String COA_OTHER_ASSETS_STR = "OTHER ASSETS";
	public static final String COA_FIXED_ASSETS_STR = "FIXED ASSETS";
	public static final String COA_CREDIT_CARD_STR = "CREDIT CARD";
	public static final String COA_CURRENT_LIABILITY_STR = "CURRENT LIABILITY";
	public static final String COA_LONG_TERM_LIABILITY_STR = "LONG TERM LIABILITY";
	public static final String COA_EQUITY_STR = "EQUITY";
	
	//registration status
	public static final int REG_ACTIVE = 1;
	public static final int REG_NON_ACTIVE = 0;
	public static final int REG_LOCKED = 2;
	
	//status nota
	
	public static final short BELUM_LUNAS = 0;
	public static final short SUDAH_LUNAS = 1;
	public static final short BELUM_LUNAS_SUDAH_DIVALIDASI = 2;
	public static final short TRANSAKSI_RETUR = 4;
	

	public static final short TREATMENT = 6;
	public static final short MEDICINE = 7;
	public static final short MISC = 8;
	
	//CARD TYPE
	public static final short KARTU_KREDIT = 1;
	public static final short KARTU_DEBET = 2;

	//lokasi lab
	public static final short LOKASI_LAB = 51;
	
	
	//transaction
	public static final String COA_DEFAULT_INPATIENT_AR = "COA_INPATIENT_AR";
	public static final String COA_DEFAULT_OUTPATIENT_AR = "COA_OUTPATIENT_AR";
	public static final String COA_DEFAULT_AP = "COA_AP";
	public static final String COA_DEFAULT_PPH21 = "COA_PPH21";
	public static final String COA_DEFAULT_MISC_TRX = "COA_MISC_TRX";
	public static final String COA_DEFAULT_PATIENT_AP = "COA_PATIENT_AP";
	public static final String COA_DEFAULT_STAFF_AP = "COA_STAFF_AP";
	
	
	//patient settlement
	public static final short BANK_SETTLEMENT = 1;
	public static final short INSURANCE_SETTLEMENT = 2;
	public static final short CASH_SETTLEMENT = 3;
	public static final short DEPOSIT_SETTLEMENT = 4;
	
	//
	public static final String FREE_BUYER_NAME = "FREE BUYER";
	public static final String MISC_CODE = "MISC-001";
	
	public static final String LIST_FONT = "font-size:8pt";
	public static final String LIST_COLOR = "font-size:8pt;background-color:#82FFA8;font-weight:bold";
	
	//CARA PASIEN KELUAR
	public static final short DIIZINKAN_PULANG = 0;
	public static final short MELARIKAN_DIRI = 1;
	public static final short PINDAH_KE_RS_LAIN = 2;
	public static final short MENINGGAL = 3;
	public static final short KEMAUAN_SENDIRI = 4;
	public static final short DI_RUJUK = 5;
	
	//KONDISI KELUAR
	public static final short SEMBUH = 6;
	public static final short MEMBAIK = 7;
	public static final short BELUM_SEMBUH = 8;
	public static final short MATI_KURANG_48_JAM = 9;
	public static final short MATI_LEBIH_48_JAM = 10;
	
	//JENIS DIAGNOSA
	public static final short DIAGNOSA_RANAP = 11;
	public static final short DIAGNOSA_RAJAL = 12;
	
	//COD TYPE
	public static final short PENYAKIT = 13;
	public static final short RUDA_PAKSA = 14;
	public static final short STILBIRTH = 15;
	public static final short PREGNANCY = 16;
	public static final short OPERATION = 17;
	
	
	//RUDA PAKSA
	public static final short BUNUH_DIRI = 18;
	public static final short PEMBUNUHAN = 19;
	public static final short KECELAKAAN = 20;
	
	
	//PREGNANCY
	public static final short KEHAMILAN = 21;
	public static final short PERSALINAN = 22;
	
	//DIAGNOSA UTAMA/BUKAN
	public static final short DIAGNOSA_UTAMA = 23;
	public static final short BUKAN_DIAGNOSA_UTAMA = 24;
	
	
	public static final String USER_SESSION = "USER AKTIF";
	
	//
	public static char NEWLINE = '\n';
	public static char CHAR_CONDENSED = 15;
	public static char CHAR_NORMAL = 18;
	public static final String DATETIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String DATE_FORMAT_SQL = "yyyy-MM-dd";
	public static final String CURRENCY_FORMAT = "#,##0";
	
	//OEN INFO
	/**
	public static final String OEN_YAYASAN = "RUMAH SAKIT CITRA MEDIKA CIRUAS";
	public static final String OEN_NPWP = "JALAN RAYA SERANG KM 09";
	public static final String OEN_PKP = "DESA RANJENG KEC. CIRUAS";
	public static final String OEN_RS = "SERANG 42182, TELP (0254)281829";
	*/
	
	
	public static final String OEN_YAYASAN = "RUMAH SAKIT TIARA SELLA";
	public static final String OEN_NPWP = "GRAHA BERNOZA";
	public static final String OEN_PKP = "JL. S PARMAN NO. 61 PADANG JATI";
	public static final String OEN_RS = "BENGKULU"; 
	
	public static final String ALAMAT = "";
	public static final String FONT_SIZE_8PT = "font-size:8pt";
	public static final String OK_BUTTON = "OK";
	public static final String CANCEL_BUTTON = "Cancel";
	
	public static final Integer APPROVED_STATUS = 2;
	public static final Integer ALLSENT_STATUS = 2;
	public static final int PORT_JASPER = 9998;
	
	
	public static final String SHIFT_PAGI_MULAI = " 07:01:00";
	public static final String SHIFT_PAGI_SELESAI = " 12:00:00";
	public static final String SHIFT_SORE_MULAI = " 12:01:00";
	public static final String SHIFT_SORE_SELESAI = " 19:00:00";
	public static final String SHIFT_MALAM_MULAI = " 19:01:00";
	public static final String SHIFT_MALAM_SELESAI = " 23:59:59";
	public static final double BIAYA_PENDAFTARAN = 15000;
	
	
	
	
	
	
	
}

-- Function: report.laporan_penjualan_rajal(stardDate varchar, endDate varchar, unitCode varchar, shift varchar)

-- DROP FUNCTION report.laporan_penjualan_rajal("varchar", "varchar", "varchar", "varchar");

CREATE OR REPLACE FUNCTION report.laporan_penjualan_rajal(startDate varchar, endDate varchar, unitcode varchar, shift varchar)
  RETURNS SETOF report.laporan_rajal AS
$BODY$declare nota record;
declare rajal_report report.laporan_rajal;
declare nomor int2;

declare nilai_sebelum_diskon float8;
declare nilai_setelah_diskon float8;
declare nilai_diskon float8;

declare item record;

declare jumlah_total float8;
declare jumlah_total_akhir float8;
declare jumlah_diskon float8;
declare jumlah_ppn float8;

begin
	nomor = 1;

	nilai_sebelum_diskon = 0;
	nilai_setelah_diskon = 0;
	nilai_diskon = 0;

	jumlah_total = 0;
	jumlah_total_akhir = 0;
	jumlah_diskon = 0;
	jumlah_ppn = 0;

	for nota in 
	select exam.*, pasien.v_patient_name from tb_examination exam,
	ms_patient pasien where exam.n_patient_id=pasien.n_patient_id
	and exam.v_note_no like unitCode and exam.n_shift_id=shift and
	exam.d_whn_create between startDate and endDate
	
	loop
		rajal_report.no_nota = nota.v_note_no;
		rajal_report.nomor = nomor;
		rajal_report.no_resep= nota.v_recipe_no;
		rajal_report.nama_pasien=nota.v_patient_name;
		rajal_report.ppn=report.convert_null(nota.n_ppn);

		jumlah_ppn = report.convert_null(nota.n_ppn);
		
		--transaksi item
		select into item 
			sum(trx.n_amount_trx) as sebelum , 
			sum(trx.n_amount_after_disc) as sesudah 
			from tb_item_trx trx
			where trx.n_note_id=nota.n_exam_id;
		
		nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(item.sebelum);
		nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(item.sesudah);
		nilai_diskon = nilai_diskon + (report.convert_null(item.sebelum) - report.convert_null(item.sesudah));

		-- transaksi racikan
		select into item 
			sum(trx.n_amount_trx) as sebelum , 
			sum(trx.n_amount_after_disc) as sesudah 
			from tb_drug_ingredients trx
			where trx.n_note_id=nota.n_exam_id;
		
		nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(item.sebelum);
		nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(item.sesudah);
		nilai_diskon = nilai_diskon + (report.convert_null(item.sebelum) - report.convert_null(item.sesudah));
		
		-- transaksi lain-lain
		select into item 
			sum(trx.n_amount_trx) as sebelum , 
			sum(trx.n_amount_after_disc) as sesudah 
			from tb_misc_trx trx
			where trx.n_note_id=nota.n_exam_id;
		
		nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(item.sebelum);
		nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(item.sesudah);
		nilai_diskon = nilai_diskon + (report.convert_null(item.sebelum) - report.convert_null(item.sesudah));

		-- transaksi treatment
		select into item 
			sum(trx.n_amount_trx) as sebelum , 
			sum(trx.n_amount_after_disc) as sesudah 
			from tb_treatment_trx trx
			where trx.n_note_id=nota.n_exam_id;
		
		nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(item.sebelum);
		nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(item.sesudah);
		nilai_diskon = nilai_diskon + (report.convert_null(item.sebelum) - report.convert_null(item.sesudah));

		-- transaksi paket
		select into item 
			sum(trx.n_amount_trx) as sebelum , 
			sum(trx.n_amount_after_disc) as sesudah 
			from tb_bundled_trx trx
			where trx.n_note_id=nota.n_exam_id;
		
		nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(item.sebelum);
		nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(item.sesudah);
		nilai_diskon = nilai_diskon + (report.convert_null(item.sebelum) - report.convert_null(item.sesudah));
		
		-- transaksi bed
		select into item 
			sum(trx.n_fee) as sebelum , 
			sum(trx.n_amount_after_disc) as sesudah 
			from tb_bed_trx trx
			where trx.n_note_id=nota.n_exam_id;
		
		nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(item.sebelum);
		nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(item.sesudah);
		nilai_diskon = nilai_diskon + (report.convert_null(item.sebelum) - report.convert_null(item.sesudah));



		rajal_report.total = nilai_sebelum_diskon;
		rajal_report.diskon = nilai_diskon;
		rajal_report.total_akhir = ceiling(nilai_setelah_diskon);
		
		jumlah_total = jumlah_total + nilai_sebelum_diskon;
		jumlah_total_akhir = jumlah_total_akhir + ceiling(nilai_setelah_diskon);
		jumlah_diskon = jumlah_diskon + nilai_diskon;

		return next rajal_report;
		nomor = nomor + 1;

	end loop;

	rajal_report.no_nota = '';
	rajal_report.nomor = null;
	rajal_report.no_resep= '';
	rajal_report.nama_pasien='TOTAL SELURUHNYA';
	rajal_report.ppn=jumlah_ppn;
	rajal_report.total = jumlah_total;
	rajal_report.diskon = jumlah_diskon;
	rajal_report.total_akhir = jumlah_total_akhir;
	
	return next rajal_report;

	

end$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION report.laporan_penjualan_rajal("varchar", "varchar", "varchar", "varchar") OWNER TO postgres;

select * from report.laporan_penjualan_rajal('2007-02-01 00:00:00', '2007-02-20 23:59:59', 'J-APTK%','2');
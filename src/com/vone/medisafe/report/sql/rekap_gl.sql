CREATE SEQUENCE public.tb_gl_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.tb_gl_id_seq
    OWNER TO postgres;
    
 
-- Table: public.tb_gl

-- DROP TABLE public.tb_gl;

CREATE TABLE public.tb_gl
(
    id integer NOT NULL,
    d_from date,
    d_to date,
    v_file_location character varying(300),
    n_status integer,
    CONSTRAINT tb_gl_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tb_gl
    OWNER to postgres;
    

-- Function: report.func_rekap_gl_bydate(bigint, date, date)

-- DROP FUNCTION report.func_rekap_gl_bydate(bigint, date, date);

CREATE OR REPLACE FUNCTION report.func_rekap_gl_bydate(
    coa_id bigint,
    d_from date,
    d_to date)
  RETURNS SETOF report.rep_gl AS
$BODY$declare rec_gl record;
declare m_row int8;
declare rec_type record;
declare m_rec_coa record;
declare my_rec record;
declare pengali int8;
declare m_balance float8; 
begin
	
		m_row = 1;
		pengali = 1;
		--select into rec_gl * from report.func_gl_bydate(n_coa_id,d_from, d_to) where n_row=m_row;

		select into m_rec_coa * 
		from ms_coa coa 
		where coa.n_coa_id = coa_id;
	
		select into rec_type * from ms_coa_type where n_ct_id=m_rec_coa.n_type;-- in (select n_type from ms_coa where n_coa_id=n_coa_id);
		
		select into my_rec report.convert_null(sum(t.n_debit)) as n_debit, report.convert_null(sum(t.n_credit)) as n_credit, t.n_coa_id 
		from tb_journal_trx t where  t.n_coa_id=coa_id and d_apl_date < d_from group by t.n_coa_id;

		if (rec_type.n_ct_natural_balance <> 1) then
			pengali = -1;
		end if;
	    
		if my_rec.n_debit is null then
		   my_rec.n_debit = 0;
		end if;

		if my_rec.n_credit is null then
		   my_rec.n_credit = 0;
		end if;

		m_balance = (pengali * (my_rec.n_debit - my_rec.n_credit));
		select into rec_gl * from report.rep_gl;
		rec_gl.n_coa_id = null;
		rec_gl.d_apl_date = null;
		rec_gl.n_journal_id = null;
		rec_gl.v_journal_batch_id = null;
		rec_gl.v_voucher_no = null;
		rec_gl.n_balance = m_balance;
		rec_gl.n_debit = null;
		rec_gl.n_credit = null;
		rec_gl.v_desc = 'SALDO AWAL';
                rec_gl.v_acct_name := m_rec_coa.v_acct_name||'['||m_rec_coa.v_acct_no||']'; 
		return next rec_gl;

		m_row = m_row + 1;

		for my_rec in select * 
		from tb_journal_trx t
		where t.n_coa_id=coa_id and (t.d_apl_date >= d_from and t.d_apl_date < (d_to+1))
		order by t.d_apl_date asc
		loop
			m_balance = m_balance + (pengali * (my_rec.n_debit - my_rec.n_credit));
			rec_gl.n_row = m_row;
			rec_gl.n_coa_id = my_rec.n_coa_id;
		        rec_gl.d_apl_date = my_rec.d_apl_date;
		        rec_gl.n_journal_id = my_rec.n_journal_id;
			rec_gl.v_journal_batch_id = my_rec.v_journal_batch_id;
		        rec_gl.v_voucher_no = my_rec.v_voucher_no;
		        rec_gl.n_balance = m_balance;
		        rec_gl.n_debit = my_rec.n_debit;
		        rec_gl.n_credit = my_rec.n_credit;
                        rec_gl.v_desc = my_rec.v_desc;
			rec_gl.v_acct_name = m_rec_coa.v_acct_name||'['||m_rec_coa.v_acct_no||']';
			return next rec_gl;
			m_row = m_row + 1;
		end loop;
        
	
	
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION report.func_rekap_gl_bydate(bigint, date, date)
  OWNER TO postgres;
  

  
-- Function: report.func_rekap_gl_all_bydate(date, date)

-- DROP FUNCTION report.func_rekap_gl_all_bydate(date, date);

CREATE OR REPLACE FUNCTION report.func_rekap_gl_all_bydate(
    dfrom date,
    dto date)
  RETURNS SETOF report.rep_gl AS
$BODY$
declare rec_gl record;
declare rec_coa record;
declare exist boolean;
declare counter int2;
DECLARE curs1 refcursor;

begin
	for rec_coa in select * 
	from ms_coa coa where coa.n_status=1 
	order by coa.v_acct_no
	loop
		exist = false;
		counter = 0;
		--select into counter count(1) from report.func_gl_bydate_arif(rec_coa.n_coa_id,dfrom,dto);
		--if (counter > 1) then
			for rec_gl in select * from report.func_rekap_gl_bydate(rec_coa.n_coa_id,dfrom,dto)
			loop
			    return next rec_gl;
			end loop;

			rec_gl.n_coa_id = null;
			rec_gl.d_apl_date = null;
			rec_gl.n_journal_id = null;
			rec_gl.v_journal_batch_id = null;
			rec_gl.v_voucher_no = null;
			rec_gl.n_balance = null;
			rec_gl.n_debit = null;
			rec_gl.n_credit = null;
			rec_gl.v_desc = null;
                        rec_gl.v_acct_name = null;
                        
			return next rec_gl;
		--end if;
		
	end loop;
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION report.func_rekap_gl_all_bydate(date, date)
  OWNER TO postgres;
  
  
    
    
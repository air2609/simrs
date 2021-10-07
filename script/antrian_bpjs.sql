-- Table: public.ms_poly_doctor

-- DROP TABLE public.ms_poly_doctor;

CREATE TABLE public.ms_poly_doctor
(
    id serial NOT NULL DEFAULT nextval('ms_poly_doctor_id_seq'::regclass),
    doctor_id integer,
    photo character varying(255),
    poly_status character varying(20),
    max_patient integer,
    schedule_from time without time zone,
    unit character varying(255),
    doctor_description character varying(1000),
    mon_schedule character varying(255),
    tue_schedule character varying(255),
    wed_schedule character varying(255),
    thu_schedule character varying(255),
    fri_schedule character varying(255),
    sat_schedule character varying(255),
    sun_schedule character varying(255),
    schedule_to time without time zone,
    CONSTRAINT ms_poly_doctor_pkey PRIMARY KEY (id),
    CONSTRAINT fk_poly_doctor_to_ms_staff FOREIGN KEY (doctor_id)
        REFERENCES public.ms_staff (n_staff_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.ms_poly_doctor
    OWNER to postgres;
    
 -- Table: public.tb_doctor_schedules

-- DROP TABLE public.tb_doctor_schedules;

CREATE TABLE public.tb_doctor_schedules
(
    id integer NOT NULL DEFAULT nextval('tb_doctor_schedules_id_seq'::regclass),
    doctor_id integer,
    schedule date,
    created_at timestamp without time zone,
    created_by character varying(50),
    schedule_month character varying(10),
    CONSTRAINT tb_doctor_schedules_pkey PRIMARY KEY (id),
    CONSTRAINT fk_doc_sch_to_staff FOREIGN KEY (doctor_id)
        REFERENCES public.ms_staff (n_staff_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tb_doctor_schedules
    OWNER to postgres;
    

 -- Table: public.tb_antrian

-- DROP TABLE public.tb_antrian;

CREATE TABLE public.tb_antrian
(
    id integer NOT NULL DEFAULT nextval('tb_antrian_id_seq'::regclass),
    doctor_id integer,
    queue_date character varying(12),
    queue_no integer,
    counter_no integer,
    bpjs_no character varying(30),
    phone_no character varying(20),
    referral_no character varying(50),
    status integer,
    created_at timestamp without time zone,
    done_at timestamp without time zone,
    done_by character varying(30),
    poliklinik character varying(100),
    CONSTRAINT tb_antrian_pkey PRIMARY KEY (id),
    CONSTRAINT fk_tb_antrian_to_ms_staff FOREIGN KEY (doctor_id)
        REFERENCES public.ms_staff (n_staff_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tb_antrian
    OWNER to postgres;

COMMENT ON COLUMN public.tb_antrian.status
    IS '0 --> new 
1 --> on service
2 --> done';  


CREATE SEQUENCE public.antrian_bpjs_seq;

ALTER SEQUENCE public.antrian_bpjs_seq
    OWNER TO postgres;
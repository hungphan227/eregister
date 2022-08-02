-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION postgres;

-- DROP SEQUENCE public.course_seq;

CREATE SEQUENCE public.course_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.student_course_relation_seq;

CREATE SEQUENCE public.student_course_relation_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.student_seq;

CREATE SEQUENCE public.student_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;-- public.course definition

-- Drop table

-- DROP TABLE public.course;

CREATE TABLE public.course (
	id int8 NOT NULL,
	course_limit int4 NULL,
	course_name varchar(255) NULL,
	course_number varchar(255) NULL,
	description varchar(1000) NULL,
	teacher varchar(255) NULL,
	CONSTRAINT course_pkey PRIMARY KEY (id)
);


-- public.student definition

-- Drop table

-- DROP TABLE public.student;

CREATE TABLE public.student (
	id int8 NOT NULL,
	age int4 NULL,
	"name" varchar(255) NULL,
	"password" varchar(255) NULL,
	student_number varchar(255) NULL,
	CONSTRAINT student_pkey PRIMARY KEY (id)
);


-- public.student_course_relation definition

-- Drop table

-- DROP TABLE public.student_course_relation;

CREATE TABLE public.student_course_relation (
	id int8 NOT NULL,
	course_id int8 NULL,
	student_id int8 NULL,
	CONSTRAINT student_course_relation_pkey PRIMARY KEY (id),
	CONSTRAINT uk74ojic066l8vffu2t5yjs8s85 UNIQUE (student_id, course_id)
);


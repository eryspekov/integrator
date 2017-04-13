CREATE TABLE argument
(
  id serial NOT NULL,
  name character varying NOT NULL,
  is_static boolean NOT NULL,
  arg_value character varying,
  CONSTRAINT argument_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE consumer_arg
(
  id serial NOT NULL,
  cs_id integer NOT NULL,
  arg_id integer NOT NULL,
  CONSTRAINT consumer_arg_pk PRIMARY KEY (id),
  CONSTRAINT consumer_arg_fk0 FOREIGN KEY (cs_id)
      REFERENCES consumer_service (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT consumer_arg_fk1 FOREIGN KEY (arg_id)
      REFERENCES argument (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE consumer_element
(
  id serial NOT NULL,
  cs_id integer NOT NULL,
  element_id integer NOT NULL,
  CONSTRAINT consumer_element_pk PRIMARY KEY (id),
  CONSTRAINT consumer_element_fk0 FOREIGN KEY (cs_id)
      REFERENCES consumer_service (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT consumer_element_fk1 FOREIGN KEY (element_id)
      REFERENCES element (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE consumer_producer
(
  id serial NOT NULL,
  cs_id integer NOT NULL,
  ps_id integer NOT NULL,
  CONSTRAINT consumer_producer_pk PRIMARY KEY (id),
  CONSTRAINT consumer_producer_fk0 FOREIGN KEY (cs_id)
      REFERENCES consumer_service (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT consumer_producer_fk1 FOREIGN KEY (ps_id)
      REFERENCES producer_service (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE consumer_service
(
  id serial NOT NULL,
  name character varying NOT NULL,
  method character varying NOT NULL,
  ws_id integer NOT NULL,
  organization_id bigint,
  CONSTRAINT consumer_service_pk PRIMARY KEY (id),
  CONSTRAINT consumer_service_fk0 FOREIGN KEY (ws_id)
      REFERENCES web_service_type (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT organizationa_id FOREIGN KEY (organization_id)
      REFERENCES organization (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT consumer_service_method_key UNIQUE (method),
  CONSTRAINT consumer_service_name_key UNIQUE (name)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE consumer_user
(
  id bigserial NOT NULL,
  cs_id bigint NOT NULL,
  user_id bigint NOT NULL,
  CONSTRAINT consumer_user_id PRIMARY KEY (id),
  CONSTRAINT cs_id FOREIGN KEY (cs_id)
      REFERENCES consumer_service (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE element
(
  id serial NOT NULL,
  name character varying NOT NULL,
  CONSTRAINT element_pk PRIMARY KEY (id),
  CONSTRAINT element_name_key UNIQUE (name)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE organization
(
  id serial NOT NULL,
  name character varying NOT NULL,
  CONSTRAINT organization_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE producer_arg
(
  id serial NOT NULL,
  ps_id integer NOT NULL,
  arg_id integer NOT NULL,
  order_num integer NOT NULL DEFAULT 1,
  CONSTRAINT producer_arg_pk PRIMARY KEY (id),
  CONSTRAINT producer_arg_fk0 FOREIGN KEY (ps_id)
      REFERENCES producer_service (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT producer_arg_fk1 FOREIGN KEY (arg_id)
      REFERENCES argument (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE producer_element
(
  id serial NOT NULL,
  ps_id integer NOT NULL,
  element_id integer NOT NULL,
  CONSTRAINT producer_element_pk PRIMARY KEY (id),
  CONSTRAINT producer_element_fk0 FOREIGN KEY (ps_id)
      REFERENCES producer_service (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT producer_element_fk1 FOREIGN KEY (element_id)
      REFERENCES element (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE producer_service
(
  id serial NOT NULL,
  organization_id integer NOT NULL,
  name character varying NOT NULL,
  url character varying NOT NULL,
  ws_id integer NOT NULL,
  with_param boolean NOT NULL,
  CONSTRAINT producer_service_pk PRIMARY KEY (id),
  CONSTRAINT producer_service_fk0 FOREIGN KEY (organization_id)
      REFERENCES organization (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT producer_service_fk1 FOREIGN KEY (ws_id)
      REFERENCES web_service_type (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT producer_service_name_key UNIQUE (name)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE service_log
(
  id bigserial NOT NULL,
  username character varying(50) NOT NULL,
  response character varying(300),
  logdate timestamp without time zone NOT NULL DEFAULT now(),
  request character varying(300),
  ipaddress character varying(27),
  CONSTRAINT log_id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE user_role
(
  id serial NOT NULL,
  user_id integer NOT NULL,
  authority character varying(50) NOT NULL,
  CONSTRAINT user_role_pk PRIMARY KEY (id),
  CONSTRAINT user_role_fk0 FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE users
(
  id integer NOT NULL DEFAULT nextval('user_id_seq'::regclass),
  username character varying(50) NOT NULL,
  password character varying(50) NOT NULL,
  enabled boolean NOT NULL DEFAULT false,
  organization_id bigint,
  CONSTRAINT user_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE web_service_type
(
  id serial NOT NULL,
  name character varying NOT NULL,
  CONSTRAINT web_service_type_pk PRIMARY KEY (id),
  CONSTRAINT web_service_type_name_key UNIQUE (name)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE argument_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 37
  CACHE 1;

CREATE SEQUENCE consumer_arg_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 30
  CACHE 1;

CREATE SEQUENCE consumer_element_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 66
  CACHE 1;

CREATE SEQUENCE consumer_producer_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 12
  CACHE 1;

CREATE SEQUENCE consumer_service_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 20
  CACHE 1;

CREATE SEQUENCE consumer_user_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 17
  CACHE 1;

CREATE SEQUENCE element_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 39
  CACHE 1;

CREATE SEQUENCE organization_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 2
  CACHE 1;

CREATE SEQUENCE producer_arg_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 37
  CACHE 1;

CREATE SEQUENCE producer_element_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 52
  CACHE 1;

CREATE SEQUENCE producer_service_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 42
  CACHE 1;


CREATE SEQUENCE service_log_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 19
  CACHE 1;

CREATE SEQUENCE user_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 8
  CACHE 1;

CREATE SEQUENCE user_role_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 7
  CACHE 1;

CREATE SEQUENCE web_service_type_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 2
  CACHE 1;
  
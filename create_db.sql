CREATE TABLE "organization" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT organization_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "producer_service" (
	"id" serial NOT NULL,
	"organization_id" serial NOT NULL,
	"name" varchar NOT NULL UNIQUE,
	"url" varchar NOT NULL UNIQUE,
	"ws_id" integer NOT NULL,
	"with_param" BOOLEAN NOT NULL DEFAULT 'true',
	CONSTRAINT producer_service_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "element" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT element_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "web_service_type" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL UNIQUE,
	CONSTRAINT web_service_type_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "argument" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL UNIQUE,
	"is_static" BOOLEAN NOT NULL,
	"value" varchar NOT NULL,
	CONSTRAINT argument_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "consumer_service" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL UNIQUE,
	"method" varchar NOT NULL UNIQUE,
	"ws_is" integer NOT NULL,
	CONSTRAINT consumer_service_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "producer_arg" (
	"id" serial NOT NULL,
	"ps_id" integer NOT NULL,
	"arg_id" integer NOT NULL,
	CONSTRAINT producer_arg_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "consumer_producer" (
	"id" serial NOT NULL,
	"cs_id" integer NOT NULL,
	"ps_id" integer NOT NULL,
	CONSTRAINT consumer_producer_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "consumer_element" (
	"id" serial NOT NULL,
	"cs_id" integer NOT NULL,
	"element_id" integer NOT NULL,
	CONSTRAINT consumer_element_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "consumer_arg" (
	"id" serial NOT NULL,
	"cs_id" integer NOT NULL,
	"arg_id" integer NOT NULL,
	CONSTRAINT consumer_arg_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "producer_element" (
	"id" serial NOT NULL,
	"ps__id" integer NOT NULL,
	"element_id" integer NOT NULL,
	CONSTRAINT producer_element_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);




ALTER TABLE "producer_service" ADD CONSTRAINT "producer_service_fk0" FOREIGN KEY ("organization_id") REFERENCES "organization"("id");
ALTER TABLE "producer_service" ADD CONSTRAINT "producer_service_fk1" FOREIGN KEY ("ws_id") REFERENCES "web_service_type"("id");




ALTER TABLE "consumer_service" ADD CONSTRAINT "consumer_service_fk0" FOREIGN KEY ("ws_is") REFERENCES "web_service_type"("id");

ALTER TABLE "producer_arg" ADD CONSTRAINT "producer_arg_fk0" FOREIGN KEY ("ps_id") REFERENCES "producer_service"("id");
ALTER TABLE "producer_arg" ADD CONSTRAINT "producer_arg_fk1" FOREIGN KEY ("arg_id") REFERENCES "argument"("id");

ALTER TABLE "consumer_producer" ADD CONSTRAINT "consumer_producer_fk0" FOREIGN KEY ("cs_id") REFERENCES "consumer_service"("id");
ALTER TABLE "consumer_producer" ADD CONSTRAINT "consumer_producer_fk1" FOREIGN KEY ("ps_id") REFERENCES "producer_service"("id");

ALTER TABLE "consumer_element" ADD CONSTRAINT "consumer_element_fk0" FOREIGN KEY ("cs_id") REFERENCES "consumer_service"("id");
ALTER TABLE "consumer_element" ADD CONSTRAINT "consumer_element_fk1" FOREIGN KEY ("element_id") REFERENCES "element"("id");

ALTER TABLE "consumer_arg" ADD CONSTRAINT "consumer_arg_fk0" FOREIGN KEY ("cs_id") REFERENCES "consumer_service"("id");
ALTER TABLE "consumer_arg" ADD CONSTRAINT "consumer_arg_fk1" FOREIGN KEY ("arg_id") REFERENCES "argument"("id");

ALTER TABLE "producer_element" ADD CONSTRAINT "producer_element_fk0" FOREIGN KEY ("ps__id") REFERENCES "producer_service"("id");
ALTER TABLE "producer_element" ADD CONSTRAINT "producer_element_fk1" FOREIGN KEY ("element_id") REFERENCES "element"("id");

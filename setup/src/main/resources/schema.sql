--------------------------------------------------------
--  Drop statements
--------------------------------------------------------
DROP SEQUENCE SERVICE_REQUEST_ID_SEQ;
DROP TABLE SERVICE_REQUEST;

--------------------------------------------------------
--  create statements
--------------------------------------------------------

CREATE SEQUENCE "SERVICE_REQUEST_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 8001 CACHE 1000 NOORDER NOCYCLE;

CREATE TABLE "SERVIZZ"."SERVICE_REQUEST"
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
      "DESCRIPTION" VARCHAR2(255 CHAR),
      "SERVICE_TYPE" VARCHAR2(255 CHAR),
      "DATE_FROM" DATE,
      "DATE_TO" DATE,
       PRIMARY KEY ("ID")
   );

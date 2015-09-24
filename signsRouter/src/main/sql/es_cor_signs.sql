--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.9
-- Dumped by pg_dump version 9.4.4
-- Started on 2015-09-24 10:55:17 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 198 (class 1259 OID 147196)
-- Name: es_cor_signs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE es_cor_signs (
    gid integer NOT NULL,
    azimut double precision,
    tipo character varying(7),
    geom geometry(Point,32629)
);


--
-- TOC entry 199 (class 1259 OID 147202)
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE es_cor_signs_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 199
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE es_cor_signs_gid_seq OWNED BY es_cor_signs.gid;


--
-- TOC entry 3340 (class 2604 OID 147204)
-- Name: gid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY es_cor_signs ALTER COLUMN gid SET DEFAULT nextval('es_cor_signs_gid_seq'::regclass);


--
-- TOC entry 3461 (class 0 OID 147196)
-- Dependencies: 198
-- Data for Name: es_cor_signs; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO es_cor_signs VALUES (2, 210, 'R101', '0101000020757F0000B1B6656810BE20418C81D1C83B525241');
INSERT INTO es_cor_signs VALUES (3, 110, 'R101', '0101000020757F0000273A7F557FBC2041EA31BAB13E525241');
INSERT INTO es_cor_signs VALUES (4, 190, 'R101', '0101000020757F0000E1CDD06587BC204162DCEE9F3E525241');
INSERT INTO es_cor_signs VALUES (1, 120, 'R101', '0101000020757F0000D2C9629903BE20418AE3ABD23C525241');
INSERT INTO es_cor_signs VALUES (5, 300, 'R101', '0101000020757F0000DE198D05B0BD20414D9A42D21F525241');
INSERT INTO es_cor_signs VALUES (6, 0, 'R303', '0101000020757F0000C2FBD74918BF204193B8D29F3E525241');
INSERT INTO es_cor_signs VALUES (7, 0, 'R302', '0101000020757F0000DED0E415E7B320419F895F6887515241');


--
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 199
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('es_cor_signs_gid_seq', 7, true);


--
-- TOC entry 3342 (class 2606 OID 147206)
-- Name: es_avi_signs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY es_cor_signs
    ADD CONSTRAINT es_avi_signs_pkey PRIMARY KEY (gid);


--
-- TOC entry 3343 (class 1259 OID 149117)
-- Name: es_cor_signs_geom_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX es_cor_signs_geom_idx ON es_cor_signs USING gist (geom);


-- Completed on 2015-09-24 10:55:17 CEST

--
-- PostgreSQL database dump complete
--


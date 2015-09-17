--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.9
-- Dumped by pg_dump version 9.4.4
-- Started on 2015-09-17 11:22:36 CEST

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
-- TOC entry 221 (class 1259 OID 143785)
-- Name: es_cor_signs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE es_cor_signs (
    gid integer NOT NULL,
    azimut double precision,
    tipo character varying(7),
    geom geometry(Point,4326)
);


--
-- TOC entry 220 (class 1259 OID 143783)
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE es_cor_signs_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 220
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE es_cor_signs_gid_seq OWNED BY es_cor_signs.gid;


--
-- TOC entry 3326 (class 2604 OID 143788)
-- Name: gid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY es_cor_signs ALTER COLUMN gid SET DEFAULT nextval('es_cor_signs_gid_seq'::regclass);


--
-- TOC entry 3448 (class 0 OID 143785)
-- Dependencies: 221
-- Data for Name: es_cor_signs; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO es_cor_signs VALUES (2, 210, 'R101', '0101000020E61000006BD90258BBCC20C01FA6C2AE30B04540');
INSERT INTO es_cor_signs VALUES (3, 110, 'R101', '0101000020E61000003C49A3A8FFCD20C0B50E9F8A34B04540');
INSERT INTO es_cor_signs VALUES (4, 190, 'R101', '0101000020E610000021D88523F9CD20C0226D707334B04540');
INSERT INTO es_cor_signs VALUES (1, 120, 'R101', '0101000020E6100000316533A8C5CC20C03810FEEB31B04540');
INSERT INTO es_cor_signs VALUES (5, 300, 'R101', '0101000020E6100000606EBB9D0ACD20C0605E99C80FB04540');


--
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 220
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('es_cor_signs_gid_seq', 5, true);


--
-- TOC entry 3328 (class 2606 OID 143793)
-- Name: es_avi_signs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY es_cor_signs
    ADD CONSTRAINT es_avi_signs_pkey PRIMARY KEY (gid);


--
-- TOC entry 3329 (class 1259 OID 143794)
-- Name: es_cor_signs_geom_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX es_cor_signs_geom_idx ON es_cor_signs USING gist (geom);


-- Completed on 2015-09-17 11:22:36 CEST

--
-- PostgreSQL database dump complete
--


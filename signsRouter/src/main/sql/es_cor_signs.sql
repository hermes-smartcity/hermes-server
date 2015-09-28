--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.9
-- Dumped by pg_dump version 9.4.4
-- Started on 2015-09-28 09:35:52 CEST

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
-- TOC entry 225 (class 1259 OID 149126)
-- Name: es_cor_signs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE es_cor_signs (
    gid integer NOT NULL,
    azimut double precision,
    tipo character varying(7),
    geom geometry(Point,32629)
);


--
-- TOC entry 226 (class 1259 OID 149132)
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE es_cor_signs_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 226
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE es_cor_signs_gid_seq OWNED BY es_cor_signs.gid;


--
-- TOC entry 3343 (class 2604 OID 149134)
-- Name: gid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY es_cor_signs ALTER COLUMN gid SET DEFAULT nextval('es_cor_signs_gid_seq'::regclass);


--
-- TOC entry 3465 (class 0 OID 149126)
-- Dependencies: 225
-- Data for Name: es_cor_signs; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO es_cor_signs VALUES (2, 210, 'R101', '0101000020757F0000B1B6656810BE20418C81D1C83B525241');
INSERT INTO es_cor_signs VALUES (3, 110, 'R101', '0101000020757F0000273A7F557FBC2041EA31BAB13E525241');
INSERT INTO es_cor_signs VALUES (4, 190, 'R101', '0101000020757F0000E1CDD06587BC204162DCEE9F3E525241');
INSERT INTO es_cor_signs VALUES (1, 120, 'R101', '0101000020757F0000D2C9629903BE20418AE3ABD23C525241');
INSERT INTO es_cor_signs VALUES (5, 300, 'R101', '0101000020757F0000DE198D05B0BD20414D9A42D21F525241');
INSERT INTO es_cor_signs VALUES (6, 0, 'R303', '0101000020757F0000C2FBD74918BF204193B8D29F3E525241');
INSERT INTO es_cor_signs VALUES (7, 0, 'R302', '0101000020757F0000DED0E415E7B320419F895F6887515241');
INSERT INTO es_cor_signs VALUES (8, 210, 'R101', '0101000020757F00006BD301EFB6BD2041EBB5422721525241');
INSERT INTO es_cor_signs VALUES (9, 300, 'R101', '0101000020757F0000E3D92628F7BA2041A5BCE07C45525241');
INSERT INTO es_cor_signs VALUES (10, 120, 'R303', '0101000020757F000023D8461DF8BA20415FE1FD6645525241');
INSERT INTO es_cor_signs VALUES (11, 330, 'R101', '0101000020757F0000F13FE2E640BC204140A1821DCE515241');
INSERT INTO es_cor_signs VALUES (12, 330, 'R1011', '0101000020757F00009E55F76AE3BD20415022A55C17525241');
INSERT INTO es_cor_signs VALUES (13, 250, 'R302', '0101000020757F0000E1AC936DD0BD20413C9570A216525241');
INSERT INTO es_cor_signs VALUES (14, 110, 'R301-50', '0101000020757F000019F40D2A48BE204168B60546CC525241');
INSERT INTO es_cor_signs VALUES (15, 170, 'R301-50', '0101000020757F000066C77E4605B820410E697D28E1525241');


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 226
-- Name: es_cor_signs_gid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('es_cor_signs_gid_seq', 15, true);


--
-- TOC entry 3345 (class 2606 OID 149136)
-- Name: es_avi_signs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY es_cor_signs
    ADD CONSTRAINT es_avi_signs_pkey PRIMARY KEY (gid);


--
-- TOC entry 3346 (class 1259 OID 149137)
-- Name: es_cor_signs_geom_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX es_cor_signs_geom_idx ON es_cor_signs USING gist (geom);


-- Completed on 2015-09-28 09:35:53 CEST

--
-- PostgreSQL database dump complete
--


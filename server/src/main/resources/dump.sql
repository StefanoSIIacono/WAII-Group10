--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3 (Debian 15.3-1.pgdg110+1)
-- Dumped by pg_dump version 15.3 (Ubuntu 15.3-0ubuntu0.23.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


\connect postgres

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: addresses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.addresses (
                                  address_id character varying(255) NOT NULL,
                                  city character varying(255),
                                  country character varying(255),
                                  house_number character varying(255),
                                  street character varying(255),
                                  zip_code character varying(255),
                                  profile_email character varying(255)
);


ALTER TABLE public.addresses OWNER TO postgres;

--
-- Name: attachments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attachments (
                                    id bigint NOT NULL,
                                    attachment bytea,
                                    content_type character varying(255),
                                    size smallint NOT NULL,
                                    message_id bigint
);


ALTER TABLE public.attachments OWNER TO postgres;

--
-- Name: attachments_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.attachments_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attachments_seq OWNER TO postgres;

--
-- Name: expert_expertise; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expert_expertise (
                                         expert_id bigint NOT NULL,
                                         expertise_id bigint NOT NULL
);


ALTER TABLE public.expert_expertise OWNER TO postgres;

--
-- Name: expertises; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expertises (
                                   id bigint NOT NULL,
                                   field character varying(255)
);


ALTER TABLE public.expertises OWNER TO postgres;

--
-- Name: expertises_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.expertises_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.expertises_seq OWNER TO postgres;

--
-- Name: experts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.experts (
                                id bigint NOT NULL,
                                name character varying(255),
                                surname character varying(255)
);


ALTER TABLE public.experts OWNER TO postgres;

--
-- Name: experts_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.experts_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.experts_seq OWNER TO postgres;

--
-- Name: managers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.managers (
                                 id bigint NOT NULL,
                                 name character varying(255),
                                 surname character varying(255)
);


ALTER TABLE public.managers OWNER TO postgres;

--
-- Name: managers_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.managers_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.managers_seq OWNER TO postgres;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
                                 id bigint NOT NULL,
                                 body character varying(255),
                                 "timestamp" timestamp(6) without time zone,
                                 expert_id bigint,
                                 ticket_id bigint
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: messages_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.messages_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.messages_seq OWNER TO postgres;

--
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
                                 id character varying(255) NOT NULL,
                                 brand character varying(255),
                                 name character varying(255)
);


ALTER TABLE public.products OWNER TO postgres;

--
-- Name: profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.profiles (
                                 email character varying(255) NOT NULL,
                                 name character varying(255),
                                 surname character varying(255)
);


ALTER TABLE public.profiles OWNER TO postgres;

--
-- Name: sequence_1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sequence_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sequence_1 OWNER TO postgres;

--
-- Name: statuses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.statuses (
                                 id bigint NOT NULL,
                                 status smallint,
                                 status_changer character varying(255),
                                 "timestamp" timestamp(6) without time zone,
                                 expert_id bigint,
                                 ticket_id bigint
);


ALTER TABLE public.statuses OWNER TO postgres;

--
-- Name: statuses_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.statuses_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.statuses_seq OWNER TO postgres;

--
-- Name: tickets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tickets (
                                id bigint NOT NULL,
                                arg character varying(255),
                                obj character varying(255),
                                priority character varying(255),
                                expert_id bigint,
                                product_id character varying(255),
                                profile_email character varying(255)
);


ALTER TABLE public.tickets OWNER TO postgres;

--
-- Name: tickets_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tickets_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tickets_seq OWNER TO postgres;

--
-- Data for Name: addresses; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.addresses (address_id, city, country, house_number, street, zip_code, profile_email) VALUES ('1', 'turin', 'italy', '12', 'via vigone', '10138', 'mario.rossi@gmail.com');
INSERT INTO public.addresses (address_id, city, country, house_number, street, zip_code, profile_email) VALUES ('2', 'narnia', 'italy', '2', 'via narnia', '98172', 'luigi.verdi@gmail.com');
INSERT INTO public.addresses (address_id, city, country, house_number, street, zip_code, profile_email) VALUES ('3', 'scampia', 'italy', '18', 'via mun', '12121', 'sergio.bianchi@gmail.com');


--
-- Data for Name: attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: expert_expertise; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.expert_expertise (expert_id, expertise_id) VALUES (1, 2);
INSERT INTO public.expert_expertise (expert_id, expertise_id) VALUES (2, 3);
INSERT INTO public.expert_expertise (expert_id, expertise_id) VALUES (4, 4);
INSERT INTO public.expert_expertise (expert_id, expertise_id) VALUES (5, 1);


--
-- Data for Name: expertises; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.expertises (id, field) VALUES (1, 'WRONG-DELIVERY');
INSERT INTO public.expertises (id, field) VALUES (2, 'DAMAGED-PRODUCT');
INSERT INTO public.expertises (id, field) VALUES (3, 'COMPUTER');
INSERT INTO public.expertises (id, field) VALUES (4, 'ELECTRONIC');
INSERT INTO public.expertises (id, field) VALUES (5, 'MECHANICAL');
INSERT INTO public.expertises (id, field) VALUES (6, 'ELECTRIC');


--
-- Data for Name: experts; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.experts (id, name, surname) VALUES (1, 'Gino', 'Cuccagno');
INSERT INTO public.experts (id, name, surname) VALUES (2, 'Mohamed', 'Letija');
INSERT INTO public.experts (id, name, surname) VALUES (3, 'Pickle', 'Rick');
INSERT INTO public.experts (id, name, surname) VALUES (4, 'Pino', 'Paolino');
INSERT INTO public.experts (id, name, surname) VALUES (5, 'Mastro', 'Gesualdo');


--
-- Data for Name: managers; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.managers (id, name, surname) VALUES (1, 'BIG', 'BOSS');


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.products (id, brand, name) VALUES ('9780702305511', 'Scholastic', 'Welsh Fairy Tales, Myths and Legends by Claire Fayers (Paperback, 2021)');
INSERT INTO public.products (id, brand, name) VALUES ('9781292204482', 'Pearson Education The Limited', 'Accounting and Finance: An Introduction 9th edition by Peter Atrill, Eddie McLaney (Paperback, 2018)');
INSERT INTO public.products (id, brand, name) VALUES ('9781788307017', 'Olympia Publishers', 'Vegans Deserve Better than a Fruit Salad by Danielle Maupertuis (Paperback, 2020)');
INSERT INTO public.products (id, brand, name) VALUES ('9781942275732', 'Zenescope Entertainment', 'The Black Sable by Joe Brusha (Paperback, 2018)');
INSERT INTO public.products (id, brand, name) VALUES ('9781941610428', 'Fons Vitae,US', 'The Prophetic Ethics and the Courtesies of Living by Al-Ghazali (Paperback, 2019)');
INSERT INTO public.products (id, brand, name) VALUES ('9781617137082', 'Hal Leonard Corporation', 'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)');
INSERT INTO public.products (id, brand, name) VALUES ('9781617137080', 'Hal Leonard Corporation', 'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)');
INSERT INTO public.products (id, brand, name) VALUES ('9781617137083', 'Hal Leonard Corporation', 'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)');
INSERT INTO public.products (id, brand, name) VALUES ('9781617137084', 'Hal Leonard Corporation', 'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)');
INSERT INTO public.products (id, brand, name) VALUES ('5707286436169', 'Nissens', 'Nissens 95601 Dryer Air Conditioning OE Replacement Top Quality');
INSERT INTO public.products (id, brand, name) VALUES ('9786420945091', 'Mercedes-Benz', 'MERCEDES CLS 320 CDI C219 2007 RHD Air Intake Hose Pipe Tube A6420945097');
INSERT INTO public.products (id, brand, name) VALUES ('5030917124013', 'Sony', 'PlayStation 3 Ps3 Destiny Vanguard Edition UK IMPORT a VideoGames');


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.profiles (email, name, surname) VALUES ('mario.rossi@gmail.com', 'mario', 'rossi');
INSERT INTO public.profiles (email, name, surname) VALUES ('luigi.verdi@gmail.com', 'luigi', 'verdi');
INSERT INTO public.profiles (email, name, surname) VALUES ('sergio.bianchi@gmail.com', 'sergio', 'bianchi');


--
-- Data for Name: statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.statuses (id, status, status_changer, "timestamp", expert_id, ticket_id) VALUES (1, 1, 'MANAGER', '1999-01-08 01:05:06', 1, 1);
INSERT INTO public.statuses (id, status, status_changer, "timestamp", expert_id, ticket_id) VALUES (2, 2, 'EXPERT', '2000-02-18 05:25:46', 2, 2);
INSERT INTO public.statuses (id, status, status_changer, "timestamp", expert_id, ticket_id) VALUES (3, 3, 'PROFILE', '2006-06-13 22:21:46', 4, 3);


--
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tickets (id, arg, obj, priority, expert_id, product_id, profile_email) VALUES (1, 'argument', 'obj', 'LOW', 1, '9781292204482', 'mario.rossi@gmail.com');
INSERT INTO public.tickets (id, arg, obj, priority, expert_id, product_id, profile_email) VALUES (2, 'please help', 'i need help', 'HIGH', 2, '9786420945091', 'mario.rossi@gmail.com');
INSERT INTO public.tickets (id, arg, obj, priority, expert_id, product_id, profile_email) VALUES (3, 'broken gift', 'you ruined my birthday', 'MEDIUM', 3, '9781617137080', 'luigi.verdi@gmail.com');


--
-- Name: attachments_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.attachments_seq', 1, false);


--
-- Name: expertises_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.expertises_seq', 1, false);


--
-- Name: experts_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.experts_seq', 1, false);


--
-- Name: managers_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.managers_seq', 1, false);


--
-- Name: messages_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.messages_seq', 1, false);


--
-- Name: sequence_1; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sequence_1', 1, false);


--
-- Name: statuses_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.statuses_seq', 1, false);


--
-- Name: tickets_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tickets_seq', 1, false);


--
-- Name: addresses addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (address_id);


--
-- Name: attachments attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_pkey PRIMARY KEY (id);


--
-- Name: expert_expertise expert_expertise_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expert_expertise
    ADD CONSTRAINT expert_expertise_pkey PRIMARY KEY (expert_id, expertise_id);


--
-- Name: expertises expertises_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expertises
    ADD CONSTRAINT expertises_pkey PRIMARY KEY (id);


--
-- Name: experts experts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.experts
    ADD CONSTRAINT experts_pkey PRIMARY KEY (id);


--
-- Name: managers managers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.managers
    ADD CONSTRAINT managers_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: profiles profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profiles
    ADD CONSTRAINT profiles_pkey PRIMARY KEY (email);


--
-- Name: statuses statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.statuses
    ADD CONSTRAINT statuses_pkey PRIMARY KEY (id);


--
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- Name: expertises uk_3600doxk9hy6emdkrho38epjk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expertises
    ADD CONSTRAINT uk_3600doxk9hy6emdkrho38epjk UNIQUE (field);


--
-- Name: tickets fk56p9eq8fjdagec8ydtf4r23lp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fk56p9eq8fjdagec8ydtf4r23lp FOREIGN KEY (profile_email) REFERENCES public.profiles(email);


--
-- Name: messages fk6iv985o3ybdk63srj731en4ba; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk6iv985o3ybdk63srj731en4ba FOREIGN KEY (ticket_id) REFERENCES public.tickets(id);


--
-- Name: tickets fkavo2av2fyyehcvlec0vowwu1j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fkavo2av2fyyehcvlec0vowwu1j FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: statuses fkc1qmjbxp508hwulx2kb898l5p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.statuses
    ADD CONSTRAINT fkc1qmjbxp508hwulx2kb898l5p FOREIGN KEY (ticket_id) REFERENCES public.tickets(id);


--
-- Name: attachments fkcf4ta8qdkixetfy7wnqfv3vkv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT fkcf4ta8qdkixetfy7wnqfv3vkv FOREIGN KEY (message_id) REFERENCES public.messages(id);


--
-- Name: tickets fkdqocj5l89sf10g9jguw7l5df9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fkdqocj5l89sf10g9jguw7l5df9 FOREIGN KEY (expert_id) REFERENCES public.experts(id);


--
-- Name: addresses fki4bubqretjd6r0py8vskj960v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT fki4bubqretjd6r0py8vskj960v FOREIGN KEY (profile_email) REFERENCES public.profiles(email);


--
-- Name: statuses fkl189tlqhbm8uxlqoqvcpkqpgc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.statuses
    ADD CONSTRAINT fkl189tlqhbm8uxlqoqvcpkqpgc FOREIGN KEY (expert_id) REFERENCES public.experts(id);


--
-- Name: messages fkrcrtpt8k87r75i3gg0qd1jvf2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkrcrtpt8k87r75i3gg0qd1jvf2 FOREIGN KEY (expert_id) REFERENCES public.experts(id);


--
-- Name: expert_expertise fkrh70nqipdmx0hcpx12w5u5fqt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expert_expertise
    ADD CONSTRAINT fkrh70nqipdmx0hcpx12w5u5fqt FOREIGN KEY (expertise_id) REFERENCES public.expertises(id);


--
-- Name: expert_expertise fks3jht9oh34rvcum2pcx52t5yd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expert_expertise
    ADD CONSTRAINT fks3jht9oh34rvcum2pcx52t5yd FOREIGN KEY (expert_id) REFERENCES public.experts(id);


--
-- PostgreSQL database dump complete
--


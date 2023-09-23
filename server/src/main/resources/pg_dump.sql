--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2 (Debian 15.2-1.pgdg110+1)
-- Dumped by pg_dump version 15.2 (Debian 15.2-1.pgdg110+1)

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: addresses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.addresses (
    id bigint NOT NULL,
    city character varying(255),
    country character varying(255),
    address character varying(255),
    zip_code character varying(255),
    profile_email character varying(255)
);


ALTER TABLE public.addresses OWNER TO postgres;

CREATE SEQUENCE public.addresses_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.addresses_seq OWNER TO postgres;

--
-- Name: attachments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attachments (
    id bigint NOT NULL,
    attachment character varying(255),
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
    expert_email character varying(255) NOT NULL,
    expertise_field character varying(255)  NOT NULL
);


ALTER TABLE public.expert_expertise OWNER TO postgres;

--
-- Name: expertises; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expertises (
    field character varying(255) NOT NULL
);


ALTER TABLE public.expertises OWNER TO postgres;

--
-- Name: experts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.experts (
    email character varying(255) NOT NULL,
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
    email character varying(255) NOT NULL,
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
    index int NOT NULL,
    body character varying(255),
    "timestamp" timestamp(6) without time zone,
    expert_email character varying(255),
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

CREATE INDEX idx_paging ON public.messages
    (
    ticket_id,
    timestamp DESC
    );

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
    expert_email character varying(255),
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
    obj character varying(255),
    priority character varying(255),
    arg_field character varying(255),
    expert_email character varying(255),
    product_id character varying(255),
    profile_email character varying(255),
    last_read_message_index_expert int NOT NULL,
    last_read_message_index_profile int NOT NULL
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

COPY public.addresses (id, city, country, address, zip_code, profile_email) FROM stdin;
1	turin	italy	via vigone 12	10138	mario.rossi@gmail.com
2	narnia	italy	via narnia 2	98172	luigi.verdi@gmail.com
3	scampia	italy	via mun 18	12121	sergio.bianchi@gmail.com
\.


--
-- Data for Name: attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attachments (id, attachment, content_type, size, message_id) FROM stdin;
\.


--
-- Data for Name: expert_expertise; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expert_expertise (expert_email, expertise_field) FROM stdin;
gino.cuccagno@tickets.com	DAMAGED-PRODUCT
mohamed.letija@tickets.com	COMPUTER
pino.paolino@tickets.com	ELECTRONIC
mastro.gesualdo@tickets.com	WRONG-DELIVERY
\.


--
-- Data for Name: expertises; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expertises (field) FROM stdin;
WRONG-DELIVERY
DAMAGED-PRODUCT
COMPUTER
ELECTRONIC
MECHANICAL
ELECTRIC
\.


--
-- Data for Name: experts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.experts (email, name, surname) FROM stdin;
gino.cuccagno@tickets.com	Gino	Cuccagno
mohamed.letija@tickets.com	Mohamed	Letija
pickle.rick@tickets.com	Pickle	Rick
pino.paolino@tickets.com	Pino	Paolino
mastro.gesualdo@tickets.com	Mastro	Gesualdo
\.


--
-- Data for Name: managers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.managers (email, name, surname) FROM stdin;
bigboss@tickets.admin.com	BIG	BOSS
\.


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, "index", body, "timestamp", expert_email, ticket_id) FROM stdin;
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, brand, name) FROM stdin;
9780702305511	Scholastic	Welsh Fairy Tales, Myths and Legends by Claire Fayers (Paperback, 2021)
9781292204482	Pearson Education The Limited	Accounting and Finance: An Introduction 9th edition by Peter Atrill, Eddie McLaney (Paperback, 2018)
9781788307017	Olympia Publishers	Vegans Deserve Better than a Fruit Salad by Danielle Maupertuis (Paperback, 2020)
9781942275732	Zenescope Entertainment	The Black Sable by Joe Brusha (Paperback, 2018)
9781941610428	Fons Vitae,US	The Prophetic Ethics and the Courtesies of Living by Al-Ghazali (Paperback, 2019)
9781617137082	Hal Leonard Corporation	My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)
9781617137080	Hal Leonard Corporation	My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)
9781617137083	Hal Leonard Corporation	My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)
9781617137084	Hal Leonard Corporation	My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)
5707286436169	Nissens	Nissens 95601 Dryer Air Conditioning OE Replacement Top Quality
9786420945091	Mercedes-Benz	MERCEDES CLS 320 CDI C219 2007 RHD Air Intake Hose Pipe Tube A6420945097
5030917124013	Sony	PlayStation 3 Ps3 Destiny Vanguard Edition UK IMPORT a VideoGames
\.


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.profiles (email, name, surname) FROM stdin;
mario.rossi@gmail.com	mario	rossi
luigi.verdi@gmail.com	luigi	verdi
sergio.bianchi@gmail.com	sergio	bianchi
\.


--
-- Data for Name: statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.statuses (id, status, status_changer, "timestamp", expert_email, ticket_id) FROM stdin;
1	1	MANAGER	1999-01-08 01:05:06	mastro.gesualdo@tickets.com	1
2	2	EXPERT	2000-02-18 05:25:46	mohamed.letija@tickets.com	2
3	3	PROFILE	2006-06-13 22:21:46	pino.paolino@tickets.com	3
\.


--
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tickets (id, obj, priority, arg_field, expert_email, product_id, profile_email, last_read_message_index_expert, last_read_message_index_profile) FROM stdin;
1	obj	LOW	WRONG-DELIVERY	mastro.gesualdo@tickets.com	9781292204482	mario.rossi@gmail.com	0	0
2	i need help	HIGH	COMPUTER	mohamed.letija@tickets.com	9786420945091	mario.rossi@gmail.com	0	0
3	you ruined my birthday	MEDIUM	ELECTRONIC	pino.paolino@tickets.com	9781617137080	luigi.verdi@gmail.com	0	0
\.


--
-- Name: attachments_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.attachments_seq', 1, false);


--
-- Name: experts_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.experts_seq', 1, false);


SELECT pg_catalog.setval('public.addresses_seq', 1, false);


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
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);


--
-- Name: attachments attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_pkey PRIMARY KEY (id);


--
-- Name: expert_expertise expert_expertise_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expert_expertise
    ADD CONSTRAINT expert_expertise_pkey PRIMARY KEY (expert_email, expertise_field);


--
-- Name: expertises expertises_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expertises
    ADD CONSTRAINT expertises_pkey PRIMARY KEY (field);


--
-- Name: experts experts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.experts
    ADD CONSTRAINT experts_pkey PRIMARY KEY (email);


--
-- Name: managers managers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.managers
    ADD CONSTRAINT managers_pkey PRIMARY KEY (email);


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
-- Name: messages fk3jvl9mj8r5ao5vlfgq6gsnnyt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk3jvl9mj8r5ao5vlfgq6gsnnyt FOREIGN KEY (expert_email) REFERENCES public.experts(email);


--
-- Name: tickets fk4vad8oe6n0d1yflqkd4sj13as; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fk4vad8oe6n0d1yflqkd4sj13as FOREIGN KEY (arg_field) REFERENCES public.expertises(field);


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
-- Name: statuses fk7c2j14r2cneccbwhv1idkoex5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.statuses
    ADD CONSTRAINT fk7c2j14r2cneccbwhv1idkoex5 FOREIGN KEY (expert_email) REFERENCES public.experts(email);


--
-- Name: tickets fk9774jyfebsw99i91o35yaucxd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fk9774jyfebsw99i91o35yaucxd FOREIGN KEY (expert_email) REFERENCES public.experts(email);


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
-- Name: addresses fki4bubqretjd6r0py8vskj960v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT fki4bubqretjd6r0py8vskj960v FOREIGN KEY (profile_email) REFERENCES public.profiles(email);


--
-- Name: expert_expertise fkmcl7gkeo699n9y0i1gt0x84vi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expert_expertise
    ADD CONSTRAINT fkmcl7gkeo699n9y0i1gt0x84vi FOREIGN KEY (expert_email) REFERENCES public.experts(email);


--
-- Name: expert_expertise fkrh70nqipdmx0hcpx12w5u5fqt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expert_expertise
    ADD CONSTRAINT fkrh70nqipdmx0hcpx12w5u5fqt FOREIGN KEY (expertise_field) REFERENCES public.expertises(field);


--
-- PostgreSQL database dump complete
--


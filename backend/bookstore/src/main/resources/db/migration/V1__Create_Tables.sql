--
-- PostgreSQL database dump
--

-- Dumped from database version 13.6
-- Dumped by pg_dump version 13.6

-- Started on 2023-07-23 23:57:03

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
-- TOC entry 202 (class 1259 OID 237598)
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    id bigint NOT NULL,
    nacionality character varying(255),
    name character varying(255)
);


ALTER TABLE public.author OWNER TO crysisprophet1234;

--
-- TOC entry 201 (class 1259 OID 237596)
-- Name: author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.author_id_seq OWNER TO crysisprophet1234;

--
-- TOC entry 3092 (class 0 OID 0)
-- Dependencies: 201
-- Name: author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_id_seq OWNED BY public.author.id;


--
-- TOC entry 204 (class 1259 OID 237609)
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    id bigint NOT NULL,
    img_url character varying(255),
    name character varying(255),
    release_date date,
    status character varying(255),
    author_id bigint,
    CONSTRAINT book_status_check CHECK ((upper((status)::text) = ANY (ARRAY['AVAILABLE'::text, 'BOOKED'::text])))
);


ALTER TABLE public.book OWNER TO crysisprophet1234;

--
-- TOC entry 212 (class 1259 OID 237681)
-- Name: book_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_category (
    book_id bigint NOT NULL,
    category_id bigint NOT NULL
);


ALTER TABLE public.book_category OWNER TO crysisprophet1234;

--
-- TOC entry 203 (class 1259 OID 237607)
-- Name: book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_id_seq OWNER TO crysisprophet1234;

--
-- TOC entry 3093 (class 0 OID 0)
-- Dependencies: 203
-- Name: book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_id_seq OWNED BY public.book.id;


--
-- TOC entry 205 (class 1259 OID 237619)
-- Name: book_reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_reservation (
    reservation_id bigint NOT NULL,
    book_id bigint NOT NULL
);


ALTER TABLE public.book_reservation OWNER TO crysisprophet1234;

--
-- TOC entry 215 (class 1259 OID 237708)
-- Name: bookstore_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookstore_user (
    id bigint NOT NULL,
    email character varying(255),
    firstname character varying(255),
    lastname character varying(255),
    password character varying(255)
);


ALTER TABLE public.bookstore_user OWNER TO crysisprophet1234;

--
-- TOC entry 214 (class 1259 OID 237706)
-- Name: bookstore_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bookstore_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bookstore_user_id_seq OWNER TO crysisprophet1234;

--
-- TOC entry 3094 (class 0 OID 0)
-- Dependencies: 214
-- Name: bookstore_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bookstore_user_id_seq OWNED BY public.bookstore_user.id;


--
-- TOC entry 207 (class 1259 OID 237626)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.category OWNER TO crysisprophet1234;

--
-- TOC entry 206 (class 1259 OID 237624)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.category_id_seq OWNER TO crysisprophet1234;

--
-- TOC entry 3095 (class 0 OID 0)
-- Dependencies: 206
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;

--
-- TOC entry 209 (class 1259 OID 237634)
-- Name: reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reservation (
    id bigint NOT NULL,
    devolution date,
    moment timestamp(6) with time zone,
    status character varying(255) DEFAULT 'IN_PROGRESS'::character varying,
    weeks integer,
    client_id bigint,
    CONSTRAINT reservation_status_check CHECK ((upper((status)::text) = ANY (ARRAY['IN_PROGRESS'::text, 'FINISHED'::text])))
);


ALTER TABLE public.reservation OWNER TO crysisprophet1234;

--
-- TOC entry 208 (class 1259 OID 237632)
-- Name: reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reservation_id_seq OWNER TO crysisprophet1234;

--
-- TOC entry 3096 (class 0 OID 0)
-- Dependencies: 208
-- Name: reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reservation_id_seq OWNED BY public.reservation.id;


--
-- TOC entry 211 (class 1259 OID 237644)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    authority character varying(255)
);


ALTER TABLE public.role OWNER TO crysisprophet1234;

--
-- TOC entry 210 (class 1259 OID 237642)
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_id_seq OWNER TO crysisprophet1234;

--
-- TOC entry 3097 (class 0 OID 0)
-- Dependencies: 210
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;


--
-- TOC entry 213 (class 1259 OID 237686)
-- Name: user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_role OWNER TO crysisprophet1234;

--
-- TOC entry 2901 (class 2604 OID 237601)
-- Name: author id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author ALTER COLUMN id SET DEFAULT nextval('public.author_id_seq'::regclass);


--
-- TOC entry 2902 (class 2604 OID 237612)
-- Name: book id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book ALTER COLUMN id SET DEFAULT nextval('public.book_id_seq'::regclass);


--
-- TOC entry 2909 (class 2604 OID 237711)
-- Name: bookstore_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookstore_user ALTER COLUMN id SET DEFAULT nextval('public.bookstore_user_id_seq'::regclass);


--
-- TOC entry 2904 (class 2604 OID 237629)
-- Name: category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- TOC entry 2905 (class 2604 OID 237637)
-- Name: reservation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation ALTER COLUMN id SET DEFAULT nextval('public.reservation_id_seq'::regclass);


--
-- TOC entry 2908 (class 2604 OID 237647)
-- Name: role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);


--
-- TOC entry 3073 (class 0 OID 237598)
-- Dependencies: 202
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.author (id, nacionality, name) FROM stdin;
1	United States	Stephen King
2	United States	Robert Cecil Martin
3	United States	H. P. Lovecraft
4	United Kingdom	J. K. Rowling
5	United States	George R. R. Martin
6	Brazil	Ailton Krenak
7	United Kingdom	Jane Austen
8	Russia	Leo Tolstoy
9	Colombia	Gabriel Garcia Marquez
10	Japan	Haruki Murakami
11	United States	Toni Morrison
12	Argentina	Jorge Luis Borges
13	United Kingdom	Agatha Christie
14	United Kingdom	Charles Dickens
15	United Kingdom	Virginia Woolf
16	Russia	Fyodor Dostoevsky
17	United States	Mark Twain
18	Ireland	Oscar Wilde
19	United Kingdom	J. R. R. Tolkien
20	United States	Ernest Hemingway
21	Japan	Yukio Mishima
22	Nigeria	Chimamanda Ngozi Adichie
23	Peru	Mario Vargas Llosa
24	United States	Edgar Allan Poe
25	Chile	Isabel Allende
26	United States	Harper Lee
27	India	George Orwell
28	United States	Francis Scott Fitzgerald
29	United States	Suzanne Collins
30	United States	Dan Brown
\.


--
-- TOC entry 3075 (class 0 OID 237609)
-- Dependencies: 204
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (id, img_url, name, release_date, status, author_id) FROM stdin;
1	https://m.media-amazon.com/images/I/51RbpshZ-LL._SY344_BO1,204,203,200_QL70_ML2_.jpg	A Game of Thrones	1996-08-06	AVAILABLE	5
3	https://2.bp.blogspot.com/-B6ErMjbhToQ/VZBQ5WGwq-I/AAAAAAAALVg/Kd6uIhJREbU/s1600/A%2BClash%2Bof%2BKings%2BUS.jpg	A Clash of Kings	1998-11-16	AVAILABLE	5
4	https://lwcurrey.cdn.bibliopolis.com/pictures/158627.jpg?auto=webp&v=1682012228	A Storm of Swords	2000-08-08	AVAILABLE	5
5	https://3.bp.blogspot.com/-GWIkKM6oEHI/VZO1XcIhnvI/AAAAAAAALXA/q00JnU6qDMs/s1600/Original%2BAFFC%2BUSA.jpg	A Feast for Crows	2005-11-08	AVAILABLE	5
6	https://upload.wikimedia.org/wikipedia/en/5/5d/A_Dance_With_Dragons_US.jpg	A Dance with Dragons	2011-07-12	AVAILABLE	5
7	https://m.media-amazon.com/images/I/41j-s9fHJcL.jpg	To Kill a Mockingbird	1960-07-11	AVAILABLE	26
8	https://m.media-amazon.com/images/I/51HSkTKlauL._SY344_BO1,204,203,200_QL70_ML2_.jpg	Harry Potter and the Sorcerers Stone	1997-06-26	AVAILABLE	4
9	https://m.media-amazon.com/images/I/519zR2oIlmL._SY344_BO1,204,203,200_QL70_ML2_.jpg	George Orwell	1944-06-08	AVAILABLE	27
10	https://m.media-amazon.com/images/I/81RE22MUk7L._AC_UF1000,1000_QL80_.jpg	Pride and Prejudice	1813-01-28	AVAILABLE	7
11	https://m.media-amazon.com/images/I/41JFqILaXyL._SY344_BO1,204,203,200_QL70_ML2_.jpg	The Great Gatsby	1925-04-10	AVAILABLE	28
12	https://m.media-amazon.com/images/I/614SwlZNtJL._AC_UF1000,1000_QL80_.jpg	The Hunger Games	2008-09-14	AVAILABLE	29
13	https://www.vjbooks.com/v/vspfiles/photos/BRODAVI01-2.jpg	The Da Vinci Code	2003-03-18	AVAILABLE	30
14	https://m.media-amazon.com/images/I/51uYlDqoIyL._SY344_BO1,204,203,200_QL70_ML2_.jpg	The Hobbit	1937-09-21	AVAILABLE	19
15	https://m.media-amazon.com/images/I/41ksNxnzTIL._SY344_BO1,204,203,200_QL70_ML2_.jpg	The Shining	1977-01-28	AVAILABLE	1
\.


--
-- TOC entry 3083 (class 0 OID 237681)
-- Dependencies: 212
-- Data for Name: book_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_category (book_id, category_id) FROM stdin;
1	2
1	18
1	19
1	16
3	2
3	18
3	19
3	16
4	2
4	18
4	19
4	16
5	2
5	18
5	19
5	16
6	2
6	18
6	19
6	16
7	6
7	15
8	2
8	19
9	34
9	6
9	44
10	15
11	6
11	15
12	18
12	44
12	16
13	1
13	4
14	2
14	19
15	3
\.


--
-- TOC entry 3076 (class 0 OID 237619)
-- Dependencies: 205
-- Data for Name: book_reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_reservation (reservation_id, book_id) FROM stdin;
\.


--
-- TOC entry 3086 (class 0 OID 237708)
-- Dependencies: 215
-- Data for Name: bookstore_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bookstore_user (id, email, firstname, lastname, password) FROM stdin;
1	leo@gmail.com	Leonardo	Poletto	$2a$10$51lUi1qFTkZOb6nuZBp5YOsBL8e.JRKPoLw8be.0HglRrQEfBq9YG
3	operator@gmail.com	Operator	Rat Dude	$2a$10$gGIf2BY0TAQD1og12oZWS.cEkuZ0xvgU9Yrj2VRZ3fP1oc8uK6WkG
2	customer@gmail.com	Customer	Amelia Hart	$2a$10$LysYs3qKKD2S1d4Fadi7ie7AjoCgeTLtj/xZcUFn4JknFoVCEprVK
\.


--
-- TOC entry 3078 (class 0 OID 237626)
-- Dependencies: 207
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.category (id, name) FROM stdin;
1	thriller
2	fantasy
3	horror
4	mystery
5	crime
6	historical fiction
7	non-fiction
8	biography
9	autobiography
10	self-help
11	health and wellness
12	travel
13	cookbooks
14	poetry
15	romance
16	drama
17	thriller
18	action
19	adventure
20	comedy
21	animation
22	family and children
23	documentary
24	war
25	musical
26	western
27	sports
28	science and nature
29	art and photography
30	religion and spirituality
31	philosophy
32	business and economics
33	technology and computing
34	politics and current affairs
35	psychology
36	education
37	environmental and sustainability
38	lifestyle and fashion
39	food and drink
40	gardening
41	home and DIY
42	true crime
43	supernatural and paranormal
44	dystopian
45	post-apocalyptic
46	steampunk
47	cyberpunk
48	urban fantasy
49	paranormal romance
50	historical romance
\.

--
-- TOC entry 3080 (class 0 OID 237634)
-- Dependencies: 209
-- Data for Name: reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reservation (id, devolution, moment, status, weeks, client_id) FROM stdin;
\.


--
-- TOC entry 3082 (class 0 OID 237644)
-- Dependencies: 211
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, authority) FROM stdin;
1	ROLE_CUSTOMER
2	ROLE_OPERATOR
3	ROLE_ADMIN
\.


--
-- TOC entry 3084 (class 0 OID 237686)
-- Dependencies: 213
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_role (user_id, role_id) FROM stdin;
1	1
1	2
1	3
2	1
3	1
3	2
\.


--
-- TOC entry 3098 (class 0 OID 0)
-- Dependencies: 201
-- Name: author_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.author_id_seq', 30, true);


--
-- TOC entry 3099 (class 0 OID 0)
-- Dependencies: 203
-- Name: book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_id_seq', 15, true);


--
-- TOC entry 3100 (class 0 OID 0)
-- Dependencies: 214
-- Name: bookstore_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bookstore_user_id_seq', 3, true);


--
-- TOC entry 3101 (class 0 OID 0)
-- Dependencies: 206
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_id_seq', 50, true);


--
-- TOC entry 3102 (class 0 OID 0)
-- Dependencies: 208
-- Name: reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reservation_id_seq', 1, true);


--
-- TOC entry 3103 (class 0 OID 0)
-- Dependencies: 210
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- TOC entry 2914 (class 2606 OID 237606)
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- TOC entry 2926 (class 2606 OID 237685)
-- Name: book_category book_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT book_category_pkey PRIMARY KEY (book_id, category_id);


--
-- TOC entry 2916 (class 2606 OID 237618)
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- TOC entry 2918 (class 2606 OID 237623)
-- Name: book_reservation book_reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reservation
    ADD CONSTRAINT book_reservation_pkey PRIMARY KEY (book_id, reservation_id);


--
-- TOC entry 2930 (class 2606 OID 237716)
-- Name: bookstore_user bookstore_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookstore_user
    ADD CONSTRAINT bookstore_user_pkey PRIMARY KEY (id);


--
-- TOC entry 2920 (class 2606 OID 237631)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);

--
-- TOC entry 2922 (class 2606 OID 237641)
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- TOC entry 2924 (class 2606 OID 237649)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 2932 (class 2606 OID 237718)
-- Name: bookstore_user uk_f50ccgwqw31bip6958vg6xnte; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookstore_user
    ADD CONSTRAINT uk_f50ccgwqw31bip6958vg6xnte UNIQUE (email);


--
-- TOC entry 2928 (class 2606 OID 237690)
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);

--
-- TOC entry 2934 (class 2606 OID 237655)
-- Name: book_reservation fk7ruw0lbuveh2dxgwof1di4uns; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reservation
    ADD CONSTRAINT fk7ruw0lbuveh2dxgwof1di4uns FOREIGN KEY (reservation_id) REFERENCES public.reservation(id);


--
-- TOC entry 2939 (class 2606 OID 237701)
-- Name: user_role fka68196081fvovjhkek5m97n3y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- TOC entry 2937 (class 2606 OID 237691)
-- Name: book_category fkam8llderp40mvbbwceqpu6l2s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fkam8llderp40mvbbwceqpu6l2s FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- TOC entry 2935 (class 2606 OID 237660)
-- Name: book_reservation fkbged23jof7k60tviumkmoyaqf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reservation
    ADD CONSTRAINT fkbged23jof7k60tviumkmoyaqf FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- TOC entry 2936 (class 2606 OID 237719)
-- Name: reservation fkf5r5geg30938x24xygcpfl2vy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fkf5r5geg30938x24xygcpfl2vy FOREIGN KEY (client_id) REFERENCES public.bookstore_user(id);


--
-- TOC entry 2933 (class 2606 OID 237650)
-- Name: book fkklnrv3weler2ftkweewlky958; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fkklnrv3weler2ftkweewlky958 FOREIGN KEY (author_id) REFERENCES public.author(id);


--
-- TOC entry 2938 (class 2606 OID 237696)
-- Name: book_category fknyegcbpvce2mnmg26h0i856fd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fknyegcbpvce2mnmg26h0i856fd FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- TOC entry 2940 (class 2606 OID 237724)
-- Name: user_role fksxl1y4ngmpajvk064witk8uw4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fksxl1y4ngmpajvk064witk8uw4 FOREIGN KEY (user_id) REFERENCES public.bookstore_user(id);


-- Completed on 2023-07-23 23:57:03

--
-- PostgreSQL database dump complete
--
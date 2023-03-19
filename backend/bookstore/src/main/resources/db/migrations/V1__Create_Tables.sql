--
-- PostgreSQL database dump
--

-- Dumped from database version 13.6
-- Dumped by pg_dump version 13.6

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
-- Name: tb_author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_author (
    id bigint NOT NULL,
    nacionality character varying(255),
    name character varying(255)
);


ALTER TABLE tb_author OWNER TO postgres;

--
-- Name: tb_author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tb_author_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tb_author_id_seq OWNER TO postgres;

--
-- Name: tb_author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tb_author_id_seq OWNED BY tb_author.id;


--
-- Name: tb_book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_book (
    id bigint NOT NULL,
    img_url character varying(255),
    name character varying(255),
    release_date date,
    status character varying(255),
    author_id bigint,
    CONSTRAINT tb_book_status_check CHECK ((upper((status)::text) = ANY (ARRAY['AVAILABLE'::text, 'BOOKED'::text])))
);


ALTER TABLE tb_book OWNER TO postgres;

--
-- Name: tb_book_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_book_category (
    book_id bigint NOT NULL,
    category_id bigint NOT NULL
);


ALTER TABLE tb_book_category OWNER TO postgres;

--
-- Name: tb_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tb_book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tb_book_id_seq OWNER TO postgres;

--
-- Name: tb_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tb_book_id_seq OWNED BY tb_book.id;


--
-- Name: tb_book_reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_book_reservation (
    book_id bigint NOT NULL,
    reservation_id bigint NOT NULL
);


ALTER TABLE tb_book_reservation OWNER TO postgres;

--
-- Name: tb_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_category (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE tb_category OWNER TO postgres;

--
-- Name: tb_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tb_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tb_category_id_seq OWNER TO postgres;

--
-- Name: tb_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tb_category_id_seq OWNED BY tb_category.id;


--
-- Name: tb_reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_reservation (
    id bigint NOT NULL,
    devolution date,
    moment timestamp(6) with time zone,
    status character varying(255) DEFAULT 'IN_PROGRESS'::character varying,
    weeks integer,
    client_id bigint,
    CONSTRAINT tb_reservation_status_check CHECK ((upper((status)::text) = ANY (ARRAY['IN_PROGRESS'::text, 'FINISHED'::text])))
);


ALTER TABLE tb_reservation OWNER TO postgres;

--
-- Name: tb_reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tb_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tb_reservation_id_seq OWNER TO postgres;

--
-- Name: tb_reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tb_reservation_id_seq OWNED BY tb_reservation.id;


--
-- Name: tb_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_role (
    id bigint NOT NULL,
    authority character varying(255)
);


ALTER TABLE tb_role OWNER TO postgres;

--
-- Name: tb_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tb_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tb_role_id_seq OWNER TO postgres;

--
-- Name: tb_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tb_role_id_seq OWNED BY tb_role.id;


--
-- Name: tb_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_user (
    id bigint NOT NULL,
    email character varying(255),
    firstname character varying(255),
    lastname character varying(255),
    password character varying(255)
);


ALTER TABLE tb_user OWNER TO postgres;

--
-- Name: tb_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tb_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tb_user_id_seq OWNER TO postgres;

--
-- Name: tb_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tb_user_id_seq OWNED BY tb_user.id;


--
-- Name: tb_user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tb_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE tb_user_role OWNER TO postgres;

--
-- Name: tb_author id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_author ALTER COLUMN id SET DEFAULT nextval('tb_author_id_seq'::regclass);


--
-- Name: tb_book id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book ALTER COLUMN id SET DEFAULT nextval('tb_book_id_seq'::regclass);


--
-- Name: tb_category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_category ALTER COLUMN id SET DEFAULT nextval('tb_category_id_seq'::regclass);


--
-- Name: tb_reservation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_reservation ALTER COLUMN id SET DEFAULT nextval('tb_reservation_id_seq'::regclass);


--
-- Name: tb_role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_role ALTER COLUMN id SET DEFAULT nextval('tb_role_id_seq'::regclass);


--
-- Name: tb_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_user ALTER COLUMN id SET DEFAULT nextval('tb_user_id_seq'::regclass);


--
-- Name: tb_author tb_author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_author
    ADD CONSTRAINT tb_author_pkey PRIMARY KEY (id);


--
-- Name: tb_book_category tb_book_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book_category
    ADD CONSTRAINT tb_book_category_pkey PRIMARY KEY (book_id, category_id);


--
-- Name: tb_book tb_book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book
    ADD CONSTRAINT tb_book_pkey PRIMARY KEY (id);


--
-- Name: tb_book_reservation tb_book_reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book_reservation
    ADD CONSTRAINT tb_book_reservation_pkey PRIMARY KEY (book_id, reservation_id);


--
-- Name: tb_category tb_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_category
    ADD CONSTRAINT tb_category_pkey PRIMARY KEY (id);


--
-- Name: tb_reservation tb_reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_reservation
    ADD CONSTRAINT tb_reservation_pkey PRIMARY KEY (id);


--
-- Name: tb_role tb_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_role
    ADD CONSTRAINT tb_role_pkey PRIMARY KEY (id);


--
-- Name: tb_user tb_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_user
    ADD CONSTRAINT tb_user_pkey PRIMARY KEY (id);


--
-- Name: tb_user_role tb_user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_user_role
    ADD CONSTRAINT tb_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: tb_user uk_4vih17mube9j7cqyjlfbcrk4m; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_user
    ADD CONSTRAINT uk_4vih17mube9j7cqyjlfbcrk4m UNIQUE (email);


--
-- Name: tb_book_category fk3liuw1v2in16w46rnehxfumi5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book_category
    ADD CONSTRAINT fk3liuw1v2in16w46rnehxfumi5 FOREIGN KEY (book_id) REFERENCES tb_book(id);


--
-- Name: tb_book_reservation fk4n0fp84kx7iy0t2e9y1ao9s5m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book_reservation
    ADD CONSTRAINT fk4n0fp84kx7iy0t2e9y1ao9s5m FOREIGN KEY (book_id) REFERENCES tb_book(id);


--
-- Name: tb_user_role fk7vn3h53d0tqdimm8cp45gc0kl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_user_role
    ADD CONSTRAINT fk7vn3h53d0tqdimm8cp45gc0kl FOREIGN KEY (user_id) REFERENCES tb_user(id);


--
-- Name: tb_book fkc27yltypnyytr71q1a0vdg8w9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book
    ADD CONSTRAINT fkc27yltypnyytr71q1a0vdg8w9 FOREIGN KEY (author_id) REFERENCES tb_author(id);


--
-- Name: tb_user_role fkea2ootw6b6bb0xt3ptl28bymv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_user_role
    ADD CONSTRAINT fkea2ootw6b6bb0xt3ptl28bymv FOREIGN KEY (role_id) REFERENCES tb_role(id);


--
-- Name: tb_book_reservation fkf8vkjtl4pfkm3lkqbs8387utw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book_reservation
    ADD CONSTRAINT fkf8vkjtl4pfkm3lkqbs8387utw FOREIGN KEY (reservation_id) REFERENCES tb_reservation(id);


--
-- Name: tb_reservation fkpi88hvmc5v2e1s352y0bis1lu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_reservation
    ADD CONSTRAINT fkpi88hvmc5v2e1s352y0bis1lu FOREIGN KEY (client_id) REFERENCES tb_user(id);


--
-- Name: tb_book_category fkso210qv50xliaohfbvei17mps; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_book_category
    ADD CONSTRAINT fkso210qv50xliaohfbvei17mps FOREIGN KEY (category_id) REFERENCES tb_category(id);


--
-- PostgreSQL database dump complete
--


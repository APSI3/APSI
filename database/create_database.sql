SET client_encoding = 'UTF8';

CREATE ROLE apsi WITH LOGIN PASSWORD 'apsi';


CREATE TABLE public.users (
    id integer NOT NULL,
    login character varying(128) NOT NULL
);


ALTER TABLE public.users OWNER TO apsi;


CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO apsi;
ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

------------------------------------------------------------------------

insert into public.users(login)
values ('testuser1');


------------------------------------------------------------------------

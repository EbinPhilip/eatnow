-- Table: public.address

-- DROP TABLE IF EXISTS public.address;

CREATE TABLE IF NOT EXISTS public.address
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    user_id text COLLATE pg_catalog."default",
    index integer,
    address character varying(255) COLLATE pg_catalog."default",
    latitude double precision,
    longitude double precision
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.address
    OWNER to postgres;
-- Index: address_user_id_idx

-- DROP INDEX IF EXISTS public.address_user_id_idx;

CREATE UNIQUE INDEX IF NOT EXISTS address_user_id_idx
    ON public.address USING btree
    (user_id COLLATE pg_catalog."default" ASC NULLS LAST, index ASC NULLS LAST)
    TABLESPACE pg_default;
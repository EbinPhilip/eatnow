-- Table: public.order

-- DROP TABLE IF EXISTS public."order";

CREATE TABLE IF NOT EXISTS public."order"
(
    id uuid NOT NULL,
    user_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    address character varying(255) COLLATE pg_catalog."default" NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    items json NOT NULL,
    total double precision NOT NULL,
    transaction_id uuid,
    time_stamp timestamp without time zone NOT NULL,
    status integer NOT NULL,
    restaurant_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT order_pk PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."order"
    OWNER to postgres;
-- Index: order_restaurantid_idx

-- DROP INDEX IF EXISTS public.order_restaurantid_idx;

CREATE INDEX IF NOT EXISTS order_restaurantid_idx
    ON public."order" USING btree
    (restaurant_id COLLATE pg_catalog."default" ASC NULLS LAST, time_stamp DESC NULLS FIRST)
    TABLESPACE pg_default;
-- Index: order_transaction_id_idx

-- DROP INDEX IF EXISTS public.order_transaction_id_idx;

CREATE UNIQUE INDEX IF NOT EXISTS order_transaction_id_idx
    ON public."order" USING btree
    (transaction_id ASC NULLS LAST)
    TABLESPACE pg_default;
-- Index: order_user_id_idx

-- DROP INDEX IF EXISTS public.order_user_id_idx;

CREATE INDEX IF NOT EXISTS order_user_id_idx
    ON public."order" USING btree
    (user_id COLLATE pg_catalog."default" ASC NULLS LAST, time_stamp DESC NULLS FIRST)
    TABLESPACE pg_default;
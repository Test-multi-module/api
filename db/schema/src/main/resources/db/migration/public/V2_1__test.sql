create table  users(
    id uuid NOT NULL,
    nick_name character varying(64) NOT NULL,
    day_of_birth timestamptz
);

ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

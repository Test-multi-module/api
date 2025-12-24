create table  users(
    id uuid NOT NULL,
    nick_name character varying(64) NOT NULL,
    day_of_birth timestamptz,
    auth_user_id UUID UNIQUE NOT NULL,
    provider_avatar_url text
);

ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE users ADD CONSTRAINT users_fkey
    FOREIGN KEY (auth_user_id) REFERENCES auth.users(id) ON DELETE CASCADE;

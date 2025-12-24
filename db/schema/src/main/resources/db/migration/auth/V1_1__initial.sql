CREATE TABLE users (
    id uuid NOT NULL,

    created_at timestamptz  NOT NULL default now(),
    updated_at timestamptz  NOT NULL default now(),

    disabled boolean NOT NULL DEFAULT false,
    deleted boolean NOT NULL DEFAULT false,

    email character varying(255),
    authorization_type integer
);

ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

CREATE TABLE oauth2_accounts(
    id uuid NOT NULL,
    user_id uuid NOT NULL,

    provider varchar(32),
    provider_user_id varchar(255) NOT NULL,
    email_at_provider varchar(254) NOT NULL,

    created_at timestamptz  NOT NULL default now(),
    last_login_at timestamptz NOT NULL default now(),

    provider_avatar_url text
);

ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_pkey PRIMARY KEY (id);
ALTER TABLE oauth2_accounts
    ADD CONSTRAINT oauth2_accounts_fkey FOREIGN KEY (user_id) REFERENCES users(id);

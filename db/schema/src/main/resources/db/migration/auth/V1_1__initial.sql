CREATE TABLE users (
    id uuid NOT NULL,
    profile_completed boolean NOT NULL
);

ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

CREATE TABLE oauth2_accounts(
    id uuid NOT NULL,
    user_id uuid NOT NULL,

    provider varchar(32) NOT NULL,
    provider_user_id varchar(255) NOT NULL,

    email_at_provider varchar(254) NOT NULL,
    email_verified boolean NOT NULL,

    given_name text,
    family_name text
);

ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_pkey
    PRIMARY KEY (id);
ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_fkey
    FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_uq_provider_provider_user_id
    UNIQUE(provider, provider_user_id);

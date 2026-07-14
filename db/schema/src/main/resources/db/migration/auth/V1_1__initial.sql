CREATE TABLE auth_users (
    id uuid NOT NULL,
    profile_completed boolean NOT NULL,
    email varchar(254) NOT NULL,
    password_hash varchar(255),
    email_verified boolean NOT NULL
);

ALTER TABLE auth_users ADD CONSTRAINT auth_users_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX auth_users_uq_email_lower ON auth_users (LOWER(email));

CREATE TABLE oauth2_accounts(
    id uuid NOT NULL,
    user_id uuid NOT NULL,

    provider varchar(32) NOT NULL,
    provider_user_id varchar(255) NOT NULL,
    email_at_provider varchar(254) NOT NULL,

    given_name text,
    family_name text
);

ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_pkey
    PRIMARY KEY (id);
ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_fkey
    FOREIGN KEY (user_id) REFERENCES auth_users(id);
ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_uq_provider_provider_user_id
    UNIQUE(provider, provider_user_id);

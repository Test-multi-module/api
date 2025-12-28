CREATE TABLE users (
    id uuid NOT NULL,

    created_at timestamptz  NOT NULL default now(),
    updated_at timestamptz  NOT NULL default now(),
    disabled_at timestamptz,
    deleted_at timestamptz,

    email character varying(255)
);

ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

CREATE TABLE oauth2_accounts(
    id uuid NOT NULL,
    user_id uuid NOT NULL,

    provider varchar(32),
    provider_user_id varchar(255) NOT NULL,
    email_at_provider varchar(254) NOT NULL,

    email_verified boolean NOT NULL default FALSE,

    created_at timestamptz  NOT NULL default now(),
    updated_at timestamptz  NOT NULL default now(),
    disabled_at timestamptz,
    deleted_at timestamptz,

    last_login_at timestamptz,

    provider_avatar_url text,
    given_name text,
    family_name text
);

ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_pkey
    PRIMARY KEY (id);
ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_fkey
    FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE oauth2_accounts ADD CONSTRAINT oauth2_accounts_uq_provider_provider_user_id
    UNIQUE(provider, provider_user_id);
ALTER TABLE oauth2_accounts ADD CONSTRAINT  oauth2_accounts_uq_provider_user_id
    UNIQUE(provider, user_id);

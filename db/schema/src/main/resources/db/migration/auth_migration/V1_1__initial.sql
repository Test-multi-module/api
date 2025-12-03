CREATE TABLE auth_users (
    id uuid NOT NULL PRIMARY KEY ,

    created timestamp NOT NULL,
    updated timestamp NOT NULL,

    disabled boolean NOT NULL DEFAULT false,
    activated boolean NOT NULL DEFAULT  false,

    role integer NOT NULL,

    login character varying(100),
    password character varying(255),
    email character varying(255)
);

CREATE TYPE confirmation_token_status AS ENUM ('PENDING', 'CONFIRMED', 'EXPIRED');

CREATE TABLE email_confirmation_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    token UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    status confirmation_token_status DEFAULT 'PENDING'
);
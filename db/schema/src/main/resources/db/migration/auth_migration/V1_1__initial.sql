CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE auth.auth_users (
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
CREATE TYPE confirmation_token_status AS ENUM ('PENDING', 'CONFIRMED', 'EXPIRED');

CREATE TABLE email_confirmation_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    token UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    status confirmation_token_status DEFAULT 'PENDING'
);
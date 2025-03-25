create table if not exists users(
    id uuid NOT NULL PRIMARY KEY,
    nick_name character varying(255) NOT NULL,
    day_of_birth timestamp,
    auth_user_id UUID UNIQUE NOT NULL,
    FOREIGN KEY (auth_user_id) REFERENCES auth.auth_users(id) ON DELETE CASCADE
)
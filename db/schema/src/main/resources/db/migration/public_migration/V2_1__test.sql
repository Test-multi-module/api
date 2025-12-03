create table  users(
    id uuid NOT NULL PRIMARY KEY,
    nick_name character varying(255) NOT NULL,
    day_of_birth timestamp,
    auth_user_id UUID UNIQUE NOT NULL,
    FOREIGN KEY (auth_user_id) REFERENCES auth.auth_users(id) ON DELETE CASCADE
)

create table  products(
    id uuid NOT NULL PRIMARY KEY,
    title character varying(100) NOT NULL,
    price integer,
    description character varying(1024),
    categories character varying(512) UNIQUE NOT NULL,
    rating integer
)
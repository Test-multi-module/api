create table if not exists auth_users(
    id serial primary key,
    username varchar(255),
    password varchar(255)
)
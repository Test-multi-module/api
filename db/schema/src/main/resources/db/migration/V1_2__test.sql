create table if not exists users(
    id serial primary key,
    username varchar(255),
    password varchar(255))
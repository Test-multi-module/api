create table if not exists product(
    id serial primary key ,
    categories varchar(255),
    description varchar(255),
    price int,
    rating int,
    title varchar(255)
);

create table if not exists review(
    id serial primary key ,
    comment varchar(255),
    product_id int,
    rating int,
    timestamp varchar(255),
    user_name varchar(255),
    CONSTRAINT FK_DEVICE_ATTESTATIONS_DEVICES FOREIGN KEY (product_id) REFERENCES product
);

create table if not exists users(
    id serial primary key,
    username varchar(255),
    password varchar(255)
)
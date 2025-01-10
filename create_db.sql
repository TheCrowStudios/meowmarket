show databases;
create database meowmarketdb;
use meowmarketdb;

create table listings (
id int auto_increment primary key,
title varchar(80) not null,
description varchar(8000) not null,
quantityInStock int default 1,
created_at timestamp default current_timestamp,
removed_at timestamp default null
);

create table listingImages (
foreign key(listingId) references listings(id),
imagePath varchar(255) not null
);

show tables
create table user_
(
    id        uuid primary key,
    full_name varchar(512) not null,
    role      varchar(32)  not null,
    group_id  uuid         references group_ (id) on delete set null
);
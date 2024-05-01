create table user_
(
    id        uuid primary key,
    is_active boolean,
    group_id  uuid         references group_ (id) on delete set null,
    email varchar(255) not null unique,
    full_name varchar(255) not null,
    password  varchar(255)
);

create table user_role
(
    user_id uuid references user_ (id) on delete cascade,
    role    varchar(255)
);

create table user_refresh_token
(
    user_id       uuid          not null references user_ (id) on delete cascade,
    refresh_token varchar(1024) not null
);
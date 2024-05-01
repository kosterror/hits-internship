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

insert into user_(id, is_active, group_id, email, full_name, password)
values ('7d62bd15-b200-4602-823a-1a1577bc1dad', true, null, 'change-me@domain.com', 'Dean Officer',
        '$2a$10$ycpRrU66eD4P80oAcD.xnOIWku/kSuVx0eePRpbdVJO31Q3GR0yMq');

insert into user_role
    (user_id, role)
values ('7d62bd15-b200-4602-823a-1a1577bc1dad', 'DEAN_OFFICER');

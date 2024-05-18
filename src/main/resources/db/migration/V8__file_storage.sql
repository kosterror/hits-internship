create table file_meta_info
(
    id               uuid primary key,
    bucket           varchar(255) not null,
    name             varchar(255) not null,
    size             float        not null,
    upload_date_time timestamp    not null,
    owner_id         uuid         not null references user_ (id) on delete cascade
);
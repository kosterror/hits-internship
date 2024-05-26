create table meeting
(
    id          uuid primary key,
    pair_number varchar(32)  not null,
    day_of_week varchar(32)  not null,
    date        date         not null,
    audience    varchar(255) not null,
    company_id  uuid         not null references company (id) on delete cascade
);

create table meeting_group
(
    meeting_id uuid references meeting (id) on delete cascade,
    group_id   uuid references group_ (id) on delete cascade
);

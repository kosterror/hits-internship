create table solution
(
    id        uuid primary key,
    comment   varchar(256),
    mark      int,
    date_time timestamp   not null,
    state     varchar(16) not null,
    author_id uuid references user_ (id) on delete cascade,
    task_id   uuid references task (id) on delete cascade
);

create table solution_file
(
    solution_id uuid references solution (id) on delete cascade,
    file_id     uuid references file_meta_info (id) on delete cascade
);
create table task
(
    id          uuid primary key,
    name        varchar(255) not null,
    description varchar(10000),
    created_at  timestamp    not null,
    deadline    timestamp    not null,
    author_id   uuid references user_ (id),
    semester_id uuid references semester (id)
);

create table task_file
(
    task_id uuid references task (id) on delete cascade,
    file_id uuid references file_meta_info (id) on delete cascade
);

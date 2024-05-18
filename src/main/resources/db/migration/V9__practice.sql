create table semester
(
    id                                  uuid primary key,
    start_date                          date         not null,
    end_date                            date         not null,
    change_company_application_deadline timestamp    not null,
    number                              integer      not null,
    study_year                          varchar(255) not null
);

create table change_practice_application
(
    id                   uuid primary key,
    comment              varchar(255),
    checking_employee_id uuid         references user_ (id) on delete set null,
    author_id            uuid         references user_ (id) on delete set null,
    semester_id          uuid         references semester (id) on delete set null,
    company_id           uuid         references company (id) on delete set null,
    last_updated_date    timestamp    not null,
    creation_date        timestamp    not null,
    not_partner          varchar(255),
    status               varchar(255) not null
);


create table practice
(
    id          uuid primary key,
    comment     varchar(255),
    user_id     uuid references user_ (id) on delete set null,
    semester_id uuid references semester (id) on delete set null,
    company_id  uuid references company (id) on delete set null
);
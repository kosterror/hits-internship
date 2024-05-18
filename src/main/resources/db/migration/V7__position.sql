create table position
(
    id                  uuid primary key,
    priority            integer not null,
    position_status     varchar(255),
    program_language_id uuid    references program_language (id) on delete set null,
    speciality_id       uuid    references speciality (id) on delete set null,
    company_id          uuid    references company (id) on delete set null,
    user_id             uuid    references user_ (id) on delete set null
);

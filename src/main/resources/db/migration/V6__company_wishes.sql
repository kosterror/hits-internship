create table company_wishes
(
    id                  uuid primary key,
    intern_amount       varchar(255),
    comment             varchar(255),
    program_language_id uuid references program_language (id) on delete set null,
    speciality_id       uuid references speciality (id) on delete set null
);
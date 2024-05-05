create table company
(
    id           uuid primary key,
    name         varchar(255) not null,
    website_link varchar(255),
    is_visible   bit          not null
);
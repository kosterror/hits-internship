alter table company
    add column curator_id uuid references user_ (id);

alter table company
    add column officer_id uuid references user_ (id);
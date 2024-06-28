alter table solution
    alter column mark set data type varchar(255) using (
        case mark
            when 0 then 'ZERO'
            when 1 then 'ONE'
            when 2 then 'TWO'
            when 3 then 'THREE'
            when 4 then 'FOUR'
            when 5 then 'FIVE'
            else 'FIVE_P'
            end
        );
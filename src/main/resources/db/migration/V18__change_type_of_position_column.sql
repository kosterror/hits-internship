alter table position
    alter column position_status set data type smallint using (
        case position_status
            when 'DOESNT_DO_ANYTHING' then 1
            when 'ARRANGED_INTERVIEW' then 2
            when 'PASSED_INTERVIEW' then 3
            when 'RECEIVED_OFFER' then 4
            when 'ACCEPTED_OFFER' then 5
            when 'NOT_RECEIVED_OFFER' then 6
            when 'REJECTED_OFFER' then 7
            when 'CONFIRMED_RECEIVED_OFFER' then 8
            else 0
            end
        );

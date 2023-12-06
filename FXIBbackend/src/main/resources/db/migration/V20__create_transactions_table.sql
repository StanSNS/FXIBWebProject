CREATE TABLE IF NOT EXISTS transactions (
    id bigint not null auto_increment,
    amount varchar(255) not null,
    billing_date varchar(255) not null,
    card varchar(255) not null,
    description varchar(255) not null,
    duration varchar(255) not null,
    email_sent bit not null,
    end_of_billing_date varchar(255) not null,
    receipt varchar(255) not null,
    status varchar(255) not null,
    user_email varchar(255) not null,
    user_entity_id bigint,
    primary key (id)
)
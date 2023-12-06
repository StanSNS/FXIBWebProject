CREATE TABLE IF NOT EXISTS users (
    id bigint not null auto_increment,
    agreed_to_terms bit not null,
    biography varchar(95) not null,
    email varchar(36) not null,
    jwt_token varchar(255),
    number_of_logins integer,
    password varchar(255) not null,
    registration_date varchar(255) not null,
    reset_token varchar(255),
    subscription varchar(255) not null,
    two_factor_login_code integer,
    username varchar(10) not null,
    liked_answer_id bigint,
    location_entity_id bigint,
    primary key (id)
)
CREATE TABLE IF NOT EXISTS partners (
    id bigint not null auto_increment,
    fifth_line varchar(255) not null,
    first_line varchar(255) not null,
    fourth_line varchar(255) not null,
    linkurl varchar(255) not null,
    second_line varchar(255) not null,
    seventh_line varchar(255) not null,
    sixth_line varchar(255) not null,
    third_line varchar(255) not null,
    title varchar(255) not null,
    primary key (id)
)
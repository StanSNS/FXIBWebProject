CREATE TABLE IF NOT EXISTS answers (
    id bigint not null auto_increment,
    content TEXT not null,
    date varchar(255) not null,
    deleted bit not null,
    vote_count bigint not null,
    writer varchar(10) not null,
    primary key (id)
)
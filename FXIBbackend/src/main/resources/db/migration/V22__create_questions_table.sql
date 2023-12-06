CREATE TABLE IF NOT EXISTS questions (
    id bigint not null auto_increment,
    content TEXT not null,
    date varchar(255) not null,
    deleted bit not null,
    solved bit not null,
    writer varchar(10) not null,
    topic_entity_id bigint,
    primary key (id)
)
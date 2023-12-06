CREATE TABLE IF NOT EXISTS liked_answers (
    id bigint not null auto_increment,
    answerid bigint not null,
    deleted bit not null,
    username varchar(10) not null,
    primary key (id)
)
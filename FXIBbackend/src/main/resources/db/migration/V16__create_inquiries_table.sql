CREATE TABLE IF NOT EXISTS inquiries (
    id bigint not null auto_increment,
    content TEXT,
    custom_id varchar(255),
    date varchar(255),
    title varchar(50),
    user_entity_id bigint,
    primary key (id)
)
CREATE TABLE IF NOT EXISTS locations (
    id bigint not null auto_increment,
    city varchar(255),
    continent varchar(255),
    country varchar(255),
    country_flagurl varchar(255),
    ip varchar(255),
    username varchar(255),
    primary key (id)
)
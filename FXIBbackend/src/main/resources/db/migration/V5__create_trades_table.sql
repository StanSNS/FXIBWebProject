CREATE TABLE IF NOT EXISTS trades (
    id bigint not null auto_increment,
    action varchar(255),
    close_time varchar(255),
    commission float(53),
    open_time varchar(255),
    pips float(53),
    profit float(53),
    symbol varchar(255),
    primary key (id)
)
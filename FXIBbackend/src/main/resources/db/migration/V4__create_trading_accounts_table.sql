CREATE TABLE IF NOT EXISTS trading_accounts (
    id bigint not null auto_increment,
    account_identity bigint,
    balance float(53),
    creation_date varchar(255),
    currency varchar(255),
    deposits float(53),
    equity float(53),
    first_trade_date varchar(255),
    last_update_date varchar(255),
    profit float(53),
    response_identity bigint,
    server varchar(255),
    primary key (id)
)
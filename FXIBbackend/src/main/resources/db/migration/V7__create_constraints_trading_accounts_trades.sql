ALTER TABLE  trading_accounts_trades
    ADD CONSTRAINT FKfqlen8nks9ub0k14nbnspcp5j
        FOREIGN KEY (trading_account_entity_id)
            REFERENCES trading_accounts (id);

alter table trading_accounts_trades
    add constraint FKfypltu4t9yb287cylehf6t0t
        foreign key (trades_id)
            references trades (id)


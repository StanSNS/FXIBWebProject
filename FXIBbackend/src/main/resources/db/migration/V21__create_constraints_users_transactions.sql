ALTER TABLE transactions
    ADD CONSTRAINT FKqw776v1pbxsla2xwxj2vrrm4d
        FOREIGN KEY (user_entity_id)
            REFERENCES users (id)
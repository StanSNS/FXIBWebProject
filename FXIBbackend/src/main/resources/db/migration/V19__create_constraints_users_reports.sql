ALTER TABLE reports
    ADD CONSTRAINT FKnrnim1475vtiha5yjnbbvek4h
        FOREIGN KEY (user_entity_id)
            REFERENCES users (id)
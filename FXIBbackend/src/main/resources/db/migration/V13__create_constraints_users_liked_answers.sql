ALTER TABLE users
    ADD CONSTRAINT FKhnm7t1m2idfbr1uom902vinkj
        FOREIGN KEY (liked_answer_id)
            REFERENCES liked_answers (id);
ALTER TABLE users_questions
    ADD CONSTRAINT FKa8lfneiym681t1jjq0gg5n3o9
        FOREIGN KEY (questions_id)
            REFERENCES questions (id);

ALTER TABLE users_questions
    ADD CONSTRAINT FKn2cj77n1217fm0fh02auh2mcj
        FOREIGN KEY (user_entity_id)
            REFERENCES users (id)
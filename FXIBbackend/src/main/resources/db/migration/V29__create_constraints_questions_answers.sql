ALTER TABLE questions_answers
    ADD CONSTRAINT FKjd36ut570uokat27seb4kfv6b
        FOREIGN KEY (answers_id)
            REFERENCES answers (id);

ALTER TABLE questions_answers
    ADD CONSTRAINT FKb1o9itu4gmbs8wfbhayffbj38
        FOREIGN KEY (question_entity_id)
            REFERENCES questions (id);
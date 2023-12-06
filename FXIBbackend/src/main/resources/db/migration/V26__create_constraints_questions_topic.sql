ALTER TABLE questions
    ADD CONSTRAINT FK1a55elojm6i9g8lh77utdg7ws
        FOREIGN KEY (topic_entity_id)
            REFERENCES topics (id);
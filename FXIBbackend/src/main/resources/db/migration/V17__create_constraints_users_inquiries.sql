ALTER TABLE inquiries
    ADD CONSTRAINT FKou65cb119els6dfp8g32bm74j
        FOREIGN KEY (user_entity_id)
            REFERENCES users (id);
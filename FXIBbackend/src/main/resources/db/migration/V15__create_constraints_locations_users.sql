ALTER TABLE users
    ADD CONSTRAINT FKodbqkyqo2r7332pao0ls9vt4h
        FOREIGN KEY (location_entity_id)
            REFERENCES locations (id);
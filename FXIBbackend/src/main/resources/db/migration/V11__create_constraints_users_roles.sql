ALTER TABLE users_roles
    ADD CONSTRAINT FKj6m8fwv7oqv74fcehir1a9ffy
        FOREIGN KEY (role_id)
            REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT FK2o0jvgh89lemvvo17cbqvdxaa
        FOREIGN KEY (user_id)
            REFERENCES users (id);
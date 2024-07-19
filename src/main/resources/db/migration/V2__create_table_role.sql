CREATE SEQUENCE IF NOT EXISTS role_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS role (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('role_id_seq'),
    name VARCHAR(255)
);

INSERT INTO role (name) VALUES ('conta_select');
INSERT INTO role (name) VALUES ('conta_insert');
INSERT INTO role (name) VALUES ('conta_update');
INSERT INTO role (name) VALUES ('conta_delete');

CREATE TABLE IF NOT EXISTS role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- PARA O H2 TROCAR O NEXTVAL POR AUTO_INCREMENT
    name VARCHAR(255)
);

INSERT INTO role (name) VALUES ('conta_select');
INSERT INTO role (name) VALUES ('conta_insert');
INSERT INTO role (name) VALUES ('conta_update');
INSERT INTO role (name) VALUES ('conta_delete');

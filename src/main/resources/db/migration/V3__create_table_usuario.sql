CREATE SEQUENCE IF NOT EXISTS usuario_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('usuario_id_seq'),
    username VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usuario_roles (
   usuario_id BIGINT,
   roles_id BIGINT
);


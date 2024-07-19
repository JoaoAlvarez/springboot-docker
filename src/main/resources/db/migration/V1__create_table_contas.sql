CREATE SEQUENCE IF NOT EXISTS conta_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS contas (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('conta_id_seq'),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(19, 2) NOT NULL,
    descricao VARCHAR(255),
    situacao VARCHAR(50)
);


CREATE INDEX IF NOT EXISTS idx_data_vencimento ON contas(data_vencimento);
CREATE INDEX IF NOT EXISTS idx_situacao ON contas(situacao);

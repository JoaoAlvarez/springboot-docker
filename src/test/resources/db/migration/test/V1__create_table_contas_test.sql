CREATE TABLE IF NOT EXISTS contas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- PARA O H2 TROCAR O NEXTVAL POR AUTO_INCREMENT
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(19, 2) NOT NULL,
    descricao VARCHAR(255),
    situacao VARCHAR(50)
);


CREATE INDEX IF NOT EXISTS idx_data_vencimento ON contas(data_vencimento);
CREATE INDEX IF NOT EXISTS idx_situacao ON contas(situacao);

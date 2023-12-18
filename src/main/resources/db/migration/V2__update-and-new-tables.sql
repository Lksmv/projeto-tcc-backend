CREATE TABLE forma_de_pagamento
(
    id_forma_pagamento SERIAL PRIMARY KEY,
    nome               VARCHAR(100) NOT NULL,
    taxa               DOUBLE PRECISION,
    ativo          CHAR
);

INSERT INTO forma_de_pagamento (id_forma_pagamento,nome,taxa,ativo)
VALUES (999,'credito',0,'F');

CREATE TABLE Pagamento
(
    id_pagamento          SERIAL PRIMARY KEY,
    id_aluguel INTEGER REFERENCES aluguel (id_aluguel),
    valor                 DOUBLE PRECISION,
    data                  DATE,
    id_forma_de_pagamento INTEGER REFERENCES forma_de_pagamento (id_forma_pagamento)
);


CREATE TABLE Categoria
(
    id_categoria SERIAL PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    ativo          CHAR
);

ALTER TABLE Produto
    ADD COLUMN id_categoria INTEGER REFERENCES categoria (id_categoria),
    ADD COLUMN traje_vendido CHAR,
    ADD COLUMN quantidade INTEGER;

CREATE TABLE Funcionario
(
    id_funcionario SERIAL PRIMARY KEY,
    nome           VARCHAR(100),
    ativo          CHAR
);

ALTER TABLE Aluguel
    ADD COLUMN patrocinio CHAR,
    DROP COLUMN valor_pago,
    DROP COLUMN id_usuario,
    ADD COLUMN id_funcionario INTEGER REFERENCES Funcionario (id_funcionario);


CREATE TABLE Credito
(
    id_credito  SERIAL PRIMARY KEY,
    id_cliente INTEGER REFERENCES cliente (id_cliente),
    data       DATE,
    valor             DOUBLE PRECISION,
    observacoes VARCHAR(500)
);

ALTER TABLE Cliente
    DROP COLUMN credito,
    DROP COLUMN credito_observacoes;

CREATE TABLE Meta
(
    id_meta     SERIAL PRIMARY KEY,
    nome        VARCHAR(30),
    data_inicio DATE,
    data_fim    DATE,
    valor       DOUBLE PRECISION,
    ativo          CHAR
);





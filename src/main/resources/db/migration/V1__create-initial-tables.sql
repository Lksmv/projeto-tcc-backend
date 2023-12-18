CREATE TABLE cargo
(
    id_cargo SERIAL PRIMARY KEY,
    nome     VARCHAR(100) NOT NULL
);

-- Inserção dos cargos 'admin' e 'funcionario'
INSERT INTO cargo (nome)
VALUES ('ROLE_ADMIN'),
       ('ROLE_FUNCIONARIO');

--Tabela Cliente
CREATE TABLE Cliente
(
    id_cliente          SERIAL PRIMARY KEY,
    nome                VARCHAR(100),
    cpf                 CHAR(11),
    nascimento          DATE,
    telefone            VARCHAR(14),
    rede_social         VARCHAR(50),
    pessoas_autorizadas VARCHAR(100),
    observacoes         VARCHAR(500),
    CEP                 VARCHAR(9),
    UF                  CHAR(2),
    endereco            VARCHAR(100),
    bairro              VARCHAR(50),
    credito             DOUBLE PRECISION,
    credito_observacoes VARCHAR(500),
    ativo               CHAR
);

-- Tabela Produto
CREATE TABLE Produto
(
    id_produto  SERIAL PRIMARY KEY,
    nome        VARCHAR,
    genero      CHAR,
    tamanho     VARCHAR(5),
    cor         VARCHAR(50),
    marca       VARCHAR(50),
    valor       DOUBLE PRECISION,
    observacoes VARCHAR(500),
    status      INTEGER,
    ativo       CHAR
);

-- Tabela Usuario
CREATE TABLE Usuario
(
    id_usuario SERIAL PRIMARY KEY,
    nome       VARCHAR,
    login      VARCHAR,
    senha      VARCHAR,
    id_cargo   INTEGER REFERENCES cargo (id_cargo),
    ativo      CHAR
);

-- Tabela Aluguel
CREATE TABLE Aluguel
(
    id_aluguel       SERIAL PRIMARY KEY,
    id_cliente       INTEGER REFERENCES Cliente (id_cliente),
    id_usuario       INTEGER REFERENCES usuario (id_usuario),
    data_saida       DATE,
    data_devolucao   DATE,
    data_emissao     DATE,
    valor            DOUBLE PRECISION,
    valor_pago       DOUBLE PRECISION,
    utilizar_credito CHAR,
    observacoes      VARCHAR,
    valor_adicional  DOUBLE PRECISION,
    status           INTEGER,
    ativo            CHAR
);

-- Tabela Aluguel_Produto
CREATE TABLE Aluguel_Produto
(
    status     INTEGER,
    id_aluguel INTEGER REFERENCES Aluguel (id_aluguel),
    id_produto INTEGER REFERENCES Produto (id_produto)
);

-- Tabela Imagem
CREATE TABLE imagem_produto
(
    id_imagem    SERIAL PRIMARY KEY,
    caminho_imagem VARCHAR NOT NULL,
    id_produto   INTEGER REFERENCES produto (id_produto)
);
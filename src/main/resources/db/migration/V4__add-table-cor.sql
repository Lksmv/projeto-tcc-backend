CREATE TABLE Cor
(
    id_cor SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

ALTER TABLE Produto DROP COLUMN cor;

ALTER TABLE Produto
    ADD id_cor INTEGER REFERENCES Cor(id_cor);

INSERT INTO Usuario (nome,login,senha,id_cargo,ativo)
VALUES ('admin','admin','admin123',1,'S');
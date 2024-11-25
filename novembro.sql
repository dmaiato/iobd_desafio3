DROP DATABASE IF EXISTS novembro;


CREATE DATABASE novembro;

\c novembro;

CREATE TABLE veiculo (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(10),
    ano INT
);

CREATE TABLE foto_veiculo (
    id serial primary key,
    foto bytea,
    id_veiculo int references veiculo(id)
);

CREATE TABLE carro (
    numero_portas INT
) INHERITS (veiculo);

CREATE TABLE moto (
    cilindradas INT
) INHERITS (veiculo);

DROP ROLE IF EXISTS adm;
CREATE ROLE adm WITH PASSWORD '123' LOGIN SUPERUSER;

DROP ROLE IF EXISTS estagiario;
CREATE ROLE estagiario WITH PASSWORD '123' LOGIN;
GRANT CONNECT on DATABASE novembro TO estagiario;
GRANT USAGE ON SCHEMA public TO estagiario;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO estagiario;

DROP ROLE IF EXISTS protegido;
CREATE ROLE protegido WITH PASSWORD '123' LOGIN;
GRANT CONNECT on DATABASE novembro TO protegido;
GRANT USAGE ON SCHEMA public TO protegido;
GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA public TO protegido;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO protegido;

-- Inserir dados na superclasse
INSERT INTO veiculo (placa, ano) VALUES ('ABC1234', 2020);

-- Inserir dados nas subclasses
INSERT INTO carro (placa, ano, numero_portas) VALUES ('DEF5678', 2021, 4);

INSERT INTO moto (placa, ano, cilindradas) VALUES ('GHI9012', 2022, 150);

-- Consultar dados da superclasse (inclui dados das subclasses)
SELECT * FROM veiculo;

-- Consultar dados apenas da superclasse
SELECT * FROM ONLY veiculo;

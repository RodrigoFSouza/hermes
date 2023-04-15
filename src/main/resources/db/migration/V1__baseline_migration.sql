CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE usuarios (
     id uuid NOT NULL,
     nome  VARCHAR(250)  NOT NULL,
     email VARCHAR(250)  NOT NULL,
     senha VARCHAR(250)  NOT NULL,
     CONSTRAINT usuarios_pkey PRIMARY KEY (id)
);

INSERT INTO usuarios (id, nome, email, senha) VALUES
(uuid_generate_v4(), 'rodrigo','rodrigo@cronos.com.br', 'teste123'),
(uuid_generate_v4(), 'pedro','pedro@cronos.com.br', 'teste321'),
(uuid_generate_v4(), 'joao','joao@cronos.com.br', 'teste999'),
(uuid_generate_v4(), 'maria','maria@cronos.com.br', 'teste333');

CREATE TABLE lancamentos (
     id uuid NOT NULL,
     mes INT  NOT NULL,
     ano INT  NOT NULL,
     usuario_id uuid  NOT NULL,
     valor NUMERIC(15,2)  NOT NULL,
     data_cadastro TIMESTAMP  NOT NULL,
     CONSTRAINT lancamentos_pkey PRIMARY KEY (id),
     FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
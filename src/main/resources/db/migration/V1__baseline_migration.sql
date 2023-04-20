CREATE TABLE usuarios (
     id uuid NOT NULL,
     nome  VARCHAR(250)  NOT NULL,
     email VARCHAR(250)  NOT NULL,
     senha VARCHAR(250)  NOT NULL,
     CONSTRAINT usuarios_pkey PRIMARY KEY (id)
);

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
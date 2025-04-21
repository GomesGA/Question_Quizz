-- Chave estrangeira de alternativas para perguntas
ALTER TABLE alternativas
ADD CONSTRAINT fk_alternativas_perguntas
FOREIGN KEY(id_perg) REFERENCES perguntas(id_perg)
ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Chave estrangeira de perguntas para tipo_perguntas
ALTER TABLE perguntas
ADD CONSTRAINT fk_perguntas_tipo
FOREIGN KEY(id_tipo) REFERENCES tipo_perguntas(id_tipo)
ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Chave estrangeira de perguntas para usuarios
ALTER TABLE perguntas
ADD CONSTRAINT fk_perguntas_usuarios
FOREIGN KEY(id_usuario) REFERENCES usuarios(id)
ON UPDATE NO ACTION ON DELETE NO ACTION; 
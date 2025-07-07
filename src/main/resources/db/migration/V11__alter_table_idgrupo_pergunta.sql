ALTER TABLE perguntas ADD COLUMN id_grupo INT;

ALTER TABLE perguntas 
ADD CONSTRAINT fk_perguntas_grupo 
FOREIGN KEY (id_grupo) 
REFERENCES grupos(id);
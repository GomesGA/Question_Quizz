-- Inserir tipos de perguntas
INSERT INTO tipo_perguntas (descricao)
VALUES 
    ('Múltipla Escolha'),
    ('Numérico'),
    ('Verdadeiro/Falso'),
    ('Texto Aberto');

COMMENT ON TABLE tipo_perguntas IS 'Inserção dos tipos de perguntas padrão'; 
-- Inserir usuário administrador
INSERT INTO usuarios (nome, email, senha_hash, is_admin)
VALUES (
    'Administrador',
    'admin@questionquizz.com',
    '$2a$12$5twvv392KLCzEE38/TW0/eg2AL.H.IMadEBEgUe65ccJWWgwN/EFa',
    true
);

COMMENT ON TABLE usuarios IS 'Inserção do usuário administrador padrão'; 
package br.com.ufu.question_quizz.dto

data class UsuarioRequestDTO(
    val nome: String,
    val email: String,
    val senha: String
)

data class AtualizarSenhaRequestDTO(
    val email: String,
    val novaSenha: String
) 
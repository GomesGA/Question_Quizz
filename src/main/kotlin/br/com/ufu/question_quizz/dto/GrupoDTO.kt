package br.com.ufu.question_quizz.dto

data class GrupoDTO(
    val descricao: String,
    val imagemPath: String,
    val usuarioId: Int
)

data class GrupoDeleteDTO(
    val id: Int,
    val usuarioId: Int
)
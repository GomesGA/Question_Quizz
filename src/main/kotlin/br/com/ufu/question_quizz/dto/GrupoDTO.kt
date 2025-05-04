package br.com.ufu.question_quizz.dto

data class GrupoDTO(
    val id: Int? = null,
    val descricao: String,
    val imagemPath: String,
    val usuarioId: Int
)
package br.com.ufu.question_quizz.dto

data class GrupoResponseDTO(
    val id: Int,
    val descricao: String,
    val path: String,
    val idUsuario: Int?
)

data class GrupoDeleteResponseDTO(
    val id: Int,
    val descricao: String
)
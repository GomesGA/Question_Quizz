package br.com.ufu.question_quizz.dto

import java.math.BigDecimal

data class PerguntaRequestDTO(
    val idTipo: Int,
    val idUsuario: Int,
    val pergunta: String,
    val privada: Boolean? = false,
    val resposta: RespostaDTO
)

data class RespostaDTO(
    val alternativas: List<AlternativaRequestDTO>? = null,
    val texto: String? = null,
    val numero: BigDecimal? = null,
    val booleano: Boolean? = null
)

data class AlternativaRequestDTO(
    val descricao: String,
    val correta: Boolean
) 
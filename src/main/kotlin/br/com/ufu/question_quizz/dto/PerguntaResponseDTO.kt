package br.com.ufu.question_quizz.dto

import java.math.BigDecimal

data class PerguntaResponseDTO(
    val id: Int,
    val idUsuario: Int,
    val gabaritoTexto: String?,
    val gabaritoNumero: BigDecimal?,
    val gabaritoBooleano: Boolean?,
    val alternativas: List<AlternativaResponseDTO>?
) 
package br.com.ufu.question_quizz.model

import jakarta.persistence.*

@Entity
@Table(name = "tipo_perguntas")
data class TipoPergunta(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    val id: Int? = null,

    @Column(nullable = false, length = 255)
    val descricao: String
) 
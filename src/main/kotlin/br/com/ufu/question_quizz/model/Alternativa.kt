package br.com.ufu.question_quizz.model

import jakarta.persistence.*

@Entity
@Table(name = "alternativas")
data class Alternativa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alter")
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_perg", nullable = false)
    val pergunta: Pergunta,

    @Column(nullable = false)
    val correta: Boolean = false,

    @Column(nullable = false, length = 255)
    val descricao: String
)
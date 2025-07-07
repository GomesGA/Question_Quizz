package br.com.ufu.question_quizz.model

import jakarta.persistence.*

@Entity
@Table(name = "grupos")
data class Grupo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false)
    val descricao: String,

    @Column(name = "path")
    val imagemPath: String,

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    val usuario: Usuario? = null // Pode ser null para grupos padrão
)
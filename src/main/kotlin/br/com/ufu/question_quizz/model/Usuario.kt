package br.com.ufu.question_quizz.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, length = 255)
    val nome: String,

    @Column(nullable = false, length = 255, unique = true)
    val email: String,

    @Column(name = "senha_hash", nullable = false, length = 255)
    var senhaHash: String,

    @Column(name = "dt_criacao")
    val dataCriacao: LocalDateTime = LocalDateTime.now(),

    @Column(name = "dt_atualizacao")
    val dataAtualizacao: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_ativo", nullable = false)
    val ativo: Boolean = true,

    @Column(name = "is_admin")
    val admin: Boolean = false
) 
package br.com.ufu.question_quizz.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "perguntas")
data class Pergunta(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perg")
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    val usuario: Usuario? = null,

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    val tipo: TipoPergunta,

    @Column(nullable = false, length = 255)
    val pergunta: String,

    @Column(name = "is_private")
    val privada: Boolean? = null,

    @Column(name = "gabarito_txt", length = 255)
    val gabaritoTexto: String? = null,

    @Column(name = "gabarito_num", precision = 19, scale = 2)
    val gabaritoNumero: BigDecimal? = null,

    @Column(name = "gabarito_bool")
    val gabaritoBooleano: Boolean? = null
) 
package br.com.ufu.question_quizz.repository

import br.com.ufu.question_quizz.model.Alternativa
import org.springframework.data.jpa.repository.JpaRepository

interface AlternativaRepository : JpaRepository<Alternativa, Int> {
    fun findByPerguntaId(idPergunta: Int): List<Alternativa>
} 
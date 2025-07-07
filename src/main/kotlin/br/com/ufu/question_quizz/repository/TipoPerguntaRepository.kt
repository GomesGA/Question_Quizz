package br.com.ufu.question_quizz.repository

import br.com.ufu.question_quizz.model.TipoPergunta
import org.springframework.data.jpa.repository.JpaRepository

interface TipoPerguntaRepository : JpaRepository<TipoPergunta, Int> {
}

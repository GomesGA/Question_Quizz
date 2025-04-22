package br.com.ufu.question_quizz.repository

import br.com.ufu.question_quizz.model.Pergunta
import org.springframework.data.jpa.repository.JpaRepository

interface PerguntasRepository : JpaRepository<Pergunta, Int> {
}



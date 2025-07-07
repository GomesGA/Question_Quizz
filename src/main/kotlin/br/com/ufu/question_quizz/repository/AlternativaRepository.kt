package br.com.ufu.question_quizz.repository

import br.com.ufu.question_quizz.model.Alternativa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface AlternativaRepository : JpaRepository<Alternativa, Int> {
    fun findByPerguntaId(idPergunta: Int): List<Alternativa>
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Alternativa a WHERE a.pergunta.id = :idPergunta")
    fun deleteByPerguntaId(@Param("idPergunta") idPergunta: Int)
} 
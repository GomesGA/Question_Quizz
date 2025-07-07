package br.com.ufu.question_quizz.repository

import br.com.ufu.question_quizz.model.Grupo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GrupoRepository : JpaRepository<Grupo, Int> {
    fun findByUsuarioIdOrUsuarioIsNull(usuarioId: Int): List<Grupo>
} 
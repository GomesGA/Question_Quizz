package br.com.ufu.question_quizz.repository

import org.springframework.data.jpa.repository.JpaRepository
import br.com.ufu.question_quizz.model.Usuario
import java.util.Optional

interface UsuarioRepository : JpaRepository<Usuario, Int> {
    fun findByEmail(email: String): Optional<Usuario>
}

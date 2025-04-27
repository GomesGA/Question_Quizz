package br.com.ufu.question_quizz.repository

import br.com.ufu.question_quizz.model.Pergunta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PerguntasRepository : JpaRepository<Pergunta, Int> {
    
    /**
     * Busca perguntas com base em filtros opcionais de ID do usuário e ID da pergunta.
     * 
     * A consulta segue a seguinte lógica:
     * 
     * 1. Se nenhum parâmetro for fornecido (idUsuario = null e idPergunta = null):
     *    - Retorna apenas perguntas públicas (privada = false) ou com privada = null
     * 
     * 2. Se apenas o idUsuario for fornecido (idUsuario != null e idPergunta = null):
     *    - Retorna todas as perguntas do usuário, independente de serem públicas ou privadas
     * 
     * 3. Se apenas o idPergunta for fornecido (idUsuario = null e idPergunta != null):
     *    - Retorna a pergunta específica, independente de ser pública ou privada
     * 
     * 4. Se ambos os parâmetros forem fornecidos (idUsuario != null e idPergunta != null):
     *    - Retorna a pergunta específica do usuário
     * 
     * @param idUsuario ID do usuário (opcional)
     * @param idPergunta ID da pergunta (opcional)
     * @return Lista de perguntas que atendem aos critérios de busca
     */
    @Query("""
        SELECT p FROM Pergunta p 
        WHERE (
            (:idUsuario IS NULL AND :idPergunta IS NULL AND (p.privada = false OR p.privada IS NULL))
            OR
            (:idUsuario IS NOT NULL AND :idPergunta IS NULL AND p.usuario.id = :idUsuario)
            OR
            (:idUsuario IS NULL AND :idPergunta IS NOT NULL AND p.id = :idPergunta)
            OR
            (:idUsuario IS NOT NULL AND :idPergunta IS NOT NULL AND p.usuario.id = :idUsuario AND p.id = :idPergunta)
        )
    """)
    fun findByFiltros(@Param("idUsuario") idUsuario: Int?, @Param("idPergunta") idPergunta: Int?): List<Pergunta>
}



package br.com.ufu.question_quizz.repository

import br.com.ufu.question_quizz.model.Pergunta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PerguntasRepository : JpaRepository<Pergunta, Int> {
    
    /**
     * Busca perguntas com base em filtros opcionais de ID do usuário, ID da pergunta e ID do grupo.
     * 
     * A consulta segue a seguinte lógica:
     * 
     * 1. Se nenhum parâmetro for fornecido (idUsuario = null, idPergunta = null e idGrupo = null):
     *    - Retorna apenas perguntas públicas (privada = false) ou com privada = null
     * 
     * 2. Se apenas o idUsuario for fornecido:
     *    - Retorna todas as perguntas do usuário, independente de serem públicas ou privadas
     * 
     * 3. Se apenas o idPergunta for fornecido:
     *    - Retorna a pergunta específica, independente de ser pública ou privada
     * 
     * 4. Se apenas o idGrupo for fornecido:
     *    - Retorna todas as perguntas do grupo
     * 
     * 5. Se múltiplos parâmetros forem fornecidos:
     *    - Retorna as perguntas que atendem a todos os critérios especificados
     * 
     * @param idUsuario ID do usuário (opcional)
     * @param idPergunta ID da pergunta (opcional)
     * @param idGrupo ID do grupo (opcional)
     * @return Lista de perguntas que atendem aos critérios de busca
     */
    @Query("""
        SELECT p FROM Pergunta p 
        WHERE (
            (:idUsuario IS NULL AND :idPergunta IS NULL AND :idGrupo IS NULL AND (p.privada = false OR p.privada IS NULL))
            OR
            (:idUsuario IS NOT NULL AND p.usuario.id = :idUsuario)
            OR
            (:idPergunta IS NOT NULL AND p.id = :idPergunta)
            OR
            (:idGrupo IS NOT NULL AND p.grupo.id = :idGrupo)
            OR
            (:idUsuario IS NOT NULL AND :idPergunta IS NOT NULL AND p.usuario.id = :idUsuario AND p.id = :idPergunta)
            OR
            (:idUsuario IS NOT NULL AND :idGrupo IS NOT NULL AND p.usuario.id = :idUsuario AND p.grupo.id = :idGrupo)
            OR
            (:idPergunta IS NOT NULL AND :idGrupo IS NOT NULL AND p.id = :idPergunta AND p.grupo.id = :idGrupo)
            OR
            (:idUsuario IS NOT NULL AND :idPergunta IS NOT NULL AND :idGrupo IS NOT NULL 
                AND p.usuario.id = :idUsuario AND p.id = :idPergunta AND p.grupo.id = :idGrupo)
        )
    """)
    fun findByFiltros(
        @Param("idUsuario") idUsuario: Int?,
        @Param("idPergunta") idPergunta: Int?,
        @Param("idGrupo") idGrupo: Int?
    ): List<Pergunta>
}



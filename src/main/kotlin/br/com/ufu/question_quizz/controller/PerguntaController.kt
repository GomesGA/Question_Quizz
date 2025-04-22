package br.com.ufu.question_quizz.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import br.com.ufu.question_quizz.dto.TipoPerguntaResponseDTO
import br.com.ufu.question_quizz.repository.TipoPerguntaRepository
import org.springframework.http.ResponseEntity
import br.com.ufu.question_quizz.dto.ApiResponse
import br.com.ufu.question_quizz.model.TipoPergunta
import org.slf4j.LoggerFactory
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

/**
 * Controlador responsável por gerenciar as operações relacionadas a perguntas.
 * 
 * Este controlador fornece endpoints para manipulação de perguntas e seus tipos.
 * Todas as operações são acessíveis através do prefixo '/api/pergunta'.
 *
 * @property tipoPerguntaRepository Repositório para acesso aos dados de tipos de pergunta
 */
@Tag(name = "Perguntas", description = "API para gerenciamento de perguntas e seus tipos")
@RestController
@RequestMapping("/api/pergunta")
class PerguntaController (
    private val tipoPerguntaRepository: TipoPerguntaRepository
) {
    private val logger = LoggerFactory.getLogger(PerguntaController::class.java)

    /**
     * Retorna uma lista de todos os tipos de perguntas disponíveis no sistema.
     * 
     * Este endpoint retorna uma lista de tipos de perguntas com seus respectivos IDs e descrições.
     * A resposta é encapsulada em um objeto ApiResponse contendo:
     * - success: indica se a operação foi bem-sucedida
     * - data: lista de tipos de perguntas
     * - message: mensagem de erro (se houver)
     *
     * @return ResponseEntity contendo a lista de tipos de perguntas ou mensagem de erro
     * @throws Exception se ocorrer algum erro durante a consulta ao banco de dados
     */
    @Operation(
        summary = "Listar tipos de pergunta",
        description = "Retorna uma lista de todos os tipos de perguntas disponíveis no sistema"
    )
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Tipos de pergunta listados com sucesso"),
        SwaggerApiResponse(responseCode = "400", description = "Erro ao listar tipos de pergunta")
    ])
    @GetMapping("/tipos")
    fun listarTiposPergunta(): ResponseEntity<ApiResponse<List<TipoPerguntaResponseDTO>>> {
        try {
            val tiposPergunta = tipoPerguntaRepository.findAll()
            logger.info("Encontrados ${tiposPergunta.size} tipos de pergunta")
            
            val tiposPerguntaDTO = tiposPergunta.mapNotNull { tipo ->
                tipo.id?.let { id ->
                    TipoPerguntaResponseDTO(
                        id = id,
                        descricao = tipo.descricao
                    )
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success(tiposPerguntaDTO))
        } catch (e: Exception) {
            logger.error("Erro ao listar tipos de pergunta", e)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Erro ao listar tipos de pergunta: ${e.message}"))
        }
    }
}


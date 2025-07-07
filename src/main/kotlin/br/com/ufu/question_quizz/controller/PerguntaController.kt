package br.com.ufu.question_quizz.controller

import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import br.com.ufu.question_quizz.dto.*
import org.springframework.http.ResponseEntity
import br.com.ufu.question_quizz.dto.ApiResponse
import br.com.ufu.question_quizz.service.PerguntaService
import org.slf4j.LoggerFactory
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestBody
import jakarta.validation.Valid
import org.springframework.http.HttpStatus

/**
 * Controlador responsável por gerenciar as operações relacionadas a perguntas.
 * 
 * Este controlador fornece endpoints para manipulação de perguntas e seus tipos.
 * Todas as operações são acessíveis através do prefixo '/api/pergunta'.
 */
@Tag(name = "Perguntas", description = "API para gerenciamento de perguntas e seus tipos")
@RestController
@RequestMapping("/api/pergunta")
class PerguntaController(
    private val perguntaService: PerguntaService
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
            val tiposPergunta = perguntaService.listarTiposPergunta()
            logger.info("Encontrados ${tiposPergunta.size} tipos de pergunta")
            return ResponseEntity.ok(ApiResponse.success(tiposPergunta))
        } catch (e: Exception) {
            logger.error("Erro ao listar tipos de pergunta", e)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Erro ao listar tipos de pergunta: ${e.message}"))
        }
    }

    /**
     * Cria uma nova pergunta no sistema.
     * 
     * Este endpoint permite a criação de perguntas com diferentes tipos de gabarito:
     * - Múltipla escolha: através do campo alternativas
     * - Numérico: através do campo gabaritoNumero
     * - Verdadeiro/Falso: através do campo gabaritoBooleano
     * - Texto aberto: através do campo gabaritoTexto
     *
     * @param perguntaRequest DTO contendo os dados da pergunta a ser criada
     * @return ResponseEntity contendo a pergunta criada ou mensagem de erro
     */
    @Operation(
        summary = "Criar pergunta",
        description = "Cria uma nova pergunta no sistema com seu respectivo gabarito"
    )
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Pergunta criada com sucesso"),
        SwaggerApiResponse(responseCode = "400", description = "Erro ao criar pergunta")
    ])
    @PostMapping
    fun criarPergunta(@Valid @RequestBody perguntaRequest: PerguntaRequestDTO): ResponseEntity<ApiResponse<PerguntaResponseDTO>> {
        try {
            val pergunta = perguntaService.criarPergunta(perguntaRequest)
            logger.info("Pergunta criada com sucesso: ${pergunta.id}")
            return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                    message = "Pergunta criada com sucesso",
                    data = pergunta
                )
            )
        } catch (e: Exception) {
            logger.error("Erro ao criar pergunta", e)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Erro ao criar pergunta: ${e.message}"))
        }
    }

    @Operation(
        summary = "Buscar perguntas",
        description = "Busca perguntas com filtros opcionais de ID do usuário, ID da pergunta e ID do grupo"
    )
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Perguntas encontradas com sucesso"),
        SwaggerApiResponse(responseCode = "400", description = "Erro ao buscar perguntas")
    ])
    @GetMapping
    fun buscarPerguntas(
        @RequestParam(required = false) idUsuario: Int?,
        @RequestParam(required = false) idPergunta: Int?,
        @RequestParam(required = false) idGrupo: Int?
    ): ResponseEntity<ApiResponse<List<PerguntaResponseDTO>>> {
        try {
            val perguntas = perguntaService.buscarPerguntas(idUsuario, idPergunta, idGrupo)
            logger.info("Encontradas ${perguntas.size} perguntas")
            return ResponseEntity.ok(ApiResponse.success(perguntas))
        } catch (e: Exception) {
            logger.error("Erro ao buscar perguntas", e)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Erro ao buscar perguntas: ${e.message}"))
        }
    }

    @Operation(
        summary = "Deletar pergunta",
        description = "Remove uma pergunta do sistema, validando se ela pertence ao usuário"
    )
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Pergunta deletada com sucesso"),
        SwaggerApiResponse(responseCode = "400", description = "Erro ao deletar pergunta"),
        SwaggerApiResponse(responseCode = "404", description = "Pergunta não encontrada")
    ])
    @DeleteMapping
    fun deletarPergunta(@Valid @RequestBody perguntaDeleteDTO: PerguntaDeleteDTO): ResponseEntity<ApiResponse<String>> {
        try {
            val mensagem = perguntaService.deletarPergunta(perguntaDeleteDTO)
            logger.info(mensagem)
            return ResponseEntity.ok(
                ApiResponse.success(
                    message = "Pergunta deletada com sucesso",
                    data = mensagem
                )
            )
        } catch (e: Exception) {
            logger.error("Erro ao deletar pergunta", e)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Erro ao deletar pergunta: ${e.message}"))
        }
    }
}


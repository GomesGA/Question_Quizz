package br.com.ufu.question_quizz.controller

import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import br.com.ufu.question_quizz.dto.*
import br.com.ufu.question_quizz.repository.*
import org.springframework.http.ResponseEntity
import br.com.ufu.question_quizz.dto.ApiResponse
import br.com.ufu.question_quizz.model.*
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
 *
 * @property tipoPerguntaRepository Repositório para acesso aos dados de tipos de pergunta
 * @property repoAlternativa Repositório para acesso aos dados de alternativas
 */
@Tag(name = "Perguntas", description = "API para gerenciamento de perguntas e seus tipos")
@RestController
@RequestMapping("/api/pergunta")
class PerguntaController(
    private val tipoPerguntaRepository: TipoPerguntaRepository,
    private val perguntasRepository: PerguntasRepository,
    private val usuarioRepository: UsuarioRepository,
    private val alternativaRepository: AlternativaRepository,
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
            perguntaService.validarPergunta(perguntaRequest)

            val tipoPergunta = tipoPerguntaRepository.findById(perguntaRequest.idTipo)
                .orElseThrow { IllegalArgumentException("Tipo de pergunta não encontrado") }

            val usuario = usuarioRepository.findById(perguntaRequest.idUsuario)
                .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

            val pergunta = Pergunta(
                tipo = tipoPergunta,
                usuario = usuario,
                pergunta = perguntaRequest.pergunta,
                privada = perguntaRequest.privada,
                gabaritoTexto = perguntaRequest.resposta.texto,
                gabaritoNumero = perguntaRequest.resposta.numero,
                gabaritoBooleano = perguntaRequest.resposta.booleano
            )

            val perguntaSalva = perguntasRepository.save(pergunta)

            var alternativasResponse: List<AlternativaResponseDTO>? = null

            // Se houver alternativas, salva-as
            perguntaRequest.resposta.alternativas?.let { alternativas ->
                val alternativasSalvas = alternativas.map { alt ->
                    Alternativa(
                        pergunta = perguntaSalva,
                        descricao = alt.descricao,
                        correta = alt.correta
                    )
                }
                val alternativasSalvasComIds = alternativaRepository.saveAll(alternativasSalvas)
                
                // Cria a lista de alternativas com os IDs
                alternativasResponse = alternativasSalvasComIds.map { alt ->
                    AlternativaResponseDTO(
                        id = alt.id!!,
                        descricao = alt.descricao,
                        correta = alt.correta
                    )
                }
            }

            val response = PerguntaResponseDTO(
                id = perguntaSalva.id!!.toInt(),
                idUsuario = usuario.id!!.toInt(),
                gabaritoTexto = perguntaSalva.gabaritoTexto,
                gabaritoNumero = perguntaSalva.gabaritoNumero,
                gabaritoBooleano = perguntaSalva.gabaritoBooleano,
                alternativas = alternativasResponse
            )

            logger.info("Pergunta criada com sucesso: ${perguntaSalva.id}")
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                message = "Pergunta criada com sucesso",
                data = response
            ))
        } catch (e: Exception) {
            logger.error("Erro ao criar pergunta", e)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Erro ao criar pergunta: ${e.message}"))
        }
    }

    @Operation(
        summary = "Buscar perguntas",
        description = "Busca perguntas com filtros opcionais de ID do usuário e ID da pergunta"
    )
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Perguntas encontradas com sucesso"),
        SwaggerApiResponse(responseCode = "400", description = "Erro ao buscar perguntas")
    ])
    @GetMapping
    fun buscarPerguntas(
        @RequestParam(required = false) idUsuario: Int?,
        @RequestParam(required = false) idPergunta: Int?
    ): ResponseEntity<ApiResponse<List<PerguntaResponseDTO>>> {
        try {
            val perguntas = perguntasRepository.findByFiltros(idUsuario, idPergunta)
            
            val perguntasResponse = perguntas.map { pergunta ->
                val alternativas = try {
                    alternativaRepository.findByPerguntaId(pergunta.id ?: throw IllegalArgumentException("ID da pergunta não pode ser nulo"))
                } catch (e: Exception) {
                    emptyList()
                }
                
                PerguntaResponseDTO(
                    id = pergunta.id ?: throw IllegalArgumentException("ID da pergunta não pode ser nulo"),
                    idUsuario = pergunta.usuario?.id ?: throw IllegalArgumentException("ID do usuário não pode ser nulo"),
                    gabaritoTexto = pergunta.gabaritoTexto,
                    gabaritoNumero = pergunta.gabaritoNumero,
                    gabaritoBooleano = pergunta.gabaritoBooleano,
                    alternativas = alternativas.map { alt ->
                        AlternativaResponseDTO(
                            id = alt.id ?: throw IllegalArgumentException("ID da alternativa não pode ser nulo"),
                            descricao = alt.descricao,
                            correta = alt.correta
                        )
                    }
                )
            }

            logger.info("Encontradas ${perguntas.size} perguntas")
            return ResponseEntity.ok(ApiResponse.success(perguntasResponse))
        } catch (e: Exception) {
            logger.error("Erro ao buscar perguntas", e)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Erro ao buscar perguntas: ${e.message}"))
        }
    }
}


package br.com.ufu.question_quizz.controller

import br.com.ufu.question_quizz.dto.ApiResponse
import br.com.ufu.question_quizz.dto.GrupoDTO
import br.com.ufu.question_quizz.dto.GrupoResponseDTO
import br.com.ufu.question_quizz.dto.GrupoDeleteDTO
import br.com.ufu.question_quizz.dto.GrupoDeleteResponseDTO
import br.com.ufu.question_quizz.service.GrupoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Grupos", description = "API para gerenciamento de grupos")
@RestController
@RequestMapping("/api/grupos")
class GrupoController(private val grupoService: GrupoService) {

    @Operation(summary = "Buscar grupos por usuário", description = "Retorna todos os grupos associados a um usuário específico")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Grupos encontrados com sucesso"),
        SwaggerApiResponse(responseCode = "404", description = "Usuário não encontrado")
    ])
    @GetMapping("/usuario/{usuarioId}")
    fun buscarGruposPorUsuario(@PathVariable usuarioId: Int): ResponseEntity<ApiResponse<List<GrupoResponseDTO>>> {
        return try {
            val grupos = grupoService.buscarGruposPorUsuario(usuarioId)
            ResponseEntity.ok(
                ApiResponse.success(
                    message = "Grupos encontrados com sucesso",
                    data = grupos
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse.error(
                    message = e.message ?: "Erro ao buscar grupos",
                    data = null
                )
            )
        }
    }

    @Operation(summary = "Criar grupo", description = "Cria um novo grupo no sistema")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Grupo criado com sucesso"),
        SwaggerApiResponse(responseCode = "400", description = "Dados inválidos")
    ])
    @PostMapping
    fun criarGrupo(@RequestBody grupoDTO: GrupoDTO): ResponseEntity<ApiResponse<GrupoResponseDTO>> {
        return try {
            val grupo = grupoService.criarGrupo(grupoDTO)
            ResponseEntity.ok(
                ApiResponse.success(
                    message = "Grupo criado com sucesso",
                    data = grupo
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse.error(
                    message = e.message ?: "Erro ao criar grupo",
                    data = null
                )
            )
        }
    }

    @Operation(summary = "Deletar grupo", description = "Remove um grupo do sistema")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Grupo deletado com sucesso"),
        SwaggerApiResponse(responseCode = "404", description = "Grupo não encontrado")
    ])
    @DeleteMapping
    fun deletarGrupo(@RequestBody grupoDeleteDTO: GrupoDeleteDTO): ResponseEntity<ApiResponse<GrupoDeleteResponseDTO>> {
        return try {
            val grupo = grupoService.deletarGrupo(grupoDeleteDTO)
            ResponseEntity.ok(
                ApiResponse.success(
                    message = "Grupo deletado com sucesso",
                    data = grupo
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse.error(
                    message = e.message ?: "Erro ao deletar grupo",
                    data = null
                )
            )
        }
    }
}
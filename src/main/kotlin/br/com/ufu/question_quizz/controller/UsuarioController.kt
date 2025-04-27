package br.com.ufu.question_quizz.controller

import br.com.ufu.question_quizz.dto.*
import br.com.ufu.question_quizz.model.Usuario
import br.com.ufu.question_quizz.repository.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

/**
 * Controlador responsável por gerenciar as operações relacionadas a usuários.
 * 
 * Este controlador fornece endpoints para registro e autenticação de usuários.
 * Todas as operações são acessíveis através do prefixo '/api/usuarios'.
 *
 * @property usuarioRepository Repositório para acesso aos dados de usuários
 * @property passwordEncoder Codificador de senhas usando BCrypt
 */
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
@RestController
@RequestMapping("/api/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    /**
     * Registra um novo usuário no sistema.
     * 
     * Este endpoint recebe os dados do usuário e realiza o cadastro no sistema.
     * A senha é criptografada antes de ser armazenada no banco de dados.
     * 
     * @param request DTO contendo os dados do usuário a ser registrado
     * @return ResponseEntity contendo os dados do usuário registrado ou mensagem de erro
     * @throws Exception se ocorrer algum erro durante o processo de registro
     */
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema com os dados fornecidos")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
        SwaggerApiResponse(responseCode = "400", description = "Email já cadastrado")
    ])
    @PostMapping("/registrar")
    fun registrar(@RequestBody request: UsuarioRequestDTO): ResponseEntity<ApiResponse<UsuarioResponseDTO>> {
        val usuarioExistente = usuarioRepository.findByEmail(request.email)
        if (usuarioExistente.isPresent) {
            return ResponseEntity.badRequest().body(
                ApiResponse.error(
                    message = "Email já cadastrado"
                )
            )
        }

        val senhaHash = passwordEncoder.encode(request.senha)
        val usuario = Usuario(
            nome = request.nome,
            email = request.email,
            senhaHash = senhaHash
        )

        val usuarioSalvo = usuarioRepository.save(usuario)
        val response = UsuarioResponseDTO(
            id = usuarioSalvo.id!!,
            nome = usuarioSalvo.nome,
            email = usuarioSalvo.email
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(
                message = "Usuário registrado com sucesso",
                data = response
            )
        )
    }

    /**
     * Realiza a autenticação de um usuário no sistema.
     * 
     * Este endpoint recebe as credenciais do usuário e verifica se são válidas.
     * A senha fornecida é comparada com a senha criptografada armazenada no banco de dados.
     * 
     * @param request DTO contendo as credenciais do usuário (email e senha)
     * @return ResponseEntity contendo os dados do usuário autenticado ou mensagem de erro
     * @throws Exception se ocorrer algum erro durante o processo de autenticação
     */
    @Operation(summary = "Autenticar usuário", description = "Realiza o login do usuário com email e senha")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        SwaggerApiResponse(responseCode = "401", description = "Credenciais inválidas")
    ])
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<ApiResponse<LoginResponseDTO>> {
        val usuario = usuarioRepository.findByEmail(request.email)
            .orElse(null)

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error(
                    message = "Email não encontrado"
                )
            )
        }

        if (!passwordEncoder.matches(request.senha, usuario.senhaHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error(
                    message = "Senha inválida"
                )
            )
        }

        val usuarioResponse = UsuarioResponseDTO(
            id = usuario.id!!,
            nome = usuario.nome,
            email = usuario.email
        )

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "Login realizado com sucesso",
                data = LoginResponseDTO(usuarioResponse)
            )
        )
    }

    /**
     * Atualiza a senha de um usuário existente.
     * 
     * Este endpoint recebe o email do usuário e a nova senha,
     * atualizando a senha criptografada no banco de dados.
     * 
     * @param request DTO contendo o email do usuário e a nova senha
     * @return ResponseEntity contendo mensagem de sucesso ou erro
     * @throws Exception se ocorrer algum erro durante o processo de atualização
     */
    @Operation(summary = "Atualizar senha", description = "Atualiza a senha de um usuário existente")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
        SwaggerApiResponse(responseCode = "404", description = "Usuário não encontrado")
    ])
    @PutMapping("/atualizar-senha")
    fun atualizarSenha(@RequestBody request: AtualizarSenhaRequestDTO): ResponseEntity<ApiResponse<Unit>> {
        val usuario = usuarioRepository.findByEmail(request.email)
            .orElse(null)

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error(
                    message = "Usuário não encontrado"
                )
            )
        }

        val novaSenhaHash = passwordEncoder.encode(request.novaSenha)
        usuarioRepository.save(usuario.copy(senhaHash = novaSenhaHash))

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "Senha atualizada com sucesso"
            )
        )
    }
} 
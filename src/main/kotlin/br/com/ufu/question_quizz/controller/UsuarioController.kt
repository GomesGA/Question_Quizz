package br.com.ufu.question_quizz.controller

import br.com.ufu.question_quizz.dto.*
import br.com.ufu.question_quizz.model.Usuario
import br.com.ufu.question_quizz.repository.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@RestController
@RequestMapping("/api/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    @PostMapping("/registrar")
    fun registrar(@RequestBody request: UsuarioRequestDTO): ResponseEntity<ApiResponse<UsuarioResponseDTO>> {
        val usuarioExistente = usuarioRepository.findByEmail(request.email)
        if (usuarioExistente.isPresent) {
            return ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
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

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Usuário registrado com sucesso",
                data = response
            )
        )
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<ApiResponse<LoginResponseDTO>> {
        val usuario = usuarioRepository.findByEmail(request.email)
            .orElse(null)

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse(
                    success = false,
                    message = "Email não encontrado"
                )
            )
        }

        if (!passwordEncoder.matches(request.senha, usuario.senhaHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse(
                    success = false,
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
            ApiResponse(
                success = true,
                message = "Login realizado com sucesso",
                data = LoginResponseDTO(usuarioResponse)
            )
        )
    }
} 
package br.com.ufu.question_quizz.service

import br.com.ufu.question_quizz.model.Grupo
import br.com.ufu.question_quizz.repository.GrupoRepository
import br.com.ufu.question_quizz.repository.UsuarioRepository
import br.com.ufu.question_quizz.dto.GrupoDTO
import br.com.ufu.question_quizz.dto.GrupoResponseDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityNotFoundException

@Service
class GrupoService(
    private val grupoRepository: GrupoRepository,
    private val usuarioRepository: UsuarioRepository
) {

    @Transactional(readOnly = true)
    fun buscarGruposPorUsuario(usuarioId: Int): List<GrupoResponseDTO> {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { EntityNotFoundException("Usuário não encontrado com ID: $usuarioId") }

        val grupos = grupoRepository.findByUsuarioIdOrUsuarioIsNull(usuarioId)
        return grupos.map { grupo ->
            GrupoResponseDTO(
                id = grupo.id!!,
                descricao = grupo.descricao
            )
        }
    }

    @Transactional
    fun criarGrupo(grupoDTO: GrupoDTO): GrupoResponseDTO {
        val usuario = usuarioRepository.findById(grupoDTO.usuarioId)
            .orElseThrow { EntityNotFoundException("Usuário não encontrado com ID: ${grupoDTO.usuarioId}") }

        val grupo = Grupo(
            descricao = grupoDTO.descricao,
            imagemPath = grupoDTO.imagemPath,
            usuario = usuario
        )

        val grupoSalvo = grupoRepository.save(grupo)
        return GrupoResponseDTO(
            id = grupoSalvo.id!!,
            descricao = grupoSalvo.descricao
        )
    }

    @Transactional
    fun deletarGrupo(grupoDTO: GrupoDTO): GrupoResponseDTO {
        val grupo = grupoRepository.findById(grupoDTO.id!!)
            .orElseThrow { EntityNotFoundException("Grupo não encontrado com ID: ${grupoDTO.id}") }

        if (grupo.usuario?.id != grupoDTO.usuarioId) {
            throw IllegalStateException("O grupo não pertence ao usuário especificado")
        }

        grupoRepository.delete(grupo)
        return GrupoResponseDTO(
            id = grupo.id!!,
            descricao = grupo.descricao
        )
    }
}
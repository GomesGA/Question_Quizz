package br.com.ufu.question_quizz.service

import br.com.ufu.question_quizz.model.Grupo
import br.com.ufu.question_quizz.repository.GrupoRepository
import br.com.ufu.question_quizz.repository.UsuarioRepository
import br.com.ufu.question_quizz.dto.GrupoDTO
import br.com.ufu.question_quizz.dto.GrupoResponseDTO
import br.com.ufu.question_quizz.dto.GrupoDeleteDTO
import br.com.ufu.question_quizz.dto.GrupoDeleteResponseDTO
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
                descricao = grupo.descricao,
                path = grupo.imagemPath,
                idUsuario = grupo.usuario?.id
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
            descricao = grupoSalvo.descricao,
            path = grupoSalvo.imagemPath,
            idUsuario = grupoSalvo.usuario?.id
        )
    }

    @Transactional
    fun deletarGrupo(grupoDeleteDTO: GrupoDeleteDTO): GrupoDeleteResponseDTO {
        val grupo = grupoRepository.findById(grupoDeleteDTO.id)
            .orElseThrow { EntityNotFoundException("Grupo não encontrado com ID: ${grupoDeleteDTO.id}") }

        if (grupo.usuario?.id != grupoDeleteDTO.usuarioId) {
            throw IllegalStateException("O grupo não pertence ao usuário especificado")
        }

        grupoRepository.delete(grupo)
        return GrupoDeleteResponseDTO(
            id = grupo.id!!,
            descricao = grupo.descricao
        )
    }
}
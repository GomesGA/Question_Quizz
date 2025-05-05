package br.com.ufu.question_quizz.service

import br.com.ufu.question_quizz.dto.*
import br.com.ufu.question_quizz.exception.ValidationException
import br.com.ufu.question_quizz.model.*
import br.com.ufu.question_quizz.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class PerguntaService(
    private val tipoPerguntaRepository: TipoPerguntaRepository,
    private val perguntasRepository: PerguntasRepository,
    private val usuarioRepository: UsuarioRepository,
    private val alternativaRepository: AlternativaRepository,
    private val grupoRepository: GrupoRepository
) {

    fun listarTiposPergunta(): List<TipoPerguntaResponseDTO> {
        val tiposPergunta = tipoPerguntaRepository.findAll()
        return tiposPergunta.mapNotNull { tipo ->
            tipo.id?.let { id ->
                TipoPerguntaResponseDTO(
                    id = id,
                    descricao = tipo.descricao
                )
            }
        }
    }

    @Transactional
    fun criarPergunta(perguntaRequest: PerguntaRequestDTO): PerguntaResponseDTO {
        validarPergunta(perguntaRequest)

        val tipoPergunta = tipoPerguntaRepository.findById(perguntaRequest.idTipo)
            .orElseThrow { IllegalArgumentException("Tipo de pergunta não encontrado") }

        val usuario = usuarioRepository.findById(perguntaRequest.idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        val grupo = perguntaRequest.idGrupo?.let { grupoId ->
            grupoRepository.findById(grupoId)
                .orElseThrow { IllegalArgumentException("Grupo não encontrado") }
        }

        val pergunta = Pergunta(
            tipo = tipoPergunta,
            usuario = usuario,
            grupo = grupo,
            pergunta = perguntaRequest.pergunta,
            privada = perguntaRequest.privada,
            gabaritoTexto = perguntaRequest.resposta.texto,
            gabaritoNumero = perguntaRequest.resposta.numero,
            gabaritoBooleano = perguntaRequest.resposta.booleano
        )

        val perguntaSalva = perguntasRepository.save(pergunta)

        var alternativasResponse: List<AlternativaResponseDTO>? = null

        perguntaRequest.resposta.alternativas?.let { alternativas ->
            val alternativasSalvas = alternativas.map { alt ->
                Alternativa(
                    pergunta = perguntaSalva,
                    descricao = alt.descricao,
                    correta = alt.correta
                )
            }
            val alternativasSalvasComIds = alternativaRepository.saveAll(alternativasSalvas)
            
            alternativasResponse = alternativasSalvasComIds.map { alt ->
                AlternativaResponseDTO(
                    id = alt.id!!,
                    descricao = alt.descricao,
                    correta = alt.correta
                )
            }
        }

        return PerguntaResponseDTO(
            id = perguntaSalva.id!!.toInt(),
            idUsuario = usuario.id!!.toInt(),
            idGrupo = grupo?.id,
            privada = perguntaSalva.privada,
            descricao = perguntaSalva.pergunta,
            gabaritoTexto = perguntaSalva.gabaritoTexto,
            gabaritoNumero = perguntaSalva.gabaritoNumero,
            gabaritoBooleano = perguntaSalva.gabaritoBooleano,
            alternativas = alternativasResponse
        )
    }

    fun buscarPerguntas(idUsuario: Int?, idPergunta: Int?, idGrupo: Int?): List<PerguntaResponseDTO> {
        val perguntas = perguntasRepository.findByFiltros(idUsuario, idPergunta, idGrupo)
        
        return perguntas.map { pergunta ->
            val alternativas = try {
                alternativaRepository.findByPerguntaId(pergunta.id ?: throw IllegalArgumentException("ID da pergunta não pode ser nulo"))
            } catch (e: Exception) {
                emptyList()
            }
            
            PerguntaResponseDTO(
                id = pergunta.id ?: throw IllegalArgumentException("ID da pergunta não pode ser nulo"),
                idUsuario = pergunta.usuario?.id ?: throw IllegalArgumentException("ID do usuário não pode ser nulo"),
                idGrupo = pergunta.grupo?.id,
                privada = pergunta.privada,
                descricao = pergunta.pergunta,
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
    }

    @Transactional
    fun deletarPergunta(perguntaDeleteDTO: PerguntaDeleteDTO): String {
        val pergunta = perguntasRepository.findById(perguntaDeleteDTO.id)
            .orElseThrow { IllegalArgumentException("Pergunta não encontrada com ID: ${perguntaDeleteDTO.id}") }

        val usuarioId = pergunta.usuario?.id ?: throw IllegalStateException("A pergunta não está vinculada a nenhum usuário")

        if (usuarioId != perguntaDeleteDTO.idUsuario) {
            throw IllegalStateException("A pergunta não pertence ao usuário especificado. ID do usuário da pergunta: $usuarioId, ID do usuário informado: ${perguntaDeleteDTO.idUsuario}")
        }

        alternativaRepository.deleteByPerguntaId(pergunta.id!!)
        perguntasRepository.delete(pergunta)

        return "Pergunta ID ${pergunta.id} removida com sucesso"
    }

    private fun validarPergunta(perguntaRequest: PerguntaRequestDTO) {
        when (perguntaRequest.idTipo) {
            Integer.valueOf(1) -> validarPerguntaMultiplaEscolha(perguntaRequest)
            Integer.valueOf(2) -> validarPerguntaNumerica(perguntaRequest)
            Integer.valueOf(3) -> validarPerguntaVerdadeiroFalso(perguntaRequest)
            Integer.valueOf(4) -> validarPerguntaTexto(perguntaRequest)
            else -> throw ValidationException("Tipo de pergunta inválido")
        }
    }

    private fun validarPerguntaMultiplaEscolha(perguntaRequest: PerguntaRequestDTO) {
        val alternativas = perguntaRequest.resposta.alternativas
        
        if (alternativas.isNullOrEmpty()) {
            throw ValidationException("Perguntas de múltipla escolha devem ter alternativas")
        }

        val temAlternativaCorreta = alternativas.any { it.correta }
        if (!temAlternativaCorreta) {
            throw ValidationException("Perguntas de múltipla escolha devem ter pelo menos uma alternativa correta")
        }

        if (perguntaRequest.resposta.numero != null) {
            throw ValidationException("Perguntas de múltipla escolha não devem ter resposta numérica")
        }
        if (perguntaRequest.resposta.booleano != null) {
            throw ValidationException("Perguntas de múltipla escolha não devem ter resposta booleana")
        }
        if (!perguntaRequest.resposta.texto.isNullOrBlank()) {
            throw ValidationException("Perguntas de múltipla escolha não devem ter resposta textual")
        }
    }

    private fun validarPerguntaNumerica(perguntaRequest: PerguntaRequestDTO) {
        if (perguntaRequest.resposta.numero == null) {
            throw ValidationException("Perguntas numéricas devem ter um valor numérico preenchido")
        }

        if (!perguntaRequest.resposta.alternativas.isNullOrEmpty()) {
            throw ValidationException("Perguntas numéricas não devem ter alternativas")
        }
        if (perguntaRequest.resposta.booleano != null) {
            throw ValidationException("Perguntas numéricas não devem ter resposta booleana")
        }
        if (!perguntaRequest.resposta.texto.isNullOrBlank()) {
            throw ValidationException("Perguntas numéricas não devem ter resposta textual")
        }
    }

    private fun validarPerguntaVerdadeiroFalso(perguntaRequest: PerguntaRequestDTO) {
        if (perguntaRequest.resposta.booleano == null) {
            throw ValidationException("Perguntas de verdadeiro/falso devem ter um valor booleano preenchido")
        }

        if (!perguntaRequest.resposta.alternativas.isNullOrEmpty()) {
            throw ValidationException("Perguntas de verdadeiro/falso não devem ter alternativas")
        }
        if (perguntaRequest.resposta.numero != null) {
            throw ValidationException("Perguntas de verdadeiro/falso não devem ter resposta numérica")
        }
        if (!perguntaRequest.resposta.texto.isNullOrBlank()) {
            throw ValidationException("Perguntas de verdadeiro/falso não devem ter resposta textual")
        }
    }

    private fun validarPerguntaTexto(perguntaRequest: PerguntaRequestDTO) {
        if (perguntaRequest.resposta.texto.isNullOrBlank()) {
            throw ValidationException("Perguntas de texto aberto devem ter um texto preenchido")
        }

        if (!perguntaRequest.resposta.alternativas.isNullOrEmpty()) {
            throw ValidationException("Perguntas de texto aberto não devem ter alternativas")
        }
        if (perguntaRequest.resposta.numero != null) {
            throw ValidationException("Perguntas de texto aberto não devem ter resposta numérica")
        }
        if (perguntaRequest.resposta.booleano != null) {
            throw ValidationException("Perguntas de texto aberto não devem ter resposta booleana")
        }
    }
} 
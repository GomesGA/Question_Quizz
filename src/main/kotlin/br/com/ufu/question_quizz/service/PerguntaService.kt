package br.com.ufu.question_quizz.service

import br.com.ufu.question_quizz.dto.PerguntaRequestDTO
import br.com.ufu.question_quizz.exception.ValidationException
import org.springframework.stereotype.Service

@Service
class PerguntaService {

    fun validarPergunta(perguntaRequest: PerguntaRequestDTO) {
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

        // Verifica se outros campos estão vazios
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

        // Verifica se outros campos estão vazios
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

        // Verifica se outros campos estão vazios
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

        // Verifica se outros campos estão vazios
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
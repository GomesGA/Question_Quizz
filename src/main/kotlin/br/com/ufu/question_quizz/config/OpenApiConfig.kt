package br.com.ufu.question_quizz.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(Info()
                .title("Question Quizz API")
                .version("1.0.0")
                .description("API para gerenciamento de perguntas e questionários")
                .contact(Contact()
                    .name("Equipe de Desenvolvimento")
                    .email("dev@ufu.br")
                    .url("https://www.ufu.br"))
                .license(License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
    }
} 
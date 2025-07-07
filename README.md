📱 Question Quizz - Aplicativo Mobile
Este projeto foi desenvolvido com a valiosa colaboração de:

@D-Salge
@Gemidio04
@salgeee
@sergio-rocha1
@vinicius-salge

📌 Visão Geral
O Question Quizz é um aplicativo de aprendizado interativo desenvolvido em Kotlin com Jetpack Compose e Spring Boot, projetado para oferecer uma experiência moderna de quizzes educacionais.

🔗 Repositório do Aplicativo (Frontend): [https://github.com/sergio-rocha1/question_quizz.git](https://github.com/GomesGA/question_quizz_app.git)
Projeto de sistema de questionários desenvolvido com Spring Boot e Kotlin.

## Pré-requisitos

- Java 17
- Docker
- Docker Compose
- Gradle

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITÓRIO]
cd question_quizz
```

2. Configure as variáveis de ambiente:
Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:
```
DB_USERNAME=admin
DB_PASSWORD=ufu123
DB_NAME=ufuproject
```

## Executando o Projeto

1. Inicie o banco de dados PostgreSQL usando Docker:
```bash
docker-compose up -d
```

2. Execute a aplicação:
```bash
./gradlew bootRun
```

A aplicação estará disponível em: `http://localhost:8080`

## Encerrando o Projeto

Para encerrar a aplicação e os containers Docker de forma adequada:

1. Pare a aplicação Spring Boot:
   - Pressione `Ctrl + C` no terminal onde a aplicação está rodando
   - Aguarde a mensagem de encerramento do Spring Boot

2. Pare e remova os containers Docker:
```bash
docker-compose down
```

3. (Opcional) Se quiser remover também os volumes do banco de dados:
```bash
docker-compose down -v
```

## Documentação da API

A documentação da API está disponível através do Swagger UI:

- Interface visual: `http://localhost:8080/api/docs`
- Especificação OpenAPI: `http://localhost:8080/api/v1/api-docs`

### Endpoints Disponíveis

#### Usuários
- `POST /api/usuarios/registrar` - Registra um novo usuário
- `POST /api/usuarios/login` - Realiza login de usuário

#### Perguntas
- `GET /api/pergunta/tipos` - Lista todos os tipos de perguntas

## Estrutura do Projeto

```
question_quizz/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── br/com/ufu/question_quizz/
│   │   │       ├── controller/     # Controladores da API
│   │   │       ├── model/          # Entidades do sistema
│   │   │       ├── repository/     # Repositórios JPA
│   │   │       ├── service/        # Lógica de negócio
│   │   │       ├── dto/            # Objetos de transferência de dados
│   │   │       └── QuestionQuizzApplication.kt
│   │   └── resources/
│   │       ├── application.yml     # Configurações da aplicação
│   │       └── db/
│   │           └── migration/      # Scripts de migração do banco de dados
│   └── test/                       # Testes da aplicação
├── docker-compose.yaml             # Configuração do container PostgreSQL
└── build.gradle                    # Dependências e configurações do projeto
```

## Tecnologias Utilizadas

- Spring Boot 3.2.4
- Kotlin 1.9.25
- PostgreSQL 15
- Flyway (para migrações do banco de dados)
- Gradle
- Docker
- Swagger/OpenAPI (para documentação da API)

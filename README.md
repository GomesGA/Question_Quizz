# Question Quizz

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

## Estrutura do Projeto

```
question_quizz/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── br/com/ufu/question_quizz/
│   │   │       ├── model/          # Entidades do sistema
│   │   │       └── QuestionQuizzApplication.kt
│   │   └── resources/
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

## Endpoints da API

[Em desenvolvimento]

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request
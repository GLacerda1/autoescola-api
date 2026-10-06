# Auto Escola

API REST em Java 17 e Spring Boot para gerenciar alunos, instrutores, usuários e aulas de direção.

## Integrantes

- Gabriel Lacerda Araujo
- Julia Carolina
- Fernando Carlos
- Gabriel Guilherme

## Funcionalidades

- Cadastro, consulta paginada, atualização e desativação lógica de alunos e instrutores.
- Administração de usuários, com senhas protegidas por BCrypt e autorização por perfil.
- Login com token JWT, válido por 30 minutos.
- Agendamento e cancelamento de aulas conforme as regras de horário e antecedência.
- Consulta de endereço por CEP.
- Documentação OpenAPI/Swagger, desligada por padrão.

## Requisitos

- JDK 17.
- MySQL 8 ou compatível.

## Preparar secrets

Na pasta raiz do projeto, crie seu arquivo local de configuração:

```bash
cp .env.example .env
```

Gere quatro valores distintos, um para o segredo JWT, um para cada conta MySQL e um para a senha inicial do administrador:

```bash
openssl rand -hex 32
openssl rand -hex 24
openssl rand -hex 24
openssl rand -hex 24
```

Preencha os campos vazios em `.env` com os valores gerados. O segredo JWT deve ter no mínimo 32 bytes. A senha inicial do administrador deve ter entre 16 e 72 bytes UTF-8. Senhas criadas pela API devem ter pelo menos 12 caracteres e não podem exceder 72 bytes UTF-8.


## Criar o banco e as contas MySQL

Usando uma conta administrativa do MySQL, crie o banco e duas contas. Substitua os marcadores pelas senhas fortes que você gerou: use uma senha exclusiva para cada conta e a mesma senha correspondente nos campos do `.env`.

```sql
CREATE DATABASE autoescola CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'autoescola_app'@'localhost' IDENTIFIED BY '<SENHA_FORTE_DA_APLICACAO>';
CREATE USER 'autoescola_migrator'@'localhost' IDENTIFIED BY '<SENHA_FORTE_DO_MIGRADOR>';

GRANT SELECT, INSERT, UPDATE, DELETE ON autoescola.* TO 'autoescola_app'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON autoescola.* TO 'autoescola_migrator'@'localhost';
```

A conta `autoescola_app` é usada pela API para as operações normais. A conta `autoescola_migrator` é usada pelo Flyway para atualizar o esquema do banco. Assim, a conta usada nas requisições da API não recebe permissões de alteração do esquema.

No `.env`, preencha `DB_URL` com `jdbc:mysql://localhost:3306/autoescola`, informe os nomes e senhas das duas contas nos campos `DB_USERNAME`, `DB_PASSWORD`, `DB_MIGRATION_USERNAME` e `DB_MIGRATION_PASSWORD`, e defina `JWT_SECRET`.

`AUTOESCOLA_ADMIN_LOGIN` e `AUTOESCOLA_ADMIN_PASSWORD` são usados somente na primeira inicialização, quando ainda não existe um administrador. Defina ambos antes de iniciar a aplicação pela primeira vez. Depois que a conta for criada, remova esses dois valores do `.env`; o administrador poderá alterar a própria senha pela API.

`CORS_ALLOWED_ORIGINS` deve conter apenas as origens exatas da interface web, separadas por vírgula. Deixe-a vazia se não houver interface web em outra origem. Não use `*` em produção. Para consultar o Swagger durante o desenvolvimento local, defina `SWAGGER_ENABLED=true`; ele fica desligado por padrão.

## Abrir e executar

1. Abra a pasta `AutoEscola`
2. Configure o JDK do projeto como 17.
3. Confirme que o MySQL está ativo e que `.env` foi preenchido.
4. Execute `br.com.autoescola.api.AutoEscolaApplication`.

O Flyway aplica as migrações automaticamente. A API usa a porta 8085 por padrão; ela pode ser alterada com `SERVER_PORT`.

Para autenticar, envie `POST /login` com o login e a senha definidos para o administrador inicial. Envie o JWT retornado nos demais pedidos pelo cabeçalho `Authorization: Bearer <token>`.

## Documentação da API

- Swagger UI, quando habilitado: `http://localhost:8085/swagger-ui/index.html`
- OpenAPI JSON, quando habilitado: `http://localhost:8085/v3/api-docs`
- Consulta de CEP: `GET /integracoes/cep/{cep}`

As operações protegidas exigem autenticação. Cadastros e administração de alunos e instrutores exigem perfil de administrador. Consulte o Swagger para detalhes dos endpoints.

## Testes

Os testes usam H2 em memória e credenciais aleatórias durante a execução; não precisam de MySQL, `.env` ou segredos locais:

```bash
./mvnw test
```

No IntelliJ, também é possível executar os testes pela janela Maven em **Lifecycle > test**.

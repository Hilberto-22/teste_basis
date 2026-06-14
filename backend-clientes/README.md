# Backend Clientes

API REST em Java/Spring Boot para o teste tecnico de diretorio de clientes.

## Requisitos

- Java 21
- Maven 3.9+ ou uso do Maven Wrapper incluido no projeto

## Como executar

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Por padrao, a aplicacao sobe consumindo o arquivo remoto configurado em `src/main/resources/application.properties`.

## Como testar

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

Os testes usam um dataset local em `src/test/resources/clients-test.json`, entao nao dependem da disponibilidade do endpoint externo.

## Endpoints

### Listar clientes

`GET /api/clients`

Query params suportados:

- `page`: pagina atual, comecando em `1`
- `limit`: quantidade de itens por pagina
- `state`: UF ou nome completo do estado
- `name`: busca parcial por nome

Exemplo:

```http
GET /api/clients?page=1&limit=9&state=SP&name=Ana
```

Resposta:

```json
{
  "total": 1,
  "page": 1,
  "limit": 9,
  "data": [
    {
      "id": "5551d0bf-cde1-3ab6-bd8b-fc254d00d614",
      "name": "Ana Souza",
      "email": "ana.souza@example.com",
      "city": "Sao Paulo",
      "state": "Sao Paulo",
      "stateCode": "SP",
      "pictureUrl": "https://randomuser.me/api/portraits/med/women/20.jpg"
    }
  ]
}
```

### Buscar cliente por id

`GET /api/clients/{id}`

Exemplo:

```http
GET /api/clients/5551d0bf-cde1-3ab6-bd8b-fc254d00d614
```

## Swagger

Com a aplicacao em execucao, a documentacao pode ser acessada em:

`/swagger-ui/index.html`

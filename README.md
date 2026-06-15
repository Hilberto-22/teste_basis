# Diretório de Clientes

Aplicação FullStack para listagem, busca e visualização de clientes, desenvolvida com backend em Java Spring Boot e frontend em Angular.

## 📌 Objetivo

O sistema permite:

* Listar clientes em formato de cards
* Buscar clientes por nome
* Filtrar clientes por estado (UF)
* Paginar os resultados
* Visualizar detalhes completos do cliente

---

# 🛠️ Tecnologias utilizadas

## Backend

* Java 21
* Spring Boot
* Spring Web
* Lombok
* Maven

## Frontend

* Angular 18
* TypeScript
* Angular Material / Bootstrap (opcional)

---

# 📂 Estrutura do projeto

```bash
client-directory/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── ...
│
└── README.md
```

---

# 🚀 Como executar o projeto

## Pré-requisitos

Antes de iniciar, certifique-se de possuir instalado:

* Java 17+
* Maven 3.9+
* Node.js 20+
* Angular CLI

---

# ▶️ Executando o Backend

## 1. Acesse a pasta backend

```bash
cd backend
```

## 2. Instale as dependências

```bash
mvn clean install
```

## 3. Execute o projeto

```bash
mvn spring-boot:run
```

A API estará disponível em:

```bash
http://localhost:8080
```

---

# ▶️ Executando o Frontend

## 1. Acesse a pasta frontend

```bash
cd frontend
```

## 2. Instale as dependências

```bash
npm install
```

## 3. Execute a aplicação

```bash
ng serve
```

A aplicação estará disponível em:

```bash
http://localhost:4200
```

---

# 🌐 API REST

## Listar clientes

```http
GET /api/clients
```

### Query Params

| Parâmetro | Tipo   | Descrição           |
| --------- | ------ | ------------------- |
| page      | number | Página atual        |
| limit     | number | Quantidade de itens |
| state     | string | Filtrar por estado  |
| name      | string | Buscar por nome     |

### Exemplo

```http
GET /api/clients?page=1&limit=9&state=SP&name=Ana
```

### Resposta

```json
{
  "total": 25,
  "page": 1,
  "limit": 9,
  "data": [
    {
      "id": "1",
      "name": "Ana Silva"
    }
  ]
}
```

---

## Buscar cliente por ID

```http
GET /api/clients/{id}
```

### Exemplo

```http
GET /api/clients/1
```

---

# 🎨 Funcionalidades

* ✅ Listagem de clientes
* ✅ Busca por nome
* ✅ Filtro por estado
* ✅ Paginação
* ✅ Visualização de detalhes
* ✅ Layout responsivo
* ✅ Integração entre frontend e backend

---

# 📷 Layout

O frontend foi desenvolvido seguindo o layout de referência disponibilizado no desafio técnico.

---

# 🧪 Testes

## Backend

```bash
mvn test
```

## Frontend

```bash
ng test
```

---

# 📦 Build para produção

## Backend

```bash
mvn clean package
```

## Frontend

```bash
ng build
```

---

# 👨‍💻 Autor

Desenvolvido por Hilberto Filho.

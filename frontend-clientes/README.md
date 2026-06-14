# Frontend Clientes

Frontend em Angular para o teste tecnico de diretorio de clientes.

## O que foi implementado

- Grid responsivo com 3 colunas no desktop
- Interface baseada em Bootstrap 5
- Busca por nome com debounce
- Filtro por UF
- Paginacao com total de itens e pagina atual
- Painel de detalhes ao clicar em um cliente
- Integracao com o backend em `http://localhost:8080`

## Pre-requisitos

- Node.js 18+
- npm 9+
- Backend Java rodando em `http://localhost:8080`

## Como rodar

1. Instale as dependencias:

```bash
npm install
```

2. Inicie o frontend:

```bash
npm start
```

3. Acesse:

```text
http://localhost:4200
```

## Scripts uteis

- `npm start`: sobe o servidor de desenvolvimento
- `npm run build`: gera o build de producao
- `npm test`: executa os testes unitarios

## Observacoes

- A aplicacao consome os endpoints `GET /api/clients` e `GET /api/clients/:id`.
- Em desenvolvimento, o Angular usa proxy para encaminhar `/api` para `http://localhost:8080` e evitar problemas de CORS.

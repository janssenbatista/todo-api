# Todo API
API de lista de tarefas

## Executando a API
Essa aplicação utilizando o Gradle como ferramenta de build. Então na pasta raiz do projeto execute o seguinte comando:
- Windows
```
gradlew.bat bootRun
```
- Linux/Mac
```bash
./gradlew bootRun 
```

Os endpoints estarão disponíveis em: http://localhost:8080/api

## Endpoints da API de Tarefas

### 1. Criar uma Nova Tarefa

- **Endpoint:** `POST /tarefas`
- **Descrição:** Este endpoint permite a criação de uma nova tarefa. O corpo da requisição deve conter os detalhes da tarefa a ser criada.
- **Request Body:**
    - `CreateTaskDto`: Objeto que contém os detalhes da tarefa, como nome, descrição, data de vencimento, etc.
- **Resposta:**
    - **Status:** `201 Created`
    - **Corpo:** Retorna o objeto `TaskEntity` recém-criado com todos os detalhes da tarefa, incluindo seu `ID` gerado.

### 2. Listar Todas as Tarefas

- **Endpoint:** `GET /tarefas`
- **Descrição:** Este endpoint recupera uma lista paginada de todas as tarefas existentes.
- **Query Parameters:**
    - `pageNumber` (opcional, default = 0): Número da página a ser recuperada.
    - `pageSize` (opcional, default = 10): Número de itens por página.
- **Resposta:**
    - **Status:** `200 OK`
    - **Corpo:** Retorna um objeto `Page<TaskEntity>` contendo a lista de tarefas na página especificada.

### 3. Atualizar uma Tarefa

- **Endpoint:** `PUT /tarefas/{id}`
- **Descrição:** Este endpoint permite a atualização de uma tarefa existente. O ID da tarefa a ser atualizada deve ser fornecido na URL, e o corpo da requisição deve conter os novos detalhes da tarefa.
- **Path Parameter:**
    - `id`: O ID da tarefa que deve ser atualizada.
- **Request Body:**
    - `UpdateTaskDto`: Objeto contendo os novos detalhes da tarefa.
- **Resposta:**
    - **Status:** `200 OK`
    - **Corpo:** Retorna o objeto `TaskEntity` atualizado com todos os novos detalhes.

### 4. Deletar uma Tarefa

- **Endpoint:** `DELETE /tarefas/{id}`
- **Descrição:** Este endpoint permite a exclusão de uma tarefa específica pelo seu ID.
- **Path Parameter:**
    - `id`: O ID da tarefa a ser deletada.
- **Resposta:**
    - **Status:** `204 No Content`
    - **Corpo:** Não retorna conteúdo, indicando que a tarefa foi deletada com sucesso.

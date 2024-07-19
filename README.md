# desafio-springboot-docker

### 📋 Sobre o projeto
Aplicação Web para gerenciamento de tarefas(To-Do list), onde é possível adicionar, atualizar, visualizar e deletar tarefas.

### IMPORTANTE
Neste projeto, explorei diferentes abordagens dentro de um único contexto. Como prática de microservices, utilizei autenticação por Client BasicAuthentication, implementei exceções personalizadas e realizei a validação de DTOs usando anotações customizadas acionadas pelo `@Valid` na camada de Resource. Além disso, desenvolvi diversas outras funcionalidades para demonstrar de forma prática meu conhecimento. Estou aberto a dúvidas e feedbacks a qualquer momento.

### Tecnologias
- Java 17
- Spring Boot
- PostgreSQL
- Hibernate
- Flyway
- Google jib
- Docker Compose

## 🚀 Executando projeto
#### Gere uma imagem docker (Executa os testes)
```
./mvnw -ntp verify jib:dockerBuild
```
insira ``` -DskipTests``` se desejar pular os testes durante a build 
#### Para execução do BackEnd em conjunto ao Banco de dados (PostgreSQL)
```
docker-compose -f src/main/docker/app.yml up -d
```

<hr>

## Endpoints
O projeto disponibiliza endpoints para Usuários e Contas, onde utilizam o padrão Rest de comunicação, consumindo e retornando dados no formato JSON.

### Autenticação e Autorização

Este projeto utiliza autenticação do tipo Basic Auth. Todas as requisições, exceto a de criação de usuário, requerem o envio do nome de usuário e senha. Além disso, o usuário deve possuir as permissões (roles) correspondentes à rota que deseja acessar. As seguintes permissões estão pré-cadastradas:

- ID 1: conta_select
- ID 2: conta_insert
- ID 3: conta_update
- ID 4: conta_delete

Essa versão é mais clara e direta, facilitando a compreensão das informações sobre a autenticação e autorização no projeto.

## Usuário
#### Criar
POST /usuario
```bash
http://localhost:8080/usuario/
```
Request Body
```bash
{
	"username": "joao",
	"password": "senha",
	"roles": [
	    {"id": 1},
	    {"id": 2},
	    {"id": 3},
	    {"id": 4},
	]
}
```
Possiveis roles:
- Acesso a consulta de contas: ``` {"id": 1, "name": "conta_select"}```
- Acesso a inserção de contas: ``` {"id": 2, "name": "conta_insert"} ```
- Acesso a atualização de contas: ``` {"id": 4, "name": "conta_update"}```
- Acesso a deleção de contas: ``` {"id": 4, "name": "conta_delete"}```



### Contas
#### Listar contas a pagar (Pendentes)
GET /api/v1/conta/listar
```bash
http://localhost:8080/api/v1/conta/listar?dataVencimento=13/06/2024&descricao=ta&size=10&sort=dataVencimento,asc&page=0
```
Nele recebemos os possiveis parametros de URL:
- dataVencimento: Data do vencimento da conta no formato dd/MM/yyyy
- descricao: Descrição da conta
- page: pagina desejada (default: 0)
- size: quantidade de Itens por página
- sort: Ordenaçao da paginação no seguinte formato "{campo},{asc ou desc}" (exemplo: dataVencimento,asc) 

#### Listar por Id
GET api/v1/conta/:id
```bash
http://localhost:8080/api/v1/conta/1
```

#### Listar Todos
GET /api/v1/conta/total-valor-pago
```bash
http://localhost:8080/api/v1/conta/total-valor-pago?dataInicial=20/02/2000&dataFinal=20/02/2026
```
Nele recebemos os possiveis parametros de URL:
- dataInicial: Data inicial do periodo formato dd/MM/yyyy
- dataFinal: Data final do periodo formato dd/MM/yyyy

#### Criar
POST /api/v1/conta
```bash
http://localhost:8080/api/v1/conta
```
Request Body
```bash
{
  "dataVencimento": "13/06/2024",
  "valor": 100.00,
  "descricao": "Conta de água"
}
```
#### Atualizar
PATCH /api/v1/conta/:id
```bash
http://localhost:8080/api/v1/conta/13
```
Request Body
```bash
{
	"id": 13,
	"dataVencimento": "13/06/2024",
	"dataPagamento": "2025-06-13",
	"valor": 100.00,
	"descricao": "Conta de água",
	"situacao": "PAGO"
}
```
Obs.: Podendo receber ambos os formatos de data

#### Importar Contas por CSV
PATCH /api/v1/conta/import
```bash
http://localhost:8080/api/v1/conta/import
```
Request Muiltpart com o parametro "file"
```bash
descricao,dataVencimento,dataPagamento,valor,situacao
Conta de luz,20/02/2020,,100,
IPVA,12/03/2021,12/04/2021,22.50,PAGO
```
# springboot-docker

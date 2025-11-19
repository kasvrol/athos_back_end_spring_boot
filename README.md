# Athos: Backend

Este repositório contém todos os microsserviços, o API Gateway e a infraestrutura do backend para o aplicativo Athos - um sistema de gerenciamento de eventos esportivos multidisciplinares.

## Serviços

### ✅ Implementados e Funcionais
- **ms-autenticacao**: Gerencia cadastro, login, JWT e perfil de usuários
- **ms-evento**: Criação e gerenciamento de eventos esportivos e inscrições
- **ms-campeonato**: Gerenciamento de campeonatos, equipes e tabelas (Round-Robin)
- **ms-avaliacao**: Sistema de avaliações e ratings entre usuários
- **ms-notificacao**: Notificações em tempo real via RabbitMQ
- **ms-recomendacao**: Criação de recomendações de eventos esportivos e campeonatos

## Infraestrutura

- **API Gateway**: NGINX (porta 80)
- **Bancos de Dados**: PostgreSQL (relacional) + MongoDB (não-relacional)
- **Message Broker**: RabbitMQ
- **Containerização**: Docker Compose

## Status do Sistema

✅ Autenticação e Autorização (JWT)  
✅ Gerenciamento de Usuários  
✅ Normalização de Esportes (36 esportes seeded)  
✅ Criação e Gestão de Eventos  
✅ Criação e Gestão de Campeonatos  
✅ Gerenciamento de Equipes e Membros  
✅ Geração de Tabelas (Round-Robin)  
✅ Atualização de Placares  
✅ Cálculo de Classificação  
✅ Sistema de Notificações (RabbitMQ)  
✅ Avaliações e Ratings  

## Como Executar

1. Certifique-se de ter Docker e Docker Compose instalados
2. Clone o repositório
3. Na raiz do projeto, execute:

```bash
docker compose up --build -d
```
4. Aguarde ~30 segundos para inicialização dos serviços
5. Verifique o status:

```bash
docker compose ps
```

## Endpoints Principais
Todos os endpoints passam pelo API Gateway em http://localhost:8081

POST /api/autenticacao/login - Login
POST /api/autenticacao/registrar - Registro
GET /api/perfil - Perfil do usuário
GET/POST /api/eventos - Eventos
GET/POST /api/campeonatos - Campeonatos
POST /api/campeonatos/{id}/gerar-tabela - Gerar tabela
GET/POST /api/equipes - Equipes
GET/POST /api/partidas - Partidas
PATCH /api/partidas/{id}/placar - Atualizar placar
GET/POST /api/avaliacoes - Avaliações
GET/POST /api/notificacoes - Notificações

## Comandos Úteis

```bash
### Ver status dos containers
docker compose ps

### Ver logs de um serviço
docker compose logs ms-autenticacao

### Testar endpoint de esportes
curl http://localhost:8081/api/esportes | jq 'length'

* Acessar RabbitMQ Management UI
* URL: http://localhost:15672
* Usuário: guest
* Senha: guest

### Rebuild completo
docker compose down -v
docker compose up --build -d
```

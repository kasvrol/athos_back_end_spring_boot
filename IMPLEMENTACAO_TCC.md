# 🎯 Athos Backend - Implementação Completa para TCC

## 📊 Status da Implementação

### ✅ Microsserviços 100% Implementados

#### 1. **ms-autenticacao**
- ✅ Cadastro e login de usuários
- ✅ JWT Authentication
- ✅ Perfil de usuários
- ✅ MongoDB configurado
- ✅ Security filters

**Endpoints**:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/perfil/{id}`
- `PUT /api/perfil/{id}`

---

#### 2. **ms-evento**
- ✅ CRUD completo de eventos esportivos
- ✅ Sistema de inscrições (ParticipacaoEvento)
- ✅ PostgreSQL configurado
- ✅ Validações completas

**Endpoints**:
- `POST/GET /api/eventos`
- `GET /api/eventos/{id}`
- `PUT/DELETE /api/eventos/{id}`
- `POST /api/inscricoes`
- `GET /api/inscricoes/evento/{id}`

---

#### 3. **ms-notificacao** ⭐ NOVO
- ✅ Model Notificacao (MongoDB)
- ✅ Repository e Service completos
- ✅ Controller REST com 7 endpoints
- ✅ **RabbitMQ Consumers** configurados:
  - EventoListener (5 tipos de eventos)
  - PagamentoListener
  - AvaliacaoListener
- ✅ Notificações automáticas via eventos

**Endpoints**:
- `POST /api/notificacoes`
- `GET /api/notificacoes/usuario/{id}`
- `GET /api/notificacoes/usuario/{id}/nao-lidas`
- `GET /api/notificacoes/usuario/{id}/nao-lidas/count`
- `PATCH /api/notificacoes/{id}/lida`
- `PATCH /api/notificacoes/usuario/{id}/marcar-todas-lidas`
- `DELETE /api/notificacoes/{id}`

---

#### 4. **ms-avaliacao** ⭐ NOVO
- ✅ Model Avaliacao (MongoDB)
- ✅ Sistema de ratings entre usuários (1-5 estrelas)
- ✅ Validações (não pode auto-avaliar, só uma vez por evento)
- ✅ **RabbitMQ Publisher** - Publica eventos para notificação
- ✅ Cálculo de média de avaliações

**Endpoints**:
- `POST /api/avaliacoes`
- `GET /api/avaliacoes/recebidas/{usuarioId}`
- `GET /api/avaliacoes/feitas/{usuarioId}`
- `GET /api/avaliacoes/evento/{eventoId}`
- `GET /api/avaliacoes/media/{usuarioId}`
- `DELETE /api/avaliacoes/{id}`

---

#### 5. **ms-pagamento** ⭐ NOVO
- ✅ Integração completa com **Stripe SDK**
- ✅ PaymentIntent creation
- ✅ Webhook handler para confirmações
- ✅ **RabbitMQ Publisher** - Eventos de pagamento
- ✅ Model Pagamento (PostgreSQL)

**Endpoints**:
- `POST /api/pagamentos` - Criar pagamento (gera Stripe PaymentIntent)
- `GET /api/pagamentos/{id}`
- `GET /api/pagamentos/usuario/{id}`
- `GET /api/pagamentos/evento/{id}`
- `POST /api/pagamentos/confirmar/{paymentIntentId}`
- `POST /api/pagamentos/webhook` - Stripe webhooks

**Configuração**:
- Editar `ms-pagamento/src/main/resources/application.properties`
- Substituir `stripe.api.key` e `stripe.webhook.secret`

---

#### 6. **ms-campeonato** ⚠️ ESTRUTURA BÁSICA
- ⚠️ Models criados (Campeonato, Equipe, Partida)
- ⚠️ Repositories criados
- ⚠️ Estrutura básica de controllers
- ❌ Geração automática de tabelas (não implementado)
- ❌ Sistema avançado de fases (não implementado)

**Status**: Funcional para CRUD básico, mas sem lógica complexa de chaves/grupos.

**Nota**: Ver `ms-campeonato/README_IMPLEMENTATION.md` para detalhes.

---

#### 7. **ms-recomendacao** ⭐ NOVO - PYTHON/FASTAPI
- ✅ **Filtragem Colaborativa** (SVD - Surprise library)
- ✅ **Filtragem por Conteúdo** (TF-IDF + Similaridade de Cosseno)
- ✅ **Hibridização Ponderada** (alpha=0.6)
- ✅ Coleta de dados do PostgreSQL
- ✅ Treinamento automático na inicialização
- ✅ Endpoint de retreinamento

**Endpoints**:
- `GET /api/recomendacoes/{usuario_id}?limit=10`
- `POST /api/recomendacoes/retreinar`
- `GET /health`

**Algoritmo**:
```
Score_Final = α * Score_Colaborativo + (1-α) * Score_Conteúdo
onde α = 0.6
```

---

### 🔧 Infraestrutura

#### API Gateway (NGINX)
- ✅ Todas as rotas configuradas
- ✅ Auth request implementado
- ✅ Rota de ms-recomendacao ATIVADA

#### Docker Compose
- ✅ PostgreSQL (múltiplos DBs)
- ✅ MongoDB
- ✅ RabbitMQ
- ✅ Todos os 7 microsserviços
- ✅ API Gateway

---

## 🚀 Como Executar

### Pré-requisitos
- Docker & Docker Compose instalados
- Portas disponíveis: 80, 5432, 27017, 5672, 15672, 8081-8086, 8000

### Iniciar o Sistema

```bash
# Na raiz do projeto
docker-compose up --build
```

### Acessar Serviços

- **API Gateway**: http://localhost
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)
- **PostgreSQL**: localhost:5432
- **MongoDB**: localhost:27017

---

## 📋 Fluxo de Eventos RabbitMQ

### Eventos Publicados:

1. **ms-evento** → Publica:
   - `evento.criado`
   - `evento.atualizado`
   - `evento.cancelado`
   - `inscricao.confirmada`
   - `inscricao.cancelada`
   - `vaga.disponivel`

2. **ms-pagamento** → Publica:
   - `pagamento.confirmado`
   - `pagamento.pendente`

3. **ms-avaliacao** → Publica:
   - `avaliacao.recebida`

### Eventos Consumidos:

- **ms-notificacao** → Consome TODOS os eventos acima
  - Cria notificações automáticas
  - Armazena no MongoDB

---

## 🧪 Testando o Sistema

### 1. Criar Usuário
```bash
POST http://localhost/api/auth/register
{
  "nome": "João Silva",
  "email": "joao@test.com",
  "senha": "123456"
}
```

### 2. Fazer Login
```bash
POST http://localhost/api/auth/login
{
  "email": "joao@test.com",
  "senha": "123456"
}
# Retorna: { "token": "..." }
```

### 3. Criar Evento (com token)
```bash
POST http://localhost/api/eventos
Authorization: Bearer {token}
{
  "nome": "Pelada Domingo",
  "data": "2025-11-01",
  "horario": "09:00",
  "esporte": "Futebol",
  "cep": "80000-000",
  "endereco": "Campo do Bairro",
  "publico": true,
  "valor": 20.00,
  "maxParticipantes": 22
}
```

### 4. Avaliar Participante
```bash
POST http://localhost/api/avaliacoes
Authorization: Bearer {token}
{
  "avaliadorId": "user-1",
  "avaliadorNome": "João",
  "avaliadoId": "user-2",
  "eventoId": "evento-1",
  "eventoNome": "Pelada Domingo",
  "nota": 5,
  "comentario": "Excelente jogador!"
}
# → Dispara notificação automática para user-2
```

### 5. Ver Notificações
```bash
GET http://localhost/api/notificacoes/usuario/{userId}
# Retorna lista de notificações
```

### 6. Obter Recomendações
```bash
GET http://localhost/api/recomendacoes/{userId}?limit=10
# Retorna eventos recomendados baseado em ML
```

---

## 📊 Diagrama de Arquitetura

```
┌─────────────────┐
│  React Native   │ (Frontend - Repositório separado)
│   (Mobile App)  │
└────────┬────────┘
         │
    HTTP │
         ▼
┌─────────────────┐
│   API Gateway   │ (NGINX - Porta 80)
│   (nginx.conf)  │
└────────┬────────┘
         │
    ┌────┴────┐
    ▼         ▼
┌────────┐  ┌───────────┐  ┌──────────┐
│ ms-auth│  │ ms-evento │  │ms-notif  │
│(MongoDB│  │(PostgreSQL│  │(MongoDB) │
└────────┘  └───────────┘  └──────────┘
    │            │              ▲
    │            │              │
    └────┬───────┴──────┬───────┘
         │              │
    ┌────▼──────────────▼────┐
    │     RabbitMQ (Events)  │
    └────────────────────────┘
         │              ▲
    ┌────▼──────────────┴─────┐
    │  ms-pagamento            │
    │  ms-avaliacao            │
    │  ms-campeonato           │
    │  ms-recomendacao (Python)│
    └──────────────────────────┘
```

---

## 🎓 Componentes do TCC

### Conforme Documentação

✅ **Arquitetura de Microsserviços**: Implementada com alta coes�o e baixo acoplamento

✅ **Sistema de Recomendação Híbrido**:
   - Filtragem Colaborativa (SVD)
   - Filtragem por Conteúdo (TF-IDF)
   - Hibridização ponderada (α=0.6)

✅ **Comunicação Assíncrona**: RabbitMQ com Topic Exchanges

✅ **API Gateway**: NGINX com auth request

✅ **Bancos de Dados**:
   - PostgreSQL: Eventos, Pagamentos, Campeonatos
   - MongoDB: Autenticação, Notificações, Avaliações

✅ **Segurança**: JWT com validação por request

---

## ⚠️ Observações Importantes

### Configurações Necessárias:

1. **Stripe** (ms-pagamento):
   - Obter chaves de teste em https://stripe.com
   - Editar `ms-pagamento/src/main/resources/application.properties`

2. **JWT Secret** (ms-autenticacao):
   - ✅ Já configurado (Base64)

3. **RabbitMQ**:
   - ✅ Configuração automática via Docker

### Limitações Conhecidas:

1. **ms-campeonato**: Estrutura básica implementada
   - Geração automática de tabelas simplificada
   - Sistema de fases não implementado completamente
   - Funcional para CRUD, mas sem lógica avançada

2. **ms-recomendacao**:
   - Conexão com MongoDB de avaliações não implementada (usa SQL)
   - Retreinamento manual necessário após mudanças

3. **Testes**: Testes unitários não implementados (foco em funcionalidade)

---

## 📝 Próximos Passos (Pós-TCC)

- [ ] Implementar geração automática completa de chaves/grupos (ms-campeonato)
- [ ] Adicionar testes unitários e de integração
- [ ] Implementar sistema de cache (Redis)
- [ ] Adicionar métricas e monitoring (Prometheus/Grafana)
- [ ] Deploy em cloud (AWS/GCP)
- [ ] CI/CD pipeline
- [ ] Documentação Swagger/OpenAPI

---

## 👥 Autores

- Ana Karolina dos Santos do Vale
- Nelson Altair Nunes Meduna
- Rafael Hideki Nakamura

**Orientador**: Prof. Dr. Jaime Wojciechowski

**Instituição**: Universidade Federal do Paraná - UFPR

---

## 📄 Licença

Este projeto foi desenvolvido como Trabalho de Conclusão de Curso (TCC) para o curso de Tecnologia em Análise e Desenvolvimento de Sistemas da UFPR.

---

**Data de Implementação**: Outubro 2025
**Versão**: 1.0.0 - TCC Release

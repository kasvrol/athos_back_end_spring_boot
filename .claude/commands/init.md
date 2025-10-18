# Comando Init - Projeto Athos TCC

Você está trabalhando no backend do **Athos**, um aplicativo de gerenciamento de eventos esportivos desenvolvido como TCC.

## Arquitetura
- **Microsserviços**: Java Spring Boot
- **Sistema de Recomendação**: Python FastAPI
- **Bancos**: PostgreSQL (relacional), MongoDB (não-relacional)
- **Message Broker**: RabbitMQ
- **API Gateway**: NGINX
- **Frontend**: React Native (repositório separado)

## Microsserviços do Sistema

### ✅ Implementados
1. **ms-autenticacao** - Cadastro, login, JWT, perfil de usuários
2. **ms-evento** - CRUD de eventos esportivos e inscrições

### ⚠️ Parcialmente Implementados (apenas esqueleto)
3. **ms-campeonato** - Gerenciamento de campeonatos e equipes
4. **ms-pagamento** - Integração com Stripe para pagamentos
5. **ms-avaliacao** - Sistema de avaliações entre usuários
6. **ms-notificacao** - Notificações via RabbitMQ

### ❌ Não Implementados
7. **ms-recomendacao** - Sistema de recomendação híbrido (Python/FastAPI) usando filtragem colaborativa (SVD) + filtragem baseada em conteúdo (TF-IDF)

## Funcionalidades Principais
- Criação e gerenciamento de eventos esportivos multiesportivos
- Sistema de inscrições e controle de vagas
- Avaliações entre participantes para construir confiança
- Sistema de recomendação personalizado
- Gerenciamento de campeonatos com fases e partidas
- Pagamentos via Stripe
- Notificações em tempo real

## Observações da Documentação
- A documentação menciona que há correções pendentes
- O arquivo `ms-autenticacao/src/main/resources/application.properties` foi modificado (git status)
- O API Gateway tem rotas comentadas para ms-recomendacao

## Objetivo
Completar todos os microsserviços para apresentação do TCC, garantindo funcionalidade completa e integração entre os serviços.

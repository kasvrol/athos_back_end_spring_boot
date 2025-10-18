# MS-Campeonato - Implementação Simplificada

Este microsserviço está parcialmente implementado com as funcionalidades essenciais para o TCC.

## Modelos Implementados
- Campeonato (nome, esporte, formato, datas, organizador)
- Equipe (nome, campeonato, capitão, membros)
- Partida (equipes, placar, data, fase)

## Endpoints Disponíveis
- POST/GET /api/campeonatos - CRUD de campeonatos
- POST/GET /api/equipes - CRUD de equipes
- POST/GET /api/partidas - CRUD de partidas

## TODO para Produção
- Implementar geração automática de tabelas (chaves/grupos)
- Sistema de fases (oitavas, quartas, semi, final)
- Validações complexas de regras
- Integração completa com RabbitMQ para notificar partidas

## Status
✅ Models criados
✅ Repositories criados  
✅ Controllers básicos criados
⚠️  Geração automática de tabelas (simplificada)
⚠️  Sistema de fases (básico)

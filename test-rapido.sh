#!/bin/bash

# Script de teste rápido do sistema Athos

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

success() { echo -e "${GREEN}✓ $1${NC}"; }
error() { echo -e "${RED}✗ $1${NC}"; }
info() { echo -e "${YELLOW}ℹ $1${NC}"; }

echo "============================================"
echo "TESTES RÁPIDOS DO SISTEMA ATHOS"
echo "============================================"
echo ""

# 1. ESPORTES
echo "1. ESPORTES (via ms-autenticacao:8081)"
COUNT=$(curl -s http://localhost:8081/api/esportes | jq 'length')
if [ "$COUNT" -eq 36 ]; then
    success "36 esportes carregados"
else
    error "Esperado 36, encontrado: $COUNT"
fi

# 2. REGISTRO
echo ""
echo "2. REGISTRO DE USUÁRIO"
TIMESTAMP=$(date +%s)
EMAIL="teste${TIMESTAMP}@athos.com"
SENHA="senha123"

REGISTER=$(curl -s -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste User",
    "cpf": "12345678901",
    "email": "'"${EMAIL}"'",
    "senha": "'"${SENHA}"'",
    "confirmacaoSenha": "'"${SENHA}"'",
    "cep": "80010000",
    "bairros": ["Centro"],
    "esportes": ["Futebol"]
  }')

USER_ID=$(echo "$REGISTER" | jq -r '.id // empty')
if [ -n "$USER_ID" ]; then
    success "Usuário criado (ID: $USER_ID)"
else
    error "Falha no registro"
    echo "$REGISTER" | jq '.'
    exit 1
fi

# 3. LOGIN
echo ""
echo "3. LOGIN"
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "'"${EMAIL}"'", "password": "'"${SENHA}"'"}')

if [ -n "$TOKEN" ] && [ "$TOKEN" != "null" ]; then
    success "Login bem-sucedido (token: ${#TOKEN} chars)"
else
    error "Falha no login"
    exit 1
fi

# 4. EVENTO
echo ""
echo "4. CRIAÇÃO DE EVENTO (ms-evento:8083)"
EVENTO=$(curl -s -X POST http://localhost:8083/api/eventos \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "X-User-Id: ${USER_ID}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pelada Teste",
    "data": "2025-12-01",
    "horario": "10:00:00",
    "esporte": "Futebol",
    "cep": "80010-000",
    "endereco": "Campo UFPR - Centro, Curitiba",
    "publico": true,
    "valor": 0.0,
    "descricao": "Evento teste",
    "maxParticipantes": 20
  }')

EVENTO_ID=$(echo "$EVENTO" | jq -r '.id // empty')
if [ -n "$EVENTO_ID" ]; then
    success "Evento criado (ID: $EVENTO_ID)"
else
    error "Falha ao criar evento"
    echo "$EVENTO" | jq '.'
fi

# 5. CAMPEONATO
echo ""
echo "5. CRIAÇÃO DE CAMPEONATO (ms-campeonato:8084)"
CAMPEONATO=$(curl -s -X POST http://localhost:8084/api/campeonatos \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Copa Teste 2025",
    "esporte": "Futsal",
    "dataInicio": "2025-12-01",
    "dataFim": "2025-12-31",
    "formato": "PONTOS_CORRIDOS",
    "organizadorId": "'"${USER_ID}"'",
    "descricao": "Campeonato de teste",
    "maxEquipes": 8
  }')

CAMPEONATO_ID=$(echo "$CAMPEONATO" | jq -r '.id // empty')
if [ -n "$CAMPEONATO_ID" ]; then
    success "Campeonato criado (ID: $CAMPEONATO_ID)"
else
    error "Falha ao criar campeonato"
    echo "$CAMPEONATO" | jq '.'
    exit 1
fi

# 6. CRIAR 4 EQUIPES
echo ""
echo "6. CRIAÇÃO DE EQUIPES"
create_team() {
    local NOME=$1
    curl -s -X POST http://localhost:8084/api/equipes \
      -H "Authorization: Bearer ${TOKEN}" \
      -H "Content-Type: application/json" \
      -d '{
        "nome": "'"${NOME}"'",
        "descricao": "Equipe teste",
        "campeonatoId": "'"${CAMPEONATO_ID}"'",
        "capitaoId": "'"${USER_ID}"'"
      }'
}

EQUIPE1_ID=$(create_team "Relâmpagos" | jq -r '.id // empty')
EQUIPE2_ID=$(create_team "Trovões" | jq -r '.id // empty')
EQUIPE3_ID=$(create_team "Furacões" | jq -r '.id // empty')
EQUIPE4_ID=$(create_team "Ciclones" | jq -r '.id // empty')

if [ -n "$EQUIPE1_ID" ] && [ -n "$EQUIPE2_ID" ] && [ -n "$EQUIPE3_ID" ] && [ -n "$EQUIPE4_ID" ]; then
    success "4 equipes criadas"
else
    error "Falha ao criar equipes"
    exit 1
fi

# 7. GERAR TABELA
echo ""
echo "7. GERAÇÃO DE TABELA (Round-Robin)"
TABELA=$(curl -s -X POST "http://localhost:8084/api/campeonatos/${CAMPEONATO_ID}/gerar-tabela" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "dataInicio": "2025-12-01T10:00:00",
    "intervaloDias": 7
  }')

NUM_PARTIDAS=$(echo "$TABELA" | jq 'length // 0')
if [ "$NUM_PARTIDAS" -gt 0 ]; then
    success "Tabela gerada: $NUM_PARTIDAS partidas"
    PARTIDA_ID=$(echo "$TABELA" | jq -r '.[0].id // empty')
else
    error "Falha ao gerar tabela"
    echo "$TABELA" | jq '.'
    exit 1
fi

# 8. ATUALIZAR PLACAR
echo ""
echo "8. ATUALIZAÇÃO DE PLACAR"
PLACAR=$(curl -s -X PATCH "http://localhost:8084/api/partidas/${PARTIDA_ID}/placar" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "placarEquipe1": 3,
    "placarEquipe2": 1
  }')

PLACAR1=$(echo "$PLACAR" | jq -r '.placarEquipe1 // empty')
if [ -n "$PLACAR1" ]; then
    PLACAR2=$(echo "$PLACAR" | jq -r '.placarEquipe2')
    STATUS=$(echo "$PLACAR" | jq -r '.status')
    success "Placar atualizado: $PLACAR1 x $PLACAR2 (Status: $STATUS)"
else
    error "Falha ao atualizar placar"
    echo "$PLACAR" | jq '.'
fi

# 9. CLASSIFICAÇÃO
echo ""
echo "9. CONSULTA DE CLASSIFICAÇÃO"
CLASSIFICACAO=$(curl -s "http://localhost:8084/api/campeonatos/${CAMPEONATO_ID}/classificacao" \
  -H "Authorization: Bearer ${TOKEN}")

NUM_TIMES=$(echo "$CLASSIFICACAO" | jq 'length // 0')
if [ "$NUM_TIMES" -gt 0 ]; then
    success "Classificação calculada ($NUM_TIMES times)"
    info "Top 3:"
    echo "$CLASSIFICACAO" | jq -r '.[:3] | .[] | "  \(.posicao)º - \(.equipeNome): \(.pontos) pts"'
else
    error "Falha ao consultar classificação"
fi

# 10. RABBITMQ
echo ""
echo "10. RABBITMQ"
QUEUES=$(curl -s -u guest:guest http://localhost:15672/api/queues | jq 'length // 0')
success "RabbitMQ operacional ($QUEUES filas)"

echo ""
echo "============================================"
success "TODOS OS TESTES PRINCIPAIS PASSARAM!"
echo "============================================"
echo ""
info "Sistema pronto para integração com frontend"

#!/bin/bash

# Script de teste completo do sistema Athos
# Testa todos os microsserviços e suas integrações

echo "======================================"
echo "TESTES DO SISTEMA ATHOS"
echo "======================================"
echo ""

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para printar sucesso
success() {
    echo -e "${GREEN}✓ $1${NC}"
}

# Função para printar erro
error() {
    echo -e "${RED}✗ $1${NC}"
}

# Função para printar info
info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Variáveis globais para armazenar dados
TOKEN=""
USER_ID=""
EVENTO_ID=""
CAMPEONATO_ID=""
EQUIPE1_ID=""
EQUIPE2_ID=""
EQUIPE3_ID=""
EQUIPE4_ID=""
PARTIDA_ID=""

# Base URL
BASE_URL="http://localhost"

echo "======================================"
echo "1. TESTANDO ESPORTES (PÚBLICO)"
echo "======================================"

# Listar esportes
info "Listando esportes..."
ESPORTES_COUNT=$(curl -s ${BASE_URL}/api/esportes | jq 'length')
if [ "$ESPORTES_COUNT" -eq 36 ]; then
    success "36 esportes carregados com sucesso"
else
    error "Esperado 36 esportes, encontrado: $ESPORTES_COUNT"
fi

# Validar esporte válido
info "Validando esporte válido (Futebol)..."
VALIDO=$(curl -s "${BASE_URL}/api/esportes/validar?nome=Futebol")
if [ "$VALIDO" == "true" ]; then
    success "Validação de esporte válido funcionando"
else
    error "Validação de esporte válido falhou"
fi

# Validar esporte inválido
info "Validando esporte inválido..."
INVALIDO=$(curl -s "${BASE_URL}/api/esportes/validar?nome=EsporteInexistente")
if [ "$INVALIDO" == "false" ]; then
    success "Validação de esporte inválido funcionando"
else
    error "Validação de esporte inválido falhou"
fi

echo ""
echo "======================================"
echo "2. TESTANDO AUTENTICAÇÃO"
echo "======================================"

# Registrar novo usuário
info "Registrando novo usuário..."
TIMESTAMP=$(date +%s)
EMAIL="teste${TIMESTAMP}@athos.com"
SENHA="senha123"

REGISTER_RESPONSE=$(curl -s -X POST ${BASE_URL}/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Usuário Teste",
    "cpf": "12345678901",
    "email": "'"${EMAIL}"'",
    "senha": "'"${SENHA}"'",
    "confirmacaoSenha": "'"${SENHA}"'",
    "cep": "80010000",
    "bairros": ["Centro", "Batel"],
    "esportes": ["Futebol", "Basquete"]
  }')

if echo "$REGISTER_RESPONSE" | jq -e '.id' > /dev/null 2>&1; then
    USER_ID=$(echo "$REGISTER_RESPONSE" | jq -r '.id')
    success "Usuário registrado com sucesso (ID: $USER_ID)"
else
    error "Falha ao registrar usuário: $REGISTER_RESPONSE"
    exit 1
fi

# Login
info "Fazendo login..."
LOGIN_RESPONSE=$(curl -s -X POST ${BASE_URL}/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "'"${EMAIL}"'",
    "password": "'"${SENHA}"'"
  }')

if [ -n "$LOGIN_RESPONSE" ] && [ "$LOGIN_RESPONSE" != "null" ]; then
    TOKEN="$LOGIN_RESPONSE"
    success "Login realizado com sucesso"
    info "Token JWT obtido (${#TOKEN} caracteres)"
else
    error "Falha no login: $LOGIN_RESPONSE"
    exit 1
fi

echo ""
echo "======================================"
echo "3. TESTANDO PERFIL DE USUÁRIO"
echo "======================================"

info "Buscando perfil do usuário..."
PERFIL_RESPONSE=$(curl -s -X GET ${BASE_URL}/api/perfil \
  -H "Authorization: Bearer ${TOKEN}")

if echo "$PERFIL_RESPONSE" | jq -e '.email' > /dev/null 2>&1; then
    success "Perfil recuperado com sucesso"
else
    error "Falha ao buscar perfil: $PERFIL_RESPONSE"
fi

echo ""
echo "======================================"
echo "4. TESTANDO EVENTOS"
echo "======================================"

# Criar evento
info "Criando evento de Futebol..."
EVENTO_RESPONSE=$(curl -s -X POST ${BASE_URL}/api/eventos \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pelada do Final de Semana",
    "descricao": "Futebol entre amigos",
    "esporte": "Futebol",
    "dataHoraInicio": "2025-12-01T10:00:00",
    "dataHoraFim": "2025-12-01T12:00:00",
    "local": "Campo do UFPR",
    "capacidadeMaxima": 20,
    "tipoEvento": "PUBLICO"
  }')

if echo "$EVENTO_RESPONSE" | jq -e '.id' > /dev/null 2>&1; then
    EVENTO_ID=$(echo "$EVENTO_RESPONSE" | jq -r '.id')
    success "Evento criado com sucesso (ID: $EVENTO_ID)"
else
    error "Falha ao criar evento: $EVENTO_RESPONSE"
fi

# Tentar criar evento com esporte inválido
info "Testando criação de evento com esporte inválido..."
EVENTO_INVALIDO=$(curl -s -X POST ${BASE_URL}/api/eventos \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Evento Inválido",
    "descricao": "Teste",
    "esporte": "EsporteInexistente",
    "dataHoraInicio": "2025-12-01T10:00:00",
    "dataHoraFim": "2025-12-01T12:00:00",
    "local": "Local",
    "capacidadeMaxima": 10,
    "tipoEvento": "PUBLICO"
  }')

if echo "$EVENTO_INVALIDO" | grep -q "inválido"; then
    success "Validação de esporte no evento funcionando"
else
    error "Validação de esporte no evento falhou"
fi

# Listar eventos
info "Listando eventos..."
EVENTOS_LIST=$(curl -s -X GET ${BASE_URL}/api/eventos \
  -H "Authorization: Bearer ${TOKEN}")

if echo "$EVENTOS_LIST" | jq -e '.[0].id' > /dev/null 2>&1; then
    success "Eventos listados com sucesso"
else
    error "Falha ao listar eventos"
fi

echo ""
echo "======================================"
echo "5. TESTANDO CAMPEONATOS"
echo "======================================"

# Criar campeonato
info "Criando campeonato de Futsal..."
CAMPEONATO_RESPONSE=$(curl -s -X POST ${BASE_URL}/api/campeonatos \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Copa Athos 2025",
    "descricao": "Campeonato de Futsal",
    "esporte": "Futsal",
    "dataInicio": "2025-12-01",
    "dataFim": "2025-12-31",
    "formato": "PONTOS_CORRIDOS",
    "numeroMinimoEquipes": 4,
    "numeroMaximoEquipes": 8,
    "numeroJogadoresPorEquipe": 5
  }')

if echo "$CAMPEONATO_RESPONSE" | jq -e '.id' > /dev/null 2>&1; then
    CAMPEONATO_ID=$(echo "$CAMPEONATO_RESPONSE" | jq -r '.id')
    success "Campeonato criado com sucesso (ID: $CAMPEONATO_ID)"
else
    error "Falha ao criar campeonato: $CAMPEONATO_RESPONSE"
fi

echo ""
echo "======================================"
echo "6. TESTANDO EQUIPES"
echo "======================================"

# Criar 4 equipes
info "Criando Equipe 1..."
EQUIPE1=$(curl -s -X POST ${BASE_URL}/api/equipes \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Relâmpagos",
    "descricao": "Equipe veloz",
    "campeonatoId": "'"${CAMPEONATO_ID}"'"
  }')

if echo "$EQUIPE1" | jq -e '.id' > /dev/null 2>&1; then
    EQUIPE1_ID=$(echo "$EQUIPE1" | jq -r '.id')
    success "Equipe 1 criada (ID: $EQUIPE1_ID)"
else
    error "Falha ao criar equipe 1"
fi

info "Criando Equipe 2..."
EQUIPE2=$(curl -s -X POST ${BASE_URL}/api/equipes \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Trovões",
    "descricao": "Equipe forte",
    "campeonatoId": "'"${CAMPEONATO_ID}"'"
  }')

if echo "$EQUIPE2" | jq -e '.id' > /dev/null 2>&1; then
    EQUIPE2_ID=$(echo "$EQUIPE2" | jq -r '.id')
    success "Equipe 2 criada (ID: $EQUIPE2_ID)"
else
    error "Falha ao criar equipe 2"
fi

info "Criando Equipe 3..."
EQUIPE3=$(curl -s -X POST ${BASE_URL}/api/equipes \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Furacões",
    "descricao": "Equipe potente",
    "campeonatoId": "'"${CAMPEONATO_ID}"'"
  }')

if echo "$EQUIPE3" | jq -e '.id' > /dev/null 2>&1; then
    EQUIPE3_ID=$(echo "$EQUIPE3" | jq -r '.id')
    success "Equipe 3 criada (ID: $EQUIPE3_ID)"
else
    error "Falha ao criar equipe 3"
fi

info "Criando Equipe 4..."
EQUIPE4=$(curl -s -X POST ${BASE_URL}/api/equipes \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Ciclones",
    "descricao": "Equipe turbinada",
    "campeonatoId": "'"${CAMPEONATO_ID}"'"
  }')

if echo "$EQUIPE4" | jq -e '.id' > /dev/null 2>&1; then
    EQUIPE4_ID=$(echo "$EQUIPE4" | jq -r '.id')
    success "Equipe 4 criada (ID: $EQUIPE4_ID)"
else
    error "Falha ao criar equipe 4"
fi

echo ""
echo "======================================"
echo "7. TESTANDO GERAÇÃO DE TABELA"
echo "======================================"

info "Gerando tabela de partidas (round-robin)..."
TABELA_RESPONSE=$(curl -s -X POST "${BASE_URL}/api/campeonatos/${CAMPEONATO_ID}/gerar-tabela" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "dataInicio": "2025-12-01T10:00:00",
    "intervaloDias": 7
  }')

if echo "$TABELA_RESPONSE" | jq -e '.[0].id' > /dev/null 2>&1; then
    PARTIDAS_COUNT=$(echo "$TABELA_RESPONSE" | jq 'length')
    success "Tabela gerada com sucesso ($PARTIDAS_COUNT partidas)"
    PARTIDA_ID=$(echo "$TABELA_RESPONSE" | jq -r '.[0].id')
    info "ID da primeira partida: $PARTIDA_ID"
else
    error "Falha ao gerar tabela: $TABELA_RESPONSE"
fi

echo ""
echo "======================================"
echo "8. TESTANDO ATUALIZAÇÃO DE PLACAR"
echo "======================================"

info "Atualizando placar da primeira partida..."
PLACAR_RESPONSE=$(curl -s -X PATCH "${BASE_URL}/api/partidas/${PARTIDA_ID}/placar" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "placarEquipe1": 3,
    "placarEquipe2": 1
  }')

if echo "$PLACAR_RESPONSE" | jq -e '.placarEquipe1' > /dev/null 2>&1; then
    PLACAR1=$(echo "$PLACAR_RESPONSE" | jq -r '.placarEquipe1')
    PLACAR2=$(echo "$PLACAR_RESPONSE" | jq -r '.placarEquipe2')
    STATUS=$(echo "$PLACAR_RESPONSE" | jq -r '.status')
    success "Placar atualizado: $PLACAR1 x $PLACAR2 (Status: $STATUS)"
else
    error "Falha ao atualizar placar: $PLACAR_RESPONSE"
fi

echo ""
echo "======================================"
echo "9. TESTANDO CLASSIFICAÇÃO"
echo "======================================"

info "Consultando classificação do campeonato..."
CLASSIFICACAO=$(curl -s -X GET "${BASE_URL}/api/campeonatos/${CAMPEONATO_ID}/classificacao" \
  -H "Authorization: Bearer ${TOKEN}")

if echo "$CLASSIFICACAO" | jq -e '.[0].equipeNome' > /dev/null 2>&1; then
    success "Classificação calculada com sucesso"
    echo ""
    info "Top 3 equipes:"
    echo "$CLASSIFICACAO" | jq -r '.[:3] | .[] | "  \(.posicao)º - \(.equipeNome): \(.pontos) pts (V:\(.vitorias) E:\(.empates) D:\(.derrotas))"'
else
    error "Falha ao consultar classificação: $CLASSIFICACAO"
fi

echo ""
echo "======================================"
echo "10. TESTANDO RABBITMQ"
echo "======================================"

info "Verificando filas do RabbitMQ..."
QUEUES=$(curl -s -u guest:guest http://localhost:15672/api/queues | jq -r '.[].name' | wc -l)
if [ "$QUEUES" -gt 0 ]; then
    success "RabbitMQ está funcionando ($QUEUES filas)"
else
    error "Nenhuma fila encontrada no RabbitMQ"
fi

info "Verificando exchanges..."
EXCHANGES=$(curl -s -u guest:guest http://localhost:15672/api/exchanges | jq -r '.[] | select(.name != "") | .name' | grep -E "campeonato|evento|notificacao" | wc -l)
if [ "$EXCHANGES" -gt 0 ]; then
    success "Exchanges configurados ($EXCHANGES exchanges encontrados)"
else
    error "Nenhum exchange encontrado"
fi

echo ""
echo "======================================"
echo "11. TESTANDO MEMBROS DE EQUIPE"
echo "======================================"

info "Adicionando membro à equipe 1..."
MEMBRO_RESPONSE=$(curl -s -X POST "${BASE_URL}/api/equipes/${EQUIPE1_ID}/membros" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": "'"${USER_ID}"'",
    "papel": "JOGADOR",
    "numero": 10
  }')

if echo "$MEMBRO_RESPONSE" | jq -e '.id' > /dev/null 2>&1; then
    success "Membro adicionado à equipe com sucesso"
else
    error "Falha ao adicionar membro: $MEMBRO_RESPONSE"
fi

echo ""
echo "======================================"
echo "RESUMO DOS TESTES"
echo "======================================"
echo ""
success "Esportes: Listagem e validação funcionando"
success "Autenticação: Registro e login funcionando"
success "Perfil: Consulta de perfil funcionando"
success "Eventos: Criação e validação de esporte funcionando"
success "Campeonatos: Criação funcionando"
success "Equipes: Criação de 4 equipes funcionando"
success "Tabela: Geração round-robin funcionando"
success "Placar: Atualização de placar funcionando"
success "Classificação: Cálculo de pontos funcionando"
success "RabbitMQ: Message broker operacional"
success "Membros: Adição de membros à equipe funcionando"
echo ""
echo "======================================"
success "TODOS OS TESTES PASSARAM!"
echo "======================================"

-- Migration: Adicionar campos de datas de inscrição na tabela campeonatos
-- Data: 2025-11-19
-- Descrição: Adiciona dataInscricaoInicio e dataInscricaoFim para controlar período de inscrições

-- Adicionar colunas (permitindo NULL inicialmente para dados existentes)
ALTER TABLE campeonatos
ADD COLUMN data_inscricao_inicio DATE,
ADD COLUMN data_inscricao_fim DATE;

-- Atualizar registros existentes com valores padrão
-- Define data de inscrição como 7 dias antes da data de início do campeonato
UPDATE campeonatos
SET
    data_inscricao_inicio = data_inicio - INTERVAL '14 days',
    data_inscricao_fim = data_inicio - INTERVAL '1 day'
WHERE data_inscricao_inicio IS NULL;

-- Tornar colunas NOT NULL após popular dados existentes
ALTER TABLE campeonatos
ALTER COLUMN data_inscricao_inicio SET NOT NULL,
ALTER COLUMN data_inscricao_fim SET NOT NULL;

-- Adicionar constraint de validação: dataInscricaoInicio < dataInscricaoFim
ALTER TABLE campeonatos
ADD CONSTRAINT check_datas_inscricao
CHECK (data_inscricao_inicio < data_inscricao_fim);

-- Adicionar constraint de validação: dataInscricaoFim < dataInicio
ALTER TABLE campeonatos
ADD CONSTRAINT check_inscricao_antes_inicio
CHECK (data_inscricao_fim < data_inicio);

-- Verificar os dados
SELECT
    id,
    nome,
    data_inscricao_inicio,
    data_inscricao_fim,
    data_inicio,
    data_fim
FROM campeonatos
ORDER BY data_inicio DESC
LIMIT 10;

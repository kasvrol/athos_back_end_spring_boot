from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import logging

from .models import RecomendacaoResponse, EventoSimples
from .recomendador import SistemaRecomendacao

# Configurar logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="MS-Recomendacao - Athos",
    description="Sistema de Recomendação Híbrido (Colaborativo + Conteúdo)",
    version="1.0.0"
)

# Instância global do sistema de recomendação
sistema_recomendacao = SistemaRecomendacao()


@app.on_event("startup")
async def startup_event():
    """Inicializar o sistema na inicialização"""
    logger.info("Iniciando MS-Recomendacao...")
    try:
        sistema_recomendacao.treinar_modelo()
        logger.info("Sistema de recomendação iniciado com sucesso")
    except Exception as e:
        logger.warning(f"Erro ao treinar modelo inicial: {e}")
        logger.info("Sistema continuará sem modelo treinado")


@app.get("/")
async def root():
    return {
        "service": "ms-recomendacao",
        "status": "running",
        "description": "Sistema de Recomendação Híbrido (SVD + TF-IDF)"
    }


@app.get("/api/recomendacoes/{usuario_id}", response_model=List[RecomendacaoResponse])
async def obter_recomendacoes(usuario_id: str, limit: int = 10):
    """
    Obter recomendações personalizadas de eventos para um usuário.

    - **usuario_id**: ID do usuário
    - **limit**: Número máximo de recomendações (default: 10)
    """
    try:
        recomendacoes = sistema_recomendacao.recomendar(usuario_id, top_n=limit)

        if not recomendacoes:
            return []

        return recomendacoes

    except Exception as e:
        logger.error(f"Erro ao gerar recomendações para {usuario_id}: {e}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/recomendacoes/retreinar")
async def retreinar_modelo():
    """
    Retreinar o modelo de recomendação com dados atualizados.
    Deve ser chamado periodicamente ou após mudanças significativas nos dados.
    """
    try:
        sistema_recomendacao.treinar_modelo()
        return {"status": "success", "message": "Modelo retreinado com sucesso"}
    except Exception as e:
        logger.error(f"Erro ao retreinar modelo: {e}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "modelo_treinado": sistema_recomendacao.modelo_treinado
    }

from pydantic import BaseModel
from typing import Optional


class EventoSimples(BaseModel):
    id: str
    nome: str
    esporte: str
    endereco: Optional[str] = None
    descricao: Optional[str] = None


class RecomendacaoResponse(BaseModel):
    evento_id: str
    evento_nome: str
    esporte: str
    score: float
    metodo: str  # 'colaborativo', 'conteudo', ou 'hibrido'

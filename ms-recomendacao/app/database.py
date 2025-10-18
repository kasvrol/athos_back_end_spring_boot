import os
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker
import pandas as pd
import logging

logger = logging.getLogger(__name__)

# Configuração do banco (PostgreSQL)
DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://admin:password@postgres-db:5432/athos_evento_db"
)

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


def obter_dados_avaliacoes():
    """Obter avaliações do banco MongoDB de avaliações"""
    # TODO: Conectar ao MongoDB do ms-avaliacao para pegar avaliações reais
    # Por enquanto, retorna DataFrame vazio para não quebrar
    logger.warning("Dados de avaliações ainda não implementados")
    return pd.DataFrame(columns=['usuario_id', 'evento_id', 'nota'])


def obter_dados_participacoes():
    """Obter participações confirmadas em eventos"""
    try:
        query = """
            SELECT
                usuario_id,
                evento_id,
                1 as interacao
            FROM participacao_evento
            WHERE status = 'CONFIRMADO'
        """

        with engine.connect() as conn:
            df = pd.read_sql(text(query), conn)

        logger.info(f"Carregadas {len(df)} participações")
        return df

    except Exception as e:
        logger.error(f"Erro ao carregar participações: {e}")
        return pd.DataFrame(columns=['usuario_id', 'evento_id', 'interacao'])


def obter_dados_eventos():
    """Obter dados dos eventos para filtragem por conteúdo"""
    try:
        query = """
            SELECT
                id,
                nome,
                esporte,
                endereco,
                descricao,
                cep
            FROM eventos
        """

        with engine.connect() as conn:
            df = pd.read_sql(text(query), conn)

        # Criar coluna de texto combinado para TF-IDF
        df['texto_completo'] = (
            df['nome'].fillna('') + ' ' +
            df['esporte'].fillna('') + ' ' +
            df['descricao'].fillna('') + ' ' +
            df['endereco'].fillna('')
        )

        logger.info(f"Carregados {len(df)} eventos")
        return df

    except Exception as e:
        logger.error(f"Erro ao carregar eventos: {e}")
        return pd.DataFrame(columns=['id', 'nome', 'esporte', 'endereco', 'descricao', 'texto_completo'])

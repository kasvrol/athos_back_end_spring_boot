import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from surprise import SVD, Dataset, Reader
from surprise.model_selection import train_test_split
import logging

from .database import obter_dados_participacoes, obter_dados_eventos, obter_dados_avaliacoes
from .models import RecomendacaoResponse

logger = logging.getLogger(__name__)


class SistemaRecomendacao:
    def __init__(self, alpha=0.6):
        """
        Sistema Híbrido de Recomendação

        alpha: peso para filtragem colaborativa (0-1)
               (1-alpha) será o peso da filtragem por conteúdo
        """
        self.alpha = alpha
        self.modelo_colaborativo = None
        self.matriz_similaridade = None
        self.eventos_df = None
        self.tfidf_vectorizer = None
        self.modelo_treinado = False

    def treinar_modelo(self):
        """Treinar ambos os modelos (colaborativo e conteúdo)"""
        logger.info("Iniciando treinamento do modelo...")

        try:
            # Treinar modelo colaborativo
            self._treinar_colaborativo()

            # Treinar modelo de conteúdo
            self._treinar_conteudo()

            self.modelo_treinado = True
            logger.info("Modelos treinados com sucesso")

        except Exception as e:
            logger.error(f"Erro no treinamento: {e}")
            self.modelo_treinado = False
            raise

    def _treinar_colaborativo(self):
        """Treinar modelo de filtragem colaborativa (SVD)"""
        logger.info("Treinando modelo colaborativo (SVD)...")

        # Obter dados de interações
        participacoes = obter_dados_participacoes()
        avaliacoes = obter_dados_avaliacoes()

        # Combinar participações e avaliações
        if len(avaliacoes) > 0:
            interacoes = pd.concat([
                participacoes,
                avaliacoes[['usuario_id', 'evento_id', 'nota']].rename(columns={'nota': 'interacao'})
            ])
        else:
            interacoes = participacoes

        if len(interacoes) == 0:
            logger.warning("Nenhuma interação encontrada para treinar modelo colaborativo")
            return

        # Preparar dados para Surprise
        reader = Reader(rating_scale=(0, 5))
        data = Dataset.load_from_df(
            interacoes[['usuario_id', 'evento_id', 'interacao']],
            reader
        )

        # Treinar SVD
        self.modelo_colaborativo = SVD(n_factors=20, n_epochs=20)
        trainset = data.build_full_trainset()
        self.modelo_colaborativo.fit(trainset)

        logger.info("Modelo colaborativo treinado")

    def _treinar_conteudo(self):
        """Treinar modelo de filtragem por conteúdo (TF-IDF + Cosseno)"""
        logger.info("Treinando modelo de conteúdo (TF-IDF)...")

        # Obter dados dos eventos
        self.eventos_df = obter_dados_eventos()

        if len(self.eventos_df) == 0:
            logger.warning("Nenhum evento encontrado para treinar modelo de conteúdo")
            return

        # Criar vetorizador TF-IDF
        self.tfidf_vectorizer = TfidfVectorizer(
            max_features=100,
            stop_words='english',
            ngram_range=(1, 2)
        )

        # Criar matriz TF-IDF
        tfidf_matrix = self.tfidf_vectorizer.fit_transform(
            self.eventos_df['texto_completo']
        )

        # Calcular similaridade de cosseno
        self.matriz_similaridade = cosine_similarity(tfidf_matrix, tfidf_matrix)

        logger.info(f"Modelo de conteúdo treinado com {len(self.eventos_df)} eventos")

    def recomendar(self, usuario_id: str, top_n: int = 10):
        """
        Gerar recomendações híbridas para um usuário
        """
        if not self.modelo_treinado:
            logger.warning("Modelo não treinado, tentando treinar...")
            self.treinar_modelo()

        # Obter recomendações colaborativas
        recom_colaborativas = self._recomendar_colaborativo(usuario_id, top_n * 2)

        # Obter recomendações por conteúdo
        recom_conteudo = self._recomendar_conteudo(usuario_id, top_n * 2)

        # Combinar (hibridização ponderada)
        recomendacoes_finais = self._combinar_recomendacoes(
            recom_colaborativas,
            recom_conteudo,
            top_n
        )

        return recomendacoes_finais

    def _recomendar_colaborativo(self, usuario_id: str, top_n: int):
        """Gerar recomendações usando filtragem colaborativa"""
        if self.modelo_colaborativo is None:
            return []

        try:
            # Obter todos os eventos
            todos_eventos = self.eventos_df['id'].tolist()

            # Predizer scores para todos os eventos
            predicoes = []
            for evento_id in todos_eventos:
                pred = self.modelo_colaborativo.predict(usuario_id, evento_id)
                predicoes.append({
                    'evento_id': evento_id,
                    'score': pred.est
                })

            # Ordenar por score
            predicoes = sorted(predicoes, key=lambda x: x['score'], reverse=True)

            return predicoes[:top_n]

        except Exception as e:
            logger.error(f"Erro na recomendação colaborativa: {e}")
            return []

    def _recomendar_conteudo(self, usuario_id: str, top_n: int):
        """Gerar recomendações usando filtragem por conteúdo"""
        if self.matriz_similaridade is None or len(self.eventos_df) == 0:
            return []

        try:
            # Obter eventos que o usuário já participou
            participacoes = obter_dados_participacoes()
            eventos_usuario = participacoes[
                participacoes['usuario_id'] == usuario_id
            ]['evento_id'].tolist()

            if not eventos_usuario:
                # Se usuário nunca participou, retornar eventos populares
                return []

            # Para cada evento do usuário, encontrar similares
            scores_eventos = {}

            for evento_id in eventos_usuario:
                # Encontrar índice do evento
                idx = self.eventos_df[self.eventos_df['id'] == evento_id].index

                if len(idx) == 0:
                    continue

                idx = idx[0]

                # Obter scores de similaridade
                sim_scores = list(enumerate(self.matriz_similaridade[idx]))

                # Acumular scores
                for i, score in sim_scores:
                    evento_similar_id = self.eventos_df.iloc[i]['id']

                    if evento_similar_id not in eventos_usuario:
                        if evento_similar_id not in scores_eventos:
                            scores_eventos[evento_similar_id] = 0
                        scores_eventos[evento_similar_id] += score

            # Converter para lista e ordenar
            recomendacoes = [
                {'evento_id': k, 'score': v}
                for k, v in scores_eventos.items()
            ]
            recomendacoes = sorted(recomendacoes, key=lambda x: x['score'], reverse=True)

            return recomendacoes[:top_n]

        except Exception as e:
            logger.error(f"Erro na recomendação por conteúdo: {e}")
            return []

    def _combinar_recomendacoes(self, colab, conteudo, top_n):
        """Combinar recomendações usando média ponderada"""
        scores_combinados = {}

        # Normalizar scores colaborativos
        if colab:
            max_colab = max([r['score'] for r in colab])
            min_colab = min([r['score'] for r in colab])
            range_colab = max_colab - min_colab if max_colab != min_colab else 1

            for rec in colab:
                score_norm = (rec['score'] - min_colab) / range_colab
                scores_combinados[rec['evento_id']] = self.alpha * score_norm

        # Normalizar scores de conteúdo
        if conteudo:
            max_cont = max([r['score'] for r in conteudo])
            min_cont = min([r['score'] for r in conteudo])
            range_cont = max_cont - min_cont if max_cont != min_cont else 1

            for rec in conteudo:
                score_norm = (rec['score'] - min_cont) / range_cont
                evento_id = rec['evento_id']

                if evento_id in scores_combinados:
                    scores_combinados[evento_id] += (1 - self.alpha) * score_norm
                else:
                    scores_combinados[evento_id] = (1 - self.alpha) * score_norm

        # Ordenar e pegar top N
        recomendacoes_ordenadas = sorted(
            scores_combinados.items(),
            key=lambda x: x[1],
            reverse=True
        )[:top_n]

        # Criar resposta com informações dos eventos
        resultado = []
        for evento_id, score in recomendacoes_ordenadas:
            evento = self.eventos_df[self.eventos_df['id'] == evento_id]

            if len(evento) > 0:
                evento = evento.iloc[0]
                resultado.append(RecomendacaoResponse(
                    evento_id=evento_id,
                    evento_nome=evento['nome'],
                    esporte=evento['esporte'],
                    score=float(score),
                    metodo='hibrido'
                ))

        return resultado

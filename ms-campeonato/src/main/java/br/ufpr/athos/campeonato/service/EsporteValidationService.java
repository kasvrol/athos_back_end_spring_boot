package br.ufpr.athos.campeonato.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EsporteValidationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.autenticacao.url:http://ms-autenticacao:8080}")
    private String autenticacaoUrl;

    public boolean validarEsporte(String esporte) {
        try {
            String url = autenticacaoUrl + "/api/esportes/validar?nome=" + esporte;
            Boolean resultado = restTemplate.getForObject(url, Boolean.class);
            return resultado != null && resultado;
        } catch (Exception e) {
            // Log error and return false
            System.err.println("Erro ao validar esporte: " + e.getMessage());
            return false;
        }
    }
}

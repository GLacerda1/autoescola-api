package br.com.autoescola.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class IntegrationConfig {
    @Bean
    RestClient viaCepRestClient(@Value("${integracao.viacep.base-url:https://viacep.com.br}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}

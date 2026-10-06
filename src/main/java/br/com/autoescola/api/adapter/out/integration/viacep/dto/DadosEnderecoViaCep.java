package br.com.autoescola.api.adapter.out.integration.viacep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosEnderecoViaCep(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        @JsonProperty("localidade") String cidade,
        String uf,
        @JsonProperty("erro") Boolean erro) {
}

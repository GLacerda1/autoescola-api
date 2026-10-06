package br.com.autoescola.api.adapter.out.integration.viacep;

import br.com.autoescola.api.adapter.out.integration.viacep.dto.DadosEnderecoViaCep;
import br.com.autoescola.api.exception.type.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class ViaCepService {
    private final RestClient viaCepRestClient;

    public DadosEnderecoViaCep consultar(String cepInformado) {
        String cep = cepInformado == null ? "" : cepInformado.replaceAll("\\D", "");
        if (!cep.matches("[0-9]{8}")) {
            throw new ValidacaoException("Informe um CEP com exatamente 8 números.");
        }
        try {
            DadosEnderecoViaCep resposta = viaCepRestClient.get()
                    .uri("/ws/{cep}/json/", cep)
                    .retrieve()
                    .body(DadosEnderecoViaCep.class);
            if (resposta == null || Boolean.TRUE.equals(resposta.erro())) {
                throw new ViaCepNotFoundException("O CEP informado não foi encontrado.");
            }
            return resposta;
        } catch (ViaCepNotFoundException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new ViaCepIndisponivelException("Não foi possível consultar o ViaCEP no momento.", ex);
        }
    }
}

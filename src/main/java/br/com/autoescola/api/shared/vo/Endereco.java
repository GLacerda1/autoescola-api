package br.com.autoescola.api.shared.vo;

import br.com.autoescola.api.shared.vo.dto.DadosEndereco;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Endereco {
    @Column(nullable = false, length = 100)
    private String logradouro;
    @Column(length = 10)
    private String numero;
    @Column(length = 20)
    private String complemento;
    @Column(nullable = false, length = 100)
    private String bairro;
    @Column(nullable = false, length = 100)
    private String cidade;
    @Column(nullable = false, length = 2)
    private String uf;
    @Column(nullable = false, length = 9)
    private String cep;

    public Endereco(DadosEndereco dados) {
        this.logradouro = dados.logradouro();
        this.numero = dados.numero();
        this.complemento = dados.complemento();
        this.bairro = dados.bairro();
        this.cidade = dados.cidade();
        this.uf = dados.uf();
        this.cep = dados.cep();
    }

    public void atualizarInformacoes(DadosEndereco dados) {
        if(dados.logradouro() != null && !dados.logradouro().isBlank()) {
            this.logradouro = dados.logradouro();
        }
        if(dados.numero() != null) {
            this.numero = dados.numero();
        }
        if(dados.complemento() != null) {
            this.complemento = dados.complemento();
        }
        if(dados.bairro() != null && !dados.bairro().isBlank()) {
            this.bairro = dados.bairro();
        }
        if(dados.cidade() != null && !dados.cidade().isBlank()) {
            this.cidade = dados.cidade();
        }
        if(dados.uf() != null && !dados.uf().isBlank()) {
            this.uf = dados.uf();
        }
        if(dados.cep() != null && !dados.cep().isBlank()) {
            this.cep = dados.cep();
        }
    }
}

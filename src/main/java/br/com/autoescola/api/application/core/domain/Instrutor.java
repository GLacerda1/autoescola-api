package br.com.autoescola.api.application.core.domain;

import br.com.autoescola.api.shared.vo.Endereco;
import br.com.autoescola.api.adapter.in.controller.request.instrutor.DadosAtualizacaoInstrutor;
import br.com.autoescola.api.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import br.com.autoescola.api.exception.type.ValidacaoException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity(name = "Instrutor")
@Table(name = "instrutores")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Instrutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false, length = 20)
    private String telefone;
    @Column(nullable = false, unique = true, length = 11)
    private String cnh;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;
    @Column(nullable = false)
    private boolean ativo = true;

    public Instrutor(
            Long id,
            String nome,
            String email,
            String telefone,
            String cnh,
            Especialidade especialidade,
            Endereco endereco,
            boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cnh = cnh;
        this.especialidade = especialidade;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCnh() {
        return cnh;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Instrutor(DadosCadastroInstrutor dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cnh = dados.cnh();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(DadosAtualizacaoInstrutor dados) {
        if (dados.email() != null && !dados.email().equals(this.email)) {
            throw new ValidacaoException("O e-mail do instrutor não pode ser alterado.");
        }
        if (dados.cnh() != null && !dados.cnh().equals(this.cnh)) {
            throw new ValidacaoException("A CNH do instrutor não pode ser alterada.");
        }
        if (dados.especialidade() != null && dados.especialidade() != this.especialidade) {
            throw new ValidacaoException("A especialidade do instrutor não pode ser alterada.");
        }
        if(dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome();
        }
        if(dados.telefone() != null && !dados.telefone().isBlank()) {
            this.telefone = dados.telefone();
        }
        if(dados.endereco() != null) {
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}

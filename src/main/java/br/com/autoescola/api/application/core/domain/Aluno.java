package br.com.autoescola.api.application.core.domain;

import br.com.autoescola.api.adapter.in.controller.request.aluno.DadosAtualizacaoAluno;
import br.com.autoescola.api.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.autoescola.api.shared.vo.Endereco;
import br.com.autoescola.api.exception.type.ValidacaoException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Aluno")
@Table(name = "alunos")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Aluno {
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
    private String cpf;

    @Embedded
    private Endereco endereco;
    @Column(nullable = false)
    private boolean ativo = true;

    public Aluno(DadosCadastroAluno dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.endereco = new Endereco(dados.endereco());
    }

    public Aluno(Long id, String nome, String email, String telefone, String cpf, Endereco endereco, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public void atualizarInformacoes(DadosAtualizacaoAluno dados) {
        if (dados.email() != null && !dados.email().equals(this.email)) {
            throw new ValidacaoException("O e-mail do aluno não pode ser alterado.");
        }
        if (dados.cpf() != null && !dados.cpf().equals(this.cpf)) {
            throw new ValidacaoException("O CPF do aluno não pode ser alterado.");
        }
        if (dados.nome() != null && !dados.nome().isBlank()) this.nome = dados.nome();
        if (dados.telefone() != null && !dados.telefone().isBlank()) this.telefone = dados.telefone();
        if (dados.endereco() != null) this.endereco.atualizarInformacoes(dados.endereco());
    }

    public void excluir() {
        this.ativo = false;
    }
}

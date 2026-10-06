package br.com.autoescola.api.adapter.in.controller.response.aluno;

import br.com.autoescola.api.application.core.domain.Aluno;

public record DadosListagemAluno(Long id, String nome, String email, String cpf) {
    public DadosListagemAluno(Aluno aluno) {
        this(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getCpf());
    }
}

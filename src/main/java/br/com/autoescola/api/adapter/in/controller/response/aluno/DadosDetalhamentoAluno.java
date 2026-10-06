package br.com.autoescola.api.adapter.in.controller.response.aluno;

import br.com.autoescola.api.application.core.domain.Aluno;
import br.com.autoescola.api.shared.vo.dto.DadosEndereco;

public record DadosDetalhamentoAluno(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        DadosEndereco endereco,
        boolean ativo) {
    public DadosDetalhamentoAluno(Aluno aluno) {
        this(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getTelefone(), aluno.getCpf(),
                new DadosEndereco(aluno.getEndereco()), aluno.isAtivo());
    }
}

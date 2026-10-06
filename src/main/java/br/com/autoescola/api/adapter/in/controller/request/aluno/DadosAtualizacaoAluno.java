package br.com.autoescola.api.adapter.in.controller.request.aluno;

import br.com.autoescola.api.shared.vo.dto.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoAluno(
        @NotNull Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        @Valid DadosEndereco endereco) {
}

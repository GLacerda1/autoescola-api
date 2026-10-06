package br.com.autoescola.api.adapter.in.controller.request.instrutor;

import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import br.com.autoescola.api.shared.vo.dto.DadosEndereco;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

public record DadosAtualizacaoInstrutor(
        @NotNull
        Long id,
        String nome,
        String email,
        String telefone,
        String cnh,
        Especialidade especialidade,
        @Valid DadosEndereco endereco) {
}

package br.com.autoescola.api.adapter.in.controller.response.instrutor;

import br.com.autoescola.api.application.core.domain.Instrutor;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import br.com.autoescola.api.shared.vo.dto.DadosEndereco;

public record DadosDetalhamentoInstrutor(
        Long id,
        String nome,
        String email,
        String telefone,
        String cnh,
        Especialidade especialidade,
        DadosEndereco endereco,
        boolean ativo) {
    public DadosDetalhamentoInstrutor(Instrutor instrutor) {
        this(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getTelefone(),
                instrutor.getCnh(),
                instrutor.getEspecialidade(),
                new DadosEndereco(instrutor.getEndereco()),
                instrutor.isAtivo()
        );
    }
}
package br.com.autoescola.api.adapter.in.controller.response.instrutor;

import br.com.autoescola.api.application.core.domain.Instrutor;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;

public record DadosListagemInstrutor(
        Long id,
        String nome,
        String email,
        String cnh,
        Especialidade especialidade) {
    public DadosListagemInstrutor(Instrutor instrutor) {
        this(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getCnh(),
                instrutor.getEspecialidade()
        );
    }
}

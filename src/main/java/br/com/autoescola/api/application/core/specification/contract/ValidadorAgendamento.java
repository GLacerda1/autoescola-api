package br.com.autoescola.api.application.core.specification.contract;

import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;

public interface ValidadorAgendamento {
    void validar(DadosAgendamento dados);
}
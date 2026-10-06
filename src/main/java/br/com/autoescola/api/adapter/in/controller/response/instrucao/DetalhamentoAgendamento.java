package br.com.autoescola.api.adapter.in.controller.response.instrucao;

import br.com.autoescola.api.application.core.domain.Instrucao;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import br.com.autoescola.api.shared.vo.enumeration.MotivoCancelamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DetalhamentoAgendamento(
        Long id,

        @JsonProperty("nome_aluno")
        String nomeAluno,

        @JsonProperty("nome_instrutor")
        String nomeInstrutor,
        Especialidade especialidade,

        @JsonProperty("data_hora")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
        LocalDateTime dataHora,
        String situacao,
        MotivoCancelamento motivoCancelamento) {
    public DetalhamentoAgendamento(Instrucao instrucao) {
        this(
                instrucao.getId(),
                instrucao.getAluno().getNome(),
                instrucao.getInstrutor().getNome(),
                instrucao.getInstrutor().getEspecialidade(),
                instrucao.getDataHora(),
                instrucao.getSituacao().name(),
                instrucao.getMotivoCancelamento()
        );
    }
}

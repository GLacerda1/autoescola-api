package br.com.autoescola.api.adapter.in.controller.request.instrucao;

import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAtualizacaoInstrucao(
        @JsonProperty("id_instrutor") Long idInstrutor,
        Especialidade especialidade,
        @NotNull @Future
        @JsonProperty("data_hora")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
        LocalDateTime dataHora) {
}

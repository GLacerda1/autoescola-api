package br.com.autoescola.api.adapter.in.controller.request.instrucao;

import br.com.autoescola.api.shared.vo.enumeration.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoInstrucao(@NotNull MotivoCancelamento motivo) {
}

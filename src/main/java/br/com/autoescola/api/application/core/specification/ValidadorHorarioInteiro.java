package br.com.autoescola.api.application.core.specification;

import br.com.autoescola.api.application.core.specification.contract.ValidadorAgendamento;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.autoescola.api.exception.type.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorHorarioInteiro implements ValidadorAgendamento {
    @Override
    public void validar(DadosAgendamento dados) {
        LocalDateTime dataEscolhida = dados.dataHora();

        if (dataEscolhida.getMinute() != 0
                /*&& dataEscolhida.getSecond() != 0
                && dataEscolhida.getNano() != 0*/) {
            throw new ValidacaoException("Este campo deve ser preenchido apenas com horas inteiras (ex: 09:00, 13:00,...");
        }
    }
}
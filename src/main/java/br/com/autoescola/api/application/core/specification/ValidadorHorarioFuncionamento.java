package br.com.autoescola.api.application.core.specification;

import br.com.autoescola.api.application.core.specification.contract.ValidadorAgendamento;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.autoescola.api.exception.type.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamento {
    @Override
    public void validar(DadosAgendamento dados) {
        LocalDateTime dataEscolhida = dados.dataHora();

        boolean domingo = dataEscolhida.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean preAbertura = dataEscolhida.getHour() < 6;
        boolean posFechamento = dataEscolhida.getHour() > (21 - 1);

        if (domingo || preAbertura || posFechamento) {
            throw new ValidacaoException("Tentativa de agendamento fora do horário de funcionamento!");
        }
    }
}
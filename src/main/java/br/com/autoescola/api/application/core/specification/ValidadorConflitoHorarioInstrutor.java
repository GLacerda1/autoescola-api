package br.com.autoescola.api.application.core.specification;

import br.com.autoescola.api.application.core.specification.contract.ValidadorAgendamento;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.autoescola.api.application.port.out.InstrucaoRepository;
import br.com.autoescola.api.application.core.domain.SituacaoInstrucao;
import br.com.autoescola.api.exception.type.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorConflitoHorarioInstrutor implements ValidadorAgendamento {
    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamento dados) {
        if (dados.idInstrutor() == null) return;
        boolean instrutorOcupado = repository.existsByInstrutorIdAndDataHoraAndSituacao(
                dados.idInstrutor(), dados.dataHora(), SituacaoInstrucao.AGENDADA);

        if (instrutorOcupado) {
            throw new ValidacaoException("Instrutor indisponível na data/hora escolhida!");
        }
    }
}

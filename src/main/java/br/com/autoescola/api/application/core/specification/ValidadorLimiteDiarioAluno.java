package br.com.autoescola.api.application.core.specification;

import br.com.autoescola.api.application.core.specification.contract.ValidadorAgendamento;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.autoescola.api.application.port.out.InstrucaoRepository;
import br.com.autoescola.api.application.core.domain.SituacaoInstrucao;
import br.com.autoescola.api.exception.type.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ValidadorLimiteDiarioAluno implements ValidadorAgendamento {
    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamento dados) {
        LocalDateTime inicioExpediente = dados.dataHora().withHour(6);
        LocalDateTime fimExpediente = dados.dataHora().withHour(21 - 1);

        long quantidadeAgendada = repository.countByAlunoIdAndDataHoraBetweenAndSituacao(
                dados.idAluno(),
                inicioExpediente,
                fimExpediente,
                SituacaoInstrucao.AGENDADA
        );
        if (quantidadeAgendada >= 2) {
            throw new ValidacaoException("O aluno não pode ter mais de duas instruções agendadas no mesmo dia.");
        }
    }
}

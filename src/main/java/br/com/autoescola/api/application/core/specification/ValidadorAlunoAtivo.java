package br.com.autoescola.api.application.core.specification;

import br.com.autoescola.api.application.core.specification.contract.ValidadorAgendamento;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.autoescola.api.exception.type.ValidacaoException;
import br.com.autoescola.api.application.port.out.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorAlunoAtivo implements ValidadorAgendamento {
    private final AlunoRepository alunoRepository;

    @Override
    public void validar(DadosAgendamento dados) {
        if (alunoRepository.existsByIdAndAtivoFalse(dados.idAluno())) {
            throw new ValidacaoException("Não pode agendar instrução para aluno inativo!");
        }
    }
}
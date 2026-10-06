package br.com.autoescola.api.application.service;

import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAtualizacaoInstrucao;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosCancelamentoInstrucao;
import br.com.autoescola.api.adapter.in.controller.response.instrucao.DetalhamentoAgendamento;
import br.com.autoescola.api.application.core.domain.Aluno;
import br.com.autoescola.api.application.core.domain.Instrucao;
import br.com.autoescola.api.application.core.domain.Instrutor;
import br.com.autoescola.api.application.core.domain.SituacaoInstrucao;
import br.com.autoescola.api.application.core.specification.contract.ValidadorAgendamento;
import br.com.autoescola.api.application.port.out.AlunoRepository;
import br.com.autoescola.api.application.port.out.InstrucaoRepository;
import br.com.autoescola.api.application.port.out.InstrutorRepository;
import br.com.autoescola.api.exception.type.AlunoNotFoundException;
import br.com.autoescola.api.exception.type.InstrucaoNotFoundException;
import br.com.autoescola.api.exception.type.InstrutorNotFoundException;
import br.com.autoescola.api.exception.type.ValidacaoException;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AgendaDeInstrucoes {
    private final InstrucaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final List<ValidadorAgendamento> validadoresAgendamento;

    @Value("${autoescola.cancelamento.antecedencia-horas:24}")
    private long antecedenciaCancelamentoHoras;

    @Transactional
    public DetalhamentoAgendamento agendar(DadosAgendamento dados) {
        Aluno aluno = alunoRepository.findById(dados.idAluno())
                .orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado."));
        if (!aluno.isAtivo()) throw new ValidacaoException("Não é possível agendar para um aluno inativo.");
        validarAgendamento(dados);

        Instrutor instrutor = escolherInstrutor(dados.especialidade(), dados.idInstrutor(), dados.dataHora(), null);
        Instrucao instrucao = repository.save(new Instrucao(aluno, instrutor, dados.dataHora()));
        return new DetalhamentoAgendamento(instrucao);
    }

    @Transactional(readOnly = true)
    public Page<DetalhamentoAgendamento> listar(Pageable pageable) {
        return repository.findAllBySituacao(SituacaoInstrucao.AGENDADA, pageable)
                .map(DetalhamentoAgendamento::new);
    }

    @Transactional(readOnly = true)
    public DetalhamentoAgendamento detalhar(Long id) {
        return new DetalhamentoAgendamento(buscar(id));
    }

    @Transactional
    public DetalhamentoAgendamento atualizar(Long id, DadosAtualizacaoInstrucao dados) {
        Instrucao instrucao = buscar(id);
        if (instrucao.getSituacao() != SituacaoInstrucao.AGENDADA) {
            throw new ValidacaoException("Não é possível alterar uma instrução cancelada.");
        }
        LocalDateTime dataHora = dados.dataHora();
        Especialidade especialidade = dados.especialidade() != null
                ? dados.especialidade()
                : instrucao.getInstrutor().getEspecialidade();
        validarHorario(dataHora);

        long quantidadeDeOutrasInstrucoes = repository.countByAlunoIdAndDataHoraBetweenAndIdNotAndSituacao(
                instrucao.getAluno().getId(), dataHora.toLocalDate().atStartOfDay(),
                dataHora.toLocalDate().atTime(23, 59, 59), id, SituacaoInstrucao.AGENDADA);
        if (quantidadeDeOutrasInstrucoes >= 2) {
            throw new ValidacaoException("O aluno não pode ter mais de duas instruções agendadas no mesmo dia.");
        }

        Long idInstrutor = dados.idInstrutor() == null && dados.especialidade() == null
                ? instrucao.getInstrutor().getId() : dados.idInstrutor();
        Instrutor instrutor = escolherInstrutor(especialidade, idInstrutor, dataHora, id);
        instrucao.reagendar(instrutor, dataHora);
        return new DetalhamentoAgendamento(repository.save(instrucao));
    }

    @Transactional
    public void cancelar(Long id, DadosCancelamentoInstrucao dados) {
        Instrucao instrucao = buscar(id);
        if (instrucao.getSituacao() == SituacaoInstrucao.CANCELADA) {
            throw new ValidacaoException("A instrução já está cancelada.");
        }
        LocalDateTime limite = LocalDateTime.now().plusHours(antecedenciaCancelamentoHoras);
        if (instrucao.getDataHora().isBefore(limite)) {
            throw new ValidacaoException("O cancelamento deve ser feito com pelo menos "
                    + antecedenciaCancelamentoHoras + " horas de antecedência.");
        }
        instrucao.cancelar(dados.motivo());
        repository.save(instrucao);
    }

    private void validarAgendamento(DadosAgendamento dados) {
        validarHorario(dados.dataHora());
        validadoresAgendamento.forEach(validador -> validador.validar(dados));
    }

    private void validarHorario(LocalDateTime dataHora) {
        if (dataHora == null) throw new ValidacaoException("Data e hora são obrigatórias.");
        long antecedenciaMinutos = Duration.between(LocalDateTime.now(), dataHora).toMinutes();
        if (antecedenciaMinutos < 30) throw new ValidacaoException("Agende com pelo menos 30 minutos de antecedência.");
        if (dataHora.getDayOfWeek().getValue() == 7 || dataHora.getHour() < 6 || dataHora.getHour() > 20) {
            throw new ValidacaoException("O funcionamento é de segunda a sábado, das 06:00 às 21:00; a última instrução começa às 20:00.");
        }
        if (dataHora.getMinute() != 0 || dataHora.getSecond() != 0 || dataHora.getNano() != 0) {
            throw new ValidacaoException("As instruções devem começar em uma hora cheia.");
        }
    }

    private Instrutor escolherInstrutor(Especialidade especialidade, Long idInstrutor,
                                        LocalDateTime dataHora, Long idIgnorado) {
        if (idInstrutor != null) {
            Instrutor instrutor = instrutorRepository.findById(idInstrutor)
                    .orElseThrow(() -> new InstrutorNotFoundException("Instrutor não encontrado."));
            if (!instrutor.isAtivo()) throw new ValidacaoException("Não é possível agendar com um instrutor inativo.");
            if (especialidade != null && instrutor.getEspecialidade() != especialidade) {
                throw new ValidacaoException("A especialidade informada não corresponde ao instrutor.");
            }
            boolean ocupado = idIgnorado == null
                    ? repository.existsByInstrutorIdAndDataHoraAndSituacao(idInstrutor, dataHora, SituacaoInstrucao.AGENDADA)
                    : repository.existsByInstrutorIdAndDataHoraAndIdNotAndSituacao(idInstrutor, dataHora, idIgnorado, SituacaoInstrucao.AGENDADA);
            if (ocupado) throw new ValidacaoException("Instrutor indisponível na data e hora escolhidas.");
            return instrutor;
        }

        if (especialidade == null) {
            throw new ValidacaoException("Informe o instrutor ou uma especialidade para selecionar automaticamente.");
        }
        List<Instrutor> disponiveis = instrutorRepository.findAllByAtivoTrueAndEspecialidade(especialidade).stream()
                .filter(instrutor -> idIgnorado == null
                        ? !repository.existsByInstrutorIdAndDataHoraAndSituacao(instrutor.getId(), dataHora, SituacaoInstrucao.AGENDADA)
                        : !repository.existsByInstrutorIdAndDataHoraAndIdNotAndSituacao(instrutor.getId(), dataHora, idIgnorado, SituacaoInstrucao.AGENDADA))
                .toList();
        if (disponiveis.isEmpty()) throw new ValidacaoException("Não há instrutor disponível para a data e hora escolhidas.");
        return disponiveis.get(ThreadLocalRandom.current().nextInt(disponiveis.size()));
    }

    private Instrucao buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new InstrucaoNotFoundException("Instrução não encontrada."));
    }
}

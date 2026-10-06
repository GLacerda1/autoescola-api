package br.com.autoescola.api;

import br.com.autoescola.api.application.core.domain.Instrucao;
import br.com.autoescola.api.application.core.domain.SituacaoInstrucao;
import br.com.autoescola.api.application.port.out.AlunoRepository;
import br.com.autoescola.api.application.port.out.InstrucaoRepository;
import br.com.autoescola.api.application.port.out.InstrutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class InstrucaoRepositoryTest {
    @Autowired
    InstrucaoRepository repository;
    @Autowired
    AlunoRepository alunoRepository;
    @Autowired
    InstrutorRepository instrutorRepository;

    @Test
    void persisteInstrucaoEConsultaSomenteAsAgendadas() {
        var aluno = alunoRepository.save(TestFixtures.aluno());
        var instrutor = instrutorRepository.save(TestFixtures.instrutor());
        var horario = LocalDateTime.of(2099, 1, 5, 10, 0);
        var instrucao = repository.save(new Instrucao(aluno, instrutor, horario));

        assertThat(repository.existsByInstrutorIdAndDataHoraAndSituacao(
                instrutor.getId(), horario, SituacaoInstrucao.AGENDADA)).isTrue();
        assertThat(repository.findAllBySituacao(SituacaoInstrucao.AGENDADA,
                org.springframework.data.domain.Pageable.unpaged()).getContent()).contains(instrucao);

        instrucao.cancelar(br.com.autoescola.api.shared.vo.enumeration.MotivoCancelamento.OUTROS);
        repository.save(instrucao);
        assertThat(repository.existsByInstrutorIdAndDataHoraAndSituacao(
                instrutor.getId(), horario, SituacaoInstrucao.AGENDADA)).isFalse();
    }
}

package br.com.autoescola.api.application.port.out;

import br.com.autoescola.api.application.core.domain.Instrucao;
import br.com.autoescola.api.application.core.domain.SituacaoInstrucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {
    boolean existsByInstrutorIdAndDataHoraAndSituacao(Long idInstrutor, LocalDateTime dataHora, SituacaoInstrucao situacao);
    boolean existsByInstrutorIdAndDataHoraAndIdNotAndSituacao(Long idInstrutor, LocalDateTime dataHora, Long id, SituacaoInstrucao situacao);
    long countByAlunoIdAndDataHoraBetweenAndSituacao(Long idAluno, LocalDateTime inicio, LocalDateTime fim, SituacaoInstrucao situacao);
    long countByAlunoIdAndDataHoraBetweenAndIdNotAndSituacao(Long idAluno, LocalDateTime inicio, LocalDateTime fim, Long id, SituacaoInstrucao situacao);
    Page<Instrucao> findAllBySituacao(SituacaoInstrucao situacao, Pageable pageable);
}

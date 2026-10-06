package br.com.autoescola.api.application.port.out;

import br.com.autoescola.api.application.core.domain.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Page<Aluno> findAllByAtivoTrue(Pageable pageable);
    boolean existsByIdAndAtivoFalse(Long id);
}

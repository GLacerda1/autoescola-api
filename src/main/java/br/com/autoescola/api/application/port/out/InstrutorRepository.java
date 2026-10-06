package br.com.autoescola.api.application.port.out;

import br.com.autoescola.api.application.core.domain.Instrutor;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    Page<Instrutor> findAllByAtivoTrue(Pageable pageable);
    List<Instrutor> findAllByAtivoTrueAndEspecialidade(Especialidade especialidade);
    boolean existsByIdAndAtivoFalse(Long id);
}

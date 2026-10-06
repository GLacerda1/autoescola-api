package br.com.autoescola.api;

import br.com.autoescola.api.application.port.out.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AlunoRepositoryTest {
    @Autowired
    AlunoRepository repository;

    @Test
    void persisteEListaAlunoAtivo() {
        var salvo = repository.save(TestFixtures.aluno());

        assertThat(repository.findById(salvo.getId())).isPresent();
        assertThat(repository.findAllByAtivoTrue(org.springframework.data.domain.Pageable.unpaged()))
                .extracting("email").containsExactly("ana@example.com");
    }
}

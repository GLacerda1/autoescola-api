package br.com.autoescola.api;

import br.com.autoescola.api.application.port.out.InstrutorRepository;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class InstrutorRepositoryTest {
    @Autowired
    InstrutorRepository repository;

    @Test
    void persisteEFiltraInstrutoresPorAtividadeEEspecialidade() {
        var salvo = repository.save(TestFixtures.instrutor());

        assertThat(repository.findById(salvo.getId())).isPresent();
        assertThat(repository.findAllByAtivoTrueAndEspecialidade(Especialidade.CARROS))
                .extracting("email").containsExactly("carlos@example.com");
    }
}

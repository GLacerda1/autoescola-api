package br.com.autoescola.api;

import br.com.autoescola.api.application.core.domain.Usuario;
import br.com.autoescola.api.application.port.out.UsuarioRepository;
import br.com.autoescola.api.shared.vo.enumeration.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UsuarioRepositoryTest {
    @Autowired
    UsuarioRepository repository;

    @Test
    void persisteUsuarioEPermiteLocalizarPorLogin() {
        repository.save(new Usuario("teste", "$2a$12$hash", Role.USER));

        assertThat(repository.findByLogin("teste")).isPresent();
        assertThat(repository.existsByLogin("teste")).isTrue();
    }
}

package br.com.autoescola.api;

import br.com.autoescola.api.application.port.out.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AutoEscolaApplicationTests {
    private static final String TEST_ADMIN_LOGIN = "admin-testes";
    private static final String TEST_ADMIN_PASSWORD = UUID.randomUUID() + UUID.randomUUID().toString().replace("-", "");
    private static final String TEST_JWT_SECRET = UUID.randomUUID().toString() + UUID.randomUUID();

    @Autowired
    UsuarioRepository usuarios;
    @Autowired
    PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void configurarCredenciaisDeTeste(DynamicPropertyRegistry registry) {
        registry.add("api.security.token.secret", () -> TEST_JWT_SECRET);
        registry.add("app.bootstrap-admin.login", () -> TEST_ADMIN_LOGIN);
        registry.add("app.bootstrap-admin.password", () -> TEST_ADMIN_PASSWORD);
    }

    @Test
    void iniciaAplicacaoECriaAdministradorComSenhaCodificada() {
        var administrador = usuarios.findByLogin(TEST_ADMIN_LOGIN).orElseThrow();

        assertThat(administrador.getSenha()).isNotEqualTo(TEST_ADMIN_PASSWORD);
        assertThat(passwordEncoder.matches(TEST_ADMIN_PASSWORD, administrador.getSenha())).isTrue();
    }
}

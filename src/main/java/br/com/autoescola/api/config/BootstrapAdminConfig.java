package br.com.autoescola.api.config;

import br.com.autoescola.api.application.core.domain.Usuario;
import br.com.autoescola.api.application.port.out.UsuarioRepository;
import br.com.autoescola.api.shared.vo.enumeration.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

@Configuration
public class BootstrapAdminConfig {
    @Bean
    ApplicationRunner createInitialAdmin(UsuarioRepository repository, PasswordEncoder passwordEncoder,
                                         @Value("${app.bootstrap-admin.login:}") String login,
                                         @Value("${app.bootstrap-admin.password:}") String senha) {
        return args -> {
            if (repository.existsByPerfil(Role.ADMIN)) {
                return;
            }

            if (login.isBlank() || senha.isBlank()) {
                throw new IllegalStateException(
                        "Nenhum administrador existe. Configure AUTOESCOLA_ADMIN_LOGIN e AUTOESCOLA_ADMIN_PASSWORD para a primeira inicialização.");
            }
            int tamanhoSenha = senha.getBytes(StandardCharsets.UTF_8).length;
            if (tamanhoSenha < 16 || tamanhoSenha > 72) {
                throw new IllegalStateException(
                        "AUTOESCOLA_ADMIN_PASSWORD deve ter entre 16 e 72 bytes UTF-8.");
            }
            if (repository.existsByLogin(login)) {
                throw new IllegalStateException(
                        "AUTOESCOLA_ADMIN_LOGIN já está em uso por uma conta sem perfil de administrador.");
            }

            repository.save(new Usuario(login, passwordEncoder.encode(senha), Role.ADMIN));
        };
    }
}

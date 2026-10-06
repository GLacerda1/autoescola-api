package br.com.autoescola.api.application.port.out;

import br.com.autoescola.api.application.core.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import br.com.autoescola.api.shared.vo.enumeration.Role;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByPerfil(Role perfil);
}

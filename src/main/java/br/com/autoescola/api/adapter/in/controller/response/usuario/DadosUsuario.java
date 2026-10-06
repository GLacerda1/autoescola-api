package br.com.autoescola.api.adapter.in.controller.response.usuario;

import br.com.autoescola.api.application.core.domain.Usuario;
import br.com.autoescola.api.shared.vo.enumeration.Role;

public record DadosUsuario(Long id, String login, Role perfil) {
    public DadosUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getPerfil());
    }
}

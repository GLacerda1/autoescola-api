package br.com.autoescola.api.adapter.in.controller.request.usuario;

import br.com.autoescola.api.shared.vo.enumeration.Role;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoUsuario(
        @Size(max = 100) String login,
        Role perfil) {
}

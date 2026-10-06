package br.com.autoescola.api.adapter.in.controller.request.usuario;

import br.com.autoescola.api.shared.vo.enumeration.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank @Size(max = 100) String login,
        @NotBlank @Size(min = 12, max = 72) String senha,
        Role perfil) {
}

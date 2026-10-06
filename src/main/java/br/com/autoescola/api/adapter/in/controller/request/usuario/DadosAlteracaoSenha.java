package br.com.autoescola.api.adapter.in.controller.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosAlteracaoSenha(
        @NotBlank String senhaAtual,
        @NotBlank @Size(min = 12, max = 72) String novaSenha) {
}

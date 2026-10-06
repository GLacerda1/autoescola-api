package br.com.autoescola.api.adapter.in.controller;

import br.com.autoescola.api.adapter.in.controller.request.usuario.DadosAlteracaoSenha;
import br.com.autoescola.api.adapter.in.controller.request.usuario.DadosAtualizacaoUsuario;
import br.com.autoescola.api.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.autoescola.api.adapter.in.controller.response.usuario.DadosUsuario;
import br.com.autoescola.api.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        return ResponseEntity.status(201).body(service.cadastrar(dados));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DadosUsuario>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "login") Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosUsuario> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosUsuario> atualizar(@PathVariable Long id,
                                                   @RequestBody @Valid DadosAtualizacaoUsuario dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/minha-senha")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> alterarMinhaSenha(@RequestBody @Valid DadosAlteracaoSenha dados,
                                                   org.springframework.security.core.Authentication autenticacao) {
        service.alterarMinhaSenha(autenticacao.getName(), dados);
        return ResponseEntity.noContent().build();
    }
}

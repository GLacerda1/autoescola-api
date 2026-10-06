package br.com.autoescola.api.adapter.in.controller;

import br.com.autoescola.api.adapter.out.integration.viacep.ViaCepService;
import br.com.autoescola.api.adapter.out.integration.viacep.dto.DadosEnderecoViaCep;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integracoes/cep")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class IntegracaoCepController {
    private final ViaCepService viaCepService;

    @GetMapping("/{cep}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Consulta um CEP na API externa ViaCEP")
    public ResponseEntity<DadosEnderecoViaCep> consultar(@PathVariable String cep) {
        return ResponseEntity.ok(viaCepService.consultar(cep));
    }
}

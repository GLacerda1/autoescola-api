package br.com.autoescola.api.adapter.in.controller;

import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosAtualizacaoInstrucao;
import br.com.autoescola.api.adapter.in.controller.request.instrucao.DadosCancelamentoInstrucao;
import br.com.autoescola.api.adapter.in.controller.response.instrucao.DetalhamentoAgendamento;
import br.com.autoescola.api.application.service.AgendaDeInstrucoes;
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
@RequestMapping("/instrucoes")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class InstrucaoController {
    private final AgendaDeInstrucoes agenda;

    @PostMapping
    public ResponseEntity<DetalhamentoAgendamento> agendarInstrucao(@RequestBody @Valid DadosAgendamento dados) {
        return ResponseEntity.status(201).body(agenda.agendar(dados));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<DetalhamentoAgendamento>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return ResponseEntity.ok(agenda.listar(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DetalhamentoAgendamento> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(agenda.detalhar(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DetalhamentoAgendamento> atualizar(@PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoInstrucao dados) {
        return ResponseEntity.ok(agenda.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> cancelar(@PathVariable Long id,
            @RequestBody @Valid DadosCancelamentoInstrucao dados) {
        agenda.cancelar(id, dados);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancelamento")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> cancelarPelaAcao(@PathVariable Long id,
            @RequestBody @Valid DadosCancelamentoInstrucao dados) {
        agenda.cancelar(id, dados);
        return ResponseEntity.noContent().build();
    }
}

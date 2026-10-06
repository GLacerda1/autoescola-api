package br.com.autoescola.api.application.service;

import br.com.autoescola.api.adapter.in.controller.request.instrutor.DadosAtualizacaoInstrutor;
import br.com.autoescola.api.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.autoescola.api.adapter.in.controller.response.instrutor.DadosDetalhamentoInstrutor;
import br.com.autoescola.api.adapter.in.controller.response.instrutor.DadosListagemInstrutor;
import br.com.autoescola.api.application.core.domain.Instrutor;
import br.com.autoescola.api.application.port.out.InstrutorRepository;
import br.com.autoescola.api.exception.type.InstrutorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstrutorService {
    private final InstrutorRepository repository;

    @Transactional
    public DadosDetalhamentoInstrutor cadastrarInstrutor(DadosCadastroInstrutor dados) {
        Instrutor instrutor = new Instrutor(dados);
        Instrutor saved = repository.save(instrutor);
        return new DadosDetalhamentoInstrutor(saved);
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemInstrutor> listarInstrutores(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(DadosListagemInstrutor::new);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoInstrutor detalharInstrutor(Long id) {
        Instrutor instrutor = repository
                .findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não existe!"));
        return new DadosDetalhamentoInstrutor(instrutor);
    }

    @Transactional
    public DadosDetalhamentoInstrutor atualizarInstrutor(DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = repository
                .findById(dados.id())
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não existe!"));
        instrutor.atualizarInformacoes(dados);
        Instrutor saved = repository.save(instrutor);
        return new DadosDetalhamentoInstrutor(saved);
    }

    @Transactional
    public void excluirInstrutor(Long id) {
        Instrutor instrutor = repository
                .findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não existe!"));
        instrutor.excluir();
        repository.save(instrutor);
    }
}

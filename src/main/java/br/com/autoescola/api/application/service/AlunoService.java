package br.com.autoescola.api.application.service;

import br.com.autoescola.api.adapter.in.controller.request.aluno.DadosAtualizacaoAluno;
import br.com.autoescola.api.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.autoescola.api.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.autoescola.api.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.autoescola.api.application.core.domain.Aluno;
import br.com.autoescola.api.application.port.out.AlunoRepository;
import br.com.autoescola.api.exception.type.AlunoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;

    @Transactional
    public DadosDetalhamentoAluno cadastrar(DadosCadastroAluno dados) {
        return new DadosDetalhamentoAluno(repository.save(new Aluno(dados)));
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemAluno> listar(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable).map(DadosListagemAluno::new);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoAluno detalhar(Long id) {
        return new DadosDetalhamentoAluno(buscar(id));
    }

    @Transactional
    public DadosDetalhamentoAluno atualizar(DadosAtualizacaoAluno dados) {
        Aluno aluno = buscar(dados.id());
        aluno.atualizarInformacoes(dados);
        return new DadosDetalhamentoAluno(repository.save(aluno));
    }

    @Transactional
    public void excluir(Long id) {
        Aluno aluno = buscar(id);
        aluno.excluir();
        repository.save(aluno);
    }

    private Aluno buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado."));
    }
}

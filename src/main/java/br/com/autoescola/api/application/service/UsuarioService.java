package br.com.autoescola.api.application.service;

import br.com.autoescola.api.adapter.in.controller.request.usuario.DadosAlteracaoSenha;
import br.com.autoescola.api.adapter.in.controller.request.usuario.DadosAtualizacaoUsuario;
import br.com.autoescola.api.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.autoescola.api.adapter.in.controller.response.usuario.DadosUsuario;
import br.com.autoescola.api.application.core.domain.Usuario;
import br.com.autoescola.api.application.port.out.UsuarioRepository;
import br.com.autoescola.api.exception.type.ConflitoException;
import br.com.autoescola.api.exception.type.UsuarioNotFoundException;
import br.com.autoescola.api.exception.type.ValidacaoException;
import br.com.autoescola.api.shared.vo.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DadosUsuario cadastrar(DadosCadastroUsuario dados) {
        if (repository.existsByLogin(dados.login())) throw new ConflitoException("Já existe um usuário com esse login.");
        Role perfil = dados.perfil() == null ? Role.USER : dados.perfil();
        Usuario usuario = new Usuario(dados.login(), codificarSenha(dados.senha()), perfil);
        return new DadosUsuario(repository.save(usuario));
    }

    @Transactional(readOnly = true)
    public Page<DadosUsuario> listar(Pageable pageable) {
        return repository.findAll(pageable).map(DadosUsuario::new);
    }

    @Transactional(readOnly = true)
    public DadosUsuario detalhar(Long id) {
        return new DadosUsuario(buscar(id));
    }

    @Transactional
    public DadosUsuario atualizar(Long id, DadosAtualizacaoUsuario dados) {
        Usuario usuario = buscar(id);
        if (dados.login() != null && !dados.login().equals(usuario.getLogin()) && repository.existsByLogin(dados.login())) {
            throw new ConflitoException("Já existe um usuário com esse login.");
        }
        usuario.atualizarPerfil(dados.login(), dados.perfil());
        return new DadosUsuario(repository.save(usuario));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscar(id));
    }

    @Transactional
    public void alterarMinhaSenha(String login, DadosAlteracaoSenha dados) {
        Usuario usuario = repository.findByLogin(login)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));
        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new ValidacaoException("A senha atual está incorreta.");
        }
        usuario.alterarSenha(codificarSenha(dados.novaSenha()));
        repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));
    }

    private Usuario buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));
    }

    private String codificarSenha(String senha) {
        if (senha.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ValidacaoException("A senha não pode exceder 72 bytes em UTF-8.");
        }
        return passwordEncoder.encode(senha);
    }
}

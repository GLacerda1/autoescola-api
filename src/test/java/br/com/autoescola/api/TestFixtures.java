package br.com.autoescola.api;

import br.com.autoescola.api.application.core.domain.Aluno;
import br.com.autoescola.api.application.core.domain.Instrutor;
import br.com.autoescola.api.shared.vo.Endereco;
import br.com.autoescola.api.shared.vo.enumeration.Especialidade;

final class TestFixtures {
    private TestFixtures() {
    }

    static Endereco endereco() {
        return new Endereco("Rua Teste", "10", "", "Centro", "São Paulo", "SP", "01001-000");
    }

    static Aluno aluno() {
        return new Aluno(null, "Ana Teste", "ana@example.com", "11999990000", "12345678901", endereco(), true);
    }

    static Instrutor instrutor() {
        return new Instrutor(null, "Carlos Teste", "carlos@example.com", "11999991111",
                "01234567890", Especialidade.CARROS, endereco(), true);
    }
}

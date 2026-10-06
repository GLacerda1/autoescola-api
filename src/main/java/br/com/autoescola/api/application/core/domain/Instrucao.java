package br.com.autoescola.api.application.core.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import br.com.autoescola.api.shared.vo.enumeration.MotivoCancelamento;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity(name = "Instrucao")
@Table(name = "instrucoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Instrucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private SituacaoInstrucao situacao = SituacaoInstrucao.AGENDADA;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "motivo_cancelamento", length = 30)
    private MotivoCancelamento motivoCancelamento;

    public Instrucao(Aluno aluno, Instrutor instrutor, LocalDateTime dataHora) {
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.dataHora = dataHora;
        this.situacao = SituacaoInstrucao.AGENDADA;
    }

    public void reagendar(Instrutor instrutor, LocalDateTime dataHora) {
        this.instrutor = instrutor;
        this.dataHora = dataHora;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.situacao = SituacaoInstrucao.CANCELADA;
        this.motivoCancelamento = motivo;
    }
}

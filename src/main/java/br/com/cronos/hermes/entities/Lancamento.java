package br.com.cronos.hermes.entities;

import br.com.cronos.hermes.dto.LancamentoDto;
import br.com.cronos.hermes.entities.enums.StatusLancamento;
import br.com.cronos.hermes.entities.enums.TipoLancamento;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "lancamentos")
public class Lancamento {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "ano")
    private Integer ano;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusLancamento status;

    public LancamentoDto toDto() {
        return LancamentoDto.builder()
            .id(this.id)
            .descricao(descricao)
            .mes(this.mes)
            .ano(this.ano)
            .usuario(this.usuario)
            .valor(this.valor)
            .tipo(tipo)
            .status(status)
            .build();
    }
}
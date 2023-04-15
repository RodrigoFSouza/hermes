package br.com.cronos.hermes.entities;

import br.com.cronos.hermes.dto.LancamentoDto;
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
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "ano")
    private Integer ano;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    public LancamentoDto toDto() {
        return LancamentoDto.builder()
            .id(this.id)
                .mes(this.mes)
                .ano(this.ano)
                .usuario(this.usuario)
                .valor(this.valor)
                .dataCadastro(this.dataCadastro)
            .build();
    }
}
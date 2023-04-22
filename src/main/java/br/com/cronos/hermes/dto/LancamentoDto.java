package br.com.cronos.hermes.dto;

import br.com.cronos.hermes.entities.Lancamento;
import br.com.cronos.hermes.entities.Usuario;
import br.com.cronos.hermes.entities.enums.StatusLancamento;
import br.com.cronos.hermes.entities.enums.TipoLancamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LancamentoDto {
    @EqualsAndHashCode.Include
    @Schema(description = "Código de identificação do Lancamento")
    private UUID id;
    private String descricao;
    @Schema
    private Integer mes;
    @Schema
    private Integer ano;
    @Schema
    @NotNull(message = "Deve informar um usuário")
    private Usuario usuario;
    @Schema
    private BigDecimal valor;
    private TipoLancamento tipo;
    private StatusLancamento status;

    public Lancamento toEntity() {
        return Lancamento.builder()
            .id(this.id)
            .descricao(descricao)
            .mes(this.mes)
            .ano(this.ano)
            .usuario(this.usuario)
            .valor(this.valor)
            .status(status)
            .tipo(tipo)
            .build();
    }
}
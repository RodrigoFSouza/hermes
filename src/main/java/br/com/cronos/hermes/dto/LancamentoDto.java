package br.com.cronos.hermes.dto;

import br.com.cronos.hermes.entities.Lancamento;
import br.com.cronos.hermes.entities.Usuario;
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
    @Schema
    private Integer mes;
    @Schema
    private Integer ano;
    @Schema
    @NotNull(message = "Deve informar um usuário")
    private Usuario usuario;
    @Schema
    private BigDecimal valor;

    public Lancamento toEntity() {
        return Lancamento.builder()
            .id(this.id)
            .mes(this.mes)
            .ano(this.ano)
            .usuario(this.usuario)
            .valor(this.valor)
            .build();
    }
}
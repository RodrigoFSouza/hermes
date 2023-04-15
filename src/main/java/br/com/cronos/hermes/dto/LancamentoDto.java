package br.com.cronos.hermes.dto;

import br.com.cronos.hermes.entities.Lancamento;
import br.com.cronos.hermes.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LancamentoDto {
    @EqualsAndHashCode.Include
    @Schema(description = "Código de identificação do Lancamento")
    private final UUID id;
    @Schema
    private Integer mes;
    @Schema
    private Integer ano;
    @Schema
    private Usuario usuario;
    @Schema
    private BigDecimal valor;
    @Schema
    private LocalDateTime dataCadastro;

    public Lancamento toEntity() {
        return Lancamento.builder()
            .id(this.id)
                .mes(this.mes)
                .ano(this.ano)
                .usuario(this.usuario)
                .valor(this.valor)
                .dataCadastro(this.dataCadastro)
            .build();
    }
}
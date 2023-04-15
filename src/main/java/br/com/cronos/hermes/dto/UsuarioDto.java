package br.com.cronos.hermes.dto;

import br.com.cronos.hermes.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioDto {


    @EqualsAndHashCode.Include
    @Schema(description = "Código de identificação do Usuario")
    private final UUID id;
    @Schema
    private String nome;
    @Schema
    private String email;
    @Schema
    private String senha;

    public Usuario toEntity() {
        return Usuario.builder()
                .id(this.id)
                .nome(this.nome)
                .email(this.email)
                .senha(this.senha)
                .build();
    }
}
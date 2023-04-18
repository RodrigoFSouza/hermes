package br.com.cronos.hermes.dto;

import br.com.cronos.hermes.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioDto {


    @EqualsAndHashCode.Include
    @Schema(description = "Código de identificação do Usuario")
    private UUID id;
    @Schema
    @NotEmpty(message = "Deve preencher o nome do usuário")
    private String nome;
    @Schema
    @NotEmpty(message = "Deve preencher o email do usuário")
    private String email;
    @Schema
    @NotEmpty(message = "Deve preencher a senha do usuário")
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
package br.com.cronos.hermes.entities;

import br.com.cronos.hermes.dto.UsuarioDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "usuarios")
public class Usuario {


    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "email")
    private String email;
    @Column(name = "senha")
    private String senha;


    public UsuarioDto toDto() {
        return UsuarioDto.builder()
            .id(this.id)
                .nome(this.nome)
                .email(this.email)
                .senha(this.senha)
            .build();
    }
}
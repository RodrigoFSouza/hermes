package br.com.cronos.hermes.utils;

import br.com.cronos.hermes.dto.UsuarioDto;
import br.com.cronos.hermes.entities.Usuario;

import java.util.UUID;

public final class UsuarioData {

    private UsuarioData() {}

    public static UsuarioDto.UsuarioDtoBuilder newDtoBuilder() {
        return UsuarioDto.builder()
                    .nome(UsuarioConstants.NOME)
                    .email(UsuarioConstants.EMAIL)
                    .senha(UsuarioConstants.SENHA)
;
    }
    
    public static UsuarioDto.UsuarioDtoBuilder updateDtoBuilder() {
        return UsuarioDto.builder()
                .id(UUID.randomUUID())
                    .nome(UsuarioConstants.NOME_UPDATE)
                    .email(UsuarioConstants.EMAIL_UPDATE)
                    .senha(UsuarioConstants.SENHA_UPDATE)
;
    }
    
    public static Usuario.UsuarioBuilder newBuilder() {
        return Usuario.builder()
                    .nome(UsuarioConstants.NOME)
                    .email(UsuarioConstants.EMAIL)
                    .senha(UsuarioConstants.SENHA)
;
    }
}
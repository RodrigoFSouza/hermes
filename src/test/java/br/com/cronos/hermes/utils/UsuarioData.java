package br.com.cronos.hermes.utils;

import br.com.cronos.hermes.entities.Usuario;

import java.util.UUID;

public final class UsuarioData {

    private UsuarioData() {}

    public static Usuario.UsuarioBuilder newUsuarioBuilder() {
        return Usuario.builder()
                .id(UUID.fromString("4fe42adf-4228-45de-adb2-6b81dc623afd"))
                .nome("joao")
                .email("joao@cronos.com.br")
                .senha("teste123");
    }
}
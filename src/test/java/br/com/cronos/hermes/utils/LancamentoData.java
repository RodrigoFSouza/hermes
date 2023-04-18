package br.com.cronos.hermes.utils;

import br.com.cronos.hermes.dto.LancamentoDto;
import br.com.cronos.hermes.entities.Lancamento;

import java.util.UUID;

public final class LancamentoData {

    private LancamentoData() {}

    public static LancamentoDto.LancamentoDtoBuilder newLancamentoDtoBuilder() {
        return LancamentoDto.builder()
                    .mes(LancamentoConstants.MES)
                    .ano(LancamentoConstants.ANO)
                    .usuario(UsuarioData.newBuilder().build())
                    .valor(LancamentoConstants.VALOR);
    }
    
    public static LancamentoDto.LancamentoDtoBuilder updateLancamentoDtoBuilder() {
        return LancamentoDto.builder()
                .id(UUID.randomUUID())
                    .mes(LancamentoConstants.MES_UPDATE)
                    .ano(LancamentoConstants.ANO_UPDATE)
                    .usuario(UsuarioData.newBuilder().build())
                    .valor(LancamentoConstants.VALOR_UPDATE);
    }
    
    public static Lancamento.LancamentoBuilder newLancamentoBuilder() {
        return Lancamento.builder()
                    .mes(LancamentoConstants.MES)
                    .ano(LancamentoConstants.ANO)
                    .usuario(UsuarioData.newBuilder().build())
                    .valor(LancamentoConstants.VALOR);
    }
}
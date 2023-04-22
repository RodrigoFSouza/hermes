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
                    .descricao(LancamentoConstants.DESCRICAO)
                    .tipo(LancamentoConstants.TIPO)
                    .status(LancamentoConstants.STATUS)
                    .valor(LancamentoConstants.VALOR);
    }
    
    public static LancamentoDto.LancamentoDtoBuilder updateLancamentoDtoBuilder() {
        return LancamentoDto.builder()
                .id(UUID.randomUUID())
                .descricao(LancamentoConstants.DESCRICAO_UPDATE)
                .mes(LancamentoConstants.MES_UPDATE)
                .ano(LancamentoConstants.ANO_UPDATE)
                .usuario(UsuarioData.newBuilder().build())
                .tipo(LancamentoConstants.TIPO_UPDATE)
                .status(LancamentoConstants.STATUS_UPDATE)
                .valor(LancamentoConstants.VALOR_UPDATE);
    }
    
    public static Lancamento.LancamentoBuilder newLancamentoBuilder() {
        return Lancamento.builder()
                    .mes(LancamentoConstants.MES)
                    .ano(LancamentoConstants.ANO)
                    .descricao(LancamentoConstants.DESCRICAO)
                    .tipo(LancamentoConstants.TIPO)
                    .status(LancamentoConstants.STATUS)
                    .usuario(UsuarioData.newBuilder().build())
                    .valor(LancamentoConstants.VALOR);
    }
}
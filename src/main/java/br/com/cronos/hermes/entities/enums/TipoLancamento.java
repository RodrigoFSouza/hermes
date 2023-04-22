package br.com.cronos.hermes.entities.enums;

public enum TipoLancamento {
    RECEITA("RECEITA"),
    DESPESA("DESPESA");

    private final String tipoText;

    TipoLancamento(String tipoText) {
        this.tipoText = tipoText;
    }

    public String getTipoText() {
        return tipoText;
    }
}

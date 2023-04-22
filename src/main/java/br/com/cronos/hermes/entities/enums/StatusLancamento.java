package br.com.cronos.hermes.entities.enums;

public enum StatusLancamento {
    PENDENTE("PENDENTE"),
    CANCELADO("CANCELADO"),
    EFETIVADO("EFETIVADO");

    private final String statusText;

    StatusLancamento(String statusText) {
        this.statusText = statusText;
    }
    public String getStatusText() {
        return statusText;
    }
}

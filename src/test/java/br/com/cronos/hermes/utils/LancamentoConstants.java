package br.com.cronos.hermes.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class LancamentoConstants {
    public static final String BASE_URL = "/api/v1/lancamentos";
    public static final UUID ID_QUE_NAO_EXISTE = UUID.fromString("85283f23-3f32-4cbe-93d2-e541f189f10b");

    public static final Integer MES = 53;
    public static final Integer ANO = 19;
    public static final BigDecimal VALOR = BigDecimal.valueOf(22.48);
    public static final LocalDateTime DATA_CADASTRO = LocalDateTime.of(2022, 07, 03, 22, 42, 48);
    public static final Integer MES_UPDATE = 71;
    public static final Integer ANO_UPDATE = 8;
    public static final BigDecimal VALOR_UPDATE = BigDecimal.valueOf(25.29);
    public static final LocalDateTime DATA_CADASTRO_UPDATE = LocalDateTime.of(2022, 12, 03, 22, 44, 48);


    private LancamentoConstants() {}
}

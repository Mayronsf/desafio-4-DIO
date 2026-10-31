package br.com.dio.padroes.frete;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Região de destino a partir da UF. O centro de distribuição fica em SP,
 * então quanto mais longe do Sudeste, maior o fator de custo e o prazo.
 */
public enum Regiao {

    SUDESTE("1.0", 0, "SP", "RJ", "MG", "ES"),
    SUL("1.2", 1, "PR", "SC", "RS"),
    CENTRO_OESTE("1.4", 2, "DF", "GO", "MT", "MS"),
    NORDESTE("1.6", 3, "BA", "SE", "AL", "PE", "PB", "RN", "CE", "PI", "MA"),
    NORTE("1.9", 4, "AM", "PA", "AC", "RO", "RR", "AP", "TO");

    private final BigDecimal fatorCusto;
    private final int diasExtras;
    private final Set<String> ufs;

    Regiao(String fatorCusto, int diasExtras, String... ufs) {
        this.fatorCusto = new BigDecimal(fatorCusto);
        this.diasExtras = diasExtras;
        this.ufs = Set.of(ufs);
    }

    public static Regiao daUf(String uf) {
        for (Regiao regiao : values()) {
            if (regiao.ufs.contains(uf.toUpperCase())) {
                return regiao;
            }
        }
        throw new IllegalArgumentException("UF desconhecida: " + uf);
    }

    public BigDecimal fatorCusto() {
        return fatorCusto;
    }

    public int diasExtras() {
        return diasExtras;
    }
}

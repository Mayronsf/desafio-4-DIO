package br.com.dio.padroes.desconto;

import br.com.dio.padroes.exception.RegraNegocioException;
import br.com.dio.padroes.model.DescontoAplicado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CadeiaDescontosTest {

    private final CadeiaDescontos cadeia = new CadeiaDescontos(
            List.of(new DescontoPrimeiraCompra(), new DescontoProgressivo(), new DescontoCupom()));

    private ContextoDesconto aplicar(String produtos, String frete, String cupom, boolean primeiraCompra) {
        return cadeia.aplicar(new ContextoDesconto(new BigDecimal(produtos), new BigDecimal(frete), cupom, primeiraCompra));
    }

    @Test
    void semNenhumaRegraAplicavelNaoHaDesconto() {
        ContextoDesconto contexto = aplicar("100.00", "20.00", null, false);

        assertThat(contexto.getDescontos()).isEmpty();
        assertThat(contexto.total()).isEqualByComparingTo("0");
    }

    @Test
    void todasAsRegrasAplicaveisSeAcumulamNaOrdemDaCorrente() {
        ContextoDesconto contexto = aplicar("600.00", "30.00", " dio10 ", true);

        assertThat(contexto.getDescontos()).extracting(DescontoAplicado::valor)
                .containsExactly(new BigDecimal("60.00"), new BigDecimal("30.00"), new BigDecimal("60.00"));
        assertThat(contexto.total()).isEqualByComparingTo("150.00");
    }

    @Test
    void cupomFreteGratisZeraOFrete() {
        ContextoDesconto contexto = aplicar("100.00", "26.50", "FRETEGRATIS", false);

        assertThat(contexto.total()).isEqualByComparingTo("26.50");
    }

    @Test
    void cupomInvalidoInterrompeACorrente() {
        assertThatThrownBy(() -> aplicar("100.00", "10.00", "NAOEXISTE", false))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("NAOEXISTE");
    }
}

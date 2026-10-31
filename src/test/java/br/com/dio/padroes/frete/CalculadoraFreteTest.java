package br.com.dio.padroes.frete;

import br.com.dio.padroes.model.Endereco;
import br.com.dio.padroes.model.ModalidadeFrete;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalculadoraFreteTest {

    private final CalculadoraFrete calculadora =
            new CalculadoraFrete(List.of(new FretePac(), new FreteSedex(), new FreteRetirada()));

    private static Endereco em(String uf) {
        return new Endereco("00000000", "Rua", "Bairro", "Cidade", uf);
    }

    @Test
    void pacParaNordesteAplicaFatorDaRegiao() {
        CotacaoFrete cotacao = calculadora.cotar(ModalidadeFrete.PAC, em("BA"), new BigDecimal("2"));

        assertThat(cotacao.valor()).isEqualByComparingTo("24.00");
        assertThat(cotacao.prazoDias()).isEqualTo(11);
    }

    @Test
    void sedexParaNorteEhMaisCaroEMaisRapidoQuePac() {
        CotacaoFrete sedex = calculadora.cotar(ModalidadeFrete.SEDEX, em("am"), BigDecimal.ONE);
        CotacaoFrete pac = calculadora.cotar(ModalidadeFrete.PAC, em("am"), BigDecimal.ONE);

        assertThat(sedex.valor()).isEqualByComparingTo("41.80");
        assertThat(sedex.prazoDias()).isLessThan(pac.prazoDias());
    }

    @Test
    void simulacaoTrazTodasAsModalidadesDaMaisBarataParaAMaisCara() {
        List<CotacaoFrete> cotacoes = calculadora.simular(em("SP"), new BigDecimal("3"));

        assertThat(cotacoes).extracting(CotacaoFrete::modalidade)
                .containsExactly(ModalidadeFrete.RETIRADA, ModalidadeFrete.PAC, ModalidadeFrete.SEDEX);
    }
}

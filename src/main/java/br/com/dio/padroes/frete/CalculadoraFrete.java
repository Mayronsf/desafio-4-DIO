package br.com.dio.padroes.frete;

import br.com.dio.padroes.model.Endereco;
import br.com.dio.padroes.model.ModalidadeFrete;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Contexto do Strategy. O Spring injeta todas as implementações de FreteStrategy
 * e a calculadora escolhe a certa pela modalidade, sem nenhum switch.
 */
@Component
public class CalculadoraFrete {

    private final Map<ModalidadeFrete, FreteStrategy> estrategias = new EnumMap<>(ModalidadeFrete.class);

    public CalculadoraFrete(List<FreteStrategy> estrategias) {
        estrategias.forEach(e -> this.estrategias.put(e.modalidade(), e));
    }

    public CotacaoFrete cotar(ModalidadeFrete modalidade, Endereco destino, BigDecimal pesoKg) {
        FreteStrategy estrategia = estrategias.get(modalidade);
        if (estrategia == null) {
            throw new IllegalArgumentException("Modalidade sem implementação: " + modalidade);
        }
        return estrategia.cotar(Regiao.daUf(destino.getUf()), pesoKg);
    }

    public List<CotacaoFrete> simular(Endereco destino, BigDecimal pesoKg) {
        Regiao regiao = Regiao.daUf(destino.getUf());
        return estrategias.values().stream()
                .map(e -> e.cotar(regiao, pesoKg))
                .sorted(Comparator.comparing(CotacaoFrete::valor))
                .toList();
    }
}

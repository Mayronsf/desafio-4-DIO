package br.com.dio.padroes.desconto;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Monta a corrente a partir dos beans de RegraDesconto, na ordem do @Order.
 * Uma regra nova entra na corrente só por existir como @Component.
 */
@Component
public class CadeiaDescontos {

    private final RegraDesconto primeira;

    public CadeiaDescontos(List<RegraDesconto> regras) {
        for (int i = 0; i < regras.size() - 1; i++) {
            regras.get(i).encadear(regras.get(i + 1));
        }
        this.primeira = regras.isEmpty() ? null : regras.getFirst();
    }

    public ContextoDesconto aplicar(ContextoDesconto contexto) {
        if (primeira != null) {
            primeira.aplicar(contexto);
        }
        return contexto;
    }
}

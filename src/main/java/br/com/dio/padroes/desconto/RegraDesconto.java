package br.com.dio.padroes.desconto;

/**
 * Chain of Responsibility: cada regra decide se concede desconto
 * e depois repassa o contexto para a próxima regra da corrente.
 */
public abstract class RegraDesconto {

    private RegraDesconto proxima;

    void encadear(RegraDesconto proxima) {
        this.proxima = proxima;
    }

    public final void aplicar(ContextoDesconto contexto) {
        processar(contexto);
        if (proxima != null) {
            proxima.aplicar(contexto);
        }
    }

    protected abstract void processar(ContextoDesconto contexto);
}

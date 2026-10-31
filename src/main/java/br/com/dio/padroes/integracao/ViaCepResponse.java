package br.com.dio.padroes.integracao;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        Boolean erro) {

    public boolean naoEncontrado() {
        return Boolean.TRUE.equals(erro) || uf == null;
    }
}

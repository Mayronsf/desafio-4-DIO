package br.com.dio.padroes;

import br.com.dio.padroes.integracao.ViaCepClient;
import br.com.dio.padroes.integracao.ViaCepResponse;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoApiTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ViaCepClient viaCep;

    @BeforeEach
    void configurarViaCep() {
        when(viaCep.consultar("01001000"))
                .thenReturn(new ViaCepResponse("01001-000", "Praça da Sé", "Sé", "São Paulo", "SP", null));
        when(viaCep.consultar("20040002"))
                .thenReturn(new ViaCepResponse("20040-002", "Rua da Assembleia", "Centro", "Rio de Janeiro", "RJ", null));
        when(viaCep.consultar("99999999"))
                .thenReturn(new ViaCepResponse(null, null, null, null, null, true));
    }

    @Test
    void fluxoCompletoDoPedido() throws Exception {
        long clienteId = criarCliente("Ana", "ana@email.com", "01001-000");

        // Primeira compra (10%) + DIO10 (10%) sobre R$ 200; SEDEX 2kg em SP = 18 + 2 * 4 = R$ 26
        long pedidoId = criarPedido(clienteId, "200.00", "2", "SEDEX", "dio10")
                .andExpect(jsonPath("$.valorFrete").value(26.00))
                .andExpect(jsonPath("$.descontos", hasSize(2)))
                .andExpect(jsonPath("$.valorDesconto").value(40.00))
                .andExpect(jsonPath("$.valorTotal").value(186.00))
                .andExpect(jsonPath("$.status").value("CRIADO"))
                .andReturn().getResponse().getContentAsString().transform(PedidoApiTest::idDe);

        alterarStatus(pedidoId, "PAGO").andExpect(status().isOk());
        alterarStatus(pedidoId, "ENVIADO").andExpect(status().isOk());
        alterarStatus(pedidoId, "CANCELADO").andExpect(status().isUnprocessableEntity());

        mvc.perform(get("/pedidos/{id}/historico", pedidoId))
                .andExpect(jsonPath("$[*].statusNovo", contains("CRIADO", "PAGO", "ENVIADO")));

        // Segundo pedido já não é primeira compra: só o FRETEGRATIS conta
        criarPedido(clienteId, "200.00", "2", "PAC", "FRETEGRATIS")
                .andExpect(jsonPath("$.valorFrete").value(15.00))
                .andExpect(jsonPath("$.valorDesconto").value(15.00))
                .andExpect(jsonPath("$.valorTotal").value(200.00));
    }

    @Test
    void enderecoJaConhecidoNaoConsultaOViaCepDeNovo() throws Exception {
        // CEP usado só neste teste, para garantir que ainda não está no banco
        criarCliente("Bia", "bia@email.com", "20040002");
        criarCliente("Caio", "caio@email.com", "20040-002");

        verify(viaCep, times(1)).consultar(anyString());
    }

    @Test
    void cepInexistenteRetorna404() throws Exception {
        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Davi", "email": "davi@email.com", "cep": "99999-999"}"""))
                .andExpect(status().isNotFound());
    }

    @Test
    void simulaFreteDeTodasAsModalidades() throws Exception {
        long clienteId = criarCliente("Eva", "eva@email.com", "01001000");

        mvc.perform(get("/clientes/{id}/frete", clienteId).param("pesoKg", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].modalidade", contains("RETIRADA", "PAC", "SEDEX")));
    }

    @Test
    void cupomInvalidoNaoCriaPedido() throws Exception {
        long clienteId = criarCliente("Fabi", "fabi@email.com", "01001000");

        criarPedido(clienteId, "50.00", "1", "PAC", "BLACKFRIDAY")
                .andExpect(status().isUnprocessableEntity());
    }

    private long criarCliente(String nome, String email, String cep) throws Exception {
        String json = mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "%s", "email": "%s", "cep": "%s"}""".formatted(nome, email, cep)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return idDe(json);
    }

    private ResultActions criarPedido(long clienteId, String valor, String peso, String modalidade, String cupom)
            throws Exception {
        return mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"clienteId": %d, "valorProdutos": %s, "pesoKg": %s, "modalidadeFrete": "%s", "cupom": "%s"}"""
                        .formatted(clienteId, valor, peso, modalidade, cupom)));
    }

    private ResultActions alterarStatus(long pedidoId, String status) throws Exception {
        return mvc.perform(patch("/pedidos/{id}/status", pedidoId).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"status": "%s"}""".formatted(status)));
    }

    private static long idDe(String json) {
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }
}

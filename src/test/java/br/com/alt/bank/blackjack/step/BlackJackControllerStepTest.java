package br.com.alt.bank.blackjack.step;

import br.com.alt.bank.blackjack.domain.Rodada;
import br.com.alt.bank.blackjack.dto.RequestSelecionarCartaDTO;
import br.com.alt.bank.blackjack.dto.RequestJogadorDTO;
import br.com.alt.bank.blackjack.repository.RodadaRepository;
import br.com.alt.bank.blackjack.service.impl.BaralhoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class BlackJackControllerStepTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BaralhoServiceImpl servico;
    @Autowired
    private RodadaRepository redadaRepository;


    @Dado("Que realizado a chamada ao servico iniciarJogo passando os parametros {string}, {int}")
    public void queRealizadoAChamadaAoServicoIniciarJogoPassandoOsParametros(String nome, int valorAS) throws Exception {
        mockMvc.perform(post("/api/v1/iniciarJogo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRequestUsuarioDTO(nome, valorAS))))
                .andExpect(status().isCreated());
    }

    @Quando("Realizo nova chamada ao servico iniciarJogo passando os parametros {string}, {int}.")
    public void realizoNovaChamadaAoServicoIniciarJogoPassandoOsParametros(String nome, int valorAS) throws Exception {
        mockMvc.perform(post("/api/v1/iniciarJogo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRequestUsuarioDTO(nome, valorAS))))
                .andExpect(status().isBadRequest());
    }

    @Quando("Realizo a chamada ao servico para buscar as rodadas.")
    public void realizoAChamadaAoServicoParaBuscarAsRodadas() throws Exception {
        mockMvc.perform(get("/api/v1/buscarRodadasEncerradas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Quando("Realizo nova chamada ao servico iniciarJogo passando um valor invalido para a carta AS {string}, {int}.")
    public void realizoNovaChamadaAoServicoIniciarJogoPassandoUmValorInvalidoParaACartaAS(String nome, int valorAS) throws Exception {
        mockMvc.perform(post("/api/v1/iniciarJogo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRequestUsuarioDTO(nome, valorAS))))
                .andExpect(status().isBadRequest());
        ;
    }

    @Então("Sera criado uma nova rodada.")
    public void seraCriadoUmaNovaRodada() {
        Rodada rodada = redadaRepository.findByDataFimIsNull();
        Assert.assertNotNull(rodada);
    }

    @Quando("Realizo a chamada ao servico embaralhar")
    public void realizoAChamadaAoServicoEmbaralhar() throws Exception {
        mockMvc.perform(get("/api/v1/embaralhar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Quando("Realizo a chamadada ao servico selecionar carta passando os parametos: {int}, {string}")
    public void realizoAChamadadaAoServicoSelecionarCartaPassandoOsParametos(int carta, String nome) throws Exception {
        mockMvc.perform(put("/api/v1/selecionarCarta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRequestSelecionarCarta(carta, nome))))
                .andExpect(status().isOk());
    }

    @Quando("Quando realizado a busca dos pontos do jogador passando o parametro: {string}")
    public void quandoRealizadoABuscaDosPontosDoJogadorPassandoOParametro(String nome) throws Exception {
        mockMvc.perform(get("/api/v1/buscarPontosPorJogador")
                        .param("nome", nome)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome do jogador " + nome + " não encontrado!!!"));
    }

    @Quando("chamo o servico para consultar mesa.")
    public void chamoOServicoParaConsultarMesa() throws Exception {
        mockMvc.perform(get("/api/v1/monstrarMesa")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Dado("Que realizado a chamada ao servico deletar")
    public void queRealizadoAChamadaAoServicoDeletar() throws Exception {
        mockMvc.perform(delete("/api/v1/deletarJogo")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(204));
    }

    @Quando("realizado a chamada ao servico iniciarJogo passando os parametros {string}, {int}")
    public void realizadoAChamadaAoServicoIniciarJogoPassandoOsParametros(String nome, int valorAS) throws Exception {
        mockMvc.perform(post("/api/v1/iniciarJogo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRequestUsuarioDTO(nome, valorAS))))
                .andExpect(status().isCreated());
    }

    private RequestSelecionarCartaDTO getRequestSelecionarCarta(int carta, String nome) {
        RequestSelecionarCartaDTO dto = new RequestSelecionarCartaDTO(nome, carta);
        return dto;
    }

    private static RequestJogadorDTO getRequestUsuarioDTO(String nome, int valorAS) {
        RequestJogadorDTO dados = new RequestJogadorDTO(nome, valorAS);
        return dados;
    }

}

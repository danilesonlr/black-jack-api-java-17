package br.com.alt.bank.blackjack.service;

import br.com.alt.bank.blackjack.dto.*;
import br.com.alt.bank.blackjack.exception.BlackJackExeption;

import java.util.List;

public interface BaralhoService {
    /**
     * Serviço para iniciar Jogo.
     * @return DTO.
     */
    ResponseJogadorDTO iniciarJogo(RequestJogadorDTO dados) throws BlackJackExeption;

    /**
     * Serviço para embaralhar cartas
     * @return DTO.
     */
    ResponseEmbaralharDTO embaralhar() throws BlackJackExeption;

    /**
     * Serviço para selcionar carta e alocar ao Usuário
     * @return String.
     */
    String selecionarCarta(RequestSelecionarCartaDTO dados) throws BlackJackExeption;

    /**
     * Serviço para monstrar cartas da mesa
     * @return Lista de DTO.
     */
    List<MesaResponseDTO> mostrarMesa() throws BlackJackExeption;

    /**
     * Serviço para buscar pontos Por Jogadores.
     * @return DTO.
     */
    ResponsePontosJogadoresDTO buscarPontosPorJogadores(String nome) throws BlackJackExeption;

    /**
     * Serviço para deletar jogo.
     */
    void deletarJogo();

    /**
     *  Serviço para retornar todas as rodadas encerradas.
     * @return
     */
    RodadasEncerradasDTO buscarRodadasEncerradas() throws BlackJackExeption;
}

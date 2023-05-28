package br.com.alt.bank.blackjack.dto;

import br.com.alt.bank.blackjack.domain.Jogador;

public record ResponsePontosJogadoresDTO(String nome, Integer pontos){

    public ResponsePontosJogadoresDTO(Jogador jogador){
        this(jogador.getNome(), jogador.getPonto());
    }
}

package br.com.alt.bank.blackjack.dto;

import lombok.NonNull;

public record RequestJogadorDTO(
        @NonNull
        String nome,
        @NonNull
        Integer valorA){}

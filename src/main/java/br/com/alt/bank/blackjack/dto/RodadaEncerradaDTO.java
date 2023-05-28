package br.com.alt.bank.blackjack.dto;

import java.util.Date;

public record RodadaEncerradaDTO(
        Long id,
        String jogadadorVencedor,
        Integer pontuacaoVencedor,
        String nomePerderdor,
        Integer pontuacaoPerderdor,
        Date inicioJogo,
        Date fimDoJogo){}

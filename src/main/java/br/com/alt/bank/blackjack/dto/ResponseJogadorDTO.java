package br.com.alt.bank.blackjack.dto;

import lombok.NonNull;

import java.util.Date;

public record ResponseJogadorDTO(Long id, Date inicioJogo, Date fimDoJogo){}

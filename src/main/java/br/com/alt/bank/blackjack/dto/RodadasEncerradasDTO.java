package br.com.alt.bank.blackjack.dto;

import java.util.Date;
import java.util.List;

public record RodadasEncerradasDTO<T>(List<T> rodadas){}

package br.com.alt.bank.blackjack.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Cartas {

    AS (1),
    DOIS(2),
    TRES(3),
    QUATRO(4),
    CINCO(5),
    SEIS(6),
    SETE(7),
    OITO(8),
    NOVE(9),
    DEZ(10),
    VALETE(11),
    DAMA(11),
    REI(11);

    private Integer valor;

}

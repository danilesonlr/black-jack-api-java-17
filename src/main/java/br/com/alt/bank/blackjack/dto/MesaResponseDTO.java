package br.com.alt.bank.blackjack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MesaResponseDTO<T> implements Serializable {
    private String jogador;
    private Integer pontos;
    private List<T> Cartas;
}

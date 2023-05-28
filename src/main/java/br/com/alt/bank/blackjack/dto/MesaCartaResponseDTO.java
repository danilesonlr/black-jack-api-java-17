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
public class MesaCartaResponseDTO implements Serializable {
    private String nome;
    private Integer valor;
    private String naipe;
    private Integer posicao;
}

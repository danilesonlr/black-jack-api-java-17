package br.com.alt.bank.blackjack.domain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "carta")
@Entity(name = "carta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Carta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer valor ;
    private String naipe ;
    private Integer posicao;
    @ManyToOne
    @JoinColumn(name = "rodada_id")
    private Rodada rodada;
    @ManyToOne
    @JoinColumn(name = "JOGADOR_ID")
    private Jogador jogador;
}

package br.com.alt.bank.blackjack.domain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name = "rodada")
@Entity(name = "rodada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Rodada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeVencedor;
    private Integer pontosVencedor;
    private String nomePerderdor;
    private Integer pontuacaoPerdedor;
    private Integer rodadas;
    @Column(name="DATA_INICIO")
    private Date dataInicio ;
    @Column(name="DATA_FIM")
    private Date dataFim ;

}

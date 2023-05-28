package br.com.alt.bank.blackjack.repository;

import br.com.alt.bank.blackjack.domain.Carta;
import br.com.alt.bank.blackjack.domain.Rodada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartaRepository extends JpaRepository<Carta, Long> {

    Carta findByPosicaoAndJogadorIsNull(Integer carta);

    List<Carta> findByJogadorIsNull();

}

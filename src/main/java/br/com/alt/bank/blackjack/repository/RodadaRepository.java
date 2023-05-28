package br.com.alt.bank.blackjack.repository;

import br.com.alt.bank.blackjack.domain.Carta;
import br.com.alt.bank.blackjack.domain.Rodada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RodadaRepository extends JpaRepository<Rodada, Long> {
    Rodada findByDataFimIsNull();
    List findByDataFimIsNotNull();

}

package br.com.alt.bank.blackjack.repository;

import br.com.alt.bank.blackjack.domain.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    Jogador findByNome(String nome);
}

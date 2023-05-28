package br.com.alt.bank.blackjack.exception;

public class BlackJackExeption extends Exception{

    public BlackJackExeption(String message) {
        super(message);
    }

    public BlackJackExeption(Throwable t) {
        super(t);
    }
}

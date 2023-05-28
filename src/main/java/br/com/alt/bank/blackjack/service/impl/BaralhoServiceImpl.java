package br.com.alt.bank.blackjack.service.impl;

import br.com.alt.bank.blackjack.domain.Carta;
import br.com.alt.bank.blackjack.domain.Rodada;
import br.com.alt.bank.blackjack.domain.Jogador;
import br.com.alt.bank.blackjack.dto.*;
import br.com.alt.bank.blackjack.exception.BlackJackExeption;
import br.com.alt.bank.blackjack.repository.CartaRepository;
import br.com.alt.bank.blackjack.repository.RodadaRepository;
import br.com.alt.bank.blackjack.repository.JogadorRepository;
import br.com.alt.bank.blackjack.service.BaralhoService;
import br.com.alt.bank.blackjack.service.Cartas;
import br.com.alt.bank.blackjack.service.Naiper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class BaralhoServiceImpl<T> implements BaralhoService {

    public static final int VALOR_0 = 0;
    public static final String MESA = "MESA";
    public static final int VALOR_21 = 21;
    public static final int VALOR_5 = 5;
    public static final int VALOR_17 = 17;
    @Autowired
    private RodadaRepository rodadaRepository;
    @Autowired
    private JogadorRepository jogadorRepository;
    @Autowired
    private CartaRepository cartaRepository;

    @Override
    public ResponseJogadorDTO iniciarJogo(RequestJogadorDTO dados) throws BlackJackExeption {

        if (!validaCartaAs(dados.valorA())) {
            montarException("O valor permitido para o campo As e 1 ou 10.");
        }
        if (!validaRodada()) {
            montarException("Não e possível criar novo jogo já existe um jogo em andamento.");
        }
        log.warn("Iniciando rodada!!!");
        Rodada rodada = salvarRodada();
        salvarUsuario(rodada, dados.nome().toUpperCase(), dados.valorA());
        salvarUsuario(rodada, MESA, null);
        salvarCarta(rodada, dados.valorA());
        return new ResponseJogadorDTO(rodada.getId(), rodada.getDataInicio(), rodada.getDataFim());
    }

    private static void montarException(String message) throws BlackJackExeption {
        throw new BlackJackExeption(message);
    }

    @Override

    public ResponseEmbaralharDTO embaralhar() throws BlackJackExeption {
        if (validaRodada()) {
            montarException("Não existe mesa aberta no momento. Favor iniciar um jogo.");
        }
        log.warn("Iniciando processo de embaralhamento...");
        List<Carta> cartas = cartaRepository.findAll();
        Collections.shuffle(cartas);
        List<Integer> cartasID = new ArrayList<>();
        cartas.forEach(carta -> {
            if (carta.getJogador() == null) {
                cartasID.add(carta.getPosicao());
            }
        });
        return new ResponseEmbaralharDTO(cartasID);
    }

    @Override
    public String selecionarCarta(RequestSelecionarCartaDTO dados) throws BlackJackExeption {

        Rodada rodada = rodadaRepository.findByDataFimIsNull();
        if (validaRodada()) {
            montarException("Não existe mesa aberta no momento. Favor iniciar um jogo.");
        }
        String jogadorGanhador = validarJogoEncerrado(rodada);
        if (jogadorGanhador != null) return jogadorGanhador;

        Jogador mesa = jogadorRepository.findByNome(MESA);
        Carta carta = cartaRepository.findByPosicaoAndJogadorIsNull(dados.carta());
        Jogador jogador = jogadorRepository.findByNome(dados.nomeJogador().toUpperCase());
        if (!validarJogador(dados.nomeJogador())) {
            montarException("Favor digite um jogador valido!!!");
        }
        if (!validaObjetoNulo((T) jogador)) {
            montarException("Jogador " + dados.nomeJogador() + " não encontrado!!!");
        }
        if (!validaObjetoNulo((T) carta)) {
            montarException("Carta " + dados.carta() + " não encontrado!!!");
        }
        if (!validaObjetoNulo((T) rodada)) {
            montarException("Jogo não encontrado!!!");
        }
        log.warn("Realizando buscar da cartas para Jogador " + dados.nomeJogador());
        carta.setJogador(jogador);
        cartaRepository.save(carta);
        jogador.setPonto(somarPontos(jogador.getPonto(), carta.getValor()));
        jogadorRepository.save(jogador);
        gravarSelecaoMesa();
        Integer quantRodadas = rodada.getRodadas();
        rodada.setRodadas(quantRodadas++);
        rodadaRepository.save(rodada);
        String encerrarJogo = encerrarJogo(rodada, mesa, jogador);
        if (encerrarJogo != null) return encerrarJogo;
        return "Carta " + carta.getPosicao() + " Vinculada ao Jogador " + jogador.getNome();
    }

    @Override
    public List<MesaResponseDTO> mostrarMesa() throws BlackJackExeption {
        if (validaRodada()) {
            montarException("Não existe mesa aberta no momento. Favor iniciar um jogo.");
        }
        List<Jogador> jogadores = jogadorRepository.findAll();
        List<Carta> cartas = cartaRepository.findAll();
        List<MesaResponseDTO> mesa = new ArrayList<>();
        for (Jogador jogadore : jogadores) {
            MesaResponseDTO mesaIntegrador = new MesaResponseDTO();
            mesaIntegrador.setJogador(jogadore.getNome());
            mesaIntegrador.setPontos(jogadore.getPonto());
            List<MesaCartaResponseDTO> listCartasIntegrador = new ArrayList<>();
            for (Carta carta : cartas) {
                MesaCartaResponseDTO cartaIntegrador = new MesaCartaResponseDTO();
                if (carta.getJogador() != null) {
                    if (carta.getJogador().getId().equals(jogadore.getId())) {
                        cartaIntegrador.setNome(carta.getNome());
                        cartaIntegrador.setNaipe(carta.getNaipe());
                        cartaIntegrador.setValor(carta.getValor());
                        cartaIntegrador.setPosicao(carta.getPosicao());
                        listCartasIntegrador.add(cartaIntegrador);
                    }
                }
            }
            mesaIntegrador.setCartas(listCartasIntegrador);
            mesa.add(mesaIntegrador);
        }
        return mesa;
    }

    @Override
    public ResponsePontosJogadoresDTO buscarPontosPorJogadores(String nome) throws BlackJackExeption {
        if (!validaObjetoNulo((T) nome)) {
            montarException("Campo do Jogador esta em branco.");
        }
        Jogador jogador = jogadorRepository.findByNome(nome.toUpperCase());
        if (!validaObjetoNulo((T) jogador)) {
            montarException("Nome do jogador " + nome + " não encontrado!!!");
        }
        return new ResponsePontosJogadoresDTO(jogador.getNome(), jogador.getPonto());
    }

    @Override
    public RodadasEncerradasDTO buscarRodadasEncerradas() throws BlackJackExeption {
        List<RodadaEncerradaDTO> ret = new ArrayList<>();

        List<Rodada> rodadas = rodadaRepository.findByDataFimIsNotNull();
        if(!validaListNulo((List<T>) rodadas)){
            montarException("Não existe rodadas finalizadas para apresentação");
        }
        rodadas.forEach(rodada -> {
            Integer pontosMesa;
            RodadaEncerradaDTO dto = new RodadaEncerradaDTO(rodada.getId(),
                    rodada.getNomeVencedor(),
                    rodada.getPontosVencedor(),
                    rodada.getNomePerderdor(),
                    rodada.getPontuacaoPerdedor(),
                    rodada.getDataInicio(), rodada.getDataFim());
            ret.add(dto);
        });
        return new RodadasEncerradasDTO(ret);
    }

    @Override
    public void deletarJogo() {
        log.warn("Deletando Jogo");
        cartaRepository.deleteAll();
        jogadorRepository.deleteAll();
        rodadaRepository.deleteAll();
    }

    private Rodada salvarRodada() {
        Rodada rodada = new Rodada();
        rodada.setRodadas(VALOR_0);
        rodada.setDataInicio(Calendar.getInstance().getTime());
        return rodadaRepository.save(rodada);
    }

    private Jogador salvarUsuario(Rodada rodada, String name, Integer valorA) {
        Jogador jogador = new Jogador();
        jogador.setPonto(VALOR_0);
        jogador.setRodadasVencidas(VALOR_0);
        jogador.setRodada(rodada);
        jogador.setNome(name);
        jogador.setValorA(valorA);
        return jogadorRepository.save(jogador);
    }

    private Iterable<Carta> salvarCarta(Rodada rodada, Integer valorA) {
        return cartaRepository.saveAll(criarBaralho(rodada, valorA));
    }


    private List<Carta> criarBaralho(Rodada rodada, Integer valorA) {
        List<Carta> cartas = new ArrayList<>();
        Integer cont = VALOR_0;

        for (Naiper naiper : Naiper.values()) {
            for (Cartas cartaEnum : Cartas.values()) {
                Carta carta = new Carta();
                carta.setNaipe(naiper.name());
                carta.setRodada(rodada);
                carta.setPosicao(cont);
                if (cartaEnum.name().equals("AS")) {
                    carta.setNome(cartaEnum.name());
                    carta.setValor(valorA);
                } else {
                    carta.setNome(cartaEnum.name());
                    carta.setValor(cartaEnum.getValor());
                }
                cartas.add(carta);
                cont++;
            }
        }
        return cartas;
    }

    private Integer somarPontos(Integer ponto, Integer valor) {
        return ponto + valor;
    }

    private boolean validaRodada() {
        Rodada rodada = rodadaRepository.findByDataFimIsNull();
        if (rodada != null) {
            if (rodada.getDataFim() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean validaCartaAs(Integer valorAs) {
        if (valorAs == null) {
            return false;
        } else if (valorAs != 1 && valorAs != 10) {
            return false;
        }
        return true;
    }

    private Boolean validaObjetoNulo(T objeto) {
        if (objeto == null) {
            return false;
        }
        return true;
    }

    private Boolean validaListNulo(List<T> lista) {
        if (lista == null || lista.isEmpty()) {
            return false;
        }
        return true;
    }

    private void gravarSelecaoMesa() {
        Jogador jogador = jogadorRepository.findByNome(MESA);
        List<Carta> cartas = cartaRepository.findByJogadorIsNull();
        Collections.shuffle(cartas);
        Carta carta = cartas.get(VALOR_0);
        carta.setJogador(jogador);
        cartaRepository.save(carta);
        jogador.setPonto(somarPontos(jogador.getPonto(), carta.getValor()));
        jogadorRepository.save(jogador);
    }

    private boolean validarJogador(String nomeJogador) {
        if (nomeJogador == null || nomeJogador.toUpperCase().equals(MESA)) {
            return false;
        }
        return true;
    }

    private String encerrarJogo(Rodada rodada, Jogador mesa, Jogador jogador) {
        if (validaObjetoNulo((T) mesa) && validaObjetoNulo((T) jogador)) {

            if (rodada.getRodadas().equals(VALOR_5)
                    || mesa.getPonto() >= VALOR_17
                    || jogador.getPonto() >= VALOR_17) {
                if (mesa.getPonto() > jogador.getPonto()) {
                    if (mesa.getPonto() > VALOR_21) {
                        Integer quantRodadas = mesa.getRodadasVencidas();
                        jogador.setRodadasVencidas(quantRodadas);
                        return retornarVencedor(rodada, jogador, mesa);
                    }
                    Integer quantRodadas = mesa.getRodadasVencidas();
                    mesa.setRodadasVencidas(quantRodadas);
                    return retornarVencedor(rodada, mesa, jogador);
                } else {
                    if (jogador.getPonto() > VALOR_21) {
                        Integer quantRodadas = mesa.getRodadasVencidas();
                        mesa.setRodadasVencidas(quantRodadas);
                        return retornarVencedor(rodada, mesa, jogador);
                    }
                    Integer quantRodadas = mesa.getRodadasVencidas();
                    jogador.setRodadasVencidas(quantRodadas);
                    return retornarVencedor(rodada, jogador, mesa);
                }
            }
        }
        return null;
    }

    private String retornarVencedor(Rodada rodada, Jogador Vencedor, Jogador Perderdor) {
        rodada.setNomeVencedor(Vencedor.getNome());
        rodada.setPontosVencedor(Vencedor.getPonto());
        rodada.setNomePerderdor(Perderdor.getNome());
        rodada.setPontuacaoPerdedor(Perderdor.getPonto());
        rodada.setDataFim(new Date());
        rodadaRepository.save(rodada);
        cartaRepository.deleteAll();
        jogadorRepository.deleteAll();
        return getRetorneGanhador(Vencedor);
    }

    private String validarJogoEncerrado(Rodada rodada) {
        if (rodada != null && rodada.getDataFim() != null) {
            Jogador jogadorGanhador = jogadorRepository.findByNome(rodada.getNomeVencedor().toUpperCase());
            return getRetorneGanhador(jogadorGanhador);
        }
        return null;
    }

    private static String getRetorneGanhador(Jogador jogadorGanhador) {
        return "Jogo encerrado!!! Vitória do jogador:"
                + jogadorGanhador.getNome() + " com "
                + jogadorGanhador.getPonto() + " pontos";
    }
}

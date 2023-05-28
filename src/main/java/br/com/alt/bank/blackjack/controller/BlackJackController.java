package br.com.alt.bank.blackjack.controller;

import br.com.alt.bank.blackjack.dto.*;
import br.com.alt.bank.blackjack.exception.BlackJackExeption;
import br.com.alt.bank.blackjack.service.BaralhoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BlackJackController {

    @Autowired
    private BaralhoService service;

    @ApiOperation(value = "Criar novo jogo por jogador.")
    @PostMapping("/iniciarJogo")
    public ResponseEntity iniciarJogo(@RequestBody @Validated RequestJogadorDTO dados, UriComponentsBuilder uriBuilder) throws BlackJackExeption {
        ResponseJogadorDTO dto = service.iniciarJogo(dados);
        var uri = uriBuilder.path("/iniciarJogo/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }


    @ApiOperation(value = "Função para embaralhar.")
    @GetMapping("/embaralhar")
    public ResponseEntity embaralhar() throws BlackJackExeption {
        ResponseEmbaralharDTO dto = service.embaralhar();
        return ResponseEntity.ok(dto);
    }

    @ApiOperation(value = "Seleciona a carta por ID.")
    @PutMapping("/selecionarCarta")
    public ResponseEntity selecionarCarta(@RequestBody RequestSelecionarCartaDTO dados) throws BlackJackExeption {
        return ResponseEntity.ok(service.selecionarCarta(dados));
    }

    @ApiOperation(value = "Mostra pontuação e carta dos entegrantes da mesa.")
    @GetMapping("/monstrarMesa")
    public ResponseEntity monstrarMesa() throws BlackJackExeption {

        List<MesaResponseDTO> dto = service.mostrarMesa();
        return ResponseEntity.ok(dto);
    }

    @ApiOperation(value = "Buscar Pontos por Jogadores.")
    @GetMapping("/buscarPontosPorJogador")
    public ResponseEntity buscarPontosPorJogador(@RequestParam  String nome) throws BlackJackExeption {
        return ResponseEntity.ok(service.buscarPontosPorJogadores(nome));
    }

    @ApiOperation(value = "Buscar Rodadas Encerradas.")
    @GetMapping("/buscarRodadasEncerradas")
    public ResponseEntity buscarRodadasEncerradas() throws BlackJackExeption {
        return ResponseEntity.ok(service.buscarRodadasEncerradas());
    }

    @ApiOperation(value = "Deleta o Jogo.")
    @DeleteMapping("/deletarJogo")
    public ResponseEntity deletarJogo(){
        service.deletarJogo();
        return ResponseEntity.noContent().build();
    }


}

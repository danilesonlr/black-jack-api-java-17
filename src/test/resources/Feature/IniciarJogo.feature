#language:pt

  Funcionalidade: iniciarJogo
    Cenario: Criar novo Jogo
      Dado Que realizado a chamada ao servico iniciarJogo passando os parametros "Danielson", 10
      Quando Realizo nova chamada ao servico iniciarJogo passando os parametros "NovoJogo", 1.
      Quando Realizo nova chamada ao servico iniciarJogo passando um valor invalido para a carta AS "NovoJogo", 27.
      Então Sera criado uma nova rodada.


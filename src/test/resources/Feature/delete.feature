#language:pt

  Funcionalidade: Deletar Jogo
    Cenario: Deletando jogo.
      Dado Que realizado a chamada ao servico deletar
      Quando realizado a chamada ao servico iniciarJogo passando os parametros "Danielson", 10
      Então Sera criado uma nova rodada.


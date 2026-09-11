package com.example.gestaoconvenios.application.convenios.contatos.buscacontatos;

public record BuscaContatosCommand(
    Long empresaConveniadaId,
    String search,
    Integer page,
    Integer size
) {}

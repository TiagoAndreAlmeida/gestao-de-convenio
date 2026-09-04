package com.example.gestaoconvenios.infrastructure.convenios.controller.contato.response;

public record CadastraContatoResponse(
    Long id,
    String nome,
    String cargo,
    String email,
    String telefone,
    Long empresaId
) {}

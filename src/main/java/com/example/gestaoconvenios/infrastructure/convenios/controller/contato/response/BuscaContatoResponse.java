package com.example.gestaoconvenios.infrastructure.convenios.controller.contato.response;

import java.time.LocalDateTime;

public record BuscaContatoResponse(
    Long id,
    String nome,
    String cargo,
    String email,
    String telefone,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {}

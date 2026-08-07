package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.response;

import java.time.LocalDateTime;

public record CadastraEmpresaConveniadaResponse(
    String razaoSocial,
    String cnpj,
    String endereco,
    boolean ativa,
    LocalDateTime criadaEm,
    LocalDateTime atualizadoEm
) {}

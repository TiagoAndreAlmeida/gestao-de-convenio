package com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Busca Contatos", description = "Parâmetros de entrada para busca de contatos de uma empresa conveniada")
public record BuscaContatosRequest(
    @Schema(description = "Número da página a ser retornada (0-indexed)", example = "0")
    @NotNull(message = "O parâmetro 'page' não pode ser nulo.")
    @Min(value = 0, message = "O parâmetro 'page' deve ser maior ou igual a 0.")
    Integer page,

    @Schema(description = "Quantidade de contatos a serem retornados por página", example = "10")
    @NotNull(message = "O parâmetro 'size' não pode ser nulo.")
    @Min(value = 1, message = "O parâmetro 'size' deve ser maior ou igual a 1.")
    @Max(value = 20, message = "O parâmetro 'size' deve ser menor ou igual a 20.")
    Integer size,

    @Schema(description = "Termo de busca para filtrar contatos pelo nome ou email", example = "João")
    String search
) {}

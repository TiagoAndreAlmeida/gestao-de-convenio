package com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados de entrada para cadastro de um contato de uma empresa conveniada")
public record CadastraContatoRequest(
    @Schema(description = "Nome do contato")
    @NotBlank(message = "nome é obrigatório")
    String nome,

    @Schema(description = "Cargo ocupado pelo contato")
    @NotBlank(message = "cargo é obrigatório")
    String cargo,

    @Schema(description = "Email utilizado pelo contato na empresa")
    @NotBlank(message = "email é obrigatório")
    @Email(message = "email precisa ser válido")
    String email,

    @Schema(description = "Telefone de contato na empresa do contato sem formatação com DDD", example = "85996457841")
    @NotBlank(message = "telefone é obrigatório")
    String telefone
) {

}

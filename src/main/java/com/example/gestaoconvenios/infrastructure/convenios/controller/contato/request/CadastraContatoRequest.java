package com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastraContatoRequest(
    @NotBlank(message = "nome é obrigatório")
    String nome,

    @NotBlank(message = "cargo é obrigatório")
    String cargo,

    @NotBlank(message = "email é obrigatório")
    @Email(message = "email precisa ser válido")
    String email,

    @NotBlank(message = "telefone é obrigatório")
    String telefone
) {

}

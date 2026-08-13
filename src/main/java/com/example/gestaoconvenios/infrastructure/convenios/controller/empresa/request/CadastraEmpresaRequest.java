package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.request;

import java.util.List;

import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.CadastraContatoRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CadastraEmpresaRequest(
    @NotBlank(message = "razaoSocial é Obrigatório")
    String razaoSocial,

    @NotBlank(message = "cnpj é Obrigatório")
    String cnpj,

    @NotBlank(message = "endereco é Obrigatório")
    String endereco,

    @NotEmpty(message = "pelo menos 1 contato é necessário")
    @Valid
    List<CadastraContatoRequest> contatos
) {}

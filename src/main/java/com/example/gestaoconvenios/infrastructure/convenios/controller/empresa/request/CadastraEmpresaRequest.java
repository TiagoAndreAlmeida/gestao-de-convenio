package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.request;

import java.util.List;

import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.CadastraContatoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "Cadastro Empresa Conveniada", description = "Dados de entrada para cadastro de uma nova Empresa conveniada")
public record CadastraEmpresaRequest(
    @Schema(description = "Razão social oficial da empresa", example = "Parceiro de Negócios Brasil LTDA")
    @NotBlank(message = "razaoSocial é Obrigatório")
    String razaoSocial,

    @Schema(description = "CNPJ sem formatação seguindo as regras vigêntes", example = "1234567891011", pattern = "^\\\\d{14}$")
    @NotBlank(message = "cnpj é Obrigatório")
    String cnpj,

    @Schema(description = "Endereço físico completo da empresa", example = "Av. Paulista, 1500, Bela Vista, São Paulo - SP")
    @NotBlank(message = "endereco é Obrigatório")
    String endereco,

    @Schema(description = "Lista contendo pelo menos um contato associado à empresa")
    @NotEmpty(message = "pelo menos 1 contato é necessário")
    @Valid
    List<CadastraContatoRequest> contatos
) {}

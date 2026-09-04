package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Response Empresa Conveniada", description = "Dados retornados após o sucesso do cadastro da empresa")
public record CadastraEmpresaConveniadaResponse(
    @Schema(description = "Razão Social registrada", example = "Parceiro  de Negócios Brasil LTDA")
    String razaoSocial,
    
    @Schema(description = "CNPJ registrado", example = "12345678910111")
    String cnpj,

    @Schema(description = "Endereço registrado", example = "Av. Paulista, 1500, Bela Vista, São Paulo - SP")
    String endereco,

    @Schema(description = "flag informando que a empresa está ativa", example = "true")
    boolean ativa,
    
    @Schema(description = "Data e hora de criação do registro")
    LocalDateTime criadoEm,
    
    @Schema(description = "Data e hora da ultima atualização do registro")
    LocalDateTime atualizadoEm
) {}

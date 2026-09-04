package com.example.gestaoconvenios.infrastructure.convenios.controller.contato.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Response Contato", description = "Dados retornados após o sucesso do cadastro de um contato")
public record CadastraContatoResponse(
    @Schema(description = "ID do contato registrado", example = "1")
    Long id,
    @Schema(description = "Nome do contato", example = "João da Silva")
    String nome,
    @Schema(description = "Cargo do contato", example = "Gerente de Vendas")
    String cargo,
    @Schema(description = "Email do contato", example = "joao.silva@empresa.com")
    String email,
    @Schema(description = "Telefone do contato", example = "11999999999")
    String telefone,
    @Schema(description = "ID da empresa conveniada", example = "1")
    Long empresaId
) {}

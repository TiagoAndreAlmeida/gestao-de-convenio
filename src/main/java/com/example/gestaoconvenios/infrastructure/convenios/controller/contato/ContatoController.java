package com.example.gestaoconvenios.infrastructure.convenios.controller.contato;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestaoconvenios.application.contatos.CadastraContatoCommand;
import com.example.gestaoconvenios.application.contatos.CadastraContatoUseCase;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.CadastraContatoRequest;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.response.CadastraContatoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("")
@Tag(name = "Contatos", description = "Endpoints para gerenciamento de contatos de empresas conveniadas")
public class ContatoController {
    private CadastraContatoUseCase cadastraContatoUseCase;

    public ContatoController(CadastraContatoUseCase cadastraContatoUseCase) {
        this.cadastraContatoUseCase = cadastraContatoUseCase;
    }

    @PostMapping("/empresas/{empresaId}/contatos")
    @Operation(
        summary = "Cadastra um novo contato para uma empresa conveniada",
        description = "Registra um novo contato associado a uma empresa conveniada existente no sistema.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Contato criado com sucesso",
                content = @Content(schema = @Schema(implementation = CadastraContatoResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Empresa conveniada não encontrada",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Conflito: Contato com o email informado já está cadastrado",
                content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    public ResponseEntity<CadastraContatoResponse> cadastraContato(
        @RequestParam Long empresaId, @RequestBody CadastraContatoRequest request
    ) {
        CadastraContatoCommand command = new CadastraContatoCommand(
            request.nome(),
            request.cargo(),
            request.email(),
            request.telefone(),
            empresaId
        );

        Contato contato = cadastraContatoUseCase.execute(command);
        CadastraContatoResponse response = new CadastraContatoResponse(
            contato.getId(),
            contato.getNome(),
            contato.getCargo(),
            contato.getEmail(),
            contato.getTelefone(),
            contato.getEmpresaConveniada().getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    

}
